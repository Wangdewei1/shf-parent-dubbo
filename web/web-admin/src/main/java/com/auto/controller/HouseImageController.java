package com.auto.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.auto.constant.FileConstant;
import com.auto.entity.House;
import com.auto.entity.HouseImage;
import com.auto.result.Result;
import com.auto.service.HouseImageService;
import com.auto.service.HouseService;
import com.auto.util.FileUtil;
import com.auto.util.QiniuUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 图片上传
 */
@Controller
@RequestMapping("/houseImage")
public class HouseImageController {
    @Reference
    private HouseImageService houseImageService;

    @Reference
    private HouseService houseService;

    private static final String ACTION_UPLOAD_SHOW_PAGE = "house/upload";

    /**
     * 到上传房源图片页面
     * @param houseId
     * @param type
     * @param model
     * @return
     */
    @GetMapping("/uploadShow/{houseId}/{type}")
    public String uploadFile(@PathVariable("houseId") Long houseId,
                             @PathVariable("type") Integer type,
                             Model model){
        //往请求域中加houseId
        model.addAttribute("houseId", houseId);
        //
        model.addAttribute("type", type);
        return ACTION_UPLOAD_SHOW_PAGE;
    }

    /**
     * 上传图片
     */
    @PostMapping("/upload/{houseId}/{houseImageType}")
    @ResponseBody
    public Result uploadImageByHouseIdAndHouseImageType(@PathVariable("houseId") Long houseId,
                                                        @PathVariable("houseImageType") Integer type,
                                                        @RequestParam("file") MultipartFile file) throws IOException {
        //上传文件到七牛云
        //1.上传文件有唯一的名字，获取真实文件名 并设置为唯一值
        String uuidName = FileUtil.getUUIDName(file.getOriginalFilename());
        //2.设置上传路径目录级别
        String randomDirPath = "shf/" + FileUtil.getRandomDirPath(FileConstant.DEFAULT_DIR_LEVEL, FileConstant.DEFAULT_DIR_SIZE);
        //3.构建图片在七牛云的路径
        String qinNiuDirPath = randomDirPath + uuidName;
        //4.上传图片七牛云
        QiniuUtils.upload2Qiniu(file.getBytes(), qinNiuDirPath);
        //5.获取访问七牛云的访问路径
        String url = QiniuUtils.getUrl(qinNiuDirPath);

        //将图片信息保存到数据库
        //1.
        HouseImage houseImage = new HouseImage();
        houseImage.setHouseId(houseId);
        houseImage.setImageName(uuidName);
        houseImage.setType(type);
        houseImage.setImageUrl(url);
        houseImageService.insert(houseImage);

        //保存默认路径
        //2.
        House house = houseService.getById(houseId);
        if (house.getDefaultImageUrl() == null || "".equals(house.getDefaultImageUrl()) || !house.getDefaultImageUrl().matches("^http://rx5lypsnu.hn-bkt.clouddn.com/.*$")){
            //如果没有默认图片，就添加
            house.setDefaultImageUrl(url);
            //更新房源信息
            houseService.update(house);
        }
        return Result.ok();
    }
}

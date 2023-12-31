package com.auto.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.auto.constant.FileConstant;
import com.auto.entity.House;
import com.auto.entity.HouseImage;
import com.auto.result.Result;
import com.auto.service.HouseImageService;
import com.auto.service.HouseService;
import com.auto.service.UploadQinNiuFileService;
import com.auto.util.FileUtil;
import com.auto.util.QiniuUtils;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private static final String ACTION_UPLOAD_DELETE_SHOW = "redirect:/house/";
    @Reference
    private HouseImageService houseImageService;

    @Reference
    private HouseService houseService;

    @Reference
    private UploadQinNiuFileService uploadQinNiuFileService;

    private static final String ACTION_UPLOAD_SHOW_PAGE = "house/upload";

    /**
     * 到上传房源图片页面
     * @param houseId
     * @param type
     * @param model
     * @return
     */
    @GetMapping("/uploadShow/{houseId}/{type}")
    @PreAuthorize("hasAnyAuthority('house.editImage')")
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
    @PreAuthorize("hasAnyAuthority('house.editImage')")
    public Result uploadImageByHouseIdAndHouseImageType(@PathVariable("houseId") Long houseId,
                                                        @PathVariable("houseImageType") Integer type,
                                                        @RequestParam("file") MultipartFile file) throws IOException {
        //上传文件到七牛云
        String qinNiuDirPath = uploadQinNiuFileService.getQinNiuPath(file.getBytes(), file.getOriginalFilename());

        //5.获取访问七牛云的访问路径
        String url = QiniuUtils.getUrl(qinNiuDirPath);

        //将图片信息保存到数据库
        //1.
        HouseImage houseImage = new HouseImage();
        houseImage.setHouseId(houseId);
        houseImage.setImageName(uploadQinNiuFileService.getUUIDName(file.getOriginalFilename()));
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


    /**
     * 删除图片
     */
    @GetMapping("/delete/{houseId}/{id}")
    @PreAuthorize("hasAnyAuthority('house.editImage')")
    public String deleteHouseImage(@PathVariable("houseId") Long houseId,
                                   @PathVariable("id") Long id){
        //根据图片的id查询图片的信息
        HouseImage houseImage = houseImageService.getById(id);
        //从七牛云中删除图片
        QiniuUtils.deleteFileFromQiniu(houseImage.getImageName());
        //从数据库中删除图片
        houseImageService.delete(id);
        //判断当前删除的图片，是否是默认的图片
        House house = houseService.getById(houseId);
        if (houseImage.getImageUrl().equals(house.getDefaultImageUrl())){
            //如果是则给当前房源默认图片路径 设置一个字符串
            house.setDefaultImageUrl("null");
            houseService.update(house);
        }
        //重定向show + id
        //注意根据id，重定向时要加/
        return ACTION_UPLOAD_DELETE_SHOW + houseId;
    }

}

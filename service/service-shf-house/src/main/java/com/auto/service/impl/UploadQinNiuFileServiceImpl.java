package com.auto.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.auto.constant.FileConstant;
import com.auto.service.UploadQinNiuFileService;
import com.auto.util.FileUtil;
import com.auto.util.QiniuUtils;

/**
 * 封装上传文件 到七牛云
 */
@Service(interfaceClass = UploadQinNiuFileService.class)
public class UploadQinNiuFileServiceImpl implements UploadQinNiuFileService {
    /**
     * 获取七牛云 储存路径
     * @param bytes
     * @param originalFileName
     * @return
     */
    @Override
    public String getQinNiuPath(byte[] bytes, String originalFileName) {
        //1.上传文件的唯一名称
        String uuidName = FileUtil.getUUIDName(originalFileName);

        //2.设置上传到七牛云中的路径
        String randomDirPath = "shf/" + FileUtil.getRandomDirPath(FileConstant.DEFAULT_DIR_LEVEL, FileConstant.DEFAULT_DIR_SIZE);

        //3.转变成七牛云的储存路径
        String qinNiuDirPath = randomDirPath + uuidName;

        //4.加域名的七牛云储存路径

        //5.将图片上传到七牛云
        QiniuUtils.upload2Qiniu(bytes, qinNiuDirPath);
        return qinNiuDirPath;
    }


    /**
     * 获取uuidName
     */
    @Override
    public String getUUIDName(String originalFileName) {
        return FileUtil.getUUIDName(originalFileName);
    }
}

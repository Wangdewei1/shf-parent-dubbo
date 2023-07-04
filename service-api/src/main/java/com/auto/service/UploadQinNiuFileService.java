package com.auto.service;

/**
 * 封装上传文件 到七牛云
 */
public interface UploadQinNiuFileService {

    String getQinNiuPath(byte[] bytes,String originalFileName);

    String getUUIDName(String originalFileName);

}

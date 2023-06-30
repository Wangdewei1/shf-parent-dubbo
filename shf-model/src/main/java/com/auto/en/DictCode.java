package com.auto.en;

/**
 * 字典的 字典编码
 */

public enum  DictCode {
    ROOT("ROOT"),HOUSETYPE("houseType"),FLOOR("floor"),BUILDSTRUCTURE("buildStructure"),
    DECORATION("decoration"),DIRECTION("direction"),HOUSEUSE("houseUse"),
    PROVINCE("province"),BEIJING("beijing");
    private String code;
    DictCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
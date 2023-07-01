package com.auto.en;

/**
 * 房源发布状态
 */
public enum HouseStatus {
    PUBLISHED(1,"已发布"), UNPUBLISHED(0,"未发布");
    private int code;
    private String message;

    HouseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

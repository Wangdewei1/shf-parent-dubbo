package com.auto.en;

/**
 * 用户登陆状态锁定
 */

public enum UserLoginStatusLocked {

    STATUS_LOCKED(0,"用户已锁定"),
    Status_NOMAL(1,"用户未锁定");
    private Integer code;
    private String message;

    UserLoginStatusLocked(Integer code , String message){
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }
}

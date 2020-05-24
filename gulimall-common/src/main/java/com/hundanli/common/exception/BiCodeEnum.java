package com.hundanli.common.exception;

/**
 * @author li
 * @version 1.0
 * @date 2020-05-14 20:58
 **/
public enum BiCodeEnum {

    /**
     * 1.数据校验
     * 2.未知错误
     */
    VALID_EXCEPTION(10001, "数据校验失败"),

    UNKNOWN_EXCEPTION(10000, "系统未知异常");
    private int code;
    private String msg;

    BiCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

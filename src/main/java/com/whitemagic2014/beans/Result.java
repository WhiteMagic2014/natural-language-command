package com.whitemagic2014.beans;


import com.whitemagic2014.enums.ResultEnum;

import java.io.Serializable;

public final class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer code = ResultEnum.ERROR.getCode();

    /**
     * 错误信息
     */
    private String msg = null;

    /**
     * 返回结果实体
     */
    private T data = null;

    public Result() {
    }

    private Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Result<T> error(String msg) {
        return new Result<T>(ResultEnum.ERROR.getCode(), msg, null);
    }

    public static <T> Result<T> error(ResultEnum resultEnum) {
        return new Result<T>(resultEnum.getCode(), resultEnum.getDesc(), null);
    }

    public static <T> Result<T> error(ResultEnum resultEnum, String msg) {
        return new Result<T>(resultEnum.getCode(), msg, null);
    }

    public static <T> Result<T> error(int code, String msg) {
        return new Result<T>(code, msg, null);
    }

    public static <T> Result<T> error(Result result) {
        return new Result<T>(result.getCode(), result.getMsg(), null);
    }


    public static <T> Result<T> success(int code, T data) {
        return new Result<T>(code, "", data);
    }

    public static <T> Result<T> success(T data) {
        return new Result<T>(ResultEnum.SUCCESS.getCode(), "", data);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return this.code.equals(ResultEnum.SUCCESS.getCode());
    }

    @Override
    public String toString() {
        return "Result [code=" + code + ", msg=" + msg + ", data=" + data + "]";
    }

}

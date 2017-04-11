package com.mkyong.exception;

public class CustomError {

    String errCode;
    String errDesc;

    public CustomError(String errCode, String errDesc) {
        this.errCode = errCode;
        this.errDesc = errDesc;
    }

    @Override
    public String toString() {
        return "CustomError{" +
                "errCode='" + errCode + '\'' +
                ", errDesc='" + errDesc + '\'' +
                '}';
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrDesc() {
        return errDesc;
    }

    public void setErrDesc(String errDesc) {
        this.errDesc = errDesc;
    }
}

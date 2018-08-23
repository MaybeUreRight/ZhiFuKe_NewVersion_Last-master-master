package com.weilay.pos2.bean;

import java.util.ArrayList;

/**
 * @author: Administrator
 * @date: 2018/7/2/002
 * @description: $description$
 */
public class HttpBean {
    //{"code":"1","message":"商户代码不正确或已超出有效期","data":"Illegal merchant code or expired"}
    public int code;
    public String message;
    public ArrayList<OperatorEntity> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<OperatorEntity> getData() {
        return data;
    }

    public void setData(ArrayList<OperatorEntity> data) {
        this.data = data;
    }
}

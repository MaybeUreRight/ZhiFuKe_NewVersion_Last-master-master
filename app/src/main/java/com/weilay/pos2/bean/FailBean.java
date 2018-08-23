package com.weilay.pos2.bean;

/**
 * @author: liubo
 * @date: 2018/5/11/011
 * @description: $description$
 * <p>
 * {"name":"Not Found","message":"未找到设备SN在系统中的注册记录","code":0,"status":404,"type":"yii\\web\\NotFoundHttpException"}
 */
public class FailBean {
    public String name;
    public String message;
    public int code;
    public int status;
    public String type;
}

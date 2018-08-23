package com.weilay.pos2.bean;

/**
 * @author: Administrator
 * @date: 2018/7/6/006
 * @description: $description$
 */
public class PersonalCenterBean {
    public int resId;
    public int nameId;
    public String description;

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public int getNameId() {
        return nameId;
    }

    public void setNameId(int nameId) {
        this.nameId = nameId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PersonalCenterBean(int resId, int nameId, String description) {

        this.resId = resId;
        this.nameId = nameId;
        this.description = description;
    }

    public PersonalCenterBean() {
    }

}

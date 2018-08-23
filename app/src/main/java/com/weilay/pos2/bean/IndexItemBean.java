package com.weilay.pos2.bean;

/**
 * @author: Administrator
 * @date: 2018/7/5/005
 * @description: $description$
 */
public class IndexItemBean {
    public String name;
    public int resId;

    public IndexItemBean() {
    }
    public IndexItemBean(String name, int resId) {
        this.name = name;
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}

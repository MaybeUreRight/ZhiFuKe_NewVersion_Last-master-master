package com.weilay.pos2.bean;

/**
 * @author: Administrator
 * @date: 2018/7/9/009
 * @description: $description$
 */
public class ExchangeBean {
    public int resId;
    public String typeName;
    public String sum;

    public ExchangeBean(int resId, String typeName, String sum) {
        this.resId = resId;
        this.typeName = typeName;
        this.sum = sum;
    }

    public ExchangeBean() {
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }
}

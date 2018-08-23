package com.weilay.pos2.bean;

public class OrderInfoBean {
    public String orderNumber;
    public String date;
    public String amount;//金额

    public OrderInfoBean() {
    }

    public OrderInfoBean(String orderNumber, String date, String amount) {
        this.orderNumber = orderNumber;
        this.date = date;
        this.amount = amount;
    }
}

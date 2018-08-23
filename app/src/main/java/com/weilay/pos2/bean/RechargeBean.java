package com.weilay.pos2.bean;

/**
 * @author: Administrator
 * @date: 2018/7/10/010
 * @description: $description$
 */
public class RechargeBean {
    public String name;
    public int reduceMoney;

    public RechargeBean(String name, int reduceMoney) {
        this.name = name;
        this.reduceMoney = reduceMoney;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReduceMoney() {
        return reduceMoney;
    }

    public void setReduceMoney(int reduceMoney) {
        this.reduceMoney = reduceMoney;
    }
}

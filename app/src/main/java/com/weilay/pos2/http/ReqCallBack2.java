package com.weilay.pos2.http;


import com.weilay.pos2.bean.FailBean;

public interface ReqCallBack2<T>{
    /**
     * 响应成功
     */
    void onReqSuccess(T result);


    /**
     * 响应失败
     */
    void onReqFailed(FailBean failBean);
}

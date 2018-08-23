package com.weilay.pos2.http;



public interface ReqCallBack<T>{
    /**
     * 响应成功
     */
    void onReqSuccess(T result);


    /**
     * 响应失败
     */
    void onReqFailed(T error);
}

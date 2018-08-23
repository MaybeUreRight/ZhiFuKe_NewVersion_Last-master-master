package com.weilay.pos2.http;

public interface ReqProgressCallBack<T> extends ReqCallBack2<T> {
    /**
     * 响应进度更新
     */
    void onProgress(long total, long current);
}

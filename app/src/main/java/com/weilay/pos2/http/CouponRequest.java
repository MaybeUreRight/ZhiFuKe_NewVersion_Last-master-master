package com.weilay.pos2.http;


import com.weilay.pos2.listener.ResponseListener;

import okhttp3.FormBody;

/********
 * @detail 卡券的查询
 * File Name:CouponRequest.java
 * Package:com.weilay.pos.http	
 * Date: 2016年11月1日下午5:21:52
 * Author: rxwu
 * Detail:CouponRequest
 */
public class CouponRequest {

    private static final String URL_GET_FIREND_CODE = "API/getFriendCardQRCode";

    public static void getFQrcode(String id, ResponseListener listener) {
        FormBody.Builder params = BaseParam.getParams();
        params.add("id", id);
        HttpUtils.sendPost(params, URL_GET_FIREND_CODE, listener);
    }
}

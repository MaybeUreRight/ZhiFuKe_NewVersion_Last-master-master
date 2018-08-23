package com.weilay.pos2.http;

import com.weilay.pos2.PayApplication;
import com.weilay.pos2.bean.OperatorEntity;
import com.weilay.pos2.util.DeviceUtil;
import com.weilay.pos2.util.Utils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;

/*******
 * @detail http请求基本参数封装
 * @author rxwu
 * @date 2016/07/08
 *
 */
public class BaseParam {

    public static FormBody.Builder getParams() {

        FormBody.Builder builder = new FormBody.Builder();
        OperatorEntity operatorEntity = Utils.getCurOperator();
        builder.add("sn", DeviceUtil.getimei(PayApplication.application));//// ;
        builder.add("versionCode", "" + DeviceUtil.getversionCode(PayApplication.application));
        builder.add("versionName", "" + DeviceUtil.getversionName(PayApplication.application));
        builder.add("deviceType", "box");
        if (operatorEntity != null) {
            builder.add("mid", operatorEntity.getMid());
            builder.add("operator", operatorEntity.getOperator());
            builder.add("pwd", operatorEntity.getPassword());
        }
        return builder;
    }

    public static Map<String, String> getParams2() {
        Map<String, String> params = new HashMap<>();

        OperatorEntity operatorEntity = Utils.getCurOperator();
        params.put("sn", DeviceUtil.getimei(PayApplication.application));//// ;
        params.put("versionCode", "" + DeviceUtil.getversionCode(PayApplication.application));
        params.put("versionName", "" + DeviceUtil.getversionName(PayApplication.application));
        params.put("deviceType", "box");
        if (operatorEntity != null) {
            params.put("mid", operatorEntity.getMid());
            params.put("operator", operatorEntity.getOperator());
            params.put("pwd", operatorEntity.getPassword());
        }
        return params;
    }


    /*********
     * @
     * @return
     */
    public static FormBody.Builder getBaseParams() {
        FormBody.Builder builder = new okhttp3.FormBody.Builder();
        builder.add("sn", DeviceUtil.getimei(PayApplication.application));//;
        builder.add("versionCode", "" + DeviceUtil.getversionCode(PayApplication.application));
        builder.add("versionName", "" + DeviceUtil.getversionName(PayApplication.application));
        builder.add("deviceType", "box");
        return builder;
    }

}

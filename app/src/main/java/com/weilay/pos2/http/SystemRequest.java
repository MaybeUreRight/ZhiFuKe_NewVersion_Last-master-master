package com.weilay.pos2.http;

import com.weilay.pos2.PayApplication;
import com.weilay.pos2.base.BaseActivity;
import com.weilay.pos2.local.UrlDefine;
import com.weilay.pos2.util.DeviceUtil;

import java.util.HashMap;
import java.util.Map;


public class SystemRequest {
    /*****
     * @detail 发送机器的活跃度
     */
    public static void sendMachineActivitys(long activeTimes) {
        Map<String, String> map = new HashMap<>();
        map.put("totalseconds", "" + (activeTimes / 1000));
        HttpManager.sendPost(map, UrlDefine.URL_UPDATE_MACHINE_ACTIVE, null);

    }


    /*****
     * @date 2016/11/18
     *
     * @author rxwu
     *
     * @detail 为机器注册绑定一个sn码，
     * -此方法在应用版本大于29的时候才启用，少于29的应用版本还是应用原先的sn码地址（作兼容）
     * -先检查本地的是否文件有保存这个sn码，如果没有的话，那么向服务请求生成一个新的sn码地址，然后保存到文件中，再次启动直接读取文件中的sn码
     * -如果请求失败应用应该多次尝试检查应用是否激活
     * -保存到sdcard中的sn码，刷机也不会消失，除非格式化机器，后期再讨论新的解决方案，现在解决sn码唯一的问题
     *
     */
    public static void signSnID(final BaseActivity context) {
        //如果本地没有保存sn_key
//        if (TextUtils.isEmpty(PayApplication.application.mCache.getAsString(PosDefine.SN_KEY))) {
        //如果版本大于29的版本，采取从服务获取sn码的方案
        if (DeviceUtil.getversionCode(PayApplication.application) >= 40) {
            Map<String, String> params = BaseParam.getParams2();
            HttpManager.sendPost(params, UrlDefine.URL_GET_SN, null);
        } else {
            //其他版本则直接获取当前的sn码保存
//                WeiLayApplication.app.mCache.put(PosDefine.SN_KEY, DeviceUtil.getimei());
        }

//        }
    }

}

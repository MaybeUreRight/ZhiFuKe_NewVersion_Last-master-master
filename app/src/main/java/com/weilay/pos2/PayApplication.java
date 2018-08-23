package com.weilay.pos2;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.lidroid.xutils.DbUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.MemoryCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.weilay.pos2.bean.CouponEntity;
import com.weilay.pos2.bean.MessageEntity;
import com.weilay.pos2.bean.PosDefine;
import com.weilay.pos2.db.MessageDBHelper;
import com.weilay.pos2.http.SystemRequest;
import com.weilay.pos2.util.ActivityUtils;
import com.weilay.pos2.util.CmdForAndroid;
import com.weilay.pos2.util.CustomPrinterForGetBlockInfo;
import com.weilay.pos2.util.DeviceUtil;
import com.weilay.pos2.util.LogUtils;
import com.weilay.pos2.util.T;
import com.weilay.pos2.util.Utils;
import com.weilay.pos2.util.WifiUtils;

import java.time.chrono.IsoEra;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

/**
 * @author: Administrator
 * @date: 2018/6/29/029
 * @description: $description$
 */
public class PayApplication extends Application {
    private static SharedPreferences sp_ip;// 保存以太网静态IP
    private static SharedPreferences sp_port;// 保存打印机设置
    private static SharedPreferences sp_login;// 保存登录信息
    private static SharedPreferences sp_wifi;// wifi密码
    // 判断当前的网络是否是可用的
    public static boolean networkunable = false;
    public static boolean ISWIFI = true;
    public static WifiManager wifiManager;

    public static PayApplication application;
    public static boolean Login_Suc = false;// 是否已经登录

    public static boolean isLogin_Suc() {
        return Login_Suc;
    }

    public static void setLogin_Suc(boolean login_Suc) {
        Login_Suc = login_Suc;
    }

    private static Handler handler = new Handler();

    public static void runOnUiThread(Runnable runable) {
        if (runable != null) {
            handler.post(runable);
        }
    }

    public static SharedPreferences getSp_wifi() {
        return sp_wifi;
    }

    public static SharedPreferences getSp_login() {
        return sp_login;
    }

    public static SharedPreferences getSp_port() {
        return sp_port;
    }

    public static SharedPreferences getSp_ip() {
        return sp_ip;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CustomPrinterForGetBlockInfo.start();
        application = this;
        Utils.init(application);
//        OkGo.getInstance().init(this);
        initOkGo();


        sp_ip = this.getSharedPreferences("weilayIp", 0);
        sp_port = this.getSharedPreferences("weilayPort", 0);
        sp_login = this.getSharedPreferences("weilayLogin", 0);
        sp_wifi = this.getSharedPreferences("weilayWifi", 0);


        ActivityUtils.init(application);
    }

    private final String DB_NAME = "weilai";
    public static DbUtils db;

    /*****
     * @Detail 安装db
     */
    private void installDB() {
        db = DbUtils.create(this, DB_NAME);
        try {
            db.createTableIfNotExist(MessageEntity.class);
            db.createTableIfNotExist(CouponEntity.class);
            // 清除超过7天的消息
            MessageDBHelper.clearMessage();

        } catch (Exception e) {
            LogUtils.e(e.getLocalizedMessage());
        }
    }


    private void initOkGo() {
        //1.构建
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //2.配置LOG
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);

        //3.配置超时时间
        //全局的读取超时时间
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);

        //4. 配置Cookie，以下几种任选其一就行
        //使用sp保持cookie，如果cookie不过期，则一直有效
//        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));
        //使用数据库保持cookie，如果cookie不过期，则一直有效
//        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));
        //使用内存保持cookie，app退出后，cookie消失
        builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));

        //6.Https配置，以下几种方案根据需要自己设置
        //方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        //方法二：自定义信任规则，校验服务端证书
//        HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
        //方法三：使用预埋证书，校验服务端证书（自签名证书）
//        HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(getAssets().open("srca.cer"));
        //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
//        HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
//        builder.hostnameVerifier(new SafeHostnameVerifier());
        OkGo.getInstance().init(this);


        //7.配置OkGo
        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        //        HttpHeaders headers = new HttpHeaders();
        //        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
        //        headers.put("commonHeaderKey2", "commonHeaderValue2");
        //        HttpParams params = new HttpParams();
        //        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
        //        params.put("commonParamsKey2", "这里支持中文参数");
        //-------------------------------------------------------------------------------------//

        OkGo.getInstance().init(this)                       //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3);                         //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
//                .addCommonHeaders(headers)                      //全局公共头
//                .addCommonParams(params);                       //全局公共参数
    }

    public static WifiManager getWifiManager() {
        if (wifiManager == null) {
            wifiManager = (WifiManager) application.getSystemService(Context.WIFI_SERVICE);
        }
        return wifiManager;
    }

    private BroadcastReceiver networkstateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                    NetworkInfo info = (NetworkInfo) intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    SharedPreferences.Editor editor = getSp_ip().edit();
                    if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                        if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
                            Log.i("gg", "wifi网络连接断开");
                        } else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
                            WifiInfo wifiInfo = getWifiManager().getConnectionInfo();
                            Log.i("gg", "连接到网络 " + wifiInfo.getSSID());
                        }
                    }

                    if (info != null) {
                        switch (info.getType()) {
                            case ConnectivityManager.TYPE_ETHERNET:
                                ISWIFI = false;
                                editor.putBoolean(PosDefine.ISWIFI, ISWIFI);

                                getWifiManager().setWifiEnabled(false);
                                Log.i("gg", "networkinfo is ethernet,close wifi");
                                T.showShort("本地网络已连接!");
                                break;
                            default:
                                ISWIFI = true;
                                try {
                                    WifiUtils.wifiInit(context).wifiReconnect();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                editor.putBoolean(PosDefine.ISWIFI, ISWIFI);
                                CmdForAndroid.shella("su", "netcfg eth0 down");
                                Log.i("gg", "networkinfo is wifi,close ethernet");
                                break;
                        }
                    }
                    editor.commit();
                    networkunable = (info != null && info.isConnected()) ? true : false;
                    if (networkunable) {
                        LogUtils.i("网络恢复连接成功");
                        if (TextUtils.isEmpty(DeviceUtil.getimei())) {

                            SystemRequest.signSnID(null);// 如果sn码为空的，那么签名生成一个
                        }
                        Utils.getServerTime();// 同步服务器时间
                    } else {
                        Log.i("gg", "network is not connected.the network type is:" + info.getTypeName());
                    }
                    break;

                default:
                    break;
            }

        }
    };

}

package com.weilay.pos2.http;


import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.weilay.pos2.PayApplication;
import com.weilay.pos2.bean.AppBean;
import com.weilay.pos2.bean.FailBean;
import com.weilay.pos2.listener.ResponseListener;
import com.weilay.pos2.local.Config;
import com.weilay.pos2.local.UrlDefine;
import com.weilay.pos2.util.GsonUtils;
import com.weilay.pos2.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;

public class HttpManager {

    private static Client client;

    public static Client getClient() {
        if (client == null) {
            client = new Client(PayApplication.application);
        }
        return client;
    }

    public static void get(String url, ReqCallBack reqCallBack) {
        String finalUrl = Config.ROOT_URL + url;
        LogUtils.i("finalUrl = " + finalUrl);
        OkGo.<String>get(finalUrl)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        String body = response.body().toString();
                        LogUtils.i("body = \r\n" + body);
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<String> response) {
                        super.onError(response);

                        try {
                            String body = response.getRawResponse().body().string();
                            LogUtils.i("string = " + body);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    public static void downloadImage2(String url, ReqCallBack reqCallBack) {
        String finalUrl = Config.ROOT_URL + url;
        LogUtils.i("finalUrl = " + finalUrl);
        OkGo.<File>get(finalUrl)
                .tag("")
//                .params("","")
                .execute(new FileCallback() {
                             @Override
                             public void onSuccess(com.lzy.okgo.model.Response<File> response) {
                                 String body = response.body().toString();
                                 LogUtils.i("body = \r\n" + body);

                             }

                             @Override
                             public void onError(com.lzy.okgo.model.Response<File> response) {
                                 super.onError(response);


                                 try {
                                     String body = response.getRawResponse().body().string();
                                     LogUtils.i("string = " + body);

                                 } catch (Exception e) {
                                     e.printStackTrace();
                                 }
                             }
                         }

                );
    }


    /********
     * @Detail 不需要传入activity对象的网络请求方法，可以确保回调在主线程上运行
     * @param params
     * @param url
     * @param reqCallBack
     */
    public static void sendPost(Map<String, String> params, final String url, final ReqCallBack<String> reqCallBack) {
        String finalUrl = Config.ROOT_URL + url;
        LogUtils.i("finalUrl = " + finalUrl);
        OkGo.<String>post(finalUrl)
                .tag(finalUrl)
                .params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = response.body();
                        LogUtils.i("HttpManager(sendPost) --> onSuccess\nbody = \r\n" + body);
                        reqCallBack.onReqSuccess(body);
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<String> response) {
                        super.onError(response);
                        try {
                            Throwable exception = response.getException();
                            String message = exception.getMessage();
                            LogUtils.i("message = " + message);
                            reqCallBack.onReqFailed(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    public static void sendPost2(FormBody.Builder params, final String url, final ResponseListener listener) {

        Call call = getClient().toserver(params, url);

        if (call != null) {
            call.enqueue(new Callback() {

                @Override
                public void onResponse(Call arg0, okhttp3.Response res) throws IOException {
                    String res_info = res.body().string();
                    Log.i("gg", "res_info:" + res_info);
                    try {
                        final JSONObject jo = new JSONObject(res_info);
                        if (jo.getString("code").equals("0")) {
                            if (listener != null) {
                                PayApplication.runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        listener.onSuccess(jo);
                                    }
                                });
                            }
                        } else {
                            if (listener != null) {
                                PayApplication.runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        listener.onFailed(jo.optInt("code"),
                                                jo.optString("message"));
                                    }
                                });

                            }
                        }
                    } catch (

                            JSONException e) {
                        if (listener != null) {
                            PayApplication.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    listener.onFailed(NetCodeEnum.NOJSON,
                                            "返回不是标准的json格式");
                                }
                            });

                        }
                    }
                }

                @Override
                public void onFailure(Call arg0, final IOException ioe) {
                    Log.i("gg", "onFailure:" + ioe.toString() + "---url:" + url);

                    if (listener != null) {
                        PayApplication.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if (ioe instanceof java.net.SocketTimeoutException
                                        || ioe instanceof java.io.InterruptedIOException) {
                                    listener.onFailed(NetCodeEnum.TIMEOUT,
                                            "网络请求超时");
                                } else if (ioe instanceof UnknownHostException) {
                                    listener.onFailed(NetCodeEnum.UNKNOWHOST,
                                            "服务器连接超时");
                                } else {
                                    listener.onFailed(NetCodeEnum.FAILED,
                                            "请求失败");
                                }
                            }
                        });

                    }

                }
            });

        } else {
            if (listener != null) {
                PayApplication.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        listener.networkError();
                    }

                });
            }
        }
    }


    private static void sendError(final NetCodeEnum what, final ReqCallBack listener) {
//        switch (what) {
//            case NOJSON:
//                listener.onReqFailed(what, "服务器返回数据格式有误");
//                break;
//            case UNKNOWHOST:
//                listener.onReqFailed(what, "无法连接服务器!");
//                break;
//            case TIMEOUT:
//                listener.timeOut();
//                break;
//            case NETWORK_UNABLE:
//                listener.networkError();
//                break;
//            case FAILED:
//                listener.onReqFailed(what, "请求失败");
//                break;
//            default:
//                listener.onReqFailed(NetCodeEnum.OTHERCODE, "请求失败");
//                break;
//        }
//    }
    }


    public static void app(String sn, final ReqCallBack2<AppBean> reqCallBack) {
        OkGo.<String>post(UrlDefine.APP)
                .tag(UrlDefine.TAG_APP)
                .params("sn", sn)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                        try {
                            String body = response.getRawResponse().body().string();
                            FailBean failBean = GsonUtils.convertString2Object(body, FailBean.class);
                            if (reqCallBack != null) {
                                reqCallBack.onReqFailed(failBean);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = response.body();
                        LogUtils.i("app = \r\n" + body);
                        AppBean appBean = GsonUtils.convertString2Object(body, AppBean.class);
                        reqCallBack.onReqSuccess(appBean);
                    }
                });

    }

}

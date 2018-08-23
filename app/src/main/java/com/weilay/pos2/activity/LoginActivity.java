package com.weilay.pos2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weilay.pos2.R;
import com.weilay.pos2.base.BaseActivity;
import com.weilay.pos2.bean.HttpBean;
import com.weilay.pos2.bean.OperatorEntity;
import com.weilay.pos2.http.BaseParam;
import com.weilay.pos2.http.HttpManager;
import com.weilay.pos2.http.ReqCallBack;
import com.weilay.pos2.http.SystemRequest;
import com.weilay.pos2.local.UrlDefine;
import com.weilay.pos2.util.DeviceUtil;
import com.weilay.pos2.util.GsonUtils;
import com.weilay.pos2.util.LogUtils;
import com.weilay.pos2.util.NetworkUtils;
import com.weilay.pos2.util.PasswordEncode;
import com.weilay.pos2.util.Utils;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

/**
 * 登录 界面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private AppCompatEditText merchantCode;
    private AppCompatEditText userName;
    private AppCompatEditText userPassword;
    private AppCompatTextView snCode;

    private static class MyHandler extends Handler {
        private final WeakReference<LoginActivity> mActivity;

        MyHandler(LoginActivity activity) {
            mActivity = new WeakReference<LoginActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginActivity activity = mActivity.get();
            if (activity != null) {
            }
        }
    }

    private MyHandler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    public void initView() {
        mHandler = new MyHandler(this);
        merchantCode = (AppCompatEditText) findViewById(R.id.merchant_code);
        userName = (AppCompatEditText) findViewById(R.id.user_name);
        userPassword = (AppCompatEditText) findViewById(R.id.user_password);
        AppCompatButton login = (AppCompatButton) findViewById(R.id.login);
        snCode = (AppCompatTextView) findViewById(R.id.sn_code);
        AppCompatTextView versionName = (AppCompatTextView) findViewById(R.id.version_name);

        login.setOnClickListener(this);

        if (!hasSign()) {
            SystemRequest.signSnID(LoginActivity.this);
            //如果sn码为空的，那么签名生成一个
            snCode.postDelayed(new Runnable() {
                @Override
                public void run() {
                    snCode.setText(DeviceUtil.getimei(LoginActivity.this));
                }
            }, 1000);
        } else {
            snCode.setText("SN:" + DeviceUtil.getimei(this));
        }

        versionName.setText("软件版本:" + DeviceUtil.getversionName(this));

//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                login();
//            }
//        }, 1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                login();
//                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                break;
        }
    }

    /**
     * 登录
     */
    public void login() {
        if (!NetworkUtils.isNetworkable(this)) {
            Toast.makeText(this, getString(R.string.check_network_connection), Toast.LENGTH_SHORT).show();
            return;
        }
        //校验商户代码
        String merchantCode = this.merchantCode.getText().toString().trim();
        if (TextUtils.isEmpty(merchantCode)) {
            Toast.makeText(this, getString(R.string.merchant_code_not_null), Toast.LENGTH_SHORT).show();
            return;
        }
        //校验操作员号
        String userName = this.userName.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this, getString(R.string.user_name_not_null), Toast.LENGTH_SHORT).show();
            return;
        }
        //校验登录密码
        String userPassword = this.userPassword.getText().toString().trim();
        if (TextUtils.isEmpty(userPassword)) {
            Toast.makeText(this, getString(R.string.user_password_not_null), Toast.LENGTH_SHORT).show();
            return;
        }


        if (!hasSign()) {
            SystemRequest.signSnID(this);//如果sn码为空的，那么签名生成一个
        }

        Map<String, String> params = BaseParam.getParams2();

        params.put("operator", merchantCode);
        params.put("mid", userName);
        final String encodePassword = PasswordEncode.parsePassword(userPassword);
        params.put("pwd", encodePassword);
        showLoading(getString(R.string.logining));
        HttpManager.sendPost(params, UrlDefine.URL_LOGIN, new ReqCallBack<String>() {
            @Override
            public void onReqSuccess(String result) {
                stopLoading();
                LogUtils.i("登录返回数据\n " + result);

                //返回数据有如下两种格式
                //{"code":"1","message":"商户代码不正确或已超出有效期","data":"Illegal merchant code or expired"}

                //{"code":"0","message":"身份合法","data":[{"wxpayflag":"S","mid":"90917","agentid":"80000","name":"恒泰天瑞","mobile":"15810425358","privince":"北京市","city":"北京市","area":"石景山区","address":"实兴东街11号","validto":"2028-03-31","shopname":"恒泰天瑞","operator":"666","operatorname":"技术部","lastdailyclosing":"2018-07-03 09:35:34","machineaddress":"","storeaddress":"","storemobile":"","business_name":"","branch_name":"","wechatpay":1,"alipay":1,"memberpay":2,"qrcode_lock_1":"0","token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJKSENoYW4zMTQiLCJpYXQiOjE1MzA1ODE4MTMsImF1ZCI6Im1lcmNoYW50Iiwic3ViIjoicGF5Ym94IiwibWlkIjoiOTA5MTciLCJvcGVyYXRvciI6IjY2NiIsInNuIjoiOWU2ZjU1NTkifQ.G0hIAakHUERXbekPhoCJOiErhu5XOh2cAkyMiIBlGXs"}]}
                Gson gson = new Gson();
                Map<String, Object> map = gson.fromJson(result, new TypeToken<Map<String, Object>>() {
                }.getType());
                String message = (String) map.get("message");
                Object data = map.get("data");

                if (data instanceof String) {
                    //登录出现了问题
                    LogUtils.i("data = " + data);
                    Toast.makeText(LoginActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                } else if (data instanceof List) {
                    //将操作员的信息保存到本地
                    HttpBean httpBean1 = GsonUtils.convertString2Object(result, HttpBean.class);
                    LogUtils.i("size = " + httpBean1.data.size());
                    OperatorEntity operatorEntity = httpBean1.data.get(0);
                    operatorEntity.setPassword(encodePassword);
                    Utils.saveOperator(operatorEntity);

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }

            @Override
            public void onReqFailed(String error) {
                stopLoading();
                Toast.makeText(LoginActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }

        });
    }

    //判断是否已经生成sn码
    private boolean hasSign() {
        if (TextUtils.isEmpty(DeviceUtil.getimei())) {
            return false;
        }
        return true;
    }
}

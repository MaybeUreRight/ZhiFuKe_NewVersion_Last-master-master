package com.weilay.pos2.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weilay.pos2.R;
import com.weilay.pos2.base.BaseActivity;
import com.weilay.pos2.bean.SendTicketInfo;
import com.weilay.pos2.fragment.FriendCouponFragment;
import com.weilay.pos2.fragment.NormalCouponFragment;
import com.weilay.pos2.http.BaseParam;
import com.weilay.pos2.http.Client;
import com.weilay.pos2.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

/**
 * 发券界面
 */
public class SendTicketListActivity2 extends BaseActivity implements OnClickListener {

    private final int TICKET_SUC = 1000;
    private final int TICKET_ERR = 1001;
    private Client client;
    private String couponList_url = "API/getCouponList";
    private TextView normel_tv, firend_tv;
    private ArrayList<SendTicketInfo> list_sti;

    private NormalCouponFragment ncf;
    private FriendCouponFragment fcf;
    private FragmentManager fragmentManager;
    private View normalLine, friendLine;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            stopLoading();
            switch (msg.arg1) {
                case TICKET_SUC:
                    reg();
                    break;
                case TICKET_ERR:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendticket_layout2);
        client = new Client(SendTicketListActivity2.this);
//        setTitle("发券");
        Send_couponlist();
        init();

    }

    private void init() {
        View view = findViewById(R.id.st_titlecontainer);
        view.findViewById(R.id.title_back).setOnClickListener(this);
        TextView title = view.findViewById(R.id.title_title);
        title.setText(R.string.index_item_2);

        fragmentManager = getSupportFragmentManager();
        normel_tv = (TextView) findViewById(R.id.normal_coupon);
        firend_tv = (TextView) findViewById(R.id.friend_coupon);
        normalLine = findViewById(R.id.normal_view);
        friendLine = findViewById(R.id.friend_view);
        normel_tv.setOnClickListener(this);
        firend_tv.setOnClickListener(this);

        setFontWithMicrosoftYaHei(title);
        setFontWithMicrosoftYaHeiLight(normel_tv, firend_tv);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.normal_coupon:
                setTab(0);
                break;
            case R.id.friend_coupon:
                setTab(1);
                break;
            default:
                break;
        }
    }

    @SuppressLint({"ResourceAsColor", "NewApi"})
    private void setTab(int index) {
        if (isDestroyed()) {
            return;
        }
        initialise();// 初始化字体颜色;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);// 隐藏fragment,防止多个fragment显示在界面上.
        switch (index) {
            case 0:
//                normel_tv.setTextColor(Color.GREEN);
//                normel_tv.setTextColor(getColor(R.color.index_pay));
                normalLine.setVisibility(View.VISIBLE);
                if (ncf == null) {
                    ncf = new NormalCouponFragment();
                    Bundle bundle = new Bundle();
                    // bundle.putSerializable(, list_sti);
                    bundle.putParcelableArrayList("normalcoupon", list_sti);
                    ncf.setArguments(bundle);
                    transaction.add(R.id.coupon_fragment, ncf);
                } else {
                    transaction.show(ncf);
                }
                break;

            case 1:
//                firend_tv.setTextColor(Color.GREEN);
                friendLine.setVisibility(View.VISIBLE);
                if (fcf == null) {
                    fcf = new FriendCouponFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("friendcoupon", list_sti);
                    fcf.setArguments(bundle);
                    transaction.add(R.id.coupon_fragment, fcf);
                } else {
                    transaction.show(fcf);
                }
                break;
        }
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    private void reg() {
        setTab(0);
    }

    @SuppressLint("ResourceAsColor")
    private void initialise() {
//        normel_tv.setTextColor(Color.WHITE);
//        firend_tv.setTextColor(Color.WHITE);
        normel_tv.setTextColor(R.color.index_pay);
        firend_tv.setTextColor(R.color.index_pay);
        normalLine.setVisibility(View.GONE);
        friendLine.setVisibility(View.GONE);
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (ncf != null) {
            transaction.hide(ncf);
        }
        if (fcf != null) {
            transaction.hide(fcf);
        }
    }

    private void Send_couponlist() {
        showLoading("请稍候...");
        FormBody.Builder builder = BaseParam.getParams();

        Call call = client.toserver(builder, couponList_url);
        if (call != null) {
            call.enqueue(new Callback() {

                @Override
                public void onFailure(Call arg0, IOException arg1) {
                    sendMessage(TICKET_ERR, "");
                }

                @Override
                public void onResponse(Call arg0, Response res)
                        throws IOException {
                    String res_info = res.body().string();
                    LogUtils.i("gg", "res_info:" + res_info);
                    JSONObject jo;
                    try {
                        jo = new JSONObject(res_info);
                        if ("0".equals(jo.getString("code"))) {
                            list_sti = new Gson().fromJson(jo.optString("data"),
                                    new TypeToken<List<SendTicketInfo>>() {
                                    }.getType());
                            sendMessage(TICKET_SUC, "");
                        } else {
                            sendMessage(TICKET_ERR, "");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        sendMessage(TICKET_ERR, "");
                    }
                }
            });
        } else {
            sendMessage(0, "");
            Toast.makeText(SendTicketListActivity2.this, "网络异常!", Toast.LENGTH_LONG).show();
        }
    }

    private void sendMessage(int resId, Object obj) {
        Message message = new Message();
        message.arg1 = resId;
        message.obj = obj;
        handler.sendMessage(message);
    }

}

package com.weilay.pos2.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.zxing.WriterException;
import com.weilay.pos2.R;
import com.weilay.pos2.app.PayAction;
import com.weilay.pos2.base.BaseActivity;
import com.weilay.pos2.bean.JoinVipEntity;
import com.weilay.pos2.bean.PosDefine;
import com.weilay.pos2.http.BaseParam;
import com.weilay.pos2.listener.DialogConfirmListener;
import com.weilay.pos2.listener.JoinVipListener;
import com.weilay.pos2.listener.LoadMemberRulesListener;
import com.weilay.pos2.local.Config;
import com.weilay.pos2.local.UrlDefine;
import com.weilay.pos2.util.DeviceUtil;
import com.weilay.pos2.util.EncodingHandler;
import com.weilay.pos2.util.GsonUtils;
import com.weilay.pos2.util.LogUtils;
import com.weilay.pos2.util.T;
import com.weilay.pos2.util.Utils;
import com.weilay.pos2.view.DialogConfirm;

import java.util.List;

import okhttp3.FormBody;

/**
 * 会员界面
 */
public class JoinVipActivity extends BaseActivity {

//    private String characterSet;
    private CardView prl_1_container;

//    private final int VIP_SUC = 9000;
//    private final int VIP_ERR = 9001;
    private ImageView vip_card_qr;

//    private Client client;
//    private String url = "API/updateMemberBonus";
//    private String vip_url = "API/putInQRCode";
//    private SharedPreferences sp_login;
    private TextView vipRecharge_tv, card_name, card_info, card_time;
    private ImageView card_logo;
    // private JoinVipInfo jvi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joinvip_layout);
//        client = new Client(JoinVipActivity.this);
//        sp_login = PayApplication.getSp_login();
        initView();
        initDatas();
        getVipcardInfo();
    }

    private void initDatas() {
        Utils.getMemberUpgradeRules(new LoadMemberRulesListener() {

            @Override
            public void loadSuccess(List<String> rules) {
                StringBuffer content = new StringBuffer();
                for (String rule : rules) {
                    content.append("☞  " + rule + "\n");
                }
                //这里缺少提示
//                vipHintTv.setText(content.toString());
            }

            @Override
            public void loadFailed(String msg) {
                T.showCenter("获取会员升级规则失败");
            }
        });
    }

    private void initView() {

        View view = findViewById(R.id.vip_titlecontainer);
        ((TextView) view.findViewById(R.id.title_title)).setText("会员卡");
        view.findViewById(R.id.title_back).setOnClickListener(this);

        prl_1_container = findViewById(R.id.prl_1_container);
        vip_card_qr = (ImageView) findViewById(R.id.vip_card_qr);
        vipRecharge_tv = (TextView) findViewById(R.id.vip_recharge);
        card_name = (TextView) findViewById(R.id.card_name);
        card_info = (TextView) findViewById(R.id.vip_card_info);
        card_time = (TextView) findViewById(R.id.vip_card_time);
        card_logo = (ImageView) findViewById(R.id.vip_card_logo);
        card_name.setText(Config.COMPANY_NAME);
        card_info.setText(Config.COMPANY_NAME + "玫瑰特权蓝卡");
//        card_logo.setImageResource(Config.LOGO_RES);
        card_logo.setImageResource(R.drawable.vip_logo);

        TextView vip_qrtext = findViewById(R.id.vip_qrtext);
        //设置字体
        setFontWithMicrosoftYaHeiLight(vipRecharge_tv, card_name, card_time);
        setFontWithMicrosoftYaHei(card_info, vip_qrtext);

        vipRecharge_tv.setOnClickListener(this);
    }

//    private int membertype = 1;//0 表示金额充值 1表示次数充值

//    private void reg() {
//        vipRecharge_tv.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
////                Intent intent = new Intent(JoinVipActivity.this, (membertype == 0 ? MemberRechargeActivity.class : MemberRechargeActivity.class));
//                Intent intent = new Intent(JoinVipActivity.this, MemberRechargeActivity.class);
//                intent.putExtra(PosDefine.INTENT_PAY_ACTION, PayAction.MEMBER_RECHARGE_PAY);
//                intent.putExtra("payType", "vipRecharge");
//                startActivity(intent);
//            }
//        });
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vip_recharge:
//                Intent intent = new Intent(JoinVipActivity.this, (membertype == 0 ? MemberRechargeActivity.class : MemberRechargeActivity.class));
                Intent intent = new Intent(JoinVipActivity.this, MemberRechargeActivity.class);
                intent.putExtra(PosDefine.INTENT_PAY_ACTION, PayAction.MEMBER_RECHARGE_PAY);
                intent.putExtra("payType", "vipRecharge");
                startActivity(intent);
                break;
            case R.id.title_back:
                onBackPressed();
                break;
            default:
                LogUtils.i("won't do anything");
                break;
        }
    }

    /**
     * 获取会员卡的详细信息
     */
    private void getVipcardInfo() {
        showLoading("正在获取会员信息...");
        FormBody.Builder builder = BaseParam.getParams();
        Utils.JoinVip(builder, new JoinVipListener() {

            @Override
            public void onSuc(JoinVipEntity jvi) {
                //   
                stopLoading();
                if (jvi != null) {
                    LogUtils.i("jvi = \n" + GsonUtils.convertVO2String(jvi));
                    Glide.with(JoinVipActivity.this).asBitmap().load(UrlDefine.BASE_URL + jvi.getBgurl()).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            prl_1_container.setBackground(new BitmapDrawable(resource));
                        }
                    });
                    createQR(jvi.getUrl2qrcode());
                    card_name.setText(jvi.getName());
                    card_info.setText(jvi.getCardinfo());
                    Glide.with(JoinVipActivity.this).asBitmap().load(UrlDefine.BASE_URL + jvi.getLogo()).into(card_logo);

                    final String temp = jvi.getStarttime() + " 至 " + jvi.getEndtime();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            card_time.setText(temp);
                        }
                    });
                }
            }

            @Override
            public void onErr() {
                //   
                stopLoading();
                T.showCenter("获取会员信息失败!");
            }

            @Override
            public void on404(String msg) {
                //   
                stopLoading();
                DialogConfirm.ask(JoinVipActivity.this, "会员提示",
                        msg == null ? "您还没有上架会员卡" : msg, "确定",
                        new DialogConfirmListener() {

                            @Override
                            public void okClick(DialogInterface dialog) {
                                finish();
                            }
                        });
            }
        });
    }

    private void createQR(String QRurl) {
        // 生成二维码图片，第一个参数是二维码的内容，第二个参数是正方形图片的边长，单位是像素
        Bitmap qrcodeBitmap;
        try {
//            qrcodeBitmap = EncodingHandler.createQRCode(QRurl, 700);
            qrcodeBitmap = EncodingHandler.createQRCode(QRurl, DeviceUtil.dp2px(JoinVipActivity.this,getResources().getDimension(R.dimen.dimen_188)));
            vip_card_qr.setImageBitmap(qrcodeBitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}

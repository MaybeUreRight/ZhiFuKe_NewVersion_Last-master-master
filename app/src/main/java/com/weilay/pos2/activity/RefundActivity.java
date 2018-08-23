package com.weilay.pos2.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.Result;
import com.weilay.pos2.R;
import com.weilay.pos2.base.BaseActivity;
import com.weilay.pos2.bean.CheckOutEntity;
import com.weilay.pos2.bean.OperatorEntity;
import com.weilay.pos2.bean.PayType;
import com.weilay.pos2.dialog.NumberDialog;
import com.weilay.pos2.http.RefundRequest;
import com.weilay.pos2.listener.DialogConfirmListener;
import com.weilay.pos2.listener.OnDataListener;
import com.weilay.pos2.util.ConvertUtil;
import com.weilay.pos2.util.InputMoneyFilter;
import com.weilay.pos2.util.T;
import com.weilay.pos2.util.TimeUtils;
import com.weilay.pos2.util.Utils;
import com.weilay.pos2.view.DialogConfirm;
import com.weilay.pos2.zxing.ViewfinderView;

/**
 * 退款界面
 *
 * @author: Administrator
 * @date: 2018/7/6/006
 * @description: $description$
 */
public class RefundActivity extends BaseActivity {
    public static final int CODE = 0x01;
    private TextView businessNoEt, refundMoenyEt;
    private TextView sureBtn, refundBtn;
    private CardView weixinBtn, alipayBtn, refund_cash;// searchBtn;
    private NumberDialog businessNoDialog, refundMoenyDialog;
    private static String INTENT_CHECKOUT_KEY = "intent_checkout_key";
    private CheckOutEntity checkout;
    private RelativeLayout rl_zxing;
    private PayType paytype = PayType.WEIXIN;//当前的退款方式
    //金额限制
    private InputMoneyFilter filter;
    private double max = 0;//最多可退多少钱
    private boolean scale;
    private ViewGroup.LayoutParams params = null;
    private RelativeLayout ll_business;
    private LinearLayout ll_refund;

    public static void actionStart(BaseActivity mContext) {
        Intent intent = new Intent(mContext, RefundActivity.class);
        mContext.startActivity(intent);
    }

    public static void actionCallBack(BaseActivity mContext, CheckOutEntity checkout, int code) {
        Intent intent = new Intent(mContext, RefundActivity.class);
        intent.putExtra(INTENT_CHECKOUT_KEY, checkout);
        mContext.startActivityForResult(intent, code);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentLayout(R.layout.activity_refund);
        setContentView(R.layout.activity_refund);
        checkout = (CheckOutEntity) getIntent().getSerializableExtra(INTENT_CHECKOUT_KEY);
        init();
        initEvent(checkout == null ? false : true);
    }

    //检查用户退款的权限
    private void checkPermission() {
        OperatorEntity operator = Utils.getCurOperator();
        //检查用户退款权限
        if (operator.getWechatpay() != OperatorEntity.PAY_PERMISSION_ALL && operator.getAlipay() == OperatorEntity.PAY_PERMISSION_NONE) {
            DialogConfirm.ask(RefundActivity.this, "退款提示", "抱歉，当前您没有退款的权限", "清楚了", new DialogConfirmListener() {

                @Override
                public void okClick(DialogInterface dialog) {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            });
        }
        if (operator.getWechatpay() != OperatorEntity.PAY_PERMISSION_ALL) {
            weixinBtn.setVisibility(View.GONE);
            paytype = PayType.ALIPAY;
            //支付宝按钮禁用掉
            alipayBtn.setEnabled(false);
        }
        if (operator.getAlipay() == OperatorEntity.PAY_PERMISSION_NONE) {
            alipayBtn.setVisibility(View.GONE);
            paytype = paytype.WEIXIN;
            //微信按钮禁用
            weixinBtn.setEnabled(false);
        }
    }

    public void init() {
        setTitle("退款");
        rl_zxing = (RelativeLayout) findViewById(R.id.rl_zxing);
        params = rl_zxing.getLayoutParams();
        businessNoEt = (TextView) findViewById(R.id.business_et);
        refundMoenyEt = (TextView) findViewById(R.id.refund_money_et);
        sureBtn = (TextView) findViewById(R.id.refund_scan_enter);
        refundBtn = (TextView) findViewById(R.id.refund_scan_enter);
        weixinBtn = (CardView) findViewById(R.id.refund_wechat);
        alipayBtn = (CardView) findViewById(R.id.refund_alipay);
        refund_cash = (CardView) findViewById(R.id.refund_cash);
//        searchBtn = (CardView) findViewById(R.id.btn_search);
        ll_business = (RelativeLayout) findViewById(R.id.ll_business);
        ll_refund = (LinearLayout) findViewById(R.id.ll_refund);
        resetBtnState(checkout == null ? R.id.refund_wechat : (checkout.getPayMethod() == PayType.ALIPAY ? R.id.refund_alipay : R.id.refund_wechat));
        checkPermission();
        if (checkout != null) {
            //不是手动输入的
            max = ConvertUtil.getMoney(checkout.getTotalamountYuan());
            businessNoEt.setText(checkout.getTx_no());
            refundMoenyEt.setText(checkout.getTotalamountYuan());
            rl_zxing.setVisibility(View.INVISIBLE);
        } else {
            rl_zxing.setVisibility(View.VISIBLE);
            startScan();
            getViewfinderView().setScanSize(0.5);
        }
    }


    private void initEvent(boolean init) {
        businessNoEt.setClickable(false);
        weixinBtn.setClickable(false);
        alipayBtn.setClickable(false);
        if (!init) {
            weixinBtn.setOnClickListener(this);
            alipayBtn.setOnClickListener(this);
            businessNoEt.setOnClickListener(this);
        }
        refundBtn.setOnClickListener(this);
//        searchBtn.setOnClickListener(this);
        refundMoenyEt.setOnClickListener(this);
        sureBtn.setOnClickListener(this);
        rl_zxing.setOnClickListener(this);
    }


    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.business_et:
                businessNoDialog = new NumberDialog(RefundActivity.this, R.style.dialog_no_title, businessNoEt, "商户单号", true);
                businessNoDialog.create().show();
                break;
            case R.id.refund_money_et:
                if (checkout == null) {
                    searchRefund();
                } else {
                    InputMoneyFilter filter = new InputMoneyFilter(max);
                    refundMoenyDialog = new NumberDialog(RefundActivity.this, R.style.dialog_no_title, refundMoenyEt, "输入退款金额(最多可退:" + max + "元)", false);
                    refundMoenyDialog.setFilter(filter);
                    refundMoenyDialog.create().show();
                }

                break;
            case R.id.refund_wechat:
                resetBtnState(arg0.getId());
                paytype = PayType.WEIXIN;
                break;
            case R.id.refund_alipay:
                paytype = PayType.ALIPAY;
                resetBtnState(arg0.getId());
                break;
//            case R.id.btn_search:
//                searchRefund();
//                break;
            case R.id.refund_scan_enter:
                T.showCenter("确定退款");
                refund();
                break;
            case R.id.rl_zxing:
                scale();
                break;
        }

    }

    private void scale() {
        if (scale) {
            rl_zxing.setLayoutParams(params);
            scale = false;
            ll_business.setVisibility(View.VISIBLE);
            ll_refund.setVisibility(View.VISIBLE);

        } else {
            rl_zxing.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            scale = true;
            ll_business.setVisibility(View.GONE);
            ll_refund.setVisibility(View.GONE);
        }
    }

    private void resetBtnState(int id) {
        weixinBtn.setEnabled(id == weixinBtn.getId() ? false : true);
        alipayBtn.setEnabled(id == weixinBtn.getId() ? true : false);
    }


    @Override
    public void handleDecode(Result result) {
        super.handleDecode(result);
        if (scale) {
            scale();
        }
        businessNoEt.setText(result.toString());
        if (!TextUtils.isEmpty(result.toString())) {
            searchRefund();
        } else {
            T.showCenter("无法识别扫描内容");
        }

    }

    /******
     * @detail　查找可退款的金额
     * @return void
     * @param
     */
    private void searchRefund() {
        String business_no = businessNoEt.getText().toString();
        if (business_no != null && TextUtils.isEmpty(business_no)) {
            T.showCenter("请先输入商户单号");
            return;
        }

        showLoading("正在获取可退款订单信息");
        RefundRequest.queryRefundDetail(RefundActivity.this, business_no, paytype, new OnDataListener() {

            @Override
            public void onFailed(String msg) {

            }

            @Override
            public void onFailed(int code, String msg) {
                super.onFailed(code, msg);
                stopLoading();
                T.showCenter(msg);
                if (code == 1) {
                    //已经退过款
                    sureBtn.setEnabled(false);
                }
            }

            @Override
            public void onData(Object obj) {
                stopLoading();
                checkout = (CheckOutEntity) obj;

                refundMoenyEt.setText("" + checkout.getTotalamountYuan());
                max = ConvertUtil.getMoney(checkout.getTotalamountYuan());
                if (checkout.isRefund()) {
                    //如果订单已经退过款
                    T.showCenter("订单已经退过款");
                    sureBtn.setEnabled(false);
                    sureBtn.setBackgroundResource(R.drawable.btn_black);
                    sureBtn.setText("已退过款");
                }
            }
        });

    }

    /*******
     * @detail 退款申请
     * @return void
     * @param
     * @detail
     */
    private void refund() {
        if (checkout == null) {
            T.showCenter("找不到退款信息");
            return;
        }
        checkout.setRefundamount(ConvertUtil.yuanToBranch(ConvertUtil.getMoney(refundMoenyEt.getText().toString())));
        if (checkout.getRefundamount() <= 0) {
            T.showCenter("退款金额必须大于零");
            return;
        }
        showLoading("正在退款...");
        RefundRequest.refund(RefundActivity.this, checkout, new OnDataListener() {
            @Override
            public void onFailed(int code, final String msg) {
                super.onFailed(code, msg);
                stopLoading();
                DialogConfirm.ask(RefundActivity.this, "退款提示", msg, "确定",
                        new DialogConfirmListener() {

                            @Override
                            public void okClick(DialogInterface dialog) {
                                T.showCenter(msg);
                                setResult(RESULT_FAILED);
                                finish();
                            }
                        });
            }

            @Override
            public void onFailed(String msg) {

            }

            @Override
            public void onData(final Object obj) {
                stopLoading();
                DialogConfirm.ask(RefundActivity.this, "退款提示", "退款成功", "确定",
                        new DialogConfirmListener() {
                            @Override
                            public void okClick(DialogInterface dialog) {
                                T.showCenter("退款成功");
                                setResult(RESULT_OK);
                                finish();
                                checkout.setTxtime(TimeUtils.getNowTime(TimeUtils.sdf4));
                                checkout.setRefundno(obj.toString());
//                                USBComPort usbport = new USBComPort();
//                                usbport.printOutRP(checkout);
                            }
                        });
            }
        });
    }
}

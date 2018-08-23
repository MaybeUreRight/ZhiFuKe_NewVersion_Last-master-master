package com.weilay.pos2.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.weilay.pos2.R;
import com.weilay.pos2.adapter.VerifyAdapter;
import com.weilay.pos2.base.BaseActivity;
import com.weilay.pos2.bean.CardTypeEnum;
import com.weilay.pos2.bean.CouponEntity;
import com.weilay.pos2.listener.ChargeOffCouponListener;
import com.weilay.pos2.listener.DialogAskListener;
import com.weilay.pos2.listener.DialogConfirmListener;
import com.weilay.pos2.listener.GetCouponListener;
import com.weilay.pos2.listener.OnItemclickListener;
import com.weilay.pos2.util.DialogAsk;
import com.weilay.pos2.util.LogUtils;
import com.weilay.pos2.util.T;
import com.weilay.pos2.util.Utils;
import com.weilay.pos2.view.DialogConfirm;
import com.weilay.pos2.zxing.CameraManager;

import java.util.ArrayList;

/**
 * 核销页面
 *
 * @author: Administrator
 * @date: 2018/7/7/007
 * @description: $description$
 */
public class ChargeOffActivity extends BaseActivity implements OnItemclickListener {
    public static final int VERIFY_KEYBOARD = 0;
    public static final int VERIFY_SCAN = 1;

    private ImageView verify_change;
    private int currentFlag;

    private RecyclerView verify_keyboard;
    private LinearLayout scan_ll;

    private TextView verify_edittext_tip;
    private String code;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chargeoff);
        initView();
    }

    private void initView() {
        //设置标题栏
        View view = findViewById(R.id.verify_title);
        view.findViewById(R.id.title_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.title_title)).setText(getString(R.string.verify));

        verify_change = findViewById(R.id.verify_change);
        verify_change.setOnClickListener(this);
        currentFlag = VERIFY_KEYBOARD;

        verify_edittext_tip = findViewById(R.id.verify_edittext_tip);
        scan_ll = findViewById(R.id.scan_ll);

        initRecyclerView();

        code = "";


        //启动摄像头
        startScan();
        getViewfinderView().setScanSize(0.8);

    }


    private void initRecyclerView() {
        verify_keyboard = findViewById(R.id.verify_keyboard);

        final ArrayList<String> list = new ArrayList<>();
        list.add(getString(R.string.number_1));
        list.add(getString(R.string.number_2));
        list.add(getString(R.string.number_3));
        list.add(getString(R.string.verify_clear));
        list.add(getString(R.string.number_4));
        list.add(getString(R.string.number_5));
        list.add(getString(R.string.number_6));
        list.add(getString(R.string.verify_delete));
        list.add(getString(R.string.number_7));
        list.add(getString(R.string.number_8));
        list.add(getString(R.string.number_9));
        list.add(getString(R.string.confirm));
        list.add(getString(R.string.number_00));
        list.add(getString(R.string.number_0));
        list.add(getString(R.string.number_dot));

        final VerifyAdapter adapter = new VerifyAdapter(this, list);
        verify_keyboard.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        verify_keyboard.setLayoutManager(layoutManager);
    }

    /**
     * 处理扫描结果
     *
     * @param result
     */
    public void handleDecode(Result result) {
        super.handleDecode(result);
        String resultString = result.getText();
        LogUtils.i("gg", "扫描结果:" + resultString);
        if (TextUtils.isEmpty(resultString)) {
            Toast.makeText(ChargeOffActivity.this, "Scan failed!",
                    Toast.LENGTH_SHORT).show();
        } else {
            queryCard(resultString);
        }
    }

    /*****
     * @detail 查询卡券的信息
     */
    public void queryCard(final String code) {
        showLoading("正在查询卡券信息..");
        Utils.getCouponInfo(code, new GetCouponListener() {

            @Override
            public void onFailed(String msg) {
                stopLoading();
                T.showCenter("不允许使用非本门店卡券");
            }

            @Override
            public void onData(CouponEntity coupon) {
                stopLoading();
                StringBuffer stringbuffer = new StringBuffer();
                stringbuffer.append("卡号：" + code + "\n");
                stringbuffer.append("卡券信息:"
                        + (coupon.getInfo() == null ? "" : coupon.getInfo())
                        + "\n");
                stringbuffer.append("使用提示:"
                        + (coupon.getNotice() == null ? "" : coupon.getNotice())
                        + "\n");
                switch (coupon.getType()) {
                    case CardTypeEnum.GIFT:
                    case CardTypeEnum.FRIEND_GIFT:
                    case CardTypeEnum.GROUPON:// 支持团购券
                    case CardTypeEnum.SCENIC_TICKET://新增支付景区券核销
                        DialogAsk.ask(ChargeOffActivity.this, "卡券使用提示",
                                stringbuffer.toString(), "确定使用", "取消",
                                new DialogAskListener() {
                                    @Override
                                    public void okClick(DialogInterface dialog) {
                                        showLoading("卡券核销中..");
                                        send_chargeOff(code);
                                    }

                                    @Override
                                    public void cancelClick(DialogInterface dialog) {
                                        T.showCenter("用户放弃使用卡券");
                                    }
                                });
                        break;
                    default:
                        cardConsumeNoFound("不支持此类卡券,仅支持兑换券核销");
                        break;
                }
            }
        });
    }

    /*****
     * @detail 提示卡券没找到
     */
    public void cardConsumeNoFound(String msg) {
        if (!isFinishing() && !isDestroyed()) {
            DialogConfirm.ask(this, "卡券提示", msg, "确认",
                    new DialogConfirmListener() {

                        @Override
                        public void okClick(DialogInterface dialog) {
                        }
                    });
        }
        verify_edittext_tip.setText("");
    }

    private void send_chargeOff(String codeNo) {
        showLoading("正在核销卡券...");
        Utils.sendChargeOff(codeNo, new ChargeOffCouponListener() {

            @Override
            public void onSuc() {
                DialogConfirm.ask(ChargeOffActivity.this, "核销提示", "核销成功", "确定",
                        new DialogConfirmListener() {

                            @Override
                            public void okClick(DialogInterface dialog) {
                                ChargeOffActivity.this.finish();
                            }
                        });
            }

            @Override
            public void onErr() {
                DialogConfirm.ask(ChargeOffActivity.this, "核销提示", "核销失败", "确定",
                        new DialogConfirmListener() {

                            @Override
                            public void okClick(DialogInterface dialog) {
                                ChargeOffActivity.this.finish();
                            }
                        });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.verify_change:
                if (currentFlag == VERIFY_KEYBOARD) {

                    verify_keyboard.setVisibility(View.GONE);
                    scan_ll.setVisibility(View.VISIBLE);

                    currentFlag = VERIFY_SCAN;
                    verify_change.setImageResource(R.drawable.verify_scan);
                } else {

                    verify_keyboard.setVisibility(View.VISIBLE);
                    scan_ll.setVisibility(View.GONE);

                    currentFlag = VERIFY_KEYBOARD;
                    verify_change.setImageResource(R.drawable.verify_keyboard);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onIndexItemClick(int position) {
        switch (position) {
            case 0://1
                code = code + 1;
                verify_edittext_tip.setText(code);
                break;
            case 1://2
                code = code + 2;
                verify_edittext_tip.setText(code);
                break;
            case 2://3
                code = code + 3;
                verify_edittext_tip.setText(code);
                break;
            case 3://清空
                code = "";
                verify_edittext_tip.setText(code);
                break;
            case 4://4
                code = code + 4;
                verify_edittext_tip.setText(code);
                break;
            case 5://5
                code = code + 5;
                verify_edittext_tip.setText(code);
                break;
            case 6://6
                code = code + 6;
                verify_edittext_tip.setText(code);
                break;
            case 7://删除
                if (TextUtils.isEmpty(code)) {
                    verify_edittext_tip.setText(getString(R.string.verify_input));
                } else {
                    code = code.substring(0, code.length() - 1);
                    if (TextUtils.isEmpty(code)) {
                        verify_edittext_tip.setText(getString(R.string.verify_input));
                    } else {
                        verify_edittext_tip.setText(code);
                    }
                }
                break;
            case 8://7
                code = code + 7;
                verify_edittext_tip.setText(code);
                break;
            case 9://8
                code = code + 8;
                verify_edittext_tip.setText(code);
                break;
            case 10://9
                code = code + 9;
                verify_edittext_tip.setText(code);
                break;
            case 11://确定
                if (!TextUtils.isEmpty(code)) {
                    queryCard(code);
                } else {
                    Toast.makeText(this, "兑换码不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case 12://00
                code = code + "00";
                verify_edittext_tip.setText(code);
                break;
            case 13://0
                code = code + "0";
                verify_edittext_tip.setText(code);
                break;
            case 14://.
                code = code + ".";
                verify_edittext_tip.setText(code);
                break;

            default:
                break;
        }
    }
}

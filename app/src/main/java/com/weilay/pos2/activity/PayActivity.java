package com.weilay.pos2.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.weilay.pos2.R;
import com.weilay.pos2.adapter.VerifyAdapter;
import com.weilay.pos2.app.PayAction;
import com.weilay.pos2.base.BaseActivity;
import com.weilay.pos2.bean.PayTypeEntity;
import com.weilay.pos2.bean.PosDefine;
import com.weilay.pos2.listener.OnItemclickListener;
import com.weilay.pos2.util.BaseKeyBoard;
import com.weilay.pos2.util.BaseKeyBoard.OnKeyListener;
import com.weilay.pos2.util.ConvertUtil;
import com.weilay.pos2.util.DeviceUtil;
import com.weilay.pos2.util.InputMoneyFilter;
import com.weilay.pos2.util.T;

import java.util.ArrayList;


/*********
 * @Detail 支付输入页面
 * @author Administrator
 *
 */
public class PayActivity extends BaseActivity implements OnItemclickListener {//implements OnKeyListener

    private TextView jine_tv, vipcouponinfo_tv, payinfo_santype_tv;
    private String payType = "";
    private int payAction;// 为xx支付
    private String vipNo;
    private Editable edit;
    //    private BaseKeyBoard baseKeyboard;
    private PayTypeEntity paytype;

    private RecyclerView ipl_keyboard;

    public final int NO_MORE = 102;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NO_MORE:
                    T.showCenter("超过输入的长度");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.input_pay_layout);
        setContentView(R.layout.input_pay_layout_new);
        init();
        reg();
    }

    private void init() {
        Intent payIntent = getIntent();
        payType = payIntent.getStringExtra("payType");
        // 获取支付的意图 update by rxwu at 2016/07/11
        payAction = payIntent.getIntExtra(PosDefine.INTENT_PAY_ACTION,
                PayAction.DEFAULT_PAY);

//        View keyboardView = findViewById(R.id.keyboard_view);
//        baseKeyboard = new BaseKeyBoard(keyboardView);
//		baseKeyboard.setOnkeyListener(this);

        View titleContaienr = findViewById(R.id.rpl_titlecontainer);
        titleContaienr.findViewById(R.id.title_back).setOnClickListener(this);
        TextView title = titleContaienr.findViewById(R.id.title_title);
        title.setText(R.string.pay);

        jine_tv = (TextView) findViewById(R.id.pay_amount);
        vipcouponinfo_tv = (TextView) findViewById(R.id.vipcouponinfo);
        edit = jine_tv.getEditableText();
        edit.setFilters(new InputFilter[]{new InputMoneyFilter(10000000)});

        ipl_keyboard = findViewById(R.id.ipl_keyboard);

        initRecyclerView();
    }

    private void initRecyclerView() {
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
        ipl_keyboard.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        ipl_keyboard.setLayoutManager(layoutManager);
    }

    boolean isScan = false;

    private void reg() {
        if (payType != null && payType.equals("vipRecharge")) {
            payinfo_santype_tv.setText("会员编号");
            vipcouponinfo_tv.setText("请出示会员二维码");
        }
    }

    boolean scanable = true;

    /******
     * @detail 支付处理
     * @param InputAmount
     */
    public void pay(String InputAmount) {
        switch (payAction) {
            case PayAction.MEMBER_RECHARGE_PAY:// 会员充值先扫描会员卡
                if (TextUtils.isEmpty(vipNo)) {
                    T.showCenter("请先让顾客扫描会员卡");
                    break;
                }
            default:
                double f;
                if (InputAmount.equals("")) {
                    InputAmount = "0";
                }
                try {
                    // update 将单位改成分
                    f = ConvertUtil.getMoney(InputAmount);
                    if (f >= 0.01f) {
                        if (f > 1000000f) {
                            Toast.makeText(PayActivity.this, "支付金额超过限额", Toast.LENGTH_SHORT).show();
                        } else {
                            // 将支付信息封装好传递到支付页面
                            paytype = new PayTypeEntity();
                            paytype.setTx_no(DeviceUtil.getOutTradeNo());
                            paytype.setAmount(f);
                            Intent i = new Intent(PayActivity.this, PaySelectActivity2.class);
                            i.putExtra(PosDefine.INTENTE_PAY_INFO, paytype);
                            startActivity(i);
                            finish();
                        }
                    } else {
                        T.showLong("输入金额必须大于0.01!而且不能为空!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            default:
                break;
        }

    }

    @Override
    public void onIndexItemClick(int position) {
        if (edit == null) {
            return;
        }
        switch (position) {
            case 0://1
                if (edit.length() < 8) {
                    edit.append("1");
                } else {
                    handler.sendEmptyMessage(NO_MORE);
                }
                break;
            case 1://2
                if (edit.length() < 8) {
                    edit.append("2");
                } else {
                    handler.sendEmptyMessage(NO_MORE);
                }
                break;
            case 2://3
                if (edit.length() < 8) {
                    edit.append("3");
                } else {
                    handler.sendEmptyMessage(NO_MORE);
                }
                break;
            case 4://4
                if (edit.length() < 8) {
                    edit.append("4");
                } else {
                    handler.sendEmptyMessage(NO_MORE);
                }
                break;
            case 5://5
                if (edit.length() < 8) {
                    edit.append("5");
                } else {
                    handler.sendEmptyMessage(NO_MORE);
                }
                break;
            case 6://6
                if (edit.length() < 8) {
                    edit.append("6");
                } else {
                    handler.sendEmptyMessage(NO_MORE);
                }
                break;
            case 8://7
                if (edit.length() < 8) {
                    edit.append("7");
                } else {
                    handler.sendEmptyMessage(NO_MORE);
                }
                break;
            case 9://8
                if (edit.length() < 8) {
                    edit.append("8");
                } else {

                    handler.sendEmptyMessage(NO_MORE);
                }
                break;
            case 10://9
                if (edit.length() < 8) {
                    edit.append("9");
                } else {
                    handler.sendEmptyMessage(NO_MORE);
                }
                break;
            case 13://0
                if (edit.length() < 8) {
                    edit.append("0");
                } else {

                    handler.sendEmptyMessage(NO_MORE);
                }
                break;
            case 3://清空
                edit.clear();
                break;
            case 7://删除
                if (edit.length() >= 1) {
                    edit.delete(edit.length() - 1, edit.length());
                }
                break;
            case 11://确定
                String str = jine_tv.getText().toString();
                pay(str);
                break;
            case 12://00
                if (edit.length() < 8) {
                    if (edit.length() == 7) {
                        edit.append("0");
                    } else {
                        edit.append("00");
                    }
                }
                break;
            case 14://.
                if (edit.length() < 1 || edit.toString().contains(".")) {
                    return;
                }
                edit.append(".");
                break;

            default:
                break;
        }
    }
}

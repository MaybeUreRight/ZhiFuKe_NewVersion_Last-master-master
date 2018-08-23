package com.weilay.pos2.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.weilay.pos2.R;
import com.weilay.pos2.adapter.ExchangeAdapter;
import com.weilay.pos2.base.BaseActivity;
import com.weilay.pos2.bean.ExchangeBean;
import com.weilay.pos2.bean.ShiftRecord;
import com.weilay.pos2.http.BaseParam;
import com.weilay.pos2.http.HttpUtils;
import com.weilay.pos2.listener.DialogAskListener;
import com.weilay.pos2.listener.DialogConfirmListener;
import com.weilay.pos2.listener.OnItemclickListener;
import com.weilay.pos2.listener.ResponseListener;
import com.weilay.pos2.local.UrlDefine;
import com.weilay.pos2.util.ConvertUtil;
import com.weilay.pos2.util.DialogAsk;
import com.weilay.pos2.util.T;
import com.weilay.pos2.util.Utils;
import com.weilay.pos2.view.DialogConfirm;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.FormBody;

/**
 * 交班界面
 */
public class ExchangeActivity extends BaseActivity implements OnItemclickListener {
    /**
     * Dialog需要修改
     */

    private TextView exchange_sum;
    private ArrayList<ExchangeBean> list;
    private ShiftRecord sr;
    private ExchangeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);

        initView();
        initRecyclerView();
    }

    private void initView() {
        View view = findViewById(R.id.exchange_titlecontainer);
        view.findViewById(R.id.title_back).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.title_title)).setText(getString(R.string.exchange));

        exchange_sum = findViewById(R.id.exchange_sum);

        Button exchange_now = findViewById(R.id.exchange_now);
        exchange_now.setOnClickListener(this);
        Button exchange_later = findViewById(R.id.exchange_later);
        exchange_later.setOnClickListener(this);

        setFontWithMicrosoftYaHeiLight(exchange_later, exchange_now);
    }

    private void initRecyclerView() {
        RecyclerView exchangeRecyclerview = findViewById(R.id.exchange_recyclerview);

        list = new ArrayList<>();
        ExchangeBean bean1 = new ExchangeBean(R.drawable.exchange_1, getString(R.string.exchange_1), getString(R.string.number_sample_1));
        ExchangeBean bean2 = new ExchangeBean(R.drawable.exchange_2, getString(R.string.exchange_2), getString(R.string.number_sample_1));
        ExchangeBean bean3 = new ExchangeBean(R.drawable.exchange_3, getString(R.string.exchange_3), getString(R.string.number_sample_1));
        ExchangeBean bean4 = new ExchangeBean(R.drawable.exchange_4, getString(R.string.exchange_4), getString(R.string.number_sample_1));
        ExchangeBean bean5 = new ExchangeBean(R.drawable.exchange_5, getString(R.string.exchange_5), getString(R.string.number_sample_1));
        ExchangeBean bean6 = new ExchangeBean(R.drawable.exchange_6, getString(R.string.exchange_6), getString(R.string.number_sample_1));
        list.add(bean1);
        list.add(bean2);
        list.add(bean3);
        list.add(bean4);
        list.add(bean5);
        list.add(bean6);

        adapter = new ExchangeAdapter(this, list);
        exchangeRecyclerview.setAdapter(adapter);
//        exchangeRecyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        exchangeRecyclerview.setLayoutManager(layoutManager);

//        try {
//            Long sum = 0L;
//            for (ExchangeBean bean : list) {
//                Long itemSum = Long.valueOf(bean.sum);
//                sum = sum + itemSum;
//            }
//            exchange_sum.setText(String.valueOf(sum));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        exchange(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.exchange_now:
//                DialogAsk.ask(ExchangeActivity.this, "交班提示", "是否确定交班", "确定", "取消", new DialogAskListener() {
//
//                    @Override
//                    public void okClick(DialogInterface dialog) {
//                        exchange(true);
//                    }
//
//                    @Override
//                    public void cancelClick(DialogInterface dialog) {
//                    }
//                });


                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final AlertDialog dialog = builder.create();
                View view = View.inflate(this, R.layout.dialog_exchange, null);
                view.findViewById(R.id.dialog_update_close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                view.findViewById(R.id.dialog_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                view.findViewById(R.id.dialog_confirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exchange(true);
                    }
                });
//                builder.setView(view);
                dialog.setView(view);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();


                break;
            case R.id.exchange_later:
                onBackPressed();
                finish();
                break;
        }
    }

    @Override
    public void onIndexItemClick(final int position) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ExchangeActivity.this, list.get(position).typeName, Toast.LENGTH_SHORT).show();
            }
        });

    }


    /**********
     * @Detail 交班请求 update by rxwu
     * @param isShift
     *            如果为true，那么是确认交班，否则为获取交班数据
     */
    private void exchange(final boolean isShift) {
        showLoading(isShift ? "正在交班..." : "正在获取交班数据");
        FormBody.Builder builder = BaseParam.getParams();
        builder.add("click", "" + isShift);
        HttpUtils.sendPost(builder, UrlDefine.URL_POS_SHIFT, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject json) {
                stopLoading();
                try {
                    sr = new Gson().fromJson(json.optString("data"), ShiftRecord.class);
//                    weixin_jine.setText(ConvertUtil.parseMoney(sr.getwechat()));
//                    zhifubao_jine.setText(ConvertUtil.parseMoney(sr.getalipay()));
//                    jiaoban_cash.setText(ConvertUtil.parseMoney(sr.getCash()));
//
//                    recharge_jine.setText(ConvertUtil.parseMoney(sr.getRechargeAmt()));
//                    refund_jine.setText(ConvertUtil.parseMoney(sr.getRefundAmt()));
//                    memberpay_jine.setText(ConvertUtil.parseMoney(sr.getMemberPayAmt()));
//
//                    heji_jine.setText(ConvertUtil.parseMoney(sr.getTotalamount()));

                    list.get(0).setSum(ConvertUtil.parseMoney(sr.getwechat()));
                    list.get(1).setSum(ConvertUtil.parseMoney(sr.getalipay()));
                    list.get(2).setSum(ConvertUtil.parseMoney(sr.getCash()));

                    list.get(3).setSum(ConvertUtil.parseMoney(sr.getRechargeAmt()));
                    list.get(4).setSum(ConvertUtil.parseMoney(sr.getRefundAmt()));
                    list.get(5).setSum(ConvertUtil.parseMoney(sr.getMemberPayAmt()));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            exchange_sum.setText(ConvertUtil.parseMoney(sr.getTotalamount()));
                        }
                    });

                    if (isShift) {
//                        DialogConfirm.ask(ExchangeActivity.this, "交班提示", "交班成功", "确定", new DialogConfirmListener() {
//
//                            @Override
//                            public void okClick(DialogInterface dialog) {
////                                if (sr != null) {
////                                    sr.setTitle("交班表");
////									USBComPort usbComPort = new USBComPort();
////									usbComPort.printOutJBRecord(false,sr);
////                                }
//                                shift();
//                            }
//                        });
                        exchangeResultDialog(true);
                    }
                } catch (Exception ex) {
                    if (isShift) {
                        DialogConfirm.ask(ExchangeActivity.this, "交班提示", "交班失败", "确定", new DialogConfirmListener() {

                            @Override
                            public void okClick(DialogInterface dialog) {
                                dialog.dismiss();
                                shift();
                            }

                        });
                    } else {
                        T.showCenter("获取交班数据格式有误");
                    }
                }

            }

            @Override
            public void onFailed(int code, final String msg) {
                stopLoading();
                DialogConfirm.ask(ExchangeActivity.this, "交班提示", "交班失败(" + msg + ")", "确定", new DialogConfirmListener() {

                    @Override
                    public void okClick(DialogInterface dialog) {
                        if (isShift) {
                            shift();
                        } else {
                            T.showCenter(msg);
                        }
                    }
                });

            }

            @Override
            public void networkError() {
                stopLoading();
                DialogAsk.ask(ExchangeActivity.this, "交班提示", "交班失败(网络出错，请重连网络)", "仍然登出账号", "去重连网络", new DialogAskListener() {

                    @Override
                    public void okClick(DialogInterface dialog) {
                        shift();
                    }

                    @Override
                    public void cancelClick(DialogInterface dialog) {
                        Intent intent = new Intent(ExchangeActivity.this, WifiActivity.class);
                        intent.putExtra("isLogin", true);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private void exchangeResultDialog(final boolean success) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ExchangeActivity.this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(ExchangeActivity.this, R.layout.dialog_exchange, null);
        view.findViewById(R.id.dialog_update_close).setVisibility(View.GONE);
        view.findViewById(R.id.dialog_cancel).setVisibility(View.GONE);

        TextView content = view.findViewById(R.id.dialog_update_content);
        content.setText(success ? R.string.exchange_success : R.string.exchange_fail);

        TextView textView = view.findViewById(R.id.dialog_confirm);
        textView.setText(R.string.confirm);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (success) {
                    shift();
                }
                dialog.dismiss();
            }
        });

        dialog.setView(view);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void shift() {
        Utils.isLogin = false;
        Intent intent = new Intent(ExchangeActivity.this, LoginActivity.class);
        ExchangeActivity.this.startActivity(intent);
//		GlobalPush.stopPush();// 交班时候停止接收
        finish();
    }
}

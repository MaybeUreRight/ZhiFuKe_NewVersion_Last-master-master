package com.weilay.pos2.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.weilay.pos2.R;
import com.weilay.pos2.activity.ChargeOffActivity;
import com.weilay.pos2.activity.ExchangeActivity;
import com.weilay.pos2.activity.JoinVipActivity;
import com.weilay.pos2.activity.OrderListActivity;
import com.weilay.pos2.activity.PayActivity;
import com.weilay.pos2.activity.RefundActivity;
import com.weilay.pos2.activity.SendTicketListActivity2;
import com.weilay.pos2.activity.ShiftActivity;
import com.weilay.pos2.activity.TaskActivity;
import com.weilay.pos2.activity.WifiActivity;
import com.weilay.pos2.adapter.IndexBannerAdapter;
import com.weilay.pos2.adapter.IndexItemAdapter;
import com.weilay.pos2.bean.IndexItemBean;
import com.weilay.pos2.bean.ShiftRecord;
import com.weilay.pos2.http.BaseParam;
import com.weilay.pos2.http.HttpUtils;
import com.weilay.pos2.listener.DialogAskListener;
import com.weilay.pos2.listener.DialogConfirmListener;
import com.weilay.pos2.listener.ResponseListener;
import com.weilay.pos2.local.UrlDefine;
import com.weilay.pos2.recyclerview.manager.MyItemDecoration;
import com.weilay.pos2.recyclerview.manager.PagerGridLayoutManager;
import com.weilay.pos2.recyclerview.manager.PagerGridSnapHelper;
import com.weilay.pos2.util.ConvertUtil;
import com.weilay.pos2.util.DialogAsk;
import com.weilay.pos2.util.T;
import com.weilay.pos2.view.DialogConfirm;

import org.json.JSONObject;

import java.util.ArrayList;

import me.itangqi.waveloadingview.WaveLoadingView;
import okhttp3.FormBody;

/**
 * 首页 界面
 * TODO 1，今日收益 今日共计 会员充值  三项数据需要确认
 */
public class MainFragment extends Fragment implements View.OnClickListener, IndexBannerAdapter.IndexBannerItemClickListener, IndexItemAdapter.IndexItemClickListener {
    private Activity activity;
    private TextView today_earn;//今日收益
    private TextView today_statistics;//今日共计
    private TextView member_pay;//会员充值
//    private WaveLoadingView waveLoadingView;

    public MainFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        today_earn = view.findViewById(R.id.today_earn);
        today_statistics = view.findViewById(R.id.today_statistics);
        member_pay = view.findViewById(R.id.member_pay);
//        waveLoadingView = view.findViewById(R.id.waveLoadingView);
//
//        waveLoadingView.resumeAnimation();

        view.findViewById(R.id.index_pay).setOnClickListener(this);

        initRecyclerView(view);
        initBannerRecyclerView(view);
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        if (waveLoadingView != null) {
//            waveLoadingView.pauseAnimation();
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (waveLoadingView != null) {
//            waveLoadingView.resumeAnimation();
//        }
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (waveLoadingView != null) {
//            waveLoadingView.cancelAnimation();
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.index_pay:
                startActivity(new Intent(activity, PayActivity.class));
                break;

            default:

                break;
        }
    }

    private void initBannerRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.index_recyclerview_banner);

        ArrayList<String> list = new ArrayList<>();
        list.add("https://i04piccdn.sogoucdn.com/4705e6e8012a437d");
        list.add("https://i03piccdn.sogoucdn.com/ab6caa48321d50e5");
        list.add("https://img04.sogoucdn.com/app/a/100520093/5981da53b2dc55a5-05f00efc64e0c055-0eca530c4c8658e02bf61e2091c3ca83.jpg");

        IndexBannerAdapter adapter = new IndexBannerAdapter(activity, this, this, list);

        //1.水平分页布局管理器
        PagerGridLayoutManager layoutManager = new PagerGridLayoutManager(1, 1, PagerGridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new MyItemDecoration(2, 10, true));
        recyclerView.setAdapter(adapter);

        // 2.设置滚动辅助工具
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(recyclerView);
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.index_recyclerview);

        ArrayList<IndexItemBean> list = new ArrayList<>();

        IndexItemBean bean1 = new IndexItemBean(getString(R.string.index_item_0), R.drawable.index_item_vip);
        list.add(bean1);

        IndexItemBean bean2 = new IndexItemBean(getString(R.string.index_item_1), R.drawable.index_item_verification);
        list.add(bean2);

        IndexItemBean bean3 = new IndexItemBean(getString(R.string.index_item_2), R.drawable.index_item_ticket);
        list.add(bean3);

        IndexItemBean bean4 = new IndexItemBean(getString(R.string.index_item_3), R.drawable.index_item_cardandticket);
        list.add(bean4);

        IndexItemBean bean5 = new IndexItemBean(getString(R.string.index_item_4), R.drawable.index_item_exchange);
        list.add(bean5);

        IndexItemBean bean6 = new IndexItemBean(getString(R.string.index_item_5), R.drawable.index_item_refund);
        list.add(bean6);

        IndexItemBean bean8 = new IndexItemBean(getString(R.string.index_item_7), R.drawable.order);
        list.add(bean8);

        IndexItemBean bean7 = new IndexItemBean(getString(R.string.index_item_6), R.drawable.index_item_card);
        list.add(bean7);

        //1.水平分页布局管理器
        PagerGridLayoutManager layoutManager = new PagerGridLayoutManager(2, 4, PagerGridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new MyItemDecoration(2, 0, true));

        IndexItemAdapter goodAdapter = new IndexItemAdapter(activity, this, this, list);
        recyclerView.setAdapter(goodAdapter);

        // 2.设置滚动辅助工具
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void bannerItemClick(int position) {
        T.showCenter("点击了" + position + "幅图");
    }

    @Override
    public void indexItemClick(int position) {
        switch (position) {
            case 0://会员
                startActivity(new Intent(activity, JoinVipActivity.class));
                break;
            case 1://核销
                startActivity(new Intent(activity, ChargeOffActivity.class));
                break;
            case 2://发券
                startActivity(new Intent(activity, SendTicketListActivity2.class));
                break;
            case 3://卡券互投
                startActivity(new Intent(activity, TaskActivity.class));
                break;
            case 4://交班
                startActivity(new Intent(activity, ExchangeActivity.class));
//                startActivity(new Intent(activity, ShiftActivity.class));
                break;
            case 5://退款
                startActivity(new Intent(activity, RefundActivity.class));
                break;
            case 6://我的订单
                startActivity(new Intent(activity, OrderListActivity.class));
                break;
            case 7://有卡有贷

                break;
        }
    }

    private void getTodayData() {
        FormBody.Builder builder = BaseParam.getParams();
        builder.add("click", "" + true);
        HttpUtils.sendPost(builder, UrlDefine.URL_POS_SHIFT, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject json) {
                try {
                    ShiftRecord sr = new Gson().fromJson(json.optString("data"), ShiftRecord.class);

                    //今日收益
//                    today_earn.setText();
                    //今日共计
//                    today_statistics.setText();
                    //会员充值
                    member_pay.setText(ConvertUtil.parseMoney(sr.getRechargeAmt()));

//                    list.get(0).setSum(ConvertUtil.parseMoney(sr.getwechat()));
//                    list.get(1).setSum(ConvertUtil.parseMoney(sr.getalipay()));
//                    list.get(2).setSum(ConvertUtil.parseMoney(sr.getCash()));
//
//                    list.get(3).setSum(ConvertUtil.parseMoney(sr.getRechargeAmt()));
//                    list.get(4).setSum(ConvertUtil.parseMoney(sr.getRefundAmt()));
//                    list.get(5).setSum(ConvertUtil.parseMoney(sr.getMemberPayAmt()));
                } catch (Exception ex) {
                    T.showCenter("获取数据格式有误");
                }

            }

            @Override
            public void onFailed(int code, final String msg) {
                T.showCenter(msg);
            }

            @Override
            public void networkError() {
                Toast.makeText(activity, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

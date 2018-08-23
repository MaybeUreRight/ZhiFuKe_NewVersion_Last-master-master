package com.weilay.pos2.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.weilay.pos2.R;
import com.weilay.pos2.activity.MainActivity;
import com.weilay.pos2.activity.MessageContentActivity;
import com.weilay.pos2.adapter.MessageAdapter3;
import com.weilay.pos2.app.MessageType;
import com.weilay.pos2.bean.CouponEntity;
import com.weilay.pos2.bean.MessageEntity;
import com.weilay.pos2.bean.PosDefine;
import com.weilay.pos2.db.CouponDBHelper;
import com.weilay.pos2.db.MessageDBHelper;
import com.weilay.pos2.dialog.GetCardDialog;
import com.weilay.pos2.listener.GetCouponListener;
import com.weilay.pos2.listener.TaskReceiverListener;
import com.weilay.pos2.util.LogUtils;
import com.weilay.pos2.util.SPUtils;
import com.weilay.pos2.util.T;
import com.weilay.pos2.util.Utils;

import java.util.ArrayList;

public class MessageFragment extends Fragment implements MessageAdapter3.MessageItemClickListener {
    private Activity activity;
    private RecyclerView message_recyclerview;
    private ArrayList<MessageEntity> datas;
    private MessageAdapter3 adapter;

    public MessageFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        View titleContainer = view.findViewById(R.id.fragment_message_title);
        titleContainer.findViewById(R.id.title_back).setVisibility(View.GONE);
        TextView title = titleContainer.findViewById(R.id.title_title);
        title.setText(getString(R.string.message));

        message_recyclerview = view.findViewById(R.id.message_recyclerview);

        initRecyclerView();
    }


    private void initRecyclerView() {
        datas = new ArrayList<>();

        adapter = new MessageAdapter3(activity, this, datas);
        message_recyclerview.setAdapter(adapter);

        //添加Android自带的分割线
        message_recyclerview.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));
        message_recyclerview.setLayoutManager(new LinearLayoutManager(activity));
    }

    String sound = "true";

    /**
     * @detail 初始化数据
     */
    public void initDatas() {
        sound = SPUtils.getInstance().getString(PosDefine.CACHE_SOUND_CONFIG, "");
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... arg0) {
                datas = MessageDBHelper.findAllMessages2();
                LogUtils.d("---" + new Gson().toJson(datas));
                return null;
            }

            @Override
            protected void onPostExecute(Void arg0) {
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }


    boolean onitemclick = false;

    /******
     * @detail 保存收到的卡券信息
     * @param coupon
     */
    private void saveCoupon(final CouponEntity coupon) {
        if (coupon == null) {
            return;
        }
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... arg0) {
                CouponDBHelper.saveCoupons(coupon);// 保存卡券
                return null;
            }

            protected void onPostExecute(Void result) {

            }
        }.execute();
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
    public void messageItemClick(int position) {
        //下级界面已经写好
//        startActivity(new Intent(activity, MessageContentActivity.class));
        if (onitemclick) {
            return;
        }
        onitemclick = true;
        ((MainActivity) activity).showLoading("正在查询卡券信息");
        try {
            final MessageEntity message = datas.get(position);
            switch (message.getType()) {
                case MessageType.CARD:
                    // 如果是卡券的话,查询卡券的信息
                    Utils.getAdverCardInfo(message.getMsgid(), new GetCouponListener() {

                        @Override
                        public void onData(final CouponEntity coupon) {
                            ((MainActivity) activity).stopLoading();
                            GetCardDialog dialog = new GetCardDialog();
                            dialog.setCoupon(coupon);
                            dialog.setTaskReceiverListener(new TaskReceiverListener() {

                                @Override
                                public void receiver() {
                                    MessageDBHelper.deleteMessage(message);
                                    datas.remove(message);
                                    adapter.notifyDataSetChanged();
                                    coupon.setId(message.getMsgid());
                                    // 接受任务的时候保存卡券
                                    saveCoupon(coupon);

                                }

                                @Override
                                public void cancel() {
                                    // 拒绝接收任务也要删除卡券的信息（谢总说的）
                                    MessageDBHelper.deleteMessage(message);
                                    datas.remove(message);
                                    adapter.notifyDataSetChanged();

                                }
                            });
                            dialog.show(((MainActivity) activity).getSupportFragmentManager(), "卡券任务详情");
                            onitemclick = false;
                        }

                        @Override
                        public void onFailed(String msg) {
                            // TODO Auto-generated method stub
                            onitemclick = false;
                            ((MainActivity) activity).stopLoading();
                            T.showCenter("找不到对应的卡券");
                        }

                    });
                    break;
                default:
                    onitemclick = false;
                    break;
            }
        } catch (Exception ex) {

        }
    }
}

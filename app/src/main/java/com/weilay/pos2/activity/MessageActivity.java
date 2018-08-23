package com.weilay.pos2.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.weilay.pos2.PayApplication;
import com.weilay.pos2.R;
import com.weilay.pos2.adapter.MessageAdapter;
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
import java.util.List;

/**
 * 消息中心  界面
 */
public class MessageActivity extends TitleActivity {

    private ListView messageLv;
    private MessageAdapter messageAdapter;
    private List<MessageEntity> datas = new ArrayList<>();
    private ImageView soundIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_message);
//		GlobalPush.clearNewCount();
        setTitle("消息中心");
        messageLv = (ListView) findViewById(R.id.message_lv);
        soundIv = (ImageView) findViewById(R.id.sound_setting);
        soundIv.setVisibility(View.VISIBLE);
        View emptyView = findViewById(R.id.empty_view);
        messageLv.setEmptyView(emptyView);
        messageAdapter = new MessageAdapter(this, datas);
        messageLv.setAdapter(messageAdapter);
        initDatas();
        initEvents();
    }

    String sound = "true";

    /******
     * @detail 初始化数据
     */
    public void initDatas() {
        sound = SPUtils.getInstance().getString(PosDefine.CACHE_SOUND_CONFIG, "");
        soundIv.setImageResource("true".equals(sound) ? R.drawable.icon_sound : R.drawable.icon_mute);
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... arg0) {
                datas = MessageDBHelper.findAllMessages();
                LogUtils.d("---" + new Gson().toJson(datas));
                return null;
            }

            @Override
            protected void onPostExecute(Void arg0) {
                messageAdapter.notifyDataSetChange(datas);
            }
        }.execute();
    }

    boolean onitemclick = false;

    public void initEvents() {
        soundIv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                sound = "true".equals(sound) ? "false" : "true";
                T.showCenter("true".equals(sound) ? "已经打开消息提醒声音" : "已经关闭消息提醒声音");
                SPUtils.getInstance().put(PosDefine.CACHE_SOUND_CONFIG, sound);

                soundIv.setImageResource("true".equals(sound) ? R.drawable.icon_sound : R.drawable.icon_mute);
            }
        });
        messageLv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (onitemclick) {
                    return;
                }
                onitemclick = true;
                showLoading("正在查询卡券信息");
                try {
                    final MessageEntity message = datas.get(arg2);
                    switch (message.getType()) {
                        case MessageType.CARD:
                            // 如果是卡券的话,查询卡券的信息
                            Utils.getAdverCardInfo(message.getMsgid(), new GetCouponListener() {

                                @Override
                                public void onData(final CouponEntity coupon) {
                                    stopLoading();
                                    GetCardDialog dialog = new GetCardDialog();
                                    dialog.setCoupon(coupon);
                                    dialog.setTaskReceiverListener(new TaskReceiverListener() {

                                        @Override
                                        public void receiver() {
                                            MessageDBHelper.deleteMessage(message);
                                            datas.remove(message);
                                            messageAdapter.notifyDataSetChange(datas);
                                            coupon.setId(message.getMsgid());
                                            // 接受任务的时候保存卡券
                                            saveCoupon(coupon);

                                        }

                                        @Override
                                        public void cancel() {
                                            // 拒绝接收任务也要删除卡券的信息（谢总说的）
                                            MessageDBHelper.deleteMessage(message);
                                            datas.remove(message);
                                            messageAdapter.notifyDataSetChange(datas);

                                        }
                                    });
                                    dialog.show(getSupportFragmentManager(), "卡券任务详情");
                                    onitemclick = false;
                                }

                                @Override
                                public void onFailed(String msg) {
                                    // TODO Auto-generated method stub
                                    onitemclick = false;
                                    stopLoading();
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
        });
    }

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

//	@Override
//	public void pushArraival(int messageCount) {
//		super.pushArraival(messageCount);
//		initDatas();
//	}

}

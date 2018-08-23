package com.weilay.pos2.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weilay.pos2.R;
import com.weilay.pos2.app.MessageType;
import com.weilay.pos2.bean.MessageEntity;
import com.weilay.pos2.util.ConvertUtil;
import com.weilay.pos2.util.ServerTimeUtils;
import com.weilay.pos2.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter3 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private List<MessageEntity> goodBeanList;
    private MessageItemClickListener messageItemClickListener;


    public MessageAdapter3(Activity activity, MessageItemClickListener messageItemClickListener, ArrayList<MessageEntity> goodBeanList) {
        this.activity = activity;
        this.goodBeanList = goodBeanList;
        this.messageItemClickListener = messageItemClickListener;
        if (this.goodBeanList == null) {
            this.goodBeanList = new ArrayList<>();
        }
    }
    int count = 0;
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1,  int position) {
        //相当于listview的adapter中的getview方法
        MessageEntity sti = goodBeanList.get(position);
        NormalViewHoler holder = (NormalViewHoler) holder1;
        try {
            ++count;
            switch (sti.getType()) {
                case MessageType.GLOBAL:
                    holder.titleTv.setText(sti.getTitle() == null ? "" : sti.getTitle());
                    holder.contentTv.setText(sti.getData());
                    break;
                case MessageType.CARD:
                    double price = ConvertUtil.getMoney(sti.getData());
                    String priceStr = "每领一张可获得" + ConvertUtil.branchToYuan(price) + "元奖励";
                    holder.titleTv.setText("你有新的卡券信息");
                    holder.contentTv.setText(priceStr);
                    break;
                default:
                    break;
            }
            String time = "" + TimeUtils.parseComeTime(ServerTimeUtils.server_time, sti.getTime() * 1000);
            holder.timeTv.setText(time);

            final int tempPosition = position;
            holder.item_message_rootview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messageItemClickListener.messageItemClick(tempPosition);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public NormalViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        //负责创建视图
        View view = LayoutInflater.from(activity).inflate(R.layout.item_message2, parent, false);
        return new NormalViewHoler(view);
    }

    @Override
    public int getItemCount() {
        return goodBeanList.size();
    }

    class NormalViewHoler extends RecyclerView.ViewHolder {
        public View item_message_rootview;
        public TextView titleTv, contentTv, timeTv;

         NormalViewHoler(View itemView) {
            super(itemView);
             item_message_rootview = itemView.findViewById(R.id.item_message_rootview);
            titleTv = (TextView) itemView.findViewById(R.id.message_title_tv);
            contentTv = (TextView) itemView.findViewById(R.id.message_content_tv);
            timeTv = (TextView) itemView.findViewById(R.id.message_time_tv);
        }
    }
    public interface MessageItemClickListener{
        void messageItemClick(int position);
    }
}

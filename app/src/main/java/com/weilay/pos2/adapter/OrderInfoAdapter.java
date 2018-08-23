package com.weilay.pos2.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.weilay.pos2.R;
import com.weilay.pos2.bean.OrderInfoBean;
import com.weilay.pos2.bean.PersonalCenterBean;
import com.weilay.pos2.listener.OnItemclickListener;

import java.util.ArrayList;


public class OrderInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<OrderInfoBean> list;
    private OnItemclickListener onItemclickListener;


    public OrderInfoAdapter(Context context, ArrayList<OrderInfoBean> list) {
        this.context = context;
        this.list = list;
        onItemclickListener = (OnItemclickListener) context;
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //相当于listview的adapter中的getview方法
        OrderInfoBean bean = list.get(position);
        NormalViewHoler normalViewHoler = (NormalViewHoler) holder;

        normalViewHoler.item_order_info_order_number.setText(bean.orderNumber);
        normalViewHoler.item_order_info_order_date.setText(bean.date);
        normalViewHoler.item_order_info_order_amount.setText(bean.amount);

        normalViewHoler.itemView.setTag(position);//将位置保存在tag中

        normalViewHoler.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemclickListener.onIndexItemClick(position);
            }
        });


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //负责创建视图
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_info, parent, false);
        return new NormalViewHoler(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class NormalViewHoler extends RecyclerView.ViewHolder {
        private TextView item_order_info_order_number;
        private TextView item_order_info_order_date;
        private TextView item_order_info_order_amount;

        public NormalViewHoler(View itemView) {
            super(itemView);
            item_order_info_order_number = itemView.findViewById(R.id.item_order_info_order_number);
            item_order_info_order_date = itemView.findViewById(R.id.item_order_info_order_date);
            item_order_info_order_amount = itemView.findViewById(R.id.item_order_info_order_amount);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "MicrosoftYaHeiLight.ttf");
            item_order_info_order_number.setTypeface(tf);
            item_order_info_order_date.setTypeface(tf);
            item_order_info_order_amount.setTypeface(tf);
        }
    }
}

package com.weilay.pos2.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.weilay.pos2.R;
import com.weilay.pos2.bean.ExchangeBean;
import com.weilay.pos2.listener.OnItemclickListener;

import java.util.ArrayList;


public class ExchangeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<ExchangeBean> list;
    private OnItemclickListener onItemclickListener;


    public ExchangeAdapter(Context context, ArrayList<ExchangeBean> list) {
        this.context = context;
        this.list = list;
        onItemclickListener = (OnItemclickListener) this.context;
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        NormalViewHoler normalViewHoler = (NormalViewHoler) holder;
        ExchangeBean bean = list.get(position);

        normalViewHoler.item_exchange_icon.setImageResource(bean.resId);
        normalViewHoler.item_exchange_typename.setText(bean.typeName);
        normalViewHoler.item_exchange_sum.setText(bean.sum);
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_exchange, parent, false);
        return new NormalViewHoler(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class NormalViewHoler extends RecyclerView.ViewHolder {
        private ImageView item_exchange_icon;
        private TextView item_exchange_typename;
        private TextView item_exchange_sum;

        public NormalViewHoler(View itemView) {
            super(itemView);
            item_exchange_icon = itemView.findViewById(R.id.item_exchange_icon);
            item_exchange_typename = itemView.findViewById(R.id.item_exchange_typename);
            item_exchange_sum = itemView.findViewById(R.id.item_exchange_sum);

            Typeface tf = Typeface.createFromAsset(context.getAssets(), "MicrosoftYaHeiLight.ttf");
            item_exchange_typename.setTypeface(tf);
            item_exchange_sum.setTypeface(tf);
        }
    }
}

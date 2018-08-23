package com.weilay.pos2.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.weilay.pos2.R;
import com.weilay.pos2.bean.PersonalCenterBean;
import com.weilay.pos2.bean.RechargeBean;
import com.weilay.pos2.listener.OnItemclickListener;

import java.util.ArrayList;


public class RechargeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<RechargeBean> list;
    private OnItemclickListener onItemclickListener;


    public RechargeAdapter(Context context, ArrayList<RechargeBean> list) {
        this.context = context;
        this.list = list;
        onItemclickListener = (OnItemclickListener) this.context;
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        //相当于listview的adapter中的getview方法
        RechargeBean bean = list.get(position);
        NormalViewHoler normalViewHoler = (NormalViewHoler) holder;
        normalViewHoler.item_recharge_button.setText(bean.name);
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_recharge, parent, false);
        return new NormalViewHoler(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class NormalViewHoler extends RecyclerView.ViewHolder {
        private Button item_recharge_button;

        public NormalViewHoler(View itemView) {
            super(itemView);
            item_recharge_button = itemView.findViewById(R.id.item_recharge_button);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "MicrosoftYaHeiLight.ttf");
            item_recharge_button.setTypeface(tf);
        }
    }
}

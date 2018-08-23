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
import com.weilay.pos2.bean.PersonalCenterBean;
import com.weilay.pos2.listener.OnItemclickListener;

import java.util.ArrayList;


public class InfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<PersonalCenterBean> list;
    private OnItemclickListener onItemclickListener;


    public InfoAdapter(Context context, Fragment fragment, ArrayList<PersonalCenterBean> list) {
        this.context = context;
        this.list = list;
        onItemclickListener = (OnItemclickListener) fragment;
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        //相当于listview的adapter中的getview方法
        PersonalCenterBean bean = list.get(position);
        NormalViewHoler normalViewHoler = (NormalViewHoler) holder;

        normalViewHoler.item_personalcenter_tv.setText(context.getString(bean.nameId));
        normalViewHoler.item_personalcenter_desc.setText(bean.description);
        normalViewHoler.item_personalcenter_iv.setImageResource(bean.resId);

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
        View view = LayoutInflater.from(context).inflate(R.layout.item_info, parent, false);
        return new NormalViewHoler(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class NormalViewHoler extends RecyclerView.ViewHolder {
        //普通商品的Item
        private ImageView item_personalcenter_iv;
        private TextView item_personalcenter_tv;
        private TextView item_personalcenter_desc;

        public NormalViewHoler(View itemView) {
            super(itemView);
            item_personalcenter_iv = itemView.findViewById(R.id.item_personalcenter_iv);
            item_personalcenter_tv = itemView.findViewById(R.id.item_personalcenter_tv);
            item_personalcenter_desc = itemView.findViewById(R.id.item_personalcenter_desc);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "MicrosoftYaHeiLight.ttf");
            item_personalcenter_tv.setTypeface(tf);
            item_personalcenter_desc.setTypeface(tf);
        }
    }
}

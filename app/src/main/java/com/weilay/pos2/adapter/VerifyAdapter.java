package com.weilay.pos2.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.weilay.pos2.R;
import com.weilay.pos2.bean.PersonalCenterBean;
import com.weilay.pos2.listener.OnItemclickListener;

import java.util.ArrayList;


public class VerifyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<String> list;
    private OnItemclickListener onItemclickListener;
    private int height;


    public VerifyAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
        onItemclickListener = (OnItemclickListener) this.context;
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        NormalViewHoler normalViewHoler = (NormalViewHoler) holder;
        String str = list.get(position);

        Log.i("Demo", "position = " + position + "   ----------    str = " + str);
        normalViewHoler.item_verify_tv.setText(str);
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_verify, parent, false);
        return new NormalViewHoler(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class NormalViewHoler extends RecyclerView.ViewHolder {
        //普通商品的Item
        private TextView item_verify_tv;

        public NormalViewHoler(View itemView) {
            super(itemView);
            item_verify_tv = itemView.findViewById(R.id.item_verify_tv);
//            Typeface tf = Typeface.createFromAsset(context.getAssets(), "MicrosoftYaHeiLight.ttf");
//            item_verify_tv.setTypeface(tf);
        }
    }
}

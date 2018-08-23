package com.weilay.pos2.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.weilay.pos2.R;
import com.weilay.pos2.bean.IndexItemBean;

import java.util.ArrayList;


public class IndexItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private Fragment fragment;
    private ArrayList<IndexItemBean> goodBeanList;
    private IndexItemClickListener indexItemClickListener;


    public IndexItemAdapter(Activity activity, Fragment fragment, IndexItemClickListener indexItemClickListener, ArrayList<IndexItemBean> goodBeanList) {
        this.activity = activity;
        this.fragment = fragment;
        this.goodBeanList = goodBeanList;
        this.indexItemClickListener = indexItemClickListener;
        if (this.goodBeanList == null) {
            this.goodBeanList = new ArrayList<>();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //相当于listview的adapter中的getview方法
        IndexItemBean goodBean = goodBeanList.get(position);
        NormalViewHoler normalViewHoler = (NormalViewHoler) holder;
        normalViewHoler.item_index_tv.setText(goodBean.name);
//        normalViewHoler.item_index_iv.setImageResource(goodBean.resId);
        Glide.with(fragment).asBitmap().load(goodBean.resId).into(normalViewHoler.item_index_iv).getSize(new SizeReadyCallback() {
            @Override
            public void onSizeReady(int width, int height) {

            }
        });
        normalViewHoler.itemView.setTag(position);//将位置保存在tag中
        final int temp = position;
        normalViewHoler.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indexItemClickListener.indexItemClick(temp);
            }
        });


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //负责创建视图
        View view = LayoutInflater.from(activity).inflate(R.layout.item_index, parent, false);
//        View v = View.inflate(context, R.layout.item_index, null);
        return new NormalViewHoler(view);
    }

    @Override
    public int getItemCount() {
        return goodBeanList.size();
    }

    class NormalViewHoler extends RecyclerView.ViewHolder {
        //普通商品的Item
        private TextView item_index_tv;
        private ImageView item_index_iv;

        public NormalViewHoler(View itemView) {
            super(itemView);
            item_index_tv = itemView.findViewById(R.id.item_index_tv);
            item_index_iv = itemView.findViewById(R.id.item_index_iv);
//            Typeface tf = Typeface.createFromAsset(activity.getAssets(), "MicrosoftYaHeiLight.ttf");
//            item_index_tv.setTypeface(tf);

        }
    }

    public interface IndexItemClickListener {
        void indexItemClick(int position);
    }
}

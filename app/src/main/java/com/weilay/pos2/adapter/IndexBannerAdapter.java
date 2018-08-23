package com.weilay.pos2.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.weilay.pos2.R;

import java.util.ArrayList;


public class IndexBannerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private Fragment fragment;
    private ArrayList<String> goodBeanList;
    private IndexBannerItemClickListener indexBannerItemClickListener;


    public IndexBannerAdapter(Activity activity,Fragment fragment,IndexBannerItemClickListener indexBannerItemClickListener, ArrayList<String> goodBeanList) {
        this.activity = activity;
        this.fragment = fragment;
        this.goodBeanList = goodBeanList;
        this.indexBannerItemClickListener = indexBannerItemClickListener;
        if (this.goodBeanList == null) {
            this.goodBeanList = new ArrayList<>();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,  int position) {
        //相当于listview的adapter中的getview方法
        String goodBean = goodBeanList.get(position);
        NormalViewHoler normalViewHoler = (NormalViewHoler) holder;
        Glide.with(fragment).asBitmap().load(goodBean).into(normalViewHoler.item_banner_iv);
        normalViewHoler.itemView.setTag(position);//将位置保存在tag中
        final int temp = position;
        normalViewHoler.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indexBannerItemClickListener.bannerItemClick(temp);
            }
        });


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //负责创建视图
        View view = LayoutInflater.from(activity).inflate(R.layout.item_banner, parent, false);
        return new NormalViewHoler(view);
    }

    @Override
    public int getItemCount() {
        return goodBeanList.size();
    }

    class NormalViewHoler extends RecyclerView.ViewHolder {
        private ImageView item_banner_iv;

        public NormalViewHoler(View itemView) {
            super(itemView);
            item_banner_iv = itemView.findViewById(R.id.item_banner_iv);
        }
    }

    public interface IndexBannerItemClickListener{
        void bannerItemClick(int position);
    }
}

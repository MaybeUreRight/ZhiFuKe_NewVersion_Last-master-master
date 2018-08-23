package com.weilay.pos2.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.weilay.pos2.R;
import com.weilay.pos2.base.BaseAdapter;
import com.weilay.pos2.base.BaseViewHolder;
import com.weilay.pos2.bean.PayType;

import java.util.List;

public class PayAdapter extends BaseAdapter<PayType> {
    //private Activity mContext;
    private PayType currentSelect = PayType.WEIXIN;

    public PayAdapter(Activity context, List<PayType> datas) {
        super(context, datas);
        //	this.mContext=context;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        if (arg1 == null) {
//            arg1 = LayoutInflater.from(context).inflate(R.layout.pay_item, null);
            arg1 = LayoutInflater.from(context).inflate(R.layout.pay_item_new, null);
        }
        ImageView payIcon = BaseViewHolder.get(arg1, R.id.pay_icon);
        TextView payText = BaseViewHolder.get(arg1, R.id.pay_text);
        payIcon.setImageResource(datas.get(arg0).getIcon());
        payText.setText(datas.get(arg0).getValue());
        payIcon.setEnabled(currentSelect == datas.get(arg0) ? false : true);
        return arg1;
    }

    public void setSelect(PayType payType) {
        currentSelect = payType;
        List temps = datas;
        notifyDataSetChange(temps);

    }

}

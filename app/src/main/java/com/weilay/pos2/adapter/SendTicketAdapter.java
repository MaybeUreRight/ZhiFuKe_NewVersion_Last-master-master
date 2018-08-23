package com.weilay.pos2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.weilay.pos2.R;
import com.weilay.pos2.bean.SendTicketInfo;
import com.weilay.pos2.local.UrlDefine;

import java.util.List;

public class SendTicketAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<SendTicketInfo> list_sti;
    private Context context;

    public SendTicketAdapter(Context context, List<SendTicketInfo> list_sti) {
        inflater = LayoutInflater.from(context);
        this.list_sti = list_sti;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list_sti.size();
    }

    @Override
    public Object getItem(int position) {
        return list_sti.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        SendTicketInfo sti = list_sti.get(position);
        if (view == null) {
            view = inflater.inflate(R.layout.sendticket_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.cardinfo_tv = (TextView) view.findViewById(R.id.cardinfo_tv);
            viewHolder.merchantlogo_iv = (ImageView) view
                    .findViewById(R.id.merchantlogo_iv);
            viewHolder.sendticket_stock = (TextView) view
                    .findViewById(R.id.sendticket_stock);
            viewHolder.sendticket_deadline = (TextView) view
                    .findViewById(R.id.sendticket_deadline);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        setLayout(viewHolder, sti);

        return view;
    }

    private void setLayout(ViewHolder viewHolder, SendTicketInfo sti) {
        viewHolder.cardinfo_tv.setText(sti.getCardinfo());

        Glide.with(context).load(UrlDefine.BASE_URL + sti.getMerchantlogo()).into(viewHolder.merchantlogo_iv);

        String stock = "数量："+sti.getStock()+"张";
        viewHolder.sendticket_stock.setText(stock);

        String deadLine = sti.getDeadline()+"  到期";
        viewHolder.sendticket_deadline.setText(deadLine);
        // String color = sti.getColor();
        // viewHolder.layout.setBackgroundColor(Color.parseColor(color != null ? color
        // : "#FFFFFF"));
    }

    class ViewHolder {
        ImageView merchantlogo_iv;
        TextView cardinfo_tv, sendticket_stock, sendticket_deadline;
    }

}

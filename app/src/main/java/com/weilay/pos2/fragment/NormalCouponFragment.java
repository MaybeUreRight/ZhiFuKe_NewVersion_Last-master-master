package com.weilay.pos2.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.weilay.pos2.R;
import com.weilay.pos2.activity.SendTicketBeginAvtivity;
import com.weilay.pos2.adapter.SendTicketAdapter;
import com.weilay.pos2.bean.CardTypeEnum;
import com.weilay.pos2.bean.SendTicketInfo;

import java.util.ArrayList;

public class NormalCouponFragment extends SendTickectFragment {


    /**
     * 数据格式如下：
     * {
     * "code": 0,
     * "message": "OK",
     * "data": [
     * {
     * "id": "4460",
     * "merchantlogo": "/Uploads/subMerchants/90917/logo.png",
     * "stock": "203",
     * "type": "CASH",
     * "cardinfo": "100元代金券",
     * "begin_timestamp": "1531152000",
     * "deadline": "2018-08-31",
     * "end_time": "1535731199",
     * "merchantname": "智付客",
     * "url2qrcode": "http://zfk.zfk360.cn/UserCenter/viewCard?id=4460&mt=C&sn=9e6f5559",
     * "color": "Color050"
     * },
     * {
     * "id": "1253",
     * "merchantlogo": "/Uploads/subMerchants/90917/logo.jpg",
     * "stock": "371",
     * "type": "DISCOUNT",
     * "cardinfo": "优乐美奶茶折扣券",
     * "begin_timestamp": "0",
     * "deadline": "领取当天至15天内有效",
     * "end_time": "15",
     * "merchantname": "智付客",
     * "url2qrcode": "http://zfk.zfk360.cn/UserCenter/viewCard?id=1253&mt=C&sn=9e6f5559",
     * "color": "Color100"
     * },
     * {
     * "id": "219",
     * "merchantlogo": "/Uploads/subMerchants/90917/logo.jpg",
     * "stock": "979",
     * "type": "DISCOUNT",
     * "cardinfo": "9折优惠券",
     * "begin_timestamp": "0",
     * "deadline": "领取当天至15天内有效",
     * "end_time": "15",
     * "merchantname": "智付客",
     * "url2qrcode": "http://zfk.zfk360.cn/UserCenter/viewCard?id=219&mt=C&sn=9e6f5559",
     * "color": "Color010"
     * },
     * {
     * "id": "218",
     * "merchantlogo": "/Uploads/subMerchants/90917/logo.jpg",
     * "stock": "950",
     * "type": "GIFT",
     * "cardinfo": "咖啡一杯",
     * "begin_timestamp": "0",
     * "deadline": "领取当天至30天内有效",
     * "end_time": "30",
     * "merchantname": "智付客",
     * "url2qrcode": "http://zfk.zfk360.cn/UserCenter/viewCard?id=218&mt=C&sn=9e6f5559",
     * "color": "Color010"
     * }
     * ]
     * }
     */


    private ListView nc_listview;
    private ArrayList<SendTicketInfo> list_sti;
    private ArrayList<SendTicketInfo> normalList;
    private SendTicketInfo sti;

    @Override
    public View initViews(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.sendticket_layout, container, false);
        nc_listview = (ListView) view.findViewById(R.id.sendticket_listview);
        TextView emptyView = (TextView) view.findViewById(R.id.empty_view);
        nc_listview.setEmptyView(emptyView);
        list_sti = getArguments().getParcelableArrayList("normalcoupon");
        normalList = new ArrayList<SendTicketInfo>();
        if (list_sti != null) {
            for (int i = 0; i < list_sti.size(); i++) {
                SendTicketInfo for_sti = list_sti.get(i);
                switch (for_sti.getType()) {
                    case CardTypeEnum.FRIEND_CASH:

                        break;
                    case CardTypeEnum.FRIEND_GIFT:

                        break;
                    default:
                        normalList.add(for_sti);
                        break;
                }

            }
        }

        SendTicketAdapter adapter = new SendTicketAdapter(getActivity(), normalList);
        nc_listview.setAdapter(adapter);
        nc_listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                sti = normalList.get(position);
                // view.setTag(position);
                // listview_init(view);
                if (sti != null) {
                    Intent intent = new Intent(getActivity(), SendTicketBeginAvtivity.class);
                    // Bundle bundle = new Bundle();
                    // bundle.putParcelable("sti", sti);
                    intent.putExtra("sti", sti);
                    startActivity(intent);
                }
            }
        });
        return view;
    }

    @Override
    public void initDatas() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initEvents() {
        // TODO Auto-generated method stub

    }


}

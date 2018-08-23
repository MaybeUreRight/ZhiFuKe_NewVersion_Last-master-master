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
import com.weilay.pos2.http.CouponRequest;
import com.weilay.pos2.listener.ResponseListener;
import com.weilay.pos2.util.T;

import org.json.JSONObject;

import java.util.ArrayList;

public class FriendCouponFragment extends SendTickectFragment {
	private ListView fc_listview;
	private ArrayList<SendTicketInfo> list_sti;
	private ArrayList<SendTicketInfo> FriendList;
	private SendTicketInfo sti;
	@Override
	public View initViews(LayoutInflater inflater, ViewGroup container) {
		View view = inflater.inflate(R.layout.sendticket_layout, container, false);
		fc_listview = (ListView) view.findViewById(R.id.sendticket_listview);
		TextView emptyView = (TextView) view.findViewById(R.id.empty_view);
		fc_listview.setEmptyView(emptyView);
		list_sti = getArguments().getParcelableArrayList("friendcoupon");
		FriendList = new ArrayList<SendTicketInfo>();
		if (list_sti != null) {
			for (int i = 0; i < list_sti.size(); i++) {
				SendTicketInfo for_sti = list_sti.get(i);
				switch (for_sti.getType()) {
				case CardTypeEnum.FRIEND_CASH:
					FriendList.add(for_sti);
					break;
				case CardTypeEnum.FRIEND_GIFT:
					FriendList.add(for_sti);
					break;
				default:
					break;
				}
			}
		}

		SendTicketAdapter adapter = new SendTicketAdapter(getActivity(), FriendList);
		fc_listview.setAdapter(adapter);
		fc_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				sti = FriendList.get(position);
				mContext.showLoading("正在获取发券信息");
				CouponRequest.getFQrcode(sti.getId(), new ResponseListener() {
					@Override
					public void onSuccess(JSONObject json) {
						mContext.stopLoading();
						// TODO Auto-generated method stub
						String qrcode=json.optString("data");
						sti.setUrl2qrcode(qrcode);
						if(getActivity()!=null){
							Intent intent = new Intent(getActivity(), SendTicketBeginAvtivity.class);
							intent.putExtra("sti", sti);
							startActivity(intent);
						}
						
						
						
					}
					
					@Override
					public void onFailed(int code, String msg) {
						// TODO Auto-generated method stub
						mContext.stopLoading();
						T.showCenter(msg);
					}
				});
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

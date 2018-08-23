package com.weilay.pos2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.weilay.pos2.R;
import com.weilay.pos2.bean.RechargePreferEntity;

import java.util.ArrayList;
import java.util.List;

/******
 * @detail 充值优惠金额
 * @author rxwu
 * @date 2016/07/25
 *
 */
public class RechargePreferAdapter extends BaseAdapter {
	private List<RechargePreferEntity> datas = new ArrayList<>();
	private Context mContext;

	public RechargePreferAdapter(Context context) {
		this.mContext = context;
	}

	@Override
	public int getCount() {
		//  
		return datas == null ? 0 : datas.size();
	}

	@Override
	public RechargePreferEntity getItem(int arg0) {
		//  
		return datas.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		//  
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		//  
		ViewHolder viewholder = null;
		if (arg1 == null) {
			arg1 = LayoutInflater.from(mContext).inflate(R.layout.member_recharge_prefer, null);
			viewholder = new ViewHolder();
			viewholder.preferitem = (TextView) arg1.findViewById(R.id.member_recharge_item_tv);
			arg1.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) arg1.getTag();
		}
		viewholder.preferitem.setText((arg0+1)+"、"+getItem(arg0).getName());
		return arg1;
	}

	/******
	 * @detail viewHolder
	 * @author Administrator
	 *
	 */
	class ViewHolder {
		TextView preferitem;
	}

	public void notifyDataSetChange(List<RechargePreferEntity> datas) {
		this.datas = datas;
		notifyDataSetChanged();
	}
}

package com.weilay.pos2.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.weilay.pos2.R;
import com.weilay.pos2.adapter.CouponAdapter;
import com.weilay.pos2.base.BaseFragment;
import com.weilay.pos2.bean.CouponEntity;

import java.util.ArrayList;
import java.util.List;

public class CouponTaskFragment extends BaseFragment {
	private ListView taskList;
	private CouponAdapter adapter = null;
	private List<CouponEntity> datas = new ArrayList<>();

	@Override
	public View initViews(LayoutInflater inflater, ViewGroup container) {
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.fragment_task, null);
		}
		taskList = getViewById(R.id.task_list);
		View emptyView = getViewById(R.id.empty_view);
		taskList.setEmptyView(emptyView);
		adapter = new CouponAdapter(mContext, datas);
		taskList.setAdapter(adapter);
		return mRootView;
	}

	@Override
	public void initDatas() {

	}

	@Override
	public void initEvents() {

	}

	public void setDatas(List<CouponEntity> datas) {
		if (datas != null && adapter != null) {
			adapter.notityDataSetChange(datas);
		}
	}

}

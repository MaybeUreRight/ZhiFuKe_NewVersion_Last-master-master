package com.weilay.pos2.listener;


import com.weilay.pos2.bean.CouponEntity;

public interface GetCouponListener {
	// 接受数据
	public void onData(CouponEntity entity);

	// 失败的情况
	public void onFailed(String msg);
}

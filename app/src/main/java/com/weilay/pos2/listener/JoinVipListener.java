package com.weilay.pos2.listener;


import com.weilay.pos2.bean.JoinVipEntity;

public interface JoinVipListener {
	public void onSuc(JoinVipEntity info);
	public void onErr();
	public void on404(String msg);//没有上架会员卡
}

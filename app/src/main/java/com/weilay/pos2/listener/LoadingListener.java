package com.weilay.pos2.listener;

public interface LoadingListener {
	/*****
	 * @detail 加载取消
	 */
	public void onCancel();

	/******
	 * @detail 加载超时
	 */
	public void timeOut();

}

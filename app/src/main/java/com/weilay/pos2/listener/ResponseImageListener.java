package com.weilay.pos2.listener;

import android.graphics.Bitmap;

import com.weilay.pos2.util.T;


public abstract class ResponseImageListener implements BaseResponseListener {

	/**********
	 * @Detail 图片下载成功后的保存文件
	 * @param bitmap
	 */
	public abstract void loadSuccess(Bitmap bitmap);


	/******
	 * @detail 网络请求错误
	 */
	public void networkError() {

	}

	/*****
	 * @detail 连接超时
	 */
	public void timeOut() {
		T.showCenter("连接超时");
	}

	/*****
	 * @detail 服务器连接超时
	 */
	public void serverTimeOut() {
		T.showCenter("服务器连接超时");
	}

}

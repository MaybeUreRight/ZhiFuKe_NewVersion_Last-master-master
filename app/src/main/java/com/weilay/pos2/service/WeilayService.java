package com.weilay.pos2.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.weilay.pos2.http.BaseParam;
import com.weilay.pos2.http.HttpManager;
import com.weilay.pos2.local.UrlDefine;

import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class WeilayService extends Service {
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		joinadalliance();
	}

	private void joinadalliance() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				send_joinadalliance();

			}
		}, 5000, 3600000);// 登录后每隔1小时发一次请求
	}

	private void send_joinadalliance() {
		Map<String, String> params2 = BaseParam.getParams2();
		HttpManager.sendPost(params2, UrlDefine.URL_IAMONLINE,null);
//		FormBody.Builder builder = BaseParam.getParams();
//		Call call = client.toserver(builder, UrlDefine.URL_IAMONLINE);
//		if (call != null) {
//			call.enqueue(new Callback() {
//
//
//				@Override
//				public void onFailure(Call arg0, IOException ioe) {
//				}
//
//				@Override
//				public void onResponse(Call arg0, Response res)
//						throws IOException {
//					String res_info = res.body().string();
//				}
//			});
//
//		} else {
//		}
	}

}

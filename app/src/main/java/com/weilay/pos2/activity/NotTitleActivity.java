package com.weilay.pos2.activity;

import android.app.ActionBar.LayoutParams;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.Result;
import com.weilay.pos2.R;
import com.weilay.pos2.base.BaseActivity;
import com.weilay.pos2.util.ActivityStackControlUtil;
import com.weilay.pos2.util.LogUtils;
import com.weilay.pos2.zxing.ViewfinderView;

public class NotTitleActivity extends BaseActivity {
	// private ImageView balk_iv, lock_iv;
	private RelativeLayout layoutContent;
	private TextView messageCountTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//     
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
		super.setContentView(R.layout.not_title);
		Main_init();
	}

	private void Main_init() {
		layoutContent = (RelativeLayout) findViewById(R.id.rl_main);
	}

	public void setContentLayout(int resId) {

		if (layoutContent == null) {
			return;
		}
		layoutContent.removeAllViews();
		View v = LayoutInflater.from(this).inflate(resId, null);
		v.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		layoutContent.addView(v);
	}

	protected String getRunningActivityName() {
		ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
		return runningActivity;
	}

	@Override
	protected void onResume() {
		super.onResume();
//		int count = GlobalPush.getNewCount();
//		// 更新消息条数
//		if (count == 0) {
//			messageCountTv.setVisibility(View.GONE);
//		} else {
//			messageCountTv.setVisibility(View.VISIBLE);
//			messageCountTv.setText(count + "");
//		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 注：回调 2
		// Bugtags.onPause(this);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// 注：回调 3
		// Bugtags.onDispatchTouchEvent(this, event);
		return super.dispatchTouchEvent(event);
	}

	// 处理扫描的结果
	public void handleDecode(Result result) {
		super.handleDecode(result);
	}

	//
	public void drawViewfinder() {

	}

	@Override
	public ViewfinderView getViewfinderView() {
		return super.getViewfinderView();
	}

//	@Override
//	public void pushArraival(int messageCount) {
//		// TODO Auto-generated method stu
//		if (messageCountTv == null) {
//			return;
//		}
//		LogUtils.d("--[NoTitleActivity]receiver the message count is :" + messageCount);
//		if (messageCount != 0) {
//			messageCountTv.setVisibility(View.VISIBLE);
//			messageCountTv.setText("" + messageCount);
//		} else {
//			messageCountTv.setVisibility(View.GONE);
//		}
//	}

	protected void home() {
		LogUtils.e("gg", "当前Activity:" + getRunningActivityName());
		String ActivityName = getRunningActivityName();
		if (!ActivityName.equals("com.weilay.pos.MainActivity")) {
			ActivityStackControlUtil.closeAll();
			Intent i = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(i);
		}
	}

	protected void back() {
		if (ActivityStackControlUtil.size() > 1) {
			try {
				onBackPressed();
				finish();
			} catch (Exception ex) {
			}

		} else {
//			Intent i = new Intent(getApplicationContext(), StartActivity.class);
//			startActivity(i);
		}

	}

	/****
	 * @detail 跳转到消息页面
	 * @return voiｄ
	 */
	protected void message() {
		Intent intent = new Intent(NotTitleActivity.this, MessageActivity.class);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		
	}
}

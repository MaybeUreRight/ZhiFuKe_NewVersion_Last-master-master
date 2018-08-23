package com.weilay.pos2.activity;

import android.app.ActionBar.LayoutParams;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.weilay.pos2.R;
import com.weilay.pos2.base.BaseActivity;
import com.weilay.pos2.util.ActivityStackControlUtil;
import com.weilay.pos2.zxing.ViewfinderView;

public class TitleActivity extends BaseActivity {
    private RelativeLayout layoutContent;
    public TextView Title_item_tv;

    public void setTitle(String titleName) {
        Title_item_tv.setText(titleName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.item_title);
        Item_init();
    }

    private void Item_init() {
        layoutContent = (RelativeLayout) findViewById(R.id.rl_main);
        Title_item_tv = (TextView) findViewById(R.id.item_title_tv);
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

    protected String getRunningActivityName() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        return runningActivity;
    }

    protected void home() {
        String ActivityName = getRunningActivityName();
        if (!ActivityName.equals("com.weilay.pos.MainActivity")) {
            ActivityStackControlUtil.closeAll();
            Intent i = new Intent(getApplicationContext(),
                    MainActivity.class);
            startActivity(i);
        }
    }

    private long lastClickTime = 0;

    protected void back() {
        if (ActivityStackControlUtil.size() > 1) {
            onBackPressed();
            finish();
        } else {
            //双击退出应用
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - lastClickTime > 500) {
                Toast.makeText(this, "再次点击将退出应用", Toast.LENGTH_SHORT).show();
                lastClickTime = currentTimeMillis;
            } else {
                ActivityStackControlUtil.exitApp();
            }
        }
    }

    @Override
    public void onClick(View v) {

    }
}

package com.weilay.pos2.base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.zxing.Result;
import com.weilay.pos2.PayApplication;
import com.weilay.pos2.R;
import com.weilay.pos2.listener.LoadingListener;
import com.weilay.pos2.util.ActivityStackControlUtil;
import com.weilay.pos2.util.BeepManager;
import com.weilay.pos2.util.DeviceUtil;
import com.weilay.pos2.util.LogUtils;
//import com.google.zxing.client.android.camera.CameraManager;
import com.weilay.pos2.zxing.CameraManager;
import com.weilay.pos2.util.ProgressDialogUtil;
import com.weilay.pos2.zxing.ViewfinderView;
import com.weilay.pos2.zxing.ZxingActivityHandler;

import java.io.IOException;

/**
 * @author: Administrator
 * @date: 2018/6/29/029
 * @description: $description$
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback {

    private Handler handler2 = new Handler();
    public int RESULT_FAILED = -2;
    private ZxingActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private SurfaceView surfaceView;
//    private BeepManager beepManager;
    CameraManager mCameraManager;

    ProgressDialog loadingDialog;
    Runnable restartCamera = new Runnable() {
        public void run() {
            LogUtils.i("正在重启摄像头");
            restartCerame();
        }
    };

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeCamera();

        // 注：回调 1
//        Bugtags.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeCarmera();
        // BeepClose();
//        if (beepManager != null) {
//            beepManager.CloseBeep();
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onDestroy() {
        ActivityStackControlUtil.remove(getClass());
//        if (mqttReceiver != null) {
//            unregisterReceiver(mqttReceiver);
//        }
        stopLoading();
        closeCarmera();
        super.onDestroy();
    }

    /**
     * 监听外设小键盘 *(keyCode=155)是进入支付界面,Enter(keyCode=160|66)是确认支付
     * BackSpace(keyCode=67)返回
     */
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        Log.i("gg", "keyCode:" + keyCode);
        switch (keyCode) {
            case 155:
                if (PayApplication.isLogin_Suc()) {
//                    Intent intent = new Intent(this, PayActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    startActivity(intent);
                }
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    ;

    /******
     * @Detail 显示进度条
     * @param text
     */
    public void showLoading(String text) {
        showLoading(text, 25, true, null);
    }

    /******
     * @Detail 显示进度条
     * @param text
     * @param loadingListener
     */
    public void showLoading(String text, LoadingListener loadingListener) {
        showLoading(text, 25, true, loadingListener);
    }

    /******
     * @Detail 显示进度条
     * @param text
     * @param closeable
     */
    public void showLoading(String text, boolean closeable) {
        showLoading(text, 25, closeable, null);
    }

    /*****
     * @Detail 显示一个加载进度条
     * @param text
     *            text 显示加载的提示内容
     * @param sec
     *            多少秒后超时隐藏进度条
     * @param loadingListener
     *            loading过程的监听
     * @param closeable
     *            是否可以关闭
     */

    public void showLoading(String text, int sec, boolean closeable, final LoadingListener loadingListener) {
        if (!isFinishing()) { // this.loadingListener = loadingListener;
            if (loadingDialog != null && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
            if (sec <= 25) {
                sec = 25;// 默认15s后无响应提示超时
            }
            loadingDialog = ProgressDialogUtil.progressDialog(this, text);
            loadingDialog.setCancelable(closeable);
            loadingDialog.setCanceledOnTouchOutside(closeable);

            loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface arg0) {
                    // TODO Auto-generated method stub
                    if (loadingListener != null) {
                        loadingListener.onCancel();
                    }
                    arg0.dismiss();
                }
            });

            if (!loadingDialog.isShowing() && !isFinishing()) {
                loadingDialog.show();
            }

            View dialog_view = loadingDialog.getWindow().getDecorView();
            DeviceUtil.setDialogText(dialog_view);
            dialog_view.postDelayed(new Runnable() {

                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    stopLoading();// 到时间就将进度条隐藏
                    if (loadingListener != null) {
                        loadingListener.timeOut();
                    }
                }
            }, sec * 1000);
        }
    }

    /*****
     * @Detail 停止进度条加载
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void stopLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()
                && (!isFinishing() || !isDestroyed())) {
            loadingDialog.dismiss();
        }
        loadingDialog = null;
    }


    protected void setFontWithMicrosoftYaHeiLight(TextView... textViews) {
//        Typeface tf = Typeface.createFromAsset(getAssets(), "MicrosoftYaHeiLight.ttf");
//        for (int i = 0; i < textViews.length; i++) {
//            textViews[i].setTypeface(tf);
//        }
    }

    protected void setFontWithMicrosoftYaHei(TextView... textViews) {
//        Typeface tf = Typeface.createFromAsset(getAssets(), "MicrosoftYaHei.ttc");
//        for (int i = 0; i < textViews.length; i++) {
//            textViews[i].setTypeface(tf);
//        }
    }


    // 处理扫描的结果
    public void handleDecode(Result result) {
//        if (beepManager != null) {
//            beepManager.playBeep();
//        }
        // inactivityTimer.onActivity();
        // closeCarmera();//关闭暂停摄像头
        // 2s后重启摄像头
        handler2.removeCallbacks(restartCamera);
        handler2.postDelayed(restartCamera, 5 * 1000);
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    public SurfaceView getSurfaceView() {
        return surfaceView;
    }

    public CameraManager getmCameraManager() {
        return mCameraManager;
    }

    /******
     * @detail 初始化摄像头
     * @param surfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (mCameraManager.isOpen()) {
            return;
        }
        try {
            mCameraManager.openDriver(surfaceHolder);
            if (handler == null) {
                handler = new ZxingActivityHandler(this, mCameraManager);
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

    }

    /*****
     * @detail 重新恢复摄像头
     */
    public void resumeCamera() {

        if (surfaceView != null) {
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            if (hasSurface) {
                initCamera(surfaceHolder);
            } else {
                surfaceHolder.addCallback(this);
            }
            restartCerame();
        }
    }

    public void restartCerame() {
        useCamera = true;
        if (handler != null) {
            handler.restartPreviewAndDecode();
        }
    }

    private boolean useCamera = false;

    public void startScan() {
        CameraManager.init(this);
        mCameraManager = CameraManager.get();
//        mCameraManager = new CameraManager(this);
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        viewfinderView.setmCameraManager(mCameraManager);

        viewfinderView.setVisibility(View.VISIBLE);
        surfaceView.setVisibility(View.VISIBLE);
        useCamera = true;
        resumeCamera();
//        beepManager = new BeepManager(this, R.raw.beep);

    }

    /*****
     * @detail 关闭摄像头
     */
    public void closeCarmera() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        if (useCamera) {
            if (mCameraManager != null) {
                mCameraManager.closeDriver();

            } else {
                Log.i("gg", "Can't close driver ! CameraManager is null !");
            }
            useCamera = false;
        }

    }

    public Handler getHandler() {
        return handler;
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(arg0);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        hasSurface = false;
    }

}

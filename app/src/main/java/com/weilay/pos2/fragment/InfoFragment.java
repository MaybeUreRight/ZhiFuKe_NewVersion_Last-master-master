package com.weilay.pos2.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;
import com.weilay.pos2.BuildConfig;
import com.weilay.pos2.R;
import com.weilay.pos2.activity.DeviceInfoActivity;
import com.weilay.pos2.activity.LoginActivity;
import com.weilay.pos2.adapter.InfoAdapter;
import com.weilay.pos2.bean.AppBean;
import com.weilay.pos2.bean.FailBean;
import com.weilay.pos2.bean.PersonalCenterBean;
import com.weilay.pos2.http.ReqProgressCallBack;
import com.weilay.pos2.listener.OnItemclickListener;
import com.weilay.pos2.local.UrlDefine;
import com.weilay.pos2.util.ActivityStackControlUtil;
import com.weilay.pos2.util.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

/**
 * 我的 界面
 */
public class InfoFragment extends Fragment implements View.OnClickListener, OnItemclickListener {
    private Activity activity;
    private RecyclerView recyclerView;
    //    private ReqCallBack2<AppBean> reqCallBack;
    private ReqProgressCallBack<AppBean> reqCallBack;
    private Handler handler;

    private DownloadListener downloadAPKListener = new DownloadListener("download") {
        @Override
        public void onStart(Progress progress) {
            Log.i("Demo", "开始下载新版本APK");
        }

        @Override
        public void onProgress(Progress progress) {
            Log.i("","filePath = " + progress.filePath);
            Log.i("","fileName = " + progress.fileName);
            long currentSize = progress.currentSize;
            long totalSize = progress.totalSize;
            Log.i("Demo", "进度 = " + currentSize * 100 / totalSize);
        }

        @Override
        public void onError(Progress progress) {
            LogUtils.i("progress = " + progress.toString());
            DownloadTask download = OkDownload.getInstance().getTask("download");
            download.restart();
        }

        @Override
        public void onFinish(File file, Progress progress) {
            String path = UrlDefine.ROOT_PATH + file.getName();
            LogUtils.i("onFinish --> path  =  " + path);
            LogUtils.i("onFinish --> file.getPath()  =  " + file.getPath());
            LogUtils.i("onFinish --> progress.filePath  =  " + progress.filePath);
            LogUtils.i("onFinish --> progress.fileName  =  " + progress.fileName);
//            AppUtils2.installApp(path, BuildConfig.APPLICATION_ID + ".fileprovider");


            //创建Intent意图
            Intent intent=new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//启动新的activity
            //创建URI
            Uri uri;
            //判断版本是否是 7.0 及 7.0 以上
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(Objects.requireNonNull(getActivity()), BuildConfig.APPLICATION_ID + ".fileProvider", new File(path));
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                uri = Uri.fromFile(new File(path));
            }

            //设置Uri和类型
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            //执行安装
            startActivity(intent);



        }

        @Override
        public void onRemove(Progress progress) {
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    private void initView(View view) {
        handler = new Handler();
        View titleView = view.findViewById(R.id.fragment_info_container);
//        titleView.findViewById(R.id.title_back).setOnClickListener(this);
        titleView.findViewById(R.id.title_back).setVisibility(View.GONE);
        ((TextView) titleView.findViewById(R.id.title_title)).setText(getString(R.string.personal_center));

        view.findViewById(R.id.personalcenter_logout).setOnClickListener(this);
        recyclerView = view.findViewById(R.id.personalcenter_recyclerview);

        initRecyclerView();

        initReqCallBack();
    }

    private void initReqCallBack() {
        reqCallBack = new ReqProgressCallBack<AppBean>() {

            @Override
            public void onReqSuccess(final AppBean result) {
                LogUtils.i("onReqSuccess");
                int lastVersionCode = result.version_code;
                int currentVersionCode = BuildConfig.VERSION_CODE;
                if (lastVersionCode > currentVersionCode) {
                    //立即下载
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            GetRequest<File> request = OkGo.get(result.file_url);//.headers("", "").params("", "");
                            DownloadTask downloadTask = OkDownload.request("download", request)
                                    .priority(100)
                                    .save()
                                    .folder(UrlDefine.NEW_APK)
                                    .register(downloadAPKListener);
                            downloadTask.start();
                        }
                    }, UrlDefine.DOWNLOAD_NEW_APK);
                }
            }

            @Override
            public void onReqFailed(FailBean failBean) {
                LogUtils.i("onReqFailed");
                String message = failBean.message;
                LogUtils.i("message = \r\n" + message);
            }

            @Override
            public void onProgress(long total, long current) {

            }
        };
    }

    private void initRecyclerView() {

        ArrayList<PersonalCenterBean> list = new ArrayList<>();
        PersonalCenterBean bean1 = new PersonalCenterBean(R.drawable.personalcenter_deviceinfo, R.string.personal_center_deviceinfo, "");
        PersonalCenterBean bean2 = new PersonalCenterBean(R.drawable.personalcenter_update, R.string.personal_center_update, BuildConfig.VERSION_NAME);
        list.add(bean1);
        list.add(bean2);

        InfoAdapter adapter = new InfoAdapter(activity, this, list);
        recyclerView.setAdapter(adapter);


        //添加Android自带的分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.personalcenter_logout:
                //退出登录
                startActivity(new Intent(activity, LoginActivity.class));
                ActivityStackControlUtil.closeAll();
            default:

                break;
        }
    }

    @Override
    public void onIndexItemClick(int position) {
        switch (position) {
            case 0://设备信息
                startActivity(new Intent(activity, DeviceInfoActivity.class));
//                Toast.makeText(activity, R.string.personal_center_deviceinfo, Toast.LENGTH_SHORT).show();
                break;
            case 1://系统更新
//                HttpManager.app("", reqCallBack);

                String downloadUrl = "https://dldir1.qq.com/weixin/android/weixin672android1340.apk";
                GetRequest<File> request = OkGo.get(downloadUrl);//.headers("", "").params("", "");
                DownloadTask downloadTask = OkDownload.request("download", request)
                        .priority(100)
                        .save()
                        .folder(UrlDefine.NEW_APK)
                        .register(downloadAPKListener);
                downloadTask.start();


//                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//                View view = View.inflate(activity, R.layout.dialog_update, null);
//                ImageView dialog_update_close = view.findViewById(R.id.dialog_update_close);
//                Button dialog_update_confirm = view.findViewById(R.id.dialog_update_confirm);
//                builder.setView(view);
//                final AlertDialog dialog = builder.create();
//                dialog.setCancelable(true);
//                dialog.setCanceledOnTouchOutside(true);
//                dialog.show();
//
//                dialog_update_close.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//                dialog_update_confirm.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //更新
//                        HttpManager.app("", reqCallBack);
//                    }
//                });
                break;
        }
    }
}

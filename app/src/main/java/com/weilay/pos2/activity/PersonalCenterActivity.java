package com.weilay.pos2.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.weilay.pos2.BuildConfig;
import com.weilay.pos2.R;
import com.weilay.pos2.adapter.PersonalCenterAdapter;
import com.weilay.pos2.base.BaseActivity;
import com.weilay.pos2.bean.PersonalCenterBean;
import com.weilay.pos2.listener.OnItemclickListener;

import java.util.ArrayList;

/**
 * 个人中心 界面
 * @author: Administrator
 * @date: 2018/7/6/006
 * @description: $description$
 */
public class PersonalCenterActivity extends BaseActivity implements OnItemclickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalcenter);
        initView();
    }

    private void initView() {

        View titleView = findViewById(R.id.title_container);
        titleView.findViewById(R.id.title_back).setOnClickListener(this);
        ((TextView) titleView.findViewById(R.id.title_title)).setText(getString(R.string.personal_center));

        findViewById(R.id.personalcenter_logout).setOnClickListener(this);

        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.personalcenter_recyclerview);

        ArrayList<PersonalCenterBean> list = new ArrayList<>();
        PersonalCenterBean bean1 = new PersonalCenterBean(R.drawable.personalcenter_deviceinfo, R.string.personal_center_deviceinfo, "");
        PersonalCenterBean bean2 = new PersonalCenterBean(R.drawable.personalcenter_update, R.string.personal_center_update, BuildConfig.VERSION_NAME);
        list.add(bean1);
        list.add(bean2);

        PersonalCenterAdapter adapter = new PersonalCenterAdapter(this, list);
        recyclerView.setAdapter(adapter);


        //添加Android自带的分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.personalcenter_logout:
                //登出
                Toast.makeText(this, R.string.logout, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onIndexItemClick(int position) {
        switch (position) {
            case 0://设备信息

                Toast.makeText(this, R.string.personal_center_deviceinfo, Toast.LENGTH_SHORT).show();
                break;
            case 1://系统更新
//                Toast.makeText(this, R.string.personal_center_update, Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View view = View.inflate(this, R.layout.dialog_update, null);
                builder.setView(view);
                builder.create().show();
                break;
        }
    }
}

package com.weilay.pos2.activity;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.weilay.pos2.BuildConfig;
import com.weilay.pos2.PayApplication;
import com.weilay.pos2.R;
import com.weilay.pos2.base.BaseActivity;
import com.weilay.pos2.util.DeviceUtil;
import com.weilay.pos2.util.SPUtils;

/**
 * 设备信息 界面
 */
public class DeviceInfoActivity extends BaseActivity {
    private TextView device_info_code, device_info_type, device_info_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);

        initView();
    }

    private void initView() {
        View view = findViewById(R.id.device_info_title_container);
        view.findViewById(R.id.title_back).setOnClickListener(this);
        TextView title = view.findViewById(R.id.title_title);
        title.setText(R.string.device_info);

        device_info_code = findViewById(R.id.device_info_code);
        device_info_type = findViewById(R.id.device_info_type);
        device_info_version = findViewById(R.id.device_info_version);
        TextView device_info_code1 = findViewById(R.id.device_info_code1);
        TextView device_info_type1 = findViewById(R.id.device_info_type1);
        TextView device_info_version1 = findViewById(R.id.device_info_version1);

        String snCode = "" + DeviceUtil.getimei(PayApplication.application);
        device_info_code.setText(snCode);

        String type = "" + Build.VERSION.RELEASE;
        device_info_type.setText(type);

        String versionName = "" + BuildConfig.VERSION_NAME;
        device_info_version.setText(versionName);

        setFontWithMicrosoftYaHeiLight(device_info_code1, device_info_code
                , device_info_type1, device_info_type
                , device_info_version1, device_info_version);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }
}

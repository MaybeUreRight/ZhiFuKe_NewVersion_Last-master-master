package com.weilay.pos2.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.weilay.pos2.R;
import com.weilay.pos2.base.BaseActivity;

/**
 * 消息内容界面
 * //TODO 没有真实数据
 */
public class MessageContentActivity extends BaseActivity {
    private TextView mc_title, mc_autor, mc_date, mc_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_content);

        initView();
    }

    private void initView() {
        View view = findViewById(R.id.mc_titlecontainer);
        view.findViewById(R.id.title_back).setOnClickListener(this);
        TextView title = view.findViewById(R.id.title_title);
        title.setText(R.string.mc);


        mc_title = findViewById(R.id.mc_title);
        mc_autor = findViewById(R.id.mc_autor);
        mc_date = findViewById(R.id.mc_date);
        mc_content = findViewById(R.id.mc_content);

        setFontWithMicrosoftYaHeiLight(mc_title, mc_autor, mc_date, mc_content);
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

package com.weilay.pos2.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.weilay.pos2.R;
import com.weilay.pos2.base.BaseActivity;

/**
 * 支付成功界面
 */
public class SuccessActivity extends BaseActivity {

    private TextView pay_amount, pay_success_brew_detail, pay_success_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        initView();
    }

    private void initView() {

        View view = findViewById(R.id.success_titlecontaienr);
        view.findViewById(R.id.title_back).setOnClickListener(this);
        TextView title = view.findViewById(R.id.title_title);
        title.setText(R.string.verify);


        pay_amount = findViewById(R.id.pay_amount);
        pay_success_brew_detail = findViewById(R.id.pay_success_brew_detail);
        pay_success_done = findViewById(R.id.pay_success_done);

        pay_success_brew_detail.setOnClickListener(this);
        pay_success_done.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.pay_success_brew_detail:
                Toast.makeText(this, getString(R.string.brew_detail), Toast.LENGTH_SHORT).show();
                break;
            case R.id.pay_success_done:
                Toast.makeText(this, getString(R.string.pay_done), Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}

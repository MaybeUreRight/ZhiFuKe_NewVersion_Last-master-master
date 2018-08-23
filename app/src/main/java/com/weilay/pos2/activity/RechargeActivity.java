package com.weilay.pos2.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.weilay.pos2.R;
import com.weilay.pos2.adapter.RechargeAdapter;
import com.weilay.pos2.base.BaseActivity;
import com.weilay.pos2.bean.RechargeBean;
import com.weilay.pos2.listener.OnItemclickListener;

import java.util.ArrayList;

/**
 * 充值界面
 */
public class RechargeActivity extends BaseActivity implements OnItemclickListener{
    private ImageView rechargeScan;
    private EditText recharge_keyword;
    private Button recharge_search;
    private TextView rechargeNumber;
    private TextView rechargeMemBalance;
    private TextView rechargeDonorBalance;
    private TextView rechargeVipLevel;
    private TextView rechargeSum2;
    private TextView rechargeDonorSum;
    private RecyclerView rechargeDiscounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        initView();
    }

    private void initView() {
        View rechargeTitlecontainer = findViewById(R.id.recharge_titlecontainer);
        rechargeTitlecontainer.findViewById(R.id.title_back).setOnClickListener(this);
        ((TextView) rechargeTitlecontainer.findViewById(R.id.title_title)).setText(getString(R.string.recharge));


        TextView recharge_discounts1 = findViewById(R.id.recharge_discounts1);
        TextView recharge_donor_sum1 = findViewById(R.id.recharge_donor_sum1);
        TextView recharge_sum1 = findViewById(R.id.recharge_sum1);
        TextView recharge_vip_level1 = findViewById(R.id.recharge_vip_level1);
        TextView recharge_donor_balance1 = findViewById(R.id.recharge_donor_balance1);
        TextView recharge_mem_balance1 = findViewById(R.id.recharge_mem_balance1);
        TextView recharge_number1 = findViewById(R.id.recharge_number1);
        recharge_keyword = findViewById(R.id.recharge_keyword);
        rechargeScan = (ImageView) findViewById(R.id.recharge_scan);
        recharge_search = findViewById(R.id.recharge_search);
        recharge_search.setOnClickListener(this);
        rechargeNumber = (TextView) findViewById(R.id.recharge_number);
        rechargeMemBalance = (TextView) findViewById(R.id.recharge_mem_balance);
        rechargeDonorBalance = (TextView) findViewById(R.id.recharge_donor_balance);
        rechargeVipLevel = (TextView) findViewById(R.id.recharge_vip_level);
        rechargeSum2 = (TextView) findViewById(R.id.recharge_sum2);
        rechargeDonorSum = (TextView) findViewById(R.id.recharge_donor_sum);
        rechargeDiscounts = (RecyclerView) findViewById(R.id.recharge_discounts);

        setFontWithMicrosoftYaHeiLight(recharge_search, rechargeNumber, rechargeMemBalance
                , rechargeDonorBalance, rechargeVipLevel, rechargeSum2, rechargeDonorSum
                , recharge_keyword, recharge_number1, recharge_mem_balance1
                , recharge_donor_balance1, recharge_vip_level1, recharge_sum1
                , recharge_donor_sum1, recharge_discounts1);


        initRecyclerView();
    }

    private void initRecyclerView() {
        ArrayList<RechargeBean> list = new ArrayList<>();
        RechargeBean bean = new RechargeBean(getString(R.string.recharge_discount_example), -20);
        list.add(bean);

        RechargeAdapter adapter = new RechargeAdapter(this, list);
        rechargeDiscounts.setAdapter(adapter);
        rechargeDiscounts.setLayoutManager(new GridLayoutManager(this, 4));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.recharge_search:
                break;
        }
    }

    @Override
    public void onIndexItemClick(int position) {

    }
}


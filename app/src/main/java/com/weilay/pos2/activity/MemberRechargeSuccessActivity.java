package com.weilay.pos2.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weilay.pos2.R;
import com.weilay.pos2.base.BaseActivity;

/**
 * 会员充值成功界面
 */
public class MemberRechargeSuccessActivity extends BaseActivity {
    private TextView payAmount, mrsContentGood, mrsContentMerchant, mrsContentState, mrsContentDealTime, mrsContentWayOfPay, mrsContentNumberOfDeal, mrsContentNumberOfMerchant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_recharge_success);


        initView();
    }

    private void initView() {
        View view = findViewById(R.id.mrs_titlecontaienr);
        view.findViewById(R.id.title_back).setOnClickListener(this);
        TextView title = view.findViewById(R.id.title_title);
        title.setText(R.string.recharge);


        RelativeLayout mrsSuccessContainer = findViewById(R.id.mrs_success_container);
        TextView mrsSuccessTv = mrsSuccessContainer.findViewById(R.id.mrs_success_tv);
        LinearLayout payAmountContainer = mrsSuccessContainer.findViewById(R.id.pay_amount_container);
        payAmount = payAmountContainer.findViewById(R.id.pay_amount);
        TextView payAmountUnit = payAmountContainer.findViewById(R.id.pay_amount_unit);

        View mrsContent = mrsSuccessContainer.findViewById(R.id.mrs_content);

        TextView mrsContentGoodTv = mrsContent.findViewById(R.id.mrs_content_good_tv);
        mrsContentGood = mrsContent.findViewById(R.id.mrs_content_good);
        TextView mrsContentMerchantTv = mrsContent.findViewById(R.id.mrs_content_merchant_tv);
        mrsContentMerchant = mrsContent.findViewById(R.id.mrs_content_merchant);
        TextView mrsContentStateTv = mrsContent.findViewById(R.id.mrs_content_state_tv);
        mrsContentState = mrsContent.findViewById(R.id.mrs_content_state);
        TextView mrsContentDealTimeTv = mrsContent.findViewById(R.id.mrs_content_deal_time_tv);
        mrsContentDealTime = mrsContent.findViewById(R.id.mrs_content_deal_time);
        TextView mrsContentWayOfPayTv = mrsContent.findViewById(R.id.mrs_content_way_of_pay_tv);
        mrsContentWayOfPay = mrsContent.findViewById(R.id.mrs_content_way_of_pay);
        TextView mrsContentNumberOfDealTv = mrsContent.findViewById(R.id.mrs_content_number_of_deal_tv);
        mrsContentNumberOfDeal = mrsContent.findViewById(R.id.mrs_content_number_of_deal);
        TextView mrsContentNumberOfMerchantTv = mrsContent.findViewById(R.id.mrs_content_number_of_merchant_tv);
        mrsContentNumberOfMerchant = mrsContent.findViewById(R.id.mrs_content_number_of_merchant);


        TextView mrsSuccessDone = findViewById(R.id.mrs_success_done);
        mrsSuccessDone.setOnClickListener(this);

        setFontWithMicrosoftYaHei(payAmount, mrsSuccessTv, payAmountUnit);
        setFontWithMicrosoftYaHeiLight(mrsSuccessDone, mrsContentGood, mrsContentMerchant
                , mrsContentState, mrsContentDealTime, mrsContentWayOfPay
                , mrsContentNumberOfDeal, mrsContentNumberOfMerchant
                , mrsContentGoodTv, mrsContentMerchantTv, mrsContentStateTv
                , mrsContentDealTimeTv, mrsContentWayOfPayTv
                , mrsContentNumberOfDealTv, mrsContentNumberOfMerchantTv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.mrs_success_done:
                onBackPressed();
                break;

            default:
                break;
        }
    }
}
package com.weilay.pos2.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.weilay.pos2.R;
import com.weilay.pos2.base.BaseActivity;

/**
 * 订单信息 界面
 */
public class OrderInfoActivity extends BaseActivity {
    private TextView oiPayAmount, mrsContentGood, mrsContentMerchant, mrsContentState, mrsContentDealTime, mrsContentWayOfPay, mrsContentNumberOfDeal, mrsContentNumberOfMerchant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);

        initView();
    }

    private void initView() {
        View oiTitlecontainer = findViewById(R.id.oi_titlecontainer);
        oiTitlecontainer.findViewById(R.id.title_back).setOnClickListener(this);
        TextView title = oiTitlecontainer.findViewById(R.id.title_title);
        title.setText(R.string.order_info);

        TextView oiPayAmountTv = findViewById(R.id.oi_pay_amount_tv);
        oiPayAmount = (TextView) findViewById(R.id.oi_pay_amount);
        TextView oiUnitTwo = (TextView) findViewById(R.id.oi_unit_two);

        View mrsContent = findViewById(R.id.oi_content);
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

        TextView oi_refund = findViewById(R.id.oi_refund);
        oi_refund.setOnClickListener(this);

        setFontWithMicrosoftYaHei(oiPayAmount, oiUnitTwo);
        setFontWithMicrosoftYaHeiLight(oiPayAmountTv, mrsContentMerchant, mrsContentGood
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
            case R.id.oi_refund:
                //TODO 退款
                break;
            default:
                break;
        }
    }
}

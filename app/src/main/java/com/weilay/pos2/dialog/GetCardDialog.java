package com.weilay.pos2.dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.weilay.pos2.R;
import com.weilay.pos2.base.BaseDialogFragment;
import com.weilay.pos2.bean.CardTypeEnum;
import com.weilay.pos2.bean.CouponEntity;
import com.weilay.pos2.db.CouponDBHelper;
import com.weilay.pos2.listener.TaskReceiverListener;

/******
 * @detail 使用优惠卡券
 * @author rxwu
 *
 */
public class GetCardDialog extends BaseDialogFragment implements OnClickListener {

    public GetCardDialog() {
    }

//    public GetCardDialog(CouponEntity coupon) {
//        this.coupon = coupon;
//        // 初始化付款的信息
//    }


    public void setCoupon(CouponEntity coupon) {
        this.coupon = coupon;
    }

    CouponEntity coupon = null;

    private TaskReceiverListener taskReceiverListener = new TaskReceiverListener() {

        @Override
        public void cancel() {

        }

        @Override
        public void receiver() {

        }
    };

    public void setTaskReceiverListener(TaskReceiverListener taskReceiverListener) {
        if (taskReceiverListener != null) {
            this.taskReceiverListener = taskReceiverListener;
        }
    }

    //	Intent intent = null;
    private Button okBtn, cancelBtn;
    private TextView cardTypeTv, cardNoTv, cardInfoTv, cardDateTv, midNameTv, closeBtn;

    @Override
    public View initViews(LayoutInflater inflater, ViewGroup container) {
        mRootView = inflater.inflate(R.layout.dialog_get_card, container);
        // ((TextView)findViewById(R.id.common_head_tv)).setText("会员充值");
//		intent = new Intent(getActivity(), PaySelectActivity2.class);
        okBtn = getViewById(R.id.btn_sure);
        cancelBtn = getViewById(R.id.btn_cancel);
        midNameTv = getViewById(R.id.mid_name);
        cardTypeTv = getViewById(R.id.card_type);
        cardNoTv = getViewById(R.id.card_no);
        cardDateTv = getViewById(R.id.card_date);
        cardInfoTv = getViewById(R.id.card_info);
        closeBtn = getViewById(R.id.btn_close);

        return mRootView;
    }

    @Override
    public void initDatas() {
        cardNoTv.setText(coupon.getInfo());
        cardDateTv.setText(coupon.getDeadline() == null ? "" : coupon.getDeadline());
        cardInfoTv.setText(coupon.getNotice() == null ? "" : coupon.getNotice());
        cardTypeTv.setText(CardTypeEnum.getTypeName(coupon.getType()));
        midNameTv.setText(coupon.getMerchantname() == null ? "" : coupon.getMerchantname());
    }

    @Override
    public void initEvents() {
        okBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        closeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.btn_sure:
                // 确认接收任务，将卡券任务保存到卡券中心中
                CouponDBHelper.saveCoupons(coupon);
                taskReceiverListener.receiver();
                dismiss();// 关闭弹窗
                break;
            case R.id.btn_cancel:
                dismiss();
                taskReceiverListener.cancel();
                break;
            default:
                dismiss();
                break;
        }
    }
}

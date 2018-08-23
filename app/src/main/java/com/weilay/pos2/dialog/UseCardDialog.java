package com.weilay.pos2.dialog;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.weilay.pos2.R;
import com.weilay.pos2.activity.PaySelectActivity2;
import com.weilay.pos2.bean.CardTypeEnum;
import com.weilay.pos2.bean.CouponEntity;
import com.weilay.pos2.bean.PayTypeEntity;
import com.weilay.pos2.bean.PosDefine;
import com.weilay.pos2.listener.CardUseListener;
import com.weilay.pos2.util.ConvertUtil;
import com.weilay.pos2.util.T;
import com.weilay.pos2.util.Utils;


/******
 * @detail 使用优惠卡券
 * @author rxwu
 *
 */
public class UseCardDialog extends BaseDialogFragment implements OnClickListener {
    CouponEntity coupon = null;
    Intent intent = null;
    private Button useBtn, cancelBtn;
    private TextView cardTypeTv, cardNoTv, cardInfoTv, cardDateTv;
    private TextView afterUseTv, beforeUseTv;
    private double araAmount;
    private String tx_no;
    private PayTypeEntity mPay = null;

    private CardUseListener cardUseListener;

    public void setCardUseListener(CardUseListener cardUseListener) {
        this.cardUseListener = cardUseListener;
    }

    public UseCardDialog(CouponEntity coupon, PayTypeEntity pay) {
        this.coupon = coupon;
        // 初始化付款的信息
        mPay = pay;
        //获取应付金额
        araAmount = mPay.getAraamount();
    }

    @Override
    public View initViews(LayoutInflater inflater, ViewGroup container) {
        mRootView = inflater.inflate(R.layout.dialog_use_card, container);
        intent = new Intent(getActivity(), PaySelectActivity2.class);
        useBtn = getViewById(R.id.btn_sure);
        cancelBtn = getViewById(R.id.btn_cancel);
        cardTypeTv = getViewById(R.id.card_type);
        cardNoTv = getViewById(R.id.card_no);
        cardDateTv = getViewById(R.id.card_date);
        cardInfoTv = getViewById(R.id.card_info);
        afterUseTv = getViewById(R.id.after_use_tv);
        beforeUseTv = getViewById(R.id.before_use_tv);
        return mRootView;
    }

    @Override
    public void initDatas() {
        if (coupon == null) {
            intent.putExtra(PosDefine.INTENTE_PAY_INFO, mPay);
            startActivity(intent);
            getActivity().finish();
            return;
        }
        // 计算折扣后的金额
        cardNoTv.setText(coupon.getInfo());
        cardDateTv.setText(coupon.getDate() == null ? "" : coupon.getDate());
        cardInfoTv.setText(coupon.getNotice() == null ? "" : coupon.getNotice());
        cardTypeTv.setText(CardTypeEnum.getTypeName(coupon.getType()));
        useCard();
    }

    @Override
    public void initEvents() {
        // TODO Auto-generated method stub
        useBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    double couponDiscountAmount = 0;
    double couponDiscount = 0;

    /******
     * @detail 使用卡券
     */
    public void useCard() {
        double afteramount = 0;
        switch (coupon.getType()) {
            case CardTypeEnum.CASH:
            case CardTypeEnum.FRIEND_CASH:
                couponDiscountAmount = ConvertUtil.branchToYuan(coupon.getAmount());
                couponDiscount = 10;
                afteramount = ConvertUtil.getMoney(araAmount - couponDiscountAmount);
                afteramount = afteramount > 0 ? afteramount : 0;
                //	araAmount = araAmount > 0 ? araAmount : 0;
                break;
            case CardTypeEnum.DISCOUNT:
                afteramount = ConvertUtil.getMoney(araAmount * (coupon.getAmount()));// 直接乘以折扣
                afteramount = afteramount > 0 ? afteramount : 0;
                couponDiscountAmount = araAmount - afteramount;
                //	araAmount = araAmount > 0 ? araAmount : 0;
                //couponDiscountAmount = ConvertUtil.getMoney(payAmount - araAmount);
                couponDiscount = coupon.getAmount();// 会员的折扣
			/*payType.setCouponDiscountAmount(couponDiscountAmount);
			payType.setCouponDiscount(couponDiscount);
			payType.setCouponType(coupon.getType());*/
                break;
            default:
                break;
        }
        beforeUseTv.setText(araAmount + "元");
        afterUseTv.setText(afteramount + "元");

    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.btn_sure:
                Utils.cardConsume(coupon
                        , mPay
                        , couponDiscountAmount
                        , araAmount
                        , couponDiscount
                        , ""
                        , new CardUseListener() {
                            @Override
                            public void success(PayTypeEntity result) {
                                // 确认卡券优惠信息

                                // 确认使用卡券
                                intent.putExtra(PosDefine.INTENTE_PAY_INFO, result);
                                if (cardUseListener != null) {
                                    cardUseListener.success(result);
                                    dismiss();
                                } else {
                                    startActivity(intent);
                                    dismiss();
                                    mContext.finish();
                                }

                            }

                            @Override
                            public void failed(String msg) {
                                if (cardUseListener != null) {
                                    cardUseListener.failed(msg);
                                } else {
                                    T.showCenter("使用失败" + msg);
                                }
                                dismiss();
                            }
                        });
                break;
            default:
                dismiss();
                break;
        }
    }

}

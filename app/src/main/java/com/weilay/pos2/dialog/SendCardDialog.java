package com.weilay.pos2.dialog;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.weilay.pos2.R;
import com.weilay.pos2.bean.CouponEntity;
import com.weilay.pos2.listener.DialogAskListener;
import com.weilay.pos2.listener.DialogConfirmListener;
import com.weilay.pos2.listener.OnDataListener;
import com.weilay.pos2.local.Config;
import com.weilay.pos2.util.ConvertUtil;
import com.weilay.pos2.util.DialogAsk;
import com.weilay.pos2.util.QRCodeUtil;
import com.weilay.pos2.util.T;
import com.weilay.pos2.util.Utils;
import com.weilay.pos2.view.DialogConfirm;

/*******
 * @detail 发送卡券的弹窗
 * @author rxwu
 *
 */
public class SendCardDialog extends BaseDialogFragment implements OnClickListener {
    public void setCoupon(CouponEntity coupon) {
        if (coupon == null) {
            T.showCenter("获取不到卡券的信息");
            dismiss();
            return;
        }
        this.coupon = coupon;
    }

    private CouponEntity coupon;

    public SendCardDialog() {

    }

    // 发券商户名称，卡券的标题、卡券的卡号、有效期、卡券使用须知、优惠说明、奖励说明
    private TextView midNameTv, couponTitleTv, cardstockTv, deadlineTv, cardInfoTv, cardPreferTv, cardMissionTv;
    private ImageView cardCodeIv;// 发券二维码
    private Button cancelBtn, sureBtn;

    @Override
    public View initViews(LayoutInflater inflater, ViewGroup container) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.dialog_send_card, container);
        }
        midNameTv = getViewById(R.id.mid_name_tv);
        couponTitleTv = getViewById(R.id.card_title_tv);
        cardstockTv = getViewById(R.id.card_stock_tv);
        cardInfoTv = getViewById(R.id.card_info_tv);
        deadlineTv = getViewById(R.id.card_date_title);
        cardPreferTv = getViewById(R.id.card_prefer_tv);
        cardMissionTv = getViewById(R.id.card_mission_tv);
        cardCodeIv = getViewById(R.id.coupon_code_iv);
        cancelBtn = getViewById(R.id.btn_cancel);
        sureBtn = getViewById(R.id.btn_sure);
        return mRootView;
    }

    @Override
    public void initDatas() {
        initCard(coupon);
        midNameTv.setText(coupon.getMerchantname());
        couponTitleTv.setText("卡券名称：" + coupon.getTitle());
        cardstockTv.setText("库存：" + coupon.getStock());
        deadlineTv.setText("有效日期：" + coupon.getDeadline());
        cardInfoTv.setText(coupon.getInfo());
        cardPreferTv.setText(coupon.getNotice());
        cardMissionTv.setText("任务奖励：每领一张可获得" + ConvertUtil.getMoney(coupon.getMerchantcommission() / 100) + "元奖励");
    }

    @Override
    public void initEvents() {
        sureBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_sure:
                if (coupon.getStock() >= 0) {
                    DialogAsk.ask(mContext, "发券提示", "是否确认发券", "确定", "取消", new DialogAskListener() {

                        @Override
                        public void okClick(DialogInterface dialog) {
//						PrintOrderData.printCoupon(coupon);
                        }

                        @Override
                        public void cancelClick(DialogInterface dialog) {
                            // dialog.dismiss();
                        }
                    });
                } else {
                    DialogConfirm.ask(mContext, "卡券使用提示", "抱歉，卡券库存不足", "确定", null);
                }
                break;
            default:
                break;
        }

    }

    /*******
     * @detail 读取卡券的二维码
     */
    private void loadCard() {
        if (coupon.getStock() >= 0) {
            new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... arg0) {
                    return QRCodeUtil.createQRImage(coupon.getUrl2qrcode(), 400, 400, null, null);
                }

                @Override
                protected void onPostExecute(Bitmap result) {
                    super.onPostExecute(result);
                    if (result != null) {
                        cardCodeIv.setImageBitmap(result);
                        cardCodeIv.setScaleType(ScaleType.FIT_XY);
                    } else {
                        cardCodeIv.setImageResource(Config.LOGO_RES);
                    }
                }
            }.execute();
        } else {
            DialogConfirm.ask(mContext, "卡券使用提示", "抱歉，卡券库存不足", "确定", null);
        }
    }

    /*****
     * @detail 发卡券列表
     * @param coupon
     */
    private void initCard(final CouponEntity coupon) {
        if (coupon == null) {
            return;
        }
        // TODO PRINTER
        mContext.showLoading("正在获取卡券信息");
        Utils.getCouponQRCode(coupon.getId(), new OnDataListener() {

            @Override
            public void onFailed(String msg) {
                mContext.stopLoading();
                DialogConfirm.ask(mContext, "获取卡券信息提示", "获取卡券信息失败", "确定", new DialogConfirmListener() {

                    @Override
                    public void okClick(DialogInterface dialog) {
                        dismiss();
                    }
                });
            }

            @Override
            public void onData(Object obj) {
                mContext.stopLoading();
                String code = obj.toString();
                coupon.setUrl2qrcode(code);
                loadCard();
            }
        });
    }

}

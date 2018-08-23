package com.weilay.pos2.activity;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.zxing.WriterException;
import com.weilay.pos2.R;
import com.weilay.pos2.base.BaseActivity;
import com.weilay.pos2.bean.SendTicketInfo;
import com.weilay.pos2.local.UrlDefine;
import com.weilay.pos2.util.EncodingHandler;
import com.weilay.pos2.util.LogUtils;
import com.weilay.pos2.util.T;

/**
 * 发券界面
 */
public class SendTicketBeginAvtivity extends BaseActivity {
    private ImageView logo_iv, ticket_qrcode_iv;
    private TextView merchantName_tv, date_tv, finish_tv,
            sendticket_stock;
    private SendTicketInfo sti;
    private Dialog ticketBegin_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentLayout(R.layout.sendticketbegin_layout);
//        setContentLayout(R.layout.sendticketbegin_layout_new);
        setContentView(R.layout.sendticketbegin_layout_new);
//        setTitle("发券");
        sti = getIntent().getParcelableExtra("sti");
        LogUtils.i("\tSendTicketBeginAvtivity --> \n\tsti  = \n\t" + sti);
        if (sti == null || TextUtils.isEmpty(sti.getUrl2qrcode())) {
            T.showCenter("获取不到卡券信息");
            finish();
            return;
        }
        init();
        initDatas();
        reg();
    }

    private void init() {
        View titleContainer = findViewById(R.id.stb_titlecontainer);
        titleContainer.findViewById(R.id.title_back).setOnClickListener(this);
        TextView title = titleContainer.findViewById(R.id.title_title);
        title.setText(R.string.index_item_2);


        logo_iv = (ImageView) findViewById(R.id.merchantlogo_iv);
        merchantName_tv = (TextView) findViewById(R.id.merchantname_tv);
        date_tv = (TextView) findViewById(R.id.sendticket_deadline);
        ticket_qrcode_iv = (ImageView) findViewById(R.id.ticket_qrcode);
        finish_tv = (TextView) findViewById(R.id.sendticket_finish);
        finish_tv.setText("发券");
        sendticket_stock = (TextView) findViewById(R.id.sendticket_stock);
        ticketBegin_dialog = new Dialog(SendTicketBeginAvtivity.this,
                android.R.style.Animation);
        ticketBegin_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ticketBegin_dialog.setContentView(R.layout.sendticket_cardqr_layout);

        setFontWithMicrosoftYaHei(merchantName_tv);
        setFontWithMicrosoftYaHeiLight(date_tv, sendticket_stock);
    }

    private void initDatas() {
        createQR(sti.getUrl2qrcode(), ticket_qrcode_iv);
        finish_tv.setText("发券");
        finish_tv.setEnabled(true);
    }

    private void reg() {

//        Glide.with(SendTicketBeginAvtivity.this).load(UrlDefine.BASE_URL + sti.getMerchantlogo()).into(logo_iv);
        date_tv.setText(sti.getDeadline());
        String stock = "数量：" + sti.getStock() + "张";
        sendticket_stock.setText(stock);
        createQR(sti.getUrl2qrcode(), ticket_qrcode_iv);

        Glide.with(this).asBitmap().load(UrlDefine.BASE_URL + sti.getMerchantlogo()).into(new BitmapImageViewTarget(logo_iv) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(SendTicketBeginAvtivity.this.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                logo_iv.setImageDrawable(circularBitmapDrawable);
            }
        });

        merchantName_tv.setText(sti.getCardinfo());

        finish_tv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//				USBComPort usbComPort = new USBComPort();
//				if (usbComPort.SendTicket(sti)) {
//
//				} else {
//					T.showCenter("打印失败!找不到打印机.");
//				}

            }
        });
    }

    private void createQR(String QRurl, ImageView iv) {
        // 生成二维码图片，第一个参数是二维码的内容，第二个参数是正方形图片的边长，单位是像素
        Bitmap qrcodeBitmap;
        try {
            qrcodeBitmap = EncodingHandler.createQRCode(QRurl, 700);
            iv.setImageBitmap(qrcodeBitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
        }
    }
}

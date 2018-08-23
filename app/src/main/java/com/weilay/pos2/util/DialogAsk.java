package com.weilay.pos2.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.weilay.pos2.R;
import com.weilay.pos2.listener.DialogAskListener;


/**
 * Created by  rxwu on 2016/3/20 0020.
 * <p/>
 * Email：1158577255@qq.com
 * <p/>
 * Detail
 */
public class DialogAsk {
    private static AlertDialog.Builder builder;
    private static String mTitle="操作提示?",mOkBtn="确定",mCancelBtn="取消",mContent="确定要进行此操作吗？";

    protected DialogAsk(){

    }
    public static void ask(Activity context, String title,String content, String okBtn, String cancelBtn, final DialogAskListener listener){
        if(title!=null){
            mTitle=title;
        }
        if(okBtn!=null){
            mOkBtn=okBtn;
        }
        if(cancelBtn!=null){
            mCancelBtn=cancelBtn;
        }
        if(content!=null){
            mContent=content;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if(context==null || context.isFinishing() || context.isDestroyed()){
                return;
            }
        }
        View view =LayoutInflater.from(context).inflate(R.layout.dialog_ask,null);
        builder=new AlertDialog.Builder(context);

        final AlertDialog dialog=builder.create();
        ImageView cancel=(ImageView)view.findViewById(R.id.dialog_close);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if(listener!=null){
                    listener.cancelClick(dialog);
                }*/
                dialog.dismiss();
            }
        });
        TextView titleTv=(TextView)view.findViewById(R.id.dialog_ask_title);
        Button positiveBtn=(Button)view.findViewById(R.id.dialog_ask_sure);
        Button negativeBtn=(Button)view.findViewById(R.id.dialog_ask_cancel);
        TextView contentTv=(TextView)view.findViewById(R.id.dialog_ask_content);
        titleTv.setText(mTitle);
        positiveBtn.setText(mOkBtn);
        negativeBtn.setText(mCancelBtn);
        contentTv.setText(mContent);
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	  if(listener!=null)
                listener.okClick(dialog);
                dialog.dismiss();
            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            	  if(listener!=null)
                listener.cancelClick(dialog);
                dialog.dismiss();
            }
        });
        dialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
        dialog.show();
        dialog.getWindow().setContentView(view);
        //一定得在show完dialog后来set属性
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = 450;
        dialog.getWindow().setAttributes(lp);
    }


}

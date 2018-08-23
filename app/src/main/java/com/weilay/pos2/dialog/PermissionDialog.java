package com.weilay.pos2.dialog;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.weilay.pos2.R;
import com.weilay.pos2.base.BaseDialogFragment;
import com.weilay.pos2.bean.RechageLockEntity;
import com.weilay.pos2.listener.OnDataListener;
import com.weilay.pos2.util.EncodingHandler;
import com.weilay.pos2.util.T;
import com.weilay.pos2.util.Utils;

import java.util.Timer;
import java.util.TimerTask;

public class PermissionDialog extends BaseDialogFragment {
	private ImageView scanIv;//扫描的二维码
	private TextView closeBtn;
	private RechageLockEntity mLock;
	private boolean mPermission;
	@Override
	public View initViews(LayoutInflater inflater, ViewGroup container) {
		if(mRootView==null){
			mRootView=inflater.inflate(R.layout.dialog_permission,container,false);
		}
		scanIv=getViewById(R.id.scan_img);
		closeBtn=getViewById(R.id.btn_close);
		return mRootView;
	}

	@Override
	public void initDatas() {
		// 请求验证权限的二维码
		Utils.getRechargeLock(new OnDataListener<RechageLockEntity>() {
			
			@Override
			public void onFailed(String msg) {
				T.showCenter(msg);
				getActivity().finish();
			}
			
			@Override
			public void onData(RechageLockEntity obj) {
				mLock=obj;
				new CreateQRTask().execute(obj);
			}
		});
	}

	@Override
	public void initEvents() {
		closeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dismiss();
				getActivity().finish();
			}
		});
		scanIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dismiss();
				getActivity().finish();
			}
		});
	}
	
	/*****
	 * @Detail 创建
	 * File Name:PermissionDialog.java
	 * Package:com.weilay.dialog	
	 * Date: 2017年2月24日下午2:54:20
	 * Author: rxwu
	 * Detail:createQRTask
	 */
	class CreateQRTask extends AsyncTask<RechageLockEntity, Void,Bitmap>{

		@Override
		protected Bitmap doInBackground(RechageLockEntity... arg0) {
			try {
				if(arg0!=null){
					Bitmap qrcodeBitmap = EncodingHandler.createQRCode(arg0[0].getUrl(), 700);
					return qrcodeBitmap;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if(result!=null){
				scanIv.setImageBitmap(result);
				startQuery();
			}else{
				//失败
				T.showCenter("无法获取会员充值信息");
				getActivity().finish();
			}
			
		}
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		stopQuery();
	}
	
	public void startQuery(){
		timer=new Timer();
		timer.scheduleAtFixedRate(queryLock, 100, 3*1000);//每三秒查询一次
	}
	public void stopQuery(){
		if(timer!=null){
			timer.cancel();
			timer=null;
		}
	}
	private Timer timer;
	private TimerTask queryLock=new TimerTask() {
		
		@Override
		public void run() {
			Utils.queryRechageLock(mLock, new OnDataListener<Integer>() {

				@Override
				public void onData(Integer obj) {
					stopQuery();
					//验证成功
					dismiss();
				}

				@Override
				public void onFailed(int code,String msg) {
					if(code==1000)
					{
						//未扫描二维码
					}else{
						T.showCenter(msg);
						stopQuery();
						getActivity().finish();
					}
				}
		});
		}
	};
	
}

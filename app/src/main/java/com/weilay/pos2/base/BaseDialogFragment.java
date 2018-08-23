package com.weilay.pos2.base;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;


/******
 * @detail 弹窗
 * @author Administrator
 *
 */
public abstract class BaseDialogFragment extends DialogFragment {
	public View mRootView;
	public BaseActivity mContext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(new
				ColorDrawable(Color.TRANSPARENT));
		setCancelable(false);
		mContext = (BaseActivity) getActivity();
		View view = initViews(inflater, container);
		initDatas();
		initEvents();
		return view;
	}

	public abstract View initViews(LayoutInflater inflater, ViewGroup container);

	public abstract void initDatas();

	public abstract void initEvents();

	@SuppressWarnings("unchecked")
	public <T extends View> T getViewById(int id) {
		return (T) mRootView.findViewById(id);
	}
}

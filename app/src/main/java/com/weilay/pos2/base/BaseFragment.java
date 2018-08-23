package com.weilay.pos2.base;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.lang.reflect.Field;

public abstract class BaseFragment extends Fragment {
	public View mRootView;
	public BaseActivity mContext;
	private boolean reload = false;

	public void setReload(boolean reload) {
		this.reload = reload;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext = (BaseActivity) getActivity();
		View view = initViews(inflater, container);
		initDatas();
		initEvents();
		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	public abstract View initViews(LayoutInflater inflater, ViewGroup container);

	public abstract void initDatas();

	public abstract void initEvents();

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden && reload) {
			initDatas();
		}
		super.onHiddenChanged(hidden);
	}

	/**********
	 * @detail 取消网络请求的时候的调用函数，由继承基本片段方法的子类重写方便处理界面的UI
	 */
	public void cancelHttpRequest() {

	}

	public <T extends View> T getViewById(int id) {
		return (T) mRootView.findViewById(id);
	}

	/*******
	 * @detail 重写onDetach,避免发生activity has been destoryed 错误
	 */
	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}


	protected void setFontWithMicrosoftYaHeiLight(TextView... textViews) {
		Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "MicrosoftYaHeiLight.ttf");
		for (int i = 0; i < textViews.length; i++) {
			textViews[i].setTypeface(tf);
		}
	}

	protected void setFontWithMicrosoftYaHei(TextView... textViews) {
		Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "MicrosoftYaHei.ttc");
		for (int i = 0; i < textViews.length; i++) {
			textViews[i].setTypeface(tf);
		}
	}
}

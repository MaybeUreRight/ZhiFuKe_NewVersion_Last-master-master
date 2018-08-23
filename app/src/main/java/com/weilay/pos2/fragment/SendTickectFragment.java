package com.weilay.pos2.fragment;

import android.support.v4.app.Fragment;


import com.weilay.pos2.base.BaseFragment;

import java.lang.reflect.Field;

/******
 * @detail 片段基类抽象
 * @author rxwu
 * @date 2016/08/22
 *
 */
public abstract class SendTickectFragment extends BaseFragment {

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
	
	
}

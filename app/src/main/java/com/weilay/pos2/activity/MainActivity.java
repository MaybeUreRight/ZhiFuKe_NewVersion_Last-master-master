package com.weilay.pos2.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.weilay.pos2.R;
import com.weilay.pos2.base.BaseActivity;
import com.weilay.pos2.fragment.InfoFragment;
import com.weilay.pos2.fragment.MainFragment;
import com.weilay.pos2.fragment.MessageFragment;

import java.util.HashMap;


import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;

/**
 * 主页面
 *
 * @author: Administrator
 * @date: 2018/7/4/004
 * @description: $description$
 */
public class MainActivity extends BaseActivity {
    private LinearLayout navigator_index, navigator_message, navigator_mine;
    private ImageView navigator_index_iv, navigator_message_iv, navigator_mine_iv;
    /**
     * 第一个fragment
     */
    public static final int PAGE_COMMON = 0;
    /**
     * 第二个fragment
     */
    public static final int PAGE_TRANSLUCENT = 1;
    /**
     * 第三个fragment
     */
    public static final int PAGE_COORDINATOR = 2;

    /**
     * 管理fragment
     */
    private HashMap<Integer, Fragment> fragments = new HashMap<>();

    //当前activity的fragment控件
    private int fragmentContentId = R.id.main_content;

    /**
     * 设置默认的fragment
     */
    private int currentTab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFrag();
        initView();

        // 设置默认的Fragment
        defaultFragment();

        changeImage(0);
    }

    private void initView() {

        LinearLayout bottomContainer = findViewById(R.id.bottom_container);
        navigator_index = bottomContainer.findViewById(R.id.navigator_index);
        navigator_index_iv = navigator_index.findViewById(R.id.navigator_index_iv);

        navigator_message = bottomContainer.findViewById(R.id.navigator_message);
        navigator_message_iv = navigator_message.findViewById(R.id.navigator_message_iv);

        navigator_mine = bottomContainer.findViewById(R.id.navigator_mine);
        navigator_mine_iv = navigator_mine.findViewById(R.id.navigator_mine_iv);

        navigator_index.setOnClickListener(this);
        navigator_message.setOnClickListener(this);
        navigator_mine.setOnClickListener(this);
    }

    private void initFrag() {
        fragments.put(PAGE_COMMON, new MainFragment());
        fragments.put(PAGE_TRANSLUCENT, new MessageFragment());
        fragments.put(PAGE_COORDINATOR, new InfoFragment());
    }

    private void defaultFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(fragmentContentId, fragments.get(PAGE_COMMON));
        currentTab = PAGE_COMMON;
        ft.commit();
    }

    /**
     * 点击切换下部按钮
     *
     * @param page
     */
    private void changeTab(int page) {
        //默认的currentTab == 当前的页码，不做任何处理
        if (currentTab == page) {
            return;
        }

        //获取fragment的页码
        Fragment fragment = fragments.get(page);
        //fragment事务
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //如果该Fragment对象被添加到了它的Activity中，那么它返回true，否则返回false。
        //当前activity中添加的不是这个fragment
        if (!fragment.isAdded()) {
            //所以将他加进去
            ft.add(fragmentContentId, fragment);
        }
        //隐藏当前currentTab的
        ft.hide(fragments.get(currentTab));
        //显示现在page的
        ft.show(fragments.get(page));
        //设置当前currentTab底部的状态
        changeImage(currentTab);
        //当前显示的赋值给currentTab
        currentTab = page;
        //设置当前currentTab底部的状态
        changeImage(currentTab);
        //activity被销毁？  ！否
        if (!this.isFinishing()) {
            //允许状态丢失
            ft.commitAllowingStateLoss();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.navigator_index://首页
                if (currentTab == 0) {
                    break;
                }
                changeTab(0);
                break;

            case R.id.navigator_message://消息
                if (currentTab == 1) {
                    break;
                }
                changeTab(1);

                break;

            case R.id.navigator_mine://我的
                if (currentTab == 2) {
                    break;
                }
                changeTab(2);
                break;
            default:
                break;
        }
    }

    private void changeImage(int currentPosition) {
        switch (currentPosition) {
            case 0:
                navigator_index_iv.setImageResource(R.drawable.index_selected);
                navigator_message_iv.setImageResource(R.drawable.message_unselected);
                navigator_mine_iv.setImageResource(R.drawable.mine_unselected);
                break;
            case 1:
                navigator_index_iv.setImageResource(R.drawable.index_unselected);
                navigator_message_iv.setImageResource(R.drawable.message_selected);
                navigator_mine_iv.setImageResource(R.drawable.mine_unselected);
                break;
            case 2:
                navigator_index_iv.setImageResource(R.drawable.index_unselected);
                navigator_message_iv.setImageResource(R.drawable.message_unselected);
                navigator_mine_iv.setImageResource(R.drawable.mine_selected);
                break;
        }
    }


}

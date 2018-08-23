package com.weilay.pos2.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.weilay.pos2.R;
import com.weilay.pos2.adapter.OrderInfoAdapter;
import com.weilay.pos2.base.BaseActivity;
import com.weilay.pos2.bean.OrderInfoBean;
import com.weilay.pos2.listener.OnItemclickListener;
import com.weilay.pos2.util.DeviceUtil;
import com.weilay.pos2.util.LogUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 我的订单界面
 * TODO 1，目前不知道调用哪个接口来获取数据
 */

public class OrderListActivity extends BaseActivity implements OnItemclickListener {
    private TextView ol_date;
    private RecyclerView ol_recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        initView();
    }

    private void initView() {
        View view = findViewById(R.id.ol_titlecontainer);
        view.findViewById(R.id.title_back).setOnClickListener(this);
        TextView title = view.findViewById(R.id.title_title);
        title.setText(R.string.order_list);

        ol_date = findViewById(R.id.ol_date);
        ol_date.setOnClickListener(this);
        ol_recyclerview = findViewById(R.id.ol_recyclerview);
        initRecyclerView();
    }

    private void initRecyclerView() {

        ArrayList<OrderInfoBean> list = new ArrayList<>();
        OrderInfoBean bean1 = new OrderInfoBean("7237127811", "07-26 10:40", "101.00");
        OrderInfoBean bean2 = new OrderInfoBean("7237127812", "07-26 10:40", "102.00");
        OrderInfoBean bean3 = new OrderInfoBean("7237127813", "07-26 10:40", "103.00");
        OrderInfoBean bean4 = new OrderInfoBean("7237127814", "07-26 10:40", "104.00");
        OrderInfoBean bean5 = new OrderInfoBean("7237127815", "07-26 10:40", "105.00");
        list.add(bean1);
        list.add(bean2);
        list.add(bean3);
        list.add(bean4);
        list.add(bean5);

        OrderInfoAdapter adapter = new OrderInfoAdapter(this, list);
        ol_recyclerview.setAdapter(adapter);

        //添加Android自带的分割线
        ol_recyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        ol_recyclerview.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.ol_date:
                showSelectDateDialog();
                break;
            default:
                break;
        }
    }

    /**
     * 显示选择日期的对话框
     */
    private void showSelectDateDialog() {
        int[] screenProperty = DeviceUtil.getAndroiodScreenProperty(this);
        final MaterialCalendarView materialCalendarView = new MaterialCalendarView(this);
        materialCalendarView.setLayoutParams(new ViewGroup.LayoutParams(screenProperty[0] * 2 / 3, screenProperty[1] / 2));
        materialCalendarView.setCurrentDate(new Date());
        materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 7, 26))
                .setMaximumDate(CalendarDay.from(2019, 7, 26))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(materialCalendarView);
        final AlertDialog dialog = builder.create();


        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
                LogUtils.i("b = " + b);
                LogUtils.i("calendarDay.getYear = " + calendarDay.getYear());
                LogUtils.i("calendarDay.getMonth = " + calendarDay.getMonth());
                LogUtils.i("calendarDay.getDay = " + calendarDay.getDay());


                int day = calendarDay.getDay();
                int month = calendarDay.getMonth();
                int year = calendarDay.getYear();
                String date = year + "-" + (month + 1) + "-" + day;
                ol_date.setText(date);

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onIndexItemClick(int position) {

    }
}

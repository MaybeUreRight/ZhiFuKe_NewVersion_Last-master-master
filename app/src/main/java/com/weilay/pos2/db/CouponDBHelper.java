package com.weilay.pos2.db;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;
import com.weilay.pos2.PayApplication;
import com.weilay.pos2.bean.CouponEntity;
import com.weilay.pos2.util.LogUtils;
import com.weilay.pos2.util.ServerTimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*******
 * @Detail 消息体信息处理辅助类
 * @author rxwu
 * @date 2016/08/04
 *
 */
public class CouponDBHelper {
	// 卡券信息保存(三个月)
	private static long SVAE_TIME = 3 * 30 * 24 * 60 * 60 * 1000;
	public static Map<String, String> keys = new HashMap<>();

	/*********
	 * @detail 保存收到的消息
	 */
	public static void saveCoupons(CouponEntity coupon) {
		// 排重处理
		if (coupon == null || isExists(coupon)) {
			return;
		}
		try {
			PayApplication.db.save(coupon);
		} catch (DbException e) {
			LogUtils.e("保存卡券信息出错：（cause:" + e.getLocalizedMessage() + ")");
		}
	}

	/******
	 * @detail 查找卡券是否存在
	 */
	private static boolean isExists(CouponEntity coupon) {
		if (coupon == null)
			return false;
		try {
			long count = PayApplication.db.count(Selector.from(CouponEntity.class).where("id", "=", coupon.getId()));
			return count > 0 ? true : false;
		} catch (Exception ex) {
			LogUtils.e("判断卡券是否存在时发生错误" + ex.getLocalizedMessage());
		}
		return false;
	}

	/*******
	 * @detail 清除所有大于7天的记录
	 */
	public static void clearCoupons() {
		try {
			PayApplication.db.delete(CouponEntity.class,
					WhereBuilder.b("time", "<", ServerTimeUtils.getServerTime() - SVAE_TIME));
		} catch (DbException e) {
			LogUtils.e("清除卡券信息出错：（cause:" + e.getLocalizedMessage() + ")");
		}
	}

	/*****
	 * @detail 查找所有的消息,以cid为查询条件
	 */
	public static List<String> findAllMessages() {
		List<String>codes=new ArrayList<>();
		try {
			List<DbModel> datas = PayApplication.db.findDbModelAll(Selector.from(CouponEntity.class).select("id"));
			keys.clear();
			for (DbModel coupon : datas) {
				String code=coupon.getString("id");
				if (code != null){
					keys.put(code,code);
					codes.add(code);
				}
					
			}
			return codes;
		} catch (Exception ex) {
			LogUtils.e("获取卡券列表出错：（cause:" + ex.getLocalizedMessage() + ")");
		}
		return null;
	}

	public static void delteCoupon(CouponEntity item) {
		// TODO Auto-generated method stub\
		if(item==null || item.getId()==null){
			return;
		}
		try{
			PayApplication.db.delete(CouponEntity.class, WhereBuilder.b("id","=", item.getId()));
		}catch(Exception ex){
			
		}
		
	}

}

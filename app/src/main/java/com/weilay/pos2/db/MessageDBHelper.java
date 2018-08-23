package com.weilay.pos2.db;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;
import com.weilay.pos2.PayApplication;
import com.weilay.pos2.bean.CouponEntity;
import com.weilay.pos2.bean.MessageEntity;
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
public class MessageDBHelper {
    // 消息保存的时长(一个星期)
    private static long SVAE_TIME = 7 * 24 * 60 * 60 * 1000;
    public static Map<String, String> keys = new HashMap<>();
    /*********
     * @detail 保存收到的消息
     *//*
	public static void saveMessages(List<MessageEntity> msgs) {
	initKeys();
		if (msgs == null) {
			return;
		}
		// 排重处理
		for (MessageEntity item : msgs) {
			if (keys.containsKey(item.getMsgid())) {
				msgs.remove(item);
			}
		}
		try {
			LogUtils.e("保存了" + (msgs == null ? 0 : msgs.size()) + "条推送的信息");
			 PayApplication.db.saveAll(msgs);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			LogUtils.e("保存推送内容时出错：" + e.getLocalizedMessage());
		}

	}*/

    /*********
     * @detail 保存收到的消息
     */
    public static int saveMessage(MessageEntity item) {
        initKeys();
        // 排重处理
        if (item == null || keys.containsKey(item.getMsgid())) {
            return 0;
        }
        try {
            PayApplication.db.save(item);
            keys.put(item.getMsgid(), item.getMsgid());
            return 1;
        } catch (DbException e) {
            LogUtils.e("保存推送内容时出错：" + e.getLocalizedMessage());
        } catch (Exception ex) {
            LogUtils.e("保存推送内容时出错：" + ex.getLocalizedMessage());
        }
        return 0;

    }

    /*******
     * @detail 清除所有大于7天的记录
     */
    public static void clearMessage() {
        try {
            PayApplication.db.delete(MessageEntity.class,
                    WhereBuilder.b("time", "<", ServerTimeUtils.getServerTime() - SVAE_TIME));
        } catch (DbException ex) {
            LogUtils.e("删除过期信息时出错：" + ex.getLocalizedMessage());
        }
    }

    //初始化
    public static void initKeys() {
        if (keys == null || keys.size() == 0) {
            keys = new HashMap<String, String>();
            try {
                List<DbModel> datas = PayApplication.db.findDbModelAll(Selector.from(MessageEntity.class).select("msgid"));
                List<DbModel> coupons = PayApplication.db.findDbModelAll(Selector.from(CouponEntity.class).select("id"));
                //keys.clear();
                if (datas != null && datas.size() > 0) {
                    for (DbModel model : datas) {
                        keys.put(model.getString("msg_id"), model.getString("msg_id"));
                    }
                }
                if (coupons != null && coupons.size() > 0) {
                    for (DbModel model : coupons) {
                        keys.put(model.getString("id"), model.getString("id"));
                    }
                }
            } catch (Exception ex) {
                LogUtils.e("find the receiver message keys map happend exception,cause:" + ex.getLocalizedMessage());
            }
        }

    }

    /*****
     * @detail 查找所有的消息
     */
    public static List<MessageEntity> findAllMessages() {
        try {
            List<MessageEntity> datas = PayApplication.db
                    .findAll(Selector.from(MessageEntity.class).orderBy("id", true));
            LogUtils.e("总共有" + (datas == null ? 0 : datas.size() + "条推送消息"));
            keys.clear();
            if (datas != null && !datas.isEmpty() && datas.size() > 0) {
                for (MessageEntity item : datas) {
                    if (item.getMsgid() != null)
                        keys.put(item.getMsgid(), item.getMsgid());
                }
            }
            return datas;
        } catch (Exception ex) {
            LogUtils.e("获取推送消息列表时错误：" + ex.getLocalizedMessage());
        }
        return null;
    }
    /*****
     * @detail 查找所有的消息
     */
    public static ArrayList<MessageEntity> findAllMessages2() {
        ArrayList<MessageEntity> tempList = new ArrayList<>();
        try {
            List<MessageEntity> datas = PayApplication.db
                    .findAll(Selector.from(MessageEntity.class).orderBy("id", true));
            LogUtils.e("总共有" + (datas == null ? 0 : datas.size() + "条推送消息"));
            keys.clear();
            if (datas != null && !datas.isEmpty() && datas.size() > 0) {
                for (MessageEntity item : datas) {
                    if (item.getMsgid() != null)
                        keys.put(item.getMsgid(), item.getMsgid());
                }
                tempList.addAll(datas);
            }
            return tempList;
        } catch (Exception ex) {
            LogUtils.e("获取推送消息列表时错误：" + ex.getLocalizedMessage());
        }
        return null;
    }

    /****
     * @detail 删除单条信息
     * @param messageEntity
     */
    public static void deleteMessage(MessageEntity messageEntity) {
        // TODO Auto-generated method stub
        if (messageEntity == null) {
            return;
        }
        try {
            PayApplication.db.delete(messageEntity);
            if (keys != null && keys.containsKey(messageEntity.getMsgid())) {
                keys.remove(messageEntity.getMsgid());
            }
        } catch (DbException ex) {
            LogUtils.e("删除推送记录失败：");
        }
    }

    /****
     * @detail 删除所有的信息
     */
    public static void deleteMessage() {
        try {
            PayApplication.db.delete(MessageEntity.class, WhereBuilder.b("id", ">", 0));
            if (keys != null) {
                keys.clear();
            }
        } catch (DbException ex) {
            LogUtils.e("删除推送记录失败：");
        }
    }

}

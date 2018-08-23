package com.weilay.pos2.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weilay.pos2.base.BaseActivity;
import com.weilay.pos2.bean.CheckOutEntity;
import com.weilay.pos2.bean.CouponEntity;
import com.weilay.pos2.bean.JoinVipEntity;
import com.weilay.pos2.bean.MachineEntity;
import com.weilay.pos2.bean.MemberEntity;
import com.weilay.pos2.bean.MemberTimesLevelEntity;
import com.weilay.pos2.bean.OperatorEntity;
import com.weilay.pos2.bean.PayTypeEntity;
import com.weilay.pos2.bean.PosDefine;
import com.weilay.pos2.bean.RechageLockEntity;
import com.weilay.pos2.http.BaseParam;
import com.weilay.pos2.http.HttpManager;
import com.weilay.pos2.http.NetCodeEnum;
import com.weilay.pos2.listener.CardUseListener;
import com.weilay.pos2.listener.ChargeOffCouponListener;
import com.weilay.pos2.listener.CheckOutListener;
import com.weilay.pos2.listener.DialogAskListener;
import com.weilay.pos2.listener.GetCouponListener;
import com.weilay.pos2.listener.JoinVipListener;
import com.weilay.pos2.listener.LoadMemberRulesListener;
import com.weilay.pos2.listener.OnCheckMachineStateListener;
import com.weilay.pos2.listener.OnDataListener;
import com.weilay.pos2.listener.ResponseListener;
import com.weilay.pos2.local.UrlDefine;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import okhttp3.FormBody;

/*******
 * @detail 系统工具
 * @author rxwu
 * @date 2016/07/08
 */
public class Utils {
    @SuppressLint("StaticFieldLeak")
    private static Application sApplication;
    public static boolean isLogin = false;

    private static final LinkedList<Activity> ACTIVITY_LIST = new LinkedList<>();

    private static Application.ActivityLifecycleCallbacks mCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            setTopActivity(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            setTopActivity(activity);
        }

        @Override
        public void onActivityResumed(Activity activity) {
            setTopActivity(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            ACTIVITY_LIST.remove(activity);
        }
    };

    private static void setTopActivity(final Activity activity) {
        if (activity.getClass() == PermissionUtils.PermissionActivity.class) return;
        if (ACTIVITY_LIST.contains(activity)) {
            if (!ACTIVITY_LIST.getLast().equals(activity)) {
                ACTIVITY_LIST.remove(activity);
                ACTIVITY_LIST.addLast(activity);
            }
        } else {
            ACTIVITY_LIST.addLast(activity);
        }
    }

    static LinkedList<Activity> getActivityList() {
        return ACTIVITY_LIST;
    }

//    public static void login(final BaseActivity act, String mid, String operator, String pwd, final LoginListener loginListener) {
//        final String pwdStr = PasswordEncode.parsePassword(pwd);
//        FormBody.Builder builder = BaseParam.getBaseParams();
//        builder.add("mid", mid);
//        builder.add("operator", operator);
//        builder.add("pwd", pwdStr);
//        HttpUtils.sendPost(act, builder, UrlDefine.URL_LOGIN, new ResponseListener() {
//
//            @Override
//            public void onSuccess(JSONObject json) {
//                //  
//                // update by rxwu
//                try {
//                    OperatorEntity operatorEntity = new Gson()
//                            .fromJson(json.optJSONArray("data").optJSONObject(0).toString(), OperatorEntity.class);
//                    operatorEntity.setPassword(pwdStr);
//                    Utils.saveOperator(operatorEntity);// 保存用户信息
//                    loginListener.loginSuccess(operatorEntity);
//                } catch (Exception ex) {
//                    loginListener.loginFailed("登录返回格式有误", NetCodeEnum.NOJSON.getCode());
//                }
//
//            }
//
//            @Override
//            public void onFailed(int code, String msg) {
//                //  
//                loginListener.loginFailed(msg, code);
//            }
//
//            @Override
//            public void networkError() {
//                //  
//                loginListener.loginFailed("网络异常", NetCodeEnum.NETWORK_UNABLE.getCode());
//            }
//        });
//    }

    /**
     * Init utils.
     * <p>Init it in the class of Application.</p>
     *
     * @param app application
     */
    public static void init(@NonNull final Application app) {
        Utils.sApplication = app;
        Utils.sApplication.registerActivityLifecycleCallbacks(mCallbacks);
    }
    /**
     * Return the context of Application object.
     *
     * @return the context of Application object
     */
    public static Application getApp() {
        if (sApplication != null) return sApplication;
        throw new NullPointerException("u should init first");
    }
    /*******
     * @detail 查询订单识别的信息
     */
    public static void queryOrderRead(final OnDataListener listener) {
        String cache_machine = SPUtils.getInstance().getString(PosDefine.CACHE_MACHINE_INFO,"");
        if (cache_machine != null && !TextUtils.isEmpty(cache_machine)) {
            listener.onData(new Gson().fromJson(cache_machine, MachineEntity.class));
            return;
        }
        FormBody.Builder builder = BaseParam.getParams();
        HttpManager.sendPost2(builder, UrlDefine.MACHINE_INFO_URL, new ResponseListener() {

            @Override
            public void onSuccess(JSONObject json) {
                //
                try {
                    List<MachineEntity> machines = new Gson().fromJson(json.optString("data"),
                            new TypeToken<List<MachineEntity>>() {
                            }.getType());
                    if (machines != null && machines.size() > 0) {
                        LogUtils.d("获取机器码信息成功!");
                        MachineEntity machineInfo = machines.get(0);
                        listener.onData(machineInfo);
                        String key = PosDefine.CACHE_MACHINE_INFO;
                        String value = new Gson().toJson(machineInfo);
                        int saveTime = 10 * 60;
                        SPUtils.getInstance().put(key, newStringWithDateInfo(saveTime, value));
                    } else {
                        listener.onData(null);
                    }

                } catch (Exception ex) {
                    listener.onFailed("获取机器码信息失败" + ex.getLocalizedMessage());
                    Log.e("", "获取机器码信息失败");
                }

            }

            @Override
            public void onFailed(int code, String msg) {
                //
                listener.onFailed("获取机器码信息失败" + msg);
            }

            @Override
            public void networkError() {
                //
                listener.onFailed("网络请求异常");
            }
        });
    }

    /*******
     * @detail 机器是否在线
     */
    public static void MachineOnLine(final OnCheckMachineStateListener listener) {
        FormBody.Builder param = BaseParam.getParams();
        // 检查设备是否在线
        HttpManager.sendPost2(param, UrlDefine.URL_CHECK_MACHINE_STATE, new ResponseListener() {

            @Override
            public void onSuccess(JSONObject json) {
                //
                // Log.e("gg", "MachineOnLine->onSuccess:" +
                // json.toString());
                listener.onLine();
            }

            @Override
            public void onFailed(int code, String msg) {
                //
                listener.nofound(code, msg);
            }

            @Override
            public void networkError() {
                //
                listener.connectFailed("网络请求失败");
            }

        });
    }

    /***********
     * @detail 获取当前用户的信息
     */
    public static OperatorEntity getCurOperator() {
        String operatorStr = SPUtils.getInstance().getString(PosDefine.CACHE_OPERATOR);
        if (!TextUtils.isEmpty(operatorStr)) {
            OperatorEntity operator = GsonUtils.convertString2Object(operatorStr, OperatorEntity.class);
            return operator;
        }
        return null;
    }

    /***********
     * @detail 保存用户的信息
     */
    public static boolean saveOperator(OperatorEntity operator) {
        if (operator != null) {
            SPUtils.getInstance().put(PosDefine.CACHE_OPERATOR, GsonUtils.convertVO2String(operator));
            return true;
        }
        return false;

    }

    /*****
     * @detail 根据会员卡号获取卡券的详情
     * @param vipNo
     */
    public static void getCouponInfo(final String vipNo, final GetCouponListener listener) {
        FormBody.Builder params = BaseParam.getParams();
        params.add("code", vipNo);
        HttpManager.sendPost2(params, UrlDefine.URL_GET_COUPON_INFO, new ResponseListener() {

            @Override
            public void onSuccess(JSONObject json) {
                try {
                    CouponEntity coupon = new Gson().fromJson(json.optString("data"), CouponEntity.class);
                    if (coupon != null) {
                        coupon.setCode(vipNo);
                        listener.onData(coupon);
                    } else {
                        listener.onFailed("查无卡券");
                    }
                } catch (Exception ex) {
                    Log.e("", ex.getLocalizedMessage());
                    listener.onFailed("获取卡券信息出错");
                }
            }

            @Override
            public void onFailed(int code, String msg) {
                //  
                listener.onFailed(msg);
            }

            @Override
            public void networkError() {
                //  
                listener.onFailed("网络异常");
            }
        });

    }

    /*****
     * @detail 根据会员卡号获取卡券的详情
     * @param cid
     *            卡券的id
     */
    public static void getAdverCardInfo(final String cid, final GetCouponListener listener) {
        //
        FormBody.Builder params = BaseParam.getParams();
        params.add("cid", cid);
        HttpManager.sendPost2(params, UrlDefine.URL_GET_ADVER_CARD_INFO, new ResponseListener() {

            @Override
            public void onSuccess(JSONObject json) {
                //
                try {
                    List<CouponEntity> coupons = new Gson().fromJson(json.optString("data"),
                            new TypeToken<List<CouponEntity>>() {
                            }.getType());
                    CouponEntity coupon = null;
                    if (coupons != null && coupons.size() > 0) {
                        coupon = coupons.get(0);
                        coupon.setId(cid);
                        listener.onData(coupon);
                    } else {
                        listener.onFailed("查无卡券");
                    }
                } catch (Exception ex) {
                    Log.e("", ex.getLocalizedMessage());
                    listener.onFailed("获取卡券信息出错");
                }
            }

            @Override
            public void onFailed(int code, String msg) {
                //
                listener.onFailed(msg);
            }

            @Override
            public void networkError() {
                //
                listener.onFailed("网络异常");
            }
        });

    }

    /********
     * @detail 卡券核销
     * @param coupon
     * @param couponDiscountAmount
     *            卡券优惠的金额
     * @param paidamount
     *            订单实付的金额
     */
    public static void cardConsume(final CouponEntity coupon
            , final PayTypeEntity mPay
            , final double couponDiscountAmount
            , final double paidamount
            , final double couponDiscount
            , String open_id
            , final CardUseListener listener) {
        if (coupon == null) {
            return;
        }
        FormBody.Builder params = BaseParam.getParams();
        params.add("code", coupon.getCode());
        params.add("tx_no", mPay.getTx_no());
        params.add("receivable", "" + ConvertUtil.yuanToBranch(couponDiscountAmount));
        params.add("paidamount", "" + ConvertUtil.yuanToBranch(paidamount));
        params.add("openId", open_id != null ? open_id : "");
        HttpManager.sendPost2( params, UrlDefine.URL_CARD_CONSUME, new ResponseListener() {

            @Override
            public void onSuccess(JSONObject json) {
                //
                try {
                    if (json.optInt("code") == 0) {
                        mPay.setCouponDiscountAmount(couponDiscountAmount);
                        mPay.setCouponType(coupon.getType());
                        mPay.setCouponDiscount(couponDiscount);
                        listener.success(mPay);
                    } else {
                        listener.failed(json.optString("message"));
                    }
                } catch (Exception ex) {
                    Log.e("", ex.getLocalizedMessage());
                    listener.failed("核销失败");
                }
            }

            @Override
            public void onFailed(int code, String msg) {
                //
                listener.failed(msg);
            }

            @Override
            public void networkError() {
                //
                listener.failed("网络异常");
            }
        });

    }

    /********
     * @detail 更新会员的余额
     */
    public static void updateMemberBalance( boolean memberPay, PayTypeEntity type, String membershipno, String open_id, String phone, final OnDataListener listener) {
        double d = ConvertUtil.yuanToBranch(type.getAraamount());
        FormBody.Builder params = BaseParam.getParams();
        params.add("phone", phone);
        params.add("open_id", open_id);
        params.add("code", membershipno);
        params.add("balance", "" + (memberPay ? ConvertUtil.doubleToString(d) : 0));
        params.add("recordBalance", "");
        params.add("tx_no", type.getTx_no());
        params.add("member_card_pay_amt", "" + ConvertUtil.doubleToString(d));
        params.add("discount_amt", "" + ConvertUtil.yuanToBranch(type.getMemberDiscountAmount()));
        HttpManager.sendPost2( params, UrlDefine.URL_UPDATE_MEMBER_RECHARGE, new ResponseListener() {

            @Override
            public void onSuccess(JSONObject json) {
                //
                if (json.optInt("code") == 0) {
                    listener.onData(json);
                } else {
                    listener.onFailed(json.optString("message"));
                }
            }

            @Override
            public void onFailed(int code, String msg) {
                //
                listener.onFailed(msg);
            }

            @Override
            public void networkError() {
                //
                listener.onFailed("网络异常");
            }
        });
    }

    /*********
     * @detail 会员充值
     * @param member
     * @param rechargeMoney
     *            普通充值的充值金额
     * @param level
     * @param giftMoney
     * @param listener
     */
    public static void memberRecharge(MemberEntity member
            , double rechargeMoney
            , MemberTimesLevelEntity level
            , double giftMoney
            , final OnDataListener listener) {
        String tx_no = DeviceUtil.getOutTradeNo();
        long recharge = 0;
        long gift = ConvertUtil.yuanToBranch(giftMoney);
        FormBody.Builder builder = BaseParam.getParams();
        String rechargeUrl = "";
        if (member.getMember_card_type() == 1) {
            rechargeUrl = UrlDefine.URL_MEMBER_TIME_RECHARGE;
            recharge = (int) level.getLevel_amount();
            builder.add("rechargeId", "" + level.getId());
        } else {
            rechargeUrl = UrlDefine.URL_MEMBER_RECHARGE;
            recharge = ConvertUtil.yuanToBranch(rechargeMoney);
        }
        builder.add("phone", "");
        builder.add("membership_number", member.getMembership_number());
        builder.add("rechargeAmt", "" + recharge);
        builder.add("giftAmt", "" + gift);
        builder.add("tx_no", tx_no);
        HttpManager.sendPost2( builder, rechargeUrl, new ResponseListener() {

            @Override
            public void onSuccess(JSONObject json) {
                //
                listener.onData(json);
            }

            @Override
            public void onFailed(int code, String msg) {
                //
                listener.onFailed(msg);
            }

            @Override
            public void networkError() {
                //
                listener.onFailed("网络出错");
            }
        });
    }

    /**********
     * @detail 查询会员的升级规则
     */
    public static void getMemberUpgradeRules(final LoadMemberRulesListener listener) {
        FormBody.Builder params = BaseParam.getParams();
        HttpManager.sendPost2(params, UrlDefine.URL_GET_MEMBER_UPGRADE_RULES, new ResponseListener() {

            @Override
            public void onSuccess(JSONObject json) {
                Type type = new TypeToken<List<String>>() {
                }.getType();
                try {
                    List<String> rules = new Gson().fromJson(json.optString("data"), type);
                    if (rules == null) {
                        listener.loadFailed("商家未创建升级规则");
                    } else {
                        listener.loadSuccess(rules);
                    }
                } catch (Exception ex) {
                    listener.loadFailed("商家未创建升级规则");
                }
            }

            @Override
            public void onFailed(int code, String msg) {
                listener.loadFailed(msg);
            }
        });
    }

    /********
     * @detail 获取互投卡券
     * @param cids
     * @param onDataListener
     */
    public static void loadTaskCoupon(String cids, final OnDataListener onDataListener) {
        FormBody.Builder params = BaseParam.getParams();
        params.add("cids", cids);
        HttpManager.sendPost2( params, UrlDefine.URL_GET_ADVER_CARD_INFO, new ResponseListener() {

            @Override
            public void onSuccess(JSONObject json) {
                //
                try {
                    Type couponType = new TypeToken<List<CouponEntity>>() {
                    }.getType();
                    List<CouponEntity> datas = new Gson().fromJson(json.optString("data"), couponType);
                    onDataListener.onData(datas);
                } catch (Exception ex) {
                    onDataListener.onFailed(ex.getLocalizedMessage());
                }
            }

            @Override
            public void onFailed(int code, String msg) {
                //
                if (code == 404) {
                    onDataListener.onFailed(code, msg);
                } else {
                    onDataListener.onFailed(msg);
                }
            }
        });

    }

    /********
     * @detail 获取互投卡券的二维码
     * @param cardId
     * @param onDataListener
     */
    public static void getCouponQRCode(String cardId, final OnDataListener onDataListener) {
        FormBody.Builder params = BaseParam.getParams();
        params.add("id", cardId);
        HttpManager.sendPost2(params, UrlDefine.URL_GET_ADVER_QRCODE, new ResponseListener() {

            @Override
            public void onSuccess(JSONObject json) {
                //
                try {
                    String qrcode = json.optString("data");
                    onDataListener.onData(qrcode);
                } catch (Exception ex) {
                    onDataListener.onFailed(ex.getLocalizedMessage());
                }
            }

            @Override
            public void onFailed(int code, String msg) {
                //
                onDataListener.onFailed(msg);
            }
        });

    }

    private static final String HAS_SYNCHRONIZED = "HAS_SYNCHRONIZED";

    /********
     * @detail 获取服务器的时间
     */
    public static void getServerTime() {
        FormBody.Builder params = BaseParam.getParams();
        HttpManager.sendPost2(params, UrlDefine.URL_GET_SERVER_TIME, new ResponseListener() {

            @Override
            public void onSuccess(JSONObject json) {
                long time = json.optLong("data");// 服务器返回的时间必须乘以1000才可以
                ServerTimeUtils.setServerTime(time * 1000);
            }

            @Override
            public void onFailed(int code, String msg) {
            }

            @Override
            public void onFailed(NetCodeEnum code, String msg) {
            }

            @Override
            public void networkError() {
            }
        });
    }

    public static void sendChargeOff(String codeNo, final ChargeOffCouponListener chargeOffCouponListener) {
        FormBody.Builder builder = BaseParam.getParams();
        builder.add("code", codeNo);
        HttpManager.sendPost2(builder, UrlDefine.URL_CHARGEOFF_COUPON, new ResponseListener() {

            @Override
            public void onSuccess(JSONObject json) {
                //  
                chargeOffCouponListener.onSuc();

            }

            @Override
            public void onFailed(int code, String msg) {
                //  
                chargeOffCouponListener.onErr();
            }
        });
    }

    public static void SendCheckOut(FormBody.Builder builder, final CheckOutListener checkOutListener) {
        HttpManager.sendPost2( builder, UrlDefine.URL_CHECKOUT, new ResponseListener() {

            @Override
            public void onSuccess(JSONObject json) {
                //
                Type type = new TypeToken<List<CheckOutEntity>>() {
                }.getType();

                try {
                    List<CheckOutEntity> list = new Gson().fromJson(json.getString("data"), type);
                    checkOutListener.onSuc(list);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int code, String msg) {
                //
                checkOutListener.onErr();
            }
        });
    }

    /************
     * @detail 发起退款申请
     * @return void
     * @param
     * @detail
     */
//    public static void SendRefund(BaseActivity activity, FormBody.Builder builder, final RefundListener refundListener) {
//
//    }

    /*****
     * @detail 领取会员卡
     * @return void
     * @param
     * @detail
     */
	public static void JoinVip(FormBody.Builder builder, final JoinVipListener joinVipListener) {
		HttpManager.sendPost2( builder, UrlDefine.URL_JOINVIP, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject json) {
				//
				Type type = new TypeToken<JoinVipEntity>() {
				}.getType();
				JoinVipEntity jvi;
				try {
					jvi = new Gson().fromJson(json.getString("data"), type);
					joinVipListener.onSuc(jvi);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailed(int code, String msg) {
				//
				switch (code) {
				case 404:
					joinVipListener.on404(msg);
					break;
				default:
					joinVipListener.onErr();
					break;
				}
			}
		});
	}

    /*****
     *
     * @detail 绑定激活设备
     */
	public static void activeDevice(final BaseActivity activty) {
		DialogAsk.ask(activty, "激活设备提示", "您的设备尚未激活，请问你确认现在激活吗？", "确认", "取消", new DialogAskListener() {

			@Override
			public void okClick(DialogInterface dialog) {
				// TODO no active
				// 激活设备
				FormBody.Builder params = BaseParam.getParams();
				HttpManager.sendPost2(params, UrlDefine.URL_ACTIVE_MACHINE, new ResponseListener() {
					@Override
					public void onSuccess(JSONObject json) {
						//
						T.showCenter("设备已激活");
//						StartActivity.actionStart(activty);
					}

					@Override
					public void onFailed(int code, String msg) {
						//
						T.showShort("激活失败");

					}
				});

			}

			@Override
			public void cancelClick(DialogInterface dialog) {
				//
				T.showCenter("如需使用设备请先激活设备");
			}
		});

	}

    /**
     * 获取解锁的二维码
     */
	public static void getRechargeLock(final OnDataListener listener) {
		FormBody.Builder params = BaseParam.getParams();
		params.add("optype", "1");
		HttpManager.sendPost2(params, UrlDefine.URL_RECHAGE_LOCK, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject json) {
				//
				listener.onData(new Gson().fromJson(json.optString("data"), RechageLockEntity.class));
			}

			@Override
			public void onFailed(int code, String msg) {
				//
				listener.onFailed(code, msg);
			}

			@Override
			public void networkError() {
				//
				super.networkError();
				listener.onFailed(NetCodeEnum.NETWORK_UNABLE.getCode(), "网络出错");
			}
		});

	}

    /**
     *
     查询解锁是否成功
     */
	public static void queryRechageLock(final RechageLockEntity mLock, final OnDataListener<Integer> listener) {
		FormBody.Builder params = BaseParam.getParams();
		params.add("optype", mLock.getOptype());
		params.add("sceneid", mLock.getSceneid());
		HttpManager.sendPost2(params, UrlDefine.URL_CHECK_RECHAGE_LOCK, new ResponseListener() {

			@Override
			public void onSuccess(JSONObject json) {
				//
				listener.onData(0);
			}

			@Override
			public void onFailed(int code, String msg) {
				//
				listener.onFailed(code, msg);
			}

			@Override
			public void networkError() {
				//
				super.networkError();
				listener.onFailed(NetCodeEnum.NETWORK_UNABLE.getCode(), "网络出错");
			}
		});

	}



    public static String newStringWithDateInfo(int second, String strInfo) {
        return createDateInfo(second) + strInfo;
    }

    private static final char mSeparator = ' ';

    private static String createDateInfo(int second) {
        String currentTime = System.currentTimeMillis() + "";
        while (currentTime.length() < 13) {
            currentTime = "0" + currentTime;
        }
        return currentTime + "-" + second + mSeparator;
    }
}

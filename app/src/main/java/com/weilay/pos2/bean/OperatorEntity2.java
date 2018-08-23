package com.weilay.pos2.bean;

/*******
 * @detail 操作员的信息
 * @author Administrator
 *
 */


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2016/3/22.
 * <p/>
 * Email:1158577255@qq.com
 * <p/>
 * detail:操作员登陆后的信息实体
 */
@Entity
public class OperatorEntity2 {
    //支付的权限0表示没有支付权限 1表示有主扫被扫两种权限 2.刷卡 3.扫码支付
    public static final int PAY_PERMISSION_NONE = 0;
    public static final int PAY_PERMISSION_ALL = 1;
    public static final int PAY_PERMISSION_MICRO = 2;
    public static final int PAY_PREMISSION_UNIFIED = 3;

    private String mid;// 商户编号
    private String name;// 名称
    private String password;// 密码（保存加密后）
    private String validto;// 最后一次登录时间
    private String shopname;// 商店名称

    private String upgradetype = "";

    //是否有支付的权限
    private int wechatpay;//微信支付
    private int alipay;//支付宝支付权限
    private int memberpay = PAY_PERMISSION_MICRO;//会员卡默认主扫权限
    private int salepay = PAY_PERMISSION_MICRO;//优惠券默认主扫权限


    private String operator;// 操作员号
    private String operatorname;// 操作员姓名
    private String img;// 操作员头像

    private String status;// 操作员状态
    private String statusname;
    private String lastdailyclosing;// 上次交班时间
    private String joinadalliance;// 是否加入广告联盟


    private String address;// 商户的地址
    private String mobile;//商户的联系电话

    private String machineaddress;// 设备的地址
    private String storemobile;// 门店电话
    private String storeaddress;// 门店地址
    private String business_name;// 总店名称
    private String branch_name;// 分店名称
    private int qrcode_lock_1;//会员充值锁	1表示需要验证

    // 代理商信息
    private String agentid;// 代理商id
    private String agentname;// 代理商名称

    @Generated(hash = 451777700)
    public OperatorEntity2(String mid, String name, String password, String validto,
            String shopname, String upgradetype, int wechatpay, int alipay,
            int memberpay, int salepay, String operator, String operatorname,
            String img, String status, String statusname, String lastdailyclosing,
            String joinadalliance, String address, String mobile,
            String machineaddress, String storemobile, String storeaddress,
            String business_name, String branch_name, int qrcode_lock_1,
            String agentid, String agentname) {
        this.mid = mid;
        this.name = name;
        this.password = password;
        this.validto = validto;
        this.shopname = shopname;
        this.upgradetype = upgradetype;
        this.wechatpay = wechatpay;
        this.alipay = alipay;
        this.memberpay = memberpay;
        this.salepay = salepay;
        this.operator = operator;
        this.operatorname = operatorname;
        this.img = img;
        this.status = status;
        this.statusname = statusname;
        this.lastdailyclosing = lastdailyclosing;
        this.joinadalliance = joinadalliance;
        this.address = address;
        this.mobile = mobile;
        this.machineaddress = machineaddress;
        this.storemobile = storemobile;
        this.storeaddress = storeaddress;
        this.business_name = business_name;
        this.branch_name = branch_name;
        this.qrcode_lock_1 = qrcode_lock_1;
        this.agentid = agentid;
        this.agentname = agentname;
    }

    @Generated(hash = 1310331368)
    public OperatorEntity2() {
    }

    /*****
     * @detail 根据支付方式获取对应的权限
     * @return int
     * @param
     * @detail
     */
    public int getPermission(PayType paytype) {
        switch (paytype) {
            case WEIXIN:
                return wechatpay;
            case CHUZHIKA:
                return memberpay;
            case ALIPAY:
                return alipay;
            case SALE:
                return salepay;
            default:
                return PAY_PERMISSION_ALL;
        }
    }

    public String getMid() {
        return this.mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getValidto() {
        return this.validto;
    }

    public void setValidto(String validto) {
        this.validto = validto;
    }

    public String getShopname() {
        return this.shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getUpgradetype() {
        return this.upgradetype;
    }

    public void setUpgradetype(String upgradetype) {
        this.upgradetype = upgradetype;
    }

    public int getWechatpay() {
        return this.wechatpay;
    }

    public void setWechatpay(int wechatpay) {
        this.wechatpay = wechatpay;
    }

    public int getAlipay() {
        return this.alipay;
    }

    public void setAlipay(int alipay) {
        this.alipay = alipay;
    }

    public int getMemberpay() {
        return this.memberpay;
    }

    public void setMemberpay(int memberpay) {
        this.memberpay = memberpay;
    }

    public int getSalepay() {
        return this.salepay;
    }

    public void setSalepay(int salepay) {
        this.salepay = salepay;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperatorname() {
        return this.operatorname;
    }

    public void setOperatorname(String operatorname) {
        this.operatorname = operatorname;
    }

    public String getImg() {
        return this.img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusname() {
        return this.statusname;
    }

    public void setStatusname(String statusname) {
        this.statusname = statusname;
    }

    public String getLastdailyclosing() {
        return this.lastdailyclosing;
    }

    public void setLastdailyclosing(String lastdailyclosing) {
        this.lastdailyclosing = lastdailyclosing;
    }

    public String getJoinadalliance() {
        return this.joinadalliance;
    }

    public void setJoinadalliance(String joinadalliance) {
        this.joinadalliance = joinadalliance;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMachineaddress() {
        return this.machineaddress;
    }

    public void setMachineaddress(String machineaddress) {
        this.machineaddress = machineaddress;
    }

    public String getStoremobile() {
        return this.storemobile;
    }

    public void setStoremobile(String storemobile) {
        this.storemobile = storemobile;
    }

    public String getStoreaddress() {
        return this.storeaddress;
    }

    public void setStoreaddress(String storeaddress) {
        this.storeaddress = storeaddress;
    }

    public String getBusiness_name() {
        return this.business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getBranch_name() {
        return this.branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public int getQrcode_lock_1() {
        return this.qrcode_lock_1;
    }

    public void setQrcode_lock_1(int qrcode_lock_1) {
        this.qrcode_lock_1 = qrcode_lock_1;
    }

    public String getAgentid() {
        return this.agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }

    public String getAgentname() {
        return this.agentname;
    }

    public void setAgentname(String agentname) {
        this.agentname = agentname;
    }
}

/**
 * BCReqParams.java
 *
 * Created by xuanzhui on 2015/7/29.
 * Copyright (c) 2015 BeeCloud. All rights reserved.
 */
package cn.wd.checkout.api;

import java.util.Date;

import cn.wd.checkout.processor.WDCache;
import cn.wd.checkout.processor.WDException;



/**
 * 向服务端请求的基类
 * 包含请求的公用参数
 */
public class WDReqParams {

    private String appId;

    //签名生成时间
    //时间戳, 毫秒数
    private Long timestamp;

    //加密签名
    //算法: md5(app_id+timestamp+app_secret), 32位16进制格式, 不区分大小写
    private String appSign;

    /**
     * 渠道类型
     * 根据不同场景选择不同的支付方式
     */
    public WDChannelTypes channel;

    /**
     * 请求类型
     */
    public enum ReqType{PAY}
    
    

    /**
     * 渠道支付类型
     */
    public static enum WDChannelTypes {

        /**
         * 微信手机原生APP支付
         */
        wepay,

        /**
         * 支付宝手机原生APP支付
         */
        alipay,
        /**
         * 银联手机原生APP支付
         */
        uppay,
        /**
         * 链支付
         */
        wdepay,
        /**
         * 个人钱包
         */
        perwallet_huarui;
        /**
         * 判断是否为有效的app端支付渠道类型
         *
         * @param channel 支付渠道类型
         * @return true表示有效
         */
        public static boolean isValidAPPPaymentChannelType(WDChannelTypes channel) {
            return channel == wepay ||
                    channel == alipay ||
                    channel == uppay ||
                    channel == wdepay||
                    channel == perwallet_huarui
                    ;
        }


        /**
         * @param channel 支付渠道类型
         * @return 实际的渠道支付名
         */
        public static String getTranslatedChannelName(String channel) {
            if (channel.equals(wepay.name()))
                return "微信手机原生APP支付";
            else if (channel.equals(alipay.name()))
                return "支付宝手机原生APP支付";
            else if (channel.equals(uppay.name()))
                return "银联手机原生APP支付";
            else if (channel.equals(perwallet_huarui.name()))
            	return "个人钱包手机原生APP支付";
            else
                return "非法的支付类型";
        }
    }

    /**
     * 收银台的唯一标识
     * @return  收银台应用APPID
     */
    public String getAppId() {
        return appId;
    }

    /**
     * 时间戳, 毫秒数
     * @return  签名生成时间
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * 算法: md5(app_id+timestamp+app_secret), 32位16进制格式, 不区分大小写
     * @return  加密签名
     */
    public String getAppSign() {
        return appSign;
    }

    /**
     * 初始化参数
     * @param channel   渠道类型
     * @param reqType   请求类型
     */
    public WDReqParams(WDChannelTypes channel, ReqType reqType) throws WDException{

        if (reqType == ReqType.PAY && (channel == null ||
                !WDChannelTypes.isValidAPPPaymentChannelType(channel)))
            throw new WDException("非法APP支付渠道");


        WDCache mCache = WDCache.getInstance(null);

        if (mCache.appId == null || mCache.appSecret == null) {
            throw new WDException("parameters: 请通过统一收银台初始化appId和appSecret");
        } else {
            appId = mCache.appId;
            timestamp = (new Date()).getTime();
            appSign = mCache.appSecret;
            this.channel = channel;
        }
    }
}

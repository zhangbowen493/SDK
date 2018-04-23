package cn.wd.checkout.api;

import cn.wd.checkout.processor.WDCache;


/**
 * 全局参数配置类
 * 建议在主activity中初始化
 */
public class CheckOut {
	/**
     * 设置AppId和AppSecret(从统一收银台网站的控制台获得), 并进行一系列异步的初始化
     * 本函数必须在所有其他统一收银台函数调用前被调用, 推荐在主Activity的onCreate函数中调用
     *
     * @param appId     appid 统一收银台签约获取 id唯一
     * @param appSecret App Secret 统一收银台签约获取 不唯一 每天都会重新生成 故需要每次设置
     */
    public static void setAppIdAndSecret(String appId, String appSecret) {
        WDCache instance = WDCache.getInstance(null);
        instance.appId = appId;
        instance.appSecret = appSecret;
    }
    /**
     * 修改是否打印请求参数
     *  @param debug		是否打印日志 默认false
     */
    public static void setIsPrint(Boolean debug){
    	if(null!=debug)
    		WDCache.getInstance(null).isDeBug = debug;
    }

    /**
     * 修改所有网络请求的超时时间，单位是毫秒，默认为10秒.
     *
     * @param networkTimeout 超时时间，单位为毫秒，例如5000表示5秒.
     */
    public static void setNetworkTimeout(Integer networkTimeout) {
    	WDCache.getInstance(null).networkTimeout = networkTimeout;
    }
    /**
     * 设置网络请求环境
     * @param way CS 生产		CT 测试 	
     */
    public static void setNetworkWay(String way){
    	WDCache.getInstance(null).requestWay = way;
    }
    /**
     * 设置链支付网络请求环境
     * @param way 	TS测试环境
     */
    public static void setLianNetworkWay(String way){
    	WDCache.getInstance(null).lianrequestWay = way;
    }
    /**
     * 自定义请求地址  不对外开放
     * @param ip
     * @param URL
     */
    public static void setCustomURL(String ip ,String URL){
    	WDCache.getInstance(null).requestWay = "custom";
    	WDCache.getInstance(null).customIP = ip;
    	WDCache.getInstance(null).customURL = URL;
    }
}

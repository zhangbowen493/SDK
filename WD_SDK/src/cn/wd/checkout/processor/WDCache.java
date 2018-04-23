package cn.wd.checkout.processor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.wd.checkout.api.WDPay;
import cn.wd.checkout.api.WDPay.PAYPAL_PAY_TYPE;


import android.app.Activity;
import android.os.Handler;

/**
 * 缓存类  单例模式
 * @author Administrator
 *
 */
public class WDCache {
	
	private static Activity contextActivity;
	private static WDCache instance;
	/**
	 * 是否正在进行支付
	 */
	private static Boolean isPaying = false;
	public  Boolean getIsPaying() {
		return isPaying;
	}

	public void setIsPaying(final Boolean paying) {
		if(!paying){
			new Handler(contextActivity.getMainLooper()).postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					WDCache.isPaying = paying;
				}
			}, 500);
		}else{
			WDCache.isPaying = paying;
		}
		
	}

	/**
	 * 是否打印日志
	 */
	public Boolean isDeBug = false;
	 /**
     * 控制台注册的App Id
     */
    public String appId;

    /**
     * 控制台注册的App Secret
     */
    public String appSecret;
    
    /**
     * 微信App Id
     */
    public String wxAppId;
    /**
     * 网络请求环境
     */
    public static String requestWay;
    /**
     * 网络请求环境
     */
    public static String lianrequestWay;
    /**
     * 自定义IP地址 包含端口
     */
    public static String customIP;
    /**
     * 自定义IP地址 后缀地址
     */
    public static String customURL;
    
    /**
     * 网络请求timeout时间
     * 以毫秒为单位
     */
    public Integer networkTimeout;

    /**
     * 线程池
     */
    public static ExecutorService executorService = Executors.newCachedThreadPool();
    
    /**
     * 支付宝
     */
    public WDPay.PAYPAL_PAY_TYPE paypalPayType;

	
	 private WDCache() {
	    }

	    /**
	     * 唯一获取实例的方法
	     * @return  BCCache实例
	     */
	    public synchronized static WDCache getInstance(Activity contextActivity) {
	        if (instance == null) {
	            instance = new WDCache();

	            instance.appId = null;
	            instance.appSecret = null;

	            instance.networkTimeout = 10000;
	        }

	        if (contextActivity != null)
	            WDCache.contextActivity = contextActivity;

	        return instance;
	    }

	
}

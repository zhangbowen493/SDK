package com.wondersgroup.wallet;

import android.content.Context;

import com.wondersgroup.request.IRequest;
import com.wondersgroup.request.UrlConnectionImpl;
import com.wondersgroup.request.VolleyImpl;

/**
 * SDK全局配置类
 * @author liuzhipeng
 *
 */
public class WDGlobalSetting {
	private static WDGlobalSetting mInstance;
	private static Context mContext;
	private static IRequest request;
	public static boolean isDebug = false;
	
	// 生产:production 测试:test 开发:develop
	public static final String Environment_P = "production";
	public static final String Environment_T = "test";
	public static final String Environment_D = "develop";
	
	public static int networkTimeout = 30000;
	/**网络请求环境  默认生成环境*/
	public static String Environment ;
	
	public static synchronized WDGlobalSetting getInstance(Context context) {
		if (context != null) {
			mContext = context;
		}
		if (mInstance == null) {
			mInstance = new WDGlobalSetting();
		}
		return mInstance;
	}
	public static IRequest getRequest(Context context) {
		if (request == null) request = VolleyImpl.getInstance(context);
//		request = UrlConnectionImpl.getInstance();
		return request;
	}
	
	
}

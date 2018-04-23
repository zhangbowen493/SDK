package com.wondersgroup.wallet;

import android.util.Log;

/**
 * Log统一管理类
 * 
 * 
 */
public class WDLogUtil
{

	private WDLogUtil()
	{
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	public static boolean isDebug = WDGlobalSetting.isDebug;// 是否需要打印bug，可以在application的onCreate函数里面初始化
	private static final String TAG = "PersonalWallet SDK======";

	// 下面四个是默认tag的函数
	public static void i(Object msg)
	{
		if (isDebug)
			Log.i(TAG, msg.toString());

	}

	public static void d(Object msg)
	{
		if (isDebug)
			Log.d(TAG, msg.toString());
	}

	public static void e(Object msg)
	{
		if (isDebug)
			Log.e(TAG, msg.toString());
	}

	public static void v(Object msg)
	{
		if (isDebug)
			Log.v(TAG, msg.toString());
	}

	// 下面是传入自定义tag的函数
	public static void i(String tag, Object msg)
	{
		if (isDebug)
			Log.i(tag, msg.toString());
	}

	public static void d(String tag, Object msg)
	{
		if (isDebug)
			Log.d(tag, msg.toString());
	}

	public static void e(String tag, Object msg)
	{
		if (isDebug)
			Log.e(tag, msg.toString());
	}

	public static void v(String tag, Object msg)
	{
		if (isDebug)
			Log.v(tag, msg.toString());
	}

	public static void w(String tag, Object msg) {
		// TODO Auto-generated method stub
		if (isDebug)
			Log.w(tag, msg.toString());
	}
}
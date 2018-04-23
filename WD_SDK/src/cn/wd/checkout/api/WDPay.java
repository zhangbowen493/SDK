package cn.wd.checkout.api;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import cn.wanda.lianpay.LPCallBack;
import cn.wanda.lianpay.LPPayApi;
import cn.wanda.lianpay.LPPayResult;
import cn.wd.checkout.processor.WDCache;
import cn.wd.checkout.processor.WDException;
import cn.wd.checkout.processor.WDHttpClientUtil;
import cn.wd.checkout.processor.WDLogUtil;
import cn.wd.checkout.processor.WDPayReqParams;
import cn.wd.checkout.processor.WDValidationUtil;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wondersgroup.wallet.WDPwSDK;
import com.wondersgroup.wallet.WDResultCallback;

/**
 * 支付类
 * 
 * @author Administrator checkout1 分支添加
 */
public class WDPay {
	private static final String TAG = "Checkout WDPay";
	/**
	 * 保留callback实例
	 */
	public static WDCallBack payCallback;
	/**
	 * UI层信息回馈
	 */
	public static Handler payHandler;

	private static Activity mContextActivity;

	private static WDPay instance;

	// IWXAPI 是第三方app和微信通信的openapi接口
	public static IWXAPI wxAPI = null;

	public enum PAYPAL_PAY_TYPE {
		SANDBOX, LIVE
	}

	private WDPay() {
	}

	/**
	 * 唯一获取BCPay实例的入口
	 * 
	 * @param context
	 *            留存context
	 * @return BCPay实例
	 */
	public synchronized static WDPay getInstance(Context context) {
		mContextActivity = (Activity) context;
		if (instance == null) {
			instance = new WDPay();
			payCallback = null;
			payHandler = null;
		}
		return instance;
	}

	/**
	 * 万达统一收银台支付方法 可更改商户
	 * 
	 * @param appId
	 *            商户ID
	 * @param appSecret
	 *            商户工作密钥
	 * @param channelType
	 *            支付类型 对于支付手机APP端目前只支持WX_APP, ALI_APP, UN_APP , lianpay
	 * @see cn.wd.checkout.api.WDReqParams.WDChannelTypes
	 * @param submerno
	 *            子商户号 区别接入商户，商户在健康云（医药云）注册的商户号
	 * @param billTitle
	 *            商品名称, 32个字节内, 汉字以2个字节计
	 * @param goods_desc
	 *            商品描述
	 * @param billTotalFee
	 *            支付金额，以分为单位，必须是正整数
	 * @param billNum
	 *            商户自定义订单号
	 * @param order_desc
	 *            订单描述
	 * @param optional
	 *            为扩展参数，可以传入任意数量的key/value对来补充对业务逻辑的需求
	 * @param callback
	 *            支付完成后的回调函数
	 * 
	 */
	public void reqPayAsync(String appId, String appSecret,
			final WDReqParams.WDChannelTypes channelType,
			final String submerno, final String billTitle,
			final String goods_desc, final Long billTotalFee,
			final String billNum, final String order_desc,
			final Map<String, String> optional, final WDCallBack callback) {
		payHandler = null;
		if (callback == null) {
			WDLogUtil.e(TAG, "请初始化callback");
			return;
		}

		if (appId == null || appSecret == null) {
			callback.done(new WDPayResult(WDPayResult.RESULT_FAIL,
					WDPayResult.FAIL_INVALID_PARAMS,
					"parameters: 请设置支付平台appId和appSecret"));
			return;
		}

		WDCache instance = WDCache.getInstance(null);
		instance.appId = appId;
		instance.appSecret = appSecret;

		if (WDReqParams.WDChannelTypes.alipay.equals(channelType)
				|| WDReqParams.WDChannelTypes.wepay.equals(channelType)
				|| WDReqParams.WDChannelTypes.uppay.equals(channelType)
				|| WDReqParams.WDChannelTypes.wdepay.equals(channelType)
				|| WDReqParams.WDChannelTypes.perwallet_huarui.equals(channelType)) {
			this.reqPaymentAsync(channelType, submerno, billTitle, goods_desc,
					billTotalFee, billNum, order_desc, null, optional, callback);
		} else {
			if (callback != null) {
				callback.done(new WDPayResult(WDPayResult.RESULT_FAIL,
						WDPayResult.FAIL_UNKNOWN_WAY, "暂不支持的支付渠道！"));
			}
		}

	}

	/**
	 * 万达统一收银台支付方法
	 * 
	 * @param appId
	 *            商户ID
	 * @param appSecret
	 *            商户工作密钥
	 * @param channelType
	 *            支付类型 对于支付手机APP端目前只支持WX_APP, ALI_APP, UN_APP
	 * @see cn.wd.checkout.api.WDReqParams.WDChannelTypes
	 * @param submerno
	 *            子商户号 区别接入商户，商户在健康云（医药云）注册的商户号
	 * @param billTitle
	 *            商品名称, 32个字节内, 汉字以2个字节计
	 * @param goods_desc
	 *            商品描述
	 * @param billTotalFee
	 *            支付金额，以分为单位，必须是正整数
	 * @param billNum
	 *            商户自定义订单号
	 * @param order_desc
	 *            订单描述
	 * @param optional
	 *            为扩展参数，可以传入任意数量的key/value对来补充对业务逻辑的需求
	 * @param mHandler
	 *            支付完成后的回调函数 UI层通知
	 */
	public void reqPayAsync(String appId, String appSecret,
			final WDReqParams.WDChannelTypes channelType,
			final String submerno, final String billTitle,
			final String goods_desc, final Long billTotalFee,
			final String billNum, final String order_desc,
			final Map<String, String> optional, Handler mHandler) {
		payCallback = null;
		if (mHandler == null) {
			WDLogUtil.e(TAG, "请初始化mHandler");
			return;
		}

		if (appId == null || appSecret == null) {
			Message msg = payHandler.obtainMessage();
			msg.what = WDPayResult.RESULT_FAIL_HANDLER;
			msg.obj = WDPayResult.FAIL_INVALID_PARAMS
					+ " /parameters: 请设置支付平台appId和appSecret";
			payHandler.sendMessage(msg);
			return;
		}

		WDCache instance = WDCache.getInstance(null);
		instance.appId = appId;
		instance.appSecret = appSecret;

		if (WDReqParams.WDChannelTypes.alipay.equals(channelType)
				|| WDReqParams.WDChannelTypes.wepay.equals(channelType)
				|| WDReqParams.WDChannelTypes.uppay.equals(channelType)
				|| WDReqParams.WDChannelTypes.wdepay.equals(channelType)
				|| WDReqParams.WDChannelTypes.perwallet_huarui.equals(channelType)) {
			this.reqPaymentAsync(channelType, submerno, billTitle, goods_desc,
					billTotalFee, billNum, order_desc, null, optional, mHandler);
		} else {
			if (mHandler != null) {
				Message msg = payHandler.obtainMessage();
				msg.what = WDPayResult.RESULT_FAIL_HANDLER;
				msg.obj = WDPayResult.FAIL_UNKNOWN_WAY + " /暂不支持的支付渠道！";
				payHandler.sendMessage(msg);
			}
		}
	}

	// /**
	// * 万达统一收银台支付方法 已过期
	// *
	// * @param channelType
	// * 支付类型 对于支付手机APP端目前只支持WX_APP, ALI_APP, UN_APP , lianpay
	// * @see cn.wd.checkout.api.WDReqParams.WDChannelTypes
	// * @param submerno
	// * 子商户号 区别接入商户，商户在健康云（医药云）注册的商户号
	// * @param billTitle
	// * 商品名称, 32个字节内, 汉字以2个字节计
	// * @param goods_desc
	// * 商品描述
	// * @param billTotalFee
	// * 支付金额，以分为单位，必须是正整数
	// * @param billNum
	// * 商户自定义订单号
	// * @param order_desc
	// * 订单描述
	// * @param optional
	// * 为扩展参数，可以传入任意数量的key/value对来补充对业务逻辑的需求
	// * @param callback
	// * 支付完成后的回调函数
	// */
	// public void reqPayAsync(final WDReqParams.WDChannelTypes channelType,
	// final String submerno, final String billTitle,
	// final String goods_desc, final Long billTotalFee,
	// final String billNum, final String order_desc,
	// final Map<String, String> optional, final WDCallBack callback) {
	// payHandler = null;
	// if (callback == null) {
	// WDLogUtil.e(TAG, "请初始化callback");
	// return;
	// }
	//
	// if (WDReqParams.WDChannelTypes.alipay.equals(channelType)) {
	//
	// this.reqAliPaymentAsync(billTitle, submerno, goods_desc,
	// billTotalFee, billNum, order_desc, optional, callback);
	// } else if (WDReqParams.WDChannelTypes.wepay.equals(channelType)) {
	// this.reqWXPaymentAsync(billTitle, submerno, goods_desc,
	// billTotalFee, billNum, order_desc, optional, callback);
	//
	// } else if (WDReqParams.WDChannelTypes.uppay.equals(channelType)) {
	// this.reqUnionPaymentAsync(billTitle, submerno, goods_desc,
	// billTotalFee, billNum, order_desc, optional, callback);
	// } else if (WDReqParams.WDChannelTypes.wdepay.equals(channelType)) {
	// this.reqLianPaymentAsync(billTitle, submerno, goods_desc,
	// billTotalFee, billNum, order_desc, optional, callback);
	// } else {
	// if (callback != null) {
	// callback.done(new WDPayResult(WDPayResult.RESULT_FAIL,
	// WDPayResult.FAIL_UNKNOWN_WAY, "暂不支持的支付渠道！"));
	// }
	// }
	// }
	//
	// /**
	// * 万达统一收银台支付方法 已过期
	// *
	// * @param channelType
	// * 支付类型 对于支付手机APP端目前只支持WX_APP, ALI_APP, UN_APP
	// * @see cn.wd.checkout.api.WDReqParams.WDChannelTypes
	// * @param submerno
	// * 子商户号 区别接入商户，商户在健康云（医药云）注册的商户号
	// * @param billTitle
	// * 商品名称, 32个字节内, 汉字以2个字节计
	// * @param goods_desc
	// * 商品描述
	// * @param billTotalFee
	// * 支付金额，以分为单位，必须是正整数
	// * @param billNum
	// * 商户自定义订单号
	// * @param order_desc
	// * 订单描述
	// * @param optional
	// * 为扩展参数，可以传入任意数量的key/value对来补充对业务逻辑的需求
	// * @param mHandler
	// * 支付完成后的回调函数 UI层通知
	// */
	// public void reqPayAsync(final WDReqParams.WDChannelTypes channelType,
	// final String submerno, final String billTitle,
	// final String goods_desc, final Long billTotalFee,
	// final String billNum, final String order_desc,
	// final Map<String, String> optional, Handler mHandler) {
	// payCallback = null;
	// if (mHandler == null) {
	// WDLogUtil.e(TAG, "请初始化mHandler");
	// return;
	// }
	//
	// if (WDReqParams.WDChannelTypes.alipay.equals(channelType)) {
	//
	// this.reqPaymentAsync(WDReqParams.WDChannelTypes.alipay, submerno,
	// billTitle, goods_desc, billTotalFee, billNum, order_desc,
	// null, optional, mHandler);
	//
	// } else if (WDReqParams.WDChannelTypes.wepay.equals(channelType)) {
	// this.reqPaymentAsync(WDReqParams.WDChannelTypes.wepay, submerno,
	// billTitle, goods_desc, billTotalFee, billNum, order_desc,
	// null, optional, mHandler);
	//
	// } else if (WDReqParams.WDChannelTypes.uppay.equals(channelType)) {
	// this.reqPaymentAsync(WDReqParams.WDChannelTypes.uppay, submerno,
	// billTitle, goods_desc, billTotalFee, billNum, order_desc,
	// null, optional, mHandler);
	//
	// } else if(WDReqParams.WDChannelTypes.wdepay.equals(channelType)){
	// this.reqPaymentAsync(WDReqParams.WDChannelTypes.wdepay, submerno,
	// billTitle, goods_desc, billTotalFee, billNum, order_desc,
	// null, optional, mHandler);
	// }else{
	// if (mHandler != null) {
	// Message msg = payHandler.obtainMessage();
	// msg.what = WDPayResult.RESULT_FAIL_HANDLER;
	// msg.obj = WDPayResult.FAIL_UNKNOWN_WAY + " /暂不支持的支付渠道！";
	// payHandler.sendMessage(msg);
	// }
	// }
	// }

	static Activity getContextActivity() {
		return mContextActivity;
	}

	/**
	 * 初始化微信支付，必须在需要调起微信支付的Activity的onCreate函数中调用，例如：
	 * WDPay.initWechatPay(XXActivity.this); 微信支付只有经过初始化才能成功调起，其他支付渠道无此要求。
	 * 
	 * @param context
	 *            需要在某Activity里初始化微信支付，此参数需要传递该Activity.this，不能为null
	 * @return 返回出错信息，如果成功则为null
	 */
	private static String initWechatPay(Context context, String wechatAppID) {
		String errMsg = null;

		if (context == null) {
			errMsg = "Error: initWechatPay里，context参数不能为null.";
			WDLogUtil.e(TAG, errMsg);
			return errMsg;
		}

		if (wechatAppID == null || wechatAppID.length() == 0) {
			errMsg = "Error: initWechatPay里，wx_appid必须为合法的微信AppID.";
			WDLogUtil.e(TAG, errMsg);
			return errMsg;
		}

		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		wxAPI = WXAPIFactory.createWXAPI(context, null);

		WDCache.getInstance(null).wxAppId = wechatAppID;

		try {
			if (isWXPaySupported()) {
				// 将该app注册到微信
				wxAPI.registerApp(wechatAppID);
			} else {
				errMsg = "Error: 安装的微信版本不支持支付.";
				WDLogUtil.d(TAG, errMsg);
			}
		} catch (Exception ignored) {
			errMsg = "Error: 无法注册微信 " + wechatAppID + ". Exception: "
					+ ignored.getMessage();
			WDLogUtil.e(TAG, errMsg);
		}

		return errMsg;
	}

	/**
	 * 判断微信是否支持支付
	 * 
	 * @return true表示支持
	 */
	public static boolean isWXPaySupported() {
		boolean isPaySupported = false;
		if (wxAPI != null) {
			isPaySupported = wxAPI.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
		}
		return isPaySupported;
	}

	/**
	 * 判断微信客户端是否安装并被支持
	 * 
	 * @return true表示支持
	 */
	public static boolean isWXAppInstalledAndSupported() {
		boolean isWXAppInstalledAndSupported = false;
		if (wxAPI != null) {
			isWXAppInstalledAndSupported = wxAPI.isWXAppInstalled()
					&& wxAPI.isWXAppSupportAPI();
		}
		return isWXAppInstalledAndSupported;
	}

	/**
	 * 支付调用总接口
	 * 
	 * @param channelType
	 *            支付类型 对于支付手机APP端目前只支持WX_APP, ALI_APP, UN_APP
	 * @see cn.wd.checkout.api.WDReqParams.WDChannelTypes
	 * @param submerno
	 *            子商户号 区别接入商户，商户在健康云（医药云）注册的商户号
	 * @param billTitle
	 *            商品名称, 32个字节内, 汉字以2个字节计
	 * @param billTitle
	 *            商品描述
	 * @param billTotalFee
	 *            支付金额，以分为单位，必须是正整数
	 * @param billNum
	 *            商户自定义订单号
	 * @param billNum_desc
	 *            订单号描述
	 * @param billTimeout
	 *            订单超时时间，以秒为单位，可以为null
	 * @param optional
	 *            为扩展参数，可以传入任意数量的key/value对来补充对业务逻辑的需求
	 * @param callback
	 *            支付完成后的回调函数
	 */
	private void reqPaymentAsync(final WDReqParams.WDChannelTypes channelType,
			final String submerno, final String billTitle,
			final String billTitle_desc, final Long billTotalFee,
			final String billNum, final String billNum_desc,
			final Integer billTimeout, final Map<String, String> optional,
			final WDCallBack callback) {

		if (callback == null) {
			WDLogUtil.e(TAG, "请初始化callback");
			return;
		}

		if (WDCache.getInstance(mContextActivity).getIsPaying()) {
			WDLogUtil.e(TAG, "已经在处理支付，此次支付请求抛弃！！！！！！");
			return;
		}

		WDCache.getInstance(mContextActivity).setIsPaying(true);

		payCallback = callback;

		WDCache.executorService.execute(new Runnable() {
			@Override
			public void run() {

				// 校验并准备公用参数
				WDPayReqParams parameters;
				try {
					parameters = new WDPayReqParams(channelType);
				} catch (WDException e) {
					callback.done(new WDPayResult(WDPayResult.RESULT_FAIL,
							WDPayResult.FAIL_EXCEPTION, e.getMessage()));
					WDCache.getInstance(mContextActivity).setIsPaying(false);
					return;
				}

				String paramValidRes = prepareParametersForPay(submerno,
						billTitle, billTitle_desc, billTotalFee, billNum,
						billNum_desc, optional, parameters);

				if (paramValidRes != null) {
					callback.done(new WDPayResult(WDPayResult.RESULT_FAIL,
							WDPayResult.FAIL_INVALID_PARAMS, paramValidRes));
					WDCache.getInstance(mContextActivity).setIsPaying(false);
					return;
				}

				parameters.billTimeout = billTimeout;

				String payURL = WDHttpClientUtil
						.getBillPayURL(mContextActivity);
				WDLogUtil.i(
						TAG,
						"请求数据：payURL=" + payURL + "::"
								+ parameters.transToBillReqMapParams());
				HttpResponse response = WDHttpClientUtil.httpPost(payURL,
						parameters.transToBillReqMapParams());

				if (null == response) {
					callback.done(new WDPayResult(WDPayResult.RESULT_FAIL,
							WDPayResult.FAIL_NETWORK_ISSUE, "Network Error"));
					WDCache.getInstance(mContextActivity).setIsPaying(false);
					return;
				}
				if (response.getStatusLine().getStatusCode() == 200) {
					String ret;
					try {
						ret = EntityUtils.toString(response.getEntity(),
								"UTF-8");

						WDLogUtil.i(TAG, "返回数据：" + ret);
						// 反序列化json串
						Gson res = new Gson();

						Type type = new TypeToken<Map<String, Object>>(){
						}.getType();
						Map<String, Object> responseMap = res.fromJson(ret,
								type);

						// //判断后台返回结果
						String resultCode = (String) responseMap.get("retcode");
						if (WDHttpClientUtil.Success.equals(resultCode)) {
							if (mContextActivity != null) {
								// 针对不同的支付渠道调用不同的API
								switch (channelType) {
								case wepay:
									reqWXPaymentViaAPP(responseMap);
									break;
								case alipay:
									reqAliPaymentViaAPP(responseMap);
									break;
								case uppay:
									reqUnionPaymentViaAPP(responseMap);
									break;
								case wdepay:
									reqLianPaymentViaAPP(responseMap);
									break;
								case perwallet_huarui:
									reqWalletPaymentViaAPP(billTitle,
											billTitle_desc, billTotalFee,
											billNum, billNum_desc, billTimeout,
											optional, callback);
									break;
								default:
									callback.done(new WDPayResult(
											WDPayResult.RESULT_FAIL,
											WDPayResult.FAIL_INVALID_PARAMS,
											"channelType参数不合法"));
									WDCache.getInstance(mContextActivity)
											.setIsPaying(false);
								}

							} else {
								callback.done(new WDPayResult(
										WDPayResult.RESULT_FAIL,
										WDPayResult.FAIL_INVALID_PARAMS,
										"Context-Activity Exception in reqAliPayment"));
								WDCache.getInstance(mContextActivity)
										.setIsPaying(false);
							}
						} else {
							// 返回后端传回的错误信息
							callback.done(new WDPayResult(
									WDPayResult.RESULT_FAIL,
									WDPayResult.FAIL_ERR_FROM_SERVER, String
											.valueOf(responseMap.get("retmsg"))));
							WDCache.getInstance(mContextActivity).setIsPaying(
									false);
						}

					} catch (IOException e) {
						callback.done(new WDPayResult(WDPayResult.RESULT_FAIL,
								WDPayResult.FAIL_NETWORK_ISSUE,
								"Invalid Response"));
						WDCache.getInstance(mContextActivity)
								.setIsPaying(false);
					}
				} else {
					callback.done(new WDPayResult(WDPayResult.RESULT_FAIL,
							WDPayResult.FAIL_NETWORK_ISSUE, "Network Error"));
					WDCache.getInstance(mContextActivity).setIsPaying(false);
				}

			}
		});
	}

	/**
	 * 支付调用总接口
	 * 
	 * @param channelType
	 *            支付类型 对于支付手机APP端目前只支持WX_APP, ALI_APP, UN_APP
	 * @see cn.wd.checkout.api.WDReqParams.WDChannelTypes
	 * @param submerno
	 *            子商户号 区别接入商户，商户在健康云（医药云）注册的商户号
	 * @param billTitle
	 *            商品名称, 32个字节内, 汉字以2个字节计
	 * @param billTitle
	 *            商品描述
	 * @param billTotalFee
	 *            支付金额，以分为单位，必须是正整数
	 * @param billNum
	 *            商户自定义订单号
	 * @param billNum_desc
	 *            订单号描述
	 * @param billTimeout
	 *            订单超时时间，以秒为单位，可以为null
	 * @param optional
	 *            为扩展参数，可以传入任意数量的key/value对来补充对业务逻辑的需求
	 * @param mHandler
	 *            支付完成后的回调 UI层回调通知
	 */
	private void reqPaymentAsync(final WDReqParams.WDChannelTypes channelType,
			final String submerno, final String billTitle,
			final String billTitle_desc, final Long billTotalFee,
			final String billNum, final String billNum_desc,
			final Integer billTimeout, final Map<String, String> optional,
			final Handler mHandler) {

		if (mHandler == null) {
			WDLogUtil.e(TAG, "请初始化mHandler");
			return;
		}

		if (WDCache.getInstance(mContextActivity).getIsPaying()) {
			WDLogUtil.e(TAG, "正在处理支付中，此次支付请求抛弃！！！！！！");
			return;
		}

		WDCache.getInstance(mContextActivity).setIsPaying(true);

		payHandler = mHandler;

		WDCache.executorService.execute(new Runnable() {
			@Override
			public void run() {

				// 校验并准备公用参数
				WDPayReqParams parameters;
				try {
					parameters = new WDPayReqParams(channelType);
				} catch (WDException e) {
					Message msg = payHandler.obtainMessage();
					msg.what = WDPayResult.RESULT_FAIL_HANDLER;
					msg.obj = WDPayResult.FAIL_EXCEPTION + " /"
							+ e.getMessage();
					payHandler.sendMessage(msg);
					WDCache.getInstance(mContextActivity).setIsPaying(false);
					return;
				}
				String paramValidRes = prepareParametersForPay(submerno,
						billTitle, billTitle_desc, billTotalFee, billNum,
						billNum_desc, optional, parameters);
				if (paramValidRes != null) {
					Message msg = payHandler.obtainMessage();
					msg.what = WDPayResult.RESULT_FAIL_HANDLER;
					msg.obj = WDPayResult.FAIL_INVALID_PARAMS + " /"
							+ paramValidRes;
					payHandler.sendMessage(msg);
					WDCache.getInstance(mContextActivity).setIsPaying(false);
					return;
				}

				parameters.billTimeout = billTimeout;

				String payURL = WDHttpClientUtil
						.getBillPayURL(mContextActivity);
				WDLogUtil.i(
						TAG,
						"请求数据：payURL=" + payURL + "::"
								+ parameters.transToBillReqMapParams());
				HttpResponse response = WDHttpClientUtil.httpPost(payURL,
						parameters.transToBillReqMapParams());
				if (null == response) {
					Message msg = payHandler.obtainMessage();
					msg.what = WDPayResult.RESULT_FAIL_HANDLER;
					msg.obj = WDPayResult.FAIL_NETWORK_ISSUE
							+ " /Network Error";
					payHandler.sendMessage(msg);
					WDCache.getInstance(mContextActivity).setIsPaying(false);
					return;
				}
				if (response.getStatusLine().getStatusCode() == 200) {
					String ret;
					try {
						ret = EntityUtils.toString(response.getEntity(),
								"UTF-8");

						WDLogUtil.i("ret=" + ret);
						// 反序列化json串
						Gson res = new Gson();

						Type type = new TypeToken<Map<String, Object>>() {
						}.getType();
						Map<String, Object> responseMap = res.fromJson(ret,
								type);

						// //判断后台返回结果
						String resultCode = (String) responseMap.get("retcode");
						if (WDHttpClientUtil.Success.equals(resultCode)) {

							if (mContextActivity != null) {

								// 针对不同的支付渠道调用不同的API
								switch (channelType) {
								case wepay:
									reqWXPaymentViaAPP(responseMap);
									break;
								case alipay:
									reqAliPaymentViaAPP(responseMap);
									break;
								case uppay:
									reqUnionPaymentViaAPP(responseMap);
									break;
								case wdepay:
									reqLianPaymentViaAPP(responseMap);
									break;
								default:
									Message msg = payHandler.obtainMessage();
									msg.what = WDPayResult.RESULT_FAIL_HANDLER;
									msg.obj = WDPayResult.FAIL_INVALID_PARAMS
											+ " /channelType参数不合法";
									payHandler.sendMessage(msg);
									WDCache.getInstance(mContextActivity)
											.setIsPaying(false);
								}

							} else {
								Message msg = payHandler.obtainMessage();
								msg.what = WDPayResult.RESULT_FAIL_HANDLER;
								msg.obj = WDPayResult.FAIL_INVALID_PARAMS
										+ " /Context-Activity Exception in reqAliPayment";
								payHandler.sendMessage(msg);
								WDCache.getInstance(mContextActivity)
										.setIsPaying(false);
							}
						} else {
							// 返回后端传回的错误信息
							Message msg = payHandler.obtainMessage();
							msg.what = WDPayResult.RESULT_FAIL_HANDLER;
							msg.obj = WDPayResult.FAIL_ERR_FROM_SERVER + " /"
									+ String.valueOf(responseMap.get("retmsg"));
							payHandler.sendMessage(msg);
							WDCache.getInstance(mContextActivity).setIsPaying(
									false);
						}

					} catch (IOException e) {
						Message msg = payHandler.obtainMessage();
						msg.what = WDPayResult.RESULT_FAIL_HANDLER;
						msg.obj = WDPayResult.FAIL_NETWORK_ISSUE
								+ " /Invalid Response";
						payHandler.sendMessage(msg);
						WDCache.getInstance(mContextActivity)
								.setIsPaying(false);
					}
				} else {
					Message msg = payHandler.obtainMessage();
					msg.what = WDPayResult.RESULT_FAIL_HANDLER;
					msg.obj = WDPayResult.FAIL_NETWORK_ISSUE
							+ " /Network Error";
					payHandler.sendMessage(msg);
					WDCache.getInstance(mContextActivity).setIsPaying(false);
				}

			}
		});
	}

	/**
	 * 校验bill参数 设置公用参数
	 * 
	 * @param submerno
	 *            子商户号 区别接入商户，商户在健康云（医药云）注册的商户号
	 * @param billTitle
	 *            商品描述, 32个字节内, 汉字以2个字节计
	 * @param billTitle_desc
	 *            商品详细描述
	 * @param billTotalFee
	 *            支付金额，以分为单位，必须是正整数
	 * @param billNum
	 *            商户自定义订单号
	 * @param billNum
	 *            订单描述
	 * @param parameters
	 *            用于存储公用信息
	 * @param optional
	 *            为扩展参数，可以传入任意数量的key/value对来补充对业务逻辑的需求
	 * @return 返回校验失败信息, 为null则表明校验通过
	 */
	private String prepareParametersForPay(final String submerno,
			final String billTitle, final String billTitle_desc,
			final Long billTotalFee, final String billNum,
			final String billNum_desc, final Map<String, String> optional,
			WDPayReqParams parameters) {

		if (!WDValidationUtil.isValidBillTitleLength(billTitle)) {
			return "parameters: 不合法的参数-订单标题长度不合法, 32个字节内, 汉字以2个字节计";
		}

		if (!WDValidationUtil.isValidBillNum(billNum))
			return "parameters: 订单号必须是长度8~32位字母和数字组合成的字符串";

		if (billTotalFee < 0) {
			return "parameters: billTotalFee " + billTotalFee
					+ " 格式不正确, 必须是以分为单位的正整数, 比如100表示1元";
		}

		if (!WDValidationUtil.isValidString(billTitle_desc)) {
			return "parameters: billTitle_desc " + billTitle_desc + "商品描述不可为空！";
		}

		parameters.title = billTitle;
		parameters.goods_desc = billTitle_desc;
		parameters.totalFee = billTotalFee;
		parameters.billNum = billNum;
		parameters.order_desc = billNum_desc;
		parameters.optional = optional;
		parameters.submerno = submerno;

		return null;
	}

	/**
	 * 与服务器交互后下一步进入支付宝app支付
	 * 
	 * @param responseMap
	 *            服务端返回参数
	 */
	private void reqAliPaymentViaAPP(final Map<String, Object> responseMap) {

		String orderString = (String) responseMap.get("retsign");

		PayTask aliPay = new PayTask(mContextActivity);
		String aliResult = aliPay.pay(orderString, true);

		// 解析ali返回结果
		Pattern pattern = Pattern.compile("resultStatus=\\{(\\d+?)\\}");
		Matcher matcher = pattern.matcher(aliResult);
		String resCode = "";
		if (matcher.find())
			resCode = matcher.group(1);

		String result;
		String errMsg;

		int msgWhat;

		// 9000-订单支付成功, 8000-正在处理中, 4000-订单支付失败, 6001-用户中途取消, 6002-网络连接出错
		String errDetail;
		if (resCode.equals("9000")) {
			msgWhat = WDPayResult.RESULT_SUCCESS_HANDLER;
			result = WDPayResult.RESULT_SUCCESS;
			errMsg = WDPayResult.RESULT_SUCCESS;
			errDetail = errMsg;
		} else if (resCode.equals("6001")) {
			msgWhat = WDPayResult.RESULT_CANCEL_HANDLER;
			result = WDPayResult.RESULT_CANCEL;
			errMsg = WDPayResult.RESULT_CANCEL;
			errDetail = errMsg;
		} else if (resCode.equals("8000")) {
			msgWhat = WDPayResult.RESULT_FAIL_HANDLER;
			result = WDPayResult.RESULT_FAIL;
			errMsg = WDPayResult.RESULT_PAYING_UNCONFIRMED;
			errDetail = "订单正在处理中，无法获取成功确认信息";
		} else {
			msgWhat = WDPayResult.RESULT_FAIL_HANDLER;
			result = WDPayResult.RESULT_FAIL;
			errMsg = WDPayResult.FAIL_ERR_FROM_CHANNEL;

			if (resCode.equals("4000")) {
				errDetail = "订单支付失败";
			} else if (resCode.equals("6002")) {
				errDetail = "网络连接出错";
			} else
				errDetail = "未知错误 /" + resCode;
		}

		if (payCallback != null)
			payCallback.done(new WDPayResult(result, errMsg, errDetail));

		if (payHandler != null) {
			Message msg = payHandler.obtainMessage();
			msg.what = msgWhat;
			msg.obj = errMsg + " /" + errDetail;
			payHandler.sendMessage(msg);
		}
		WDCache.getInstance(mContextActivity).setIsPaying(false);
	}

	/**
	 * 与服务器交互后下一步进入个人钱包支付
	 * 
	 * @param billTitle
	 *            商品名称, 32个字节内, 汉字以2个字节计
	 * @param billTitle_desc
	 *            商品描述
	 * @param billTotalFee
	 *            支付金额，以分为单位，必须是正整数
	 * @param billNum
	 *            商户自定义订单号
	 * @param billNum_desc
	 *            订单号描述
	 * @param billTimeout
	 *            订单超时时间，以秒为单位，可以为null
	 * @param optional
	 *            为扩展参数，可以传入任意数量的key/value对来补充对业务逻辑的需求
	 * @param callback
	 *            支付完成后的回调函数
	 * 
	 */
	private void reqWalletPaymentViaAPP(String billTitle,
			String billTitle_desc, Long billTotalFee, String billNum,
			String billNum_desc, Integer billTimeout,
			Map<String, String> optional, WDCallBack callback) {
		

		if(null==optional){
			payCallback
			.done(new WDPayResult(WDPayResult.RESULT_FAIL, WDPayResult.FAIL_INVALID_PARAMS, WDPayResult.FAIL_INVALID_PARAMS));
			return;
		}
		
		String walletAppID = optional.get("walletAppID");
		String userID = optional.get("merUserId");
		String outTradeNo = optional.get("outTradeNo");
		String goodsTag = optional.get("goodsTag");
		String deviceInfo = optional.get("deviceInfo");
		String openID = optional.get("openID");
		String personUnionID = optional.get("personUnionID");
		
		
		if(null==userID || null == outTradeNo || null == openID || null == personUnionID){
			payCallback
			.done(new WDPayResult(WDPayResult.RESULT_FAIL, WDPayResult.FAIL_INVALID_PARAMS, WDPayResult.FAIL_INVALID_PARAMS));
			return;
		}

		Map<String, Object> param = new HashMap<String, Object>();
		/** 当前Activity */
		param.put("Activity", mContextActivity);
		/** 商户AppId */
		param.put("merAppId", walletAppID);
		/** 商户用户ID */
		param.put("merUserId", optional.get("merUserId"));
		/** 商户订单号 */
		param.put("outTradeNo", optional.get("outTradeNo"));
		/** 商品描述 */
		param.put("body", billTitle+"");
		/** 商品详情 */
		param.put("detail", billTitle_desc+"");
		/** 附加数据 */
		param.put("attach", "附加数据");
		/** 确认支付金额 */
		param.put("confirmOrder", billTotalFee+"");
		/** 展示金额 */
		param.put("payAmt", billTotalFee+"");
		/** 优惠金额 */
		param.put("discountAmt", "0");
		/** 支付金额 */
		param.put("payFee", billTotalFee+"");
		/** 商品标记 */
		param.put("goodsTag", optional.get("goodsTag"));
		/** 交易有效时间 */
		param.put("timeValid", billTimeout+"");
		/** 硬件信息 */
		param.put("deviceInfo", optional.get("deviceInfo"));

		/** 用户ID */
		param.put("openID", optional.get("openID"));
		/** 开通钱包标识 */
		param.put("personUnionID", optional.get("personUnionID"));
		
		WDPwSDK.getInstance().SpendSDK(param, new WDResultCallback() {
			/**
			 * returnMsg 返回信息 returnCode 返回码 payID 支付流水号 outTradeNo 商户订单号 sign
			 * 签名 tradeType random 随机数 discountAmt 优惠金额 mchID 商户号 payFee 支付金额
			 * 实际支付金额 payAmt 订单金额 展示金额
			 */
			@Override
			public void onSucess(Map<String, Object> callbackMap) {
				String info = "SpendSDK - sendSuccessMsg :"
						+ callbackMap.toString();
				WDLogUtil.e("WalletPAY", " info = " +info);
				String result1 = WDPayResult.RESULT_SUCCESS;
				String errMsg = WDPayResult.RESULT_SUCCESS;
				int msgWhat = WDPayResult.RESULT_SUCCESS_HANDLER;
				String errDetail = errMsg;
				
				String walletReturnCode = callbackMap.get("returnCode").toString();
				if("CANCEL".equals(walletReturnCode)){
					result1 = WDPayResult.RESULT_CANCEL;
				errMsg = (String) callbackMap.get("returnMsg");
				errDetail = errMsg;
				
				}else if("CHHR_ERROR".equals(walletReturnCode)){
					result1 = WDPayResult.RESULT_FAIL;
					errMsg = (String) callbackMap.get("returnMsg");
					errDetail = errMsg;
				}
				
				WDLogUtil.e("WalletPAY", " result1 = " + result1 + " errMsg = "
						+ errMsg + " errDetail = " + errDetail);
				
				
				if (payCallback != null)
					payCallback
							.done(new WDPayResult(result1, errMsg, errDetail));

				if (payHandler != null) {
					Message msg = payHandler.obtainMessage();
					msg.what = msgWhat;
					msg.obj = errMsg + " /" + errDetail;
					payHandler.sendMessage(msg);
				}
			}

			@Override
			public void onFail(Map<String, Object> callbackMap) {
				String info = "SpendSDK - sendFailMsg :"
						+ callbackMap.toString();
				WDLogUtil.e("WalletPAY", " info = " +info);
				
				String returnCode = (String) callbackMap.get("returnCode");
				String returnMsg = (String) callbackMap.get("returnMsg");

				String result1;
				String errMsg;
				int msgWhat;
				String errDetail;

				if ("CANCEL".equals(returnCode)) {
					// 用户取消
					msgWhat = WDPayResult.RESULT_CANCEL_HANDLER;
					result1 = WDPayResult.RESULT_CANCEL;
					errMsg = WDPayResult.RESULT_CANCEL;
					errDetail = errMsg;
				} else {
					msgWhat = WDPayResult.RESULT_FAIL_HANDLER;
					result1 = WDPayResult.RESULT_FAIL;
					errMsg = WDPayResult.RESULT_PAYING_UNCONFIRMED;
					errDetail = returnMsg;
				}
				WDLogUtil.e("WalletPAY", " result1 = " + result1 + " errMsg = "
						+ errMsg + " errDetail = " + errDetail);
				if (payCallback != null)
					payCallback
							.done(new WDPayResult(result1, errMsg, errDetail));

				if (payHandler != null) {
					Message msg = payHandler.obtainMessage();
					msg.what = msgWhat;
					msg.obj = errMsg + " /" + errDetail;
					payHandler.sendMessage(msg);
				}

			}
		});

		WDCache.getInstance(mContextActivity).setIsPaying(false);
	}

	/**
	 * 与服务器交互后下一步进入链支付app支付
	 * 
	 * @param responseMap
	 */
	private void reqLianPaymentViaAPP(Map<String, Object> responseMap) {
		// TODO Auto-generated method stub
		LPPayApi lpPayApi = LPPayApi.getInstance(mContextActivity);
		String way = WDCache.getInstance(mContextActivity).lianrequestWay;
		if ("TS".equals(way)) {
			lpPayApi.setPayWay("TS");
		}
		String paramStr = (String) responseMap.get("retsign");
		lpPayApi.pay(paramStr, new LPCallBack() {

			@Override
			public void done(LPPayResult result) {
				int resCode = result.getResult();
				String result1;
				String errMsg;
				int msgWhat;
				// LPPayResult.RESULT_SUCCESS-订单支付成功, LPPayResult.PARAMS_NULL &
				// LPPayResult.PARAMS_VIOLATIONS-订单支付失败,
				// LPPayResult.RESULT_CANCEL-用户中途取消,
				String errDetail;
				if (resCode == LPPayResult.RESULT_SUCCESS) {
					msgWhat = WDPayResult.RESULT_SUCCESS_HANDLER;
					result1 = WDPayResult.RESULT_SUCCESS;
					errMsg = WDPayResult.RESULT_SUCCESS;
					errDetail = errMsg;
				} else if (resCode == LPPayResult.RESULT_CANCEL) {
					msgWhat = WDPayResult.RESULT_CANCEL_HANDLER;
					result1 = WDPayResult.RESULT_CANCEL;
					errMsg = WDPayResult.RESULT_CANCEL;
					errDetail = errMsg;
				} else if (resCode == LPPayResult.PARAMS_NULL
						|| resCode == LPPayResult.PARAMS_VIOLATIONS) {
					msgWhat = WDPayResult.RESULT_FAIL_HANDLER;
					result1 = WDPayResult.RESULT_FAIL;
					errMsg = WDPayResult.RESULT_PAYING_UNCONFIRMED;
					errDetail = result.getErrMsg();
				} else {
					msgWhat = WDPayResult.RESULT_FAIL_HANDLER;
					result1 = WDPayResult.RESULT_FAIL;
					errMsg = WDPayResult.FAIL_ERR_FROM_CHANNEL
							+ result.getErrMsg();

					// if (resCode==LPPayResult.RESULT_FAIL)
					errDetail = "订单支付失败";
					// else
					// errDetail = "网络连接出错";
				}

				WDLogUtil.i("WDPAY", "resCode = " + resCode + " result1 = "
						+ result1 + " errMsg = " + errMsg + " errDetail = "
						+ errDetail);
				if (payCallback != null)
					payCallback
							.done(new WDPayResult(result1, errMsg, errDetail));

				if (payHandler != null) {
					Message msg = payHandler.obtainMessage();
					msg.what = msgWhat;
					msg.obj = errMsg + " /" + errDetail;
					payHandler.sendMessage(msg);
				}
			}
		});
		WDCache.getInstance(mContextActivity).setIsPaying(false);
	}

	/**
	 * 与服务器交互后下一步进入微信app支付
	 * 
	 * @param responseMap
	 *            服务端返回参数
	 */
	private void reqWXPaymentViaAPP(final Map<String, Object> responseMap) {
		Map<String, Object> resMap = (Map<String, Object>) responseMap
				.get("message");
		// 获取到服务器的订单参数后，以下主要代码即可调起微信支付。
		PayReq request = new PayReq();
		request.appId = String.valueOf(resMap.get("appid"));
		request.partnerId = String.valueOf(resMap.get("partnerid"));
		request.prepayId = String.valueOf(resMap.get("prepayid"));
		request.packageValue = String.valueOf(resMap.get("pack_age"));
		request.nonceStr = String.valueOf(resMap.get("noncestr"));
		request.timeStamp = String.valueOf(resMap.get("timestamp"));
		request.sign = String.valueOf(resMap.get("sign"));
		WDLogUtil.w(TAG, "request.appId=" + request.appId
				+ " request.partnerId=" + request.partnerId
				+ " request.prepayId=" + request.prepayId
				+ " request.packageValue=" + request.packageValue
				+ " request.nonceStr=" + request.nonceStr
				+ " request.timeStamp=" + request.timeStamp + " request.sign="
				+ request.sign);

		instance.initWechatPay(mContextActivity, request.appId);

		if (WDPay.isWXAppInstalledAndSupported() && WDPay.isWXPaySupported()) {
			if (wxAPI != null) {
				wxAPI.sendReq(request);
			} else {
				if (payCallback != null)
					payCallback.done(new WDPayResult(WDPayResult.RESULT_FAIL,
							WDPayResult.FAIL_EXCEPTION, "Error: 微信API为空"));
				if (payHandler != null) {
					Message msg = payHandler.obtainMessage();
					msg.what = WDPayResult.RESULT_FAIL_HANDLER;
					msg.obj = WDPayResult.FAIL_EXCEPTION + " /Error: 微信API为空";
					payHandler.sendMessage(msg);
				}
			}
		} else {
			if (payCallback != null) {
				payCallback.done(new WDPayResult(WDPayResult.RESULT_FAIL,
						WDPayResult.FAIL_WEIXIN_VERSION_ERROR, "微信版本不支持支付操作"));
			} else {
				WDLogUtil.e(TAG, "请初始化callback");
			}
			if (payHandler != null) {
				Message msg = payHandler.obtainMessage();
				msg.what = WDPayResult.RESULT_FAIL_HANDLER;
				msg.obj = WDPayResult.FAIL_WEIXIN_VERSION_ERROR
						+ " /微信版本不支持支付操作";
				payHandler.sendMessage(msg);
			} else {
				WDLogUtil.e(TAG, "请初始化payHandler");
			}
		}

		WDCache.getInstance(mContextActivity).setIsPaying(false);
	}

	/**
	 * 与服务器交互后下一步进入银联app支付
	 * 
	 * @param responseMap
	 *            服务端返回参数
	 */
	private void reqUnionPaymentViaAPP(final Map<String, Object> responseMap) {

		Map<String, Object> resMap = (Map<String, Object>) responseMap
				.get("message");

		String TN = (String) resMap.get("tn");

		Intent intent = new Intent();
		intent.setClass(mContextActivity, WDUnionPaymentActivity.class);
		intent.putExtra("tn", TN);
		mContextActivity.startActivity(intent);

		WDCache.getInstance(mContextActivity).setIsPaying(false);
	}

}

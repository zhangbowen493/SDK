package com.wondersgroup.PwServer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request.Method;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.MyRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wondersgroup.request.IReqCallback;
import com.wondersgroup.utils.ResPonseBean;
import com.wondersgroup.utils.ResultGenerateUtil;
import com.wondersgroup.wallet.WDGlobalSetting;
import com.wondersgroup.wallet.WDLogUtil;
import com.wondersgroup.wallet.WDResultCallback;

/**
 * 个人钱包后台接口类
 * 
 * @author liuzhipeng
 *
 */
public class BackgroundInterface {

	public static final String HTTPS_SUCCESS = "00";

	/** 生产 */
	public static final String HTTPS_URL_P = "http://cash.wdepay.cn/";
	/** 测试 */
	public static final String HTTPS_URL_T = "http://cashkaif.wdepay.cn/";
//	public static final String HTTPS_URL_T = "http://172.16.31.140:8089/";
	/** 开发 */
	public static final String HTTPS_URL_D = "http://cashtest.wdepay.cn/";
//	public static final String HTTPS_URL_D = "http://10.1.64.212:8096/";

	/** 初始化 */
	public static final String INIT = "PersonalWalletWebServer/sdkinitial/sdkintial.xhtml";// 初始化
	/** 登陆 */
	public static final String Login = "PersonalWalletWebServer/sdklogin/quicklogincheck.xhtml";// 登陆
	/** 开通钱包 */
	public static final String BUILD = "PersonalWalletWebServer/skdaccount/openaccount.xhtml";// 开户绑卡
	/** 添加卡 */
	public static final String ADD_CARD = "00";// 添加卡
	/** 充值 */
	public static final String Recharge = "PersonalWalletWebServer/order/preRecharge";// 充值
	/** 消费 */
	public static final String Consume = "PersonalWalletWebServer/order/preConsume";// 消费
	/** 提现 */
	public static final String Getdeposit = "PersonalWalletWebServer/order/preGetdeposit";// 提现
	/** 余额查询 */
	public static final String QUERY_ACCOUNT_BALANCE = "PersonalWalletWebServer/accountbalance/accountbalance.xhtml";// 查询用户余额
	/** 商户订单查询 */
	public static final String QUERY_ORDER_LIST = "PersonalWalletWebServer/order/orderQuery";// 查询商户订单
	/** 用户交易明细查询 */
	public static final String QUERY_TRANSACTION_LIST = "PersonalWalletWebServer/trade/tradeQuery";// 查询用户交易明细
	/** 用户信息查询 */
	public static final String QUERY_USER_INFO = "PersonalWalletWebServer/userinfo/userinfo.xhtml";// 查询用户信息
	/** 绑卡列表查询 */
	public static final String QUERY_CARD = "PersonalWalletWebServer/cardslist/cardslistcheck.xhtml";// 查询用户信息

	private static String getURL(String path) {
		if (WDGlobalSetting.Environment_T.equals(WDGlobalSetting.Environment)) {
			WDLogUtil.e("后台地址：测试", HTTPS_URL_T + path);
			return HTTPS_URL_T + path;
		} else if (WDGlobalSetting.Environment_D.equals(WDGlobalSetting.Environment)) {
			WDLogUtil.e("后台地址：开发", HTTPS_URL_D + path);
			return HTTPS_URL_D + path;
		} else {
			WDLogUtil.e("后台地址：生产", HTTPS_URL_P + path);
			return HTTPS_URL_P + path;
		}
	}

	/**
	 * 网络请求报文参数统一处理
	 * 
	 * @param params
	 * @return
	 */
	private static Map<String, Object> getNetParams(Map<String, Object> params) {
		Map<String, Object> headMap = new HashMap<String, Object>();
		headMap.put("identifiCation", " ");
		headMap.put("timestamp", System.currentTimeMillis());
		headMap.put("version", "1.0");
		headMap.put("tradeCode", " ");
		headMap.put("tradeMsg", " ");
		headMap.put("sign", " ");

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("head", headMap);
		data.put("body", params);
		return data;
	}

	/**
	 * 初始化
	 * 
	 * @param params
	 * @param callback
	 */
	public static void init(Context context, Map<String, Object> params,
			final WDResultCallback callback) {

		WDGlobalSetting.getRequest(context).httpsPost(getURL(INIT),
				getNetParams(params), new IReqCallback() {
					@Override
					public void success(String response) {
						// TODO Auto-generated method stub
						WDLogUtil.e("DBG", response);
						JSONObject parseObject = JSON.parseObject(response);
						Map<String, Object> callbackMap = new HashMap<String, Object>();
						ResPonseBean resPonseBean = JSON.parseObject(
								parseObject.getString("body"),
								ResPonseBean.class);
						if (HTTPS_SUCCESS.equals(resPonseBean.returnCode)) {
							JSONObject parse = JSON
									.parseObject((String) resPonseBean.data);
							callbackMap.put("returnCode",
									resPonseBean.returnCode);
							callbackMap.put("returnMsg", resPonseBean.errorMsg);
							callbackMap.put("fundAppId",
									parse.getString("fundAppId"));
							callbackMap.put("random", parse.getString("random"));
							callbackMap.put("sign", parse.getString("sign"));
						} else {
							callbackMap.put("returnCode", "11"
									+ resPonseBean.errorCode);
							callbackMap.put("returnMsg", resPonseBean.errorMsg);
						}
						callback.onSucess(callbackMap);
					}

					@Override
					public void error(String error) {
						// TODO Auto-generated method stubi
						if (null == error)
							callback.onFail(ResultGenerateUtil
									.generateReturnMap(ResultGenerateUtil.ReturnCode.NET_ERROR.toString(),"NetWork Error"));
						else
							callback.onFail(ResultGenerateUtil
									.generateReturnMap(ResultGenerateUtil.ReturnCode.NET_ERROR.toString(),error));
					}
				});
	}

	/**
	 * 登录
	 * 
	 * @param params
	 * @param callback
	 */
	public static void login(Map<String, Object> params,
			final WDResultCallback callback) {
		Context context = (Context) params.get("Context");
		WDGlobalSetting.getRequest(context).httpsPost(getURL(Login),
				getNetParams(params), new IReqCallback() {
					@Override
					public void success(String response) {
						// TODO Auto-generated method stub
						JSONObject parseObject = JSON.parseObject(response);
						Map<String, Object> callbackMap = new HashMap<String, Object>();
						ResPonseBean resPonseBean = JSON.parseObject(
								parseObject.getString("body"),
								ResPonseBean.class);
						if (HTTPS_SUCCESS.equals(resPonseBean.returnCode)) {
							JSONObject parse = JSON
									.parseObject((String) resPonseBean.data);
							callbackMap.put("returnCode",
									resPonseBean.returnCode);
							callbackMap.put("returnMsg", resPonseBean.errorMsg);
							callbackMap.put("sequcence",
									parse.getString("sequcence"));
						} else {
							callbackMap.put("returnCode", "11"
									+ resPonseBean.errorCode);
							callbackMap.put("returnMsg", resPonseBean.errorMsg);
						}
						callback.onSucess(callbackMap);
					}

					@Override
					public void error(String error) {
						// TODO Auto-generated method stub
						if (null == error)
							callback.onFail(ResultGenerateUtil
									.generateReturnMap(ResultGenerateUtil.ReturnCode.NET_ERROR.toString(),"NetWork Error"));
						else
							callback.onFail(ResultGenerateUtil
									.generateReturnMap(ResultGenerateUtil.ReturnCode.NET_ERROR.toString(),error));
					}
				});
	}

	/**
	 * 开户
	 * 
	 * @param params
	 * @param callback
	 */
	public static void openAccount(Map<String, Object> params,
			final WDResultCallback callback) {
		Context context = (Context) params.get("Context");
		WDGlobalSetting.getRequest(context).httpsPost(getURL(BUILD),
				getNetParams(params), new IReqCallback() {
					@Override
					public void success(String response) {
						// TODO Auto-generated method stub
						JSONObject parseObject = JSON.parseObject(response);
						Map<String, Object> callbackMap = new HashMap<String, Object>();
						ResPonseBean resPonseBean = JSON.parseObject(
								parseObject.getString("body"),
								ResPonseBean.class);

						if (HTTPS_SUCCESS.equals(resPonseBean.returnCode)) {
							callbackMap.put("returnCode",
									resPonseBean.returnCode);
							callbackMap.put("returnMsg", resPonseBean.errorMsg);
						} else {
							callbackMap.put("returnCode", "11"
									+ resPonseBean.errorCode);
							callbackMap.put("returnMsg", resPonseBean.errorMsg);
						}
						callback.onSucess(callbackMap);
					}

					@Override
					public void error(String error) {
						// TODO Auto-generated method stub
						if (null == error)
							callback.onFail(ResultGenerateUtil
									.generateReturnMap(ResultGenerateUtil.ReturnCode.NET_ERROR.toString(),"NetWork Error"));
						else
							callback.onFail(ResultGenerateUtil
									.generateReturnMap(ResultGenerateUtil.ReturnCode.NET_ERROR.toString(),error));
					}
				});
	}

	/**
	 * 添加卡
	 * 
	 * @param params
	 * @param callback
	 */
	public static void addCard(Map<String, Object> params,
			final WDResultCallback callback) {
		Context context = (Context) params.get("Context");
		WDGlobalSetting.getRequest(context).httpsPost(getURL(ADD_CARD),
				getNetParams(params), new IReqCallback() {
					@Override
					public void success(String response) {
						// TODO Auto-generated method stub
						JSONObject parseObject = JSON.parseObject(response);
						Map<String, Object> callbackMap = new HashMap<String, Object>();
						ResPonseBean resPonseBean = JSON.parseObject(
								parseObject.getString("body"),
								ResPonseBean.class);
						if (HTTPS_SUCCESS.equals(resPonseBean.returnCode)) {
//							JSONObject parse = JSON
//									.parseObject((String) resPonseBean.data);
//							callbackMap.put("returnCode",
//									resPonseBean.returnCode);
//							callbackMap.put("returnMsg", resPonseBean.errorMsg);
//							callbackMap.put("outTradeNo",
//									parse.getString("outTradeNo"));
//							callbackMap.put("tranAmt",
//									parse.getString("tranAmt"));
//							callbackMap.put("random", parse.getString("random"));
//							callbackMap.put("sign", parse.getString("sign"));
//							callbackMap.put("merName",
//									parse.getString("merName"));
//							callbackMap.put("merId", parse.getString("merId"));
							callbackMap.put("response", response);
						} else {
							callbackMap.put("returnCode", "11"
									+ resPonseBean.errorCode);
							callbackMap.put("returnMsg", resPonseBean.errorMsg);
						}
						callback.onSucess(callbackMap);
					}

					@Override
					public void error(String error) {
						// TODO Auto-generated method stub
						if (null == error)
							callback.onFail(ResultGenerateUtil
									.generateReturnMap(ResultGenerateUtil.ReturnCode.NET_ERROR.toString(),"NetWork Error"));
						else
							callback.onFail(ResultGenerateUtil
									.generateReturnMap(ResultGenerateUtil.ReturnCode.NET_ERROR.toString(),error));
					}
				});
	}

	/**
	 * 查询账户余额 (后台返回报文格式需调整)
	 * 
	 * @param params
	 * @param callback
	 */
	public static void queryAccountBalance(Map<String, Object> params,
			final WDResultCallback callback) {
		Context context = (Context) params.get("Context");
		WDGlobalSetting.getRequest(context).httpsPost(
				getURL(QUERY_ACCOUNT_BALANCE), getNetParams(params),
				new IReqCallback() {
					@Override
					public void success(String response) {
						// TODO Auto-generated method stub
						JSONObject parseObject = JSON.parseObject(response);
						Map<String, Object> callbackMap = new HashMap<String, Object>();
						ResPonseBean resPonseBean = JSON.parseObject(
								parseObject.getString("body"),
								ResPonseBean.class);
						if (HTTPS_SUCCESS.equals(resPonseBean.returnCode)) {
							JSONObject BalanceObj = JSON
									.parseObject((String) resPonseBean.data);
							callbackMap.put("returnCode",
									resPonseBean.returnCode);
							callbackMap.put("returnMsg", resPonseBean.errorMsg);
							String balanceAmt = BalanceObj
									.getString("balanceAmt");
							callbackMap.put("balanceAmt", balanceAmt);
							callbackMap.put("response", response);
						} else {
							callbackMap.put("returnCode", "11"
									+ resPonseBean.errorCode);
							callbackMap.put("returnMsg", resPonseBean.errorMsg);
						}
						callback.onSucess(callbackMap);
					}

					@Override
					public void error(String error) {
						// TODO Auto-generated method stub
						if (null == error)
							callback.onFail(ResultGenerateUtil
									.generateReturnMap(ResultGenerateUtil.ReturnCode.NET_ERROR.toString(),"NetWork Error"));
						else
							callback.onFail(ResultGenerateUtil
									.generateReturnMap(ResultGenerateUtil.ReturnCode.NET_ERROR.toString(),error));
					}
				});
	}

	/**
	 * 充值
	 * 
	 * @param params
	 * @param callback
	 */
	public static void deposits(Map<String, Object> params,
			final WDResultCallback callback) {
		Context context = (Context) params.get("Context");
		WDGlobalSetting.getRequest(context).httpsPost(getURL(Recharge),
				getNetParams(params), new IReqCallback() {
					@Override
					public void success(String response) {
						WDLogUtil.e("DBG", "BackgroundInterface:SUCCESS："
								+ response);
						// TODO Auto-generated method stub
						JSONObject parseObject = JSON.parseObject(response);
						Map<String, Object> callbackMap = new HashMap<String, Object>();
						ResPonseBean resPonseBean = JSON.parseObject(
								parseObject.getString("body"),
								ResPonseBean.class);
						if (HTTPS_SUCCESS.equals(resPonseBean.returnCode)) {
							JSONObject BalanceObj = JSON
									.parseObject(resPonseBean.data);
							callbackMap.put("returnCode",
									resPonseBean.returnCode);
							callbackMap.put("returnMsg", resPonseBean.errorMsg);
							String orderId = BalanceObj.getString("orderId");
							callbackMap.put("orderId", orderId);
							String random = BalanceObj.getString("random");
							callbackMap.put("random", random);
							String sign = BalanceObj.getString("sign");
							callbackMap.put("sign", sign);
							String tranAmt = BalanceObj.getString("tranAmt");
							callbackMap.put("tranAmt", tranAmt);
							callbackMap.put("response", response);
						} else {
							callbackMap.put("returnCode", "11"
									+ resPonseBean.errorCode);
							callbackMap.put("returnMsg", resPonseBean.errorMsg);
						}
						callback.onSucess(callbackMap);
					}

					@Override
					public void error(String error) {
						WDLogUtil.e("DBG", "BackgroundInterface:FAIL：" + error);
						// TODO Auto-generated method stub
						if (null == error)
							callback.onFail(ResultGenerateUtil
									.generateReturnMap(ResultGenerateUtil.ReturnCode.NET_ERROR.toString(),"NetWork Error"));
						else
							callback.onFail(ResultGenerateUtil
									.generateReturnMap(ResultGenerateUtil.ReturnCode.NET_ERROR.toString(),error));
					}
				});
	}

	/**
	 * 消费
	 * 
	 * @param params
	 * @param callback
	 */
	public static void pay(Map<String, Object> params,
			final WDResultCallback callback) {
		Context context = (Context) params.get("Context");
		WDGlobalSetting.getRequest(context).httpsPost(getURL(Consume),
				getNetParams(params), new IReqCallback() {
					@Override
					public void success(String response) {
						WDLogUtil.e("DBG", "BackgroundInterface:SUCCESS："
								+ response);
						// TODO Auto-generated method stub
						JSONObject parseObject = JSON.parseObject(response);
						Map<String, Object> callbackMap = new HashMap<String, Object>();
						ResPonseBean resPonseBean = JSON.parseObject(
								parseObject.getString("body"),
								ResPonseBean.class);
						if (HTTPS_SUCCESS.equals(resPonseBean.returnCode)) {
							JSONObject BalanceObj = JSON
									.parseObject(resPonseBean.data);
							callbackMap.put("returnCode",
									resPonseBean.returnCode);
							callbackMap.put("returnMsg", resPonseBean.errorMsg);
							String discountAmt = BalanceObj
									.getString("discountAmt");
							callbackMap.put("discountAmt", discountAmt);
							String outTradeNo = BalanceObj
									.getString("outTradeNo");
							callbackMap.put("outTradeNo", outTradeNo);
							String payFee = BalanceObj.getString("payFee");
							callbackMap.put("payFee", payFee);
							String random = BalanceObj.getString("random");
							callbackMap.put("random", random);
							String sign = BalanceObj.getString("sign");
							callbackMap.put("sign", sign);
							String totalFee = BalanceObj.getString("totalFee");
							callbackMap.put("totalFee", totalFee);
							String merName = BalanceObj.getString("merName");
							callbackMap.put("merName", merName);
							String merId = BalanceObj.getString("merId");
							callbackMap.put("merId", merId);
							callbackMap.put("response", response);
						} else {
							callbackMap.put("returnCode", "11"
									+ resPonseBean.errorCode);
							callbackMap.put("returnMsg", resPonseBean.errorMsg);
						}
						callback.onSucess(callbackMap);
					}

					@Override
					public void error(String error) {
						// TODO Auto-generated method stub
						if (null == error) {
							callback.onFail(ResultGenerateUtil
									.generateReturnMap(ResultGenerateUtil.ReturnCode.NET_ERROR.toString(),"NetWork Error"));
							WDLogUtil.e("DBG", "BackgroundInterface:Fail："
									+ error);
						} else {
							callback.onFail(ResultGenerateUtil
									.generateReturnMap(ResultGenerateUtil.ReturnCode.NET_ERROR.toString(),error));
							WDLogUtil.e("DBG", "BackgroundInterface:Fail："
									+ error);
						}
					}

				});
	}

	// /**
	// * 消费
	// * @param params
	// * @param callback
	// */
	// public static void pay(final Map<String, Object> params, final
	// WDResultCallback callback) {
	// final IReqCallback iReqCallback = new IReqCallback() {
	// @Override
	// public void success(String response) {
	// WDLogUtil.e("DBG", "BackgroundInterface:SUCCESS："+response);
	// // TODO Auto-generated method stub
	// JSONObject parseObject = JSON.parseObject(response);
	// Map<String, Object> callbackMap = new HashMap<String, Object>();
	// ResPonseBean resPonseBean =
	// JSON.parseObject(parseObject.getString("body"), ResPonseBean.class);
	// if(HTTPS_SUCCESS.equals(resPonseBean.returnCode)){
	// callbackMap.put("returnCode", resPonseBean.returnCode);
	// callbackMap.put("returnMsg", resPonseBean.errorMsg);
	// JSONObject BalanceObj = JSON.parseObject(resPonseBean.data);
	// String discountAmt=BalanceObj.getString("discountAmt");
	// callbackMap.put("discountAmt", discountAmt);
	// String outTradeNo=BalanceObj.getString("outTradeNo");
	// callbackMap.put("outTradeNo", outTradeNo);
	// String payFee=BalanceObj.getString("payFee");
	// callbackMap.put("payFee", payFee);
	// String random=BalanceObj.getString("random");
	// callbackMap.put("random", random);
	// String sign=BalanceObj.getString("sign");
	// callbackMap.put("sign", sign);
	// String totalFee=BalanceObj.getString("totalFee");
	// callbackMap.put("totalFee", totalFee);
	// String merName=BalanceObj.getString("merName");
	// callbackMap.put("merName", merName);
	// String merId=BalanceObj.getString("merId");
	// callbackMap.put("merId", merId);
	// }else{
	// callbackMap.put("returnCode", "11"+resPonseBean.errorCode);
	// callbackMap.put("returnMsg", resPonseBean.errorMsg);
	// }
	// callback.onSucess(callbackMap);
	// }
	// @Override
	// public void error(String error) {
	// // TODO Auto-generated method stub
	// if(null==error){
	// callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.NET_ERROR.toString(),"NetWork Error"));
	// WDLogUtil.e("DBG", "BackgroundInterface:Fail："+error);
	// }
	// else{
	// callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.NET_ERROR.toString(),error));
	// WDLogUtil.e("DBG", "BackgroundInterface:Fail："+error);
	// }
	// }
	// };
	//
	// final Context context = (Context) params.get("Context");
	// WDLogUtil.e("DBG", "1");
	// // WDGlobalSetting.getRequest(context).httpsPost(getURL(Consume),
	// getNetParams(params), null);
	//
	// new Thread(new Runnable() {
	// @Override
	// public void run() {
	// RequestFuture<String> future = RequestFuture.newFuture(iReqCallback);
	//
	// MyRequest stringRequest;
	// try {
	// stringRequest = new
	// MyRequest(Method.POST,getURL(Consume),getHeaders(params),future,future);
	// RequestQueue requestQueue;
	// requestQueue = Volley.newRequestQueue(context);
	// requestQueue.add(stringRequest);
	// JSONObject request = future.get("");
	// future.get(3000, TimeUnit.SECONDS);//添加请求超时
	// Log.e("DBG","Volley方法内同步："+request);
	// WDLogUtil.e("DBG", "3");
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// } catch (ExecutionException e) {
	// e.printStackTrace();
	// } catch (TimeoutException e) {
	// e.printStackTrace();
	// }catch (AuthFailureError e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	// }
	// }).start();
	//
	//
	// WDLogUtil.e("DBG", "2");
	// }

	// 设置请求头
	public static Map<String, Object> getHeaders(Map<String, Object> params)
			throws AuthFailureError {
		Map<String, Object> headMap = new HashMap<String, Object>();
		headMap.put("identifiCation", " ");
		headMap.put("timestamp", System.currentTimeMillis());
		headMap.put("version", "1.0");
		headMap.put("tradeCode", " ");
		headMap.put("tradeMsg", " ");
		headMap.put("sign", " ");

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("head", headMap);
		data.put("body", params);

		return data;
	}

	/**
	 * 提现
	 * 
	 * @param params
	 * @param callback
	 */
	public static void enchashment(Map<String, Object> params,
			final WDResultCallback callback) {
		Context context = (Context) params.get("Context");
		WDGlobalSetting.getRequest(context).httpsPost(getURL(Getdeposit),
				getNetParams(params), new IReqCallback() {
					@Override
					public void success(String response) {
						WDLogUtil.e("DBG", "BackgroundInterface:SUCCESS："
								+ response);
						// TODO Auto-generated method stub
						JSONObject parseObject = JSON.parseObject(response);
						Map<String, Object> callbackMap = new HashMap<String, Object>();
						ResPonseBean resPonseBean = JSON.parseObject(
								parseObject.getString("body"),
								ResPonseBean.class);
						if (HTTPS_SUCCESS.equals(resPonseBean.returnCode)) {
							WDLogUtil.e("DBG", "提现后台返回：" + response);
							callbackMap.put("returnCode",
									resPonseBean.returnCode);
							callbackMap.put("returnMsg", resPonseBean.errorMsg);
							JSONObject BalanceObj = JSON
									.parseObject(resPonseBean.data);
							String orderId = BalanceObj.getString("orderId");
							callbackMap.put("orderId", orderId);
							String tranAmt = BalanceObj.getString("tranAmt");
							callbackMap.put("tranAmt", tranAmt);
							callbackMap.put("response", response);
						} else {
							callbackMap.put("returnCode", "11"
									+ resPonseBean.errorCode);
							callbackMap.put("returnMsg", resPonseBean.errorMsg);
						}
						callback.onSucess(callbackMap);
					}

					@Override
					public void error(String error) {
						WDLogUtil.e("DBG", "BackgroundInterface:FAIL：" + error);
						// TODO Auto-generated method stub
						if (null == error)
							callback.onFail(ResultGenerateUtil
									.generateReturnMap(ResultGenerateUtil.ReturnCode.NET_ERROR.toString(),"NetWork Error"));
						else
							callback.onFail(ResultGenerateUtil
									.generateReturnMap(ResultGenerateUtil.ReturnCode.NET_ERROR.toString(),error));
					}
				});
	}

	/**
	 * 商户订单查询
	 * 
	 * @param params
	 * @param callback
	 */
	public static void queryOrderList(Map<String, Object> params,
			final WDResultCallback callback) {
		Context context = (Context) params.get("Context");
		WDGlobalSetting.getRequest(context).httpsPost(getURL(QUERY_ORDER_LIST),
				getNetParams(params), new IReqCallback() {
					@Override
					public void success(String response) {
						WDLogUtil.e("DBG", "商户订单查询：" + response);
						JSONObject parseObject = JSON.parseObject(response);
						Map<String, Object> callbackMap = new HashMap<String, Object>();
						ResPonseBean resPonseBean = JSON.parseObject(
								parseObject.getString("body"),
								ResPonseBean.class);
						WDLogUtil.e("DBG", "返回码：" + resPonseBean.returnCode);
						if (HTTPS_SUCCESS.equals(resPonseBean.returnCode)) {
							callbackMap.put("returnCode",
									resPonseBean.returnCode);
							callbackMap.put("returnMsg", resPonseBean.errorMsg);
							JSONObject BalanceObj = JSON
									.parseObject(resPonseBean.data);
							String tradeType = BalanceObj
									.getString("tradeType");
							callbackMap.put("tradeType", tradeType);
							String tradeState = BalanceObj
									.getString("tradeState");
							callbackMap.put("tradeState", tradeState);
							String timeValid = BalanceObj
									.getString("timeValid");
							callbackMap.put("timeValid", timeValid);
							String timeStart = BalanceObj
									.getString("timeStart");
							callbackMap.put("timeStart", timeStart);
							
							String timeEnd = BalanceObj
									.getString("timeEnd");
							callbackMap.put("timeEnd", timeEnd);
							String payFee = BalanceObj
									.getString("payFee");
							callbackMap.put("payFee", payFee);
							String openID = BalanceObj
									.getString("openID");
							callbackMap.put("openID", openID);
							String limitPay = BalanceObj
									.getString("limitPay");
							callbackMap.put("limitPay", limitPay);
							String goodsTag = BalanceObj
									.getString("goodsTag");
							callbackMap.put("goodsTag", goodsTag);
							String feeType = BalanceObj
									.getString("feeType");
							callbackMap.put("feeType", feeType);
							String deviceInfo = BalanceObj
									.getString("deviceInfo");
							callbackMap.put("deviceInfo", deviceInfo);
							String detail = BalanceObj
									.getString("detail");
							callbackMap.put("detail", detail);
							String confirmOrder = BalanceObj
									.getString("confirmOrder");
							callbackMap.put("confirmOrder", confirmOrder);
							String body = BalanceObj
									.getString("body");
							callbackMap.put("body", body);
							String bankCardNo = BalanceObj
									.getString("bankCardNo");
							callbackMap.put("bankCardNo", bankCardNo);
							String attach = BalanceObj
									.getString("attach");
							callbackMap.put("attach", attach);
							String discountList = BalanceObj
									.getString("discountList");
							callbackMap.put("discountList", discountList);
							String mchID = BalanceObj.getString("mchID");
							callbackMap.put("mchID", mchID);
							String outTradeNo = BalanceObj
									.getString("outTradeNo");
							callbackMap.put("outTradeNo", outTradeNo);
							String payID = BalanceObj.getString("payID");
							callbackMap.put("payID", payID);
							callbackMap.put("response", response);
						} else {
							callbackMap.put("returnCode", "11"
									+ resPonseBean.errorCode);
							callbackMap.put("returnMsg", resPonseBean.errorMsg);
						}
						callback.onSucess(callbackMap);
					}

					@Override
					public void error(String error) {
						// TODO Auto-generated method stub
						if (null == error)
							callback.onFail(ResultGenerateUtil
									.generateReturnMap(ResultGenerateUtil.ReturnCode.NET_ERROR.toString(),"NetWork Error"));
						else
							callback.onFail(ResultGenerateUtil
									.generateReturnMap(ResultGenerateUtil.ReturnCode.NET_ERROR.toString(),error));
					}
				});

	}

	/**
	 * 用户交易明细查询
	 * 
	 * @param params
	 * @param callback
	 */
	public static void queryTransactionList(Map<String, Object> params,
			final WDResultCallback callback) {
		Context context = (Context) params.get("Context");
		WDGlobalSetting.getRequest(context).httpsPost(getURL(QUERY_TRANSACTION_LIST),
				getNetParams(params), new IReqCallback() {
					@Override
					public void success(String response) {
						WDLogUtil.e("DBG", "用户交易明细查询：" + response);
						JSONObject parseObject = JSON.parseObject(response);
						Map<String, Object> callbackMap = new HashMap<String, Object>();
						ResPonseBean resPonseBean = JSON.parseObject(
								parseObject.getString("body"),
								ResPonseBean.class);
						WDLogUtil.e("DBG", "返回码：" + resPonseBean.returnCode);
						if (HTTPS_SUCCESS.equals(resPonseBean.returnCode)) {
							callbackMap.put("returnCode",
									resPonseBean.returnCode);
							callbackMap.put("returnMsg", resPonseBean.errorMsg);
							JSONObject BalanceObj = JSON
									.parseObject(resPonseBean.data);
							// String payID = BalanceObj.getString("payID");
							// callbackMap.put("payID", payID);
							callbackMap.put("response", response);
						} else {
							callbackMap.put("returnCode", "11"
									+ resPonseBean.errorCode);
							callbackMap.put("returnMsg", resPonseBean.errorMsg);
							callbackMap.put("response", response);
						}
						callback.onSucess(callbackMap);
					}

					@Override
					public void error(String error) {
						// TODO Auto-generated method stub
						if (null == error)
							callback.onFail(ResultGenerateUtil
									.generateReturnMap(ResultGenerateUtil.ReturnCode.NET_ERROR.toString(),"NetWork Error"));
						else
							callback.onFail(ResultGenerateUtil
									.generateReturnMap(ResultGenerateUtil.ReturnCode.NET_ERROR.toString(),error));
					}
				});

	}
	
	/**
	 * 用户信息查询
	 * 
	 * @param params
	 * @param callback
	 */
	public static void queryTransactionInfo(Map<String, Object> params,
			final WDResultCallback callback) {
		Context context = (Context) params.get("Context");
		WDGlobalSetting.getRequest(context).httpsPost(getURL(QUERY_USER_INFO),
				getNetParams(params), new IReqCallback() {
					@Override
					public void success(String response) {
						WDLogUtil.e("DBG", "用户信息查询：" + response);
						JSONObject parseObject = JSON.parseObject(response);
						Map<String, Object> callbackMap = new HashMap<String, Object>();
						ResPonseBean resPonseBean = JSON.parseObject(
								parseObject.getString("body"),
								ResPonseBean.class);
						if (HTTPS_SUCCESS.equals(resPonseBean.returnCode)) {
							callbackMap.put("returnCode",
									resPonseBean.returnCode);
							callbackMap.put("returnMsg", resPonseBean.errorMsg);
							JSONObject BalanceObj = JSON
									.parseObject(resPonseBean.data);
							callbackMap.put("response", response);
							// String payID = BalanceObj.getString("payID");
							// callbackMap.put("payID", payID);
						} else {
							callbackMap.put("returnCode", "11"
									+ resPonseBean.errorCode);
							callbackMap.put("returnMsg", resPonseBean.errorMsg);
							callbackMap.put("response", response);
						}
						callback.onSucess(callbackMap);
					}

					@Override
					public void error(String error) {
						// TODO Auto-generated method stub
						if (null == error)
							callback.onFail(ResultGenerateUtil
									.generateReturnMap(ResultGenerateUtil.ReturnCode.NET_ERROR.toString(),"NetWork Error"));
						else
							callback.onFail(ResultGenerateUtil
									.generateReturnMap(ResultGenerateUtil.ReturnCode.NET_ERROR.toString(),error));
					}
				});

	}
	
	
	/**
	 * 已绑定銀行卡列表查詢
	 * 
	 * @param params1
	 * @param wdResultCallback
	 */
	public static void queryBindedCardList(Map<String, Object> params,
			final WDResultCallback callback) {
		// TODO Auto-generated method stub
		Context context = (Context) params.get("Context");
		WDGlobalSetting.getRequest(context).httpsPost(getURL(QUERY_CARD),
				getNetParams(params), new IReqCallback() {
					@Override
					public void success(String response) {
						WDLogUtil.e("DBG", "绑卡列表查询SUCCESS：" + response);
						JSONObject parseObject = JSON.parseObject(response);
						Map<String, Object> callbackMap = new HashMap<String, Object>();
						ResPonseBean resPonseBean = JSON.parseObject(
								parseObject.getString("body"),
								ResPonseBean.class);
						if (HTTPS_SUCCESS.equals(resPonseBean.returnCode)) {
							callbackMap.put("returnCode",
									resPonseBean.returnCode);
							callbackMap.put("returnMsg", resPonseBean.errorMsg);
							callbackMap.put("response", response);
						} else {
							callbackMap.put("returnCode", "11"
									+ resPonseBean.errorCode);
							callbackMap.put("returnMsg", resPonseBean.errorMsg);
							callbackMap.put("response", response);
						}
						callback.onSucess(callbackMap);
					}

					@Override
					public void error(String error) {
						// TODO Auto-generated method stub
						WDLogUtil.e("DBG", "绑卡列表查询ERROR：" + error);
						if (null == error)
							callback.onFail(ResultGenerateUtil
									.generateReturnMap(ResultGenerateUtil.ReturnCode.NET_ERROR.toString(),"NetWork Error"));
						else
							callback.onFail(ResultGenerateUtil
									.generateReturnMap(ResultGenerateUtil.ReturnCode.NET_ERROR.toString(),error));
					}
				});
	}
}

package com.wondersgroup.entity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;

import com.wondersgroup.PwServer.BackgroundInterface;
import com.wondersgroup.SHRB.SHRBManager;
import com.wondersgroup.utils.ResultGenerateUtil;
import com.wondersgroup.utils.WDValidationUtil;
import com.wondersgroup.wallet.WDLogUtil;
import com.wondersgroup.wallet.WDResultCallback;
import com.wondersgroup.wallet.WalletEntryActivtiy;
	/*
 *@作者: kevin
 *@版本: 
 *@version create time：2017-3-18 下午3:05:16
 */
public class Transaction {
	/**充值*/
	public static final String TYPE_Recharge = "Recharge";
	/**消费*/
	public static final String TYPE_Spend = "Spend";
	/**提现*/
	public static final String TYPE_Extract = "Extract";
	
	public Transaction() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 充值
	 * @param params
	 * @param callback
	 */
	public void RechargeSDK(Map<String, Object> params, final WDResultCallback callback) {
		
		Map<String, Object> params1 = new HashMap<String, Object>();
		
		String merAppId = (String) params.get("merAppId");
		String merUserId = (String) params.get("merUserId");
		final String tranAmt = (String) params.get("tranAmt");
		final String openID = (String) params.get("openID");
		final String personUnionID = (String) params.get("personUnionID");
		final Activity activity =  (Activity) params.get("Activity");
		
		if(WDValidationUtil.isNull(merAppId)){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"merAppId参数有误"));
			return;
		}
		if(WDValidationUtil.isNull(merUserId)){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"merUserId参数有误")); 
			return;
		}
		if(WDValidationUtil.isNull(tranAmt)||!WDValidationUtil.isJinE(tranAmt)){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"tranAmt参数有误"));
			return;
		}
		if(WDValidationUtil.isNull(openID)){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"openID参数有误"));
			return;
		}
		if(WDValidationUtil.isNull(personUnionID)){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"personUnionID参数有误"));
			return;
		}
		if(activity==null){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"activity参数有误"));
			return;
		}
		
		params1.put("Context", activity);
		params1.put("merAppId", merAppId);
		params1.put("merUserId",merUserId);
		params1.put("tranAmt",tranAmt);
		params1.put("terminalType", "android");
		BackgroundInterface.deposits(params1, new WDResultCallback() {
			@Override
			public void onSucess(Map<String, Object> callbackMap) {
				// TODO Auto-generated method stub
				if(!BackgroundInterface.HTTPS_SUCCESS.equals(callbackMap.get("returnCode"))){
					callback.onFail(callbackMap);
					return;
				}
//				callback.onSucess(callbackMap);
				String SoutTradeNo = (String) callbackMap.get("orderId");
				String StranAmt = (String) callbackMap.get("tranAmt");
				
				if(WDValidationUtil.isNull(SoutTradeNo)){
					callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.SERVER_ERROR.toString()
							,"outTradeNo参数有误"));
					return;
				}
				if(WDValidationUtil.isNull(StranAmt)){
					callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.SERVER_ERROR.toString()
							,"tranAmt参数有误"));
					return;
				}
				
				WalletEntryActivtiy.callback = callback;
				Intent intent = new Intent(activity, WalletEntryActivtiy.class);
				
				intent.putExtra(WalletEntryActivtiy.PAY_TYPE, TYPE_Recharge);
				intent.putExtra("openID", openID);
				intent.putExtra("outTradeNo", SoutTradeNo);
				intent.putExtra("personUnionID", personUnionID);
				intent.putExtra("tranAmt", StranAmt);
				
				activity.startActivity(intent);
			}
			
			@Override
			public void onFail(Map<String, Object> callbackMap) {
				// TODO Auto-generated method stub
				//网络请求成功 获取数据失败
				callback.onFail(callbackMap);
			}
		});
	}
//	/**
//	 * 充值
//	 * @param params
//	 * @param callback
//	 */
//	public void RechargeSDK(Map<String, Object> params, final WDResultCallback callback) {
//
//		Map<String, Object> params1 = new HashMap<String, Object>();
//		
//		String merAppId = (String) params.get("merAppId");
//		String merUserId = (String) params.get("merUserId");
//		final String tranAmt = (String) params.get("tranAmt");
//		final String openID = (String) params.get("openID");
//		final String personUnionID = (String) params.get("personUnionID");
//		final Activity activity =  (Activity) params.get("Activity");
//		
//		params1.put("Context", activity);
//		params1.put("merAppId", merAppId);
//		params1.put("merUserId",merUserId);
//		params1.put("tranAmt",tranAmt);
//		params1.put("terminalType", "android");
//		BackgroundInterface.deposits(params1, new WDResultCallback() {
//			@Override
//			public void onSucess(Map<String, Object> callbackMap) {
//				// TODO Auto-generated method stub
//				if(!BackgroundInterface.HTTPS_SUCCESS.equals(callbackMap.get("returnCode"))){
//					callback.onFail(callbackMap);
//					return;
//				}
//				callback.onSucess(callbackMap);
//				Map<String, Object> params2 = new HashMap<String, Object>();
//
//				String SoutTradeNo = (String) callbackMap.get("orderId");
//				String StranAmt = (String) callbackMap.get("tranAmt");
//				params2.put("outTradeNo", SoutTradeNo);
//				params2.put("openID", openID);
//				params2.put("personUnionID", personUnionID);
//				params2.put("activity", activity);
//				params2.put("tranAmt", StranAmt);
//				new SHRBManager(new WDResultCallback() {
//					@Override
//					public void onSucess(Map<String, Object> callbackMap) {
//						callback.onSucess(callbackMap);
//					}
//					@Override
//					public void onFail(Map<String, Object> callbackMap) {
//						callback.onFail(callbackMap);
//					}
//				}).deposits(params2);
//			}
//			
//			@Override
//			public void onFail(Map<String, Object> callbackMap) {
//				// TODO Auto-generated method stub
//				//网络请求成功 获取数据失败
//				callback.onFail(callbackMap);
//			}
//		});
//	}
	/**
	 * 消费
	 * @param params
	 * @param callback
	 */
	public  void SpendSDK(Map<String, Object> params, final WDResultCallback callback) {
		
		
		final Activity activity =  (Activity) params.get("Activity");
		final String openID = (String) params.get("openID");
		final String personUnionID = (String) params.get("personUnionID");
		/**商户AppId	*/ String merAppId = (String) params.get("merAppId");
		/**商户用户ID	*/ String merUserId = (String) params.get("merUserId");
		/**商户订单号	*/ String outTradeNo = (String) params.get("outTradeNo");
		/**商品描述	*/ final String body = (String) params.get("body");
		/**商品详情	*/ final String detail = (String) params.get("detail");
		/**附加数据	*/ final String attach = (String) params.get("attach");
		/**确认支付	*/ final String confirmOrder = (String) params.get("confirmOrder");
		/**展示金额	*/ final String payAmt = (String) params.get("payAmt");
		/**优惠金额	*/ final String discountAmt = (String) params.get("discountAmt");
		/**实际支付金额	*/ final String payFee = (String) params.get("payFee");
		/**指定支付方式   */ final String limitPay = "01";
		/**货币类型	*/ final String feeType = "CNY";
		/**商品标记	*/ final String goodsTag = (String) params.get("goodsTag");
		/**交易有效时间   */ final String timeValid = (String) params.get("timeValid");
		/**硬件信息	*/ final String deviceInfo = (String) params.get("deviceInfo");
		
		if(activity==null){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"参数有误：activity为空！"));
			return;
		}
		if(WDValidationUtil.isNull(openID)){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"参数有误：openID为空！"));
			return;
		}
		if(WDValidationUtil.isNull(personUnionID)){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"参数有误：personUnionID为空！"));
			return;
		}
		if(WDValidationUtil.isNull(merAppId)){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"参数有误：merAppId为空！"));
			return;
		}
		if(WDValidationUtil.isNull(merUserId)){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"参数有误：merUserId为空！"));
			return;
		}
		if(WDValidationUtil.isNull(outTradeNo)){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"参数有误：outTradeNo为空！"));
			return;
		}
		if(body==null){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"参数有误：body为空！"));
			return;
		}
		if(WDValidationUtil.isNull(detail)){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"参数有误：detail为空！"));
			return;
		}
		if(WDValidationUtil.isNull(attach)){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"参数有误：attach为空！"));
			return;
		}
		if(WDValidationUtil.isNull(confirmOrder)){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"参数有误：confirmOrder为空！"));
			return;
		}
		if(!WDValidationUtil.isJinE(payAmt)){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"参数有误：payAmt！"));
			return;
		}
		if(!WDValidationUtil.isDiscountJinE(discountAmt)){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"参数有误：discountAmt！"));
			return;
		}
		if(!WDValidationUtil.isDiscountJinE(payFee)){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"参数有误：payFee！"));
			return;
		}
		if(WDValidationUtil.isNull(goodsTag)){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"参数有误：goodsTag为空！"));
			return;
		}
		if(WDValidationUtil.isNull(timeValid)){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"参数有误：timeValid为空！"));
			return;
		}
		
		/**网络请求参数*/
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("Context", activity);
		params1.put("merAppId", merAppId);
		params1.put("merUserId", merUserId);
		params1.put("outTradeNo", outTradeNo);
		params1.put("body", body);
		params1.put("detail", detail);
		params1.put("attach", attach);
		params1.put("confirmOrder", confirmOrder);
		params1.put("discountAmt", discountAmt);
		params1.put("payAmt", payAmt);
		params1.put("payFee", payFee);
		params1.put("limitPay", limitPay);
		params1.put("feeType", feeType);
		params1.put("goodsTag", goodsTag);
		params1.put("timeValid", timeValid);
		params1.put("deviceInfo", deviceInfo);
		params1.put("terminalType", "android");
		WDLogUtil.e("DBG", "后台通讯前："+"订单金额:"+payAmt+";支付金额:"+payFee+";优惠金额:"+discountAmt);
		BackgroundInterface.pay(params1, new WDResultCallback() {
			@Override
			public void onSucess(Map<String, Object> callbackMap) {
				WDLogUtil.e("DBG", "消费后台SUCCESS："+callbackMap.toString());
				// TODO Auto-generated method stub
				if(!BackgroundInterface.HTTPS_SUCCESS.equals(callbackMap.get("returnCode"))){
					callback.onFail(callbackMap);
					return;
				}
//				final Map<String, Object> params2 = new HashMap<String, Object>();
				String SoutTradeNo = (String) callbackMap.get("outTradeNo");
				String SdiscountAmt = (String) callbackMap.get("discountAmt");//优惠金额
				String SpayFee = (String) callbackMap.get("payFee");//确认支付金额
				String StotalFee = (String) callbackMap.get("totalFee");//展示金额(总金额)
				String Srandom = (String) callbackMap.get("random");
				String Ssign = (String) callbackMap.get("sign");
				String SmerName = (String) callbackMap.get("merName");
				String SmerId = (String) callbackMap.get("merId");
				
				WDLogUtil.e("DBG", "后台返回"+"订单金额:"+StotalFee+";支付金额:"+SpayFee+";优惠金额:"+SdiscountAmt);
				if(SoutTradeNo==null){
					callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.SERVER_ERROR.toString()
							,"参数有误：outTradeNo为空！"));
					return;
				}
				if(SdiscountAmt==null){
					callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.SERVER_ERROR.toString()
							,"参数有误：discountAmt为空！"));
					return;
				}
				if(SpayFee==null){
					callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.SERVER_ERROR.toString()
							,"参数有误：payFee为空！"));
					return;
				}
				if(StotalFee==null){
					callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.SERVER_ERROR.toString()
							,"参数有误：totalFee为空！"));
					return;
				}
				if(Srandom==null){
					callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.SERVER_ERROR.toString()
							,"参数有误：random为空！"));
					return;
				}
				if(Ssign==null){
					callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.SERVER_ERROR.toString()
							,"参数有误：sign为空！"));
					return;
				}
				if(SmerName==null){
					callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.SERVER_ERROR.toString()
							,"参数有误：merName为空！"));
					return;
				}
				if(SmerId==null){
					callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.SERVER_ERROR.toString()
							,"参数有误：merId为空！"));
					return;
				}
				
				WalletEntryActivtiy.callback = callback;
				Intent intent = new Intent(activity, WalletEntryActivtiy.class);
				intent.putExtra(WalletEntryActivtiy.PAY_TYPE, TYPE_Spend);
				/**华瑞SDK请求参数*/
				intent.putExtra("openID", openID);//用户ID
				intent.putExtra("personUnionID", personUnionID);//开通钱包标识
				intent.putExtra("mchName", SmerName);//商户名
				intent.putExtra("mchID", SmerId);//商户号
				intent.putExtra("outTradeNo", SoutTradeNo);//商户订单号
				intent.putExtra("random", Srandom);//随机数
				intent.putExtra("sign", Ssign);//签名
				intent.putExtra("payAmt", StotalFee);//订单金额（展示金额）必输
				intent.putExtra("payFee", SpayFee);//支付金额（实际支付金额）必输
				intent.putExtra("discountAmt", SdiscountAmt);//优惠金额（无优惠请输入0）必输
				intent.putExtra("feeType", feeType);//货币类型 默认：CNY 人民币
				intent.putExtra("attach", attach);//附加数据
				intent.putExtra("confirmOrder", "N");//确认支付 N:用户提交订单后，立即扣款
				intent.putExtra("limitPay", limitPay);//指定支付方式 01: 绑定的银行卡或者账户
				intent.putExtra("goodsTag", goodsTag);//商品标记（预留） 
				intent.putExtra("timeValid", timeValid);//交易有效时间 （预留） 
				intent.putExtra("deviceInfo", deviceInfo);//硬件信息（预留） 
				intent.putExtra("body", "手机");//商品描述
				intent.putExtra("detail", detail);//商品详情
				
				
				activity.startActivity(intent);
				
			}
			
			@Override
			public void onFail(Map<String, Object> callbackMap) {
				// TODO Auto-generated method stub
				WDLogUtil.e("DBG", "消费后台FAIL："+callbackMap.toString());
				//网络请求成功 获取数据失败
				callback.onFail(callbackMap);
			}
		});
	}
//	/**
//	 * 消费
//	 * @param params
//	 * @param callback
//	 */
//	public  void SpendSDK(Map<String, Object> params, final WDResultCallback callback) {
//		
//		final Activity activity =  (Activity) params.get("Activity");
//		final String openID = (String) params.get("openID");
//		final String personUnionID = (String) params.get("personUnionID");
//		/**商户AppId	*/ String merAppId = (String) params.get("merAppId");
//		/**商户用户ID	*/ String merUserId = (String) params.get("merUserId");
//		/**商户订单号	*/ String outTradeNo = (String) params.get("outTradeNo");
//		/**商品描述	*/ final String body = (String) params.get("body");
//		/**商品详情	*/ final String detail = (String) params.get("detail");
//		/**附加数据	*/ final String attach = (String) params.get("attach");
//		/**确认支付	*/ final String confirmOrder = (String) params.get("confirmOrder");
//		/**展示金额	*/ final String payAmt = (String) params.get("payAmt");
//		/**优惠金额	*/ final String discountAmt = (String) params.get("discountAmt");
//		/**指定支付方式*/ final String limitPay = "01";
//		/**货币类型	*/ final String feeType = "CNY";
//		/**商品标记	*/ final String goodsTag = (String) params.get("goodsTag");
//		/**交易有效时间*/ final String timeValid = (String) params.get("timeValid");
//		/**硬件信息	*/ final String deviceInfo = (String) params.get("deviceInfo");
//		
//		/**网络请求参数*/
//		Map<String, Object> params1 = new HashMap<String, Object>();
//		params1.put("Context", activity);
//		params1.put("merAppId", merAppId);
//		params1.put("merUserId", merUserId);
//		params1.put("outTradeNo", outTradeNo);
//		params1.put("body", body);
//		params1.put("detail", detail);
//		params1.put("attach", attach);
//		params1.put("confirmOrder", confirmOrder);
//		params1.put("discountAmt", discountAmt);
//		params1.put("payAmt", payAmt);
//		params1.put("limitPay", limitPay);
//		params1.put("feeType", feeType);
//		params1.put("goodsTag", goodsTag);
//		params1.put("timeValid", timeValid);
//		params1.put("deviceInfo", deviceInfo);
//		params1.put("terminalType", "android");
//		BackgroundInterface.pay(params1, new WDResultCallback() {
//			@Override
//			public void onSucess(Map<String, Object> callbackMap) {
//				WDLogUtil.e("DBG", "消费后台SUCCESS："+callbackMap.toString());
//				// TODO Auto-generated method stub
//				if(!BackgroundInterface.HTTPS_SUCCESS.equals(callbackMap.get("returnCode"))){
//					callback.onFail(callbackMap);
//					return;
//				}
//				final Map<String, Object> params2 = new HashMap<String, Object>();
//				String SoutTradeNo = (String) callbackMap.get("outTradeNo");
//				String SdiscountAmt = (String) callbackMap.get("discountAmt");//优惠金额
//				String SpayFee = (String) callbackMap.get("payFee");//确认支付金额
//				String StotalFee = (String) callbackMap.get("totalFee");//展示金额(总金额)
//				String Srandom = (String) callbackMap.get("random");
//				String Ssign = (String) callbackMap.get("sign");
//				
//				String SmerName = (String) callbackMap.get("merName");
//				String SmerId = (String) callbackMap.get("merId");
//				
//				/**华瑞SDK请求参数*/
//				params2.put("openID", openID);//用户ID
//				params2.put("personUnionID", personUnionID);//开通钱包标识
//				params2.put("mchName", SmerName);//商户名
//				params2.put("mchID", SmerId);//商户号
//				params2.put("outTradeNo", SoutTradeNo);//商户订单号
//				params2.put("random", Srandom);//随机数
//				params2.put("sign", Ssign);//签名
//				params2.put("payAmt", StotalFee);//订单金额（展示金额）必输
//				params2.put("payFee", SpayFee);//支付金额（实际支付金额）必输
//				params2.put("discountAmt", SdiscountAmt);//优惠金额（无优惠请输入0）必输
//				params2.put("feeType", feeType);//货币类型 默认：CNY 人民币
//				params2.put("attach", attach);//附加数据
//				params2.put("confirmOrder", "N");//确认支付 N:用户提交订单后，立即扣款
//				params2.put("limitPay", limitPay);//指定支付方式 01: 绑定的银行卡或者账户
//				params2.put("goodsTag", goodsTag);//商品标记（预留） 
//				params2.put("timeValid", timeValid);//交易有效时间 （预留） 
//				params2.put("deviceInfo", deviceInfo);//硬件信息（预留） 
//				params2.put("body", "手机");//商品描述
//				params2.put("detail", detail);//商品详情
//				params2.put("activity", activity);
//				new SHRBManager(new WDResultCallback() {
//					
//					@Override
//					public void onSucess(Map<String, Object> callbackMap) {
//						callback.onSucess(callbackMap);
//					}
//					
//					@Override
//					public void onFail(Map<String, Object> callbackMap) {
//						callback.onFail(callbackMap);
//					}
//				}).orderPay(params2);
//			}
//			
//			@Override
//			public void onFail(Map<String, Object> callbackMap) {
//				// TODO Auto-generated method stub
//				WDLogUtil.e("DBG", "消费后台FAIL："+callbackMap.toString());
//				//网络请求成功 获取数据失败
//				callback.onFail(callbackMap);
//			}
//		});
//	}
	/**
	 * 提现
	 * @param params
	 * @param callback
	 */
	public  void ExtractSDK(Map<String, Object> params, final WDResultCallback callback) {
		
		final Activity activity =  (Activity) params.get("Activity");
		final String openID = (String) params.get("openID");
		final String personUnionID = (String) params.get("personUnionID");
		String merAppId=(String) params.get("merAppId");//商户AppID
		String merUserId=(String) params.get("merUserId");//商户用户ID
		String tranAmt=(String) params.get("tranAmt");//提现金额
		
		if(activity==null){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"activity参数有误"));
			return;
		}
		if(WDValidationUtil.isNull(openID)){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"openID参数有误"));
			return;
		}
		if(WDValidationUtil.isNull(personUnionID)){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"personUnionID参数有误"));
			return;
		}
		if(WDValidationUtil.isNull(merAppId)){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"merAppId参数有误"));
			return;
		}
		if(WDValidationUtil.isNull(merUserId)){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"merUserId参数有误"));
			return;
		}
		if(!WDValidationUtil.isJinE(tranAmt)){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"tranAmt参数有误"));
			return;
		}
		
		//第一步 完成后台请求
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("merAppId", merAppId);
		params1.put("merUserId", merUserId);
		params1.put("tranAmt", tranAmt);
		params1.put("terminalType", "android");
		BackgroundInterface.enchashment(params1, new WDResultCallback() {
			
			@Override
			public void onSucess(Map<String, Object> callbackMap) {
				// TODO Auto-generated method stub
				if(!BackgroundInterface.HTTPS_SUCCESS.equals(callbackMap.get("returnCode"))){
					callback.onFail(callbackMap);
					return;
				}
				String StranAmt = (String) callbackMap.get("tranAmt");
				String SorderId = (String) callbackMap.get("orderId");
				if(StranAmt==null){
					callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
							,"参数有误：tranAmt为空！"));
					return;
				}
				if(SorderId==null){
					callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
							,"参数有误：orderId为空！"));
					return;
				}
				
				//第二步 完成HRSDK请求
				WalletEntryActivtiy.callback = callback;
				Intent intent = new Intent(activity, WalletEntryActivtiy.class);
				intent.putExtra(WalletEntryActivtiy.PAY_TYPE, TYPE_Extract);
				intent.putExtra("openID", openID);
				intent.putExtra("personUnionID", personUnionID);
				intent.putExtra("tranAmt",StranAmt );
				intent.putExtra("outTradeNo", SorderId);
				
				activity.startActivity(intent);
				
			}
			
			@Override
			public void onFail(Map<String, Object> callbackMap) {
				callback.onFail(callbackMap);
			}
		});
	}
//	/**
//	 * 提现
//	 * @param params
//	 * @param callback
//	 */
//	public  void ExtractSDK(Map<String, Object> params, final WDResultCallback callback) {
//
//		final Activity activity =  (Activity) params.get("Activity");
//		final String openID = (String) params.get("openID");
//		final String personUnionID = (String) params.get("personUnionID");
//		String merAppId=(String) params.get("merAppId");//商户AppID
//		String merUserId=(String) params.get("merUserId");//商户用户ID
//		String tranAmt=(String) params.get("tranAmt");//提现金额
//		
//		//第一步 完成后台请求
//		Map<String, Object> params1 = new HashMap<String, Object>();
//		params1.put("merAppId", merAppId);
//		params1.put("merUserId", merUserId);
//		params1.put("tranAmt", tranAmt);
//		params1.put("terminalType", "android");
//		BackgroundInterface.enchashment(params1, new WDResultCallback() {
//			
//			@Override
//			public void onSucess(Map<String, Object> callbackMap) {
//				// TODO Auto-generated method stub
//				if(!BackgroundInterface.HTTPS_SUCCESS.equals(callbackMap.get("returnCode"))){
//					callback.onFail(callbackMap);
//					return;
//				}
//				//第二步 完成HRSDK请求
//				Map<String, Object> params2 = new HashMap<String, Object>();
//				params2.put("openID", openID);
//				params2.put("personUnionID", personUnionID);
//				params2.put("tranAmt", callbackMap.get("tranAmt"));
//				params2.put("outTradeNo", (String) callbackMap.get("orderId"));
//				params2.put("Activity", activity);
//				
//				new SHRBManager(new WDResultCallback() {
//					
//					@Override
//					public void onSucess(Map<String, Object> callbackMap) {
//						callback.onSucess(callbackMap);
//					}
//					
//					@Override
//					public void onFail(Map<String, Object> callbackMap) {
//						callback.onFail(callbackMap);
//					}
//				}).withdraw(params2);;
//			}
//			
//			@Override
//			public void onFail(Map<String, Object> callbackMap) {
//				callback.onFail(callbackMap);
//			}
//		});
//	}
	
	
	
	
}



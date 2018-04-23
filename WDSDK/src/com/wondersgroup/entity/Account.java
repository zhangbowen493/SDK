package com.wondersgroup.entity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.shrb.walletsdk.SHRBWalletSDK;
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
 *@version create time：2017-3-18 下午3:04:42
 */
public class Account {

	public static final String TYPE_BindCard = "BindCard";
	public static final String TYPE_AddCard = "AddCard";
	
	public Account() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 开通钱包
	 * callback返回结果
	 * @param params
	 * @param callback
	 */
	public void BuildWalletSDK(Map<String, Object> params,
			final WDResultCallback callback) {
		
		WDLogUtil.e("开通钱包=》"+params.toString());
		
		final String realName = (String) params.get("realName");// 真实姓名
		final String identity = (String) params.get("identity");// 身份证号
		final String cardNo = (String) params.get("cardNo");// 卡号
		final String mobile = (String) params.get("mobile");// 手机号
		final Activity activity = (Activity) params.get("Activity");
		final String openID = (String) params.get("openID");
		final String sequence = (String) params.get("sequence");
		final String merAppId = (String) params.get("merAppId");
		final String merUserId = (String) params.get("merUserId");
		
		if (WDValidationUtil.isNull(merAppId)) {
			callback.onFail(ResultGenerateUtil
					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"merAppId参数有误"));
			return;
		}
		if (WDValidationUtil.isNull(merUserId) ) {
			callback.onFail(ResultGenerateUtil
					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"merUserId参数有误"));
			return;
		}
		if (WDValidationUtil.isNull(realName) ) {
			callback.onFail(ResultGenerateUtil
					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"realName参数有误"));
			return;
		}
		if (WDValidationUtil.isNull(identity)|| !WDValidationUtil.getValidIdCard0531(identity)) {
			callback.onFail(ResultGenerateUtil
					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"identity参数有误"));
			return;
		}
		if (!WDValidationUtil.checkBankCard0531(cardNo)) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"cardNo参数有误"));
			return;
		}
		if (WDValidationUtil.isNull(mobile) || !WDValidationUtil.isPhone(mobile)) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"mobile参数有误"));
			return;
		}
		if (activity == null
				|| WDValidationUtil.isSameType(activity, Activity.class)) {
			callback.onFail(ResultGenerateUtil
					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"activity参数有误"));
			return;
		}
		if (WDValidationUtil.isNull(openID) ) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"openID参数有误"));
			return;
		}
		if (WDValidationUtil.isNull(sequence) ) {
			callback.onFail(ResultGenerateUtil
					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"sequence参数有误"));
			return;
		}
		/** 网络请求参数 */
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("mobile", mobile);// 手机号
		params1.put("realName", realName);// 真实姓名
		params1.put("identity", identity);// 身份证号
		params1.put("merAppId", merAppId);// 商户appid
		params1.put("merUserId", merUserId);// 商户用户ID
		params1.put("openID", openID);
		params1.put("sequence", sequence);
		params1.put("terminalType", "android");
		BackgroundInterface.openAccount(params1, new WDResultCallback() {
			@Override
			public void onSucess(Map<String, Object> callbackMap) {
				// TODO Auto-generated method stub
				// SHRBWalletSDK.bindCard(openID, extraMessage, activity);
				WalletEntryActivtiy.callback = callback;
				if (!BackgroundInterface.HTTPS_SUCCESS.equals(callbackMap
						.get("returnCode"))) {
					callback.onFail(callbackMap);
					return;
				}
				
				Intent intent = new Intent(activity, WalletEntryActivtiy.class);
				
				intent.putExtra(WalletEntryActivtiy.PAY_TYPE, TYPE_BindCard);
				intent.putExtra("openID", openID);
				intent.putExtra("mobile", mobile);
				intent.putExtra("realName", realName);
				intent.putExtra("identity", identity);
				intent.putExtra("cardNo", cardNo);
				
				activity.startActivity(intent);
			}
			
			@Override
			public void onFail(Map<String, Object> callbackMap) {
				// TODO Auto-generated method stub
				// 网络请求成功 获取数据失败
				callback.onFail(callbackMap);
			}
		});
		
	}
//	/**
//	 * 开通钱包
//	 * 
//	 * @param params
//	 * @param callback
//	 */
//	public void BuildWalletSDK(Map<String, Object> params,
//			final WDResultCallback callback) {
//
//		WDLogUtil.e("开通钱包=》"+params.toString());
//
//		final String realName = (String) params.get("realName");// 真实姓名
//		final String identity = (String) params.get("identity");// 身份证号
//		final String cardNo = (String) params.get("cardNo");// 卡号
//		final String mobile = (String) params.get("mobile");// 手机号
//		final Activity activity = (Activity) params.get("Activity");
//		final String openID = (String) params.get("openID");
//		final String sequence = (String) params.get("sequence");
//		final String merAppId = (String) params.get("merAppId");
//		final String merUserId = (String) params.get("merUserId");
//
//		if (merAppId == null || merAppId.length() < 2) {
//			callback.onFail(ResultGenerateUtil
//					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误：merAppId！"));
//			return;
//		}
//		if (merUserId == null || merUserId.length() < 2) {
//			callback.onFail(ResultGenerateUtil
//					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误：merUserId！"));
//			return;
//		}
//		//		if (realName == null || WDValidationUtil.isName(realName)) {
//		if (realName == null) {
//			callback.onFail(ResultGenerateUtil
//					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误：realName！"));
//			return;
//		}
//		//		if (identity == null || WDValidationUtil.getValidIdCard(identity)) {
//		if (identity == null ) {
//			callback.onFail(ResultGenerateUtil
//					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误：identity！"));
//			return;
//		}
////		if (cardNo == null || WDValidationUtil.checkBankCard(cardNo)) {
//			if (cardNo == null ) {
//			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误：cardNo！"));
//			return;
//		}
//		if (mobile == null) {
////			if (mobile == null || WDValidationUtil.isPhone(mobile)) {
//			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误：mobile！"));
//			return;
//		}
//		if (activity == null
//				|| WDValidationUtil.isSameType(activity, Activity.class)) {
//			callback.onFail(ResultGenerateUtil
//					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误:activity！"));
//			return;
//		}
//		if (openID == null || openID.length() < 1) {
//			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误:openID！"));
//			return;
//		}
//		if (sequence == null || sequence.length() < 1) {
//			callback.onFail(ResultGenerateUtil
//					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误:sequence！"));
//			return;
//		}
//		/** 网络请求参数 */
//		Map<String, Object> params1 = new HashMap<String, Object>();
//		params1.put("mobile", mobile);// 手机号
//		params1.put("realName", realName);// 真实姓名
//		params1.put("identity", identity);// 身份证号
//		params1.put("merAppId", merAppId);// 商户appid
//		params1.put("merUserId", merUserId);// 商户用户ID
//		params1.put("openID", openID);
//		params1.put("sequence", sequence);
//		params1.put("terminalType", "android");
//		BackgroundInterface.openAccount(params1, new WDResultCallback() {
//			@Override
//			public void onSucess(Map<String, Object> callbackMap) {
//				// TODO Auto-generated method stub
//				// SHRBWalletSDK.bindCard(openID, extraMessage, activity);
//				if (!BackgroundInterface.HTTPS_SUCCESS.equals(callbackMap
//						.get("returnCode"))) {
//					callback.onFail(callbackMap);
//					return;
//				}
//
//				Map<String, Object> params2 = new HashMap<String, Object>();
//				params2.put("openID", openID);
//				params2.put("Activity", activity);
//				params2.put("mobile", mobile);// 手机号
//				params2.put("realName", realName);// 真实姓名
//				params2.put("identity", identity);// 身份证号
//				params2.put("cardNo", cardNo);// 身份证号
//
//				new SHRBManager(new WDResultCallback() {
//					@Override
//					public void onSucess(Map<String, Object> callbackMap) {
//						// TODO Auto-generated method stub
//						callback.onSucess(ResultGenerateUtil
//								.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"调用开户成功！"));
//					}
//
//					@Override
//					public void onFail(Map<String, Object> callbackMap) {
//						// TODO Auto-generated method stub
//						callback.onFail(callbackMap);
//					}
//				}).BuildWalletSDK(params2);
//			}
//
//			@Override
//			public void onFail(Map<String, Object> callbackMap) {
//				// TODO Auto-generated method stub
//				// 网络请求成功 获取数据失败
//				callback.onFail(callbackMap);
//			}
//		});
//
//	}

	/**
	 * 添加卡
	 * 
	 * @param params
	 * @param callback
	 */
	public void AddCardSDK(Map<String, Object> params,
			final WDResultCallback callback) {
		final String openID = (String) params.get("openID");
		final String personUnionID = (String) params.get("personUnionID");
		final String cardNo = (String) params.get("cardNo");// 卡号
		final String mobile = (String) params.get("mobile");// 手机号
		final Activity activity = (Activity) params.get("Activity");
		
		if (WDValidationUtil.isNull(openID)  || openID.length() < 1) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"openID参数有误"));
			return;
		}
		if (WDValidationUtil.isNull(personUnionID)|| personUnionID.length() < 2) {
			callback.onFail(ResultGenerateUtil
					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"personUnionID参数有误"));
			return;
		}
		if (!WDValidationUtil.checkBankCard0531(cardNo)) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"cardNo参数有误"));
			return;
		}
		if (WDValidationUtil.isNull(mobile) || !WDValidationUtil.isPhone0531(mobile)) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"mobile参数有误"));
			return;
		}
		if (activity == null
				|| WDValidationUtil.isSameType(activity, Activity.class)) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"activity参数有误"));
			return;
		}
		
		WalletEntryActivtiy.callback = callback;
		Intent intent = new Intent(activity, WalletEntryActivtiy.class);
		
		intent.putExtra(WalletEntryActivtiy.PAY_TYPE, TYPE_AddCard);
		intent.putExtra("openID", openID);
		intent.putExtra("personUnionID", personUnionID);
		intent.putExtra("cardNo", cardNo);
		intent.putExtra("mobile", mobile);
		
		activity.startActivity(intent);
		
	}
//	/**
//	 * 绑卡开户
//	 * 
//	 * @param params
//	 * @param callback
//	 */
//	public void AddCardSDK(Map<String, Object> params,
//			final WDResultCallback callback) {
//		final String realName = (String) params.get("realName");// 真实姓名
//		final String identity = (String) params.get("identity");// 身份证号
//		final String cardNo = (String) params.get("cardNo");// 卡号
//		final String mobile = (String) params.get("mobile");// 手机号
//		final Activity activity = (Activity) params.get("Activity");
//		final String openID = (String) params.get("openID");
//		final String sequence = (String) params.get("sequence");
//		final String merAppId = (String) params.get("merAppId");
//		final String merUserId = (String) params.get("merUserId");
//		final String personUnionID = (String) params.get("personUnionID");
//
//		if (realName == null || realName.length() < 2) {
//			callback.onFail(ResultGenerateUtil
//					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误：realName！"));
//			return;
//		}
//		if (identity == null || identity.length() < 16) {
//			callback.onFail(ResultGenerateUtil
//					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误：identity！"));
//			return;
//		}
//		if (cardNo == null || cardNo.length() < 16) {
//			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误：cardNo！"));
//			return;
//		}
//		if (mobile == null || mobile.length() < 11) {
//			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误：mobile！"));
//			return;
//		}
//		if (activity == null
//				|| WDValidationUtil.isSameType(activity, Activity.class)) {
//			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误！"));
//			return;
//		}
//		if (openID == null || openID.length() < 1) {
//			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误:openID！"));
//			return;
//		}
//		if (sequence == null || sequence.length() < 1) {
//			callback.onFail(ResultGenerateUtil
//					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误:sequence！"));
//			return;
//		}
//		if (merAppId == null || merAppId.length() < 2) {
//			callback.onFail(ResultGenerateUtil
//					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误：merAppId！"));
//			return;
//		}
//		if (merUserId == null || merUserId.length() < 2) {
//			callback.onFail(ResultGenerateUtil
//					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误：merUserId！"));
//			return;
//		}
//		if (personUnionID == null || personUnionID.length() < 2) {
//			callback.onFail(ResultGenerateUtil
//					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误：personUnionID！"));
//			return;
//		}
//
//		/** 网络请求参数 */
//		Map<String, Object> params1 = new HashMap<String, Object>();
//		params1.put("mobile", mobile);// 手机号
//		params1.put("realName", realName);// 真实姓名
//		params1.put("identity", identity);// 身份证号
//		params1.put("merAppId", merAppId);// 商户appid
//		params1.put("merUserId", merUserId);// 商户用户ID
//		params1.put("openID", openID);
//		params1.put("sequence", sequence);
//		params1.put("terminalType", "android");
//		BackgroundInterface.addCard(params1, new WDResultCallback() {
//			@Override
//			public void onSucess(Map<String, Object> callbackMap) {
//				// TODO Auto-generated method stub
//				if (!BackgroundInterface.HTTPS_SUCCESS.equals(callbackMap
//						.get("returnCode"))) {
//					callback.onFail(callbackMap);
//					return;
//				}
//
//				Map<String, Object> params2 = new HashMap<String, Object>();
//				params2.put("openID", openID);
//				params2.put("Activity", activity);
//				params2.put("mobile", mobile);// 手机号
//				params2.put("realName", realName);// 真实姓名
//				params2.put("identity", identity);// 身份证号
//				params2.put("cardNo", cardNo);// 身份证号
//				params2.put("personUnionID", personUnionID);// 身份证号
//				new SHRBManager(new WDResultCallback() {
//
//					@Override
//					public void onSucess(Map<String, Object> callbackMap) {
//						// TODO Auto-generated method stub
//
//						callback.onSucess(ResultGenerateUtil
//								.generateReturnMap(ResultGenerateUtil.ReturnCode.SUCCESS.toString(),"调用添加卡成功！"));
//					}
//
//					@Override
//					public void onFail(Map<String, Object> callbackMap) {
//						// TODO Auto-generated method stub
//						callback.onSucess(callbackMap);
//					}
//				}).AddCardSDK(params2);
//			}
//
//			@Override
//			public void onFail(Map<String, Object> callbackMap) {
//				// TODO Auto-generated method stub
//				// 网络请求成功 获取数据失败
//				callback.onFail(callbackMap);
//			}
//		});
//	}
}

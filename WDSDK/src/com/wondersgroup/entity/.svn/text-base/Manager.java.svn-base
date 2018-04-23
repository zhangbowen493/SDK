package com.wondersgroup.entity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;

import com.wondersgroup.PwServer.BackgroundInterface;
import com.wondersgroup.SHRB.SHRBManager;
import com.wondersgroup.utils.ResultGenerateUtil;
import com.wondersgroup.utils.WDValidationUtil;
import com.wondersgroup.wallet.WDGlobalSetting;
import com.wondersgroup.wallet.WDLogUtil;
import com.wondersgroup.wallet.WDResultCallback;

/*
 *@作者: kevin
 *@版本: 
 *@version create time：2017-3-18 下午2:57:46
 */
public class Manager {

	public Manager() {
		// TODO Auto-generated constructor stub
	}

	// public boolean isInit() {
	// return WDGlobalSetting.isInit;
	// }
	private static boolean Initing = false;

	/**
	 * 初始化sdk
	 * 
	 * @param params
	 *            key 类型{Context Activity, merAppId String, merAppKey String}
	 * @param callback
	 */
	public void initWalletSDK(final Map<String, Object> params,
			final WDResultCallback callback) {
		if (Initing) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(
					ResultGenerateUtil.ReturnCode.PROCESS_ERROR.toString(),
					"SDK正在初始化，不要重复初始化SDK"));
			return;
		}
		Initing = true;
		// if (isInit()) {
		// callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PROCESS_ERROR.toString(),"SDK已经初始化，不要重复初始化SDK"));
		// Initing = false;
		// return;
		// }

		final Context context = (Context) params.get("Context");
		String merAppId = (String) params.get("merAppId");
		String merAppKey = (String) params.get("merAppKey");

		if (context == null) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(
					ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),
					"context参数有误"));
			Initing = false;
			return;
		}
		if (WDValidationUtil.isNull(merAppId) || merAppId.length() < 6) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(
					ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),
					"参数有误：请输入正确的商户ID！"));
			Initing = false;
			return;
		}
		if (WDValidationUtil.isNull(merAppKey) || merAppKey.length() < 10) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(
					ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),
					"参数有误：请输入正确的商户密钥！"));
			Initing = false;
			return;
		}
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("merAppId", merAppId);
		params1.put("merAppKey", merAppKey);
		params1.put("terminalType", "android");
		// 钱包后台获取商户号和签名参数以及用于签名的随机数
		BackgroundInterface.init(context, params1, new WDResultCallback() {
			@Override
			public void onSucess(Map<String, Object> callbackMap) {
				// TODO Auto-generated method stub
				// 网络请求成功 获取数据成功

				if (!BackgroundInterface.HTTPS_SUCCESS.equals(callbackMap
						.get("returnCode"))) {
					Initing = false;
					callback.onFail(callbackMap);
					return;
				}

				String fundAppId = (String) callbackMap.get("fundAppId");
				final String random = (String) callbackMap.get("random");
				final String sign = (String) callbackMap.get("sign");

				if (WDValidationUtil.isNull(fundAppId)
						|| fundAppId.length() < 24) {
					Initing = false;
					callback.onFail(ResultGenerateUtil.generateReturnMap(
							ResultGenerateUtil.ReturnCode.SERVER_ERROR
									.toString(), "服务器商户id配置异常！"));
					return;
				}
				if (WDValidationUtil.isNull(random) || random.length() < 6) {
					Initing = false;
					callback.onFail(ResultGenerateUtil.generateReturnMap(
							ResultGenerateUtil.ReturnCode.SERVER_ERROR
									.toString(), "随机数异常！"));
					return;
				}
				if (WDValidationUtil.isNull(sign) || sign.length() < 24) {
					Initing = false;
					callback.onFail(ResultGenerateUtil.generateReturnMap(
							ResultGenerateUtil.ReturnCode.SERVER_ERROR
									.toString(), "加密信息异常！"));
					return;
				}

				Map<String, Object> params2 = new HashMap<String, Object>();
				params2.put("fundAppId", fundAppId);
				params2.put("Context", context);

				new SHRBManager(new WDResultCallback() {

					@Override
					public void onSucess(Map<String, Object> callbackMap) {
						// TODO Auto-generated method stub
						WDLogUtil.e("HRSDK初始化成功！");
						// 华瑞SDK初始化成功
						Map<String, Object> params3 = new HashMap<String, Object>();
						params3.put("random", random);
						params3.put("sign", sign);
						params3.put("Context", context);
						new SHRBManager(new WDResultCallback() {
							@Override
							public void onSucess(Map<String, Object> callbackMap) {
								// TODO Auto-generated method stub
								WDLogUtil.e("HRSDK验证开发者成功！");
								// 华瑞验证开发者失败成功 个人钱包SDK初始化成功
								// WDGlobalSetting.isInit= true;
								Initing = false;
								callback.onSucess(callbackMap);
							}

							@Override
							public void onFail(Map<String, Object> callbackMap) {
								// TODO Auto-generated method stub
								// 华瑞验证开发者失败
								WDLogUtil.e("HRSDK验证开发者失败！");
								Initing = false;
								// WDGlobalSetting.isInit= false;
								callback.onFail(callbackMap);
							}
						}).approveDev(params3);
					}

					@Override
					public void onFail(Map<String, Object> callbackMap) {
						// TODO Auto-generated method stub
						WDLogUtil.e("HRSDK初始化失败！");
						// 华瑞SDK初始化失败
						Initing = false;
						// WDGlobalSetting.isInit= false;
						callback.onFail(callbackMap);
					}
				}).init(params2);
			}

			@Override
			public void onFail(Map<String, Object> callbackMap) {
				// TODO Auto-generated method stub
				// WDGlobalSetting.isInit= false;
				Initing = false;
				// 网络请求成功 获取数据失败
				callback.onFail(callbackMap);
			}
		});

	}

	/**
	 * 一键登录
	 * 
	 * @param params
	 *            key 类型{merAppId String 商户ID, merUserId String 用户唯一标识}
	 * @param callback
	 */
	public void loginWalletSDK(Map<String, Object> params,
			final WDResultCallback callback) {

		// if(WDGlobalSetting.isInit == false){
		// callback.onFail(ResultGenerateUtil.generateErrorMap("SDK未初始化，请初始化SDK"));
		// return;
		// }
		String merAppId = (String) params.get("merAppId");
		String merUserId = (String) params.get("merUserId");

		if (WDValidationUtil.isNull(merAppId) || merAppId.length() < 1) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(
					ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),
					"参数有误：请输入正确的商户ID！"));
			return;
		}
		if (WDValidationUtil.isNull(merUserId) || merUserId.length() < 1) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(
					ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),
					"参数有误：请输入正确的用户ID！"));
			return;
		}

		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("merAppId", merAppId);
		params1.put("merUserId", merUserId);
		params1.put("terminalType", "android");

		BackgroundInterface.login(params1, new WDResultCallback() {
			@Override
			public void onSucess(Map<String, Object> callbackMap) {
				// TODO Auto-generated method stub
				if (!BackgroundInterface.HTTPS_SUCCESS.equals(callbackMap
						.get("returnCode"))) {
					callback.onFail(callbackMap);
					return;
				}

				Map<String, Object> params2 = new HashMap<String, Object>();
				final String sequcence = (String) callbackMap.get("sequcence");
				if (WDValidationUtil.isNull(sequcence)
						|| sequcence.length() < 1) {
					callback.onFail(ResultGenerateUtil.generateReturnMap(
							ResultGenerateUtil.ReturnCode.SERVER_ERROR
									.toString(), "用户序列号异常！"));
					return;
				}
				params2.put("sequcence", sequcence);

				new SHRBManager(new WDResultCallback() {
					@Override
					public void onSucess(Map<String, Object> callbackMap) {
						// TODO Auto-generated method stub
						// 登陆成功
						WDLogUtil.e("sequence==>" + sequcence);
						callbackMap.put("sequence", sequcence);
						callback.onSucess(callbackMap);
					}

					@Override
					public void onFail(Map<String, Object> callbackMap) {
						// TODO Auto-generated method stub
						// 一键登录失败
						callback.onFail(callbackMap);
					}
				}).userLogin(params2);
			}

			@Override
			public void onFail(Map<String, Object> callbackMap) {
				// TODO Auto-generated method stub
				// 网络请求成功 获取数据失败
				callback.onFail(callbackMap);
			}
		});
	}

}

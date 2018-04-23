package com.wondersgroup.entity;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.wondersgroup.PwServer.BackgroundInterface;
import com.wondersgroup.SHRB.SHRBManager;
import com.wondersgroup.utils.ResultGenerateUtil;
import com.wondersgroup.utils.WDValidationUtil;
import com.wondersgroup.wallet.WDResultCallback;
	/*
 *@作者: kevin
 *@版本: 
 *@version create time：2017-3-18 下午3:05:51
 */
public class Query {

	public Query() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 账户余额查询
	 * @param params
	 * @param callback
	 */
	public  void queryAccountBalance(Map<String, Object> params, final WDResultCallback callback) {

		Map<String, Object> params1 = new HashMap<String, Object>();
		final Context context =  (Context) params.get("Context");
		final String merAppId =  (String) params.get("merAppId");
		final String openId =  (String) params.get("openId");
		if(context==null){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"context参数有误"));
			return;
		}
		if(WDValidationUtil.isNull(openId)){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"openId参数有误"));
			return;
		}
		if(WDValidationUtil.isNull(merAppId)){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"merAppId参数有误"));
			return;
		}
		
		params1.put("Context", context);
		params1.put("merAppId", params.get("merAppId"));
		params1.put("openId", params.get("openId"));
		params1.put("terminalType", "android");
		BackgroundInterface.queryAccountBalance(params1, new WDResultCallback() {
			
			@Override
			public void onSucess(Map<String, Object> callbackMap) {
				// TODO Auto-generated method stub
				if(!BackgroundInterface.HTTPS_SUCCESS.equals(callbackMap.get("returnCode"))){
					callback.onFail(callbackMap);
					return;
				}
				callback.onSucess(callbackMap);
			}
			
			@Override
			public void onFail(Map<String, Object> callbackMap) {
				// TODO Auto-generated method stub
				callback.onFail(callbackMap);
			}
		});
	}
	/**
	 * 商户订单查询
	 * @param params
	 * @param callback
	 */
	public  void queryOrderList(Map<String, Object> params, final WDResultCallback callback) {

		Map<String, Object> params1 = new HashMap<String, Object>();
		final Context context =  (Context) params.get("Context");
		final String merAppId =  (String) params.get("merAppId");
		final String merUserId =  (String) params.get("merUserId");
		final String merOrderNO =  (String) params.get("merOrderNO");
		if(context==null){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"context参数有误"));
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
		if(WDValidationUtil.isNull(merOrderNO)||!WDValidationUtil.isOrderNO(merOrderNO)){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"merOrderNO参数有误"));
			return;
		}
		
		params1.put("Context", context);
		params1.put("merAppId", merAppId);
		params1.put("merUserId", merUserId);
		params1.put("merOrderNO", merOrderNO);
		params1.put("terminalType", "android");
		
		BackgroundInterface.queryOrderList(params1, new WDResultCallback() {
			
			@Override
			public void onSucess(Map<String, Object> callbackMap) {
				// TODO Auto-generated method stub
				if(!BackgroundInterface.HTTPS_SUCCESS.equals(callbackMap.get("returnCode"))){
					callback.onFail(callbackMap);
					return;
				}
				callback.onSucess(callbackMap);
			}
			
			@Override
			public void onFail(Map<String, Object> callbackMap) {
				// TODO Auto-generated method stub
				callback.onFail(callbackMap);
			}
		});
	}
	/**
	 * 用户交易明细查询
	 * @param params
	 * @param callback
	 */
	public void queryTransactionList(Map<String, Object> params, final WDResultCallback callback) {

		Map<String, Object> params1 = new HashMap<String, Object>();
		final Context context =  (Context) params.get("Context");
		final String merAppId =  (String) params.get("merAppId");
		final String merUserId =  (String) params.get("merUserId");
		final String openID =  (String) params.get("openID");
		final String startDate =  (String) params.get("startDate");
		final String endDate =  (String) params.get("endDate");
		
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
		if(WDValidationUtil.isNull(openID)){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"openID参数有误"));
			return;
		}
		if(WDValidationUtil.isNull(startDate)||!WDValidationUtil.isDateFormate(startDate)){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"startDate参数有误"));
			return;
		}
		if(WDValidationUtil.isNull(endDate)||!WDValidationUtil.isDateFormate(endDate)){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"endDate参数有误"));
			return;
		}
		
		params1.put("Context", context);
		params1.put("merAppId", merAppId);
		params1.put("merUserId", merUserId);
		params1.put("openID", openID);
		params1.put("startDate", startDate);
		params1.put("endDate", endDate);
		params1.put("terminalType", "android");
		BackgroundInterface.queryTransactionList(params1, new WDResultCallback() {
			
			@Override
			public void onSucess(Map<String, Object> callbackMap) {
				// TODO Auto-generated method stub
				if(!BackgroundInterface.HTTPS_SUCCESS.equals(callbackMap.get("returnCode"))){
					callback.onFail(callbackMap);
					return;
				}
				callback.onSucess(callbackMap);
			}
			
			@Override
			public void onFail(Map<String, Object> callbackMap) {
				// TODO Auto-generated method stub
				callback.onFail(callbackMap);
			}
		});
	}
	/**
	 * 用户信息查询
	 * @param params
	 * @param callback
	 */
	public void queryUserInfo(Map<String, Object> params, final WDResultCallback callback) {
		
		Map<String, Object> params1 = new HashMap<String, Object>();
		final Context context =  (Context) params.get("Context");
		params1.put("Context", context);
		params1.put("merAppId", params.get("merAppId"));
		params1.put("merUserId", params.get("merUserId"));
		params1.put("openID", params.get("openID"));
		params1.put("terminalType", "android");
		BackgroundInterface.queryTransactionInfo(params1, new WDResultCallback() {
			
			@Override
			public void onSucess(Map<String, Object> callbackMap) {
				// TODO Auto-generated method stub
				if(!BackgroundInterface.HTTPS_SUCCESS.equals(callbackMap.get("returnCode"))){
					callback.onFail(callbackMap);
					return;
				}
				callback.onSucess(callbackMap);
			}
			
			@Override
			public void onFail(Map<String, Object> callbackMap) {
				// TODO Auto-generated method stub
				callback.onFail(callbackMap);
			}
		});
	}
	/**
	 * 已綁定銀行卡列表查詢
	 * @param params
	 * @param callback
	 */
	public void queryBindedCardList(Map<String, Object> params, final WDResultCallback callback){
		Map<String, Object> params1 = new HashMap<String, Object>();
		
		final Context context =  (Context) params.get("Context");
		final String openId =  (String) params.get("openId");
		if(context==null){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"context参数有误"));
			return;
		}
		if(WDValidationUtil.isNull(openId)){
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString()
					,"openId参数有误"));
			return;
		}
		params1.put("Context", context);
		params1.put("openId", openId);
		params1.put("terminalType", "android");
		
		BackgroundInterface.queryBindedCardList(params1, new WDResultCallback() {
			
			@Override
			public void onSucess(Map<String, Object> callbackMap) {
				// TODO Auto-generated method stub
				if(!BackgroundInterface.HTTPS_SUCCESS.equals(callbackMap.get("returnCode"))){
					callback.onFail(callbackMap);
					return;
				}
				callback.onSucess(callbackMap);
			}
			
			@Override
			public void onFail(Map<String, Object> callbackMap) {
				// TODO Auto-generated method stub
				callback.onFail(callbackMap);
			}
		});
	}
	/**
	 * 查看账户明文
	 * @param params
	 * @param callback
	 */
	public void queryAccountMsg(Map<String, Object> params, final WDResultCallback callback){
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("openID", params.get("openID"));
		params1.put("personUnionID", params.get("personUnionID"));
		params1.put("Activity", params.get("Activity"));
		new SHRBManager(callback).accountPlain(params1);
	}
}



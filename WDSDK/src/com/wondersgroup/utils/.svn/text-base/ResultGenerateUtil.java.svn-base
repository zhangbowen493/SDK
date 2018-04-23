package com.wondersgroup.utils;

import java.util.HashMap;
import java.util.Map;

import com.shrb.walletsdk.SHRBWalletSDK;
import com.wondersgroup.SHRB.SHRBManager;
import com.wondersgroup.wallet.WDLogUtil;


/**
 * 接口返回结果生成工具类
 * @author liuzhipeng
 *
 */
public class ResultGenerateUtil {
	
	/**
	 * returnCode 参数
	 * @author kevin
	 *
	 */
	public static enum ReturnCode{
		SUCCESS,PARAM_ERROR,NET_ERROR,PROCESS_ERROR,SERVER_ERROR,CHHR_ERROR,CANCEL
	}
	/**
	 * returnMsg 参数
	 * @author kevin
	 *
	 */
	public static enum ReturnMsg{
		sdk000001,//未初始化SDK
		sdk000002,//	未验证开发者
		sdk010001,//	用户已开通钱包
		sdk010002,//	用户未开通钱包
		sdk010003,//	未调用一键登录
		sdk010004,//	openID不匹配
		sdk010005,//	personUnionID不匹配
		sdk010006,//	身份证号格式不正确
		sdk010007,//	姓名格式不正确
		sdk010008,//	手机号格式不正确
		sdk010009,//	银行卡号格式不正确
		sdk010010,//	openID为空
		sdk010011,//	personUnionID为空
		sdk020001,//	充值金额为空
		sdk020002,//	提现金额为空
		sdk020003,//	支付金额为空
		sdk020004,//	支付金额必须等于订单金额减优惠金额
		sdk020005,//	金额格式错误
		sdk090001,//	网络错误
	}
	
	public static Map<String,Object> generateReturnMap(String TAG , String msg){
		WDLogUtil.e(msg);
		Map<String, Object> callbackMap = new HashMap<String, Object>();
		callbackMap.put("returnCode", TAG);
		callbackMap.put("returnMsg", msg);
		return callbackMap;
	}

//	/**
//	 * 网络请求错误
//	 * @param error
//	 * @return
//	 */
//	public static Map<String, Object> generateNETErrorMap(String error) {
//		
//		WDLogUtil.e(error);
//		Map<String, Object> callbackMap = new HashMap<String, Object>();
//		callbackMap.put("returnCode", HTTPS_Fild);
//		callbackMap.put("returnMsg", HTTPS_Fild_MSG);
//		callbackMap.put("errorCode", HTTPS_Fild);
//		callbackMap.put("errorMsg", error);
//		return callbackMap;
//	}
//	/**
//	 * 返回自定义错误
//	 * @param TAG
//	 * @param error
//	 * @return
//	 */
//	public static Map<String, Object> generateErrorMap( String TAG) {
//		WDLogUtil.e(TAG);
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("returnCode", "001");
//		map.put("returnMsg", "fail");
//		map.put("errorCode", "0011");
//		map.put("errorMsg", TAG);
//		return map;
//	}
//	
//	
//	public static Map<String, Object> generateSuccessMap(String info) {
//		WDLogUtil.e("generateSuccessMap==>"+info);
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("returnCode", "00");
//		map.put("returnMsg", info);
//		map.put("errorCode", "00");
//		map.put("errorMsg", "");
//		return map;
//	}
//	
//	/**
//	 * 返回第三方渠道 自定义错误信息
//	 * @param error
//	 * @return
//	 */
//	public static Map<String, Object> generateHRErrorMap(String errorCode ,String error) {
//		WDLogUtil.e(error);
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("returnCode", "HR001");
//		map.put("returnMsg", "fail");
//		map.put("errorCode", "HR"+errorCode);
//		map.put("errorMsg", error);
//		return map;
//	}
	/**
	 * 返回第三方渠道成功并带参数map
	 * @param mapData
	 * @return
	 */
	public static Map<String,Object> generateHRMap(Map<String, Object> mapData){
		WDLogUtil.e("generateHRMap==>"+mapData.toString());
		Map<String, Object> map = new HashMap<String, Object>();
		
		//获取返回结果returnCode
		String returnCode =(String) mapData.get("returnCode");
		String errorCode =(String) mapData.get("errorCode");
		
		if("999001".equals(returnCode)){
			//用户取消
			map.put("returnCode", ReturnCode.CANCEL);
			map.put("returnMsg", mapData.get("returnMsg"));
			
		}else if(SHRBWalletSDK.HRSDK_HR_CODE_SUCCESS.equals(returnCode)){
			map.put("returnCode", ReturnCode.SUCCESS.toString());
			map.put("returnMsg", ReturnCode.SUCCESS.toString());
		}else{
			map.put("returnCode", ReturnCode.CHHR_ERROR.toString());
			map.put("returnMsg", mapData.get("errorCode"));
		}
//
//		//TODO：可以获取接口中其他字段，用作业务逻辑处理
//			map.put("returnCode", "HR-"+returnCode);
//			map.put("errorCode", "HR-"+mapData.get("errorCode"));//返回调试错误码
		
		 for (Map.Entry<String, Object> entry : mapData.entrySet()) {
			 if("errorCode".equals(entry.getKey())|| "returnCode".equals(entry.getKey())
					 || "returnMsg".equals(entry.getKey())|| "errorMsg".equals(entry.getKey())
					 ){
					 continue;
			 }else{
				 map.put(entry.getKey(), mapData.get(entry.getKey()));
			 }
         }
		return map;
	}

}

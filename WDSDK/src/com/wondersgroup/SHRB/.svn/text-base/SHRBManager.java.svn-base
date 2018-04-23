package com.wondersgroup.SHRB;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.shrb.walletsdk.SHRBWalletSDK;
import com.wondersgroup.utils.ResultGenerateUtil;
import com.wondersgroup.utils.WDValidationUtil;
import com.wondersgroup.wallet.WDLogUtil;
import com.wondersgroup.wallet.WDResultCallback;

/**
 * 华瑞银行sdk业务封装类
 * 
 * @author liuzhipeng
 *
 */
public class SHRBManager {

	public static String SUCCESS_CODE = "000000";

	private WDResultCallback mCallback;

	/**
	 * 判断获取到的returnCode是否为“000000”
	 * 
	 * @param code
	 * @return
	 */
	public static Boolean checkReturnCode(String code) {
		if (null != code && !TextUtils.isEmpty(code) && !"".equals(code)) {
			if (SUCCESS_CODE.equals(code)) {
				return true;
			}
			return false;
		}
		return false;
	}

	// private void callbackError(String returnCode, String returnMsg,String
	// errorCode,String errorMsg) {
	// Map<String, Object> map = new HashMap<String, Object>();
	// map.put("returnCode", returnCode);
	// map.put("returnMsg", returnMsg);
	// map.put("errorCode","HR"+ errorCode);
	// map.put("errorMsg", errorMsg);
	// mCallback.onFail(map);
	// }
	// private void callbackError(String returnCode, String returnMsg,String
	// errorCode,String errorMsg) {
	// Map<String, Object> map = new HashMap<String, Object>();
	// map.put("returnCode", returnCode);
	// map.put("returnMsg", returnMsg);
	// map.put("errorCode","HR"+ errorCode);
	// map.put("errorMsg", errorMsg);
	// mCallback.onFail(map);
	// }

	public SHRBManager(WDResultCallback callback) {
		this.mCallback = callback;
	}

	/**
	 * 初始化sdk
	 * 
	 * @param params
	 */
	public void init(Map<String, Object> params) {
		// app识别号
		String appID = (String) params.get("fundAppId");
		Activity context = (Activity) params.get("Context");

		SHRBWalletSDK.initWithAppID(appID, context, new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Map responseMap = (Map) msg.obj;
				String returnCode = (String) responseMap.get("returnCode");
				if (returnCode != null) {
					Map<String, Object> hrMap = ResultGenerateUtil
							.generateHRMap(responseMap);
					if (checkReturnCode(responseMap.get("returnCode")
							.toString())) {
						// 初始化成功
						mCallback.onSucess(hrMap);

					} else {
						// 初始化失败
						mCallback.onFail(hrMap);
					}
					// if
					// (checkReturnCode(responseMap.get("returnCode").toString()))
					// {
					// //初始化成功
					// Map<String, Object> map = new HashMap<String, Object>();
					// map.put("returnCode",
					// responseMap.get("returnCode").toString());
					// map.put("returnMsg",
					// responseMap.get("returnMsg").toString());
					// mCallback.onSucess(map);
					//
					// } else {
					// //初始化失败
					//
					// callbackError(responseMap);
					// //
					// callbackError(responseMap.get("returnCode").toString(),
					// // responseMap.get("returnMsg").toString(),
					// // responseMap.get("errorCode").toString(),
					// // responseMap.get("errorMsg").toString());
					// }
				}
				WDLogUtil.d("initWithAppID ==>" + responseMap.toString());
			}
		});

	}

	/**
	 * 验证开发者
	 * 
	 * @param params
	 */
	public void approveDev(Map<String, Object> params) {
		// 开发者服务器提供的MD5签名sign，使⽤appID + randomNumber + secret ⽣成的MD5签名字符串
		String sign = (String) params.get("sign");
		// ⽤来⽣成MD5签名的随机数
		String random = (String) params.get("random");

		WDLogUtil.e("approveDev==>" + params.toString());

		SHRBWalletSDK.approveDev(sign, random, new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Map responseMap = (Map) msg.obj;
				String returnCode = (String) responseMap.get("returnCode");
				if (returnCode != null) {

					Map<String, Object> hrMap = ResultGenerateUtil
							.generateHRMap(responseMap);
					if (checkReturnCode(responseMap.get("returnCode")
							.toString())) {
						// 初始化成功
						mCallback.onSucess(hrMap);

					} else {
						// 初始化失败
						mCallback.onFail(hrMap);
					}

					// if
					// (checkReturnCode(responseMap.get("returnCode").toString()))
					// {
					// //开发者认证成功
					// Map<String, Object> map = new HashMap<String, Object>();
					// map.put("returnCode",
					// responseMap.get("returnCode").toString());
					// map.put("returnMsg",
					// responseMap.get("returnMsg").toString());
					// mCallback.onSucess(map);
					// } else {
					// //开发者认证失败
					// callbackError(responseMap.get("returnCode").toString(),
					// responseMap.get("returnMsg").toString(),
					// responseMap.get("errorCode").toString(),
					// responseMap.get("errorMsg").toString());
					// }
				}
				WDLogUtil.d("initWithAppID ==>" + responseMap.toString());
			}
		});

	}

	/**
	 * ⼀键登录
	 * 
	 * @param params
	 */
	public void userLogin(Map<String, Object> params) {
		//钱包后台服务器提供的商户用户序列号sequcence
		String sequcence = (String) params.get("sequcence");
		WDLogUtil.e("sequcence==>"+sequcence);
		SHRBWalletSDK.Users.getUserIDs(sequcence, new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Map responseMap = (Map) msg.obj;
				String returnCode = (String) responseMap.get("returnCode");
				if (returnCode != null) {

					Map<String, Object> hrMap = ResultGenerateUtil
							.generateHRMap(responseMap);
					if (checkReturnCode(responseMap.get("returnCode")
							.toString())) {
						// 初始化成功
						mCallback.onSucess(hrMap);
					} else {
						// 初始化失败
						mCallback.onFail(hrMap);
					}
				}
			}
		});
	}

	/**
	 * 查看账户明文
	 * 
	 * @param params
	 */
	public void accountPlain(Map<String, Object> params) {

		Map<String, Object> extraMessage = new HashMap<String, Object>();
		extraMessage.put("", "");
		Activity activity = (Activity) params.get("Activity");
		String openID = (String) params.get("openID");
		String personUnionID = (String) params.get("personUnionID");
		if (activity == null
				|| WDValidationUtil.isSameType(activity, Activity.class)) {
			mCallback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误:activity！"));
			return;
		}
		if (openID == null || openID.length() < 1) {
			mCallback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误:openID！"));
			return;
		}
		if (personUnionID == null || personUnionID.length() < 1) {
			mCallback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误:personUnionID！"));
			return;
		}

		SHRBWalletSDK.Account.accountPlain(openID, personUnionID, extraMessage,
				activity);

	}

	/**
	 * 开通钱包
	 * 
	 * @param params
	 * @param callback
	 */
	public void BuildWalletSDK(Map<String, Object> params) {
		String realName = (String) params.get("realName");// 真实姓名
		String identity = (String) params.get("identity");// 身份证号
		String cardNo = (String) params.get("cardNo");// 卡号
		String mobile = (String) params.get("mobile");// 手机号
//		if (realName == null) {
//			WDLogUtil.e( "realName="+realName);
//			mCallback.onFail(ResultGenerateUtil
//					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误:realName有误！"));
//			return;
//		}
//		if (identity == null || WDValidationUtil.getValidIdCard(identity)) {
//			mCallback.onFail(ResultGenerateUtil
//					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误:identity有误！"));
//			return;
//		}
//		if (cardNo == null || WDValidationUtil.checkBankCard(cardNo)) {
//			mCallback.onFail(ResultGenerateUtil
//					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误:cardNumb有误！"));
//			return;
//		}
//		if (mobile == null || WDValidationUtil.isPhone(mobile)) {
//			mCallback.onFail(ResultGenerateUtil
//					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误:phoneNumb有误！"));
//			return;
//		}

		Map<String, Object> extraMessage = new HashMap<String, Object>();
		extraMessage.put("mobile", mobile);
		extraMessage.put("realName", realName);
		extraMessage.put("identity", identity);
		extraMessage.put("cardNo", cardNo);
		Activity activity = (Activity) params.get("Activity");
		String openID = (String) params.get("openID");
		SHRBWalletSDK.Users.bindCard(openID, extraMessage, activity);
		mCallback.onSucess(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.SUCCESS.toString(),"开通钱包调用成功"));
	}

	/**
	 * 绑卡开户
	 * 
	 * @param params
	 * @param callback
	 */
	public void AddCardSDK(Map<String, Object> params) {

		String realName = (String) params.get("realName");// 真实姓名
		if (realName == null || WDValidationUtil.isName(realName)) {
			mCallback.onFail(ResultGenerateUtil
					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误:realName有误！"));
			return;
		}
		String identity = (String) params.get("identity");// 身份证号
		if (identity == null || WDValidationUtil.getValidIdCard0531(identity)) {
			mCallback.onFail(ResultGenerateUtil
					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误:identity有误！"));
			return;
		}
		String cardNo = (String) params.get("cardNo");// 卡号
		if (WDValidationUtil.checkBankCard0531(cardNo)) {
			mCallback.onFail(ResultGenerateUtil
					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"cardNumb参数有误"));
			return;
		}
		String mobile = (String) params.get("mobile");// 手机号
		if (mobile == null || WDValidationUtil.isPhone(mobile)) {
			mCallback.onFail(ResultGenerateUtil
					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误:phoneNumb有误！"));
			return;
		}
		Map<String, Object> extraMessage = new HashMap<String, Object>();
		extraMessage.put("mobile", mobile);
		extraMessage.put("realName", realName);
		extraMessage.put("identity", identity);
		extraMessage.put("cardNo", cardNo);
		Activity activity = (Activity) params.get("Activity");
		if (activity == null
				|| WDValidationUtil.isSameType(activity, Activity.class)) {
			mCallback.onFail(ResultGenerateUtil
					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误:activity有误！"));
			return;
		}
		String openID = (String) params.get("openID");
		if (openID == null || openID.length() < 1) {
			mCallback.onFail(ResultGenerateUtil
					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误：open有误！"));
			return;
		}
		String personUnionID = (String) params.get("personUnionID");
		if (personUnionID == null || personUnionID.length() < 1) {
			mCallback.onFail(ResultGenerateUtil
					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误：personUnionID有误！"));
			return;
		}
		SHRBWalletSDK.Users.addCard(openID, personUnionID, extraMessage, activity);
		mCallback.onSucess(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.SUCCESS.toString(),"添加卡调用"));
	}

	/**
	 * 充值
	 * 
	 * @param params
	 */
	public void deposits(Map<String, Object> params) {

		Map<String, Object> extraMessage = new HashMap<String, Object>();
		String outTradeNo = (String) params.get("outTradeNo");
		if (outTradeNo == null) {
			mCallback.onFail(ResultGenerateUtil
					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误:订单号不可为空！"));
			return;
		}
		extraMessage.put("outTradeNo", outTradeNo);

		Activity activity = (Activity) params.get("activity");
		if (activity == null
				|| WDValidationUtil.isSameType(activity, Activity.class)) {
			mCallback.onFail(ResultGenerateUtil
					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误:activity有误！"));
			return;
		}
		String openID = (String) params.get("openID");
		if (openID == null || openID.length() < 1) {
			mCallback.onFail(ResultGenerateUtil
					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误：open有误！"));
			return;
		}
		String personUnionID = (String) params.get("personUnionID");
		if (personUnionID == null || personUnionID.length() < 1) {
			mCallback.onFail(ResultGenerateUtil
					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误：personUnionID有误！"));
			return;
		}
		String money = WDValidationUtil.fenToYuan((String) params.get("tranAmt"));
		if (money == null || money.length() < 1) {
			mCallback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误：金额有误！"));
			return;
		}

		SHRBWalletSDK.Account.deposits(openID, personUnionID, money, extraMessage,
				activity);
	}

	/**
	 * 提現
	 * 
	 * @param params
	 * @param callback
	 */
	public void withdraw(Map<String, Object> params) {
		Map<String, String> extraMessage = new HashMap<String, String>();

		Activity activity = (Activity) params.get("Activity");
		String openID = (String) params.get("openID");
		String personUnionID = (String) params.get("personUnionID");
		String outTradeNo = (String) params.get("outTradeNo");// 商户订单号
		String tranAmt = WDValidationUtil.fenToYuan((String) params.get("tranAmt"));
		if (activity == null
				|| WDValidationUtil.isSameType(activity, Activity.class)) {
			mCallback.onFail(ResultGenerateUtil
					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误！：Activity"));
			return;
		}
		if (openID == null || openID.length() < 1) {
			mCallback.onFail(ResultGenerateUtil
					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误！:openID"));
			return;
		}
		if (personUnionID == null || personUnionID.length() < 1) {
			mCallback.onFail(ResultGenerateUtil
					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误！:personUnionID"));
			return;
		}

		if (outTradeNo == null || outTradeNo.length() < 1) {
			mCallback.onFail(ResultGenerateUtil
					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误！：订单号outTradeNo："+outTradeNo));
			return;
		}

		extraMessage.put("outTradeNo", outTradeNo);

		if (tranAmt == null || tranAmt.length() < 1) {
			mCallback
			.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误！：tranAmt"));
			return;
		}
		SHRBWalletSDK.Account.withdraw(openID, personUnionID, tranAmt, extraMessage,
				activity);
		mCallback.onSucess(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.SUCCESS.toString(),"提现调用成功"));
	}

	/**
	 * 订单⽀付
	 * 
	 * @param params
	 */
	public void orderPay(Map<String, Object> params) {

		Activity activity = (Activity) params.get("activity");
		String openID = (String) params.get("openID");// 用户ID
		String personUnionID = (String) params.get("personUnionID");// 开通钱包标识
		String mchID = (String) params.get("mchID");// 商户号
		String outTradeNo = (String) params.get("outTradeNo");// 商户订单号
		String body = (String) params.get("body");// 商品描述
		String random = (String) params.get("random");// 随机数
		String payAmt = WDValidationUtil.fenToYuan((String) params.get("payAmt"));// 订单金额（展示金额）必输
		String discountAmt = WDValidationUtil.fenToYuan((String) params.get("discountAmt"));// 优惠金额（无优惠请输入0）必输
		String payFee = WDValidationUtil.fenToYuan((String) params.get("payFee"));// 支付金额（实际支付金额）必输
		String mchName = (String) params.get("mchName");// 商户名
		String feeType = (String) params.get("feeType");// 货币类型 默认：CNY 人民币
		String sign = (String) params.get("sign");// 签名
		String attach = (String) params.get("attach");// 附加数据
		if (attach == null || attach.length() < 1) {
			mCallback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:attach"));
			return;
		}
		String confirmOrder = (String) params.get("confirmOrder");// 确认支付
		if (confirmOrder == null || confirmOrder.length() < 1) {
			mCallback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:confirmOrder"));
			return;
		}
		// N:用户提交订单后，立即扣款
		String limitPay = (String) params.get("limitPay");// 指定支付方式 01:
		if (limitPay == null || limitPay.length() < 1) {
			mCallback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:limitPay"));
			return;
		}
		// 绑定的银行卡或者账户
		String goodsTag = (String) params.get("goodsTag");// 商品标记（预留）
		if (goodsTag == null || goodsTag.length() < 1) {
			mCallback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:goodsTag"));
			return;
		}
		String timeValid = (String) params.get("timeValid");// 交易有效时间 （预留）
		if (timeValid == null || timeValid.length() < 1) {
			mCallback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:timeValid"));
			return;
		}
		String deviceInfo = (String) params.get("deviceInfo");// 硬件信息（预留）
		if (deviceInfo == null || deviceInfo.length() < 1) {
			mCallback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:deviceInfo"));
			return;
		}
		String detail = (String) params.get("detail");// 商品详情
		if (detail == null || detail.length() < 1) {
			mCallback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:detail"));
			return;
		}
		Map<String, Object> extraMessage = new HashMap<String, Object>();
		extraMessage.put("attach", attach);// 附加数据
		extraMessage.put("confirmOrder", confirmOrder);// 确认支付 N:用户提交订单后，立即扣款
		extraMessage.put("limitPay", limitPay);// 指定支付方式 01: 绑定的银行卡或者账户
		extraMessage.put("goodsTag", goodsTag);// 商品标记（预留）
		extraMessage.put("timeValid", timeValid);// 交易有效时间 （预留）
		extraMessage.put("deviceInfo", deviceInfo);// 硬件信息（预留）
		extraMessage.put("body", body);// 商品描述
		extraMessage.put("detail", detail);// 商品详情

		if (activity == null
				|| WDValidationUtil.isSameType(activity, Activity.class)) {
			mCallback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:activity:"+activity.toString()));
			return;
		}
		if (openID == null || openID.length() < 1) {
			mCallback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:openID"));
			return;
		}
		if (personUnionID == null || personUnionID.length() < 1) {
			mCallback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:personUnionID"));
			return;
		}
		if (mchID == null || mchID.length() < 1) {
			mCallback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:mchID"));
			return;
		}
		if (outTradeNo == null || outTradeNo.length() < 1) {
			mCallback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:outTradeNo"));
			return;
		}
		if (body == null || body.length() < 1) {
			mCallback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:body"));
			return;
		}
		if (random == null || random.length() < 1) {
			mCallback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:random"));
			return;
		}
		if (payAmt == null || payAmt.length() < 1) {
			mCallback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:payAmt"));
			return;
		}
		if (discountAmt == null || discountAmt.length() < 1) {
			mCallback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:discountAmt"));
			return;
		}
		if (payFee == null || payFee.length() < 1) {
			mCallback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:payFee"));
			return;
		}
		if (mchName == null || mchName.length() < 1) {
			mCallback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:mchName"));
			return;
		}
		if (feeType == null || feeType.length() < 1) {
			mCallback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:feeType"));
			return;
		}
		if (sign == null || sign.length() < 1) {
			mCallback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:sign"));
			return;
		}
		
		SHRBWalletSDK.Account.orderPay(openID, personUnionID, mchName, mchID,
				outTradeNo, random, sign, payAmt, discountAmt, payFee, feeType,
				extraMessage, activity);
		mCallback.onSucess(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.SUCCESS.toString(),"支付调用成功！"));
	}

}

package com.wondersgroup.wallet;

import java.util.HashMap;
import java.util.Map;

import com.shrb.walletsdk.SHRBWalletSDK;
import com.wondersgroup.entity.Account;
import com.wondersgroup.entity.Transaction;
import com.wondersgroup.utils.ResultGenerateUtil;
import com.wondersgroup.utils.WDValidationUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/*
 *@作者: kevin
 *@版本: 
 *@version create time：2017-3-27 上午10:46:55
 */
public class WalletEntryActivtiy extends Activity {

	private static final String TAG = "WalletEntryActivtiy";

	public static WDResultCallback callback;

	public static final String PAY_TYPE = "PAY_TYPE";

	private Activity activity;

	public WalletEntryActivtiy() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity = WalletEntryActivtiy.this;
		getData();
	}

	private void getData() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		String payType = intent.getStringExtra(PAY_TYPE);
		if (Account.TYPE_BindCard.equals(payType)) {
			bindCard(intent);
		} else if (Account.TYPE_AddCard.equals(payType)) {
			addCard(intent);
		} else if(Transaction.TYPE_Recharge.equals(payType)){
			Recharge(intent);
		} else if(Transaction.TYPE_Spend.equals(payType)){
			Spend(intent);
		}else if(Transaction.TYPE_Extract.equals(payType)){
			Extract(intent);
		}

	}

	/**
	 * 提现
	 * @param intent
	 */
	private void Extract(Intent intent) {
		// TODO Auto-generated method stub
		Map<String, String> extraMessage = new HashMap<String, String>();

		String openID = intent.getStringExtra("openID");
		String personUnionID = intent.getStringExtra("personUnionID");
		String outTradeNo = intent.getStringExtra("outTradeNo");// 商户订单号
		String tranAmt = WDValidationUtil.fenToYuan(intent.getStringExtra("tranAmt"));
		if (activity == null
				|| WDValidationUtil.isSameType(activity, Activity.class)) {
			callback.onFail(ResultGenerateUtil
					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误！：Activity"));
			return;
		}
		if (openID == null || openID.length() < 1) {
			callback.onFail(ResultGenerateUtil
					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误！:openID"));
			return;
		}
		if (personUnionID == null || personUnionID.length() < 1) {
			callback.onFail(ResultGenerateUtil
					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误！:personUnionID"));
			return;
		}

		if (outTradeNo == null || outTradeNo.length() < 1) {
			callback.onFail(ResultGenerateUtil
					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误！：订单号outTradeNo："+outTradeNo));
			return;
		}

		extraMessage.put("outTradeNo", outTradeNo);

		if (tranAmt == null || tranAmt.length() < 1) {
			callback
			.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误！：tranAmt"));
			return;
		}
		SHRBWalletSDK.Account.withdraw(openID, personUnionID, tranAmt, extraMessage,
				activity);
		
		
	}

	/**
	 * 消费
	 * @param intent
	 */
	private void Spend(Intent intent) {
		// TODO Auto-generated method stub
		WDLogUtil.e(TAG, "调用消费！");
		String openID = intent.getStringExtra("openID");// 用户ID
		String personUnionID = intent.getStringExtra("personUnionID");// 开通钱包标识
		String mchID = intent.getStringExtra("mchID");// 商户号
		String outTradeNo = intent.getStringExtra("outTradeNo");// 商户订单号
		String body = intent.getStringExtra("body");// 商品描述
		String random = intent.getStringExtra("random");// 随机数
		String payAmt = WDValidationUtil.fenToYuan(intent.getStringExtra("payAmt"));// 订单金额（展示金额）必输
		String discountAmt = WDValidationUtil.fenToYuan(intent.getStringExtra("discountAmt"));// 优惠金额（无优惠请输入0）必输
		String payFee = WDValidationUtil.fenToYuan(intent.getStringExtra("payFee"));// 支付金额（实际支付金额）必输
		String mchName = intent.getStringExtra("mchName");// 商户名
		String feeType = intent.getStringExtra("feeType");// 货币类型 默认：CNY 人民币
		String sign = intent.getStringExtra("sign");// 签名
		String attach = intent.getStringExtra("attach");// 附加数据
		String confirmOrder = intent.getStringExtra("confirmOrder");// 确认支付
		// N:用户提交订单后，立即扣款
		String limitPay = intent.getStringExtra("limitPay");// 指定支付方式 01:
		// 绑定的银行卡或者账户
		String goodsTag = intent.getStringExtra("goodsTag");// 商品标记（预留）
		String timeValid = intent.getStringExtra("timeValid");// 交易有效时间 （预留）
		String deviceInfo = intent.getStringExtra("deviceInfo");// 硬件信息（预留）
		String detail = intent.getStringExtra("detail");// 商品详情
		
		if (attach == null || attach.length() < 1) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:attach"));
			return;
		}
		if (confirmOrder == null || confirmOrder.length() < 1) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:confirmOrder"));
			return;
		}
		if (limitPay == null || limitPay.length() < 1) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:limitPay"));
			return;
		}
		if (goodsTag == null || goodsTag.length() < 1) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:goodsTag"));
			return;
		}
		if (timeValid == null || timeValid.length() < 1) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:timeValid"));
			return;
		}
		if (deviceInfo == null || deviceInfo.length() < 1) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:deviceInfo"));
			return;
		}
		if (detail == null || detail.length() < 1) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:detail"));
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
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:activity:"+activity.toString()));
			return;
		}
		if (openID == null || openID.length() < 1) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:openID"));
			return;
		}
		if (personUnionID == null || personUnionID.length() < 1) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:personUnionID"));
			return;
		}
		if (mchID == null || mchID.length() < 1) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:mchID"));
			return;
		}
		if (outTradeNo == null || outTradeNo.length() < 1) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:outTradeNo"));
			return;
		}
		if (body == null || body.length() < 1) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:body"));
			return;
		}
		if (random == null || random.length() < 1) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:random"));
			return;
		}
		if (payAmt == null || payAmt.length() < 1) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:payAmt"));
			return;
		}
		if (discountAmt == null || discountAmt.length() < 1) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:discountAmt"));
			return;
		}
		if (payFee == null || payFee.length() < 1) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:payFee"));
			return;
		}
		if (mchName == null || mchName.length() < 1) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:mchName"));
			return;
		}
		if (feeType == null || feeType.length() < 1) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:feeType"));
			return;
		}
		if (sign == null || sign.length() < 1) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"SHRManager消费——>参数有误！:sign"));
			return;
		}
		
		SHRBWalletSDK.Account.orderPay(openID, personUnionID, mchName, mchID,
				outTradeNo, random, sign, payAmt, discountAmt, payFee, feeType,
				extraMessage, activity);
		
	}

	/**
	 * 充值
	 * @param intent
	 */
	private void Recharge(Intent intent) {
		// TODO Auto-generated method stub
		WDLogUtil.e(TAG, "调用充值！");
		String outTradeNo = intent.getStringExtra("outTradeNo");
		String openID = intent.getStringExtra("openID");
		String personUnionID = intent.getStringExtra("personUnionID");
		String money = WDValidationUtil.fenToYuan(intent.getStringExtra("tranAmt"));
		Map<String, Object> extraMessage = new HashMap<String, Object>();
		if (outTradeNo == null) {
			callback.onFail(ResultGenerateUtil
					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误:订单号不可为空！"));
			WalletEntryActivtiy.this.finish();
			return;
		}
		extraMessage.put("outTradeNo", outTradeNo);

		if (openID == null || openID.length() < 1) {
			callback.onFail(ResultGenerateUtil
					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误：open有误！"));
			WalletEntryActivtiy.this.finish();
			return;
		}
		if (personUnionID == null || personUnionID.length() < 1) {
			callback.onFail(ResultGenerateUtil
					.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误：personUnionID有误！"));
			WalletEntryActivtiy.this.finish();
			return;
		}
		if (money == null || money.length() < 1) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),"参数有误：金额有误！"));
			WalletEntryActivtiy.this.finish();
			return;
		}

		SHRBWalletSDK.Account.deposits(openID, personUnionID, money, extraMessage,
				activity);
	}

	/**
	 * 添加卡
	 * 
	 * @param intent
	 */
	private void addCard(Intent intent) {
		// TODO Auto-generated method stub

		String openID = intent.getStringExtra("openID");
		String personUnionID = intent.getStringExtra("personUnionID");// 开通钱包标识
		String cardNo = intent.getStringExtra("cardNo");// 卡号
		String mobile = intent.getStringExtra("mobile");// 手机号

		if (cardNo == null) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(
					ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),
					"参数有误:cardNumb有误！"));
			WalletEntryActivtiy.this.finish();
			return;
		}
		if (mobile == null) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(
					ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),
					"参数有误:phoneNumb有误！"));
			WalletEntryActivtiy.this.finish();
			return;
		}

		Map<String, Object> extraMessage = new HashMap<String, Object>();
		extraMessage.put("mobile", mobile);
		extraMessage.put("cardNo", cardNo);
		SHRBWalletSDK.Users.addCard(openID, personUnionID, extraMessage, activity);
	}

	/**
	 * 开通钱包
	 * 
	 * @param intent
	 */
	private void bindCard(Intent intent) {
		// TODO Auto-generated method stub
		String realName = intent.getStringExtra("realName");// 真实姓名
		String identity = intent.getStringExtra("identity");// 身份证号
		String cardNo = intent.getStringExtra("cardNo");// 卡号
		String mobile = intent.getStringExtra("mobile");// 手机号
		String openID = intent.getStringExtra("openID");

		if (realName == null) {
			Log.e("+++++++++++++++++++++", "realName");
			callback.onFail(ResultGenerateUtil.generateReturnMap(
					ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),
					"参数有误:realName有误！"));
			WalletEntryActivtiy.this.finish();
			return;
		}
		if (identity == null) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(
					ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),
					"参数有误:identity有误！"));
			WalletEntryActivtiy.this.finish();
			return;
		}
		if (cardNo == null) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(
					ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),
					"参数有误:cardNumb有误！"));
			WalletEntryActivtiy.this.finish();
			return;
		}
		if (mobile == null) {
			callback.onFail(ResultGenerateUtil.generateReturnMap(
					ResultGenerateUtil.ReturnCode.PARAM_ERROR.toString(),
					"参数有误:phoneNumb有误！"));
			WalletEntryActivtiy.this.finish();
			return;
		}

		Map<String, Object> extraMessage = new HashMap<String, Object>();
		extraMessage.put("mobile", mobile);
		extraMessage.put("realName", realName);
		extraMessage.put("identity", identity.toUpperCase());
		extraMessage.put("cardNo", cardNo);

		SHRBWalletSDK.Users.bindCard(openID, extraMessage, activity);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(data==null){
			if(callback!=null){
				callback.onFail(ResultGenerateUtil.generateReturnMap(ResultGenerateUtil.ReturnCode.CANCEL.toString(), "用户取消"));
				WalletEntryActivtiy.this.finish();
			}else{
				WDLogUtil.e(TAG,"callback为空");
			}
		}else{
			HashMap map = (HashMap) data.getSerializableExtra("data");
		switch (requestCode) {
		case SHRBWalletSDK.METHOD_BIND_CARD://绑定卡
			WDLogUtil.e(TAG, "开户返回结果："+map.toString());
			if (resultCode == -1) {
				if (map != null) {
				WDLogUtil.e(TAG, map.toString());
				if(callback!=null){
					callback.onSucess(ResultGenerateUtil.generateHRMap(map));
				}else{
					WDLogUtil.e(TAG,"callback为空");
				}
				WalletEntryActivtiy.this.finish();
			}
				
			}
			break;
		case SHRBWalletSDK.METHOD_PAY://消费
			WDLogUtil.e(TAG, data);
			WDLogUtil.e(TAG, "消费返回结果："+map.toString());
			if (resultCode == -1) {
				if (map != null) {
				WDLogUtil.e(TAG, map.toString());
				if(callback!=null){
					callback.onSucess(ResultGenerateUtil.generateHRMap(map));
				}else{
					WDLogUtil.e(TAG,"callback为空");
				}
				WalletEntryActivtiy.this.finish();
			}
			}
			break;
		case SHRBWalletSDK.METHOD_WITHDRAW://提现
			WDLogUtil.e(TAG, "提现返回结果："+map.toString());
			if (resultCode == -1) {
				if (map != null) {
				WDLogUtil.e(TAG, map.toString());
				if(callback!=null){
					callback.onSucess(ResultGenerateUtil.generateHRMap(map));
				}else{
					WDLogUtil.e(TAG,"callback为空");
				}
				WalletEntryActivtiy.this.finish();
			}
			}
			break;
		case SHRBWalletSDK.METHOD_DEPOSITS://充值
			WDLogUtil.e(TAG, "充值返回结果："+map.toString());
			if (resultCode == -1) {
				if (map != null) {
				WDLogUtil.e(TAG, map.toString());
				if(callback!=null){
					callback.onSucess(ResultGenerateUtil.generateHRMap(map));
				}else{
					WDLogUtil.e(TAG,"callback为空");
				}
				WalletEntryActivtiy.this.finish();
			}
			}
			break;
		case SHRBWalletSDK.METHOD_AUTHENTICATE:
			
			break;
		case SHRBWalletSDK.METHOD_ADD_CARD://添加卡
			WDLogUtil.e(TAG, "添加卡返回结果："+map.toString());
			if (resultCode == -1) {
				if (map != null) {
				WDLogUtil.e(TAG, map.toString());
				if(callback!=null){
					callback.onSucess(ResultGenerateUtil.generateHRMap(map));
				}else{
					WDLogUtil.e(TAG,"callback为空");
				}
				WalletEntryActivtiy.this.finish();
			}
			}
			break;
		}
	}
		
		
		
	}
}

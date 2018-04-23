package com.wondersgroup.wallet;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.wondersgroup.PwServer.BackgroundInterface;
import com.wondersgroup.SHRB.SHRBManager;
import com.wondersgroup.entity.Account;
import com.wondersgroup.entity.Manager;
import com.wondersgroup.entity.Query;
import com.wondersgroup.entity.Transaction;
import com.wondersgroup.request.IRequest;
import com.wondersgroup.utils.ResultGenerateUtil;
import com.wondersgroup.utils.WDValidationUtil;

/**
 * 万达个人钱包SDK
 * 
 * @author liuzhipeng
 * 
 */
public class WDPwSDK {
	/**支付*/
	public static final String Query_Payment = "1";
	/**充值*/
	public static final String Query_Recharge = "2";
	/**提现*/
	public static final String Query_Extract = "3";
	/**消费*/
	public static final String Query_Spend = "4";
	/**退款*/
	public static final String Query_Refund = "5";

	private static WDPwSDK mInstance;

	public static synchronized WDPwSDK getInstance() {

		if (mInstance == null) {
			mInstance = new WDPwSDK();
		}
		return mInstance;
	}

	private Manager mManager;

	private synchronized Manager getManager() {

		if (mManager == null) {
			mManager = new Manager();
		}
		return mManager;
	}
	
//	public boolean getSDKState(){
//		return getManager().isInit();
//	}
	
	/**
	 * 初始化
	 * @param params
	 * @param callback
	 */
	public void initWalletSDK(Map<String, Object> params, WDResultCallback callback){
		getManager().initWalletSDK(params, callback);
	}
	/**
	 * 一键登录
	 * @param params
	 * @param callback
	 */
	public void loginWalletSDK(Map<String, Object> params, WDResultCallback callback){
		getManager().loginWalletSDK(params, callback);
	}

	private Transaction mTransaction;

	private synchronized Transaction getTransaction() {

		if (mTransaction == null) {
			mTransaction = new Transaction();
		}
		return mTransaction;
	}
	/**
	 * 充值
	 * @param params
	 * @param callback
	 */
	public void RechargeSDK(Map<String, Object> params, WDResultCallback callback){
		getTransaction().RechargeSDK(params, callback);
	}
	
	/**
	 * 消费
	 * @param params
	 * @param callback
	 */
	public void SpendSDK(Map<String, Object> params, WDResultCallback callback){
		getTransaction().SpendSDK(params, callback);
	}
	/**
	 * 提现
	 * @param params
	 * @param callback
	 */
	public void ExtractSDK(Map<String, Object> params, WDResultCallback callback){
		getTransaction().ExtractSDK(params, callback);
	}
//	/**
//	 * 充值
//	 * @param params
//	 * @param callback
//	 */
//	public void RechargeSDK(Map<String, Object> params, WDResultCallback callback){
//		getTransaction().RechargeSDK(params, callback);
//	}
	
//	/**
//	 * 消费
//	 * @param params
//	 * @param callback
//	 */
//	public void SpendSDK (Map<String, Object> params, WDResultCallback callback){
//		getTransaction().SpendSDK(params, callback);
//	}
	
//	/**
//	 * 提现
//	 * @param params
//	 * @param callback
//	 */
//	public void ExtractSDK(Map<String, Object> params, WDResultCallback callback){
//		getTransaction().ExtractSDK(params, callback);
//	}

	private Account mAccount;

	private synchronized Account getAccount() {
		if (mAccount == null) {
			mAccount = new Account();
		}
		return mAccount;
	}
	/**
	 * 开通钱包
	 * @param params
	 * @param callback
	 */
	public void BuildWalletSDK(Map<String, Object> params, WDResultCallback callback){
		getAccount().BuildWalletSDK(params, callback);
	}
	/**
	 * 添加卡
	 * @param params
	 * @param callback
	 */
	public void AddCardSDK(Map<String, Object> params, WDResultCallback callback){
		getAccount().AddCardSDK(params, callback);
	}
//	/**
//	 * 开通钱包
//	 * @param params
//	 * @param callback
//	 */
//	public void BuildWalletSDK (Map<String, Object> params, WDResultCallback callback){
//		getAccount().BuildWalletSDK(params, callback);
//	}
//	/**
//	 * 添加卡
//	 * @param params
//	 * @param callback
//	 */
//	public void AddCardSDK(Map<String, Object> params, WDResultCallback callback){
//		getAccount().AddCardSDK(params, callback);
//	}
	
	
	private Query mQuery;
	private synchronized Query getQuery() {
		if (mQuery == null) {
			mQuery = new Query();
		}
		return mQuery;
	}
	/**
	 * 查询账户余额
	 * @param params
	 * @param callback
	 */
	public void queryAccountBalance (Map<String, Object> params, WDResultCallback callback){
		getQuery().queryAccountBalance(params, callback);
	}
	/**
	 * 查询绑定卡列表
	 * @param params
	 * @param callback
	 */
	public void queryBindedCardList (Map<String, Object> params, WDResultCallback callback){
		getQuery().queryBindedCardList(params, callback);
	}
	/**
	 * 商户订单查询
	 * @param params
	 * @param callback
	 */
	public void queryOrderList (Map<String, Object> params, WDResultCallback callback){
		getQuery().queryOrderList(params, callback);
	}
	/**
	 * 查询用户交易明细
	 * @param params
	 * @param callback
	 */
	public void queryTransactionList (Map<String, Object> params, WDResultCallback callback){
		getQuery().queryTransactionList(params, callback);
	}
	/**
	 * 查询用户信息
	 * @param params
	 * @param callback
	 */
	public void queryuserinfo (Map<String, Object> params, WDResultCallback callback){
		getQuery().queryUserInfo(params, callback);
	}
	/**
	 * 查看账户明文
	 * @param params
	 * @param callback
	 */
	public void queryAccountMsg (Map<String, Object> params, WDResultCallback callback){
		getQuery().queryAccountMsg(params, callback);
	}
	

	private enum Channel {
		/**
		 * 华瑞银行
		 */
		SHRB;

		private static String getChannelName(Channel channel) {
			switch (channel) {
			case SHRB:
				return "华瑞银行";

			default:
				return "未知渠道";
			}
		}
	}

	private static Channel getChannel() {
		return Channel.SHRB;
	}

}

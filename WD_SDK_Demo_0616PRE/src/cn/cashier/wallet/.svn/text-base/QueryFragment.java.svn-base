package cn.cashier.wallet;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.cashier.R;
import cn.cashier.SDKDemoApplication;
import cn.cashier.SPUtils;
import cn.cashier.appnative.PayDemoActivity;

import com.wondersgroup.wallet.WDLogUtil;
import com.wondersgroup.wallet.WDPwSDK;
import com.wondersgroup.wallet.WDResultCallback;

/**
 * 查询界面
 * 
 * @author Luckydog
 *
 */
public class QueryFragment extends Fragment {
	final static String TAG = "QueryFragment";
	private RelativeLayout rl_recharge, rl_deposit, rl_consume, rl_add_card,rl_query_card,
	rl_query_balance,rl_query_account_msg,rl_query_shorderform,rl_query_transaction_detail,rl_query_user_info;
	private TextView tv_account, tv_userinfo, tv_log;
	private Map<String, Object> param;
	private Activity mContext;
	/**
	 * 充值
	 */
	public static final int FRAGMENT_RECHARGE = 1;
	/**
	 * 提现
	 */
	public static final int FRAGMENT_DEPOSIT = 2;
	/**
	 * 消费
	 */
	public static final int FRAGMENT_CONSUME = 3;
	/**
	 * 添加卡
	 */
	public static final int FRAGMENT_ADD_CARD = 4;
	/**
	 * 开户
	 */
	public static final int FRAGMENT_OPEN_ACCOUNT = 5;
	/**
	 * 开户成功
	 */
	private static final int FRAGMENT_OPEN_SUCCESS =6;
	private FragmentManager fm;
	private FragmentTransaction transaction;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_query, null);
		mContext = this.getActivity();
		initView(view);
		return view;
	}

	private Boolean isOpen;

	private void initView(View view) {
		String puID=SPUtils.getString(mContext, SPUtils.PERSONUNIONID, null);
		WDLogUtil.e("LoginActivity", "PERSONUNIONID:"+puID);
		if (puID!=null) {
			isOpen=true;
		}else{
			isOpen=false;
		}
		rl_recharge = (RelativeLayout) view.findViewById(R.id.rl_recharge);
		rl_deposit = (RelativeLayout) view.findViewById(R.id.rl_deposit);
		rl_consume = (RelativeLayout) view.findViewById(R.id.rl_consume);
		rl_add_card = (RelativeLayout) view.findViewById(R.id.rl_add_card);
		rl_query_card = (RelativeLayout) view.findViewById(R.id.rl_query_card);
		rl_query_balance = (RelativeLayout) view
				.findViewById(R.id.rl_query_balance);
		rl_query_shorderform = (RelativeLayout) view
				.findViewById(R.id.rl_query_shorderform);
		rl_query_transaction_detail = (RelativeLayout) view
				.findViewById(R.id.rl_query_transaction_detail);
		rl_query_user_info = (RelativeLayout) view
				.findViewById(R.id.rl_query_user_info);
		rl_query_account_msg = (RelativeLayout) view
				.findViewById(R.id.rl_query_account_msg);
		tv_account = (TextView) view.findViewById(R.id.tv_account);
//		tv_userinfo = (TextView) view.findViewById(R.id.tv_userinfo);
		tv_log = (TextView) view.findViewById(R.id.tv_log);
		rl_recharge.setOnClickListener(new btOnclick());
		rl_deposit.setOnClickListener(new btOnclick());
		rl_consume.setOnClickListener(new btOnclick());
		rl_add_card.setOnClickListener(new btOnclick());
		rl_query_card.setOnClickListener(new btOnclick());
		rl_query_balance.setOnClickListener(new btOnclick());
		rl_query_shorderform.setOnClickListener(new btOnclick());
		rl_query_transaction_detail.setOnClickListener(new btOnclick());
		rl_query_user_info.setOnClickListener(new btOnclick());
		rl_query_account_msg.setOnClickListener(new btOnclick());
		view.findViewById(R.id.btn_open).setOnClickListener(new btOnclick());
		if (isOpen) {
			// 已开户
			view.findViewById(R.id.rl_close).setVisibility(View.GONE);
			view.findViewById(R.id.ll_open).setVisibility(View.VISIBLE);
		} else {
			// 未开户
			view.findViewById(R.id.rl_close).setVisibility(View.VISIBLE);
			view.findViewById(R.id.ll_open).setVisibility(View.GONE);
		}
	}

	private class btOnclick implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.rl_recharge:
				// 充值
				setInfo("rl_recharge.click");
				selectFM(FRAGMENT_RECHARGE);
				break;
			case R.id.rl_deposit:
				// 提现
				setInfo("rl_deposit.click");
				selectFM(FRAGMENT_DEPOSIT);
				break;
			case R.id.rl_consume:
				// 消费
				setInfo("rl_consume.click");
				selectFM(FRAGMENT_CONSUME);
				break;
			case R.id.btn_open:
				// 开户
				setInfo("rl_open_account.click");
				selectFM(FRAGMENT_OPEN_ACCOUNT);
				break;
			case R.id.rl_query_balance:
				// 查询账户余额
				setInfo("rl_query_balance.click");
				queryBalance();
				break;
			case R.id.rl_query_shorderform:
				// 查询商户订单
				setInfo("rl_query_shorderform.click");
				queryshorderform();
				break;
			case R.id.rl_query_transaction_detail:
				// 查询用户交易明细
				setInfo("rl_query_transaction_detail.click");
				querytransactiondetail();
				break;
			case R.id.rl_query_user_info:
				// 查询用户信息
				setInfo("rl_query_user_info.click");
				queryuserinfo();
				break;
			case R.id.rl_query_account_msg:
				// 查询账户明文
				setInfo("rl_query_account_msg.click");
				queryaccountmsg();
				break;
			case R.id.rl_add_card:
				// 添加卡
				setInfo("rl_add_card.click");
				selectFM(FRAGMENT_ADD_CARD);
				break;
			case R.id.rl_query_card:
				// 查询绑定卡列表
				setInfo("rl_query_card.click");
				querycard();
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * 查询绑卡列表
	 */
	private void querycard() {
		// TODO Auto-generated method stub
		Activity activity = this.getActivity();
		if (WalletActivity.wdPwSdk == null) {
			WalletActivity.wdPwSdk = WDPwSDK.getInstance();
		}
		param = new HashMap<String, Object>();
		String openID = SPUtils.getString(this.getActivity(), SPUtils.OPENID,
				"");
		param.put("openId", openID);
		param.put("Context", activity);
		WalletActivity.wdPwSdk.queryBindedCardList(param, new WDResultCallback() {

			@Override
			public void onSucess(Map<String, Object> callbackMap) {
				String info = "queryBindedCardList - sendSuccessMsg :"
						+ callbackMap.toString();
				setInfo(info);
			}

			@Override
			public void onFail(Map<String, Object> callbackMap) {
				String info = "queryAccountBalance - sendFailMsg :"
						+ callbackMap.toString();
				setInfo(info);
			}
		});
	
	}
	
	/**
	 * 查询账户明文
	 */
	public void queryaccountmsg() {
		if (WalletActivity.wdPwSdk == null) {
			WalletActivity.wdPwSdk = WDPwSDK.getInstance();
		}
		param = new HashMap<String, Object>();
		String personUnionID = SPUtils.getString(this.getActivity(),
				SPUtils.PERSONUNIONID, "");
		String openID = SPUtils.getString(this.getActivity(), SPUtils.OPENID,
				"");
		Activity activity = this.getActivity();
		param.put("personUnionID", personUnionID);
		param.put("openID", openID);
		param.put("Activity", activity);
		WalletActivity.wdPwSdk.queryAccountMsg(param, new WDResultCallback() {
			
			@Override
			public void onSucess(Map<String, Object> callbackMap) {
				String info = "queryAccountMsg - sendSuccessMsg :"
						+ callbackMap.toString();
				tv_log.setText(info);
				setInfo(info);
			}
			
			@Override
			public void onFail(Map<String, Object> callbackMap) {
				String info = "queryAccountMsg - sendFailMsg :"
						+ callbackMap.toString();
				tv_log.setText(info);
				setInfo(info);
			}
		});
	}
	/**
	 * 查询商户订单
	 */
	public void queryshorderform() {
		if (WalletActivity.wdPwSdk == null) {
			WalletActivity.wdPwSdk = WDPwSDK.getInstance();
		}
		String merOrderNO=SDKDemoApplication.getInstance().getConsume_Number();
		if (merOrderNO.equals("0")) {
			Toast.makeText(getActivity(), "先消费再查询", 1).show();
		}else{
			Toast.makeText(getActivity(), "订单号："+merOrderNO, 1).show();
		}
		param = new HashMap<String, Object>();
		String merAppID = SPUtils.getString(this.getActivity(),
				SPUtils.MERAPPID, "");
		String merUserId = SPUtils.getString(this.getActivity(), SPUtils.MERUSERID,
				"");
		Activity activity = this.getActivity();
		
		param.put("merAppId", merAppID);
		param.put("merUserId", merUserId);
		param.put("merOrderNO", merOrderNO);
		param.put("Context", activity);
		WalletActivity.wdPwSdk.queryOrderList(param, new WDResultCallback() {

			@Override
			public void onSucess(Map<String, Object> callbackMap) {
				String info = "queryOrderList - sendSuccessMsg :"
						+ callbackMap.toString();
				setInfo(info);
			}

			@Override
			public void onFail(Map<String, Object> callbackMap) {
				String info = "queryOrderList - sendFailMsg :"
						+ callbackMap.toString();
				setInfo(info);
			}
		});
	}
	/**
	 * 查询用户交易明细
	 */
	public void querytransactiondetail() {
		if (WalletActivity.wdPwSdk == null) {
			WalletActivity.wdPwSdk = WDPwSDK.getInstance();
		}
		param = new HashMap<String, Object>();
		String merAppID = SPUtils.getString(this.getActivity(),
				SPUtils.MERAPPID, "");
		String merUserId = SPUtils.getString(this.getActivity(), SPUtils.MERUSERID,
				"");
		String openID = SPUtils.getString(this.getActivity(), SPUtils.OPENID,
				"");
		String startDate="20170404";
		String endDate="20400404";
		Activity activity = this.getActivity();
		param.put("merAppId", merAppID);
		param.put("merUserId", merUserId);
		param.put("openID", openID);
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		param.put("txnMode", WDPwSDK.Query_Spend);
		param.put("Context", activity);
		WalletActivity.wdPwSdk.queryTransactionList(param, new WDResultCallback() {

			@Override
			public void onSucess(Map<String, Object> callbackMap) {
				String info = "querytransactiondetail - sendSuccessMsg :"
						+ callbackMap.toString();
				setInfo(info);
			}

			@Override
			public void onFail(Map<String, Object> callbackMap) {
				String info = "querytransactiondetail - sendFailMsg :"
						+ callbackMap.toString();
				setInfo(info);
			}
		});
	
	}
	/**
	 * 查询用户信息
	 */
	public void queryuserinfo() {

		if (WalletActivity.wdPwSdk == null) {
			WalletActivity.wdPwSdk = WDPwSDK.getInstance();
		}
		param = new HashMap<String, Object>();
		String merAppID = SPUtils.getString(this.getActivity(),
				SPUtils.MERAPPID, "");
		String merUserId = SPUtils.getString(this.getActivity(), SPUtils.MERUSERID,
				"");
		String openID = SPUtils.getString(this.getActivity(), SPUtils.OPENID,
				"");
		Activity activity = this.getActivity();
		param.put("merAppId", merAppID);
		param.put("merUserId", merUserId);
		param.put("openID", openID);
		param.put("Context", activity);
		WalletActivity.wdPwSdk.queryuserinfo(param, new WDResultCallback() {

			@Override
			public void onSucess(Map<String, Object> callbackMap) {
				String info = "querytransactiondetail - sendSuccessMsg :"
						+ callbackMap.toString();
				setInfo(info);
			}

			@Override
			public void onFail(Map<String, Object> callbackMap) {
				String info = "querytransactiondetail - sendFailMsg :"
						+ callbackMap.toString();
				setInfo(info);
			}
		});
	
	
	}
	/**
	 * 跳转Fragment
	 * 
	 * @param i
	 */
	private void selectFM(int i) {
		fm = getFragmentManager();
		transaction = fm.beginTransaction();
		switch (i) {
		case FRAGMENT_RECHARGE:
			RechargeFragment fm_recharge = new RechargeFragment();
			transaction.hide(this);
			transaction.add(R.id.content, fm_recharge);
			transaction.addToBackStack(null);
			setInfo("进入RechargeFragment");
			break;
		case FRAGMENT_DEPOSIT:
			DepositFragment fm_deposit = new DepositFragment();
			transaction.hide(this);
			transaction.add(R.id.content, fm_deposit);
			transaction.addToBackStack(null);
			setInfo("进入DepositFragment");
			break;
		case FRAGMENT_CONSUME:
			startActivity(new Intent(mContext, PayDemoActivity.class));
			break;
		case FRAGMENT_ADD_CARD:
			AddCardFragment fm_add_card = new AddCardFragment();
			transaction.hide(this);
			transaction.add(R.id.content, fm_add_card);
			transaction.addToBackStack(null);
			setInfo("进入AddCardFragment");
			break;
		case FRAGMENT_OPEN_ACCOUNT:
			OpenAccountFragment fm_open_acc = new OpenAccountFragment();
			transaction.hide(this);
			transaction.add(R.id.content, fm_open_acc);
			transaction.addToBackStack(null);
			setInfo("进入OpenAccountFragment");
			break;
		case FRAGMENT_OPEN_SUCCESS:
			QueryFragment query_fragment = new QueryFragment();
			transaction.replace(R.id.content, query_fragment);
			transaction.addToBackStack(null);
			setInfo("QueryFragment重载");
			break;
		default:
			break;
		}
		transaction.commit();
	}

	/**
	 * 查询余额
	 */
	public void queryBalance() {
		if (WalletActivity.wdPwSdk == null) {
			WalletActivity.wdPwSdk = WDPwSDK.getInstance();
		}
		param = new HashMap<String, Object>();
		String merAppID = SPUtils.getString(this.getActivity(),
				SPUtils.MERAPPID, "");
		String openID = SPUtils.getString(this.getActivity(), SPUtils.OPENID,
				"");
		Activity activity = this.getActivity();
		param.put("merAppId", merAppID);
		param.put("openId", openID);
		param.put("Context", activity);
		WalletActivity.wdPwSdk.queryAccountBalance(param, new WDResultCallback() {

			@Override
			public void onSucess(Map<String, Object> callbackMap) {
				String info = "queryAccountBalance - sendSuccessMsg :"
						+ callbackMap.toString();
				String balance=(String) callbackMap.get("balanceAmt");
				if (balance!=null) {
					tv_account.setText(balance);
				}
				setInfo(info);
			}

			@Override
			public void onFail(Map<String, Object> callbackMap) {
				String info = "queryAccountBalance - sendFailMsg :"
						+ callbackMap.toString();
				setInfo(info);
			}
		});
	}

	//将获取信息以逗号切割显示在输入框中
	public static void splitString(String str, TextView tv_log2) {
		String[] arr = str.split("\\,");
		String info = "";
		for (int i = 0; i < arr.length; i++) {
			if (i == 0) {
				info = arr[0];
			} else {
				info += "," + "\n" + arr[i];
			}
		}
		tv_log2.setText(info);
	}
	protected void setInfo(final String info2) {
		// TODO Auto-generated method stub
		Log.e(TAG, info2);
		tv_log.setText(info2);
	}
}

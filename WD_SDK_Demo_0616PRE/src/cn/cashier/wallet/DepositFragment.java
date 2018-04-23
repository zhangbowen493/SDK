package cn.cashier.wallet;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import cn.cashier.R;
import cn.cashier.SPUtils;

import com.wondersgroup.wallet.WDPwSDK;
import com.wondersgroup.wallet.WDResultCallback;
/**
 * 提现界面
 * @author Luckydog
 *
 */
public class DepositFragment extends Fragment{
	final static String TAG = "DepositFragment";
	private TextView tv_balance,tv_balance_available,tv_bank,tv_log;
	private EditText et_money;
	private String money;
	private Map<String, Object> param;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_deposit, null);
		initView(view);
		return view;
	}
	private void initView(View view) {
		tv_balance=(TextView) view.findViewById(R.id.tv_account);
//		tv_balance_available=(TextView) view.findViewById(R.id.tv_balance);
//		tv_bank=(TextView) view.findViewById(R.id.tv_bank);
		tv_log=(TextView) view.findViewById(R.id.tv_log);
		et_money=(EditText) view.findViewById(R.id.et_money);
		view.findViewById(R.id.bt_deposit).setOnClickListener(new btOnclick());
	}

	private class btOnclick implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.bt_deposit:
				money=et_money.getText().toString();
				setInfo("bt_deposit");
				deposit();
				break;
			default:
				break;
			}
		}
	}
	/**
	 * 提现
	 */
	public void deposit() {
		if (WalletActivity.wdPwSdk==null) {
			WalletActivity.wdPwSdk=WDPwSDK.getInstance();
		}
		param = new HashMap<String, Object>();
		Activity activity = this.getActivity();
		param.put("merAppId",SPUtils.getString(this.getActivity(),
				SPUtils.MERAPPID, ""));
		param.put("merUserId",SPUtils.getString(this.getActivity(),
				SPUtils.MERUSERID, ""));
		param.put("tranAmt",money);
		param.put("openID",SPUtils.getString(activity,SPUtils.OPENID, ""));
		param.put("personUnionID",SPUtils.getString(activity,SPUtils.PERSONUNIONID, ""));
		param.put("Activity",activity);
		WalletActivity.wdPwSdk.ExtractSDK(param, new WDResultCallback() {

			@Override
			public void onSucess(Map<String, Object> callbackMap) {
				String info = "RechargeSDK - sendSuccessMsg :"+callbackMap.toString();
				setInfo(info);
			}

			@Override
			public void onFail(Map<String, Object> callbackMap) {
				String info = "RechargeSDK - sendFailMsg :"+callbackMap.toString();
				setInfo(info);
			}
		});
	}
	/**
	 * 打印信息
	 */
	protected void setInfo(final String info2) {
		Log.e(TAG, info2);
		tv_log.setText(info2);
	}
}

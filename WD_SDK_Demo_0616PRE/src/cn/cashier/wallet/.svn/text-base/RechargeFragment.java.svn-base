package cn.cashier.wallet;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import cn.cashier.R;
import cn.cashier.SPUtils;

import com.wondersgroup.wallet.WDPwSDK;
import com.wondersgroup.wallet.WDResultCallback;
/**
 * 充值界面
 * @author Luckydog
 *
 */
public class RechargeFragment extends Fragment{
	final static String TAG = "RechargeFragment";
	private TextView tv_log;
	private Map<String, Object> param;
	private EditText et_money;
	private String money,order_id;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_recharge, null);
		initView(view);
		return view;
	}
	private void initView(View view) {
		et_money=(EditText) view.findViewById(R.id.et_money);
		tv_log=(TextView) view.findViewById(R.id.tv_log);
		view.findViewById(R.id.tv_deposits).setOnClickListener(new btOnclick());;
	}
	private class btOnclick implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_deposits:
				money=et_money.getText().toString();
				recharge();
				break;
			default:
				break;
			}
		}
	}
	public void recharge() {
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
		WalletActivity.wdPwSdk.RechargeSDK(param, new WDResultCallback() {

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


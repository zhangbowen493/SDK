package cn.cashier.wallet;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.cashier.R;
import cn.cashier.SPUtils;

import com.wondersgroup.wallet.WDLogUtil;
import com.wondersgroup.wallet.WDPwSDK;
import com.wondersgroup.wallet.WDResultCallback;
/**
 * 开户界面
 * @author Luckydog
 *
 */
public class OpenAccountFragment extends Fragment{
	final static String TAG = "OpenAccountFragment";
	private String IDCard,name,phone_number,bankcard_numb;
	private EditText et_IDCard,et_name,et_phone_number,et_bankcard_numb;
	private TextView tv_log;
	private Activity mContext;
	private Map<String, Object> param;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_open_account, null);
		mContext=this.getActivity();
		initView(view);
		return view;
	}
	private void initView(View view) {
		et_IDCard=(EditText) view.findViewById(R.id.et_IDCard);
		et_name=(EditText) view.findViewById(R.id.et_name);
		et_phone_number=(EditText) view.findViewById(R.id.et_phone_number);
		et_bankcard_numb=(EditText) view.findViewById(R.id.et_bankcard_numb);
		tv_log=(TextView) view.findViewById(R.id.tv_log);
		view.findViewById(R.id.bt_open_account).setOnClickListener(new btOnclick());
	}
	private class btOnclick implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.bt_open_account:
				IDCard=et_IDCard.getText().toString();
				name=et_name.getText().toString();
				phone_number=et_phone_number.getText().toString();
				bankcard_numb=et_bankcard_numb.getText().toString();
				if (IDCard!=null&&name!=null&&phone_number!=null&&bankcard_numb!=null) {
					openAccount();
				}else{
					Toast.makeText(mContext, "参数不能为空", Toast.LENGTH_LONG).show();
				}
				break;
			default:
				break;
			}
		}
	}
	/**
	 * 开通钱包
	 */
	public void openAccount() {
		if (WalletActivity.wdPwSdk==null) {
			WalletActivity.wdPwSdk=WDPwSDK.getInstance();
		}
		WDLogUtil.i(TAG, "openID="+SPUtils.getString(mContext, SPUtils.OPENID, null)
				+" sequence="+SPUtils.getString(mContext, SPUtils.SEQUCENCE, null)
				+" merAppId="+SPUtils.getString(mContext, SPUtils.MERAPPID, null)
				+" merUserId="+SPUtils.getString(mContext, SPUtils.MERUSERID, null)
				);
		
		param = new HashMap<String, Object>();
		Activity activity = this.getActivity();
		param.put("realName",name);// 真实姓名
		param.put("identity", IDCard);// 身份证号
		param.put("cardNo", bankcard_numb);// 卡号
		param.put("mobile", phone_number);// 手机号
		param.put("Activity", activity);
		param.put("openID", SPUtils.getString(mContext, SPUtils.OPENID, null));
		param.put("sequence", SPUtils.getString(mContext, SPUtils.SEQUCENCE, null));
		param.put("merAppId", SPUtils.getString(mContext, SPUtils.MERAPPID, null));
		param.put("merUserId", SPUtils.getString(mContext, SPUtils.MERUSERID, null));
		WalletActivity.wdPwSdk.BuildWalletSDK(param, new WDResultCallback() {
			@Override
			public void onSucess(Map<String, Object> callbackMap) {
				// TODO Auto-generated method stub
				String info = "BuildWalletSDK - sendSuccessMsg :"+callbackMap.toString();
				setInfo(info);
				SPUtils.put(getActivity(), SPUtils.PERSONUNIONID, (String) callbackMap.get("personUnionID"));
				reFreshFragment();
			}

			@Override
			public void onFail(Map<String, Object> callbackMap) {
				// TODO Auto-generated method stub
				String info = "BuildWalletSDK - sendFailMsg :"+callbackMap.toString();
				setInfo(info);
			}
		});
	}
	protected void reFreshFragment() {
		FragmentManager fm = getFragmentManager();;
		FragmentTransaction transaction=fm.beginTransaction();
		QueryFragment fm_query = new QueryFragment();
        transaction.hide(this);
        transaction.replace(R.id.content, fm_query);
        transaction.commitAllowingStateLoss();
        setInfo("刷新QueryFragment");
	}
	/**
	 * 打印信息
	 */
	protected void setInfo(final String info2) {
		Log.e(TAG, info2);
		tv_log.setText(info2);
	}
	
}

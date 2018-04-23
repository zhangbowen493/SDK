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
import android.widget.Toast;

import cn.cashier.R;
import cn.cashier.SPUtils;

import com.wondersgroup.wallet.WDPwSDK;
import com.wondersgroup.wallet.WDResultCallback;
/**
 * 添加银行卡界面
 * @author Luckydog
 *
 */
public class AddCardFragment extends Fragment{
	final static String TAG = "AddCardFragment";
	private String phone_number,bankcard_numb;
	private EditText et_phone_number,et_bankcard_numb;
	private Activity mContext;
	private Map<String, Object> param;
	private TextView tv_log;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = this.getActivity();
		View view = inflater.inflate(R.layout.fragment_add_card, null);
		initView(view);
		return view;
	}
	private void initView(View view) {
		tv_log=(TextView) view.findViewById(R.id.tv_log);
		et_phone_number=(EditText) view.findViewById(R.id.et_phone_number);
		et_bankcard_numb=(EditText) view.findViewById(R.id.et_bankcard_numb);
		tv_log=(TextView) view.findViewById(R.id.tv_log);
		view.findViewById(R.id.bt_add_card).setOnClickListener(new btOnclick());
	}
	private class btOnclick implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.bt_add_card:
				phone_number=et_phone_number.getText().toString();
				bankcard_numb=et_bankcard_numb.getText().toString();
//				if (IDCard!=null&&name!=null&&phone_number!=null&&bankcard_numb!=null) {
					addCard();
//				}else{
//					Toast.makeText(mContext, "参数不能为空", Toast.LENGTH_LONG).show();
//				}
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * 添加银行卡
	 */
	public void addCard() {
		if (WalletActivity.wdPwSdk==null) {
			WalletActivity.wdPwSdk=WDPwSDK.getInstance();
		}
		param = new HashMap<String, Object>();
		Activity activity = this.getActivity();
		param.put("openID", SPUtils.getString(mContext, SPUtils.OPENID, ""));
		param.put("personUnionID",SPUtils.getString(activity,SPUtils.PERSONUNIONID, ""));
		param.put("cardNo", bankcard_numb);// 卡号
		param.put("mobile", phone_number);// 手机号
		param.put("Activity", activity);
		WalletActivity.wdPwSdk.AddCardSDK(param, new WDResultCallback() {
			
			@Override
			public void onSucess(Map<String, Object> callbackMap) {
				// TODO Auto-generated method stub
				String info = "AddCardSDK - sendSuccessMsg :"+callbackMap.toString();
				setInfo(info);
			}
			
			@Override
			public void onFail(Map<String, Object> callbackMap) {
				// TODO Auto-generated method stub
				String info = "AddCardSDK - sendFailMsg :"+callbackMap.toString();
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

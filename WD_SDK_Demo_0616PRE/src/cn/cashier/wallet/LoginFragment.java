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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.cashier.R;
import cn.cashier.SPUtils;

import com.wondersgroup.wallet.WDGlobalSetting;
import com.wondersgroup.wallet.WDLogUtil;
import com.wondersgroup.wallet.WDPwSDK;
import com.wondersgroup.wallet.WDResultCallback;
/**
 * 登录界面（初始化+一键登录）
 * @author Luckydog
 *
 */
public class LoginFragment extends Fragment{

	final static String TAG = "LoginActivity";
	private EditText et_serial,et_secret_key,et_userID;
	private String serial,secret_key,userID,merName,sequcence,openID,personUnionID,unionID;
	private Activity mContext;
	private Map<String, Object> param;
	private FragmentManager fm;
	private FragmentTransaction transaction;
	/**
	 * 结果代码
	 */
	private String returnCode;
	/**
	 * 结果信息
	 */
	private String returnMsg;
	/**
	 * 标识id
	 */
	private String correspondingId;
	private View view;
	private TextView tv_log;
	private EditText et_merName;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_login, null);
		mContext=this.getActivity();
		initView(view);
		WDGlobalSetting.isDebug = true;
		return view;
	}
	private void initView(View view) {
		et_serial=(EditText) view.findViewById(R.id.et_serial);
		et_secret_key=(EditText)  view.findViewById(R.id.et_secret_key);
		et_userID=(EditText) view. findViewById(R.id.et_userID);
		tv_log=(TextView) view.findViewById(R.id.tv_log);
		et_merName=(EditText) view.findViewById(R.id.et_merapp_name);
		view.findViewById(R.id.btn_login).setOnClickListener(new btOnclick());
		view.findViewById(R.id.btn_init).setOnClickListener(new btOnclick());
		view.findViewById(R.id.button1).setOnClickListener(new btOnclick());
		
		et_serial.setText(SPUtils.getString(mContext, SPUtils.MERAPPID, "wonders"));
		et_secret_key.setText(SPUtils.getString(mContext, SPUtils.MERAPPKEY, "wonderssecret"));
		et_merName.setText(SPUtils.getString(mContext, SPUtils.MERAPPNAME, "万达"));
		
//		if(WDPwSDK.getInstance().getSDKState()){
//			view.findViewById(R.id.btn_login).setEnabled(true);
//		}
		
	}

	private class btOnclick implements OnClickListener {

		@Override
		public void onClick(View v) {
			serial=et_serial.getText().toString();
			secret_key=et_secret_key.getText().toString();
			userID=et_userID.getText().toString();
			merName = et_merName.getText().toString();
			switch (v.getId()) {
			case R.id.btn_init:
				if (serial!=null&&secret_key!=null) {
					initialize();
				}else{
					Toast.makeText(mContext, "参数不能为空", Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.btn_login:
				if (userID!=null) {
					login(userID);
				}else{
					Toast.makeText(mContext, "参数不能为空", Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.button1:
				saveData();
				selectFM(1);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 个人钱包SDK初始化
	 */
	private void initialize() {
		if (WalletActivity.wdPwSdk==null) {
			WalletActivity.wdPwSdk=WDPwSDK.getInstance();
		}
//		view.findViewById(R.id.btn_init).setEnabled(false);
//		 int wayid = (Integer) SPUtils.get(getActivity(), SPUtils.WAYTAG, 1);
		SPUtils.clear(this.getActivity());
//		SPUtils.put(getActivity(), SPUtils.WAYTAG, wayid);
		param = new HashMap<String, Object>();
		Activity activity = this.getActivity();
		param.put("Context", activity);
		param.put("merAppId", serial);
		param.put("merAppKey", secret_key);
		WalletActivity.wdPwSdk.initWalletSDK(param, new WDResultCallback() {

			@Override
			public void onSucess(Map<String, Object> callbackMap) {
				if (callbackMap==null) {
					setInfo("callbackMap为空");
				}
				//初始化成功，设置登录按键为可使用
				view.findViewById(R.id.btn_login).setEnabled(true);
				String info = "initWalletSDK - sendSuccessMsg :"+callbackMap.toString();
				setInfo(info);
			}

			@Override
			public void onFail(Map<String, Object> callbackMap) {
				//结果代码 String 正常为全0，出错时为错误码
				returnCode=(String) callbackMap.get("returnCode");
//				//结果信息 String 正常为空字符串，出错时为错误信息 
				returnMsg=(String) callbackMap.get("returnMsg");
//				//错误代码 String 正常为全0，出错时为错误码
				String errorCode= (String) callbackMap.get("errorCode");
//				//错误信息 String 正常为空字符串，出错时为错误信息
				correspondingId=(String) callbackMap.get("errorMsg");
				String info = "initWalletSDK - sendFailMsg:"+callbackMap.toString();
				setInfo(info);
			}
		});
	}

	/**
	 * 数据保存
	 */
	public void saveData() {
		// TODO Auto-generated method stub
		WDLogUtil.e(TAG, "MERAPPID="+serial
				+" MERAPPKEY="+secret_key
				+" MERAPPNAME="+merName
				+" MERUSERID="+userID
				+" OPENID="+openID
				+" PERSONUNIONID="+personUnionID
				+" SEQUCENCE="+sequcence
				+" UNIONID="+unionID
				);
		SPUtils.put(this.getActivity(), SPUtils.MERAPPID, serial);
		SPUtils.put(this.getActivity(), SPUtils.MERAPPKEY, secret_key);
		SPUtils.put(this.getActivity(), SPUtils.MERAPPNAME, merName);
		SPUtils.put(this.getActivity(), SPUtils.MERUSERID, userID);
		SPUtils.put(this.getActivity(), SPUtils.OPENID, openID);
		SPUtils.put(this.getActivity(), SPUtils.PERSONUNIONID, personUnionID);
		SPUtils.put(this.getActivity(), SPUtils.SEQUCENCE, sequcence);
		SPUtils.put(this.getActivity(), SPUtils.UNIONID, unionID);
	}
	/**
	 * 实现一键登录个人钱包账户
	 */
	private void login(String userID) {
		Activity activity = this.getActivity();
		param = new HashMap<String, Object>();
		param.put("Context", activity);
		param.put("merAppId", serial);
		param.put("merUserId", userID);
		if (WalletActivity.wdPwSdk==null) {
			WalletActivity.wdPwSdk=WDPwSDK.getInstance();
		}
		WalletActivity.wdPwSdk.loginWalletSDK(param, new WDResultCallback() {
			
			@Override
			public void onSucess(Map<String, Object> callbackMap) {
				String info = "loginWellatSDK - sendSuccessMsg:"+callbackMap.toString();;
				setInfo(info);
				
				sequcence = (String) callbackMap.get("sequence");
				openID = (String) callbackMap.get("openID");
				personUnionID = (String) callbackMap.get("personUnionID");
				unionID = (String) callbackMap.get("unionID");
				saveData();
				selectFM(1);
			}
			
			@Override
			public void onFail(Map<String, Object> callbackMap) {
				String info = "loginWellatSDK - sendFailMsg:"+callbackMap.toString();;
				setInfo(info);
			}
		});
	}
	
	private void selectFM(int i) {
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        switch (i) {
        case 1:
            QueryFragment fm_query = new QueryFragment();
            transaction.hide(this);
            transaction.add(R.id.content, fm_query);
            transaction.addToBackStack(null);
            setInfo("进入QueryFragment");
            break;
        default:
            break;
        }
        transaction.commit();
    }
	/**
	 * 打印信息
	 */
	protected void setInfo(final String info2) {
		Log.e(TAG, info2);
		tv_log.setText(info2);
	}

}


package cn.cashier.wallet;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;
import cn.cashier.MainActivity;
import cn.cashier.R;
import cn.cashier.SDKDemoApplication;
import cn.cashier.SPUtils;
import cn.wd.checkout.api.CheckOut;

import com.wondersgroup.wallet.WDGlobalSetting;
import com.wondersgroup.wallet.WDPwSDK;

public class WalletActivity extends Activity {

	final static String TAG = "MainActivity";
	private Activity mContext;
	/**
	 * 一键登录Fragment
	 */
	private LoginFragment loginFragment;
	/** 
	 * 用于对Fragment进行管理 
	 */  
	private FragmentManager fragmentManager; 
	/**
	 * SDK
	 */
	public static WDPwSDK wdPwSdk;
	
	// 访问环境标识  1:联调测试环境   2：正式环境 0:开发测试环境
		private int wayid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mContext = this;
		initState();
		initView();
	}

	private void initView() {
		wdPwSdk=WDPwSDK.getInstance();
		// 开启一个Fragment事务  
		fragmentManager = getFragmentManager();  
		FragmentTransaction transaction = fragmentManager.beginTransaction();  
		if (loginFragment == null) {  
			// 如果MessageFragment为空，则创建一个并添加到界面上  
			loginFragment = new LoginFragment();  
			transaction.add(R.id.content, loginFragment);  
		} else {  
			// 如果MessageFragment不为空，则直接将它显示出来  
			transaction.show(loginFragment);  
		}  
		transaction.commit();  
//		 wayid = (Integer) SPUtils.get(getApplicationContext(), SPUtils.WAYTAG, 1);
		 wayid = SDKDemoApplication.Networke_way ;
		 /**
         * 设置访问网络环境  CT 为联调测试环境 不调用此方法为生产环境
         */
        if(wayid==1){
        	Toast.makeText(getApplicationContext(), "联调测试环境", Toast.LENGTH_SHORT).show();
        	WDGlobalSetting.Environment = WDGlobalSetting.Environment_T;
        }else if(wayid==2){
        	Toast.makeText(getApplicationContext(), "正式环境", Toast.LENGTH_SHORT).show();
        	WDGlobalSetting.Environment = WDGlobalSetting.Environment_P;
        }else{
        	Toast.makeText(getApplicationContext(), "开发测试环境", Toast.LENGTH_SHORT).show();
        	WDGlobalSetting.Environment = WDGlobalSetting.Environment_D;
        }
        WDGlobalSetting.isDebug= true;
        
        
	}
	/**
	 * 沉浸式状态栏
	 */
	private void initState() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void setInfo(final String info2) {
		Log.e(TAG, info2);
	}

}

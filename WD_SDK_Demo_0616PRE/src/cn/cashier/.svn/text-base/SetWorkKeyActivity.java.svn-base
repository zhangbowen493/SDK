package cn.cashier;

import cn.cashier.R;
import cn.cashier.appnative.PayDemoActivity;
import cn.cashier.web.JSPayActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
	/*
 *@作者: Administrator
 *@版本: 
 *@version create time：2016-4-25 下午2:05:31
 */
public class SetWorkKeyActivity extends Activity {

	private EditText edtAppid;
	private EditText edtAppSecret;
	private Button btnSet;
	private EditText edtAppSubmerno;
	private EditText edtAppSubmerName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_work_key);
		
		initVeiw();
	}

	private void initVeiw() {
		edtAppid = (EditText) findViewById(R.id.edt_set_key_appid);
		edtAppSecret = (EditText) findViewById(R.id.edt_set_key_app_secret);
		edtAppSubmerno = (EditText) findViewById(R.id.edt_set_key_app_submerno);
		edtAppSubmerName = (EditText) findViewById(R.id.edt_set_key_app_submer_name);
		
		edtAppid.setText("wd2015tst001");
		edtAppSecret.setText("6XtC7H8NuykaRv423hrf1gGS09FEZQoB");
		edtAppSubmerno.setText("wdtstsub00001");
		edtAppSubmerName.setText("万达信息");
		
		btnSet = (Button) findViewById(R.id.btn_set_key_set);
		btnSet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String strAppid = edtAppid.getText().toString().trim();
				String strAppsecret = edtAppSecret.getText().toString().trim();
				String strAppSubmernot = edtAppSubmerno.getText().toString().trim();
				String strAppSubmerName = edtAppSubmerName.getText().toString().trim();
				
				if(TextUtils.isEmpty(strAppSubmerName)||TextUtils.isEmpty(strAppid)||TextUtils.isEmpty(strAppsecret)||strAppsecret.length()!=32||TextUtils.isEmpty(strAppSubmernot)){
					Toast.makeText(getApplicationContext(), "请设置参数", Toast.LENGTH_LONG).show();
					return;
				}
				
				SPUtils.put(SetWorkKeyActivity.this, SPUtils.MERAPPID, strAppid);
				SPUtils.put(SetWorkKeyActivity.this, SPUtils.MERAPPKEY, strAppsecret);
				SPUtils.put(SetWorkKeyActivity.this, SPUtils.MERSUBMERNO, strAppSubmernot);
				SPUtils.put(SetWorkKeyActivity.this, SPUtils.MERAPPNAME, strAppSubmerName);
					
				Intent intent = new Intent(getApplicationContext(), PayFunctionActivity.class);
				startActivity(intent);
				
			}
		});
		
		RadioGroup rg = (RadioGroup) findViewById(R.id.rg_main_huanjing);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				int buttonId = arg0.getCheckedRadioButtonId();
				
				RadioButton rb = (RadioButton) SetWorkKeyActivity.this.findViewById(buttonId);
				
				switch (buttonId) {
				case R.id.radioButton1:
					//联调环境
//					SPUtils.put(SetWorkKeyActivity.this, SPUtils.WAYTAG, 1);
					SDKDemoApplication.Networke_way = 1;
					break;
				case R.id.radioButton2:
					//正式环境
//					SPUtils.put(SetWorkKeyActivity.this, SPUtils.WAYTAG, 2);
					SDKDemoApplication.Networke_way = 2;
					break;
				case R.id.radioButton3:
					//开发测试环境
//					SPUtils.put(SetWorkKeyActivity.this, SPUtils.WAYTAG, 0);
					SDKDemoApplication.Networke_way = 0;
					break;
				default:
					break;
				}
				
			}
		});
//		SPUtils.put(SetWorkKeyActivity.this, SPUtils.WAYTAG, 1);
	}

}



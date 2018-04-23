package cn.cashier;

import cn.cashier.wallet.WalletActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
	/*
 *@作者: kevin
 *@版本: 
 *@version create time：2017-5-10 下午2:33:35
 */
public class PayFunctionActivity extends Activity implements OnClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_function);
		
		findViewById(R.id.button1).setOnClickListener(this); 
		findViewById(R.id.button2).setOnClickListener(this); 
		
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (v.getId()) {
		case R.id.button1:
			intent= new Intent(PayFunctionActivity.this, MainActivity.class);
			break;
		case R.id.button2:
			intent= new Intent(PayFunctionActivity.this, WalletActivity.class);
			break;

		default:
			break;
		}
		
		if(null!=intent){
			startActivity(intent);
			intent = null;
		}
	}

}



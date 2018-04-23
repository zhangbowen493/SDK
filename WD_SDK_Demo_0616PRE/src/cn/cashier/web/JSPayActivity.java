package cn.cashier.web;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;
import cn.cashier.R;
import cn.wd.checkout.api.CheckOut;
import cn.wd.checkout.api.WDJavaScriptObject;
import cn.wd.checkout.api.WDPay;
import cn.wd.checkout.api.WDPayResult;

/*
 *@作者: Administrator
 *@版本: 
 *@version create time：2016-8-26 下午4:07:36
 */
public class JSPayActivity extends Activity {
	

	private Button mButton;
	private WebView mWebView;

	// appid
	private String strAppid;
	// app secret 工作密钥
	private String strAppsecret;
	//子商户号
	private String submerno;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_js_pay);
		init();
		
		
	}


	@SuppressLint("JavascriptInterface")
	private void init() {
		// TODO Auto-generated method stub
		mButton = (Button) findViewById(R.id.button);

		mWebView = (WebView) findViewById(R.id.webview);

		WebSettings mWebSettings = mWebView.getSettings();
		// 启用JavaScript
		mWebSettings.setJavaScriptEnabled(true);
		// 设置编码格式
		mWebSettings.setDefaultTextEncodingName("utf-8");
		// 设置本地调用对象及其接口
		mWebView.addJavascriptInterface(new JavaScriptObject(JSPayActivity.this), "blin");
		// 调用本地html文件
		mWebView.loadUrl("file:///android_asset/JSCallSDK.html");

		mButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mWebView.loadUrl("javascript:android_callJavaScript('这是点击安卓端的Button后传过来的')");
				// mWebView.loadUrl("javascript:callJavaScript('这是点击安卓端的Button后传过来的')");
				
			}
			
		});
		
		CheckOut.setIsPrint(true);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mWebView.destroy();
	}

	/**
	 * 继承SDK内部类实现回调方法
	 * @author Administrator
	 *
	 */
	class JavaScriptObject extends WDJavaScriptObject{

		public JavaScriptObject(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		public void callback(final WDPayResult bcPayResult) {
			// TODO Auto-generated method stub
			
			JSPayActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					String result = bcPayResult.getResult();
					Log.i("demo", "done   result=" + result);
					String resultMsg = "";
					 if (result.equals(WDPayResult.RESULT_SUCCESS))
		                 resultMsg = "用户支付成功";
		             else if (result.equals(WDPayResult.RESULT_CANCEL))
		                 resultMsg = "用户取消支付";
		             else if(result.equals(WDPayResult.RESULT_FAIL)) {
		             	String info = "支付失败, 原因: " + bcPayResult.getErrMsg()
		                         + ", " + bcPayResult.getDetailInfo();
		                 resultMsg = info;
		             } else if(result.equals(WDPayResult.FAIL_UNKNOWN_WAY)){
		             	resultMsg = "未知支付渠道";
		             } else if(result.equals(WDPayResult.FAIL_WEIXIN_VERSION_ERROR)){
		             	resultMsg = "针对微信 支付版本错误（版本不支持）";
		             } else if(result.equals(WDPayResult.FAIL_EXCEPTION)){
		             	resultMsg = "支付过程中的Exception";
		             } else if(result.equals(WDPayResult.FAIL_ERR_FROM_CHANNEL)){
		             	resultMsg = "从第三方app支付渠道返回的错误信息，原因: " + bcPayResult.getErrMsg();
		             } else if(result.equals(WDPayResult.FAIL_INVALID_PARAMS)){
		             	resultMsg = "参数不合法造成的支付失败";
		             }else if(result.equals(WDPayResult.RESULT_PAYING_UNCONFIRMED)){
		             	resultMsg = "表示支付中，未获取确认信息";
		             } else{
		                 resultMsg = "invalid return";
		             }
					
					
					//原生代码反馈信息到html页面
					mWebView.loadUrl("javascript:android_callJavaScript('"+resultMsg+"')");
					
				}
			});
		}

		
	}

}

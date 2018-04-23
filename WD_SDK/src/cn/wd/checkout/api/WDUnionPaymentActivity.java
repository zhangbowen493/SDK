/**
 * BCUnionPaymentActivity.java
 *
 * Created by xuanzhui on 2015/7/27.
 * Copyright (c) 2015 BeeCloud. All rights reserved.
 */
package cn.wd.checkout.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


import cn.wd.checkout.processor.WDLogUtil;

import com.unionpay.UPPayAssistEx;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;


/**
 * 用于银联支付
 */
public class WDUnionPaymentActivity extends Activity {

    private static Integer targetVersion = 53;
   
	private static final String TAG = "BCUnionPaymentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume(){
        super.onResume();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String tn= extras.getString("tn");
            int retPay;

            int curVer = getUNAPKVersion();
            if (curVer == -1)
                retPay = -1;
            else if (curVer < targetVersion)
                retPay = 2;
            else
                retPay = UPPayAssistEx.startPay(this, null, null, tn, "01");


            //插件问题 -1表示没有安装插件，2表示插件需要升级
            if (retPay==-1 || retPay==2) {

                WDPay instance = WDPay.getInstance(WDUnionPaymentActivity.this);

                if (instance != null && instance.payCallback != null) {
                	
                	if(-1==retPay){
                		installApk();
                	}else{
                		instance.payCallback.done(new WDPayResult(WDPayResult.RESULT_FAIL,
                                WDPayResult.FAIL_PLUGIN_NEED_UPGRADE,
                                "银联插件问题, 需重新安装或升级"));
                		this.finish();
                	}
                } else {
                    WDLogUtil.e("BCUnionPaymentActivity", "BCPay instance or payCallback NPE");
                }
                if(instance != null && instance.payHandler != null){
                	
                	if(-1==retPay){
                		installApk();
                	}else{
                		Message msg = instance.payHandler.obtainMessage();
             			msg.what= WDPayResult.RESULT_FAIL_HANDLER;
             			msg.obj = WDPayResult.FAIL_PLUGIN_NEED_UPGRADE 
             					+" /银联插件问题, 需重新安装或升级";
             			instance.payHandler.sendMessage(msg);
             			this.finish();
                	}
                }

            }
        }
    }
    /**
     * 安装apk
     */
    private void installApk() {
		// TODO Auto-generated method stub
    	 if(copyApkFromAssets(this, "unionpay.apk", Environment.getExternalStorageDirectory().getAbsolutePath()+"/unionpay.apk")){
    	      Builder m = new AlertDialog.Builder(this)
//    	        .setIcon(R.drawable.ic_launcher)
    	        .setMessage("银联在线支付服务尚未安装，是否现在安装？")
//    	        .setIcon(R.drawable.ic_launcher)
    	        .setPositiveButton("安装", new OnClickListener() {
    	        @Override
    	        public void onClick(DialogInterface dialog, int which) {
    	        Intent intent = new Intent(Intent.ACTION_VIEW);
    	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	        intent.setDataAndType(Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath()+"/unionpay.apk"),
    	                                 "application/vnd.android.package-archive");
    	                    WDUnionPaymentActivity.this.startActivity(intent);
    	                    }
    	                  }).setNegativeButton("取消", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int arg1) {
								// TODO Auto-generated method stub
								int msgWhat = WDPayResult.RESULT_CANCEL_HANDLER;
				                String result = WDPayResult.RESULT_CANCEL;
				               String errMsg = WDPayResult.RESULT_CANCEL;
				               String detailInfo = "银联支付:用户取消了支付";
								
								WDPay instance = WDPay.getInstance(WDUnionPaymentActivity.this);
								
								if (instance != null && instance.payCallback != null) {
						            instance.payCallback.done(new WDPayResult(result, errMsg, detailInfo));
						        }
						        
						        if (instance != null && instance.payHandler != null) {
						        	
						        	Message msg = instance.payHandler.obtainMessage();
									msg.what= msgWhat;
									msg.obj = errMsg +" /"+detailInfo;
									instance.payHandler.sendMessage(msg);
						        }
								
								WDUnionPaymentActivity.this.finish();
								
								dialog.dismiss();
							}
						});
    	      m.show();
    	    }
	}

	/**
     * 复制assets下apk
     * @param context
     * @param fileName
     * @param path
     * @return
     */
    public boolean copyApkFromAssets(Context context, String fileName, String path) {
        boolean copyIsFinish = false;
        try {
          InputStream is = context.getAssets().open(fileName);
          File file = new File(path);
          file.createNewFile();
          FileOutputStream fos = new FileOutputStream(file);
          byte[] temp = new byte[1024];
          int i = 0;
          while ((i = is.read(temp)) > 0) {
            fos.write(temp, 0, i);
          }
          fos.close();
          is.close();
          copyIsFinish = true;
        } catch (IOException e) {
          e.printStackTrace();
        }
        return copyIsFinish;
      }

    /**
     * 处理银联手机支付控件返回的支付结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }

        String result = WDPayResult.RESULT_FAIL;
        String errMsg = WDPayResult.FAIL_ERR_FROM_CHANNEL;
        String detailInfo = "银联支付:";
        int msgWhat = WDPayResult.RESULT_FAIL_HANDLER;
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
        String str = data.getExtras().getString("pay_result");
        if (str == null) {
            result = WDPayResult.RESULT_FAIL;
            errMsg = WDPayResult.FAIL_ERR_FROM_CHANNEL;
            detailInfo += "invalid pay_result";
        } else {
            if (str.equalsIgnoreCase("success")) {
                result = WDPayResult.RESULT_SUCCESS;
                msgWhat = WDPayResult.RESULT_SUCCESS_HANDLER;
                errMsg = WDPayResult.RESULT_SUCCESS;
                detailInfo += "支付成功！";
            } else if (str.equalsIgnoreCase("fail")) {
                result = WDPayResult.RESULT_FAIL;
                errMsg = WDPayResult.RESULT_FAIL;
                detailInfo += "支付失败！";
            } else if (str.equalsIgnoreCase("cancel")) {
            	msgWhat = WDPayResult.RESULT_CANCEL_HANDLER;
                result = WDPayResult.RESULT_CANCEL;
                errMsg = WDPayResult.RESULT_CANCEL;
                detailInfo += "用户取消了支付";
            }
        }

        WDPay instance = WDPay.getInstance(WDUnionPaymentActivity.this);

        if (instance != null && instance.payCallback != null) {
            instance.payCallback.done(new WDPayResult(result, errMsg, detailInfo));
        }
        
        if (instance != null && instance.payHandler != null) {
        	
        	Message msg = instance.payHandler.obtainMessage();
			msg.what= msgWhat;
			msg.obj = errMsg +" /"+detailInfo;
			instance.payHandler.sendMessage(msg);
        }

        this.finish();
    }
    private static final String UN_APK_PACKAGE = "com.unionpay.uppay";
    	/**
    	 * 获取银联移动服务app版本号
    	 * @return Integer
    	 * -1 	未获取到 活未安装
    	 * 2	插件出问题 或 需要更新
    	 * else。。。。
    	 */
    private int getUNAPKVersion() {
        Integer version = -1;

        PackageManager packageManager=getPackageManager();
        try {
            PackageInfo Info=packageManager.getPackageInfo(UN_APK_PACKAGE, 0);
            version = Info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        	WDLogUtil.e("union payment", e.getMessage());
        }

        return version;
    }
}

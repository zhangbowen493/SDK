/**
 * BCWechatPaymentActivity.java
 *
 * Created by xuanzhui on 2015/7/27.
 * Copyright (c) 2015 BeeCloud. All rights reserved.
 */
package cn.wd.checkout.api;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import cn.wd.checkout.processor.WDCache;
import cn.wd.checkout.processor.WDLogUtil;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


/**
 * 微信支付结果接收类
 */
public class WDWechatPaymentActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "Checkout WechatPay";

    private IWXAPI wxAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WDLogUtil.i(TAG, "into weixin return activity");

        try {
            String wxAppId = WDCache.getInstance(null).wxAppId;
            if (wxAppId != null && wxAppId.length() > 0) {
                wxAPI = WXAPIFactory.createWXAPI(this, wxAppId);
                wxAPI.handleIntent(getIntent(), this);
            } else {
            	WDLogUtil.e(TAG, "Error: wxAppId 不合法 WechatPaymentActivity: " + wxAppId);
            }
        } catch (Exception ex) {
        	WDLogUtil.e(TAG, ex.getMessage());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            setIntent(intent);
            if (wxAPI != null) {
                wxAPI.handleIntent(intent, this);
            }
        } catch (Exception ex) {
        	WDLogUtil.e(TAG, ex.getMessage());
        }
    }

    /**
     * 微信发送请求到第三方应用时，会回调到该方法
     */
    @Override
    public void onReq(BaseReq baseReq) {
    	
    	WDLogUtil.i(TAG, "onReq(BaseReq baseReq) "+baseReq.toString());

    }
    
    String result = WDPayResult.RESULT_FAIL;
    String errMag = WDPayResult.FAIL_ERR_FROM_CHANNEL;
    String detailInfo = "WECHAT_PAY: ";
    int msgWhat = WDPayResult.RESULT_FAIL_HANDLER;
    /**
     * 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
     */
    @Override
    public void onResp(BaseResp baseResp) {
    	WDLogUtil.i(TAG, "onPayFinish, result code = " + baseResp.errCode);
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
            	msgWhat = WDPayResult.RESULT_SUCCESS_HANDLER;
                result = WDPayResult.RESULT_SUCCESS;
                errMag = WDPayResult.RESULT_SUCCESS;
                detailInfo += "用户支付已成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
            	msgWhat = WDPayResult.RESULT_CANCEL_HANDLER;
                result = WDPayResult.RESULT_CANCEL;
                errMag = WDPayResult.RESULT_CANCEL;
                detailInfo += "用户取消";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                detailInfo += "发送被拒绝";
                break;
            case BaseResp.ErrCode.ERR_COMM:
                detailInfo += "一般错误";
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                detailInfo += "不支持错误";
                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                detailInfo += "发送失败";
                break;
            default:
                detailInfo += "支付失败";
        }
        this.finish();
        
    }

	private void CallBack() {
		WDPay instance = WDPay.getInstance(WDWechatPaymentActivity.this);

        WDLogUtil.i(TAG,"115 payCallback.done (result="+ result+" errMag ="+ errMag+" detailInfo="+ detailInfo);
        if (instance != null && instance.payCallback != null) {
        	WDLogUtil.i(TAG,"116 payCallback.done (result="+ result+" errMag ="+ errMag+" detailInfo="+ detailInfo);
            instance.payCallback.done(new WDPayResult(result, errMag, detailInfo));
        }
        if (instance != null && instance.payHandler != null) {
        	
        	Message msg = instance.payHandler.obtainMessage();
			msg.what= msgWhat;
			msg.obj = errMag +" /"+detailInfo;
			instance.payHandler.sendMessage(msg);
			WDLogUtil.i(TAG,"125 payHandler (result="+ result+" errMag ="+ errMag+" detailInfo="+ detailInfo);
        }
        
	}
    
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	CallBack();
    }
}

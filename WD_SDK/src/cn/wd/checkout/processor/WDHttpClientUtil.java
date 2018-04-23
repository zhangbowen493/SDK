/**
 * BCHttpClientUtil.java
 *
 * Created by xuanzhui on 2015/7/27.
 * Copyright (c) 2015 BeeCloud. All rights reserved.
 */
package cn.wd.checkout.processor;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import com.google.gson.Gson;

import android.app.Activity;
import android.content.Context;
import cn.wd.checkout.api.WDPay;
import cn.wd.checkout.api.WDReqParams;


/**
 * 网络请求工具类
 */
public class WDHttpClientUtil {
	/**
	 * 网络请求成功
	 */
	public static final String Success ="succ";
	/**
	 * 网络请求失败
	 */
	public static final String Failure ="fail";
	

    //订单支付部分URL 和 获取扫码信息
    private static final String BILL_PAY_URL = "directpay/payService.do?";


    private final static String PAYPAL_LIVE_BASE_URL = "https://api.paypal.com/v1/";
    private final static String PAYPAL_SANDBOX_BASE_URL = "https://api.sandbox.paypal.com/v1/";

    private final static String PAYPAL_ACCESS_TOKEN_URL= "oauth2/token";

    /**
     * 随机获取主机, 并加入API版本号
     */
    private static String getRandomHost(Activity context) {
    	String way = WDCache.getInstance(context).requestWay;
    	if("CST".equals(way)){
    		return "http://cashtest.wdepay.cn:20080/";//开发测试地址
    	}else if("CT".equals(way)){
//    		return "http://cash.wdepay.cn:20080/";//测试环境地址
    		return "http://cashzsc.wdepay.cn:20080/";//测试环境地址
    	}else if("custom".equals(way)){
    		return WDCache.getInstance(context).customIP;
    	}else {
    		return "http://cash.wdepay.cn:30080/";//生产环境地址
    	}
    }

    /**
     * @return  支付请求URL
     */
    public static String getBillPayURL(Activity context) {
    	String way = WDCache.getInstance(context).requestWay;
    	if("custom".equals(way)){
    		return getRandomHost(context) +"/"+WDCache.customURL;
    	}
        return getRandomHost(context) + BILL_PAY_URL;
    }

    /**
     * @return  获取扫码信息URL
     */
    public static String getQRCodeReqURL(Activity context) {
    	String way = WDCache.getInstance(context).requestWay;
    	if("custom".equals(way)){
    		return getRandomHost(context) +"/"+WDCache.customURL;
    	}
        return getRandomHost(context) + BILL_PAY_URL;
    }



    public static String getPayPalAccessTokenUrl() {
        if (WDCache.getInstance(null).paypalPayType == WDPay.PAYPAL_PAY_TYPE.LIVE)
            return PAYPAL_LIVE_BASE_URL + PAYPAL_ACCESS_TOKEN_URL;
        else
            return PAYPAL_SANDBOX_BASE_URL + PAYPAL_ACCESS_TOKEN_URL;
    }

    /**
     * @return  HttpClient实例
     */
    public static HttpClient wrapClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactory sf = new WDSSLSocketFactory(trustStore);
            
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(params, WDCache.getInstance(null).networkTimeout);
            HttpConnectionParams.setSoTimeout(params, WDCache.getInstance(null).networkTimeout);

            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                    params, registry);
            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    /**
     * http get 请求
     * @param url   请求uri
     * @return      HttpResponse请求结果实例
     */
    public static HttpResponse httpGet(String url) {
        HttpClient client = wrapClient();

        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = null;
        try {
            response = client.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * http post 请求
     * @param url       请求url
     * @param entity    post参数
     * @return          HttpResponse请求结果实例
     */
    public static HttpResponse httpPost(String url, StringEntity entity) {
    	// https 请求模式
//        HttpClient client = wrapClient();
        
        HttpClient client = new DefaultHttpClient();

        
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);

        try {
            return client.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * http post 请求
     * @param url       请求url
     * @param para      post参数
     * @return          HttpResponse请求结果实例
     */
    public static HttpResponse httpPost(String url, Map<String, Object> para) {
        Gson gson = new Gson();
        String param = gson.toJson(para);

        //Log.w("BCHttpClientUtil", param);

        StringEntity entity;
        try {
            entity = new StringEntity(param, HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        entity.setContentType("application/json");
        return httpPost(url, entity);
    }


}

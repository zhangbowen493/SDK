/**
 * BCMD5Util.java
 *
 * Created by xuanzhui on 2015/7/27.
 * Copyright (c) 2015 BeeCloud. All rights reserved.
 */
package cn.wd.checkout.processor;

import android.util.Base64;

import java.security.MessageDigest;


/**
 * 用于MD5签名
 */
public class WDSecurityUtil {
	private static final String TAG = "Checkout WDSecurityUtil";

	/**
	 * 上传参数加密串
	 * 
	 * @param amount		交易金额 （以分为单位）
	 * @param appid			开发商APPID  （统一收银台 签约获取  ）
	 * @param channel		第三方支付渠道
	 * @param order_no		开发商订单号
	 * @param trantime		交易时间
	 * @param appSecret		开发商APPSecret （统一收银台 签约获取  ）
	 * |amount|appid|order_no|trantime|签名密钥串|	
	 * @return
	 */
	public static String getParamsMD5(String amount,String appid,String order_no,String trantime,String appSecret){
		StringBuffer strB = new StringBuffer("|");
		strB.append(amount).append("|").append(appid).append("|")
		.append(order_no).append("|")
		.append(trantime).append("|").append(appSecret).append("|");
		String digest = getMessageMD5Digest(strB.toString());
		return digest;
	}

    /**
     * @param s     待签名的字符串
     * @return      签名后的字符串
     */
    public static String getMessageMD5Digest(String s) {

        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] buffer = s.getBytes();
            //获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            //使用指定的字节更新摘要
            mdTemp.update(buffer);
            //获得密文
            byte[] md = mdTemp.digest();

            //把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param login login name
     * @param pass  password
     * @return  Base64 Auth encoded string
     */
    static String getB64Auth (String login, String pass) {
        String source=login+":"+pass;
        return "Basic "+ Base64.encodeToString(source.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
    }
}

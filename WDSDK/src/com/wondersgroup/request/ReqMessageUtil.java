package com.wondersgroup.request;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.wondersgroup.utils.WDValidationUtil;
import com.wondersgroup.wallet.WDLogUtil;


public class ReqMessageUtil {

	/**
	 * 网络请求参数转换函数，将Map形式参数转换成字符串
	 * @param map
	 * @return
	 */
	public static String paramsMapToStr(Map<String, Object> map) {
		if (map == null || map.isEmpty())
			return "";
		StringBuffer params = new StringBuffer("");
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			params.append(entry.getKey() + "=");
			params.append(entry.getValue().toString() + "&");
		}
		params.deleteCharAt(params.length() - 1);
		WDLogUtil.i(params);
		return params.toString();
	}
	/**
	 * 将map中的object转换成string
	 * @param map
	 * @return
	 */
	public static Map<String, String> paramsStringMap(Map<String, Object> map) {
		WDLogUtil.i("ReqMessageUtil.paramsStringMap==>"+map);
		Map<String, String> params = new HashMap<String, String>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			params.put(entry.getKey(), entry.getValue().toString());
		}
		WDLogUtil.i("ReqMessageUtil.paramsStringMap==>"+params);
		return params;
	}
	/**
	 * 将map中的object转换成string
	 * @param map
	 * @return
	 */
	public static Map<String, String> getParamsPost(Map<String, Object> map) {
		Map<String, String> params = new HashMap<String, String>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			params.put(entry.getKey(), entry.getValue().toString());
		}
		return params;
	}
	
	public static String getHeaderDate() {
		Date d = new Date();
		DateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
		return f.format(d);
	}
	/**
	 * 获取请求头中的sign
	 * @param dentifiCation
	 * @param timestamp
	 * @param version
	 * @param tradeCode
	 * @return
	 */
	public static String getHeaderSign(String dentifiCation, String timestamp, String version, String tradeCode) {
		StringBuffer s = new StringBuffer("");
		s.append(dentifiCation);
		s.append(timestamp);
		s.append(version);
		s.append(tradeCode);
		return getMD5(s.toString());
	}
	/**
	 * md5加密
	 * @param val
	 * @return
	 */
	public static String getMD5(String val) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(
					val.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}
		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}
	
	public static byte[] getReqMsg(Map<String, Object> header, Map<String, Object> body) {
		StringBuffer sb = new StringBuffer();
		sb.append("{\"head\":{");
		for (Map.Entry<String, Object> entry : header.entrySet()) {
			sb.append("\"");
			sb.append(entry.getKey());
			sb.append("\":");
			if (WDValidationUtil.isSameType(entry.getValue(), String.class)) sb.append("\"");
			sb.append(entry.getValue().toString());
			if (WDValidationUtil.isSameType(entry.getValue(), String.class)) sb.append("\"");
			sb.append(",");
		}
		if (!header.entrySet().isEmpty()) sb.deleteCharAt(sb.length() - 1);
		sb.append("},\"body\":{");
		for (Map.Entry<String, Object> entry : body.entrySet()) {
			sb.append("\"");
			sb.append(entry.getKey());
			sb.append("\":");
			if (WDValidationUtil.isSameType(entry.getValue(), String.class)) sb.append("\"");
			sb.append(entry.getValue().toString());
			if (WDValidationUtil.isSameType(entry.getValue(), String.class)) sb.append("\"");
			sb.append(",");
		}
		if (!body.entrySet().isEmpty()) sb.deleteCharAt(sb.length() - 1);
		sb.append("}}");
		return sb.toString().getBytes();
	}
	
}

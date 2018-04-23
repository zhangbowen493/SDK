package com.wondersgroup.request;

import java.util.Map;

import org.json.JSONObject;

import com.wondersgroup.wallet.WDResultCallback;
/**
 * 网络请求接口
 * @author liuzhipeng
 *
 */
public interface IRequest {

	public void httpGet(String url, IReqCallback callback);
	public void httpGet(String url, Map<String, Object> params, IReqCallback callback);
	public void httpPost(String url, Map<String, Object> params, IReqCallback callback);
	public void httpsGet(String url, IReqCallback callback);
	public void httpsGet(String url, Map<String, Object> params, IReqCallback callback);
	public void httpsPost(String url, Map<String, Object> params, IReqCallback callback);
	public void clean();
}

package com.android.volley.toolbox;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;

public class MyRequest extends Request<JSONObject>{
	private Response.Listener<JSONObject> mListener;
    private  Map<String, Object> headers;
    private Priority mPriority;

    public MyRequest(int method,String url,  Map<String, Object> headers, Response.ErrorListener errorListener,Response.Listener listener){
        super(method,url,errorListener);
        this.mListener = listener;
        this.headers = headers;

    }

    public MyRequest(int method, String url, Response.ErrorListener listener) {
        super(method, url, listener);
    }

    //设置超时
    @Override
    public RetryPolicy getRetryPolicy() {
        RetryPolicy retryPolicy = new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        return retryPolicy;
    }
    //Post请求参数(参数是键值对)
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return super.getParams();
    }
    //Post请求参数(参数是json的形式,也可以是字符串类型，主要看服务器需求)
    @Override
    public byte[] getBody() throws AuthFailureError {
        return super.getBody();
    }

    public void setPriority(Priority priority) {
        mPriority = priority;
    }
    //设置优先级
    @Override
    public Priority getPriority() {
        return mPriority == null ? Priority.NORMAL : mPriority;
    }

    //设置请求头
    public Map<String, Object> getHeaders(Map<String, Object> params) throws AuthFailureError {
        Map<String, Object> headMap = new HashMap<String, Object>();
		headMap.put("identifiCation"," ");
		headMap.put("timestamp", System.currentTimeMillis());
		headMap.put("version", "1.0");
		headMap.put("tradeCode", " ");
		headMap.put("tradeMsg", " ");
		headMap.put("sign", " ");

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("head", headMap);
		data.put("body", params);
        
        return data;
    }
    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse networkResponse) {
        String json = null;
        try {
            json = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));

            return  Response.success(JSON.parseObject(json),HttpHeaderParser.parseCacheHeaders(networkResponse));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    //将数据的返回结果回调给主线程
    @Override
    protected void deliverResponse(JSONObject jsonObject) {
        mListener.onResponse(jsonObject);
    }

}

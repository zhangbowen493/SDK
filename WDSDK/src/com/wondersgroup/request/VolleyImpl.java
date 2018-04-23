package com.wondersgroup.request;

import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.content.Context;
import android.os.Build;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wondersgroup.wallet.WDGlobalSetting;
import com.wondersgroup.wallet.WDLogUtil;
import com.wondersgroup.wallet.WDResultCallback;

public class VolleyImpl implements IRequest{

	private static VolleyImpl mInstance;
	private static Context mContext;
	private RequestQueue httpReqQueue;
	private RequestQueue httpsReqQueue;
	
	private VolleyImpl() {
		// TODO Auto-generated constructor stub
	}
	
	public synchronized static VolleyImpl getInstance(Context context) {
		mContext = context;
		if (mInstance == null) {
			mInstance = new VolleyImpl();
		}
		return mInstance;
	}
	
	private synchronized RequestQueue getHttpQueue() {
		if (httpReqQueue == null) {
			httpReqQueue = Volley.newRequestQueue(mContext);
		}
		return httpReqQueue;
	}
	
	private synchronized RequestQueue getHttpsQueue() {
		if (httpsReqQueue == null) {
			HttpStack stack;
			if (Build.VERSION.SDK_INT >= 9) {
                stack = new HurlStack(null, initSSLSocketFactory());
            } else {
                stack = new HttpClientStack(getHttpClient());
            }
			httpsReqQueue = Volley.newRequestQueue(mContext, stack);
		}
		return httpsReqQueue;
	}
	
	@Override
	public void httpGet(String url, final IReqCallback callback) {
		StringRequest req = getStringRequest(false, url, null, callback);
		getHttpQueue().add(req);
	}

	@Override
	public void httpGet(String url, Map<String, Object> params,
			final IReqCallback callback) {
		// TODO Auto-generated method stub
		StringRequest req = getStringRequest(false, url + "?" + ReqMessageUtil.paramsMapToStr(params), null, callback);
		getHttpQueue().add(req);
	}

	@Override
	public void httpPost(String url, final Map<String, Object> params,
			final IReqCallback callback) {
		// TODO Auto-generated method stub
		StringRequest req = getStringRequest(true, url, params, callback);
		getHttpQueue().add(req);
	}

	@Override
	public void httpsGet(String url, final IReqCallback callback) {
		// TODO Auto-generated method stub
		StringRequest req = getStringRequest(false, url, null, callback);
		getHttpsQueue().add(req);
	}

	@Override
	public void httpsGet(String url, final Map<String, Object> params,
			final IReqCallback callback) {
		// TODO Auto-generated method stub
		StringRequest req = getStringRequest(false, url + "?" + ReqMessageUtil.paramsMapToStr(params), null, callback);
		getHttpsQueue().add(req);
	}

	@Override
	public void clean() {
		// TODO Auto-generated method stub
		if (httpReqQueue != null) {
			httpReqQueue.stop();
			httpReqQueue = null;
		}
		if (httpsReqQueue != null) {
			httpsReqQueue.stop();
			httpsReqQueue = null;
		}
	}
	/**
	 * json 格式数据请求
	 * @param url
	 * @param params
	 * @param callback
	 * @return
	 */
	private JsonRequest getJsonRequest(String url, final Map<String, Object> params, final IReqCallback callback){
		JSONObject jsonObject = new JSONObject(params);
		WDLogUtil.e( "jsonRequest -> " +url+ jsonObject.toString());
		   JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST,url, jsonObject,
	                new Response.Listener<JSONObject>() {
	                    @Override
	                    public void onResponse(JSONObject response) {
	                        WDLogUtil.e( "response -> " + response.toString());
	        				callback.success(response.toString());
	                    }
	                }, new Response.ErrorListener() {
	            @Override
	            public void onErrorResponse(VolleyError error) {
	            	WDLogUtil.e( error.getMessage(), error);
	            	callback.error(error.getMessage());
	            }
	        })
	        {
			   
	            //注意此处override的getParams()方法,在此处设置post需要提交的参数根本不起作用
	            //必须象上面那样,构成JSONObject当做实参传入JsonObjectRequest对象里
	            //所以这个方法在此处是不需要的
//	    @Override
//	    protected Map<String, String> getParams() {
//	          Map<String, String> map = new HashMap<String, String>();
//	            map.put("name1", "value1");
//	            map.put("name2", "value2");

//	        return params;
//	    }

	            @Override
	            public Map<String, String> getHeaders() {
	                HashMap<String, String> headers = new HashMap<String, String>();
	                headers.put("Accept", "application/json");
	                headers.put("Content-Type", "application/json; charset=UTF-8");

	                return headers;
	            }
	        };
	        
	        return jsonRequest;
	}
	
	private StringRequest getStringRequest(final Boolean isPost, String url, final Map<String, Object> params, final IReqCallback callback) {
		StringRequest req = new StringRequest(isPost ? Request.Method.POST : Request.Method.GET, 
				url, new Response.Listener<String>() {
			
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				WDLogUtil.i(response);
				callback.success(response);
			}
		}, new Response.ErrorListener() {
			
			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				WDLogUtil.i(error);
				callback.error(error.getMessage());
			}
		}) {
			// 需要重写获取参数的函数,可以向服务器提交参数
			protected Map<String, String> getParams() throws AuthFailureError {
				// TODO Auto-generated method stub
//				if (isPost) return ReqMessageUtil.getParamsPost(params);
				return ReqMessageUtil.paramsStringMap(params);
			}
			@Override  
			   public Map<String, String> getHeaders() throws AuthFailureError  
			   {  
			Map<String, String> headers = new HashMap<String, String>();  
			headers.put("Charset", "UTF-8");  
			headers.put("Content-Type", "application/x-javascript");  
			headers.put("Accept-Encoding", "gzip,deflate");  
			return headers;  
			   }  
			
//			@Override
//            public String getBodyContentType() {
//                return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
//            }
//			@Override
//			public byte[] getBody() throws AuthFailureError {
//				// TODO Auto-generated method stub
//				return super.getBody();
//			}
		};
		return req;
	}
	

	private HttpClient getHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactory sf = new NoCheckSSLSocketFactory(trustStore);
            
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(params, WDGlobalSetting.networkTimeout);
            HttpConnectionParams.setSoTimeout(params, WDGlobalSetting.networkTimeout);

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
	private static javax.net.ssl.SSLSocketFactory initSSLSocketFactory() {
		javax.net.ssl.SSLSocketFactory sf = null;
        try {
        	KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());
			String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
	        TrustManagerFactory tmf = null;
	        tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(trustStore);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
            sf = sslContext.getSocketFactory();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
           
        return sf;
    }

	@Override
	public void httpsPost(String url, Map<String, Object> params,
			IReqCallback callback) {
		// TODO Auto-generated method stub
				WDLogUtil.e("httpsPost==>"+params);
//				StringRequest req = getStringRequest(true, url, params, callback);
				JsonRequest req = getJsonRequest( url, params, callback);
				getHttpQueue().add(req);
	}
	
	
	

}

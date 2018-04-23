package com.wondersgroup.request;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

import android.content.Context;

import com.wondersgroup.wallet.WDGlobalSetting;

public class UrlConnectionImpl implements IRequest {

	private static UrlConnectionImpl mInstance;
//	private static Context mContext;
	private ExecutorService executorService = Executors.newCachedThreadPool();
	
	private UrlConnectionImpl() {
		// TODO Auto-generated constructor stub
	}
	
	public synchronized static UrlConnectionImpl getInstance() {//Context context
//		mContext = context;
		if (mInstance == null) {
			mInstance = new UrlConnectionImpl();
		}
		return mInstance;
	}
	
	@Override
	public void httpGet(final String url, final IReqCallback callback) {
		// TODO Auto-generated method stub
		executorService.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				connect(false, url, null, callback);
			}
		});
	}

	@Override
	public void httpGet(final String url, final Map<String, Object> params,
			final IReqCallback callback) {
		// TODO Auto-generated method stub
		executorService.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				connect(false, url + "?" + ReqMessageUtil.paramsMapToStr(params), params, callback);
			}
		});
	}

	@Override
	public void httpPost(final String url, final Map<String, Object> params,
			final IReqCallback callback) {
		// TODO Auto-generated method stub
		executorService.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				connect(true, url, params, callback);
			}
		});
	}

	@Override
	public void httpsGet(final String url, final IReqCallback callback) {
		// TODO Auto-generated method stub
		executorService.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				connect(false, url, null, callback);
			}
		});
	}

	@Override
	public void httpsGet(final String url, final Map<String, Object> params,
			final IReqCallback callback) {
		// TODO Auto-generated method stub
		executorService.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				connect(false, url + "?" + ReqMessageUtil.paramsMapToStr(params), params, callback);
			}
		});
	}

	@Override
	public void httpsPost(final String url, final Map<String, Object> params,
			final IReqCallback callback) {
		// TODO Auto-generated method stub
		executorService.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				connect(true, url, params, callback);
			}
		});
	}
	
	@Override
	public void clean() {
		// TODO Auto-generated method stub
		executorService.shutdownNow();
	}


	private static final X509TrustManager xtm = new X509TrustManager() {
		public void checkClientTrusted(X509Certificate[] chain, String authType) {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	};
	private static final AllowAllHostnameVerifier HOSTNAME_VERIFIER = new AllowAllHostnameVerifier();
	private static X509TrustManager[] xtmArray = new X509TrustManager[] { xtm };
	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		 
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
    
	private void connect(Boolean isPost, String u, Map<String, Object> p, IReqCallback callback) {
		
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(u);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod(isPost ? "POST" : "GET");
			urlConnection.setUseCaches(false);
			
			urlConnection.setRequestProperty("encoding","UTF-8");
			urlConnection.setConnectTimeout(WDGlobalSetting.networkTimeout);
			urlConnection.setReadTimeout(WDGlobalSetting.networkTimeout);
			
			if (urlConnection instanceof HttpsURLConnection) { // 是Https请求
				SSLContext sslContext = SSLContext.getInstance("TLS");
				sslContext.init(new KeyManager[0], xtmArray, new SecureRandom());
			    if (sslContext != null) {
			        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
			        ((HttpsURLConnection) urlConnection).setSSLSocketFactory(sslSocketFactory);
			        ((HttpsURLConnection) urlConnection).setHostnameVerifier(HOSTNAME_VERIFIER);
			    }
			}
			
//			urlConnection.connect();
			
			if (isPost) {
				byte[] params = ReqMessageUtil.paramsMapToStr(p).getBytes();
				urlConnection.setDoOutput(true); 
				urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				urlConnection.setRequestProperty("Content-Length", String.valueOf(params.length));
				OutputStream outStream = urlConnection.getOutputStream();
				outStream.write(params);
				outStream.flush();
				outStream.close();
			}
			
			int responseCode = urlConnection.getResponseCode();
			if (responseCode == 200) { // 请求成功
			    InputStream inputStream = urlConnection.getInputStream();
			    // 读取结果，发送到主线程
			    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				byte[] data = new byte[2048];
				int count = -1;
				while ((count = inputStream.read(data, 0, 2048)) != -1)
					outStream.write(data, 0, count);
				data = null;
				String response = new String(outStream.toByteArray());
				
				callback.success(response);
				
			    inputStream.close();
			} else {
				callback.error("" + responseCode);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			callback.error(e.getMessage());
		} finally {  
            if (urlConnection != null) {  
                //关闭连接 即设置 http.keepAlive = false;  
            	urlConnection.disconnect();  
            }  
    }  
	}

}

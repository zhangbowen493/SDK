package com.wondersgroup.request;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import com.wondersgroup.wallet.WDLogUtil;

import android.content.Context;

public class HttpsUtil {

	
	 public static SSLSocketFactory buildSSLSocketFactory(Context context, int certRawResId) {  
		KeyStore keyStore = null;  
		try {  
			keyStore = buildKeyStore(context, certRawResId);  
		} catch (KeyStoreException e) {  
			e.printStackTrace();  
		} catch (CertificateException e) {  
			e.printStackTrace();  
		} catch (NoSuchAlgorithmException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
	
		String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();  
		TrustManagerFactory tmf = null;  
		try {  
			tmf = TrustManagerFactory.getInstance(tmfAlgorithm);  
			tmf.init(keyStore);  
		
		} catch (NoSuchAlgorithmException e) {  
			e.printStackTrace();  
		} catch (KeyStoreException e) {  
			e.printStackTrace();  
		}  
		
		SSLContext sslContext = null;  
		try {  
			sslContext = SSLContext.getInstance("TLS");  
		} catch (NoSuchAlgorithmException e) {  
			e.printStackTrace();  
		}  
		try {  
			sslContext.init(null, tmf.getTrustManagers(), null);  
		} catch (KeyManagementException e) {  
			e.printStackTrace();  
		}  
		
		return sslContext.getSocketFactory();  
	
	}  

	 public static HttpClient getHttpClient(Context context, int certRawResId) {  
		KeyStore keyStore = null;  
		try {  
			keyStore = buildKeyStore(context, certRawResId);  
		} catch (KeyStoreException e) {  
			e.printStackTrace();  
		} catch (CertificateException e) {  
			e.printStackTrace();  
		} catch (NoSuchAlgorithmException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
		if (keyStore != null) {  
		}  
		org.apache.http.conn.ssl.SSLSocketFactory sslSocketFactory = null;  
		try {  
			sslSocketFactory = new org.apache.http.conn.ssl.SSLSocketFactory(keyStore);  
		} catch (NoSuchAlgorithmException e) {  
			e.printStackTrace();  
		} catch (KeyManagementException e) {  
			e.printStackTrace();  
		} catch (KeyStoreException e) {  
			e.printStackTrace();  
		} catch (UnrecoverableKeyException e) {  
			e.printStackTrace();  
		}  
		
		HttpParams params = new BasicHttpParams();  
		
		SchemeRegistry schemeRegistry = new SchemeRegistry();  
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));  
		schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));  
		
		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);  
		
		return new DefaultHttpClient(cm, params);  
	}  

	 public static KeyStore buildKeyStore(Context context, int certRawResId)  
	throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {  
		String keyStoreType = KeyStore.getDefaultType();  
		KeyStore keyStore = KeyStore.getInstance(keyStoreType);  
		keyStore.load(null, null);  
		
		Certificate cert = readCert(context, certRawResId);  
		keyStore.setCertificateEntry("ca", cert);  
		
		return keyStore;  
	}  

	 public static Certificate readCert(Context context, int certResourceID) {  
		InputStream inputStream = context.getResources().openRawResource(certResourceID);  
		Certificate ca = null;  
		
		CertificateFactory cf = null;  
		try {  
			cf = CertificateFactory.getInstance("X.509");  
			ca = cf.generateCertificate(inputStream);  
		
		} catch (CertificateException e) {  
			e.printStackTrace();  
		}  
		return ca;  
	}  
	
}

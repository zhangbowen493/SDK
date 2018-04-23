package cn.cashier;


import android.app.Application;

public class SDKDemoApplication extends Application {

	private static SDKDemoApplication instance;
	/**
	 * 当前批次�?
	 */
	public String Batch_Number="000000";
	/**
	 * 订单流水�?
	 */
	public String Consume_Number="0";
	
	public static int Networke_way=1;
	
	
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		if (instance == null) {
			instance = SDKDemoApplication.this;
		}
	}
	public static SDKDemoApplication getInstance() {
		return instance;
	}
	/**
	 * 获得批次�?
	 */
	public String getBatch_Number() {
		return Batch_Number;
	}
	/**
	 * 设置批次�?
	 */
	public void setBatch_Number(String batch_Number) {
		Batch_Number = batch_Number;
	}
	/**
	 * 获得流水�?
	 */
	public String getConsume_Number() {
		return Consume_Number;
	}
	/**
	 * 设置流水�?
	 */
	public void setConsume_Number(String Consume_Number) {
		this.Consume_Number = Consume_Number;
	}
}



package com.wondersgroup.wallet;

import java.util.Map;

public interface WDResultCallback {
	public void onSucess(Map<String, Object> callbackMap);
	public void onFail(Map<String, Object> callbackMap);
}

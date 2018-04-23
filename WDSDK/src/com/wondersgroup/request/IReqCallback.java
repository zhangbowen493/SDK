package com.wondersgroup.request;

import java.util.Map;

/**
 * 网络请求结果回调接口
 * @author liuzhipeng
 *
 */
public interface IReqCallback {

	public void success(String response);
	public void error(String error);
}

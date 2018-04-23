/**
 * BCPayResult.java
 *
 * Created by xuanzhui on 2015/7/27.
 * Copyright (c) 2015 BeeCloud. All rights reserved.
 */
package cn.wd.checkout.api;


/**
 * 支付结果返回类
 * 
 */
public class WDPayResult implements WDResult {
	// result包含支付成功、取消支付、支付失败
	private String result;
	// 针对支付失败的情况，提供失败原因
	private String errMsg;
	// 提供详细的支付信息，比如原生的支付宝返回信息
	private String detailInfo;

	/**
	 * 表示支付成功
	 */
	public static final String RESULT_SUCCESS = "SUCCESS";
	/**
	 * 表示用户取消支付
	 */
	public static final String RESULT_CANCEL = "CANCEL";
	/**
	 * 表示支付失败
	 */
	public static final String RESULT_FAIL = "FAIL";

	/**
	 * 表示支付成功 针对handler
	 */
	public static final int RESULT_SUCCESS_HANDLER = 1;
	/**
	 * 表示用户取消支付 针对handler
	 */
	public static final int RESULT_CANCEL_HANDLER = -1;
	/**
	 * 表示支付失败 针对handler
	 */
	public static final int RESULT_FAIL_HANDLER = 0;
	
	/**
	 * 表示支付中，未获取确认信息
	 */
	public static final String RESULT_PAYING_UNCONFIRMED = "RESULT_PAYING_UNCONFIRMED";
	/**
	 * 网络问题造成的支付失败
	 */
	public static final String FAIL_NETWORK_ISSUE = "FAIL_NETWORK_ISSUE";
	/**
	 * 参数不合法造成的支付失败
	 */
	public static final String FAIL_INVALID_PARAMS = "FAIL_INVALID_PARAMS";
	/**
	 * 从收银台服务端返回的错误 （调用统一收银台SDK失败）
	 */
	public static final String FAIL_ERR_FROM_SERVER = "FAIL_ERR_FROM_SERVER";

	/**
	 * 从第三方app支付渠道返回的错误信息 （支付渠道返回失败）
	 */
	public static final String FAIL_ERR_FROM_CHANNEL = "FAIL_ERR_FROM_CHANNEL";
	/**
	 * 支付过程中的Exception
	 */
	public static final String FAIL_EXCEPTION = "FAIL_EXCEPTION";
	/**

	/**
	 * 针对微信 支付版本错误（版本不支持）
	 */
	public static final String FAIL_WEIXIN_VERSION_ERROR = "WEIXIN_VERSION_ERROR";

	/**
	 * 未知支付渠道
	 */
	public static final String FAIL_UNKNOWN_WAY = "UNKNOWN_WAY";
	
	
	  /**
     * 针对银联，存在插件不存在需要安装的问题
     */
    public static final String FAIL_PLUGIN_NOT_INSTALLED = "FAIL_PLUGIN_NOT_INSTALLED";

    /**
     * 针对银联，存在插件需要升级的问题
     */
    public static final String FAIL_PLUGIN_NEED_UPGRADE = "FAIL_PLUGIN_NEED_UPGRADE";

	/**
	 * 构造函数
	 * 
	 * @param result
	 *            包含支付成功, 用户取消支付, 支付失败
	 * @param errMsg
	 *            支付失败的分类错误信息
	 * @param detailInfo
	 *            详细的支付结果信息, 对于错误显示详细的错误信息
	 */
	public WDPayResult(String result, String errMsg, String detailInfo) {
		this.result = result;
		this.errMsg = errMsg;
		this.detailInfo = detailInfo;
	}

	/**
	 * @return 支付结果
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @return 支付失败的分类错误信息
	 */
	public String getErrMsg() {
		return errMsg;
	}

	/**
	 * @return 详细的支付结果信息, 对于错误显示详细的错误信息
	 */
	public String getDetailInfo() {
		return detailInfo;
	}
}

package cn.wd.checkout.api;


/**
 * 操作回调类
 * @author Administrator
 *
 */
public interface WDCallBack {
	 /**
     * 操作完成后的回调接口
     * @param result    包含支付或者查询结果信息
     */
    void done(WDResult result);
}

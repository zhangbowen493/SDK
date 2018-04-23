package cn.wd.checkout.api;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.JavascriptInterface;
import cn.wd.checkout.processor.WDLogUtil;

import com.google.gson.Gson;

/*
 *@作者: Administrator
 *@版本: 
 *@version create time：2016-9-19 下午2:44:38
 */
public abstract class WDJavaScriptObject {

	Context mContext;

	public WDJavaScriptObject(Context context) {
		mContext = context;
	}

	// appid
	private String strAppid;
	// app secret 工作密钥
	private String strAppsecret;
	// 子商户号
	private String submerno;

	// 支付结果返回入口
	WDCallBack bcCallback = new WDCallBack() {
		@Override
		public void done(final WDResult bcResult) {
			final WDPayResult bcPayResult = (WDPayResult) bcResult;
			callback(bcPayResult);
		}
	};

	public abstract void callback(WDPayResult str);

	@JavascriptInterface
	public void CallAndroid(String name) {
		
		
		Gson gson = new Gson();
		try {
			
			WDParamBean paramBean = gson.fromJson(name, WDParamBean.class);
			WDLogUtil.d("------"+paramBean.toString());

		pay(paramBean);
		} catch (Exception e) {
			// TODO: handle exception
			callback(new WDPayResult(WDPayResult.RESULT_FAIL, "参数不合法", "数据格式不合法"));
		}
	}

	public void pay(WDParamBean paramBean) {
		// TODO Auto-generated method stub

		strAppid = paramBean.getAppid();
		strAppsecret = paramBean.getAppkey();
		submerno = paramBean.getSubnum();

		// 在主activity的onCreate函数中初始化账户中的AppID和AppSecret
		// appId appid 统一收银台签约获取 id唯一
		// appSecret App Secret 统一收银台签约获取 不唯一 每天都会重新生成 故需要每次设置
		CheckOut.setAppIdAndSecret(strAppid, strAppsecret);
		
		
		if("CT".equals(paramBean.getAccess())){
			//联调测试
			WDLogUtil.d("联调测试环境");
			CheckOut.setNetworkWay("CT");
			CheckOut.setLianNetworkWay("");
		}else if("CST".equals(paramBean.getAccess())){
			//开发测试环境
			WDLogUtil.d("开发测试环境");
			CheckOut.setNetworkWay("CST");
            CheckOut.setLianNetworkWay("TS");
		}else{
			//正式环境
			WDLogUtil.d("正式环境");
			CheckOut.setNetworkWay("");
        	CheckOut.setLianNetworkWay("");
		}

		String channel = paramBean.getChannel();
		String money = paramBean.getAmount();
		String goodsTitle = paramBean.getSubject();
		String goodsDesc = paramBean.getBody();
		String orderTitle = paramBean.getOrderNo();
		String orderDesc = paramBean.getDescription();
		Long i = 0l;
		if (isNumeric(money)) {
			i = Long.parseLong(money);
		} else {
			callback(new WDPayResult(WDPayResult.RESULT_FAIL, "参数不合法", "交易金额不合法"));
			return;
		}
		if ("weixin".equals(channel)) { // 微信
			// 对于微信支付, 手机内存太小会有OutOfResourcesException造成的卡顿, 以致无法完成支付
			// 这个是微信自身存在的问题
			WDPay.getInstance(mContext).reqPayAsync(strAppid, strAppsecret,
					WDReqParams.WDChannelTypes.wepay, submerno, goodsTitle, // 订单标题
					goodsDesc, i, // 订单金额(分)
					orderTitle, // 订单流水号
					orderDesc, null, // 扩展参数(可以null)
					bcCallback);

		} else if ("alipay".equals(channel)) { // 支付宝

			WDPay.getInstance(mContext).reqPayAsync(strAppid, strAppsecret,
					WDReqParams.WDChannelTypes.alipay, submerno, goodsTitle, // 订单标题
					goodsDesc, i, // 订单金额(分)
					orderTitle, // 订单流水号
					orderDesc, null, // 扩展参数(可以null)
					bcCallback);

		} else if ("uppay".equals(channel)) { // 银联
			WDPay.getInstance(mContext).reqPayAsync(strAppid, strAppsecret,
					WDReqParams.WDChannelTypes.uppay, submerno, goodsTitle, // 订单标题
					goodsDesc, i, // 订单金额(分)
					orderTitle, // 订单流水号
					orderDesc, null, // 扩展参数(可以null) 
					bcCallback);
		} else if ("wdepay".equals(channel)) { // 链支付
			WDPay.getInstance(mContext).reqPayAsync(strAppid, strAppsecret,
					WDReqParams.WDChannelTypes.wdepay, submerno, goodsTitle, // 订单标题
					goodsDesc, i, // 订单金额(分)
					orderTitle, // 订单流水号
					orderDesc, null, // 扩展参数(可以null)
					bcCallback);
		} else if ("".equals(channel)) {

			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri content_url = Uri
					.parse("http://www.wdepay.cn/MobileFront/user/download/WandaApk.do");
			intent.setData(content_url);
			mContext.startActivity(intent);

		}
	}

	public final static boolean isNumeric(String s) {
		if (s != null && !"".equals(s.trim()))
			return s.matches("^\\+?[1-9][0-9]*$")&&s.length()<10;
		else
			return false;
	}
}

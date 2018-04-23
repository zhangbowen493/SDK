package com.wondersgroup.utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验类
 * 
 * @author liuzhipeng
 *
 */
public class WDValidationUtil {

	/**
	 * 判断对象o是否是type类型
	 * 
	 * @param o
	 * @param type
	 * @return
	 */
	public static boolean isSameType(Object o, Class type) {
		if (o == null)
			return false;
		return o.getClass().equals(type);
	}

	/**
	 * 验证参数中的字符串参数不为空
	 * 
	 * @param map
	 * @param params
	 * @return true为通过验证，false为没通过验证
	 */
	public static boolean validateStringParams(Map<String, Object> map,
			String... params) {
		if (map == null)
			return false;
		for (int i = 0; i < params.length; i++) {
			if (!isSameType(params[i], String.class) || params[i].equals(""))
				return false;
		}
		return true;
	}

	/**
	 * 中国电信号码格式验证 手机段： 133,153,180,181,189,177,1700,173
	 * **/
	private static final String CHINA_TELECOM_PATTERN = "(^1(33|53|7[37]|8[019])\\d{8}$)|(^1700\\d{7}$)";

	/**
	 * 中国联通号码格式验证 手机段：130,131,132,155,156,185,186,145,176,1707,1708,1709
	 * **/
	private static final String CHINA_UNICOM_PATTERN = "(^1(3[0-2]|4[5]|5[56]|7[6]|8[56])\\d{8}$)|(^170[7-9]\\d{7}$)";

	/**
	 * 中国移动号码格式验证
	 * 手机段：134,135,136,137,138,139,150,151,152,157,158,159,182,183,184
	 * ,187,188,147,178,1705
	 **/
	private static final String CHINA_MOBILE_PATTERN = "(^1(3[4-9]|4[7]|5[0-27-9]|7[8]|8[2-478])\\d{8}$)|(^1705\\d{7}$)";

	/**
	 * 仅手机号格式校验
	 */
	private static final String PHONE_PATTERN = new StringBuilder(300)
			.append(CHINA_MOBILE_PATTERN).append("|")
			.append(CHINA_TELECOM_PATTERN).append("|")
			.append(CHINA_UNICOM_PATTERN).toString();

	/**
	 * 检验是否为手机号码
	 * 
	 * @param phoneNumb
	 * @return
	 */
	public static boolean isPhone(String phoneNumb) {
		return match(PHONE_PATTERN, phoneNumb);
	}

	/**
	 * 检验是否为手机号码
	 * 
	 * @param phoneNumb
	 * @return
	 */
	public static boolean isPhone0531(String phoneNumb) {
		String Is_Phone = phoneNumb.trim();
		if (Is_Phone.length() != 11||!isNumber1(Is_Phone)) {
			return false;
		}
		if (Is_Phone.substring(0, 1).equals("1")) {
			return true;
		}
		return false;
	}

	private static boolean match(String regex, String input) {
		return Pattern.matches(regex, input);
	}

	/**
	 * 检验是否为姓名
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isName(String name) {
		String regEx = "^(([\u4e00-\u9fa5]{2,8})|([a-zA-Z]{2,16}))$";
		Pattern pat = Pattern.compile(regEx);
		Matcher matcher = pat.matcher(name);
		boolean flg = false;
		if (matcher.find())
			flg = true;

		return flg;
	}

	/*
	 * 校验过程： 1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
	 * 2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，将个位十位数字相加，即将其减去9），再求和。
	 * 3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
	 */
	/**
	 * 校验银行卡卡号
	 */
	public static boolean checkBankCar(String bankCard) {
		if (bankCard.length() < 15 || bankCard.length() > 19) {
			return false;
		}
		char bit = getBankCardCheckCode(bankCard.substring(0,
				bankCard.length() - 1));
		if (bit == 'N') {
			return false;
		}
		return bankCard.charAt(bankCard.length() - 1) == bit;
	}

	/*
	 * 校验过程： 1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
	 * 2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，将个位十位数字相加，即将其减去9），再求和。
	 * 3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
	 */
	/**
	 * 校验银行卡卡号
	 */
	public static boolean checkBankCard0531(String bankCard) {
		String Is_bankCard = bankCard.trim();
		if (isNull(Is_bankCard) || Is_bankCard.length() < 10
				|| Is_bankCard.length() > 19||!isNumber1(Is_bankCard)) {
			return false;
		}
		return true;
	}

	/**
	 * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
	 * 
	 * @param nonCheckCodeBankCard
	 * @return
	 */
	private static char getBankCardCheckCode(String nonCheckCodeBankCard) {
		if (nonCheckCodeBankCard == null
				|| nonCheckCodeBankCard.trim().length() == 0
				|| !nonCheckCodeBankCard.matches("\\d+")) {
			// 如果传的不是数据返回N
			return 'N';
		}
		char[] chs = nonCheckCodeBankCard.trim().toCharArray();
		int luhmSum = 0;
		for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
			int k = chs[i] - '0';
			if (j % 2 == 0) {
				k *= 2;
				k = k / 10 + k % 10;
			}
			luhmSum += k;
		}
		return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
	}

	/**
	 * 检验身份证号码是否正确
	 * 
	 * @param cardid
	 * @return
	 */
	public static boolean getValidIdCard(String cardid) {
		String ls_id = cardid;
		if (ls_id.length() != 18) {
			return false;
		}
		char[] l_id = ls_id.toCharArray();
		int l_jyw = 0;
		int[] wi = new int[] { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8,
				4, 2, 1 };
		char[] ai = new char[] { '1', '0', 'X', '9', '8', '7', '6', '5', '4',
				'3', '2' };
		for (int i = 0; i < 17; i++) {
			if (l_id[i] < '0' || l_id[i] > '9') {
				return false;
			}
			l_jyw += (l_id[i] - '0') * wi[i];
		}
		l_jyw = l_jyw % 11;
		if (ai[l_jyw] != l_id[17]) {
			return false;
		}
		return true;
	}

	/**
	 * 检验身份证号码是否正确
	 * 
	 * @param cardid
	 * @return
	 */
	public static boolean getValidIdCard0531(String cardid) {
		String ls_id = cardid.trim();
		if (ls_id.length() != 18 && ls_id.length() != 15) {
			return false;
		}
		return ls_id.matches("[0-9]{15}|[0-9]{17}[0-9X]");
	}

	/**
	 * 
	 * 功能描述：金额字符串转换：单位分转成单元
	 * 
	 * @param str
	 *            传入需要转换的金额字符串
	 * @return 转换后的金额字符串
	 */
	public static String fenToYuan(Object o) {
		if (o == null)
			return "0.00";
		String s = o.toString();
		int len = -1;
		StringBuilder sb = new StringBuilder();
		if (s != null && s.trim().length() > 0 && !s.equalsIgnoreCase("null")) {
			s = removeZero(s);
			if (s != null && s.trim().length() > 0
					&& !s.equalsIgnoreCase("null")) {
				len = s.length();
				int tmp = s.indexOf("-");
				if (tmp >= 0) {
					if (len == 2) {
						sb.append("-0.0").append(s.substring(1));
					} else if (len == 3) {
						sb.append("-0.").append(s.substring(1));
					} else {
						sb.append(s.substring(0, len - 2)).append(".")
								.append(s.substring(len - 2));
					}
				} else {
					if (len == 1) {
						sb.append("0.0").append(s);
					} else if (len == 2) {
						sb.append("0.").append(s);
					} else {
						sb.append(s.substring(0, len - 2)).append(".")
								.append(s.substring(len - 2));
					}
				}
			} else {
				sb.append("0.00");
			}
		} else {
			sb.append("0.00");
		}
		return sb.toString();
	}

	/**
	 * 
	 * 功能描述：去除字符串首部为"0"字符
	 * 
	 * @param str
	 *            传入需要转换的字符串
	 * @return 转换后的字符串
	 */
	public static String removeZero(String str) {
		char ch;
		String result = "";
		if (str != null && str.trim().length() > 0
				&& !str.trim().equalsIgnoreCase("null")) {
			try {
				for (int i = 0; i < str.length(); i++) {
					ch = str.charAt(i);
					if (ch != '0') {
						result = str.substring(i);
						break;
					}
				}
			} catch (Exception e) {
				result = "";
			}
		} else {
			result = "";
		}
		return result;

	}

	/**
	 * 判断是不是整数
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumber1(String str) {// 判断整型
		return str.matches("^\\d+$$");
	}

	/**
	 * 判断是不是小数
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumber2(String str) {// 判断小数，与判断整型的区别在与d后面的小数点（红色）
		return str.matches("\\d+\\.\\d+$");
	}

	/**
	 * 判断是不是小数开头
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumber3(String str) {// 判断小数点开头
		return str.matches("\\.\\d+$");
	}

	/**
	 * 判断金额是否合法
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isJinE(String tranAmt) {
		if (WDValidationUtil.isNull(tranAmt) || tranAmt.length() < 1 || tranAmt.length() > 10
				|| isNumber2(tranAmt) || isNumber3(tranAmt)
				|| Double.parseDouble(tranAmt) <= 0) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * 判断优惠金额是否合法
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isDiscountJinE(String tranAmt) {
		if (tranAmt == null || tranAmt.length() < 1 || tranAmt.length() > 10
				|| isNumber2(tranAmt) || isNumber3(tranAmt)
				|| Double.parseDouble(tranAmt) < 0) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * 判断是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNull(String msg) {
		if (msg == null || "".equals(msg.trim())) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 判断是否为正确订单号
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isOrderNO(String msg) {
		String input = msg.trim();
		boolean flage = false;
		String check_str = "[0-9a-zA-Z]*";
		Pattern pzm = Pattern.compile(check_str);
		Matcher mat = pzm.matcher(input);
		flage = mat.matches();
		return flage;
	}

	/**
	 * 判断是否为指定日期格式
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isDateFormate(String msg) {
		String date = msg.trim();
		DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		try {
			Date d = (Date) formatter.parse(date);
			return msg.equals(formatter.format(d));
		} catch (Exception e) {
			return false;
		}
	}
}

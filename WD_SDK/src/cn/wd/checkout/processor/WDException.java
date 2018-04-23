/**
 * BCException.java
 *
 * Created by xuanzhui on 2015/7/29.
 * Copyright (c) 2015 BeeCloud. All rights reserved.
 */
package cn.wd.checkout.processor;

/**
 * 异常类
 */
public class WDException extends Exception {
    public WDException(String exceptionMsg){
        super(exceptionMsg);
    }
}

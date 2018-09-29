package com.szmengran.hbase.exception;

import com.suntak.exception.AbstractException;
import com.szmengran.exception.CustomerExceptionMessage;

/**
 * @Package com.szmengran.exception
 * @Description: TODO
 * @date 2018年9月27日 下午3:51:23
 * @author <a href="mailto:android_li@sina.cn">Joe</a>
 */
public class BusinessException extends AbstractException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public BusinessException(Integer status) {
		super.setStatus(status);
		super.setMsg(CustomerExceptionMessage.getMessage(status));
	}
	
	public BusinessException(Integer status, String errmsg) {
		super.setStatus(status);
		super.setMsg(errmsg);
	}

}

package com.szmengran.hbase.service;

import com.szmengran.hbase.entity.T_common_file;

/**
 * @Package com.szmengran.hbase.service
 * @Description: TODO
 * @date 2018年9月29日 下午4:10:00
 * @author <a href="mailto:android_li@sina.cn">Joe</a>
 */
public interface FileService {
	
	public Boolean insert(T_common_file t_common_file) throws Exception;
	
	/**
	 * 根据文件ID查找文件
	 * @param fileid
	 * @return
	 * @throws Exception 
	 * @author <a href="mailto:android_li@sina.cn">Joe</a>
	 */
	public T_common_file findById(String fileid) throws Exception;
}

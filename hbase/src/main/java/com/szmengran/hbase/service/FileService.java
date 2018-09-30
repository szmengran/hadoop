package com.szmengran.hbase.service;

import java.io.OutputStream;

import org.springframework.web.multipart.MultipartFile;

import com.szmengran.hbase.entity.T_common_file;

/**
 * @Package com.szmengran.hbase.service
 * @Description: TODO
 * @date 2018年9月29日 下午4:10:00
 * @author <a href="mailto:android_li@sina.cn">Joe</a>
 */
public interface FileService {
	
	/**
	 * 新增一个文件，如果文件已经存在则返回已存在的文件信息
	 * @param t_common_file
	 * @return
	 * @throws Exception 
	 * @author <a href="mailto:android_li@sina.cn">Joe</a>
	 */
	public T_common_file insert(T_common_file t_common_file) throws Exception;
	
	/**
	 * 根据文件ID查找文件
	 * @param fileid
	 * @return
	 * @throws Exception 
	 * @author <a href="mailto:android_li@sina.cn">Joe</a>
	 */
	public T_common_file findById(String fileid) throws Exception;
	
	/**
	 * 文件上传
	 * @param file
	 * @param userid
	 * @return
	 * @throws Exception 
	 * @author <a href="mailto:android_li@sina.cn">Joe</a>
	 */
	public T_common_file upload(MultipartFile file, String userid) throws Exception;
	
	
	/**
	 * 文件下载
	 * @param path
	 * @param outputStream
	 * @param fileid
	 * @throws Exception 
	 * @author <a href="mailto:android_li@sina.cn">Joe</a>
	 */
	public void download(String path, OutputStream outputStream, String fileid) throws Exception;
	
}

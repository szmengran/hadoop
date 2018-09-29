package com.szmengran.hbase.service.impl;

import java.util.Calendar;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.szmengran.hbase.entity.T_common_file;
import com.szmengran.hbase.service.FileService;
import com.szmengran.hbase.service.HBaseService;
import com.szmengran.hbase.utils.HBaseConnectionPool;
import com.szmengran.hbase.utils.HdfsFileSystem;

/**
 * @Package com.szmengran.hbase.service.impl
 * @Description: HBase操作服务
 * @date 2018年9月28日 上午8:40:31
 * @author <a href="mailto:android_li@sina.cn">Joe</a>
 */
@Service
public class HBaseServiceImpl implements HBaseService{
	
	@Value("${hbase.config.hbase.client.keyvalue.maxsize}")
	private int maxsize;
	
	@Autowired
	private HBaseConnectionPool hbaseConnectionPool;
	
	@Autowired
	FileService fileService;
	
	@Override
	public T_common_file upload(MultipartFile file, String userid) throws Exception {
		try {
			String orgname = file.getOriginalFilename();
			long size = file.getSize();
            String fileid = DigestUtils.md5Hex(new StringBuffer(orgname).append(file.getSize()).append(userid).toString());
            T_common_file t_common_file = new T_common_file();
            t_common_file.setFileid(fileid);
            t_common_file.setSize(size);
            t_common_file.setOrgname(orgname);
            String arr[] = orgname.split("\\.");
            String suffix = arr.length > 1 ? arr[arr.length-1] : "";
            t_common_file.setSuffix(suffix);
            t_common_file.setType(file.getContentType());
            Boolean flag = fileService.insert(t_common_file);
            if (flag) { //如果数据保存成功则表示没有重复的文件存在
            	if ((1<<20)*10 < size) { //hbase文件大小限制在10MB
            		uploadBigFile(file, fileid, userid);
            	} else {
            		uploadSmallFile(file, fileid);
            	}
            } else {
            	T_common_file old_file = checkMD5Sum(file, fileid);
            	if (old_file != null) {
            		return old_file;
            	} else if ((1<<20)*10 < size) { //hbase文件大小限制在10MB
            		uploadBigFile(file, fileid, userid);
            	} else {
            		uploadSmallFile(file, fileid);
            	}
            }
            return t_common_file;
		} catch (Exception e) {
			throw e;
		}
	}
	
	private T_common_file checkMD5Sum(MultipartFile file, String fileid) throws Exception{
		T_common_file t_common_file = fileService.findById(fileid);
		String md5 = DigestUtils.md5Hex(file.getBytes());
//		t_common_file
		return t_common_file;
	}
	
	private void uploadSmallFile(MultipartFile file, String fileid) throws Exception {
		Connection connection = null;
		Table table = null;
		try {
			connection = hbaseConnectionPool.getConnection();
			table = connection.getTable(TableName.valueOf("file"));
			Put put = new Put(fileid.getBytes());
	        put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("data"), IOUtils.toByteArray(file.getInputStream()));
	        table.put(put);
		} catch (Exception e) {
			throw e;
		} finally {
			table.close();
			connection.close();
		}
	}
	
	private void uploadBigFile(MultipartFile file, String fileid, String userid) throws Exception {
//		String orgname = file.getOriginalFilename();
//	    Long size = file.getSize();
//	    String type = file.getContentType();
//	    String md5 = DigestUtils.md5Hex(IOUtils.toByteArray(file.getInputStream())); 
//	    String userid = "engineering";
//	    T_common_file t_common_file = new T_common_file();
//	    t_common_file.setFileid(md5);
//	    t_common_file.setOrgname(orgname);
//	    t_common_file.setSize(size);
//	    t_common_file.setType(type);
//	    t_common_file.setUserid(userid);
//	    t_common_file.setPath("/user/"+userid);
//	    String arr[] = orgname.split("\\.");
//	    t_common_file.setSuffix(arr[arr.length-1]);
//	    files.add(t_common_file);
//	    if(fileService.save(t_common_file)) {
//	    		HdfsFileSystem.createFile(file.getInputStream(), t_common_file.getPath(), md5);  
//	    }
		StringBuffer path = new StringBuffer();
		Calendar calendar = Calendar.getInstance();
		path.append("/").append(userid).append("/").append(calendar.get(Calendar.YEAR)).append("/").append(Calendar.MONTH+1).append("/").append(Calendar.DATE);
	    HdfsFileSystem.createFile(file.getInputStream(), path.toString(), fileid);
	}
	
	public static void main (String args[]) {
		System.out.println((1<<20)*10);
		System.out.println(1024*1024*10);
	}
}

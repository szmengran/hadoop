package com.szmengran.hbase.service.impl;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.szmengran.common.orm.dao.AbstractDao;
import com.szmengran.hbase.entity.T_common_file;
import com.szmengran.hbase.service.FileService;

/**
 * @Package com.szmengran.hbase.service.impl
 * @Description: 文件服务
 * @date 2018年9月29日 下午4:10:19
 * @author <a href="mailto:android_li@sina.cn">Joe</a>
 */
@Service
public class FileServiceImpl implements FileService{

	@Autowired
	AbstractDao abstractDao;
	
	@Override
	public Boolean insert(T_common_file t_common_file) throws Exception {
		Timestamp createstamp = new Timestamp(System.currentTimeMillis());
		try {
			t_common_file.setCreatestamp(createstamp);
			t_common_file.setUpdatestamp(createstamp);
			t_common_file.setValidstatus("1");
			abstractDao.insert(t_common_file);
			return true;
		}catch(Exception e) {
			if(e.getMessage().contains("Duplicate entry")) {
				return false;
			}else{
				throw e;
			}
		}
	}
	
	@Override
	public T_common_file findById(String fileid) throws Exception {
		T_common_file t_common_file = new T_common_file();
		t_common_file.setFileid(fileid);
		return abstractDao.findByPrimaryKey(t_common_file);
	}
}

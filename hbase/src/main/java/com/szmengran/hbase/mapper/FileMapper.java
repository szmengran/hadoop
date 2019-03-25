package com.szmengran.hbase.mapper;
/**
 * @Package com.szmengran.hbase.mapper
 * @Description: TODO
 * @date Feb 18, 2019 8:37:40 PM
 * @author <a href="mailto:android_li@sina.cn">Joe</a>
 */

import org.apache.ibatis.annotations.Mapper;

import com.szmengran.hbase.entity.T_common_file;
import com.szmengran.mybatis.utils.mapper.IMapper;

@Mapper
public interface FileMapper extends IMapper<T_common_file> {
    
}

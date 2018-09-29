package com.szmengran.hbase.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.szmengran.hbase.utils.HBaseConnectionPool;

/**
 * @Package com.szmengran.hbase.config
 * @Description: HBase配置
 * @date 2018年9月21日 下午4:00:58
 * @author <a href="mailto:android_li@sina.cn">Joe</a>
 */
@Configuration
public class HBaseConfig {

	@Value("${hbase.config.hbase.zookeeper.quorum}")
	private String zkHost;
	
	@Value("${hbase.config.hbase.zookeeper.property.clientPort}")
	private String port;
	
	@Value("${hbase.config.hbase.client.keyvalue.maxsize}")
	private String maxsize;
	
	@Value("${hbase.pool.maxnum}")
	private int maxnum;
	
    @Bean
    public HBaseConnectionPool hbaseConnectionPool() throws Exception {
    	return new HBaseConnectionPool(zkHost, port, maxsize, maxnum);
    }
}

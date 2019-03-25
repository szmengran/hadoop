package com.szmengran.hbase.config;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.szmengran.hbase.utils.HbaseConnectionPool;

/**
 * @Package com.szmengran.hbase.config
 * @Description: HBase配置
 * @date 2018年9月21日 下午4:00:58
 * @author <a href="mailto:android_li@sina.cn">Joe</a>
 */
@Configuration
public class HBaseConnectionPoolConfig {

    @Value("${hbase.config.hbase.zookeeper.quorum}")
    private String quorum;
    
    @Value("${hbase.config.hbase.zookeeper.property.clientPort}")
    private String clientPort;
    
    @Value("${hbase.config.hbase.client.keyvalue.maxsize}")
    private String maxsize;
    
    @Value("${hbase.pool.maxnum}")
    private int maxnum;
    
    @Bean
    public HbaseConnectionPool hbaseConnectionPool() throws Exception {
        org.apache.hadoop.conf.Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", quorum);
        configuration.set("hbase.zookeeper.property.clientPort", clientPort);
        configuration.set("hbase.client.keyvalue.maxsize", maxsize);
        return HbaseConnectionPool.getInstance(configuration, maxnum);
    }
}

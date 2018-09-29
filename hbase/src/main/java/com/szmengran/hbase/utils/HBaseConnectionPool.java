package com.szmengran.hbase.utils;
/**
 * @Package com.szmengran.hbase.utils
 * @Description: TODO
 * @date 2018年9月22日 下午2:06:31
 * @author <a href="mailto:android_li@sina.cn">Joe</a>
 */

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

public class HBaseConnectionPool {

	private Configuration conf = null;
	private ExecutorService pool = null;
	
	public HBaseConnectionPool(String zkHost, String port, String maxsize, int maxnum) {
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", zkHost);
		conf.set("hbase.zookeeper.property.clientPort", port);
		conf.set("hbase.client.keyvalue.maxsize", maxsize);
		pool = Executors.newFixedThreadPool(maxnum);// 建立一个数量为maxnum的线程池
	}
	
	public Connection getConnection() throws IOException{
		Connection connection = null;// 要创建的connection
		try {
			connection = ConnectionFactory.createConnection(this.conf, this.pool);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		return connection;
	}
	
	
}

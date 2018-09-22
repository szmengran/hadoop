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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HBaseConnectionPool {
	
	@Value("${hbase.zookeeper.quorum}")
	private static String zkHost;
	
	@Value("${hbase.pool.maxnum}")
	private static int maxnum;
	
	private static Connection connection = null;// 要创建的connection

	public static Connection getConnection() {
		if (null == connection) {
			if (null == connection) {// 空的时候创建，不为空就直接返回；典型的单例模式
				Configuration conf = HBaseConfiguration.create();
				conf.set("hbase.zookeeper.quorum", zkHost);
				ExecutorService pool = Executors.newFixedThreadPool(maxnum);// 建立一个数量为maxnum的线程池
				try {
					connection = ConnectionFactory.createConnection(conf, pool);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		return connection;
	}
}

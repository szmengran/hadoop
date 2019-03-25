package com.szmengran.hbase.utils;
/**
 * @Package com.szmengran.hbase.utils
 * @Description: TODO
 * @date 2018年9月22日 下午2:06:31
 * @author <a href="mailto:android_li@sina.cn">Joe</a>
 */

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

public class HbaseConnectionPool {

    private static HbaseConnectionPool hbaseConnectionPool; //防止指令重排导致BUG
    private Connection connection;
    
    /**
     * 获取一个Hbase连接
     * @return
     * @throws IOException 
     * @author <a href="mailto:android_li@sina.cn">Joe</a>
     */
    public Connection getConnection() throws IOException {
        return this.connection;
    }
    
    /**
     * 获取Hbase连接池实例 
     * @param configuration
     * @param maxnum
     * @return 
     * @author <a href="mailto:android_li@sina.cn">Joe</a>
     * @throws IOException 
     */
    public static HbaseConnectionPool getInstance(Configuration configuration, int maxnum) throws IOException {
        if (null != hbaseConnectionPool) {
            return hbaseConnectionPool;
        }
        synchronized (HbaseConnectionPool.class) {
            if (null != hbaseConnectionPool) {
                return hbaseConnectionPool;
            }
            ExecutorService executor = new ThreadPoolExecutor(20, maxnum, 0L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
            Connection connection = ConnectionFactory.createConnection(configuration, executor);
            return new HbaseConnectionPool(connection);
        }
    }
    
    private HbaseConnectionPool(Connection connection) {
        this.connection = connection;
    }
    
}

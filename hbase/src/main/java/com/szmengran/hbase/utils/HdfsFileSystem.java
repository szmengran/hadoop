package com.szmengran.hbase.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;

/**
 * @Package com.szmengran.hbase.utils
 * @Description: 分布式文件操作
 * @date 2018年1月18日 下午3:57:31
 * @author <a href="mailto:android_li@sina.cn">Joe</a>
 */

public class HdfsFileSystem {
	
	public static String HDFS_HOST;
	
	@Value("${hbase.hdfs.host}")
    public void setHost(String host) {
		HdfsFileSystem.HDFS_HOST = host;
    }

	/**
	 * 按路径上传文件到hdfs
	 * 
	 * @param conf
	 * @param local
	 * @param remote
	 * @throws IOException
	 */
	public static void copyFile(Configuration conf, String uri, String local, String remote) throws IOException {
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		fs.copyFromLocalFile(new Path(local), new Path(remote));
		fs.close();
	}

	/**
	 * 按路径下载hdfs上的文件
	 * 
	 * @param conf
	 * @param uri
	 * @param remote
	 * @param local
	 * @throws IOException
	 */
	public static void download(Configuration conf, String uri, String remote, String local) throws IOException {
		Path path = new Path(remote);
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		fs.copyToLocalFile(path, new Path(local));
		fs.close();
	}

	/**
	 * File对象上传到hdfs
	 * 
	 * @param conf
	 * @param uri
	 * @param remote
	 * @param local
	 * @throws IOException
	 */
	public static void createFile(File localPath, String hdfsPath) throws IOException {
		InputStream in = null;
		hdfsPath = HDFS_HOST+hdfsPath;
		try {
			Configuration conf = new Configuration();
			FileSystem fileSystem = FileSystem.get(URI.create(hdfsPath), conf);
			FSDataOutputStream out = fileSystem.create(new Path(hdfsPath));
			in = new BufferedInputStream(new FileInputStream(localPath));
			IOUtils.copyBytes(in, out, 4096, false);
			out.hsync();
			out.close();
		} finally {
			IOUtils.closeStream(in);
		}
	}
	
	/**
	 * File对象上传到hdfs
	 * @param inputStream
	 * @param filepath
	 * @param filename
	 * @throws IOException      
	 * @return: void      
	 * @throws   
	 * @author <a href="mailto:android_li@sina.cn">Joe</a>
	 */
	public static void createFile(InputStream inputStream, String filepath, String filename) throws IOException {
		InputStream in = null;
		String hdfsPath = HDFS_HOST+filepath+"/"+filename;
		try {
			Configuration conf = new Configuration();
			FileSystem fileSystem = FileSystem.get(URI.create(hdfsPath), conf);
			FSDataOutputStream out = fileSystem.create(new Path(hdfsPath));
			in = new BufferedInputStream(inputStream);
			IOUtils.copyBytes(in, out, 4096, false);
			out.hsync();
			out.close();
		} finally {
			IOUtils.closeStream(in);
		}
	}
}

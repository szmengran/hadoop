package com.szmengran.hbase.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

/**
 * @Package com.szmengran.hbase.utils
 * @Description: 分布式文件操作
 * @date 2018年1月18日 下午3:57:31
 * @author <a href="mailto:android_li@sina.cn">Joe</a>
 */

public class HdfsFileSystem {
    
    /**
     * 将文件复制到输出流中，用于文件下载
     * @param uri
     * @param fpath
     * @param out
     * @param username
     * @throws IOException
     * @throws InterruptedException 
     * @author <a href="mailto:android_li@sina.cn">Joe</a>
     */
    public static void copyFileAsStream(String uri, String fpath, OutputStream out, String username) throws IOException, InterruptedException {
        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(URI.create(uri), conf, username);
        org.apache.hadoop.fs.Path filePath = new org.apache.hadoop.fs.Path(fpath);
        FSDataInputStream fsInput = hdfs.open(filePath);
        IOUtils.copyBytes(fsInput, out, 4096, false);
        fsInput.close();
        out.flush();
    }

    /**
     * 文件上传到hdfs
     * @param uri
     * @param inputStream
     * @param filepath
     * @param filename
     * @param username 拥有操作HDFS的用户名
     * @throws Exception 
     * @author <a href="mailto:android_li@sina.cn">Joe</a>
     */
    public static void createFile(String uri, InputStream inputStream, String filepath, String filename, String username) throws Exception {
        InputStream in = null;
        FSDataOutputStream out = null;
        String hdfsPath = uri+filepath+"/"+filename;
        try {
            Configuration conf = new Configuration();
            FileSystem fileSystem = FileSystem.get(URI.create(uri), conf, username);
            out = fileSystem.create(new Path(hdfsPath));
            in = new BufferedInputStream(inputStream);
            IOUtils.copyBytes(in, out, 4096, false);
            out.hsync();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            IOUtils.closeStream(in);
        }
    }
}

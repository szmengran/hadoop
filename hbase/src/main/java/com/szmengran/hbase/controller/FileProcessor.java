package com.szmengran.hbase.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.suntak.exception.model.Response;
import com.szmengran.hbase.entity.T_common_file;
import com.szmengran.hbase.exception.BusinessException;
import com.szmengran.hbase.service.HBaseService;

/**
 * @Package com.szmengran.controller
 * @Description: 文件上传与下载
 * @date 2018年9月27日 下午3:20:36
 * @author <a href="mailto:android_li@sina.cn">Joe</a>
 */
@RestController
@RequestMapping(value = "/api/v1/hbase")
public class FileProcessor {
	
	@Autowired
	private HBaseService hbaseService;
	
    @PostMapping(value = "/files/{userid}")
    @ResponseBody
    public Response uploadSmallerFiles(HttpServletRequest request, @PathVariable("userid") String userid) throws Exception {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        // this map is used to save the names of files and consistent row keys
        List<Map<String,Object>> mapInfo = new ArrayList<>();

        //record the number of failed file
        int failedFile = 0;

        if (files != null && files.size() > 0){
        	ExecutorService executor = Executors.newFixedThreadPool(5);
        	Future<T_common_file>[] futures = new Future[files.size()];
            for (int i = 0; i < files.size(); i++) {
            	MultipartFile file = files.get(i);
            	Future<T_common_file> future = executor.submit(() -> {
            		return hbaseService.upload(file, userid);
            	});
            	futures[i] = future;
            }
            T_common_file t_common_file = futures[0].get();
            System.out.println(t_common_file.getOrgname());
        }else {
            throw new BusinessException(30001001, "请检查是否上传了文件");
        }
        Map<String,Object> internalMap = new HashMap<>();
        internalMap.put("total",files.size());
        internalMap.put("error",failedFile);
        internalMap.put("data",mapInfo);
        Response response = new Response();
        response.setData(internalMap);
        return response;
    }
//
//
//    @GetMapping(value = "/api/file/download/smallerfiles/{id}")
//    public Response downloadSmallerFiles(@PathVariable("id")String rowkey, HttpServletResponse res){
//        //get connection Hbase
//        initHbase();
//        //get instance of table
//        Table table = null;
//
//        Map<String,Object> tempMap = new HashMap<>();
//        tempMap.put("id",rowkey);
//
//        try {
//            table = connectionHbase.getTable(TableName.valueOf(TABLENAME));
//        } catch (IOException e) {
//            System.out.println("获取数据库表的实例时出错");
//            e.printStackTrace();
//        }
//        //get files' bytes
//        Get get = new Get(Bytes.toBytes(rowkey));
//        Result result = null;
//        try {
//            result = table.get(get);
//        } catch (IOException e) {
//            System.out.println("找不到行键对应的文件");
//        }
//        //get file's name
//        String name = Bytes.toString(result.getValue(Bytes.toBytes("fileInfo"),Bytes.toBytes("file_name")));
//        tempMap.put("name",name);
//
//        //set header
//        res.setContentType("application/force-download");
//        res.setHeader("Content-Disposition", "attachment;fileName=" + name);
//
//        //get file's byte code
//        byte[] content = result.getValue(Bytes.toBytes("fileInfo"),Bytes.toBytes("file_content"));
//        try {
//            ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
//            OutputStream outputStream = res.getOutputStream();
//            byte[] buffer = new byte[1024];
//            int count;
//            while((count = inputStream.read(buffer)) != -1){
//                outputStream.write(buffer,0,count);
//            }
//            outputStream.flush();
//            outputStream.close();
//            inputStream.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        Map<String,Object> internalMap = new HashMap<>();
//        internalMap.put("data",tempMap);
//        Response response = new Response();
//        response.setData(internalMap);
//        return response;
//    }

}

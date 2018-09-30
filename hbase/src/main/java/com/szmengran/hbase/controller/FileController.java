package com.szmengran.hbase.controller;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.szmengran.hbase.service.FileService;

/**
 * @Package com.szmengran.controller
 * @Description: 文件上传与下载
 * @date 2018年9月27日 下午3:20:36
 * @author <a href="mailto:android_li@sina.cn">Joe</a>
 */
@RestController
@RequestMapping(value = "/api/v1/hbase")
public class FileController {
	
	@Autowired
	private FileService fileService;
	
    @PostMapping(value = "/files/{userid}")
    @ResponseBody
    public Response upload(HttpServletRequest request, @PathVariable("userid") String userid) throws Exception {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        // this map is used to save the names of files and consistent row keys
        List<Map<String,Object>> mapInfo = new ArrayList<>();

        //record the number of failed file
        int failedFile = 0;

        if (files != null && files.size() > 0){
        	ExecutorService executor = Executors.newFixedThreadPool(5);
        	@SuppressWarnings("unchecked")
			Future<T_common_file>[] futures = new Future[files.size()];
            for (int i = 0; i < files.size(); i++) {
            	MultipartFile file = files.get(i);
            	Future<T_common_file> future = executor.submit(() -> {
            		return fileService.upload(file, userid);
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
    


    @GetMapping(value = "/download/{fileid}")
    public void download(@PathVariable("fileid")String fileid, HttpServletRequest request, HttpServletResponse res) throws Exception{
    	T_common_file t_common_file = fileService.findById(fileid);
    	String filename = t_common_file.getOrgname();
    	if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
    		filename = URLEncoder.encode(filename, "UTF-8");
    	} else {	
    		filename = new String(filename.getBytes("UTF-8"), "ISO8859-1");
    	}
        res.setContentType("application/force-download");
        res.setHeader("Content-Disposition", "attachment;fileName=" + filename);

        OutputStream outputStream = res.getOutputStream();
        fileService.download(t_common_file.getPath(), outputStream, t_common_file.getFileid());
        outputStream.flush();
        outputStream.close();
    }

}

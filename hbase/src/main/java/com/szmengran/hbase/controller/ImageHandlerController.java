package com.szmengran.hbase.controller;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.szmengran.hbase.entity.T_common_file;
import com.szmengran.hbase.service.FileService;

/**
 * @Package com.szmengran.hbase.controller
 * @Description: TODO
 * @date 2018年10月21日 下午9:16:34
 * @author <a href="mailto:android_li@sina.cn">Joe</a>
 */
@RestController
@RequestMapping(value = "/api/v1/hbase")
public class ImageHandlerController {

	@Autowired
	private FileService fileService;
	
    @GetMapping(value = "/image/resize/{fileid}")
    public void download(@PathVariable("fileid")String fileid, HttpServletRequest request, HttpServletResponse response) throws Exception{
    	response.setContentType("image/jpeg");
    	T_common_file t_common_file = fileService.findById(fileid);
    	String strQuality = request.getParameter("quality"); //压缩比例
    	String rotate = request.getParameter("rotate"); //旋转度数
    	String strWidth = request.getParameter("width"); //宽度
    	String strHeight = request.getParameter("height"); //高度
        OutputStream outputStream = response.getOutputStream();
        try {
        	fileService.downloadImage(outputStream, t_common_file.getFileid(), strQuality, rotate, strWidth, strHeight);
        } finally {
        	outputStream.flush();
	        outputStream.close();
	        response.flushBuffer();
        }
    }
}

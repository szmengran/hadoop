package com.szmengran.hbase.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.imageio.ImageIO;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.szmengran.hbase.entity.T_common_file;
import com.szmengran.hbase.mapper.FileMapper;
import com.szmengran.hbase.service.FileService;
import com.szmengran.hbase.utils.HBaseConnectionPool;
import com.szmengran.hbase.utils.HdfsFileSystem;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

/**
 * @Package com.szmengran.hbase.service.impl
 * @Description: 文件服务
 * @date 2018年9月29日 下午4:10:19
 * @author <a href="mailto:android_li@sina.cn">Joe</a>
 */
@Service
public class FileServiceImpl implements FileService{
	
	@Value("${hbase.hdfs.host}")
	public String HDFS_HOST;
	
	@Value("${hbase.hdfs.username}")
	public String username;
	
	@Autowired
	private HBaseConnectionPool hbaseConnectionPool;
	
//	@Autowired
//	AbstractDao abstractDao;
	
	@Autowired
	private FileMapper fileMapper;
	
	@Override
	public T_common_file insert(T_common_file t_common_file) throws Exception {
		Timestamp createstamp = new Timestamp(System.currentTimeMillis());
		try {
			t_common_file.setCreatestamp(createstamp);
			t_common_file.setUpdatestamp(createstamp);
			t_common_file.setValidstatus("1");
			fileMapper.insert(t_common_file);
			return null;
		}catch(Exception e) {
			if(e.getMessage().contains("Duplicate entry")) {
				return fileMapper.findById(t_common_file);
			}else{
				throw e;
			}
		}
	}
	
	@Override
	public T_common_file findById(String fileid) throws Exception {
		T_common_file t_common_file = new T_common_file();
		t_common_file.setFileid(fileid);
		return fileMapper.findById(t_common_file);
	}

	@Override
	public T_common_file upload(MultipartFile file, String userid) throws Exception {
		InputStream inputStream = null;
		try {
			inputStream = file.getInputStream();
			String fileid = DigestUtils.md5Hex(file.getInputStream()); //计算MD5校验值
			String orgname = file.getOriginalFilename();
			long size = file.getSize();
			T_common_file t_common_file = new T_common_file();
            t_common_file.setFileid(fileid);
            t_common_file.setFilesize(size);
            t_common_file.setOrgname(orgname);
            String arr[] = orgname.split("\\.");
            String suffix = arr.length > 1 ? arr[arr.length-1] : "";
            t_common_file.setSuffix(suffix);
            t_common_file.setType(file.getContentType());
            t_common_file.setUserid(userid);
            T_common_file oldFile = findById(fileid); //查找是否有重复的文件
            if (oldFile == null) { //如果数据保存成功则表示没有重复的文件存在
            	String path = null;
            	if ((1<<20)*10 < size) { //hbase文件大小限制在10MB
            		path = uploadBigFile(file, fileid);
            	} else {
            		uploadSmallFile(file, fileid);
            	}
            	t_common_file.setPath(path);
            	insert(t_common_file);
            } else {
            	return oldFile;
            }
            return t_common_file;
		} catch (Exception e) {
			inputStream.close();
			throw e;
		}
	}
	
	/**
	 * 上传小文件的HBase
	 * @param file
	 * @param fileid
	 * @throws Exception 
	 * @author <a href="mailto:android_li@sina.cn">Joe</a>
	 */
	private void uploadSmallFile(MultipartFile file, String fileid) throws Exception {
		Connection connection = null;
		Table table = null;
		try {
			connection = hbaseConnectionPool.getConnection();
			table = connection.getTable(TableName.valueOf("file"));
			Put put = new Put(fileid.getBytes());
	        put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("data"), file.getBytes());
	        put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("type"), file.getContentType().getBytes());
	        table.put(put);
		} catch (Exception e) {
			throw e;
		} finally {
			table.close();
			connection.close();
		}
	}
	
	/**
	 * 上传大文件到HDFS，并返回文件路径
	 * @param file
	 * @param fileid
	 * @return
	 * @throws Exception 
	 * @author <a href="mailto:android_li@sina.cn">Joe</a>
	 */
	private String uploadBigFile(MultipartFile file, String fileid) throws Exception {
		StringBuffer path = new StringBuffer();
		Calendar calendar = Calendar.getInstance();
		path.append("/").append(calendar.get(Calendar.YEAR)).append("/").append(Calendar.MONTH+1).append("/").append(Calendar.DATE);
	    HdfsFileSystem.createFile(HDFS_HOST, file.getInputStream(), path.toString(), fileid, username);
	    return path.toString();
	}
	
	public void download(String path, OutputStream outputStream, String fileid) throws Exception {
		if (StringUtils.isBlank(path)) {
	        Table table = null;
			try {
				table = hbaseConnectionPool.getConnection().getTable(TableName.valueOf("file"));
				//get files' bytes
				Get get = new Get(Bytes.toBytes(fileid));
				Result result = null;
				try {
				  result = table.get(get);
				} catch (IOException e) {
				  throw new Exception("找不到行键对应的文件");
				}
				//get file's name
				byte[] content = result.getValue(Bytes.toBytes("info"),Bytes.toBytes("data"));
				outputStream.write(content);
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			} finally {
				table.close();
			}
		} else {
			HdfsFileSystem.copyFileAsStream(HDFS_HOST, path+"/"+fileid, outputStream, username);
		}
	}
	
	/**
	 * 根据指定的宽高输出图片
	 * @param outputStream
	 * @param fileid
	 * @param strWidth
	 * @param strHeight
	 * @throws Exception 
	 * @author <a href="mailto:android_li@sina.cn">Joe</a>
	 */
	public void downloadImage(OutputStream outputStream, String fileid, String strQuality, String rotate, String strWidth, String strHeight) throws Exception {
		Table table = null;
		BufferedImage image = null;
		InputStream inStream = null;
		try {
			table = hbaseConnectionPool.getConnection().getTable(TableName.valueOf("file"));
			//get files' bytes
			Get get = new Get(Bytes.toBytes(fileid));
			Result result = null;
			try {
				result = table.get(get);
			} catch (IOException e) {
				throw new Exception("找不到行键对应的文件");
			}
			Float quality = 1f;
			if (StringUtils.isNotBlank(strQuality)) {
				quality = Float.valueOf(strQuality);
			}
			
			Double rotateD = 0D;
			
			if (StringUtils.isNotBlank(rotate)) {
				rotateD = Double.parseDouble(rotate);
			}
			
			//get file's name
			byte[] content = result.getValue(Bytes.toBytes("info"), Bytes.toBytes("data"));
			byte[] typeByte = result.getValue(Bytes.toBytes("info"), Bytes.toBytes("type"));
			String type = "jpeg";
			if (typeByte != null) {
				type = new String(typeByte).toLowerCase().indexOf("png") > -1 ? "png" : "jpeg";
			}
			inStream = new ByteArrayInputStream(content);
			if (StringUtils.isBlank(strWidth) && StringUtils.isBlank(strHeight)) {
				Thumbnails.of(inStream).scale(1f).outputQuality(quality).rotate(rotateD).toOutputStream(outputStream);
				return;
			}
			image = ImageIO.read(inStream);
			int imageWidth = image.getWidth();  
			int imageHeitht = image.getHeight();
			if (StringUtils.isNotBlank(strWidth) && StringUtils.isNotBlank(strHeight)) { //有指定宽高 进行剪切
				Integer width = Integer.valueOf(strWidth);
				Integer height = Integer.valueOf(strHeight);
				if ((float)width / height != (float)imageWidth / imageHeitht) {  
				    if (imageWidth/width < imageHeitht/height) {  
				    	image = Thumbnails.of(image).width(width).asBufferedImage();  
				    } else {  
				    	image = Thumbnails.of(image).height(height).asBufferedImage();  
				    }  
				    Thumbnails.of(image).sourceRegion(Positions.CENTER, width, height).size(width, height).outputQuality(quality).rotate(rotateD).outputFormat(type).toOutputStream(outputStream);  
				}else {  
				    Thumbnails.of(image).size(width, height).outputQuality(quality).rotate(rotateD).outputFormat(type).toOutputStream(outputStream); 
				}
			} else if (StringUtils.isNotBlank(strWidth)) { //按宽比例缩放
				double scale=1f;
				Integer width = Integer.valueOf(strWidth);
				if (width < imageHeitht) {
					scale = width.doubleValue() / imageWidth;
				}
				Thumbnails.of(image).scale(scale).outputQuality(quality).rotate(rotateD).outputFormat(type).toOutputStream(outputStream);
			} else if (StringUtils.isNotBlank(strHeight)) { //按高比例缩放
				double scale=1f;
				Integer height = Integer.valueOf(strHeight);
				if(height < imageHeitht){
					scale = height.doubleValue() / imageHeitht;
				}
				Thumbnails.of(image).scale(scale).outputQuality(quality).rotate(rotateD).outputFormat(type).toOutputStream(outputStream);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			table.close();
			inStream.close();
			if (image != null) {
				image.flush();
			}
		}
	}
}

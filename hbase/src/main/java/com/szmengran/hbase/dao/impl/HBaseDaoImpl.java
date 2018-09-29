//package com.szmengran.hbase.dao.impl;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import org.apache.commons.codec.digest.DigestUtils;
//import org.apache.hadoop.hbase.Cell;
//import org.apache.hadoop.hbase.CellUtil;
//import org.apache.hadoop.hbase.CompareOperator;
//import org.apache.hadoop.hbase.TableName;
//import org.apache.hadoop.hbase.client.Delete;
//import org.apache.hadoop.hbase.client.Put;
//import org.apache.hadoop.hbase.client.Result;
//import org.apache.hadoop.hbase.client.ResultScanner;
//import org.apache.hadoop.hbase.client.Scan;
//import org.apache.hadoop.hbase.client.Table;
//import org.apache.hadoop.hbase.filter.Filter;
//import org.apache.hadoop.hbase.filter.FilterList;
//import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
//import org.apache.hadoop.hbase.util.Bytes;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.szmengran.hbase.dao.HBaseDao;
//import com.szmengran.hbase.entity.T_file_info;
//import com.szmengran.hbase.utils.HBaseConnectionPool;
//
///**
// * @Package com.szmengran.hbase.dao
// * @Description: Hbase操作工具类
// * @date 2018年9月21日 上午11:46:07
// * @author <a href="mailto:android_li@sina.cn">Joe</a>
// */
//@Component
//public class HBaseDaoImpl implements HBaseDao{
//
//	@Autowired
//	private HBaseConnectionPool hbaseConnectionPool;
//	private static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
//	            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
//	            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
//	            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
//	            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
//	            "W", "X", "Y", "Z" };
//	@Override
//	public void insert(String tableName, String rowId, String familyName, String qualifier, String value)
//			throws Exception {
//		Table table = null;
//		try {
//			table = HBaseConnectionPool.getInstance().getConnection().getTable(TableName.valueOf(tableName));
//			Put put = new Put(rowId.getBytes());// 一个PUT代表一行数据，再NEW一个PUT表示第二行数据,每行一个唯一的ROWKEY，此处rowkey为put构造方法中传入的值
//			put.addColumn(familyName.getBytes(), qualifier.getBytes(), value.getBytes());// 本行数据的第一列
//				table.put(put);
//		} catch (IOException e) {
//			throw e;
//		} finally {
//			table.close();
//		}
//	}
//	
//	@Override
//	public void insert(String tableName, String rowId, String familyName, String qualifier, byte[] value)
//			throws Exception {
//		Table table = null;
//		try {
//			table = HBaseConnectionPool.getInstance().getConnection().getTable(TableName.valueOf(tableName));
//			Put put = new Put(rowId.getBytes());// 一个PUT代表一行数据，再NEW一个PUT表示第二行数据,每行一个唯一的ROWKEY，此处rowkey为put构造方法中传入的值
//			put.addColumn(familyName.getBytes(), qualifier.getBytes(), value);// 本行数据的第一列
//			table.put(put);
//		} catch (IOException e) {
//			throw e;
//		} finally {
//			table.close();
//		}
//	}
//	
//	@Override
//	public void deleteByRowId(String tablename, String rowkey) throws Exception{
//		Table table = null;
//		try {
//			table = HBaseConnectionPool.getInstance().getConnection().getTable(TableName.valueOf(tablename));
//			Delete d1 = new Delete(rowkey.getBytes());
//			table.delete(d1);// d1.addColumn(family, qualifier);d1.addFamily(family);
//			System.out.println("删除行成功!");
//		} catch (IOException e) {
//			throw e;
//		} finally {
//			table.close();
//		}
//	}
//	
//	/**
//	 * 根据ID查找数据
//	 * @param tableName
//	 * @param rowId
//	 * @throws Exception 
//	 * @author <a href="mailto:android_li@sina.cn">Joe</a>
//	 */
////	public static <T> T findByRowId(T object, String rowId) throws Exception {
////		Table table = null;
////		try {
////			table = HBaseConnectionPool.getConnection().getTable(TableName.valueOf(object.getClass().getSimpleName().toUpperCase()));
////			Get scan = new Get(rowId.getBytes());// 根据rowkey查询
////			Result r = table.get(scan);
////			Map<String, Method> mapGet = ReflectHandler.getFieldAndGetMethodFromObject(object);
////			for (Cell key : r.rawCells()) {
////				System.out.println("列：" + new String(CellUtil.cloneFamily(key)) + ":"
////						+ new String(CellUtil.cloneQualifier(key)) + "====值:"
////						+ new String(CellUtil.cloneValue(key)));
////				List<String> idFields = dbManager.getPrimaryKeys(object);
////				for (int i = 0; i < idFields.size(); i++) {
////					String field = idFields.get(i).toUpperCase();
////					if (i == 0) {
////						sb.append("  " + field + " = ?");
////					} else {
////						sb.append(" AND " + field + " = ?");
////					}
////				}
////				dbManager.prepareStatement(sb.toString());
////				int index = 1;
////				Map<String, Method> mapGet = ReflectHandler.getFieldAndGetMethodFromObject(object);
////				for (int i = 0; i < idFields.size(); i++) {
////					Method method = mapGet.get(idFields.get(i).toUpperCase());
////					Object value = method.invoke(object);
////					dbManager.setPrepareParameters(index++, value);
////				}
////				Object obj = object.getClass().newInstance();
////				dbManager.executePrepareQuery();
////				if (dbManager.next()) {
////					dbManager.setObjectValueByField(obj);
////				} else {
////					return null;
////				}
////				return (T)obj;
////			}
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
////	}
////	
//	private String generateRowkey(String filename){
//        String rowkey = "";
//        //get current time
//        String currentTime = String.valueOf(System.currentTimeMillis());
//        // get a random number
//        StringBuffer shortBuffer = new StringBuffer();
//        String uuid = UUID.randomUUID().toString().replace("-", "");
//        for (int i = 0; i < 8; i++) {
//            String str = uuid.substring(i * 4, i * 4 + 4);
//            int x = Integer.parseInt(str, 16);
//            shortBuffer.append(chars[x % 0x3E]);
//        }
//
//        //get suffix
//        String suffix = filename.substring(filename.lastIndexOf(".") + 1);
//
//        // generate a rowkey
//        rowkey = suffix + "(@_@)" + currentTime + "(@_@)" + shortBuffer.toString();
//        return rowkey;
//    }
//	
//	@Override
//	public T_file_info upload(MultipartFile file) throws Exception {
//		Table table = null;
//		try {
//			String orgname = file.getOriginalFilename();
//			byte[] fileBytes = file.getBytes();
//            String fileid = DigestUtils.md5Hex(fileBytes);
//			table = HBaseConnectionPool.getInstance().getConnection().getTable(TableName.valueOf("file"));
//			Put put = new Put(fileid.getBytes());
//			put.addColumn(Bytes.toBytes("fileInfo"), Bytes.toBytes("fileid"), Bytes.toBytes(fileid));
//            put.addColumn(Bytes.toBytes("fileInfo"), Bytes.toBytes("content"), fileBytes);
//            T_file_info t_file_info = new T_file_info();
//            t_file_info.setFileid(fileid);
//            t_file_info.setSize(file.getSize());
//            t_file_info.setOrgname(orgname);
//            file.getContentType();
//            String arr[] = orgname.split("\\.");
//            String suffix = arr.length > 1 ? arr[arr.length-1] : "";
//            t_file_info.setSuffix(suffix);
//            t_file_info.setType(file.getContentType());
//            return t_file_info;
//		} catch (Exception e) {
//			throw e;
//		} finally {
//			table.close();
//		}
//	}
//	
//	@Override
//	public void findByConditions(String tableName, String familyName, String qualifier, String value) {
//
//		try {
//			Table table = HBaseConnectionPool.getInstance().getConnection().getTable(TableName.valueOf(tableName));
//			Filter filter = new SingleColumnValueFilter(Bytes.toBytes(familyName), Bytes.toBytes(qualifier),
//					CompareOperator.EQUAL, Bytes.toBytes(value)); // 当列familyName的值为value时进行查询
//			Scan s = new Scan();
//			s.setFilter(filter);
//			ResultScanner rs = table.getScanner(s);
//			for (Result r : rs) {
//				System.out.println("获得到rowkey:" + new String(r.getRow()));
//				for (Cell keyValue : r.rawCells()) {
//					System.out.println("列：" + new String(CellUtil.cloneFamily(keyValue)) + ":"
//							+ new String(CellUtil.cloneQualifier(keyValue)) + "====值:"
//							+ new String(CellUtil.cloneValue(keyValue)));
//				}
//			}
//			rs.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	@Override
//	public void findByConditions(String tableName, String[] familyNames, String[] qualifiers, String[] values) {
//
//		try {
//			Table table = HBaseConnectionPool.getInstance().getConnection().getTable(TableName.valueOf(tableName));
//			List<Filter> filters = new ArrayList<Filter>();
//			if (familyNames != null && familyNames.length > 0) {
//				int i = 0;
//				for (String familyName : familyNames) {
//					Filter filter = new SingleColumnValueFilter(Bytes.toBytes(familyName), Bytes.toBytes(qualifiers[i]),
//							CompareOperator.EQUAL, Bytes.toBytes(values[i]));
//					filters.add(filter);
//					i++;
//				}
//			}
//			FilterList filterList = new FilterList(filters);
//			Scan scan = new Scan();
//			scan.setFilter(filterList);
//			ResultScanner rs = table.getScanner(scan);
//			for (Result r : rs) {
//				System.out.println("获得到rowkey:" + new String(r.getRow()));
//				for (Cell keyValue : r.rawCells()) {
//					System.out.println("列：" + new String(CellUtil.cloneFamily(keyValue)) + ":"
//							+ new String(CellUtil.cloneQualifier(keyValue)) + "====值:"
//							+ new String(CellUtil.cloneValue(keyValue)));
//				}
//			}
//			rs.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
////	public <T> List<T> findByConditions(DBManager dbManager, Class<T> clazz, String strSql, Object[] params,
////			Integer startRow, Integer pageSize) throws SQLException, NoSuchMethodException, SecurityException,
////			IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
////		List<T> list = null;
////		if (pageSize != null) { // 如果pageSize!=null说明传进来的参数有值，表示需要分页
////			dbManager.prepareStatement(getPageSql(strSql, startRow, pageSize));
////		} else {
////			dbManager.prepareStatement(strSql);
////		}
////		if (params != null) {
////			int index = 1;
////			for (Object value : params) {
////				dbManager.setPrepareParameters(index++, value);
////			}
////		}
////		dbManager.executePrepareQuery();
////		while (dbManager.next()) {
////			if (list == null) {
////				list = new ArrayList<T>();
////			}
////			list.add((T) dbManager.setObjectValueByField(clazz.newInstance()));
////		}
////		return list;
////	}
//}

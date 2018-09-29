package com.szmengran.hbase.dao;

import org.springframework.web.multipart.MultipartFile;

import com.szmengran.hbase.entity.T_common_file;

/**
 * @Package com.szmengran.hbase.dao
 * @Description: TODO
 * @date 2018年9月28日 上午8:36:23
 * @author <a href="mailto:android_li@sina.cn">Joe</a>
 */
public interface HBaseDao {
	
	/**
	 * 添加行列数据数据
	 * @param tableName
	 * @param rowId
	 * @param familyName
	 * @param qualifier
	 * @param value
	 * @throws Exception 
	 * @author <a href="mailto:android_li@sina.cn">Joe</a>
	 */
	public void insert(String tableName, String rowId, String familyName, String qualifier, String value) throws Exception;
	
	/**
	 * 添加行列数据数据
	 * @param tableName
	 * @param rowId
	 * @param familyName
	 * @param qualifier
	 * @param value
	 * @throws Exception 
	 * @author <a href="mailto:android_li@sina.cn">Joe</a>
	 */
	public void insert(String tableName, String rowId, String familyName, String qualifier, byte[] value) throws Exception;
	
	/**
	 * 删除行
	 * @param tablename
	 * @param rowkey 
	 * @author <a href="mailto:android_li@sina.cn">Joe</a>
	 */
	public void deleteByRowId(String tablename, String rowkey) throws Exception;
	/**
	 * 根据ID查找数据
	 * @param tableName
	 * @param rowId
	 * @throws Exception 
	 * @author <a href="mailto:android_li@sina.cn">Joe</a>
	 */
//	public static <T> T findByRowId(T object, String rowId) throws Exception {
//		Table table = null;
//		try {
//			table = HBaseConnectionPool.getConnection().getTable(TableName.valueOf(object.getClass().getSimpleName().toUpperCase()));
//			Get scan = new Get(rowId.getBytes());// 根据rowkey查询
//			Result r = table.get(scan);
//			Map<String, Method> mapGet = ReflectHandler.getFieldAndGetMethodFromObject(object);
//			for (Cell key : r.rawCells()) {
//				System.out.println("列：" + new String(CellUtil.cloneFamily(key)) + ":"
//						+ new String(CellUtil.cloneQualifier(key)) + "====值:"
//						+ new String(CellUtil.cloneValue(key)));
//				List<String> idFields = dbManager.getPrimaryKeys(object);
//				for (int i = 0; i < idFields.size(); i++) {
//					String field = idFields.get(i).toUpperCase();
//					if (i == 0) {
//						sb.append("  " + field + " = ?");
//					} else {
//						sb.append(" AND " + field + " = ?");
//					}
//				}
//				dbManager.prepareStatement(sb.toString());
//				int index = 1;
//				Map<String, Method> mapGet = ReflectHandler.getFieldAndGetMethodFromObject(object);
//				for (int i = 0; i < idFields.size(); i++) {
//					Method method = mapGet.get(idFields.get(i).toUpperCase());
//					Object value = method.invoke(object);
//					dbManager.setPrepareParameters(index++, value);
//				}
//				Object obj = object.getClass().newInstance();
//				dbManager.executePrepareQuery();
//				if (dbManager.next()) {
//					dbManager.setObjectValueByField(obj);
//				} else {
//					return null;
//				}
//				return (T)obj;
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
	/**
	 * 文件上传
	 * @param file
	 * @return
	 * @throws Exception 
	 * @author <a href="mailto:android_li@sina.cn">Joe</a>
	 */
	public T_common_file upload(MultipartFile file) throws Exception;
	
	/**
	 * 根据列条件查询
	 * 
	 * @param tableName
	 */
	public void findByConditions(String tableName, String familyName, String qualifier, String value) throws Exception;

	/**
	 * 多条件查询
	 * 
	 * @param tableName
	 */
	public void findByConditions(String tableName, String[] familyNames, String[] qualifiers, String[] values) throws Exception;
}

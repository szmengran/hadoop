package com.szmengran.hbase.dao;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

import com.szmengran.common.orm.DBManager;
import com.szmengran.common.reflect.ReflectHandler;
import com.szmengran.hbase.utils.HBaseConnectionPool;

/**
 * @Package com.szmengran.hbase.dao
 * @Description: TODO
 * @date 2018年9月21日 上午11:46:07
 * @author <a href="mailto:android_li@sina.cn">Joe</a>
 */
@Service
public class HBaseDao {
	
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
	public static void insert(String tableName, String rowId, String familyName, String qualifier, String value)
			throws Exception {
		Table table = null;
		try {
			table = HBaseConnectionPool.getConnection().getTable(TableName.valueOf(tableName));
			Put put = new Put(rowId.getBytes());// 一个PUT代表一行数据，再NEW一个PUT表示第二行数据,每行一个唯一的ROWKEY，此处rowkey为put构造方法中传入的值
			put.addColumn(familyName.getBytes(), qualifier.getBytes(), value.getBytes());// 本行数据的第一列
				table.put(put);
		} catch (IOException e) {
			throw e;
		} finally {
			table.close();
		}
	}
	
	/**
	 * 删除行
	 * @param tablename
	 * @param rowkey 
	 * @author <a href="mailto:android_li@sina.cn">Joe</a>
	 */
	public static void deleteByRowId(String tablename, String rowkey) throws Exception{
		Table table = null;
		try {
			table = HBaseConnectionPool.getConnection().getTable(TableName.valueOf(tablename));
			Delete d1 = new Delete(rowkey.getBytes());
			table.delete(d1);// d1.addColumn(family, qualifier);d1.addFamily(family);
			System.out.println("删除行成功!");
		} catch (IOException e) {
			throw e;
		} finally {
			table.close();
		}
	}
	
	/**
	 * 根据ID查找数据
	 * @param tableName
	 * @param rowId
	 * @throws Exception 
	 * @author <a href="mailto:android_li@sina.cn">Joe</a>
	 */
	public static <T> T findByRowId(T object, String rowId) throws Exception {
		Table table = null;
		try {
			table = HBaseConnectionPool.getConnection().getTable(TableName.valueOf(object.getClass().getSimpleName().toUpperCase()));
			Get scan = new Get(rowId.getBytes());// 根据rowkey查询
			Result r = table.get(scan);
			Map<String, Method> mapGet = ReflectHandler.getFieldAndGetMethodFromObject(object);
			for (Cell key : r.rawCells()) {
				System.out.println("列：" + new String(CellUtil.cloneFamily(key)) + ":"
						+ new String(CellUtil.cloneQualifier(key)) + "====值:"
						+ new String(CellUtil.cloneValue(key)));
				List<String> idFields = dbManager.getPrimaryKeys(object);
				for (int i = 0; i < idFields.size(); i++) {
					String field = idFields.get(i).toUpperCase();
					if (i == 0) {
						sb.append("  " + field + " = ?");
					} else {
						sb.append(" AND " + field + " = ?");
					}
				}
				dbManager.prepareStatement(sb.toString());
				int index = 1;
				Map<String, Method> mapGet = ReflectHandler.getFieldAndGetMethodFromObject(object);
				for (int i = 0; i < idFields.size(); i++) {
					Method method = mapGet.get(idFields.get(i).toUpperCase());
					Object value = method.invoke(object);
					dbManager.setPrepareParameters(index++, value);
				}
				Object obj = object.getClass().newInstance();
				dbManager.executePrepareQuery();
				if (dbManager.next()) {
					dbManager.setObjectValueByField(obj);
				} else {
					return null;
				}
				return (T)obj;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据列条件查询
	 * 
	 * @param tableName
	 */
	public static void findByConditions(String tableName, String familyName, String qualifier, String value) {

		try {
			Table table = HBaseConnectionPool.getConnection().getTable(TableName.valueOf(tableName));
			Filter filter = new SingleColumnValueFilter(Bytes.toBytes(familyName), Bytes.toBytes(qualifier),
					CompareOperator.EQUAL, Bytes.toBytes(value)); // 当列familyName的值为value时进行查询
			Scan s = new Scan();
			s.setFilter(filter);
			ResultScanner rs = table.getScanner(s);
			for (Result r : rs) {
				System.out.println("获得到rowkey:" + new String(r.getRow()));
				for (Cell keyValue : r.rawCells()) {
					System.out.println("列：" + new String(CellUtil.cloneFamily(keyValue)) + ":"
							+ new String(CellUtil.cloneQualifier(keyValue)) + "====值:"
							+ new String(CellUtil.cloneValue(keyValue)));
				}
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 多条件查询
	 * 
	 * @param tableName
	 */
	public static void findByConditions(String tableName, String[] familyNames, String[] qualifiers, String[] values) {

		try {
			Table table = HBaseConnectionPool.getConnection().getTable(TableName.valueOf(tableName));
			List<Filter> filters = new ArrayList<Filter>();
			if (familyNames != null && familyNames.length > 0) {
				int i = 0;
				for (String familyName : familyNames) {
					Filter filter = new SingleColumnValueFilter(Bytes.toBytes(familyName), Bytes.toBytes(qualifiers[i]),
							CompareOperator.EQUAL, Bytes.toBytes(values[i]));
					filters.add(filter);
					i++;
				}
			}
			FilterList filterList = new FilterList(filters);
			Scan scan = new Scan();
			scan.setFilter(filterList);
			ResultScanner rs = table.getScanner(scan);
			for (Result r : rs) {
				System.out.println("获得到rowkey:" + new String(r.getRow()));
				for (Cell keyValue : r.rawCells()) {
					System.out.println("列：" + new String(CellUtil.cloneFamily(keyValue)) + ":"
							+ new String(CellUtil.cloneQualifier(keyValue)) + "====值:"
							+ new String(CellUtil.cloneValue(keyValue)));
				}
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public <T> List<T> findByConditions(DBManager dbManager, Class<T> clazz, String strSql, Object[] params,
			Integer startRow, Integer pageSize) throws SQLException, NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		List<T> list = null;
		if (pageSize != null) { // 如果pageSize!=null说明传进来的参数有值，表示需要分页
			dbManager.prepareStatement(getPageSql(strSql, startRow, pageSize));
		} else {
			dbManager.prepareStatement(strSql);
		}
		if (params != null) {
			int index = 1;
			for (Object value : params) {
				dbManager.setPrepareParameters(index++, value);
			}
		}
		dbManager.executePrepareQuery();
		while (dbManager.next()) {
			if (list == null) {
				list = new ArrayList<T>();
			}
			list.add((T) dbManager.setObjectValueByField(clazz.newInstance()));
		}
		return list;
	}
}

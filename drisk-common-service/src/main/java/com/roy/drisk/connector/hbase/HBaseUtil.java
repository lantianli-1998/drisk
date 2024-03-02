package com.roy.drisk.connector.hbase;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 */
public class HBaseUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(HBaseUtil.class);
	public static final String DEFAUTL_ENCODING = "UTF-8";
	private Charset charset;
	private HBaseConnection connection;

	public HBaseUtil(HBaseConnection connection) {
		this(connection, DEFAUTL_ENCODING);
	}

	public HBaseUtil(HBaseConnection connection, String encoding) {
		this.connection = connection;
		this.charset = Charset.forName(encoding);
	}

	public Charset getCharset() {
		return charset;
	}

	public HBaseConnection getConnection() {
		return connection;
	}

	private Table getTable(String tableName) throws IOException {
		return getConnection().getTable(TableName.valueOf(tableName.getBytes(getCharset())));
	}

	private void releaseTable(Table table) {
		if (table == null)
			return;
		try {
			table.close();
		} catch (IOException e) {
			LOGGER.error("HBaseUtilException", e);
		}
	}

	public <T> T execute(String tableName, TableCallback<T> action) {
		Table table = null;
		try {
			table = getTable(tableName);
			return action.doInTable(table);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			releaseTable(table);
		}
	}

	public <T> T get(String tableName, String rowName, final RowMapper<T> mapper) {
		return get(tableName, rowName, null, null, mapper);
	}

	public <T> T get(String tableName, String rowName, String familyName, final RowMapper<T> mapper) {
		return get(tableName, rowName, familyName, null, mapper);
	}

	public <T> T get(String tableName, final String rowName, final String familyName, final String qualifier,
			final RowMapper<T> mapper) {
		return execute(tableName, htable -> {
			Get get = new Get(rowName.getBytes(getCharset()));
			if (familyName != null) {
				byte[] family = familyName.getBytes(getCharset());
				if (qualifier != null) {
					get.addColumn(family, qualifier.getBytes(getCharset()));
				} else {
					get.addFamily(family);
				}
			}
			Result result = htable.get(get);
			return mapper.mapRow(result, 0);
		});
	}

	public Result getMultiVersion(String tableName, String rowName, int version) {
		return execute(tableName, htable -> {
			Get get = new Get(rowName.getBytes(getCharset()));
			get.setMaxVersions(version);
			return htable.get(get);
		});
	}

	public Result getMultiVersion(String tableName, String family, String rowName, Map<String, String> map,
			int version) {
		return execute(tableName, htable -> {
			Get get = new Get(rowName.getBytes(getCharset()));
			get.setMaxVersions(version);
			FilterList filterList = new FilterList();
			if (map != null) {
				Set<String> keySet = map.keySet();
				Iterator<String> it = keySet.iterator();
				while (it.hasNext()) {
					String key = it.next();
					String val = map.get(key);
					Filter filter = new SingleColumnValueFilter(Bytes.toBytes(family), Bytes.toBytes(key),
							CompareOp.EQUAL, Bytes.toBytes(val));
					filterList.addFilter(filter);
				}
			}
			get.setFilter(filterList);
			return htable.get(get);
		});
	}

	public <T> T find(String tableName, String family, final ResultsExtractor<T> action) {
		Scan scan = new Scan();
		scan.addFamily(family.getBytes(getCharset()));
		return find(tableName, scan, action);
	}

	public <T> T find(String tableName, String family, String qualifier, final ResultsExtractor<T> action) {
		Scan scan = new Scan();
		scan.addColumn(family.getBytes(getCharset()), qualifier.getBytes(getCharset()));
		return find(tableName, scan, action);
	}

	public <T> T find(String tableName, final Scan scan, final ResultsExtractor<T> action) {
		return execute(tableName, htable -> {
			ResultScanner scanner = htable.getScanner(scan);
			try {
				return action.extractData(scanner);
			} finally {
				scanner.close();
			}
		});
	}

	public <T> List<T> find(String tableName, String family, final RowMapper<T> action) {
		Scan scan = new Scan();
		scan.addFamily(family.getBytes(getCharset()));
		return find(tableName, scan, action);
	}

	public <T> List<T> find(String tableName, String family, String qualifier, final RowMapper<T> action) {
		Scan scan = new Scan();
		scan.addColumn(family.getBytes(getCharset()), qualifier.getBytes(getCharset()));
		return find(tableName, scan, action);
	}

	public <T> List<T> find(String tableName, final Scan scan, final RowMapper<T> action) {
		return find(tableName, scan, new RowMapperResultsExtractor<>(action));
	}

	public void put(String tableName, final String rowName, final String familyName, final String qualifier,
			final byte[] value) {
		execute(tableName, htable -> {
			Put put = new Put(rowName.getBytes(getCharset()));
			put.addColumn(familyName.getBytes(getCharset()), qualifier.getBytes(getCharset()), value);
			htable.put(put);
			return null;
		});
	}

	public void put(String tableName, final String rowName, Map<String, Map<String, String>> families) {
		execute(tableName, htable -> {
			Put put = new Put(rowName.getBytes(getCharset()));
			families.forEach((familyName, qualifiers) -> qualifiers
					.forEach((qualifier, value) -> put.addColumn(familyName.getBytes(getCharset()),
							qualifier.getBytes(getCharset()), value.getBytes(getCharset()))));
			htable.put(put);
			return null;
		});
	}

	public void delete(String tableName, final String rowName, final String familyName) {
		delete(tableName, rowName, familyName, null);
	}

	public void delete(String tableName, final String rowName, final String familyName, final String qualifier) {
		execute(tableName, htable -> {
			Delete delete = new Delete(rowName.getBytes(getCharset()));
			byte[] family = familyName.getBytes(getCharset());
			if (qualifier != null) {
				delete.addColumn(family, qualifier.getBytes(getCharset()));
			} else {
				delete.addFamily(family);
			}
			htable.delete(delete);
			return null;
		});
	}

	public void deleteAll(String tableName, final String rowName) {
		execute(tableName, htable -> {
			Delete delete = new Delete(rowName.getBytes(getCharset()));
			htable.delete(delete);
			return null;
		});
	}

	// 删除指定列族中所有列的时间戳等于指定时间戳的版本数据。
	public void deleteFamilyVersion(String tableName, String family, String rowName, long timestamp) {
		execute(tableName, htable -> {
			Delete delete = new Delete(rowName.getBytes(getCharset()));
			delete.deleteFamilyVersion(Bytes.toBytes(family), timestamp);
			htable.delete(delete);
			return null;
		});
	}

	/**
	 * 根据指定列查找数据是否存在
	 * @param tableName表名
	 * @param family列簇名
	 * @param rowName rowkey
	 * @param map 指定列的参数值集合
	 * @param CheckExistenceOnly 校验是否存在
	 * @return boolean true存在 false 不存在
	 */
	public Result isExsistData(String tableName, String family, String rowName, Map<String, String> map,boolean CheckExistenceOnly ) {
		return execute(tableName, htable -> {
			Get get = new Get(rowName.getBytes(getCharset()));
			FilterList filterList = new FilterList();
			if (map != null) {
				Set<String> keySet = map.keySet();
				Iterator<String> it = keySet.iterator();
				while (it.hasNext()) {
					String key = it.next();
					String val = map.get(key);
					Filter filter = new SingleColumnValueFilter(Bytes.toBytes(family), Bytes.toBytes(key),
							CompareOp.EQUAL, Bytes.toBytes(val));
					filterList.addFilter(filter);
					get.setFilter(filterList);
				}
			}
			if (CheckExistenceOnly) {
				get.setCheckExistenceOnly(true);
			}
			Result result = htable.get(get);
			return result;
		});
	}

}

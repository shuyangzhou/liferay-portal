package com.liferay.portal.upgrade;

import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBInspector;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.db.DBType;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.InputStream;
import java.sql.SQLException;

public class UpgradeTestUtil {

	public static boolean hasColumn(String tableName, String columnName)
		throws Exception {

		return _dbInspector.hasColumn(tableName, columnName);
	}

	public static boolean hasTable(String tableName) throws Exception {
		return _dbInspector.hasTable(tableName);
	}

	public static void populateDB(Class clazz) throws Exception {
		DB db = DBManagerUtil.getDB();

		DBType dbType = db.getDBType();

		InputStream is = clazz.getResourceAsStream(
			"dependencies/portal-tables-" + dbType.getName() + ".sql");

		db.runSQLTemplateString(StringUtil.read(is), false, false);

		is = clazz.getResourceAsStream(
			"dependencies/indexes-" + dbType.getName() + ".sql");

		db.runSQLTemplateString(StringUtil.read(is), false, false);
	}

	private static DBInspector _dbInspector;

	static {
		try {
			_dbInspector = new DBInspector(DataAccess.getConnection());
		}
		catch (SQLException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

}

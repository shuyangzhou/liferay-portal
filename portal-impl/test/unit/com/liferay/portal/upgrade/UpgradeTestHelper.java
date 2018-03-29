package com.liferay.portal.upgrade;

import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import java.sql.Connection;
import java.sql.SQLException;

public class UpgradeTestHelper extends UpgradeProcess {
	@Override
	protected void doUpgrade() throws Exception {
		throw new UnsupportedOperationException();
	}

	public boolean hasColumn(String tableName, String columnName)
		throws Exception {

		return super.hasColumn(tableName, columnName);
	}

	public boolean hasTable(String tableName) throws Exception {
		return super.hasTable(tableName);
	}

	public UpgradeTestHelper(Connection connection)
		throws SQLException {

		this.connection = connection;
	}
}

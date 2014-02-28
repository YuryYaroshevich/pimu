package thomsonreuters.news.swift.metadata.pimu.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import thomsonreuters.news.swift.metadata.pimu.bcploadingscript.BCPLoadingScript;

public class BCPDataLoader extends DbContext {

	private String mappingTable1 = "pimu_mapping1";
	private String mappingTable2 = "pimu_mapping2";
	private String statusTable = "pimu_status";

	public void loadBCP(String bcpPath, BCPLoadingScript script)
			throws Exception {
		Connection connection = openConnection();
		Statement statement = null;
		ResultSet rs = null;
		connection.setAutoCommit(false);
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(10);
			// what table is inactive now?
			System.out.println(new Date()
					+ " ---find out what table is inactive");
			rs = statement.executeQuery(String.format(
					"select edcoidtable from %s where status = 'I'",
					statusTable));
			rs.next();
			String inactiveTableName = rs.getString("edcoidtable");
			System.out.println(new Date() + " ---clean inactive table - "
					+ inactiveTableName);
			statement.executeUpdate(String.format("delete from %s",
					inactiveTableName));
			System.out.println(new Date() + " ---run script");
			script.run(inactiveTableName);
			// change statuses
			System.out.println(new Date() + " ---update status");
			statement.executeUpdate(String.format(
					"update md.status set status='I' where edcoidtable = '%s'",
					getActiveTableName(inactiveTableName)));
			statement.executeUpdate(String.format(
					"update md.status set status='A' where edcoidtable = '%s'",
					inactiveTableName));

			connection.commit();
		} finally {
			connection.rollback();
			closeResources(statement, rs, connection);
		}
	}

	private String getActiveTableName(String inactiveTableName) {
		if (mappingTable1.equals(inactiveTableName)) {
			return mappingTable2;
		} else {
			return mappingTable1;
		}
	}

	private static void closeResources(AutoCloseable... resources)
			throws Exception {
		for (AutoCloseable res : resources) {
			if (res != null) {
				res.close();
			}
		}
	}
}

package thomsonreuters.news.swift.metadata.pimu.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import thomsonreuters.news.swift.metadata.pimu.bcploadingscript.LoadingScript;
import thomsonreuters.news.swift.metadata.pimu.config.Properties;
import thomsonreuters.news.swift.metadata.pimu.validation.MappingValidator;

public class DataLoader extends DbContext {
	private String mappingTable1;
	private String mappingTable2;
	private String statusTable;

	private int attemptsNum;

	private MappingValidator validator;

	private static Logger logger = LoggerFactory.getLogger(DataLoader.class);

	public DataLoader(Properties props) {
		super(props.getDbDriverName(), props.getDbConnectionString(), props
				.getDbUser(), props.getDbPassword());

		this.mappingTable1 = props.getDbMappingTable1();
		this.mappingTable2 = props.getDbMappingTable2();
		this.statusTable = props.getDbStatusTable();
		this.attemptsNum = props.getDbAttemptsNumber();

		this.validator = new MappingValidator(props.getDbMappingValidRatio());
	}

	public void insertFileDataInDb(LoadingScript script, String version)
			throws Exception {
		Connection connection = null;
		try {
			connection = openConnection(attemptsNum);
			connection.setAutoCommit(false);

			doInserting(connection, script, version);

			connection.commit();
		} catch (SQLException e) {
			attemptsNum--;
			if (attemptsNum > 0) {
				logger.error(
						String.format(
								"Couldn't finish inserting data from bcp file in database. Will try %s more times.",
								attemptsNum), e);
				doInserting(connection, script, version);
			}
			connection.rollback();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}

	private void doInserting(Connection connection, LoadingScript script,
			String version) throws Exception {
		String inactiveTable = fetchInactiveTable(connection);
		logger.info(String.format("%s is currently inactive table.",
				inactiveTable));

		cleanInactiveTable(connection, inactiveTable);
		logger.info(String.format("%s was cleaned.", inactiveTable));

		Process proc = script.run(inactiveTable);
		eatProcessStreams(proc);
		if (proc.waitFor() != 0) {
			logger.error("Script was not terminated normal.");
			throw new SQLException("Script was not terminated normal.");
		}

		int inactiveTableRowsNum = countRowsIn(inactiveTable, connection);
		int activeTableRowsNum = countRowsIn(getActiveTable(inactiveTable),
				connection);
		logger.info(String
				.format("Inactive table - %s, number of rows = %s. Active table - %s, number of rows = %s.",
						inactiveTable, inactiveTableRowsNum,
						getActiveTable(inactiveTable), activeTableRowsNum));
		boolean isValid = validator.isMappingValid(inactiveTableRowsNum,
				activeTableRowsNum);

		if (isValid) {
			changeStatuses(inactiveTable, connection, version);
			logger.info("Statuses were changed.");
		}
	}

	private String fetchInactiveTable(Connection connection)
			throws SQLException {
		ResultSet rs = null;
		try (Statement statement = connection.createStatement()) {
			rs = statement.executeQuery(String.format(
					"select edcoidtable from %s where status = 'I'",
					statusTable));
			rs.next();
			return rs.getString("edcoidtable");
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	private void cleanInactiveTable(Connection connection, String inactiveTable)
			throws SQLException {
		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(String.format("delete from %s",
					inactiveTable));
		}
	}

	private int countRowsIn(String tableName, Connection connection)
			throws SQLException {
		ResultSet rs = null;
		try (Statement statement = connection.createStatement()) {
			rs = statement.executeQuery(String.format(
					"select count(*) from %s", tableName));
			rs.next();
			return rs.getInt(1);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	private void changeStatuses(String inactiveTable, Connection connection,
			String version) throws SQLException {
		try (PreparedStatement statementForActive = connection
				.prepareStatement(String
						.format("update %s set status=?, refresh_time=? where edcoidtable = ?",
								statusTable));
				PreparedStatement statementForInactive = connection
						.prepareStatement(String
								.format("update %s set status=?, refresh_time=?, version=? where edcoidtable = ?",
										statusTable));) {
			statementForActive.setString(1, "I");
			statementForActive.setDate(2, new Date(System.currentTimeMillis()));
			statementForActive.setString(3, getActiveTable(inactiveTable));
			statementForActive.executeUpdate();

			statementForInactive.setString(1, "A");
			statementForInactive.setDate(2,
					new Date(System.currentTimeMillis()));
			statementForInactive.setString(3, version);
			statementForInactive.setString(4, inactiveTable);
			statementForInactive.executeUpdate();
		}
	}

	private String getActiveTable(String inactiveTableName) {
		if (mappingTable1.equals(inactiveTableName)) {
			return mappingTable2;
		} else {
			return mappingTable1;
		}
	}

	private static void eatProcessStreams(Process proc) {
		StreamEater inputEater = new StreamEater(proc.getInputStream());
		StreamEater errorEater = new StreamEater(proc.getErrorStream());
		inputEater.start();
		errorEater.start();
	}

	private static class StreamEater extends Thread {
		private InputStream streamToEat;

		StreamEater(InputStream is) {
			streamToEat = is;
		}

		@Override
		public void run() {
			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(streamToEat))) {
				try {
					while (reader.readLine() != null)
						;
				} catch (IOException e1) {
					logger.error(
							"Error occurred while reading threads from process, returned by LoadingScript.",
							e1);
				}
			} catch (IOException e2) {
				logger.error(
						"Error occurred while closing inner resources of BufferedReader in StreamEater.",
						e2);
			}
		}
	}
}

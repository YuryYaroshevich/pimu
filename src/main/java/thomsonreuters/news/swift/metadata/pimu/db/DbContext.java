package thomsonreuters.news.swift.metadata.pimu.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbContext {
	private Connection connection;

	protected Logger logger = LoggerFactory.getLogger(DataLoader.class);

	private String driverName;
	private String connectionString;
	private String userName;
	private String password;

	public DbContext(String driverName, String connectionString,
			String userName, String password) {
		this.driverName = driverName;
		this.connectionString = connectionString;
		this.userName = userName;
		this.password = password;
	}

	protected Connection openConnection(int attemptsNum) throws Exception {
		try {
			if (connection != null) {
				return connection;
			}

			Class.forName(driverName);

			connection = DriverManager.getConnection(connectionString,
					userName, password);
			return connection;
		} catch (ClassNotFoundException e) {
			logger.error(String.format("Couldn't load database driver - %s",
					driverName), e);
			throw e;
		} catch (SQLException e) {
			attemptsNum--;
			if (attemptsNum > 0) {
				logger.error(
						String.format(
								"Couldn't connect to database. Will try %s more times.",
								attemptsNum), e);
				return openConnection(attemptsNum);
			}
			logger.error(e.getMessage(), e);
			throw new DbContextException("Couldn't connect to database.", e);
		}
	}
}

package thomsonreuters.news.swift.metadata.pimu.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbContext {
	private Connection connection;

	protected Connection openConnection() throws SQLException, ClassNotFoundException {
		if (isValidConnection()) {
			return connection;
		}

		Class.forName("oracle.jdbc.OracleDriver");
		connection = DriverManager.getConnection(
				"jdbc:oracle:thin:@localhost:1521:xe",//"md@//10.242.136.242:1521/PRGNPRD.ime.reuters.com", 
				"system",//"md",
				"1234");//"taxman");
		return connection;
	}

	private boolean isValidConnection() throws SQLException {
		return (connection != null) && (connection.isValid(0));
	}
}

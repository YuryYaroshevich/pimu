package thomsonreuters.news.swift.metadata.pimu.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Aleh_Yafimau
 * Date: 8/14/13
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */

public class PropertiesProvider {

	private static Logger logger = LoggerFactory.getLogger(PropertiesProvider.class);

	public static Properties createProperties(String propsFilePath) {
		java.util.Properties prop = new java.util.Properties();

		Properties result = new Properties();
		try {
			prop.load(new FileInputStream(propsFilePath));

			logger.debug(String.format("Found properties-file %s. Started reading properties.", propsFilePath));

			result.setFtpHostname(readProperty(prop, "ftp.hostname"));
			result.setFtpLogin(readProperty(prop, "ftp.login"));
			result.setFtpPassword(readProperty(prop, "ftp.password"));
			result.setFtpWorkingDir(readProperty(prop, "ftp.workingDir"));
			
			result.setBcpRowTerm(readProperty(prop, "bcp.rowTerm"));
			result.setBcpRowTermRegexp(readProperty(prop, "bcp.rowTermRegexp"));
			result.setBcpFieldTerm(readProperty(prop, "bcp.fieldTerm"));
			result.setBcpFieldTermRegexp(readProperty(prop, "bcp.fieldTermRegexp"));
			
			result.setCsvPath(readProperty(prop, "csv.path"));
			
			result.setZipRegexp(readProperty(prop, "zip.regexp"));
			result.setZipDatePattern(readProperty(prop, "zip.datePattern"));
			
			result.setDbAttemptsNumber(readPropertyInt(prop, "db.attemptsNumber", true));
			result.setDbConnectionString(readProperty(prop, "db.connectionString"));
			result.setDbDriverName(readProperty(prop, "db.driverName"));
			result.setDbUser(readProperty(prop, "db.user"));
			result.setDbPassword(readProperty(prop, "db.password"));
			result.setDbMappingTable1(readProperty(prop, "db.mappingTable1"));
			result.setDbMappingTable2(readProperty(prop, "db.mappingTable2"));
			result.setDbStatusTable(readProperty(prop, "db.statusTable"));
			result.setDbMappingValidRatio(readPropertyFloat(prop, "db.mappingValidRatio", true));
			
			result.setSqlldrOracleBinDirPath(readProperty(prop, "sqlldr.oracleBinDirPath"));
			result.setSqlldrConnectionString(readProperty(prop, "sqlldr.connectionString"));
			result.setSqlldrCtlFilePath(readProperty(prop, "sqlldr.ctlFilePath"));
			result.setSqlldrLogFilePath(readProperty(prop, "sqlldr.logFilePath"));
			result.setSqlldrRowsNumPerOneCommit(readPropertyInt(prop, "sqlldr.rowsNumPerOneCommit", true));

			logger.debug(String.format("All properties have been read from properties-file %s.", propsFilePath));
		} catch (FileNotFoundException e) {
			logger.error(String.format("Properties-file %s was not found.", propsFilePath), e);
		} catch (IOException ex) {
			logger.error(String.format("Error reading properties-file %s.", propsFilePath), ex);
		}

		return result;
	}

	private static int readPropertyInt(java.util.Properties prop, String propertyName, boolean nonNegative){
		String propValue = readProperty(prop, propertyName);

		int intValue = 0;
		try {
			intValue = Integer.parseInt(propValue);
		} catch (NumberFormatException e) {
			logger.error(String.format("Property %s cannot be parsed as integer. Exiting...", propertyName));
		}

		if(nonNegative && intValue < 0){
			logger.error(String.format("Property %s cannot have negative value. Exiting...", propertyName));
		}
		return intValue;
	}
	
	private static float readPropertyFloat(java.util.Properties prop, String propertyName, boolean nonNegative){
		String propValue = readProperty(prop, propertyName);

		float floatValue = 0;
		try {
			floatValue = Float.parseFloat(propValue);
		} catch (NumberFormatException e) {
			logger.error(String.format("Property %s cannot be parsed as float. Exiting...", propertyName));
		}

		if(nonNegative && floatValue < 0){
			logger.error(String.format("Property %s cannot have negative value. Exiting...", propertyName));
		}
		return floatValue;
	}

	private static String readProperty(java.util.Properties prop, String propertyName){
		logger.debug(String.format("Getting property %s from properties-file.", propertyName));

		String propValue = prop.getProperty(propertyName);
		if (propValue == null) {
			logger.error(String.format("Property %s not found in properties-file. Exiting...", propertyName));
		}
		return propValue;
	}

}
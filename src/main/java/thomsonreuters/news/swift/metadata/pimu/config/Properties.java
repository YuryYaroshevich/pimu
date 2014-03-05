package thomsonreuters.news.swift.metadata.pimu.config;

public class Properties {
	private String ftpHostname;
	private String ftpLogin;
	private String ftpPassword;
	private String ftpWorkingDir;

	private String bcpRowTerm;
	private String bcpRowTermRegexp;
	private String bcpFieldTerm;
	private String bcpFieldTermRegexp;

	private String csvPath;

	private String zipRegexp;
	private String zipDatePattern;

	private int dbAttemptsNumber;
	private String dbConnectionString;
	private String dbDriverName;
	private String dbUser;
	private String dbPassword;
	private String dbMappingTable1;
	private String dbMappingTable2;
	private String dbStatusTable;
	private float dbMappingValidRatio;

	private String sqlldrOracleBinDirPath;
	private String sqlldrConnectionString;
	private String sqlldrCtlFilePath;
	private String sqlldrLogFilePath;
	private int sqlldrRowsNumPerOneCommit;

	

	public String getFtpHostname() {
		return ftpHostname;
	}

	public void setFtpHostname(String ftpHostname) {
		this.ftpHostname = ftpHostname;
	}

	public String getFtpLogin() {
		return ftpLogin;
	}

	public void setFtpLogin(String ftpLogin) {
		this.ftpLogin = ftpLogin;
	}

	public String getFtpPassword() {
		return ftpPassword;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	public String getFtpWorkingDir() {
		return ftpWorkingDir;
	}

	public void setFtpWorkingDir(String ftpWorkingDir) {
		this.ftpWorkingDir = ftpWorkingDir;
	}

	public String getBcpRowTerm() {
		return bcpRowTerm;
	}

	public void setBcpRowTerm(String bcpRowTerm) {
		this.bcpRowTerm = bcpRowTerm;
	}

	public String getBcpRowTermRegexp() {
		return bcpRowTermRegexp;
	}

	public void setBcpRowTermRegexp(String bcpRowTermRegexp) {
		this.bcpRowTermRegexp = bcpRowTermRegexp;
	}

	public String getBcpFieldTerm() {
		return bcpFieldTerm;
	}

	public void setBcpFieldTerm(String bcpFieldTerm) {
		this.bcpFieldTerm = bcpFieldTerm;
	}

	public String getBcpFieldTermRegexp() {
		return bcpFieldTermRegexp;
	}

	public void setBcpFieldTermRegexp(String bcpFieldTermRegexp) {
		this.bcpFieldTermRegexp = bcpFieldTermRegexp;
	}

	public String getCsvPath() {
		return csvPath;
	}

	public void setCsvPath(String csvPath) {
		this.csvPath = csvPath;
	}

	public String getZipRegexp() {
		return zipRegexp;
	}

	public void setZipRegexp(String zipRegexp) {
		this.zipRegexp = zipRegexp;
	}

	public String getZipDatePattern() {
		return zipDatePattern;
	}

	public void setZipDatePattern(String zipDatePattern) {
		this.zipDatePattern = zipDatePattern;
	}

	public int getDbAttemptsNumber() {
		return dbAttemptsNumber;
	}

	public void setDbAttemptsNumber(int dbAttemptsNumber) {
		this.dbAttemptsNumber = dbAttemptsNumber;
	}

	public String getDbConnectionString() {
		return dbConnectionString;
	}

	public void setDbConnectionString(String dbConnectionString) {
		this.dbConnectionString = dbConnectionString;
	}
	
	public String getDbDriverName() {
		return dbDriverName;
	}

	public void setDbDriverName(String dbDriverName) {
		this.dbDriverName = dbDriverName;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String getDbMappingTable1() {
		return dbMappingTable1;
	}

	public void setDbMappingTable1(String dbMappingTable1) {
		this.dbMappingTable1 = dbMappingTable1;
	}

	public String getDbMappingTable2() {
		return dbMappingTable2;
	}

	public void setDbMappingTable2(String dbMappingTable2) {
		this.dbMappingTable2 = dbMappingTable2;
	}

	public String getDbStatusTable() {
		return dbStatusTable;
	}

	public void setDbStatusTable(String dbStatusTable) {
		this.dbStatusTable = dbStatusTable;
	}

	public float getDbMappingValidRatio() {
		return dbMappingValidRatio;
	}

	public void setDbMappingValidRatio(float dbMappingValidRatio) {
		this.dbMappingValidRatio = dbMappingValidRatio;
	}

	public String getSqlldrOracleBinDirPath() {
		return sqlldrOracleBinDirPath;
	}

	public void setSqlldrOracleBinDirPath(String sqlldrOracleBinDirPath) {
		this.sqlldrOracleBinDirPath = sqlldrOracleBinDirPath;
	}

	public String getSqlldrConnectionString() {
		return sqlldrConnectionString;
	}

	public void setSqlldrConnectionString(String sqlldrConnectionString) {
		this.sqlldrConnectionString = sqlldrConnectionString;
	}

	public String getSqlldrCtlFilePath() {
		return sqlldrCtlFilePath;
	}

	public void setSqlldrCtlFilePath(String sqlldrCtlFilePath) {
		this.sqlldrCtlFilePath = sqlldrCtlFilePath;
	}

	public String getSqlldrLogFilePath() {
		return sqlldrLogFilePath;
	}

	public void setSqlldrLogFilePath(String sqlldrLogFilePath) {
		this.sqlldrLogFilePath = sqlldrLogFilePath;
	}

	public int getSqlldrRowsNumPerOneCommit() {
		return sqlldrRowsNumPerOneCommit;
	}

	public void setSqlldrRowsNumPerOneCommit(int sqlldrRowsNumPerOneCommit) {
		this.sqlldrRowsNumPerOneCommit = sqlldrRowsNumPerOneCommit;
	}
}

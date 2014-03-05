package thomsonreuters.news.swift.metadata.pimu.bcptransfering;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipInputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.joda.time.DateMidnight;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import thomsonreuters.news.swift.metadata.pimu.bcpprocessing.BCP;
import thomsonreuters.news.swift.metadata.pimu.config.Properties;

public class BCPTransfer {
	private String userName;
	private String password;
	private String ftpHostname;
	private String workingDir;
	
	private String zipRegexp;
	private String zipDatePattern;

	private Logger logger = LoggerFactory.getLogger(BCPTransfer.class);

	public BCPTransfer(Properties props) {
		this.ftpHostname = props.getFtpHostname();
		this.userName = props.getFtpLogin();
		this.password = props.getFtpPassword();
		this.workingDir = props.getFtpWorkingDir();
		
		this.zipRegexp = props.getZipRegexp();
		this.zipDatePattern = props.getZipDatePattern();
	}

	public BCP transfer(BCP bcp)
			throws Exception {
		FTPClient ftp = new FTPClient();
		try {
			logger.info("Connection to ftp is started.");
			ftp.connect(ftpHostname);
			ftp.login(userName, password);
			ftp.changeWorkingDirectory(workingDir);
			logger.info("Connection to ftp has been succeeded.");

			FTPFile[] ftpFiles = ftp.listFiles();
			Pattern zipArchivePattern = Pattern.compile(zipRegexp);
			for (FTPFile ftpFile : ftpFiles) {
				if (ftpFile.getType() == FTPFile.FILE_TYPE) {
					Matcher zipMatcher = zipArchivePattern.matcher(ftpFile
							.getName());
					if (zipMatcher.find()) {
						return transferIfDaily(ftpFile, ftp, bcp);
					}
				}
			}
			throw new Exception("There is no appropriate zip archive.");
		} catch (IOException e) {
			logger.error("Could not connect to ftp server.", e);
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			try {
				ftp.disconnect();
			} catch (IOException e) {
				logger.error("Error occurred while disconnecting from ftp server.");
			}
		}
	}

	private BCP transferIfDaily(FTPFile ftpFile, FTPClient ftp, BCP bcp)
			throws Exception {
		try {
			String fileName = ftpFile.getName();
			if (isDateCorrect(fileName, zipDatePattern)) {
				logger.info(String.format(
						"Zip archive for today was found - %s.",
						ftpFile.getName()));
				// documentation recommends to call these methods
				ftp.enterLocalPassiveMode();
				ftp.setFileType(FTP.BINARY_FILE_TYPE);

				InputStream fromFtp = ftp.retrieveFileStream(fileName);
				try (ZipInputStream zipIn = new ZipInputStream(fromFtp);) {
					zipIn.getNextEntry();
					bcp.setVersion(extractFileVersion(fileName));
					bcp.makeBCPTree(zipIn);
					checkIfEmpty(bcp);
					return bcp;
				}
			}
			throw new Exception("Zip archive was found is not for today.");
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	private static boolean isDateCorrect(String fileName, String datePattern) {
		DateTimeFormatter fileNameToDate = DateTimeFormat
				.forPattern("'EANORGANIZATION_'yyyyMMdd_HHmm'.zip'");
		DateMidnight dateOfFile = fileNameToDate.parseDateTime(fileName)
				.toDateMidnight();
		DateMidnight today = DateMidnight.now();
		return today.equals(dateOfFile);
	}

	private static String extractFileVersion(String fileName) {
		Matcher versionMatcher = Pattern.compile("(.+)\\.zip")
				.matcher(fileName);
		if (versionMatcher.find()) {
			return versionMatcher.group(1);
		}
		return "";
	}

	private void checkIfEmpty(BCP bcp) {
		if (bcp.isEmpty()) {
			logger.error("BCP file is empty");
			// JMS alert
		} else {
			logger.info("BCP tree was build.");
		}
	}
}

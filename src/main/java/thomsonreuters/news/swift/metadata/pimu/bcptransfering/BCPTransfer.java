package thomsonreuters.news.swift.metadata.pimu.bcptransfering;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipInputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import thomsonreuters.news.swift.metadata.pimu.bcpprocessing.BCP;
import thomsonreuters.news.swift.metadata.pimu.bcpprocessing.BCPBuilder;

public class BCPTransfer {
	private String userName;
	private String password;
	private String ftpHostName;

	public BCPTransfer(String ftpHostName, String userName, String password) {
		this.ftpHostName = ftpHostName;
		this.userName = userName;
		this.password = password;
	}

	public BCP transfer(String workingDir, String zipRegexp,
			BCPBuilder bcpBuilder) throws Exception {
		FTPClient ftp = new FTPClient();
		try {
			ftp.connect(ftpHostName);
			ftp.login(userName, password);
			ftp.changeWorkingDirectory(workingDir);

			FTPFile[] ftpFiles = ftp.listFiles();
			Pattern zipArchivePattern = Pattern.compile(zipRegexp);
			for (FTPFile ftpFile : ftpFiles) {
				if (ftpFile.getType() == FTPFile.FILE_TYPE) {
					Matcher zipMatcher = zipArchivePattern.matcher(ftpFile
							.getName());
					if (zipMatcher.find()) {
						// documentation recommends to call these methods
						ftp.enterLocalPassiveMode();
						ftp.setFileType(FTP.BINARY_FILE_TYPE);

						InputStream fromFtp = ftp.retrieveFileStream(ftpFile
								.getName());
						ZipInputStream zipIn = new ZipInputStream(fromFtp);
						zipIn.getNextEntry();
						try {
							BCP bcp = bcpBuilder.build(zipIn);
							if (!ftp.completePendingCommand()) {
								// throw exception
							}
							return bcp;
						} finally {
							zipIn.close();
							fromFtp.close();
						}
					}
				}
			}
			throw new Exception("there is no zip archive");
		} finally {
			ftp.disconnect();
		}
	}

}

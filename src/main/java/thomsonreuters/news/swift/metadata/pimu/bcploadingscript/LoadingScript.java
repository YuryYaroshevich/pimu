package thomsonreuters.news.swift.metadata.pimu.bcploadingscript;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import thomsonreuters.news.swift.metadata.pimu.config.Properties;

public class LoadingScript {
	private String csvFilePath;
	private String oracleBinDirPath;
	private String connectionString;
	private String ctlFilePath;
	private String sqlLoaderLogPath;
	private int rowsNumPerOneCommit;

	private Logger logger = LoggerFactory.getLogger(LoadingScript.class);

	public LoadingScript(Properties props) {
		csvFilePath = props.getCsvPath();
		oracleBinDirPath = props.getSqlldrOracleBinDirPath();
		connectionString = props.getSqlldrConnectionString();
		ctlFilePath = props.getSqlldrCtlFilePath();
		sqlLoaderLogPath = props.getSqlldrLogFilePath();
		rowsNumPerOneCommit = props.getSqlldrRowsNumPerOneCommit();

	}

	public Process run(String tableName) throws IOException {
		constructCTLFile(tableName);
		constructBatScript();
		Process proc = Runtime.getRuntime().exec("runSqlldr.bat");
		return proc;
	}

	private void constructCTLFile(String tableName) throws IOException {
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("sqlldr.ctl")));) {
			String ctlData = String
					.format("load data\n infile '%s'\n append into table %s\n fields terminated by \",\" \n (organizationid, edcoid, commonname)",
							csvFilePath, tableName);
			writer.write(ctlData);
			logger.info(String.format("%s file was created.", "sqlldr.ctl"));
		} catch (IOException e) {
			logger.error("Error occurred while constructing of ctl file.", e);
			throw e;
		}
	}

	private void constructBatScript() throws IOException {
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("runSqlldr.bat")));) {
			String ctlData = String
					.format("@echo off \n cd %s \n sqlldr %s control=%s log=%s rows=%s",
							oracleBinDirPath, connectionString, ctlFilePath,
							sqlLoaderLogPath, rowsNumPerOneCommit);
			writer.write(ctlData);
			logger.info(String.format("%s file was created.", "runSqlldr.bat"));
		} catch (IOException e) {
			logger.error("Error occurred while constructing of bat file.", e);
			throw e;
		}
	}
}

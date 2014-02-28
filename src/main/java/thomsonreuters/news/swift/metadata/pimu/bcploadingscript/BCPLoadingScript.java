package thomsonreuters.news.swift.metadata.pimu.bcploadingscript;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class BCPLoadingScript {

	public void run(String tableName) throws IOException {
		constructCTLFile(tableName);
		Runtime.getRuntime().exec("runSqlldr.bat");
	}

	@SuppressWarnings("resource")
	private void constructCTLFile(String tableName) throws IOException {
		Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("sqlldr.ctl")));
		writer.write(String
				.format("load data\n infile 'd:\bcpf.csv'\n append into table %s\n fields terminated by ",
						"\n (organizationid, edcoid, commonname)", tableName));
	}

}

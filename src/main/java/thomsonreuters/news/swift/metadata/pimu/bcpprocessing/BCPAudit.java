package thomsonreuters.news.swift.metadata.pimu.bcpprocessing;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.Scanner;

public class BCPAudit {
	private String auditFilePath;
	
	public static final int EMPTY_AUDIT = -1;

	public BCPAudit(String auditFilePath) {
		this.auditFilePath = auditFilePath;
	}

	public void writeBCPRowsNum(int bcpRowsNumber) throws IOException {
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(auditFilePath)))) {
			writer.write(String.format("%d\n%s", bcpRowsNumber,
					new Date().toString()));
		}
	}

	public int readBCPRowsNum() throws FileNotFoundException {
		try (Scanner scan = new Scanner(new FileInputStream(auditFilePath))) {
			if (scan.hasNext()) {
				return scan.nextInt();
			} else {
				return EMPTY_AUDIT;
			}			
		}
	}
}

package thomsonreuters.news.swift.metadata.pimu;

import java.util.Date;

import thomsonreuters.news.swift.metadata.pimu.bcploadingscript.BCPLoadingScript;
import thomsonreuters.news.swift.metadata.pimu.bcpprocessing.BCP;
import thomsonreuters.news.swift.metadata.pimu.bcpprocessing.BCPAudit;
import thomsonreuters.news.swift.metadata.pimu.bcpprocessing.BCPBuilder;
import thomsonreuters.news.swift.metadata.pimu.bcpprocessing.editing.ChangeToCSVStyle;
import thomsonreuters.news.swift.metadata.pimu.bcpprocessing.editing.SymbolSequenceReplacement;
import thomsonreuters.news.swift.metadata.pimu.bcptransfering.BCPTransfer;
import thomsonreuters.news.swift.metadata.pimu.db.BCPDataLoader;
import thomsonreuters.news.swift.metadata.pimu.validation.BCPValidator;

public class Application {
	private BCPAudit bcpAudit = new BCPAudit("BCPAudit.txt");

	public void run() throws Exception {
		// get BCP file from FTP
		BCPBuilder bcpBuilder = createBCPBuilder();
		BCPTransfer bcpTransfer = new BCPTransfer("10.122.254.245",
				"eandevfull", "EANRWU$3r");
		System.out.println(new Date() + " ---bcp transfer start");
		BCP bcp = bcpTransfer.transfer("EAN/News/", ".+\\.zip", bcpBuilder);
		System.out.println(new Date() + " ---bcp transfer end");
		// validate BCP file
		System.out.println(new Date() + " ---bcp create start");
		BCPValidator bcpValidator = new BCPValidator();
		System.out.println(new Date() + " ---bcp create end");
		boolean isValid = bcpValidator.isBCPValid(bcp,
				bcpAudit.readBCPRowsNum(), 0.05f);
		// if valid put in database
		if (isValid) {
			bcpAudit.writeBCPRowsNum(bcp.getRowsNumber());
			// transforming to CSV format
			tuneBCP(bcp);
			System.out.println(new Date() + " ---writing csv start");
			bcp.write("d:/bcpf.csv");
			System.out.println(new Date() + " ---writing csv finish");
			BCPDataLoader dataLoader = new BCPDataLoader();
			System.out.println(new Date() + " insert in db start");
			dataLoader.loadBCP("d:/bcpf.csv", new BCPLoadingScript());
			System.out.println(new Date() + " insert in db finish");
		}
	}

	private BCPBuilder createBCPBuilder() {
		BCPBuilder bcpBuilder = new BCPBuilder();
		bcpBuilder.setFieldTerm("|^|");
		bcpBuilder.setFieldTermRegexp("\\|\\^\\|");
		bcpBuilder.setRowTerm("|!|\r");
		bcpBuilder.setRowTermRegexp("\\|!\\|\\s+");
		return bcpBuilder;
	}

	private void tuneBCP(BCP bcp) {
		bcp.addEditor(new ChangeToCSVStyle());
		bcp.addRowEditor(new SymbolSequenceReplacement(".OQ", ".O", 1));
	}
}

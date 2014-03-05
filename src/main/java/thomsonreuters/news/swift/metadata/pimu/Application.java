package thomsonreuters.news.swift.metadata.pimu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import thomsonreuters.news.swift.metadata.pimu.bcploadingscript.LoadingScript;
import thomsonreuters.news.swift.metadata.pimu.bcpprocessing.BCP;
import thomsonreuters.news.swift.metadata.pimu.bcpprocessing.editing.ChangeToCSVStyle;
import thomsonreuters.news.swift.metadata.pimu.bcpprocessing.editing.SymbolSequenceReplacement;
import thomsonreuters.news.swift.metadata.pimu.bcptransfering.BCPTransfer;
import thomsonreuters.news.swift.metadata.pimu.config.Properties;
import thomsonreuters.news.swift.metadata.pimu.config.PropertiesProvider;
import thomsonreuters.news.swift.metadata.pimu.db.DataLoader;

public class Application {
	private Logger logger = LoggerFactory.getLogger(Application.class);

	public void run() throws Exception {
		Properties props = PropertiesProvider
				.createProperties("pimu.properties");

		BCPTransfer bcpTransfer = new BCPTransfer(props);

		logger.info("Transferring zip file from ftp is started.");
		BCP bcp = createEmptyBCP(props);
		bcpTransfer.transfer(bcp);

		logger.info("Writing bcp file in csv format.");
		tuneBCP(bcp);
		bcp.write(props.getCsvPath());

		logger.info("Loading csv file content to database.");
		DataLoader dataLoader = new DataLoader(props);
		LoadingScript script = new LoadingScript(props);
		dataLoader.insertFileDataInDb(script, bcp.getVersion());
	}

	private BCP createEmptyBCP(Properties props) {
		return new BCP(props.getBcpFieldTerm(), props.getBcpFieldTermRegexp(),
				props.getBcpRowTerm(), props.getBcpRowTermRegexp());
	}

	private void tuneBCP(BCP bcp) {
		bcp.addEditor(new ChangeToCSVStyle());
		bcp.addRowEditor(new SymbolSequenceReplacement(".OQ", ".O", 1));
	}
}

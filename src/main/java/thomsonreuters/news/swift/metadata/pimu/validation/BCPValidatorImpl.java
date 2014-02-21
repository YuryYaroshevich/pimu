package thomsonreuters.news.swift.metadata.pimu.validation;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class BCPValidatorImpl implements BCPValidator {

	private FileRowsCounter fileRowsCounter = new FileRowsCounterImpl();

	public FileRowsCounter getFileRowsCounter() {
		return fileRowsCounter;
	}

	public void setFileRowsCounter(FileRowsCounter fileRowsCounter) {
		this.fileRowsCounter = fileRowsCounter;
	}

	@Override
	public boolean isBCPValid(File bcp, final float validRatio,
			int rowsNumInPrevBCP, String rowTerminator,
			Pattern rowTerminatorPattern) throws IOException {
		int rowsNumInCurrBCP = fileRowsCounter.countRows(bcp, rowTerminator,
				rowTerminatorPattern);
		float ratio1 = rowsNumInCurrBCP / rowsNumInPrevBCP;
		float ratio2 = rowsNumInPrevBCP / rowsNumInCurrBCP;
		if ((ratio1 <= validRatio) || (ratio2 <= validRatio)) {
			return true;
		} else {
			return false;
		}
	}

}

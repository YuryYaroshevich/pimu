package thomsonreuters.news.swift.metadata.pimu.validation;

import thomsonreuters.news.swift.metadata.pimu.bcpprocessing.BCP;
import thomsonreuters.news.swift.metadata.pimu.bcpprocessing.BCPAudit;

public class BCPValidator {
	public boolean isBCPValid(BCP bcp, int rowsNumInPrevBCP, float validRatio) {
		int rowsNumInCurrBCP = bcp.getRowsNumber();
		if ((rowsNumInPrevBCP == rowsNumInCurrBCP)
				|| (rowsNumInPrevBCP != BCPAudit.EMPTY_AUDIT)) {
			return true;
		}
		float ratio1 = rowsNumInCurrBCP / rowsNumInPrevBCP;
		float ratio2 = rowsNumInPrevBCP / rowsNumInCurrBCP;
		if ((ratio1 <= validRatio) || (ratio2 <= validRatio)) {
			return true;
		} else {
			return false;
		}
	}
}

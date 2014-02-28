package thomsonreuters.news.swift.metadata.pimu.bcpprocessing.editing;

import thomsonreuters.news.swift.metadata.pimu.bcpprocessing.BCP;

public class ChangeToCSVStyle implements BCPEditor {
	@Override
	public void editBCP(BCP bcp) {
		bcp.setRowTerm("\n");
		bcp.setFieldTerm(", ");
		bcp.disableHeader();
	}
}

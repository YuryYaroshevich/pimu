package thomsonreuters.news.swift.metadata.pimu.bcpprocessing.editing;

import java.util.List;

import thomsonreuters.news.swift.metadata.pimu.bcpprocessing.BCP.BCPRow;

public class SymbolSequenceReplacement implements BCPRowEditor {
	private String oldSequence;
	private String newSequence;
	private int columnNumber;

	public SymbolSequenceReplacement(String oldSequence, String newSequence,
			int columnNumber) {
		this.oldSequence = oldSequence;
		this.newSequence = newSequence;
		this.columnNumber = columnNumber;
	}

	@Override
	public void editRow(BCPRow row) {
		List<String> fields = row.getFields();
		String fieldWhereToChange = fields.get(columnNumber);
		String changedField = fieldWhereToChange.replace(oldSequence,
				newSequence);
		fields.set(columnNumber, changedField);
	}
}

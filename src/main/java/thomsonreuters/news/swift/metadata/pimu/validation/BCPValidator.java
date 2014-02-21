package thomsonreuters.news.swift.metadata.pimu.validation;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public interface BCPValidator {
	boolean isBCPValid(File bpc, float validRatio, int linesNumInPrevBPC,
			String rowTerminator, Pattern rowTerminatorPattern)
			throws IOException;
}

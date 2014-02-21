package thomsonreuters.news.swift.metadata.pimu.validation;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public interface FileRowsCounter {
	int countRows(File file, String rowTerminator,
			Pattern rowTerminatorPattern) throws IOException;
}

package thomsonreuters.news.swift.metadata.pimu.validationtest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

import thomsonreuters.news.swift.metadata.pimu.validation.BCPValidator;
import thomsonreuters.news.swift.metadata.pimu.validation.BCPValidatorImpl;
import thomsonreuters.news.swift.metadata.pimu.validation.FileRowsCounter;

public class BCPValidatorTest {
	private static final String ROW_TERMINATOR = "dfg#%T#32f";
	private static final int CURRENT_FILE_ROWS_NUM = 1231;
	private static final int PREVIOUS_FILE_ROWS_NUM = 1280;

	@Test
	public void bcpValidator_should_invalidate_bcp_file_if_ratio_of_num_of_rows_bigger_than_given_constraint()
			throws IOException {
		BCPValidator bcpValidator = new BCPValidatorImpl();

		File bcpMock = mock(File.class);
		Pattern rowTermPattrnMock = Pattern.compile("sfsf");
		FileRowsCounter fileRowsCountMock = mock(FileRowsCounter.class);

		when(
				fileRowsCountMock.countRows(bcpMock, ROW_TERMINATOR,
						rowTermPattrnMock)).thenReturn(CURRENT_FILE_ROWS_NUM);

		((BCPValidatorImpl) bcpValidator).setFileRowsCounter(fileRowsCountMock);

		boolean isValid = bcpValidator.isBCPValid(bcpMock, 0.05f,
				PREVIOUS_FILE_ROWS_NUM, ROW_TERMINATOR, rowTermPattrnMock);
		
		Assert.assertTrue("File should be valid", isValid);

	}

}

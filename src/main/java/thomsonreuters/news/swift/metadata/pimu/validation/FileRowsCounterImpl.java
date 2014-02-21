package thomsonreuters.news.swift.metadata.pimu.validation;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileRowsCounterImpl implements FileRowsCounter {
	// Size of byte array for buffered reading of file
	private static final int BUFFER_ARRAY_SIZE = 1024;

	/**
	 * Gets number of table rows in a file. The workflow of method is the
	 * following: it reads from file some number of bytes in byte array.
	 * Then it creates a String object corresponding to that array. In this
	 * string it finds row terminators with help of regular expression. But
	 * there are possible such kind of situations when part of row terminator
	 * was read and another part of it is still in buffer. To workaround such
	 * situations this method also checks if there is some piece of row
	 * terminator in the end of read text. So it reads probably missing bytes
	 * and concatenate them with part of row terminator, then it compares the
	 * result with rowTerminator. If they are not equal, it call reset() method
	 * of InputStream object to put these bytes back in buffer.
	 */
	public int countRows(File file, String rowTerminator,
			Pattern rowTerminatorPattern) throws IOException {
		try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
			byte[] readedBytes = new byte[BUFFER_ARRAY_SIZE
					+ rowTerminator.length()];
			int rowsNumber = 0;

			while (is.read(readedBytes) != -1) {
				String readedText = new String(readedBytes);
				Matcher rowTerminatorMatcher = rowTerminatorPattern
						.matcher(readedText);
				// find row terminators that are fully in the readedText
				while (rowTerminatorMatcher.find()) {
					rowsNumber++;
				}
				// find out if there is part of row terminator in the end of
				// readedText
				int rowTermLen = rowTerminator.length();
				int readedTextLen = readedText.length();
				for (int len = rowTermLen - 1; len > 0; len--) {
					String readedTextPiece = readedText.substring(readedTextLen
							- len);
					String rowTerminatorPiece = rowTerminator.substring(0, len);
					if (rowTerminatorPiece.equals(readedTextPiece)) {
						// 1 is plused because I don't want marked position
						// become invalid after reading (rowTermLen - len)
						// symbols
						is.mark(rowTermLen - len + 1);
						byte[] itCanBeRowTermRemainder = new byte[rowTermLen
								- len];
						is.read(itCanBeRowTermRemainder);
						String itCanBeRowTerminator = rowTerminatorPiece
								.concat(new String(itCanBeRowTermRemainder));
						if (rowTerminator.equals(itCanBeRowTerminator)) {
							rowsNumber++;
						} else {
							is.reset();
						}
						break;
					}
				}
				// I do this to avoid mixing of read bytes.
				for (int i = 0; i < readedBytes.length; i++) {
					readedBytes[i] = 0;
				}
			}
			return rowsNumber;
		}
	}
}

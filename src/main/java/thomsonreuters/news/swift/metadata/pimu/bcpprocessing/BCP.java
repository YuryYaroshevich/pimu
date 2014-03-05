package thomsonreuters.news.swift.metadata.pimu.bcpprocessing;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import thomsonreuters.news.swift.metadata.pimu.bcpprocessing.editing.BCPEditor;
import thomsonreuters.news.swift.metadata.pimu.bcpprocessing.editing.BCPRowEditor;

public class BCP {
	private BCPHeader header;
	private List<BCPRow> rows;
	
	private String version;
	
	private String rowTerm;
	private String rowTermRegexp;
	private String fieldTerm;
	private String fieldTermRegexp;
	
	private boolean empty;

	private List<BCPEditor> editors = new ArrayList<BCPEditor>();
	private List<BCPRowEditor> rowEditors = new ArrayList<BCPRowEditor>();

	public BCP(String fieldTerm, String fieldTermRegexp,
			String rowTerm, String rowTermRegexp) {
		this.rowTerm = rowTerm;
		this.rowTermRegexp = rowTermRegexp;
		this.fieldTerm = fieldTerm;
		this.fieldTermRegexp = fieldTermRegexp;
		this.empty = true;
	}
		 
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void makeBCPTree(InputStream is) {
		try (Scanner scanner = new Scanner(new BufferedInputStream(is));) {
			scanner.useDelimiter(Pattern.compile(rowTermRegexp));
			if (scanner.hasNext()) {
				empty = false;
				
				String header = scanner.next();
				Pattern fieldTermPattern = Pattern.compile(fieldTermRegexp);
				this.header = new BCPHeader(header, fieldTerm, fieldTermPattern);

				rows = new ArrayList<BCPRow>();
				while (scanner.hasNext()) {
					String row = scanner.next();
					BCPRow bcpRow = new BCPRow(row, fieldTerm, fieldTermPattern);
					rows.add(bcpRow);
				}
			}
		}
	}

	public boolean isEmpty() {
		return empty;
	}
	
	public void setRowTerm(String rowTerm) {
		this.rowTerm = rowTerm;
	}

	public void setFieldTerm(String fieldTerm) {
		this.fieldTerm = fieldTerm;
	}

	public void disableHeader() {
		header.disabled = true;
	}

	public void write(String bcpPath) throws IOException {
		if (isEmpty()) {
			return;
		}
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(bcpPath)))) {
			editBCP();
			if (!header.disabled) {
				writer.write(String.format("%s%s", header, rowTerm));
			}
			for (BCPRow row : rows) {
				editRow(row);
				writer.write(String.format("%s%s", row, rowTerm));
			}
		}
	}

	public void addEditor(BCPEditor editor) {
		editors.add(editor);
	}

	private void editBCP() {
		for (BCPEditor editor : editors) {
			editor.editBCP(this);
		}
	}

	public void addRowEditor(BCPRowEditor rowEditor) {
		rowEditors.add(rowEditor);
	}

	private void editRow(BCPRow row) {
		for (BCPRowEditor rowEditor : rowEditors) {
			row.setFieldTerm(fieldTerm);
			rowEditor.editRow(row);
		}
	}

	public int getColumnNumber(String columnName) {
		return header.getColumnNumber(columnName);
	}

	public BCPHeader getHeader() {
		return header;
	}

	public List<BCPRow> getRows() {
		return rows;
	}

	public int getRowsNumber() {
		return rows.size();
	}

	public static class BCPRow {
		private List<String> fields;
		private String fieldTerm;

		@SuppressWarnings("resource")
		BCPRow(String row, String fieldTerm, Pattern fieldTermPattern) {
			this.fieldTerm = fieldTerm;

			fields = new ArrayList<String>();
			Scanner scanner = new Scanner(row);
			scanner.useDelimiter(fieldTermPattern);
			while (scanner.hasNext()) {
				fields.add(scanner.next());
			}
		}

		public void setFieldTerm(String fieldTerm) {
			this.fieldTerm = fieldTerm;
		}

		public List<String> getFields() {
			return fields;
		}

		public String toString() {
			StringBuilder result = new StringBuilder();
			for (int i = 0; i < fields.size(); i++) {
				result.append(fields.get(i));
				if ((i + 1) != fields.size()) {
					result.append(fieldTerm);
				}
			}
			return result.toString();
		}
	}

	public static class BCPHeader extends BCPRow {
		private boolean disabled;

		BCPHeader(String header, String fieldTerm, Pattern fieldTermPattern) {
			super(header, fieldTerm, fieldTermPattern);
			disabled = false;
		}

		public int getColumnNumber(String columnName) {
			return getFields().indexOf(columnName);
		}

		public void setColumnName(int columnNumber, String newName) {
			getFields().set(columnNumber, newName);
		}
	}
}

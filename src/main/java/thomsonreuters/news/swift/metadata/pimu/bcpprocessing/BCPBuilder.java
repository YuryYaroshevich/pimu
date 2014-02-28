package thomsonreuters.news.swift.metadata.pimu.bcpprocessing;

import java.io.IOException;
import java.io.InputStream;

public class BCPBuilder {
	private String fieldTerm;
	private String fieldTermRegexp;
	private String rowTerm;
	private String rowTermRegexp;

	public void setFieldTerm(String fieldTerm) {
		this.fieldTerm = fieldTerm;
	}

	public void setFieldTermRegexp(String fieldTermRegexp) {
		this.fieldTermRegexp = fieldTermRegexp;
	}

	public void setRowTerm(String rowTerm) {
		this.rowTerm = rowTerm;
	}

	public void setRowTermRegexp(String rowTermRegexp) {
		this.rowTermRegexp = rowTermRegexp;
	}

	public BCP build(InputStream is) throws IOException {
		return new BCP(is, fieldTerm, fieldTermRegexp, rowTerm, rowTermRegexp);
	}
}

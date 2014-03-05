package thomsonreuters.news.swift.metadata.pimu.db;

public class DbContextException extends Exception {
	private static final long serialVersionUID = 2037995987700126051L;

	public DbContextException(String errorMsg, Exception e) {
		super(errorMsg, e);
	}
}

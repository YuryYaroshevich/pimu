package thomsonreuters.news.swift.metadata.pimu;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
	public static void main(String[] args) throws IOException {
		/*
		 * File f = new File("d:/file.txt"); System.out.println(new
		 * FileRowsCounterImpl().countRows(f, "|!|\r",
		 * Pattern.compile("\\|!\\|\r")));
		 */

		Scanner scanner = new Scanner(new BufferedReader(new FileReader(
				"d:/file.txt")));//"EANORGANIZATION_20140213_0812/EANORGANIZATION_20140213_0812.bcp")));
		scanner.useDelimiter(Pattern.compile("\\|!\\|\r"));
		int i = 0;
		Pattern column2Patrn = Pattern.compile("\\|\\^\\|.+\\|\\^\\|");
		while (scanner.hasNext()) {
			i++;
			String row = scanner.next();
			Matcher m = column2Patrn.matcher(row);
			if (m.find()) {
				String column2 = m.group().replaceAll("\\.OQ", ".O");
				System.out.println(column2);
			}
			System.out.println(row);

		}
		System.out.println(i-1);
	}
}

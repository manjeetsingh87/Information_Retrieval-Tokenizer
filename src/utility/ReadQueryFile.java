package utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ReadQueryFile {
	/**
	 * This method is used to read the query_file.txt, reading all the queries line-by-line and adds the query string to the List<String>
	 * @param queryFile
	 * @return
	 */
	public static List<String> getQueries(String queryFile) {
		List<String> queriesList = new ArrayList<String>();
		BufferedReader bis = null;
		try {
			File inputFile = new File(queryFile);
			FileReader fis = new FileReader(inputFile);
			bis = new BufferedReader(fis);
			String line = null;
			while(null != (line = bis.readLine())) {
				queriesList.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != bis)
					bis.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return queriesList;
	}
}

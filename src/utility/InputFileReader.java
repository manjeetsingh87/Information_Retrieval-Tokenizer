package utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import pojo.DocumentPostingsPojo;

public class InputFileReader {
	/**
	 * This method reads the input file, input.idx
	 * @param fileName
	 * @return
	 */
	public List<DocumentPostingsPojo> readFile(String fileName) {
		String[] inputDataArr = null;
		BufferedReader bis = null;
		List<DocumentPostingsPojo> inputDataPojo = new ArrayList<DocumentPostingsPojo>();
		try {
			File inputFile = new File(fileName);
			FileReader fis = new FileReader(inputFile);
			bis = new BufferedReader(fis);
			String line = null;
			while(null != (line = bis.readLine())) {
				inputDataArr = line.split("\\\\");
				inputDataPojo.add(InputDataParser.parseInputData(inputDataArr));
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
		return inputDataPojo;
	}
}
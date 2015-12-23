package utility;

import java.util.LinkedList;
import java.util.List;

import pojo.DocumentPostingsPojo;
import pojo.PostingsPojo;

/**
 * @author MANJEET
 *
 */
public class InputDataParser {
	
	/**
	 * 
	 * @param dataArr
	 * @return
	 */
	public static DocumentPostingsPojo parseInputData(String[] dataArr) {
		DocumentPostingsPojo pojoObj = new DocumentPostingsPojo();
		pojoObj.setTerm(dataArr[0]);
		pojoObj.setListSize(Integer.parseInt(dataArr[1].substring(1, dataArr[1].length())));
		pojoObj.setPostings(parsePostingsData(dataArr[2]));
		return pojoObj;
	}
	
	/**
	 * 
	 * @param postingsData
	 * @return
	 */
	private static List<PostingsPojo> parsePostingsData(String postingsData) {
		List<PostingsPojo> postingsList = new LinkedList<PostingsPojo>();
		String[] data = postingsData.substring(2, postingsData.indexOf("]")).split(",");
		if(data.length>0) {
			for(int i=0; i<data.length; i++) {
				if(data[i].trim().length() > 0 && data[i].contains("/")) {
					String[] arr = data[i].split("/");
					PostingsPojo obj = new PostingsPojo();
					obj.setDocId(arr[0]);
					obj.setFrequency(Integer.parseInt(arr[1]));
					postingsList.add(obj);
				}
			}
		}
		return postingsList;
	}
}

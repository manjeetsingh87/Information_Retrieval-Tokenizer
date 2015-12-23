package comparator;

import java.util.Comparator;
import pojo.PostingsPojo;

public class DocIdComparator implements Comparator<PostingsPojo>{

	public int compare(PostingsPojo obj1, PostingsPojo obj2) {
		return obj1.getDocId().trim().compareTo(obj2.getDocId().trim());
	}
}
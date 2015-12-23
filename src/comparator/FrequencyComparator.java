package comparator;

import java.util.Comparator;
import pojo.PostingsPojo;

public class FrequencyComparator implements Comparator<PostingsPojo> {
	
	public int compare(PostingsPojo obj1, PostingsPojo obj2) {
		return obj2.getFrequency().compareTo(obj1.getFrequency());
	}
}
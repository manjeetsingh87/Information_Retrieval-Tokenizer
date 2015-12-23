package comparator;

import java.util.Comparator;

import pojo.TopKTermsPojo;

public class TopKTermsComparator implements Comparator<TopKTermsPojo> {
	
	public int compare(TopKTermsPojo obj1, TopKTermsPojo obj2) {
		return obj2.getListSize().compareTo(obj1.getListSize());
	}
}
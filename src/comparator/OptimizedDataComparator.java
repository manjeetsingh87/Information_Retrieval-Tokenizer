package comparator;

import java.util.Comparator;

import pojo.OptimizedPostingsDataPojo;

public class OptimizedDataComparator implements Comparator<OptimizedPostingsDataPojo> {

	public int compare(OptimizedPostingsDataPojo o1, OptimizedPostingsDataPojo o2) {
		return o1.getPostingSize().compareTo(o2.getPostingSize());
	}
}

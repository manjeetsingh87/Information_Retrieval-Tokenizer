package pojo;

import java.util.ArrayList;
import java.util.List;

public class TermDataPojo {
	private int documentCount;
	private int comparisons;
	private double time;
	List<PostingsPojo> docsIdSet = new ArrayList<PostingsPojo>();
	
	/**
	 * @return the documentCount
	 */
	public int getDocumentCount() {
		return documentCount;
	}
	/**
	 * @param documentCount the documentCount to set
	 */
	public void setDocumentCount(int documentCount) {
		this.documentCount = documentCount;
	}
	
	/**
	 * @return the comparisons
	 */
	public int getComparisons() {
		return comparisons;
	}
	/**
	 * @param comparisons the comparisons to set
	 */
	public void setComparisons(int comparisons) {
		this.comparisons = comparisons;
	}
	
	/**
	 * @return the time
	 */
	public double getTime() {
		return time;
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(double time) {
		this.time = time;
	}
	
	/**
	 * @return the docsIdSet
	 */
	public List<PostingsPojo> getDocsIdSet() {
		return docsIdSet;
	}
	/**
	 * @param docsIdSet the docsIdSet to set
	 */
	public void setDocsIdSet(List<PostingsPojo> docsIdSet) {
		this.docsIdSet = docsIdSet;
	}
}
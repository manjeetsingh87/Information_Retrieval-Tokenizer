package pojo;

import java.util.LinkedList;
import java.util.List;

public class SortedPostingsDataPojo {
	private String term;
	private List<PostingsPojo> sortedDocIdList = new LinkedList<PostingsPojo>();
	private List<PostingsPojo> sortedFrequencyList = new LinkedList<PostingsPojo>();
	
	/**
	 * @return the term
	 */
	public String getTerm() {
		return term;
	}
	/**
	 * @param term the term to set
	 */
	public void setTerm(String term) {
		this.term = term;
	}
	
	/**
	 * @return the sortedDocIdList
	 */
	public List<PostingsPojo> getSortedDocIdList() {
		return sortedDocIdList;
	}
	/**
	 * @param add docId to the sortedDocIdList
	 */
	public void setSortedDocIdList(List<PostingsPojo> sortedDocIdList) {
		this.sortedDocIdList = sortedDocIdList;
	}
	
	/**
	 * @return the sortedFrequencyList
	 */
	public List<PostingsPojo> getSortedFrequencyList() {
		return sortedFrequencyList;
	}
	/**
	 * @param adds frequency to the sortedFrequencyList
	 */
	public void setSortedFrequencyList(List<PostingsPojo> sortedFrequencyList) {
		this.sortedFrequencyList = sortedFrequencyList;
	}
}

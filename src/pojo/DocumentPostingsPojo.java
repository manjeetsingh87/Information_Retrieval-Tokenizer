package pojo;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class DocumentPostingsPojo implements Serializable{

	private static final long serialVersionUID = 1L;
	private String term;
	private Integer listSize;
	private List<PostingsPojo> postings = new LinkedList<PostingsPojo>();
	
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
	 * @return the listSize
	 */
	public Integer getListSize() {
		return listSize;
	}
	/**
	 * @param listSize the listSize to set
	 */
	public void setListSize(Integer listSize) {
		this.listSize = listSize;
	}
	
	/**
	 * @return the postings
	 */
	public List<PostingsPojo> getPostings() {
		return postings;
	}
	/**
	 * @param postings the postings to set
	 */
	public void setPostings(List<PostingsPojo> posting) {
		this.postings = posting;
	}
}
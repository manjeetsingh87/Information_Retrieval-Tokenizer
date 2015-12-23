package pojo;

import java.io.Serializable;

public class PostingsPojo implements Serializable{

	private static final long serialVersionUID = 1L;
	private String docId;
	private Integer frequency;
	
	/**
	 * @return the docId
	 */
	public String getDocId() {
		return docId;
	}
	/**
	 * @param docId the docId to set
	 */
	public void setDocId(String docId) {
		this.docId = docId;
	}
	
	/**
	 * @return the frequency
	 */
	public Integer getFrequency() {
		return frequency;
	}
	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}
}
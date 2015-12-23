package pojo;

/**
 * @author MANJEET
 *
 */
public class TopKTermsPojo {
	private String term;
	private Integer listSize;
	
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
	
	@Override
	public String toString() {
		return this.term;
	}
}
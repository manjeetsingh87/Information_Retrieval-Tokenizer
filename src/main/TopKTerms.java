package main;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import pojo.DocumentPostingsPojo;
import pojo.TopKTermsPojo;

import comparator.TopKTermsComparator;

public class TopKTerms {
	/**
	 * This method returns the top k terms based on the postings list size of the terms in the input file(index.idx)
	 * @param inputDataList
	 * @param queryParam
	 * @return
	 */
	public List<TopKTermsPojo> getTopKTerms(List<DocumentPostingsPojo> inputDataList, int queryParam) {
		List<TopKTermsPojo> topKTermsList = new LinkedList<TopKTermsPojo>();
		try {
			for(DocumentPostingsPojo obj : inputDataList) {
				TopKTermsPojo termObj = new TopKTermsPojo();
				termObj.setTerm(obj.getTerm());
				termObj.setListSize(obj.getListSize());
				topKTermsList.add(termObj);
			}
			Collections.sort(topKTermsList, new TopKTermsComparator());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return topKTermsList.subList(0, queryParam);
	}
}

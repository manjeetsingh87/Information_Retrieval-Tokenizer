package main;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import pojo.PostingsPojo;
import pojo.SortedPostingsDataPojo;
import pojo.TermDataPojo;
import pojo.TopKTermsPojo;

public class OutputFileWriter {
	
	/**
	 * This method is used to write the top k terms to the output file
	 * @param streamWriter
	 * @param topKTermsList
	 * @param query
	 */
	public void writeTopKTerms(Writer streamWriter, List<TopKTermsPojo> topKTermsList, String query) {
		try {
			streamWriter.write("FUNCTION: getTopK "+query+" \n"+"Result: ");
			String topKData = "";
			for(TopKTermsPojo term : topKTermsList) {
				int index = topKTermsList.indexOf(term);
				if(topKData.length() == 0)
					topKData = term.getTerm().concat(", ");
				else if(index < topKTermsList.size() -1)
					topKData = topKData.concat(term.getTerm().concat(", "));
				else
					topKData = topKData.concat(term.getTerm());
			}
			streamWriter.write(topKData+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method is used to write the postings list to the output file
	 * @param streamWriter
	 * @param postingsList
	 * @param query[]
	 */
	public void writePostings(Writer streamWriter, List<SortedPostingsDataPojo> postingsList, String[] query) {
		try {
			for(int i=0; i<query.length; i++) {
				streamWriter.write("FUNCTION: getPostings "+query[i]+" \n"); 
				if(!postingsList.isEmpty()) {
					String docIds = "";
					String frequency = "";
					for(SortedPostingsDataPojo obj : postingsList) {
						if(docIds.length() == 0)
							docIds = writeDocIdstoString(obj.getSortedDocIdList());
						else
							docIds = docIds.concat(",").concat(writeDocIdstoString(obj.getSortedDocIdList()));
						
						if(frequency.length() == 0)
							frequency = writeDocIdstoString(obj.getSortedFrequencyList());
						else 
							frequency = frequency.concat(",").concat(writeDocIdstoString(obj.getSortedFrequencyList()));
					}
					if(docIds.length() == 0 || frequency.length() == 0) {
						streamWriter.write("No doc id found \n");
						streamWriter.write("No doc frequency found \n");
					} else {
						streamWriter.write("Ordered by doc IDs: "+docIds+"\n");
						streamWriter.write("Ordered by TF: "+frequency+"\n");
					}
				} else {
					streamWriter.write("term not found \n");
				}
			}		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method is used to write the postings list to the output file
	 * @param streamWriter
	 * @param postingsList
	 * @param query
	 */
	public void writePostings(Writer streamWriter, List<SortedPostingsDataPojo> postingsList, String query) {
		try {
			streamWriter.write("FUNCTION: getPostings "+query+" \n"); 
			if(!postingsList.isEmpty()) {
				String docIds = "";
				String frequency = "";
				for(SortedPostingsDataPojo obj : postingsList) {
					if(docIds.length() == 0)
						docIds = writeDocIdstoString(obj.getSortedDocIdList());
					else
						docIds = docIds.concat(",").concat(writeDocIdstoString(obj.getSortedDocIdList()));
					
					if(frequency.length() == 0)
						frequency = writeDocIdstoString(obj.getSortedFrequencyList());
					else 
						frequency = frequency.concat(",").concat(writeDocIdstoString(obj.getSortedFrequencyList()));
				}
				if(docIds.length() == 0 || frequency.length() == 0) {
					streamWriter.write("No doc id found \n");
					streamWriter.write("No doc frequency found \n");
				} else {
					streamWriter.write("Ordered by doc IDs: "+docIds+"\n");
					streamWriter.write("Ordered by TF: "+frequency+"\n");
				}
			} else {
				streamWriter.write("term not found \n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String writeDocIdstoString(List<PostingsPojo> postingsList) {
		String strData = "";
		for(PostingsPojo obj : postingsList) {
			int index = postingsList.indexOf(obj);
			if(strData.length() == 0)
				strData = obj.getDocId().concat(", ");
			else if(index < postingsList.size() -1)
				strData = strData.concat(obj.getDocId().concat(", "));
			else
				strData = strData.concat(obj.getDocId());
		}
		return strData;
	}
	
	/**
	 * This method is used to write the TermAtATIme operations data to the output.log file.
	 * @param streamWriter
	 * @param termAtTimeData
	 * @param query
	 * @param oprnType
	 */
	public void writeTermAtTimeData(Writer streamWriter, TermDataPojo termAtTimeData, String query, String oprnType, int optimizedComparisons) {
		try {
			if(termAtTimeData.getDocsIdSet().isEmpty()) {
				streamWriter.write("terms not found \n");
			} else {
				streamWriter.write("FUNCTION: termAtATimeQuery"+oprnType+" "+query+" \n");
				streamWriter.write(termAtTimeData.getDocumentCount()+" documents are found \n");
				streamWriter.write(termAtTimeData.getComparisons()+" comparisons are made \n");
				streamWriter.write(termAtTimeData.getTime()+" seconds are used \n");
				streamWriter.write(optimizedComparisons+" comparisons are made with optimization \n");
				String docIds = "";
				for(PostingsPojo posting : termAtTimeData.getDocsIdSet()) {
					if(docIds.length() == 0)
						docIds = posting.getDocId();
					else 
						docIds = docIds.concat(", ").concat(posting.getDocId());
				}
				streamWriter.write("Result: "+docIds+"\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method is used to write the DocAtATIme operations data to the output.log file.
	 * @param streamWriter
	 * @param termAtTimeData
	 * @param query
	 * @param oprnType
	 */
	public void writeDocAtTimeData(Writer streamWriter, TermDataPojo termAtTimeData, String query, String oprnType) {
		try {
			if(!termAtTimeData.getDocsIdSet().isEmpty()) {
				streamWriter.write("FUNCTION: docAtATimeQuery"+oprnType+" "+query+" \n");
				streamWriter.write(termAtTimeData.getDocumentCount()+" documents are found \n");
				streamWriter.write(termAtTimeData.getComparisons()+" comparisons are made \n");
				streamWriter.write(termAtTimeData.getTime()+" seconds are used \n");
				String docIds = "";
				for(PostingsPojo posting : termAtTimeData.getDocsIdSet()) {
					if(docIds.length() == 0)
						docIds = posting.getDocId();
					else 
						docIds = docIds.concat(", ").concat(posting.getDocId());
				}
				streamWriter.write("Result: "+docIds+"\n");
			} else {
				streamWriter.write("terms not found \n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
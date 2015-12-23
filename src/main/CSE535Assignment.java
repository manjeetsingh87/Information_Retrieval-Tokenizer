package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import pojo.DocumentPostingsPojo;
import pojo.OptimizedPostingsDataPojo;
import pojo.PostingsPojo;
import pojo.SortedPostingsDataPojo;
import pojo.TermDataPojo;
import pojo.TopKTermsPojo;
import utility.InputFileReader;
import utility.ReadQueryFile;
import comparator.DocIdComparator;
import comparator.FrequencyComparator;

/**
  * @author MANJEET
 *
 */
public class CSE535Assignment {
	private static String[] queryTerms ;
	private static final String POSTINGS_QUERY = "getPostings";
	private static final String TERM_AT_TIME_AND_QUERY = "termAtATimeQueryAnd";
	private static final String TERM_AT_TIME_OR_QUERY = "termAtATimeQueryOr";
	private static final String DOC_AT_TIME_AND_QUERY = "docAtATimeQueryAnd";
	private static final String DOC_AT_TIME_OR_QUERY = "docAtATimeQueryOr";
	private static OutputFileWriter fileWriter;
	public static void main(String[] args) {
		String[] dataArr ;
		try {
			Scanner scanInput = new Scanner(System.in);
			String data= scanInput.nextLine();
			dataArr = data.split(" ");
			if(null == dataArr[4] || dataArr[4].length() == 0 || Integer.parseInt(dataArr[4]) < 0) {
				System.out.println("Please provide a correct topKterms data value");
			}
			scanInput.close();
			termDocumentOperations(dataArr[2], dataArr[3], dataArr[4], dataArr[5]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * This method performs all the operations on the input file, including sorting and arranging the data from input.idx file, performing the operations on data and 
	 * writing the output of the operations to an output.log file.
	 * @param inputFile
	 * @param outputFile
	 * @param topKTerms
	 * @param queryFile
	 */
	private static void termDocumentOperations(String inputFile, String outputFile, String topKTerms, String queryFile) {
		List<String> queries = ReadQueryFile.getQueries(queryFile);
		String[] queryNames = getQueries(queries);
		OutputStream stream ;	
		Writer streamWriter = null ;
		QueryFunctions queryFunction = new QueryFunctions();
		String postingsQuery ;
		String termAtTimeAndQuery ;
		String termAtTimeOrQuery ;
		String docAtTimeAndQuery ;
		String docAtTimeOrQuery ;
		try {
			stream = new FileOutputStream(new File(outputFile));
			streamWriter =  new OutputStreamWriter(stream);

			List<DocumentPostingsPojo> inputDataList = new InputFileReader().readFile(inputFile);
			displayTopKTerms(inputDataList, topKTerms, streamWriter);
			
			List<SortedPostingsDataPojo> sortedPostingsList = createSortedDocIdList(inputDataList);
			List<OptimizedPostingsDataPojo> optimizedPostingsList = createOptimizedTermDocIdList(inputDataList);
			for(int i=0; i<queryNames.length; i++) {
				switch (queryNames[i]) {
				case POSTINGS_QUERY:
					postingsQuery = queryTerms[i];
					showPostingsData(sortedPostingsList, queryFunction, postingsQuery, streamWriter);
					break;
				case TERM_AT_TIME_AND_QUERY:
					termAtTimeAndQuery = queryTerms[i];
					showTermAtTimeAndData(sortedPostingsList, queryFunction, termAtTimeAndQuery, streamWriter, optimizedPostingsList);
					break;
				case TERM_AT_TIME_OR_QUERY:
					termAtTimeOrQuery = queryTerms[i];
					showTermAtTimeOrData(sortedPostingsList, queryFunction, termAtTimeOrQuery, streamWriter, optimizedPostingsList);
					break;
				case DOC_AT_TIME_AND_QUERY:
					docAtTimeAndQuery = queryTerms[i];
					showDocAtTimeAndData(sortedPostingsList, queryFunction, docAtTimeAndQuery, streamWriter);
					break;
				case DOC_AT_TIME_OR_QUERY:
					docAtTimeOrQuery = queryTerms[i];
					showDocAtTimeOrData(sortedPostingsList, queryFunction, docAtTimeOrQuery, streamWriter);
					break;	
				default:
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				streamWriter.flush();
				streamWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * This method gets the top k terms from the index.idx list based on the postings list size
	 * @param inputDataList
	 * @param topKTerms
	 * @param streamWriter
	 */
	private static void displayTopKTerms(List<DocumentPostingsPojo> inputDataList, String topKTerms, Writer streamWriter) {
		fileWriter = new OutputFileWriter();
		List<TopKTermsPojo> topKTermsList = new TopKTerms().getTopKTerms(inputDataList, Integer.parseInt(topKTerms));
		fileWriter.writeTopKTerms(streamWriter, topKTermsList, topKTerms);
	}
	
	private static void showPostingsData(List<SortedPostingsDataPojo> sortedPostingsList, QueryFunctions query, String postingsQuery, Writer streamWriter) {
		fileWriter = new OutputFileWriter();
		List<SortedPostingsDataPojo> postingsList = query.getPostings(sortedPostingsList, postingsQuery);
		String[] postingsQueryArr = null;
		if(null != postingsQuery && postingsQuery.contains(" ")) {
			postingsQueryArr = postingsQuery.split(" ");
			fileWriter.writePostings(streamWriter, postingsList, postingsQueryArr);
		}	
		else if(null != postingsQuery)
			fileWriter.writePostings(streamWriter, postingsList, postingsQuery);
	}
	
	private static void showTermAtTimeAndData(List<SortedPostingsDataPojo> sortedPostingsList, QueryFunctions query, String termAtTimeAndQuery, 
			Writer streamWriter, List<OptimizedPostingsDataPojo> optimizedPostingsList) {
		fileWriter = new OutputFileWriter();
		TermDataPojo termAtTimeAndData = query.termAtaTimeAndDocs(sortedPostingsList, termAtTimeAndQuery);
		int optimizedComparisons = query.optimizedTermAtTimeAndComparisons(optimizedPostingsList, termAtTimeAndQuery);
		fileWriter.writeTermAtTimeData(streamWriter, termAtTimeAndData, termAtTimeAndQuery, "And", optimizedComparisons);
	}
	
	private static void showTermAtTimeOrData(List<SortedPostingsDataPojo> sortedPostingsList, QueryFunctions query, String termAtTimeOrQuery, 
			Writer streamWriter, List<OptimizedPostingsDataPojo> optimizedPostingsList) {
		fileWriter = new OutputFileWriter();
		TermDataPojo termAtTimeOrData = query.termAtaTimeOrDocs(sortedPostingsList, termAtTimeOrQuery);
		int optimizedComparisons = query.optimizedTermAtTimeOrComparisons(optimizedPostingsList, termAtTimeOrQuery);
		fileWriter.writeTermAtTimeData(streamWriter, termAtTimeOrData, termAtTimeOrQuery, "Or", optimizedComparisons);
	}
	
	private static void showDocAtTimeAndData(List<SortedPostingsDataPojo> sortedPostingsList, QueryFunctions query, String docAtTimeAndQuery, Writer streamWriter) {
		fileWriter = new OutputFileWriter();
		TermDataPojo docAtTimeAndData = query.docAtATimeAndDocs(sortedPostingsList, docAtTimeAndQuery);
		fileWriter.writeDocAtTimeData(streamWriter, docAtTimeAndData, docAtTimeAndQuery, "And");
	}
	
	private static void showDocAtTimeOrData(List<SortedPostingsDataPojo> sortedPostingsList, QueryFunctions query, String docAtTimeOrQuery, Writer streamWriter) {
		fileWriter = new OutputFileWriter();
		TermDataPojo docAtTimeOrData = query.docAtaTimeOrDocs(sortedPostingsList, docAtTimeOrQuery);
		fileWriter.writeDocAtTimeData(streamWriter, docAtTimeOrData, docAtTimeOrQuery, "Or");
		
	}
	
	/**
	 * This method reads the input query file and returns the names of the queries and the query parameters in the queryNames and queryTerms String[] 
	 * respectively.
	 * The queryNames array is used to perform the switch case operation for identifying the query type and 
	 * queryTerms contains the data specific to every queryName which is passed to the QueryFunction() for getting the query results.
	 * @param queries
	 * @return
	 */
	private static String[] getQueries(List<String> queries) {
		String[] queryNames = new String[queries.size()];
		queryTerms = new String[queries.size()];
		int i=0;
		for(String query : queries) {
			String[] temp = query.split(" ");
			queryNames[i] = temp[0];
			for(int j=1; j<temp.length; j++) {
				if(j<temp.length && j==1)
					queryTerms[i] = temp[j];
				else if(j<temp.length)
					queryTerms[i] = queryTerms[i].concat(" ").concat(temp[j]);
			}
			i++;
		}
		return queryNames;
	}
	
	/**
	 * This method is responsible for sorting the data based on documentId and frequency respectively and set the in the LinkedList of SortedPostingsDataPojo().
	 * @param inputDataList
	 * @return
	 */
	private static List<SortedPostingsDataPojo> createSortedDocIdList(List<DocumentPostingsPojo> inputDataList) {
		List<SortedPostingsDataPojo> sortedPostingsList = new LinkedList<SortedPostingsDataPojo>();
		for(DocumentPostingsPojo pojo : inputDataList) {
			SortedPostingsDataPojo postingsDataObj = new SortedPostingsDataPojo();
			OptimizedPostingsDataPojo optimizedDataObj = new OptimizedPostingsDataPojo();
			String term = pojo.getTerm();
			postingsDataObj.setTerm(term);
			optimizedDataObj.setTerm(term);
			optimizedDataObj.setPostingSize(pojo.getListSize());
			List<PostingsPojo> docIdList = new LinkedList<PostingsPojo>();
			List<PostingsPojo> frequencyList = new LinkedList<PostingsPojo>();
			for(PostingsPojo data : pojo.getPostings()) {
				PostingsPojo obj = setDataList(data);
				docIdList.add(obj);
				frequencyList.add(obj);
			}
			Collections.sort(docIdList, new DocIdComparator());
			postingsDataObj.setSortedDocIdList(docIdList);
			optimizedDataObj.setSortedDocIdList(docIdList);
			Collections.sort(frequencyList, new FrequencyComparator());
			postingsDataObj.setSortedFrequencyList(frequencyList);
			optimizedDataObj.setSortedDocIdList(frequencyList);
			sortedPostingsList.add(postingsDataObj);
		}
		return sortedPostingsList;
	}
	
	/**
	 * This method is responsible for sorting the data based on documentId and frequency respectively and set the in the LinkedList of OptimizedPostingsDataPojo().
	 * @param inputDataList
	 * @return
	 */
	private static List<OptimizedPostingsDataPojo> createOptimizedTermDocIdList(List<DocumentPostingsPojo> inputDataList) {
		List<OptimizedPostingsDataPojo> sortedPostingsList = new LinkedList<OptimizedPostingsDataPojo>();
		for(DocumentPostingsPojo pojo : inputDataList) {
			OptimizedPostingsDataPojo optimizedDataObj = new OptimizedPostingsDataPojo();
			String term = pojo.getTerm();
			optimizedDataObj.setTerm(term);
			optimizedDataObj.setPostingSize(pojo.getListSize());
			List<PostingsPojo> docIdList = new LinkedList<PostingsPojo>();
			List<PostingsPojo> frequencyList = new LinkedList<PostingsPojo>();
			for(PostingsPojo data : pojo.getPostings()) {
				PostingsPojo obj = setDataList(data);
				docIdList.add(obj);
				frequencyList.add(obj);
			}
			Collections.sort(docIdList, new DocIdComparator());
			optimizedDataObj.setSortedDocIdList(docIdList);
			Collections.sort(frequencyList, new FrequencyComparator());
			optimizedDataObj.setSortedFrequencyList(frequencyList);
			sortedPostingsList.add(optimizedDataObj);
		}
		return sortedPostingsList;
	}
	
	private static PostingsPojo setDataList(PostingsPojo data) {
		PostingsPojo docIdObj = new PostingsPojo();
		docIdObj.setDocId(data.getDocId());
		docIdObj.setFrequency(data.getFrequency());
		return docIdObj;
	}
}
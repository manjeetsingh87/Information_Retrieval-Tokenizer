package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pojo.OptimizedPostingsDataPojo;
import pojo.PostingsPojo;
import pojo.SortedPostingsDataPojo;
import pojo.TermDataPojo;
import comparator.DocIdComparator;
import comparator.OptimizedDataComparator;

public class QueryFunctions {

	/**
	 * This method creates a postings data list for a given set of terms.
	 * It finds the document and frequency list for all given terms and returns a postings list to the calling class to display
	 * @param postingsDataList
	 * @param query
	 * @return
	 */
	public List<SortedPostingsDataPojo> getPostings(List<SortedPostingsDataPojo> postingsDataList, String query) {
		List<SortedPostingsDataPojo> postingList = new ArrayList<SortedPostingsDataPojo>();
		String[] queryArr = null;
		if(query.contains(" "))
			queryArr = query.split(" ");
		/*used when user enters multiple term data values */
		if(null != queryArr) {
			for(int i=0; i<queryArr.length; i++) {
				for(SortedPostingsDataPojo pojo : postingsDataList) {
					SortedPostingsDataPojo dataItem = getDataItem(pojo, queryArr[i]);
					if(null != dataItem) {
						postingList.add(dataItem);
						break;
					}	
				}
			}
		} /*used when the user enters a single term data value*/else {
			for(SortedPostingsDataPojo pojo : postingsDataList) {
				SortedPostingsDataPojo dataItem = getDataItem(pojo, query);
				if(null != dataItem) {
					postingList.add(dataItem);
					break;
				}
			}
		}
		return postingList;
	}
	
	/**
	 * This method checks if the object term is similar to the list and returns the postings data associated with that object
	 * @param postingObj
	 * @param query
	 * @return
	 */
	private SortedPostingsDataPojo getDataItem(SortedPostingsDataPojo postingObj, String query) {
		if(query.trim().equals(postingObj.getTerm().trim()))
			return postingObj;
		return null;
	}
	
	/**
	 * This method returns a list of all the documents which are common in all the term postings 
	 * It performs an intersection operation i.e it checks of all the posting documents contain a particular term and returns the List of all such documents
	 * ordered in increasing order
	 * @param postingsDataList
	 * @param query
	 * @return
	 */
	public TermDataPojo termAtaTimeAndDocs(List<SortedPostingsDataPojo> postingsDataList, String query) {
		long startTime = System.nanoTime();
		TermDataPojo docsSet = new TermDataPojo();
		List<PostingsPojo> firstDocList = new ArrayList<PostingsPojo>();
		int comparisons = 0;
		int occurance = 1;
		int first = 0;
		List<SortedPostingsDataPojo> termAtTimeAndDocsList = getSelectedDataList(postingsDataList, query);
		for(SortedPostingsDataPojo postingsData : termAtTimeAndDocsList) {
			List<PostingsPojo> termDocData = postingsData.getSortedFrequencyList();
			/*checks if this is the first set of term documents and if true adds all the documents containing this term
			 *  to a temporary list called firstDocList
			 */
			if(first==0) {
				firstDocList.addAll(termDocData);
				first++;
			} else if(!firstDocList.isEmpty()){
				List<PostingsPojo> commonDocsList = new ArrayList<PostingsPojo>();;
				for(PostingsPojo currentDocObj : termDocData) {
					boolean isPresent = false;
					for(PostingsPojo firstObj : firstDocList) {
						if(currentDocObj.getDocId().trim().equalsIgnoreCase(firstObj.getDocId().trim())) {
							/*
							 * this loop checks if the objects in the current term list under iteration are present in the first list.
							 * If yes, it adds to the common list and ned of loop replaces the first list with the common data list since it's the required 
							 * doc id data which is common to all lists							
							 */
							commonDocsList.add(currentDocObj);
							occurance++;
							isPresent = true;
						}
						comparisons++;
						if(isPresent)
							break;
					}
				}
				if(!commonDocsList.isEmpty())
					firstDocList = commonDocsList;
			} else
				break;
		}
		
		Collections.sort(firstDocList, new DocIdComparator());
		long endTime = System.nanoTime();
		double timeDiff = (endTime-startTime)*1000000000.0;
		docsSet.setTime(timeDiff);
		docsSet.setComparisons(comparisons);
		docsSet.setDocumentCount(occurance);
		docsSet.setDocsIdSet(firstDocList);
		return docsSet;
	}

	/**
	 * This method returns a list of all the documents which are common in any of the term postings
	 * @param postingsDataList
	 * @param query
	 * @return
	 */
	public TermDataPojo termAtaTimeOrDocs(List<SortedPostingsDataPojo> postingsDataList, String query) {
		long startTime = System.nanoTime();
		TermDataPojo docsSet = new TermDataPojo();
		List<PostingsPojo> docsList = new ArrayList<PostingsPojo>();
		int comparisons = 0;
		int occurance = 1;
		List<SortedPostingsDataPojo> termAtTimeOrInputList = getSelectedDataList(postingsDataList, query);
		for(SortedPostingsDataPojo pojo : termAtTimeOrInputList) {
			List<PostingsPojo> selectedObj = pojo.getSortedFrequencyList();
			/* This checks if the firstTermList is empty. If yes then it adds the first list to this */
			if(docsList.isEmpty())
				docsList.addAll(selectedObj);
			else {
				for(PostingsPojo temp : selectedObj) {
					PostingsPojo currentDocObj = new PostingsPojo();
					String docId = temp.getDocId().trim();
					currentDocObj.setDocId(docId);
					boolean isPresent = false;
					for(PostingsPojo obj : docsList) {
						if(obj.getDocId().equals(docId)) {
							isPresent = true;
							occurance++;
							break;
						}	
					}
					if(!isPresent)
						docsList.add(currentDocObj);
					
					comparisons++;
				}
			}
		}
		Collections.sort(docsList, new DocIdComparator());
		long endTime = System.nanoTime();
		double timeDiff = (endTime-startTime)*1000000000.0;
		docsSet.setTime(timeDiff);
		docsSet.setComparisons(comparisons);
		docsSet.setDocumentCount(occurance);
		docsSet.setDocsIdSet(docsList);
		return docsSet;
	}
	
	/**
	 * This method performs document at a time comparisons and returns all the documents matching every term data list
	 * It iterates over all the linked lists using their pointers and does a check with the document id's and adds all the common document id's to a commonDocsList
	 * @param postingsDataList
	 * @param query
	 * @return
	 */
	public TermDataPojo docAtATimeAndDocs(List<SortedPostingsDataPojo> postingsDataList, String query) {
		long startTime = System.nanoTime();
		TermDataPojo docsSet = new TermDataPojo();
		List<PostingsPojo> commonDocsList = new ArrayList<PostingsPojo>();
		int comparisons = 0;
		int occurance = 1;
		int next = 1;
		boolean stopIteration = false;
		List<SortedPostingsDataPojo> docAtTimeAndInputList = getSelectedDataList(postingsDataList, query);
		if(docAtTimeAndInputList.size() > 1) {
			Map<Integer, Integer> objectIndexMap = new HashMap<Integer, Integer>();
			/* Get the first linked list term data. This list will be used as reference while iterating the other lists*/
			List<PostingsPojo> selectedObj = docAtTimeAndInputList.get(0).getSortedDocIdList();
			for(PostingsPojo selectedDocIdObj : selectedObj) {
				boolean isDocIdCommon = false;
				String selectedDocId = selectedDocIdObj.getDocId().trim();
				while(docAtTimeAndInputList.listIterator().hasNext()) {
					/* Fetch the other term data lists recursively and compare the document id's to the reference list selectedObj*/
					List<PostingsPojo> nextTermList = docAtTimeAndInputList.listIterator(next).next().getSortedDocIdList();
					boolean isPresent = false;
					int j=0;
					if(!objectIndexMap.isEmpty() && null != objectIndexMap.get(next))
						j = objectIndexMap.get(next);
					for(int i=0; i<nextTermList.size(); i++) {
						String nextObjDocId = nextTermList.get(j).getDocId().trim();
						comparisons++;
						if(nextObjDocId.equals(selectedDocId)) {
							isPresent = true;
							occurance++;
							objectIndexMap.put(next, j+1);
							break;
						} else {
							int objComp = nextObjDocId.compareTo(selectedDocId);
							if(objComp > 0) {
								objectIndexMap.put(next, j);
								break;
							} else if(objComp < 0) {
								j = j+1;
								objectIndexMap.put(next, j);
								
							}	
							isPresent = false;
						}
					}
					if(j==nextTermList.size()) {
						stopIteration = true;
					}	
					if(next<docAtTimeAndInputList.size()-1 && isPresent)
						next++;
					else if(isPresent) {
						isDocIdCommon = true;
						break;
					} else
						break;
				}
				if(isDocIdCommon)
					commonDocsList.add(selectedDocIdObj);
				if(stopIteration)
					break;
			}
		}
		Collections.sort(commonDocsList, new DocIdComparator());
		long endTime = System.nanoTime();
		double timeDiff = (endTime-startTime)*1000000000.0;
		docsSet.setTime(timeDiff);
		docsSet.setComparisons(comparisons);
		docsSet.setDocumentCount(occurance);
		docsSet.setDocsIdSet(commonDocsList);
		return docsSet;
	}
	
	/**
	 * This method does a union comparisons for document at a time comparison and returns all the document id's present in every term list.
	 * It ensures that duplicate document id's a re not present in the final commonDocsList.
	 * @param postingsDataList
	 * @param query
	 * @return
	 */
	public TermDataPojo docAtaTimeOrDocs(List<SortedPostingsDataPojo> postingsDataList, String query) {
		long startTime = System.nanoTime();
		TermDataPojo docsSet = new TermDataPojo();
		List<PostingsPojo> commonDocsList = new ArrayList<PostingsPojo>();
		int comparisons = 0;
		int occurance = 1;
		List<SortedPostingsDataPojo> docAtTimeAndInputList = getSelectedDataList(postingsDataList, query);
		Map<String, Boolean> foundDocIdMap = new HashMap<String, Boolean>();
		Map<Integer, Boolean> isListIteratedMap = new HashMap<Integer, Boolean>();
		List<PostingsPojo> restOfDocIds = new ArrayList<PostingsPojo>();
		Map<String, Boolean> remainingDocIdMap = new HashMap<String, Boolean>();
		Map<Integer, Integer> lastObjectPosforList = new HashMap<Integer, Integer>();
		try {
			if(docAtTimeAndInputList.size() > 1) {
				/* This gets the first postings data list for the first term in the query and uses this as reference to compare to other linked lists */
				List<PostingsPojo> selectedDocObjList = docAtTimeAndInputList.get(0).getSortedDocIdList();
				for(PostingsPojo selectedDocObj : selectedDocObjList) {
					int next = 1;
					String selectedDocId =  selectedDocObj.getDocId().trim();
					int selectedDocObjIndex = selectedDocObjList.indexOf(selectedDocObj);
					while(docAtTimeAndInputList.listIterator().hasNext()) {
						/* This is to check if the current postings list has been iterated completely or not. If it is and the index of the pointer to list
						 * iterators is less that the size of the input list and increments if the conditions are satisfied */
						if(null != isListIteratedMap.get(next) && next<docAtTimeAndInputList.size()-1) {
							next++;
							break;
						} else if(null != isListIteratedMap.get(next))	
							break;
						else {
							List<PostingsPojo> nextTermList = docAtTimeAndInputList.listIterator(next).next().getSortedDocIdList();
							if(null != lastObjectPosforList.get(next))
								nextTermList = nextTermList.subList(lastObjectPosforList.get(next), nextTermList.size()-1);
							for(PostingsPojo nextTermObj : nextTermList) {
								boolean isFound = false;
								boolean isAddedtoRestList = false;
								String nextTermDocId = nextTermObj.getDocId().trim();
								int objIndex = nextTermList.indexOf(nextTermObj);
								PostingsPojo objToAddToList = new PostingsPojo();
								occurance++;
								/* Here we compare the document id in the first linked it to the next one and add the smaller document id 
								 * to the final commonDocsList. The larger document id is placed in a temporary file and later all these document id's from
								 * the temporary file are added to the final commonDocsList */
								if(Integer.valueOf(nextTermDocId).compareTo(Integer.valueOf(selectedDocId)) <= 0) {
									isFound = true;
									objToAddToList.setDocId(nextTermDocId);;
								} else if(Integer.valueOf(selectedDocId).compareTo(Integer.valueOf(nextTermDocId)) <= 0) {
									isFound = true;
									objToAddToList.setDocId(selectedDocId);
								}
								if(objIndex == nextTermList.size()-1 && isFound)
									isListIteratedMap.put(next, true);
								if(isFound && null == foundDocIdMap.get(objToAddToList.getDocId())) {
									foundDocIdMap.put(objToAddToList.getDocId(), true);
									commonDocsList.add(objToAddToList);
								} 
								if(null == foundDocIdMap.get(nextTermDocId) && null == remainingDocIdMap.get(nextTermDocId)) {
									restOfDocIds.add(nextTermObj);
									isAddedtoRestList = true;
									remainingDocIdMap.put(nextTermDocId, true);
								} else if(null == foundDocIdMap.get(selectedDocId) && null == remainingDocIdMap.get(selectedDocId)) {
									restOfDocIds.add(selectedDocObj);
									isAddedtoRestList = true;
									remainingDocIdMap.put(selectedDocId, true);
								}
								
								if(!isFound && isAddedtoRestList && objIndex <= nextTermList.size()-1)
									lastObjectPosforList.put(objIndex, next);
								
								if(null != foundDocIdMap.get(nextTermDocId) && null != remainingDocIdMap.get(nextTermDocId)) {
									restOfDocIds.remove(nextTermObj);
									remainingDocIdMap.remove(nextTermDocId);
								} else if(null != foundDocIdMap.get(selectedDocId) && null != remainingDocIdMap.get(selectedDocId)) {
									restOfDocIds.remove(selectedDocObj);
									remainingDocIdMap.remove(selectedDocId);
								}
								comparisons++;
							}
							if(next<docAtTimeAndInputList.size()-1)
								next++;
							else 
								break;
						}
					}
					if(null == foundDocIdMap.get(selectedDocId) && null == remainingDocIdMap.get(selectedDocId))
						restOfDocIds.add(selectedDocObj);
					if(selectedDocObjIndex == selectedDocObjList.size()-1)
						break;
				}
			}
			if(!restOfDocIds.isEmpty())
				commonDocsList.addAll(restOfDocIds);
			Collections.sort(commonDocsList, new DocIdComparator());
			long endTime = System.nanoTime();
			double timeDiff = (endTime-startTime)*1000000000.0;
			docsSet.setTime(timeDiff);
			docsSet.setComparisons(comparisons);
			docsSet.setDocumentCount(occurance);
			docsSet.setDocsIdSet(commonDocsList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return docsSet;
	}
	
	private List<SortedPostingsDataPojo> getSelectedDataList(List<SortedPostingsDataPojo> inputList, String queries) {
		List<SortedPostingsDataPojo> selectedDataList = new LinkedList<SortedPostingsDataPojo>();
		try {
			String[] queryArr = null;
			queryArr = queries.split(" ");
			if(queryArr.length > 1) {
				for(int i=0; i<queryArr.length; i++) {
					for(SortedPostingsDataPojo pojo : inputList) {
						if(queryArr[i].trim().equals(pojo.getTerm().trim())) {
							selectedDataList.add(pojo);
						}	
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return selectedDataList;
	}
	
	private List<OptimizedPostingsDataPojo> getSelectedTermsDataList(List<OptimizedPostingsDataPojo> inputList, String queries) {
		List<OptimizedPostingsDataPojo> selectedDataList = new LinkedList<OptimizedPostingsDataPojo>();
		try {
			String[] queryArr = null;
			queryArr = queries.split(" ");
			if(queryArr.length > 1) {
				for(int i=0; i<queryArr.length; i++) {
					for(OptimizedPostingsDataPojo pojo : inputList) {
						if(queryArr[i].trim().equals(pojo.getTerm().trim())) {
							selectedDataList.add(pojo);
						}	
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return selectedDataList;
	}
	
	/**
	 * This method returns the optimized comparisons 
	 * @param dataPojo
	 * @param query
	 * @return
	 */
	public int optimizedTermAtTimeAndComparisons(List<OptimizedPostingsDataPojo> dataPojo, String queries) {
		int comparisons = 0;
		int first = 0;
		List<PostingsPojo> firstDocList = new ArrayList<PostingsPojo>();
		try {
			List<OptimizedPostingsDataPojo> optimalTermAtTimeList = getSelectedTermsDataList(dataPojo, queries);
			Collections.sort(optimalTermAtTimeList, new OptimizedDataComparator());
			for(OptimizedPostingsDataPojo postingsData : optimalTermAtTimeList) {
				List<PostingsPojo> termDocData = postingsData.getSortedFrequencyList();
				/*checks if this is the first set of term documents and if true adds all the documents containing this term
				 *  to a temporary list called firstDocList
				 */
				if(first==0) {
					firstDocList.addAll(termDocData);
					first++;
				} else if(!firstDocList.isEmpty()){
					List<PostingsPojo> commonDocsList = new ArrayList<PostingsPojo>();;
					for(PostingsPojo currentDocObj : termDocData) {
						boolean isPresent = false;
						for(PostingsPojo firstObj : firstDocList) {
							if(currentDocObj.getDocId().trim().equalsIgnoreCase(firstObj.getDocId().trim())) {
								commonDocsList.add(currentDocObj);
								isPresent = true;
							}
							comparisons++;
							if(isPresent)
								break;
						}					}
					if(!commonDocsList.isEmpty())
						firstDocList = commonDocsList;
				} else
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return comparisons;
	}
	
	/**
	 * This method returns the optimized comparisons 
	 * @param dataPojo
	 * @param query
	 * @return
	 */
	public int optimizedTermAtTimeOrComparisons(List<OptimizedPostingsDataPojo> dataPojo, String queries) {
		int comparisons = 0;
		List<PostingsPojo> firstDocList = new ArrayList<PostingsPojo>();
		try {
			List<OptimizedPostingsDataPojo> optimalTermAtTimeList = getSelectedTermsDataList(dataPojo, queries);
			Collections.sort(optimalTermAtTimeList, new OptimizedDataComparator());
			for(OptimizedPostingsDataPojo pojo : optimalTermAtTimeList) {
				List<PostingsPojo> selectedObj = pojo.getSortedFrequencyList();
				/*This checks if the firstTermList is empty. If yes then it adds the first list to this */
				if(firstDocList.isEmpty())
					firstDocList.addAll(selectedObj);
				else {
					for(PostingsPojo temp : selectedObj) {
						PostingsPojo currentDocObj = new PostingsPojo();
						String docId = temp.getDocId().trim();
						currentDocObj.setDocId(docId);
						boolean isPresent = false;
						for(PostingsPojo obj : firstDocList) {
							if(obj.getDocId().equals(docId)) {
								isPresent = true;
								break;
							}	
						}
						if(!isPresent)
							firstDocList.add(currentDocObj);
						
						comparisons++;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return comparisons;
	}
}
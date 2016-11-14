package de.uni_koeln.spinfo.textengineering.ir.boole;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uni_koeln.spinfo.textengineering.ir.basic.Corpus;
import de.uni_koeln.spinfo.textengineering.ir.basic.InformationRetrieval;
import de.uni_koeln.spinfo.textengineering.ir.basic.Work;

public class InvertedIndex implements InformationRetrieval {
	
	Map<String, Set<Integer>> index;
	
	public InvertedIndex(Corpus corpus) {
		
		index = index(corpus);
	}
	
	private Map<String, Set<Integer>> index(Corpus corpus) {
		
		HashMap<String, Set<Integer>> invIndex = new HashMap<String, Set<Integer>>();
		
		List<Work> works = corpus.getWorks();
		
		for (int i = 0; i < works.size(); i++) {
			Work work = works.get(i);
			List<String> terms = Arrays.asList(work.getText().split("\\s+"));
			
			for (String term : terms) {
				Set<Integer> postings = invIndex.get(term);
				if (postings == null) {
					postings = new HashSet<>();
					invIndex.put(term, postings);
				}
				
				postings.add(i);
			}
		}
		
		return invIndex;
	}

	@Override
	public Set<Integer> search(String query) {
		
		List<String> queries = Arrays.asList(query.split("\\s+"));
		
		ArrayList<Set<Integer>> allPostingsLists = new ArrayList<>();
		
		for (String q : queries) {
			Set<Integer> postings = index.get(q);
			allPostingsLists.add(postings);
		}
		
		Set<Integer> result = allPostingsLists.get(0);
		
		for (Set<?> postings : allPostingsLists) {
			result.retainAll(postings);
			//result.addAll(postings);
		}
		
		return result;
	}

}

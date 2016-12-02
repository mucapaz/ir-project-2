package search;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import index.Index;

public class SearchIndex {
	
	private Index index;
	private TfIdf tfIdf;
	
	public SearchIndex(Index index){
		this.index = index;
		this.tfIdf = new TfIdf(index);		
	}
	
//	public String[] rankedSearch(List<String> query){
//		
//		return null;
//	}
	
	public String[] search(List<String> query){
		Set<String> docs = new HashSet<String>();
		
		for(String term : query){
			if(isUsefulTerm(term)){
				for(Integer at : index.getDocMap().keySet()){
					if(!docs.contains(index.getDocMap().get(at)) 
							&& tfIdf.tfIdfValue(term, at) > 0.0){
						docs.add(index.getDocMap().get(at));
					}
						
				}
			}			
		}
		
		return docs.toArray(new String[0]);
	}
	
	public boolean isUsefulTerm(String term){
		return index.getIndexMap().containsKey(term);
	}
	
}

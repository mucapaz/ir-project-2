package search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import index.Index;

public class SearchIndex {

	class RankedDoc{

		private String doc;
		private Integer at;

		private double rank = 0.0;

		public RankedDoc(String doc, Integer at){
			this.setDoc(doc);
			this.setAt(at);
		}

		public String getDoc() {
			return doc;
		}

		public void setDoc(String doc) {
			this.doc = doc;
		}

		public Integer getAt() {
			return at;
		}

		public void setAt(Integer at) {
			this.at = at;
		}

		public double getRank() {
			return rank;
		}

		public void setRank(double rank) {
			this.rank = rank;
		}		
	}
	
	private Index index;
	private TfIdf tfIdf;
	
	public SearchIndex(Index index){
		this.index = index;
		this.tfIdf = new TfIdf(index);		
	}

	public String[] rankedSearch(List<String> query){
		Set<String> docs = new HashSet<String>();
		List<RankedDoc> ar = new ArrayList<RankedDoc>();

		query = usefullTerms(query);

		String doc;
		for(String term : query){
			for(Integer at : index.getDocMap().keySet()){
				doc = index.getDocMap().get(at);
				if(!docs.contains(doc) 
						&& tfIdf.tfIdfValue(term, at) > 0.0){
					docs.add(doc);
					ar.add(new RankedDoc(doc, at));
				}
			}
		}	

		double[] scores = new double[ar.size()];
		double[] l1 = new double[ar.size()];
		double[] l2 = new double[ar.size()];

		double tfIdfQuery = 0.0;
		double tfIdfDocument;

		for(String term : query){
			tfIdfQuery = tfIdf.query(term, query);

			for(int x=0;x< ar.size();x++){	
				tfIdfDocument = tfIdf.tfIdfValue(term, ar.get(x).at);
				scores[x] += tfIdfDocument * tfIdfQuery;
				l1[x] += tfIdfDocument*tfIdfDocument;
				l2[x] += tfIdfQuery*tfIdfQuery;
			}
		}

		for(int x=0;x<scores.length;x++){
			scores[x] = scores[x]/(Math.sqrt(l1[x])*Math.sqrt(l2[x]));
			ar.get(x).setRank(scores[x]);
		}
		
		Comparator<RankedDoc> comparator = new Comparator<RankedDoc>() {
		    @Override
		    public int compare(RankedDoc left, RankedDoc right) {
		    	if(left.rank == right.rank){
		    		return left.at - right.at;
		    	}else{
		    		if(left.rank > right.rank) return -1;
		    		else return 1;
		    	}
		    }
		};
		
		Collections.sort(ar, comparator);
	
		String[] ret = new String[ar.size()];
		for(int x=0;x<ar.size();x++){
			ret[x] = ar.get(x).getDoc();
		}
		
		return ret;
	}



	public String[] search(List<String> query){
		Set<String> docs = new HashSet<String>();

		query = usefullTerms(query);

		for(String term : query){
			for(Integer at : index.getDocMap().keySet()){
				if(!docs.contains(index.getDocMap().get(at)) 
						&& tfIdf.tfIdfValue(term, at) > 0.0){
					docs.add(index.getDocMap().get(at));
				}

			}

		}

		return docs.toArray(new String[0]);
	}

	public List<String> usefullTerms(List<String> query){
		List<String> ret = new ArrayList<String>();

		for(String q : query){
			if(isUsefulTerm(q)) ret.add(q);
		}

		return ret;		
	}

	public boolean isUsefulTerm(String term){
		return index.getIndexMap().containsKey(term);
	}

}

package search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import index.Index;

public class TfIdf {

	private double[][] tf;
	private double[][] idf;

	private Index index;
	private Map<String, Integer> termPos;


	public TfIdf(Index index){
		this.index = index;
		this.tf = new double[index.getTerms().size()][index.getDocMap().size()];
		this.idf = new double[index.getTerms().size()][index.getDocMap().size()];
		this.termPos = new HashMap<String, Integer>();
		process();
	}

	private void process() {
		int x=0,y=0;
		double audIdf = 0;
		for(String term : index.getTerms()){
			y=0;
			for(Integer doc : index.getDocMap().keySet()){
				if(y == 0)
					audIdf = idf(term);
				
				tf[x][y] = tf(term,doc);
				idf[x][y] = audIdf;

				y++;
			}
			termPos.put(term, x);
			x++;
		}	
	}

	private double idf(String term){
		double ret = index.getDocMap().size()/index.getIndexMap().get(term).size();

		return Math.log(ret);
	}

	private double tf(String term, Integer doc){
		double ret = 0.0;

		for(Integer at : index.getIndexMap().get(term))
			if(at.equals(doc)) ret+=1.0;

		return ret;		
	}

	public double tfIdfValue(String term, Integer doc){

		return tf[termPos.get(term)][doc] * idf[termPos.get(term)][doc];
	}

	public double query(String term, List<String> query) {
		
		double tf = 0.0;
	
		for(String q : query){
			if(term.equals(q))tf += 1.0;
		}
		
		double n = 1 + index.getDocMap().size();
		double d = tf>=0.0 ? 1 : 0; 
		
		d += index.getIndexMap().get(term).size();
		
		double idf = Math.log(n/d);
		
		return tf*idf;
	}
}

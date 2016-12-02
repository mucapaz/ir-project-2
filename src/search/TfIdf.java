package search;

import java.util.HashMap;
import java.util.Map;

import index.Index;

public class TfIdf {
	
	private double[][] tfIdf;
	private Index index;
	private Map<String, Integer> termPos;
	
	
	public TfIdf(Index index){
		this.index = index;
		this.tfIdf = new double[index.getTerms().size()][index.getDocMap().size()];
		this.termPos = new HashMap<String, Integer>();
		process();
	}
	
	private void process() {
		int x=0,y=0;
		double idf = 0;
		for(String term : index.getTerms()){
			y=0;
			for(Integer doc : index.getDocMap().keySet()){
				if(y == 0){
					idf = idf(term);
					tfIdf[x][y] = tf(term,doc)*idf;
				}else{
					tfIdf[x][y] = tf(term,doc)*idf;
				}
				
				if(tfIdf[x][y] != 0){
					System.out.println(tfIdf);
				}
				
				y++;
			}
			
			termPos.put(term, x);
			x++;
		}	
	}
	
	private double idf(String term){
		double ret = 1 + 
				index.getDocMap().size()/index.getIndexMap().get(term).size();
		
		return Math.log(ret);
	}
	
	private double tf(String term, Integer doc){
		double ret = 0.0;
		
		for(Integer at : index.getIndexMap().get(term))
			if(at.equals(doc)) ret+=1.0;
		
		return ret;		
	}

	public double tfIdfValue(String term, Integer doc){
	
		return tfIdf[termPos.get(term)][doc];
	}
	
	public double[][] getTfIdf() {
		return tfIdf;
	}

	public void setTfIdf(double[][] tfIdf) {
		this.tfIdf = tfIdf;
	}
	
}

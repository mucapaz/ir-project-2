package index;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Index {

	private Map<String, List<Integer>> indexMap;
	private Map<Integer, String> docMap;
	
	public static void main(String[] args) throws IOException{
		Index index = Index.createIndexFromDocuments(new File("documents/processed/"));
		index.saveIndex(new File("index/default/"), false);
		
		
		
//		Index i2 = Index.createIndexFromIndexFiles(new File("index/default/indexMap"),
//				new File("index/default/docMap"), false);
//		
//		
//		i2.saveIndex(new File("index/dois/"), false);
		
	}	
	
	public static Index createIndexFromDocuments(File directory) 
			throws FileNotFoundException{
		
		Index index = new Index();

		File[] docs = directory.listFiles();

		int x=0;
		for(File doc : docs){
			index.getDocMap().put(x, doc.getAbsolutePath());
			index.addDocToIndex(doc, x);
			x++;
		}
		
		index.sortIndexMap();
		
		return index;
	}
	
	public static Index createIndexFromIndexFiles(File indexMapFile, 
			File docMapFile, boolean compressed) throws FileNotFoundException{
		
		Index index = new Index();
		
		Scanner in = new Scanner(indexMapFile);
		
		String label;
		String[] input;
		int last, at;
		while(in.hasNextLine()){
			input = in.nextLine().split(" ");
			index.getIndexMap().put(input[0], new ArrayList<Integer>());
			last = -1;
			
			for(int x=1;x<input.length;x++){
				
				at = Integer.parseInt(input[x]);
				if(last == -1 || !compressed){
					index.getIndexMap().get(input[0]).add(at);
					last = at;
				}else{
					index.getIndexMap().get(input[0]).add(at + last);
					last = at + last;
				}
				
			}
		}

		
		in = new Scanner(docMapFile);
		
		while(in.hasNextLine()){
			input = in.nextLine().split(" ");
			index.getDocMap().put(Integer.parseInt((input[0])), input[1]);
		}
		
		return index;
	}
	
	
	private Index(){
		setIndexMap(new HashMap<String, List<Integer>>());
		setDocMap(new HashMap<Integer, String>());
	}
	
	private void sortIndexMap() {
		for(String label : getIndexMap().keySet())
			Collections.sort(getIndexMap().get(label));
	}

	public void addDocToIndex(File doc, int docNumber) 
			throws FileNotFoundException{

		Scanner in = new Scanner(doc);
		String[] inputs;
		String label;

		while(in.hasNextLine()){
			inputs = in.nextLine().split(" ");

			for(int x=1;x<inputs.length;x++){
				label = createLabel(inputs[0],inputs[x]);

				if(!getIndexMap().containsKey(label)){
					getIndexMap().put(label, new ArrayList<Integer>());
					getIndexMap().get(label).add(docNumber);
				}else{
					getIndexMap().get(label).add(docNumber);
				}
			}
		}

	}

	public void saveIndex(File directory, boolean compress) throws IOException{
		if(!directory.exists()){
			directory.mkdirs();
		}
		
		saveIndexMap(new File(directory.getAbsolutePath()+"/indexMap"), compress);
		saveDocMap(new File(directory.getAbsolutePath()+"/docMap"));
	}

	public void saveIndexMap(File file, boolean compress) throws IOException{
		FileWriter pw = new FileWriter(file);
		
		int last;
		for(String label : getIndexMap().keySet()){
			pw.write(label);
			last = -1;
			for(Integer docNumber : getIndexMap().get(label)){
				if(last == -1 || !compress) pw.write(" " + docNumber);
				else pw.write(" " + (docNumber - last));
				last = docNumber;
			}
			pw.write("\n");
		}
		pw.flush();
		pw.close();
	}
	
	public void saveDocMap(File file) throws IOException{
		FileWriter pw = new FileWriter(file);

		for(Integer docNumber : getDocMap().keySet()){
			pw.write(docNumber + " " + getDocMap().get(docNumber));
			pw.write("\n");
		}
		pw.flush();
		pw.close();
	}
	

	public String createLabel(String s1, String s2){
		return "[" + s1 + "].[" + s2 +"]";
	}
	
	public Set<String> getTerms(){
		return getIndexMap().keySet();
	}
	
	public Map<Integer, String> getDocMap(){
		return docMap;
	}

	public Map<String, List<Integer>> getIndexMap() {
		return indexMap;
	}

	public void setIndexMap(Map<String, List<Integer>> indexMap) {
		this.indexMap = indexMap;
	}

	public void setDocMap(Map<Integer, String> docMap) {
		this.docMap = docMap;
	}
	
}

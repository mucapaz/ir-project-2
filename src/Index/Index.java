package Index;

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

public class Index {

	Map<String, List<Integer>> indexMap;
	Map<Integer, String> docMap;

	public static Index createIndexFromDocuments(File directory) 
			throws FileNotFoundException{
		
		Index index = new Index();

		File[] docs = directory.listFiles();

		int x=0;
		for(File doc : docs){
			index.docMap.put(x, doc.getAbsolutePath());
			index.addDocToIndex(doc, x);
			x++;
		}
		
		index.sortIndexMap();
		
		return index;
	}
	
	
	private Index(){
		indexMap = new HashMap<String, List<Integer>>();
		docMap = new HashMap<Integer, String>();
	}
	
	private void sortIndexMap() {
		for(String label : indexMap.keySet())
			Collections.sort(indexMap.get(label));
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

				if(!indexMap.containsKey(label)){
					indexMap.put(label, new ArrayList<Integer>());
					indexMap.get(label).add(docNumber);
				}else{
					indexMap.get(label).add(docNumber);
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
		for(String label : indexMap.keySet()){
			pw.write(label);
			last = -1;
			for(Integer docNumber : indexMap.get(label)){
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

		for(Integer docNumber : docMap.keySet()){
			pw.write(docNumber + " " + docMap.get(docNumber));
			pw.write("\n");
		}
		pw.flush();
		pw.close();
	}
	

	public static String createLabel(String s1, String s2){
		return "[" + s1 + "].[" + s2 +"]";
	}

}

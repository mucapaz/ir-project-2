import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import index.Index;
import search.SearchIndex;

public class Main {

	public static void main(String[] args) throws FileNotFoundException{
		
		Index index = Index.createIndexFromDocuments(new File("documents/processed/"));
		SearchIndex search = new SearchIndex(index); 
		
		List<String> query = new ArrayList<String>();
		query.add("[modelo].[soul]");
		query.add("[modelo].[soul]");
		query.add("[versao].[t5]");
		
		String[] res = search.rankedSearch(query);
		
		for(String i : res){
			System.out.println(i);
		}
		
	}
	
}

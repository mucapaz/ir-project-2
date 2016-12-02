package Index;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import Pre.Instance;

public class Creator {
			
	public static void main(String[] args) throws IOException{
		Index index = Index.createIndexFromDocuments(new File("documents/processed/"));
		index.saveIndex(new File("index/default/"), false);	
	}

}

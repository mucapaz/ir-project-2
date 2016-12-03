package pre;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Instance {
	
	private Map<String, List<String>> attrValueMap;
	
	public Instance(File fileInstance) throws FileNotFoundException{
		
		attrValueMap = new HashMap<String, List<String>>();
		
		Scanner in = new Scanner(fileInstance);
		
		String[] attrValue;
		while(in.hasNextLine()){
			String line = in.nextLine().toLowerCase();
			attrValue = line.split(" ");
			
			if(attrValueMap.containsKey(attrValue[0])){
				attrValueMap.get(attrValue[0]).add(attrValue[1]);
			}else{
				attrValueMap.put(attrValue[0], new ArrayList<String>());
				attrValueMap.get(attrValue[0]).add(attrValue[1]);
			}
		}
	}
	
	public String[] attrArray(){
		return attrValueMap.keySet().toArray(new String[0]);
	}

	public void keep(Set<String> keepSet) {
		String[] ar = attrValueMap.keySet().toArray(new String[0]);
		
		for(String s : ar){
			if(!keepSet.contains(s))attrValueMap.remove(s);
		}
	}

	public void saveTo(File file) throws IOException {

		FileWriter fw = new FileWriter(file);

		String[] ar = attrValueMap.keySet().toArray(new String[0]);
		
		List<String> values;
		
		for(String attr : ar){
			values = attrValueMap.get(attr);
			
			fw.write(attr);
			for(String value : values){
				fw.write(" " + value);
			}
			fw.write("\n");
		}
		fw.close();
		
	}
}

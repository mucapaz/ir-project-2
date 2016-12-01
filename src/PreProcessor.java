import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class PreProcessor {

	public static void main(String[] args) throws IOException{
		File dir = new File("documents/raw/");

		Instance[] instances = readInstances(dir);

		Set<String> keep = new HashSet<String>();
		keep.add("marca");
		keep.add("modelo");
		keep.add("cilindradas");
		keep.add("versao");
		keep.add("ano");
		
		int x =0;
		
		for(Instance inst : instances) {
			inst.keep(keep);
			inst.saveTo(new File("documents/processed/" + x++));
		}
			
	}

	public static Instance[] readInstances(File dir) throws FileNotFoundException{
		Instance[] instances = new Instance[dir.listFiles().length];

		int x=0;
		for(File file : dir.listFiles()){
			instances[x++] = new Instance(file);
		}

		return instances;
	}

	public static void mostUsedAttrs(Instance[] instances, int number) throws FileNotFoundException{
		String[] mostUsed = new String[number];

		Map<String, Integer> counter  = new Hashtable<String, Integer>();

		int count;
		for(Instance instance : instances){
			for(String attr : instance.attrArray()){
				count = counter.containsKey(attr) ? counter.get(attr) : 0;
				counter.put(attr, count + 1);
			}
		}

		List<Integer> timesUsed = new ArrayList(counter.values());
		Collections.sort(timesUsed);
		Collections.reverse(timesUsed);
		
		String[] candidates = counter.keySet().toArray(new String[0]);
		
		Set<String> in = new HashSet<String>();
		
		for(int x=0;x<number;x++){
			for(int y=0;y<candidates.length;y++){
				if(timesUsed.get(x) == counter.get(candidates[y]) && 
						!in.contains(candidates[y]) ){
					mostUsed[x] = candidates[y];
					in.add(candidates[y]);
				}
			}
		}
		
	}

}

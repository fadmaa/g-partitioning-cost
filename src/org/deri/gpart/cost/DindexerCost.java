package org.deri.gpart.cost;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class DindexerCost {
	private static int c = 1;

	private static Map<String, Set<String>> readPartitions(String path) throws Exception{
		Map<String, Set<String>> tripleToPartitions = new HashMap<String, Set<String>>();
		for(File file: files(path)){
			Scanner scan = new Scanner(file);
			while(scan.hasNextLine()){
				String line = scan.nextLine();
				String triple = line.trim().substring(0, line.length()-2).trim();
				if(! tripleToPartitions.containsKey(triple)){
					tripleToPartitions.put(triple, new HashSet<String>());
				}
				tripleToPartitions.get(triple).add(file.getName());
			}
			scan.close();
		}
		return tripleToPartitions;
	}
	
	private static File[] files(String path) {
		File dir = new File(path);
		return dir.listFiles();
	}
	
	public static void main(String[] args) throws Exception{
		//PARTITIONS_DIR MODEL_FILE QUERY num_triples_res
		String dir = args[0];
		Map<String, Set<String>> tripletoPartition = readPartitions(dir);
		//execute query
		ResultSet res = QueryExecutor.executeQuery(Utility.readNT(args[1]), args[2]); 
		int cost  = 0;
		int numOfTriples = Integer.valueOf(args[3]);
		while(res.hasNext()){
			Set<String> solSetTriples = new HashSet<String>();
			QuerySolution sol = res.nextSolution();
			for(int i=1; i <= numOfTriples; i+=1){
				solSetTriples.add(Utility.tripleAsString(sol.getResource("s" + i), sol.getResource("p" + i), sol.get("o" + i)));
			}
			List<String> solListTriples = new ArrayList<String>(solSetTriples);
			for(int i=0; i < solListTriples.size()-1; i += 1){
				String t1 = solListTriples.get(i);
				Set<String> set1 = tripletoPartition.get(t1);
				for(int j= i+1; j < solListTriples.size(); j+=1) {
					String t2 = solListTriples.get(j);
					Set<String> set2 = new HashSet<String>(tripletoPartition.get(t2));
					set2.retainAll(set1);
					if(set2.isEmpty()) {
						cost += c;
					}/* else {
						System.out.println(t1 + " and " + t2 + " are both in " + set2);
					}*/
				}
			}
		}
		System.out.println("****************************************");
		System.out.println("cost is " + cost);
		//for each solution
		    //for each pair of triples t1,t2 in the solution
		        //cost += if(tripletoPartition(t1) intersect tripletoPartition(t2) is not Phi) 0 else c
	}
}

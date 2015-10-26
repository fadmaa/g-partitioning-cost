package org.deri.gpart.cost;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class RDF2Metis {

	public static void main(String[] args) throws Exception{
		//NT_file output_graph output_ids
		BufferedWriter gout = new BufferedWriter(new FileWriter(args[1]));
		BufferedWriter nout = new BufferedWriter(new FileWriter(args[2]));
		Map<String, Long> ids = new HashMap<String, Long>();
		Map<Long, Set<Long>> adjacency = new TreeMap<Long, Set<Long>>();
		long counter = 0;
		Model model = Utility.readNT(args[0]);
		StmtIterator iter =  model.listStatements();
		while(iter.hasNext()){
			Statement stmt = iter.nextStatement();
			String s = "<" + stmt.getSubject().getURI() + ">";
			String o = stmt.getObject().isResource()? "<" + stmt.getObject().asResource().getURI() + ">" : stmt.getObject().toString();
			if(! ids.containsKey(s)){
				counter += 1;
				ids.put(s, counter);
			}
			if(! ids.containsKey(o)){
				counter += 1;
				ids.put(o, counter);
			}
			if(!adjacency.containsKey(ids.get(s))){
				adjacency.put(ids.get(s), new HashSet<Long>());
			}
			if(!adjacency.containsKey(ids.get(o))){
				adjacency.put(ids.get(o), new HashSet<Long>());
			}
			adjacency.get(ids.get(s)).add(ids.get(o));
			adjacency.get(ids.get(o)).add(ids.get(s));
		}
		long edgeCounter = 0;
		for(long i=1; i<=adjacency.keySet().size(); i += 1){
			edgeCounter += adjacency.get(i).size();
		}
		gout.write(adjacency.keySet().size() + " " + edgeCounter/2 + "\n");
		for(long i=1; i<=adjacency.keySet().size(); i += 1){
			for(Long l:adjacency.get(i)){
				gout.write(l + " ");
			}
			gout.write("\n");
		}
		
		for(String id: ids.keySet()){
			nout.write( ids.get(id)+ "\t" + id + "\n");
		}
		gout.close();
		nout.close();
	}
}

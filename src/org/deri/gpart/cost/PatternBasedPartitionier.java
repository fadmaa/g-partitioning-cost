package org.deri.gpart.cost;

import java.io.IOException;
import java.util.Iterator;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class PatternBasedPartitionier extends Partitioner{

	private String query;
	protected PatternBasedPartitionier(int numOfPartitions, String baseDir, String filename, String query)
			throws IOException {
		super(numOfPartitions, baseDir, filename);
		this.query = query;
	}

	@Override
	protected void processData() throws Exception {
		ResultSet res = QueryExecutor.executeQuery(model, query);
		while(res.hasNext()){
			QuerySolution sol = res.nextSolution();
			int numOfTriples = 1;
			Iterator<String> iter = sol.varNames();
			while(iter.hasNext()) {
				iter.next();
				numOfTriples += 1;
			}
			numOfTriples /= 3;
			int partition = Math.abs(sol.getResource("s1").getURI().hashCode() % numOfPartitions);
			for(int i=1; i <= numOfTriples; i+=1){
				RDFNode objNode = sol.get("o" + i);
				if(objNode==null) continue;
				String t = Utility.tripleAsString(sol.getResource("s" + i), sol.getResource("p" + i), objNode);
				writeTriple(t, partition);
			}
		}
	}
	
	public static void main(String[] args) throws Exception{
		//Filename outputdir numOfPartitions query
		int numOfPartitions = Integer.parseInt(args[2]);
		PatternBasedPartitionier partitioner = new PatternBasedPartitionier(numOfPartitions, args[1], args[0], args[3]);
		partitioner.run();
	}

}

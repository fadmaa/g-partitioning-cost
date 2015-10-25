package org.deri.gpart.cost;

import java.io.IOException;

import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class HashbasedPartitioner extends Partitioner{

	protected HashbasedPartitioner(int numOfPartitions, String baseDir, String filename)
			throws IOException {
		super(numOfPartitions, baseDir, filename);
	}

	@Override
	protected void processData() throws Exception{
		StmtIterator iter = model.listStatements();
		while(iter.hasNext()){
			Statement stmt = iter.nextStatement();
			String t = Utility.tripleAsString(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
			int partition = Math.abs(t.hashCode() % numOfPartitions);
			writeTriple(t, partition);
		}
	}
	
	public static void main(String[] args) throws Exception{
		//Filename outputdir numOfPartitions
		int numOfPartitions = Integer.parseInt(args[2]);
		HashbasedPartitioner partitioner = new HashbasedPartitioner(numOfPartitions, args[1], args[0]);
		partitioner.run();
	}
}

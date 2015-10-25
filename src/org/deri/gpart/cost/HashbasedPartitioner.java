package org.deri.gpart.cost;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class HashbasedPartitioner {

	public static void main(String[] args) throws Exception{
		//Filename outputdir numOfPartitions
		int numOfPartitions = Integer.parseInt(args[2]);
		List<BufferedWriter> fouts = new ArrayList<BufferedWriter>();
		for(int i=0; i<numOfPartitions; i+= 1){
			File f = new File(args[1] + "/part-" + i);
			//f.createNewFile();
			BufferedWriter fout = new BufferedWriter(new FileWriter(f));
			fouts.add(fout);
		}
		
		Model model = Utility.readNT(args[0]);
		
		StmtIterator iter = model.listStatements();
		while(iter.hasNext()){
			Statement stmt = iter.nextStatement();
			String t = Utility.tripleAsString(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
			int partition = Math.abs(t.hashCode() % numOfPartitions);
			fouts.get(partition).write(t + " .\n");
		}
		
		//close all outputstreams
		for(BufferedWriter fout: fouts){
			fout.close();
		}
		
	}
}

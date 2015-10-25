package org.deri.gpart.cost;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;

public abstract class Partitioner {

	private List<BufferedWriter> fouts;
	protected Model model;
	protected int numOfPartitions;
	
	protected Partitioner(int numOfPartitions, String baseDir, String filename) throws IOException{
		model = Utility.readNT(filename);
		this.numOfPartitions = numOfPartitions;
		fouts = new ArrayList<BufferedWriter>();
		for(int i=0; i<numOfPartitions; i+= 1){
			File f = new File(baseDir + "/part-" + i);
			//f.createNewFile();
			BufferedWriter fout = new BufferedWriter(new FileWriter(f));
			fouts.add(fout);
		}
	}
	
	protected void teardown() throws IOException{
		//close all outputstreams
		for(BufferedWriter fout: fouts){
			fout.close();
		}
	}
	
	protected void writeTriple(String triple, int partition) throws IOException{
		fouts.get(partition).write(triple + " .\n");
	}
	
	public void run() throws Exception{
		processData();
		teardown();
	}
	
	abstract protected void processData() throws Exception;
}

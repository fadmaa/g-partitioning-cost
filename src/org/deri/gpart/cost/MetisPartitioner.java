package org.deri.gpart.cost;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class MetisPartitioner extends Partitioner{
	Map<Long, String> idsMap = new HashMap<Long, String>(); 
	private String metisFilename;
	
	protected MetisPartitioner(int numOfPartitions, String baseDir, String filename, String metisFile, String idsFilename) throws IOException {
		super(numOfPartitions, baseDir, filename);
		this.metisFilename = metisFile;
		File idsFile = new File(idsFilename);
		Scanner scan = new Scanner(idsFile);
		while(scan.hasNextLine()){
			String line = scan.nextLine();
			String[] strs = line.split("\t");
			idsMap.put(Long.valueOf(strs[0]), strs[1].trim());
		}
		scan.close();
	}

	@Override
	protected void processData() throws Exception {
		File metisFile = new File(metisFilename);
		Scanner scan = new Scanner(metisFile);
		long i = 0;
		while(scan.hasNextLine()){
			i += 1;
			String line = scan.nextLine();
			int partition = Integer.valueOf(line);
			String s = idsMap.get(i).trim();
			if(s.startsWith("<") && s.endsWith(">")){
				StmtIterator iter = model.listStatements(model.getResource(s.substring(1, s.length()-1)), null, (RDFNode)null);
				while(iter.hasNext()){
					Statement stmt =  iter.nextStatement();
					String triple = Utility.tripleAsString(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
					writeTriple(triple, partition);
				}
			}
		}
		scan.close();		
	}
	
	public static void main(String[] args) throws Exception{
		//Filename outputdir metisOutputFile idsFilename numOfPartitions
		MetisPartitioner partitioner = new MetisPartitioner(Integer.valueOf(args[4]), args[1], args[0], args[2], args[3]);
		partitioner.run();
	}

}

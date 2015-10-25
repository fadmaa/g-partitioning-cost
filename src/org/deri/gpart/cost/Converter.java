package org.deri.gpart.cost;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class Converter {

	//static String dirname = "/Users/fadi/development/tools/lubm1.7/dataset_1";
	static String dirname = "/Users/fadi/data/lubm/5u";
	static String resultFilename = "/Users/fadi/data/lubm/5u/uobm_dataset_5.nt";
	
	public static void main(String[] args) throws Exception{
		File folder = new File(dirname);
		File[] listOfFiles = folder.listFiles();

		Model model = ModelFactory.createDefaultModel();
		FileOutputStream fout = new FileOutputStream(resultFilename);
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				Model m = ModelFactory.createDefaultModel();
				FileInputStream fin = new FileInputStream(listOfFiles[i]);
				m.read(fin, "", "RDF/XML");
				fin.close();
				model.add(m);
			}
		}
		
		model.write(fout, "N-TRIPLE");
		fout.close();
	}
}

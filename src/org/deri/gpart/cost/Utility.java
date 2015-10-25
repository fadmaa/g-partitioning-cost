package org.deri.gpart.cost;

import java.io.FileInputStream;
import java.io.IOException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

public class Utility {

	public static Model readNT(String filePath) throws IOException{
		Model model = ModelFactory.createDefaultModel();
		FileInputStream fin = new FileInputStream(filePath);
		model.read(fin, null, "N-TRIPLE");
		fin.close();
		return model;
	}
	
	private static String toN3String(RDFNode node){
		if(node.isResource()) return "<" + node.asResource().getURI() + ">";
		else return node.asLiteral().getLexicalForm();
	}
	
	public static String tripleAsString(Resource s, Resource p, RDFNode o){
		return "<" + s.getURI() + "> <" + p.getURI() + "> " + 
				toN3String(o);
	}
}

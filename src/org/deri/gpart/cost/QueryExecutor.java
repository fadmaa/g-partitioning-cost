package org.deri.gpart.cost;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;

public class QueryExecutor {

	public static ResultSet executeQuery(Model model, String query) throws Exception{		
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		return qexec.execSelect();
	}
}

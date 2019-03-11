package org.sadiframework.client.testing;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.sadiframework.beans.TestCaseBean;
import org.sadiframework.utils.RdfUtils;


import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.ResourceUtils;

public class TestCase
{
	private static final Logger log = Logger.getLogger(TestCase.class);
	
	Model inputModel;
	Model expectedOutputModel;

	public TestCase(RDFNode input, RDFNode output)
	{
		inputModel = createModel(input);
		expectedOutputModel = createModel(output);
	}
	
	public TestCase(String input, String output)
	{
		inputModel = createModel(input);
		expectedOutputModel = createModel(output);
	}
	
	public TestCase(TestCaseBean bean)
	{
		this(bean.getInput(), bean.getExpectedOutput());
	}
	
	public Model getInputModel()
	{
		return inputModel;
	}

	public Model getExpectedOutputModel()
	{
		return expectedOutputModel;
	}

	private static Model createModel(RDFNode source)
	{
		if (source.isResource()) {
			Resource sourceResource = source.asResource();
			
			/* if there are statements about this resource, it's inline;
			 * if not, it's the URI of a remote resource...
			 */
			if (sourceResource.listProperties().hasNext()) {
				return ResourceUtils.reachableClosure(source.asResource());
			} else {
				Model model = ModelFactory.createDefaultModel();
				model.read(sourceResource.getURI());
				return model;
			}
		} else {
			Model model = ModelFactory.createDefaultModel();
			try {
				RdfUtils.loadModelFromString(model, source.asLiteral().getString());
			} catch (IOException e) {
				log.error("failed to create test model", e);
			}
			return model;
		}
	}
	
	private static Model createModel(String pathOrURLOrRDF)
	{
		Model model = ModelFactory.createDefaultModel();
		try {
			RdfUtils.loadModelFromString(model, pathOrURLOrRDF);
		} catch (IOException e) {
			log.error("failed to create test model", e);
		}
		return model;
	}
}

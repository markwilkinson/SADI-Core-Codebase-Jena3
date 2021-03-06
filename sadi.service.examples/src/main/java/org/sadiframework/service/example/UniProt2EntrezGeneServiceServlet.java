package org.sadiframework.service.example;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sadiframework.service.annotations.ContactEmail;
import org.sadiframework.service.annotations.TestCase;
import org.sadiframework.service.annotations.TestCases;
import org.sadiframework.utils.LSRNUtils;
import org.sadiframework.vocab.LSRN;
import org.sadiframework.vocab.SIO;

import uk.ac.ebi.kraken.interfaces.uniprot.DatabaseCrossReference;
import uk.ac.ebi.kraken.interfaces.uniprot.DatabaseType;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;
import uk.ac.ebi.kraken.interfaces.uniprot.dbx.geneid.GeneId;

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDFS;

@ContactEmail("info@sadiframework.org")
@TestCases(
		@TestCase(
				input = "http://sadiframework.org/examples/t/uniprot2EntrezGene.input.1.rdf",
				output = "http://sadiframework.org/examples/t/uniprot2EntrezGene.output.1.rdf"
		)
)
public class UniProt2EntrezGeneServiceServlet extends UniProtServiceServlet
{
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(UniProt2EntrezGeneServiceServlet.class);

	@Override
	public void processInput(UniProtEntry input, Resource output)
	{
		for (DatabaseCrossReference xref: input.getDatabaseCrossReferences(DatabaseType.GENEID)) {

			GeneId entrezGene = (GeneId)xref;
			String entrezGeneId = entrezGene.getGeneIdAccessionNumber().getValue();

			Resource entrezGeneNode = LSRNUtils.createInstance(output.getModel(), LSRNUtils.getClass(LSRN.Namespace.ENTREZ_GENE), entrezGeneId);
			entrezGeneNode.addProperty(RDFS.label, entrezGene.toString());

			output.addProperty(SIO.is_encoded_by, entrezGeneNode);

		}
	}
}

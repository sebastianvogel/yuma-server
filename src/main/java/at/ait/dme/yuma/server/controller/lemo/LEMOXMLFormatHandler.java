package at.ait.dme.yuma.server.controller.lemo;

import java.io.StringWriter;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;

import at.ait.dme.yuma.server.URIBuilder;
import at.ait.dme.yuma.server.controller.FormatHandler;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.AnnotationTree;

/**
 * Format handler for LEMO RDF/XML.
 * 
 * @author Rainer Simon
 */
public class LEMOXMLFormatHandler implements FormatHandler {
	
	private static final String NS_ANNOTATION = "http://lemo.mminf.univie.ac.at/annotation-core#";
	private static final String NS_SCOPE = "http://lemo.mminf.univie.ac.at/ann-tel#";

	@Override
	public Annotation parse(String serialized) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String serialize(Annotation a) {
		Model m = ModelFactory.createDefaultModel();
		m.setNsPrefix("a", NS_ANNOTATION);
		m.setNsPrefix("scope", NS_SCOPE);
		
		Resource annotation = m.createResource(URIBuilder.toURI(a.getAnnotationID()).toString());		
		
		annotation.addProperty(m.createProperty(NS_ANNOTATION, "annotates"), a.getObjectID());
		annotation.addProperty(RDF.type, m.createProperty(NS_ANNOTATION, "Annotation"));
		annotation.addProperty(DC.title, a.getTitle());
		annotation.addProperty(DC.creator, a.getCreatedBy());
		annotation.addProperty(m.createProperty(NS_ANNOTATION, "created"), a.getCreated().toString());
		annotation.addProperty(m.createProperty(NS_ANNOTATION, "modified"), a.getLastModified().toString());
		annotation.addProperty(m.createProperty(NS_ANNOTATION, "label"), a.getText());
		annotation.addProperty(m.createProperty(NS_SCOPE, "scope"), a.getScope().name());
		
		StringWriter sw = new StringWriter();
		m.write(sw);
		return sw.toString();
	}

	@Override
	public String serialize(AnnotationTree tree) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String serialize(List<Annotation> annotations)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return null;
	}
	
}

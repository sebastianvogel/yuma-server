package at.ait.dme.yuma.server.controller.oac;

import java.util.List;

import at.ait.dme.yuma.server.controller.RDFFormatHandler;
import at.ait.dme.yuma.server.controller.SerializationLanguage;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.AnnotationTree;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * Format handler for OAC RDF(in different serialization
 * languages).
 * 
 * @author Rainer Simon
 */
public class OACFormatHandler extends RDFFormatHandler {
	
	public OACFormatHandler() {
		super(SerializationLanguage.RDF_XML);
	}

	@Override
	public Annotation parse(String serialized) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String serialize(AnnotationTree tree) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String serialize(List<Annotation> annotations) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRDFResource(Annotation annotation, Model model) {
		// TODO Auto-generated method stub
		
	}
}

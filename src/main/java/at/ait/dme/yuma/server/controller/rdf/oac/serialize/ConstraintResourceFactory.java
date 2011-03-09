package at.ait.dme.yuma.server.controller.rdf.oac.serialize;

import at.ait.dme.yuma.server.controller.rdf.oac.OACFormatHandler;
import at.ait.dme.yuma.server.exception.InvalidAnnotationException;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.MediaType;

import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * Used to obtain resources that constrain an annotation.
 * 
 * @author chef
 */
class ConstraintResourceFactory {

	private static final String NS_CNT = "http://www.w3.org/2008/content#";
	private static ConstraintResourceFactory instance;
	
	private ConstraintResourceFactory() {
		// constructor only called from within this class
	}
	
	static ConstraintResourceFactory getInstance() {
		if (instance == null) {
			instance = new ConstraintResourceFactory();
		}
		return instance;
	}
	
	Resource createResource(Annotation annotation, Model model) throws InvalidAnnotationException {
		MediaType mediaType = annotation.getType();
		
		switch (mediaType) {
		case IMAGE:
			return createSvgConstraint(annotation.getFragment(), model);
		case MAP:
			return createSvgConstraint(annotation.getFragment(), model);
		}
		throw new InvalidAnnotationException("don't know how to handle media type '" +mediaType.toString()+ "'");
	}
	
	private Resource createSvgConstraint(String fragment, Model model) {
		Resource ret = model.createResource(AnonId.create());
		ret.addProperty(
			RDF.type, 
			model.createProperty(OACFormatHandler.NS_OAC, "SvgConstraint"));
		ret.addProperty(DC.format, "image/svg+xml");
		
		ret.addProperty(
			RDF.type, 
			model.createProperty(NS_CNT, "ContentAsText"));

		Property chars = model.createProperty(NS_CNT, "chars");
		Property charEncoding = model.createProperty(NS_CNT, "characterEncoding");
		
		ret.addProperty(chars, fragment);
		ret.addProperty(charEncoding, "utf-8");
		return ret;
	}
}
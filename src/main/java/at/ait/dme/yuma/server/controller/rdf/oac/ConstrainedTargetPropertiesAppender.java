package at.ait.dme.yuma.server.controller.rdf.oac;

import org.apache.log4j.Logger;
import org.jboss.resteasy.spi.UnsupportedMediaTypeException;

import at.ait.dme.yuma.server.model.Annotation;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Used to add constrains-related properties to the target of an annotation.
 * 
 * @author Christian Mader
 */
class ConstrainedTargetPropertiesAppender extends PropertiesAppender {

	private Logger logger = Logger.getLogger(ConstrainedTargetPropertiesAppender.class);
	private Model model;
	
	ConstrainedTargetPropertiesAppender(Resource resource, Model model) {
		super(resource);
		this.model = model;
	}
	
	@Override
	void populatePropertiesMap(Annotation annotation) {
		addProperty(createConstrainsProperty(), annotation.getObjectUri().toString());
		
		try {
			addProperty(createConstrainedByProperty(), 
				ConstraintResourceFactory.getInstance().createResource(annotation, model));
		}
		catch (UnsupportedMediaTypeException e) {
			logger.warn("Ignoring unknown media type", e);
		}
	}
	
	private Property createConstrainsProperty() {
		return model.createProperty(OACFormatHandler.NS_OAC, "constrains");
	}
	
	private Property createConstrainedByProperty() {
		return model.createProperty(OACFormatHandler.NS_OAC, "constrainedBy");
	}
}

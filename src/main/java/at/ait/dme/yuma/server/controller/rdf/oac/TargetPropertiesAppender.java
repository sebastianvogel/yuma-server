package at.ait.dme.yuma.server.controller.rdf.oac;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jboss.resteasy.spi.UnsupportedMediaTypeException;

import at.ait.dme.yuma.server.model.Annotation;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

class TargetPropertiesAppender extends PropertiesAppender {

	private Logger logger = Logger.getLogger(TargetPropertiesAppender.class);
	private Model model;
	
	TargetPropertiesAppender(Resource resource, Model model) {
		super(resource);
		this.model = model;
	}
	
	@Override
	Map<Property, Object> buildPropertiesMap(Annotation annotation) {
		HashMap<Property, Object> properties = new HashMap<Property, Object>();
		
		properties.put(createConstrainsProperty(), annotation.getObjectUri().toString());
		
		try {
			properties.put(createConstrainedByProperty(), 
				ConstraintResourceFactory.getInstance().createResource(annotation, model));
		}
		catch (UnsupportedMediaTypeException e) {
			logger.warn("Ignoring unknown media type", e);
		}
		
		return properties;
	}
	
	private Property createConstrainsProperty() {
		return model.createProperty(OACFormatHandler.NS_OAC, "constrains");
	}
	
	private Property createConstrainedByProperty() {
		return model.createProperty(OACFormatHandler.NS_OAC, "constrainedBy");
	}
}

package at.ait.dme.yuma.server.controller.oac;

import java.util.HashMap;

import at.ait.dme.yuma.server.model.Annotation;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

abstract class PropertiesAppender {
	
	private HashMap<Property, String> properties;
	private Resource resource;
	
	PropertiesAppender(Resource resource) {
		this.resource = resource;
	}

	void appendProperties(Annotation annotation) {
		properties = buildPropertiesMap(annotation);
		appendPropertiesFromMap();
	}
	
	private void appendPropertiesFromMap() {
		for (Property property : properties.keySet()) {
			String value = properties.get(property);
			if (value != null) {
				resource.addProperty(property, value);
			}
		}
	}
	
	abstract HashMap<Property, String> buildPropertiesMap(Annotation annotation);
}

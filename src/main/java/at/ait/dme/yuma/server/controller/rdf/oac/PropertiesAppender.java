package at.ait.dme.yuma.server.controller.rdf.oac;

import java.util.Map;

import at.ait.dme.yuma.server.model.Annotation;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

abstract class PropertiesAppender {
	
	private Map<Property, Object> properties;
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
			Object value = properties.get(property);
			if (value != null) {
				if (value instanceof String) {
					resource.addProperty(property, (String) value);
				}
				else if (value instanceof Resource) {
					resource.addProperty(property, (Resource) value);
				}
			}
		}
	}
	
	abstract Map<Property, Object> buildPropertiesMap(Annotation annotation);
}

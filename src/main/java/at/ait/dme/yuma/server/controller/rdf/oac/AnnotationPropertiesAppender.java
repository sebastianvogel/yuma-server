package at.ait.dme.yuma.server.controller.rdf.oac;

import java.util.HashMap;

import at.ait.dme.yuma.server.model.Annotation;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.DCTerms;

class AnnotationPropertiesAppender extends PropertiesAppender {
	
	AnnotationPropertiesAppender(Resource resource) {
		super(resource);
	}
	
	@Override
	HashMap<Property, String> buildPropertiesMap(Annotation annotation) {
		HashMap<Property, String> properties = new HashMap<Property, String>();		
		
		properties.put(DC.title, annotation.getTitle());
		properties.put(DC.creator, annotation.getCreatedBy().getUsername());
		properties.put(DCTerms.created, annotation.getCreated().toString());
		properties.put(DCTerms.modified,  annotation.getLastModified().toString());
		
		//TODO: What about the scope?
		
		return properties;
	}
	
}

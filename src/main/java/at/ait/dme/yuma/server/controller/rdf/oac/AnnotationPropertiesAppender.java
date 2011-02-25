package at.ait.dme.yuma.server.controller.rdf.oac;

import at.ait.dme.yuma.server.model.Annotation;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.DCTerms;

class AnnotationPropertiesAppender extends PropertiesAppender {
	
	AnnotationPropertiesAppender(Resource resource) {
		super(resource);
	}
	
	@Override
	void populatePropertiesMap(Annotation annotation) {
		addProperty(DC.title, annotation.getTitle());
		addProperty(DC.creator, annotation.getCreatedBy().getUsername());
		addProperty(DCTerms.created, annotation.getCreated().toString());
		addProperty(DCTerms.modified,  annotation.getLastModified().toString());
		
		//TODO: What about the scope?
	}
	
}

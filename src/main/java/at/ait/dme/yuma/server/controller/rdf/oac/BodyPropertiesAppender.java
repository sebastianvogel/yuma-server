package at.ait.dme.yuma.server.controller.rdf.oac;

import java.util.HashMap;
import java.util.List;

import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.tag.SemanticRelation;
import at.ait.dme.yuma.server.model.tag.SemanticTag;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

class BodyPropertiesAppender extends PropertiesAppender {

	private HashMap<Property, String> properties = new HashMap<Property, String>();
	private Model model;
	
	BodyPropertiesAppender(Resource resource, Model model) {
		super(resource);
		this.model = model;
	}
	
	@Override
	HashMap<Property, String> buildPropertiesMap(Annotation annotation) {
		properties.put(RDFS.label, annotation.getText());
		appendSemanticTags(annotation);
		
		return properties;
	}

	private void appendSemanticTags(Annotation annotation) {
		List<SemanticTag> semanticTags = annotation.getTags();
		
		if (semanticTags == null)
			return;
			
		for (SemanticTag semanticTag : semanticTags) {
			SemanticRelation r = semanticTag.getRelation();
			properties.put(
				model.createProperty(r.getNamespace(), r.getProperty()), 
				semanticTag.getURI().toString());
		}
	}
	
}

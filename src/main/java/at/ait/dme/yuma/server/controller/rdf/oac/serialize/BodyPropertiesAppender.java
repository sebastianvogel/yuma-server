package at.ait.dme.yuma.server.controller.rdf.oac.serialize;

import java.util.List;

import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.tag.SemanticRelation;
import at.ait.dme.yuma.server.model.tag.SemanticTag;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * Used to add properties to the body of an annotation.
 * 
 * @author Christian Mader
 */
public class BodyPropertiesAppender extends PropertiesAppender {

	private Model model;
	
	public BodyPropertiesAppender(Resource resource, Model model) {
		super(resource);
		this.model = model;
	}
	
	@Override
	void populatePropertiesMap(Annotation annotation) {
		addProperty(RDFS.label, annotation.getText());
		appendSemanticTags(annotation);
	}

	private void appendSemanticTags(Annotation annotation) {
		List<SemanticTag> semanticTags = annotation.getTags();
		
		if (semanticTags == null)
			return;
			
		for (SemanticTag semanticTag : semanticTags) {
			if (hasSemanticRelation(semanticTag)) {
				addRelationProperty(semanticTag);
			}
			else {
				addCommonTag(semanticTag);
			}
		}
	}
	
	private boolean hasSemanticRelation(SemanticTag semanticTag) {
		return semanticTag.getRelation() != null;
	}
	
	private void addRelationProperty(SemanticTag semanticTag) {
		SemanticRelation r = semanticTag.getRelation();
		
		addProperty(model.createProperty(
			r.getNamespace(), 
			r.getProperty()), 
			semanticTag.getURI().toString());		
	}
	
	private void addCommonTag(SemanticTag semanticTag) {
		addProperty(
			CTAG.tagged,
			createTagResource(semanticTag));
	}
	
	private Resource createTagResource(SemanticTag semanticTag) {
		Resource ret = model.createResource(CTAG.Tag);
		ret.addProperty(CTAG.means, semanticTag.getURI().toString());
		
		return ret;
	}
}

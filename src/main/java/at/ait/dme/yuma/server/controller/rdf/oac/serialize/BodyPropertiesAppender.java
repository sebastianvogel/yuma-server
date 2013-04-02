package at.ait.dme.yuma.server.controller.rdf.oac.serialize;

import java.util.List;

import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.tag.PlainLiteral;
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
		
		addPrimaryLabel(ret, semanticTag);
		addAlternativeLabel(ret, semanticTag);
		
		return ret;
	}
	
	private void addPrimaryLabel(Resource resource, SemanticTag semanticTag) {
		String primaryLabel = semanticTag.getPrimaryLabel();
		
		if (primaryLabel != null && !primaryLabel.isEmpty()) {
			resource.addProperty(CTAG.label, primaryLabel);			
			resource.addProperty(
				SKOSXL.prefLabel, 
				createSkosXlLabel(resource, 
					semanticTag.getPrimaryLabel(), 
					semanticTag.getPrimaryDescription(),
					semanticTag.getPrimaryLanguage()));
		}
	}
	
	private Resource createSkosXlLabel(
		Resource resource, 
		String label, 
		String description,
		String language)
	{
		Resource ret = model.createResource(SKOSXL.Label);
				
		if (label != null) {
			ret.addProperty(SKOSXL.literalForm, model.createLiteral(label, language));
		}
		if (description != null) {
			ret.addProperty(SKOS.note, model.createLiteral(description, language));
		}
		
		return ret;
	}
	
	private void addAlternativeLabel(Resource resource, SemanticTag semanticTag) {
		for (PlainLiteral altLabel : semanticTag.getAlternativeLabels()) {
			String language = altLabel.getLanguage();

			if (language == null) {
				resource.addProperty(
					SKOSXL.altLabel, 
					createSkosXlLabel(resource,	altLabel.getValue(), null, null));
			}
			else {
				resource.addProperty(
					SKOSXL.altLabel, 
					createSkosXlLabel(resource, 
						semanticTag.getAlternativeLabel(language),
						semanticTag.getAlternativeDescription(language),
						language));
			}
		}
	}
	
}

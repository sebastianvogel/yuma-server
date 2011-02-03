package at.ait.dme.yuma.server.controller.oac;

import java.util.List;

import at.ait.dme.yuma.server.URIBuilder;
import at.ait.dme.yuma.server.controller.RDFFormatHandler;
import at.ait.dme.yuma.server.controller.SerializationLanguage;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.AnnotationTree;
import at.ait.dme.yuma.server.model.tag.SemanticRelation;
import at.ait.dme.yuma.server.model.tag.SemanticTag;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * Format handler for OAC RDF(in different serialization
 * languages).
 * 
 * @author Rainer Simon
 */
public class OACFormatHandler extends RDFFormatHandler {
	
	private static final String NS_ANNOTATION = "http://annotations.mminf.univie.ac.at/annotation-body#";
	private static final String NS_OAC = "http://www.openannotation.org/ns/";
	
	private Annotation annotation;
	private Model model;
	
	public OACFormatHandler() {
		super(SerializationLanguage.RDF_XML);
	}

	@Override
	public Annotation parse(String serialized) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String serialize(AnnotationTree tree) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String serialize(List<Annotation> annotations) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRDFResource(Annotation annotation, Model model) {
		this.annotation = annotation;
		this.model = model;
		
		model.setNsPrefix("oac", NS_OAC);
		
		Resource body = createAnnotationBody();
		Resource resource = createAnnotationResource(body);
		
		
		annotation.addProperty(m.createProperty(NS_ANNOTATION, "created"), a.getCreated().toString());
		annotation.addProperty(m.createProperty(NS_ANNOTATION, "modified"), a.getLastModified().toString());
		
		if (a.getText() != null)
			annotation.addProperty(m.createProperty(NS_ANNOTATION, "label"), a.getText());
		
		annotation.addProperty(m.createProperty(NS_SCOPE, "scope"), a.getScope().name());
		
		if (a.getTags() != null) {
			for (SemanticTag t : a.getTags()) {
				SemanticRelation r = t.getRelation();
				annotation.addProperty(m.createProperty(r.getNamespace(), r.getProperty()), t.getURI().toString());
			}
		}
	}
	
	private Resource createAnnotationBody() {
		Resource ret = model.createResource(
			URIBuilder.toURI(NS_ANNOTATION).toString());
		
		addBodyProperties(ret);
		return ret;
	}
	
	private void addBodyProperties(Resource body) {
		addTitle(body);
		addCreator(body);
	}
	
	private void addTitle(Resource body) {
		if (annotation.getTitle() != null)
			body.addProperty(DC.title, annotation.getTitle());
	}
	
	private void addCreator(Resource body) {
		body.addProperty(DC.creator, annotation.getCreatedBy().getUsername());
	}

	
	private Resource createAnnotationResource(Resource body) {
		Resource ret = model.createResource(
			URIBuilder.toURI(annotation.getAnnotationID()).toString());		
		
		addAnnotationProperties(ret, body);
		return ret;
	}
	
	private void addAnnotationProperties(Resource annotResource, Resource body)
	{
		annotResource.addProperty(RDF.type, model.createProperty(NS_OAC, "Annotation"));
		annotResource.addProperty(
			model.createProperty(NS_OAC, "hasBody"), 
			body.getURI());
		annotResource.addProperty(
			model.createProperty(NS_OAC, "hasTarget"), 
			URIBuilder.toURI(annotation.getObjectUri()).toString());		
	}

}

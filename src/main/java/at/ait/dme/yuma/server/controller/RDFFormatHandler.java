package at.ait.dme.yuma.server.controller;

import java.io.StringWriter;

import at.ait.dme.yuma.server.model.Annotation;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public abstract class RDFFormatHandler implements FormatHandler {

	private SerializationLanguage serializationLanguage;
	
	public RDFFormatHandler(SerializationLanguage serializationLanguage) {
		this.serializationLanguage = serializationLanguage;
	}
	
	@Override
	public String serialize(Annotation annotation) {
		Model model = ModelFactory.createDefaultModel();

		addRDFResource(annotation, model);
		return toString(model);
	}
	
	protected String toString(Model m) {
		StringWriter sw = new StringWriter();
		m.write(sw, serializationLanguage.toString());
		return sw.toString();
	}

	protected abstract void addRDFResource(Annotation annotation, Model model);
	
}

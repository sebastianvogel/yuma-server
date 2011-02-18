package at.ait.dme.yuma.server.controller.rdf;

import java.io.StringWriter;
import java.util.List;

import at.ait.dme.yuma.server.controller.FormatHandler;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.AnnotationTree;

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
	
	@Override
	public String serialize(AnnotationTree tree) {
		return serialize(tree.asFlatList());
	}

	@Override
	public String serialize(List<Annotation> annotations) {
		Model m = ModelFactory.createDefaultModel();
		
		for (Annotation a : annotations) {
			addRDFResource(a, m);
		}

		return toString(m);
	}
	
	protected String toString(Model m) {
		StringWriter sw = new StringWriter();
		m.write(sw, serializationLanguage.toString());
		return sw.toString();
	}

	protected abstract void addRDFResource(Annotation annotation, Model model);
	
}

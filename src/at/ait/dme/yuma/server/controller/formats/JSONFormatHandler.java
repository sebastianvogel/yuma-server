package at.ait.dme.yuma.server.controller.formats;

import java.util.List;
import java.util.Map;

import com.mongodb.util.JSON;

import at.ait.dme.yuma.server.exception.InvalidAnnotationException;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.AnnotationTree;

/**
 * Format handler for JSON.
 * 
 * @author Rainer Simon
 */
public class JSONFormatHandler implements FormatHandler {

	@Override
	public Annotation parse(String serialized)
		throws InvalidAnnotationException {
		
		return new Annotation(((Map<?, ?>) JSON.parse(serialized)));
	}

	@Override
	public String serialize(Annotation annotation) {
		return JSON.serialize(annotation.toMap());
	}

	@Override
	public String serialize(AnnotationTree tree) {
		// TODO Implement this!
		return null;
	}

	@Override
	public String serialize(List<Annotation> annotations) {
		return null;
	}

}

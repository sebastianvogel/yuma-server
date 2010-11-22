package at.ait.dme.yuma.server.controller.formats;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mongodb.util.JSON;

import at.ait.dme.yuma.server.exception.InvalidAnnotationException;
import at.ait.dme.yuma.server.model.Annotation;

/**
 * Format handler for JSON.
 * 
 * @author Rainer Simon
 */
public class JSONFormatHandler implements FormatHandler {
	
	@Override
	public Annotation parse(String serialized)
		throws InvalidAnnotationException {
		
		return new Annotation(((Map<String, Object>) JSON.parse(serialized)));
	}

	@Override
	public String serialize(Annotation annotation) {
		return JSON.serialize(annotation.toMap());
	}

	@Override
	public String serialize(List<Annotation> annotations) {
		return JSON.serialize(toMaps(annotations));
	}
	
	private List<Map<String, Object>> toMaps(List<Annotation> annotations) {
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		for (Annotation a : annotations) {
			list.add(a.toMap());
		}
		
		return list;
	}

}

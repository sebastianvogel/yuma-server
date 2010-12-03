package at.ait.dme.yuma.server.controller.formats.rss;

import at.ait.dme.yuma.server.controller.formats.FormatHandler;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.AnnotationTree;

/**
 * Format handler for RSS (serialization only)
 * 
 * @author Rainer Simon
 */
public class RSSFormatHandler implements FormatHandler {

	@Override
	public Annotation parse(String serialized)
			throws UnsupportedOperationException {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public String serialize(Annotation annotation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String serialize(AnnotationTree tree) {
		// TODO Auto-generated method stub
		return null;
	}

}

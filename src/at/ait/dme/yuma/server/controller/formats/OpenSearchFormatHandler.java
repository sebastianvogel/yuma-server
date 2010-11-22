package at.ait.dme.yuma.server.controller.formats;

import java.util.List;

import at.ait.dme.yuma.server.model.Annotation;

/**
 * Format handler for OpenSearch (serialization only)
 * 
 * @author Rainer Simon
 */
public class OpenSearchFormatHandler implements FormatHandler {

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
	public String serialize(List<Annotation> annotations) {
		// TODO Auto-generated method stub
		return null;
	}

}

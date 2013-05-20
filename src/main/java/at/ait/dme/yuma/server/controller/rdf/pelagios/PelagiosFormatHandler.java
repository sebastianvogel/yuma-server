package at.ait.dme.yuma.server.controller.rdf.pelagios;

import at.ait.dme.yuma.server.controller.rdf.SerializationLanguage;
import at.ait.dme.yuma.server.controller.rdf.oac.OACFormatHandler;
import at.ait.dme.yuma.server.model.tag.SemanticTag;

import com.hp.hpl.jena.rdf.model.Resource;

public class PelagiosFormatHandler extends OACFormatHandler {
	
	public PelagiosFormatHandler(SerializationLanguage format) {
		super(format);
	}

	@Override
	protected Resource createBodyResource() {
		return model.createResource(getBodyUri());
	}
	
	private String getBodyUri() {
		if (annotation.getTags()==null) {
			return null;
		}
		
		for (SemanticTag t : annotation.getTags()) {
			if (t!=null && t.getURI()!=null) {
				return t.getURI().toString();
			}
		}
		return null;
	}

}

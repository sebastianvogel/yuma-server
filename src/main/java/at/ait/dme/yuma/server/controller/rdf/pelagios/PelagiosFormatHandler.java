package at.ait.dme.yuma.server.controller.rdf.pelagios;

import java.util.List;

import at.ait.dme.yuma.server.controller.rdf.oac.OACFormatHandler;
import at.ait.dme.yuma.server.model.tag.SemanticTag;

import com.hp.hpl.jena.rdf.model.Resource;

public class PelagiosFormatHandler extends OACFormatHandler {

	@Override
	protected Resource createBodyResource() {
		return model.createResource(getBodyUri());
	}
	
	private String getBodyUri() throws PelagiosDataException
	{
		List<SemanticTag> semanticTags = annotation.getTags();
		if (semanticTags.isEmpty()) {
			throw new PelagiosDataException("pelagios annotation doesn't contain semantic tags");
		}
		
		return semanticTags.get(0).getURI().toString(); 
	}

}

package at.ait.dme.yuma.server.controller.rdf.oac.serialize;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

class SKOS {

protected static final String uri="http://www.w3.org/2004/02/skos/core#";
	
	protected static final Resource resource( String local )
	    { return ResourceFactory.createResource( uri + local ); }
	
	protected static final Property property( String local )
	    { return ResourceFactory.createProperty( uri, local ); }
	
	public static final Property note = property("note");
}
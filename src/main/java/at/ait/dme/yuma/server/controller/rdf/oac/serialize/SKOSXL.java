package at.ait.dme.yuma.server.controller.rdf.oac.serialize;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

class SKOSXL {
	protected static final String uri="http://www.w3.org/2008/05/skos-xl#";

    protected static final Resource resource( String local )
        { return ResourceFactory.createResource( uri + local ); }

    protected static final Property property( String local )
        { return ResourceFactory.createProperty( uri, local ); }

    public static final Resource Label = resource("Label");
    public static final Property prefLabel = property("prefLabel");
    public static final Property altLabel = property("altLabel");
    public static final Property literalForm = property("literalForm");
}

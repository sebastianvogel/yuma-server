package at.ait.dme.yuma.server.controller.oac;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import at.ait.dme.yuma.server.controller.AbstractAnnotationController;
import at.ait.dme.yuma.server.controller.FormatHandler;
import at.ait.dme.yuma.server.exception.AnnotationDatabaseException;
import at.ait.dme.yuma.server.exception.AnnotationModifiedException;
import at.ait.dme.yuma.server.exception.InvalidAnnotationException;

/**
 * An annotation controller which produces an RDF/XML
 * representations of annotations based on the 
 * OAC (http://www.openannotation.com) model.
 *  
 * @author Christian Mader
 */

@Path("/api")
public class OACAnnotationController extends AbstractAnnotationController {

	@POST
	@Consumes("application/rdf+xml")
	@Path("/oac_annotation")
	@Override
	protected Response createAnnotation(String annotation, FormatHandler format)
		throws AnnotationDatabaseException, InvalidAnnotationException, AnnotationModifiedException 
	{
		return super.createAnnotation(annotation, new OACFormatHandler());
	}
	
}

package at.ait.dme.yuma.server.controller.telplus;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import at.ait.dme.yuma.server.controller.AbstractAnnotationController;
import at.ait.dme.yuma.server.db.fast.FastAnnotationDatabase;
import at.ait.dme.yuma.server.db.hibernate.Annotation;
import at.ait.dme.yuma.server.db.hibernate.RdfXmlAnnotationBuilder;
import at.ait.dme.yuma.server.db.sesame.SesameAnnotationDatabase;
import at.ait.dme.yuma.server.exception.AnnotationDatabaseException;
import at.ait.dme.yuma.server.exception.AnnotationException;

/**
 * this is a prototypical implementation of some TELplus specific annotation requests
 * as proposed by Theo van Veen.
 * 
 * @author Christian Sadilek
 */
@Path("/tp/annotations")
public class TELplusAnnotationController extends AbstractAnnotationController {
	
	
	@GET
	@Produces("application/rdf+xml")	
	@Path("/importfast")
	public Response importFast() 
		throws AnnotationDatabaseException, AnnotationException, UnsupportedEncodingException {
	
		FastAnnotationDatabase fast = new FastAnnotationDatabase();
		fast.testConnect();
		
		String allAnnotations = fast.findAnnotations("fast.ann.nsURI=\"http://dme.arcs.ac.at/annotation-middleware/Annotation/\"");
		List<Annotation> annotations = null;
		try {
			annotations = RdfXmlAnnotationBuilder.fromRdfXmlAsList(allAnnotations);
		} catch (Exception e1) {			
			e1.printStackTrace();
		}
		
		for(Annotation a : annotations) {
			// we use our own ids
			a.setId(null);
			if(a.getParentId()!=null) continue;
				
			SesameAnnotationDatabase sad = new SesameAnnotationDatabase();
			URI id = null;
			try {
				sad.connect();
				id=sad.createAnnotation(RdfXmlAnnotationBuilder.toRdfXml(a, false));					
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				sad.disconnect();	
			}				
		}	
		return Response.ok().build();
	}	
		

	
	@GET
	@Produces("application/rdf+xml")	
	@Path("/{obj-id}")
	public Response listAnnotations(@PathParam("obj-id") String objectId, 
			@QueryParam("result-count-type") String resultCountType) 
		throws AnnotationDatabaseException, AnnotationException, UnsupportedEncodingException {

		if(resultCountType!=null) {			
			int count =  new Integer(super.countAnnotations(objectId).getEntity().toString());			
			return createResultCountRepresentation(count, resultCountType);
		}
		
		return super.listAnnotations(objectId);
	}
	
	@GET
	@Produces("application/rdf+xml")	
	@Path("/linkedAnnotations/{obj-id}")
	public Response listLinkedAnnotations(@PathParam("obj-id") String objectId,
			@QueryParam("result-count-type") String resultCountType) 
		throws AnnotationDatabaseException, AnnotationException, UnsupportedEncodingException {
			
		if(resultCountType!=null) {			
			int count =  new Integer(super.countLinkedAnnotations(objectId).getEntity().toString());			
			return createResultCountRepresentation(count, resultCountType);
		} 
		
		return super.listLinkedAnnotations(objectId);
	}
	
	private Response createResultCountRepresentation(int count, String resultCountType) {
		Response response = null;
		
		if (resultCountType.equalsIgnoreCase("JSON")) {
			String resultCount = "{\"numberOfHits\":\"" + count + "\",\"unitName\":\"annotations\"}";
			response = Response.ok().type("application/javascript").entity(resultCount).build();
			
		} else if (resultCountType.equalsIgnoreCase("XML")) {
			String resultCount = "<result-count>\n<numberOfHits>" + count + "</numberOfHits>\n"
					+ "<unitName>annotations</unitName>\n</result-count>";
			response = Response.ok().type("application/xml").entity(resultCount).build();
		
		// all other types are supposed to describe the name of the callback function
		} else {
			String resultCount = resultCountType+"({\"numberOfHits\":\"" + count +  
				"\",\"unitName\":\"annotations\"})";
			response = Response.ok().type("application/javascript").entity(resultCount).build();
		}
		return response;
	}
}

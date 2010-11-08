package at.ait.dme.yuma.server.controller

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.MediaType
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam

import scala.xml._

/**
 * this class exposes controller methods to retrieve HTML representations of annotations.
 * 
 * @author Christian Sadilek
 */
@Path("/Annotation")
class HtmlAnnotationController extends AbstractAnnotationController {
  
	@GET
	@Path("/html/{id}")
	@Produces(Array(MediaType.TEXT_HTML))
	override def findAnnotationById(@PathParam("id") id:String) : Response = {
		val resp = super.findAnnotationById(id);  
		val annXml = XML.loadString(resp.getEntity().toString());
  				
		var annHtml:Elem = 
		<html>
			<head>
				<meta content="text/html; charset=utf-8" http-equiv="Content-Type"></meta>
			</head>
			<body>					
				<h2>{(annXml \ "Description" \ "author").text + " @ " + 
					(annXml \ "Description" \ "date").text }</h2>
				<h1>{Unparsed((annXml \ "Description" \ "title").text)}</h1>
				<p>{Unparsed((annXml \ "Description" \ "label").text)}</p>
				{ for(a<-(annXml \\ "svg-fragment")) yield { 
					<iframe src={"fragment/"+id} width="100%" height="100%" frameborder="0" />					
				  }
				} 			
			</body>
		</html>

		return Response.ok(annHtml.toString()).build;
    }
 
	@GET
 	@Path("/html/fragment/{id}")
	@Produces(Array(MediaType.TEXT_XML))
	def findAnnotationFragmentById(@PathParam("id") id:String) : Response = {
		val resp = super.findAnnotationById(id);  
		val annXml = XML.loadString(resp.getEntity().toString());
  				
		val fragment = {Unparsed({(annXml \ "Description" \ "svg-fragment" \ "svg").toString()})}
		if(fragment.toString().isEmpty) return Response.status(Status.NOT_FOUND).build;
  
		return Response.ok(fragment.toString()).build;
    }
 
}

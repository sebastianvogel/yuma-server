package at.ait.dme.yuma.server.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("ACL")
public class ACLController {
	
	@Context
	protected HttpServletRequest request;
	
	@Context
	protected HttpServletResponse response;
	
	@POST
	@Consumes("application/json")
	@Path("{id}")
	public Response updateACL(@PathParam("id") String id) {
		return null;
	}
	
	@GET
	@Path("{id}")
	public Response getACL(@PathParam("id") String id) {
		return null;		
	}
}

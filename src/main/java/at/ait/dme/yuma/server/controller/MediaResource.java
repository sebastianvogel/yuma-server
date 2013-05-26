package at.ait.dme.yuma.server.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import at.ait.dme.yuma.server.db.entities.MediaObjectEntity;


@Path("/media")
public class MediaResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public List<MediaObjectEntity> getMedia() {
		List<MediaObjectEntity> mediaList = new ArrayList<MediaObjectEntity>();
		//mediaList.addAll( );
		return mediaList;
	}

	@Path("{medium}")
	public MediaObjectResource getContact(
			@PathParam("medium") String medium) {
		return new MediaObjectResource();
	}
}

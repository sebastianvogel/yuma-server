package at.ait.dme.yuma.server.controller;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.exception.PermissionDeniedException;
import at.ait.dme.yuma.server.service.IGroupService;

@Path("group")
public class GroupController {
	
	//private static Logger log = Logger.getLogger(GroupController.class);
	
	@Context
	protected HttpServletRequest request;
	
	private IGroupService groupService;
	
	public GroupController() {
		groupService = Config.getInstance().getGroupService();
	}
	
	@POST
	@Path("{groupname}/{username}")
	public Response addUserToGroup(@PathParam("groupname") String groupName, @PathParam("username") String username) 
			throws PermissionDeniedException {
		groupService.addToGroup(groupName, username, new AuthContext(request));
		return Response.ok().build();
	}
	
	@DELETE
	@Path("{groupname}")
	public Response deleteGroup(@PathParam("groupname") String groupName) throws PermissionDeniedException {
		groupService.deleteGroup(groupName, new AuthContext(request));
		return Response.status(Status.NO_CONTENT).build();
	}
	
	@DELETE
	@Path("{groupname}/{username}")
	public Response removeUserFromGroup(@PathParam("groupname") String groupName,  @PathParam("username") String username) 
			throws PermissionDeniedException {
		groupService.removeFromGroup(groupName, username, new AuthContext(request));
		return Response.ok().build();
	}
}

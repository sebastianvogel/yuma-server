package at.ait.dme.yuma.server.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.exception.PermissionDeniedException;
import at.ait.dme.yuma.server.model.Group;
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
	
	/**
	 * get all groups for the client
	 * 
	 * @HTTP 200 ok
	 * @returnWrapped List&lt;Group&gt; the list of groups
	 * 
	 * @return the list of groups
	 */
	@GET
	@Produces("application/json")
	public Response getGroups() {
		List<Group> resultList = groupService.getGroups(new AuthContext(request));
		/*
		JSONArray array = new JSONArray();
		for (Group g : resultList) {
			array.add(g.getName());
		}
		*/
		return Response.ok().entity(resultList).build();
	}
	/**
	 * add a user to a group
	 * 
	 * @HTTP 204 no content
	 * @HTTP 403 PermissionDeniedException
	 * 
	 * @param groupName the name of the group
	 * @param username the name of the user
	 * @return no content
	 * @throws PermissionDeniedException
	 */
	@POST
	@Path("{groupname}/{username}")
	public Response addUserToGroup(@PathParam("groupname") String groupName, @PathParam("username") String username) 
			throws PermissionDeniedException {
		groupService.addToGroup(groupName, username, new AuthContext(request));
		return Response.noContent().build();
	}
	/**
	 * delete a group
	 * 
	 * @HTTP 204 no content
	 * @HTTP 403 PermissionDeniedException
	 * 
	 * @param groupName the name of the group
	 * @return no content
	 * @throws PermissionDeniedException
	 */
	@DELETE
	@Path("{groupname}")
	public Response deleteGroup(@PathParam("groupname") String groupName) throws PermissionDeniedException {
		groupService.deleteGroup(groupName, new AuthContext(request));
		return Response.status(Status.NO_CONTENT).build();
	}
	/**
	 * remove a user from a group
	 * 
	 * @HTTP 204 no content
	 * @HTTP 403 PermissionDeniedException
	 * 
	 * @param groupName the name of the group
	 * @param username the name of the user
	 * @return no content
	 * @throws PermissionDeniedException
	 */
	@DELETE
	@Path("{groupname}/{username}")
	public Response removeUserFromGroup(@PathParam("groupname") String groupName,  @PathParam("username") String username) 
			throws PermissionDeniedException {
		groupService.removeFromGroup(groupName, username, new AuthContext(request));
		return Response.ok().build();
	}
}

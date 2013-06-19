package at.ait.dme.yuma.server.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import at.ait.dme.yuma.server.bootstrap.BaseTest;
import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.controller.AuthContext;
import at.ait.dme.yuma.server.db.entities.UserEntity;
import at.ait.dme.yuma.server.exception.PermissionDeniedException;
import at.ait.dme.yuma.server.model.Group;

public class GroupServiceTest extends BaseTest {
	
	IGroupService groupService;
	AuthContext auth;
	private final String groupName = "coolGroup";
	private final String userName = "coolio";
	private String userURI;
	
	@Before
	public void setup() {
		groupService = Config.getInstance().getGroupService();
		auth = new AuthContext("test", "test");
		userURI = UserEntity.toUri("test", userName, false).toString();
	}
	
	@Test
	public void addUserToGroup() throws PermissionDeniedException {
		groupService.addToGroup(groupName, userName, auth);
		Group g = groupService.findGroup(groupName);
		auth.toURI(true);
		Assert.assertTrue("not member:" + userURI, g.hasMember(userURI));
		groupService.removeFromGroup(groupName, userName, auth);
		g = groupService.findGroup(groupName);
		Assert.assertFalse(g.hasMember(userURI));
	}
	
	@Test
	public void getList() {
		List<Group> groups = groupService.getGroups(auth);
		Assert.assertNotNull(groups);
		Assert.assertFalse(groups.isEmpty());
		
		//auth = new AuthContext("coolio", "test1");
		//groups = groupService.getGroups(auth);
		//Assert.assertTrue(groups.isEmpty());
	}
	
	
	@Test(expected = PermissionDeniedException.class)
	public void notOwner() throws PermissionDeniedException {
		auth = new AuthContext("coolio", "test");
		groupService.addToGroup(groupName, "thisIsNotOK", auth);
	}
	
	@Test
	public void deleteGroup() throws PermissionDeniedException {
		groupService.deleteGroup(groupName, auth);
		Assert.assertNull(groupService.findGroup(groupName));
	}
}

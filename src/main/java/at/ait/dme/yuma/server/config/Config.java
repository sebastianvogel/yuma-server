package at.ait.dme.yuma.server.config;

import at.ait.dme.yuma.server.exception.AnnotationDatabaseException;
import at.ait.dme.yuma.server.model.Scope;
import at.ait.dme.yuma.server.service.IACLService;
import at.ait.dme.yuma.server.service.IAnnotationService;
import at.ait.dme.yuma.server.service.IMediaService;
import at.ait.dme.yuma.server.service.IGroupService;

/**
 * Configuration settings for the annotation server.
 * 
 * @author Christian Sadilek
 */
public class Config {
	//private static Logger logger = Logger.getLogger(Config.class);
	
	public static final String HeaderCheckReadPermissionsFor = "CheckPermissionsFor";

	private String serverBaseUrl;
	private String adminUsername;
	private String adminPassword;
	
	private IAnnotationService annotationService;
	private IACLService aclService;
	private IMediaService mediaService;
	private IGroupService groupService;

	private Scope scopePolicy;
	
	private Config()  {}

	public static Config getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	public void setServerBaseUrl(String serverBaseUrl) {
		this.serverBaseUrl = serverBaseUrl;
	}

	public void setAdminUsername(String adminUsername) {
		this.adminUsername = adminUsername;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}
	
	public void setAnnotationService(IAnnotationService annotationService) {
		this.annotationService = annotationService;
	}
	
	public String getServerBaseUrl() {
		return serverBaseUrl;
	}
	
	public String getAdminUsername() {
		return adminUsername;
	}
	
	public String getAdminPassword() {
		return adminPassword;
	}
	
	public IAnnotationService getAnnotationService() throws AnnotationDatabaseException {
		return annotationService;
	}
	
	public IACLService getAclService() {
		return aclService;
	}
	
	public IGroupService getGroupService() {
		return groupService;
	}

	public void setAclService(IACLService aclService) {
		this.aclService = aclService;
	}
	

	/**
	 * @return the mediaService
	 */
	public IMediaService getMediaService() {
		return mediaService;
	}

	/**
	 * @param mediaService the mediaService to set
	 */
	public void setMediaService(IMediaService mediaService) {
		this.mediaService = mediaService;
	}

	public void setGroupService(IGroupService groupService) {
		this.groupService = groupService;
	}

	public Scope getScopePolicy() {
		return scopePolicy;
	}

	public void setScopePolicyPublic(boolean bol) {
		this.scopePolicy = bol ? Scope.PUBLIC : Scope.PRIVATE;
	}
	public void setScopePolicy(Scope scopePolicy) {
		this.scopePolicy = scopePolicy;
	}

	private static class SingletonHolder { 
        public static final Config INSTANCE = new Config();
	}
}

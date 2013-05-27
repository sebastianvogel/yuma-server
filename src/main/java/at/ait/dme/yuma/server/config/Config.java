package at.ait.dme.yuma.server.config;

import at.ait.dme.yuma.server.exception.AnnotationDatabaseException;
import at.ait.dme.yuma.server.service.IACLService;
import at.ait.dme.yuma.server.service.IAnnotationService;
import at.ait.dme.yuma.server.service.IMediaObjectService;

/**
 * Configuration settings for the annotation server.
 * 
 * @author Christian Sadilek
 */
public class Config {
	//private static Logger logger = Logger.getLogger(Config.class);

	private String serverBaseUrl;
	private String adminUsername;
	private String adminPassword;
	
	private IAnnotationService annotationService;
	private IACLService aclService;
	private IMediaObjectService mediaObjectService;
	
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

	public void setAclService(IACLService aclService) {
		this.aclService = aclService;
	}
	
	/**
	 * @return the mediaObjectService
	 */
	public IMediaObjectService getMediaObjectService() {
		return mediaObjectService;
	}

	/**
	 * @param mediaObjectService the mediaObjectService to set
	 */
	public void setMediaObjectService(IMediaObjectService mediaObjectService) {
		this.mediaObjectService = mediaObjectService;
	}


	private static class SingletonHolder { 
        public static final Config INSTANCE = new Config();
	}
}

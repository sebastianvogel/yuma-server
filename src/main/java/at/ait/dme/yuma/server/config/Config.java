package at.ait.dme.yuma.server.config;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.ait.dme.yuma.server.db.AbstractAnnotationDB;
import at.ait.dme.yuma.server.exception.AnnotationDatabaseException;

/**
 * Configuration settings for the annotation server.
 * 
 * @author Christian Sadilek
 */
@Component
public class Config {
	private static Logger logger = Logger.getLogger(Config.class);

	private static Config singletonInstance;
	
	private String serverBaseUrl;
	private String suiteBaseUrl;
	private String adminUsername;
	private String adminPassword;
	
	private static AbstractAnnotationDB db;
	
	private Config()  {}

	public static Config getInstance() {
		if (singletonInstance == null)  {
			throw new IllegalStateException("configuration has not been initialized");
		}
		return singletonInstance;
	}
	
	@Autowired
	private void setAnnotationDB(AbstractAnnotationDB annotationDb) {
		db = annotationDb;
	}
	
	
	/**
	 * create instance of config
	 * @param serverBaseUrl
	 * @param suiteBaseUrl
	 * @param adminUsername
	 * @param adminPassword
	 */
	public static void createInstance(String serverBaseUrl,
			String suiteBaseUrl, String adminUsername, String adminPassword) {

		if (singletonInstance != null) {
			return;
		}
		singletonInstance = new Config();
		singletonInstance.serverBaseUrl = serverBaseUrl;
		singletonInstance.suiteBaseUrl = suiteBaseUrl;
		singletonInstance.adminUsername = adminUsername;
		singletonInstance.adminPassword = adminPassword;
	}
	
	public String getServerBaseUrl() {
		return serverBaseUrl;
	}
	
	public String getSuiteBaseUrl() {
		return suiteBaseUrl;
	}
	
	public String getAdminUsername() {
		return adminUsername;
	}
	
	public String getAdminPassword() {
		return adminPassword;
	}
	
	public AbstractAnnotationDB getAnnotationDatabase() throws AnnotationDatabaseException {
		return db;
	}
}

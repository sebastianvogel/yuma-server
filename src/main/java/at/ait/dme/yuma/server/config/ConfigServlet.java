package at.ait.dme.yuma.server.config;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import at.ait.dme.yuma.server.db.AbstractAnnotationDB;

/**
 * Configuration servlet.
 * 
 * @author Christian Sadilek
 */
public class ConfigServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(ConfigServlet.class);

	private static final long serialVersionUID = -4041232932917515128L;

	@Override
	public void init(ServletConfig config) throws ServletException {		
		ServletContext app = config.getServletContext();
	    String serverBaseUrl = app.getInitParameter("server.base.url");
	    String suiteBaseUrl = app.getInitParameter("suite.base.url");
	    String adminUsername = app.getInitParameter("admin.username");
	    String adminPassword = app.getInitParameter("admin.password");
	   
	    Config.createInstance(
	    		serverBaseUrl, suiteBaseUrl, 
	    		adminUsername, adminPassword);
	    try {
	    	logger.info("try to init database");
	    	Config.getInstance().getAnnotationDatabase().init();
	    } catch(Throwable t) {
	    	logger.fatal(t.getMessage(), t);
	    	throw new ServletException("failed to initialize annotation database");
	    }
	}
	
	@Override
	public void destroy() {
	    try {
	    	Config.getInstance().getAnnotationDatabase().shutdown();
	    } catch(Throwable t) {
	    	logger.fatal("failed to shutdown annotation database", t);
	    }
	}

}

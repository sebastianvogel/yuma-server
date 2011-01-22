package at.ait.dme.yuma.server.bootstrap;

import java.util.Collections;

import org.jboss.resteasy.plugins.server.tjws.TJWSEmbeddedJaxrsServer;

import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.exception.AnnotationDatabaseException;

/**
 * The code in all of the test setup methods is the same
 *
 * @author Gerald de Jong <geralddejong@gmail.com>
 * @author Christian Sadilek <christian.sadilek@gmail.com>
 * @author Rainer Simon
 */
public class Setup {
       
    public static void buildHibernateConfiguration() throws AnnotationDatabaseException {
    	new Config.Builder(
    			"at.ait.dme.yuma.server.db.hibernate.HibernateAnnotationDB",				
				"http://localhost:8081/yuma-server/",
				"http://localhost:8081/yuma-suite/",
				"admin",
				"admin"
		)    	
    			.dbDriver("org.postgresql.Driver")
    			.dbDriverProtocol("jdbc:postgresql")
    			.dbHost("localhost")
				.dbPort("5432")
				.dbName("ait")
				.dbUser("postgres")
				.dbPass("postgres").
				createInstance();
		
		Config.getInstance().getAnnotationDatabase().init();
    }
    
    public static void buildMongoDBConfiguration() throws AnnotationDatabaseException {
    	new Config.Builder(
    			"at.ait.dme.yuma.server.db.mongodb.MongoAnnotationDB",				
				"http://localhost:8081/yuma-server/",
				"http://localhost:8081/yuma-suite",
				"admin",
				"admin"
		)    	
				.dbHost("localhost")
				.dbPort("27017")
				.dbName("ait")
				.dbUser("mongodb")
				.dbPass("mongodb").
				createInstance();

		Config.getInstance().getAnnotationDatabase().init();
    }   
    
    public static void startEmbeddedJaxrsServer(Class <?> clazz) {
    	TJWSEmbeddedJaxrsServer tjws = new TJWSEmbeddedJaxrsServer();
		tjws.setBindAddress("localhost");
		tjws.setRootResourcePath("/yuma-server");
		tjws.setPort(8081);
		tjws.getDeployment().setResourceClasses(Collections.singletonList(clazz.getName()));
		tjws.start();
    }
}

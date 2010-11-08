package at.ait.dme.yuma.server.db.fast.test;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.db.fast.FastAnnotationDatabase;

/**
* Tests for the FAST annotation database
*  
* @author Christian Sadilek
*/
public class FastAnnotationDatabaseTest {
	
	@BeforeClass
	public static void setUp() {	
		new Config.Builder("at.researchstudio.dme.annotation.db.fast.FastAnnotationDatabase",
				"http://dme.arcs.ac.at/annotation-middleware/Annotation/",
				"http://dme.arcs.ac.at/annotation-middleware/Annotation/body").
				dbHost("http://svrims.dei.unipd.it").dbPort("8080").
				dbUser("fast").dbPass("dummypwd").
				createInstance();
	}
	
	@Ignore @Test
	public void testSearch() throws Exception {
		FastAnnotationDatabase db = new FastAnnotationDatabase();
		try {
			String term = "fast.annotation.annotatedDigitalObject.identifier = " +
				"\"http://wienwoche.files.wordpress.com/2007/10/tower_wien_schwechat.jpg\"";			
			db.connect();
			db.findAnnotations(term);
		} finally {
			db.disconnect();
		}
	}
}

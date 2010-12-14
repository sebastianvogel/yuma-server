package at.ait.dme.yuma.server.db.hibernate;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import at.ait.dme.yuma.server.bootstrap.Data;
import at.ait.dme.yuma.server.bootstrap.Setup;
import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.controller.json.JSONFormatHandler;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.SemanticTag;

public class HibernateAnnotationDBTest {

	@BeforeClass
	public static void setUp() throws Exception {		
		Setup.buildHibernateConfiguration();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		Config.getInstance().getAnnotationDatabase().shutdown();
	}
	
	@Test
	public void testHibernateCRUD() throws Exception {
		JSONFormatHandler format = new JSONFormatHandler();
		
		HibernateAnnotationDB db = new HibernateAnnotationDB();
		db.connect();
		
		// Create + Read
		Annotation before = format.parse(Data.ANNOTATION_JSON_UPDATE);
		for (SemanticTag t : before.getTags()) {
			System.out.println(t.getURI());
		}
		String id = db.createAnnotation(before);
		System.out.println(id);
	}
	
}

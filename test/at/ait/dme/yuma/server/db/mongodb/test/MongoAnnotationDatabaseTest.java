package at.ait.dme.yuma.server.db.mongodb.test;

import junit.framework.TestCase;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import at.ait.dme.yuma.server.Data;
import at.ait.dme.yuma.server.Setup;
import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.db.mongodb.MongoAnnotationDB;
import at.ait.dme.yuma.server.model.Annotation;

public class MongoAnnotationDatabaseTest extends TestCase {
	
	@BeforeClass
	public void setUp() throws Exception {				
		Setup.buildMongoDBConfiguration();
	}

	@AfterClass
	public void tearDown() throws Exception {
		Config.getInstance().getAnnotationDatabase().shutdown();
	}
	
	@Test
	public void testMongoDBCRUD() throws Exception {		
		MongoAnnotationDB db = new MongoAnnotationDB();
		db.connect();
		
		Annotation before = new Annotation(Data.JSON_ANNOTATION);
		String id = db.createAnnotation(before);
		Annotation after = db.findAnnotationById(id);
		assertEquals(before, after);
		assertEquals(id, after.getAnnotationID());
	}
	
}

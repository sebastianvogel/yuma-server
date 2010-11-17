package at.ait.dme.yuma.server.db.mongodb.test;

import junit.framework.TestCase;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import at.ait.dme.yuma.server.Data;
import at.ait.dme.yuma.server.Setup;
import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.db.mongodb.MongoAnnotationDB;
import at.ait.dme.yuma.server.exception.AnnotationHasReplyException;
import at.ait.dme.yuma.server.exception.AnnotationNotFoundException;
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
		
		// Create + Read
		Annotation before = new Annotation(Data.JSON_ANNOTATION_01);
		String id = db.createAnnotation(before);
		Annotation after = db.findAnnotationById(id);
		assertEquals(before, after);
		assertEquals(id, after.getAnnotationID());
		
		// Update
		Annotation beforeUpdate = new Annotation(Data.JSON_ANNOTATION_02);
		id = db.updateAnnotation(id, beforeUpdate);
		Annotation afterUpdate = db.findAnnotationById(id);
		assertFalse(afterUpdate.equals(after));
		assertEquals(beforeUpdate, afterUpdate);
		
		// Delete
		db.deleteAnnotation(id);
		try {
			db.findAnnotationById(id);
			fail("Annotation was not deleted");
		} catch (AnnotationNotFoundException e) {
			// Expected exception
		}
		
		// Store root annotation
		Annotation root = new Annotation(Data.JSON_ANNOTATION_01);
		String rootId = db.createAnnotation(root);
		
		// Store reply
		Annotation reply = new Annotation(Data.JSON_ANNOTATION_02.replace("@rootId@", rootId));
		String replyId = db.createAnnotation(reply);
		
		// Try deleting root
		try {
			db.deleteAnnotation(rootId);
			fail("Annotation that has replies must not be deletable!");
		} catch (AnnotationHasReplyException e) {
			// Expected
		}
		
		// Delete reply, then root
		db.deleteAnnotation(replyId);
		db.deleteAnnotation(rootId);
		
		try {
			db.deleteAnnotation(rootId);
			fail("Annotation was not deleted properly!");
		} catch (AnnotationNotFoundException e) {
			// Expected
		}		
	}
	
}

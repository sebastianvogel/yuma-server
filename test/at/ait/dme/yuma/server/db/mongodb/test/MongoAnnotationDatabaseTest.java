package at.ait.dme.yuma.server.db.mongodb.test;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

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
import at.ait.dme.yuma.server.model.AnnotationTree;

public class MongoAnnotationDatabaseTest {
	
	@BeforeClass
	public static void setUp() throws Exception {		
		Setup.buildMongoDBConfiguration();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		Config.getInstance().getAnnotationDatabase().shutdown();
	}
	
	@Test
	public void testMongoDBCRUD() throws Exception {		
		MongoAnnotationDB db = new MongoAnnotationDB();
		db.connect();
		
		// Create + Read
		Annotation before = new Annotation(Data.ANNOTATION_JSON_ORIGINAL);
		String id = db.createAnnotation(before);
		Annotation after = db.findAnnotationById(id);
		assertEquals(before, after);
		assertEquals(id, after.getAnnotationID());
		
		// Update
		Annotation beforeUpdate = new Annotation(Data.ANNOTATION_JSON_UPDATE);
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
	}
	
	@Test
	public void testReplies() throws Exception {
		MongoAnnotationDB db = new MongoAnnotationDB();
		db.connect();
		
		// Store annotation
		Annotation root = new Annotation(Data.ANNOTATION_JSON_ORIGINAL);
		String parentId = db.createAnnotation(root);
		
		// Store reply
		Annotation reply = new Annotation(Data.REPLY_JSON.replace("@parentId@", parentId));
		String replyId = db.createAnnotation(reply);
		
		// Check stored annotations
		long numberOfAnnotations = db.countAnnotationsForObject(root.getObjectID());
		assertEquals(2, numberOfAnnotations);
		AnnotationTree annotationTree = db.getAnnotationTreeForObject(root.getObjectID());
		assertEquals(1, annotationTree.getChildren(null).size());
		
		// Try delete root
		try {
			db.deleteAnnotation(parentId);
			fail("Annotation that has replies must not be deletable!");
		} catch (AnnotationHasReplyException e) {
			// Expected
		}
		
		// Delete reply, then root
		db.deleteAnnotation(replyId);
		db.deleteAnnotation(parentId);
	}
	
}

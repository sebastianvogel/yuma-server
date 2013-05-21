package at.ait.dme.yuma.server.db.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import at.ait.dme.yuma.server.bootstrap.BaseTest;
import at.ait.dme.yuma.server.bootstrap.Data;
import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.controller.json.JSONFormatHandler;
import at.ait.dme.yuma.server.exception.AnnotationHasReplyException;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.service.IAnnotationService;

public class HibernateAnnotationDBTest extends BaseTest {

	private static final String OBJ_URI = "http://dme.ait.ac.at/object/lissabon.jpg";
	
	@Test
	public void testHibernateCRUD() throws Exception {
		JSONFormatHandler format = new JSONFormatHandler();
		
		IAnnotationService db = Config.getInstance().getAnnotationService();
		
		// Create
		Annotation before = format.parse(Data.ANNOTATION_JSON_ORIGINAL);
		
		String id = db.createAnnotation(before, "test");
		System.out.println("Created: " + id);
		
		// Read
		Annotation annotation = db.findAnnotationById(id);
		System.out.println(format.serialize(annotation));
		
		// Update
		Annotation after = format.parse(Data.ANNOTATION_JSON_UPDATE);		
		id = db.updateAnnotation(id, after, "test");
		System.out.println("Updated to: " + id);
		
		// Create reply
		Annotation reply = format.parse(Data.reply(id, id));
		String replyId = db.createAnnotation(reply, "test");
		
		// Try delete root annotation
		try {
			db.deleteAnnotation(id, "test");
			
			fail("Annotation has reply - delete should fail!");
		} catch (AnnotationHasReplyException e) {
			// Expected
		}
		
		long count = db.countAnnotationsForObject(OBJ_URI);
		
		// Delete
		db.deleteAnnotation(replyId, "test");
		db.deleteAnnotation(id, "test");
		
		assertEquals(count - 2, db.countAnnotationsForObject(OBJ_URI));
		
		// Search
		List<Annotation> annotations = db.findAnnotations("ponte");
		for (Annotation a : annotations) {
			System.out.println(format.serialize(a));
		}
	}
	
}

package at.ait.dme.yuma.server.db.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import at.ait.dme.yuma.server.bootstrap.BaseTest;
import at.ait.dme.yuma.server.bootstrap.Data;
import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.controller.AuthContext;
import at.ait.dme.yuma.server.controller.json.JSONFormatHandler;
import at.ait.dme.yuma.server.exception.AnnotationHasReplyException;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.service.IAnnotationService;

public class HibernateAnnotationDBTest extends BaseTest {

	private static final String OBJ_URI = "http://dme.ait.ac.at/object/lissabon.jpg";
	
	@Test
	public void testHibernateCRUD() throws Exception {
		JSONFormatHandler format = new JSONFormatHandler();
		
		AuthContext auth = new AuthContext("test", "test");
		
		IAnnotationService db = Config.getInstance().getAnnotationService();
		
		// Create
		Annotation before = format.parse(Data.ANNOTATION_JSON_ORIGINAL);
		
		String id = db.createAnnotation(before, auth);
		System.out.println("Created: " + id);
		
		// Read
		Annotation annotation = db.findAnnotationById(id, auth);
		System.out.println(format.serialize(annotation));
		
		// Update
		Annotation after = format.parse(Data.ANNOTATION_JSON_UPDATE);		
		id = db.updateAnnotation(id, after, auth);
		System.out.println("Updated to: " + id);
		
		// Create reply
		Annotation reply = format.parse(Data.reply(id, id));
		String replyId = db.createAnnotation(reply, auth);
		
		// Try delete root annotation
		try {
			db.deleteAnnotation(id, auth);
			
			fail("Annotation has reply - delete should fail!");
		} catch (AnnotationHasReplyException e) {
			// Expected
		}
		
		long count = db.countAnnotationsForObject(OBJ_URI);
		
		// Delete
		db.deleteAnnotation(replyId, auth);
		db.deleteAnnotation(id, auth);
		
		assertEquals(count - 2, db.countAnnotationsForObject(OBJ_URI));
		
		// Search
		List<Annotation> annotations = db.findAnnotations("ponte", auth);
		for (Annotation a : annotations) {
			System.out.println(format.serialize(a));
		}
	}
	
}

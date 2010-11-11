package at.ait.dme.yuma.server.db.mongodb.test;

import java.util.Date;

import org.junit.Test;

import at.ait.dme.yuma.server.Data;
import at.ait.dme.yuma.server.db.mongodb.MongoAnnotationDB;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.SemanticTag;

public class MongoAnnotationDatabaseTest {

	@Test
	public void testCreateUpdateDeleteAnnotation() throws Exception {
		Annotation a = new Annotation();
		
		a.setAnnotationID(new Long(123198553));
		a.setRootId(new Long(2345345));
		a.setParentId(new Long(765423));
		a.setObjectID("asdf23adsf");
		a.setCreated(new Date());
		a.setLastModified(new Date());
		a.setCreatedBy("guest");
		a.setTitle("Test Aasdfnnotation");
		a.setText("Just some random test annotation.");
		a.setFragment("<svg/>");
		
		SemanticTag tagA = new SemanticTag();
		tagA.setTitle("tagA");
		a.addTag(tagA);
		
		SemanticTag tagB = new SemanticTag();
		tagB.setTitle("tagB");
		a.addTag(tagB);
		
		SemanticTag tagC = new SemanticTag();
		tagC.setTitle("tagC");
		a.addTag(tagC);
		
		a.addReply("reply01");
		a.addReply("reply02");
		a.addReply("reply03");
		a.addReply("reply04");
		a.addReply("reply05");
		a.addReply("reply06");
		
		MongoAnnotationDB db = new MongoAnnotationDB();
		db.connect();
		String id = db.createAnnotation(a);
		db.findAnnotationById(id);
	}
	
	@Test
	public void testJSON() throws Exception {		
		MongoAnnotationDB db = new MongoAnnotationDB();
		db.connect();
		
		String id = db.createAnnotation(new Annotation(Data.JSON_ANNOTATION));
		db.findAnnotationById(id);
	}
	
}

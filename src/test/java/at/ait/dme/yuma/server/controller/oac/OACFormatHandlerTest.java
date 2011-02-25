package at.ait.dme.yuma.server.controller.oac;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import at.ait.dme.yuma.server.bootstrap.Data;
import at.ait.dme.yuma.server.bootstrap.Setup;
import at.ait.dme.yuma.server.controller.json.JSONFormatHandler;
import at.ait.dme.yuma.server.controller.rdf.oac.OACFormatHandler;
import at.ait.dme.yuma.server.db.hibernate.HibernateAnnotationDB;
import at.ait.dme.yuma.server.exception.AnnotationDatabaseException;
import at.ait.dme.yuma.server.model.Annotation;

public class OACFormatHandlerTest {

	private HibernateAnnotationDB db;
	
	@BeforeClass
	public static void setUp() throws Exception {
		Setup.buildHibernateConfiguration();
	}

	@Test
	public void testSerializationWithFragment() throws Exception {
		Annotation before = new JSONFormatHandler().parse(Data.ANNOTATION_JSON_UPDATE);
		OACFormatHandler oacFormat = new OACFormatHandler();
		
		String serializedAnnotation = oacFormat.serialize(before);
		System.out.println(serializedAnnotation);
		
		Annotation after = oacFormat.parse(serializedAnnotation);
		assertEquals(before, after);
	}
	
	@Test
	public void testSerializationWithoutFragment() throws Exception {
		Annotation before = new JSONFormatHandler().parse(Data.ANNOTATION_JSON_NOFRAGMENT);
		OACFormatHandler oacFormat = new OACFormatHandler();
		
		String serializedAnnotation = oacFormat.serialize(before);
		System.out.println(serializedAnnotation);
		
		Annotation after = oacFormat.parse(serializedAnnotation);
		assertEquals(before, after);		
	}

	@Test
	public void testReplySerializationWithoutFragment() throws Exception 
	{	
		String rootId = getParentAnnotationId();
		String replyAnnotationJSON = Data.reply(rootId, rootId);
		Annotation replyAnnotation = new JSONFormatHandler().parse(replyAnnotationJSON);
		
		String oacSerializedReplyAnnotation = new OACFormatHandler().serialize(replyAnnotation);
		System.out.println(oacSerializedReplyAnnotation);
		
		cleanUp(rootId);
	}
	
	@Test
	public void testReplySerializationWithFragment() throws Exception {
		String rootId = getParentAnnotationId();
		String replyAnnotationWithFragmentJSON = Data.replyWithFragment(rootId, rootId);
		Annotation replyAnnotationWithFragment = new JSONFormatHandler().parse(replyAnnotationWithFragmentJSON);

		String oacSerializedReplyAnnotationWithFragment = new OACFormatHandler().serialize(replyAnnotationWithFragment);
		System.out.println(oacSerializedReplyAnnotationWithFragment);
		
		cleanUp(rootId);
	}
	
	private String getParentAnnotationId() throws Exception {
		establishDbConnection();
		
		Annotation root = new JSONFormatHandler().parse(Data.ROOT_JSON);
		return db.createAnnotation(root);
	}
	
	private void establishDbConnection() throws AnnotationDatabaseException {
		db = new HibernateAnnotationDB();
		db.connect();
	}

	private void cleanUp(String annotationId) throws Exception {
		db.deleteAnnotation(annotationId);
		db.disconnect();
	}
}

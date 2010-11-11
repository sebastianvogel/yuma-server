package at.ait.dme.yuma.server.db.concurrent;

import static org.junit.Assert.fail;

import java.util.concurrent.CountDownLatch;

import at.ait.dme.yuma.server.Data;
import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.db.mongodb.MongoAnnotationDB;
import at.ait.dme.yuma.server.exception.AnnotationNotFoundException;
import at.ait.dme.yuma.server.model.Annotation;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


/**
* Test for concurrent access to the annotation database
*  
* @author Christian Sadilek
* @author Rainer Simon
*/
public class ConcurrentTest {
	private static final int THREADS = 25;

	@BeforeClass
	public static void setUp() throws Exception {

	}

	@AfterClass
	public static void tearDown() throws Exception {
		Config.getInstance().getAnnotationDatabase().shutdown();
	}
	
	@Test
	public void testConcurrentCrud() throws Exception {
		final CountDownLatch startGate = new CountDownLatch(1);
		final CountDownLatch endGate = new CountDownLatch(THREADS);			
		
		for (int i = 0; i < THREADS; i++) {
			final int index = i;
			Thread thread = new Thread() {
				@Override
				public void run() {
					try {
						startGate.await();						
						MongoAnnotationDB db = new MongoAnnotationDB();						
						try {
							long start = System.currentTimeMillis();
							db.connect();						
							String id = db.createAnnotation(new Annotation(Data.JSON_ANNOTATION));
							db.disconnect();
							System.out.println("CREATE TIME for:"+index +"="+(System.currentTimeMillis()- start)+" ms" + " ID:"+ id);														

							start = System.currentTimeMillis();
							db.connect();																											
							id = db.updateAnnotation(id, new Annotation(Data.JSON_ANNOTATION));
							db.disconnect();
							System.out.println("UPDATE TIME for:"+index +"="+(System.currentTimeMillis()- start)+" ms");
					
							start = System.currentTimeMillis();																																			
							db.connect();
							db.deleteAnnotation(id);							
							db.disconnect();
							System.out.println("DELETE TIME for:"+index +"="+(System.currentTimeMillis()- start)+" ms");
						} finally {
							endGate.countDown();
							db.disconnect();
						}
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			};
			thread.start();
		}
		long start = System.currentTimeMillis();
		startGate.countDown();
		endGate.await();
		System.out.println("TOTAL TIME:"+(System.currentTimeMillis()- start)+" ms");
	}
	
	@Test
	public void testReadCommited() throws Exception {
		MongoAnnotationDB db1 = new MongoAnnotationDB();
		MongoAnnotationDB db2 = new MongoAnnotationDB();
		
		db1.setAutoCommit(false);
		String id = null;
		try {			
			db1.connect();
			id = db1.createAnnotation(new Annotation(Data.JSON_ANNOTATION));
			db2.connect();
			try {
				db2.findAnnotationById(id.toString());
				fail("AnnotationNotFoundException expected");
			} catch(AnnotationNotFoundException anfe) {
				/*expected */
			}
			db1.commit();
			db2.findAnnotationById(id.toString());			
		} finally {
			if(id!=null) {
				db1.deleteAnnotation(id.toString());
				db1.commit();
			}
			db1.disconnect();
			db2.disconnect();			
		}
		
	}
}

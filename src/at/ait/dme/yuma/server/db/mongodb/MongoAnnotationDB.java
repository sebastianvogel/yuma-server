package at.ait.dme.yuma.server.db.mongodb;

import java.net.UnknownHostException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import at.ait.dme.yuma.server.db.AbstractAnnotationDB;
import at.ait.dme.yuma.server.exception.AnnotationHasReplyException;
import at.ait.dme.yuma.server.exception.AnnotationDatabaseException;
import at.ait.dme.yuma.server.exception.AnnotationModifiedException;
import at.ait.dme.yuma.server.exception.AnnotationNotFoundException;
import at.ait.dme.yuma.server.model.Annotation;

public class MongoAnnotationDB extends AbstractAnnotationDB {
	
	private DBCollection collection = null;

	@Override
	public void init() throws AnnotationDatabaseException {
		
	}

	@Override
	public void shutdown() {
		
	}

	@Override
	public void connect(HttpServletRequest request)	throws AnnotationDatabaseException {
		if (collection == null) {
			try {
				Mongo m = new Mongo("localhost", 27017);
				DB db = m.getDB("yuma");
				collection = db.getCollection("annotations");	
			} catch (UnknownHostException e) {
				throw new AnnotationDatabaseException(e);
			} catch (MongoException e) {
				throw new AnnotationDatabaseException(e);
			}
		}
	}

	@Override
	public void disconnect() {
		
	}

	@Override
	public void commit() throws AnnotationDatabaseException {
		
	}

	@Override
	public void rollback() throws AnnotationDatabaseException {
		
	}

	@Override
	public String createAnnotation(Annotation annotation) throws AnnotationDatabaseException, AnnotationModifiedException {
		try {
			collection.insert(new BasicDBObject(annotation.toMap()));
			return "ok";
		} catch (MongoException e) {
			throw new AnnotationDatabaseException(e);
		}
	}

	@Override
	public String updateAnnotation(String annotationId, Annotation annotation)
			throws AnnotationDatabaseException, AnnotationHasReplyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAnnotation(String annotationId)
			throws AnnotationDatabaseException, AnnotationHasReplyException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Annotation> listAnnotations(String objectId)
			throws AnnotationDatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int countAnnotations(String objectId)
			throws AnnotationDatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Annotation> listAnnotationReplies(String annotationId)
			throws AnnotationDatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Annotation findAnnotationById(String annotationId)
			throws AnnotationDatabaseException, AnnotationNotFoundException {

		System.out.println(collection.findOne());
		collection.drop();
		return null;
	}

	@Override
	public List<Annotation> findAnnotations(String query)
			throws AnnotationDatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

}

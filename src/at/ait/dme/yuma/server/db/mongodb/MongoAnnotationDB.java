package at.ait.dme.yuma.server.db.mongodb;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.bson.types.ObjectId;

import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;

import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.db.AbstractAnnotationDB;
import at.ait.dme.yuma.server.exception.InvalidAnnotationException;
import at.ait.dme.yuma.server.exception.AnnotationHasReplyException;
import at.ait.dme.yuma.server.exception.AnnotationDatabaseException;
import at.ait.dme.yuma.server.exception.AnnotationModifiedException;
import at.ait.dme.yuma.server.exception.AnnotationNotFoundException;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.AnnotationTree;

/**
 * Annotation DB implementation based on the 
 * MongoDB key/value store.
 * 
 * @author Rainer Simon
 */
public class MongoAnnotationDB extends AbstractAnnotationDB {
	
	/**
	 * DB object ID key
	 */
	private static final String OID = "_id";
	
	/**
	 * MongoDB database connection
	 */
	private static Mongo MONGO = null;
	
	/**
	 * Database/collection name
	 */
	private static String DB_NAME;
	
	/**
	 * The annotations collection
	 */
	private DBCollection collection = null;
	
	@Override
	public synchronized void init() throws AnnotationDatabaseException {
		final Config config = Config.getInstance();
		try {
			if (MONGO == null) {
				MONGO = new Mongo(config.getDbHost(), Integer.parseInt(config.getDbPort()));
				DB_NAME = config.getDbName();
			}
		} catch (NumberFormatException e) {
			throw new AnnotationDatabaseException(e);
		} catch (MongoException e) {
			throw new AnnotationDatabaseException(e);
		} catch (UnknownHostException e) {
			throw new AnnotationDatabaseException(e);
		}
	}

	@Override
	public void shutdown() {
		if (MONGO != null) MONGO.close();
	}

	@Override
	public void connect(HttpServletRequest request)	throws AnnotationDatabaseException {
		if (MONGO == null) 
			throw new AnnotationDatabaseException("Database not initialized");
		
		try {
			DB db = MONGO.getDB(DB_NAME);
			collection = db.getCollection(DB_NAME);
		} catch (MongoException e) {
			throw new AnnotationDatabaseException(e.getMessage());
		}
	}

	@Override
	public void disconnect() {
		//
	}

	@Override
	public void commit() throws AnnotationDatabaseException {
		// TODO check http://www.mongodb.org/display/DOCS/Atomic+Operations
	}

	@Override
	public void rollback() throws AnnotationDatabaseException {
		// TODO check http://www.mongodb.org/display/DOCS/Atomic+Operations
	}

	@Override
	public String createAnnotation(Annotation annotation) throws AnnotationDatabaseException, AnnotationModifiedException {
		try {
			// TODO check for consistency - if it's a reply it needs to have the same objectID etc.
			BasicDBObject dbo = new BasicDBObject(annotation.toMap());
			collection.insert(dbo);
			return ((ObjectId) dbo.get(OID)).toString();
		} catch (MongoException e) {
			throw new AnnotationDatabaseException(e);
		}
	}

	@Override
	public String updateAnnotation(String annotationId, Annotation annotation)
			throws AnnotationDatabaseException, AnnotationNotFoundException, AnnotationHasReplyException {
		
		DBObject before = findDBObjectByAnnotationID(annotationId);

		if (countReplies(annotationId) > 0)
			throw new AnnotationHasReplyException();
		
		BasicDBObject after = new BasicDBObject(annotation.toMap());
		collection.update(before, after);

		// Note: MongoDB does not change the ID on updates
		return annotationId;
	}

	@Override
	public void deleteAnnotation(String annotationId)
			throws AnnotationDatabaseException, AnnotationNotFoundException, AnnotationHasReplyException {
		
		DBObject dbo = findDBObjectByAnnotationID(annotationId);

		if (countReplies(annotationId) > 0)
			throw new AnnotationHasReplyException();
		
		collection.remove(dbo);
	}

	@Override
	public AnnotationTree getAnnotationTreeForObject(String objectId)
			throws AnnotationDatabaseException {
		
		ArrayList<Annotation> annotations = new ArrayList<Annotation>();
		
		BasicDBObject query = new BasicDBObject();
		query.put(Annotation.OBJECT_ID, objectId);
		DBCursor cursor = collection.find(query);
		
		while (cursor.hasNext()) {
			try {
				annotations.add(new Annotation(cursor.next().toMap()));
			} catch (InvalidAnnotationException e) {
				// Should never happen
				throw new AnnotationDatabaseException(e);
			}
		}
		
		return new AnnotationTree(annotations);
	}
	
	@Override
	public long countAnnotationsForObject(String objectId)
			throws AnnotationDatabaseException {

		BasicDBObject query = new BasicDBObject();
		query.put(Annotation.OBJECT_ID, objectId);
		return collection.count(query);
	}

	@Override
	public Annotation findAnnotationById(String annotationId)
			throws AnnotationDatabaseException, AnnotationNotFoundException {

		try {
			Annotation annotation = new Annotation(findDBObjectByAnnotationID(annotationId).toMap());
			annotation.setAnnotationID(annotationId);
			
			return annotation;
		} catch (InvalidAnnotationException e) {
			// Should never happen
			throw new AnnotationDatabaseException(e);
		}
	}
	
	@Override
	public long countReplies(String annotationId)
		throws AnnotationDatabaseException, AnnotationNotFoundException {

		BasicDBObject query = new BasicDBObject();
		query.put(Annotation.PARENT_ID, annotationId);
		return collection.count(query);
	}
	
	@Override
	public AnnotationTree findThreadForAnnotation(String annotationId)
			throws AnnotationDatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Annotation> findAnnotations(String query)
			throws AnnotationDatabaseException {

		// TODO implement correctly!
		BasicDBObject dbo = new BasicDBObject();
		dbo.put(Annotation.TITLE, query);
		dbo.put(Annotation.TEXT, query);
		
		DBCursor cursor = collection.find(dbo);
		ArrayList<Annotation> results = new ArrayList<Annotation>();
		while(cursor.hasNext()) {
			try {
				results.add(new Annotation(cursor.next().toMap()));
			} catch (InvalidAnnotationException e) {
				throw new AnnotationDatabaseException(e);
			}
		}
		return results;
	}
	
	private DBObject findDBObjectByAnnotationID(String annotationId)
		throws AnnotationDatabaseException, AnnotationNotFoundException {
		
		BasicDBObject query = new BasicDBObject();
		query.put(OID, new ObjectId(annotationId));
		DBCursor cursor = collection.find(query);
		
		if (cursor.count() > 1)
			// Should never happen
			throw new AnnotationDatabaseException("More than one object for this ID");
		
		if (cursor.count() > 0)
				return cursor.next();
		
		throw new AnnotationNotFoundException();
	}

}

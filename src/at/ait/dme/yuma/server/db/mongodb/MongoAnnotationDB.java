package at.ait.dme.yuma.server.db.mongodb;

import java.net.UnknownHostException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.bson.types.ObjectId;

import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;

import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.db.AbstractAnnotationDB;
import at.ait.dme.yuma.server.exception.AnnotationFormatException;
import at.ait.dme.yuma.server.exception.AnnotationHasReplyException;
import at.ait.dme.yuma.server.exception.AnnotationDatabaseException;
import at.ait.dme.yuma.server.exception.AnnotationModifiedException;
import at.ait.dme.yuma.server.exception.AnnotationNotFoundException;
import at.ait.dme.yuma.server.model.Annotation;

public class MongoAnnotationDB extends AbstractAnnotationDB {
	
	private static final String OID = "_id";
	
	/**
	 * Singleton MongoDB database connection
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
			BasicDBObject dbo = new BasicDBObject(annotation.toMap());
			collection.insert(dbo);
			return ((ObjectId) dbo.get(OID)).toString();
		} catch (MongoException e) {
			throw new AnnotationDatabaseException(e);
		}
	}

	@Override
	public String updateAnnotation(String annotationId, Annotation annotation)
			throws AnnotationDatabaseException, AnnotationHasReplyException {
		
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

		BasicDBObject query = new BasicDBObject();
		query.put(OID, new ObjectId(annotationId));
		DBCursor cursor = collection.find(query);
		
		if (cursor.count() > 1)
			// Should never happen
			throw new AnnotationDatabaseException("More than one object for this ID");
		
		if (cursor.count() > 0) {
			try {
				return new Annotation(cursor.next().toString());
			} catch (AnnotationFormatException e) {
				// Should never happen
				throw new AnnotationDatabaseException(e);
			}
		}
		
		throw new AnnotationNotFoundException();
	}

	@Override
	public List<Annotation> findAnnotations(String query)
			throws AnnotationDatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

}

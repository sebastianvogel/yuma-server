package at.ait.dme.yuma.server.db.europeana;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import at.ait.dme.yuma.server.db.AbstractAnnotationDB;
import at.ait.dme.yuma.server.exception.AnnotationDatabaseException;
import at.ait.dme.yuma.server.exception.AnnotationHasReplyException;
import at.ait.dme.yuma.server.exception.AnnotationModifiedException;
import at.ait.dme.yuma.server.exception.AnnotationNotFoundException;
import at.ait.dme.yuma.server.exception.InvalidAnnotationException;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.AnnotationTree;

public class EuropeanaAnnotationAPI extends AbstractAnnotationDB {

	@Override
	public void init() throws AnnotationDatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connect(HttpServletRequest request)
			throws AnnotationDatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commit() throws AnnotationDatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rollback() throws AnnotationDatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String createAnnotation(Annotation annotation)
			throws AnnotationDatabaseException, AnnotationModifiedException,
			InvalidAnnotationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateAnnotation(String annotationId, Annotation annotation)
			throws AnnotationDatabaseException, AnnotationNotFoundException,
			AnnotationHasReplyException, InvalidAnnotationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAnnotation(String annotationId)
			throws AnnotationDatabaseException, AnnotationNotFoundException,
			AnnotationHasReplyException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AnnotationTree findAnnotationsForObject(String objectUri)
			throws AnnotationDatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long countAnnotationsForObject(String objectUri)
			throws AnnotationDatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Annotation> findAnnotationsForUser(String username)
			throws AnnotationDatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Annotation findAnnotationById(String annotationId)
			throws AnnotationDatabaseException, AnnotationNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long countReplies(String annotationId)
			throws AnnotationDatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public AnnotationTree findThreadForAnnotation(String annotationId)
			throws AnnotationDatabaseException, AnnotationNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Annotation> getMostRecent(int n)
			throws AnnotationDatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Annotation> findAnnotations(String query)
			throws AnnotationDatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

}

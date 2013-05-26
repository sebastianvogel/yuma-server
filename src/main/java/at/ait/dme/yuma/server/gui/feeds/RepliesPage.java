package at.ait.dme.yuma.server.gui.feeds;

import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.protocol.http.servlet.AbortWithHttpStatusException;

import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.exception.AnnotationNotFoundException;
import at.ait.dme.yuma.server.exception.PermissionDeniedException;
import at.ait.dme.yuma.server.gui.BaseAnnotationListPage;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.service.IAnnotationService;

public class RepliesPage extends BaseAnnotationListPage {
	
	//private Logger logger = Logger.getLogger(RepliesPage.class);
	
	public static final String PARAM_PARENT_ID = "parentId";
	
	private static final String TITLE = "YUMA Annotation Server - Replies to ";
	private static final String HEADLINE = "Replies to ";
	private static final String FEEDS = "feeds/replies/";
	
	public RepliesPage(final PageParameters parameters) throws AnnotationNotFoundException, PermissionDeniedException {
		Annotation parent = 
			getParentAnnotation(parameters.getString(PARAM_PARENT_ID));		
		
		if (parent == null) 
			throw new AbortWithHttpStatusException(404, true);

		setTitle(TITLE + "'" + parent.getTitle() + "'");
		setHeadline(HEADLINE + "'" + parent.getTitle() + "'");		
		setAnnotations(getReplies(parent.getAnnotationID()));
		setFeedURL(Config.getInstance().getServerBaseUrl() + FEEDS + parent.getAnnotationID());
	}
	
	private Annotation getParentAnnotation(String id) throws AnnotationNotFoundException, PermissionDeniedException {
		IAnnotationService annotationService = Config.getInstance().getAnnotationService();
		return annotationService.findAnnotationById(id, null);
	}
	
	private List<Annotation> getReplies(String id) throws AnnotationNotFoundException, PermissionDeniedException {
		IAnnotationService annotationService = Config.getInstance().getAnnotationService();
		return annotationService.getReplies(id, null).asFlatList();
	}

}

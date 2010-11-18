package at.ait.dme.yuma.server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class represents the entire tree structure of annotations
 * that exist for an annotated object. The tree consists of
 * root annotations (the annotations that directly reference the
 * annotated object) and replies (annotations that reference another
 * annotation)
 * 
 * @author Rainer Simon
 */
public class AnnotationTree {
	
	/**
	 * Root annotations in this tree
	 */
	private ArrayList<Annotation> rootAnnotations = new ArrayList<Annotation>();
	
	/**
	 * Replies in this tree (mapped by parent ID)
	 */
	private HashMap<String, ArrayList<Annotation>> replies = new HashMap<String, ArrayList<Annotation>>();
	
	public AnnotationTree(List<Annotation> annotations) {
		// Temporary map to store replies while building the tree
		HashMap<String, ArrayList<Annotation>> allReplies = new HashMap<String, ArrayList<Annotation>>();
		
		for (Annotation a : annotations) {
			if (a.getParentId() == null) {
				// Add to root annotation list
				rootAnnotations.add(a);
			} else {
				// Add to appropriate reply list
				ArrayList<Annotation> replyList = allReplies.get(a.getParentId());
				if (replyList == null) 
					replyList = new ArrayList<Annotation>();
				replyList.add(a);
				allReplies.put(a.getParentId(), replyList);
			}
		}
	}
	
	/**
	 * Returns the child annotations (i.e. direct replies) for the 
	 * given annotation ID. If null is specified as annotation ID,
	 * this method will return the root annotations of this tree.
	 * @param annotationId the annotation ID (or null)
	 * @return the child (or root) annotations 
	 */
	public List<Annotation> getChildren(String annotationId) {
		if (annotationId == null)
			return rootAnnotations;
		
		return replies.get(annotationId);
	}
	
	@Override
	public String toString() {
		// TODO implement this!
		return null;
	}

}

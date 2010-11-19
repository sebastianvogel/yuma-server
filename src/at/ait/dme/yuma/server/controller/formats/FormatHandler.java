package at.ait.dme.yuma.server.controller.formats;

import java.util.List;

import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.AnnotationTree;
import at.ait.dme.yuma.server.exception.InvalidAnnotationException;

/**
 * The interface each format handler needs to implement.
 * An implementation does not need to support each interface
 * functionality (e.g. some implementations may only support
 * serialization, but not parsing). In this case, implementations
 * shall raise an
 *  
 * @author Rainer Simon
 */
public interface FormatHandler {
	
	/**
	 * Parses a string representation of the given format to an annotation. 
	 * @param serialized the serialized representation
	 * @return the annotation
	 * @throws UnsupportedOperationException if this implementation does not support parsing
	 */
	public Annotation parse(String serialized)
		throws UnsupportedOperationException, InvalidAnnotationException;
	
	/**
	 * Serializes an annotation to a representation in the given format.
	 * @param annotation the annotation
	 * @return the serialized representation
	 * @throws UnsupportedOperationException if this implementation does not support serialization
	 */
	public String serialize(Annotation annotation)
		throws UnsupportedOperationException;

	/**
	 * Serializes an annotation tree to a representation in the given format.
	 * @param tree the annotation tree
	 * @return the serialized representation
	 * @throws UnsupportedOperationException if this implementation does not support serialization
	 */
	public String serialize(AnnotationTree tree)
		throws UnsupportedOperationException;
	
	/**
	 * Serializes a flat list of annotations (e.g. search result) into
	 * the given format.
	 * @param annotations the list of annotations
	 * @return the serialized representation
	 * @throws UnsupportedOperationException
	 */
	public String serialize(List<Annotation> annotations)
		throws UnsupportedOperationException;
	
}

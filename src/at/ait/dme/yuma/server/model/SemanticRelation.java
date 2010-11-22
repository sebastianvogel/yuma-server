package at.ait.dme.yuma.server.model;

/**
 * A 'semantic relation', used to express a typed link between an 
 * annotated item and a semantic tag. 
 * 
 * @author Rainer Simon
 *
 */
public class SemanticRelation {
	
	/**
	 * Namespace URI of this relation
	 */
	private String namespace;
	
	/**
	 * Relation property
	 */
	private String property;
	
	public SemanticRelation(String namespace, String property) {
		this.namespace = namespace;
		this.property = property;
	}
	
	public String getNamespace() {
		return namespace;
	}
	
	public String getProperty() {
		return property;
	}

}

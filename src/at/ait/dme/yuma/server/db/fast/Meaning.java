package at.ait.dme.yuma.server.db.fast;

import java.io.IOException;
import java.io.StringWriter;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

/**
 * represents a meaning in FAST
 *  
 * @author Christian Sadilek
 */
public class Meaning {
	private static final String MEANING = "meaning";		
	private static final String NAMESPACE = "namespace";	
	private static final String DESCRIPTION = "description";
	private static final String IDENTIFIER = "identifier";
	
	private String id;
	private String namespace;
	private String description;
	
		
	public Meaning(String id, String namespace, String description) {
		super();
		this.id = id;
		this.namespace = namespace;
		this.description = description;
	}

	public String getId() {
		return id;
	}
	
	public String getNamespace() {
		return namespace;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		Element meaning = new Element(MEANING);
		meaning.setAttribute(new Attribute(DESCRIPTION,description));
		meaning.setAttribute(new Attribute(IDENTIFIER,id));
		meaning.setAttribute(new Attribute(NAMESPACE,namespace));
		
		StringWriter sw = new StringWriter();
		XMLOutputter xmlOutputter = new XMLOutputter();
		try {
			xmlOutputter.output(meaning, sw);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return sw.toString();
	}			
}

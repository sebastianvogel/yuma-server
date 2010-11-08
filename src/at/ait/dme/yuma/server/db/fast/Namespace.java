package at.ait.dme.yuma.server.db.fast;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


/**
 * represents a namespace in FAST
 *  
 * @author Christian Sadilek
 */
public class Namespace {
	private static final String NAMESPACE = "namespace";	
	private static final String DESCRIPTION = "description";
	private static final String IDENTIFIER = "identifier";
	private static final String PREFIX = "prefix";
	private static final String TRUSTED_NAMESPACES = "trusted-namespaces";			
	
	private String id;
	private String prefix;
	private String description;
	private Collection<String> trustedNamespaceIds = new ArrayList<String>();
		
	public Namespace(String id, String prefix, String description) {
		super();
		this.id = id;
		this.prefix = prefix;
		this.description = description;
	}

	public String getId() {
		return id;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void addTrustedNamespace(String id) {
		trustedNamespaceIds.add(id);
	}
	
	public Collection<String> getTrustedNamespaceIds() {
		return trustedNamespaceIds;
	}

	@Override
	public String toString() {
		Element namespace = new Element(NAMESPACE);
		namespace.setAttribute(new Attribute(DESCRIPTION,description));
		namespace.setAttribute(new Attribute(IDENTIFIER,id));
		namespace.setAttribute(new Attribute(PREFIX,prefix));
		
		Element trustedNamespaces = new Element(TRUSTED_NAMESPACES);
		for(String trustedNamespaceId : trustedNamespaceIds) {
			Element trustedNamespace = new Element(NAMESPACE);
			trustedNamespace.setAttribute(IDENTIFIER, trustedNamespaceId);
			trustedNamespaces.addContent(trustedNamespace);
		}
		namespace.addContent(trustedNamespaces);
		
		StringWriter sw = new StringWriter();
		XMLOutputter xmlOutputter = new XMLOutputter();
		try {
			xmlOutputter.output(namespace, sw);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return sw.toString();
	}			
}

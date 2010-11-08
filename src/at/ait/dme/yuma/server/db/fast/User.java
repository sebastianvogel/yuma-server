package at.ait.dme.yuma.server.db.fast;

import java.io.IOException;
import java.io.StringWriter;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

/**
 * represents a user in FAST
 *  
 * @author Christian Sadilek
 */
public class User {
	private static final String USER = "user";		
	private static final String IDENTIFIER = "identifier";
	private static final String NAME = "full-name";		
	private static final String PASSWORD = "password";	
	
	private static final String LANGUAGE = "language";		
	private static final String COUNTRY = "country";	
	private static final String MAIL = "e-mail";	

	private static final String NAMESPACE = "namespace";	
	
	private String id;
	private String name;
	private String password;	
	private String mail;
	private String country;
	private String language;
	private String namespace;

	
	public User(String id, String name, String password, String mail, String country, 
			String language, String namespace) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.mail = mail;
		this.country = country;
		this.language = language;
		this.namespace = namespace;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getMail() {
		return mail;
	}

	public String getPassword() {
		return password;
	}

	public String getCountry() {
		return country;
	}

	public String getLanguage() {
		return language;
	}

	public String getNamespace() {
		return namespace;
	}

	@Override
	public String toString() {
		Element user = new Element(USER);
		user.setAttribute(new Attribute(IDENTIFIER,id));
		user.setAttribute(new Attribute(NAME,name));
		user.setAttribute(new Attribute(PASSWORD,password));
		user.setAttribute(new Attribute(MAIL,mail));
		user.setAttribute(new Attribute(COUNTRY,country));
		user.setAttribute(new Attribute(LANGUAGE,language));		
		user.setAttribute(new Attribute(NAMESPACE,namespace));
		
		StringWriter sw = new StringWriter();
		XMLOutputter xmlOutputter = new XMLOutputter();
		try {
			xmlOutputter.output(user, sw);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return sw.toString();
	}			
}

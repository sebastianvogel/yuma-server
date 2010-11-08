package at.ait.dme.yuma.server.db.fast;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.log4j.Logger;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClientExecutor;
import org.jboss.resteasy.util.HttpResponseCodes;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.db.AnnotationDatabase;
import at.ait.dme.yuma.server.exception.AnnotationAlreadyReferencedException;
import at.ait.dme.yuma.server.exception.AnnotationDatabaseException;
import at.ait.dme.yuma.server.exception.AnnotationException;
import at.ait.dme.yuma.server.exception.AnnotationNotFoundException;

/**
 * Implementation of AnnotationDatabase for FAST (unipd)
 * 
 * @author Christian Sadilek
 */
public class FastAnnotationDatabase extends AnnotationDatabase {
	private static Logger logger = Logger.getLogger(FastAnnotationDatabase.class);
	
	private static final String URL_ENCODING = "UTF-8";	
	private static final String ANNOTATION_BODY_XPATH = "//html";
	private static final String FAST_SEARCH_RESULT_ITEM_ID_XPATH = 
		"/f:fast/f:result/f:item/@identifier";
	
	private static String CQL_ANNOTATES_FIELD = 
		"fast.annotation.annotatedDigitalObject.identifier";	
	private static String CQL_RELATEDTO_FIELD = 
		"fast.annotation.relatedDigitalObject.identifier";
	
	private static String COOKIE_NAME_USER = "lemo_user";
	
	private FastService fastService = null;
	
	public FastAnnotationDatabase() throws AnnotationDatabaseException {		
		setAutoCommit(true);
	}	
	
	@Override
	public void connect(HttpServletRequest request) throws AnnotationDatabaseException {
		try {
			HttpClient client = new HttpClient();
			Credentials defaultcreds = new UsernamePasswordCredentials(
					getUserNameFromCookie(request),					
					Config.getInstance().getDbPass());
			client.getState().setCredentials(new AuthScope(
					AuthScope.ANY_HOST, 
					AuthScope.ANY_PORT, 
					AuthScope.ANY_REALM), 
					defaultcreds);
			client.getParams().setAuthenticationPreemptive(true);

			fastService = ProxyFactory.create(FastService.class,
					Config.getInstance().getDbHost()+":"+
					Config.getInstance().getDbPort(),
					new ApacheHttpClientExecutor(client));			
		} catch(Throwable t) {
			logger.error(t.getMessage(), t);		
			throw new AnnotationDatabaseException(t);
		}
	}
	
	public void testConnect() throws AnnotationDatabaseException {
		try {
			fastService = ProxyFactory.create(FastService.class,
				"http://svrims.dei.unipd.it:8080");				
		} catch(Throwable t) {
			logger.error(t.getMessage(), t);		
			throw new AnnotationDatabaseException(t);
		}
	}
	
	@Override
	public void disconnect() {
		fastService = null;
	}
	
	@Override
	public void init() throws AnnotationDatabaseException {}

	@Override
	public void shutdown() {}
	
	@Override
	public void commit() throws AnnotationDatabaseException {
		throw new UnsupportedOperationException("auto commit is on");
	}

	@Override
	public void rollback() throws AnnotationDatabaseException {
		throw new UnsupportedOperationException("auto commit is on");
	}

	@Override
	public URI createAnnotation(String annotation) throws AnnotationDatabaseException, 
		AnnotationException {
		
		try {
			String id = createUniqueId();
			annotation = FastAnnotationBuilder.toFast(id, annotation);			
			ClientResponse<String> response=fastService.createAnnotation(annotation);
		
			if(response.getStatus()!=HttpResponseCodes.SC_CREATED)
				throw new AnnotationDatabaseException(UNEXPECTED_RESPONSE+":"+
						response.getStatus());
			
			return new URI(Config.getInstance().getAnnotationBaseUrl()+id);			
		} catch (IOException ioe) {
			logger.error(FAILED_TO_PARSE_ANNOTATION, ioe);
			throw new AnnotationException(ioe);			
		} catch (JDOMException je) {
			logger.error(je.getMessage(), je);
			throw new AnnotationException(je);					
		} catch (URISyntaxException urie) {
			logger.error(urie.getMessage(), urie);
			throw new AnnotationDatabaseException(urie);
		} catch (AnnotationDatabaseException ade) {
			logger.error(FAILED_TO_SAVE_ANNOTATION, ade);
			throw ade;
		} catch(Throwable t) {
			logger.error(t.getMessage(), t);		
			throw new AnnotationDatabaseException(t);
		}
	}

	@Override
	public URI updateAnnotation(String id, String annotation) throws AnnotationDatabaseException, 
		AnnotationAlreadyReferencedException, AnnotationException {
		
		try {
			String fastid = createValidFastAnnotationId(id);
			annotation = FastAnnotationBuilder.toFast(fastid, annotation);
			ClientResponse<String> response=fastService.updateAnnotation(fastid,annotation);
			
			if(response.getStatus()==HttpResponseCodes.SC_CONFLICT)
				throw new AnnotationAlreadyReferencedException();
			
			if(response.getStatus()!=HttpResponseCodes.SC_CREATED)
				throw new AnnotationDatabaseException(UNEXPECTED_RESPONSE+":"+
						response.getStatus());
			
			return new URI(Config.getInstance().getAnnotationBaseUrl()+id);		
		} catch (IOException ioe) {
			logger.error(FAILED_TO_PARSE_ANNOTATION, ioe);
			throw new AnnotationException(ioe);
		} catch (JDOMException je) {
			logger.error(je.getMessage(), je);
			throw new AnnotationException(je);		
		} catch (AnnotationDatabaseException ade) {
			logger.error(FAILED_TO_SAVE_ANNOTATION, ade);
			throw ade;
		} catch (AnnotationAlreadyReferencedException aare) {
			logger.error(aare.getMessage(), aare);
			throw aare;
		} catch(Throwable t) {
			logger.error(t.getMessage(), t);		
			throw new AnnotationDatabaseException(t);
		}
	}

	@Override
	public void deleteAnnotation(String id) throws AnnotationDatabaseException, 
		AnnotationAlreadyReferencedException, AnnotationException {
		
		try {
			id = createValidFastAnnotationId(id);
			
			ClientResponse<String> response=fastService.deleteAnnotation(id);			
			
			if(response.getStatus()==HttpResponseCodes.SC_CONFLICT)
				throw new AnnotationAlreadyReferencedException();			
			
			if(response.getStatus()!=HttpResponseCodes.SC_OK && 
					response.getStatus()!=HttpResponseCodes.SC_NO_CONTENT)
				throw new AnnotationDatabaseException(UNEXPECTED_RESPONSE+":"+
						response.getStatus());
			
		} catch (AnnotationDatabaseException ade) {
			logger.error(FAILED_TO_DELETE_ANNOTATION, ade);
			throw ade;
		}catch (AnnotationAlreadyReferencedException aare) {
			logger.error(aare.getMessage(), aare);
			throw aare;		
		} catch(Throwable t) {
			logger.error(t.getMessage(), t);		
			throw new AnnotationDatabaseException(t);
		}
	}

	private List<String> executeSearch(String term) throws AnnotationDatabaseException, 
		JDOMException, IOException {
		
		ClientResponse<String> response=fastService.search(term);			
		
		if(response.getStatus()!=HttpResponseCodes.SC_OK)
			throw new AnnotationDatabaseException(UNEXPECTED_RESPONSE+":"+
					response.getStatus());
		
		Document document = new SAXBuilder().build(new StringReader(response.getEntity()));			
		XPath xPath = XPath.newInstance(FAST_SEARCH_RESULT_ITEM_ID_XPATH);
		xPath.addNamespace(FastAnnotationBuilder.FAST_NS);
		List<Attribute> itemIds = xPath.selectNodes(document);
		
		List<String> annotationIds = new ArrayList<String>();
		for(Attribute itemId : itemIds) annotationIds.add(itemId.getValue());
		return annotationIds;
	}
	
	@Override
	public String listAnnotations(String objectId) throws AnnotationDatabaseException, 
		AnnotationException {
		
		return findAnnotations(CQL_ANNOTATES_FIELD + "=\"" + objectId +"\"");
	}

	@Override
	public String findAnnotations(String term) throws AnnotationDatabaseException {
		try {
			List<String> annotationIds = executeSearch(term);
			if(annotationIds.isEmpty()) return "";
			
			// we now have to request each annotation separately which is not efficient but the 
			// only way to do this in FAST.
			Document mainDoc  = null;
			for(String annotationId : annotationIds) {
				String annotation=findAnnotationById(annotationId);
				if(mainDoc==null)
					mainDoc = new SAXBuilder().build(new StringReader(annotation));
				else {
					Document subDoc = new SAXBuilder().build(new StringReader(annotation));
					Element rdfDesc=subDoc.getRootElement().getChild(
							FastAnnotationBuilder.RDF_DESCRIPTION,
							FastAnnotationBuilder.RDF_NS);
					mainDoc.getRootElement().addContent((Element)rdfDesc.clone());
				}									
			}			
			StringWriter sw = new StringWriter();
			new XMLOutputter(Format.getPrettyFormat()).output(mainDoc, sw);
			return sw.toString();		
		} catch (IOException ioe) {
			logger.error(FAILED_TO_PARSE_ANNOTATION, ioe);
			throw new AnnotationDatabaseException(ioe);			
		} catch (JDOMException je) {
			logger.error(FAILED_TO_PARSE_ANNOTATION, je);
			throw new AnnotationDatabaseException(je);		
		} catch(Throwable t) {
			logger.error(t.getMessage(), t);		
			throw new AnnotationDatabaseException(t);
		}
	}
	
	@Override
	public int countAnnotations(String objectId) throws AnnotationDatabaseException, 
		AnnotationException {
		
		try {
			return executeSearch(CQL_ANNOTATES_FIELD + "=\"" + objectId +"\"").size();
		} catch (JDOMException jde) {
			throw new AnnotationDatabaseException(jde);
		} catch (IOException ioe) {
			throw new AnnotationDatabaseException(ioe);
		}
	}
	
	@Override
	public String findAnnotationById(String id) throws AnnotationDatabaseException, 
			AnnotationNotFoundException {
	
		try {
			id = createValidFastAnnotationId(id);

			ClientResponse<String> response=fastService.retrieveAnnotation(id);			
			if(response.getStatus()==HttpResponseCodes.SC_NOT_FOUND)
				throw new AnnotationNotFoundException(response.getEntity());
			
			if(response.getStatus()!=HttpResponseCodes.SC_OK)
				throw new AnnotationDatabaseException(UNEXPECTED_RESPONSE+":"+
						response.getStatus());
			
			return FastAnnotationBuilder.fromFast(response.getEntity());
		} catch (AnnotationDatabaseException ade) {
			logger.error(FAILED_TO_READ_ANNOTATION, ade);
			throw ade;
		} catch (IOException ioe) {
			logger.error(FAILED_TO_PARSE_ANNOTATION, ioe);
			throw new AnnotationDatabaseException(ioe);
		} catch (JDOMException je) {
			logger.error(je.getMessage(), je);
			throw new AnnotationDatabaseException(je);		
		} catch (AnnotationNotFoundException anfe) {
			throw anfe;
		} catch(Throwable t) {
			logger.error(t.getMessage(), t);		
			throw new AnnotationDatabaseException(t);
		}		
	}
	
	@Override
	public String findAnnotationBodyById(String id) throws AnnotationDatabaseException, 	
			AnnotationNotFoundException, AnnotationException {
		try {
			Document document = new SAXBuilder().build(new StringReader(findAnnotationById(id)));			
			XPath xPath = XPath.newInstance(ANNOTATION_BODY_XPATH);
			Object htmlEl = xPath.selectSingleNode(document);
			
			if(htmlEl instanceof Element) {
				StringWriter swHtml = new StringWriter();
				new XMLOutputter().output(((Element)htmlEl), swHtml);				
				return swHtml.toString();
			}				
		} catch (IOException ioe) {
			logger.error(FAILED_TO_PARSE_ANNOTATION, ioe);
			throw new AnnotationDatabaseException(ioe);
		} catch (JDOMException je) {
			logger.error(FAILED_TO_PARSE_ANNOTATION, je);
			throw new AnnotationDatabaseException(je);		
		} 
		
		return "";
	}

	@Override
	public String listAnnotationReplies(String id) throws AnnotationDatabaseException, 
		AnnotationException {
		
		return findAnnotations(CQL_RELATEDTO_FIELD + "=\"" + 
				id.replace(Config.getInstance().getAnnotationBaseUrl(),"") + "\"");
	}

	@Override
	public String listLinkedAnnotations(String linkedObjectId) throws AnnotationDatabaseException, 
		AnnotationException {
		
		try {
			return findAnnotations(CQL_RELATEDTO_FIELD + "=\"" + 
					URLEncoder.encode(linkedObjectId, URL_ENCODING) +"\"");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
			throw new AnnotationException(e);
		}

	}
	
	@Override
	public int countLinkedAnnotations(String linkedObjectId) throws AnnotationDatabaseException, 
		AnnotationException {
	
		try {
			return executeSearch(CQL_RELATEDTO_FIELD + "=\"" + 
					URLEncoder.encode(linkedObjectId, URL_ENCODING) +"\"").size();
		} catch (JDOMException jde) {
			throw new AnnotationDatabaseException(jde);
		} catch (IOException ioe) {
			throw new AnnotationDatabaseException(ioe);
		}
	}		
	
	/**
	 * creates an unique id used to store annotations in FAST. We can't use java.util.UUID because
	 * the string representation has more than 32 characters and FAST only allows ids <= 32 bytes.
	 * 
	 * @return unique id as String
	 * @throws UnknownHostException
	 * @throws NoSuchAlgorithmException
	 */
	private String createUniqueId() throws UnknownHostException, NoSuchAlgorithmException {
		InetAddress addr=InetAddress.getLocalHost();        
		StringBuilder sb = new StringBuilder();
		
		sb.append(Integer.toHexString(addr.getHostName().hashCode()))
			.append(Long.toHexString(System.currentTimeMillis()))
			.append(Integer.toHexString(System.identityHashCode(this)))
			.append(Integer.toHexString(SecureRandom.getInstance("SHA1PRNG").nextInt(1024)));
	
		return sb.toString();
	}
	 
	private String createValidFastAnnotationId(String annotationId) {
		annotationId = annotationId.replace(Config.getInstance().getAnnotationBaseUrl(), "");
		
		//append NS
		if(!annotationId.endsWith(Config.getInstance().getAnnotationBaseUrl())) 
			annotationId += ";"+Config.getInstance().getAnnotationBaseUrl();
		return annotationId;
			
	}
	
	private String getUserNameFromCookie(HttpServletRequest request) 
		throws UnsupportedEncodingException {
		
		String userName = "unknown";
		if(request!=null&&request.getCookies()!=null) {
			for(Cookie c : request.getCookies()) {
				if(c.getName().equals(COOKIE_NAME_USER)) {
					userName = c.getValue();
				}
			}
		}
		return userName+";"+URLEncoder.encode(FastAnnotationBuilder.FAST_COMMON_NS, URL_ENCODING);
	}

}

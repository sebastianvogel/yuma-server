package at.ait.dme.yuma.server.db.fast;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

import at.ait.dme.yuma.server.config.Config;

/**
 * converts annotations from our annotation model (LEMO based on Annotea) to
 * the FAST annotation model and vice versa (see Report M5.5 Integration of the
 * Annotation Middleware and the FAST Annotation Service).
 * 
 * @author Christian Sadilek
 */
public class FastAnnotationBuilder {
	private static class LemoAnnotation {
		private String objectId, scope, author, format, isLinkedTo, threadRoot;
		private Element rootElement, rdfDescription;
		
		public LemoAnnotation(String annotation) throws JDOMException, IOException {
			Document annDocument = new SAXBuilder().build(new StringReader(annotation));
			rootElement = annDocument.getRootElement();
			rdfDescription = rootElement.getChild(RDF_DESCRIPTION, RDF_NS);
			if(rdfDescription==null) throw new JDOMException("rdf description element not found");		
			
			Element annotates = rdfDescription.getChild(LEMO_ANNOTATES, LEMO_ANNOTATION_NS);
			if(annotates==null) throw new JDOMException("annotates element not found");
			objectId=annotates.getAttributeValue(RDF_RESOURCE, RDF_NS);

			author = rdfDescription.getChildText(LEMO_AUTHOR, LEMO_ANNOTATION_NS);
			if(author==null) throw new JDOMException("author element not found");

			scope = rdfDescription.getChildText(LEMO_SCOPE, LEMO_SCOPE_NS);
			if(scope==null) throw new JDOMException("scope element not found");

			format = rdfDescription.getChildText(LEMO_FORMAT, LEMO_FORMAT_NS);
			if(format==null) throw new JDOMException("format element not found");

			isLinkedTo = rdfDescription.getChildText(LEMO_REL_ISLINKEDTO, LEMO_REL_NS);
			
			Element threadRootEl = rdfDescription.getChild(THREAD_ROOT, THREAD_NS);
			threadRoot = null;
			if(threadRootEl!=null) threadRoot=threadRootEl.getAttributeValue(RDF_RESOURCE, RDF_NS);
		}

		public String getObjectId() {
			return objectId;
		}
		public String getAuthor() {
			return author;
		}
		public String getScope() {
			return scope.toUpperCase();
		}
		public String getFormat() {
			return format;
		}
		public String getIsLinkedTo() {
			return isLinkedTo;
		}
		public String getThreadRoot() {
			return threadRoot;
		}
		public Element getRootElement() {
			return rootElement;
		}
		public Element getRdfDescription() {
			return rdfDescription;
		}
	}
	
	public static final org.jdom.Namespace FAST_NS = 
		org.jdom.Namespace.getNamespace("f","http://fast.dei.unipd.it/");	
	public static final String FAST_NS_ATTRIBUTE = "xmlns=\""+FAST_NS.getURI()+"\"";
	
	public static final org.jdom.Namespace ANNOTEA_ANNOTATION_NS = 
		org.jdom.Namespace.getNamespace("ann","http://www.w3.org/2000/10/annotation-ns#");	
	public static final String ANNOTEA_ANNOTATION_BODY = "body";	

	public static final org.jdom.Namespace LEMO_ANNOTATION_NS = 
		org.jdom.Namespace.getNamespace("a","http://lemo.mminf.univie.ac.at/annotation-core#");	
	public static final String LEMO_ANNOTATES = "annotates";	
	public static final String LEMO_AUTHOR = "author";		
	
	public static final org.jdom.Namespace LEMO_SCOPE_NS = 
		org.jdom.Namespace.getNamespace("scope","http://lemo.mminf.univie.ac.at/ann-tel#");
	public static final String LEMO_SCOPE = "scope";
	
	public static final org.jdom.Namespace LEMO_FORMAT_NS = 
		org.jdom.Namespace.getNamespace("dc","http://purl.org/dc/elements/1.1/");
	public static final String LEMO_FORMAT = "format";

	public static final org.jdom.Namespace LEMO_REL_NS = 
		org.jdom.Namespace.getNamespace("rel","http://lemo.mminf.univie.ac.at/ann-relationship#");
	public static final String LEMO_REL_ISLINKEDTO = "isLinkedTo";
	
	public static final org.jdom.Namespace  THREAD_NS = 
		org.jdom.Namespace.getNamespace("tr","http://www.w3.org/2001/03/thread#");
	public static final String THREAD_ROOT = "root";

	public static final org.jdom.Namespace RDF_NS = 
		org.jdom.Namespace.getNamespace("rdf","http://www.w3.org/1999/02/22-rdf-syntax-ns#");	
	public static final String RDF_DESCRIPTION = "Description";	
	public static final String RDF_RESOURCE = "resource";
	public static final String RDF_ABOUT = "about";
	
	public static final String FAST_ANNOTATION_NS = Config.getInstance().getAnnotationBaseUrl(); 
	public static final String FAST_COMMON_NS ="http://www.theeuropeanlibrary.org/telplus/";

	private static final String IDENTIFIER = "identifier";
	private static final String NAMESPACE = "namespace";
	private static final String MIMETYPE = "mime-type";
	private static final String FAST = "fast";
	private static final String ANNOTATE = "annotate";	
	private static final String ANNOTATION = "annotation";
	private static final String RELATE_TO = "relate-to";	
	private static final String ANNOTATION_SCOPE = "scope";	
	private static final String ANCHOR = "anchor";
	private static final String DIGITAL_OBJECT = "digital-object";	
	private static final String USER = "user";
	private static final String FAST_CONTENT_XPATH = "/annotation/signs/sign/content[rdf:RDF]";
	private static final String FAST_CONTENT_XPATH_FULL = "/fast/annotation/signs/sign/content[rdf:RDF]";
	private static final String CONTENT = "content";	
	private static final String SIGN = "sign";
	private static final String SIGNS = "signs";
	private static final String SIGN_MIMETYPE_VALUE = "application/rdf+xml";	
	private static final String MEANINGS = "meanings";
	private static final String MEANING = "meaning";
	private static final String MEANING_ID ="annotea";		
						
	public static String toFast(String id, String annotation) throws IOException, JDOMException {		
		LemoAnnotation lemoAnn = new LemoAnnotation(annotation);		
		id=id.replace(";"+Config.getInstance().getAnnotationBaseUrl(), "");
		addIds(lemoAnn.getRdfDescription(), id);		

		// construct FAST annotation model		
		Element ann = new Element(ANNOTATION);
		if(id!=null) ann.setAttribute(new Attribute(IDENTIFIER,id));
		ann.setAttribute(new Attribute(NAMESPACE,FAST_ANNOTATION_NS));
		ann.setAttribute(new Attribute(ANNOTATION_SCOPE, lemoAnn.getScope()));		
		Element user = new Element(USER);
		user.setAttribute(new Attribute(IDENTIFIER,lemoAnn.getAuthor()));
		user.setAttribute(new Attribute(NAMESPACE,FAST_COMMON_NS));
		ann.addContent(user);
		
		Element signs = new Element(SIGNS);
		
		Element content = (Element)lemoAnn.getRootElement().clone();
		Element annotateAnchor = createDigitalObjectAnchor(lemoAnn.getObjectId(), 
				FAST_COMMON_NS, lemoAnn.getFormat());
		Element relateToAnchor = createDigitalObjectAnchor(lemoAnn.getIsLinkedTo(), 
				FAST_COMMON_NS, lemoAnn.getFormat());
		signs.addContent(createSign(content, annotateAnchor, relateToAnchor));		
		
		if(lemoAnn.getThreadRoot() != null) {
			Element relateToReplyAnchor = createDigitalObjectAnchor(
					lemoAnn.getThreadRoot().replace(FAST_ANNOTATION_NS, ""), 
					FAST_ANNOTATION_NS, 
					null);		
			signs.addContent(createSign(null,(Element)annotateAnchor.clone(),relateToReplyAnchor));						
		}		
		ann.addContent(signs);
						
		StringWriter sw = new StringWriter();
		new XMLOutputter().output(ann, sw);
		return sw.toString();
	}
	
	private static Element createSign(Element content, Element annotateAnchor, 
			Element relateToAnchor) {
		Element sign = new Element(SIGN);
		sign.setAttribute(new Attribute(MIMETYPE, SIGN_MIMETYPE_VALUE));
		
		Element contentElement = new Element(CONTENT);
		if(content!=null) contentElement.addContent(content);
		sign.addContent(contentElement);
		
		Element meanings = new Element(MEANINGS);
		Element meaning = new Element(MEANING);
		meaning.setAttribute(new Attribute(IDENTIFIER,MEANING_ID));
		meaning.setAttribute(new Attribute(NAMESPACE,FAST_COMMON_NS));
		meanings.addContent(meaning);
		sign.addContent(meanings);
		
		if(annotateAnchor!=null) {
			Element annotate = new Element(ANNOTATE);
			annotate.addContent(annotateAnchor);		
			sign.addContent(annotate);
		}
		
		if(relateToAnchor!=null) {
			Element relateTo = new Element(RELATE_TO);
			relateTo.addContent(relateToAnchor);		
			sign.addContent(relateTo);
		}
		
		return sign;
	}
	
	private static Element createDigitalObjectAnchor(String id, String ns, String format) {
		if(id==null) return null;
		
		Element anchor = new Element(ANCHOR);
		Element digitalObject = new Element(DIGITAL_OBJECT);
		if(id!=null) digitalObject.setAttribute(new Attribute(IDENTIFIER,id));
		if(ns!=null) digitalObject.setAttribute(new Attribute(NAMESPACE,ns));
		if(format!=null) digitalObject.setAttribute(new Attribute(MIMETYPE, format));		
		anchor.addContent(digitalObject);
		return anchor;
		
	}
	
	private static void addIds(Element rdfDescription, String annotationId) {
		Attribute rdfAbout=rdfDescription.getAttribute(RDF_ABOUT, RDF_NS);
		if(rdfAbout==null) {
			rdfDescription.setAttribute(RDF_ABOUT,
					Config.getInstance().getAnnotationBaseUrl()+annotationId,RDF_NS);		
		}
				
		Element annoteaBody=rdfDescription.getChild(
				ANNOTEA_ANNOTATION_BODY, 
				ANNOTEA_ANNOTATION_NS);
		
		if(annoteaBody!=null) {
			Element rdfAnnBodyDesription=annoteaBody.getChild(RDF_DESCRIPTION,RDF_NS);
			if(rdfAnnBodyDesription!=null) {
				Attribute rdfAnnBodyAbout=rdfAnnBodyDesription.getAttribute(RDF_ABOUT, RDF_NS);				
				if(rdfAnnBodyAbout==null) {
					rdfAnnBodyDesription.setAttribute(RDF_ABOUT,
							Config.getInstance().getAnnotationBodyBaseUrl()+annotationId,RDF_NS);
				}								
			}
		}				
	}
	
	public static String fromFast(String annotation) throws JDOMException, IOException {
		// remove the FAST default NS: otherwise all elements stored w/o a NS are returned in the
		// FAST NS.
		annotation=annotation.replaceAll(FAST_NS_ATTRIBUTE, "");		
		Document document = new SAXBuilder().build(new StringReader(annotation));
		
		XPath xpath = null;
		if(document.getRootElement()!=null&&document.getRootElement().getName().equals(FAST)) {
			xpath = XPath.newInstance(FAST_CONTENT_XPATH_FULL);
			xpath.addNamespace(RDF_NS);
		} else {
			xpath = XPath.newInstance(FAST_CONTENT_XPATH);
			xpath.addNamespace(RDF_NS);
		}
		Object content = xpath.selectSingleNode(document);
		
		if(content instanceof Element) {
			StringWriter swContent = new StringWriter();
			new XMLOutputter().output(((Element)content).getChildren(), swContent);
			
			StringWriter swDocument = new StringWriter();
			Document newDocument = new SAXBuilder().build(new StringReader(swContent.toString()));
			new XMLOutputter().output(newDocument, swDocument);
			return swDocument.toString();
		} else {
			throw new JDOMException("content element not found");
		}
	}
}

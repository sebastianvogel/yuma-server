package at.ait.dme.yuma.server.db.fast.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.junit.BeforeClass;
import org.junit.Test;

import at.ait.dme.yuma.server.Data;
import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.db.fast.FastAnnotationBuilder;

/**
* Tests for the FAST annotation builder
*  
* @author Christian Sadilek
*/
public class FastAnnotationBuilderTest {	
	
	@BeforeClass
	public static void setUp() {		
		new Config.Builder("at.researchstudio.dme.annotation.db.fast.FastAnnotationDatabase",
				"http://localhost:8888/annotation-middleware/annotations/annotation/",
				"http://localhost:8888/annotation-middleware/annotations/annotation/body").
				createInstance();
	}
	
	@Test
	public void testToFromFastWithId() throws FileNotFoundException, JDOMException, IOException {	
		Document document = new SAXBuilder().build(new StringReader(Data.LEMO_ANNOTATION_WITH_ID));		
		StringWriter sw = new StringWriter();
		XMLOutputter xmlOutputter = new XMLOutputter();
		xmlOutputter.output(document, sw);
		String lemoAnnotation=sw.toString();
		
		String fastAnnotation=FastAnnotationBuilder.toFast("13mqhvi2dx1",lemoAnnotation);
		String newLemoAnnotation=FastAnnotationBuilder.fromFast(fastAnnotation);
		
		assertEquals(lemoAnnotation,newLemoAnnotation);		
	}
	
	@Test
	public void testToFromFastWithoutId() throws FileNotFoundException, JDOMException, IOException {	
		String id = "123avc";
		
		Document document = new SAXBuilder().build(new StringReader(Data.LEMO_ANNOTATION_WITHOUT_ID));
		StringWriter sw = new StringWriter();
		XMLOutputter xmlOutputter = new XMLOutputter();
		xmlOutputter.output(document, sw);
		String lemoAnnotation=sw.toString();
		
		String fastAnnotation=FastAnnotationBuilder.toFast(id,lemoAnnotation);
		String newLemoAnnotation=FastAnnotationBuilder.fromFast(fastAnnotation);
		
		document = new SAXBuilder().build(new StringReader(newLemoAnnotation));
		
		// check if ids have been set
		Element rdfDescription = document.getRootElement().getChild(
				FastAnnotationBuilder.RDF_DESCRIPTION, 
				FastAnnotationBuilder.RDF_NS);		
		assertNotNull(rdfDescription);
		Attribute rdfAbout=rdfDescription.getAttribute(
				FastAnnotationBuilder.RDF_ABOUT, 
				FastAnnotationBuilder.RDF_NS);
		assertNotNull(rdfAbout);		
		assertEquals(rdfAbout.getValue(),Config.getInstance().getAnnotationBaseUrl()+id);
		
		Element annoteaBody = rdfDescription.getChild(
				FastAnnotationBuilder.ANNOTEA_ANNOTATION_BODY, 
				FastAnnotationBuilder.ANNOTEA_ANNOTATION_NS);
		assertNotNull(annoteaBody);

		Element rdfAnnBodyDesription=annoteaBody.getChild(
				FastAnnotationBuilder.RDF_DESCRIPTION,
				FastAnnotationBuilder.RDF_NS);
		assertNotNull(rdfAnnBodyDesription);

		Attribute rdfAnnBdoyAbout=rdfAnnBodyDesription.getAttribute(
				FastAnnotationBuilder.RDF_ABOUT, 
				FastAnnotationBuilder.RDF_NS);
		assertNotNull(rdfAnnBdoyAbout);		
		assertEquals(rdfAnnBdoyAbout.getValue(),
				Config.getInstance().getAnnotationBodyBaseUrl()+id);					
	}
}

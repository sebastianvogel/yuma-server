package at.ait.dme.yuma.server.controller.rdf.oac;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import at.ait.dme.yuma.server.exception.InvalidAnnotationException;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.MapKeys;
import at.ait.dme.yuma.server.model.User;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.DCTerms;

/**
 * Used to convert a serialized annotation in OAC RDF format to an annotation 
 * object compliant to the server's data model. 
 * 
 * @author Christian Mader
 */
class OACParser {

	private HashMap<String, Object> properties = new HashMap<String, Object>();
	private Model model;
	
	OACParser(String serialized) {
		model = ModelFactory.createDefaultModel();
		model.read(new StringReader(serialized), null);		
	}
	
	Annotation parse() throws InvalidAnnotationException, ParseException {
		parseMainAnnotationInfo();
		parseBodyInfo();
		parseTargetInfo();
		
		return new Annotation(properties);
	}
	
	private void parseMainAnnotationInfo() throws ParseException {
		ResIterator it = model.listResourcesWithProperty(DC.title);
		
		while (it.hasNext()) {
			Resource a = it.next();
			
			properties.put(MapKeys.ANNOTATION_TITLE,
				a.getProperty(DC.title).getString());
			
			properties.put(MapKeys.ANNOTATION_CREATED_BY,
				new User(a.getProperty(DC.creator).getString()));
			
			properties.put(MapKeys.ANNOTATION_CREATED,
				createDateFromString(a.getProperty(DCTerms.created).getString()));
			
			properties.put(MapKeys.ANNOTATION_LAST_MODIFIED,
				createDateFromString(a.getProperty(DCTerms.modified).getString()));
		}
	}
	
	private Date createDateFromString(String date) throws ParseException {
		return new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(date);
	}
	
	private void parseBodyInfo() {
		
	}
	
	private void parseTargetInfo() {
		
	}
}

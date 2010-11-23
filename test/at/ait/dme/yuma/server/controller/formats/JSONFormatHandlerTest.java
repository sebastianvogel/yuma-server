package at.ait.dme.yuma.server.controller.formats;

import org.junit.Test;

import at.ait.dme.yuma.server.Data;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.AnnotationType;
import at.ait.dme.yuma.server.model.Scope;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class JSONFormatHandlerTest {
	
	@Test
	public void testJSONParsing() throws Exception {
		FormatHandler jsonFormat = new JSONFormatHandler();
		Annotation a = jsonFormat.parse(Data.ANNOTATION_JSON_UPDATE);
		
		assertEquals("", a.getParentId());
		assertEquals("", a.getRootId());
		assertEquals("Ponte 25 de Abril", a.getTitle());
		assertEquals("The 25 de Abril Bridge is a suspension bridge connecting the city of " +
				"Lisbon, capital of Portugal, to the municipality of Almada on the left " +
				"bank of the Tagus river. It was inaugurated on August 6, 1966 and a train " +
				"platform was added in 1999. It is often compared to the Golden Gate Bridge " +
				"in San Francisco, USA, due to their similarities and same construction " +
				"company. With a total length of 2.277 m, it is the 19th largest " +
				"suspension bridge in the world. The upper platform carries six car lanes, " +
				"the lower platform two train tracks. Until 1974 the bridge was named Salazar " +
				"Bridge.", a.getText());
		assertEquals(Scope.PUBLIC, a.getScope());
		// assertEquals(Scope.PUBLIC, a.getCreated());
		// assertEquals(Scope.PUBLIC, a.getLastModified());		
		assertEquals("rsimon", a.getCreatedBy());
		// assertEquals("fragment", a.getFragment());
		assertEquals(AnnotationType.IMAGE, a.getType());
		assertEquals("http://upload.wikimedia.org/wikipedia/commons/7/77/Lissabon.jpg", a.getObjectID());
		
		// TODO match tags
	}

	@Test
	public void testJSONSerialization() throws Exception {
		FormatHandler jsonFormat = new JSONFormatHandler();
		Annotation a = jsonFormat.parse(Data.ANNOTATION_JSON_UPDATE);
		
		String serialized = jsonFormat.serialize(a);
		assertTrue(serialized.contains("\"parent-id\" : \"\""));
		assertTrue(serialized.contains("\"root-id\" : \"\""));
		assertTrue(serialized.contains("\"title\" : \"Ponte 25 de Abril\""));
		assertTrue(serialized.contains("\"text\" : \"The 25 de Abril Bridge is a " +
				"suspension bridge connecting the city of Lisbon, capital of Portugal, " + 
				"to the municipality of Almada on the left bank of the Tagus river. It " +
				"was inaugurated on August 6, 1966 and a train platform was added in 1999. " +
				"It is often compared to the Golden Gate Bridge in San Francisco, USA, due to " +
				"their similarities and same construction company. With a total length of " +
				"2.277 m, it is the 19th largest suspension bridge in the world. The upper " +
				"platform carries six car lanes, the lower platform two train tracks. Until " +
				 "1974 the bridge was named Salazar Bridge.\""));
		assertTrue(serialized.contains("\"scope\" : \"public\""));
		assertTrue(serialized.contains("\"last-modified\" : { \"$date\" : \"2010-11-11T11:58:23Z\"}"));
		assertTrue(serialized.contains("\"created\" : { \"$date\" : \"2010-11-11T10:58:23Z\"}"));
		assertTrue(serialized.contains("\"created-by\" : \"rsimon\""));
		assertTrue(serialized.contains("\"fragment\" : \"" + 
				"<svg:svg xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" width=\\\"640px\\\" height=\\\"480px\\\" viewbox=\\\"0px 0px 640px 480px\\\"> " +
				  "<svg:defs xmlns:svg=\\\"http://www.w3.org/2000/svg\\\"> " +
					"<svg:symbol xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" id=\\\"Polygon\\\"> " +
					"<svg:polygon xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" " +
					"points=\\\"0,24 45,22 45,32 49,32 49,23 190,20 285,19 193,0 119,17 48,5\\\" stroke=\\\"rgb(229,0,0)\\\" " +
					"stroke-width=\\\"2\\\" fill=\\\"none\\\"> " +
					"</svg:polygon> " +
					"</svg:symbol> " +
				  "</svg:defs>" +
				"</svg:svg>" +
			  "\""));
		assertTrue(serialized.contains("\"type\" : \"image\""));
		assertTrue(serialized.contains("\"object-id\" : \"http://upload.wikimedia.org/wikipedia/commons/7/77/Lissabon.jpg\""));

		// TODO match tags
	}
	
}

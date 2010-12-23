package at.ait.dme.yuma.server.controller.lemo;

import org.junit.BeforeClass;
import org.junit.Test;

import at.ait.dme.yuma.server.bootstrap.Data;
import at.ait.dme.yuma.server.bootstrap.Setup;
import at.ait.dme.yuma.server.controller.FormatHandler;
import at.ait.dme.yuma.server.controller.json.JSONFormatHandler;
import at.ait.dme.yuma.server.model.Annotation;

public class LEMOXMLFormatHandlerTest {
	
	@BeforeClass
	public static void setUp() throws Exception {
		Setup.buildHibernateConfiguration();
	}
	
	@Test
	public void testLEMOSerialization() throws Exception {
		Annotation a = new JSONFormatHandler().parse(Data.ANNOTATION_JSON_UPDATE);
		
		FormatHandler lemoFormat = new LEMOXMLFormatHandler();
		System.out.println(lemoFormat.serialize(a));
	}

}

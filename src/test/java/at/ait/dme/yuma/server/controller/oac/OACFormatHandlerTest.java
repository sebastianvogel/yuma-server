package at.ait.dme.yuma.server.controller.oac;

import org.junit.BeforeClass;
import org.junit.Test;

import at.ait.dme.yuma.server.bootstrap.Data;
import at.ait.dme.yuma.server.bootstrap.Setup;
import at.ait.dme.yuma.server.controller.json.JSONFormatHandler;
import at.ait.dme.yuma.server.model.Annotation;

public class OACFormatHandlerTest {

	@BeforeClass
	public static void setUp() throws Exception {
		Setup.buildHibernateConfiguration();
	}

	@Test
	public void testOASerialization() throws Exception {
		Annotation annotation = new JSONFormatHandler().parse(Data.ANNOTATION_JSON_UPDATE);
		new OACFormatHandler().serialize(annotation);
	}
	
}

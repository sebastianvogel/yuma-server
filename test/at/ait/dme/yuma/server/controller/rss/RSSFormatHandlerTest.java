package at.ait.dme.yuma.server.controller.rss;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import at.ait.dme.yuma.server.bootstrap.Data;
import at.ait.dme.yuma.server.controller.FormatHandler;
import at.ait.dme.yuma.server.controller.json.JSONFormatHandler;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.AnnotationTree;

public class RSSFormatHandlerTest {

	private static AnnotationTree annotationTree;
	
	@BeforeClass
	public static void setUp() throws Exception {	
		FormatHandler jsonFormat = new JSONFormatHandler();
		List<Annotation> annotations = new ArrayList<Annotation>();
		annotations.add(jsonFormat.parse(Data.ANNOTATION_JSON_UPDATE));
		annotationTree = new AnnotationTree(annotations);
	}
	
	@Test
	public void testRSSSerialization() {
		FormatHandler rssFormat = new RSSFormatHandler();
		String rss = rssFormat.serialize(annotationTree);
		System.out.println(rss);
	}
	
}

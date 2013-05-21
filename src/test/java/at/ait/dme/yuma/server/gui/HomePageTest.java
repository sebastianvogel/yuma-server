package at.ait.dme.yuma.server.gui;

import junit.framework.TestCase;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import at.ait.dme.yuma.server.gui.search.Search;

/**
 * Simple test using the WicketTester
 */
public class HomePageTest extends TestCase {
	
	private WicketTester tester;

	@Before
	public void setUp()	{
		tester = new WicketTester(new WicketApplication());
	}

	@Test
	public void testRenderMyPage()	{
		//start and render the test page
		tester.startPage(Search.class);

		//assert rendered page class
		tester.assertRenderedPage(Search.class);
		
		//assert rendered label component
		//tester.assertLabel("message", "If you see this message wicket is properly configured and running");
		tester.assertVisible("link-timeline");
	}
	
}

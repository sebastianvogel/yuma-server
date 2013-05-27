package at.ait.dme.yuma.server.util;

import org.junit.Test;

import junit.framework.Assert;

public class URIBuilderTest {
	
	@Test
	public void testURIType() {
		Assert.assertTrue(URIBuilder.isPublic("https://google.at"));
	}

}

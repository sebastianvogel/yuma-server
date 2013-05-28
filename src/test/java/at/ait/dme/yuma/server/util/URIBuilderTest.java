package at.ait.dme.yuma.server.util;

import java.net.URI;
import java.net.URISyntaxException;

import junit.framework.Assert;

import org.junit.Test;

import at.ait.dme.yuma.server.bootstrap.BaseTest;
import at.ait.dme.yuma.server.model.URISource;

public class URIBuilderTest extends BaseTest {
	
	@Test
	public void testURIType() throws URISyntaxException {
		String uri = "https://google.at";
		Assert.assertTrue(URIBuilder.isPublic(uri));
		
		Assert.assertTrue(URIBuilder.isPublic(new URI("https://www.google.de")));
		
		Assert.assertFalse(URIBuilder.isLocal(uri));
		Assert.assertFalse(URIBuilder.isRelative(uri));
		
		URI userURI = URIBuilder.toURI("test", URISource.USER, true);
		Assert.assertNotNull(userURI);
		uri = userURI.toString();
		Assert.assertNotNull(uri);
		Assert.assertEquals("/api/user/test", uri);
		Assert.assertFalse(URIBuilder.isLocal(uri));
		Assert.assertTrue(URIBuilder.isRelative(uri));
		
		userURI = URIBuilder.toURI("test", URISource.USER, false);
		Assert.assertNotNull(userURI);
		uri = userURI.toString();
		Assert.assertNotNull(uri);
		Assert.assertTrue(URIBuilder.isLocal(uri));
		Assert.assertFalse(URIBuilder.isRelative(uri));
	}
	
	@Test
	public void testLocalURI() {
		URI userURI = URIBuilder.toURI("test", URISource.USER, false);
		String left = URIBuilder.toURI("test", URISource.USER, true).toString();
		String right = URIBuilder.toRelativeLocalURI(userURI.toString());
		Assert.assertEquals(left, right);
	}

}

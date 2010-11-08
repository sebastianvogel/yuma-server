package at.ait.dme.yuma.server.db.fast.test;

import static org.junit.Assert.assertFalse;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.db.fast.FastSetup;

/**
* Tests to setup FAST
*  
* @author Christian Sadilek
*/
@Ignore
public class FastSetupTest {
	
	@BeforeClass
	public static void setUp() {	
		new Config.Builder("at.researchstudio.dme.annotation.db.fast.FastAnnotationDatabase",
				"http://dme.arcs.ac.at/annotation-middleware/Annotation/",
				"http://dme.arcs.ac.at/annotation-middleware/Annotation/body").
				dbHost("http://svrims.dei.unipd.it").dbPort("8080").
				dbUser("fast").dbPass("dummypwd").
				createInstance();
	}
	
	@Test
	public void testListNamespaces() throws Exception {
		FastSetup setup = new FastSetup();
		setup.listNamespaces();
	}
	
	@Test
	public void testUUID() throws UnknownHostException, NoSuchAlgorithmException {
		String tmpuuid = "";
		for(int i=0; i<1000; i++) {
			String uuid = "";			
			InetAddress addr=InetAddress.getLocalHost();        
			uuid+=Integer.toHexString(addr.getHostName().hashCode())
				+Long.toHexString(System.currentTimeMillis())
				+Integer.toHexString(System.identityHashCode(this))
				+Integer.toHexString(SecureRandom.getInstance("SHA1PRNG").nextInt(1024));
			
			assertFalse(uuid.equals(tmpuuid));
		}
	}
}
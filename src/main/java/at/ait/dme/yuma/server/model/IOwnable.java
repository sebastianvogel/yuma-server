package at.ait.dme.yuma.server.model;

import java.net.URI;

public interface IOwnable {
	
	Scope getScope();
	User getCreatedBy();
	URI getURI(boolean relative);
}

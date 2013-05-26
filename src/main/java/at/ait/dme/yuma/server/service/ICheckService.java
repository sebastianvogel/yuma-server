package at.ait.dme.yuma.server.service;

import at.ait.dme.yuma.server.model.IOwnable;

public interface ICheckService {
	boolean hasReadPermission(String username, IOwnable ownable);

}

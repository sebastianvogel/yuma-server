package at.ait.dme.yuma.server.service;

import at.ait.dme.yuma.server.controller.AuthContext;
import at.ait.dme.yuma.server.model.IOwnable;

public interface ICheckService {
	boolean hasReadPermission(AuthContext auth, IOwnable ownable);
	boolean hasWritePermission(AuthContext auth, IOwnable ownable);
}

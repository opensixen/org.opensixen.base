package org.opensixen.osgi.interfaces;

import org.compiere.interfaces.Server;

public interface IApplicationServer extends Server, IService{

	public static final String path = "ApplicationServerWS";
	
	public boolean testConnection();
	
}

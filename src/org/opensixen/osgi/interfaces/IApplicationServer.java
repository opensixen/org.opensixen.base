package org.opensixen.osgi.interfaces;

import org.compiere.interfaces.Server;
import org.compiere.interfaces.Status;

public interface IApplicationServer extends Server, IService, Status{

	public static final String path = "ApplicationServerWS";
	
	public boolean testConnection();
		
}

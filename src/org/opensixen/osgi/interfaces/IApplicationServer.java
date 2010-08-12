package org.opensixen.osgi.interfaces;

import org.compiere.interfaces.Server;

public interface IApplicationServer extends Server, IService{

	public boolean testConnection();
	
}

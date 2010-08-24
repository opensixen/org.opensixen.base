/**
 * 
 */
package org.opensixen.osgi.interfaces;

import java.io.File;
import java.util.Properties;

/**
 * @author harlock
 *
 */
public interface IClientSetupValidator extends IService {

	
	/**
	 * Run OSGi validators
	 * 
	 * @param AD_Client_ID
	 * @return
	 */
	public boolean doIt(Properties ctx, int AD_Client_ID,int AD_Org_ID, String clientName, File accountsFile, String trxName);
	
}

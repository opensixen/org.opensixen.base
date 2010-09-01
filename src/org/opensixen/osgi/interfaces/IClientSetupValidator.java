/**
 * 
 */
package org.opensixen.osgi.interfaces;

import java.io.File;
import java.util.Properties;

import org.compiere.model.MAcctSchema;

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
	public boolean doIt(Properties ctx, int AD_Client_ID,int AD_Org_ID, MAcctSchema m_as, String clientName, File accountsFile, String trxName);
	
}

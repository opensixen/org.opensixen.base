/**
 * 
 */
package org.opensixen.osgi.interfaces;

/**
 * @author harlock
 *
 */
public interface ICommand extends IService {

	public static final String P_NAME="name";
	
	public void prepare();
	
	public String doIt() throws Exception;
	
}

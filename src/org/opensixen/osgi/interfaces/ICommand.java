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
	
	public int run();
	
}

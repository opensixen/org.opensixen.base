/**
 * Clase creada por Joer Viola
 */
package org.opensixen.osgi.interfaces;

import java.util.Properties;

import org.compiere.model.GridField;
import org.compiere.model.GridTab;

/**
 *  Column Callout Interface
 */
public interface IColumnCallout extends IService
{
	/**
	 *	Start Callout.
	 *  <p>
	 *	Callout's are used for cross field validation and setting values in other fields
	 *	when returning a non empty (error message) string, an exception is raised
	 *  <p>
	 *	When invoked, the Tab model has the new value!
	 *
	 *  @param ctx      Context
	 *  @param WindowNo current Window No
	 *  @param mTab     Model Tab
	 *  @param mField   Model Field
	 *  @param value    The new value
	 *  @param oldValue The old value
	 *  @return Error message or ""
	 */
	public String start (Properties ctx, int WindowNo,
		GridTab mTab, GridField mField, Object value, Object oldValue);

}

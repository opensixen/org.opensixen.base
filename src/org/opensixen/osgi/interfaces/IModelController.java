package org.opensixen.osgi.interfaces;

import org.compiere.model.PO;
import org.opensixen.osgi.OsxPO;

public interface IModelController extends IService {

	public static final String P_TABLENAME="tableName"; 
	
	/**
	 * Establece el PO que llama a este objeto
	 * Es llamado desde el contructor de PO, 
	 * llamando indirectamente a OsxPO
	 * @param po
	 */
	public void setPO(OsxPO po);
	
	/**
	 * 	Executed after Delete operation.
	 * 	@param success true if record deleted
	 *	@return true if delete is a success
	 */
	public boolean afterDelete (boolean success);
	
	/**
	 * 	Called after Save for Post-Save Operation
	 * 	@param newRecord new record
	 *	@param success true if save operation was success
	 *	@return if save was a success
	 */
	public boolean afterSave (boolean newRecord, boolean success);
	
	/**
	 * 	Executed before Delete operation.
	 *	@return true if record can be deleted
	 */
	public boolean beforeDelete();
	
	/**
	 * 	Called before Save for Pre-Save Operation
	 * 	@param newRecord new record
	 *	@return true if record can be saved
	 */
	public boolean beforeSave(boolean newRecord);
	
	/**
	 *  Load is complete
	 * 	@param success success
	 *  To be extended by sub-classes
	 */
	public void loadComplete (boolean success);
	
}

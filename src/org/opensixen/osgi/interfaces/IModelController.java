package org.opensixen.osgi.interfaces;

public interface IModelController extends IService {

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
	
}

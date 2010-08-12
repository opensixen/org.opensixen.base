package org.opensixen.osgi;

import java.util.List;

import org.opensixen.osgi.interfaces.IModelController;
/**
 * Clase que sera extendida por PO
 * 
 * Incorpora los metodos necesarios para ser controlado
 * por el sistema OSGi mediante la extension IModelController
 * @author Eloy Gomez 
 *
 */
public abstract class OsxPO {

	private List<IModelController> modelControllers;
	
	protected void osx_loadControllers()	{
		// Load OSGi ModelControllers		
		List<IModelController> controllers = Service.list(IModelController.class, new ServiceQuery(IModelController.P_TABLENAME, get_TableName()));
		for (IModelController c: controllers)	{
			c.setPO(this);
		}
		modelControllers = controllers;
		
	}
	
	public abstract String get_TableName();
	protected abstract boolean afterDelete(boolean success);
	protected abstract boolean afterSave(boolean newRecord, boolean success);
	protected abstract boolean beforeDelete();
	protected abstract boolean beforeSave(boolean newRecord);
	protected abstract void loadComplete(boolean success);
	
	
	
	protected boolean osx_afterDelete(boolean success) {
		// OSGi calls
		for (IModelController modelController:modelControllers)	{
			if (!modelController.afterDelete(success))	{
				return false;
			}
		}
		return afterDelete(success);
	}

	protected boolean osx_afterSave(boolean newRecord, boolean success) {
		// OSGi calls
		for (IModelController modelController:modelControllers)	{
			if (!modelController.afterSave(newRecord, success))	{
				return false;
			}
		}
		return afterSave(newRecord, success);
	}

	protected boolean osx_beforeDelete() {
		// OSGi calls
		for (IModelController modelController:modelControllers)	{
			if (!modelController.beforeDelete())	{
				return false;
			}
		}

		return beforeDelete();
	}

	protected boolean osx_beforeSave(boolean newRecord) {
		// OSGi calls
		for (IModelController modelController:modelControllers)	{
			if (!modelController.beforeSave(newRecord))	{
				return false;
			}
		}
		return beforeSave(newRecord);
	}
	
	/**
	 *  Load is complete
	 * 	@param success success
	 */
	protected void osx_loadComplete (boolean success)
	{
		if (modelControllers == null)	{
			return;
		}
		// OSGi calls
		for (IModelController modelController:modelControllers)	{
			modelController.loadComplete(success);
		}
		loadComplete(success);
	}   //  loadComplete
	

}

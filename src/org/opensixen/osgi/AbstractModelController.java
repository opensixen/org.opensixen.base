package org.opensixen.osgi;

import org.opensixen.osgi.interfaces.IModelController;

public class AbstractModelController implements IModelController {

	@Override
	public boolean afterDelete(boolean success) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean afterSave(boolean newRecord, boolean success) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean beforeDelete() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean beforeSave(boolean newRecord) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setPO(OsxPO po) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadComplete(boolean success) {
		// TODO Auto-generated method stub
		
	}

}

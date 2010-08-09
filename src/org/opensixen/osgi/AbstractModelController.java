package org.opensixen.osgi;

import org.opensixen.osgi.interfaces.IModelController;

public abstract class AbstractModelController implements IModelController {

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

}

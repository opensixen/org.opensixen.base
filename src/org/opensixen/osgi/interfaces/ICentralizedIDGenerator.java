package org.opensixen.osgi.interfaces;


public interface ICentralizedIDGenerator extends IService {
	public int getNextID(String tableName, String description);
}

package org.opensixen.osgi.interfaces;

import org.compiere.model.GridTabVO;

public interface IStatusBarTrxInfo extends IService {
	public static final String P_TABLENAME="tableName"; 
	
	/**
	 * Return trxInfo for this table
	 * @return
	 */
	public String getTrxInfo(GridTabVO m_vo);
}

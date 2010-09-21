/**
 * 
 */
package org.opensixen.model;

import java.lang.reflect.Constructor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.DBException;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.DB;

/**
 * Extends Query adding support for 
 * 
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 * @param <T>
 * @param <T>
 * @param <T>
 *
 */
public class POQuery extends Query{

	private String trxName;
	
	private Properties ctx;
	
	
	
	public POQuery(Properties ctx, MTable table, String whereClause,
			String trxName) {
		super(ctx, table, whereClause, trxName);
		this.ctx = ctx;
		this.trxName = trxName;
	}

	public POQuery(Properties ctx, String tableName, String whereClause, String trxName) {
		super(ctx, tableName, whereClause, trxName);
		this.ctx = ctx;
		this.trxName = trxName;
	}
	
	/**
	 * Return a list of records
	 */
	@SuppressWarnings("unchecked")
	public  <T extends PO > List<T> list(Class<T> clazz)	{
		List<T> list = new ArrayList<T>();
		String sql = buildSQL(null, true);
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			rs = createResultSet(pstmt);
			while (rs.next ())
			{
	
				try {
		            Constructor<T>	constructor	= clazz.getDeclaredConstructor(new Class[] { Properties.class, ResultSet.class, String.class });
		            T	record	= constructor.newInstance(new Object[] { ctx, rs, trxName });
		            list.add(record);
				}
				catch (Exception e)	{
					log.log(Level.SEVERE, "No se puede instanciar el objeto", e);
					return null;
				}
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
			throw new DBException(e, sql);
		} finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		return list;
	}
	
}

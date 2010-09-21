package org.opensixen.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.exceptions.DBException;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.POInfo;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.CPreparedStatement;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.opensixen.util.NameObjectPair;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

public class POFactory {

		
	private static CLogger s_log = CLogger.getCLogger(POFactory.class);
	
	private static final Object NULL = new Object();
	
	/**
	 * Return a list of objects
	 * 
	 * @param ctx
	 * @param clazz
	 * @param params
	 * @param order
	 * @param trxName
	 * @return
	 * @throws POException
	 */
	public static <T extends PO> List<T> getList_EX(Properties ctx, Class<T> clazz, QParam[] params, String[] order, String trxName)	throws POException{
		
		// Create main record
		
		T	po = null;		
		Constructor<T> po_constr;
		try {
			po_constr = clazz.getDeclaredConstructor(new Class[] { Properties.class, int.class, String.class });
			po	=  po_constr.newInstance(new Object[] { ctx, 0, trxName });
		} catch (Exception e) {
			throw new POException("No se puede instanciar el objeto", e);
		}
		

		POInfo p_info = po.get_POInfo();
		
		// Create SQL and params
		StringBuffer sql = new StringBuffer();
		ArrayList<Object> paramValues = new ArrayList<Object>();
		
		for (int i=0; i < params.length; i++)	{
			// Comprobamos si el parametro es de tipo cadena
			if (params[i].isCustom())	{
				sql.append(" ").append(params[i].getParam_str((i==0))).append(" ");
				continue;
			}
			
			// Comprobamos que el campo exista en la tabla
			int index = p_info.getColumnIndex(params[i].getName());
			if (index == -1)	{
				throw new POException("The column is not present in PO: " + params[i].getName());
			}
			// Comprobamos si se trata de filtrar por una columna virtual
			if (p_info.isVirtualColumn(index))
			{
				throw new POException ("Can not use a virtual column as a param: " + params[i].getName());
			}
			
			// Si es el primero, no lleva condicion
			if (i == 0 )	{
				sql.append(params[i].getParam_str(true));	
			}
			else {
				sql.append(" ").append(params[i].getParam_str());
			}
			
			// Add value
			paramValues.add(params[i].getValue());
			
		}
		
		// Add order
		StringBuffer orderStr = new StringBuffer();
		if (order != null)	{
			for (int i=0; i < order.length; i++)	{
				if (i != 0)	{
					orderStr.append(", ");
				}
				orderStr.append(order[i]);
			}
		}

		
		// Create query
		POQuery query = new POQuery(ctx, po.get_TableName(), sql.toString(), trxName);
		query.setParameters(paramValues);
		query.setOrderBy(orderStr.toString());
		return query.list(clazz);
		
		
	}
	/**
	 * Return a list of objects
	 * 
	 * @param ctx
	 * @param clazz
	 * @param params
	 * @param order
	 * @param trxName
	 * @return
	 */
	public static <T extends PO> List<T> getList(Properties ctx, Class<T> clazz, QParam[] params, String[] order, String trxName)	{
		try {
			return getList_EX(ctx, clazz, params, order, trxName);
		}
		catch (POException e)	{
			s_log.log(Level.SEVERE, "Error obteniendo lista de objetos: " , e);
			return null;
		}
	}

	
	/**
	 * Get a record
	 * @param <T>
	 * @param clazz
	 * @param params
	 * @return
	 */
	public static <T extends PO> T get( Class<T> clazz, QParam[] params)	{
		return get(Env.getCtx(), clazz, params, null);
	}
	
	
	public static <T extends PO> T get( Class<T> clazz, QParam param)	{
		QParam[] params = {param};
		return get(Env.getCtx(), clazz, params, null);
	}
	
	public static <T extends PO> T get(Properties ctx, Class<T> clazz, QParam param, String trxName)	{
		QParam[] params = {param};
		return get(ctx, clazz, params, trxName);
	}
	
	/**
	 * Get a record
	 * @param <T>
	 * @param clazz
	 * @param params
	 * @param trxName
	 * @return
	 */
	public static <T extends PO> T get( Class<T> clazz, QParam[] params, String trxName)	{
		return get(Env.getCtx(), clazz, params, trxName);
	}
	
	/**
	 * Get a record 
	 * @param ctx
	 * @param param
	 * @param trxName
	 * @return
	 * @throws POException
	 */
	
	public static <T extends PO> T get(Properties ctx, Class<T> clazz, QParam[] params, String trxName)	{
		
		List<T> items = getList(ctx, clazz, params, trxName);
		
		if (items.size() == 0)	{
			return null;
		}
		
		if (items.size() > 1)	{
			s_log.severe("Multiple ocurrences when only one expected.\n" + QParam.debugParams(params));
			return null;
		}
		
		return items.get(0);
	}

	/**
	 * Get an List of records
	 * @param <T>
	 * @param ctx
	 * @param clazz
	 * @param param
	 * @return
	 * @throws POException
	 */
	public static <T extends PO> List<T> getList(Properties ctx, Class<T> clazz, QParam param)	{
		QParam[] params = {param};
		return getList(ctx, clazz, params, null, null);
	}
	
	
	public static <T extends PO> List<T> getList(Properties ctx, Class<T> clazz, QParam param, String trxName)	{
		QParam[] params = {param};
		return getList(ctx, clazz, params, null, trxName);
	}

	
	
	/**
	 * Get an List of records
	 * @param <T>
	 * @param ctx
	 * @param clazz
	 * @param param
	 * @return
	 * @throws POException
	 */
	public static <T extends PO> List<T> getList(Properties ctx, Class<T> clazz, QParam[] param)	{
		return getList(ctx, clazz, param, null, null);
	}
	
	/**
	 * Get an List of records
	 * @param <T>
	 * @param ctx
	 * @param clazz
	 * @return
	 * @throws POException
	 */
	public static <T extends PO> List<T> getList(Properties ctx, Class<T> clazz)	{
		return getList(ctx, clazz, null, null, null);
	}
	
	/**
	 * Get an List of records
	 * @param <T>
	 * @param ctx
	 * @param clazz
	 * @param trxName
	 * @return
	 * @throws POException
	 */
	public static <T extends PO> List<T> getList(Properties ctx, Class<T> clazz, String trxName)	{
		return getList(ctx, clazz, null, null, trxName);
	}
	
	/**
	 * Get an List of records
	 * @param <T>
	 * @param ctx
	 * @param clazz
	 * @param param
	 * @param trxName
	 * @return
	 * @throws POException
	 */
	public static <T extends PO> List<T> getList(Properties ctx, Class<T> clazz, QParam[] param, String trxName)	{
		return getList(ctx, clazz, param, null, trxName);
	}
	
	/**
	 * Get record with this ID
	 * @param <T>
	 * @param clazz
	 * @param id
	 * @return
	 */
	public static <T extends PO> T get(Class<T> clazz, int id)	{
		return get(Env.getCtx(), clazz, id, null);
	}
	
	/**
	 * Get record with this ID
	 * @param <T>
	 * @param ctx
	 * @param clazz
	 * @param id
	 * @return
	 */
	public static <T extends PO> T get(Properties ctx, Class<T> clazz, int id)	{
		return get(ctx, clazz, id, null);
	}
	
	/**
	 * Get record with this ID
	 * @param <T>
	 * @param ctx
	 * @param clazz
	 * @param id
	 * @param trxName
	 * @return
	 */
	public static <T extends PO> T get(Properties ctx, Class<T> clazz, int id, String trxName)	{
        Constructor<T> po_constr;
		try {
			po_constr = clazz.getDeclaredConstructor(new Class[] { Properties.class, int.class, String.class });
			T	po	=  po_constr.newInstance(new Object[] { ctx, 0, trxName });
			return po;
		} catch (NoSuchMethodException e)	{throw new RuntimeException ("Error instanciando objetos.", e); }
		catch (Exception e)	{ throw new RuntimeException(e);	}			
	}

	
	public static <T extends PO> Class<T> getClass(int AD_Table_ID)	{
		String tableName = MTable.getTableName(Env.getCtx(), AD_Table_ID);
		return (Class<T>) MTable.getClass(tableName);
	}
	
	public static <T extends PO> T get(Properties ctx, int AD_Table_ID, int id, String trxName)	{
        Constructor<T> po_constr;
        Class clazz = MTable.getClass(MTable.getTableName(ctx, AD_Table_ID));
        
		try {
			po_constr = clazz.getDeclaredConstructor(new Class[] { Properties.class, int.class, String.class });
			T	po	=  po_constr.newInstance(new Object[] { ctx, 0, trxName });
			return po;
		} catch (NoSuchMethodException e)	{throw new RuntimeException ("Error instanciando objetos.", e); }
		catch (Exception e)	{ throw new RuntimeException(e);	}			
	}

	
	
	
}

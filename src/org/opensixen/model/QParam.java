package org.opensixen.model;

import org.compiere.model.MQuery;
import org.compiere.util.CLogger;

public class QParam {

	/**	CLogger											*/
	protected CLogger			log = CLogger.getCLogger (getClass());
	
	/** AND		*/
	public static String COND_AND = "AND";
	
	/** OR		*/
	public static String COND_OR = "OR";
	
	
	/** = 		*/
	public static String OPER_EQUAL = "=";
	
	/** > 		*/
	public static String OPER_BIG = ">";
	
	/** <		*/
	public static String OPER_LESS = "<";
	
	/** >=		*/
	public static String OPER_BIGEQUAL = ">=";

	/** <=		*/
	public static String OPER_LESSEQUAL = "<=";
	
	/** !=		*/
	
	public static String OPER_DIST = "!=";
	
	/** is null		*/
	public static String OPER_ISNULL = "IS NULL";
	
	/** not null	*/
	public static String OPER_NOTNULL	= "NOT NULL";
	
	
	/**	Condition	*/
	private String cond = COND_AND;
	
	/** Operator	*/
	private String oper = OPER_EQUAL;
	
	/** Name		*/
	private String name;
	
	/** Value		*/
	private Object value;
	
	/** Param String */
	private String param_str;

	/**
	 * @param cond
	 * @param name
	 * @param oper
	 * @param value
	 */
	public QParam(String cond, String name, String oper, Object value) {
		if (name == null)	{
			throw new RuntimeException("Name is mandatory");
		}
		this.name = name;
		
		if (oper != null && oper != "")	{
			this.oper = oper;
		}
		
		if (cond != null && cond != "")	{
			this.cond = cond;
		}

		this.value = value;
		if (this.value == null && (!this.oper.equals(OPER_ISNULL) && !this.oper.equals(OPER_NOTNULL)))	{
			throw new RuntimeException("Value is mandatory. PARAM=" + this.name);
		}
				
		log.fine("Added Param: " + getParam_str(false) + " Value: " + getValue().toString());
	}
	
	/**
	 * @param cond
	 * @param name
	 * @param oper
	 * @param value
	 */
	public QParam(String cond, String name, String oper, int value) {
		this(cond, name, oper, new Integer(value));
	}

	/**
	 * @param name
	 * @param value
	 */
	public QParam(String name, Object value) {
		this(null, name, null, value);
	}

	/**
	 * @param name
	 * @param value
	 */
	public QParam(String name, int value) {
		this(null, name, null, new Integer(value));
	
	}
	
	
	/**
	 * @param name
	 * @param oper
	 * @param value
	 */
	public QParam(String name, String oper, Object value) {
		this(null, name, oper, value);
	}
	
	/**
	 * @param name
	 * @param oper
	 * @param value
	 */
	public QParam(String name, String oper, int value) {
		this(null, name, oper, new Integer(value));
	}
	

	/**
	 * @param param_str
	 */
	public QParam(String param_str) {
		this.param_str = param_str;
		log.fine("Added Param: " + getParam_str(false));
	}
	

	public String getCond() {
		return cond;
	}

	public void setCond(String cond) {
		this.cond = cond;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOper() {
		return oper;
	}

	public void setOper(String oper) {
		this.oper = oper;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getParam_str(boolean ignoreCond) {
		
		StringBuffer sql = new StringBuffer();
		if (!ignoreCond)	{
			sql.append(getCond()).append(" ");
		}

		if (param_str != null)	{
			sql.append(param_str);
			return sql.toString();
		}

		
		sql.append(getName());
		
		sql.append(getOper());
		
		if (!(getOper().toUpperCase().equals(OPER_ISNULL)) && !(getOper().toUpperCase().equals(OPER_NOTNULL)))	{
			sql.append("?");
		}
		
		return sql.toString();
	}

	public String getParam_str() {
		return this.getParam_str(false);
	}
	
	
	public boolean isCustom()	{
		if (param_str != null)	{
			return true;
		}
		return false;
	}
	
	
	
	
	
	public static String debugParams(QParam[] params)	{
		StringBuffer buff = new StringBuffer();
		
		for (int i=0; i < params.length; i++)	{
			if (params[i].isCustom())	{
				buff.append(params[i].getParam_str(i==0));
			}
			else {
				buff.append(params[i].getCond()+ " " + params[i].getName()+params[i].getOper()+params[i].getValue());
			}
			buff.append("\n");
		}
		
		return buff.toString();
	}
	
	public static QParam[] get(String name, Object value)	{
		QParam[] params = { new QParam(name, value)};
		return params;
	}
	
	public static QParam[] get(String name, int value)	{
		QParam[] params = { new QParam(name, value)};
		return params;
	}
	
	/**
	 * Convert QParams to MQuery
	 * @param tableName
	 * @param params
	 * @return
	 */
	public static MQuery toMQuery(String tableName, QParam[] params)	{
		MQuery query = new MQuery(tableName);
		
		if (params.length == 0)	{
			return query;
		}
		
		for (QParam param:params)	{
			query.addRestriction(param.getParam_str(true), param.getCond(), false);
		}
		
		return query;
	}
	
}

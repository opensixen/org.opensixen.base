 /******* BEGIN LICENSE BLOCK *****
 * Versión: GPL 2.0/CDDL 1.0/EPL 1.0
 *
 * Los contenidos de este fichero están sujetos a la Licencia
 * Pública General de GNU versión 2.0 (la "Licencia"); no podrá
 * usar este fichero, excepto bajo las condiciones que otorga dicha 
 * Licencia y siempre de acuerdo con el contenido de la presente. 
 * Una copia completa de las condiciones de de dicha licencia,
 * traducida en castellano, deberá estar incluida con el presente
 * programa.
 * 
 * Adicionalmente, puede obtener una copia de la licencia en
 * http://www.gnu.org/licenses/gpl-2.0.html
 *
 * Este fichero es parte del programa opensiXen.
 *
 * OpensiXen es software libre: se puede usar, redistribuir, o
 * modificar; pero siempre bajo los términos de la Licencia 
 * Pública General de GNU, tal y como es publicada por la Free 
 * Software Foundation en su versión 2.0, o a su elección, en 
 * cualquier versión posterior.
 *
 * Este programa se distribuye con la esperanza de que sea útil,
 * pero SIN GARANTÍA ALGUNA; ni siquiera la garantía implícita 
 * MERCANTIL o de APTITUD PARA UN PROPÓSITO DETERMINADO. Consulte 
 * los detalles de la Licencia Pública General GNU para obtener una
 * información más detallada. 
 *
 * TODO EL CÓDIGO PUBLICADO JUNTO CON ESTE FICHERO FORMA PARTE DEL 
 * PROYECTO OPENSIXEN, PUDIENDO O NO ESTAR GOBERNADO POR ESTE MISMO
 * TIPO DE LICENCIA O UNA VARIANTE DE LA MISMA.
 *
 * El desarrollador/es inicial/es del código es
 *  FUNDESLE (Fundación para el desarrollo del Software Libre Empresarial).
 *  Indeos Consultoria S.L. - http://www.indeos.es
 *
 * Contribuyente(s):
 *  Eloy Gómez García <eloy@opensixen.org> 
 *
 * Alternativamente, y a elección del usuario, los contenidos de este
 * fichero podrán ser usados bajo los términos de la Licencia Común del
 * Desarrollo y la Distribución (CDDL) versión 1.0 o posterior; o bajo
 * los términos de la Licencia Pública Eclipse (EPL) versión 1.0. Una 
 * copia completa de las condiciones de dichas licencias, traducida en 
 * castellano, deberán de estar incluidas con el presente programa.
 * Adicionalmente, es posible obtener una copia original de dichas 
 * licencias en su versión original en
 *  http://www.opensource.org/licenses/cddl1.php  y en  
 *  http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * Si el usuario desea el uso de SU versión modificada de este fichero 
 * sólo bajo los términos de una o más de las licencias, y no bajo los 
 * de las otra/s, puede indicar su decisión borrando las menciones a la/s
 * licencia/s sobrantes o no utilizadas por SU versión modificada.
 *
 * Si la presente licencia triple se mantiene íntegra, cualquier usuario 
 * puede utilizar este fichero bajo cualquiera de las tres licencias que 
 * lo gobiernan,  GPL 2.0/CDDL 1.0/EPL 1.0.
 *
 * ***** END LICENSE BLOCK ***** */

package org.opensixen.model;

import org.compiere.model.MQuery;
import org.compiere.util.CLogger;

/**
 * QParam
 * 
 * Query restrictions for POFactory
 * 
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 *
 */
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

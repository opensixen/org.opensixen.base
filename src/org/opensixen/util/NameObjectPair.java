package org.opensixen.util;

import org.compiere.util.NamePair;

public class NameObjectPair extends NamePair {


	/** The Value       */
	private Object m_value = null;
	
	/**
	 *	Construct KeyValue Pair
	 *  @param value value
	 *  @param name string representation
	 */
	public NameObjectPair(String name, Object value)
	{
		super(name);
		m_value = value;
		if (m_value == null)
			m_value = "";
	}   //  ValueNamePair


	/**
	 *	Get Value
	 *  @return Value
	 */
	public Object getValue()
	{
		return m_value;
	}	//	getValue

	/**
	 *	Get ID
	 *  @return Name
	 */
	public String getID()
	{
		return getName();
	}	//	getID

	/**
	 *	Equals
	 *  @param obj Object
	 *  @return true, if equal
	 */
	public boolean equals(Object obj)
	{
		if (obj instanceof NameObjectPair)
		{
			NameObjectPair pp = (NameObjectPair)obj;
			if (pp.getName() != null && pp.getValue() != null &&
				pp.getName().equals(getName()) && pp.getValue().equals(m_value))
				return true;
			return false;
		}
		return false;
	}	//	equals

	/**
	 *  Return Hashcode of value
	 *  @return hascode
	 */
	public int hashCode()
	{
		return m_value.hashCode();
	}   //  hashCode

	
	

}

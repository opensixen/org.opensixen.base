/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.opensixen.osgi.equinox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.compiere.model.Callout;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.opensixen.osgi.ServiceQuery;

/**
 * This List looks up services as extensions in equinox.
 * The extension point must be the class name of the service interface.
 * The query attributes are checked against the attributes
 * of the extension configuration element. 
 * 
 * In order to minimize equinox lookups, a filtering iterator is used. 
 * @author viola
 * @author Eloy Gomez
 *
 * @param <T> The service this list holds implementations of.
 */
public class ExtensionList<T> implements Iterable<T>{

	public class ExtensionIterator<T> implements Iterator<T> {

		private int index = 0;

		public boolean hasNext() {
			if (elements == null)	{
				return false;
			}
			iterateUntilAccepted();
			return index<elements.length;
		}

		private void iterateUntilAccepted() {
			while (index<elements.length) {
				if (accept(elements[index]))
					break;
				index++;
			}
		}

		private boolean accept(IConfigurationElement element) {
			for (String name : filters.keySet()) {
				String expected = filters.get(name);
				String actual = element.getAttribute(name);
				if (!expected.equals(actual))
					return false;
			}
			return true;
		}

		@SuppressWarnings("unchecked")
		public T next() {
			iterateUntilAccepted();
			IConfigurationElement e = elements[index++];
			try {
				return (T) e.createExecutableExtension("class");
			} catch (CoreException ex) {
				throw new IllegalStateException(ex);
			}
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	private IConfigurationElement[] elements;
	private HashMap<String, String> filters = new HashMap<String, String>();

	public ExtensionList(Class<T> clazz, String id) {
		try {
			elements = Platform.getExtensionRegistry().getConfigurationElementsFor(id);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public ExtensionList(Class<T> type, String name, ServiceQuery query) {
		this(type, name);
		for (String key : query.keySet()) {
			addFilter(key, query.get(key));
		}
	}

	public Iterator<T> iterator() {
		return new ExtensionIterator<T>();
	}

	public void addFilter(String name, String value) {
		filters.put(name, value);
	}

	public T first() {
		Iterator<T> i = iterator();
		if (!i.hasNext())
			return null;
		return i.next();
	}

	public List<T> asList() {
		List<T> result = new ArrayList<T>();
		for (T t : this) {
			result.add(t);
		}
		return result;
	}

}

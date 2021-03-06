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

import java.util.List;

import org.opensixen.osgi.ServiceQuery;
import org.opensixen.osgi.interfaces.IService;
import org.opensixen.osgi.interfaces.IServiceLocator;



/**
 * This is the Equinox implementation of the ADempiere Service Locator.
 * It delegates work to the ExtensionList that lookups up services as extensions.
 * The ids of extension points have to correspond to the interface names of the services.
 *  
 * @author viola
 * @author Eloy Gomez
 *
 */
public class EquinoxServiceLocator implements IServiceLocator {

	public <T extends IService> List<T> list(Class<T> type) {
		ExtensionList<T> list = new ExtensionList<T>(type, type.getName());
		return list.asList();
	}

	public <T extends IService> List<T> list(Class<T> type, ServiceQuery query) {
		ExtensionList<T> list = new ExtensionList<T>(type, type.getName(), query);
		return list.asList();
	}

	public <T extends IService> T locate(Class<T> type) {
		ExtensionList<T> list = new ExtensionList<T>(type, type.getName());
		return list.first();
	}

	public <T extends IService> T locate(Class<T> type, ServiceQuery query) {
		ExtensionList<T> list = new ExtensionList<T>(type, type.getName(), query);
		return list.first();
	}

}

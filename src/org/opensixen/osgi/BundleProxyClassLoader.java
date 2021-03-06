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

package org.opensixen.osgi;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import org.eclipse.core.runtime.IPluginRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.framework.internal.core.BundleContextImpl;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * Replace ClassLoader for OSGI enviroment 
 * 
 * 
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 *
 */
public class BundleProxyClassLoader extends ClassLoader {

	private Bundle bundle;
	private ClassLoader parent;
			
	public BundleProxyClassLoader(Bundle bundle) {
		this.bundle = bundle;
	}
	
	public BundleProxyClassLoader(Bundle bundle, ClassLoader parent) {
		super(parent);
		this.parent = parent;
	    this.bundle = bundle;
	}

	// Note: Both ClassLoader.getResources(...) and bundle.getResources(...) consult 
	// the boot classloader. As a result, BundleProxyClassLoader.getResources(...) 
	// might return duplicate results from the boot classloader. Prior to Java 5 
	// Classloader.getResources was marked final. If your target environment requires
	// at least Java 5 you can prevent the occurence of duplicate boot classloader 
	// resources by overriding ClassLoader.getResources(...) instead of 
	// ClassLoader.findResources(...).   
	public Enumeration findResources(String name) throws IOException {
	    return bundle.getResources(name);
	}
	
	public URL findResource(String name) {
	    return bundle.getResource(name);
	}

	public Class findClass(String name) throws ClassNotFoundException {
	    return bundle.loadClass(name);
	}
	
	public URL getResource(String name) {
		return (parent == null) ? findResource(name) : super.getResource(name);
	}

	protected Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
		Class clazz = (parent == null) ? findClass(name) : super.loadClass(name, false);
		if (resolve)
			super.resolveClass(clazz);
		
		return clazz;
	}

	
	public static Class getClass(String name) throws ClassNotFoundException	{
		BundleContext context = Activator.getContext(); 
		
		// If no context, we are outside OSGi.
		if (context == null)	{
			return Class.forName(name);
		}
		Bundle[] bundles = context.getBundles();
		for (Bundle bundle:bundles)	{
			try {
				BundleProxyClassLoader classLoader = new BundleProxyClassLoader(bundle);
				Class clazz = classLoader.loadClass(name, true);
				return clazz;
			}
			catch (ClassNotFoundException e)	{}
		}
		throw new ClassNotFoundException("Can't load class: " + name);
	}
	
	
	public static URL findResourceInAll(String name)	{
		Bundle[] bundles = Activator.getContext().getBundles();
		for (Bundle bundle:bundles)	{
			URL resource = bundle.getResource(name);
			if (resource != null)	{
				return resource;
			}
		}
		return null;
	}
	
	
}

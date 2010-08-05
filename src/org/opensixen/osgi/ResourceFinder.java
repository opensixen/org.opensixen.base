package org.opensixen.osgi;

import java.net.URL;
import java.util.List;

import org.compiere.Adempiere;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.opensixen.osgi.interfaces.IResourceFinder;

public class ResourceFinder {

	public static URL getResource(String name) {

		List<IResourceFinder> f = Service.list(IResourceFinder.class);
		for (IResourceFinder finder : f) {
			URL url = finder.getResource(name);
			if (url != null)
				return url;
		}
		return null;

		/*
		 * IConfigurationElement[] config =
		 * Platform.getExtensionRegistry().getConfigurationElementsFor
		 * (extensionPointId) try { for (IConfigurationElement e : config) {
		 * System.out.println("Evaluating extension"); final Object o =
		 * e.createExecutableExtension("class"); if (o instanceof IGreeter) {
		 * ISafeRunnable runnable = new ISafeRunnable() {
		 * 
		 * @Override public void handleException(Throwable exception) {
		 * System.out.println("Exception in client"); }
		 * 
		 * @Override public void run() throws Exception { ((IGreeter)
		 * o).greet(); } }; SafeRunner.run(runnable); } } } catch (CoreException
		 * ex) { System.out.println(ex.getMessage()); }
		 */
	}

}

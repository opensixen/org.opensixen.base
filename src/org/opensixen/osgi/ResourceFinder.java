package org.opensixen.osgi;

import java.net.URL;
import java.util.List;

import org.opensixen.osgi.interfaces.IResourceFinder;

public class ResourceFinder {

	public static URL getResource(String name) {
		List<IResourceFinder> f = Service.list(IResourceFinder.class);
		for (IResourceFinder finder : f) {
			URL url = finder.getResource(name);
			if (url!=null)
				return url;
		}
		return null;
	}
	
}

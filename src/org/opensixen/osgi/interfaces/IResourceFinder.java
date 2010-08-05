/**
 * Clase creada por Jorg Viola
 * Adaptada por Eloy Gomez
 */
package org.opensixen.osgi.interfaces;

import java.net.URL;

public interface IResourceFinder extends IService {

	URL getResource(String name);

}

package nz.ac.massey.buto.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ClasspathUtils {

	/**
	 * Adds the jars in the given directory to classpath
	 * 
	 * @param directory
	 * @throws IOException
	 */
	public static void addDirToClasspath(File directory) throws IOException {
		
		// Iterate over files and add to classpath
		for(File file : directory.listFiles()){
			addURL(file.toURI().toURL());
		}
	}

	/**
	 * Add URL to CLASSPATH
	 * 
	 * @param URL
	 * @throws IOException
	 */
	public static void addURL(URL newURL) throws IOException {
		
		// Get system class loader
		URLClassLoader sysLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		
		// Test the newURL is not alread loaded
		for(URL url : sysLoader.getURLs()){
			
			// compare URL
			if (url.toString().equalsIgnoreCase(newURL.toString())) {
				
				// already loaded
				System.out.println("URL " + newURL + " is already in the CLASSPATH");
				return;
			}
		}
		
		// load newURL
		Class<URLClassLoader> sysclass = URLClassLoader.class;
		try {
			
			// In
			Method method = sysclass.getDeclaredMethod("addURL", new Class[] { URL.class });
			method.setAccessible(true);
			method.invoke(sysLoader, new Object[] { newURL });
			
		} catch (Throwable t) {
			t.printStackTrace();
			
			throw new IOException("Error, could not add URL to system classloader");
		}
	}
}
package nz.ac.massey.buto.settings;

import java.io.File;

/**
 * Class that contains manages the different paths that Buto requries to run.
 * 
 * @author Fergus Hewson
 *
 */
public class ButoSettings {

	/**
	 * The data folder to use.
	 */
	public static String butoHomePath;
	public static String jdkHomePath;
	public static String profileDirPath;
	public static String libDirPath;
	public static String pluginDirPath;
	
	public static String stdoutLogFolder;
	public static String stdoutLogFileName;
	
	/**
	 * Initializes all the settings.
	 */
	public static void init(){
		butoHomePath = System.getenv("BUTO_HOME");
		jdkHomePath = System.getenv("JDK_HOME");
		profileDirPath = butoHomePath + File.separator + "profiles";
		libDirPath = butoHomePath + File.separator + "lib";
		pluginDirPath = butoHomePath + File.separator + "lib" + File.separator + "plugins";
		stdoutLogFolder   = butoHomePath + File.separator + "logging";
		stdoutLogFileName = "buto-monitor_stdout.log";
	}
}

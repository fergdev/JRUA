package nz.ac.massey.buto.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * A profile is a test that is monitored by buto.
 * 
 * @author Fergus Hewson
 *
 */
public class Profile {

	/**
	 * The name of the profile, should be unique to other stored profiles.
	 */
	private String name;
	
	/**
	 * The classpath used to run test fixtures with.
	 */
	private String classPath;
	
	/**
	 * The main class used to start test fixtures with.
	 */
	private String mainClass;
	
	/**
	 * The default arguments used to start test fixtures with.
	 */
	private String defaultArgs;
	
	/**
	 * The default options passed to the JVM when starting processed for this profile.
	 */
	private String defaultOptions;
	
	/**
	 * List of Beans that will be monitored by the JMX collector during tests.
	 */
	private List<MonitoredMBean> monitoredMBeans;
	
	/**
	 * Custom ButoProperty objects for the custom monitored MBeans.
	 */
	private List<ButoProperty> customButoProperty;	
	
	/**
	 * List of oracles that have been created by this profile.
	 */
	private List<Oracle> oracleList;

	
	public Profile(String name, String classPath, String mainClass, String defaultArgs, String defaultOptions){
		this(name, classPath, mainClass, defaultArgs, defaultOptions, new ArrayList<Oracle>(), new ArrayList<MonitoredMBean>(), new ArrayList<ButoProperty>() );
	}
	
	public Profile(String name, String classPath, String mainClass, String defaultArgs, String defaultOptions, List<Oracle> oracleList){
		this(name, classPath, mainClass, defaultArgs, defaultOptions, oracleList, new ArrayList<MonitoredMBean>(), new ArrayList<ButoProperty>());
	}
	
	public Profile(String name, String classPath, String mainClass, String defaultArgs, String defaultOptions, List<Oracle> oracleList, List<MonitoredMBean> monitoredMBeans){
		this(name, classPath, mainClass, defaultArgs, defaultOptions, oracleList, monitoredMBeans, new ArrayList<ButoProperty>());
	}
	
	public Profile(String name, String classPath, String mainClass,
			String defaultArgs, String defaultOptions, List<Oracle> oracleList,
			List<MonitoredMBean> monitoredMBeans, List<ButoProperty> customDataTypes) {
		this.name = name;
		this.classPath = classPath;
		this.mainClass = mainClass;
		this.defaultArgs = defaultArgs;
		this.defaultOptions = defaultOptions;
		this.oracleList = oracleList;
		this.monitoredMBeans = monitoredMBeans;
		this.customButoProperty = customDataTypes;
	}

	public String getName() {
		return name;
	}
	public List<Oracle> getOracleList(){
		return oracleList;
	}
	
	public String getClassPath() {
		return classPath;
	}
	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}
	
	public String getMainClass() {
		return mainClass;
	}
	public void setMainClass(String mainClass) {
		this.mainClass = mainClass;
	}

	public String getDefaultArgs() {
		return defaultArgs;
	}
	public void setDefaultArgs(String defaultArgs){
		this.defaultArgs = defaultArgs;
	}
	
	public String getDefaultOptions(){
		return defaultOptions;
	}
	public void setDefaultOptions(String defaultOptions){
		this.defaultOptions = defaultOptions;
	}
	
	public List<MonitoredMBean> getMonitoredMBeans() {
		return monitoredMBeans;
	}

	public void setMonitoredMBeans(List<MonitoredMBean> monitoredMBeans) {
		this.monitoredMBeans = monitoredMBeans;
	}

	public List<ButoProperty> getCustomDataTypes() {
		return customButoProperty;
	}

	public void setCustomDataTypes(List<ButoProperty> customDataTypes) {
		this.customButoProperty = customDataTypes;
	}



}

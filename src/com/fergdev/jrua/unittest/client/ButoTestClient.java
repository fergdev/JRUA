package nz.ac.massey.buto.unittest.client;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.apache.log4j.FileAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;



import nz.ac.massey.buto.jmx.JMXUtils;
import nz.ac.massey.buto.settings.ButoSettings;
import nz.ac.massey.buto.unittest.annotations.ButoTest;
import nz.ac.massey.buto.unittest.annotations.ButoTraining;
import nz.ac.massey.buto.unittest.parameters.MalformedParameterStringException;
import nz.ac.massey.buto.unittest.parameters.ParameterStringParser;
import nz.ac.massey.buto.utils.AnnotationUtils;
import nz.ac.massey.buto.utils.ClasspathUtils;
import nz.ac.massey.buto.utils.ProcessHelper;

import static org.junit.Assert.assertTrue;

/**
 * Test client for Buto, used to drive the performance test and integrate with JUnit.
 * 
 * @author Fergus Hewson
 *
 */
public class ButoTestClient{
	
	
	/**
	 * Log4j logger.
	 */
	private static Logger logger = LogManager.getLogger(ButoTestClient.class);
	
	/**
	 * Flag to prevent multiple tests running at the same time.
	 */
	private static boolean running = false;
	
	/**
	 * Reference to the class being tested.
	 */
	private static Class<?> testType;
	
	/**
	 * Reference to the class being tested
	 */
	private static String testName;
	
	/**
	 * Parameters to configure the monitor
	 */
	private static String monitorParameters;
	
	/**
	 * Connection to the monitor MBean server
	 */
	private static MBeanServerConnection connection = null;
	
	/**
	 * Object name of the Monitor JMX object, used to communicate with the monitor VM.
	 */
	private static ObjectName monitorObjectName;
	
	/**
	 * Flag to singal whether to open the test report on completion
	 */
	private static boolean openTestReport = false;
	
	public static void startMonitoring(Class<?> testClass, String tName) throws ButoClientException{
		startMonitoring(testClass, tName,false, false);
	}
	
	/**
	 * Starts the Buto Monitor in separate VM.
	 * Should be called in the test set up method, annotated with 'org.junit.BeforeClass' annotation.
	 * 
	 * @param testKlass The test class being run
	 * @throws ButoClientException 
	 */
	public static void startMonitoring(Class<?> testClass, String tName, boolean openTestReportOnExit) throws ButoClientException{

		startMonitoring(testClass, tName,openTestReportOnExit, false);
	}
	/**
	 * Starts the Buto Monitor in separate VM.
	 * Should be called in the test set up method, annotated with 'org.junit.BeforeClass' annotation.
	 * 
	 * @param testKlass The test class being run
	 * @throws ButoClientException 
	 */
	public static void startMonitoring(Class<?> testClass, String tName, boolean openTestReportOnExit, boolean showMonitorGui) throws ButoClientException{
		
		openTestReport = openTestReportOnExit;
		testName = tName;
		
		if(System.getenv("BUTO_HOME") == null){
			throw new ButoClientException("Environment variable 'BUTO_HOME' has not been set.");
		}
		
		if(System.getenv("JDK_HOME") == null){
			throw new ButoClientException("Environment variable 'JDK_HOME' has not been set, cannot access library 'tools.jar'.");
		}

		ButoSettings.init();
		
		File log4jPropertyFile = new File(ButoSettings.butoHomePath + File.separator + "log4j-client.properties");
		
		if(log4jPropertyFile.exists()){
			PropertyConfigurator.configure(log4jPropertyFile.getPath());
		}else{
			FileAppender fa = new FileAppender();
			fa.setName("FileLogger");
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			fa.setFile(ButoSettings.butoHomePath + File.separator + 
					"logging" + File.separator + 
					"buto-client_" + sdf.format(new Date())+ ".log");
			fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
			//fa.setThreshold(Priority.ALL_INT);
			fa.setAppend(true);
			fa.activateOptions();

			// add appender to any Logger (here is root)
			Logger.getRootLogger().addAppender(fa);
		}
		
		logger.info("Buto client started.....");
		logger.info("BUTO_HOME environment variable set to '" + ButoSettings.butoHomePath + "'");
		logger.info("JDK_HOME environment variable set to '" + ButoSettings.jdkHomePath + "'");
		
		File toolsJarFile = new File(System.getenv("JDK_HOME") + File.separator + "lib" + File.separator + "tools.jar");
		
		if(!toolsJarFile.exists()){
			throw new ButoClientException("Unable to locate 'tools.jar' check that the 'JDK_HOME' variable has been set correctly."
					+ " Path = '" + toolsJarFile.getAbsolutePath() + "'");
		}
		
		logger.info("JDK tools.jar path found at '" + toolsJarFile + "'");
		logger.info("Adding JDK tools.jar to classpath");
		
		try {
			ClasspathUtils.addURL(toolsJarFile.toURI().toURL());
		} catch (Exception e){
			String message = "Error adding JDK tools.jar to classpath '" + e.getMessage() + "'";
			logger.error(message);
			throw new ButoClientException(message);
		}
		
		
		if(testClass.isAnnotationPresent(ButoTest.class)){
			testType = ButoTest.class;
			monitorParameters = "";
			
		}else if(testClass.isAnnotationPresent(ButoTraining.class)){
			
			ButoTraining butoTrainingAnnotation = testClass.getAnnotation(ButoTraining.class);
			testType = ButoTraining.class;
			monitorParameters = butoTrainingAnnotation.parameters();
			
			
			try{
				ParameterStringParser.parse(monitorParameters);
			}catch(MalformedParameterStringException ex){
				throw new ButoClientException("Error parsing parameter string - " + ex.getMessage());
			}
			
		}else{
			throw new ButoClientException("No annotation of test class provided '" 
					+ testClass + "', decoration with either 'ButoTest' or 'ButoTraining' annotations.");
		}
			
		running = true;
		
		List<Method> testMethodList = AnnotationUtils.getMethodsWithAnnotation(testClass, org.junit.Test.class);
		
		if(testMethodList.size() == 0 ){
			throw new ButoClientException("No tests are decorated with 'org.junit.Test' annotation.");
		}
		
		StringBuilder methodNameSB = new StringBuilder();
		logger.info("Printing annotated method names....");
		for(int i = 0; i < testMethodList.size(); i++){
			Method m = testMethodList.get(i);
			logger.info("Method name " + m.getName());
			methodNameSB.append(m.getName());
			if(i != testMethodList.size() - 1){
				methodNameSB.append(";");
			}
		}
		
		String mainClass = "nz.ac.massey.buto.unittest.monitor.Monitor";
		String classpath = ButoSettings.butoHomePath + File.separator + "lib" + File.separator + "*" + File.pathSeparator + toolsJarFile.getAbsolutePath();
		
		logger.info("Starting monitor....");
		logger.info("Main class '" + mainClass + "'");
		logger.info("Classpath '" + classpath + "'");
		
		
		try {

			ProcessHelper ph = new ProcessHelper(
					mainClass, 
					classpath, 
					new String[]{testName, testType.getSimpleName(), methodNameSB.toString(), monitorParameters, Boolean.toString(showMonitorGui)}, 
					new String[]{});
		
			ph.startProcess();
		} catch (Exception e) {
			logger.error("Error starting process '" + e.getMessage() + "'");
			throw new ButoClientException("Error starting test monitor '"+ e.getMessage() + "'");
		}			
		
		// offset connection to monitor via JMX
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e1) {}
		
		// Get connection to monitor via JMX
		try {
			connection = JMXUtils.getMBeanServerConnection(mainClass);
		} catch (Exception e1) {
			String message = "Error getting connection to MBean server '" + e1.getMessage() + "'";
			logger.error(message);
			throw new ButoClientException(message);
		}
		
		
		// Send start monitoring method to monitor
		try {
			monitorObjectName = new ObjectName("nz.ac.massey.buto.unittest.monitor.Monitor:type=Monitor,id=1");
			
			connection.invoke(
					monitorObjectName, 
					"notify", 
					new Object[]{"startMonitoring"}, 
					new String[]{String.class.getName()});
		} catch (Exception e){
			String message = "Error invoking command on monitor '"+ e.getMessage() + "'";
			logger.error(message);
			throw new ButoClientException(message);
		}
			

		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Stops the monitoring of the tests.
	 * Should be called in the test after class tear down method decorated with the annotation 'org.junit.BeforeClass'
	 * @throws ButoClientException
	 */
	public static void stopMonitoring() throws ButoClientException{
		running = false;
		
		try {
			connection.invoke(monitorObjectName, 
					"notify", 
					new Object[]{"stopMonitoring"}, 
					new String[]{String.class.getName()});
		} catch (InstanceNotFoundException | MBeanException
				| ReflectionException | IOException e) {
			logger.error("Error communicating with monitor vm '"  + e + "'");
		}
		
		connection = null;
		
		if(openTestReport && testType.equals(ButoTest.class)){
			System.out.println("Some tests failed, for more detailed output see report located at....");
			System.out.println("SOME PATH......");
			
			File reportPath = new File(
					ButoSettings.profileDirPath + File.separator + 
					testName + File.separator + 
					"classification_results" + File.separator +
					"report.html"
					);
			
			try {
				logger.info("Opening report path = '" + reportPath + "'");
				Desktop.getDesktop().open(reportPath);
			} catch (IOException e) {
				logger.error("Error opening Buto report '" + e.getMessage() + "'");
			}
		}
	}
	
	/**
	 * Used to notify monitor of new test. 
	 * Should be called in the test set up method decorated with the annotation 'org.junit.Before'.
	 * @throws ButoClientException
	 */
	public static void testSetup() throws ButoClientException{
		if(!running){
			throw new ButoClientException("Client is not running cannot setup test.");
		}
	
		try {
			connection.invoke(monitorObjectName, 
					"notify", 
					new Object[]{"testSetup"}, 
					new String[]{String.class.getName()});
		} catch (InstanceNotFoundException | MBeanException | ReflectionException | IOException e) {
			logger.error("Error communicating with the monitor '" + e.getMessage() + "' attempted to send message 'testSetup'");
		}
	}
	
	/**
	 * 
	 * @throws ButoClientException
	 */
	public static void testTearDown() throws ButoClientException{
		if(!running){
			throw new ButoClientException("Client is not running cannot tear down test.");
		}
		
		try {
			connection.invoke(monitorObjectName, 
					"notify", 
					new Object[]{"testTearDown"}, 
					new String[]{String.class.getName()});
		} catch (InstanceNotFoundException | MBeanException
				| ReflectionException | IOException e) {
			logger.error("Error communicating with the monitor '" + e.getMessage() + "' attempted to send message 'testTearDown'");
			throw new ButoClientException("Error invoking ");
		}
	}
	
	/**
	 * Method used to assert the test fits the generated model.
	 * 
	 * @throws ButoClientException
	 */
	public static void butoAssert() throws ButoClientException{
		if(!running){
			throw new ButoClientException("Client is not running cannot assert test.");
		}
		
		// Cannot assert a training run
		if(testType.equals(ButoTraining.class)){
			return;
		}
		
		try {
			// Invoke assert on monitor
			Boolean result = (Boolean) connection.invoke(
					monitorObjectName, 
					"butoAssert", 
					new Object[]{}, 
					new String[]{});
			
			// Call Junit assert
			assertTrue(result);
			
		} catch (InstanceNotFoundException | MBeanException
				| ReflectionException | IOException e) {
			throw new ButoClientException("Error during assertion '" + e.getMessage() + "'");
		}
	}
	
}

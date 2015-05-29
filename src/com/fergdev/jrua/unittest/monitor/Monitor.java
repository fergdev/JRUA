package nz.ac.massey.buto.unittest.monitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.apache.log4j.FileAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;

import nz.ac.massey.buto.analysis.algorithms.ClassificationAlgorithm;
import nz.ac.massey.buto.analysis.algorithms.ClassificationAlgorithmManager;
import nz.ac.massey.buto.analysis.algorithms.models.ClassificationModelResult;
import nz.ac.massey.buto.analysis.algorithms.models.ModelType;
import nz.ac.massey.buto.analysis.bounds.Bounds;
import nz.ac.massey.buto.analysis.classifier.ClassificationResult;
import nz.ac.massey.buto.analysis.classifier.Classifier;
import nz.ac.massey.buto.analysis.smoothing.DataSmoother;
import nz.ac.massey.buto.analysis.smoothing.DataSmootherManager;
import nz.ac.massey.buto.analysis.timepoint.TimePointGenerator;
import nz.ac.massey.buto.analysis.timepoint.TimePointGeneratorManager;
import nz.ac.massey.buto.domain.ButoProperty;
import nz.ac.massey.buto.domain.ButoPropertyManager;
import nz.ac.massey.buto.domain.MonitoredMBean;
import nz.ac.massey.buto.domain.Oracle;
import nz.ac.massey.buto.domain.Profile;
import nz.ac.massey.buto.domain.TrainingRun;
import nz.ac.massey.buto.graph.ChartGenerator;
import nz.ac.massey.buto.graph.GraphIO;
import nz.ac.massey.buto.graph.GraphingTools;
import nz.ac.massey.buto.io.DataSourceFactory;
import nz.ac.massey.buto.io.filedatasource.FileDataSource;
import nz.ac.massey.buto.jmx.JMXTimeoutThread;
import nz.ac.massey.buto.jmx.JMXUtils;
import nz.ac.massey.buto.jmx.collector.JMXCollector;
import nz.ac.massey.buto.jmx.collector.JMXCollectorConfiguration;
import nz.ac.massey.buto.jmx.collector.JMXCollectorConfigurationFactory;
import nz.ac.massey.buto.jmx.collector.JMXCollectorListener;
import nz.ac.massey.buto.settings.ButoSettings;

import nz.ac.massey.buto.unittest.parameters.ParameterStringParser;
import nz.ac.massey.buto.utils.ClasspathUtils;
import nz.ac.massey.buto.utils.IOUtils;
import nz.ac.massey.buto.utils.NewlineStringBuilder;

/**
 * Monitor for JUnit integration.
 * 
 * @author Fergus Hewson
 *
 */
public class Monitor implements MonitorMXBean{

	/**
	 * Log4j Monitor.
	 */
	private static Logger logger = LogManager.getLogger(Monitor.class);
	
	private static JMXCollector jmxCollector;
	private static MBeanServerConnection connection;
	private static Profile testProfile;
	private static int testCount;
	private static int testIndex = 0;
	private static String executionType;
	private static String[] testMethodNameArray;
	
	private static Map<String,String> trainingParameterMap;
	private static List<ButoProperty> butoPropertyList = new ArrayList<ButoProperty>(); //
	
	private static Monitor monitor;
	private static boolean running = true;
	private static File profileDataFolder;
	private static File classificationResultFolder;
	
	private static List<Oracle> oracleList;
	
	private static Map<String, Classifier> classifierMap;
	private static List<ClassificationResult> resultList = new ArrayList<ClassificationResult>();
	
	private static MonitorGui gui;
	private static JMXTimeoutThread timeoutThread;
	
	
	/**
	 * Main class, creates a new monitor and attaches to the 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
		
		System.out.println(args);
		
		// Check environment variable BUTO_HOME is set
		if(!System.getenv().containsKey("BUTO_HOME")){
			logger.error("Environment variable 'BUTO_HOME' is not set.");
			return;
		}
		
		// Check environment variable JDK_HOME is set
		if(!System.getenv().containsKey("JDK_HOME")){
			logger.error("Environment variable 'JDK_HOME' is not set.");
			return;
		}
		
		// Initialize settings object
		ButoSettings.init();
		
		// Set up logging
		File log4jPropertyFile = new File(
				ButoSettings.butoHomePath + File.separator + 
				"log4j-monitor.properties");
		
		// Check for log4j configuration file
		if(log4jPropertyFile.exists()){
			PropertyConfigurator.configure(log4jPropertyFile.getPath());
		}else{
			FileAppender fa = new FileAppender();
			fa.setName("FileLogger");
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			fa.setFile(
					ButoSettings.butoHomePath + File.separator + 
					"logging" + File.separator + 
					"buto-monitor_" +  sdf.format(new Date()) +  ".log");
			fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
			//fa.setThreshold(Priority.ALL_INT);
			fa.setAppend(true);
			fa.activateOptions();

			// add appender to any Logger (here is root)
			Logger.getRootLogger().addAppender(fa);
		}
		
		logger.info("Buto monitor started.....");
		logger.info("BUTO_HOME environment variable set to '" + ButoSettings.butoHomePath + "'");
		logger.info("JDK_HOME environment variable set to '" + ButoSettings.jdkHomePath + "'");
		
		// load plugins
		try {
			ClasspathUtils.addDirToClasspath(new File(ButoSettings.pluginDirPath));
		} catch (IOException e) {
			logger.error("Error loading plugins '" + e.getMessage() + "'");
			return;
		}
		
		final String testName = args[0];
		executionType = args[1];
		testMethodNameArray = args[2].split(";");
		testCount = testMethodNameArray.length;
		boolean showGui = Boolean.parseBoolean(args[4]);
		
		
		// Create data source object
		FileDataSource dataSource = (FileDataSource)DataSourceFactory.getFileDataSource(new File(ButoSettings.profileDirPath));
		
		// Load profile for test
		testProfile = dataSource.getProfile(testName);
	
		if(testProfile == null){
			
			if(executionType.equals("ButoTest")){
				logger.error("Profile does not exists cannot test.... profile name '" + testName + "'");
				return;
			}
			
			logger.info("Profile does not exist, creating new profile for test");
			
			testProfile = new Profile(testName, "", "", "", "");
			
			dataSource.saveProfile(testProfile);
		}
		
		profileDataFolder  = new File(
				ButoSettings.profileDirPath + File.separator + 
				testProfile.getName() + File.separator + 
				"data");
		
		if(executionType.equals("ButoTraining")){
			
			logger.info("Parsing parameters '" + args[3] + "'");
			trainingParameterMap = ParameterStringParser.parse(args[3]);
			
			String[] propertyArray;
			
			if(trainingParameterMap.containsKey("butoProperties")){
				propertyArray = trainingParameterMap.get("butoProperties").split(",");
			}else{
				propertyArray = new String[]{"memory_heap_used", "memory_heap_committed"};
			}
			
			for(String propertyName : propertyArray){
				
				ButoProperty butoProperty = ButoPropertyManager.getButoProperty(propertyName);
				
				if(butoProperty == null){
					logger.warn("Invalid property key '" + propertyName + "'");
					continue;
				}
				
				butoPropertyList.add(butoProperty);
			}
			
			if(!profileDataFolder.exists()){
				logger.error("Profile data folder does not exist '" + profileDataFolder + "'");
				return;
			}
			
			try{
				IOUtils.deleteDirectory(profileDataFolder);
			}catch(IOException e){
				logger.error("Error deleting old training data folder '" + e.getMessage() + "'");
				return;
			}
			
			if(!profileDataFolder.mkdir()){
				logger.error("Unable to create profile data folder '" + profileDataFolder + "'");
				return;
			}
			
		}else{
			
			if(!profileDataFolder.exists()){
				logger.error("Profile data folder does not exist '" + profileDataFolder + "'");
				return;
			}
			
			classificationResultFolder = new File(
					ButoSettings.profileDirPath + File.separator + 
					testProfile.getName() + File.separator + 
					"classification_results");
			
			if(classificationResultFolder.exists()){
				IOUtils.deleteDirectory(classificationResultFolder);
			}
			
			if(!classificationResultFolder.mkdir()){
				logger.error("Unable to create classification result folder '" + classificationResultFolder + "'");
				return;
			}
			
			deSerializeClassifiers();
			
			butoPropertyList = new ArrayList<ButoProperty>(classifierMap.get(testMethodNameArray[0]).getClassificationModelMap().keySet());
		}
		
		oracleList = new ArrayList<Oracle>();
		for(int index = 0; index < testCount; index++){
			oracleList.add(new Oracle("Test " + index, new ArrayList<TrainingRun>()));
		}
		
		if(showGui){
			gui = new MonitorGui(testName);
		}
		
		Thread thread = new Thread(){
			public void run(){
				try {
					monitor = new Monitor(testName);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
				
				while(running){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						logger.error(e.getMessage());
					}
				}
				
				
			}
		};
		thread.start();
	}
	
	public Monitor(String testName) throws Exception{
		
		debugPrint("Monitoring test '" + testName + "'");
		
		// Register MBean for communication to the client
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		try {
			ObjectName name = new ObjectName("nz.ac.massey.buto.unittest.monitor.Monitor:type=Monitor,id=1");
			mbs.registerMBean(this, name);
		} catch (Exception x) {
			logger.error("Registering mbean for monitoring failed - '" + x + "'");
			return;
		}
		
		// Create connection to the client jmx server
		try{
			connection = JMXUtils.getMBeanServerConnection("org.eclipse.jdt.internal.junit.runner.RemoteTestRunner");
		}catch(Exception ex){
			// Can't connect, print error and exit
			debugError("Error conencting to Buto client JMX server '" + ex.getMessage() + "'");
			System.exit(0);
		}
		
		
		// Create and start timeout thread
		timeoutThread = new JMXTimeoutThread(connection, 5000);
		timeoutThread.start();
	}

	@Override
	public void notify(String message) {
		
		switch(message){
		case"startMonitoring":
			
			break;
		case"stopMonitoring":
			
			debugPrint("Monitoring complete");

			if(executionType.equals("ButoTraining")){
				persistModels(oracleList);
			}else{
				outputResultGraphs(resultList);
				generateTestReport(resultList);
			}
			
			running = false;
			
			debugPrint("Execution complete, exiting...");
			
			System.exit(0);
			
			break;
		case"testSetup":
			
			debugPrint("Test setup");
			
			JMXCollectorConfiguration config = JMXCollectorConfigurationFactory.getNewConfig(butoPropertyList);
			
			jmxCollector = new JMXCollector(
					connection, 
					config, 
					new ArrayList<JMXCollectorListener>(), 
					new ArrayList<MonitoredMBean>(), 
					new TrainingRun());
			
			jmxCollector.start();
			debugPrint("Started jmx collector");
			break;
		case"testTearDown":

			jmxCollector.interrupt();

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {e.printStackTrace();}
			
			TrainingRun tr = jmxCollector.getTrainingRun();
			
			
			int oracleIndex = testIndex % testCount;
			
			oracleList.get(oracleIndex).getTrainingRunList().add(tr);
			
			testIndex++;
			
			jmxCollector = null;

			break;
		default:
			debugError("Invalid message '" + message + "'");
		}
	}

	@Override
	public boolean butoAssert() {
		debugPrint("Asserting.....");
		
		jmxCollector.interrupt();
		
		// Get current oracle
		int oracleIndex = testIndex % testCount;
		
		// Load serialized classifier
		Classifier classifier = classifierMap.get(testMethodNameArray[oracleIndex]);

		ClassificationResult classificationResult = classifier.classify(jmxCollector.getTrainingRun());
		
		resultList.add(classificationResult);
		
		debugPrint("Asserting ... result = " +  classificationResult.successful() );
		
		return classificationResult.successful();
	}

	private void debugPrint(String message ){
		logger.info(message);
		if(gui!=null){
			gui.appendText(message);
		}
	}
	
	
	private void debugError(String message ){
		logger.error(message);
		if(gui!=null){
			gui.appendText("ERRROR - " + message);
		}
	}
	private void persistModels(List<Oracle> oracleList){
		
		debugPrint("Persiting models....");
		
		
		TimePointGenerator timePointGenerator = null;
		ClassificationAlgorithm ca = null; 
		Map<String,String> caParams = null;
		DataSmoother smoother;
		int smoothingSize = 3;
		int jmxCollectionInterval = 1000;
		List<String> metaDataFilterList = new ArrayList<String>();

		//*************************************************************************
		// Time point generator
		
		if(trainingParameterMap.containsKey("timePointGenerator")){
			
			String name = trainingParameterMap.get("timePointGenerator");
			
			timePointGenerator = TimePointGeneratorManager.getTimePointGenerator(name);
			
			if(timePointGenerator == null){
				logger.warn("Invalid time-point generator '" + name + "'");
				timePointGenerator = TimePointGeneratorManager.getTimePointGenerator("nz.ac.massey.buto.analysis.timepoint.impl.LinearTimePointGenerator");
			}
		}else{
			timePointGenerator = TimePointGeneratorManager.getTimePointGenerator("nz.ac.massey.buto.analysis.timepoint.impl.LinearTimePointGenerator");
		}
		
		//*************************************************************************
		// Smoothing size
		
		if(trainingParameterMap.containsKey("smoothingSize")){
			
			String smoothingSizeStr = trainingParameterMap.get("smoothingSize");
			
			try{
				smoothingSize = Integer.parseInt(trainingParameterMap.get("smoothingSize"));
			}catch(NumberFormatException e){
				logger.error("Unable to parse smoothing size '" + smoothingSizeStr + "' using default size of 3." );
				smoothingSize = 3;
			}
		}else{
			smoothingSize = 3;
		}
		
		//*************************************************************************
		// Smoother
		
		if(trainingParameterMap.containsKey("smoother")){
			
			String name = trainingParameterMap.get("smoother");
				
			smoother = DataSmootherManager.getDataSmoother(name);
			
			if(smoother == null){
				logger.error("Unable to load smoother '" + name + "' using no smoother");
				smoother = DataSmootherManager.getDataSmoother("nz.ac.massey.buto.analysis.smoothing.impl.NoSmoother");
			}
			
		}else{
			smoother = DataSmootherManager.getDataSmoother("nz.ac.massey.buto.analysis.smoothing.impl.NoSmoother");
		}
		//dataSmootherList.add(DataSmootherManager.getDataSmoother("nz.ac.massey.buto.analysis.smoothing.impl.NoSmoother"));
		//smoother = DataSmootherManager.getDataSmoother("nz.ac.massey.buto.analysis.smoothing.impl.MeanSmoother");
		//paramSet.dataSmootherList.add(DataSmootherManager.getDataSmoother("nz.ac.massey.buto.analysis.smoothing.impl.MedianSmoother"));
	
		

		//*************************************************************************
		// Classification algorithm
		
		if(trainingParameterMap.containsKey("classificationAlgorithm")){
			
			if(!trainingParameterMap.containsKey("classificationAlgorithmParams")){
				
				logger.error("No parameters for classification algorithm provided using default algorithm and params");
				
				ca = ClassificationAlgorithmManager.getClassificationAlgorithm("nz.ac.massey.buto.analysis.algorithms.impl.SimpleClassifier");
				
				caParams = new HashMap<String, String>();
				caParams.put("stdev", "3");
				caParams.put("averageType", "mean");
			}else{
				String name = trainingParameterMap.get("classificationAlgorithm");
				String params = trainingParameterMap.get("classificationAlgorithmParams");
				
				// TODO
			}
		}else{
			ca = ClassificationAlgorithmManager.getClassificationAlgorithm("nz.ac.massey.buto.analysis.algorithms.impl.SimpleClassifier");
			
			caParams = new HashMap<String, String>();
			caParams.put("stdev", "3");
			caParams.put("averageType", "mean");
		}
		
		int oracleIndex = 0;
		for(Oracle oracle : oracleList){
			
			try {
				Classifier classifier = new Classifier(
						oracle.getTrainingRunList(), 
						butoPropertyList,
						timePointGenerator, 
						ca, 
						caParams, 
						ModelType.STANDARD,
						smoother, 
						smoothingSize, 
						jmxCollectionInterval,
						metaDataFilterList);
				
				String testName = testMethodNameArray[oracleIndex];
				
				File classifierSerialFile = new File(profileDataFolder + File.separator + testName + ".ser");
				
				debugPrint("Serializing classifier to file - '" + classifierSerialFile + "'");
				
				try{
					FileOutputStream fileOut = new FileOutputStream(classifierSerialFile);
					ObjectOutputStream out = new ObjectOutputStream(fileOut);
					out.writeObject(classifier);
					out.close();
					fileOut.close();
					
				}catch(IOException ex){
					debugError("Error serializing model - " + ex.getMessage());
				}
				
			} catch (Exception e){
				debugError("Error creating classifier - '" + e.getMessage() + "'");
			}
			oracleIndex++;
		}
	}
	
	private static void deSerializeClassifiers(){
		
		classifierMap = new HashMap<String, Classifier>();
		
		for(int index = 0; index < testMethodNameArray.length; index++){
			
			String testMethodName = testMethodNameArray[index] ;
			
			File classiferSerialFile = new File(profileDataFolder + File.separator + testMethodName + ".ser");
			
			// Load serialized classifier
			Classifier classifier;
			try{
				FileInputStream fileIn = new FileInputStream(classiferSerialFile);
				ObjectInputStream in = new ObjectInputStream(fileIn);
				
				classifier = (Classifier) in.readObject();
				
				logger.debug("Suceessfuly loaded classifier for " + testMethodName);
				
				fileIn.close();
				in.close();
				
				
				classifierMap.put(testMethodName, classifier);

			}catch(Exception ex){
				logger.error("Error deserializing classifier - " + ex.getMessage());
				return;
			};
		}
		
	}
	
	private void generateTestReport(List<ClassificationResult> resultList){
		
		File reportOutputFile = new File(classificationResultFolder + File.separator + "report.html");
		NewlineStringBuilder outputBuilder = new NewlineStringBuilder();
		
		//*************************************************************************
		// Opening tags
		outputBuilder.append("<!DOCTYPE html>");
		outputBuilder.append("<html>");
		//*************************************************************************
		// HEAD
		outputBuilder.append("<head>");
		outputBuilder.append("<title>Buto Report</title>");
		outputBuilder.append("<link type = 'text/css' rel='stylesheet' href='http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css' />");
		outputBuilder.append("<script type='text/javascript' src='http://ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js'></script>");
		outputBuilder.append("<script type='text/javascript' src='http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js'></script>");
		
		
		//******************************************************
		// JS
		outputBuilder.append("<script type ='text/javascript'><!--");
		outputBuilder.append("$(function(){$('#accordion').accordion({autoHeight:false,navigation:true,heightStyle: \"content\" });});");
		outputBuilder.append("$(function(){$('.tabs').tabs();});");
		outputBuilder.append("$(function(){$('.passed').addClass('status1')});");
		outputBuilder.append("$(function(){$('.failed').addClass('status2')});");
		outputBuilder.append("//--></script>");
		
		//******************************************************
		// CSS
		outputBuilder.append("<style type=\"text/css\">");
		outputBuilder.append("h1{text-align:center}");
		outputBuilder.append("h2{text-align:center}");
		outputBuilder.append("h3{text-align:center}");
		
		outputBuilder.append(".centerdiv{text-align: center;}");
		
		//outputBuilder.append(".ui-tabs .status1 {background: #B4FFAD;}");
		//outputBuilder.append(".ui-tabs .status2 {background: #FFADAD;}");
		
		outputBuilder.append(".status1 {background: #B4FFAD;}");
		outputBuilder.append(".status2 {background: #FFADAD;}");

		outputBuilder.append("</style>");
		
		outputBuilder.append("</head>");
		
		//*************************************************************************
		// BODY
		outputBuilder.append("<body>");
		
		outputBuilder.append("<h1>BUTO report</h1>");
		outputBuilder.append("<h2>Test class name - " + testProfile.getName() + "</h2>");
		
		//*************************************************************************
		// Test methods
		
		outputBuilder.append("<div id ='accordion'>");
		
		int index = 0;
		for(ClassificationResult result : resultList){
			String testName = testMethodNameArray[index];
			
			String classStr = "failed";
			if(result.successful()){classStr="passed";}
			
			outputBuilder.append("<h3 class='" +classStr + "'>" + testName + "</h3>");
			outputBuilder.append("<div>");
			
			/*if(result.successful()){
				outputBuilder.append("<h3>Passed</h3>");
			}else{
				outputBuilder.append("<h3>Failed</h3>");
			}*/
			
			Map<ButoProperty, ClassificationModelResult> resultMap = result.getClassificationResultMap();
			
			// Start tabs
			outputBuilder.append("<div  id='tabs-" + testName + "' class='tabs'>");
			outputBuilder.append("<ul>");
			
			for(ButoProperty property: resultMap.keySet()){
				
				ClassificationModelResult cmResult = resultMap.get(property);
				
				String classString = "passed";
				if(!cmResult.sucessful()){classString = "failed ";}
				
				outputBuilder.append("<li><a href='#tabs-" + testName + "-" + property.getKey() +"' class='" + classString +"'>" + property.getName() + "</a></li>");
			}
			outputBuilder.append("</ul>");
			
			// Process each property
			for(ButoProperty property : resultMap.keySet()){
				
				File graphPath = new File(
						classificationResultFolder + File.separator +
						testName + File.separator +
						property.getKey() + ".jpg");
				
				ClassificationModelResult cmResult = resultMap.get(property);
				
				String classString = "passed";
				if(!cmResult.sucessful()){classString = "failed ";}
				
				outputBuilder.append("<div id='tabs-" + testName + "-" + property.getKey() + "' class='" + classString +"'>");
				
				// Image
				outputBuilder.append("<div class='centerdiv'>");
				outputBuilder.append("<a href='" + graphPath + "' target='_blank'>");
				outputBuilder.append("<img src='" + graphPath + "' alt='error_loading_img' width='304' height='228'/>");
				outputBuilder.append("</a>");
				outputBuilder.append("</div>");
				
				// ERRORS
				List<Long> errorTimeStampList = cmResult.getErrorTimeStampList();
				
				if(errorTimeStampList.size() != 0){
					outputBuilder.append("<p>Error timestamps (ms)...</p>");
					outputBuilder.append("<p>");
					
					// Error time-points
					for(int i =0; i < errorTimeStampList.size(); i++){
						outputBuilder.append(errorTimeStampList.get(i).toString());
						
						if(i != errorTimeStampList.size() - 1){
							outputBuilder.append(", ");
						}
					}
					outputBuilder.append("</p>");
				}
				
				// WARNINGS 
				List<Long> warningTimeStampList = cmResult.getWarningTimeStampList();
				
				if(warningTimeStampList.size() != 0){
					outputBuilder.append("<p>Warning timestamps (ms)...</p>");
					outputBuilder.append("<p>");
					
					// Error time-points
					for(int i =0; i < warningTimeStampList.size(); i++){
						outputBuilder.append(warningTimeStampList.get(i).toString());
						
						if(i != warningTimeStampList.size() - 1){
							outputBuilder.append(", ");
						}
					}
					outputBuilder.append("</p>");
				}
				
				
				outputBuilder.append("</div>");
			}
			
			outputBuilder.append("</div>"); // close tabs
			outputBuilder.append("</div>"); // sub accordian
			
			index++;
		}
		outputBuilder.append("</div>");
		
		outputBuilder.append("</body>");
		outputBuilder.append("</html>");
		
		IOUtils.writeFile(reportOutputFile, outputBuilder.toString());
	}
	
	private void outputResultGraphs(List<ClassificationResult> resulList){
		
		int oracleIndex = 0;
		for(ClassificationResult classificationResult : resultList){
		
			
			File classificationOutputFolder = new File(
					classificationResultFolder + File.separator +
					testMethodNameArray[oracleIndex]);
			
			if(!classificationOutputFolder.mkdir()){
				debugError("Error making classification output folder '" + classificationOutputFolder + "'");
				return;
			}
			
			Map<ButoProperty, ClassificationModelResult> crMap = classificationResult.getClassificationResultMap();
		
			for(ButoProperty butoProperty : crMap.keySet()){
				
				File outputGraphFile = new File(
						classificationOutputFolder + File.separator + 
						butoProperty.getKey() + ".jpg");
				
				ClassificationModelResult modelResult = crMap.get(butoProperty);
				
				List<Bounds> boundsList = modelResult.getModel().getBounds();
				Map<Long, Number> testData = modelResult.getTestData();
				
				
				
				YIntervalSeries analysisSeries =  GraphingTools.toAnalysisSeries(boundsList);
				YIntervalSeries testDataSeries = GraphingTools.toAnalysisSeries(testData);
				YIntervalSeriesCollection seriesCollection = new YIntervalSeriesCollection();
				seriesCollection.addSeries(analysisSeries);
				seriesCollection.addSeries(testDataSeries);
				
				JFreeChart chart = ChartGenerator.createDeviationChart2(seriesCollection, butoProperty);
				
				debugPrint("Saving " + butoProperty.getName() + " chart.....");
				
				GraphIO.save(chart, outputGraphFile);
			}
			
			
			oracleIndex++;
		}
	}
	

	
}

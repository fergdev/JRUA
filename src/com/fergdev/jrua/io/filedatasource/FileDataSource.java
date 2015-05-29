package nz.ac.massey.buto.io.filedatasource;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import nz.ac.massey.buto.domain.ButoProperty;
import nz.ac.massey.buto.domain.MonitoredMBean;
import nz.ac.massey.buto.domain.Oracle;
import nz.ac.massey.buto.domain.ButoNotification;
import nz.ac.massey.buto.domain.Profile;
import nz.ac.massey.buto.domain.Snapshot;
import nz.ac.massey.buto.domain.TrainingRun;
import nz.ac.massey.buto.io.DataSource;
import nz.ac.massey.buto.io.DataSourceEvent;
import nz.ac.massey.buto.io.DataSourceListener;
import nz.ac.massey.buto.utils.IOUtils;

/**
 * DataSource implementation for accessing data stored on a file system.
 * 
 * @author Fergus Hewson
 * 
 */
public class FileDataSource implements DataSource {

	// Root folder
	// - PROFILE FOLDERS
	// --- profileSpec.data
	// --- monitoredMBeans.data
	// --- customDataTypes.data
	// --- ORACLE FOLDERS
	// ---- oracleSpec.data
	// ---- *many*trainingRunFile.data

	/**
	 * Log4j logger.
	 */
	private Logger logger = LogManager.getLogger(FileDataSource.class);
	
	/**
	 * The root folder of the data source.
	 */
	private File rootFolder;
	
	/**
	 * Listeners.
	 */
	private List<DataSourceListener> listeners = new ArrayList<DataSourceListener>();
	
	/**
	 * DateFormat object to process dates.
	 */
	private SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
	
	/**
	 * Creates a new FileDataSource using the rootFolder as the base directory
	 * for the data source.
	 * 
	 * @param rootFolder
	 *            Root folder of the data source.
	 */
	public FileDataSource(File rootFolder) {
		this.rootFolder = rootFolder;
	}

	@Override
	public String[] getProfileNames() {
		
		// Notify of start of operation
		notifyEvent(new DataSourceEvent(DataSourceEvent.GET_PROFILE_NAMES, DataSourceEvent.OPERATION_STARTED));
		
		// Get the profile names, list of all folders in the root directory
		// for this data source
		String[] profileNames = rootFolder.list();
		
		// Notify of completion of operation
		notifyEvent(new DataSourceEvent(DataSourceEvent.GET_PROFILE_NAMES, DataSourceEvent.OPERATION_COMPLETE));
	
		// Return profile names
		return profileNames;
	}
	
	@Override
	public String[] getOracleNames(Profile profile) {
		
		// Notify of start of operation
		notifyEvent(new DataSourceEvent(DataSourceEvent.GET_ORACLE_NAMES, DataSourceEvent.OPERATION_STARTED));
		
		// Get the profile folder
		File oracleFolder = new File(rootFolder + File.separator + profile.getName() + File.separator + "oracles");
		
		// check the folder exists
		if (!oracleFolder.exists()) {
			
			// log error
			logger.error("Error getting oracle names, folder does not exist. " + oracleFolder);
			
			// notify listeners of error
			notifyEvent(new DataSourceEvent(DataSourceEvent.GET_ORACLE_NAMES, DataSourceEvent.OPERATION_ERROR));
			
			// return null object since no profile could be loaded
			return null;
		}
		
		// Get the list of oracle names from the oracle folder
		String[] oracleNames = oracleFolder.list();
		
		// Notify of completion of operation
		notifyEvent(new DataSourceEvent(DataSourceEvent.GET_ORACLE_NAMES, DataSourceEvent.OPERATION_COMPLETE));
		
		// Return the list of available oracles
		return oracleNames;
	}

	@Override
	public Profile getProfile(String profileName) {

		// Notify of start of operation
		notifyEvent(new DataSourceEvent(DataSourceEvent.GET_PROFILE, DataSourceEvent.OPERATION_STARTED));
		
		// Get the profile folder
		File profileFolder = new File(rootFolder + File.separator + profileName);

		// check the folder exists
		if (!profileFolder.exists()) {
			
			// log error
			logger.error("Error loading profile, folder does not exist.");
			
			// notify listeners of error
			notifyEvent(new DataSourceEvent(DataSourceEvent.GET_PROFILE, DataSourceEvent.OPERATION_ERROR));
			
			// return null
			return null;
		}

		// get profile data file
		File profileDataFile = new File(profileFolder + File.separator+ "profile.data");

		// check the file exists
		if (!profileDataFile.exists()) {
			
			// log error
			logger.error("Profile data file does not exist.");
			
			// notify listeners of error
			notifyEvent(new DataSourceEvent(DataSourceEvent.GET_PROFILE, DataSourceEvent.OPERATION_ERROR));
			
			// return null
			return null;
		}
		// ******************************************************************************************
		// Process profile data file
		List<String> lines = IOUtils.readFile(profileDataFile);

		String[] line0 = lines.get(0).split("=");
		String[] line1 = lines.get(1).split("=");
		String[] line2 = lines.get(2).split("=");
		String[] line3 = lines.get(3).split("=");
		String[] line4 = lines.get(4).split("=");

		String fileProfileName = line0[1];
		String profileClasspath = "";
		String profileMainClass = "";
		String profileDefaultArgs = "";
		String defaultOptions = "";
		
		// test for null
		if (line1.length == 2)
			profileClasspath = line1[1];

		if (line2.length == 2)
			profileMainClass = line2[1];
		
		if (line3.length == 2)
			profileDefaultArgs = line3[1];
		
		if (line4.length == 2)
			defaultOptions = line4[1];

		// ******************************************************************************************
		// Monitored MBeans
		File mbeanDataFile = new File(profileFolder + File.separator + "monitoredMBeans.data");

		List<MonitoredMBean> monitoredMBeans = readMonitoredMBeanFile(mbeanDataFile);

		// ******************************************************************************************
		// Data Types
		File dataTypeFile = new File(profileFolder + File.separator + "customDataTypes.data");

		List<ButoProperty> customDataTypes = readCustomDataTypeFile(dataTypeFile);

		// *********************************************
		// Oracles
		File oracleFolder = new File(profileFolder + File.separator + "oracles");

		// list of oracles
		List<Oracle> oracleList = new ArrayList<Oracle>();

		// Test oracle folder
		if (oracleFolder.exists()) {

			// load the oracles
			for (String fileName : oracleFolder.list()) {

				oracleList.add(getOracle(new File(oracleFolder+ File.separator + fileName)));
			}
		}

		// ******************************************************************************************
		// Create new profile
		Profile outputProfile = new Profile(
				fileProfileName, 
				profileClasspath, 
				profileMainClass,
				profileDefaultArgs, 
				defaultOptions, 
				oracleList,
				monitoredMBeans, 
				customDataTypes
				);
		

		// Notify of end of operation
		notifyEvent(new DataSourceEvent(DataSourceEvent.GET_PROFILE, DataSourceEvent.OPERATION_COMPLETE));
		
		// return the output profile
		return outputProfile;
	}

	/**
	 * Loads a MonitoredMBean data file.
	 * 
	 * @param dataFile  The file to load.
	 * @return List of MonitoredMBeans store in the dataFile.
	 */
	private List<MonitoredMBean> readMonitoredMBeanFile(File dataFile) {

		// Output list
		List<MonitoredMBean> monitoredMBeans = new ArrayList<MonitoredMBean>();

		// Read the file
		List<String> lines = IOUtils.readFile(dataFile);

		// Check for custom mbean tag
		String header = lines.get(0);
		
		if(!header.equals("#MonitoredMBeans")) {
			
			// Log error
			logger.error("Expected #MonitoredMBeans tag");
			return null;
		}

		int lineIndex = 1;
		// Load MonitoredMBeans
		while (lineIndex < lines.size()) {
			
			String line = lines.get(lineIndex);
			
			if (!line.equals("{")) {
				logger.error("Start of MBean expected.");
				return null;
			}

			// *********************************************************
			// Get the object name
			lineIndex++;
			line = lines.get(lineIndex);

			String objectName = line.split("=", 2)[1];

			// Get collection notifications
			lineIndex++;
			line = lines.get(lineIndex);

			String collectNotifications = line.split("=")[1];

			// *********************************************************
			// Attributes
			lineIndex++;
			line = lines.get(lineIndex);

			if (!line.equals("{")) {
				logger.error("Start of attrbutes expected.");
				return null;
			}

			lineIndex++;
			line = lines.get(lineIndex);

			List<String> attributeList = new ArrayList<String>();

			while (!line.equals("}")) {

				attributeList.add(line);

				lineIndex++;
				line = lines.get(lineIndex);
			}

			if (!line.equals("}")) {
				logger.error("End of MBean expected.");
				return null;
			}

			// *********************************************************
			// Meta attributes
			lineIndex++;
			line = lines.get(lineIndex);

			if (!line.equals("{")) {
				logger.error("Start of meta attrbutes expected.");
				return null;
			}

			lineIndex++;
			line = lines.get(lineIndex);

			List<String> metaAttributeList = new ArrayList<String>();

			while (!line.equals("}")) {

				metaAttributeList.add(line);

				lineIndex++;
				line = lines.get(lineIndex);
			}

			if (!line.equals("}")) {
				logger.error("End of meta attrbutes expected.");
				return null;
			}

			// *********************************************************
			lineIndex++;
			line = lines.get(lineIndex);

			monitoredMBeans.add(
					new MonitoredMBean(
							objectName, 
							attributeList.toArray(new String[attributeList.size()]),
							metaAttributeList.toArray(new String[attributeList.size()]), 
							Boolean.parseBoolean(collectNotifications))
					);

			if (!line.equals("}")) {
				logger.error("End of MBean expected.");
				return null;
			}

			lineIndex++;
			if (lineIndex == lines.size()) {
				break;
			}
			line = lines.get(lineIndex);
		}

		// return list
		return monitoredMBeans;
	}

	/**
	 * Loads custom DataType objects stored in a data file.
	 * 
	 * @param dataFile
	 *            File to load from.
	 * @return List of custom DataType objects.
	 */
	private List<ButoProperty> readCustomDataTypeFile(File dataFile) {

		List<ButoProperty> customDataTypes = new ArrayList<ButoProperty>();

		// Read the file into a list of lines
		List<String> lines = IOUtils.readFile(dataFile);

		// Read custom data Types
		// Check for CustomDataTypes tag
		if (!lines.get(0).equals("#CustomDataTypes")) {
			logger.error("Expected CustomDataTypes tag");
			return null;
		}

		int lineIndex = 1;

		while (lineIndex < lines.size()) {

			String line = lines.get(lineIndex);

			if (!line.equals("{")) {
				logger.error("Expected start of custom MBean");
				return null;
			}

			lineIndex++;

			String keyLine = lines.get(lineIndex++);
			String nameLine = lines.get(lineIndex++);
			String typeLine = lines.get(lineIndex++);
			String unitsLine = lines.get(lineIndex++);
			String upperBoundLine = lines.get(lineIndex++);
			String lowerBoundLine = lines.get(lineIndex++);

			String key = keyLine.split("=")[1];
			String dataTypeName = nameLine.split("=")[1];
			String type = typeLine.split("=")[1];
			String units = unitsLine.split("=")[1];

			NumberFormat format = NumberFormat.getInstance();

			Number upperBound;
			Number lowerBound;
			try {
				upperBound = format.parse(upperBoundLine.split("=")[1]);
				lowerBound = format.parse(lowerBoundLine.split("=")[1]);
			} catch (ParseException e) {
				System.err
						.println("Error parsing bounds for custom data type.");
				return null;
			}

			customDataTypes.add(new ButoProperty(key, dataTypeName, units, type,lowerBound, upperBound));

			line = lines.get(lineIndex);

			if (!line.equals("}")) {
				logger.error("Expected start of custom MBean");
				return null;
			}

			lineIndex++;
		}
		
		// Return list
		return customDataTypes;
	}

	/**
	 * Creates an oracle object from the
	 * 
	 * @param oracleFolder
	 * @return An oracle.
	 */
	public Oracle getOracle(File oracleFolder){

		// check file exists
		if(!oracleFolder.exists()){
			logger.error("Error loading orcale, folder does not exist.");
			logger.error(oracleFolder);
			return null;
		}
		
		// load the oracle data file
		File oracleDataFile = new File(oracleFolder + File.separator + "oracle.data");
		
		// Check file exists 
		if(!oracleDataFile.exists()){
			logger.error("Error loading oracle, data file does not exist.");
			logger.error(oracleDataFile);
			return null;
		}
		
		// read the file
		List<String> lines = IOUtils.readFile(oracleDataFile);
		
		String[] line0 = lines.get(0).split("=");
		
		String oracleName = line0[1];
		
		List<TrainingRun> trainingRunList = new ArrayList<TrainingRun>();
		
		// Filter for oracle data file
		FilenameFilter filter = new FilenameFilter() {
			
			public boolean accept(File directory, String fileName) {
				return !fileName.equals("oracle.data");
			}
		};
		
		// load the oracles
		for(String fileName : oracleFolder.list(filter)){
			
			// load the training run
			trainingRunList.add(readTrainingRunFile(new File(oracleFolder + File.separator + fileName)));
		}		
		
		// Return oracle
		return new Oracle(oracleName, trainingRunList);
	}

	/**
	 * Loads a TrainingRun from a training run data file.
	 * 
	 * @param trainingRunDataFile File to load training run from.
	 * @return Training run object.
	 */
	private TrainingRun readTrainingRunFile(File trainingRunDataFile) {

		// check the file exists
		if (!trainingRunDataFile.exists()) {
			logger.error("Error loading training run. File doesn't exist.");
			return null;
		}

		// read the file
		List<String> lines = IOUtils.readFile(trainingRunDataFile);

		//*************************************************************
		// READ INFO
		if (!lines.get(0).equals("#Info")) {
			logger.error("Error reading training run, no #Info tag, " + trainingRunDataFile);
			return null;
		}

		// Get the info
		String[] dateLine = lines.get(1).split("=");
		String[] exitvalLine = lines.get(2).split("=");

		String dateString = dateLine[1];
		String exitValLine = exitvalLine[1];

		Date date;
		try {
			date = dateFormat.parse(dateString);
		} catch (ParseException e) {
			logger.error(e.getMessage());
			date = new Date();
		}

		TrainingRun trainingRun = new TrainingRun(date, Integer.parseInt(exitValLine));

		//*************************************************************
		// READ META DATA
		int index = 3;

		if (!lines.get(index).equals("#MetaData")) {
			logger.error("Error reading training run, can't find meta data tag " + trainingRunDataFile);
			return null;
		}

		index++;
		String line = lines.get(index);

		// Meta-data map
		Map<String, Object> metaData = trainingRun.getMetaData();

		// iterate till end of meta data
		while (!line.equals("#Snapshots")) {

			String[] lineSplit = line.split("=", 3);

			if (lineSplit.length == 2) {
				// Empty string?
				metaData.put(lineSplit[0], "");
				
			} else if (lineSplit.length == 3) {
				
				// line data
				String key = decodeString(lineSplit[0]);
				String dataType = lineSplit[1];
				String stringValue = decodeString(lineSplit[2]);
				
				// Object to add to map
				Object objectValue = null;
				
				// Switch for the data type
				switch(dataType){
				case"java.lang.Boolean":
					objectValue = Boolean.parseBoolean(stringValue);
					break;
				case"java.lang.Double":
					objectValue = Double.parseDouble(stringValue);
					break;
				case"java.lang.Integer":
					objectValue = Integer.parseInt(stringValue);
					break;
				case"java.lang.Long":
					objectValue = Long.parseLong(stringValue);
					break;
				case"java.lang.String":
					objectValue = stringValue;
					break;
				default:
					logger.error("Invalid meta data type - " + dataType);
					//index++;
					line = lines.get(++index);
					continue;
				}
				
				// add the object
				metaData.put(key, objectValue);
				
			} else {
				//logger.error("Invalid line");
				//logger.error("Line " + index + " - " + line);
			}

			// incremenet line
			line = lines.get(++index);
		}

		//*************************************************************
		// SNAPSHOTS
		if (!line.equals("#Snapshots")) {
			logger.error("Error reading training run, can't find snapshots tag.");
			logger.error("Line - " + line);
			logger.error("File - " + trainingRunDataFile);
			return null;
		}

		//index++;
		line = lines.get(++index);

		while (!line.equals("#Notifications")) {

			if (!line.equals("{")) {
				logger.error("Start of snapshot expectd.");
				logger.error("Line " + index + " - " + line);
				logger.error("File - " + trainingRunDataFile);
				return null;
			}

			//index++;
			line = lines.get(++index);

			// Get the uptime
			String[] uptimeLine = line.split("=");
			long uptime = Long.parseLong(uptimeLine[1]);

			// Create the snapshot
			Snapshot snap = new Snapshot(uptime);
			trainingRun.addSnapshot(snap);

			//index++;
			line = lines.get(++index);

			// Add snapshot data
			while (!line.equals("}")) {
				
				// split the line
				String[] lineSplit = line.split("=");

				// get data from line
				String key = decodeString(lineSplit[0]);
				String dataType = lineSplit[1];
				String stringValue = decodeString(lineSplit[2]);
				
				// converted object
				Object objectValue;

				// switch to process different data types
				switch (dataType) {
				case"java.lang.Boolean":
					objectValue = Boolean.parseBoolean(stringValue);
					break;
				case"java.lang.Double":
					objectValue = Double.parseDouble(stringValue);
					break;
				case"java.lang.Integer":
					objectValue = Integer.parseInt(stringValue);
					break;
				case"java.lang.Long":
					objectValue = Long.parseLong(stringValue);
					break;
				case"java.lang.String":
					objectValue = stringValue;
					break;

				default:
					// Error
					logger.error("Invalid snapshot data-type '" + dataType + "' " + line);
					line = lines.get(++index);
					continue;
				}

				// add to snapshot
				snap.addData(key, objectValue);

				//index++;
				line = lines.get(++index);
			}

			if (!line.equals("}")) {
				logger.error("End of snapshot expectd.");
				logger.error("Line " + index + " - " + line);
				logger.error("File - " + trainingRunDataFile);
				return null;
			}

			//index++;
			line = lines.get(++index);
		}

		//*************************************************************
		// NOTIFICATIONS
		
		if (!line.equals("#Notifications")) {
			logger.error("Error reading training run, can't find notifications tag.");
			logger.error("Line - " + line);
			logger.error("File - " + trainingRunDataFile);
			return null;
		}

		index++;
		
		// iterate to end of file
		while(index < lines.size()){
			
			// get the line
			line = lines.get(index);
			
			// decode the line
			String decodedLine = decodeString(line);
			
			// Split the line
			String[] lineSplit = decodedLine.split("--");
			
			String notificationType = lineSplit[0];
			String sourceObjectName = lineSplit[1];
			long sequenceNumber = Long.parseLong(lineSplit[2]);
			long timesStamp = Long.parseLong(lineSplit[3]);
			String message = lineSplit[4];
			
			// add new notification
			trainingRun.getNotifications().add(new ButoNotification(notificationType, sourceObjectName, sequenceNumber,timesStamp,message));
		
			index++;
		}
		
		
		return trainingRun;
	}

	@Override
	public void saveProfile(Profile profile) {

		// Notify of start of operation
		notifyEvent(new DataSourceEvent(DataSourceEvent.SAVE_PROFILE, DataSourceEvent.OPERATION_STARTED));
		
		// Root folder
		File profileFolder = new File(rootFolder + File.separator + profile.getName());
		File dataFolder = new File(profileFolder + File.separator + "data");
		File profileData = new File(profileFolder + File.separator + "profile.data");		
		File monitoredMBeansFile = new File(profileFolder + File.separator + "monitoredMBeans.data");
		File customDataTypesFile = new File(profileFolder + File.separator + "customDataTypes.data");
		
		// Test root folder exists
		if(!profileFolder.exists()){
			
			// make folder
			if(!profileFolder.mkdir()){
				logger.error("Error creating profile folder. " + profileFolder);
				return;
			}
			
		}else{
			
			// Delete the data file
			if (!profileData.delete()) {
				logger.error("Unable to delete old profile data.");
			}			
		}
		
		// Make data folder
		if(!dataFolder.mkdir()){
			logger.error("Unable to create data folder for new profile '" + dataFolder + "'");
			return;
		}
		
		// String builder for data file
		StringBuilder dataStringBuilder = new StringBuilder();
		
		// Build string
		dataStringBuilder.append("name=" + profile.getName() + "\n");
		dataStringBuilder.append("classpath=" + profile.getClassPath() + "\n");
		dataStringBuilder.append("mainClass=" + profile.getMainClass() + "\n");
		dataStringBuilder.append("defaultArgs=" + profile.getDefaultArgs() + "\n");
		dataStringBuilder.append("defaultOptions=" + profile.getDefaultOptions());
		
		// Write the data file
		IOUtils.writeFile(profileData, dataStringBuilder.toString());
		
		//*************************************************************
		// Monitored MBeans
		writeMonitoredMBeansFile(monitoredMBeansFile, profile.getMonitoredMBeans());
		
		//*************************************************************
		// Custom Data Types
		writeCustomDataTypeFile(customDataTypesFile, profile.getCustomDataTypes());
		
		// Notify of end of operation
		notifyEvent(new DataSourceEvent(DataSourceEvent.SAVE_PROFILE, DataSourceEvent.OPERATION_COMPLETE));
		
	}
	
	/**
	 * Creates the monitoredMBeans.data file.
	 * 
	 * @param outputFile The file to write to.
	 * @param monitoredMBeans The Beans to write.
	 */
	private void writeMonitoredMBeansFile(File outputFile, List<MonitoredMBean> monitoredMBeans){
		
		// Output string builder
		StringBuilder outputStringBuilder = new StringBuilder();
		
		// Start tag
		outputStringBuilder.append("#MonitoredMBeans\n");

		// monitored MBeans
		for (MonitoredMBean bean : monitoredMBeans) {

			// open mbean
			outputStringBuilder.append("{\n");

			// Save object name and process notifications
			outputStringBuilder.append("objectName=" + bean.getObjectName() + "\n");
			outputStringBuilder.append("monitorNotifications=" + bean.monitorNotifications() + "\n");

			// Monitored attributes
			outputStringBuilder.append("{\n");
			for (String attribute : bean.getMonitoredAttributes()) {
				outputStringBuilder.append(attribute + "\n");
			}
			outputStringBuilder.append("}");

			// monitored meta attributes
			outputStringBuilder.append("\n{\n");
			for (String metaAttribute : bean.getMonitoredMetaAttributes()) {
				outputStringBuilder.append(metaAttribute + "\n");
			}
			outputStringBuilder.append("}\n");

			// close Mbean
			outputStringBuilder.append("}\n");
		}
		
		// Write the file
		IOUtils.writeFile(outputFile, outputStringBuilder.toString());
	}
	
	/**
	 * Writes the customDataTypes.data file.
	 * 
	 * @param outputFile The file to write to.
	 * @param customDataTypes The DataTypes to write.
	 */
	private void writeCustomDataTypeFile(File outputFile, List<ButoProperty> customDataTypes){
		
		// File string builder
		StringBuilder outputStringBuilder = new StringBuilder();
		
		outputStringBuilder.append("#CustomDataTypes\n");
		
		// Custom data types
		for (ButoProperty dataType : customDataTypes) {

			// start tag
			outputStringBuilder.append("{\n");

			// Key value data pairs
			outputStringBuilder.append("key=" + dataType.getKey() + "\n");
			outputStringBuilder.append("name=" + dataType.getName() + "\n");
			outputStringBuilder.append("type=" + dataType.getType() + "\n");
			outputStringBuilder.append("units=" + dataType.getUnits()+ "\n");
			outputStringBuilder.append("upperBound=" + dataType.getUpperBound() + "\n");
			outputStringBuilder.append("lowerBound="+ dataType.getLowerBound() + "\n");

			// end tag
			outputStringBuilder.append("}\n");
		}
		
		// Write the file
		IOUtils.writeFile(outputFile, outputStringBuilder.toString());
	}

	/**
	 * FOR EVALUATION RUNNERS
	 * @param oracle
	 * @param oracleFolder
	 */
	public void saveOracle(Oracle oracle, File oracleFolder) {

		// check folder does not exist
		if (oracleFolder.exists()) {

			logger.error("Oracle folder already exists, " + oracle.getName());

			// try to create valid path
			String path = oracleFolder.getAbsolutePath();
			int index = 0;

			// to name oracle folder path(index)
			while (index < 100) {
				String tempPath = path + "(" + index + ")";

				File tempFile = new File(tempPath);

				// Valid filename found
				if (!tempFile.exists()) {

					oracleFolder = tempFile;

					break;
				}

				index++;
			}

			// valid filename could not be created
			if (index == 100) {
				logger.error("Cannot save oracle " + oracle.getName());
				return;
			}
		}

		// Create the orcale folder
		if (!oracleFolder.mkdir()) {
			logger.error("Error creating oracle folder. " + oracleFolder);
			return;
		}

		// create oracle data file
		File oracleDataFile = new File(oracleFolder + File.separator + "oracle.data");
		
		// Create text for file
		String text = "name=" + oracle.getName();
		
		// Write the file
		IOUtils.writeFile(oracleDataFile, text);

		// Save the training runs
		List<TrainingRun> trainingRuns = oracle.getTrainingRunList();
		
 		for(int index = 0; index < trainingRuns.size(); index++){
 			
 			// File to save to
 			File trainingRunFile = new File(oracleFolder + File.separator + "training_run_" + index + ".data");
 			
 			// Save training run
 			writeTrainingRunFile(trainingRunFile, trainingRuns.get(index));
 		}
	}

	/**
	 * Saves a training run object to a file.
	 * 
	 * @param outputFile File to save to.
	 * @param trainingRun Object to save.
	 */
	private void writeTrainingRunFile(File outputFile, TrainingRun trainingRun) {

		// check if the file exists
		if (outputFile.exists()) {
			logger.error("Error creating training run file, file already exists.");
			return;
		}
		
		// String builder for the output data
		StringBuilder outputStringBuilder = new StringBuilder();
		
		//*************************************************************
		// Write info
		outputStringBuilder.append("#Info\n");
		outputStringBuilder.append("date=" + dateFormat.format(trainingRun.getDate()) + "\n");
		outputStringBuilder.append("exitval=" + trainingRun.getExitVal() + "\n");

		//*************************************************************
		// WRITE meta-data
		outputStringBuilder.append("#MetaData\n");
		
		Map<String, Object> metaData = trainingRun.getMetaData();

		for (String key : metaData.keySet()) {
			
			// Encode the string
			String encodedKey = encodeString(key);
			
			// Get the value
			Object value = metaData.get(key);
			
			// Get the data type
			String dataType = value.getClass().getName();			
			
			// Create output string
			String valueString = encodeString(value.toString());

			// Append new line
			outputStringBuilder.append(encodedKey + "=" + dataType + "=" + valueString + "\n");
		}

		//*************************************************************
		// WRITE snapshots
		outputStringBuilder.append("#Snapshots\n");

		for (Snapshot snapshot : trainingRun.getSnapshotList()) {
			
			outputStringBuilder.append("{\n");

			outputStringBuilder.append("uptime=" + snapshot.getUptime() + "\n");

			for (String key : snapshot.keys()) {
				
				// Encode the string
				String encodedKey = encodeString(key);

				Object value = snapshot.getObject(key);

				outputStringBuilder.append(encodedKey + "=" + value.getClass().getName() + "=" + encodeString(value.toString()));
				outputStringBuilder.append("\n");
			}

			outputStringBuilder.append("}\n");
		}

		//*************************************************************
		// WRITE notifications
		outputStringBuilder.append("#Notifications\n");
		
		for (ButoNotification notification : trainingRun.getNotifications()) {
			
			String line = notification.getType() + "--" +
					notification.getSourceObjectName() + "--" +
					notification.getSequenceNumber() + "--" +
					notification.getTimeStamp() + "--" +
					notification.getMessage();
			
			// Encode the line
			String encodedLine = encodeString(line);
			
			outputStringBuilder.append(encodedLine+ "\n");
		}
		
		// write file
		IOUtils.writeFile(outputFile, outputStringBuilder.toString());
	}

	@Override
	public void deleteProfile(Profile profile) {
		
		// Notify of start of operation
		notifyEvent(new DataSourceEvent(DataSourceEvent.DELETE_PROFILE, DataSourceEvent.OPERATION_STARTED));
		
		// get profile folder file
		File profileFolderFile = new File(rootFolder.getPath() + File.separator + profile.getName());

		// check folder exists
		if (!profileFolderFile.exists()) {
			logger.error("Error deleting profile, fodler does not exist.");
			return;
		}

		// delete profile
		try {
			IOUtils.deleteDirectory(profileFolderFile);
		} catch (IOException e) {
			logger.error("Error deleting profile.");
			logger.error(e.getMessage());
		}
		
		// Notify of end of operation
		notifyEvent(new DataSourceEvent(DataSourceEvent.DELETE_PROFILE, DataSourceEvent.OPERATION_COMPLETE));
	}

	@Override
	public void deleteOracle(Profile profile, Oracle oracle) {
		
		// Notify of start of operation
		notifyEvent(new DataSourceEvent(DataSourceEvent.DELETE_ORACLE, DataSourceEvent.OPERATION_STARTED));
		
		// get the oracle folder
		File oracleFolder = new File(rootFolder.getPath() + File.separator + profile.getName() + File.separator + oracle.getName());

		// check folder exists
		if (!oracleFolder.exists()) {

			logger.error("Error deleting oracle, oracle folder does not exist.");
			return;
		}

		// delete oracle
		try {
			IOUtils.deleteDirectory(oracleFolder);
		} catch (IOException e) {
			logger.error("Error deleting oracle.");
			logger.error(e.getMessage());
		}
		
		// Notify of start of operation
		notifyEvent(new DataSourceEvent(DataSourceEvent.DELETE_ORACLE, DataSourceEvent.OPERATION_COMPLETE));
		

	}

	@Override
	public Oracle getOracle(Profile profile, String oracleName) {
		
		// notify listeners of operation stated
		notifyEvent(new DataSourceEvent(DataSourceEvent.OPERATION_STARTED, DataSourceEvent.GET_ORACLE));
		
		// Create oracle folder
		File oracleFolder = new File(rootFolder + File.separator + profile.getName() + File.separator + oracleName);
		
		Oracle outputOracle = getOracle(oracleFolder);
		
		// notify listeners of operation stated
		notifyEvent(new DataSourceEvent(DataSourceEvent.OPERATION_COMPLETE, DataSourceEvent.GET_ORACLE));
		
		// use load oracle
		return outputOracle;
	}

	@Override
	public void saveOracle(Profile profile, Oracle oracle) {
		
		// notify listeners of operation stated
		notifyEvent(new DataSourceEvent(DataSourceEvent.OPERATION_STARTED, DataSourceEvent.SAVE_ORACLE));
		
		// Create oracle folder
		File oracleFolder = new File(rootFolder + File.separator + profile.getName() + File.separator + oracle.getName());
		
		// Save the oracle
		saveOracle(oracle, oracleFolder);
		
		// notify listeners of operation stated
		notifyEvent(new DataSourceEvent(DataSourceEvent.OPERATION_COMPLETE, DataSourceEvent.SAVE_ORACLE));
	}
	
	/**
	 * Encodes a string for storage, replaces new line characters with alternate string sequences.
	 * 
	 * @param rawString The string to encode.
	 * @return An encoded string that is ready for storage,
	 */
	private String encodeString(String rawString){

		String outputString = rawString;
		
		outputString = outputString.replace("\n", "%n%");
		outputString = outputString.replace("\r", "%r%");
		outputString = outputString.replace("=", "%__%");
		
		return outputString;
	}
	
	/**
	 * Decodes a string that has been encoded, replaces alternate string sequences with the correct counter part.
	 * @param encodedString The encoded string to decode.
	 * @return A decoded string.
	 */
	private String decodeString(String encodedString){
		
		String outputString = encodedString;
		outputString = outputString.replace("%n%", "\n");
		outputString = outputString.replace("%r%", "\r");
		outputString = outputString.replace("%__%", "=");
		
		return outputString;
	}
	

	/**
	 * Notifies the listeners of an event.
	 * 
	 * @param event The event to notify notify listeners of.
	 */
	private void notifyEvent(DataSourceEvent event){
		
		// Iterate over the listeners and notify the, of the event
		for(DataSourceListener listener : listeners){
			listener.processDataSourceEvent(event);
		}
	}

	@Override
	public void addDataSourceProgressListener(DataSourceListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeDataSourceProgressListener(DataSourceListener listener) {
		listeners.remove(listener);
	}


}

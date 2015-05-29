package nz.ac.massey.buto.io.filedatasource;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nz.ac.massey.buto.domain.ButoProperty;
import nz.ac.massey.buto.domain.MonitoredMBean;
import nz.ac.massey.buto.domain.Oracle;
import nz.ac.massey.buto.domain.ButoNotification;
import nz.ac.massey.buto.domain.Profile;
import nz.ac.massey.buto.domain.Snapshot;
import nz.ac.massey.buto.domain.TrainingRun;
import nz.ac.massey.buto.io.DataSource;
import nz.ac.massey.buto.io.DataSourceFactory;
import nz.ac.massey.buto.utils.IOUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the FileDataSource object.
 *  
 * @author Fergus Hewson
 *
 */
public class FileDataSourceTest {

	private List<Profile> profileList;
	private DataSource fileDataSource;
	private File rootFolder;
	
	@Before
	public void setup(){
		
		rootFolder = new File("TestFolder");
		
		fileDataSource = DataSourceFactory.getFileDataSource(rootFolder);
		
		profileList = new ArrayList<Profile>();
		
		// ********************************************************
		// Profiles
		
		// List of oracles
		List<Oracle> oracleList = new ArrayList<Oracle>();
		
		List<TrainingRun> trainingRunList = new ArrayList<TrainingRun>(); 
		
		// Meta data
		Map<String, Object> metaData = new HashMap<String, Object>();
		metaData.put("key1", "value1");
		metaData.put("key2", 20);
		metaData.put("key3", 30.0d);
		
		// Snapshot list
		List<Snapshot> snapshotList = new ArrayList<Snapshot>();
		
		Snapshot snapshot1 = new Snapshot(1000);
		snapshotList.add(snapshot1);
		
		snapshot1.addData("booleanA1", true);
		snapshot1.addData("booleanA2", false);
		snapshot1.addData("doubleA1", 1.0d);
		snapshot1.addData("doubleA2", 2.0d);
		snapshot1.addData("integerA1", 1);
		snapshot1.addData("integerA2", 2);
		snapshot1.addData("longA1", 1L);
		snapshot1.addData("longA2", 2L);
		snapshot1.addData("stringA1", "stringVA1");
		snapshot1.addData("stringA2", "stringVA2");
		
		Snapshot snapshot2 = new Snapshot(2000);
		snapshotList.add(snapshot2);
		
		snapshot2.addData("booleanB1", true);
		snapshot2.addData("booleanB2", false);
		snapshot2.addData("doubleB1", 1.0d);
		snapshot2.addData("doubleB2", 2.0d);
		snapshot2.addData("integerB1", 1);
		snapshot2.addData("integerB2", 2);
		snapshot2.addData("longB1", 1L);
		snapshot2.addData("longB2", 2L);
		snapshot2.addData("stringB1", "stringVB1");
		snapshot2.addData("stringB2", "stringVB2");
		
		// Notifications
		List<ButoNotification> notifications = new ArrayList<ButoNotification>();
		notifications.add(new ButoNotification("Type 1", "Object 1", 1000, 1000, "hello world"));
		notifications.add(new ButoNotification("Type 2", "Object 2", 2000, 2000, "hello world"));
		
		// create oracle
		trainingRunList.add(new TrainingRun(new Date(), 1, metaData, snapshotList, notifications));
		oracleList.add(new Oracle("Oracle 1", trainingRunList));
		
		// Mbeans
		List<MonitoredMBean> mbeanList = new ArrayList<MonitoredMBean>();
		mbeanList.add(new MonitoredMBean("objectName1", new String[]{"att1A", "att1B"}, new String[]{"metaAtt1A", "metaAtt1B"}, true));
		mbeanList.add(new MonitoredMBean("objectName2", new String[]{"att2A", "att2B"}, new String[]{"metaAtt2A", "metaAtt2B"}, false));
		
		// Custom data types
		List<ButoProperty> customDataTypeList = new ArrayList<ButoProperty>();		
		customDataTypeList.add(new ButoProperty("key1", "name1", "units1", "type1", 10, 100));
		customDataTypeList.add(new ButoProperty("key2", "name2", "units2", "type2", 20, 200));
		
		// Add new profile
		profileList.add(new Profile(
				"testProfile", 
				"classPath", 
				"mainClass", 
				"defaultArgs", 
				"defaultOptions", 
				oracleList, 
				mbeanList, 
				customDataTypeList));
		

	}
	
	@After
	public void tearDown() throws IOException, InterruptedException{
		profileList = null;
		fileDataSource = null;
	
		IOUtils.deleteDirectory(rootFolder);
		
		rootFolder = null;
	}
	
	@Test
	public void saveProfileTest(){
		
		// Save the profile
		fileDataSource.saveProfile(profileList.get(0));
		
		// Load the profile
		Profile resultProfile = fileDataSource.getProfile("testProfile");
		
		// ********************************************************
		// Profile
		assertEquals("testProfile", resultProfile.getName());
		assertEquals("classPath", resultProfile.getClassPath());
		assertEquals("mainClass", resultProfile.getMainClass());
		assertEquals("defaultArgs", resultProfile.getDefaultArgs());
		assertEquals("defaultOptions", resultProfile.getDefaultOptions());
		
		// ********************************************************
		// MBeans
		
		List<MonitoredMBean> mbeanList = resultProfile.getMonitoredMBeans();
		
		assertEquals(2, mbeanList.size());
		
		MonitoredMBean mbean1 = mbeanList.get(0);
		
		assertEquals("objectName1", mbean1.getObjectName());
		assertArrayEquals(new String[]{"att1A", "att1B"}, mbean1.getMonitoredAttributes());
		assertArrayEquals(new String[]{"metaAtt1A", "metaAtt1B"}, mbean1.getMonitoredMetaAttributes());
		assertEquals(true, mbean1.monitorNotifications());
		
		MonitoredMBean mbean2 = mbeanList.get(1);
		
		assertEquals("objectName2", mbean2.getObjectName());
		assertArrayEquals(new String[]{"att2A", "att2B"}, mbean2.getMonitoredAttributes());
		assertArrayEquals(new String[]{"metaAtt2A", "metaAtt2B"}, mbean2.getMonitoredMetaAttributes());
		assertEquals(false, mbean2.monitorNotifications());
		
		// ********************************************************
		// Custom data types
		List<ButoProperty> customDataTypeList = resultProfile.getCustomDataTypes();
		
		assertEquals(2, customDataTypeList.size());
		
		ButoProperty dataType1 = customDataTypeList.get(0);
		
		assertEquals("key1", dataType1.getKey());
		assertEquals("name1", dataType1.getName());
		assertEquals("units1", dataType1.getUnits());
		assertEquals("type1", dataType1.getType());
		assertEquals(10, dataType1.getLowerBound().intValue());
		assertEquals(100, dataType1.getUpperBound().intValue());
		
		ButoProperty dataType2 = customDataTypeList.get(1);
		
		assertEquals("key2", dataType2.getKey());
		assertEquals("name2", dataType2.getName());
		assertEquals("units2", dataType2.getUnits());
		assertEquals("type2", dataType2.getType());
		assertEquals(20, dataType2.getLowerBound().intValue());
		assertEquals(200, dataType2.getUpperBound().intValue());
	}
	
	@Test
	public void saveOracleTest(){
		// Save the profile
		fileDataSource.saveProfile(profileList.get(0));
		fileDataSource.saveOracle(profileList.get(0), profileList.get(0).getOracleList().get(0));
		
		// Load the profile
		Profile resultProfile = fileDataSource.getProfile("testProfile");
		Oracle resultOracle = fileDataSource.getOracle(resultProfile, "Oracle 1");
		
		TrainingRun trainingRun1 = resultOracle.getTrainingRunList().get(0);
		
		//********************************************************
		// MetaData
		Map<String, Object> metaData = trainingRun1.getMetaData();
		
		assertEquals(3, metaData.size());
		
		assertEquals("value1", metaData.get("key1"));
		assertEquals(20, metaData.get("key2"));
		assertEquals(30.0d, metaData.get("key3"));
		
		//********************************************************
		// Snapshot list
		
		assertEquals(2, trainingRun1.getSnapshotList().size());
		
		Snapshot snapshot1 = trainingRun1.getSnapshotList().get(0);
		
		assertEquals(1000, snapshot1.getUptime());
		
		assertEquals(true, snapshot1.getObject("booleanA1"));
		assertEquals(false, snapshot1.getObject("booleanA2"));
		assertEquals(1.0d, snapshot1.getObject("doubleA1"));
		assertEquals(2.0d, snapshot1.getObject("doubleA2"));
		assertEquals(1, snapshot1.getObject("integerA1"));
		assertEquals(2, snapshot1.getObject("integerA2"));
		assertEquals(1L, snapshot1.getObject("longA1"));
		assertEquals(2L, snapshot1.getObject("longA2"));
		assertEquals("stringVA1", snapshot1.getObject("stringA1"));
		assertEquals("stringVA2", snapshot1.getObject("stringA2"));
		
		Snapshot snapshot2 = trainingRun1.getSnapshotList().get(1);
		
		assertEquals(2000, snapshot2.getUptime());
		
		assertEquals(true, snapshot2.getObject("booleanB1"));
		assertEquals(false, snapshot2.getObject("booleanB2"));
		assertEquals(1.0d, snapshot2.getObject("doubleB1"));
		assertEquals(2.0d, snapshot2.getObject("doubleB2"));
		assertEquals(1, snapshot2.getObject("integerB1"));
		assertEquals(2, snapshot2.getObject("integerB2"));
		assertEquals(1L, snapshot2.getObject("longB1"));
		assertEquals(2L, snapshot2.getObject("longB2"));
		assertEquals("stringVB1", snapshot2.getObject("stringB1"));
		assertEquals("stringVB2", snapshot2.getObject("stringB2"));
		
		//********************************************************
		// Notification list
		List<ButoNotification> notificationList = trainingRun1.getNotifications();
		
		assertEquals(2, notificationList.size());
		
		ButoNotification notification1 = notificationList.get(0);
		
		assertEquals("Type 1", notification1.getType());
		assertEquals("Object 1", notification1.getSourceObjectName());
		assertEquals(1000, notification1.getSequenceNumber());
		assertEquals(1000, notification1.getTimeStamp());
		assertEquals("hello world", notification1.getMessage());
		
		ButoNotification notification2 = notificationList.get(1);
		
		assertEquals("Type 2", notification2.getType());
		assertEquals("Object 2", notification2.getSourceObjectName());
		assertEquals(2000, notification2.getSequenceNumber());
		assertEquals(2000, notification2.getTimeStamp());
		assertEquals("hello world", notification2.getMessage());
	}

	@Test
	public void deleteProfileTest(){
		// Save the profile
		fileDataSource.saveProfile(profileList.get(0));
		
		// Load the profile
		Profile resultProfile = fileDataSource.getProfile("testProfile");
		
		// Assert profile is not null
		assertNotNull(resultProfile);
		
		// Delete the profile
		fileDataSource.deleteProfile(resultProfile);
		
		// Reload profile
		resultProfile = fileDataSource.getProfile("testProfile");
		
		// Assert is null
		assertNull(resultProfile);
	}

	@Test
	public void deleteOracleTest(){
		
		// Save the profile
		fileDataSource.saveProfile(profileList.get(0));
		
		// Save the oracle
		fileDataSource.saveOracle(profileList.get(0), profileList.get(0).getOracleList().get(0));
		
		// Load the profile and oracle
		Profile resultProfile = fileDataSource.getProfile("testProfile");
		Oracle resultOracle = fileDataSource.getOracle(resultProfile, "Oracle 1");
		
		// Test oracle is not null
		assertNotNull(resultOracle);
		
		// Delete the oracle
		fileDataSource.deleteOracle(resultProfile, resultOracle);
		
		// Reload the oracle
		resultOracle = fileDataSource.getOracle(resultProfile, "Oracle 1");
		
		// Test the oracle is now null
		assertNull(resultOracle);
	}
	
	@Test
	public void testSaveOracle(){
		
		Profile profile = profileList.get(0);
		
		// Save the profile
		fileDataSource.saveProfile(profile);
		
		// Alter the profile
		profile.setClassPath("NEW CLASSPATH");
		profile.setMainClass("NEW MAIN CLASS");
		profile.setDefaultArgs("NEW DEFAULT ARGS");
		profile.setDefaultOptions("NEW DEFAULT OPTIONS");
		
		List<MonitoredMBean> mbeanList = profile.getMonitoredMBeans();
		mbeanList.add(new MonitoredMBean("NEW BEAN", new String[]{"NEW ATT"}, new String[]{"NEW META ATT"}, true));
		
		List<ButoProperty> dataTypeList = profile.getCustomDataTypes();
		dataTypeList.add(new ButoProperty("NEW KEY", "NEW NAME", "NEW UNITS", "NEW TYPE", 100, 200));
		
		// Save the profile again
		fileDataSource.saveProfile(profile);
		
		// Reload the profile
		profile = fileDataSource.getProfile("testProfile");
		
		assertEquals("NEW CLASSPATH", profile.getClassPath());
		assertEquals("NEW MAIN CLASS", profile.getMainClass());
		assertEquals("NEW DEFAULT ARGS", profile.getDefaultArgs());
		assertEquals("NEW DEFAULT OPTIONS", profile.getDefaultOptions());
		
		mbeanList = profile.getMonitoredMBeans();
		assertEquals(3, mbeanList.size());
		
		dataTypeList = profile.getCustomDataTypes();
		assertEquals(3, mbeanList.size());
	}

}

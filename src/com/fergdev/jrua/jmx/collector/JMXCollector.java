package nz.ac.massey.buto.jmx.collector;

import java.io.IOException;
import java.lang.management.*;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.ObjectName;
import javax.management.NotificationListener;
import javax.management.ReflectionException;
import javax.management.openmbean.CompositeDataSupport;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import nz.ac.massey.buto.domain.MonitoredMBean;
import nz.ac.massey.buto.domain.ButoNotification;
import nz.ac.massey.buto.domain.Snapshot;
import nz.ac.massey.buto.domain.TrainingRun;

/**
 * Class used to extract JMX data from a MBeanServerConnection.
 * 
 * @author Fergus Hewson
 *
 */
public class JMXCollector extends Thread implements NotificationListener{

	/**
	 * The training run to add data to.
	 */
	private TrainingRun trainingRun;
	
	/**
	 * The connection to the MBeanServer to collect data from.
	 */
	private MBeanServerConnection connection;
	
	/**
	 * Log4j logger.
	 */
	private static Logger logger = LogManager.getLogger(JMXCollector.class);
	
	/**
	 * List of custom monitored MBeans to collect data from.
	 */
	private List<MonitoredMBean> monitoredMBeans;
	
	/**
	 * Flag to signify if the collector is running.
	 */
	private boolean running = true;
	
	/**
	 * List of JMXCollector listeners to notify of events.
	 */
	private List<JMXCollectorListener> jmxCollectorListeners;
	
	/**
	 * Configuration object.
	 */
	private JMXCollectorConfiguration configuration;
	
	/**
	 * Constructor
	 * @throws InterruptedException 
	 */
	public JMXCollector(
			MBeanServerConnection connection, 
			JMXCollectorConfiguration configuration, 
			List<JMXCollectorListener> jmxCollectorListeners, 
			List<MonitoredMBean> monitoredMBeans, 
			TrainingRun testFixture){
		
		this.connection = connection;
		this.jmxCollectorListeners = jmxCollectorListeners;
		this.monitoredMBeans = monitoredMBeans;
		this.trainingRun = testFixture;
		this.configuration = configuration;
		
		// notify listeners of collection starting
		notifyListeners(new JMXCollectorEvent(JMXCollectorEvent.COLLECTION_STARTED));

		// Subscribe to notifications
		try {
			setupNotificationMonitoring();
		} catch (IOException | UndeclaredThrowableException ex) {
			logger.error("IO Error setting up notification monitoring.\n" + ex);
		}
	}
	
	public void run(){
		// While the process is executing take snapshot of the process
		while(running){
			try {
				
				// Collect meta data after 5 snapshots
				if(trainingRun.getSnapshotList().size() == 1){
					
					// Collect the meta data for the test
					try {
						collectMetaData();
					} catch (Exception e){
						logger.error("Error collecting meta data.\n" + e);
					}
				}
				
				// Take a snapshot
				takeSnapshot();
				
				// Sleep
				Thread.sleep(configuration.collectionInterval);
				
			} catch (Exception ex) {
				logger.error("Expception thrown '" + ex.getMessage() + "'" );
				break;
			}
		}
		
		// Notify of end of collection
		running = false;
		
		// Notify listener
		notifyListeners(new JMXCollectorEvent(JMXCollectorEvent.COLLECTION_COMPLETE));
	}

	/**
	 * Gets the running flag for the collector.
	 * 
	 * @return True if the collector is running, false if not.
	 */
	public boolean isRunning(){
		return running;
	}
	
	public void interrupt(){
		running = false;
	}
	
	public TrainingRun getTrainingRun(){
		return trainingRun;
	}
	
	/**
	 * Sets up notification monitoring by subscribing to notifications for
	 * each of the relevant monitored MBeans.
	 */
	private void setupNotificationMonitoring()throws IOException{
		
		// Iterate over monitored MBeans and subscribe to notifications
		// from relevant MBeans
		for(MonitoredMBean bean : monitoredMBeans){
			
			// Check if the notifications from this bean should be monitored
			if(bean.monitorNotifications()){

				try {
					//Create object name
					ObjectName objName = new ObjectName(bean.getObjectName());
					
					// Add notification listener for this bean
					connection.addNotificationListener(objName, this, null, null);
					
				} catch (InstanceNotFoundException | IOException e) {
					logger.error("Unable to subscribe to notifications for bean - '" + bean.getObjectName() + "'");
					
				} catch (MalformedObjectNameException e) {
					logger.error("Malformed object name '" + bean.getObjectName() + "'");
				}
			}
		}
	}
	
	/**
	 * Takes a snapshot of the current program and adds it to the data list.
	 * @throws IOException 
	 */
	private void takeSnapshot() throws IOException{
		long startTime = System.currentTimeMillis();
		
		logger.info("Taking snapshot");
		
		// Create new snapshot object
		Snapshot newSnapshot = new Snapshot(startTime - trainingRun.getDate().getTime());
		
		if(configuration.collectBufferPoolData)
			addBufferPoolData(newSnapshot);
		
		if(configuration.collectClassLoadingData)
			addClassLoadingData(newSnapshot);
		
		if(configuration.collectCompilationData)
			addCompilationData(newSnapshot);
		
		if(configuration.collectGarbageCollectorData)
			addGarbageCollectorData(newSnapshot);	
		
		if(configuration.collectMemoryData)
			addMemoryData(newSnapshot);
		
		if(configuration.collectMemoryPoolData)
			addMemoryPoolData(newSnapshot);
		
		if(configuration.collectThreadData)
			addThreadData(newSnapshot);
		
		if(configuration.collectCustomMBeans)
			collectCustomMBeans(newSnapshot);
		
		long diff = System.currentTimeMillis()-startTime;
		logger.info("Snapshot collection time was "+ (diff) +" ms.\n\n");
		newSnapshot.addData("overhead_total", diff);
		
		// Add the snapshot
		trainingRun.addSnapshot(newSnapshot);
		
		// Notify listeners
		notifyListeners(new JMXCollectorEvent(newSnapshot));
	}
	
	/**
	 * Adds buffer pool information to a given snapshot.
	 * @throws IOException 
	 */
	private void addBufferPoolData(Snapshot s) throws IOException{
		long startTime = System.currentTimeMillis();
		
		// Iterate over list adding data to the snapshot
		for (BufferPoolMXBean bean : ManagementFactory.getPlatformMXBeans(connection, BufferPoolMXBean.class)) {

			String prefix = "bufferPool_" + bean.getName() + "_";
			
			s.addData(prefix + "count", bean.getCount());
			s.addData(prefix + "memoryUsed", bean.getMemoryUsed());
			s.addData(prefix + "totalCapacity", bean.getTotalCapacity());
		}
		
		long diff = System.currentTimeMillis()-startTime;
		logger.info("Bufferpool collection time was "+ (diff) +" ms.");
		s.addData("overhead_bufferpool", diff);
	}

	/**
	 * Adds class loading data to a snapshot.
	 * 
	 * @throws IOException 
	 */
	private void addClassLoadingData(Snapshot s) throws IOException{
		long startTime = System.currentTimeMillis();
		
		ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getPlatformMXBean(connection, ClassLoadingMXBean.class);
		
		// Add class loading data to the bean
		s.addData("classLoading_totalLoaded",classLoadingMXBean.getTotalLoadedClassCount());
		s.addData("classLoading_loadedCount",classLoadingMXBean.getLoadedClassCount());
		s.addData("classLoading_unloadedCount",classLoadingMXBean.getUnloadedClassCount());
		
		long diff = System.currentTimeMillis()-startTime;
		logger.info("CLASS LOADING collection time was "+ (diff) +" ms.");
		s.addData("overhead_classLoading", diff);
	}

	/**
	 * Adds information from the compilation MXBean to the given snapshot.
	 * @throws IOException 
	 */
	private void addCompilationData(Snapshot s) throws IOException{
		long startTime = System.currentTimeMillis();
		
		CompilationMXBean compilationMXBean = ManagementFactory.getPlatformMXBean(connection, CompilationMXBean.class);
		
		// Add the compilation information
		s.addData("compilation_time", compilationMXBean.getTotalCompilationTime());
		
		long diff = System.currentTimeMillis()-startTime;
		logger.info("COMPILATION collection time was "+ (diff) +" ms.");
		s.addData("overhead_compilation", diff);
	}
	
	/**
	 * Adds the memory data from the MemoryXBean to a snapshot.
	 * @throws IOException 
	 */
	private void addMemoryData(Snapshot s) throws IOException{
		long startTime = System.currentTimeMillis();

		MemoryMXBean memoryMXBean = ManagementFactory.getPlatformMXBean(connection, MemoryMXBean.class);
		
		// Add object pending finalisation count to the bean
		s.addData("memory_pendingFinilizationCount", memoryMXBean.getObjectPendingFinalizationCount());
		
		// Get the heap memory usage
		MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
		s.addData("memory_heap_committed", heapMemoryUsage.getCommitted());
		s.addData("memory_heap_used", heapMemoryUsage.getUsed());
		
		// Get the non heap memory usage
		MemoryUsage nonheapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
		s.addData("memory_nonHeap_committed", nonheapMemoryUsage.getCommitted());
		s.addData("memory_nonHeap_used", nonheapMemoryUsage.getUsed());
		
		long diff = System.currentTimeMillis()-startTime;
		logger.info("MEMORY collection time was "+ (diff) +" ms.");
		s.addData("overhead_memory", diff);
	}

	/**
	 * Adds garbage collector information to a snapshot.
	 * @throws IOException 
	 */
	private void addGarbageCollectorData(Snapshot s) throws IOException{
		long startTime = System.currentTimeMillis();
		
		// Iterate over the beans
		for (GarbageCollectorMXBean bean : ManagementFactory.getPlatformMXBeans(connection, GarbageCollectorMXBean.class)) {

			String strippedName = bean.getName().replace(' ', '_');
			String prefix = "gc_" + strippedName + "_";

			// Add garbage collector data to snapshot
			s.addData(prefix + "collectionCount", bean.getCollectionCount());
			s.addData(prefix + "collectionTime", bean.getCollectionTime());
		}
		
		long diff = System.currentTimeMillis()-startTime;
		logger.info("GC collection time was "+ diff +" ms.");
		s.addData("overhead_gc", diff);
	}
	
	/**
	 * Adds memory pool data to a snapshot
	 * @throws IOException 
	 */
	private void addMemoryPoolData(Snapshot s) throws IOException{
		long startTime = System.currentTimeMillis();

		// iterate over the list
		for (MemoryPoolMXBean bean : ManagementFactory.getPlatformMXBeans(connection, MemoryPoolMXBean.class)) {

			// Create key prefix
			String strippedName = bean.getName().replace(' ', '_');
			String prefix = "memoryPool_" + strippedName + "_";
			
			// Get the collection memory usage
			MemoryUsage collectionMemoryUsage = bean.getCollectionUsage();
			if(collectionMemoryUsage != null){
				s.addData(prefix + "collectionUsage_committed", collectionMemoryUsage.getCommitted());
				s.addData(prefix + "collectionUsage_used", collectionMemoryUsage.getUsed());
			}

			// If collection usage threshold supported add the data to the
			if (bean.isCollectionUsageThresholdSupported()) {
				s.addData(prefix + "collectionUsageThreshold", bean.getCollectionUsageThreshold());
				s.addData(prefix + "collectionUsageThresholdCount", bean.getCollectionUsageThresholdCount());
			}

			// Add usage to data
			MemoryUsage usage = bean.getUsage();
			s.addData(prefix + "usage_committed", usage.getCommitted());
			s.addData(prefix + "usage_used", usage.getUsed());
			
			// If usage threshold is supported add the data to the snapshot
			if (bean.isUsageThresholdSupported()) {
				s.addData(prefix + "usageThreshold", bean.getUsageThreshold());
				s.addData(prefix + "usageThresholdCount", bean.getUsageThresholdCount());
			}
		}
		
		long diff = System.currentTimeMillis() - startTime;
		logger.info("MEMORY POOL collection time was "+ (diff) +" ms.");
		s.addData("overhead_memorypool", diff);
	}
	
	/**
	 * Prints out the thread data.
	 * 
	 * @throws IOException 
	 */
	private void addThreadData(Snapshot snapshot) throws IOException{
		long startTime = System.currentTimeMillis();
		
		ThreadMXBean threadMXBean = ManagementFactory.getPlatformMXBean(connection, ThreadMXBean.class);
		
		// Get thread count data
		snapshot.addData("thread_daemonThreadCount", 		threadMXBean.getDaemonThreadCount());
		snapshot.addData("thread_peakThreadCount", 			threadMXBean.getPeakThreadCount());
		snapshot.addData("thread_threadCount", 				threadMXBean.getThreadCount());
		snapshot.addData("thread_totalStartedThreadCount", 	threadMXBean.getTotalStartedThreadCount());
		
		long diff = System.currentTimeMillis()-startTime;
		logger.info("THREAD collection time was "+ (diff) +" ms.");
		snapshot.addData("overhead_thread", diff);
	}
	
	/**
	 * Adds the values for the custom MBeans to the snapshot.
	 * 
	 * @param s The snapshot to add the data to.
	 */
	private void collectCustomMBeans(Snapshot s) throws IOException{
		
		long startTime = System.currentTimeMillis();
		
		// Iterate over each monitored MBean
		for(MonitoredMBean bean : monitoredMBeans){
			
			try {
				// Create the object name
				ObjectName objectName = new ObjectName(bean.getObjectName());
				
				// Get the attribute information for the current MBean
				AttributeList list = connection.getAttributes(objectName, bean.getMonitoredAttributes());

				// Add each attribute to the current snapshot
				for(Attribute att : list.asList()){

					// Get the obj
					Object value = att.getValue();
					
					// Test if composite data
					if(value.getClass().getName() == "javax.management.openmbean.CompositeDataSupport"){
						
						// Cast to composite data support
						CompositeDataSupport cds = (CompositeDataSupport)value;
						
						// Get the keys for the contained data
						for(String key : cds.getCompositeType().keySet()){
							
							// Add the data to the snapshot
							s.addData(bean.getObjectName() + "_" + att.getName() + "_" + key, cds.get(key));
						}
					}else{
						
						// Add data
						s.addData(bean.getObjectName() + "_" + att.getName(), value);
					}
				}

			} catch (InstanceNotFoundException | ReflectionException e) {
				logger.error("Error collecting custom MBean data - '" + bean.getObjectName() + "'");
			} catch (MalformedObjectNameException expection){
				logger.error("Error collecting custom MBean data Malformed object name - '" + bean.getObjectName() + "'");
			}
			
		}
		
		long diff = System.currentTimeMillis()-startTime;
		logger.info("CUSTOMMBEAN collection time was "+ (diff) +" ms.");
		s.addData("overhead_customMBeans", diff);
	}
	
	/**
	 * Collects all the data the Meta Data for the test fixture.
	 */
	private void collectMetaData()throws IOException{
		long startTime = System.currentTimeMillis();
		
		// Meta Data map
		Map<String, Object> metaDataMap = trainingRun.getMetaData();
		
		// Operating System Bean
		OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(connection, OperatingSystemMXBean.class);

		metaDataMap.put("os_name", 						osBean.getName());
		metaDataMap.put("os_architecture",				osBean.getArch());
		metaDataMap.put("os_version", 					osBean.getVersion());
		metaDataMap.put("os_available_processors", 		osBean.getAvailableProcessors());
		metaDataMap.put("os_systemLoadAverage", 		osBean.getSystemLoadAverage());
	
		// Java runtime Bean
		RuntimeMXBean runtimeBean = ManagementFactory.getPlatformMXBean(connection, RuntimeMXBean.class);
		
		metaDataMap.put("runtime_bootClassPath", 			runtimeBean.getBootClassPath());
		metaDataMap.put("runtime_classPath", 				runtimeBean.getClassPath());
		metaDataMap.put("runtime_inputArguements",			runtimeBean.getInputArguments().toString());
		metaDataMap.put("runtime_libraryPath",				runtimeBean.getLibraryPath());
		metaDataMap.put("runtime_managementSpecVersion", 	runtimeBean.getManagementSpecVersion());
		metaDataMap.put("runtime_name", 					runtimeBean.getName());
		metaDataMap.put("runtime_specName",					runtimeBean.getSpecName());
		metaDataMap.put("runtime_specVendor", 				runtimeBean.getSpecVendor());
		metaDataMap.put("runtime_specVersion", 				runtimeBean.getSpecVersion());
		metaDataMap.put("runtime_startTime",				runtimeBean.getStartTime());
		metaDataMap.put("runtime_uptime", 					runtimeBean.getUptime());
		metaDataMap.put("runtime_vmName",					runtimeBean.getVmName());
		metaDataMap.put("runtime_vmVendor",					runtimeBean.getVmVendor());
		metaDataMap.put("runtime_vmVersion",				runtimeBean.getVmVersion());
		
		Map<String, String> systemProperties = runtimeBean.getSystemProperties();
		// Extract keys and add to batch insert
		for(String key : systemProperties.keySet()){
			
			String dbKey = "runtime_systemProperties_" + key.replaceAll("\\s+", "");
			metaDataMap.put(dbKey, systemProperties.get(key));
		}

		// Compilation Bean
		CompilationMXBean compilationBean = ManagementFactory.getPlatformMXBean(connection, CompilationMXBean.class);
		
		metaDataMap.put("jit_name", compilationBean.getName());
		
		// Memory
		MemoryMXBean memoryMXBean = ManagementFactory.getPlatformMXBean(connection, MemoryMXBean.class);
		metaDataMap.put("memory_heapInitialSize", 		memoryMXBean.getHeapMemoryUsage().getInit());
		metaDataMap.put("memory_nonHeapInitialSize", 	memoryMXBean.getNonHeapMemoryUsage().getInit());
		 
		// Memory Pool Beans
		List<MemoryPoolMXBean> memoryPoolBeans = ManagementFactory.getPlatformMXBeans(connection, MemoryPoolMXBean.class);
		
		for(MemoryPoolMXBean memoryPoolMXBean : memoryPoolBeans){
			
			// Create key prefix
			String prefix = "memoryPool_" + memoryPoolMXBean.getName().replaceAll("\\s+", "");
			
			StringBuilder managerNamesStringBuilder = new StringBuilder();
			
			String[] managers = memoryPoolMXBean.getMemoryManagerNames();
			
			if(managers.length > 0){
				
				managerNamesStringBuilder.append(managers[0]);
				
				for(int index = 0; index < managers.length; index++){
					managerNamesStringBuilder.append(",");
					managerNamesStringBuilder.append(managers[index]);
				}
				
			}
			
			metaDataMap.put(prefix + "_managersNames", 	managerNamesStringBuilder.toString());
			metaDataMap.put(prefix + "_name", 			memoryPoolMXBean.getName());
			metaDataMap.put(prefix + "_type", 			memoryPoolMXBean.getType().toString());
		}
			
		// Collect Monitored MBean information
		for(MonitoredMBean bean : monitoredMBeans){
			
			// Get list of meta attributes
			String[] metaAttributes = bean.getMonitoredMetaAttributes();
			
			// Only process if there are attributes to collect
			if(metaAttributes.length != 0){
				
				try {
					// Create object name
					ObjectName objName = new ObjectName(bean.getObjectName());
					
					// Get the attribute values
					AttributeList attList = connection.getAttributes(objName, metaAttributes);
					
					// Add each attribute to the meta data
					for(Attribute attribute : attList.asList()){
						metaDataMap.put(bean.getObjectName() + "_" + attribute.getName(), attribute.getValue());
					}
					
				} catch (InstanceNotFoundException | ReflectionException| IOException | MalformedObjectNameException e) {
					logger.error("Could not retrieve attributes for custom MBean = '" + bean.getObjectName() + "'");
				}
			}
		}

		
		long collectionTime = (System.currentTimeMillis()-startTime);
		logger.info("METADATA collection time was "+ collectionTime +" ms.");
		metaDataMap.put("overhead_metadata", collectionTime);
		
		notifyListeners(new JMXCollectorEvent(JMXCollectorEvent.METADATA_COLLECTED));
	}

	@Override
	public void handleNotification(Notification notification, Object handback) {
		
		// Extract notification data
		String type = notification.getType();
		String source = notification.getSource().toString();
		long sequenceNumber = notification.getSequenceNumber();
		long timeStamp = notification.getTimeStamp();
		String message = notification.getMessage();
		
		// add to processed notifications list
		trainingRun.getNotifications().add(
				new ButoNotification(
						type,
						source,
						sequenceNumber,
						timeStamp,
						message)
				);
	}
	
	/**
	 * Notifies all of the JMXCollectionListeners of a JMXCollector event.
	 * 
	 * @param event The event distribute to listeners.
	 */
	private void notifyListeners(JMXCollectorEvent event){
		
		// Iterate over listeners
		for(JMXCollectorListener listener : jmxCollectorListeners){
			
			// notify listener of event
			listener.processJMXCollectorEvent(event);
		}
	}
}

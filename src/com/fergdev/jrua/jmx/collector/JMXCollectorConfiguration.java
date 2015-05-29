package nz.ac.massey.buto.jmx.collector;

/**
 * Used for individual configurations of the JMXCollector, flags are used to signal
 * which data to collect. Sleep time is the interval between snapshot collection.
 * 
 * @author Fergus Hewson
 *
 */
public class JMXCollectorConfiguration {
	
	/**
	 * Flag to signify the collection of Buffer Pool Data.
	 */
	public boolean collectBufferPoolData = false;

	/**
	 * Flag to signify the collection of Class Loading Data.
	 */
	public boolean collectClassLoadingData = false;

	/**
	 * Flag to signify the collection of Compilation Data.
	 */
	public boolean collectCompilationData = false;

	/**
	 * Flag to signify the collection of Memory Data.
	 */
	public boolean collectMemoryData = false;

	/**
	 * Flag to signify the collection of Garbage collector data.
	 */
	public boolean collectGarbageCollectorData = false;

	/**
	 * Flag to signify the collection of Memory Pool data.
	 */
	public boolean collectMemoryPoolData = false;

	/**
	 * Flag to signify the collection of Custom MBean data.
	 */
	public boolean collectCustomMBeans = false;

	/**
	 * Flag to signify the collection of thread data.
	 */
	public boolean collectThreadData = false;

	/**
	 * The interval between snapshot collections.
	 */
	public int collectionInterval = 200;
	
	/**
	 * Creates a string representation of this JMX configuration object.
	 */
	public String toString(){
		
		String output = 
				"bufferPool="+collectBufferPoolData + "\n" +
				"classLoading="+collectClassLoadingData + "\n" +
				"compilation="+collectCompilationData + "\n" +
				"memory="+collectMemoryData + "\n" +
				"garbageCollection="+collectGarbageCollectorData + "\n" +
				"memoryPool="+collectMemoryPoolData + "\n" + 
				"customMBeans=" + collectCustomMBeans + "\n" + 
				"thread=" + collectThreadData;
				
		return output;
		
	}
}

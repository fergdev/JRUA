package nz.ac.massey.buto.jmx.collector;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import nz.ac.massey.buto.domain.ButoProperty;

/**
 * Factory for JMXCollectoConfiguration objects, used to get optimal configurations based on monitored properties.
 * 
 * @author Fergus Hewson
 *
 */
public class JMXCollectorConfigurationFactory {

	/**
	 * Log4j logger.
	 */
	private static Logger logger = LogManager.getLogger(JMXCollectorConfigurationFactory.class);

	/**
	 * Gives a JMXCollector configuration based on a list of Buto properties that are being monitored.
	 * 
	 * @param propertyList List of Buto properties being monitored.
	 * @return A JMXCollector configuration optimised to collect on relevant data.
	 */
	public static JMXCollectorConfiguration getNewConfig(List<ButoProperty> propertyList){
		JMXCollectorConfiguration config  = new JMXCollectorConfiguration();
		
		logger.debug("Property list size = " + propertyList.size());
		
		for(ButoProperty property : propertyList){
			
			String key = property.getKey();
			
			if(key.startsWith("bufferPool_")){
				config.collectBufferPoolData = true;
				
			}else if(key.startsWith("classLoading_")){
				config.collectClassLoadingData = true;
				
			}else if(key.startsWith("compilation_")){
				config.collectCompilationData = true;
				
			}else if(key.startsWith("memory_")){
				config.collectMemoryData = true;
				
			}else if(key.startsWith("gc_")){
				config.collectGarbageCollectorData = true;
				
			}else if(key.startsWith("memoryPool_")){
				config.collectMemoryPoolData = true;
				
			}else if(key.startsWith("thread_")){
				config.collectThreadData = true;
				
			}else{
				config.collectCustomMBeans = true;
			}
		}
		
		logger.debug(config.toString());
		
		return config;
	}
}

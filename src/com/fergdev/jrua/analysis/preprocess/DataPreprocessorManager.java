package nz.ac.massey.buto.analysis.preprocess;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Manager for DataPreprocessor objects.
 * 
 * @author Fergus Hewson
 *
 */
public class DataPreprocessorManager {

	private static Logger logger = LogManager.getLogger(DataPreprocessorManager.class);
	
	
	
	/**
	 * List of DataPreprocessorFactory objects.
	 */
	private static ServiceLoader<DataPreprocessor> dataPreprocessorLoader = ServiceLoader.load(DataPreprocessor.class);
	
	/**
	 * Gets array of loaded DataPreprocessor names.
	 * 
	 * @return String array.
	 */
	public String[] getNames(){
		
		// List of names
		List<String> nameList = new ArrayList<String>(); 
		
		// iterate over available services
		for(DataPreprocessor  d : dataPreprocessorLoader){
			
			// add name to list
			nameList.add(d.getClass().getCanonicalName());
		}
		
		// return array
		return nameList.toArray(new String[nameList.size()]);
	}
	
	/**
	 * Gets the DataPreprocessor with the provided name.
	 * 
	 * @return DataPreprocessor object, null if name could not be found.
	 */
	public DataPreprocessor getDataPreprocessor(String name){
		
		// iterate over available services
		for(DataPreprocessor d : dataPreprocessorLoader){
			
			// check if this is the requested service
			if(d.getClass().getCanonicalName().equals(name)){
				return d;
			}
		}
		
		// service could not be found
		logger.error("Unable to retrieve DataPreProcessor service '" + name + "'");
		return null;
	}
}

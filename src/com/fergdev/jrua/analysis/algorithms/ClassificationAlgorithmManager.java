package nz.ac.massey.buto.analysis.algorithms;


import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Class to manage the ClassificationAlgorithm objects.
 * 
 * @author Fergus Hewson
 *
 */
public class ClassificationAlgorithmManager {

	private static Logger logger = LogManager.getLogger(ClassificationAlgorithmManager.class);
	
	
	/**
	 * List of loaded ClassificationAlgorithm objects.
	 */
	private static ServiceLoader<ClassificationAlgorithm> classificationAlgorithmLoader = ServiceLoader.load(ClassificationAlgorithm.class);
	
	/**
	 * List of the names for the loaded ClassificationAlgorithm objects.
	 * 
	 * @return List of the names of the available services.
	 */
	public static String[] getNames(){
		
		// list of names
		List<String> nameList = new ArrayList<String>(); 
		
		// iterate over available services building name list
		for(ClassificationAlgorithm  c : classificationAlgorithmLoader){
			nameList.add(c.getClass().getCanonicalName());
		}
		
		// return array list
		return nameList.toArray(new String[nameList.size()]);
	}
	
	/**
	 * Retrieves the ClassificationAlgorithm service that corresponds to the provided service name.
	 * 
	 * @return A ClassificationAlgorithm object, null if the service could not be found.
	 */
	public static ClassificationAlgorithm getClassificationAlgorithm(String name){
		
		ServiceLoader<ClassificationAlgorithm> classificationAlgorithmLoader = ServiceLoader.load(ClassificationAlgorithm.class);
		
		logger.info("LOADING - '" + name + "'");
		// iterate over available services
		for(ClassificationAlgorithm  c : classificationAlgorithmLoader){
			logger.info("PROCESSING - '" + c.getClass() + "'");
			// test if this is the service requested
			if(c.getClass().getCanonicalName().equals(name)){
				return c;
			}
		}
		
		// could not find service
		logger.error("Error retrieving ClassificationAlgorithm '" + name + "'");
		return null;
	}
}

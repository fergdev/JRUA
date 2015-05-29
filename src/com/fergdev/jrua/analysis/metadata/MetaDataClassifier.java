package nz.ac.massey.buto.analysis.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Model used to classify meta data.
 * 
 * @author Fergus Hewson
 *
 */
public class MetaDataClassifier implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4096379352381141511L;

	/**
	 * Model generated in constructor, used to classify test meta data.
	 */
	private Map<String, Object> model;

	/**
	 * List of keys to ignore for comparisons.
	 */
	private List<String> filterList;
	
	/**
	 * Log4j logger.
	 */
	private static Logger logger = LogManager.getLogger(MetaDataClassifier.class);
	
	
	/**
	 * Creates a meta data model out a set of training meta data.
	 * 
	 * @param trainingDataList The data set to generate the model from.
	 * @param filterList List of keys to filter comparisons from.
	 * @throws MetaDataClassifierCreationException 
	 */
	public MetaDataClassifier(List<Map<String, Object>> trainingDataList, List<String> filterList) throws MetaDataClassifierCreationException{
		
		// Set the filter list
		this.filterList = filterList;
		
		// Test for empty list
		if(trainingDataList.size() == 0){
			throw new MetaDataClassifierCreationException("Error building model training data is empty. " + trainingDataList);
		
		}else if(trainingDataList.size() == 1){
			
			// Only one map, set as model
			model = trainingDataList.get(0);
			
			return;
		}
		
		// Check for consistency
		Map<String, Object> baseMap = trainingDataList.get(0);
		
		// Get the base map keys
		Set<String> baseMapKeys = baseMap.keySet();
		
		// Remove the filter list
		baseMapKeys.removeAll(filterList);
		
		// Iterate over data
		for(int index = 1; index < trainingDataList.size(); index++){
			
			// Get the current map
			Map<String, Object> currentMap = trainingDataList.get(index);
			
			// Get the keys and remove filtered keys
			Set<String> currentMapKeys = currentMap.keySet();
			currentMapKeys.removeAll(filterList);
			
			// Iterate over the keys in base map and compare objects retrieved from 
			for(String key : baseMapKeys){
				
				// Retrieve objects from both maps
				Object baseObj = baseMap.get(key);
				Object currentObj = baseMap.get(key);
				
				// Test current object for null
				if(currentObj == null){
					String message = "Object missing from map - '" + key + "'";
					logger.error(message);
					//throw new MetaDataClassifierCreationException(message);
				// Test for equality	
				}else if(!baseObj.equals(currentObj)){
					String message = "Object differs from base map - '" + key + "' '" + baseObj.toString() + "' '" + currentObj.toString() + "'";
					logger.error(message);
					//throw new MetaDataClassifierCreationException(message);
				}
			}
			
			// Test for objects that in the current map but not in the base map
			// Remove all keys from the baseMap
			currentMapKeys.removeAll(baseMapKeys);
			
			// Left over keys are not in the base map
			for(String key : currentMapKeys){
				
				// Get the object
				Object obj = currentMap.get(key);
				
				// Throw exception
				String message = "Object missing from base map '" + key + "' '" + obj.toString() + "'";
				logger.error(message);
				//throw new MetaDataClassifierCreationException(message); 
			}
		}

		// Set the model as the base map
		model = baseMap;
	}
	
	/**
	 * Classifies a given a meta data set against the model. Returns a list of differences found
	 * between the test meta data and the model.
	 * 
	 * @param testData The meta data set to test against the model.
	 * @return A list of differences between the test data set and the model.
	 */
	public synchronized List<MetaDataDifference> classify(Map<String, Object> testData){
		
		// List of differences to return
		List<MetaDataDifference> differences = new ArrayList<MetaDataDifference>();
		
		// Get the key sets for both maps
		Set<String> modelKeys = model.keySet();
		Set<String> testDataKeys = testData.keySet();
		
		modelKeys.removeAll(filterList);
		
		// Iterate over keys in A and check for differences in B
		for(String keyA : modelKeys){
			
			// Get the values from the data
			Object modelObject = model.get(keyA);
			Object testObject = testData.get(keyA);
			
			// Test if dataB does not contain the key
			if(testObject == null){

				differences.add(new MetaDataDifference( keyA, modelObject.toString(), "null"));
				
			// Test if the two values for the same key are equal
			}else if(! modelObject.equals(testObject)){
				
				differences.add(new MetaDataDifference( keyA, modelObject.toString(), testObject.toString()));
			}
		}
		
		// Remove all the already processed keys
		testDataKeys.removeAll(modelKeys);
		
		// Iterate over the unprocessed keys, these keys did not appear in A
		for(String keyB : testDataKeys){
			
			differences.add(new MetaDataDifference( keyB, "null", testData.get(keyB).toString()));
		}
		
		// Return list of differences
		//return differences;
		return new ArrayList<MetaDataDifference>();
	}
}

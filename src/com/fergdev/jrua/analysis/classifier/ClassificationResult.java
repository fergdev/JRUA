package nz.ac.massey.buto.analysis.classifier;

import java.util.List;
import java.util.Map;

import nz.ac.massey.buto.analysis.algorithms.models.ClassificationModelResult;
import nz.ac.massey.buto.analysis.metadata.MetaDataDifference;
import nz.ac.massey.buto.analysis.notifications.NotificationDifference;
import nz.ac.massey.buto.domain.ButoProperty;

/**
 * Result object returned when a TestRun is classified against an oracle. Contains a map
 * of DataType keys to list of error timestamps for that data type, list of meta errors and 
 * list of notification errors.
 * 
 * @author Fergus Hewson
 *
 */
public class ClassificationResult {

	/**
	 * Map of DataTypes to sets of ClassificationModelDifferences.
	 */
	private Map<ButoProperty, ClassificationModelResult> classificationResultMap;
	
	/**
	 * List of differences in the meta data.
	 */
	private List<MetaDataDifference> metaDataResults;
	
	/**
	 * List of differences for notifications.
	 */
	private List<NotificationDifference> notificationDifferences;

	
	/**
	 * Creates a new classification result.
	 * 
	 * @param classificationResultMap Map of DataTypes to sets of ClassificationModelDifferences.
	 * @param metaDataResults List of differences in the meta data.
	 * @param notificationDifferences List of differences for notifications.
	 */
	public ClassificationResult(
			Map<ButoProperty, ClassificationModelResult> classificationResultMap,
			List<MetaDataDifference> metaDataResults,
			List<NotificationDifference> notificationDifferences
			){
		
		this.classificationResultMap = classificationResultMap;
		this.metaDataResults = metaDataResults;
		this.notificationDifferences = notificationDifferences;
	}
	
	/**
	 * Tests if this classification result was a successful classification.
	 * 
	 * @return True if successful, false otherwise.
	 */
	public boolean successful(){
		
		// Classification results for error
		for(ButoProperty dataType : classificationResultMap.keySet()){
			
			// Test for errors
			if(!classificationResultMap.get(dataType).sucessful()){
				return false;
			}
		}
		
		// Test meta data results
		if(metaDataResults.size() > 0){
			return false;
		}
		
		// Test notification differences
		if(notificationDifferences.size() > 0 ){
			return false;
		}
		
		// No errors in the classification
		return true;
	}

	public Map<ButoProperty, ClassificationModelResult> getClassificationResultMap() {
		return classificationResultMap;
	}

	public List<MetaDataDifference> getMetaDataResults() {
		return metaDataResults;
	}

	public List<NotificationDifference> getNotificationDifferences() {
		return notificationDifferences;
	}
	
	/**
	 * Creates a report style string for this result.
	 * 
	 * @return Report style string.
	 */
	/*
	public String toOutputString(){
		
		// String builder for output
		StringBuilder outputBuilder = new StringBuilder();
		
		String lineSeparator1 = StringUtils.repeat("#", 100)+ "\n";
		String lineSeparator2 = StringUtils.repeat("+", 100)+ "\n";
		String lineSeparator3 = StringUtils.repeat("-", 100)+ "\n";
		
		// Header
		outputBuilder.append(lineSeparator1 + lineSeparator1);
		outputBuilder.append("RESULT " + successful() + "\n\n");
		
		// Classification errors
		outputBuilder.append(lineSeparator1 + lineSeparator1);
		outputBuilder.append("CLASSIFICATION DIFFERENCES\n\n");
		
		//******************************************************
		// Classification result map
		
        // Iterate over data types
		for(ButoProperty dataType : classificationResultMap.keySet()){
			
			outputBuilder.append(lineSeparator2);
			outputBuilder.append(dataType.getName() + "\n");
			outputBuilder.append(lineSeparator3);
			
			// Header string for error information
			outputBuilder.append(String.format("%-20s%-20s%s", "Timestamp", "Value", "Bounds(timestamp, lowerbound, upperbound)") + "\n");
			
			for(ClassificationModelDifference difference : classificationResultMap.get(dataType)){
				
				// Add line
				outputBuilder.append(difference.toOutputString() + "\n");
			}
			
			outputBuilder.append(lineSeparator3);
			//outputBuilder.append("\n");
		}
		
		outputBuilder.append("\n");
		
		//******************************************************
		// Meta data results
		outputBuilder.append(lineSeparator1 + lineSeparator1);
		outputBuilder.append("META DATA DIFFERENCES\n\n");
		
		for(MetaDataDifference difference : metaDataResults){
			outputBuilder.append(difference.toString() + "\n");
		}
		
		outputBuilder.append("\n\n");
		
		//******************************************************
		// Notification results
		outputBuilder.append(lineSeparator1 + lineSeparator1);
		outputBuilder.append("NOTIFICATION DIFFERENCES\n\n");
		
		for(NotificationDifference difference : notificationDifferences){
			outputBuilder.append(difference.toString() + "\n");
		}
		
		return outputBuilder.toString();
	}
	
	public String toCsv(){
		StringBuilder builder = new StringBuilder();
		
		
		for(ButoProperty key : classificationResultMap.keySet()){
			
			builder.append(key.getName());
			
			List<ClassificationModelDifference> differences = classificationResultMap.get(key);
			
			for(ClassificationModelDifference difference : differences){
				builder.append(", " +difference.getTimeStamp());
			}
			
			builder.append("\n");		
		}
		
		return builder.toString();
	}
	*/
}

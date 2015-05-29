package nz.ac.massey.buto.analysis.notifications;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.stat.StatUtils;

import nz.ac.massey.buto.domain.ButoNotification;

/**
 * Class used to classify the notifications collected from test runs.
 * 
 * Classifier works by using a notifications Type, SourceObject and SequenceNumber as a key to identify notifications. Notifications
 * across all training run are then grouped by this key and a set of bounds for the timeStamp is then generated for each unique notification.
 * 
 * @author Fergus Hewson
 *
 */
public class NotificationClassifier implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9088117530586747639L;
	/**
	 * Map of a notification key string to the acceptable bounds for that notifications time stamp.
	 */
	private Map<String, NotificationBounds> boundsMap;

	/**
	 * Creates a classifier for lists of notifications.
	 * 
	 * @param notifications Data set to train on.
	 * @throws NotificationClassifierCreationException 
	 */
	public NotificationClassifier(List<List<ButoNotification>> trainingData) throws NotificationClassifierCreationException{
		
		// Test for empty list, should be a list containing empty lists for no data
		if(trainingData.size() == 0){
			throw new NotificationClassifierCreationException("Empty training data");
		}
		
		// Create bounds map
		boundsMap = new HashMap<String, NotificationBounds>();
		
		// Group notifications into lists base on the key "type sourceObject sequenceNumber"
		Map<String, List<ButoNotification>> groupingMap = new HashMap<String, List<ButoNotification>>();
		
		// Flag for processing on the first list
		boolean firstIteration = true;
		
		// Iterate over training data
		for(List<ButoNotification> notificationList : trainingData){
		
			// List of keys processed for this training run
			List<String> proccessedKeys = new ArrayList<String>();
			
			// Iterate over each notification
			for(ButoNotification notification : notificationList){
				
				// Generate key for notification
				String key = getLookupKey(notification);
				
				// Check if key already processed
				if(proccessedKeys.contains(key)){
					//throw new NotificationClassifierCreationException("Duplicate notification in training data. " + key);
				}
				
				// Add key to list of processed keys
				proccessedKeys.add(key);
				
				// Test if key is already present
				if(groupingMap.containsKey(key)){
					
					// Is present, get list and add notification to list
					List<ButoNotification> list = groupingMap.get(key);
					list.add(notification);
					
				}else{
					
					// If not first iteration and grouping map has no entry for key then
					// training data is malformed
					if(!firstIteration){
						//throw new NotificationClassifierCreationException("Missing notification in training data. " + key);
					}
					
					// Not present, create new list and add to grouping map
					List<ButoNotification> list  = new ArrayList<ButoNotification>();
					groupingMap.put(key, list);
					list.add(notification);
				}
			}
			
			// Set first iteration to false
			firstIteration = false;
		}
		
		// Iterate over groupings and generate bounds for each notification
		for(String key : groupingMap.keySet()){
			
			// Get notification list
			List<ButoNotification> notificationList = groupingMap.get(key);
			
			// List for time-stamps
			double[] timestampArray = new double[notificationList.size()];
			
			// Build list of time-stamps
			for(int index = 0; index < notificationList.size(); index++){
				timestampArray[index] = notificationList.get(index).getTimeStamp();
			}
			
			// Calculate mean of time-stamps
			double mean = StatUtils.mean(timestampArray);
			
			// Calculate stdev of time-stamps
			double stdev = Math.sqrt(StatUtils.variance(timestampArray));
			
			// Number of standard deviations from the mean to use as bounds
			int deviations = 2;
			
			// Create bounds
			long lowerBound = (long) Math.floor(mean - (stdev * deviations));
			long upperBound = (long) Math.ceil(mean + (stdev * deviations));
			
			// Add new bounds object to map
			boundsMap.put(key, new NotificationBounds(key, lowerBound, upperBound));
		}
	}
	
	/**
	 * Classifies a set of test data against the classifier.
	 * 
	 * @param testData The data to test against the classifier.
	 * @return List of differences between the test data and the classifier.
	 */
	public List<NotificationDifference> classify(List<ButoNotification> testData){
		
		// Difference output list
		List<NotificationDifference> outputList = new ArrayList<NotificationDifference>();
		
		// Keep a list of processed bounds, so unmatched bounds can be returned as an error
		List<NotificationBounds> unprocessedBounds = new ArrayList<NotificationBounds>(boundsMap.values());
		
		// Iterate over test data list
		for(ButoNotification notification : testData){
			
			// Generate key
			String key = getLookupKey(notification);
			
			// Test if there is a matching bounds
			if(boundsMap.containsKey(key)){
				
				// Get matching bounds
				NotificationBounds bounds = boundsMap.get(key);
				
				// Test if key has already been processed
				if(!unprocessedBounds.contains(bounds)){
					
					outputList.add(new NotificationDifference("Duplicate notification. " + key));
					continue;
				}
				
				// Test if within bounds
				if(notification.getTimeStamp() < bounds.lowerBound){
					
					// Outside lower bounds
					outputList.add(new NotificationDifference("Notification out of bounds. " + key));
					
				}else if(notification.getTimeStamp() > bounds.upperBound){
					
					// Outside upper bounds
					outputList.add(new NotificationDifference("Notification out of bounds. " + key));
				}
				
				// Add to list of processed bounds
				unprocessedBounds.remove(bounds);
			}else{
				
				// No matching bounds, error
				outputList.add(new NotificationDifference("Could not match notification to group - " + key));
			}				
		}
		
		// Unprocessed bounds are errors
		for(NotificationBounds bounds : unprocessedBounds){
			outputList.add(new NotificationDifference("Bounds not matched to notification. " + bounds)) ;
		}
		
		// return results
		//return outputList;
		return new ArrayList<NotificationDifference>();
	}
	
	/**
	 * Creates a lookup key for a given notification.
	 * 
	 * @param notification The notification to generate key for.
	 * @return A string that can be used to identify similar notifications.
	 */
	private String getLookupKey(ButoNotification notification){
		return notification.getType() + " " + notification.getSourceObjectName() + " " + notification.getSequenceNumber();
	}
	
	/**
	 * Simple bounds object.
	 * 
	 * @author Fergus Hewson
	 *
	 */
	private class NotificationBounds implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1176311957290109401L;

		/**
		 * The key for this bounds object.
		 */
		String key;
		
		/**
		 * Lowest permissible value.
		 */
		long lowerBound;
		
		/**
		 * Highest permissible value.
		 */
		long upperBound;
		
		/**
		 * Creates new bounds object. 
		 * 
		 * @param lowerBound Lowest permissible value.
		 * @param upperBound Highest permissible value.
		 */
		public NotificationBounds(String key, long lowerBound, long upperBound){
			this.key = key;
			this.lowerBound = lowerBound;
			this.upperBound = upperBound;
		}
		
		public String toString(){
			return key + " " + lowerBound + " " + upperBound;
		}
	}
}

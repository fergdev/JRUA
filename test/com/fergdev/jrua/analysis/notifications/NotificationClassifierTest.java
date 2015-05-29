package nz.ac.massey.buto.analysis.notifications;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import nz.ac.massey.buto.domain.ButoNotification;

import org.junit.Test;

/**
 * Set of tests for the notification classifier.
 * 
 * Classifier works by using a notifications Type, SourceObject and SequenceNumber as a key to identify notifications. Notifications
 * across all training run are then grouped by this key and a set of bounds for the timeStamp is then generated for each unique notification.
 * 
 * @author Fergus Hewson
 *
 */
public class NotificationClassifierTest {


	/**
	 * Creates a simple data set for testing.
	 * 
	 * @return A test data set for notifications.
	 */
	public List<List<ButoNotification>> getSimpleData(){
		
		//*****************************************************************
		// Positive Data
		List<List<ButoNotification>> notificationData = new ArrayList<List<ButoNotification>>();
		
		// Positive 1
		List<ButoNotification> notificationList1 = new ArrayList<ButoNotification>();
		notificationData.add(notificationList1);
		
		notificationList1.add(new ButoNotification("Type 1", "Object 1", 0, 1000, "Hello World"));
		notificationList1.add(new ButoNotification("Type 1", "Object 1", 1, 2000, "Hello World"));
		notificationList1.add(new ButoNotification("Type 1", "Object 1", 2, 3000, "Hello World"));
		notificationList1.add(new ButoNotification("Type 1", "Object 1", 3, 4000, "Hello World"));
		notificationList1.add(new ButoNotification("Type 1", "Object 1", 4, 5000, "Hello World"));
		
		notificationList1.add(new ButoNotification("Type 2", "Object 1", 0, 500, "Hello World"));
		notificationList1.add(new ButoNotification("Type 2", "Object 1", 1, 600, "Hello World"));
		notificationList1.add(new ButoNotification("Type 2", "Object 1", 2, 700, "Hello World"));
		notificationList1.add(new ButoNotification("Type 2", "Object 1", 3, 800, "Hello World"));
		notificationList1.add(new ButoNotification("Type 2", "Object 1", 4, 900, "Hello World"));
		
		notificationList1.add(new ButoNotification("Type 3", "Object 2", 0, 100, "Hello World"));
		notificationList1.add(new ButoNotification("Type 3", "Object 2", 1, 1800, "Hello World"));
		notificationList1.add(new ButoNotification("Type 3", "Object 2", 2, 3600, "Hello World"));
		
		notificationList1.add(new ButoNotification("Type 3", "Object 3", 0, 100, "Hello World"));
		notificationList1.add(new ButoNotification("Type 3", "Object 3", 1, 1800, "Hello World"));
		notificationList1.add(new ButoNotification("Type 3", "Object 3", 2, 3600, "Hello World"));
		
		// Positive 2
		List<ButoNotification> notificationList2 = new ArrayList<ButoNotification>();
		notificationData.add(notificationList2);
		
		notificationList2.add(new ButoNotification("Type 1", "Object 1", 0, 1000, "Hello World"));
		notificationList2.add(new ButoNotification("Type 1", "Object 1", 1, 2000, "Hello World"));
		notificationList2.add(new ButoNotification("Type 1", "Object 1", 2, 3000, "Hello World"));
		notificationList2.add(new ButoNotification("Type 1", "Object 1", 3, 4000, "Hello World"));
		notificationList2.add(new ButoNotification("Type 1", "Object 1", 4, 5000, "Hello World"));
		
		notificationList2.add(new ButoNotification("Type 2", "Object 1", 0, 500, "Hello World"));
		notificationList2.add(new ButoNotification("Type 2", "Object 1", 1, 600, "Hello World"));
		notificationList2.add(new ButoNotification("Type 2", "Object 1", 2, 700, "Hello World"));
		notificationList2.add(new ButoNotification("Type 2", "Object 1", 3, 800, "Hello World"));
		notificationList2.add(new ButoNotification("Type 2", "Object 1", 4, 900, "Hello World"));
		
		notificationList2.add(new ButoNotification("Type 3", "Object 2", 0, 100, "Hello World"));
		notificationList2.add(new ButoNotification("Type 3", "Object 2", 1, 1800, "Hello World"));
		notificationList2.add(new ButoNotification("Type 3", "Object 2", 2, 3600, "Hello World"));
		
		notificationList2.add(new ButoNotification("Type 3", "Object 3", 0, 100, "Hello World"));
		notificationList2.add(new ButoNotification("Type 3", "Object 3", 1, 1800, "Hello World"));
		notificationList2.add(new ButoNotification("Type 3", "Object 3", 2, 3600, "Hello World"));
		
		// Positive 3
		List<ButoNotification> notificationList3 = new ArrayList<ButoNotification>();
		notificationData.add(notificationList3);
		
		notificationList3.add(new ButoNotification("Type 1", "Object 1", 0, 1000, "Hello World"));
		notificationList3.add(new ButoNotification("Type 1", "Object 1", 1, 2000, "Hello World"));
		notificationList3.add(new ButoNotification("Type 1", "Object 1", 2, 3000, "Hello World"));
		notificationList3.add(new ButoNotification("Type 1", "Object 1", 3, 4000, "Hello World"));
		notificationList3.add(new ButoNotification("Type 1", "Object 1", 4, 5000, "Hello World"));
		
		notificationList3.add(new ButoNotification("Type 2", "Object 1", 0, 500, "Hello World"));
		notificationList3.add(new ButoNotification("Type 2", "Object 1", 1, 600, "Hello World"));
		notificationList3.add(new ButoNotification("Type 2", "Object 1", 2, 700, "Hello World"));
		notificationList3.add(new ButoNotification("Type 2", "Object 1", 3, 800, "Hello World"));
		notificationList3.add(new ButoNotification("Type 2", "Object 1", 4, 900, "Hello World"));
		
		notificationList3.add(new ButoNotification("Type 3", "Object 2", 0, 100, "Hello World"));
		notificationList3.add(new ButoNotification("Type 3", "Object 2", 1, 1800, "Hello World"));
		notificationList3.add(new ButoNotification("Type 3", "Object 2", 2, 3600, "Hello World"));
		
		notificationList3.add(new ButoNotification("Type 3", "Object 3", 0, 100, "Hello World"));
		notificationList3.add(new ButoNotification("Type 3", "Object 3", 1, 1800, "Hello World"));
		notificationList3.add(new ButoNotification("Type 3", "Object 3", 2, 3600, "Hello World"));		
		
		// Positive 4
		List<ButoNotification> notificationList4 = new ArrayList<ButoNotification>();
		notificationData.add(notificationList4);
		
		notificationList4.add(new ButoNotification("Type 1", "Object 1", 0, 1000, "Hello World"));
		notificationList4.add(new ButoNotification("Type 1", "Object 1", 1, 2000, "Hello World"));
		notificationList4.add(new ButoNotification("Type 1", "Object 1", 2, 3000, "Hello World"));
		notificationList4.add(new ButoNotification("Type 1", "Object 1", 3, 4000, "Hello World"));
		notificationList4.add(new ButoNotification("Type 1", "Object 1", 4, 5000, "Hello World"));	
		
		notificationList4.add(new ButoNotification("Type 2", "Object 1", 0, 500, "Hello World"));
		notificationList4.add(new ButoNotification("Type 2", "Object 1", 1, 600, "Hello World"));
		notificationList4.add(new ButoNotification("Type 2", "Object 1", 2, 700, "Hello World"));
		notificationList4.add(new ButoNotification("Type 2", "Object 1", 3, 800, "Hello World"));
		notificationList4.add(new ButoNotification("Type 2", "Object 1", 4, 900, "Hello World"));		
		
		notificationList4.add(new ButoNotification("Type 3", "Object 2", 0, 100, "Hello World"));
		notificationList4.add(new ButoNotification("Type 3", "Object 2", 1, 1800, "Hello World"));
		notificationList4.add(new ButoNotification("Type 3", "Object 2", 2, 3600, "Hello World"));
		
		notificationList4.add(new ButoNotification("Type 3", "Object 3", 0, 100, "Hello World"));
		notificationList4.add(new ButoNotification("Type 3", "Object 3", 1, 1800, "Hello World"));
		notificationList4.add(new ButoNotification("Type 3", "Object 3", 2, 3600, "Hello World"));		
		
		// Positive 5
		List<ButoNotification> notificationList5 = new ArrayList<ButoNotification>();
		notificationData.add(notificationList5);
		
		notificationList5.add(new ButoNotification("Type 1", "Object 1", 0, 1000, "Hello World"));
		notificationList5.add(new ButoNotification("Type 1", "Object 1", 1, 2000, "Hello World"));
		notificationList5.add(new ButoNotification("Type 1", "Object 1", 2, 3000, "Hello World"));
		notificationList5.add(new ButoNotification("Type 1", "Object 1", 3, 4000, "Hello World"));
		notificationList5.add(new ButoNotification("Type 1", "Object 1", 4, 5000, "Hello World"));	
		
		notificationList5.add(new ButoNotification("Type 2", "Object 1", 0, 500, "Hello World"));
		notificationList5.add(new ButoNotification("Type 2", "Object 1", 1, 600, "Hello World"));
		notificationList5.add(new ButoNotification("Type 2", "Object 1", 2, 700, "Hello World"));
		notificationList5.add(new ButoNotification("Type 2", "Object 1", 3, 800, "Hello World"));
		notificationList5.add(new ButoNotification("Type 2", "Object 1", 4, 900, "Hello World"));	
		
		notificationList5.add(new ButoNotification("Type 3", "Object 2", 0, 100, "Hello World"));
		notificationList5.add(new ButoNotification("Type 3", "Object 2", 1, 1800, "Hello World"));
		notificationList5.add(new ButoNotification("Type 3", "Object 2", 2, 3600, "Hello World"));
		
		notificationList5.add(new ButoNotification("Type 3", "Object 3", 0, 100, "Hello World"));
		notificationList5.add(new ButoNotification("Type 3", "Object 3", 1, 1800, "Hello World"));
		notificationList5.add(new ButoNotification("Type 3", "Object 3", 2, 3600, "Hello World"));		
		
		return notificationData;
	}
	
	/**
	 * Get a training set of data with variable timestamps.
	 * 
	 * @return Training set.
	 */
	public List<List<ButoNotification>> getVariantData(){
		
		//*****************************************************************
		// Positive Data
		List<List<ButoNotification>> notificationData = new ArrayList<List<ButoNotification>>();
		
		// Positive 1
		List<ButoNotification> notificationList1 = new ArrayList<ButoNotification>();
		notificationData.add(notificationList1);
		
		notificationList1.add(new ButoNotification("Type 1", "Object 1", 0, 950, "Hello World"));
		notificationList1.add(new ButoNotification("Type 1", "Object 1", 1, 1950, "Hello World"));
		notificationList1.add(new ButoNotification("Type 1", "Object 1", 2, 2950, "Hello World"));
		notificationList1.add(new ButoNotification("Type 1", "Object 1", 3, 3950, "Hello World"));
		notificationList1.add(new ButoNotification("Type 1", "Object 1", 4, 4950, "Hello World"));
		
		notificationList1.add(new ButoNotification("Type 2", "Object 1", 0, 475, "Hello World"));
		notificationList1.add(new ButoNotification("Type 2", "Object 1", 1, 575, "Hello World"));
		notificationList1.add(new ButoNotification("Type 2", "Object 1", 2, 675, "Hello World"));
		notificationList1.add(new ButoNotification("Type 2", "Object 1", 3, 775, "Hello World"));
		notificationList1.add(new ButoNotification("Type 2", "Object 1", 4, 875, "Hello World"));
		
		notificationList1.add(new ButoNotification("Type 3", "Object 2", 0, 50, "Hello World"));
		notificationList1.add(new ButoNotification("Type 3", "Object 2", 1, 1750, "Hello World"));
		notificationList1.add(new ButoNotification("Type 3", "Object 2", 2, 3550, "Hello World"));
		
		notificationList1.add(new ButoNotification("Type 3", "Object 3", 0, 50, "Hello World"));
		notificationList1.add(new ButoNotification("Type 3", "Object 3", 1, 1750, "Hello World"));
		notificationList1.add(new ButoNotification("Type 3", "Object 3", 2, 3550, "Hello World"));
		
		// Positive 2
		List<ButoNotification> notificationList2 = new ArrayList<ButoNotification>();
		notificationData.add(notificationList2);
		
		notificationList2.add(new ButoNotification("Type 1", "Object 1", 0, 1050, "Hello World"));
		notificationList2.add(new ButoNotification("Type 1", "Object 1", 1, 2050, "Hello World"));
		notificationList2.add(new ButoNotification("Type 1", "Object 1", 2, 3050, "Hello World"));
		notificationList2.add(new ButoNotification("Type 1", "Object 1", 3, 4050, "Hello World"));
		notificationList2.add(new ButoNotification("Type 1", "Object 1", 4, 5050, "Hello World"));
		
		notificationList2.add(new ButoNotification("Type 2", "Object 1", 0, 525, "Hello World"));
		notificationList2.add(new ButoNotification("Type 2", "Object 1", 1, 625, "Hello World"));
		notificationList2.add(new ButoNotification("Type 2", "Object 1", 2, 725, "Hello World"));
		notificationList2.add(new ButoNotification("Type 2", "Object 1", 3, 825, "Hello World"));
		notificationList2.add(new ButoNotification("Type 2", "Object 1", 4, 925, "Hello World"));
		
		notificationList2.add(new ButoNotification("Type 3", "Object 2", 0, 150, "Hello World"));
		notificationList2.add(new ButoNotification("Type 3", "Object 2", 1, 1850, "Hello World"));
		notificationList2.add(new ButoNotification("Type 3", "Object 2", 2, 3650, "Hello World"));
		
		notificationList2.add(new ButoNotification("Type 3", "Object 3", 0, 150, "Hello World"));
		notificationList2.add(new ButoNotification("Type 3", "Object 3", 1, 1850, "Hello World"));
		notificationList2.add(new ButoNotification("Type 3", "Object 3", 2, 3650, "Hello World"));
		
		// Positive 3
		List<ButoNotification> notificationList3 = new ArrayList<ButoNotification>();
		notificationData.add(notificationList3);
		
		notificationList3.add(new ButoNotification("Type 1", "Object 1", 0, 975, "Hello World"));
		notificationList3.add(new ButoNotification("Type 1", "Object 1", 1, 1975, "Hello World"));
		notificationList3.add(new ButoNotification("Type 1", "Object 1", 2, 2975, "Hello World"));
		notificationList3.add(new ButoNotification("Type 1", "Object 1", 3, 3975, "Hello World"));
		notificationList3.add(new ButoNotification("Type 1", "Object 1", 4, 4975, "Hello World"));
		
		notificationList3.add(new ButoNotification("Type 2", "Object 1", 0, 490, "Hello World"));
		notificationList3.add(new ButoNotification("Type 2", "Object 1", 1, 560, "Hello World"));
		notificationList3.add(new ButoNotification("Type 2", "Object 1", 2, 690, "Hello World"));
		notificationList3.add(new ButoNotification("Type 2", "Object 1", 3, 790, "Hello World"));
		notificationList3.add(new ButoNotification("Type 2", "Object 1", 4, 890, "Hello World"));
		
		notificationList3.add(new ButoNotification("Type 3", "Object 2", 0, 90, "Hello World"));
		notificationList3.add(new ButoNotification("Type 3", "Object 2", 1, 1790, "Hello World"));
		notificationList3.add(new ButoNotification("Type 3", "Object 2", 2, 3590, "Hello World"));
		
		notificationList3.add(new ButoNotification("Type 3", "Object 3", 0, 90, "Hello World"));
		notificationList3.add(new ButoNotification("Type 3", "Object 3", 1, 1800, "Hello World"));
		notificationList3.add(new ButoNotification("Type 3", "Object 3", 2, 3590, "Hello World"));		
		
		// Positive 4
		List<ButoNotification> notificationList4 = new ArrayList<ButoNotification>();
		notificationData.add(notificationList4);
		
		notificationList4.add(new ButoNotification("Type 1", "Object 1", 0, 1025, "Hello World"));
		notificationList4.add(new ButoNotification("Type 1", "Object 1", 1, 2025, "Hello World"));
		notificationList4.add(new ButoNotification("Type 1", "Object 1", 2, 3025, "Hello World"));
		notificationList4.add(new ButoNotification("Type 1", "Object 1", 3, 4025, "Hello World"));
		notificationList4.add(new ButoNotification("Type 1", "Object 1", 4, 5025, "Hello World"));	
		
		notificationList4.add(new ButoNotification("Type 2", "Object 1", 0, 510, "Hello World"));
		notificationList4.add(new ButoNotification("Type 2", "Object 1", 1, 610, "Hello World"));
		notificationList4.add(new ButoNotification("Type 2", "Object 1", 2, 710, "Hello World"));
		notificationList4.add(new ButoNotification("Type 2", "Object 1", 3, 810, "Hello World"));
		notificationList4.add(new ButoNotification("Type 2", "Object 1", 4, 910, "Hello World"));		
		
		notificationList4.add(new ButoNotification("Type 3", "Object 2", 0, 110, "Hello World"));
		notificationList4.add(new ButoNotification("Type 3", "Object 2", 1, 1810, "Hello World"));
		notificationList4.add(new ButoNotification("Type 3", "Object 2", 2, 3610, "Hello World"));
		
		notificationList4.add(new ButoNotification("Type 3", "Object 3", 0, 110, "Hello World"));
		notificationList4.add(new ButoNotification("Type 3", "Object 3", 1, 1810, "Hello World"));
		notificationList4.add(new ButoNotification("Type 3", "Object 3", 2, 3610, "Hello World"));		
		
		// Positive 5
		List<ButoNotification> notificationList5 = new ArrayList<ButoNotification>();
		notificationData.add(notificationList5);
		
		notificationList5.add(new ButoNotification("Type 1", "Object 1", 0, 1000, "Hello World"));
		notificationList5.add(new ButoNotification("Type 1", "Object 1", 1, 2000, "Hello World"));
		notificationList5.add(new ButoNotification("Type 1", "Object 1", 2, 3000, "Hello World"));
		notificationList5.add(new ButoNotification("Type 1", "Object 1", 3, 4000, "Hello World"));
		notificationList5.add(new ButoNotification("Type 1", "Object 1", 4, 5000, "Hello World"));	
		
		notificationList5.add(new ButoNotification("Type 2", "Object 1", 0, 500, "Hello World"));
		notificationList5.add(new ButoNotification("Type 2", "Object 1", 1, 600, "Hello World"));
		notificationList5.add(new ButoNotification("Type 2", "Object 1", 2, 700, "Hello World"));
		notificationList5.add(new ButoNotification("Type 2", "Object 1", 3, 800, "Hello World"));
		notificationList5.add(new ButoNotification("Type 2", "Object 1", 4, 900, "Hello World"));	
		
		notificationList5.add(new ButoNotification("Type 3", "Object 2", 0, 100, "Hello World"));
		notificationList5.add(new ButoNotification("Type 3", "Object 2", 1, 1800, "Hello World"));
		notificationList5.add(new ButoNotification("Type 3", "Object 2", 2, 3600, "Hello World"));
		
		notificationList5.add(new ButoNotification("Type 3", "Object 3", 0, 100, "Hello World"));
		notificationList5.add(new ButoNotification("Type 3", "Object 3", 1, 1800, "Hello World"));
		notificationList5.add(new ButoNotification("Type 3", "Object 3", 2, 3600, "Hello World"));		
		
		return notificationData;
	}
	
	/**
	 * Tests an empty training set against an empty test set.
	 * @throws NotificationClassifierCreationException 
	 */
	@Test
	public void emptyClassifierTest() throws NotificationClassifierCreationException{
		
		// Empty training set
		List<List<ButoNotification>> notificationData = new ArrayList<List<ButoNotification>>();
		notificationData.add(new ArrayList<ButoNotification>());
		
		// Empty test set
		List<ButoNotification> testData = new ArrayList<ButoNotification>();
		
		// Build classifier
		NotificationClassifier classifier = new NotificationClassifier(notificationData);
		
		List<NotificationDifference> differences = classifier.classify(testData);
		assertTrue(0 == differences.size());
	}
	
	/**
	 * Uses the simple data set as training data and then all the test runs as positive examples.
	 * @throws NotificationClassifierCreationException 
	 */
	@Test
	public void positiveTest() throws NotificationClassifierCreationException{
		
		List<List<ButoNotification>> notificationData = getSimpleData();
		
		//*****************************************************************
		// Build the classifier
		NotificationClassifier classifier = new NotificationClassifier(notificationData);
		
		//*****************************************************************
		// Positive Examples
		List<NotificationDifference> positiveDifferences1 = classifier.classify(notificationData.get(0));
		assertEquals(0, positiveDifferences1.size());
		
		List<NotificationDifference> positiveDifferences2 = classifier.classify(notificationData.get(1));
		assertEquals(0, positiveDifferences2.size());
		
		List<NotificationDifference> positiveDifferences3 = classifier.classify(notificationData.get(2));
		assertEquals(0, positiveDifferences3.size());
		
		List<NotificationDifference> positiveDifferences4 = classifier.classify(notificationData.get(3));
		assertEquals(0, positiveDifferences4.size());	
		
		List<NotificationDifference> positiveDifferences5 = classifier.classify(notificationData.get(4));
		assertEquals(0, positiveDifferences5.size());		
		
	}
	
	/**
	 * Trains with the simple training set and test using an empty training set.
	 * @throws NotificationClassifierCreationException 
	 */
	@Test
	public void emptyTest() throws NotificationClassifierCreationException{

		// Create empty list
		List<ButoNotification> negativeNotificationList1 = new ArrayList<ButoNotification>();
		
		// Create classifier
		NotificationClassifier classifier = new NotificationClassifier(getSimpleData());
		
		// Test for differences
		List<NotificationDifference> negativeDifferences1 = classifier.classify(negativeNotificationList1);
		assertTrue(0 != negativeDifferences1.size());		
	}
	
	@Test
	public void differentSimpleTest() throws NotificationClassifierCreationException{
		
		//*****************************************************************
		// Negative examples
		List<ButoNotification> negativeNotificationList1 = new ArrayList<ButoNotification>();
		
		negativeNotificationList1.add(new ButoNotification("Type 1", "Object 1", 0, 500, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 1", "Object 1", 1, 2000, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 1", "Object 1", 2, 3000, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 1", "Object 1", 3, 4000, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 1", "Object 1", 4, 5000, "Hello World"));	
		
		negativeNotificationList1.add(new ButoNotification("Type 2", "Object 1", 0, 500, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 2", "Object 1", 1, 600, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 2", "Object 1", 2, 700, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 2", "Object 1", 3, 800, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 2", "Object 1", 4, 900, "Hello World"));	
		
		negativeNotificationList1.add(new ButoNotification("Type 3", "Object 2", 0, 100, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 3", "Object 2", 1, 1800, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 3", "Object 2", 2, 3600, "Hello World"));
		
		negativeNotificationList1.add(new ButoNotification("Type 3", "Object 3", 0, 100, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 3", "Object 3", 1, 1800, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 3", "Object 3", 2, 3600, "Hello World"));		
		
		List<ButoNotification> negativeNotificationList2 = new ArrayList<ButoNotification>();
		
		negativeNotificationList2.add(new ButoNotification("Type 1", "Object 1", 0, 500, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 1", "Object 1", 1, 2000, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 1", "Object 1", 2, 3000, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 1", "Object 1", 3, 4000, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 1", "Object 1", 4, 5000, "Hello World"));	
		
		negativeNotificationList2.add(new ButoNotification("Type 2", "Object 1", 0, 1000, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 2", "Object 1", 1, 600, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 2", "Object 1", 2, 700, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 2", "Object 1", 3, 800, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 2", "Object 1", 4, 900, "Hello World"));	
		
		negativeNotificationList2.add(new ButoNotification("Type 3", "Object 2", 0, 100, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 3", "Object 2", 1, 1800, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 3", "Object 2", 2, 3600, "Hello World"));
		
		negativeNotificationList2.add(new ButoNotification("Type 3", "Object 3", 0, 100, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 3", "Object 3", 1, 1800, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 3", "Object 3", 2, 3600, "Hello World"));
		
		List<ButoNotification> negativeNotificationList3 = new ArrayList<ButoNotification>();
		
		negativeNotificationList3.add(new ButoNotification("Type 1", "Object 1", 0, 500, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 1", "Object 1", 1, 2000, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 1", "Object 1", 2, 3000, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 1", "Object 1", 3, 4000, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 1", "Object 1", 4, 5000, "Hello World"));	
		
		negativeNotificationList3.add(new ButoNotification("Type 2", "Object 1", 0, 200, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 2", "Object 1", 1, 600, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 2", "Object 1", 2, 700, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 2", "Object 1", 3, 800, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 2", "Object 1", 4, 900, "Hello World"));	
		
		negativeNotificationList3.add(new ButoNotification("Type 3", "Object 2", 0, 10, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 3", "Object 2", 1, 1800, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 3", "Object 2", 2, 3600, "Hello World"));
		
		negativeNotificationList3.add(new ButoNotification("Type 3", "Object 3", 0, 100, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 3", "Object 3", 1, 1800, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 3", "Object 3", 2, 3600, "Hello World"));
		
		List<ButoNotification> negativeNotificationList4 = new ArrayList<ButoNotification>();
		
		negativeNotificationList4.add(new ButoNotification("Type 1", "Object 1", 0, 500, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 1", "Object 1", 1, 2000, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 1", "Object 1", 2, 3000, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 1", "Object 1", 3, 4000, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 1", "Object 1", 4, 5000, "Hello World"));	
		
		negativeNotificationList4.add(new ButoNotification("Type 2", "Object 1", 0, 200, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 2", "Object 1", 1, 600, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 2", "Object 1", 2, 700, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 2", "Object 1", 3, 800, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 2", "Object 1", 4, 900, "Hello World"));	
		
		negativeNotificationList4.add(new ButoNotification("Type 3", "Object 2", 0, 10, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 3", "Object 2", 1, 1800, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 3", "Object 2", 2, 3600, "Hello World"));
		
		negativeNotificationList4.add(new ButoNotification("Type 3", "Object 3", 0, 1000, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 3", "Object 3", 1, 1800, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 3", "Object 3", 2, 3600, "Hello World"));
		
		List<ButoNotification> negativeNotificationList5 = new ArrayList<ButoNotification>();
		
		negativeNotificationList5.add(new ButoNotification("Type 1", "Object 1", 0, 500, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 1", "Object 1", 1, 2500, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 1", "Object 1", 2, 3000, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 1", "Object 1", 3, 4000, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 1", "Object 1", 4, 5000, "Hello World"));	
		
		negativeNotificationList5.add(new ButoNotification("Type 2", "Object 1", 0, 200, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 2", "Object 1", 1, 600, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 2", "Object 1", 2, 700, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 2", "Object 1", 3, 800, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 2", "Object 1", 4, 900, "Hello World"));	
		
		negativeNotificationList5.add(new ButoNotification("Type 3", "Object 2", 0, 10, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 3", "Object 2", 1, 1800, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 3", "Object 2", 2, 3600, "Hello World"));
		
		negativeNotificationList5.add(new ButoNotification("Type 3", "Object 3", 0, 1000, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 3", "Object 3", 1, 1800, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 3", "Object 3", 2, 3600, "Hello World"));
		
		//*****************************************************************
		// Negative examples
		NotificationClassifier classifier = new NotificationClassifier(getSimpleData());
		
		List<NotificationDifference> negativeDifferences1 = classifier.classify(negativeNotificationList1);
		assertEquals(1, negativeDifferences1.size());		
		
		List<NotificationDifference> negativeDifferences2 = classifier.classify(negativeNotificationList2);
		assertEquals(2, negativeDifferences2.size());	
		
		List<NotificationDifference> negativeDifferences3 = classifier.classify(negativeNotificationList3);
		assertEquals(3, negativeDifferences3.size());	
		
		List<NotificationDifference> negativeDifferences4 = classifier.classify(negativeNotificationList4);
		assertEquals(4, negativeDifferences4.size());	

		List<NotificationDifference> negativeDifferences5 = classifier.classify(negativeNotificationList5);
		assertEquals(5, negativeDifferences5.size());	
	}
	
	/**
	 * Tests the detection of missing notifications.
	 * @throws NotificationClassifierCreationException 
	 */
	@Test
	public void missingSimpleTest() throws NotificationClassifierCreationException{
		
		//*****************************************************************
		// Negative examples
		List<ButoNotification> negativeNotificationList1 = new ArrayList<ButoNotification>();
		
		negativeNotificationList1.add(new ButoNotification("Type 1", "Object 1", 0, 1000, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 1", "Object 1", 1, 2000, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 1", "Object 1", 2, 3000, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 1", "Object 1", 3, 4000, "Hello World"));
		
		negativeNotificationList1.add(new ButoNotification("Type 2", "Object 1", 0, 500, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 2", "Object 1", 1, 600, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 2", "Object 1", 2, 700, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 2", "Object 1", 3, 800, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 2", "Object 1", 4, 900, "Hello World"));	
		
		negativeNotificationList1.add(new ButoNotification("Type 3", "Object 2", 0, 100, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 3", "Object 2", 1, 1800, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 3", "Object 2", 2, 3600, "Hello World"));
		
		negativeNotificationList1.add(new ButoNotification("Type 3", "Object 3", 0, 100, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 3", "Object 3", 1, 1800, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 3", "Object 3", 2, 3600, "Hello World"));		
		
		List<ButoNotification> negativeNotificationList2 = new ArrayList<ButoNotification>();
		
		negativeNotificationList2.add(new ButoNotification("Type 1", "Object 1", 0, 1000, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 1", "Object 1", 1, 2000, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 1", "Object 1", 2, 3000, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 1", "Object 1", 3, 4000, "Hello World"));
		
		negativeNotificationList2.add(new ButoNotification("Type 2", "Object 1", 0, 500, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 2", "Object 1", 1, 600, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 2", "Object 1", 2, 700, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 2", "Object 1", 3, 800, "Hello World"));
		
		negativeNotificationList2.add(new ButoNotification("Type 3", "Object 2", 0, 100, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 3", "Object 2", 1, 1800, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 3", "Object 2", 2, 3600, "Hello World"));
		
		negativeNotificationList2.add(new ButoNotification("Type 3", "Object 3", 0, 100, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 3", "Object 3", 1, 1800, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 3", "Object 3", 2, 3600, "Hello World"));
		
		List<ButoNotification> negativeNotificationList3 = new ArrayList<ButoNotification>();
		
		negativeNotificationList3.add(new ButoNotification("Type 1", "Object 1", 0, 1000, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 1", "Object 1", 1, 2000, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 1", "Object 1", 2, 3000, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 1", "Object 1", 3, 4000, "Hello World"));
		
		negativeNotificationList3.add(new ButoNotification("Type 2", "Object 1", 0, 500, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 2", "Object 1", 1, 600, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 2", "Object 1", 2, 700, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 2", "Object 1", 3, 800, "Hello World"));
		
		negativeNotificationList3.add(new ButoNotification("Type 3", "Object 2", 0, 100, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 3", "Object 2", 1, 1800, "Hello World"));
		
		negativeNotificationList3.add(new ButoNotification("Type 3", "Object 3", 0, 100, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 3", "Object 3", 1, 1800, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 3", "Object 3", 2, 3600, "Hello World"));
		
		List<ButoNotification> negativeNotificationList4 = new ArrayList<ButoNotification>();
		
		negativeNotificationList4.add(new ButoNotification("Type 1", "Object 1", 0, 1000, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 1", "Object 1", 1, 2000, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 1", "Object 1", 2, 3000, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 1", "Object 1", 3, 4000, "Hello World"));
		
		negativeNotificationList4.add(new ButoNotification("Type 2", "Object 1", 0, 500, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 2", "Object 1", 1, 600, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 2", "Object 1", 2, 700, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 2", "Object 1", 3, 800, "Hello World"));
		
		negativeNotificationList4.add(new ButoNotification("Type 3", "Object 2", 0, 100, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 3", "Object 2", 1, 1800, "Hello World"));
		
		negativeNotificationList4.add(new ButoNotification("Type 3", "Object 3", 0, 100, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 3", "Object 3", 1, 1800, "Hello World"));
		
		List<ButoNotification> negativeNotificationList5 = new ArrayList<ButoNotification>();
		
		negativeNotificationList5.add(new ButoNotification("Type 1", "Object 1", 0, 1000, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 1", "Object 1", 1, 2000, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 1", "Object 1", 2, 3000, "Hello World"));
		
		negativeNotificationList5.add(new ButoNotification("Type 2", "Object 1", 0, 500, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 2", "Object 1", 1, 600, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 2", "Object 1", 2, 700, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 2", "Object 1", 3, 800, "Hello World"));
		
		negativeNotificationList5.add(new ButoNotification("Type 3", "Object 2", 0, 100, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 3", "Object 2", 1, 1800, "Hello World"));
		
		negativeNotificationList5.add(new ButoNotification("Type 3", "Object 3", 0, 100, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 3", "Object 3", 1, 1800, "Hello World"));
		
		//*****************************************************************
		// Negative examples
		NotificationClassifier classifier = new NotificationClassifier(getSimpleData());
		
		List<NotificationDifference> negativeDifferences1 = classifier.classify(negativeNotificationList1);
		assertEquals(1, negativeDifferences1.size());		
		
		List<NotificationDifference> negativeDifferences2 = classifier.classify(negativeNotificationList2);
		assertEquals(2, negativeDifferences2.size());	
		
		List<NotificationDifference> negativeDifferences3 = classifier.classify(negativeNotificationList3);
		assertEquals(3, negativeDifferences3.size());	
		
		List<NotificationDifference> negativeDifferences4 = classifier.classify(negativeNotificationList4);
		assertEquals(4, negativeDifferences4.size());	

		List<NotificationDifference> negativeDifferences5 = classifier.classify(negativeNotificationList5);
		assertEquals(5, negativeDifferences5.size());	
	}
	
	/**
	 * Tests the detection of extra notifications.
	 * @throws NotificationClassifierCreationException 
	 */
	@Test
	public void extraSimpleTest() throws NotificationClassifierCreationException{
		
		//*****************************************************************
		// Negative examples
		List<ButoNotification> negativeNotificationList1 = new ArrayList<ButoNotification>();
		
		negativeNotificationList1.add(new ButoNotification("Type 1", "Object 1", 0, 1000, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 1", "Object 1", 1, 2000, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 1", "Object 1", 2, 3000, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 1", "Object 1", 3, 4000, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 1", "Object 1", 4, 5000, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 1", "Object 1", 5, 6000, "Hello World"));
		
		negativeNotificationList1.add(new ButoNotification("Type 2", "Object 1", 0, 500, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 2", "Object 1", 1, 600, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 2", "Object 1", 2, 700, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 2", "Object 1", 3, 800, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 2", "Object 1", 4, 900, "Hello World"));
		
		negativeNotificationList1.add(new ButoNotification("Type 3", "Object 2", 0, 100, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 3", "Object 2", 1, 1800, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 3", "Object 2", 2, 3600, "Hello World"));
		
		negativeNotificationList1.add(new ButoNotification("Type 3", "Object 3", 0, 100, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 3", "Object 3", 1, 1800, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 3", "Object 3", 2, 3600, "Hello World"));
		
		List<ButoNotification> negativeNotificationList2 = new ArrayList<ButoNotification>();
		
		negativeNotificationList2.add(new ButoNotification("Type 1", "Object 1", 0, 1000, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 1", "Object 1", 1, 2000, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 1", "Object 1", 2, 3000, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 1", "Object 1", 3, 4000, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 1", "Object 1", 4, 5000, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 1", "Object 1", 5, 6000, "Hello World"));
		
		negativeNotificationList2.add(new ButoNotification("Type 2", "Object 1", 0, 500, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 2", "Object 1", 1, 600, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 2", "Object 1", 2, 700, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 2", "Object 1", 3, 800, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 2", "Object 1", 4, 900, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 2", "Object 1", 5, 1000, "Hello World"));
		
		negativeNotificationList2.add(new ButoNotification("Type 3", "Object 2", 0, 100, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 3", "Object 2", 1, 1800, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 3", "Object 2", 2, 3600, "Hello World"));
		
		negativeNotificationList2.add(new ButoNotification("Type 3", "Object 3", 0, 100, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 3", "Object 3", 1, 1800, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 3", "Object 3", 2, 3600, "Hello World"));
		
		List<ButoNotification> negativeNotificationList3 = new ArrayList<ButoNotification>();
		
		negativeNotificationList3.add(new ButoNotification("Type 1", "Object 1", 0, 1000, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 1", "Object 1", 1, 2000, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 1", "Object 1", 2, 3000, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 1", "Object 1", 3, 4000, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 1", "Object 1", 4, 5000, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 1", "Object 1", 5, 6000, "Hello World"));
		
		negativeNotificationList3.add(new ButoNotification("Type 2", "Object 1", 0, 500, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 2", "Object 1", 1, 600, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 2", "Object 1", 2, 700, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 2", "Object 1", 3, 800, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 2", "Object 1", 4, 900, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 2", "Object 1", 5, 1000, "Hello World"));
		
		negativeNotificationList3.add(new ButoNotification("Type 3", "Object 2", 0, 100, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 3", "Object 2", 1, 1800, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 3", "Object 2", 2, 3600, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 3", "Object 2", 3, 4000, "Hello World"));
		
		negativeNotificationList3.add(new ButoNotification("Type 3", "Object 3", 0, 100, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 3", "Object 3", 1, 1800, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 3", "Object 3", 2, 3600, "Hello World"));
		
		List<ButoNotification> negativeNotificationList4 = new ArrayList<ButoNotification>();
		
		negativeNotificationList4.add(new ButoNotification("Type 1", "Object 1", 0, 1000, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 1", "Object 1", 1, 2000, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 1", "Object 1", 2, 3000, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 1", "Object 1", 3, 4000, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 1", "Object 1", 4, 5000, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 1", "Object 1", 5, 6000, "Hello World"));
		
		negativeNotificationList4.add(new ButoNotification("Type 2", "Object 1", 0, 500, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 2", "Object 1", 1, 600, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 2", "Object 1", 2, 700, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 2", "Object 1", 3, 800, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 2", "Object 1", 4, 900, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 2", "Object 1", 5, 1000, "Hello World"));
		
		negativeNotificationList4.add(new ButoNotification("Type 3", "Object 2", 0, 100, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 3", "Object 2", 1, 1800, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 3", "Object 2", 2, 3600, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 3", "Object 2", 3, 4000, "Hello World"));
		
		negativeNotificationList4.add(new ButoNotification("Type 3", "Object 3", 0, 100, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 3", "Object 3", 1, 1800, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 3", "Object 3", 2, 3600, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 3", "Object 3", 3, 4000, "Hello World"));
		
		List<ButoNotification> negativeNotificationList5 = new ArrayList<ButoNotification>();
		
		negativeNotificationList5.add(new ButoNotification("Type 1", "Object 1", 0, 1000, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 1", "Object 1", 1, 2000, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 1", "Object 1", 2, 3000, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 1", "Object 1", 3, 4000, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 1", "Object 1", 4, 5000, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 1", "Object 1", 5, 6000, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 1", "Object 1", 6, 7000, "Hello World"));
		
		negativeNotificationList5.add(new ButoNotification("Type 2", "Object 1", 0, 500, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 2", "Object 1", 1, 600, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 2", "Object 1", 2, 700, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 2", "Object 1", 3, 800, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 2", "Object 1", 4, 900, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 2", "Object 1", 5, 1000, "Hello World"));
		
		negativeNotificationList5.add(new ButoNotification("Type 3", "Object 2", 0, 100, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 3", "Object 2", 1, 1800, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 3", "Object 2", 2, 3600, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 3", "Object 2", 3, 4000, "Hello World"));
		
		negativeNotificationList5.add(new ButoNotification("Type 3", "Object 3", 0, 100, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 3", "Object 3", 1, 1800, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 3", "Object 3", 2, 3600, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 3", "Object 3", 3, 4000, "Hello World"));
				
		//*****************************************************************
		// Negative examples
		NotificationClassifier classifier = new NotificationClassifier(getSimpleData());
		
		List<NotificationDifference> negativeDifferences1 = classifier.classify(negativeNotificationList1);
		assertEquals(1, negativeDifferences1.size());		
		
		List<NotificationDifference> negativeDifferences2 = classifier.classify(negativeNotificationList2);
		assertEquals(2, negativeDifferences2.size());	
		
		List<NotificationDifference> negativeDifferences3 = classifier.classify(negativeNotificationList3);
		assertEquals(3, negativeDifferences3.size());	
		
		List<NotificationDifference> negativeDifferences4 = classifier.classify(negativeNotificationList4);
		assertEquals(4, negativeDifferences4.size());	

		List<NotificationDifference> negativeDifferences5 = classifier.classify(negativeNotificationList5);
		assertEquals(5, negativeDifferences5.size());	
	}	
	
	/**
	 * Runs test against a classifier built from the variable data set.
	 * @throws NotificationClassifierCreationException 
	 */
	@Test
	public void testVariablePositive() throws NotificationClassifierCreationException{
		
		List<List<ButoNotification>> variableData = getVariantData();
		
		NotificationClassifier classifier = new NotificationClassifier(variableData);
		
		List<NotificationDifference> differences1 = classifier.classify(variableData.get(0));
		assertEquals(0, differences1.size());		
		
		List<NotificationDifference> differences2 = classifier.classify(variableData.get(1));
		assertEquals(0, differences2.size());	
		
		List<NotificationDifference> differences3 = classifier.classify(variableData.get(2));
		assertEquals(0, differences3.size());	
		
		List<NotificationDifference> differences4 = classifier.classify(variableData.get(3));
		assertEquals(0, differences4.size());	

		List<NotificationDifference> differences5 = classifier.classify(variableData.get(4));
		assertEquals(0, differences5.size());	
	}
	
	@Test
	public void differentVariantTest() throws NotificationClassifierCreationException{
		
		//*****************************************************************
		// Negative examples
		List<ButoNotification> negativeNotificationList1 = new ArrayList<ButoNotification>();
		
		negativeNotificationList1.add(new ButoNotification("Type 1", "Object 1", 0, 500, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 1", "Object 1", 1, 2000, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 1", "Object 1", 2, 3000, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 1", "Object 1", 3, 4000, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 1", "Object 1", 4, 5000, "Hello World"));	
		
		negativeNotificationList1.add(new ButoNotification("Type 2", "Object 1", 0, 500, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 2", "Object 1", 1, 600, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 2", "Object 1", 2, 700, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 2", "Object 1", 3, 800, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 2", "Object 1", 4, 900, "Hello World"));	
		
		negativeNotificationList1.add(new ButoNotification("Type 3", "Object 2", 0, 100, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 3", "Object 2", 1, 1800, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 3", "Object 2", 2, 3600, "Hello World"));
		
		negativeNotificationList1.add(new ButoNotification("Type 3", "Object 3", 0, 100, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 3", "Object 3", 1, 1800, "Hello World"));
		negativeNotificationList1.add(new ButoNotification("Type 3", "Object 3", 2, 3600, "Hello World"));		
		
		List<ButoNotification> negativeNotificationList2 = new ArrayList<ButoNotification>();
		
		negativeNotificationList2.add(new ButoNotification("Type 1", "Object 1", 0, 500, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 1", "Object 1", 1, 2000, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 1", "Object 1", 2, 3000, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 1", "Object 1", 3, 4000, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 1", "Object 1", 4, 5000, "Hello World"));	
		
		negativeNotificationList2.add(new ButoNotification("Type 2", "Object 1", 0, 1000, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 2", "Object 1", 1, 600, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 2", "Object 1", 2, 700, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 2", "Object 1", 3, 800, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 2", "Object 1", 4, 900, "Hello World"));	
		
		negativeNotificationList2.add(new ButoNotification("Type 3", "Object 2", 0, 100, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 3", "Object 2", 1, 1800, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 3", "Object 2", 2, 3600, "Hello World"));
		
		negativeNotificationList2.add(new ButoNotification("Type 3", "Object 3", 0, 100, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 3", "Object 3", 1, 1800, "Hello World"));
		negativeNotificationList2.add(new ButoNotification("Type 3", "Object 3", 2, 3600, "Hello World"));
		
		List<ButoNotification> negativeNotificationList3 = new ArrayList<ButoNotification>();
		
		negativeNotificationList3.add(new ButoNotification("Type 1", "Object 1", 0, 500, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 1", "Object 1", 1, 2000, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 1", "Object 1", 2, 3000, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 1", "Object 1", 3, 4000, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 1", "Object 1", 4, 5000, "Hello World"));	
		
		negativeNotificationList3.add(new ButoNotification("Type 2", "Object 1", 0, 200, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 2", "Object 1", 1, 600, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 2", "Object 1", 2, 700, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 2", "Object 1", 3, 800, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 2", "Object 1", 4, 900, "Hello World"));	
		
		negativeNotificationList3.add(new ButoNotification("Type 3", "Object 2", 0, 10, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 3", "Object 2", 1, 1800, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 3", "Object 2", 2, 3600, "Hello World"));
		
		negativeNotificationList3.add(new ButoNotification("Type 3", "Object 3", 0, 100, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 3", "Object 3", 1, 1800, "Hello World"));
		negativeNotificationList3.add(new ButoNotification("Type 3", "Object 3", 2, 3600, "Hello World"));
		
		List<ButoNotification> negativeNotificationList4 = new ArrayList<ButoNotification>();
		
		negativeNotificationList4.add(new ButoNotification("Type 1", "Object 1", 0, 500, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 1", "Object 1", 1, 2000, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 1", "Object 1", 2, 3000, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 1", "Object 1", 3, 4000, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 1", "Object 1", 4, 5000, "Hello World"));	
		
		negativeNotificationList4.add(new ButoNotification("Type 2", "Object 1", 0, 200, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 2", "Object 1", 1, 600, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 2", "Object 1", 2, 700, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 2", "Object 1", 3, 800, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 2", "Object 1", 4, 900, "Hello World"));	
		
		negativeNotificationList4.add(new ButoNotification("Type 3", "Object 2", 0, 10, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 3", "Object 2", 1, 1800, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 3", "Object 2", 2, 3600, "Hello World"));
		
		negativeNotificationList4.add(new ButoNotification("Type 3", "Object 3", 0, 1000, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 3", "Object 3", 1, 1800, "Hello World"));
		negativeNotificationList4.add(new ButoNotification("Type 3", "Object 3", 2, 3600, "Hello World"));
		
		List<ButoNotification> negativeNotificationList5 = new ArrayList<ButoNotification>();
		
		negativeNotificationList5.add(new ButoNotification("Type 1", "Object 1", 0, 500, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 1", "Object 1", 1, 2500, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 1", "Object 1", 2, 3000, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 1", "Object 1", 3, 4000, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 1", "Object 1", 4, 5000, "Hello World"));	
		
		negativeNotificationList5.add(new ButoNotification("Type 2", "Object 1", 0, 200, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 2", "Object 1", 1, 600, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 2", "Object 1", 2, 700, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 2", "Object 1", 3, 800, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 2", "Object 1", 4, 900, "Hello World"));	
		
		negativeNotificationList5.add(new ButoNotification("Type 3", "Object 2", 0, 10, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 3", "Object 2", 1, 1800, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 3", "Object 2", 2, 3600, "Hello World"));
		
		negativeNotificationList5.add(new ButoNotification("Type 3", "Object 3", 0, 1000, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 3", "Object 3", 1, 1800, "Hello World"));
		negativeNotificationList5.add(new ButoNotification("Type 3", "Object 3", 2, 3600, "Hello World"));
		
		//*****************************************************************
		// Negative examples
		NotificationClassifier classifier = new NotificationClassifier(getVariantData());
		
		List<NotificationDifference> negativeDifferences1 = classifier.classify(negativeNotificationList1);
		assertEquals(1, negativeDifferences1.size());		
		
		List<NotificationDifference> negativeDifferences2 = classifier.classify(negativeNotificationList2);
		assertEquals(2, negativeDifferences2.size());	
		
		List<NotificationDifference> negativeDifferences3 = classifier.classify(negativeNotificationList3);
		assertEquals(3, negativeDifferences3.size());	
		
		List<NotificationDifference> negativeDifferences4 = classifier.classify(negativeNotificationList4);
		assertEquals(4, negativeDifferences4.size());	

		List<NotificationDifference> negativeDifferences5 = classifier.classify(negativeNotificationList5);
		assertEquals(5, negativeDifferences5.size());	
	}
	
	@Test(expected = NotificationClassifierCreationException.class) 
	public void testCreationException() throws NotificationClassifierCreationException{
		List<List<ButoNotification>> notificationData = new ArrayList<List<ButoNotification>>();
		
		// Positive 1
		List<ButoNotification> notificationList1 = new ArrayList<ButoNotification>();
		notificationData.add(notificationList1);
		
		notificationList1.add(new ButoNotification("Type 1", "Object 1", 0, 1000, "Hello World"));
		notificationList1.add(new ButoNotification("Type 1", "Object 1", 1, 2000, "Hello World"));
		notificationList1.add(new ButoNotification("Type 1", "Object 1", 2, 3000, "Hello World"));
		notificationList1.add(new ButoNotification("Type 1", "Object 1", 3, 4000, "Hello World"));
		notificationList1.add(new ButoNotification("Type 1", "Object 1", 0, 5000, "Hello World")); // Should throw exception
		
		notificationList1.add(new ButoNotification("Type 2", "Object 1", 0, 500, "Hello World"));
		notificationList1.add(new ButoNotification("Type 2", "Object 1", 1, 600, "Hello World"));
		notificationList1.add(new ButoNotification("Type 2", "Object 1", 2, 700, "Hello World"));
		notificationList1.add(new ButoNotification("Type 2", "Object 1", 3, 800, "Hello World"));
		notificationList1.add(new ButoNotification("Type 2", "Object 1", 4, 900, "Hello World"));

		new NotificationClassifier(notificationData);
	}
	
	@Test(expected = NotificationClassifierCreationException.class) 
	public void testCreationException2() throws NotificationClassifierCreationException{
		
		List<List<ButoNotification>> notificationData = new ArrayList<List<ButoNotification>>();
		
		// Data 1
		List<ButoNotification> notificationList1 = new ArrayList<ButoNotification>();
		notificationData.add(notificationList1);
		
		notificationList1.add(new ButoNotification("Type 1", "Object 1", 0, 1000, "Hello World"));
		notificationList1.add(new ButoNotification("Type 1", "Object 1", 1, 2000, "Hello World"));
		notificationList1.add(new ButoNotification("Type 1", "Object 1", 2, 3000, "Hello World"));
		notificationList1.add(new ButoNotification("Type 1", "Object 1", 3, 4000, "Hello World"));
		notificationList1.add(new ButoNotification("Type 1", "Object 1", 4, 5000, "Hello World"));
		
		notificationList1.add(new ButoNotification("Type 2", "Object 1", 0, 500, "Hello World"));
		notificationList1.add(new ButoNotification("Type 2", "Object 1", 1, 600, "Hello World"));
		notificationList1.add(new ButoNotification("Type 2", "Object 1", 2, 700, "Hello World"));
		notificationList1.add(new ButoNotification("Type 2", "Object 1", 3, 800, "Hello World"));
		notificationList1.add(new ButoNotification("Type 2", "Object 1", 4, 900, "Hello World"));
		
		notificationList1.add(new ButoNotification("Type 3", "Object 2", 0, 100, "Hello World"));
		notificationList1.add(new ButoNotification("Type 3", "Object 2", 1, 1800, "Hello World"));
		notificationList1.add(new ButoNotification("Type 3", "Object 2", 2, 3600, "Hello World"));
		
		notificationList1.add(new ButoNotification("Type 3", "Object 3", 0, 100, "Hello World"));
		notificationList1.add(new ButoNotification("Type 3", "Object 3", 1, 1800, "Hello World"));
		notificationList1.add(new ButoNotification("Type 3", "Object 3", 2, 3600, "Hello World"));
		
		// Data 2
		List<ButoNotification> notificationList2 = new ArrayList<ButoNotification>();
		notificationData.add(notificationList2);
		
		notificationList2.add(new ButoNotification("Type 1", "Object 1", 0, 1000, "Hello World"));
		notificationList2.add(new ButoNotification("Type 1", "Object 1", 1, 2000, "Hello World"));
		notificationList2.add(new ButoNotification("Type 1", "Object 1", 2, 3000, "Hello World"));
		notificationList2.add(new ButoNotification("Type 1", "Object 1", 3, 4000, "Hello World"));
		notificationList2.add(new ButoNotification("Type 1", "Object 1", 4, 5000, "Hello World"));
		
		notificationList2.add(new ButoNotification("Type 2", "Object 1", 0, 500, "Hello World"));
		notificationList2.add(new ButoNotification("Type 2", "Object 1", 1, 600, "Hello World"));
		notificationList2.add(new ButoNotification("Type 2", "Object 1", 2, 700, "Hello World"));
		notificationList2.add(new ButoNotification("Type 2", "Object 1", 3, 800, "Hello World"));
		notificationList2.add(new ButoNotification("Type 2", "Object 1", 4, 900, "Hello World"));
		
		notificationList2.add(new ButoNotification("Type 3", "Object 2", 0, 100, "Hello World"));
		notificationList2.add(new ButoNotification("Type 3", "Object 2", 1, 1800, "Hello World"));
		notificationList2.add(new ButoNotification("Type 3", "Object 2", 2, 3600, "Hello World"));
		
		notificationList2.add(new ButoNotification("Type 3", "Object 3", 0, 100, "Hello World"));
		notificationList2.add(new ButoNotification("Type 3", "Object 3", 1, 1800, "Hello World"));
		notificationList2.add(new ButoNotification("Type 4", "Object 3", 2, 3600, "Hello World")); // Should throw exception

		new NotificationClassifier(notificationData);
	}
	
}

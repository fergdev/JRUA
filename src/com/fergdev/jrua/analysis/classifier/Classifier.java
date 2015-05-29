package nz.ac.massey.buto.analysis.classifier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nz.ac.massey.buto.analysis.algorithms.ClassificationAlgorithm;
import nz.ac.massey.buto.analysis.algorithms.models.ClassificationModel;
import nz.ac.massey.buto.analysis.algorithms.models.ClassificationModelCreationException;
import nz.ac.massey.buto.analysis.algorithms.models.ClassificationModelResult;
import nz.ac.massey.buto.analysis.algorithms.models.ModelType;
import nz.ac.massey.buto.analysis.bounds.Bounds;
import nz.ac.massey.buto.analysis.metadata.MetaDataClassifier;
import nz.ac.massey.buto.analysis.metadata.MetaDataClassifierCreationException;
import nz.ac.massey.buto.analysis.metadata.MetaDataDifference;
import nz.ac.massey.buto.analysis.notifications.NotificationClassifier;
import nz.ac.massey.buto.analysis.notifications.NotificationClassifierCreationException;
import nz.ac.massey.buto.analysis.notifications.NotificationDifference;
import nz.ac.massey.buto.analysis.smoothing.DataSmoother;
import nz.ac.massey.buto.analysis.smoothing.DataSmootherManager;
import nz.ac.massey.buto.analysis.timepoint.TimePoint;
import nz.ac.massey.buto.analysis.timepoint.TimePointGenerator;

import nz.ac.massey.buto.domain.ButoProperty;
import nz.ac.massey.buto.domain.ButoNotification;
import nz.ac.massey.buto.domain.TrainingRun;

/**
 * Class to perform total analysis on a training run.
 * 
 * @author Fergus Hewson
 *
 */
public class Classifier implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5885349289654462598L;

	/**
	 * Map of data type objects to classification models.
	 */
	private Map<ButoProperty, ClassificationModel> classificationModelMap;

	/**
	 * Meta data classification classifier.
	 */
	private MetaDataClassifier metaDataClassifier;
	
	/**
	 * Notification classifier.
	 */
	private NotificationClassifier notificationClassifier;
	
	/**
	 * The data smoother to preprocess data with.
	 */
	private String dataSmootherName;
	
	/**
	 * The smoothing size to use with the data smoother.
	 */
	private int smoothingSize;

	/**
	 * 
	 * @param trainingData
	 * @param butoPropertyList
	 * @param timePointGenerator
	 * @param ca
	 * @param caParams
	 * @param smoother
	 * @param smoothingSize
	 * @throws ClassificationModelCreationException 
	 * @throws MetaDataClassifierCreationException 
	 * @throws NotificationClassifierCreationException 
	 */
	public Classifier(
			List<TrainingRun> trainingRuns, 
			List<ButoProperty> butoPropertyList, 
			TimePointGenerator timePointGenerator,
			ClassificationAlgorithm ca, 
			Map<String,String> caParams,
			ModelType modelType,
			DataSmoother smoother,
			int smoothingSize,
			int jmxCollectionInterval,
			List<String> metaDataFilterList) throws ClassificationModelCreationException, MetaDataClassifierCreationException, NotificationClassifierCreationException{
		
		//**************************************************************
		// Classification Models
		
		this.classificationModelMap = new HashMap<ButoProperty, ClassificationModel>();
		this.dataSmootherName = smoother.getClass().getName();
		this.smoothingSize = smoothingSize;
		
		// Iterate over data types and build models
		for(ButoProperty butoProperty : butoPropertyList){
			
			// Get data from training runs
			List<Map<Long,Number>> dataList = new ArrayList<Map<Long,Number>>();
			List<Map<Long,Number>> smoothedDataList = new ArrayList<Map<Long,Number>>();

			// iterate over training runs
			for(TrainingRun trainingRun : trainingRuns){
				
				// get data and smooth it
				Map<Long,Number> data = trainingRun.getData(butoProperty.getKey());
				Map<Long,Number> smoothedData = smoother.smooth(data, smoothingSize, butoProperty);
				
				// add to list
				dataList.add(data);
				smoothedDataList.add(smoothedData);
			}

			// Create time-point list
			List<TimePoint> timePointList = timePointGenerator.generateTimePoints(smoothedDataList, jmxCollectionInterval);

			
			// Get bounds from classification algorithm
			List<Bounds> boundsList = ca.generateBounds(timePointList, butoProperty, caParams);
			
			// create model
			ClassificationModel model = new ClassificationModel(boundsList, butoProperty, modelType);
			
			// Add to map
			classificationModelMap.put(butoProperty, model);
		}
		
		// Meta-data and notification lists
		List<Map<String,Object>> metaDataList = new ArrayList<Map<String,Object>>();
		List<List<ButoNotification>> notificationsList = new ArrayList<List<ButoNotification>>();
		
		//Build data Lists
		for(TrainingRun trainingRun : trainingRuns){
			metaDataList.add(trainingRun.getMetaData());
			notificationsList.add(trainingRun.getNotifications());
		}
		
		//**************************************************************
		// MetaData Classifier
		metaDataClassifier = new MetaDataClassifier(metaDataList, metaDataFilterList);
		
		//**************************************************************
		// Notification Classifier
		notificationClassifier = new NotificationClassifier(notificationsList);
	}


	/**
	 * Classifies a testRun against this classifier.
	 * 
	 * @param testRun The trainingRun to classify.
	 * @return An object describing the results of the classification.
	 */
	public ClassificationResult classify(TrainingRun testRun){
		
		// Get data smoother
		DataSmoother dataSmoother = DataSmootherManager.getDataSmoother(dataSmootherName);
		
		//**************************************************************
		// Classification Models
		
		Map<ButoProperty, ClassificationModelResult> classificationResultMap = new HashMap<ButoProperty, ClassificationModelResult>();
		//Map<ButoProperty, Map<Long, Number>> smoothedDataMap = new HashMap<ButoProperty, Map<Long,Number>>();
		
		for(ButoProperty property : classificationModelMap.keySet()){
			
			// Get data
			Map<Long, Number> data = testRun.getData(property.getKey());
			
			// Smooth the data
			Map<Long, Number> smoothedData = dataSmoother.smooth(data, smoothingSize, property);
			
			//smoothedDataMap.put(property, smoothedData);
			
			// Get model
			ClassificationModel model = classificationModelMap.get(property);
			
			// Classify
			classificationResultMap.put(property, model.classify(smoothedData));
		}
		
		//**************************************************************
		// MetaData Classifier
		
		List<MetaDataDifference> metaDataResults = metaDataClassifier.classify(testRun.getMetaData());
		
		//**************************************************************
		// Notification Classifier
		
		List<NotificationDifference> notificationDifferences = notificationClassifier.classify(testRun.getNotifications());
		
		// Create new result
		return new ClassificationResult(classificationResultMap, metaDataResults, notificationDifferences);
	}
	
	/**
	 * Gets the map of models contained by this classifier.
	 * 
	 * @return
	 */
	public Map<ButoProperty, ClassificationModel> getClassificationModelMap() {
		return classificationModelMap;
	}
}

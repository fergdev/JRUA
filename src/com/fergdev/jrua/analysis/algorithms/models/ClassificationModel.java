package nz.ac.massey.buto.analysis.algorithms.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nz.ac.massey.buto.analysis.bounds.Bounds;
import nz.ac.massey.buto.analysis.bounds.BoundsCreationException;
import nz.ac.massey.buto.domain.ButoProperty;
import nz.ac.massey.buto.utils.Maths;

/**
 * Model generated by a classification algorithm, used to classify tests runs.
 * 
 * @author Fergus Hewson
 *
 */
public class ClassificationModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4634826410357242015L;

	private List<Bounds> boundList;
	
	private ButoProperty butoProperty;
	
	private ModelType modelType;
	
	public ClassificationModel(List<Bounds> boundList, ButoProperty butoProperty, ModelType modelType){
		this.boundList = boundList;
		this.butoProperty = butoProperty;
		this.modelType = modelType;
	}
	
	/**
	 * Tests the given data against the model and returns a classification result for the tes.
	 * 
	 * @param data The data to classify against the model.
	 * @return List of error timestamps.
	 */
	public ClassificationModelResult classify(Map<Long, Number> data){
		
		// List of time stamps that fall outside the permissible range
		List<Long> errorList = new ArrayList<Long>();
		List<Long> warningList = new ArrayList<Long>();
		
		
		// List of unprocessed bounds, to catch test runs shorter than model
		List<Bounds> unprocessedBounds = new ArrayList<Bounds>(boundList);
		
		// Iterate over time stamps
		for(Long timeStamp : data.keySet()){
			
			// Get the value
			Number value = data.get(timeStamp);
			
			// Get the bound
			Bounds bound;
			if(modelType == ModelType.STANDARD){
				bound = getBounds(timeStamp);
			}else{
				bound = getInterpolationBounds(timeStamp);
			}
			
			// if no bounds were found then test run is longer than the training data
			if(bound == null){
				warningList.add(timeStamp);
				continue;				
			}		
			
			// Test if value falls outside the bounds
			if(!bound.fallsWithin(value)){
				
				// add new error
				errorList.add(timeStamp);
			}
			
			// Remove processed bound
			unprocessedBounds.remove(bound);
		}

		// Add unprocessed bounds errors
		for(Bounds bound : unprocessedBounds){
			// Add new error
			warningList.add(bound.timeStamp);
		}

		// Return errors
		return new ClassificationModelResult(this, data, errorList, warningList);
	}
	
	/**
	 * Get the upper bound for a timepoint. 
	 * Note that the arg is a long - this means that the classification model
	 * is responsible for interpolation (polynomial, linear, .. ). 
	 * This is important if data points in the test series are a bit off data points
	 * used in training. (-Jens)
	 * @param timePoint a value
	 * @return
	 */
	public Number getUpperBound (long timePoint){
		return null;
	}
	
	/**
	 * Gets the lower bound for a timepoint.
	 * 
	 * @param timePoint A time point
	 * @return
	 */
	public Number getLowerBound (long timePoint){
		return null;
	}
	
	public List<Bounds> getBounds(){
		return boundList;
	}
	
	public ButoProperty getButoProperty(){
		return butoProperty;
	}
	
	private Bounds getBounds(long timeStamp){
		
		// Check for null list
		if(boundList == null){
			System.err.println("NULL bounds");
			return null;
		}
		
		// Check for empty list
		if(boundList.size() == 0){
			System.err.println("No bounds");
			return null;
		}
		
		Bounds ouputBound = null;
		long timeStampDifference = Math.abs(boundList.get(0).timeStamp - boundList.get(1).timeStamp); // need to pick a good value
		
		for(Bounds bound : boundList){
			
			if(bound == null){
				System.err.println("NULL BOUND");
				continue;
			}
			long diff = Math.abs(bound.timeStamp - timeStamp);

			if(diff < timeStampDifference){
				ouputBound = bound;
				timeStampDifference = diff;
			}
		}

		return ouputBound;
	}
	
	
	private Bounds getInterpolationBounds(long timeStamp){
		
		// test for negative timestamp
		if(timeStamp < 0){
			System.err.println("ERROR negative timestmamp used " + timeStamp);
			return null;
		}
		
		if(boundList == null){
			System.err.println("Interpolation Model NULL bounds list......");
			return null;
		}
		
		if(boundList.size() == 0){
			
			System.err.println("Empty bounds list......");
			return null;
		}
		
		// Test if less than first bound
		Bounds firstBounds = boundList.get(0);
		
		if(timeStamp < firstBounds.timeStamp){
			return firstBounds;
		}
		
		Bounds prevBounds = firstBounds;
		Bounds currentBounds = null;
		
		// Iterate over rest of bounds
		for(int index = 1; index < boundList.size(); index++){
			
			// Get the next bounds
			currentBounds = boundList.get(index);
			
			if(currentBounds == null){
				System.err.println("Interpolation NULL BOUND" );
				continue;
			}
			
			// If the time-stamp is less than the current bounds time-stamp
			// we have found the higher bound
			if(timeStamp <= currentBounds.timeStamp){
				
				// Lower bound
				Number lowerBound = Maths.linearInterpolate(prevBounds.timeStamp, prevBounds.lowerBound, currentBounds.timeStamp, currentBounds.lowerBound, timeStamp);
				
				// Upper bound
				Number upperBound = Maths.linearInterpolate(prevBounds.timeStamp, prevBounds.upperBound, currentBounds.timeStamp, currentBounds.upperBound, timeStamp);
				
				try {
					return new Bounds(timeStamp, lowerBound, upperBound, butoProperty);
				} catch (BoundsCreationException e) {
					System.err.println("Bounds creation error. " + e);
					return null;
				}
			}
			
			// Update previous bounds
			prevBounds = currentBounds;
		}
		
		//Could not find interpolation
		// Test if within range of last bound
		Bounds lastBounds = boundList.get(boundList.size() - 1);
		if(timeStamp - lastBounds.timeStamp < firstBounds.timeStamp){
			return lastBounds;
		}
		
		return null;
	}


	public String toString(){
		StringBuilder outputBuilder = new StringBuilder(String.format("%-20s%-20s%-20s\n", "Timestamp", "Lower Bound", "Upper Bound"));
		
		for(Bounds bound : boundList){
			outputBuilder.append(bound.toOutputString() + "\n");
		}
		
		return outputBuilder.toString();
	}
}
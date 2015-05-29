package nz.ac.massey.buto.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a set of training runs.
 * 
 * @author Fergus Hewson
 *
 */
public class Oracle {
	
	/**
	 * The name of the oracle.
	 */
	private String name;
	
	/**
	 * List of training runs.
	 */
	private List<TrainingRun> trainingRunList;
	
	/**
	 * Creates a new oracle.
	 * @param name Name of the oracle.
	 * @param trainingRunList List of training runs.
	 */
	public Oracle(String name, List<TrainingRun> trainingRunList){
		this.name = name;
		this.trainingRunList = trainingRunList;
	}
	
	/**
	 * Gets the oracle's training runs.
	 * @return List of training runs.
	 */
	public List<TrainingRun> getTrainingRunList() {
		return trainingRunList;
	}

	/**
	 * Gets the name of the oracle.
	 * @return Oracle name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets a list of the data from the training runs contained by this oracle
	 * for the provided key.
	 * 
	 * @param key The key of the data type to get the data list for.
	 * @return List of data.
	 */
	public List<Map<Long,Number>> getData(String key){
		
		// The list to return
		List<Map<Long,Number>> dataList = new ArrayList<Map<Long,Number>>();
		
		// Iterate over training runs building list of data
		for(TrainingRun trainingRun : trainingRunList){
			
			// get data and add to list
			dataList.add(trainingRun.getData(key));
		}
		
		// return data list
		return dataList;
	}
}

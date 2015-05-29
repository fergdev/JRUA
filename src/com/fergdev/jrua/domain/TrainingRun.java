package nz.ac.massey.buto.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Represents a training run.
 * @author Fergus Hewson
 *
 */
public class TrainingRun {
	
	/**
	 * The date the training run was executed.
	 */
	private Date date;
	
	/**
	 * The exit value of the process.
	 */
	private int exitVal;
	
	/**
	 * Map of meta data for the training run.
	 */
	private Map<String, Object> metaData;
	
	/**
	 * List of snapshots collected during the training run.
	 */
	private List<Snapshot> snapshotList;
	
	/**
	 * List of notifications collected during the training run.
	 */
	private List<ButoNotification> notifications;

	/**
	 * Creates a new empty training run with the date set to the current date.
	 */
	public TrainingRun(){
		date = new Date();
		
		// init data structures
		this.metaData = new HashMap<String, Object>();
		this.snapshotList = new ArrayList<Snapshot>();
		this.notifications = new ArrayList<ButoNotification>();
	}
	
	/**
	 * 
	 * @param date
	 * @param exitVal
	 * @param metaData
	 * @param snapshotList
	 * @param notifications
	 */
	public TrainingRun(Date date, int exitVal, Map<String, Object> metaData, List<Snapshot> snapshotList, List<ButoNotification> notifications){
		this.date = date;
		this.exitVal = exitVal;
		
		this.metaData = metaData;
		this.snapshotList = snapshotList;
		this.notifications = notifications;
	}
	
	/**
	 * 
	 * @param date
	 * @param exitVal
	 */
	public TrainingRun(Date date, int exitVal){
		this.date = date;
		this.exitVal = exitVal;
		
		// init data structures
		this.metaData = new HashMap<String, Object>();
		this.snapshotList = new ArrayList<Snapshot>();
		this.notifications = new ArrayList<ButoNotification>();
	}
	
	/**
	 * Gets the date the training run was executed.
	 * 
	 * @return The date.
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Adds a new snapshot to the training run.
	 * 
	 * @param snapshot The new snapshot to add.
	 */
	public void addSnapshot(Snapshot snapshot){
		snapshotList.add(snapshot);
	}
	
	/**
	 * Gets the list of snapshots collected during this training run.
	 * 
	 * @return List of snapshots.
	 */
	public List<Snapshot> getSnapshotList(){
		return snapshotList;
	}

	/**
	 * Gets the meta data collected during this training run.
	 * 
	 * @return Map of collected meta data.
	 */
	public Map<String, Object> getMetaData(){
		return metaData;
	}
	
	/**
	 * Gets the exit value of the process.
	 * @return Exit value.
	 */
	public int getExitVal() {
		return exitVal;
	}

	//public void setExitVal(int exitVal) {
	//	this.exitVal = exitVal;
	//}
	
	/**
	 * Gets the list of notifications collected during this training run.
	 * 
	 * @return List of notifications.
	 */
	public List<ButoNotification> getNotifications(){
		return notifications;
	}
	
	/**
	 * Returns a Map of timestamps to Number data objects only containing the data from the provided key.
	 * 
	 * @param key The key of the data to get.
	 * @return A Map of timestamps to Number data objects.
	 */
	public Map<Long, Number> getData(String key){
		
		// output list
		Map<Long, Number> data = new TreeMap<Long, Number>();
		
		// iterate over snapshots retrieving data
		for(Snapshot snapshot : getSnapshotList()){
			data.put(snapshot.getUptime(), (Number)snapshot.getObject(key));
		}
		
		// return data
		return data;
	}
	
	/**
	 * Generates a string representation of the training run.
	 */
	public String toString(){
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("Date : " + date);
		
		return sb.toString();
	}


	
}

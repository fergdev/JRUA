package nz.ac.massey.buto.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A set of data collected from a test at a particular time point.
 * 
 * @author Fergus Hewson
 *
 */
public class Snapshot {
	
	/**
	 * The timestamp the snapshot was taken.
	 */
	private long uptime;
	
	/**
	 * A map of the data collected in the snapshot.
	 */
	private Map<String, Object> data;
	
	/**
	 * Creates a snapshot with a given set of data.
	 * @param uptime The timestamp of the snapshot.
	 * @param data The data of the snapshot.
	 */
	public Snapshot(long uptime, Map<String, Object> data){
		this.uptime = uptime;
		this.data = data;
	}
	
	/**
	 * Creates a snapshot with no data.
	 * @param uptime The timestamp of the snapshot.
	 */
	public Snapshot(long uptime){
		
		this.uptime= uptime;
		// init data
		data = new HashMap<String, Object>();
	}

	/**
	 * Gets the timestamp of the snapshot.
	 * 
	 * @return The timestamp of the snapshot.
	 */
	public long getUptime() {
		return uptime;
	}
	
	/**
	 * Adds a string data object to the data set.
	 * 
	 * @param key The key of the data item.
	 * @param obj The String object to add.
	 */
	public void addData(String key, String obj){
		if(obj != null)
			data.put(key, obj);
	}
	
	/**
	 * Adds a long object to the data set.
	 * 
	 * @param key The key of the data item.
	 * @param l The long object to add.
	 */
	public void addData(String key, long l){
		data.put(key, new Long(l));
	}
	
	/**
	 * Adds an integer object to the data set.
	 * @param key The key of the data item.
	 * @param i The integer object to add.
	 */
	public void addData(String key, int i){
		data.put(key, new Integer(i));
	}
	
	/**
	 * Adds a double object to the data set.
	 * @param key The key of the data item.
	 * @param d The double object to add.
	 */
	public void addData(String key, double d) {
		data.put(key, new Double(d));
	}
	
	/**
	 * Adds a boolean object to the data set.
	 * 
	 * @param key The key of the data item.
	 * @param b The boolean object of the data item.
	 */
	public void addData(String key, boolean b){
		data.put(key, new Boolean(b));
	}
	
	/**
	 * Adds a generic object to the data set.
	 * @param key The key of the data item.
	 * @param value The object to add to the data set.
	 */
	public void addData(String key, Object value){
		data.put(key, value);
	}
	
	/**
	 * Gets the set of keys contained in the data set.
	 * @return Set of keys.
	 */
	public Set<String> keys(){
		return data.keySet();
	}
	
	/**
	 * Looks up an object in the data set using the provided key.
	 * 
	 * @param key The key of the object to retrieve.
	 * @return The object related to the provided key.
	 */
	public Object getObject(String key){
		return data.get(key);
	}


	/**
	 * Generates display string for snapshot.
	 */
	public String toString(){
		
		StringBuilder sb = new StringBuilder();
		
		Set<String> keys = data.keySet();
		
		for(String key : keys){
			sb.append(data.get(key).toString() + ", ");
		}
		int length = sb.length();
		
		sb.delete(sb.length() - 2, length);
		
		return sb.toString();
	}
}

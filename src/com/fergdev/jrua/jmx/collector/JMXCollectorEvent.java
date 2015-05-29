package nz.ac.massey.buto.jmx.collector;

import nz.ac.massey.buto.domain.Snapshot;

/**
 * Event object generated by the JMXCollecter.
 * 
 * @author Fergus Hewson
 *
 */
public class JMXCollectorEvent {

	/**
	 * Set of strings for the different Event types.
	 */
	public final static String METADATA_COLLECTED = 	"METADATA_COLLECTED";
	public final static String SNAPSHOT_TAKEN = 		"SNAPSHOT_TAKEN";
	public final static String COLLECTION_COMPLETE = 	"COLLECTION_COMPLETE";
	public final static String COLLECTION_STARTED = 	"COLLECTION_STARTED";
	
	/**
	 * String for the type of the event.
	 */
	private String eventType;
	
	/**
	 * Snapshot object for the SNAPSHOT_TAKEN event.
	 */
	private Snapshot snapshot;
	
	/**
	 * Creates a new event with the given event type.
	 * 
	 * @param eventType The event type of the event.
	 */
	public JMXCollectorEvent(String eventType){
		this.eventType = eventType;
	}
	
	/**
	 * Creates a new SNAPSHOT_TAKEN event with a given snapshot.
	 * 
	 * @param snapshot The snapshot that has been taken.
	 */
	public JMXCollectorEvent(Snapshot snapshot){
		this.eventType = SNAPSHOT_TAKEN;
		this.snapshot = snapshot;
	}

	/**
	 * Gets the snapshot for this event. Will only be set if this event is of type SNAPSHOT_TAKEN.
	 * 
	 * @return A snapshot object generated by the JMXCollector, null if this event is not of type SNAPSHOT_TAKEN.
	 */
	public Snapshot getSnapshot() {
		return snapshot;
	}
	
	/**
	 * Gets the event type of this Event object.
	 * @return The type of this event.
	 */
	public String getEventType() {
		return eventType;
	}
	
}
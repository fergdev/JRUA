package nz.ac.massey.buto.domain;

/**
 * Notification that has been collected.
 * TODO rename?
 * @author Fergus Hewson
 *
 */
public class ButoNotification {

	/**
	 * The type of the notification.
	 */
	private String type;
	
	/**
	 * Name of the source object for the notification.
	 */
	private String sourceObjectName;
	
	/**
	 * The sequence number of the notification.
	 */
	private long sequenceNumber;
	
	/**
	 * The timestamp for when the notification was emitted.
	 */
	private long timeStamp;
	
	/**
	 * The message of the notification.
	 */
	private String message;
	
	/**
	 * 
	 * 
	 * @param sequenceNumber Notification sequence number
	 * @param text The text of the notification.
	 */
	
	/**
	 * Creates a new processed notification.
	 * 
	 * @param type The type of the notification.
	 * @param sourceObjectName Name of the source object for the notification.
	 * @param sequenceNumber The sequence number of the notification.
	 * @param timeStamp The timestamp of the notification.
	 * @param message The message of the notification.
	 */
	public ButoNotification(
			String type, 
			String sourceObjectName, 
			long sequenceNumber, 
			long timeStamp, 
			String message){
		this.type = type;
		this.sourceObjectName = sourceObjectName;
		this.sequenceNumber = sequenceNumber;
		this.timeStamp = timeStamp;
		this.message = message;
	}

	/**
	 * Gets the type of this notification.
	 * 
	 * @return the notificationType
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Gets the name of the source object for this notification.
	 * 
	 * @return the sourceObjectName
	 */
	public String getSourceObjectName() {
		return sourceObjectName;
	}
	
	/**
	 * Gets the sequence number of the notification.
	 * 
	 * @return The sequence number.
	 */
	public long getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * Gets the timestamp for this notification.
	 * 
	 * @return the timeStamp
	 */
	public long getTimeStamp() {
		return timeStamp;
	}

	/**
	 * Gets the message for this notification.
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Generates a string that represents this notifications.
	 */
	public String toString(){
		return type + " " + sourceObjectName + " " + sequenceNumber + " " + timeStamp + " " + message;
	}
	
	
}

package nz.ac.massey.buto.analysis.notifications;


/**
 * Exception thrown when a malformed training set is provided to the notification classifier.
 * 
 * @author Fergus Hewson
 *
 */
@SuppressWarnings("serial")
public class NotificationClassifierCreationException extends Exception{

	/**
	 * Creates new NotificationClassifierCreationException.
	 * 
	 * @param message The message to pass to callers.
	 */
	public NotificationClassifierCreationException(String message){
		super(message);
	}
}

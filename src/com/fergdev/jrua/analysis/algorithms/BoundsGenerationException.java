package nz.ac.massey.buto.analysis.algorithms;

/**
 * Except thrown by a ClassificationAlgorithm when generating bounds.
 * 
 * @author Fergus Hewson
 *
 */
@SuppressWarnings("serial")
public class BoundsGenerationException extends Exception{

	/**
	 * Creates a new bounds creation exception with a message.
	 * 
	 * @param message The message to pass to callers.
	 */
	public BoundsGenerationException(String message){
		super(message);
	}
	
}

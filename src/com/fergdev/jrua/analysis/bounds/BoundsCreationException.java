package nz.ac.massey.buto.analysis.bounds;

/**
 * Exception thrown when there is an error creating a bounds object, usually when
 * the lower bound exceeds the upp bound.
 * @author Fergus Hewson
 *
 */
@SuppressWarnings("serial")
public class BoundsCreationException extends Exception{
	
	public BoundsCreationException(String message){
		super(message);
	}
}

package nz.ac.massey.buto.analysis.algorithms.models;

/**
 * This exception is thrown when there is an error when generating a ClassificationModel
 * 
 * @author Fergus Hewson
 *
 */
@SuppressWarnings("serial")
public class ClassificationModelCreationException extends Exception{

	public ClassificationModelCreationException(String message){
		super(message);
	}
}

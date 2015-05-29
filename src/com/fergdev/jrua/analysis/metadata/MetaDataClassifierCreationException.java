package nz.ac.massey.buto.analysis.metadata;

/**
 * Exception thrown when there is an error creating the MetaData classifier.
 * 
 * @author Fergus Hewson
 *
 */
@SuppressWarnings("serial")
public class MetaDataClassifierCreationException extends Exception{
	
	/**
	 * Creates a new MetaDataClassificationException.
	 * 
	 * @param message The reason for the exception.
	 */
	public MetaDataClassifierCreationException(String message){
		super(message);
	}
}

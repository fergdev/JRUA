package nz.ac.massey.buto.utils;

/**
 * String Builder that appends a new line character after each call to append.
 * 
 * @author Fergus Hewson
 *
 */
public class NewlineStringBuilder {
	
	/**
	 * Private string builder.
	 */
	private StringBuilder delegate;

	/**
	 * Constructor.
	 */
	public NewlineStringBuilder() {
		delegate = new StringBuilder();
	}
	
	/**
	 * Appends s + '\n' to string builder.
	 * @param s String to append.
	 */
	public void append(String s){
		delegate.append(s + "\n");
	}
	
	/**
	 * Gets the string.
	 */
	public String toString(){
		return delegate.toString();
	}
}
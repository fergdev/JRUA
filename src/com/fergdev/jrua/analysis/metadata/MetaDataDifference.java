package nz.ac.massey.buto.analysis.metadata;

/**
 * Represents a changed in a meta data value between tests.
 * 
 * @author Fergus Hewson
 *
 */
public class MetaDataDifference{
	
	/**
	 * The key for the meta data value.
	 */
	private String key;
	
	/**
	 * The original value for the meta data value.
	 */
	private String valueA;
	
	/**
	 * The different value for the meta data value.
	 */
	private String valueB;
	
	public MetaDataDifference(String key, String valueA, String valueB){
		this.key = key;
		this.valueA = valueA;
		this.valueB = valueB;
	}
	
	public String toString(){
		
		return "key '" + key + "' valueA '" + valueA + "' valueB '" + valueB + "'";
		
	}
	
}
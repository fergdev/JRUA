package nz.ac.massey.buto.domain;

import java.io.Serializable;

/**
 * Represents property that is monitored by Buto during tests.
 * 
 * @author Fergus Hewson
 *
 */
public class ButoProperty implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 95859178684768123L;

	/**
	 * Name to use to lookup this property.
	 */
	private String key;
	
	/**
	 *  Display name for this property.
	 */
	private String name;
	
	/**
	 * Object type for this property.
	 */
	private String type;
	
	/**
	 * The units of this property.
	 */
	private String units;
	
	/**
	 * Lowest permissible value for this property.
	 */
	private Number lowerBound;
	
	/**
	 * Highest permissible value for this property.
	 */
	private Number upperBound;
	
	/**
	 * Property constructor.
	 * 
	 * @param key The key to lookup up this property.
	 * @param name The display name of this property.
	 * @param units The units of this property.
	 * @param type The object type of this property.
	 * @param lowerBound The lower bound for values.
	 * @param upperBound The upper bound for values.
	 */
	public ButoProperty(String key, String name, String units, String type, Number lowerBound, Number upperBound){
		this.key = key;
		this.name = name;
		this.type = type;
		this.units = units;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}
	
	/**
	 * Gets the lookup key for this property.
	 * @return The lookup key.
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * Gets the display name of this property.
	 * @return The display name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the type of data contained by this property. e.g java.lang.Integer, java.lang.Double, java.lang.String
	 * @return The type of data.
	 */
	public String getType(){
		return type;
	}
	
	/**
	 * Gets the units of this property. eg mb, gb, threads.
	 * 
	 * @return The units.
	 */
	public String getUnits() {
		return units;
	}
	
	/**
	 * Gets the lowest permissible value of this property.
	 * @return The lower bound.
	 */
	public Number getLowerBound() {
		return lowerBound;
	}

	/**
	 * Gets the highest permissible value for this property.
	 * @return The higher bound.
	 */
	public Number getUpperBound() {
		return upperBound;
	}

	@Override
	public String toString(){
		return key + " " + name + " " + type + " " + units + " " + lowerBound + " " + upperBound;
	}
}

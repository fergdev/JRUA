package nz.ac.massey.buto.analysis.bounds;

import java.io.Serializable;

import nz.ac.massey.buto.domain.ButoProperty;
import nz.ac.massey.buto.utils.Maths;

/**
 * Represents a bounds for a timestamp.
 * 
 * @author Fergus Hewson
 *
 */
public class Bounds implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7541226804730565694L;

	/**
	 * The timestamp this bounds object relates to.
	 */
	public long timeStamp;
	
	/**
	 * The lowest permissible value for this bounds.
	 */
	public Number lowerBound;
	
	/**
	 * The highest permissible value for this bounds.
	 */
	public Number upperBound;
		
	/**
	 * Creates new bounds object.
	 * 
	 * @param timeStamp The timestamp of the bounds.
	 * @param newLowerBound The lower bounds of the bounds.
	 * @param newUpperBound The upper bound of the bounds.
	 */
	public Bounds(long timeStamp, Number newLowerBound, Number newUpperBound, ButoProperty dataType) throws BoundsCreationException{
		
		//****************************************************************
		// Assert lower bound is less than the upper bound.
		if(Double.compare(newLowerBound.doubleValue(), newUpperBound.doubleValue()) > 0){
			
			String message = "Lower bound exceeds upper bound \'" + newLowerBound.doubleValue() + "\' \'" + newUpperBound.doubleValue() + "\'";
			
			//System.err.println(message);
			
			throw new BoundsCreationException(message);
		}
		
		// Set the values
		this.timeStamp = timeStamp;
		this.lowerBound = newLowerBound;
		this.upperBound = newUpperBound;
		
		//****************************************************************
		// Round values
		
		switch(dataType.getType()){
		case("java.lang.Integer"):
			
			upperBound = (int) Math.ceil(newUpperBound.doubleValue());
			lowerBound = (int) Math.floor(newLowerBound.doubleValue());
			
			break;
		case("java.lang.Long"):
			upperBound = (long) Math.ceil(newUpperBound.doubleValue());
			lowerBound = (long) Math.floor(newLowerBound.doubleValue());				
			break;
		case("java.lang.Double"):
			// No rounding
			break;
		default:
			System.err.println("Invalid data type " + dataType.getType());
		}
		
		//****************************************************************
		// Impose DataType bounds
		
		// Make sure lower bound is larger than the lowest permissible value
		if(Maths.compare(lowerBound.doubleValue(), dataType.getLowerBound().doubleValue()) < 0){
			lowerBound = dataType.getLowerBound();
		}
		
		// Make sure upper bound is is larger than the lowest permissible value
		if(Maths.compare(upperBound.doubleValue(), dataType.getLowerBound().doubleValue()) < 0){
			upperBound = dataType.getLowerBound();
		}
		
		// make sure upper bound is larger than the permissible highest value
		if(Maths.compare(upperBound.doubleValue(), dataType.getUpperBound().doubleValue()) > 0){
			upperBound = dataType.getUpperBound();
		}
		
		// Make sure lower bound is larger than the permissible highest value
		if(Maths.compare(lowerBound.doubleValue(), dataType.getUpperBound().doubleValue()) > 0){
			lowerBound = dataType.getUpperBound();
		}
		
	}
	
	/**
	 * Tests if a given value falls with the bounds of this object.
	 * 
	 * @param value The value to test.
	 * @return True if the value falls within the bounds of this object, fase otherwise.
	 */
	public boolean fallsWithin(Number value){
		
		// test if value is higher than the lower bound
		if(value.doubleValue() < lowerBound.doubleValue()){
			return false;
		}
		
		// test the value is lower than the higher bound
		if(value.doubleValue() > upperBound.doubleValue()){
			return false;			
		}
		
		// Within bounds
		return true;
	}

	/**
	 * Info string for this object.
	 */
	public String toString(){
		return timeStamp + " " + lowerBound + " " + upperBound;
	}
	
	/**
	 * Creates a formatted output string for this object.
	 * 
	 * @return Formatted string.
	 */
	public String toOutputString(){
		
		String line = String.format("%-20s%-20s%s", timeStamp, lowerBound, upperBound);
		
		return line;
	}
	
	public boolean equals(Object obj){
		if (obj == null){
            return false;
		} 
		if (obj == this){
            return true;
		}
		if (!(obj instanceof Bounds)){
            return false;
		}
		
        Bounds objBounds = (Bounds) obj;
        
        if(timeStamp != objBounds.timeStamp){
        	return false;
        }
        
        if(Maths.compare(lowerBound.doubleValue(), objBounds.lowerBound.doubleValue()) != 0){
        	return false;
        }
        
        if(Maths.compare(upperBound.doubleValue(), objBounds.upperBound.doubleValue()) != 0){
        	return false;
        }
        
        return true;
	}
}

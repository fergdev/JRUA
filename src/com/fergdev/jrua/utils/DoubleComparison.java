package nz.ac.massey.buto.utils;

/**
 * Contains centralized method for the comparison of double values.
 * 
 * @author Fergus Hewson
 *
 */
public class DoubleComparison {

	/**
	 * Compares two double values.
	 * 
	 * @param valueA
	 * @param valueB
	 * @return
	 */
	public static int compare(double valueA, double valueB){
		
		double delta = 0.0000000001;
		double difference = Math.abs(valueA - valueB);
		
		if(difference < delta){
			return 0;
		}else if(valueA > valueB)
			return 1;
		else
			return -1;
	}
}

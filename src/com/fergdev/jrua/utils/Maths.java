package nz.ac.massey.buto.utils;

public class Maths {

	/**
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @return
	 */
	public static Double linearInterpolate(Number x1, Number y1, Number x2, Number y2, Number x3){
		
		// Check that the x3 value is greater than the x1 value
		if(Maths.compare(x1.doubleValue(), x3.doubleValue()) > 0){
			System.err.println("INTERPOLATION ERROR X1 greater than X3");
			return null;
		}
		
		// Check the x3 value is less than the x2 value
		if(Maths.compare(x2.doubleValue(), x3.doubleValue()) < 0){
			System.err.println("INTERPOLATION ERROR X3 greater than X2");
			return null;
		}
		
		// Calculate the x distance between points
		double width = x2.doubleValue() - x1.doubleValue();
		
		// Calculate the y distance between points
		double height = y2.doubleValue() - y1.doubleValue();
		
		// Calculate the offset of x3 from the first point, on the x axis
		double x3Offset = x3.doubleValue() - x1.doubleValue();
		
		// Calculate the ratio of the x3offset value to the x distance between points
		double xRatio = x3Offset / width;
		
		// Calculate the offset of the interpolated y3 value
		double y3Offset = (height * xRatio);
		
		// Calculate the total value of y3
		double y3 = y3Offset + y1.doubleValue();
		
		// return y3
		return y3;
	}

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

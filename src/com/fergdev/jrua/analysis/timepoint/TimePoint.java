package nz.ac.massey.buto.analysis.timepoint;

import org.apache.commons.math3.stat.StatUtils;

/**
 * A point is a set of relevant time stamp value pairs.
 * 
 * @author Fergus Hewson
 *
 */
public class TimePoint{
	
	public double timeStampMean;
	public double timeStampStdev;
	
	public double valueMean;
	public double valueMedian;
	public double valueStdev;
	
	public TimePoint(double timeStampMean, double timeStampStdev, double valueMean, double valueMedian, double valueStdev){
		this.timeStampMean = timeStampMean;

		
		this.valueMean = valueMean;
		this.valueMedian = valueMedian;
		this.valueStdev = valueStdev;
	}
	
	public TimePoint(double[] timestampArray, double[] values){
		timeStampMean = StatUtils.mean(timestampArray);
		timeStampStdev = Math.sqrt(StatUtils.variance(timestampArray));
		
		valueMean = StatUtils.mean(values);
		valueMedian = StatUtils.percentile(values, 50);
		valueStdev = Math.sqrt(StatUtils.variance(values));
	}

	
	
}
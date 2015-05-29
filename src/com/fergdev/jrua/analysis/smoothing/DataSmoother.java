package nz.ac.massey.buto.analysis.smoothing;

import java.util.Map;

import nz.ac.massey.buto.domain.ButoProperty;

/**
 * Provides smoothing for data sets to remove random variations in that data.
 * 
 * @author Fergus Hewson
 *
 */
public interface DataSmoother {

	/**
	 * Applies smoothing to a given data set.
	 * 
	 * @param data The data to smooth.
	 * @param smoothingSize The size of the smoothing window to use.
	 * @param dataType The data type of the data being smoothed. 
	 * @return Smoothed data set.
	 */
	public Map<Long, Number> smooth(Map<Long, Number> data, int smoothingSize, ButoProperty dataType);
}
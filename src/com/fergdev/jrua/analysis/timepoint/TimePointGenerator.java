package nz.ac.massey.buto.analysis.timepoint;

import java.util.List;
import java.util.Map;

/**
 * Interface for TimePointGernerators. Class has a single method that takes in a data set
 * and generates a List of time point objects. Essentially implementations of this class are responsible for
 * the grouping together of data items from different test runs.
 * 
 * @author Fergus Hewson
 *
 */
public interface TimePointGenerator {

	/**
	 * Generates a list of time points from a data set.
	 * @param data The data set to generate time points from.
	 * @return List of timepoints.
	 */
	public List<TimePoint> generateTimePoints(List<Map<Long, Number>> data, int timePointInterval);
}

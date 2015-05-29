package nz.ac.massey.buto.analysis.algorithms;

import java.util.List;
import java.util.Map;

import nz.ac.massey.buto.analysis.algorithms.models.ClassificationModelCreationException;
import nz.ac.massey.buto.analysis.bounds.Bounds;
import nz.ac.massey.buto.analysis.timepoint.TimePoint;
import nz.ac.massey.buto.domain.ButoProperty;

/**
 * Interface implemented by algorithms that classify training runs.
 * 
 * @author Fergus Hewson
 *
 */
public interface ClassificationAlgorithm {

	/**
	 * Creates a model of the runtime performance of a profile.
	 * @param trainingRuns The training data for the model.
	 * @param type DataType for this data set, the bounds for this object is used as the bounds for the generated datamodel.
	 * @param params Map of parameters used to configure the algorithm.
	 * @throws ClassificationModelCreationException When there is an error creating the model.
	 */
	public List<Bounds> generateBounds(List<TimePoint> timePointList, ButoProperty type, Map<String, String> params) throws ClassificationModelCreationException;
}

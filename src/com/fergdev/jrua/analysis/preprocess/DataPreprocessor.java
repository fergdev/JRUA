package nz.ac.massey.buto.analysis.preprocess;

import java.util.Map;

public interface DataPreprocessor {

	public Map<Long, Number> preProcess(Map<Long, Number> data);
	
}
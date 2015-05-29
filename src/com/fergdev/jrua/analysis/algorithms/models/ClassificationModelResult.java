package nz.ac.massey.buto.analysis.algorithms.models;

import java.util.List;
import java.util.Map;

/**
 * Result object returned from a model classification.
 * 
 * @author Fergus Hewson
 *
 */
public class ClassificationModelResult {

	private ClassificationModel model;
	
	private List<Long> errorTimeStampList;
	
	private List<Long> warningTimeStampList;
	
	private Map<Long, Number> testData;
	
	public ClassificationModelResult(ClassificationModel model, Map<Long,Number> testData, List<Long> errorTimeStampList, List<Long> warningTimeStampList){
		this.model = model;
		this.errorTimeStampList = errorTimeStampList;
		this.warningTimeStampList = warningTimeStampList;
		this.testData = testData;
	}

	public ClassificationModel getModel() {
		return model;
	}

	public List<Long> getErrorTimeStampList() {
		return errorTimeStampList;
	}
	
	public List<Long> getWarningTimeStampList(){
		return warningTimeStampList;
	}
	
	public Map<Long,Number> getTestData(){
		return testData;
	}
	
	public boolean sucessful(){
		return errorTimeStampList.size() == 0;
	}
}

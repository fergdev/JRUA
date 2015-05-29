package nz.ac.massey.buto.analysis.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the MetaDataClassificationModel.
 * 
 * @author Fergus Hewson
 *
 */
public class MetaDataClassifierTest {

	private MetaDataClassifier model;
	private List<Map<String, Object>> trainingDataList;
	private List<String> filterList;
	
	@Before
	public void setup() throws MetaDataClassifierCreationException{
		
		Map<String, Object> inputMetaData = new HashMap<String,Object>();
		
		inputMetaData.put("stringKeyA", "valueA");
		inputMetaData.put("stringKeyB", "valueB");
		inputMetaData.put("stringKeyC", "valueC");
		
		inputMetaData.put("intKeyA", 1);
		inputMetaData.put("intKeyB", 2);
		inputMetaData.put("intKeyC", 3);
		
		inputMetaData.put("longKeyA", 1l);
		inputMetaData.put("longKeyB", 2l);
		inputMetaData.put("longKeyC", 3l);
		
		inputMetaData.put("doubleKeyA", 1.0d);
		inputMetaData.put("doubleKeyB", 2.0d);
		inputMetaData.put("doubleKeyC", 3.0d);
		
		trainingDataList = new ArrayList<Map<String, Object>>();
		trainingDataList.add(inputMetaData);
		
		// Filter List
		filterList = new ArrayList<String>();
		filterList.add("filterA");
		filterList.add("filterB");
		filterList.add("filterC");
		
		// Create model
		model = new MetaDataClassifier(trainingDataList, filterList);
	}
	
	@After
	public void tearDown(){
		model = null;
		trainingDataList = null;
		filterList = null;
	}
	
	@Test(expected=MetaDataClassifierCreationException.class)
	public void testNoDataTrainingSet() throws MetaDataClassifierCreationException{
		
		// Create model
		model = new MetaDataClassifier(new ArrayList<Map<String, Object>>(), new ArrayList<String>());
		
		// Error no data
		model.classify(new HashMap<String,Object>());
	}
	
	@Test
	public void inconsistentModelTest(){
		
	}
	
	@Test
	public void inconsistentModelFilteredTest(){
		
	}
	
	@Test
	public void testEmptyTrainingSet() throws MetaDataClassifierCreationException{
		
		List<Map<String, Object>> trainingSet = new ArrayList<Map<String, Object>>();
		trainingSet.add(new HashMap<String,Object>());
		
		// Create model
		model = new MetaDataClassifier(trainingSet, new ArrayList<String>());
		
		// Classify
		List<MetaDataDifference> differences = model.classify(new HashMap<String,Object>());
		
		// Test no differences
		assertEquals(0, differences.size());
	}
	
	@Test
	public void testEqual(){
		
		// Classify
		List<MetaDataDifference> differences = model.classify(trainingDataList.get(0));
		
		assertEquals(0, differences.size());
	}
	
	@Test
	public void testMissingValues(){

		//******************************************************
		// Empty
		Map<String, Object> testMetaData = new HashMap<String,Object>();
		
		// Classify
		List<MetaDataDifference> differences = model.classify(testMetaData);
		
		assertEquals(12, differences.size());
		
		//******************************************************
		// 8 missing
		testMetaData = new HashMap<String,Object>();
		
		testMetaData.put("stringKeyA", "valueA");
		
		testMetaData.put("intKeyA", 1);
		
		testMetaData.put("longKeyA", 1l);
		
		testMetaData.put("doubleKeyA", 1.0d);
		
		// Classify
		differences = model.classify(testMetaData);
		
		assertEquals(8, differences.size());
		
		//******************************************************
		// 4 missing
		testMetaData = new HashMap<String,Object>();
		
		testMetaData.put("stringKeyA", "valueA");
		testMetaData.put("stringKeyB", "valueB");
		
		testMetaData.put("intKeyA", 1);
		testMetaData.put("intKeyB", 2);
		
		testMetaData.put("longKeyA", 1l);
		testMetaData.put("longKeyB", 2l);

		testMetaData.put("doubleKeyA", 1.0d);
		testMetaData.put("doubleKeyB", 2.0d);
		
		// Classify
		differences = model.classify(testMetaData);
		
		assertEquals(4, differences.size());
	}
	
	@Test
	public void testDifferentValues(){

		//******************************************************
		// 12 different
		Map<String, Object> testMetaData = new HashMap<String,Object>();
		
		testMetaData.put("stringKeyA", "valueA 2");
		testMetaData.put("stringKeyB", "valueB 2");
		testMetaData.put("stringKeyC", "valueC 2");
		
		testMetaData.put("intKeyA", 4);
		testMetaData.put("intKeyB", 5);
		testMetaData.put("intKeyC", 6);
		
		testMetaData.put("longKeyA", 4l);
		testMetaData.put("longKeyB", 5l);
		testMetaData.put("longKeyC", 6l);
		
		testMetaData.put("doubleKeyA", 4.0d);
		testMetaData.put("doubleKeyB", 5.0d);
		testMetaData.put("doubleKeyC", 6.0d);
		
		// Classify
		List<MetaDataDifference> differences = model.classify(testMetaData);
		
		assertEquals(12, differences.size());
		
		//******************************************************
		// 8 different
		testMetaData = new HashMap<String,Object>();
		
		testMetaData.put("stringKeyA", "valueA");
		testMetaData.put("stringKeyB", "valueB 2");
		testMetaData.put("stringKeyC", "valueC 2");
		
		testMetaData.put("intKeyA", 1);
		testMetaData.put("intKeyB", 5);
		testMetaData.put("intKeyC", 6);
		
		testMetaData.put("longKeyA", 1l);
		testMetaData.put("longKeyB", 5l);
		testMetaData.put("longKeyC", 6l);
		
		testMetaData.put("doubleKeyA", 1.0d);
		testMetaData.put("doubleKeyB", 5.0d);
		testMetaData.put("doubleKeyC", 6.0d);
		
		// Classify
		differences = model.classify(testMetaData);
		
		assertEquals(8, differences.size());
		
		//******************************************************
		// 4 different
		testMetaData = new HashMap<String,Object>();
		
		testMetaData.put("stringKeyA", "valueA");
		testMetaData.put("stringKeyB", "valueB");
		testMetaData.put("stringKeyC", "valueC 2");
		
		testMetaData.put("intKeyA", 1);
		testMetaData.put("intKeyB", 2);
		testMetaData.put("intKeyC", 6);
		
		testMetaData.put("longKeyA", 1l);
		testMetaData.put("longKeyB", 2l);
		testMetaData.put("longKeyC", 6l);
		
		testMetaData.put("doubleKeyA", 1.0d);
		testMetaData.put("doubleKeyB", 2.0d);
		testMetaData.put("doubleKeyC", 6.0d);
		
		// Classify
		differences = model.classify(testMetaData);
		
		assertEquals(4, differences.size());
	}
	

	@Test
	public void testExtraValues(){

		//******************************************************
		// 4 Extra
		Map<String, Object> testMetaData = new HashMap<String,Object>();
		
		testMetaData.put("stringKeyA", "valueA");
		testMetaData.put("stringKeyB", "valueB");
		testMetaData.put("stringKeyC", "valueC");
		testMetaData.put("stringKeyD", "valueD");
		
		testMetaData.put("intKeyA", 1);
		testMetaData.put("intKeyB", 2);
		testMetaData.put("intKeyC", 3);
		testMetaData.put("intKeyD", 4);
		
		testMetaData.put("longKeyA", 1l);
		testMetaData.put("longKeyB", 2l);
		testMetaData.put("longKeyC", 3l);
		testMetaData.put("longKeyD", 4l);
		
		testMetaData.put("doubleKeyA", 1.0d);
		testMetaData.put("doubleKeyB", 2.0d);
		testMetaData.put("doubleKeyC", 3.0d);
		testMetaData.put("doubleKeyD", 4.0d);
		
		// Classify
		List<MetaDataDifference> differences = model.classify(testMetaData);
		
		assertEquals(4, differences.size());
		
		//******************************************************
		// 8 Extra
		testMetaData = new HashMap<String,Object>();
		
		testMetaData.put("stringKeyA", "valueA");
		testMetaData.put("stringKeyB", "valueB");
		testMetaData.put("stringKeyC", "valueC");
		testMetaData.put("stringKeyD", "valueD");
		testMetaData.put("stringKeyE", "valueE");
		
		testMetaData.put("intKeyA", 1);
		testMetaData.put("intKeyB", 2);
		testMetaData.put("intKeyC", 3);
		testMetaData.put("intKeyD", 4);
		testMetaData.put("intKeyE", 5);
		
		testMetaData.put("longKeyA", 1l);
		testMetaData.put("longKeyB", 2l);
		testMetaData.put("longKeyC", 3l);
		testMetaData.put("longKeyD", 4l);
		testMetaData.put("longKeyE", 5l);
		
		testMetaData.put("doubleKeyA", 1.0d);
		testMetaData.put("doubleKeyB", 2.0d);
		testMetaData.put("doubleKeyC", 3.0d);
		testMetaData.put("doubleKeyD", 4.0d);
		testMetaData.put("doubleKeyE", 5.0d);
		
		// Classify
		differences = model.classify(testMetaData);
		
		assertEquals(8, differences.size());
		
		//******************************************************
		// 12 Extra
		testMetaData = new HashMap<String,Object>();
		
		testMetaData.put("stringKeyA", "valueA");
		testMetaData.put("stringKeyB", "valueB");
		testMetaData.put("stringKeyC", "valueC");
		testMetaData.put("stringKeyD", "valueD");
		testMetaData.put("stringKeyE", "valueE");
		testMetaData.put("stringKeyF", "valueF");
		
		testMetaData.put("intKeyA", 1);
		testMetaData.put("intKeyB", 2);
		testMetaData.put("intKeyC", 3);
		testMetaData.put("intKeyD", 4);
		testMetaData.put("intKeyE", 5);
		testMetaData.put("intKeyF", 6);
		
		testMetaData.put("longKeyA", 1l);
		testMetaData.put("longKeyB", 2l);
		testMetaData.put("longKeyC", 3l);
		testMetaData.put("longKeyD", 4l);
		testMetaData.put("longKeyE", 5l);
		testMetaData.put("longKeyF", 6l);
		
		testMetaData.put("doubleKeyA", 1.0d);
		testMetaData.put("doubleKeyB", 2.0d);
		testMetaData.put("doubleKeyC", 3.0d);
		testMetaData.put("doubleKeyD", 4.0d);
		testMetaData.put("doubleKeyE", 5.0d);
		testMetaData.put("doubleKeyF", 6.0d);
		
		// Classify
		differences = model.classify(testMetaData);
		
		assertEquals(12, differences.size());
	}

	@Test
	public void filterTest() throws MetaDataClassifierCreationException{
		
		Map<String, Object> inputMetaData = new HashMap<String,Object>();
		
		inputMetaData.put("stringKeyA", "valueA");
		inputMetaData.put("stringKeyB", "valueB");
		inputMetaData.put("stringKeyC", "valueC");
		inputMetaData.put("stringFilterA", "valueFilterA");
		
		inputMetaData.put("intKeyA", 1);
		inputMetaData.put("intKeyB", 2);
		inputMetaData.put("intKeyC", 3);
		inputMetaData.put("intFilterA", 11);
		
		inputMetaData.put("longKeyA", 1l);
		inputMetaData.put("longKeyB", 2l);
		inputMetaData.put("longKeyC", 3l);
		inputMetaData.put("longFilterA", 11l);
		
		inputMetaData.put("doubleKeyA", 1.0d);
		inputMetaData.put("doubleKeyB", 2.0d);
		inputMetaData.put("doubleKeyC", 3.0d);
		inputMetaData.put("doubleFilterA", 11.0d);
		
		trainingDataList = new ArrayList<Map<String, Object>>();
		trainingDataList.add(inputMetaData);
		
		// Filter List
		filterList = new ArrayList<String>();
		filterList.add("stringFilterA");
		filterList.add("intFilterA");
		filterList.add("longFilterA");
		filterList.add("doubleFilterA");
		
		// Create model
		model = new MetaDataClassifier(trainingDataList, filterList);
		
		List<MetaDataDifference> differences = model.classify(inputMetaData);
		assertEquals(0, differences.size());
	}
}

package nz.ac.massey.buto.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;

import nz.ac.massey.buto.analysis.bounds.Bounds;
import nz.ac.massey.buto.analysis.timepoint.TimePoint;
import nz.ac.massey.buto.domain.ButoProperty;
import nz.ac.massey.buto.domain.Oracle;
import nz.ac.massey.buto.domain.TrainingRun;

/**
 * Collection of tools for generating graphs.
 * 
 * @author Fergus Hewson
 *
 */
public class GraphingTools {

	/**
	 * Generates a graph of the DataType for the given oracle.
	 * 
	 * @param oracle The oracle to graph.
	 * @param dataType The data type to graph.
	 * @return A series collection.
	 */
	public static XYSeriesCollection graphOracle(Oracle oracle, ButoProperty dataType){
		
		// Get the data list
		List<Map<Long,Number>> dataList = oracle.getData(dataType.getKey());
		
		// Generate series
		XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
		
		int index = 0;
		
		for(Map<Long,Number> data : dataList){
			
			XYSeries series = new XYSeries(index++);
			
			for(Long key : data.keySet()){
				series.add((double)key, data.get(key).doubleValue());
			}
			
			xySeriesCollection.addSeries(series);
		}
		
		return xySeriesCollection;
	}
	
	public static XYSeriesCollection graphTrainingRunList(List<TrainingRun> trainingRunsList, ButoProperty dataType){
		
		List<Map<Long,Number>> dataList = new ArrayList<Map<Long,Number>>();
		
		for(TrainingRun run : trainingRunsList){
			dataList.add(run.getData(dataType.getKey()));
		}
		
		// Generate series
		XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
		
		int index = 0;
		
		for(Map<Long,Number> data : dataList){
			
			XYSeries series = new XYSeries(index++);
			
			for(Long key : data.keySet()){
				series.add((double)key, data.get(key).doubleValue());
			}
			
			xySeriesCollection.addSeries(series);
		}
		
		return xySeriesCollection;
	}
	
	public static YIntervalSeries toAnalysisSeries(List<Bounds> boundsList){
		
		// create interval series for output
		YIntervalSeries yintervalseries = new YIntervalSeries("Oracle");
		
		for(Bounds bound : boundsList){
			
			double avg = (bound.lowerBound.doubleValue() + bound.upperBound.doubleValue()) / 2;
			
			yintervalseries.add(bound.timeStamp, avg, bound.lowerBound.doubleValue(), bound.upperBound.doubleValue());
		}
		
		// return y interval series
		return yintervalseries;
	}
	
	public static YIntervalSeries toAnalysisSeries(TrainingRun trainingRun, ButoProperty dataType){
		
		return toAnalysisSeries(trainingRun.getData(dataType.getKey()));
	}
	
	public static YIntervalSeries toAnalysisSeries(Map<Long, Number> data){
		
		// create interval series for output
		YIntervalSeries yintervalseries = new YIntervalSeries("Oracle");
		
		for(Long key : data.keySet()){
			
			double value = data.get(key).doubleValue();
			
			yintervalseries.add(key.doubleValue(), value, value, value);
		}
		
		// return y interval series
		return yintervalseries;
	}
	
	public static XYSeriesCollection graphDataSet(List<Map<Long, Number>> dataList) {
		
		XYSeriesCollection outputData = new XYSeriesCollection();
		
		int index = 1;
		for(Map<Long, Number> data : dataList){
			
			XYSeries series = new XYSeries(index);
			for(Long key : data.keySet()){
				
				series.add(key, data.get(key));
			}
			outputData.addSeries(series);
			index++;
		}
		
		return outputData;
	}
	
	public static XYDataset graphTimePointList(List<TimePoint> timePointList){
		
		XYSeriesCollection outputData = new XYSeriesCollection();
		
		XYSeries series = new XYSeries("Time Point");
		
		// graph timepoint list
		for(TimePoint timePoint : timePointList){
			series.add( timePoint.timeStampMean, timePoint.valueMean);
		}
		
		outputData.addSeries(series);
		
		return outputData;
	}
	
	public static YIntervalSeriesCollection graphTimePointListDeviations(List<TimePoint> timePointList){
		
		YIntervalSeriesCollection timePointSeriesCollection = new YIntervalSeriesCollection();
		
		YIntervalSeries outputData1 = new YIntervalSeries("Time Point 1");
		YIntervalSeries outputData2 = new YIntervalSeries("Time Point 2");
		YIntervalSeries outputData3 = new YIntervalSeries("Time Point 3");
		
		timePointSeriesCollection.addSeries(outputData1);
		timePointSeriesCollection.addSeries(outputData2);
		timePointSeriesCollection.addSeries(outputData3);
		
		// graph timepoint list
		for(TimePoint timePoint : timePointList){

			double ylow1 = timePoint.valueMean - ( timePoint.valueStdev);
			double yhigh1 = timePoint.valueMean + ( timePoint.valueStdev);
			
			outputData1.add(timePoint.timeStampMean, timePoint.valueMean, ylow1, yhigh1);
			
			double ylow2 = timePoint.valueMean - ( timePoint.valueStdev * 2);
			double yhigh2 = timePoint.valueMean + ( timePoint.valueStdev * 2);
			
			outputData2.add(timePoint.timeStampMean, timePoint.valueMean, ylow2, yhigh2);
			
			double ylow3 = timePoint.valueMean - ( timePoint.valueStdev * 3);
			double yhigh3 = timePoint.valueMean + ( timePoint.valueStdev * 3);
			
			outputData3.add(timePoint.timeStampMean, timePoint.valueMean, ylow3, yhigh3);
		}
		
		return timePointSeriesCollection;
	}
  
}

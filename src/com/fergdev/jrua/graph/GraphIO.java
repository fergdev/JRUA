package nz.ac.massey.buto.graph;

import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

public class GraphIO {

	
	public static void save(JFreeChart chart, File outputFile){
		
		try {
			ChartUtilities.saveChartAsJPEG(outputFile, chart, 800, 600);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

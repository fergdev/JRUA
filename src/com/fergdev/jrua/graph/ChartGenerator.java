package nz.ac.massey.buto.graph;

import java.awt.BasicStroke;
import java.awt.Color;

import nz.ac.massey.buto.domain.ButoProperty;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.SeriesRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DeviationRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.YIntervalSeriesCollection;
import org.jfree.ui.RectangleInsets;

/**
 * Set of methods that generate the different types of charts used by buto.
 * 
 * @author Fergus Hewson
 *
 */
public class ChartGenerator {
	  
    /**
     * Creates a deviation chart.
     * 
     * @param xydataset
     * @param key
     * @return
     */
    public static JFreeChart createDeviationChart(YIntervalSeriesCollection xydataset, ButoProperty butoProperty){   
    	
    	XYDataset set = (XYDataset) xydataset;
   
        JFreeChart jfreechart = ChartFactory.createXYLineChart(
        		butoProperty.getName() + " vs Time", "Time (ms)", 
        		butoProperty.getName() + " (" + butoProperty.getUnits() + ")", 
        		set, 
        		PlotOrientation.VERTICAL, 
        		false, 
        		true, 
        		false);
        
        jfreechart.setBackgroundPaint(Color.white);   
        
        // Render forward
        XYPlot xyplot = (XYPlot)jfreechart.getPlot();   
        xyplot.setSeriesRenderingOrder(SeriesRenderingOrder.FORWARD);
        
        xyplot.setInsets(new RectangleInsets(5D, 5D, 5D, 20D));   
        xyplot.setBackgroundPaint(Color.white);   
        xyplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));   
        xyplot.setDomainGridlinePaint(Color.lightGray);   
        xyplot.setRangeGridlinePaint(Color.lightGray);   
        
        // Set the renderer
        DeviationRenderer deviationrenderer = new DeviationRenderer(true, false);   
        
        deviationrenderer.setSeriesStroke(0, new BasicStroke(0F, 1, 1));  
        deviationrenderer.setSeriesPaint(0, new Color(180, 180, 180));
        deviationrenderer.setSeriesFillPaint(0, new Color(180, 180, 180));  
        //deviationrenderer.setSeriesPaint(0, new Color(108, 255, 100));
        //deviationrenderer.setSeriesFillPaint(0, new Color(200, 255, 200));    
         
        deviationrenderer.setSeriesStroke(1, new BasicStroke(0F, 1, 1));  
        deviationrenderer.setSeriesPaint(1, Color.black);
        deviationrenderer.setSeriesFillPaint(1,  Color.black); 
        //deviationrenderer.setSeriesPaint(1, new Color(255, 100, 100));
        //deviationrenderer.setSeriesFillPaint(1, new Color(255, 200, 200));  
        
        xyplot.setRenderer(deviationrenderer);   
    
        // Set axis
        NumberAxis numberaxis = (NumberAxis)xyplot.getRangeAxis();     
        numberaxis.setRange(0.0, 1.0);
        //numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());   
        
        return jfreechart;   
    }
    
   public static JFreeChart createDeviationChart2(YIntervalSeriesCollection xydataset, ButoProperty butoProperty){   
    	
    	// Lookup name and units
    	XYDataset set = (XYDataset) xydataset;
    	
        JFreeChart jfreechart = ChartFactory.createXYLineChart(
        		butoProperty.getName() + " vs Time", "Time (ms)", 
        		butoProperty.getName() + " (" + butoProperty.getUnits() + ")", 
        		set, 
        		PlotOrientation.VERTICAL, 
        		false, 
        		true, 
        		false
        		);
        
        jfreechart.setBackgroundPaint(Color.white);   
        
        // Render forward
        XYPlot xyplot = (XYPlot)jfreechart.getPlot();   
        xyplot.setSeriesRenderingOrder(SeriesRenderingOrder.FORWARD);
        
        xyplot.setInsets(new RectangleInsets(5D, 5D, 5D, 20D));   
        xyplot.setBackgroundPaint(Color.white);   
        xyplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));   
        xyplot.setDomainGridlinePaint(Color.lightGray);   
        xyplot.setRangeGridlinePaint(Color.lightGray);   
        
        // Set the renderer
        DeviationRenderer deviationrenderer = new DeviationRenderer(true, false);   
        
        deviationrenderer.setSeriesStroke(0, new BasicStroke(0F, 1, 1));  
        deviationrenderer.setSeriesPaint(0, new Color(122, 255, 150));
        deviationrenderer.setSeriesFillPaint(0, new Color(122, 255, 150));    
         
        for(int index = 1; index < xydataset.getSeriesCount(); index++){
            deviationrenderer.setSeriesStroke(index, new BasicStroke(2F, 1, 1));   
            deviationrenderer.setSeriesPaint(index, Color.red);
            deviationrenderer.setSeriesFillPaint(index,  Color.red); 
        }

        xyplot.setRenderer(deviationrenderer);   
        
        // Set axis
        //NumberAxis numberaxis = (NumberAxis)xyplot.getRangeAxis();     
        //numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());   
        
        return jfreechart;   
    }
   
	/**
	 * Deviation chart for timepoints.
	 * 
	 * @param xydataset
	 * @param butoProperty
	 * @return
	 */
	public static JFreeChart createDeviationChartTimePoint(
			YIntervalSeriesCollection xydataset, ButoProperty butoProperty) {

		// Lookup name and units
		XYDataset set = (XYDataset) xydataset;

		JFreeChart jfreechart = ChartFactory.createXYLineChart(
				butoProperty.getName() + " vs Time", "Time (ms)",
				butoProperty.getName() + " (" + butoProperty.getUnits() + ")", 
				set,
				PlotOrientation.VERTICAL, 
				false, 
				true, 
				false);

		jfreechart.setBackgroundPaint(Color.white);

        // Render forward
        XYPlot xyplot = (XYPlot)jfreechart.getPlot();   
        xyplot.setSeriesRenderingOrder(SeriesRenderingOrder.FORWARD);
        
        xyplot.setInsets(new RectangleInsets(5D, 5D, 5D, 20D));   
        xyplot.setBackgroundPaint(Color.white);   
        xyplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));   
        xyplot.setDomainGridlinePaint(Color.lightGray);   
        xyplot.setRangeGridlinePaint(Color.lightGray);   
        
        // Set the renderer
        DeviationRenderer deviationrenderer = new DeviationRenderer(true, false);   
        
        Color color0 = new Color(80,80,80);
        Color color1 = new Color(140,140,140);
        Color color2 = new Color(210,210,210);
        
        deviationrenderer.setSeriesStroke(0, new BasicStroke(1F, 1, 1));  
        deviationrenderer.setSeriesPaint(0, color0);
        deviationrenderer.setSeriesFillPaint(0, color0);    
         
        deviationrenderer.setSeriesStroke(1, new BasicStroke(0F, 1, 1));   
        deviationrenderer.setSeriesPaint(1, color1);
        deviationrenderer.setSeriesFillPaint(1, color1);  
        
        deviationrenderer.setSeriesStroke(2, new BasicStroke(0F, 1, 1)); 
        deviationrenderer.setSeriesPaint(2, color2);
        deviationrenderer.setSeriesFillPaint(2, color2); 
        
        xyplot.setRenderer(deviationrenderer);   
        

		return jfreechart;
	}

	/**
	 * Creates a simple chart.
	 * @param xydataset
	 * @param key
	 * @param training
	 * @return
	 */
    public static JFreeChart createTrainingChart(XYDataset xydataset, ButoProperty butoProperty){   
    	
    	// Lookup name and units
    	String name = butoProperty.getName();
    	String units = butoProperty.getUnits();
    	
    	// Create the chart
        JFreeChart jfreechart = ChartFactory.createXYLineChart(name + " vs Time", "Time (ms)", name + " (" + units + ")", xydataset, PlotOrientation.VERTICAL, false, true, false);
        jfreechart.setBackgroundPaint(Color.white);   
        
        XYPlot xyplot = (XYPlot)jfreechart.getPlot();   
        
        xyplot.setSeriesRenderingOrder(SeriesRenderingOrder.FORWARD);
        
        xyplot.setInsets(new RectangleInsets(5D, 5D, 5D, 20D));   
        xyplot.setBackgroundPaint(Color.white);   
        
        xyplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));   
        xyplot.setDomainGridlinePaint(Color.lightGray);   
        xyplot.setRangeGridlinePaint(Color.lightGray);   
        
        // Change renderer
        XYItemRenderer renderer = xyplot.getRenderer();
        
        for(int index = 0; index < xydataset.getSeriesCount(); index++){
        	
        	renderer.setSeriesPaint(index, new Color(100, 255, 100));
        	
        	if(index == xydataset.getSeriesCount() - 1){
        		renderer.setSeriesPaint(index, new Color(255, 100, 100));
        		renderer.setSeriesStroke(index , new BasicStroke(3F, 1, 1));
        	}
        }
     
        //NumberAxis numberaxis = (NumberAxis)xyplot.getRangeAxis();     
        //numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());   
        
        return jfreechart;   
    }
    
    /**
     * Creates a standard JFreechart chart.
     * 
     * @param xydataset The data set to graph.
     * @param butoProperty The data type being graphed, this is used for titles and labels.
     * @return A JFreeChart chart.
     */
    public static JFreeChart createStandardChart(XYDataset xydataset, ButoProperty butoProperty){   
    	
    	// Lookup name and units
    	String name = butoProperty.getName();
    	String units = butoProperty.getUnits();
    	
    	// Create the chart
        JFreeChart jfreechart = ChartFactory.createXYLineChart(name + " vs Time", "Time (ms)", name + " (" + units + ")", xydataset, PlotOrientation.VERTICAL, false, true, false);
        jfreechart.setBackgroundPaint(Color.white);   
        
        XYPlot xyplot = (XYPlot)jfreechart.getPlot();   
        xyplot.setSeriesRenderingOrder(SeriesRenderingOrder.FORWARD);
        
        xyplot.setInsets(new RectangleInsets(5D, 5D, 5D, 20D));   
        xyplot.setBackgroundPaint(Color.white);   
        
        xyplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));   
        xyplot.setDomainGridlinePaint(Color.lightGray);   
        xyplot.setRangeGridlinePaint(Color.lightGray);   
     
        // Change renderer
        XYItemRenderer renderer = xyplot.getRenderer();
        
        for(int index = 0; index < xydataset.getSeriesCount(); index++){
        	renderer.setSeriesStroke(index, new BasicStroke(0.25F, 1, 1));   
        	renderer.setSeriesPaint(index, Color.black);
       }
        
        
        NumberAxis numberaxis = (NumberAxis)xyplot.getRangeAxis();     
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        return jfreechart;   
    }
    /**
     * Creates an empty JFreeChart chart, used for displaying graphs that are empty.
     * @return An empty JFreeChart.
     */
    public static JFreeChart createEmptySimpleChart(){
    	return new JFreeChart(new XYPlot());
    }
}

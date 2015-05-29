package nz.ac.massey.buto.analysis.smoothing;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Class to manage data smoother objects.
 * 
 * @author Fergus Hewson
 *
 */
public class DataSmootherManager {

	/**
	 * Log4j logger.
	 */
	private static Logger logger = LogManager.getLogger(DataSmootherManager.class);
	
	
	/**
	 * List of loaded data smoothers.
	 */
	private static ServiceLoader<DataSmoother> dataSmootherLoader = ServiceLoader.load(DataSmoother.class);
	

	/**
	 * Gets the names of the currently loaded data smoothers.
	 * 
	 * @return Array containing the names of the currently loaded data smoothers.
	 */
	public static String[] getNames(){
		List<String> nameList = new ArrayList<String>(); 
		
		for(DataSmoother  c : dataSmootherLoader){
			nameList.add(c.getClass().getCanonicalName());
		}
		
		return nameList.toArray(new String[nameList.size()]);
	}
	
	/**
	 * Gets the data smoother with the provided name.
	 *  
	 * @return A data smoother object, null if the a smoother was not found.
	 */
	public static DataSmoother getDataSmoother(String name){
		
		// iterate over smoothers
		for(DataSmoother  ds : dataSmootherLoader){
			
			// compare names
			if(ds.getClass().getCanonicalName().equals(name)){
				return ds;
			}
		}
		
		// error service could not be founds
		logger.error("Unable to retrieve DataSmoother '" + name + "'");
		return null;
	}
}

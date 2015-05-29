package nz.ac.massey.buto.analysis.timepoint;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Class that makes TimePointGenerator services available.
 * 
 * @author Fergus Hewson
 *
 */
public class TimePointGeneratorManager {
	
	//get list of services
	private static ServiceLoader<TimePointGenerator> timepointGeneratorLoader = ServiceLoader.load(TimePointGenerator.class);
	
	/**
	 * Gets a list of names for each of the TimePointGenerator services that are available.
	 * @return
	 */
	public static String[] getNames(){
		
		// List of names
		List<String> nameList = new ArrayList<String>(); 
		
		// iterate over avaliable services and retrieve name
		for(TimePointGenerator  c : timepointGeneratorLoader){
			nameList.add(c.getClass().getCanonicalName());
		}
		
		// return array
		return nameList.toArray(new String[nameList.size()]);
	}
	
	/**
	 * Gets the TimePointGenerator service that corresponds to the provided name.
	 * @param name The name of the TimePointGenerator service to retrieve.
	 * @return A TimePointGenerator object, null if the service cannot be found.
	 */
	public static TimePointGenerator getTimePointGenerator(String name){
		
		// iterate over available timepoint generators
		for(TimePointGenerator tpg : timepointGeneratorLoader){
			
			// test if is the service requested
			if(tpg.getClass().getCanonicalName().equals(name)){
				
				// return serice
				return tpg;
			}
		}
		
		// error service could not be founds
		System.err.println("Unable to retrieve TimePointGenerator '" + name + "'");
		return null;
	}
}

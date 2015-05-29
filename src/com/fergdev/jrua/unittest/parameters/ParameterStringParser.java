package nz.ac.massey.buto.unittest.parameters;

import java.util.HashMap;
import java.util.Map;

import nz.ac.massey.buto.domain.ButoProperty;
import nz.ac.massey.buto.domain.ButoPropertyManager;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ParameterStringParser {

	private static Logger logger = LogManager.getLogger(ParameterStringParser.class);
	
	public static Map<String,String> parse(String str) throws MalformedParameterStringException{

		String[] paramsStrings = str.split("&");
		Map<String, String> map = new HashMap<String, String>();
		
		for (String paramString : paramsStrings) {
			
			String[] paramArray = paramString.split("=");
			
			if(paramArray.length != 2){
				String message = "Invalid parameter string '" + paramString + "'";
				logger.error(message);
				throw new MalformedParameterStringException(message);
			}
			
			switch(paramArray[0]){
			case"properties":
				
				String[] propertyArray = paramArray[1].split(",");
				
				for(String propertyKey : propertyArray){
					ButoProperty property = ButoPropertyManager.getButoProperty(propertyKey);
					
					if(property == null){
						String message = "Invalid property key '" + propertyKey + "' see readme for list of valid properties.";
						logger.error(message);
						throw new MalformedParameterStringException(message);
					}
				}
				
				break;
			case "timePointGenerator":
				break;
			case "smoothingSize":
				
				try{
					Integer.parseInt(paramArray[1]);
				}catch(NumberFormatException ex){
					throw new MalformedParameterStringException("Smoothing size cannot be parsed string = '" + paramString + "'");
				}
				
				break;
			case "smoother":
				break;
			case "classificationModelMethod":
				break;
			case "classificationAlgorithm":
				break;
			case "classificationAlgorithmParams":
				// Cannot check....
				break;
			default:
				String message = "Invalid parameter key '" + paramArray[0] + "'";
				logger.error(message);
				throw new MalformedParameterStringException(message);
			}
			
			map.put(paramArray[0], paramArray[1]);
		}

		return map;
	}
}

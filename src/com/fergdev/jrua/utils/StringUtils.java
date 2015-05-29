package nz.ac.massey.buto.utils;

public class StringUtils {

	public static String repeat(String reString, int repeatCount){
		StringBuilder builder = new StringBuilder();
		
		for(int i = 0 ; i < repeatCount; i++){
			builder.append(reString);
		}
		
		return builder.toString();
	}
}

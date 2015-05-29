package nz.ac.massey.buto.unittest.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Utility to run Buto in training mode using the standard JUnit Parameterized test runner.
 * @author Jens Dietrich
 */
public class Repeat {
	
	public static final Object[] EMPTY_ARRAY = new Object[]{};
	public static Collection<Object[]> times(int trainingSetTimes) {
    	List<Object[]> list = new ArrayList<>();
    	for(int i = 0; i < trainingSetTimes; i++){
    		list.add(EMPTY_ARRAY);
    	}
        return list;
	}
}

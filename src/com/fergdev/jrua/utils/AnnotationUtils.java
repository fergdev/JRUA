package nz.ac.massey.buto.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AnnotationUtils {

	
	public static int countMethodsWithAnnotation(Class<?> klass, Class<? extends Annotation> annotation) {
		return getMethodsWithAnnotation(klass, annotation).size();
	}
	
	public static List<Method> getMethodsWithAnnotation(Class<?> klass, Class<? extends Annotation> annotation) {
		List<Method> methodList = new ArrayList<Method>();
		
		for (Method m : klass.getMethods()) {
			if (m.isAnnotationPresent(annotation)) {
				methodList.add(m);
			}
		}
		
		return methodList;
	}

}

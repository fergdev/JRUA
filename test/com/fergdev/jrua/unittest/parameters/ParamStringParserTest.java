package nz.ac.massey.buto.unittest.parameters;

import static org.junit.Assert.*;

import java.util.Map;

import nz.ac.massey.buto.unittest.parameters.ParameterStringParser;

import org.junit.Test;

public class ParamStringParserTest {

	@Test
	public void test1() throws MalformedParameterStringException{
		
		Map<String, String> paramMap = ParameterStringParser.parse("something");
		
		assertTrue(paramMap.size() == 0);
		
	}
}

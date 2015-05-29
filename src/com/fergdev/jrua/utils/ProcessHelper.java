package nz.ac.massey.buto.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import nz.ac.massey.buto.settings.ButoSettings;

/**
 * Class to help with the management of process used during training.
 * 
 * @author Fergus Hewson
 *
 */
public class ProcessHelper {

	/**
	 * Log4j logger.
	 */
	private Logger logger = LogManager.getLogger(ProcessHelper.class);
	
	private ProcessBuilder processBuilder;
	private Process currentProcess;
	private File logFile;

	public ProcessHelper(final String mainClass, final String classpath, final String[] arguments, final String[] options)throws IOException {
		
		// List of commands for the new process
		List<String> command = new ArrayList<String>();

		// Add strings to the command array
		command.add(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java");
		//command.addAll(Arrays.asList(options));
		command.add(mainClass);
		command.addAll(Arrays.asList(arguments));

		// Create the process builder
		processBuilder = new ProcessBuilder(command);

		// Add the classpath to the process builder 
		Map<String, String> environment = processBuilder.environment();
		environment.put("CLASSPATH", classpath);

		// Redirect the process error stream to the output stream
		processBuilder.redirectErrorStream(true);
		 
		// Create the log file
		logFile = new File(ButoSettings.stdoutLogFolder + File.separator + ButoSettings.stdoutLogFileName);
		
		// Redirect the process output to the log file
		processBuilder.redirectOutput(Redirect.appendTo(logFile));
	}

	public Process startProcess() throws IOException {

		// Test if a process is already running
		if (currentProcess != null) {
			logger.error("Attempt to start proccess when one is already executing.");
			return null;
		}
		
		// If the log file exists attempt to delete it
		if (logFile.exists()) {
			if (!logFile.delete()) {
				throw new IOException("Unable to delete the log file. " + logFile.getPath());
			}
		}

		// Create the new log file
		if (!logFile.createNewFile()) {
			throw new IOException("Unable to create the log file. " + logFile.getPath());
		}

		// Start the process
		currentProcess = processBuilder.start();

		return currentProcess;
	}
	
	/**
	 * Returns the log file for the process.
	 * 
	 * @return The log file as a string.
	 */
	public String getLogFile() {
		
		// check if there is a process to retrieve the logfile for
		if(currentProcess == null){
			
			logger.error("Attempt to get log file for closed process.");
			
			return "";
		}
		
		// Retrieve the log file
	    BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(logFile));
		} catch (FileNotFoundException e) {
			
			logger.error("Cannot find log file '" + logFile + "'");
			return "";
		}
		
		// Builder to build the string
		StringBuilder sb = new StringBuilder();
		
	    try {
	       
	    	// Iterate over lines and add to string builder
	        String line = br.readLine();
	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	    }catch(IOException ex){
	    	logger.error("Unable to read from stdout file");
	    	
	    	return "";
	    } finally {
	    	
	        try {
				br.close();
			} catch (IOException e) {
				
				logger.error("Error closing the buffered reader for the stdout.");
				e.printStackTrace();
				
				return "";
			}
	    }

		// return the log file as a string
		return sb.toString();
	}

	/**
	 * Cleans up the current process. 
	 * 
	 * @throws IOException If there was an error destroying the process.
	 */
	public void cleanUpProcess() throws IOException {
		
		// Test if the process is null
		if(currentProcess == null){
			logger.error("Attempt to shutdown a null process.");
			return;
		}
		
		// Destroy the process
		currentProcess.destroy();
		currentProcess = null;
		 
		 // If the log file exists attempt to delete it
		 /*
		 if(logFile.exists()){
			 if(!logFile.delete()){
				 throw new IOException("Unable to create the log file. " + stdoutLogPath);
			 }
		 }*/
		
	}
}
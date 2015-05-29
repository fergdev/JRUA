package nz.ac.massey.buto.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Set of utilities to manage files.
 * 
 * @author Fergus Hewson
 *
 */
public class IOUtils {
	
	/**
	 * Log4j logger.
	 */
	private static Logger logger = LogManager.getLogger(IOUtils.class);
	
	/**
	 * Recursive delete method for deleting directories.
	 * 
	 * @param f The directory to delete.
	 * @throws IOException If the file does not exist.
	 */
	public static void deleteDirectory(File f) throws IOException {
		
		// if directory, delete all files in directory
		if (f.isDirectory()) {
			
			// delete files
			for (File c : f.listFiles())
				deleteDirectory(c);
		}
		
		// delete file
		if (!f.delete()) {
			throw new FileNotFoundException("Failed to delete file: " + f);
		}
	}
	
	/**
	 * Writes the a string to file.
	 * 
	 * @param output File File to write to.
	 * @param text Text to write.
	 */
	public static void writeFile(File outputFile, String text){
		
		// IO
		try {

			// Create writers
			FileWriter fileWriter = new FileWriter(outputFile);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			// Write File
			bufferedWriter.write(text);
			
			// Clean up
			bufferedWriter.close();

		} catch (IOException e) {
			// Handle error
			logger.error("Error writing to file. " + outputFile);
			logger.error(e.getMessage());
		}
	}
	
	public static void writeFile(File outputFile, String text, String encoding) throws IOException{
		  Writer writer = new OutputStreamWriter(new FileOutputStream(outputFile), encoding);
		  
		  BufferedWriter bufferedWriter = new BufferedWriter(writer);
		  
		  bufferedWriter.write(text);
		  
		  bufferedWriter.close();
	}
	
	/**
	 * Reads a given file into a list of strings.
	 * 
	 * @param file The file to read.
	 * @return List of the lines in the file.
	 */
	public static List<String> readFile(File file) {

		// List for lines
		List<String> lines = new ArrayList<String>();

		try {
			// Create reader
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			// read lines
			String line = br.readLine();
			while (line != null) {
				lines.add(line);
				line = br.readLine();
			}
			
			// Clean up
			br.close();

		} catch (IOException e) {
			
			// Handle error
			logger.error("Error reading file." + file);
			logger.error(e.getMessage());
		}

		// return lines
		return lines;
	}
	
	public static String removeExtension(String s) {

	    String separator = File.separator;
	    String filename;

	    // Remove the path upto the filename.
	    int lastSeparatorIndex = s.lastIndexOf(separator);
	    if (lastSeparatorIndex == -1) {
	        filename = s;
	    } else {
	        filename = s.substring(lastSeparatorIndex + 1);
	    }

	    // Remove the extension.
	    int extensionIndex = filename.lastIndexOf(".");
	    if (extensionIndex == -1)
	        return filename;

	    return filename.substring(0, extensionIndex);
	}
	
}

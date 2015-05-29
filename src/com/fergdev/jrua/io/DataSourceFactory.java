package nz.ac.massey.buto.io;

import java.io.File;

import nz.ac.massey.buto.io.filedatasource.FileDataSource;

/**
 * Factory for DataSource objects.
 * 
 * @author Fergus Hewson
 *
 */
public class DataSourceFactory {

	/**
	 * Creates a new File Data source using the provided file as the root folder.
	 * 
	 * @param rootFolder Root folder of the Buto Data Repository.
	 * @return A new file data source object.
	 */
	public static DataSource getFileDataSource(File rootFolder){

		// Make the root folder if it doesnt exist
		if(!rootFolder.exists()){
		//	rootFolder.mkdir();
		}

		// return new datasource
		return new FileDataSource(rootFolder);
	}
}

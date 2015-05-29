package nz.ac.massey.buto.io;

import nz.ac.massey.buto.domain.Oracle;
import nz.ac.massey.buto.domain.Profile;

/**
 * Interface for a Buto Data Source.
 * 
 * @author Fergus Hewson
 *
 */
public interface DataSource {
	
	/**
	 * Gets the array of names for the different profiles accessible by the data source.
	 * 
	 * @return Array of names of profiles contained by this data source.
	 */
	public String[] getProfileNames();
	
	/**
	 * Gets the array of oracle names that are owned by the given profile.
	 * 
	 * @param profile The profile to get the array of oracle names for.
	 * @return Array of oracle names.
	 */
	public String[] getOracleNames(Profile profile);
	
	/**
	 * Loads a Profile with the given profileName from the data source.
	 * 
	 * @param profileName The name of the profile to load.
	 * @return A Profile object with the provided profileName, null if the profile could not be found.
	 */
	public Profile getProfile(String profileName);
	
	/**
	 * Gets an oracle from the given profile with the given oracleName.
	 * 
	 * @param profile The profile to load the oracle from.
	 * @param oracleName The name of the oracle to load.
	 * @return An oracle object, null if the oracle could not be found.
	 */
	public Oracle getOracle(Profile profile, String oracleName);
	
	/**
	 * Saves a given profile to the data source.
	 * 
	 * @param profile The profile to save.
	 */
	public void saveProfile(Profile profile);
	
	/**
	 * Saves a given oracle to the given profile.
	 * 
	 * @param profile The profile to save the orcale to. 
	 * @param oracle The oracle to save.
	 */
	public void saveOracle(Profile profile, Oracle oracle);
	
	/**
	 * Deletes a given profile from the data source.
	 * 
	 * @param profile The profile to delete.
	 */
	public void deleteProfile(Profile profile);
	
	/**
	 * Deletes a given oracle from the data source.
	 * 
	 * @param profile The profile of the oracle to delete.
	 * @param oracle The oracle to delete.
	 */
	public void deleteOracle(Profile profile, Oracle oracle);
	
	/**
	 * Adds a listener to the data source. Listeners will be notified of loading started, progress and loading complete events.
	 * 
	 * @param listener The Listener to add.
	 */
	public void addDataSourceProgressListener(DataSourceListener listener);
	
	/**
	 * Removes a listener from the data source.
	 * 
	 * @param listener The listener to remove from the data source.
	 */
	public void removeDataSourceProgressListener(DataSourceListener listener);
}

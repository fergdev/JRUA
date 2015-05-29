package nz.ac.massey.buto.jmx;

import java.io.IOException;

import javax.management.MBeanServerConnection;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Thread to monitor a connection checking for valid connection every
 * @author fergus
 *
 */
public class JMXTimeoutThread extends Thread{

	/**
	 * Log4j Monitor.
	 */
	private static Logger logger = LogManager.getLogger(JMXTimeoutThread.class);
	
	/**
	 * The MBean server connection being montored
	 */
	private MBeanServerConnection connection;
	
	/**
	 * The interval between checking the connection.
	 */
	private int checkInterval;
	
	/**
	 * Creates a new timeout thread that checks a given connection for validity and if 
	 * the check fails it prints an error message and exits the VM.
	 * 
	 * @param connection The MBeanServerConnection to monitor.
	 * @parm checkInterval The interval between checking the connection.
	 */
	public JMXTimeoutThread(MBeanServerConnection connection, int checkInterval){
		this.connection = connection;
		this.checkInterval = checkInterval;
	}
	
	/**
	 * Starts the timeout thread.
	 */
	public void run(){
		
		// Loop untill interrupted
		while(true){
			
			// Test connection
			if(!testConnection()){
				logger.error("Connection has timed out.... exiting monitor.");
				System.exit(0);
			}
			
			// Sleep
			try {
				Thread.sleep(checkInterval);
			} catch (InterruptedException e) {
				logger.debug("Timeout thread interrupted '" + e.getMessage() + "'");
			}
		}
		
	}
	
	/**
	 * Tests the set connection object for validity, is done by querying the server for 
	 * the number of MBeans contained by the server and catching any IOExceptions.
	 * 
	 * @return True if the connection is still valid, False if not.
	 */
	private boolean testConnection(){
		
		try{
			connection.getMBeanCount();
		}catch(IOException ex){
			return false;
		}
		return true;
	}
	
}

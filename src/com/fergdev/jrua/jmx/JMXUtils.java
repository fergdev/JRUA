package nz.ac.massey.buto.jmx;

import java.io.File;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import com.sun.tools.attach.spi.AttachProvider;

/**
 * Collection of utils for interacting with the JMX API.
 * @author fergus
 *
 */
public class JMXUtils {

	/**
	 * Log4j logger.
	 */
	private static Logger logger = LogManager.getLogger(JMXUtils.class);
	

	/**
	 * Gets a MBean Server connection to the provided vmName, an exception is thrown if the connection cannot be made.
	 * 
	 * @param vmName Name of the VM to connect to.
	 * @return Connection to the VM.
	 * @throws Exception If the connection cannot be made.
	 */
	public static MBeanServerConnection getMBeanServerConnection(String vmName) throws Exception{
		
		logger.info("VM NAME '" + vmName + "'");
		
		// Get the first attatch provider
		AttachProvider attachProvider = AttachProvider.providers().get(0);
		
		// Find the target vm in the list of virtual machine descriptors
        VirtualMachineDescriptor descriptor = null;
        
        // Get list of available virtual machines and iterate till correct VM is found
        for (VirtualMachineDescriptor virtualMachineDescriptor : attachProvider.listVirtualMachines()) {
        	
        	logger.info(virtualMachineDescriptor.id());
        	logger.info(virtualMachineDescriptor.displayName());
        	logger.info("\n");
        	
        	// Test for correct vm
            if (virtualMachineDescriptor.displayName().contains(vmName)) {
            	
            	// Correct VM found
                descriptor = virtualMachineDescriptor;
                break;
            }
        }

        // Test a vm has been found
        if (descriptor == null) 
        	throw new Exception("No matching VM found - " + vmName);

        // Get the virtual machine
        VirtualMachine virtualMachine = attachProvider.attachVirtualMachine(descriptor);
        
        // Attempt to get th
        String connectorAddress = (String) virtualMachine.getAgentProperties().get("com.sun.management.jmxremote.localConnectorAddress");
        
        // Test if address can be found, if not found load the management agent
        if(connectorAddress == null){

        	// attempt to load management jar
        	// get java home
            String home = virtualMachine.getSystemProperties().getProperty("java.home");
            
            // path to management jar
            String agent = home + File.separator + "lib" + File.separator + "management-agent.jar";
            
            // load the jar
            virtualMachine.loadAgent(agent);
        }
        
        // get the connector address again
        connectorAddress = (String) virtualMachine.getAgentProperties().get("com.sun.management.jmxremote.localConnectorAddress");
        
        // detatch from vm
        virtualMachine.detach();
        
        // test for null again
        if(connectorAddress == null){
        	throw new Exception("Could not retrieve connector address.");
        }
        
        // Create service url
        JMXServiceURL serviceURL = new JMXServiceURL(connectorAddress);
        
        // Get connector
        JMXConnector connector = JMXConnectorFactory.connect(serviceURL);
        
        // Get MBean server connector
        MBeanServerConnection connection = connector.getMBeanServerConnection();

        // return the connection
        return connection;
	}
}

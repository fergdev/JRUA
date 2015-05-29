package nz.ac.massey.buto.domain;

/**
 * Represents an MBean that will be monitored by the JMXCollector.
 * 
 * @author Fergus Hewson
 *
 */
public class MonitoredMBean {

	/**
	 * Name of the object to lookup.
	 */
	private String objectName;
	
	/**
	 * List of monitored attributes.
	 */
	private String[] monitoredAttributes;
	
	/**
	 * List of monitored meta attributes.
	 */
	private String[] monitoredMetaAttributes;
	
	/**
	 * Flag for monitoring notifications.
	 */
	private boolean monitorNotifications;

	/**
	 * Creates a new monitored MBean object. 
	 * 
	 * @param objectName The object name to lookup the object with.
	 * @param monitoredAttributes List of attributes to collect from this object.
	 * @param monitoredMetaAttributes List of meta-attributes to collect from this object.
	 * @param monitorNotifications Flag to signify if notifications should be monitored for this object.
	 */
	public MonitoredMBean(String objectName, String[] monitoredAttributes, String[] monitoredMetaAttributes, boolean monitorNotifications){
		this.objectName = objectName;
		this.monitoredAttributes = monitoredAttributes;
		this.monitoredMetaAttributes = monitoredMetaAttributes;
		this.monitorNotifications = monitorNotifications;
	}

	/**
	 * Gets the object name of the Bean.
	 * @return
	 */
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	/**
	 * Gets the list of attributes collected from this bean.
	 * 
	 * @return List of monitored attributes.
	 */
	public String[] getMonitoredAttributes() {
		return monitoredAttributes;
	}
	public void setMonitoredAttributes(String[] monitoredAttributes){
		this.monitoredAttributes = monitoredAttributes;
	}
	
	/**
	 * Gets the list of meta attributes collected from this bean.
	 * @return List of meta attributes.
	 */
	public String[] getMonitoredMetaAttributes(){
		return monitoredMetaAttributes;
	}
	public void setMonitoredMetaAttributes(String[] monitoredMetaAttributes){
		this.monitoredMetaAttributes = monitoredMetaAttributes;
	}
	
	/**
	 * Gets the flag to signify if notifications are collected from this bean.
	 * @return
	 */
	public boolean monitorNotifications(){
		return monitorNotifications;
	}
	public void setMonitorNotifications(boolean b){
		this.monitorNotifications = b;
	}
}

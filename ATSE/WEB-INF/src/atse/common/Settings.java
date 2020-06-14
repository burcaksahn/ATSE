package atse.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.cayenne.configuration.server.ServerRuntime;

import atse.data.Cluster;

/**
 * Setting class holds static and instance application variables that are loaded from the web.xml
 *
 * @author andac
 *
 */
public class Settings {

	/////////////////////////////////////////////////////////////
	// Static Attributes
	//
	public static int CLUSTER_COUNT=1;
    public static String WORD_SPLIT_CHAR="-";
    public static int MAX_WORD_COUNT_OF_CLUSTERS=1;
    public static List<Cluster> clusters = new ArrayList<>();
	
	
    private String propertyFile;
    private Date lastUpdateDate;
    private String clusterFilePath;
	public static Settings instance = null;
	
	
	/////////////////////////////////////////////////////////////
	// Instance Attributes
	//
	
	public static ServerRuntime cayenneRuntime;
	
	public static final String PROPERTY_TYPE_BOOLEAN = "boolean";
	public static final String PROPERTY_TYPE_EMAIL = "email";
	public static final String PROPERTY_TYPE_LOCAL_PATH = "local-path";
	public static final String PROPERTY_TYPE_NUMBER = "number";
	public static final String PROPERTY_TYPE_PATH = "path";
	public static final String PROPERTY_TYPE_STRING = "string";
	public static final String PROPERTY_TYPE_VARIABLE = "variable";
	
	public static final String[] PropertyTypes = {PROPERTY_TYPE_BOOLEAN, PROPERTY_TYPE_EMAIL, PROPERTY_TYPE_LOCAL_PATH, PROPERTY_TYPE_NUMBER, PROPERTY_TYPE_PATH, PROPERTY_TYPE_STRING, PROPERTY_TYPE_VARIABLE };

	
	public static ServerRuntime getCayenneRuntime(){
		return cayenneRuntime;
	}
	

	/////////////////////////////////////////////////////////////
	// Constructor
	//
	public Settings(){
	}

	/////////////////////////////////////////////////////////////
	// Static Methods
	//
	/**
	* @return Returns a new instance of the Settings class
	*/
	public static Settings getInstance(){
		if (instance == null)
		instance = new Settings();
		
		return instance;
	}
	
	/////////////////////////////////////////////////////////////
	// GET / SET Methods
	//
		
	public String getPropertyFile() {
		return propertyFile;
	}

	public void setPropertyFile(String propertyFile) {
		this.propertyFile = propertyFile;
	}


	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}


	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}


	public String getClusterFilePath() {
		return clusterFilePath;
	}


	public void setClusterFilePath(String clusterFilePath) {
		this.clusterFilePath = clusterFilePath;
	}
}
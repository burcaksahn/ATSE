package atse.common.properties;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import atse.common.Settings;
import atse.servlets.InitWebApplicationServlet;

public class PropertyLoader {

	/////////////////////////////////////////////////////////////
	// Static Attributes
	//
	protected static Log LOGGER = LogFactory.getLog(InitWebApplicationServlet.class);

	public static HashMap<String, String> currentValueMap = new HashMap<>();

	public static void setApplicationProperties(boolean firstLoad, String keyName, String newValue) {

		Properties properties = new Properties();
		String strSet = "", strKey = "";

		try {

			FileInputStream in = new FileInputStream(Settings.getInstance().getPropertyFile());
			properties.load(in);
			in.close();
		} catch (Exception e) {
			System.exit(1);
		}
		
		// Set the cluster-file-path
		strKey = "cluster-file-path";
		if (properties.containsKey(strKey)) {
			LOGGER.debug("Setting the " + strKey);
			if (firstLoad) {
				strSet = properties.getProperty(strKey);
				currentValueMap.put(strKey, strSet);
				Settings.getInstance().setClusterFilePath(strSet);
			} else if (keyName.equals(strKey)) {
				strSet = newValue;
				if (currentValueMap.get(strKey) != null)
					currentValueMap.remove(strKey);
				currentValueMap.put(strKey, strSet);
				Settings.getInstance().setClusterFilePath(strSet);
			}
		}
	}
}
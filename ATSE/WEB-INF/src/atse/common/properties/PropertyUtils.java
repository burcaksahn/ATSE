package atse.common.properties;


import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import atse.common.Settings;

public class PropertyUtils {
	
	private static final String XML_PROP_FILE_NAME = "properties.xml";
	private static final String PROPERTIES_EXT = ".properties";
	private static final String COMMON_SUFFIX = "common";
	private static final String SPEC_SUFFIX = "specific";

	/**
	 * Update the XML reference file
	 * @param xmlProps: the new properties object containing the new values
	 */
	/*public static void updateProperties(XmlPropertyList xmlProps){
        try {
    		File file = getXmlPropertiesFile();
    		JAXBContext jaxbContext = JAXBContext.newInstance(XmlPropertyList.class);
    		
    		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
    		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    		jaxbMarshaller.marshal(xmlProps, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	/**
	 * Launch the test on properties for given server and state
	 * 
	 * @param server : (live, staging, clement, misman...)
	 * @param state : (il, in, nj, mo, ga ...)
	 * @return the object containing the results
	 */
	/*public static PropertyTest testGivenProperties(String server, String state) {
		List<XmlProperty> listXmlProps = getXmlProperties();
		Properties properties = getPropertiesProfile(server, state);
		if (listXmlProps != null && properties != null)
			return getPropertiesErrors(listXmlProps, properties, false);
		return null;
	}*/
	
	/**
	 * Launch the test on properties for current server
	 * 
	 * @return the object containing the results
	 */
	/*public static PropertyTest testCurrentProperties() {
		List<XmlProperty> listXmlProps = getXmlProperties();
		Properties properties = getMyProperties();
		if (listXmlProps != null && properties != null)
			return getPropertiesErrors(listXmlProps, properties, true);
		return null;
	}
	*/
	/**
	 * Test the properties
	 * 
	 * @param listXmlProps : List of XML properties to be used as reference
	 * @param properties : List of actual properties
	 * @param isLocal : is the current server using these properties
	 * @return the object containing the results
	 */
	private static PropertyTest getPropertiesErrors(List<XmlProperty> listXmlProps, Properties properties, boolean isLocal) {
		try {
			PropertyTest propertyTest = new PropertyTest();
			Enumeration<?> enumeration = properties.propertyNames();
			List<String> missingPropKeys = new ArrayList<String>();
			List<String> unknowPropKeys = new ArrayList<String>();
			
			Map<String, XmlProperty> wrongTypeValues = new HashMap<String,XmlProperty>();
			Map<String,XmlProperty> unknowPaths = new HashMap<String,XmlProperty>();
			Map<String,XmlProperty> wrongEmail = new HashMap<String,XmlProperty>();
			List<String> unknowGetter = new ArrayList<String>();
			List<String> doublonProps = new ArrayList<String>();

		    while (enumeration.hasMoreElements()) {
		    	unknowPropKeys.add((String) enumeration.nextElement());
		    }
			for (Iterator<XmlProperty> iterator = listXmlProps.iterator(); iterator.hasNext();) {
				XmlProperty prop = iterator.next();
				String propName = prop.getName();
				if (properties.getProperty(propName) == null) {
					if(prop.isMandatory())
						missingPropKeys.add(propName);
				} else {
					String propValue = properties.getProperty(propName);
					//propValue = getValueWithVariables(propValue, state);
					propValue = substituteVariables(propValue, properties);
					prop.setValue(propValue);
					// Test type
					if (propValue.length() == 0) {
						if(prop.isMandatory())
							missingPropKeys.add(propName);
					} else if (prop.getType().equals(Settings.PROPERTY_TYPE_BOOLEAN) && !"true".equals(propValue) && !"false".equals(propValue))
						wrongTypeValues.put(propName, prop);
					else if (prop.getType().equals(Settings.PROPERTY_TYPE_NUMBER) && !StringUtils.isNumeric(propValue))
						wrongTypeValues.put(propName, prop);
					else if (prop.getType().equals(Settings.PROPERTY_TYPE_EMAIL) && !isValidEmailAddress(propValue))
						wrongEmail.put(propName, prop);
					else if (prop.getType().equals(Settings.PROPERTY_TYPE_PATH) && !propValue.contains(File.separator)) {
						wrongTypeValues.put(propName, prop);
					} else if (prop.getType().equals(Settings.PROPERTY_TYPE_LOCAL_PATH) && isLocal) {
						File file = new File(propValue);
						if (!file.exists())
							unknowPaths.put(propName, prop);
					}
					if (!prop.getType().equals(Settings.PROPERTY_TYPE_VARIABLE)){
						// Test getter
						if(!methodExists(prop.getJavaName())) {
							unknowGetter.add(propName);
						}
					}
				}
				unknowPropKeys.remove(propName);
			}
			
			Collections.sort(unknowPropKeys);
			Collections.sort(missingPropKeys);
			Collections.sort(unknowGetter);
			Collections.sort(doublonProps);
			propertyTest.setUnknowProperties(unknowPropKeys);
			propertyTest.setMissingProperties(missingPropKeys);
			propertyTest.setUnknowGetter(unknowGetter);
			propertyTest.setUnknowPaths(unknowPaths);
			propertyTest.setWrongEmail(wrongEmail);
			propertyTest.setWrongTypeValues(wrongTypeValues);
			
			return propertyTest;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Load a specific properties profile for a given server and state
	 * 
	 * @param server : (live, staging, clement, misman...)
	 * @param state : (il, in, nj, mo, ga ...)
	 * @return the object containing the properties
	 */
	public static Properties getPropertiesProfile(String server, String state) {
    	Properties properties = new Properties();
		try {
	    	Properties propTemp1 = new Properties();
	
	    	FileInputStream in = new FileInputStream(Settings.getInstance().getPropertyFile());
	    	properties.load(in);
	    	
	    	in = new FileInputStream(getServerPropertiesPath(server) + File.separator + server+"-"+COMMON_SUFFIX+PROPERTIES_EXT);
	    	propTemp1.load(in);
	    	properties.putAll(propTemp1);
	    	
	    	in = new FileInputStream(getServerPropertiesPath(server) +  File.separator + server+"-"+state+"-"+SPEC_SUFFIX+PROPERTIES_EXT);
	    	propTemp1.load(in);
	    	properties.putAll(propTemp1);
	    	
	        in.close();
	        return properties;
		} catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
	}
	
	/**
	 * Load the properties profile for the current server
	 * 
	 * @return the object containing the properties
	 */
	public static Properties getMyProperties() {
    	Properties properties = new Properties();
    	boolean readProperty=false;
		try {			
	    	FileInputStream in = new FileInputStream(Settings.getInstance().getPropertyFile());
	    	readProperty = true;
			properties.load(in);
	    	
	        in.close();

	        return properties;
		} catch (Exception e) {
			if(!readProperty) {
				if(StringUtils.isEmpty(Settings.getInstance().getPropertyFile())) {
					System.out.println("State properties file not found!");
					return null;
				}
				else {
					System.out.println(Settings.getInstance().getPropertyFile() + " file not found in the system!");
					return null;
				}
			}
			e.printStackTrace();
        	return null;
		}
	}
	
	/**
	 * TODO: to be removed
	 * Load the old properties, in case the news are not set up
	 * 
	 * @return the object containing the properties
	 */
	public static Properties getMyOldProperties() {
    	Properties properties = new Properties();
		try {
	    	FileInputStream in = new FileInputStream(Settings.getInstance().getPropertyFile());
	    	properties.load(in);
	        in.close();
	        return properties;
		} catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
	}
	
	/**
	 * Load the XML properties reference
	 * 
	 * @return the list of object containing the XML properties
	 */
	/*public static List<XmlProperty> getXmlProperties() {
		try {
			File file = getXmlPropertiesFile();
			JAXBContext jaxbContext = JAXBContext.newInstance(XmlPropertyList.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			XmlPropertyList xmlProps = (XmlPropertyList) jaxbUnmarshaller.unmarshal(file);
			return xmlProps.getProperties();
		} catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
	}*/
	
	/**
	 * Get the local properties root folder path
	 * 
	 * @return the path
	 */
	private static String getRootPropertiesPath() {
		String basePropPath = Settings.getInstance().getPropertyFile();
		if (basePropPath == null)
			basePropPath = Settings.getInstance().getPropertyFile();
		String returnPath = basePropPath.substring(0, basePropPath.lastIndexOf(File.separator));
		return returnPath.substring(0, returnPath.lastIndexOf(File.separator));
	}
	
	/**
	 * Get the local properties server folder path
	 * 
	 * @return the path
	 */
	private static String getServerPropertiesPath(String serverName) {
		String basePropPath = Settings.getInstance().getPropertyFile();
		if (basePropPath == null)
			basePropPath = Settings.getInstance().getPropertyFile();
		return basePropPath.substring(0, basePropPath.lastIndexOf(File.separator)) + File.separator + serverName;
	}
	
	/**
	 * Get the local properties XML file
	 * 
	 * @return the file object
	 */
	private static File getXmlPropertiesFile() {
		return new File(getRootPropertiesPath() + File.separator + XML_PROP_FILE_NAME);
	}
	
	/**
	 * Check if given string has correct email address format
	 * 
	 * @param email: string to be tested
	 * @return boolean
	 */
	private static boolean isValidEmailAddress(String email) {
	   boolean result = true;
	   
	   if (email != null && email.indexOf(";") > 0)
		   return true;
	   
	   try {
	      InternetAddress emailAddr = new InternetAddress(email);
	      emailAddr.validate();
	   } catch (AddressException ex) {
	      result = false;
	   }
	   return result;
	}
	
	/**
	 * Check if the given variable name (from XML file) has getter method in Settings class
	 * 
	 * @param javaName: variable string
	 * @return boolean
	 */
	private static boolean methodExists(String javaName) {
		Method methodToFind = getJavaGetter(javaName);
		if (methodToFind == null)
			return false;
		return true;
	}
	
	/**
	 * Call the Getter method (in Settings.class) from a variable name and return its value if exists
	 * 
	 * @param javaName: variable string
	 * @return getter value
	 */
	public static String getValueFromGetter(String javaName) {
		try {
			Method methodToFind = getJavaGetter(javaName);
			if (methodToFind == null)
				return null;
			Settings s = Settings.getInstance();
			Object returnVal = methodToFind.invoke(s);
			if (returnVal == null)
				return null;
			else if(returnVal instanceof Object[]) {
				String strArray = "";
				Object[] objValues = (Object[])returnVal;
				for (Object o : objValues) {
					strArray += o.toString() + " ";
				}
				return strArray.trim();
			} else {
				return returnVal.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	

	/**
	 * Convert a property value containing a variable (eg:{state-code}) with its actual value
	 * This method is recursive
	 * 
	 * @param propValue: property value to be replaced
	 * @param props: Properties object
	 * @return the actual value 
	 */
	public static String substituteVariables(String propValue, Properties props) {
		Matcher m = Pattern.compile("(\\{[^}]+\\})").matcher(propValue);
     	while(m.find()) {
     		String varName = m.group().substring(1, m.group().length()-1);
     		String varValue = props.getProperty(varName);
     		if (varValue == null) {
     			System.out.println("No value for varName "+varName+" in propValue " + propValue);
     			return propValue;
     		}
     			
     		if (varValue.contains("{") && varValue.contains("}"))
     			varValue = substituteVariables(varValue, props);
			propValue = propValue.replace(m.group(), varValue);
     	}
     	return propValue;
	}
	
	/**
	 * Get the getter method name from a variable name 
	 * 
	 * @param methodName
	 * @return the method as a String
	 */
	public static Method getJavaGetter(String methodName) {
		Method getter;
		try {
			getter = Settings.class.getMethod("get" + WordUtils.capitalize(methodName), (Class<?>[]) null);
		} catch (NoSuchMethodException e) {
			getter = null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if (getter == null) {
			try {
				getter = Settings.class.getMethod("is" + WordUtils.capitalize(methodName), (Class<?>[]) null);
			} catch (NoSuchMethodException e) {
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return getter;
	}
}
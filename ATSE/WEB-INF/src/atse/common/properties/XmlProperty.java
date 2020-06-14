package atse.common.properties;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.sun.xml.txw2.annotation.XmlElement;

@XmlRootElement(name = "property")
@XmlAccessorType (XmlAccessType.FIELD)
public class XmlProperty {


	private String name;
	private boolean isMandatory;
	private String defaultValue;
	private String type;
	private String javaName;
	private String value;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isMandatory() {
		return isMandatory;
	}
	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getJavaName() {
		return javaName;
	}
	public void setJavaName(String javaName) {
		this.javaName = javaName;
	}
//	public String getValue() {
//		return value;
//	}
//	public void setValue(String value) {
//		this.value = value;
//	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
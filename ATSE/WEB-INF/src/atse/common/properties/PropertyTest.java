package atse.common.properties;

import java.util.List;
import java.util.Map;


public class PropertyTest {


	private List<String> missingProperties;
	private List<String> unknowProperties;
	private Map<String,XmlProperty> wrongTypeValues;
	private Map<String,XmlProperty> unknowPaths;
	private Map<String,XmlProperty> wrongEmail;
	private List<String> unknowGetter;
	
	public List<String> getMissingProperties() {
		return missingProperties;
	}
	public void setMissingProperties(List<String> missingProperties) {
		this.missingProperties = missingProperties;
	}
	public List<String> getUnknowProperties() {
		return unknowProperties;
	}
	public void setUnknowProperties(List<String> unknowProperties) {
		this.unknowProperties = unknowProperties;
	}
	public List<String> getUnknowGetter() {
		return unknowGetter;
	}
	public void setUnknowGetter(List<String> unknowGetter) {
		this.unknowGetter = unknowGetter;
	}
	public Map<String, XmlProperty> getWrongTypeValues() {
		return wrongTypeValues;
	}
	public void setWrongTypeValues(Map<String, XmlProperty> wrongTypeValues) {
		this.wrongTypeValues = wrongTypeValues;
	}
	public Map<String, XmlProperty> getUnknowPaths() {
		return unknowPaths;
	}
	public void setUnknowPaths(Map<String, XmlProperty> unknowPaths) {
		this.unknowPaths = unknowPaths;
	}
	public Map<String, XmlProperty> getWrongEmail() {
		return wrongEmail;
	}
	public void setWrongEmail(Map<String, XmlProperty> wrongEmail) {
		this.wrongEmail = wrongEmail;
	}
}
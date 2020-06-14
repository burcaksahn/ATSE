package atse.common.properties;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "properties")
@XmlAccessorType (XmlAccessType.FIELD)
public class XmlPropertyList {

    @XmlElement(name = "property")
    private List<XmlProperty> properties = null;

	public List<XmlProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<XmlProperty> properties) {
		this.properties = properties;
	}
 }
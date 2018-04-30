package com.belk.car.product.integration.response.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * POJO to hold the group PIM system information. 
 * Current Path in xml response: /getGroupResponse/Group_Catalog/pim_entry.
 */
@XmlRootElement(name = "pim_entry")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class GroupPIMEntryInformation {
	
	private GroupHeaderData groupHeaderInformation;
	
	private GroupEntryDetailsData entries;
	
	private Map<String, List<AttributeData>> attributesInformation;
	private static final Log log = LogFactory.getLog(GroupPIMEntryInformation.class);
	
	@SuppressWarnings("unused")
	private Map<Long, List<AttributeData>> allAttributes;
	
	@XmlElement(name="group_entry_header")
	public GroupHeaderData getGroupHeaderInformation() {
		return groupHeaderInformation;
	}

	public void setGroupHeaderInformation(GroupHeaderData groupHeaderInformation) {
		this.groupHeaderInformation = groupHeaderInformation;
	}

	@XmlElement(name="entry")
	public GroupEntryDetailsData getEntries() {
		return entries;
	}

	public void setEntries(GroupEntryDetailsData entries) {
		this.entries = entries;
	}

	@XmlTransient
	public Map<String, List<AttributeData>> getAttributesInformation() {
		return attributesInformation;
	}

	public void setAttributesInformation(
			Map<String, List<AttributeData>> attributesInformation) {
		this.attributesInformation = attributesInformation;
	}
	
	@XmlTransient
	public Map<Long, List<AttributeData>> getAllAttributes() {
		Map<Long, List<AttributeData>> consolidatedAttributeInfo = new HashMap<Long, List<AttributeData>>();
		List<AttributeData> individualSectionAttrInfo = new ArrayList<AttributeData>();

		if (this.getGroupHeaderInformation() != null && this.getAttributesInformation() != null) {
			Long groupId = getGroupHeaderInformation().getPrimaryKey();
			for (Map.Entry<String, List<AttributeData>> entry : getAttributesInformation().entrySet()) {
			    individualSectionAttrInfo = removeDuplicateAttributes(entry.getValue(),individualSectionAttrInfo);
			}
			consolidatedAttributeInfo.put(groupId, individualSectionAttrInfo);
		}
		return consolidatedAttributeInfo;
	}
	
   /**
     * This method removes the duplicate attributes coming as part of different spec paths.
     * As per business the priority of the attributes is Iph attributes,zbm attributes 
     * and Ecomm spec attributes.
     * 
     * @param attributes
     * @param attributeInfo
     * @return
     */
    private List<AttributeData> removeDuplicateAttributes(
            List<AttributeData> attributes,List<AttributeData> uniqueList) {

        if(log.isDebugEnabled()){
            log.debug("current attributes size "+attributes.size());
        }

        for(AttributeData attributeInfo:attributes){
            
            if(uniqueList.contains(attributeInfo)){
                int i = uniqueList.indexOf(attributeInfo);
                AttributeData attrData = uniqueList.get(i);
                String attrSpecName = attrData.getQName().toString();
                String newAttrSpecName = attributeInfo.getQName().toString();
                if(log.isDebugEnabled()){
                    log.debug("Attribute exists with current spec "+attrSpecName+" and new spec name "+newAttrSpecName);
                }

                if(attrSpecName!=null && attrSpecName.startsWith("sec_spec")){
                    continue;
                }else if(attrSpecName!=null && !attrSpecName.startsWith("sec_spec") && newAttrSpecName.startsWith("sec_spec")){
                    uniqueList.remove(i);
                    uniqueList.add(attributeInfo);
                }else if(attrSpecName!=null && !attrSpecName.startsWith("sec_spec") && !attrSpecName.equals(newAttrSpecName) && newAttrSpecName.startsWith("zbm_sec_spec")){
                    uniqueList.remove(i);
                    uniqueList.add(attributeInfo);
                }else if(attrSpecName!=null && !(attrSpecName.startsWith("sec_spec")|| attrSpecName.startsWith("zbm_sec_spec")) && !attrSpecName.equals(newAttrSpecName) && newAttrSpecName.startsWith("Ecomm")){
                    uniqueList.remove(i);
                    uniqueList.add(attributeInfo);
                }
            }else{
                if(log.isDebugEnabled()){
                    log.debug("Attribute does not exists in the list, hence adding ");
                }
                uniqueList.add(attributeInfo);
            }
        }
        if(log.isDebugEnabled()){
            log.debug("unique attributes size "+uniqueList.size());
        }
        
        return uniqueList;
    }

}

package com.belk.car.product.integration.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.belk.car.product.integration.adapter.GroupObjectFactory;
import com.belk.car.product.integration.exception.BelkProductIntegrationException;
import com.belk.car.product.integration.request.data.GetGroupRequestType;
import com.belk.car.product.integration.request.data.GetRequestType;
import com.belk.car.product.integration.request.data.GroupIntegrationRequestData;
import com.belk.car.product.integration.response.data.AttributeData;
import com.belk.car.product.integration.response.data.ErrorResponseData;
import com.belk.car.product.integration.response.data.GroupCatalogData;
import com.belk.car.product.integration.response.data.GroupEntryDetailsData;
import com.belk.car.product.integration.response.data.GroupIntegrationResponseData;
import com.belk.car.product.integration.response.data.GroupNotFoundResponseData;
import com.belk.car.product.integration.response.data.GroupPIMEntryInformation;

/**
 * 
 * This class will be used to integrate with the new service URL for groupings for pim integration. This class
 * also unmarshalls the response. This class also consolidates the attributes data from the response 
 * to be used in the processors for handling the information from the integration.
 *
 */
public class BelkGroupService {
	
	private static JAXBContext createResponseJAXBContext;
	
	private final GroupIntegrationRequestData integrationRequestData;
	
	private Document xmlResponseStructure = null;
        
	private static final String GROUP_SECTION_PATH_IN_XML = "/getGroupResponse/Group_Catalog[@id=" + "\"%s" + "\"]" + "/pim_entry/entry/%s";
        
	private static final String GROUP_CATALOG_XML_ELEMENT_DESC = "<Group_Catalog ";
	
	private static final Log log = LogFactory.getLog(BelkGroupService.class);
	
	private static List<String> exclRequestTypes = new ArrayList<String>(){{
        add(GetGroupRequestType.GROUPID.toString());
        add(GetGroupRequestType.VENDORSTYLE.toString());
    }};
	      
    static {
		createJAXBContext();
	}
	
	public BelkGroupService(GroupIntegrationRequestData integrationRequestData) {
		this.integrationRequestData = integrationRequestData;
	}
	
	/**
	 * Creates a JAXBContext for unmarshalling the response xml structures.
	 * @return - JAXBContext for the GroupIntegrationResponseData class.
	 */
	private static JAXBContext createJAXBContext() {
		if (createResponseJAXBContext == null) {
			try {
				createResponseJAXBContext = JAXBContext.newInstance(GroupIntegrationResponseData.class, ErrorResponseData.class,
						GroupNotFoundResponseData.class, GroupObjectFactory.class);
			} catch (JAXBException e) {
				if (log.isErrorEnabled()) {
                    log.error("Couldn't create response JAXB Context", e);
                }
			}
		}
		return createResponseJAXBContext;
	}
	
	/**
	 * Integration method for groupings with the PIM system.
	 * This method will call the integration response class to get the response which will be parsed 
	 * for error as well as the data information. If the response is successful, the xml response will be 
	 * parsed to get the dom structure to get the corresponding attributes under the sections. 
	 * @return - GroupIntegrationResponseData Object containing success or error information.
	 * @throws BelkProductIntegrationException
	 */
	public GroupIntegrationResponseData getResponse() throws BelkProductIntegrationException {
		GroupIntegrationResponseData responseData = new GroupIntegrationResponseData();
		
		if (log.isInfoEnabled()) {
			log.info("Getting response from PIM webservice and parse XML for attributes");
		}
		try {
			Unmarshaller convertResponse = createResponseJAXBContext.createUnmarshaller();
			BelkGetGroupIntegrationResponse serviceResponse = new BelkGetGroupIntegrationResponse(integrationRequestData);
			String responseContents = serviceResponse.getIntegrationResponse();
			if (responseContents == null || responseContents.equals("")) {
				throw new BelkProductIntegrationException("Empty response received for input request ");
			}
			responseContents = addIndexToCatalog(responseContents);
			InputStream input = new ByteArrayInputStream(responseContents.getBytes());
			if (isErrorResponse(responseContents)) {
				ErrorResponseData errorCode = (ErrorResponseData) convertResponse.unmarshal(input);
				responseData.setErrorResponseData(errorCode);
			} else {
				createDocumentResponse(responseContents);
				if (log.isInfoEnabled()) {
					log.info("Unmarshalling the XML response from PIM");
				}
				responseData = (GroupIntegrationResponseData) convertResponse.unmarshal(input);
				if (responseData != null) {
					List<GroupCatalogData> catalogDataList = responseData.getResponseList();
					for (GroupCatalogData catalog : catalogDataList) {
						GroupPIMEntryInformation pimEntry = catalog.getPimEntry();
						Integer requestId = catalog.getRequestId();
						List<JAXBElement<String>> attributeElements = pimEntry.getEntries().getOtherElements();
						if (attributeElements != null && !attributeElements.isEmpty()) {
							Map<String, List<AttributeData>> responseInfo = 
									parseResponseForAttributeInformation(pimEntry.getEntries(), 
											integrationRequestData.getRequestType().toString(), requestId);
							pimEntry.setAttributesInformation(responseInfo);
						}
					}
				}
			}
		} catch (JAXBException e) {
			throw new BelkProductIntegrationException("Couldn't create jaxb context for integration response ", e);
		} catch (SocketTimeoutException e) {
			throw new BelkProductIntegrationException("Connection timed out while contacting webservice ", e);
		} catch (TransformerException e) {
			throw new BelkProductIntegrationException("Failed to transform input xml with the configured xsl ", e);
		} catch (IOException e) {
			throw new BelkProductIntegrationException("Failed to get response for the input request xml ", e);
		} catch (ParserConfigurationException e) {
			throw new BelkProductIntegrationException("Failed to create dom structure for the response ", e);
		} catch (SAXException e) {
			throw new BelkProductIntegrationException("Failed to create dom structure for the response ", e);
		}
		
		if (log.isInfoEnabled()) {
			log.info("Parsed XML response for attribute information from PIM");
		}
		
		return responseData;
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Method to get all the unmapped elements from the xml response. All unmapped elements in the xml could be related 
	 * to the attribute sections under <entry> section in the response. The "@XmlAnyElement" annotation is used to 
	 * consolidate all the unmapped elements. Those elements that have been categorized as JAXBElement 
	 * has the specification defined in the GroupObjectFactory class. Those that hasn't been defined in those will be 
	 * processed for the name to be searched in the DOM structure.
	 * @param attributesInfo - Collection of unmapped elements.
     * @param requestId - Response Index indicator based on the number of values in the input.
	 * @return - Collection of attribute section name and the list of attribute information. 
	 * @throws BelkProductIntegrationException - Exception while processing the response information.
	 */
	public Map<String, List<AttributeData>> parseResponseForAttributeInformation(
			GroupEntryDetailsData entryDetailsInformation, String requestType, Integer requestId) throws BelkProductIntegrationException, DOMException, ParserConfigurationException, SAXException, IOException {
		Map<String, List<AttributeData>> attributeInfoForTagName = new HashMap<String, List<AttributeData>>();
		try {
			List<JAXBElement<AttributeData>> mappedElements = new ArrayList<JAXBElement<AttributeData>>();
	        List<String> tagElementsUnIdentifiableInXml = new ArrayList<String>();
	        List<JAXBElement<String>> attributeElements = entryDetailsInformation.getOtherElements();
	        int attributeElementsSize = (attributeElements != null) ? attributeElements.size() : 0;
	        for (int i = 0; i < attributeElementsSize; i++) {
	        	Object currentElement = attributeElements.get(i);
	        	if (currentElement instanceof JAXBElement) {
	                mappedElements.add((JAXBElement<AttributeData>) currentElement);
	            } else {
	            	String name = currentElement.toString();
	                if (name != null) {
	                    name = name.substring(1, name.indexOf(":"));
	                    if (!name.equals("Copy_Sec_Spec")
                                && !isElementExcluded(name,requestType)) {
                            tagElementsUnIdentifiableInXml.add(name);
                        }

                    }
	            }
	        }
	        
	        for (JAXBElement<AttributeData> attributeElement :  mappedElements) {
	                String elementName = attributeElement.getName().toString();
				List<AttributeData> attributeInfo = getAttributes(elementName, requestType, requestId);
	                attributeInfoForTagName.put(elementName, attributeInfo);
	        }
	        for (String attrName : tagElementsUnIdentifiableInXml) {
				List<AttributeData> attributeInfo = getAttributes(attrName, requestType, requestId);
					attributeInfoForTagName.put(attrName, attributeInfo);
	        }
		} catch (XPathExpressionException e) {
			throw new BelkProductIntegrationException("Couldn't configure XPath for the response " , e);
		}
		return attributeInfoForTagName;
	}
	
	/**
     * Method to create the document structure for the response xml.
     * @param xmlResponse - integration response xml.
     * @throws ParserConfigurationException - Exception while processing the dom structure
     * @throws SAXException - Exception while processing the dom structure
     * @throws IOException - Exception while processing the dom structure
     */    
	private void createDocumentResponse(String xmlResponse) throws ParserConfigurationException, SAXException, IOException {
		if (xmlResponseStructure == null) {
            InputStream stream = new ByteArrayInputStream(xmlResponse.getBytes());
			DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            xmlResponseStructure = documentBuilder.parse(stream);
		}
	}
	
	/**
     * Method to get the attributes information under each attribute name section identified as part of the 
     * xmlanyelement annotation.
     * @param attributeSectionName - Entry section attribute name.
     * @param requestId - XML response index based on the value in the Group_Catalog section of the response.
     * @return - Collection of attribute values for the attribute section name.
     * @throws XPathExpressionException - XPath exception to get the specific node for the attribute name.
     */    
	private List<AttributeData> getAttributes(String attributeSectionName, String requestType, Integer requestId) 
			throws XPathExpressionException, DOMException, ParserConfigurationException, SAXException, IOException {
		List<AttributeData> attributes = new ArrayList<AttributeData>();
		
		if (xmlResponseStructure == null) {
			throw new IllegalArgumentException("Problem while processing the input xml contents ");
		}
		
		NodeList nList = getElementByNameForRequest(attributeSectionName, requestType, requestId);
        for (int temp = 0; temp < nList.getLength(); temp++) {
        	Node nNode = nList.item(temp);
        	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        		if (nNode.hasChildNodes()) {
        			NodeList childNodes = nNode.getChildNodes();
        			for (int i = 0; i < childNodes.getLength(); i++) {
        				Node childNode = childNodes.item(i);
        				String pimAttrName = childNode.getNodeName();
        				String value = childNode.getTextContent();
                        if (pimAttrName.matches("^_\\d*_.*")) {
                            pimAttrName = pimAttrName.substring(pimAttrName.indexOf("_", 1)+1, pimAttrName.length());
                        }
                        if(StringUtils.isBlank(value)){
                            if(log.isInfoEnabled()){
                                log.info("Skipping attribute name "+ pimAttrName +" since value is null in group parser ");
                            }
                            continue;
                        }
        				if (!childNodes.item(i).getNodeName().equals("#text")) {
        				    AttributeData attributeInfo = new AttributeData();                            
        					attributeInfo.setAttributeName(pimAttrName);
        					attributeInfo.setAttributeValue(value);
        					attributeInfo.setQName(new QName(childNode.getParentNode().getNodeName()));
        					
        					attributes.add(attributeInfo);
        				}
        			}
        		}
        	}
        }
		return attributes;
	}
	
    /**
	 * This method will use the XPath to navigate to the corresponding node and return the corresponding node
	 * list for the response index.
	 * @param attributeSectionName - Attribute section tag name in the response.
	 * @param responseId - responseIndex to navigate to the particular node.
	 * @return - Node Information.
	 * @throws XPathExpressionException - Exception while navigating to the path.
	 */
	private NodeList getElementByNameForRequest(String attributeSectionName, String requestType, Integer requestId) throws XPathExpressionException {
		NodeList attributeNodeList = null;
		String formattedInput = String.format(GROUP_SECTION_PATH_IN_XML, requestId, attributeSectionName);
		XPath xmlPath = XPathFactory.newInstance().newXPath();
		XPathExpression pathInXml = xmlPath.compile(formattedInput);
		attributeNodeList = (NodeList) pathInXml.evaluate(xmlResponseStructure, XPathConstants.NODESET);
		return attributeNodeList;
	}
	
	/**
     * Method to indicate if response is successful or not.
     * @param xmlResponse - String xml response.
     * @return - true if the response contains "<Error>" or <"error">
     */    
	private boolean isErrorResponse(String xmlResponse) {
		boolean isErrorResponse = false;
		if (xmlResponse != null) {
			isErrorResponse = (xmlResponse.contains("<Error>") | xmlResponse.contains("<error>"));
		}
		return isErrorResponse;
	}
	
	/**
     * This method is used to specifically mark the response with a specific indicator to be able
     * to use that information while parsing for attributes. Under the dom structure if response 
     * contains same section names for multiple sections, the values will be consolidated for all
     * the responses. Response from the pim system has no unique identifiers for section, hence the need to 
     * add an index to the value. For example, <Item_Catalog type="Style"> is the response and it has 2 responses
     * , this method will modify that to be <Group_Catalog type="Grouping" id="1">,<Group_Catalog type="Grouping" id="2">
     * and so on.
     * @param xmlResponse - Response XML
     * @param requestType - To identify the type attribute value in the dom struture.
     * @return - modified string value as mentioned above.
     */
	private String addIndexToCatalog(String xmlResponse) {
		Pattern pattern = Pattern.compile(GROUP_CATALOG_XML_ELEMENT_DESC);
		Integer id = 0;
        Matcher matcher = pattern.matcher(xmlResponse);
        StringBuffer changedAttributeNames = new StringBuffer();
        while (matcher.find()) {
        	matcher.appendReplacement(changedAttributeNames, 
        			"<Group_Catalog " + "id=" + "\"" + (++id) + "\" ");
    	}
        matcher.appendTail(changedAttributeNames);
        return changedAttributeNames.toString();
	}
	
    /**
     * Method to identify if the xml element is excluded or not.
     * Elements starting with Consolidated_Product_Spec and Collection_Spec must 
     * be excluded for Group request type.
     *  
     * @param name
     * @param requestType
     * @return
     */
    private boolean isElementExcluded(String name,String requestType){
        boolean isExcluded = false;
        if(exclRequestTypes.contains(requestType) && (name.equalsIgnoreCase("Consolidated_Product_Spec")||name.equalsIgnoreCase("Collection_Spec"))){
            isExcluded = true;
        }        
        return isExcluded;
    }
}
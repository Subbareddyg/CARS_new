package com.belk.car.product.integration.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.belk.car.app.util.BelkDateUtil;
import com.belk.car.app.util.PropertyLoader;
import com.belk.car.product.integration.adapter.ObjectFactory;
import com.belk.car.product.integration.exception.BelkProductIntegrationException;
import com.belk.car.product.integration.request.data.GetRequestType;
import com.belk.car.product.integration.request.data.IntegrationRequestData;
import com.belk.car.product.integration.response.data.AttributeData;
import com.belk.car.product.integration.response.data.AttributeLookupData;
import com.belk.car.product.integration.response.data.EntryDetailsData;
import com.belk.car.product.integration.response.data.ErrorResponseData;
import com.belk.car.product.integration.response.data.IntegrationResponseData;
import com.belk.car.product.integration.response.data.ItemCatalogData;
import com.belk.car.product.integration.response.data.ItemNotFoundResponseData;
import com.belk.car.product.integration.response.data.LookupAttributeData;
import com.belk.car.product.integration.response.data.PIMEntryInformation;
import com.belk.car.product.integration.response.data.PackIntegrationResponseData;



/**
 * 
 * This class will be used to integrate with the new service URL for pim integration. This class
 * also unmarshalls the response. This class also consolidates the attributes data from the response 
 * to be used in the processors for handling the information from the integration. 
 *
 */
public class BelkProductService {
	
	private static JAXBContext createResponseJAXBContext;
	
	private final IntegrationRequestData integrationRequestData;
	
	private Document xmlResponseStructure = null;
        
	private static final String ITEM_SECTION_PATH_IN_XML = "/getItemResponse/Item_Catalog[@id=" + "\"%s" + "\"]" + "/pim_entry/entry/%s";
        
	private static final String PACK_SECTION_PATH_IN_XML = "/getPackResponse/Item_Catalog[@id=" + "\"%s" + "\"]" + "/pim_entry/entry/%s";
	private static final String ITEM_CATALOG_XML_ELEMENT_DESC = "<Item_Catalog type"+ "="  + "\"%s" + "\"";
	
	private static final String ATTRIBUTE_LOOKUP_VALUE_INDICATOR = "container";
	private static final Log log = LogFactory.getLog(BelkProductService.class);
	private static List<String> exclRequestTypes = new ArrayList<String>(){{
         add(GetRequestType.SKU.toString());
         add(GetRequestType.STYLE_COLOR.toString());
         add(GetRequestType.PACK_COLOR.toString());
     }};
      
     private static List<String> sizeEelements = new ArrayList<String>(){{
         add("omni_size_desc");
         add("facet_size_1");
         add("facet_size_2");
         add("facet_size_3");
         add("facet_sub_size_1");
         add("facet_sub_size_1");
     }};
    @SuppressWarnings("rawtypes")
    private static List udaInclList = new ArrayList();
    
    @SuppressWarnings("unused")
    private static List ecommSkuSpecInclList = new ArrayList();
    
	static {
		createJAXBContext();
		getUDAInclusionList();
		getEcommSkuSpecInclList();
	}
	
	public BelkProductService(IntegrationRequestData integrationRequestData) {
		this.integrationRequestData = integrationRequestData;
	}
	
	

    /**
	 * Creates a JAXBContext for unmarshalling the response xml structures.
	 * @return - JAXBContext for the IntegrationResponseData class.
	 */
	private static JAXBContext createJAXBContext() {
		if (createResponseJAXBContext == null) {
			try {
				createResponseJAXBContext = JAXBContext.newInstance(IntegrationResponseData.class, PackIntegrationResponseData.class, 
						ErrorResponseData.class, ItemNotFoundResponseData.class, ObjectFactory.class);
			} catch (JAXBException e) {
				if (log.isErrorEnabled()) {
                    log.error("Couldn't create response JAXB Context", e);
                }
			}
		}
		return createResponseJAXBContext;
	}
	

	/**
	 * Integration method with the new PIM system. 
	 * This method will call the integration response class to get the response which will be parsed 
	 * for error as well as the data information. If the response is successful, the xml response will be 
	 * parsed to get the dom structure to get the corresponding attributes under the sections. 
	 * @return - IntegrationResponseData Object containing success or error information.
	 * @throws BelkProductIntegrationException
	 */
	public IntegrationResponseData getResponse() throws BelkProductIntegrationException {
		IntegrationResponseData responseData = new IntegrationResponseData();
		try {
            if(log.isInfoEnabled()){
                log.info("Entered getResponse Method" );
            }
			Unmarshaller convertResponse = createResponseJAXBContext.createUnmarshaller();
			BelkGetIntegrationResponse serviceResponse = new BelkGetIntegrationResponse(integrationRequestData);
			String responseContents = serviceResponse.getIntegrationResponse();
            if(log.isInfoEnabled()){
                log.info("responseContents:"+responseContents);
            }
			if (responseContents == null || responseContents.equals("")) {
				throw new BelkProductIntegrationException("Empty response received for input request ");
			}
			responseContents = addIndexToCatalog(responseContents, integrationRequestData.getRequestType());
			InputStream input = new ByteArrayInputStream(responseContents.getBytes());
            if(log.isInfoEnabled()){
                log.info("Is XMLresponseContents contains <Error>:"+isErrorResponse(responseContents));
            }
			if (isErrorResponse(responseContents)) {
				ErrorResponseData errorCode = (ErrorResponseData) convertResponse.unmarshal(input);
				responseData.setErrorResponseData(errorCode);
			} else {
				createDocumentResponse(responseContents);
				responseData = (IntegrationResponseData) convertResponse.unmarshal(input);
				if (responseData != null) {
                   if(log.isInfoEnabled()){
                       log.info("responseData is not null");
                     }
					List<ItemCatalogData> catalogDataList = responseData.getResponseList();
					for (ItemCatalogData catalog : catalogDataList) {
						PIMEntryInformation pimEntry = catalog.getPimEntry();
						Integer requestId = catalog.getRequestId();
						List<JAXBElement<String>> attributeElements = pimEntry.getEntries().getOtherElements();
						if (attributeElements != null && !attributeElements.isEmpty()) {
                            if(log.isInfoEnabled()){
                                log.info("PIM Attribute Elements is not null and not Empty");
                               }
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
		return responseData;
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Method to get all the unmapped elements from the xml response. All unmapped elements in the xml could be related 
	 * to the attribute sections under <entry> section in the response. The "@XmlAnyElement" annotation is used to 
	 * consolidate all the unmapped elements. Those elements that have been categorized as JAXBElement 
	 * has the specification defined in the ObjectFactory class. Those that hasn't been defined in those will be 
	 * processed for the name to be searched in the DOM structure. 
	 * @param attributesInfo - Collection of unmapped elements.
         * @param requestId - Response Index indicator based on the number of values in the input.
	 * @return - Collection of attribute section name and the list of attribute information. 
	 * @throws BelkProductIntegrationException - Exception while processing the response information.
	 */
	public Map<String, List<AttributeData>> parseResponseForAttributeInformation(
			EntryDetailsData entryDetailsInformation, String requestType, Integer requestId) throws BelkProductIntegrationException, DOMException, ParserConfigurationException, SAXException, IOException {
         if(log.isInfoEnabled()){
             log.info("Entered parseResponseForAttributeInformation Method" );
         }
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
                        // TODO Add exclusion list
                        if (!name.equals("Item_Pet_Ctg_Spec") && !name.equals("Copy_Sec_Spec")
                                && !isElementExcluded(name,requestType)) {
                            tagElementsUnIdentifiableInXml.add(name);
                        }
	                }
	            }
	        }
	        LookupAttributeData attributeLookupInformation = entryDetailsInformation.getAttributeLookupData();
	        for (JAXBElement<AttributeData> attributeElement :  mappedElements) {
	                String elementName = attributeElement.getName().toString();
	                if(log.isInfoEnabled()){
	                    log.info("Getting the attributes information for the element:"+elementName );
	                }
				List<AttributeData> attributeInfo = getAttributes(elementName, requestType, requestId, attributeLookupInformation);
	                attributeInfoForTagName.put(elementName, attributeInfo);
	        }
	        for (String attrName : tagElementsUnIdentifiableInXml) {
	             if(log.isInfoEnabled()){
                     log.info("Getting the attributes information for the attribute:"+attrName );
                 }
				List<AttributeData> attributeInfo = getAttributes(attrName, requestType, requestId, attributeLookupInformation);
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
     * @param requestId - XML response index based on the value in the Item_Catalog section of the response.
     * @return - Collection of attribute values for the attribute section name.
     * @throws XPathExpressionException - XPath exception to get the specific node for the attribute name.
     */    
	private List<AttributeData> getAttributes(String attributeSectionName, String requestType, Integer requestId, 
			LookupAttributeData attributeLookupData) throws XPathExpressionException, DOMException, ParserConfigurationException, SAXException, IOException {
		List<AttributeData> attributes = new ArrayList<AttributeData>();
		log.info("Inside getAttributes method ");
		String elementName = "Description";
		boolean excludeUDAForStyleColor = false;
        if(log.isInfoEnabled()){
            log.info("requestType:"+requestType );
        }
		if(requestType!=null && (requestType.equalsIgnoreCase(GetRequestType.STYLE_COLOR.toString())||requestType.equalsIgnoreCase(GetRequestType.PACK_COLOR.toString()))){
		    elementName = "SUPER_COLOR_NAME";
		    excludeUDAForStyleColor = true;
		}
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
                                log.info("Skipping attribute name "+ pimAttrName +" since value is null in Item parser ");
                            }
                            continue;
                        }
                        //ignore UDA section for style color and pack color calls
                        if(excludeUDAForStyleColor && attributeSectionName.equalsIgnoreCase("Item_UDA_Spec")){
                            continue;
                        }
                        //This check is added to include only attributes from Item_UDA_Spec provided by business into the master attribute List 
                        if(attributeSectionName.equalsIgnoreCase("Item_UDA_Spec") && !udaInclList.isEmpty() && !udaInclList.contains(pimAttrName.toUpperCase())){
                            continue;
                        }
                        
                        //This check is added to include only required attributes from Ecomm_SKU_Spec into the master attribute List to improve performance
                        if(attributeSectionName.equalsIgnoreCase("Ecomm_SKU_Spec") && !ecommSkuSpecInclList.isEmpty() && !ecommSkuSpecInclList.contains(pimAttrName.toUpperCase())){
                            continue;
                        }
                        if(log.isInfoEnabled()){
                            log.info("childNodes-NodeName::"+childNodes.item(i).getNodeName()+" and childNode-NodeName::"+childNode.getNodeName());
                        }
        				if(!childNodes.item(i).getNodeName().equals("#text")&&childNode.getNodeName().equals("Omnichannel_Size")){
        				    List<AttributeData>sizeAttributeList = parseLookupXMLForSize(attributeLookupData, pimAttrName, childNode.getTextContent(),childNode.getParentNode().getNodeName());
        				    attributes.addAll(sizeAttributeList);
        				}else if (!childNodes.item(i).getNodeName().equals("#text")) {
        				    AttributeData attributeInfo = new AttributeData();                            
        					attributeInfo.setAttributeName(pimAttrName);
        					
        					if(pimAttrName.equalsIgnoreCase("Channel_Exclusive") && value !=null){
        					    value = value.equalsIgnoreCase("Ecomm")?"Yes":"No";
        					}
        					attributeInfo.setAttributeValue(value);
        					attributeInfo.setQName(new QName(childNode.getParentNode().getNodeName()));
        					boolean isAttributeLookupValueRequired = isAttributeLookupRequired(childNode.getAttributes());
        					if (isAttributeLookupValueRequired) {
        						String lookUpAttrValue = getLookupValue(attributeLookupData, childNode.getNodeName(), value,elementName);
        						attributeInfo.setAttributeValue(lookUpAttrValue);
        					}
        					if((childNode.getNodeName().equals("SKU_Active_Start_Date") || childNode.getNodeName().equals("Launch_Date")) 
        					        && childNode.getTextContent()!=null  && childNode.hasAttributes()){
        					    String format = childNode.getAttributes().getNamedItem("format").getNodeValue();
        					    String dateValue = childNode.getTextContent();        					    

        				        if(dateValue!=null && format!=null){
        				            try{
        				                String formatedDate = BelkDateUtil.formatDate(format, dateValue);
        				                if(dateValue!=null){
                                            attributeInfo.setAttributeValue(formatedDate);
        				                }
        				            }catch(ParseException pe){
        				                if(log.isErrorEnabled()){
        				                    log.error("Error while parsing SKU_Active_Start_Date/Launch_Date "+pe);
        				                }
        				            }
        				            
        				        }        
        					}
        					attributes.add(attributeInfo);
        				}
        			}
        		}
        	}
        }
		return attributes;
	}
	
	/**
	 * This method is added to parse the attribute look up values for Omnichannel_Size section.
	 * Within the zIPH_Lkp_Red section it will parse the following elements
	 * <omni_size_desc>,<facet_size_1/>,<facet_size_2/>,<facet_size_3/>,<facet_sub_size_1/><facet_sub_size_2/>
	 * and return a list of AttributeData objects. 
	 * 
	 * @param lookupResponseData
	 * @param nodeName
	 * @param textContent
	 * @param parentNodeName
	 * @return
	 * @throws XPathExpressionException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private List<AttributeData> parseLookupXMLForSize(
            LookupAttributeData lookupResponseData, String nodeName,
            String textContent, String parentNodeName) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException{
	    List<AttributeData> sizeAttributeList = new ArrayList<AttributeData>();
	    if(log.isDebugEnabled()){
	        log.debug("Inside parseLookupXMLForSize method ");
	    }
	    if (lookupResponseData != null && lookupResponseData.getAttributeLookupDatas() != null) {
            for (AttributeLookupData lookupData : lookupResponseData.getAttributeLookupDatas()) {
                 if(log.isInfoEnabled()){
                     log.info("lookupData values in XML Format ::"+lookupData.getEntryValuesInXmlFormat() );
              }
                if (lookupData != null && lookupData.getEntryValuesInXmlFormat() != null) {
                    if (lookupData.getSourceSpecPath().contains(nodeName)) {
                                                
                        for(String elementName : sizeEelements){
                            AttributeData attributeInfo = new AttributeData();
                            InputStream stream = new ByteArrayInputStream(lookupData.getEntryValuesInXmlFormat().getBytes());
                            XPath xmlPath = XPathFactory.newInstance().newXPath();
                            String lookupAttrValue = null;
                            String attributeName = elementName.equals("omni_size_desc")?"size_name":elementName;
                            if (elementName.matches("^_\\d*_.*")) {
                                elementName = elementName.substring(elementName.indexOf("_", 1)+1, elementName.length());
                            }
                            if(log.isDebugEnabled()){
                                log.debug("attribute name in parseLookupXMLForSize method "+elementName);
                            }
                            attributeInfo.setAttributeName(attributeName);
                            attributeInfo.setAttributeValue(textContent);
                            attributeInfo.setQName(new QName(parentNodeName));
                            XPathExpression pathInXml = xmlPath.compile("/entry/*/" + elementName);
                            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                            NodeList attributeNodeList = (NodeList) pathInXml.evaluate(documentBuilder.parse(stream), XPathConstants.NODESET);
                            int nodeList = (attributeNodeList != null) ? attributeNodeList.getLength() : 0;
                            for (int i = 0; i < nodeList; i++) {
                                Node nNode = attributeNodeList.item(i);
                                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                    if (nNode.hasChildNodes()) {
                                        lookupAttrValue = nNode.getChildNodes().item(0).getNodeValue();
                                    }
                                }
                            }
                            attributeInfo.setAttributeValue(lookupAttrValue);
                            if(log.isInfoEnabled()){
                                log.info("attribute value in parseLookupXMLForSize method "+lookupAttrValue);
                            }
                            sizeAttributeList.add(attributeInfo);
                        }
                        
                    }
                }
            }
        }
        return sizeAttributeList;
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
		String formattedInput = String.format(getXPathBasedOnRequest(requestType), requestId, attributeSectionName);
		XPath xmlPath = XPathFactory.newInstance().newXPath();
		XPathExpression pathInXml = xmlPath.compile(formattedInput);
		attributeNodeList = (NodeList) pathInXml.evaluate(xmlResponseStructure, XPathConstants.NODESET);
		return attributeNodeList;
	}
	
	/**
     * Method to indicate if response is succesful or not.
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
     * Method to filter all attribute names that starts with _number_ and replace it with blank spaces.
     * @param attributeName - name of the attribute to be checked for pattern.
     * @return - attribute name without the starting pattern of _number_
     */
    private String filterAttributeName(String attributeName) {
    	Pattern pattern = Pattern.compile("_" + "(\\d+[,/%]?\\d*)" + "_");
    	String modifiedAttributeName = null;
        Matcher matcher = pattern.matcher(attributeName);
        boolean isFirst = matcher.find(0);
        if (isFirst) {
    	    StringBuffer changedAttributeNames = new StringBuffer();
		    matcher.appendReplacement(changedAttributeNames, "");
            matcher.appendTail(changedAttributeNames);
            modifiedAttributeName = changedAttributeNames.toString();
        } else {
    	    modifiedAttributeName = attributeName;
        }
        return modifiedAttributeName;
    }
	
    /**
     * This method is used to specifically mark the response with a specific indicator to be able
     * to use that information while parsing for attributes. Under the dom structure if response 
     * contains same section names for multiple sections, the values will be consolidated for all
     * the responses. Response from the pim system has no unique identifiers for section, hence the need to 
     * add an index to the value. For example, <Item_Catalog type="Style"> is the response and it has 2 responses
     * , this method will modify that to be <Item_Catalog type="Style" id="1">,<Item_Catalog type="Style" id="2">
     * and so on.
     * @param xmlResponse - Response XML
     * @param requestType - To identify the type attribute value in the dom struture.
     * @return - modified string value as mentioned above.
     */
	private String addIndexToCatalog(String xmlResponse, String requestType) {
		String catalogSectionPattern = String.format(ITEM_CATALOG_XML_ELEMENT_DESC, requestType);
		Pattern pattern = Pattern.compile(catalogSectionPattern);
		Integer id = 0;
        Matcher matcher = pattern.matcher(xmlResponse);
        StringBuffer changedAttributeNames = new StringBuffer();
        while (matcher.find()) {
        	matcher.appendReplacement(changedAttributeNames, 
        			"<Item_Catalog type" + "="  + "\"" + requestType + "\" " + "id=" + "\"" + (++id) + "\"");
    	}
        matcher.appendTail(changedAttributeNames);
        return changedAttributeNames.toString();
	}
    private String getXPathBasedOnRequest(String requestType) {
    	return (requestType.equals(GetRequestType.PACK.toString()) ? PACK_SECTION_PATH_IN_XML : ITEM_SECTION_PATH_IN_XML);
	}
	private boolean isAttributeLookupRequired(NamedNodeMap attributes) {
		boolean isAttributeLookupRequired = false;
		if (attributes != null && attributes.getLength() > 0) {
			if (attributes.getNamedItem(ATTRIBUTE_LOOKUP_VALUE_INDICATOR) != null) {
				isAttributeLookupRequired = true;
			}
		}
		return isAttributeLookupRequired;
	}
    private String getLookupValue(LookupAttributeData lookupResponseData, String attributeName, String attributeValue,String elementName) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
    	String lookupAttributeValue = attributeValue;
    	if (lookupResponseData != null && lookupResponseData.getAttributeLookupDatas() != null) {
    		for (AttributeLookupData lookupData : lookupResponseData.getAttributeLookupDatas()) {
    			if (lookupData !=null && lookupData.getCode() !=null && lookupData.getCode().equals(attributeValue)) {
    				String lookupAttrValue = parseLookupXMLValue(attributeName, elementName, lookupData);
    				if (lookupAttrValue != null) {
    					lookupAttributeValue = lookupAttrValue;
    				}
    				break;
    			}
    		}
    	}
    	return lookupAttributeValue;
    }
    private String parseLookupXMLValue(String attributeName, String elementName, AttributeLookupData lookupAttributeData) 
    		throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
    	String lookupAttrValue = null;
    	if (lookupAttributeData != null && lookupAttributeData.getEntryValuesInXmlFormat() != null) {
    		if (lookupAttributeData.getSourceSpecPath().contains(attributeName)) {
    			InputStream stream = new ByteArrayInputStream(lookupAttributeData.getEntryValuesInXmlFormat().getBytes());
    			XPath xmlPath = XPathFactory.newInstance().newXPath();
    			XPathExpression pathInXml = xmlPath.compile("/entry/*/" + elementName);
    			DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    			NodeList attributeNodeList = (NodeList) pathInXml.evaluate(documentBuilder.parse(stream), XPathConstants.NODESET);
    			int nodeList = (attributeNodeList != null) ? attributeNodeList.getLength() : 0;
    			for (int i = 0; i < nodeList; i++) {
    				Node nNode = attributeNodeList.item(i);
    	        	if (nNode.getNodeType() == Node.ELEMENT_NODE) {
    	        		if (nNode.hasChildNodes()) {
    	        			lookupAttrValue = nNode.getChildNodes().item(0).getNodeValue();
    	        		}
				    }
    		    }
    		}
    	}
    	return lookupAttrValue;
    }
    
    /**
     * Method to identify if the xml element is excluded or not.
     * Elements starting with sec_spec and zbm_sec_spec must 
     * be excluded for sku,style_color and pack_color request types.
     *  
     * @param name
     * @param requestType
     * @return
     */
    private boolean isElementExcluded(String name,String requestType){
        boolean isExcluded = false;
        if(exclRequestTypes.contains(requestType) && (name.startsWith("sec_spec")||name.startsWith("zbm_sec_spec"))){
            isExcluded = true;
        }        
        return isExcluded;
    }
    
    /**
     * Method to load the UDA Inclusion List provided by business.All
     * other attributes as part of Item_UDA_Spec will be ignored for processing. 
     * 
     */
    @SuppressWarnings("unchecked")
    private static void getUDAInclusionList(){
        Properties properties = PropertyLoader.loadProperties("UDAInclList.properties");
        String value =  properties.getProperty("UDA_INCL_LIST");
        if(value!=null && !value.isEmpty()){
            udaInclList = Arrays.asList(value.split(","));
        }
        if(log.isInfoEnabled()){
            log.info("UDA incl List isEmpty  "+udaInclList.isEmpty());
        }
        if(udaInclList.isEmpty()){
           udaInclList =  new ArrayList<String>(){{
                add("BRIDAL");
                add("BRIDAL_REGISTRY");
                add("ECOMMERCE_ONLY");
                add("SOLD_IN_STORES");
                add("SOLD_ONLINE");
                add("GIFT_CARD");
                add("TRENDS");
                add("YC_TRENDS");
                add("FALL_FY17_WOMENS_BIG_IDEAS");
                add("VENDOR_COLLECTION");
            }};
        }
        
        if(log.isInfoEnabled()){
            log.info("UDA incl List "+udaInclList);
        }
    }
    
    /**
     * Method to load the Ecomm_Sku_Spec Inclusion List.All
     * other attributes as part of Ecomm_SKU_Spec will be ignored for processing.
     * This is done to avoid unnecessary processing for performance improvement.
     */
    private static void getEcommSkuSpecInclList() {
        Properties properties = PropertyLoader.loadProperties("EcommSkuSpec_InclList.properties");
        String value =  properties.getProperty("ECOMM_SKU_SPEC_INCL");
        if(value!=null && !value.isEmpty()){
            ecommSkuSpecInclList = Arrays.asList(value.split(","));
        }
        if(log.isInfoEnabled()){
            log.info("ECOMM SKU SPEC incl List isEmpty  "+ecommSkuSpecInclList.isEmpty());
        }
                
        if(log.isInfoEnabled()){
            log.info("ECOMM SKU SPEC incl List "+ecommSkuSpecInclList);
        }
        
    }
}

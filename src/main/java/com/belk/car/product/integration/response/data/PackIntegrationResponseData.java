package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class will be used to unmarshall the response for getPack request. The data will
 * be similar in structure to the get item request, except for the root element. JAXB currently
 * doesn't support either or conditions for the xml element mapping, hence this class
 * has to be used to map the pack response.
 * 
 */
@XmlRootElement(name = "getPackResponse", namespace = "http://services.belkinc.com/product")
public class PackIntegrationResponseData extends IntegrationResponseData {

}

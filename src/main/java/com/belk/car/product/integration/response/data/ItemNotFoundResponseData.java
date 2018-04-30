package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * This class is used to identify the response when the search criteria used for the 
 * input request doesn't yield any result from the integration. The current integration
 * returns a xml response starting with <Error> which is different when the service is
 * down which comes with an element starting with <error>. Since JAXB doesn't support
 * case insensitive mapping, this class has to be created to be able to map it to
 * the item not found response from PIM.
 */
@XmlRootElement(name = "Error")
public class ItemNotFoundResponseData extends ErrorResponseData {

}

package com.belk.car.product.integration.adapter;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * 
 * This class will act as the XML Registry component for configuring all dynamic elements that could be coming 
 * in the response for the groupings integration for cars with the PIM application. 
 * Currently this list needn't be maintained as the application will handle elements not defined as part
 * of this class as well. 
 *
 */
@XmlRegistry
public class GroupObjectFactory {
	
	public static final String SEC_SPEC_4694_WOMEN = "sec_spec_4694-Women";
	@XmlElementDecl(name = SEC_SPEC_4694_WOMEN)
	public JAXBElement<String> createSEC_SPEC_4694_WOMEN(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_4694_WOMEN), String.class, attribute);
	}

	public static final String SEC_SPEC_4748_SHORTS = "sec_spec_4748-Shorts";
	@XmlElementDecl(name = SEC_SPEC_4748_SHORTS)
	public JAXBElement<String> createSEC_SPEC_4748_SHORTS(String attribute) { 
	    return new JAXBElement<String>(new QName(SEC_SPEC_4748_SHORTS), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_4694_WOMEN = "zbm_sec_spec_4694_Women";
	@XmlElementDecl(name = ZBM_SEC_SPEC_4694_WOMEN)
	public JAXBElement<String> createZBM_SEC_SPEC_4694_WOMEN(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_4694_WOMEN), String.class, attribute);
	}

	public static final String ZBM_SEC_SPEC_4748_SHORTS = "zbm_sec_spec_4748_Shorts";
	@XmlElementDecl(name = ZBM_SEC_SPEC_4748_SHORTS)
	public JAXBElement<String> createZBM_SEC_SPEC_4748_SHORTS(String attribute) { 
	    return new JAXBElement<String>(new QName(ZBM_SEC_SPEC_4748_SHORTS), String.class, attribute);
	}
	
	public static final String ECOMM_STYLE_SPEC = "Ecomm_Style_Spec";
	@XmlElementDecl(name = ECOMM_STYLE_SPEC)
	public JAXBElement<String> createECOMM_STYLE_SPEC(String attribute) { 
	    return new JAXBElement<String>(new QName(ECOMM_STYLE_SPEC), String.class, attribute);
	}

}

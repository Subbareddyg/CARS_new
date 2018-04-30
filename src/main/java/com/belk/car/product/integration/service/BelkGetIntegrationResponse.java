package com.belk.car.product.integration.service;

import com.belk.car.app.util.PropertyLoader;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.belk.car.product.integration.exception.BelkProductIntegrationException;
import com.belk.car.product.integration.request.data.IntegrationRequestData;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class will be used to make a http call to the new PIM integration service. This class will
 * also use the XSL stylesheet to parse the input request to the specified format for the integration. 
 * 
 */
public class BelkGetIntegrationResponse {
	
	private final IntegrationRequestData requestData;
	
	private static Source xslSource;
	
	private static Transformer inputTransformer;
	
	private static JAXBContext createContext;
        
    private static final Log log = LogFactory.getLog(BelkGetIntegrationResponse.class);
	
	static {
		createJaxbContext();
	}
	
	public BelkGetIntegrationResponse(IntegrationRequestData requestData) {
		this.requestData = requestData;
	}
	
	/**
	 * Method to get the transformer object using the specified stylesheet configuration.
	 * @return - Transformer instance.
	 */
	private static Transformer createTransformerInstance() {
		if (inputTransformer == null) {
			TransformerFactory factory =  TransformerFactory.newInstance();
			try {
				inputTransformer = factory.newTransformer(getXSLTemplate("/Integration_Request.xsl"));
			} catch (TransformerConfigurationException e) {
				if (log.isErrorEnabled()) {
					log.error("Couldn't create transformer object for xsl ", e);
                }
			}
	    }
		return inputTransformer;
	}
	
	/**
	 * Creates a singleton JAXBContext for the IntegrationRequestData class.
	 * @return - JAXBContext
	 */
	private static JAXBContext createJaxbContext() {
		if (createContext ==  null) {
			try {
				createContext = JAXBContext.newInstance(IntegrationRequestData.class);
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				if (log.isErrorEnabled()) {
					log.error("Couldn't create JAXB Context for input xml ", e);
                }
			}
		}
		return createContext;
	}
	
	/**
	 * This method will be used to fetch the configured xsl template for the input request from the 
	 * class path.
	 * @param fileName
	 * @return - Style sheet resource.
	 */
	private static Source getXSLTemplate(String fileName) {
		if (xslSource == null) {
			InputStream xslInputStream = BelkGetIntegrationResponse.class.getClassLoader().getResourceAsStream(fileName);
            xslSource = new StreamSource(xslInputStream);
        }
		return xslSource;
	}
	
	/**
	 * Method to create and use the transformer instance and make a request to get the response.
	 * @return - response in xml format.
	 * @throws JAXBException - Exception while creating the JAXBContext
	 * @throws TransformerException - Exception while transforming the input request using the configured stylesheet.
	 * @throws SocketTimeoutException - Exception while trying to connect to the configured service
	 * @throws IOException - Exception while trying to process the response from the service.
	 * @throws BelkProductIntegrationException - Integration service url isn't configured.
	 */
	public String getIntegrationResponse() throws JAXBException, TransformerException, SocketTimeoutException, 
	       IOException, BelkProductIntegrationException {
		createTransformerInstance();
		String xmlRequest = getXmlRequestString();
		String response = sendRequestToPIM(createHttpConnection(), xmlRequest);
		return response;
	}
	
	/**
	 * Method to create the input xml structure. 
	 * @return - Request XML String
	 * @throws JAXBException - Exception while marshalling the input data.
	 * @throws TransformerException - Exception while transforming the input xml with the stylesheet.
	 */
	private String getXmlRequestString() throws JAXBException, TransformerException {
		String xmlRequest = null;
		if (inputTransformer != null && createContext != null) {
			ByteArrayOutputStream plainXMLRequestData = new ByteArrayOutputStream();
			Marshaller createXmlRequest = createContext.createMarshaller();
			createXmlRequest.marshal(requestData, plainXMLRequestData);
			ByteArrayOutputStream requestDataWithHeaders = new ByteArrayOutputStream();
			StreamResult streamResult = new StreamResult(requestDataWithHeaders);
			ByteArrayInputStream inputStream = new ByteArrayInputStream(plainXMLRequestData.toByteArray());
			Source outputData = new StreamSource(inputStream);
			inputTransformer.transform(outputData, streamResult);
			xmlRequest = requestDataWithHeaders.toString();
		}
		if (log.isDebugEnabled()) {
            log.debug("Request Xml " + xmlRequest);
        }
        return xmlRequest;
	}
	
	/**
	 * Method to create a HttpURLConnection object with the configured service url
	 * @return - HttpURLConnection
	 * @throws IOException - Exception while creating the http connection.
	 * @throws BelkProductIntegrationException - Integration service url isn't configured.
	 */
	private HttpURLConnection createHttpConnection() throws IOException, BelkProductIntegrationException {
		HttpURLConnection urlConn;
		URL url = new URL(getServiceURL());
		urlConn = (HttpURLConnection) url.openConnection();
		urlConn.setRequestMethod("POST");
		urlConn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
		urlConn.setDoInput(true);
		urlConn.setDoOutput(true);
		urlConn.setUseCaches(false);
		urlConn.setDefaultUseCaches(false);
		return urlConn;
	}
	
	/**
	 * Method to integrate request with the configured service url.
	 * @param httpCon - HttpURLConnection object with the service URL
	 * @param xmlContents - Input XML Request
	 * @return - Response String in XML format.
	 * @throws SocketTimeoutException - Exception while connecting to the service.
	 */
	private String sendRequestToPIM(HttpURLConnection httpCon, String xmlContents) throws SocketTimeoutException {
		StringBuilder responseContents = new StringBuilder();
		PrintWriter responseObject = null;
		InputStreamReader responseReader = null;
		try {
			responseObject = new PrintWriter(new OutputStreamWriter(httpCon.getOutputStream(), "8859_1"), true);
			responseObject.print(xmlContents);
			responseObject.flush();
			responseReader = new InputStreamReader(httpCon.getInputStream());
			BufferedReader reader = new BufferedReader(responseReader);
			String line;
			while ((line = reader.readLine()) != null) {
				responseContents.append(line);
			}
		} catch (MalformedURLException exp) {
			exp.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (responseObject != null) {
				try {
					responseObject.close();
				} catch (Exception e) {
					if (log.isErrorEnabled()) {
                        log.error("Exception while closing response object ", e);
                    }
				}
			}
			if (responseReader != null) {
				try {
					responseReader.close();
				} catch (IOException e) {
					if (log.isErrorEnabled()) {
                        log.error("Exception while closing response object ", e);
                    }
				} catch (Exception e) {
					if (log.isErrorEnabled()) {
                        log.error("Exception while closing response object ", e);
                    }
				}
			}
		}
		return responseContents.toString();
	}
	
	/**
     * Method to read the specific configured url for the service URL integration
     * @return - Service URL.
     */
    private String getServiceURL() throws BelkProductIntegrationException {
        Properties properties = PropertyLoader.loadProperties("jms.properties");
        Object configuredProperty = properties.get("PIMCarIntegrationServiceWSDL");
        if (configuredProperty == null) {
        	throw new BelkProductIntegrationException("Service URL isn't configured in the specifi properties ");
        }
        return configuredProperty.toString();
    }
	
        

	

}

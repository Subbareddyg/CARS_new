/**
 * @author afusy12
 */
package com.belk.car.integrations.pim;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.belk.car.app.service.QuartzJobManagerForPIMJobs;
import com.belk.car.integrations.pim.xml.PoMessage;
import com.belk.eil.logging.utils.appender.message.LogProcessorMessage;

public class POMessageMQListner implements MessageListener {

	Logger log = LogManager.getLogger(POMessageMQListner.class);
	
	private QuartzJobManagerForPIMJobs quartzJobManagerForPIMJobs;

	POMessageMQListner() { }

	public QuartzJobManagerForPIMJobs getQuartzJobManagerForPIMJobs() {
		return quartzJobManagerForPIMJobs;
	}

	public void setQuartzJobManagerForPIMJobs(
			QuartzJobManagerForPIMJobs quartzJobManagerForPIMJobs) {
		this.quartzJobManagerForPIMJobs = quartzJobManagerForPIMJobs;
	}
	
	/**
	 * This method called when PO message received on MQ for CAR creation
	 * 
	 */
	public void onMessage(Message message) {
		System.out.println("--------------------------------------> Begin POMessage Listening -------------------------------------------->");
		String correlId = null;
		String poMessage =null;
		try {
			correlId = message.getStringProperty("correlationId");
			if (message instanceof TextMessage) {
				// Received MQ message
				poMessage = ((TextMessage) message).getText();
			} else if (message instanceof BytesMessage) {
				// Received MQ message
				byte[] byteArray = new byte[(int) ((BytesMessage) message).getBodyLength()];
				((BytesMessage) message).readBytes(byteArray);
				poMessage = new String(byteArray);
			} else {
				ErrorLog("PO message type does not supported " + correlId, correlId, poMessage);
			}
			if (null == poMessage || poMessage.isEmpty()) {
				InfoLog("Processing PO message in method onMessage -- poMessage is emtpy " + correlId, correlId, poMessage );
			
			}else {
				InfoLog("Processing PO message in method onMessage -- poMessage is " + correlId, correlId, poMessage);
				// Create poMessageObjects through xml beans
				PoMessage poMessageObj = unmarshallPOMessageXML(poMessage);
				// Call import cars method by passing poMessageObj as input parameter
				quartzJobManagerForPIMJobs.processPOCAR(poMessageObj);
				
			}
		} catch (JMSException e) {
			ErrorLog("Exception occured while processing the PO message -- "+correlId+" -- "+ e.getMessage(), correlId, poMessage);
		} catch (IOException ioe) {
			ErrorLog("Exception occured while processing the PO message -- "+correlId+" -- "+ ioe.getMessage(), correlId, poMessage);
		} catch (JAXBException jaxbe) {
			ErrorLog("JAXBException occurred while reading and ummarshalling -- "+correlId+" -- "+ jaxbe.getMessage(), correlId, poMessage);
		} catch (Exception ex) {
			ErrorLog("Exception occured while processing the PO message -- "+correlId+" -- "+ ex.getMessage(), correlId, poMessage);
		}
		System.out.println("-------------------------------------> End POMessage Listening -------------------------------------------------->");
	}


	/**
	 * 
	 * @param poMessage
	 * @return
	 * @throws JAXBException 
	 */
	public static PoMessage unmarshallPOMessageXML(String poMessage)
			throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(PoMessage.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		// Convert poMessageString to poMessageReader
		StringReader poMessageReader = new StringReader(poMessage);
		PoMessage message = (PoMessage) unmarshaller.unmarshal(poMessageReader);
		return message;
	}

	/**
	 * @param message
	 * @param correlationId
	 * @param payload
	 * @return
	 */
	public void InfoLog(String desc,
			String correlationId, String payload) {
		HashMap<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("payloadMessageType", "MQ");
		parameterMap.put("payloadMessageDomain", "XML");
		parameterMap.put("payloadMessageCCSID",System.setProperty("file.encoding", "UTF-8"));
		parameterMap.put("payloadMessageEncoding",System.setProperty("file.encoding", "UTF-8"));
		parameterMap.put("payloadMessageParentCorrelationId", "");
		parameterMap.put("payloadMessageCorrelationId", correlationId);
		parameterMap.put("payloadMessageHeader", null);
		parameterMap.put("payloadMessageBody", payload);
		parameterMap.put("javaClassName", Object.class.getName());
		LogProcessorMessage eilInfoMsg = new LogProcessorMessage(desc, parameterMap);
		log.info(eilInfoMsg);
	}
	
	/**
	 * @param message
	 * @param correlationId
	 * @param payload
	 * @return
	 */
	public void ErrorLog(String desc,
			String correlationId, String payload) {
		HashMap<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("payloadMessageType", "MQ");
		parameterMap.put("payloadMessageDomain", "XML");
		parameterMap.put("payloadMessageCCSID",System.setProperty("file.encoding", "UTF-8"));
		parameterMap.put("payloadMessageEncoding",System.setProperty("file.encoding", "UTF-8"));
		parameterMap.put("payloadMessageParentCorrelationId", "");
		parameterMap.put("payloadMessageCorrelationId", correlationId);
		parameterMap.put("payloadMessageHeader", null);
		parameterMap.put("payloadMessageBody", payload);
		parameterMap.put("javaClassName", Object.class.getName());
		LogProcessorMessage eilErrorMsg = new LogProcessorMessage(desc, parameterMap);
		log.error(eilErrorMsg);
	}
}

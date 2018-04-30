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
import com.belk.car.integrations.pim.xml.DropShipMessage;
import com.belk.eil.logging.utils.appender.message.LogProcessorMessage;

public class DropShipMessageMQListner implements MessageListener{
	
	Logger log = LogManager.getLogger(DropShipMessageMQListner.class);
	
	private QuartzJobManagerForPIMJobs quartzJobManagerForPIMJobs;
	
	DropShipMessageMQListner(){	}
	
	public QuartzJobManagerForPIMJobs getQuartzJobManagerForPIMJobs() {
		return quartzJobManagerForPIMJobs;
	}
	public void setQuartzJobManagerForPIMJobs(QuartzJobManagerForPIMJobs quartzJobManagerForPIMJobs) {
		this.quartzJobManagerForPIMJobs = quartzJobManagerForPIMJobs;
	}
	
	/**
	 * This method called when DROPSHIP message received on MQ for CAR creation
	 * 
	 */
	public void onMessage(Message message) {
		System.out.println("-------------------------------------> Begin DROPSHIP Message Listening ----------------------------------->");
		String correlId = null;
		String dropShipMessage=null;
		try {
			correlId = message.getStringProperty("correlationId");
			if (message instanceof TextMessage) {
				// Received MQ message
				dropShipMessage = ((TextMessage) message).getText();
			} else if (message instanceof BytesMessage) {
				// Received MQ message
				byte[] byteArray = new byte[(int) ((BytesMessage) message).getBodyLength()];
				((BytesMessage) message).readBytes(byteArray);
				dropShipMessage = new String(byteArray);
			} else {
				ErrorLog("DROPSHIP message type does not supported -- " + correlId, correlId, dropShipMessage);
			}
			
			if (null == dropShipMessage || dropShipMessage.isEmpty()) {
				System.out.println("-------------------> dropShipMessage is null/empty in onMessageMethod --------------------------->");
				ErrorLog("dropShipMessage is empty in onMessageMethod for correlId-- "+"no dropShipMessage", correlId, dropShipMessage);				
			}else{
				InfoLog(" Processing DROPSHIP message -- " + correlId, correlId, dropShipMessage);
				// Create dropShipMessageObj through xml beans
				DropShipMessage dropShipMessageObj = unmarshallPOMessageXML(dropShipMessage);
				// Call create dropship cars method by passing dropShipMessageObj as input parameter
				quartzJobManagerForPIMJobs.processDropShipCAR(dropShipMessageObj);
			}
		} catch (JMSException e) {
			ErrorLog("Exception occured while processing the DROPSHIP message -- "+correlId+" -- "+ e.getMessage() , correlId, dropShipMessage);
		} catch (IOException ioe) {
			ErrorLog("Exception occured while processing the DROPSHIP message --  "+correlId+" -- "+ ioe.getMessage(), correlId, dropShipMessage);
		} catch (JAXBException jaxbe) {
			ErrorLog("JAXBException occurred while reading and ummarshalling -- "+correlId+" -- "+ jaxbe.getMessage(), correlId, dropShipMessage);
		} catch (Exception ex) {
			ErrorLog("Exception occured while processing the DROPSHIP message -- "+correlId+" -- "+ ex.getMessage(), correlId, dropShipMessage);
		}
		System.out.println("---------------------------------------> End DROPSHIP Message Listening -------------------------------------->");
	}
	
	/**
	 * @param dropshipMessage
	 * @return
	 * @throws JAXBException 
	 */
	public static DropShipMessage unmarshallPOMessageXML(String dropshipMessage)
			throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(DropShipMessage.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		// Convert dropshipMessage String to dropshipMessageReader
		StringReader dropshipMessageReader = new StringReader(dropshipMessage);
		DropShipMessage message = (DropShipMessage) unmarshaller.unmarshal(dropshipMessageReader);
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
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
import com.belk.car.integrations.pim.xml.RLRItems;
import com.belk.eil.logging.utils.appender.message.LogProcessorMessage;

public class RLRMessageMQListener implements MessageListener{
	
	Logger log = LogManager.getLogger(RLRMessageMQListener.class);
	
	private QuartzJobManagerForPIMJobs quartzJobManagerForPIMJobs;
	
	RLRMessageMQListener(){	}
	
	public QuartzJobManagerForPIMJobs getQuartzJobManagerForPIMJobs() {
		return quartzJobManagerForPIMJobs;
	}
	public void setQuartzJobManagerForPIMJobs(QuartzJobManagerForPIMJobs quartzJobManagerForPIMJobs) {
		this.quartzJobManagerForPIMJobs = quartzJobManagerForPIMJobs;
	}
	
	/**
	 * This method called when RLR message received on MQ 
	 * for updating the class/dept.
	 */
	public void onMessage(Message message) {
		System.out.println("---------------------------------> Begin RLR Message Listening -------------------------------------------->");
		String correlId = null;
		String rlrMessage=null;
		try {
			correlId = message.getStringProperty("correlationId");
			if (message instanceof TextMessage) {
				// Received MQ message
				rlrMessage = ((TextMessage) message).getText();
			} else if (message instanceof BytesMessage) {
				// Received MQ message
				byte[] byteArray = new byte[(int) ((BytesMessage) message).getBodyLength()];
				((BytesMessage) message).readBytes(byteArray);
				rlrMessage = new String(byteArray);
			} else {
				ErrorLog("RLR message type does not supported -- " + correlId, correlId, rlrMessage);
			}
			if (rlrMessage != null) {
				InfoLog("Processing RLR message -- " + correlId, correlId,
						rlrMessage);
				// Create rlrMessageObj through xml beans
				RLRItems rlrMessageObj = unmarshallPOMessageXML(rlrMessage);
				// Call process RLR method by passing rlrMessageObj as input
				// parameter
				quartzJobManagerForPIMJobs.processRLRMessage(rlrMessageObj);
			}
		} catch (JMSException e) {
			ErrorLog("Exception occured while processing the RLR message -- "+correlId+" -- "+ e.getMessage(), correlId, rlrMessage);
		} catch (IOException ioe) {
			ErrorLog("Exception occured while processing the RLR message -- "+correlId+" -- "+ ioe.getMessage(), correlId, rlrMessage);
		} catch (JAXBException jaxbe) {
			ErrorLog("JAXBException occurred while reading and ummarshalling -- "+correlId+" -- "+ jaxbe.getMessage(), correlId, rlrMessage);
		} catch (Exception ex) {
			ErrorLog("Exception occured while processing the RLR message -- "+correlId+" -- "+ ex.getMessage(), correlId, rlrMessage);
		}
		System.out.println("--------------------------------------> End RLR Message Listening --------------------------------------------->");
	}
	
	/**
	 * @param rlrMessageReader
	 * @return
	 * @throws JAXBException 
	 */
	public static RLRItems unmarshallPOMessageXML(String rlrMessage)
			throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(RLRItems.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		// Convert dropshipMessage String to dropshipMessageReader
		StringReader rlrMessageReader = new StringReader(rlrMessage);
		RLRItems message = (RLRItems) unmarshaller.unmarshal(rlrMessageReader);
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

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

import com.belk.car.app.exceptions.GroupCreateException;
import com.belk.car.app.service.QuartzJobManagerForPIMJobs;
import com.belk.car.integrations.pim.xml.GroupingMessage;
import com.belk.eil.logging.utils.appender.message.LogProcessorMessage;

/**
 * 
 */
public class GroupingMessageMQListener implements MessageListener {

    Logger log = LogManager.getLogger(GroupingMessageMQListener.class);

    private QuartzJobManagerForPIMJobs quartzJobManagerForPIMJobs;

    public QuartzJobManagerForPIMJobs getQuartzJobManagerForPIMJobs() {
        return quartzJobManagerForPIMJobs;
    }

    public void setQuartzJobManagerForPIMJobs(QuartzJobManagerForPIMJobs quartzJobManagerForPIMJobs) {
        this.quartzJobManagerForPIMJobs = quartzJobManagerForPIMJobs;
    }

    /**
     * This method called when Grouping message received on MQ for CAR creation
     * 
     * @param message
     */
    public void onMessage(Message message) {
        if (log.isInfoEnabled()) {
            log.info("--------------------------------------> Begin GroupingMessage Listening -------------------------------------------->");
        }
        String correlId = null;
        String groupingMessage = null;
        try {
            correlId = message.getStringProperty("correlationId");
            if (message instanceof TextMessage) {
                // Received MQ message
                groupingMessage = ((TextMessage) message).getText();
            } else if (message instanceof BytesMessage) {
                // Received MQ message
                byte[] byteArray = new byte[(int) ((BytesMessage) message).getBodyLength()];
                ((BytesMessage) message).readBytes(byteArray);
                groupingMessage = new String(byteArray);
            } else {
                ErrorLog("Unsupported MQ message type for grouping message - expected TextMessage or ByteMessage "
                        + correlId, correlId, groupingMessage, null);
            }
            if (groupingMessage != null) {
                InfoLog("Processing Grouping message -- " + correlId, correlId, groupingMessage);

                if (log.isInfoEnabled()) {
                    log.info("Grouping Message XML " + groupingMessage);
                }

                // Create groupingMessageObjects through xml beans
                GroupingMessage groupingMessageObj = unmarshallGroupingMessageXML(groupingMessage);
                // Call import cars method by passing groupingMessageObj as
                // input parameter
                quartzJobManagerForPIMJobs.processGrouping(groupingMessageObj);

                if (log.isInfoEnabled()) {
                    log.info("Grouping Message Processed");
                }
            }
        } catch (JMSException e) {
            ErrorLog("Exception occured while processing the Grouping message -- " + correlId + " -- " + e.getMessage(),
                    correlId, groupingMessage, e);
        } catch (IOException ioe) {
            ErrorLog("Exception occured while processing the Grouping message -- " + correlId + " -- "
                    + ioe.getMessage(), correlId, groupingMessage, ioe);
        } catch (JAXBException jaxbe) {
            ErrorLog("JAXBException occurred while reading and ummarshalling -- " + correlId + " -- "
                    + jaxbe.getMessage(), correlId, groupingMessage, jaxbe);
        } catch (GroupCreateException gce) {
            ErrorLog("No Child CAR(s) found!  Logging GroupCre message in GROUP_CREATE_MSG_FAILURE table.", correlId, groupingMessage, null);
            
            quartzJobManagerForPIMJobs.saveGroupingFailureInDB(gce.getStyleOrins(),groupingMessage);
        } catch (Exception ex) {
            ErrorLog(
                    "Exception occured while processing the Grouping message -- " + correlId + " -- " + ex.getMessage(),
                    correlId, groupingMessage, ex);
        }
        if (log.isInfoEnabled()) {
            log.info(
                    "-------------------------------------> End GroupingMessage Listening -------------------------------------------------->");
        }
    }

    /**
     * 
     * @param groupingMessage
     * @return
     * @throws JAXBException
     */
    public static GroupingMessage unmarshallGroupingMessageXML(String groupingMessage) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(GroupingMessage.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        // Convert groupingMessageString to groupingMessageReader
        StringReader groupingMessageReader = new StringReader(groupingMessage);
        GroupingMessage message = (GroupingMessage) unmarshaller.unmarshal(groupingMessageReader);
        return message;
    }

    /**
     * @param desc
     * @param correlationId
     * @param payload
     */
    public void InfoLog(String desc, String correlationId, String payload) {
        HashMap<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("payloadMessageType", "MQ");
        parameterMap.put("payloadMessageDomain", "XML");
        parameterMap.put("payloadMessageCCSID", System.setProperty("file.encoding", "UTF-8"));
        parameterMap.put("payloadMessageEncoding", System.setProperty("file.encoding", "UTF-8"));
        parameterMap.put("payloadMessageParentCorrelationId", "");
        parameterMap.put("payloadMessageCorrelationId", correlationId);
        parameterMap.put("payloadMessageHeader", null);
        parameterMap.put("payloadMessageBody", payload);
        parameterMap.put("javaClassName", Object.class.getName());
        LogProcessorMessage eilInfoMsg = new LogProcessorMessage(desc, parameterMap);
        if (log.isInfoEnabled()) {
            log.info(eilInfoMsg);
        }
    }

    /**
     * @param desc
     * @param correlationId
     * @param payload
     */
    public void ErrorLog(String desc, String correlationId, String payload, Exception e) {
        HashMap<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("payloadMessageType", "MQ");
        parameterMap.put("payloadMessageDomain", "XML");
        parameterMap.put("payloadMessageCCSID", System.setProperty("file.encoding", "UTF-8"));
        parameterMap.put("payloadMessageEncoding", System.setProperty("file.encoding", "UTF-8"));
        parameterMap.put("payloadMessageParentCorrelationId", "");
        parameterMap.put("payloadMessageCorrelationId", correlationId);
        parameterMap.put("payloadMessageHeader", null);
        parameterMap.put("payloadMessageBody", payload);
        parameterMap.put("javaClassName", Object.class.getName());
        LogProcessorMessage eilErrorMsg = new LogProcessorMessage(desc, parameterMap);
        if (e != null) {
        	log.error(eilErrorMsg, e);
        } else {
        	log.error(eilErrorMsg);
        }
    }

}
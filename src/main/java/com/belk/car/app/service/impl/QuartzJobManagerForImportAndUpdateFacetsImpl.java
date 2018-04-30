/**
 * 
 */
package com.belk.car.app.service.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.belk.car.app.exceptions.CarJobDetailException;
import com.belk.car.app.exceptions.IExceptionProcessor;
import com.belk.car.app.exceptions.SendEmailException;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.Config;
import com.belk.car.app.model.NotificationType;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.service.AttributeManager;
import com.belk.car.app.service.CarFacetImportAndUpdateManager;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.EmailManager;
import com.belk.car.app.service.QuartzJobManagerForImportAndUpdateFacets;
import com.belk.car.util.DateUtils;

/**
 * @author afuszm1
 *
 */
public class QuartzJobManagerForImportAndUpdateFacetsImpl implements QuartzJobManagerForImportAndUpdateFacets, IExceptionProcessor{

	private CarLookupManager lookupManager;
	private UserManager userManager;
	private CarManager carManager;
	private EmailManager sendEmailManager;
	private CarFacetImportAndUpdateManager carFacetImportAndUpdateManager;
	private transient final Log log = LogFactory.getLog(QuartzJobManagerForImportAndUpdateFacetsImpl.class);
	//As this job is used only for Facet attribute and will run this job few time only so i hard coded below values
	//in future if they want to schedule this job then we will insert these values into the database.
	//final String localDirName = "/cars/data/carsdata/facetattr/";
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	

	
	

    
	//The Attribute manager code is added for the testing purpose 
	
	private AttributeManager attributeManager;

	/**
	 * @param attributeManager
	 *            the attributeManager to set
	 */
	public void setAttributeManager(AttributeManager attributeManager) {
		this.attributeManager = attributeManager;
	}
	
	public void setSendEmailManager(EmailManager sendEmailManager) {
		this.sendEmailManager = sendEmailManager;
	}
	public void setLookupManager(CarLookupManager lookupManager) {
		this.lookupManager = lookupManager;
	}
	
	public UserManager getUserManager() {
		return userManager;
	}
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}
	
	public void setCarFacetImportAndUpdateManager(CarFacetImportAndUpdateManager carFacetImportAndUpdateManager) {
		this.carFacetImportAndUpdateManager = carFacetImportAndUpdateManager;
	}
	
	public void importAndUpdateCarFacetAttibutes() throws IOException, CarJobDetailException{
		
		//Need to configure below CONFIG values for Facet attributes 
		
		/*final String host = ((Config) lookupManager.getById(Config.class, Config.RRD_FTP_HOST)).getValue();
		final String userName = ((Config) lookupManager.getById(Config.class, Config.RRD_FTP_USERNAME)).getValue();
		final String password = ((Config) lookupManager.getById(Config.class, Config.RRD_FTP_PASSWORD)).getValue();
		final String remoteDirName = ((Config) lookupManager.getById(Config.class, Config.RRD_MC_TEMP_IMAGE_DOWNLOAD_DIR)).getValue();*/
		//final String localDirName = ((Config) lookupManager.getById(Config.class, Config.LOCAL_MC_TEMP_IMAGE_DOWNLOAD_DIR)).getValue();
		final String localDirName =((Config) lookupManager.getById(Config.class, Config.BELK_FACET_ATTRIBUTE_LOCAL_DIR)).getValue();

		final boolean deleteAfterGet = true;
		
		/*try {
			FTPClient ftp = FtpUtil.openConnection(host, userName, password);
			FtpUtil.setTransferModeToAscii(ftp);
			FtpUtil.getDataFiles(ftp, remoteDirName, localDirName, deleteAfterGet);
			FtpUtil.closeConnection(ftp);
		} catch (IOException ioex) {
			log.error("Could not read files from the  FTP", ioex);
		}*/
		//processFacetAttibutes();//this method call added for testing
		FileProcessor readFacetAtrFileToDB = new FileProcessor(){
			public void process(File f) throws Exception {
				if (!f.exists() || f.isDirectory() || (f.length() == 0))
					return;
				log.info("calling read and process method for: "+ f.getName());
				processFacetAttibutes(f);
			}
			
			public String getProcessName() {
				return "reading XML from FTP";
			}
		};

		String importFileName = ((Config) lookupManager.getById(Config.class, Config.RRD_IMPORT_FILENAME)).getValue();
		String completedDirName = ((Config) lookupManager.getById(Config.class, Config.RRD_FILE_COMPLETED_DIRECTORY_NAME)).getValue();

		FileProcessorChain fileProcessorChain = new FileProcessorChain("importing and update Facet Attributes");
		fileProcessorChain.setExceptionProcessor(this) ;
		fileProcessorChain.setFiles(findFiles(localDirName, importFileName));
		fileProcessorChain.addFileProcessor(readFacetAtrFileToDB, FileProcessorChain.ActionOnException.ContinueWithNextFile);
		fileProcessorChain.addFileProcessor(new FileArchiver(completedDirName), FileProcessorChain.ActionOnException.ContinueChain);
		fileProcessorChain.processFiles();
	}
	
	private File[] findFiles(String dirName, String fileName) {
		return findFiles(dirName, fileName, false);
	} 
	
	private File[] findFiles(String dirName, String fileName, boolean includeDirectories) {
		// We need all of the config values
		if (dirName == null) {
			throw new IllegalArgumentException("Cannot process files. The directory name is missing");
		}

		boolean useAllFilesInDirectory = StringUtils.isNotBlank(dirName) && StringUtils.isBlank(fileName);
		File[] files = null;
		if (useAllFilesInDirectory) { //User selected to process all files under the directory			
			File dir = new File(dirName);
			FileFilter fileFilter = (includeDirectories) ? null : new FileFilter() {
				public boolean accept(File pathname) {
					return (!pathname.isDirectory());
				}
			};
			files = dir.listFiles(fileFilter);
		} else { //User selected to scan a single file
			String filePath = dirName + fileName;
			File fl = new File(filePath);
			files = new File[] { fl };
		}
		return files;
	}
	
	interface FileProcessor {
		public String getProcessName();

		public void process(File file) throws Exception;
	}	
	
	/*
	 * Method to Read XML and update car attribute table only  Facet attributes.
	 */
	public void processFacetAttibutes(File f) throws Exception {
	//public void processFacetAttibutes() throws IOException, CarJobDetailException {
		log.info("-------------->Start Importing and Updating Facet attribute into Car Attributes values<-----------");
		try{
			
			Config userName = (Config) lookupManager.getById(Config.class, Config.CAR_IMPORT_USER);

			User systemUser = this.userManager.getUserByUsername(userName.getValue());
			Date date = new Date();
			DocumentBuilderFactory documenBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder =  documenBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse (f);
            doc.getDocumentElement ().normalize ();
            NodeList LOP = doc.getElementsByTagName("VendorAttribute");
            int LOPLength=LOP.getLength();
            String vendorNo="";
            String vendorStyleNumber="";
            Map<String, String> attributeMap=null;
            String ERROR_CD_ATTR_NOT_PRESENT_IN_ATTRIBUTE="ERROR1";
            String ERROR_CD_ATTR_NOT_MAPPED="ERROR2";
            
            final String localGeneratedDirName =((Config) lookupManager.getById(Config.class, Config.BELK_FACET_ATTRIBUTE_LOCAL_GENERATED_DIR)).getValue();
        	File fErr=  new File(localGeneratedDirName+"error.txt");//

            Set<String> attrNotProcessed=new TreeSet<String>();
        	//As this job is used only for Facet attribute and will run this job few time only so i hard coded below values
        	//in future if they want to schedule this job then we will insert these values into the database.
            File fAtt= new File(localGeneratedDirName+"DataSendToCMP"+dateFormat.format(date).toString()+".txt");
            
            FileWriter fw = new FileWriter(fAtt);
            PrintWriter out = new PrintWriter(fw);

            FileWriter fwError = new FileWriter(fErr,true);
            PrintWriter outError = new PrintWriter(fwError);
 
         
            for(int s=0; s < LOPLength ; s++)
            { 
            	try{
                    Node FPN =LOP.item(s);
                    if(FPN.getNodeType() == Node.ELEMENT_NODE)
                    {
                        Element firstPElement = (Element)FPN;
                    
                        NodeList oNameList = firstPElement.getElementsByTagName("VendorNumber");
                        Element firstNameElement = (Element)oNameList.item(0);
                        if(firstNameElement != null){
                        	NodeList textNList = firstNameElement.getChildNodes();
                        	if(textNList.item(0) != null)
                        		vendorNo=((Node)textNList.item(0)).getNodeValue().trim();
                        }
                        NodeList IDList = firstPElement.getElementsByTagName("VendorStyleNumber");
                        Element IDElement = (Element)IDList.item(0);
                        if(IDElement != null){
                            NodeList textIDList = IDElement.getChildNodes();
                            if(textIDList.item(0) != null)
                            	vendorStyleNumber=((Node)textIDList.item(0)).getNodeValue().trim();
                        }
                        log.debug("Product Code ---->>>>>>---"+vendorNo+vendorStyleNumber);
                        NodeList AttributeList = firstPElement.getElementsByTagName("Attribute");
                        
                        if(attributeMap == null ){
                        	attributeMap=new HashMap<String, String>();
                        }else{
                        	attributeMap.clear();
                        }
                        if(!attrNotProcessed.isEmpty())
                  			attrNotProcessed.clear();
                  	
                        String attrName="";
                        String attrValue="";
                        int attrsLength=AttributeList.getLength();
                        
                        for(int i=0; i < attrsLength ; i++)
                        { 
                        	attrName="";
                            attrValue="";
                        	Node FPN1 =AttributeList.item(i);
                            
                            Element firstAElement = (Element)FPN1;
                                   
                            NodeList aNameList = firstAElement.getElementsByTagName("Name");
                            Element ANameElement = (Element)aNameList.item(0);
                            if(ANameElement != null){
                            	NodeList textNList = ANameElement.getChildNodes();
                            	if(textNList.item(0) != null)
                            		attrName=((Node)textNList.item(0)).getNodeValue().trim();
                            }
                            
                            NodeList aValueList = firstAElement.getElementsByTagName("Value");
                            Element AValueElement = (Element)aValueList.item(0);
                            if(AValueElement != null){
                            	 NodeList textVList = AValueElement.getChildNodes();
                            	 if(textVList.item(0) != null)
                            	 	 attrValue=((Node)textVList.item(0)).getNodeValue().trim();
                            }
                            
                            log.debug(vendorNo+vendorStyleNumber+"    Attribute Name -->>> -- "+attrName+"  Value  ------>>>- "+attrValue);
                            boolean isPresentFL = attributeManager.getAttributesByBlueMartiniNameAndType(attrName, "FACET");
                            if(isPresentFL){
                            	attributeMap.put(attrName.toUpperCase(), attrValue);
                            	attrNotProcessed.add(attrName.toUpperCase());
                            }else{
                            	log.debug(attrName +" of "+vendorNo+vendorStyleNumber+" is not present in the Attribute table.");
                            	outError.write(f.getName()+"    "+ERROR_CD_ATTR_NOT_PRESENT_IN_ATTRIBUTE+" - "+vendorNo+vendorStyleNumber+" - "+attrName+" is not present in the Attribute table.");
                            	outError.println();
                            }
                         }//end of for loop with variable i 
                          // System.err.println("attributeMap  "+attributeMap);
                          //Get Vendor Style 
                         VendorStyle vendorStyle = carManager.getVendorStyle(vendorNo, vendorStyleNumber);
                         if(vendorStyle != null){
                          	List<Car> cars = carManager.getAllCarForStyle(vendorStyle.getVendorStyleId());
                           	if(cars != null && cars.size() > 0){
                          		for(Car car:cars){
                          		    this.carFacetImportAndUpdateManager.resyncAttributes(car, systemUser,attributeMap,attrNotProcessed);
                                    this.carManager.save(car) ;
                          		 }
                          	 }
                         }
                         if(!attrNotProcessed.isEmpty()){
                        	 Iterator<String> iAttrName=attrNotProcessed.iterator();
                        	 while(iAttrName.hasNext()){
                        		   attributeMap.remove((String)iAttrName.next());
                        	 }
                        	 log.debug(attrNotProcessed +" of Product Code " +vendorNo+vendorStyleNumber+ " are not processed.");
                        	 outError.write(f.getName()+"    "+ERROR_CD_ATTR_NOT_MAPPED+"-"+attrNotProcessed +" of Product Code " +vendorNo+vendorStyleNumber+ " are not processed.");
                        	 outError.println();
                        	 outError.println();
                         }
                         Iterator entries = attributeMap.entrySet().iterator();
                         while (entries.hasNext()) 
                         {  
                        	 Entry thisEntry = (Entry) entries.next();   
                        	 Object key = thisEntry.getKey();   
                        	 Object value = thisEntry.getValue(); 
                        	 out.write(vendorNo+vendorStyleNumber+"|");
                             out.write(key+"|");
                             out.write(value+"");
                             out.println("");
                         }
                      }    //end of if clause 
             	    }catch(Exception e){
             	    	log.error("Error occurred 1 : "+ vendorNo+vendorStyleNumber +" data is not updated. "+e);
             	    	outError.write(f.getName()+"    PRODUCT CODE  " +vendorNo+vendorStyleNumber+ " is not processed.");
             	    	outError.println();
             	    	e.printStackTrace();
             	    	continue;
             	   }finally{
             		   //do nothing for now
             	   }
                }
          //Flush the output to the file
          
            out.flush();
            outError.flush();
            //Close the Print Writer
            out.close();
            outError.close();
            //Close the File Writer
            fw.close(); 
            fwError.close();
			//Code ends here
     	}catch(Exception ex){
     		log.error("Error occurred 2 : Car Attribute table is not updated.    "+ex);
     		ex.printStackTrace();
		}finally{
			//do nothing for now
		}
	}
	
	static class FileProcessorChain {
		String name;

		public FileProcessorChain(String name) {
			this.name = name;
		}

		IExceptionProcessor exceptionProcessor ;
		public void setExceptionProcessor(IExceptionProcessor exceptionProcessor) {
			this.exceptionProcessor = exceptionProcessor ;
		}
		
		File[] files = new File[0];

		private void setFiles(File[] files) {
			this.files = files;
		}

		public void setFiles(Collection<File> files) {
			this.files = files.toArray(new File[0]);
		}

		public void addFiles(Collection<File> additionalFiles) {
			List<File> fileList = Arrays.asList(this.files);
			fileList.addAll(additionalFiles);
			this.files = fileList.toArray(new File[0]);
		}

		List<FileProcessor> fileProcessors = new ArrayList<FileProcessor>();
		Map<FileProcessor, ActionOnException> fileProcessor2ActionOnError = new HashMap<FileProcessor, ActionOnException>();

		public static enum ActionOnException {
			ContinueChain, ContinueWithNextFile, DoNotContinue
		}

		public void addFileProcessor(FileProcessor fileProcessor) {
			fileProcessors.add(fileProcessor);
		}

		public void addFileProcessor(FileProcessor fileProcessor, ActionOnException actionOnException) {
			addFileProcessor(fileProcessor);
			fileProcessor2ActionOnError.put(fileProcessor, actionOnException);
		}

		public void processFiles() {
			if (files == null || files.length < 1 || fileProcessors == null || fileProcessors.size() < 1)
				return;
			Log log = LogFactory.getLog(QuartzJobManagerForImportAndUpdateFacetsImpl.class);

			processingFiles: 
			for (File file : files) {

				processingTheCurrentFile: 
				for (FileProcessor fileProcessor : fileProcessors) {
					try {
						if (log.isInfoEnabled())
							log.info("running process (" + this.name + ") on file " + file.getName());
						//if (file.length() > 0)
						fileProcessor.process(file);
						if (log.isInfoEnabled())
							log.info("- " + fileProcessor.getProcessName() + ": success");
					} catch (Exception ex) {
						if (log.isErrorEnabled()) {
							log.error("- " + fileProcessor.getProcessName() + ": failure", ex);
						}
						
						if (this.exceptionProcessor != null) {
							exceptionProcessor.processException(fileProcessor.getProcessName() + ":(" +  file.getName() + ")", ex);
						}

						ActionOnException actionOnEx = fileProcessor2ActionOnError.get(fileProcessor);
						String actionStr;
						switch (actionOnEx) {
						case ContinueChain:
							actionStr = "Continuing with next process on the current file.";
							break;
						case DoNotContinue:
							actionStr = "Processing on all files has stopped.";
							break;
						default:
						case ContinueWithNextFile:
							actionStr = "Continuing with next file.";
							break;
						}
						log.error(actionStr);

						switch (actionOnEx) {
						case ContinueChain:
							continue processingTheCurrentFile;
						case DoNotContinue:
							return;
						default:
						case ContinueWithNextFile:
							continue processingFiles;
						}
					}
				}
			}
		}

	}
	
	private class FileArchiver implements FileProcessor {
		String archiveDirName;

		FileArchiver(String archiveDirName) {
			this.archiveDirName = archiveDirName;
		}

		public void process(File f) throws Exception {
			if (f.isDirectory() || !f.exists())
				return;

			File destDir = new File(f.getParentFile(), archiveDirName);
			String destFileName = new StringBuffer().append(DateUtils.formatDate(new Date(), "MM_dd_yyyy_HH_mm_ss")).append("_").append(f.getName()).toString();
			File destFile = new File(destDir, destFileName);

			if (!destDir.exists())
				destDir.mkdirs();

			boolean isMoveSuccessful = f.renameTo(destFile);

			if (isMoveSuccessful) {
					log.info(f.getName() + " file was archived successfully.");
			} else {
					log.error("Error moving file " + f.getName() + " to directory: " + destDir);
			}
		}

		public String getProcessName() {
			return "archiving";
		}
	}
	
	public void processException(String processName, Exception ex) {
		//StringWriter sw = new StringWriter();
		//ex.printStackTrace(new PrintWriter(sw));
		//sendFailureNotification(processName, sw.toString()); commenting on 07/30 as per code review comment
	}

	private void sendFailureNotification(String processName, String content) {
		NotificationType type = lookupManager.getNotificationType(NotificationType.SYSTEM_FAILURE);

		Config userName = (Config) lookupManager.getById(Config.class, Config.SYSTEM_USER);
		Config sendNotifications = (Config) lookupManager.getById(Config.class, Config.SEND_EMAIL_NOTIFICATIONS);
		Config emailNotificationList = (Config) lookupManager.getById(Config.class, Config.SCHEDULED_PROCESS_ERROR_NOTIFICATION_LIST);

		User systemUser = this.userManager.getUserByUsername(userName.getValue());

		Map<String, String> model = new HashMap<String, String>();
		String[] emails = StringUtils.split(emailNotificationList.getValue(), ",;");
		for (String email : emails) {
			model.put("userEmail", email);
			model.put("processName", processName);
			model.put("exceptionContent", StringUtils.abbreviate(content,4000));
			model.put("executionDate", DateUtils.formatDate(new Date(), "MM/dd/yyyy HH:mm:ss"));

			try {
				if ("true".equalsIgnoreCase(sendNotifications.getValue())) {
					sendEmailManager.sendNotificationEmail(type, systemUser, model);
				}
			} catch (SendEmailException se) {
				log.error("Error when sending email to: " + email);
			} catch (Exception ex) {
				log.error("General Exception occured. Cause: " + email);
			}
		}

	}
}

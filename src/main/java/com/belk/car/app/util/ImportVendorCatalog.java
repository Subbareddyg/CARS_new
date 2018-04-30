
package com.belk.car.app.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.parser.ParserInitializationException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.dao.DataAccessException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.exceptions.SendEmailException;
import com.belk.car.app.model.vendorcatalog.CompositeKeyForVndrCatRecord;
import com.belk.car.app.model.vendorcatalog.CompositeKeyForVndrCatlHeader;
import com.belk.car.app.model.vendorcatalog.VendorCatalog;
import com.belk.car.app.model.vendorcatalog.VendorCatalogDepartment;
import com.belk.car.app.model.vendorcatalog.VendorCatalogHeader;
import com.belk.car.app.model.vendorcatalog.VendorCatalogRecord;
import com.belk.car.app.service.DropshipManager;

/**
 * Class with method to read the IDB Feed file
 * 
 * @author afusy45
 */
public class ImportVendorCatalog implements DropShipConstants {



	private DropshipManager dropshipManager;

	/**
	 * @return the dropshipManager
	 */
	public DropshipManager getDropshipManager() {
		return dropshipManager;
	}

	/**
	 * @param dropshipManager the dropshipManager to set
	 */
	public void setDropshipManager(DropshipManager dropshipManager) {
		this.dropshipManager = dropshipManager;
	}

	private static transient final Log log = LogFactory.getLog(ImportVendorCatalog.class);

	// List<VendorCatalogHeader> resultList = new
	// ArrayList<VendorCatalogHeader>();
	private List<VendorCatalogRecord> listForRecords = null;// new
	// ArrayList<VendorCatalogRecordModel>();

	private VendorCatalogHeader catalogHeaderModel = null;
	private VendorCatalogRecord catalogRecordModel = null;
	private CompositeKeyForVndrCatlHeader compositeKey = null;
	private CompositeKeyForVndrCatRecord compositeKeyForVndrCatRecord = null;
	private Map<String, List> headerFieldList = new HashMap<String, List>();
	private Map<String, List> errorMap1 = new HashMap<String, List>();
	private List errorList = new ArrayList();
	private boolean isValidData = true;

	/**
	 * Method to copy images from external site to destination folder Only
	 * images mentioned in vendor Catalog file will be saved
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public boolean methodReadingImagesFromSite(
			List list, Long catalogId, String destinationDirectory, String destinationFtpHost,
			String destinationFtpUname, String destinationFtpPasswd, String sourceFtp,
			String ftpUname, String ftpPwd)
	throws IOException, ParseException {
		log.debug("Read images from site");

		log.info("list size" + list.size());
		log.info("sourceFtp--))))" + sourceFtp);
		List imageList = new ArrayList();
		boolean saveImage = false;
		for (int l = 0; l < list.size(); l++) {
			log.info("1st record" + list.get(l).toString());
			String imagePath = sourceFtp + list.get(l).toString();
			log.info("ImagePath bedore appending to buffer" + imagePath);

			try {
				log.debug("inside try to connect to site");

				log.info("Destination Directory" + destinationDirectory);
				FileUtils.copyURLToFile(new URL(imagePath), new File(destinationDirectory));
				log.info("copy successful-----------====== :)):)");
				saveImage = true;
				imageList.add(list.get(l).toString());
			}

			catch (Exception e) {
				e.printStackTrace();
				log.debug("Copying image failed");
				//"No-Image-Available" image should be shown in case no images are available for the catalog file
				FileUtils.copyURLToFile(new URL(
						NO_IMAGE_AVAILABLE_LOGO),
						new File(destinationDirectory));

			}


		}
		if (saveImage) {
			// Save in DB
			dropshipManager.saveImageData(imageList, catalogId);

		}
		return true;
	}

	/**
	 * Method to read image names from unmapped folder and save to Image table
	 * @author afusy45
	 * @param String DestDir
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean methodSavingImagesToImageTable(String destDir, long catalogId){
		log.info("Directory from which images should be read"+destDir);
		List imageNamesList = new ArrayList();

		/* Following code might be required if image extensions are fixed.
		  List<String> extensionOfFiles = new ArrayList<String>();
		extensionOfFiles.add("jpg");
		extensionOfFiles.add("jpeg");
		extensionOfFiles.add("jpe");
		extensionOfFiles.add("jfif");
		extensionOfFiles.add("bmp");
		extensionOfFiles.add("dib");
		extensionOfFiles.add("tif");
		extensionOfFiles.add("tiff");
		extensionOfFiles.add("psd");
		extensionOfFiles.add("gif");
		extensionOfFiles.add("png");*/

		File destDirectory = new File(destDir);
		//Extract list of files at the location(Directory)
		File[] files = destDirectory.listFiles();
		if(files !=null){
			List<File> imageFiles = FtpUtil.getImageFilesFromDirectory(files);
			if(imageFiles != null){
				/** copying images names to DB table */
				for (int i = 0; i < imageFiles.size(); i++) {

					FileInputStream fis = null;
					/**Added for filter functionality */

					log.info("file at ftp...."+imageFiles.get(i).getName());
					//File file = new File(files[i].getName());
					imageNamesList.add(imageFiles.get(i).getName());
					//Make entry into VendorCatalogImage table

					
				}
			}//files not null

			try {
				dropshipManager.saveImageData(imageNamesList,catalogId);
			}
			catch (ParseException e) {
				log.error("Exception while saving image data in table"+e);
			}
		}
		return true;
	}

	/**
	 * Method to read Text File
	 * 
	 * @param fl
	 * @return List
	 */
	public List readTextFile(File fl, VendorCatalog catalog, String delimiter)
	throws IOException, ParseException {
		log.debug("Inside read Text File");
		List<VendorCatalogHeader> resultList = new ArrayList<VendorCatalogHeader>();

		if (log.isInfoEnabled()){
			log.info("Opened Path");
		}
		HashMap<Long, List<VendorCatalogRecord>> records = new HashMap<Long, List<VendorCatalogRecord>>();
		List<VendorCatalogDepartment> departments = new ArrayList();
		String noteSubject = "";
		String noteText = "";
		List noteTextList = new ArrayList();
		List errorListForV = new ArrayList();
		List errorListForP = new ArrayList();
		int processingErrorCount = 1;
		int validationErrorCount = 1;
		int alertMsg = 0;
		List imageValueslist = new ArrayList();
		List<Object[]> car_user_ids = new ArrayList();
		errorMap1 = new HashMap<String, List>();

		// Reads line by line into List
		List list = FileUtils.readLines(fl, UTF_8);
		String str = (String) list.get(0);
		String[] headerFields = null;
		String[] recordLength = null;
		if ("|".equals(delimiter) && !("".equals(delimiter))) {
			log.info("Delimiter is a pipe");
			delimiter = "\\" + delimiter;
			headerFields = str.split(delimiter);
		}
		if (!"".equals(delimiter)) {
			headerFields = str.split(delimiter);
		}

		// Count of header fields
		log.info("Count of header fields" + headerFields.length);
		headerFieldList = new HashMap<String, List>();

		/* Validations */
		// To check unique header field names
		for (int i = 0; i < headerFields.length; i++) {
			// Check for unique header length
			if (headerFieldList.containsKey(headerFields[i])) {
				log.debug("header name is not unique");
				if (validationErrorCount <= 5) {
					errorListForV.add(0);
					errorMap1.put("V", errorListForV);
					validationErrorCount++;
					alertMsg = 0;
				}
				else {
					alertMsg = 1;
				}
				noteSubject = "Validation Error";
				noteText = "The header field values are not unique for catalog:"
					+ catalog.getVendorCatalogName() + "and file name:" + fl.getName();
				// errorList.add("Validation Error");
				noteTextList.add(noteText);
				isValidData = false;
			}
			else {
				log.debug("header name is unique");
				headerFieldList.put(headerFields[i], errorList);

			}

		}
		headerFieldList.clear();
		// Check for record values count and header fields count
		for (int j = 1; j < list.size(); j++) {
			String record = (String) list.get(j);
			/*
			 * Start validation of no of values in record match the no of header
			 * fields
			 */
			if ("|".equals(delimiter)) {
				log.info("Delimiter is a pipe");
				delimiter = "\\" + delimiter;
				recordLength = record.split(delimiter);
			}else{
				recordLength = record.split(delimiter);
			}
			log.info("recordLength = "+recordLength.length);
		}

		/* End of validation loop */

		/* Processing the records */
		List nullRowsArray = new ArrayList();
		int nullRowCount =0;
		if (errorMap1.isEmpty()) {
			log.debug("If errorMap1 is empty");
			Short positionNumber = 0;
			List imageNamelist = new ArrayList();
			for (int i = 0; i < headerFields.length; i++) {
				log.debug("Inside for loop for header....in processing loop");
				/* Need to get the position number of the image name headers */
				if (headerFields[i].equalsIgnoreCase(MAIN_IMAGE_NAME)) {
					log.info("Header-->main image name");
					imageNamelist.add(positionNumber);
				}
				if (headerFields[i].equalsIgnoreCase(SWATCH_IMAGE_NAME)) {
					log.info("Header-->SWATCH_IMAGE_NAME");
					imageNamelist.add(positionNumber);
				}
				if (headerFields[i].equalsIgnoreCase(ALTERNATE_IMAGE_1)) {
					log.info("Header-->ALTERNATE_IMAGE_1");
					imageNamelist.add(positionNumber);
				}
				if (headerFields[i].equalsIgnoreCase(ALTERNATE_IMAGE_2)) {
					log.info("Header-->ALTERNATE_IMAGE_2");
					imageNamelist.add(positionNumber);
				}
				if (headerFields[i].equalsIgnoreCase(ALTERNATE_IMAGE_3)) {
					log.info("Header-->ALTERNATE_IMAGE_3");
					imageNamelist.add(positionNumber);
				}
				if (headerFields[i].equalsIgnoreCase(ALTERNATE_IMAGE_4)) {
					log.info("Header-->ALTERNATE_IMAGE_4");
					imageNamelist.add(positionNumber);
				}
				if (headerFields[i].equalsIgnoreCase(ALTERNATE_IMAGE_5)) {
					log.info("Header-->ALTERNATE_IMAGE_5");
					imageNamelist.add(positionNumber);
				}
				positionNumber++;

				/* Entries in catalog header object */
				catalogHeaderModel = new VendorCatalogHeader();
				// catalogRecordModel = new VendorCatalogRecordModel();
				listForRecords = new ArrayList<VendorCatalogRecord>();
				catalogHeaderModel.setVendorCatalogHeaderFieldName(headerFields[i]);				
				catalogHeaderModel.setVendorCatalogID(catalog);
				catalogHeaderModel.setVendorCatalogFieldNum(new Long(i + 1));				
				records.put(new Long(i), listForRecords);
				resultList.add(catalogHeaderModel);

			}// End of for loop of headerFields

			// Fort each header enter value in record table
			
			for (int j = 1; j < list.size(); j++) {
				int counter=0;
				log.debug("Inside for loop for record....");
				String record = (String) list.get(j);
				String[] recordValue = null;
				if ("|".equals(delimiter)) {
					log.info("Delimiter is a pipe");
					delimiter = "\\" + delimiter;
					recordValue = record.split(delimiter);
				}
				if(nullRowCount>=5){
					log.debug("if more than 5 rows are blank");
					
					//break;
					j = list.size();
				}else{
				recordValue = record.split(delimiter);
				
				if(recordValue.length>1){
				for (int i = 0; i < headerFields.length; i++) {
					log.debug("Inside for loop for headerFields....");

					/* Process the values of images and add it to list */
					String imageValue = "";
					if (recordValue[i].equalsIgnoreCase(" ") || recordValue[i].trim().equalsIgnoreCase("") || recordValue[i].equals(null)){ 
					
						imageValue = recordValue[i];
					}
					/** adding image names if not empty */
					if (imageNamelist.contains(i) && !imageValue.equals(BLANK)) {

						log.debug("image value is not blank");
						imageValueslist.add(imageValue);

					}
					//positionNumber++;

					log.debug("Inside for loop for headerFields....");
					catalogRecordModel = new VendorCatalogRecord();
					compositeKeyForVndrCatRecord = new CompositeKeyForVndrCatRecord();
					compositeKeyForVndrCatRecord.setCatalogId(catalog.getVendorCatalogID());
					compositeKeyForVndrCatRecord.setHeaderNum(new Long(i + 1));
					compositeKeyForVndrCatRecord.setRecordNumber(new Long(j));
					catalogRecordModel.setCompositeKey(compositeKeyForVndrCatRecord);
					//If value is empty or contains space ,then inserting null.
					log.info("recordValue[i] for header"+i+"and row no"+j+""+recordValue[i]);
					if(recordValue[i].equalsIgnoreCase(" ") || recordValue[i].trim().equalsIgnoreCase("") || recordValue[i].equals(null)){
						log.debug("record value is null");
						counter++;
						catalogRecordModel.setVendorCatalogFieldValue(null);
					}else{
						catalogRecordModel.setVendorCatalogFieldValue(recordValue[i]);
					}
					
					if(counter >= headerFields.length){
						log.debug("full row is empty");
						if(nullRowsArray.size()<5){
						nullRowsArray.add(new Long(j));
						}
						nullRowCount++;
					}else{
						nullRowCount =0;
					}
					
					listForRecords = records.get(new Long(i));
					listForRecords.add(catalogRecordModel);

					records.put(new Long(i), listForRecords);


				}
				}else{
					log.debug("row is empty with record value length check");
					nullRowsArray.add(new Long(j));
					nullRowCount++;
				}
				}//end else loop if null row count <5
				if(nullRowCount>=5){
					log.debug("if more than 5 rows are blank");
					
					//break;
					j = list.size();
				}
			}// End of for loop
		}// end of error map is empty
		else {
			log.debug("error map is not empty");

			// if ((errorListForP.size() + errorListForV.size()) < 5) {
			log.debug("If no of errors is less than 5");
			try {
				dropshipManager.saveVendorCatalogNote(catalog, noteSubject, noteTextList);
			}
			catch (ParseException e) {
				log.error("Exception while saving vendor catalog note"+e);
			}
			// Send Email
			try {
				dropshipManager.sendEmailNotification(catalog, list.size(), errorMap1, alertMsg);

			}
			catch (Exception e) {
				log.error("Exception while sending email"+e);

			}

		}
		/* Check if there were no validation errors before saving */
		if (isValidData) {
			log.debug("There were no validation errors");

			dropshipManager.saveHeader(resultList);

			try {
				dropshipManager.deleteRecord(catalog.getVendorCatalogID());
			}
			catch (Exception e) {
				log.error("Exception deleting record from record table" + e);
			}
			Set<Long> set = records.keySet();
			Iterator<Long> it = set.iterator();
			/*while (it.hasNext()) {
				log.debug("Record saving for next header");
				dropshipManager.saveRecord(records.get(it.next()));
			}*/
			List<VendorCatalogRecord> list1 =new ArrayList<VendorCatalogRecord>();
			List<VendorCatalogRecord> list2 =new ArrayList<VendorCatalogRecord>();
			
			
			while (it.hasNext()) {
			    log.debug("Record saving for next header");
			   
			    list1 = records.get(it.next());
			  log.info("main list size"+list.size());
			    log.info("list1 size"+list1.size());
			    
			 for(int k=0;k<list1.size();k++){
				
				   if(!nullRowsArray.contains(new Long(k))){
					   log.info("k does not belong to null array"+k);
				  list2.add(list1.get(k));
				   }
				 }
			 
			    dropshipManager.saveRecord(list2);
			    
			}
			try {
				dropshipManager.sendEmailNotification(catalog, list2.size(), errorMap1, alertMsg);

			}
			catch (Exception e) {
				log.error("Exception while sending email"+e);

			}


			/*
			 * Get all the departments associated to vendor catalog and hence
			 * the car_user_ids who are Buyer and own these departments
			 */

			sendMailToWebBuyers(catalog);
			

	}

		return imageValueslist;
	}

	public List readExcelFile(File fl, VendorCatalog catalog)
	throws FileNotFoundException, IOException {

		log.debug("Inside method to read xls file");
		boolean success = true;
		String noteSubject = "";
		String noteText = "";
		List noteTextList = new ArrayList();
		// List for storing image names
		List imageList = new ArrayList();
		List<VendorCatalogHeader> resultList = new ArrayList<VendorCatalogHeader>();
		HSSFSheet sheet = null;
		HSSFWorkbook wb= new HSSFWorkbook();
		try{
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fl));
			// POIFSFileSystem fs = new POIFSFileSystem();
			wb = new HSSFWorkbook(fs);
			sheet = wb.getSheetAt(0);
		}catch(Exception ie){
			log.error("Invalid Excel file"+ie);
		}


		List errorListForV = new ArrayList();
		List errorListForP = new ArrayList();
		errorMap1 = new HashMap<String, List>();
		HashMap<Long, List<VendorCatalogRecord>> records = new HashMap<Long, List<VendorCatalogRecord>>();
		log.debug("reading excel file ....");
		int headerFieldCount = 0;
		int recordFieldCount = 0;
		int processingErrorCount = 1;
		int validationErrorCount = 1;
		int alertMsg = 0;
		int i = 0;
		Iterator rowIterator = sheet.rowIterator();
		headerFieldList = new HashMap<String, List>();
		/* Validations */
		// To check unique header field names
		HSSFRow row;
		BigDecimal decimal = null;
		Double cellValue = null;
		Iterator cellIterator;
		int cellno =0;
		HSSFCell cell;
		if (rowIterator.hasNext()) {
			row = (HSSFRow) rowIterator.next();
			int rowNum = row.getRowNum();
			cellIterator = row.cellIterator();

			cellno = row.getPhysicalNumberOfCells();
			headerFieldList = new HashMap<String, List>();
			// while (cellIterator.hasNext()) {
			for (int c = 0; c < cellno; c++) {

				cell = (HSSFCell) cellIterator.next();

				String headerField = cell.getRichStringCellValue().toString();
				log.info("Header Field = "+headerField);
				headerFieldCount = headerFieldCount + 1;
				if(!headerField.equals("")){



					// i = cell.getCellNum();
					if (headerFieldList.containsKey(headerField)) {
						log.debug("header name is not unique :"+headerField);
						if (validationErrorCount <= 5) {
							errorListForV.add(rowNum);

							errorMap1.put("V", errorListForV);
							validationErrorCount++;
							alertMsg = 0;
						}
						else {
							alertMsg = 1;
						}
						noteSubject = "Validation Error";
						noteText = "The header field values are not unique for catalog:"
							+ catalog.getVendorCatalogName() + "and file name:" + fl.getName() + " and header field:"+headerField;

						noteTextList.add(noteText);
						isValidData = false;
					}
					else {
						log.debug("header name is unique");
						headerFieldList.put(headerField, errorList);
					}

				}
			}

		}

		// Validation for records
		while (rowIterator.hasNext()) {

			row = (HSSFRow) rowIterator.next();
			int rowNum = row.getRowNum();
			String recordValue = "";

			
			cellIterator = row.cellIterator();
			cellno = 0;
			cellno = row.getPhysicalNumberOfCells();
			 while (cellIterator.hasNext()) {
			//for (int c = 0; c < cellno; c++) {

				cell = (HSSFCell) cellIterator.next();
				int cellType = cell.getCellType();
				log.info("cell number =============" + cell.getCellNum());
				recordFieldCount = cell.getCellNum();
				if (cellType == 0) {
					cellValue = cell.getNumericCellValue();
					decimal = new BigDecimal(cellValue);
					recordValue = decimal.toPlainString();

				}
				else if (cellType == 1) {
					recordValue = cell.getRichStringCellValue().getString();

				}
				else {
					recordValue = null;

				}

			}
			log.debug("recordFieldCount" + recordFieldCount);
			log.debug("headerFieldCount" + headerFieldCount);
                        log.debug("rowNum-"+rowNum);
			/* Following might be required if notes are to added for all the validations.
			  if (recordFieldCount > (headerFieldCount-1)) {
				log.debug("field value has no matching header value");
				// Send email
				if (processingErrorCount <= 5) {
					errorListForP.add(rowNum);
					errorMap1.put("P", errorListForP);
					processingErrorCount++;
					alertMsg = 0;

				}
				else {
					alertMsg = 1;
				}
				noteSubject = "Processing Error";
				noteText = "The number of record filed values is not matching with the number of header fields for catalog:"
					+ catalog.getVendorCatalogName()
					+ "for file:"
					+ fl.getName()
					+ "at record no:" + rowNum;

				noteTextList.add(noteText);
				isValidData = false;
			}*/
		}
		log.debug("Validation Complete...");
		/* End of validation loop */

		/* Processing Records */
		List nullRowsArray = new ArrayList();
		if (errorMap1.isEmpty()) {
			log.debug("error Map is empty.");
			Map<Short, String> map = new HashMap<Short, String>();
			Iterator rowIterator1 = sheet.rowIterator();
			if (rowIterator1.hasNext()) {

				row = (HSSFRow) rowIterator1.next();
				cellIterator = row.cellIterator();

				//
				while (cellIterator.hasNext()) {

					cell = (HSSFCell) cellIterator.next();

					String headerField = cell.getRichStringCellValue().toString();
					/* Get cell number for image related header names */

					if (headerField.equalsIgnoreCase(MAIN_IMAGE_NAME)) {
						map.put(cell.getCellNum(),MAIN_IMAGE_NAME);
						log.debug("Main Image added to Map:"+ cell.getCellNum());
					}
					if (headerField.equalsIgnoreCase(SWATCH_IMAGE_NAME)) {
						map.put(cell.getCellNum(),SWATCH_IMAGE_NAME);
						log.debug("Swatch Image added to Map:"+ cell.getCellNum());
					}
					if (headerField.equalsIgnoreCase(ALTERNATE_IMAGE_1)) {
						map.put(cell.getCellNum(),ALTERNATE_IMAGE_1);
						log.debug("Alt1 Image added to Map:"+ cell.getCellNum());
					}
					if (headerField.equalsIgnoreCase(ALTERNATE_IMAGE_2)) {
						map.put(cell.getCellNum(),ALTERNATE_IMAGE_2);
						
					}
					if (headerField.equalsIgnoreCase(ALTERNATE_IMAGE_3)) {
						map.put(cell.getCellNum(),ALTERNATE_IMAGE_3);
					}
					if (headerField.equalsIgnoreCase(ALTERNATE_IMAGE_4)) {
						map.put(cell.getCellNum(),ALTERNATE_IMAGE_4);
					}
					if (headerField.equalsIgnoreCase(ALTERNATE_IMAGE_5)) {
						map.put(cell.getCellNum(),ALTERNATE_IMAGE_5);
					}

					i = cell.getCellNum();
					// added if condition to ignore the empty headers
					// fixed as part of CARS dropship 2012
					if(!"".equals(headerField)) {
						catalogHeaderModel = new VendorCatalogHeader();
						
						listForRecords = new ArrayList<VendorCatalogRecord>();
						catalogHeaderModel.setVendorCatalogHeaderFieldName(headerField);
						catalogHeaderModel.setVendorCatalogID(catalog);
						catalogHeaderModel.setVendorCatalogFieldNum(new Long(i + 1));
						records.put(new Long(i), listForRecords);
						resultList.add(catalogHeaderModel);
					}
				}

			}
			String recordValue = "";
			int nullRowCount =0;
			int oldRowNum =1;
			int storedRowNum =1;
			// Read other records
			while (rowIterator1.hasNext()) {
				row = (HSSFRow) rowIterator1.next();
				int rowNum = row.getRowNum();
				log.debug("Processing row number.........."+rowNum);
				
				int nullRow = 1;
				int nullFieldCount =0;
				
				//new code to check row number sequence
				
				if(rowNum<=1){
					oldRowNum = rowNum;
					
					//oldRowNum  = rowNum1;
				}else{
					log.debug("expected row number"+(storedRowNum+1));
					
					if(rowNum!=(storedRowNum+1)){
						log.debug("next row number missing");
						
						storedRowNum = rowNum;
						rowNum  = oldRowNum+1;
						oldRowNum = rowNum;
						//rowNum = rowNum1;
					}else{
						log.debug("row number is in sequence");
						storedRowNum = rowNum;
						rowNum = oldRowNum+1;;
						oldRowNum = rowNum;
					}
					
				}
				
				//end new code
				for(short k=0;k<resultList.size();k++){
					cell= row.getCell(k);//cellIterator();

					short colNumber = 0;
					int cellType = 0;
					//cell = (HSSFCell) cellIterator.next();
					if(null !=cell){
						
					cellType = cell.getCellType();
					
					if (cellType == 0) {
						cellValue  = cell.getNumericCellValue();
						decimal = new BigDecimal(cellValue);
						recordValue = decimal.toPlainString();

					}
					else{
						recordValue = cell.getRichStringCellValue().getString();

					}
					
					colNumber = cell.getCellNum();//map.get(cell);
					cell = row.getCell(colNumber);
					/* Get image values for image names */
					if (map.containsKey(colNumber)) {
						log.info("Map containsvalue for header-related to images");
						
						// }
						if (null != cell) {
							log.info("if the cell has value with image name");
							imageList.add(recordValue);
						}
					}

					i = cell.getCellNum();
					
					catalogRecordModel = new VendorCatalogRecord();
					compositeKeyForVndrCatRecord = new CompositeKeyForVndrCatRecord();
					compositeKeyForVndrCatRecord.setCatalogId(catalog.getVendorCatalogID());
					compositeKeyForVndrCatRecord.setHeaderNum(new Long(k + 1));
					compositeKeyForVndrCatRecord.setRecordNumber(new Long(rowNum));
					catalogRecordModel.setCompositeKey(compositeKeyForVndrCatRecord);
					//If value is empty or contains space ,then inserting null.
					if(recordValue.equalsIgnoreCase(" ") || recordValue.equalsIgnoreCase("")){
						log.debug("if record value is null in if");
						nullFieldCount++;
						if(nullFieldCount>=resultList.size()){
							log.debug("Full row is empty-row nmm"+rowNum);
							//removed the check as per request by client
							//if(nullRowsArray.size()<5){
								nullRowsArray.add(new Long(rowNum));
							//	}
							nullRowCount++;
						}
						catalogRecordModel.setVendorCatalogFieldValue(null);
					}else{
						log.debug("recordValue not null in if"+recordValue);
						catalogRecordModel.setVendorCatalogFieldValue(recordValue);
					}
					/*if(nullRowCount>=5){
						break;
					}*/
					
					}
					else {
						log.debug("no cell defined");
						recordValue = null;
						catalogRecordModel = new VendorCatalogRecord();
						compositeKeyForVndrCatRecord = new CompositeKeyForVndrCatRecord();
						compositeKeyForVndrCatRecord.setCatalogId(catalog.getVendorCatalogID());
						compositeKeyForVndrCatRecord.setHeaderNum(new Long(k + 1));
						compositeKeyForVndrCatRecord.setRecordNumber(new Long(rowNum));
						catalogRecordModel.setCompositeKey(compositeKeyForVndrCatRecord);
						//If value is empty or contains space ,then inserting null.
						//if(recordValue.equalsIgnoreCase(" ") || recordValue.equalsIgnoreCase("")){
							catalogRecordModel.setVendorCatalogFieldValue(null);
							
						nullFieldCount++;
							if(nullFieldCount >= resultList.size()){
								log.debug("Full row is empty in else row num"+rowNum);
								//if(nullRowsArray.size()<5){
								nullRowsArray.add(new Long(rowNum));
								//}
								nullRowCount++;
								
								
							}
							
						//}
						
						//}
							/*if(nullRowCount>=5){
								break;
							}*/

					}
					//if (records.get(new Long(k)) != null) {
						//records.get(new Long(k)).add(catalogRecordModel);
						listForRecords = records.get(new Long(k));
						listForRecords.add(catalogRecordModel);
						log.debug("listForRecords size for header no"+listForRecords.size()+"header no"+k);
						records.put(new Long(k), listForRecords);
					//}
				}
				/*if(nullRowCount>=5){
					break;
				}*/
			}
			isValidData = true;
		}
		else {
			log.debug("error map is not empty");

			try {
				dropshipManager.saveVendorCatalogNote(catalog, noteSubject, noteTextList);
			}
			catch (ParseException e) {
				log.error("Exception while saving vendor catalog note"+e);
			}
			// Send Email
			try {
				dropshipManager.sendEmailNotification(catalog, sheet.getPhysicalNumberOfRows(),
						errorMap1, alertMsg);

			}
			catch (Exception e) {
				log.error("Exception while sending email"+e);
				success = true;
			}

		}
		if (isValidData) {
			log.debug("There were no validation errors");
			try {
				dropshipManager.deleteRecord(catalog.getVendorCatalogID());
			}
			catch (DataAccessException dae) {
				log.error("Exception while deleting records from header table" + dae);
			}
			try {

				dropshipManager.saveHeader(resultList);
				success = true;
			}
			catch (DataAccessException dae) {
				log.error("Exception while saving data to vendorHeader tabel" + dae);
				success = false;
			}
			log.debug("After saving header....");
			Set<Long> set = records.keySet();
			Iterator<Long> it = set.iterator();
			List<VendorCatalogRecord> list1 =new ArrayList<VendorCatalogRecord>();
			log.debug("nullRowsArray size"+nullRowsArray.size());
			while (it.hasNext()) {
			    log.debug("Record saving for next header");
			   
			    list1 = records.get(it.next());
			    
			    log.info("list1 size"+list1.size());
			    List<VendorCatalogRecord> list2 =new ArrayList<VendorCatalogRecord>();
			 for(int k=1;k<=list1.size();k++){
				
				   if(!nullRowsArray.contains(new Long(k))){
					   log.debug("k does not belong to null array"+(k));
				  list2.add(list1.get(k-1));
				   }
				 }
			 
			    dropshipManager.saveRecord(list2);
			    
			}
			try {
				dropshipManager.sendEmailNotification(catalog, sheet.getPhysicalNumberOfRows(),
						errorMap1, alertMsg);

			}
			catch (Exception e) {
				log.error("Exception while sending email"+e);
				success = true;
			}

			
			/*
			 * Get all the departments associated to vendor catalog and hence
			 * the car_user_ids who are web buyers and own these departments
			 */

			sendMailToWebBuyers(catalog);
			
			
		}
		else{
			log.debug("Data is not valid");
		}
		return imageList;
	}

	/**
	 * Method to read xml file
	 * 
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public List readXMLFile(File fl, VendorCatalog catalog)
	throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(fl);
		// List for storing image names
		List list = new ArrayList();
		errorMap1 = new HashMap<String, List>();
		List<VendorCatalogHeader> resultList = new ArrayList<VendorCatalogHeader>();
		List errorListForV = new ArrayList();
		List errorListForP = new ArrayList();
		boolean success = true;

		String noteSubject = "";
		String noteText = "";
		List noteTextList = new ArrayList();
		List recordValueList = new ArrayList();
		HashMap<String, List<VendorCatalogRecord>> headerValues = new HashMap<String, List<VendorCatalogRecord>>();
		doc.getDocumentElement().normalize();
		log.debug("reading xml file....");

		log.info("root element of xml is:" + doc.getDocumentElement().getNodeName());
		// For every node with tag name Product
		Node n = doc.getFirstChild();

		NodeList nl = n.getChildNodes();
		Node an;
		Node an2;
		int nodeCount = 0;
		String headerField = "";
		int processingErrorCount = 1;
		int validationErrorCount = 1;
		int alertMsg = 0;
		/* Validation check loop */
		for (int i = 0; i < nl.getLength(); i++) {

			an = nl.item(i);

			if (an.getNodeType() == Node.ELEMENT_NODE) {

				log.info("If node type is element of node" + an);
				headerFieldList = new HashMap<String, List>();
				nodeCount++;

				NodeList nl2 = an.getChildNodes();
				listForRecords = new ArrayList<VendorCatalogRecord>();

				for (int i2 = 1; i2 < nl2.getLength(); i2++) {

					an2 = nl2.item(i2);
					log.info("Inside tag values for node" + an + "Values" + an2.getNodeName());

					// Tag Name
					headerField = an2.getNodeName();
					if (!headerField.startsWith("#")) {
						if (headerFieldList.containsKey(headerField)) {
							log.debug("header name is not unique");
							if (validationErrorCount <= 5) {
								errorListForV.add(i2);
								errorMap1.put("V", errorListForV);
								validationErrorCount++;
								alertMsg = 0;
							}
							else {
								alertMsg = 1;
							}
							noteSubject = "Validation Error";
							noteText = "The header field values are not unique for catalog:"
								+ catalog.getVendorCatalogName() + "and file name:"
								+ fl.getName();
							noteTextList.add(noteText);
							isValidData = false;
						}
						else {
							log.debug("header name is unique");
							headerFieldList.put(headerField, errorList);
						}

					}

					// Tag value
					if ((an2.getNodeName() == null)
							&& (an2.getFirstChild().getTextContent() != null)) {

						log.debug("field value doesnt have tag name");
						if (processingErrorCount <= 5) {
							errorListForP.add(i2);

							errorMap1.put("P", errorListForP);
							processingErrorCount++;
							alertMsg = 0;
						}
						else {
							alertMsg = 1;
						}

						noteSubject = "Processing Error";
						noteText = "The number of record filed values is not matching with the number of header fields for catalog:"
							+ catalog.getVendorCatalogName()
							+ "for file:"
							+ fl.getName()
							+ "at record no:" + i2;

						noteTextList.add(noteText);
						isValidData = false;

					}

				}

			}
		}

		/* End of validation check loop */

		/* If error map is empty process records */
		if (errorMap1.isEmpty()) {
			log.debug("if errorMap is empty");

			NodeList mainImageName = null;
			NodeList swatchImageName = null;
			NodeList alternateImageName1 = null;
			NodeList alternateImageName2 = null;
			NodeList alternateImageName3 = null;
			NodeList alternateImageName4 = null;
			NodeList alternateImageName5 = null;
			for (int i = 0; i < nl.getLength(); i++) {

				an = nl.item(i);
				if (an.getNodeType() == Node.ELEMENT_NODE) {
					/* Read image names and save it in list */

					Element fstElmnt = (Element) an;
					// getting all image name nodes from xml
					NodeList mainImageNameList = fstElmnt.getElementsByTagName(MAIN_IMAGE_NAME);
					if (null != mainImageNameList) {
						Element mainImageElmnt = (Element) mainImageNameList.item(0);
						mainImageName = mainImageElmnt.getChildNodes();
					}
					NodeList swatchImageNameList = fstElmnt.getElementsByTagName(SWATCH_IMAGE_NAME);
					Element swatchNmElmnt = (Element) swatchImageNameList.item(0);
					if (null != swatchNmElmnt && !swatchNmElmnt.equals(BLANK)) {
						swatchImageName = swatchNmElmnt.getChildNodes();
					}
					NodeList alternateImageNameList1 = fstElmnt
					.getElementsByTagName(ALTERNATE_IMAGE_1);
					Element alternateNmElmnt1 = (Element) alternateImageNameList1.item(0);
					if (null != alternateNmElmnt1 && !alternateNmElmnt1.equals(BLANK)) {
						alternateImageName1 = alternateNmElmnt1.getChildNodes();
					}
					NodeList alternateImageNameList2 = fstElmnt
					.getElementsByTagName(ALTERNATE_IMAGE_2);
					Element alternateNmElmnt2 = (Element) alternateImageNameList2.item(0);
					if (null != alternateNmElmnt2 && !alternateNmElmnt2.equals(BLANK)) {
						alternateImageName2 = alternateNmElmnt2.getChildNodes();
					}
					NodeList alternateImageNameList3 = fstElmnt
					.getElementsByTagName(ALTERNATE_IMAGE_3);
					Element alternateNmElmnt3 = (Element) alternateImageNameList3.item(0);
					if (null != alternateNmElmnt3 && !alternateNmElmnt3.equals(BLANK)) {
						alternateImageName3 = alternateNmElmnt3.getChildNodes();
					}
					NodeList alternateImageNameList4 = fstElmnt
					.getElementsByTagName(ALTERNATE_IMAGE_4);
					Element alternateNmElmnt4 = (Element) alternateImageNameList4.item(0);
					if (null != alternateNmElmnt4 && !alternateNmElmnt4.equals(BLANK)) {
						alternateImageName4 = alternateNmElmnt4.getChildNodes();
					}
					NodeList alternateImageNameList5 = fstElmnt
					.getElementsByTagName(ALTERNATE_IMAGE_5);
					Element alternateNmElmnt5 = (Element) alternateImageNameList5.item(0);
					if (null != alternateNmElmnt5 && !alternateNmElmnt5.equals(BLANK)) {
						alternateImageName5 = alternateNmElmnt5.getChildNodes();
					}
					// check if main image name is not empty then add it to
					// list
					if (null != mainImageName && !mainImageName.equals(BLANK)) {
						list.add(((Node) mainImageName.item(0)).getNodeValue());
					}
					// check if swatch image name is not empty then add it
					// to list
					if (null != swatchImageName && !swatchImageName.equals(BLANK)) {
						list.add(((Node) swatchImageName.item(0)).getNodeValue());
					}
					// check if alternate image1 name is not empty then add
					// it to list
					if (null != alternateImageName1 && !alternateImageName1.equals(BLANK)) {
						list.add(((Node) alternateImageName1.item(0)).getNodeValue());
					}
					// check if alternate image2 name is not empty then add
					// it to list
					if (null != alternateImageName2 && !alternateImageName2.equals(BLANK)) {
						list.add(((Node) alternateImageName2.item(0)).getNodeValue());
					}
					// check if alternate image3 name is not empty then add
					// it to list
					if (null != alternateImageName3 && !alternateImageName3.equals(BLANK)) {
						list.add(((Node) alternateImageName3.item(0)).getNodeValue());
					}
					// check if alternate image4 name is not empty then add
					// it to list
					if (null != alternateImageName4 && !alternateImageName4.equals(BLANK)) {
						list.add(((Node) alternateImageName4.item(0)).getNodeValue());
					}
					// check if alternate image5 name is not empty then add
					// it to list
					if (null != alternateImageName5 && !alternateImageName5.equals(BLANK)) {
						list.add(((Node) alternateImageName5.item(0)).getNodeValue());
					}

					/* End saving image names */
					int count = 0;

					log.debug("If node type is element of node" + an);
					NodeList nl2 = an.getChildNodes();
					for (int i2 = 1; i2 < nl2.getLength(); i2++) {
						String value = "";
						an2 = nl2.item(i2);
						log.debug("Inside tag values for node" + an + "Values" + an2.getNodeName());

						// Tag Name
						headerField = an2.getNodeName();

						log.debug("Inside for loop for header....in processing loop");
						if (!headerField.startsWith("#")) {
							count++;
							if (!headerValues.containsKey(headerField)) {
								log.debug("new header field.--------------");
								catalogHeaderModel = new VendorCatalogHeader();

								listForRecords = new ArrayList<VendorCatalogRecord>();
								catalogHeaderModel.setVendorCatalogHeaderFieldName(headerField);
								catalogHeaderModel.setVendorCatalogID(catalog);
								catalogHeaderModel.setVendorCatalogFieldNum(new Long(count));
								resultList.add(catalogHeaderModel);

								value = an2.getTextContent();

								catalogRecordModel = new VendorCatalogRecord();
								compositeKeyForVndrCatRecord = new CompositeKeyForVndrCatRecord();
								compositeKeyForVndrCatRecord.setCatalogId(catalog
										.getVendorCatalogID());

								compositeKeyForVndrCatRecord.setHeaderNum(new Long(count));
								compositeKeyForVndrCatRecord.setRecordNumber(new Long(i));

								catalogRecordModel.setCompositeKey(compositeKeyForVndrCatRecord);
								
								//If value is empty or contains space ,then inserting null.
								if(value.equalsIgnoreCase(" ") || value.equalsIgnoreCase("")){
									catalogRecordModel.setVendorCatalogFieldValue(null);
								}else{
									catalogRecordModel.setVendorCatalogFieldValue(value);
								}
								//catalogRecordModel.setVendorCatalogFieldValue(value);
								recordValueList.add(catalogRecordModel);

								headerValues.put(headerField, recordValueList);

							}
							else {
								value = an2.getTextContent();

								catalogRecordModel = new VendorCatalogRecord();
								compositeKeyForVndrCatRecord = new CompositeKeyForVndrCatRecord();
								compositeKeyForVndrCatRecord.setCatalogId(catalog
										.getVendorCatalogID());
								compositeKeyForVndrCatRecord.setHeaderNum(new Long(count));
								compositeKeyForVndrCatRecord.setRecordNumber(new Long(i));

								catalogRecordModel.setCompositeKey(compositeKeyForVndrCatRecord);
								//If value is empty or contains space ,then inserting null.
								if(value.equalsIgnoreCase(" ") || value.equalsIgnoreCase("")){
									catalogRecordModel.setVendorCatalogFieldValue(null);
								}else{
									catalogRecordModel.setVendorCatalogFieldValue(value);
								}
								//catalogRecordModel.setVendorCatalogFieldValue(value);
								recordValueList = headerValues.get(headerField);
								recordValueList.add(catalogRecordModel);

								headerValues.put(headerField, recordValueList);
							}
						}

					}// End of for loop

				}

			}
			isValidData = true;
		}
		else {
			// if ((errorListForP.size() + errorListForV.size()) < 5) {

			try {
				dropshipManager.saveVendorCatalogNote(catalog, noteSubject, noteTextList);
			}
			catch (ParseException e) {
				log.error("Exception while saving vendor catalog note"+e);
			}
			// Send Email
			try {
				dropshipManager.sendEmailNotification(catalog, nodeCount, errorMap1, alertMsg);

			}
			catch (Exception e) {
				e.printStackTrace();
				success = true;
			}

			isValidData = false;
		}// End of error map not empty

		if (isValidData) {
			log.debug("There were no validation errors");

			dropshipManager.saveHeader(resultList);

			dropshipManager.deleteRecord(catalog.getVendorCatalogID());
			Set<String> set = headerValues.keySet();
			Iterator<String> it = set.iterator();
			while (it.hasNext()) {

				dropshipManager.saveRecord(headerValues.get(it.next()));
			}
			try {
				dropshipManager.sendEmailNotification(catalog, nodeCount, errorMap1, alertMsg);
			}
			catch (Exception e) {
				e.printStackTrace();
			}


			sendMailToWebBuyers(catalog);
		}

		return list;
	}

	/**
	 * This method copies image files from source ftp location to destination ftp location
	 * @param String	destinationFtp
	 * @param String	sourceFtp
	 * @param String	ftpUname
	 * @param String	ftpPwd
	 * @return boolean 
	 */
	public boolean methodInvokingImportFTPImages(Long catalogId,String destinationDirectory,
			String sourceFtp, String ftpUname, String ftpPwd){
		try{
			log.info("vendor FTP option selected and the destination dir for images is:"+destinationDirectory);

			FTPClient ftpSource = new FTPClient();
			FTPClient ftpDestination =  new FTPClient();
			String sourceFtpHost = "";
			String sourceIP = "";
			FileOutputStream fos;
			List imageList = new ArrayList();

			//check FTPClient for source
			if (sourceFtp == null) {
				log.debug("Ftp Connection is not established or invalid since sourceFtp is null.");
				return false;
			}else{
				StringTokenizer st = null;
				st = new StringTokenizer(sourceFtp,SLASH);
				st.nextToken();
				if(st.hasMoreTokens())
					sourceIP=  st.nextToken();

				log.info("Source IP"+sourceIP);
				while (st.hasMoreTokens()) {

					try{
						sourceFtpHost = st.nextToken();
					}catch(Exception ex){
						log.error("Exception while parsing source ftp"+ex);
					}
				}
				log.info("sourceFtpHost entered..."+sourceFtpHost);
			}
			/** FTP credentials for destination */
			if(sourceIP != null && StringUtils.isNotBlank(sourceIP)) {
				try{
					ftpSource = FtpUtil.openConnection(sourceIP, ftpUname, ftpPwd);
					log.debug("Connection opened...........");
				}catch(IOException io){
					log.error(io);
				}

				log.info("Current Working Directory (Before): " + ftpSource.printWorkingDirectory());
				ftpSource.changeWorkingDirectory(sourceFtpHost);
				log.info("changed directory...."+ftpSource.printWorkingDirectory());
				//extensions for image type

				/*List<String> extensionOfFiles = new ArrayList<String>();
				extensionOfFiles.add("jpg");
				extensionOfFiles.add("jpeg");
				extensionOfFiles.add("jpe");
				extensionOfFiles.add("jfif");
				extensionOfFiles.add("bmp");
				extensionOfFiles.add("dib");
				extensionOfFiles.add("tif");
				extensionOfFiles.add("tiff");
				extensionOfFiles.add("psd");
				extensionOfFiles.add("gif");
				extensionOfFiles.add("png");*/
				FTPFile[] files = ftpSource.listFiles(); //listing images files present at source location
				//FtpUtil.closeConnection(ftpSource);
				FTPFile[] imageFiles = FtpUtil.getImageFilesFromFTP(files);
				if(imageFiles != null){
					/** copying images from source to destination */
					for (int i = 0; i < imageFiles.length; i++) {

						FileInputStream fis = null;
						/**Added for filter functionality */
						log.info("file at ftp...."+imageFiles[i].getName());
						//File file = new File(files[i].getName());

						//Code to copy images from ftp to local machine
						//File type 2--Image file
						ftpSource.setFileType(2);
						File file = new File( destinationDirectory+"/"+ imageFiles[ i ].getName() );
						try{
							fos = new FileOutputStream( file ); 

							ftpSource.retrieveFile( imageFiles[ i ].getName(), fos );

							fos.close();
						}catch(IOException iex){
							log.error("Exception while moving file from ftp to local"+iex);
						}
						imageList.add(imageFiles[ i ].getName());
					}
					//Save the same in VendorCatalogImage Table
					try {
						dropshipManager.saveImageData(imageList,catalogId);
						log.info("Stored and saved successfully.................");
					}
					catch (ParseException e) {
						log.error("Exception while saving image data in table"+e);
					}
					finally{
						FtpUtil.closeConnection(ftpDestination);
						FtpUtil.closeConnection(ftpSource);
					}
				}

			}

		}catch(ParserInitializationException pie){
			log.error("Ftp Connection is not established or invalid."+pie);
			return false;
		}
		catch (IOException ex) {
			//Logger.getLogger(FtpUtil.class.getName()).log(Level.SEVERE, null, ex);
			log.error("Ftp Connection is not established or invalid."+ex);
			return false;
		}

		return true;
	}

	

	/**
	 * Method to convert values in set to array of comma seperated values
	 */
	@SuppressWarnings("unchecked")
	public String readValuesFromList(Set set) {
		if (set.size() < 0 || set.isEmpty()) {
			return null; // Depending on how you want to deal with this case...
		}
		StringBuilder result = new StringBuilder();
		java.util.Iterator i = set.iterator();
		result.append(i.next());
		while (i.hasNext()) {
			result.append(",").append(i.next().toString());
		}
		return result.toString();
	}

	/**
	 * Method to Get all the departments associated to vendor catalog and hence
	 * the car_user_ids who are web buyers and own these departments
	 */
	public void sendMailToWebBuyers(VendorCatalog catalog){
		List emailIDs = new ArrayList();
		/*
		 * Change the status of catalog file to Data Mapping in vendor
		 * catalog table
		 */
		try {


			dropshipManager.changeStatusOfCatalogFile(catalog);
			log.debug("Status changed:" + catalog.getStatusCD());
		}
		catch (DataAccessException dae) {
			log.error("Exception while changing status of file to Data Mapping" + dae);
		}
		try {
			emailIDs = dropshipManager.getBuyerMailIDs(catalog.getVendorCatalogID());
		}
		catch (DataAccessException dae) {
			log
			.error("Exception while executing the query to get mail IDs of web buyers associated to catalogs departments"
					+ dae);
		}

		log.info("emailIDs" + emailIDs);

		try {
			dropshipManager.sendEmailForCatalogDataMapping(catalog, emailIDs);
		}
		catch (SendEmailException ex) {
			log
			.error("Exception while sending email for new catalog with vendor name ready for data mapping"
					+ ex);
		}
		
	}

}
/**
 * 
 */
package com.belk.car.app.util;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.service.DropshipManager;

/**
 * Purpose : This class creates a Car by reading a Cross reference feed file.
 * If  Belk UPC exists in CARS, then it creates a new CAR for the 
 * and populate any non IDB provided data from the previous CAR request and route to Buyer.  
 * Make a note in that CAR that initiated due to Cross Reference from Belk UPC and indicate the  UPC
 * 
 * Initial Project: EC-2009-00092 - FY 10 eCommerce Drop-Ship Phase I
 * 
 * Initial Requirements: BR.026
 * 
 * @author afusy01
 * 
 */
public class ReadIDBCrossReferenceFeedFileForCarCreation implements DropShipConstants {
	
	private DropshipManager dropshipManager;
	
	//Set start and end limit for each field 
	public static final int STARTNEWBELKUPC = 0;
	public static final int ENDNEWBELKUPC = STARTNEWBELKUPC + 13;
	
	public static final int STARTOLDBELKUPC = ENDNEWBELKUPC;
	public static final int ENDOLDBELKUPC = STARTOLDBELKUPC + 13;
	
	public static final int STARTVNDRSTYLENUMFROMNEWBELKUPC = ENDOLDBELKUPC;
	public static final int ENDVNDRSTYLENUMFROMNEWBELKUPC = STARTVNDRSTYLENUMFROMNEWBELKUPC + 20;
	
	public static final int STARTVNDRNBR = ENDVNDRSTYLENUMFROMNEWBELKUPC;
	public static final int ENDVNDRNBR = STARTVNDRNBR + 7;

	public static final int STARTVNDRNAME = ENDVNDRNBR;
	public static final int ENDVNDRNAME = STARTVNDRNAME + 30;

	public static final int STARTDPTNBR = ENDVNDRNAME;
	public static final int ENDDPTNBR = STARTDPTNBR + 3;

	public static final int STARTDEPTNAME = ENDDPTNBR;
	public static final int ENDDEPTNAME = STARTDEPTNAME + 25;

	public static final int STARTCLASSNBR = ENDDEPTNAME;
	public static final int ENDCLASSNBR = STARTCLASSNBR + 4;

	public static final int STARTCLASSNAME = ENDCLASSNBR;
	public static final int ENDCLASSNAME = STARTCLASSNAME + 30;

	public static final int STARTVNDRSTYLE = ENDCLASSNAME;
	public static final int ENDVNDRSTYLE = STARTVNDRSTYLE + 20;

	public static final int STARTSTYLEDESC = ENDVNDRSTYLE;
	public static final int ENDSTYLEDESC = STARTSTYLEDESC + 20;

	public static final int STARTVNDRCOLOR = ENDSTYLEDESC;
	public static final int ENDVNDRCOLOR = STARTVNDRCOLOR + 3;

	public static final int STARTVNDRCOLORNAME = ENDVNDRCOLOR;
	public static final int ENDVNDRCOLORNAME = STARTVNDRCOLORNAME + 10;

	public static final int STARTVNDRSZCODE = ENDVNDRCOLORNAME;
	public static final int ENDVNDRSZCODE = STARTVNDRSZCODE + 5;

	public static final int STARTVNDRSZDESC = ENDVNDRSZCODE;
	public static final int ENDVNDRSZDESC = STARTVNDRSZDESC + 20;

	public static final int STARTVENDORUPC = ENDVNDRSZDESC;
	public static final int ENDVENDORUPC = STARTVENDORUPC + 13;

	public static final int STARTUPCADDDATE = ENDVENDORUPC;
	public static final int ENDUPCADDDATE = STARTUPCADDDATE + 10;

	public static final int STARTIDBUPCUNITPRICE = ENDUPCADDDATE;
	public static final int ENDIDBUPCUNITPRICE = STARTIDBUPCUNITPRICE + 11;

	public static final int STARTIDBUPCUNITCOST = ENDIDBUPCUNITPRICE;
	public static final int ENDIDBUPCUNITCOST = STARTIDBUPCUNITCOST + 11;

	public static final int STARTSZCHARTCODE = ENDIDBUPCUNITCOST;
	public static final int ENDSZCHARTCODE = STARTSZCHARTCODE + 3;

	public static final int STARTBELKUPC = ENDSZCHARTCODE;
	public static final int ENDBELKUPC = STARTBELKUPC + 13;

	public static final int STARTSTYLEDRPSHIPFLG = ENDBELKUPC;
	public static final int ENDSTYLEDRPSHIPFLG = STARTSTYLEDRPSHIPFLG + 1;

	public static final int STARTSKUDRPSHIPFLG = ENDSTYLEDRPSHIPFLG;
	public static final int ENDSKUDRPSHIPFLG = STARTSKUDRPSHIPFLG + 1;

	public static final int STARTDATEFLGUPDATED = ENDSKUDRPSHIPFLG;
	public static final int ENDDATEFLGUPDATED = STARTDATEFLGUPDATED + 26;

	public static final int STARTNOTORDERABLEFLG = ENDDATEFLGUPDATED;
	public static final int ENDNOTORDERABLEFLG = STARTNOTORDERABLEFLG + 1;

	public static final int STARTUPCDISCFLG = ENDNOTORDERABLEFLG;
	public static final int ENDUPCDISCFLG = STARTUPCDISCFLG + 1;

	public static final int STARTUPCDISCDATE = ENDUPCDISCFLG;
	public static final int ENDUPCDISCDATE = STARTUPCDISCDATE + 10;

	
	private static transient final Log log = LogFactory
			.getLog(ReadIDBCrossReferenceFeedFileForCarCreation.class);


	/**
	 * @param dropshipManager the dropshipManager to set
	 */
	public void setDropshipManager(DropshipManager dropshipManager) {
		this.dropshipManager = dropshipManager;
	}

	/**
	 * @return the dropshipManager
	 */
	public DropshipManager getDropshipManager() {
		return dropshipManager;
	}

	/**
	 * Method to process Cross Reference Feed for car creation
	 * @param fl
	 * @return List
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void processReferenceFeed(File fl) {
		
		try {
			// Reads line by line into List
			List<String> list = FileUtils.readLines(fl, UTF_8);

			Iterator<String> iterator = list.iterator();

			while (iterator.hasNext()) {
				String str = iterator.next();

				// Check if field is empty or has less than 5 characters then
				// skip the current row
				if (str == null || str.length() < 5) {
					log.debug("skip the current row....");
					continue;
				}

				// Read the values of each row into a string
				String oldBelkUPC = str.substring(STARTOLDBELKUPC,ENDOLDBELKUPC).trim();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("toupc", str.substring(STARTNEWBELKUPC,ENDNEWBELKUPC).trim());
				map.put("fromupc", oldBelkUPC);
				map.put("newstyleid", str.substring(STARTVNDRSTYLENUMFROMNEWBELKUPC,ENDVNDRSTYLENUMFROMNEWBELKUPC).trim());
				map.put("vendorno", str.substring(STARTVNDRNBR, ENDVNDRNBR).trim());
				map.put("vendorname", str.substring(STARTVNDRNAME, ENDVNDRNAME).trim());
				map.put("deptno", str.substring(STARTDPTNBR, ENDDPTNBR).trim());
				map.put("deptname", str.substring(STARTDEPTNAME, ENDDEPTNAME).trim());
				map.put("classno", str.substring(STARTCLASSNBR, ENDCLASSNBR).trim());
				map.put("classname", str.substring(STARTCLASSNAME, ENDCLASSNAME).trim());
				map.put("oldstyleid", str.substring(STARTVNDRSTYLE, ENDVNDRSTYLE).trim());
				map.put("styledesc", str.substring(STARTSTYLEDESC, ENDSTYLEDESC).trim());
				map.put("color", str.substring(STARTVNDRCOLOR, ENDVNDRCOLOR).trim());
				map.put("colorname", str.substring(STARTVNDRCOLORNAME,ENDVNDRCOLORNAME).trim());
				map.put("sizecode", str.substring(STARTVNDRSZCODE,ENDVNDRSZCODE).trim());
				map.put("sizedesc", str.substring(STARTVNDRSZDESC,ENDVNDRSZDESC).trim());
				map.put("vendorupc", str.substring(STARTVENDORUPC, ENDVENDORUPC).trim());

				Object vendorSku = this.dropshipManager
						.checkUpcExists(oldBelkUPC);
				if(null != vendorSku) {
					this.dropshipManager.createCarForCrossReferenceFeed(vendorSku, map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

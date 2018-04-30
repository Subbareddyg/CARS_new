package com.belk.car.app.util;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.belk.car.app.to.IdbCarDataTO;
import com.belk.car.app.to.IdbCarSkuTO;

public class ReadIDBFeedForDropship {
	public static final int STARTVNDRNBR = 0;
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

	public static final int STARTIDBSTYLEUNITPRICE = ENDIDBUPCUNITCOST;
	public static final int ENDIDBSTYLEUNITPRICE = STARTIDBSTYLEUNITPRICE + 11;

	public static final int STARTIDBSTYLEUNITCOST = ENDIDBSTYLEUNITPRICE;
	public static final int ENDIDBSTYLEUNITCOST = STARTIDBSTYLEUNITCOST + 11;

	public static final int STARTSZCHARTCODE = ENDIDBSTYLEUNITCOST;
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

	public static final int STARTSOURCEOFCODE = ENDUPCDISCDATE;
	public static final int ENDSOURCEOFCODE = STARTSOURCEOFCODE + 8;

	public static final int START_FILLER = ENDSOURCEOFCODE;
	public static final int END_FILLER = START_FILLER + 16;

	private static transient final Log log = LogFactory.getLog(ReadIDBFeedForDropship.class);

	public static Collection<IdbCarDataTO> process(File fl)
			throws IOException {
		if (log.isInfoEnabled())
			log.info("Opened Path");

		List list = FileUtils.readLines(fl, "UTF-8");
		HashMap<String, IdbCarDataTO> data = new HashMap<String, IdbCarDataTO>();
		java.util.Iterator it = list.iterator();

		nextRow:
		while (it.hasNext()) {
			String str = (String) it.next();
			if (str == null || str.length() < 5 )
				break nextRow ; 

                        String vendorNumber = str.substring(STARTVNDRNBR, ENDVNDRNBR).trim();
				String vendorName = str.substring(STARTVNDRNAME, ENDVNDRNAME).trim();

				String deptNbr = str.substring(STARTDPTNBR, ENDDPTNBR).trim();
				String deptName = str.substring(STARTDEPTNAME, ENDDEPTNAME).trim();
				String classNumber = str.substring(STARTCLASSNBR, ENDCLASSNBR).trim();
				String className = str.substring(STARTCLASSNAME, ENDCLASSNAME).trim();
				String vendorStyle = str.substring(STARTVNDRSTYLE, ENDVNDRSTYLE).trim();
				String vendorStyleDesc = str.substring(STARTSTYLEDESC, ENDSTYLEDESC).trim();
				String vendorColor = str.substring(STARTVNDRCOLOR, ENDVNDRCOLOR).trim();

				String vendorColorName = str.substring(STARTVNDRCOLORNAME, ENDVNDRCOLORNAME).trim();

				String vendorSizeCode = str.substring(STARTVNDRSZCODE, ENDVNDRSZCODE).trim();

				String vendorSizeDesc = str.substring(STARTVNDRSZDESC, ENDVNDRSZDESC).trim();

				String vendorUPC = str.substring(STARTVENDORUPC, ENDVENDORUPC).trim();

				String upcAddDate = str.substring(STARTUPCADDDATE, ENDUPCADDDATE).trim();





			String validItemFlag = "Y";
			String poNumber = null;

			String productCode = vendorNumber + "-" + vendorStyle;
			IdbCarDataTO idb = data.get(productCode);
			if (idb == null) {
				idb = new IdbCarDataTO();
				idb.setClassName(className);
				idb.setClassNumber(classNumber);
				idb.setDepartmentName(deptName);
				idb.setDepartmentNumber(deptNbr);
				idb.setVendorName(vendorName);
				idb.setVendorNumber(vendorNumber);
				idb.setVendorStyle(vendorStyle);
				idb.setVendorStyleDescription(vendorStyleDesc);
				idb.setPoNumber(poNumber);
				idb.setValidItemFlag(validItemFlag);
				data.put(productCode, idb);
			}

			

			String belkUpc = str.substring(STARTBELKUPC, ENDBELKUPC).trim();

			if ("Y".equals(validItemFlag) && !idb.containsSku(belkUpc)) {
				IdbCarSkuTO skuTo = new IdbCarSkuTO();
				skuTo.setBelkUPC(belkUpc);
				skuTo.setUpcAddDate(upcAddDate);
				skuTo.setVendorColor(vendorColor);
				skuTo.setVendorColorName(vendorColorName);
				skuTo.setVendorSizeCode(vendorSizeCode);
				skuTo.setVendorSizeDesc(vendorSizeDesc);
				skuTo.setLongSku(vendorUPC);
				skuTo.setSetFlag("N");
				
				idb.addSku(skuTo);
			}
		}
		
		validateVendorNumber(data);
		return data.values();
	}
	
	/**
	 * validateVendorNumber - validates that vendorNumber is Integer not alphanumeric
	 * @param data
	 */
	private static void  validateVendorNumber(HashMap<String, IdbCarDataTO> data) {
		String vendorNumber =null;
		int i,len;
		Set<String> prd_cd_set = new HashSet<String>(data.keySet());
		String productCode;
		String regExp = "";
		String[] prdVen;
		java.util.Iterator it2 = prd_cd_set.iterator();
		log.debug("Total number of Imported product codes are : "+data.size());
		while(it2.hasNext())
		{
			
			regExp = "";
			productCode = (String)it2.next();
			prdVen = productCode.split("-");							
			if (prdVen.length == 2) {				
				vendorNumber = prdVen[0];
				len = vendorNumber.length();
				if (len != 0) {
					for (i = 0; i < len; i++) {
					regExp = regExp.concat("[0-9]");
					}					
					Pattern p = Pattern.compile(regExp);
					Matcher m = p.matcher(vendorNumber);
					if (m.matches() == false) {
						log.debug("Incorrect vendor number:"+ vendorNumber +" :vendor number is Alphanumeric ");
						data.remove(productCode);
					}										
				 } else {
					 log.debug("Incorrect vendor number:"+ vendorNumber +" :Length is wrong");
					data.remove(productCode);
				 }
		    } else {
		    	log.debug("Vendor Number Missing");
			    data.remove(productCode);
		    }
	     }
		log.debug("Total number of valid product codes are : "+data.size());
	 }
}

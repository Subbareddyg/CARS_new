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
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.belk.car.app.to.IdbCarDataTO;
import com.belk.car.app.to.IdbCarSkuTO;

public class ReadIDBFile {
	public static final int STARTVALIDITEMFLAG = 0;
	public static final int ENDVALIDITEMFLAG = STARTVALIDITEMFLAG + 1;

	public static final int STARTVNDRNBR = ENDVALIDITEMFLAG;
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
	
	public static final int STARTSTYLEDESC = ENDVNDRSTYLE ;
	public static final int ENDSTYLEDESC = STARTSTYLEDESC + 50;
	
	public static final int STARTVNDRCOLOR = ENDSTYLEDESC;
	public static final int ENDVNDRCOLOR = STARTVNDRCOLOR + 3;

	public static final int STARTVNDRCOLORNAME = ENDVNDRCOLOR;
	public static final int ENDVNDRCOLORNAME = STARTVNDRCOLORNAME + 10;

	public static final int STARTVNDRSZCODE = ENDVNDRCOLORNAME;
	public static final int ENDVNDRSZCODE = STARTVNDRSZCODE + 5;
	
	public static final int STARTVNDRSZDESC = ENDVNDRSZCODE ;
	public static final int ENDVNDRSZDESC = STARTVNDRSZDESC + 20;

	public static final int STARTUPC = ENDVNDRSZDESC;
	public static final int ENDUPC = STARTUPC + 13;

	public static final int STARTUPCADDDATE = ENDUPC;
	public static final int ENDUPCADDDATE = STARTUPCADDDATE + 10;
	
	public static final int STARTPRICE = ENDUPCADDDATE;
	public static final int ENDPRICE = STARTPRICE + 10;
	
	public static final int STARTFLAG01 = ENDPRICE;
	public static final int ENDFLAG01 = STARTFLAG01 + 1;
	
	public static final int STARTFLAG02 = ENDFLAG01;
	public static final int ENDFLAG02 = STARTFLAG02 + 1;
	
	public static final int STARTSZCHARTCODE = ENDFLAG02 ;
	public static final int ENDSTRTSZCHARTCODE = STARTSZCHARTCODE + 3;
	
	public static final int STARTBELKUPC = ENDSTRTSZCHARTCODE;
	public static final int ENDBELKUPC = STARTBELKUPC + 13;
	
	public static final int STARTPONBR = ENDBELKUPC;
	public static final int ENDPONBR = STARTPONBR + 9;
	
	public static final int STARTSTARTDELIVERYDATE = ENDPONBR;
	public static final int ENDSTARTDELIVERYDATE = STARTSTARTDELIVERYDATE + 10;
	
	public static final int STARTENDDELIVERYDATE = ENDSTARTDELIVERYDATE;
	public static final int ENDENDDELIVERYDATE = STARTENDDELIVERYDATE + 10;
	
	public static final int STARTSETPARENTUPC = ENDENDDELIVERYDATE;
	public static final int ENDSETPARENTUPC = STARTSETPARENTUPC + 13;
	
	//Possible Values: S=Set, C=Component
	public static final int STARTSETIND = ENDSETPARENTUPC;
	public static final int ENDSETIND = STARTSETIND + 1;
	
	public static final int STARTCARID = ENDSETIND;
	public static final int ENDCARID = STARTCARID + 12;
	
	public static final int STARTREPLENISHMENT = ENDCARID;
	public static final int ENDREPLENISHMENT = STARTREPLENISHMENT+ 1;

	public static final int START_DEMAND_CENTER = ENDREPLENISHMENT;
	public static final int END_DEMAND_CENTER = START_DEMAND_CENTER+ 2;

	public static final int START_RECORD_ID = END_DEMAND_CENTER;
	public static final int END_RECORD_ID = START_RECORD_ID+ 2;

	public static final int START_LONG_DESC = END_RECORD_ID;
	public static final int END_LONG_DESC = START_LONG_DESC + 200;

	public static final int START_DWT = END_LONG_DESC ;
	public static final int END_DWT = START_DWT + 10;

	public static final int START_CTW = END_DWT ;
	public static final int END_CTW = START_CTW + 10;

	public static final int START_DTW = END_CTW ;
	public static final int END_DTW = START_DTW + 10;

	public static final int START_GRAMS = END_DTW ;
	public static final int END_GRAMS = START_GRAMS + 10;

	public static final int START_COUNTRY = END_GRAMS ;
	public static final int END_COUNTRY = START_COUNTRY + 20;

	public static final int START_FILLER = END_COUNTRY ;
	public static final int END_FILLER = START_FILLER + 12;

	public static final int START_EOR = END_FILLER ;
	public static final int END_EOR = START_EOR + 1;
	
	private static transient final Log log = LogFactory
			.getLog(ReadIDBFile.class);

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

			String validItemFlag = str.substring(STARTVALIDITEMFLAG, ENDVALIDITEMFLAG);
			String vendorNumber = str.substring(STARTVNDRNBR, ENDVNDRNBR)
					.trim();
			String vendorName = str.substring(STARTVNDRNAME, ENDVNDRNAME)
					.trim();
			String deptNbr = str.substring(STARTDPTNBR, ENDDPTNBR).trim();
			String deptName = str.substring(STARTDEPTNAME, ENDDEPTNAME).trim();
			String classNumber = str.substring(STARTCLASSNBR, ENDCLASSNBR)
					.trim();
			String className = str.substring(STARTCLASSNAME, ENDCLASSNAME)
					.trim();
			String vendorStyle = str.substring(STARTVNDRSTYLE, ENDVNDRSTYLE)
					.trim();
			String vendorStyleDesc = str
					.substring(STARTSTYLEDESC, ENDSTYLEDESC).trim();
			String poNumber = str.substring(STARTPONBR, ENDPONBR).trim();

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
				idb.setExpectedShipDate(str.substring(
						STARTSTARTDELIVERYDATE, ENDSTARTDELIVERYDATE).trim()) ;
				idb.setDueDate(str.substring(
						STARTSTARTDELIVERYDATE, ENDSTARTDELIVERYDATE).trim());
				idb.setValidItemFlag(validItemFlag);
				/*if ("N".equals(validItemFlag)) {
					if (str.length() > 284) {
						idb.setManualCarId(str.substring(STARTCARID, ENDCARID).trim());
					} else {
						if (str.length() > 272) {
							idb.setManualCarId(str.substring(STARTCARID, str.length()).trim());
						}
					}
				} else {
					idb.setManualCarId(str.substring(STARTCARID, ENDCARID).trim());
				}*/
				data.put(productCode, idb);
			}

			//Set the PO Number to the LAST PO Number
			if (StringUtils.isNotBlank(poNumber) && !StringUtils.equals(poNumber, idb.getPoNumber())) {
				idb.setPoNumber(poNumber);
				idb.setExpectedShipDate(str.substring(STARTSTARTDELIVERYDATE, ENDSTARTDELIVERYDATE).trim());
				idb.setDueDate(str.substring(STARTSTARTDELIVERYDATE, ENDSTARTDELIVERYDATE).trim());
			}
			
			// Set the Manual Car... even there are other PO..
			//Manual car should override PO..
			if (str.length() > 284) {
				idb.setManualCarId(str.substring(STARTCARID, ENDCARID).trim());
			} else {
				if (str.length() > 272) {
					idb.setManualCarId(str.substring(STARTCARID, str.length()).trim());
				}
			}

			String belkUpc = str.substring(STARTBELKUPC, ENDBELKUPC).trim();

			if ("Y".equals(validItemFlag) && !idb.containsSku(belkUpc)) {
				IdbCarSkuTO skuTo = new IdbCarSkuTO();
				skuTo.setBelkUPC(belkUpc);
				skuTo.setEndDeliveryDate(str.substring(STARTENDDELIVERYDATE,
						ENDENDDELIVERYDATE).trim());
				skuTo.setStartDeliveryDate(str.substring(
						STARTSTARTDELIVERYDATE, ENDSTARTDELIVERYDATE).trim());
				skuTo.setUpcAddDate(str.substring(STARTUPCADDDATE,
						ENDUPCADDDATE).trim());
				skuTo.setVendorColor(str
						.substring(STARTVNDRCOLOR, ENDVNDRCOLOR).trim());
				skuTo.setVendorColorName(str.substring(STARTVNDRCOLORNAME,
						ENDVNDRCOLORNAME).trim());
				skuTo.setVendorSizeCode(str.substring(STARTVNDRSZCODE,
						ENDVNDRSZCODE).trim());
				skuTo.setVendorSizeDesc(str.substring(STARTVNDRSZDESC,
						ENDVNDRSZDESC).trim());
				skuTo.setLongSku(str.substring(STARTUPC,
						ENDUPC).trim());
				skuTo.setParentUPC(str.substring(STARTSETPARENTUPC,
						ENDSETPARENTUPC).trim());
				//added the retail price to SKU
				double retailprice = Double.parseDouble(str.substring(STARTPRICE,ENDPRICE).trim());
				skuTo.setRetailPrice(retailprice);
				
				skuTo.setSetFlag("S".equals(str.substring(STARTSETIND, ENDSETIND).trim()) ? "Y" : "N");
				
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

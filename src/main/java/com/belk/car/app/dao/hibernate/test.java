package com.belk.car.app.dao.hibernate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Properties;

public class test {

	private static Object id=2;
	private static Object carId=546;
	private static Object catalogId=20;
	private static Object headerNum=1;
	private static Object templateIdMaster=2;
	private static Object attrId3;
	private static Object attrId1;
	private static Object attrId2;
	private static Object templateId;

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		File srcFile = new File("C:/","Sunset.jpg");
		if (srcFile.isDirectory() || !srcFile.exists()){
			
		}
		File destdir= new File("C:/pic/temp");
		if (!destdir.exists())
			destdir.mkdirs();
		File destFile = new File("C:/pic/temp","temp.jpg");
		FileInputStream fis = new FileInputStream(srcFile);
		FileOutputStream fos = new FileOutputStream(destFile);
		FileChannel srcChannel = fis.getChannel();
		FileChannel destChannel = fos.getChannel();
		srcChannel.transferTo(0, srcFile.length(), destChannel);
		srcChannel.close();
		destChannel.close();
		fis.close();
		fos.close();
		/*Properties properties = new Properties();
		try {
		    properties.load(new FileInputStream("./src/conf/dev/dist.properties"));
		    String path=properties.getProperty("ftp.imagePath");
		    System.out.println("path="+path);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		/*StringBuffer queryBuf = new StringBuffer("select r.* ");

		queryBuf
				.append(" from vendor_catalog_record r ")
				.append(
						" inner join vendor_catalog_header h on r.vndr_catl_hdr_fld_num = h.vndr_catl_hdr_fld_num ")
				.append(
						" inner join master_attribute_mapping m on m.vndr_catl_hdr_id = h.vendor_catalog_header_id ")
				.append(" where  ").append("  m.catalog_master_attr_id IN(").append(
						attrId1).append(",").append(attrId2).append(",")
				.append(attrId3).append(") and m.catalog_template_id=")
				.append(templateId).append(" and h.vendor_catalog_id=").append(
						catalogId).append(" and r.vendor_catalog_id=").append(
						catalogId).append(" order by r.VNDR_CATL_RECORD_NUM");
		*/

		 File file1 = new File("./Sunset.jpg");
		    File file2 = new File("Sunset.jpg");

		    file1 = file1.getCanonicalFile();
		    file2 = file2.getCanonicalFile();
		  
	}


}

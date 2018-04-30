package com.belk.car.app;

//STEP 1. Import required packages
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.belk.car.app.DataCleanupDTO;


public class CARSVendorNumberCleanup  {
 // JDBC driver name and database URL
 static final String JDBC_DRIVER = "oracle.jdbc.OracleDriver";  
 static final String DB_URL = "jdbc:oracle:thin:@(DESCRIPTION=(LOAD_BALANCE=YES)(FAILOVER=ON)(ENABLE=BROKEN)(ADDRESS=(PROTOCOL=TCP)(HOST=40.143.11.8)(PORT=1521))(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=BELKCARS)))"; 
 //  Database credentials
 static final String USER = "CARS";
 static final String PASS = "TEST";
 
/*     
 * 
 *   sample
        //Disable the FK on vendor table
         DELETE FROM VENDOR WHERE VENDOR_ID IN (
		 SELECT DUP_VENDOR_ID FROM (
		 SELECT VENDOR_NUMBER, MAX(VENDOR_ID) DUP_VENDOR_ID FROM (
		 SELECT VENDOR_NUMBER,0 AS VENDOR_ID FROM VENDOR WHERE VENDOR_NUMBER NOT IN (SELECT RMS_VENDOR_ID FROM RMS_CARS_VENDOR_XREF_TMP)
		 UNION ALL
		 SELECT B.VEN_ID VENDOR_NUMBER, VENDOR_ID FROM VENDOR A, RMS_CARS_VENDOR_XREF_TMP B WHERE B.RMS_VENDOR_ID = A.VENDOR_NUMBER
		 ) GROUP BY VENDOR_NUMBER
		 HAVING COUNT(1) > 1
		 )
		 )
		 ;
*/
/*
		UPDATE VENDOR A SET A.VENDOR_NUMBER = (
		SELECT B.VEN_ID
		FROM RMS_CARS_VENDOR_XREF_TMP B
		WHERE A.VENDOR_NUMBER = B.RMS_VENDOR_ID
		)
		WHERE EXISTS
		(SELECT * FROM RMS_CARS_VENDOR_XREF_TMP B
		WHERE A.VENDOR_NUMBER = B.RMS_VENDOR_ID) ;
		
		--- Update vendor id
        UPDATE VENDOR_STYLE A SET A.VENDOR_ID = (
		SELECT B.VENDOR_ID
		FROM VENDOR B
		WHERE A.VENDOR_NUMBER = B.VENDOR_NUMBER
		)
		WHERE EXISTS
		(SELECT * FROM VENDOR B
		WHERE A.VENDOR_NUMBER = B.VENDOR_NUMBER) ;
*/
 
 static Map<String, String> dupeStyleMap= new HashMap<String, String>();
 
 public static void main(String[] args) {
 
 CARSVendorNumberCleanup cleanUP=new CARSVendorNumberCleanup();
 List<DataCleanupDTO> cleanupDTOs;
 try {
	    cleanUP.GetAllVendorDetails();
		cleanupDTOs = cleanUP.GetAllCARDetails();
		cleanUP.UpdateCARStatus(cleanupDTOs);
		
 }catch(SQLException se){
	    //Handle errors for JDBC
	    se.printStackTrace();
	 }catch(Exception e){
	    //Handle errors for Class.forName
	    e.printStackTrace();
	 }
	 System.out.println("Goodbye!");
	}//end main
 
 
 private void UpdateCARStatus(List<DataCleanupDTO> cleanupDTOs)
		 throws SQLException, ClassNotFoundException {
		 Connection conn = null;
		 Statement stmt = null;
		 
		// STEP 2: Register JDBC driver
		Class.forName("oracle.jdbc.OracleDriver");

		// STEP 3: Open a connection
		System.out.println("Connecting to database...");
		conn = DriverManager.getConnection(DB_URL, USER, PASS);

		// STEP 4: Execute a query
		System.out.println("Creating UpdateCARStatus statement...");
		stmt = conn.createStatement();
		// Republish the CARs have the 8 digit issue
		for (DataCleanupDTO dataCleanupDTO : cleanupDTOs) {
			System.out.println("dataCleanupDTO.getVsId()---------"+dataCleanupDTO.getVsId());
			/*if (dataCleanupDTO.getCarStatus().equals("SENT_TO_CMP")) {
				StringBuffer carQuery = new StringBuffer();
				carQuery.append(" UPDATE CAR SET CONTENT_STATUS_CD='PUBLISHED' "
						+ " WHERE CAR_ID='" + dataCleanupDTO.getCarId() + "'); ");
				stmt = conn.createStatement();
				ResultSet rsCar = stmt.executeQuery(carQuery.toString());
				conn.commit();
				rsCar.close();

			}
			// Republish the CAR images have the issues
			if (dataCleanupDTO.getCarStatus().equals("SENT_TO_CMP")) {
				StringBuffer sqlImageQuery = new StringBuffer();
				sqlImageQuery
						.append(" UPDATE IMAGE  SET IMAGE_PROCESSING_STATUS_CD='PENDING' "
								+ " WHERE IMAGE_ID IN (SELECT IMAGE_ID FROM CAR_IMAGE WHERE CAR_ID='"
								+ dataCleanupDTO.getCarId() + "');");
				stmt = conn.createStatement();
				ResultSet rsImage = stmt.executeQuery(sqlImageQuery.toString());
				conn.commit();
				rsImage.close();
				System.out.println("SENT_TO_CMP "+dataCleanupDTO.getCarId());
			}*/
		    String CARVendorStyleID=dupeStyleMap.get(dataCleanupDTO.getVsId());
			if ( CARVendorStyleID != null) {
				//update vendor style id for car
				StringBuffer vsUpdateQuery = new StringBuffer();
				vsUpdateQuery.append(" UPDATE CAR SET  " +
						        " VENDOR_STYLE_ID='"+CARVendorStyleID+"'"
						       + " WHERE VENDOR_STYLE_ID='" + dataCleanupDTO.getVsId() + "'");
				stmt = conn.createStatement();
				System.out.println("vsUpdateQuery------"+vsUpdateQuery.toString());
				ResultSet rsVsUpdateQuery = stmt.executeQuery(vsUpdateQuery.toString());
				conn.commit();
				rsVsUpdateQuery.close();
				
				//update vendor style id for car
				StringBuffer vendorSkuUpdate = new StringBuffer();
				vendorSkuUpdate.append(" UPDATE VENDOR_SKU SET  " +
						        " VENDOR_STYLE_ID='"+CARVendorStyleID+"'"
							  + " WHERE VENDOR_STYLE_ID='" + dataCleanupDTO.getVsId() + "'");
				stmt = conn.createStatement();
				System.out.println("vendorSkuUpdate--------"+vendorSkuUpdate.toString());
				ResultSet rsVendorSku = stmt.executeQuery(vendorSkuUpdate.toString());
				conn.commit();
				rsVendorSku.close();
				
				//update vendor style id for car
				StringBuffer vendorSample = new StringBuffer();
				vendorSample.append(" UPDATE SAMPLE SET  " +
						        " VENDOR_STYLE_ID='"+CARVendorStyleID+"'"
							  + " WHERE VENDOR_STYLE_ID='" + dataCleanupDTO.getVsId() + "'");
				stmt = conn.createStatement();
				System.out.println("vendorSample--------"+vendorSample.toString());
				ResultSet vendorSampleRS = stmt.executeQuery(vendorSample.toString());
				conn.commit();
				vendorSampleRS.close();
				
				StringBuffer vsUpdateQueryPIM = new StringBuffer();
				vsUpdateQueryPIM.append(" DELETE FROM VENDORSTYLE_PIM_ATTRIBUTE WHERE " +
						        " VENDOR_STYLE_ID='"+dataCleanupDTO.getVsId()+"'");
				stmt = conn.createStatement();
				System.out.println("vsUpdateQueryPIM---------"+vsUpdateQueryPIM.toString());
				ResultSet rsvsUpdateQueryPIM = stmt.executeQuery(vsUpdateQueryPIM.toString());
				conn.commit();
				rsvsUpdateQueryPIM.close();
				
				StringBuffer vsUpdateQuery2 = new StringBuffer();
				vsUpdateQuery2.append(" DELETE FROM VENDOR_STYLE WHERE " +
						        " VENDOR_STYLE_ID='"+dataCleanupDTO.getVsId()+"'");
				stmt = conn.createStatement();
				System.out.println("vsUpdateQuery2----------"+vsUpdateQuery2.toString());
				ResultSet rsvsUpdateQuery2 = stmt.executeQuery(vsUpdateQuery2.toString());
				conn.commit();
				rsvsUpdateQuery2.close();
			} else {
					// Update ven_id from RMS table and update in vendor_style
					// and vendor id from
					StringBuffer vsUpdateQuery = new StringBuffer();
					vsUpdateQuery
							.append(" UPDATE VENDOR_STYLE SET VENDOR_NUMBER ="
									+ " (SELECT VEN_ID FROM RMS_CARS_VENDOR_XREF_TMP WHERE RMS_VENDOR_ID='"
									+ dataCleanupDTO.getvNo() + "')  WHERE VENDOR_STYLE_ID='"
									+ dataCleanupDTO.getVsId() + "'");
					stmt = conn.createStatement();
					System.out.println("vsUpdateQuery--------" + vsUpdateQuery.toString());
					ResultSet rsVsUpdateQuery = stmt.executeQuery(vsUpdateQuery
							.toString());
					conn.commit();
					rsVsUpdateQuery.close();

					// Update ven_id from RMS table and update in vendor_style
					// and vendor id from
					StringBuffer vendorUpdateQuery = new StringBuffer();
					vendorUpdateQuery
							.append(" UPDATE VENDOR_STYLE SET VENDOR_ID = "
									+ " (SELECT VENDOR_ID FROM VENDOR WHERE  VENDOR_NUMBER IN ( SELECT VEN_ID FROM RMS_CARS_VENDOR_XREF_TMP "
									+ " WHERE RMS_VENDOR_ID='"
									+ dataCleanupDTO.getvNo() + "'))" +
									" WHERE VENDOR_STYLE_ID='"
											+ dataCleanupDTO.getVsId() + "'");
					stmt = conn.createStatement();
					System.out.println("--------vendorUpdateQuery"+vendorUpdateQuery);
					ResultSet vendorUpdateQueryRS = stmt
							.executeQuery(vendorUpdateQuery.toString());
					conn.commit();
					vendorUpdateQueryRS.close();
				}
			}
		stmt.close();
		conn.close();
 }

	public List<DataCleanupDTO> GetAllCARDetails()
			throws SQLException, ClassNotFoundException {
		
		Connection conn = null;
		 Statement stmt = null;
		// STEP 2: Register JDBC driver
		Class.forName("oracle.jdbc.OracleDriver");

		// STEP 3: Open a connection
		System.out.println("Connecting to database...");
		conn = DriverManager.getConnection(DB_URL, USER, PASS);

		// STEP 4: Execute a query
		System.out.println("Creating GetAllCARDetails statement...");
		stmt = conn.createStatement();

		List<DataCleanupDTO> cleanupDTOs = new ArrayList<DataCleanupDTO>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT C.CAR_ID, VS.VENDOR_NUMBER,VS.VENDOR_STYLE_NUMBER , VS.VENDOR_STYLE_ID,C.CONTENT_STATUS_CD"
				+ " FROM VENDOR_STYLE VS "
				+ " INNER JOIN RMS_CARS_VENDOR_XREF_TMP RMS ON RMS.RMS_VENDOR_ID=VS.VENDOR_NUMBER "
				+ " INNER JOIN CAR C ON C.VENDOR_STYLE_ID=VS.VENDOR_STYLE_ID ");
		System.out.println("GetAllCARDetails --------"+sql.toString());
		ResultSet rs = stmt.executeQuery(sql.toString());
		// STEP 5: Extract data from result set
		while (rs.next()) {
			// Retrieve by column name
			int carId = rs.getInt("CAR_ID");
			System.out.println(carId);
			int vNo = rs.getInt("VENDOR_NUMBER");
			String vsNo = rs.getString("VENDOR_STYLE_NUMBER");
			String vsId = rs.getString("VENDOR_STYLE_ID");
			String carStatus = rs.getString("CONTENT_STATUS_CD");
			DataCleanupDTO cleanupDTO = new DataCleanupDTO();
			cleanupDTO.setCarId(carId);
			cleanupDTO.setVsNo(vsNo);
			cleanupDTO.setvNo(vNo);
			cleanupDTO.setVsId(vsId);
			cleanupDTO.setCarStatus(carStatus);
			cleanupDTOs.add(cleanupDTO);
		}
		// STEP 6: Clean-up environment
		rs.close();
		stmt.close();
		conn.close();

		return cleanupDTOs;
	}
	
	public void GetAllVendorDetails()
			throws SQLException, ClassNotFoundException {
		
		Connection conn = null;
		 Statement stmt = null;
		// STEP 2: Register JDBC driver
		Class.forName("oracle.jdbc.OracleDriver");

		// STEP 3: Open a connection
		System.out.println("Connecting to database...");
		conn = DriverManager.getConnection(DB_URL, USER, PASS);

		// STEP 4: Execute a query
		System.out.println("Creating GetAllVendorDetails statement...");
		stmt = conn.createStatement();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  RMS_VENDOR_STYLE, " +
				" CAR_VENDOR_STYLE FROM vw_rms_cars_style_xref_tmp ");
		System.out.println("GetAllVendorDetails-----"+sql.toString());
		ResultSet rs = stmt.executeQuery(sql.toString());
		// STEP 5: Extract data from result set
		while (rs.next()) {
			// Retrieve by column name
			String RMSVs = rs.getString("RMS_VENDOR_STYLE");
			String CARVs = rs.getString("CAR_VENDOR_STYLE");
			dupeStyleMap.put(RMSVs, CARVs);
		}
		// STEP 6: Clean-up environment
		rs.close();
		stmt.close();
		conn.close();
	}
}// end Data clean up

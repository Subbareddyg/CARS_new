package com.belk.car.app.dao.hibernate;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.appfuse.dao.hibernate.UniversalDaoHibernate;
import org.appfuse.model.User;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.belk.car.app.Constants;
import com.belk.car.app.dao.VendorImageEmailNotificationDao;
import com.belk.car.app.dto.FailedImageDTO;
import com.belk.car.app.dto.RRDCheckEmailNotificationDTO;
import com.belk.car.app.dto.FailedImagesDeptDTO;

public class VendorImageEmailNotificationDaoHibernate extends UniversalDaoHibernate implements
VendorImageEmailNotificationDao  {
	
	// Append common query
	StringBuffer sbQueryClause = commonQuery();
	StringBuffer gruopByQuery  = commonGroupByQuery();
	
	/**
	 * This method populates the failed image details for all the CARS including Regular,Dropship and Outfit.
	 * This method returns map and this map will be used to send an email notification to all users  
	 * return Map<String, RRDCheckEmailNotificationDTO> 
	 * @author - afurxd2 
	 */
	@Override
	public Map<String, RRDCheckEmailNotificationDTO> populateEmailListForAllCARS() {
		// finalMap will be used to send an email for all users of Regular,Outfit and Drop ship
		Map<String, RRDCheckEmailNotificationDTO> failedImagesMapForAllCARS = new HashMap<String, RRDCheckEmailNotificationDTO>();
		//populate failed image details for Regular CARS
		failedImagesMapForAllCARS = populateEmailDetailsForAllRegularCARS();
		//populate failed image details for Drop ship CARS
		failedImagesMapForAllCARS = populateEmailDetailsForAllDropshipCARS(failedImagesMapForAllCARS);
		//populate failed image details for Outfit CARS
		failedImagesMapForAllCARS = populateEmailDetailsForAllOutfitCARS(failedImagesMapForAllCARS);
		return failedImagesMapForAllCARS;
	}

	/**
	 * This method populates the failed image details for all regular CARS
	 * 
	 * @return Map<String, RRDCheckEmailNotificationDTO>
	 */
	private Map<String, RRDCheckEmailNotificationDTO> populateEmailDetailsForAllRegularCARS() {
		Map<String, RRDCheckEmailNotificationDTO> failedImagesMapForRegularCARS = new HashMap<String, RRDCheckEmailNotificationDTO>();
		failedImagesMapForRegularCARS=populateFailedImageDetailsForRegularCARBuyers();
		//failedImagesMapForRegularCARS= populateFailedImageDetailsForRegularCARVendors(failedImagesMapForRegularCARS);
		return failedImagesMapForRegularCARS;
	}
	
	/**
	 * This method populates the failed image details for all regular CARs Buyer
	 * 
	 * @return Map<String, RRDCheckEmailNotificationDTO>
	 */
	@SuppressWarnings("unchecked")
	private Map<String, RRDCheckEmailNotificationDTO> populateFailedImageDetailsForRegularCARBuyers() {
		Map<String, RRDCheckEmailNotificationDTO> failedImagesMapForRegularCARBuyers = new HashMap<String, RRDCheckEmailNotificationDTO>();
		List<RRDCheckEmailNotificationDTO> resultSetList = new ArrayList<RRDCheckEmailNotificationDTO>();
		StringBuffer query = new StringBuffer();
		query.append(sbQueryClause);
		query.append(" WHERE TRUNC(VI.UPDATED_DATE) = TRUNC(SYSDATE)");
		query.append(" AND vi.VENDOR_IMAGE_STATUS_CD IN ('MQ_FAILED','CREATIVE_FAILED')");
		query.append(" AND cu.USER_TYPE_CD IN ('BUYER') AND C.SOURCE_TYPE_CD NOT IN ('DROPSHIP', 'OUTFIT') ");
		query.append(" AND TRUNC(RIC.CREATED_DATE) = TRUNC(SYSDATE)");
		 query.append(" AND CU.STATUS_CD IN ('ACTIVE') ");
		query.append(gruopByQuery);
		query.append("  ORDER BY D.DEPT_CD DESC ");
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query.toString());
			resultSetList = sQuery.list();
		} catch (Exception e) {
			log.error("Hibernate Exception" + e);
		}
		
		return mapDBObjectsToDTO(resultSetList,	failedImagesMapForRegularCARBuyers);

	}
	
	/**
	 * This method populates the failed image details for all regular CARS Vendors
	 * 
	 * @return Map<String, RRDCheckEmailNotificationDTO>
	 */
	private Map<String, RRDCheckEmailNotificationDTO> populateFailedImageDetailsForRegularCARVendors(
			Map<String, RRDCheckEmailNotificationDTO> failedImagesMapForRegularCARS) {
		List<RRDCheckEmailNotificationDTO> resultSetList = new ArrayList<RRDCheckEmailNotificationDTO>();
		StringBuffer query = new StringBuffer();
		query.append(sbQueryClause);
		query.append(" INNER JOIN VENDOR V ON V.VENDOR_ID = VS.VENDOR_ID ");
		query.append(" INNER JOIN CAR_USER_VENDOR CUV ON CUV.VENDOR_ID = V.VENDOR_ID AND CUV.CAR_USER_ID = CUD.CAR_USER_ID ");
		query.append(" WHERE TRUNC(VI.UPDATED_DATE) = TRUNC(SYSDATE) ");
		query.append(" AND vi.VENDOR_IMAGE_STATUS_CD IN ('MQ_FAILED','CREATIVE_FAILED') ");
		query.append(" AND cu.USER_TYPE_CD IN ('VENDOR') AND C.SOURCE_TYPE_CD NOT IN ('DROPSHIP', 'OUTFIT') ");
	    query.append(" AND TRUNC(RIC.CREATED_DATE) = TRUNC(SYSDATE) ");
	    query.append(" AND CU.STATUS_CD IN ('ACTIVE') ");
	    query.append(gruopByQuery);
		query.append(" ORDER BY D.DEPT_CD DESC ");
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query
					.toString());
			resultSetList = sQuery.list();
		} catch (Exception e) {
			log.error("Hibernate Exception" + e);

		}
		return mapDBObjectsToDTO(resultSetList, failedImagesMapForRegularCARS);
	}

	/**
	 * This method populates the failed image details for all Drop Ship CARS
	 * @param failedImagesMapForAllCARS 
	 * 
	 * @return Map<String, RRDCheckEmailNotificationDTO>
	 */
	private Map<String, RRDCheckEmailNotificationDTO> populateEmailDetailsForAllDropshipCARS(
			Map<String, RRDCheckEmailNotificationDTO> failedImagesMapForDropShipCARS) {
		failedImagesMapForDropShipCARS=populateFailedImageDetailsForDropShipCARBuyers(failedImagesMapForDropShipCARS);
		//failedImagesMapForDropShipCARS= populateFailedImageDetailsForDropShipCARVendors(failedImagesMapForDropShipCARS);
		return failedImagesMapForDropShipCARS;
	}
	
	/**
	 * This method populates the failed image details for all Drop Ship CAR Buyers
	 * If image uploaded by Vendor
	 * @param failedImagesMapForAllCARS 
	 * 
	 * @return Map<String, RRDCheckEmailNotificationDTO>
	 */
	private Map<String, RRDCheckEmailNotificationDTO> populateFailedImageDetailsForDropShipCARBuyers(
			Map<String, RRDCheckEmailNotificationDTO> failedImagesMapForDropShipCARS) {
		
		List<RRDCheckEmailNotificationDTO> resultSetList = new ArrayList<RRDCheckEmailNotificationDTO>();
		StringBuffer query = new StringBuffer();
		query.append(sbQueryClause);
		query.append(" WHERE TRUNC(VI.UPDATED_DATE) = TRUNC(SYSDATE) ");
		query.append(" AND vi.VENDOR_IMAGE_STATUS_CD IN ('MQ_FAILED','CREATIVE_FAILED')");
		query.append(" AND cu.USER_TYPE_CD IN ('BUYER') " + 
		             " AND I.IMAGE_SOURCE_TYPE_CD IN ('BUYER_UPLOADED','VENDOR_UPLOADED') " + 
				     " AND C.SOURCE_TYPE_CD = 'DROPSHIP' ");
		query.append(" AND CU.STATUS_CD IN ('ACTIVE') ");
		query.append(" AND TRUNC(RIC.CREATED_DATE) = TRUNC(SYSDATE)");
		query.append(gruopByQuery);
		query.append(" ORDER BY D.DEPT_CD DESC ");
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query
					.toString());
			//System.out.println("populateFailedImageDetailsForDropShipCARBuyers :"+query.toString());
			resultSetList = sQuery.list();
		} catch (Exception e) {
			log.error("Hibernate Exception" + e);
		}
		return mapDBObjectsToDTO(resultSetList,failedImagesMapForDropShipCARS);
	}
	
	
	/**
	 * This method populates the failed image details for Drop Ship CAR Vendor
	 * If image uploaded by Vendor
	 * @param failedImagesMapForAllCARS 
	 * 
	 * @return Map<String, RRDCheckEmailNotificationDTO>
	 */	
	private Map<String, RRDCheckEmailNotificationDTO> populateFailedImageDetailsForDropShipCARVendors(
			Map<String, RRDCheckEmailNotificationDTO> failedImagesMapForDropShipCARS) {

	    List<RRDCheckEmailNotificationDTO> resultSetList = new ArrayList<RRDCheckEmailNotificationDTO>();
		StringBuffer query = new StringBuffer();
		query.append(sbQueryClause);
		query.append(" INNER JOIN VENDOR V ON V.VENDOR_ID = VS.VENDOR_ID ");
		query.append(" INNER JOIN CAR_USER_VENDOR CUV ON CUV.VENDOR_ID = V.VENDOR_ID AND CUV.CAR_USER_ID = CUD.CAR_USER_ID ");
		query.append(" where Trunc(vi.updated_date) = TRUNC(SYSDATE) ");
		query.append(" AND vi.VENDOR_IMAGE_STATUS_CD IN ('MQ_FAILED','CREATIVE_FAILED')");
		query.append(" AND cu.USER_TYPE_CD IN ('VENDOR') "
			       + " AND I.IMAGE_SOURCE_TYPE_CD = 'VENDOR_UPLOADED' "
			       + " AND C.SOURCE_TYPE_CD = 'DROPSHIP' ");
		query.append(" AND CU.STATUS_CD IN ('ACTIVE') ");
		query.append(" AND TRUNC(RIC.CREATED_DATE) = TRUNC(SYSDATE)");
		query.append(gruopByQuery);
		query.append(" ORDER BY D.DEPT_CD DESC ");
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query
					.toString());
			resultSetList = sQuery.list();
		} catch (Exception e) {
			log.error("Hibernate Exception" + e);
		}

		return mapDBObjectsToDTO(resultSetList, failedImagesMapForDropShipCARS);
	}

	/**
	 * This method populates the failed image details for all Outfit CARS
	 * @param failedImagesMapForAllCARS 
	 * 
	 * @return Map<String, RRDCheckEmailNotificationDTO>
	 */
	private Map<String, RRDCheckEmailNotificationDTO> populateEmailDetailsForAllOutfitCARS(
			Map<String, RRDCheckEmailNotificationDTO> failedImagesMapForOutfitCARS) {
		failedImagesMapForOutfitCARS=populateFailedImageDetailsForOutfitCARBuyerAndVendor(failedImagesMapForOutfitCARS);
		failedImagesMapForOutfitCARS= populateFailedImageDetailsForOufitCARBuyers(failedImagesMapForOutfitCARS);
		return failedImagesMapForOutfitCARS;
	}

	/**
	 * This method populates the failed image details for all Outfit CAR Buyer and Vendor
	 * @param failedImagesMapForAllCARS 
	 * 
	 * @return Map<String, RRDCheckEmailNotificationDTO>
	 */
	private Map<String, RRDCheckEmailNotificationDTO> populateFailedImageDetailsForOutfitCARBuyerAndVendor(
			Map<String, RRDCheckEmailNotificationDTO> failedImagesMapForOutfitCARS) {
		List<RRDCheckEmailNotificationDTO> resultSetList = new ArrayList<RRDCheckEmailNotificationDTO>();
		StringBuffer query = new StringBuffer();
		query.append("SELECT ")
				.append("C.CAR_ID, D.DEPT_CD, dbms_lob.SubStr(wm_concat(DISTINCT D.NAME)), VI.CREATED_BY, CU.USER_TYPE_CD, I.IMAGE_LOCATION,I.DESCR,"
						+ " VS.VENDOR_STYLE_NUMBER, VI.VENDOR_IMAGE_STATUS_CD, VI.BUYER_APPROVED, C.EXPECTED_SHIP_DATE,"
						+ " VI.COLOR_CODE,I.IMAGE_SOURCE_TYPE_CD, RIC.CHECK_TYPE, dbms_lob.SubStr(wm_concat(DISTINCT RICF.FEEDBACK)), CU.FIRST_NAME, CU.LAST_NAME ")
				.append(" FROM CAR C ")
				.append(" INNER JOIN DEPARTMENT D ON D.DEPT_ID = C.DEPT_ID")
				.append(" INNER JOIN CAR_IMAGE CI ON C.CAR_ID = CI.CAR_ID")
				.append(" INNER JOIN IMAGE I ON CI.IMAGE_ID = I.IMAGE_ID")
				.append(" INNER JOIN VENDOR_IMAGE VI ON VI.IMAGE_ID = I.IMAGE_ID")
				.append(" INNER JOIN CAR_USER CU ON VI.CREATED_BY = CU.EMAIL_ADDR")
				.append(" INNER JOIN VENDOR_STYLE VS ON VS.VENDOR_STYLE_ID = VI.VENDOR_STYLE_ID ")
				.append(" INNER JOIN RRD_IMAGE_CHECK RIC ON  RIC.VENDOR_IMAGE_ID = VI.VENDOR_IMAGE_ID")
				.append(" INNER JOIN RRD_IMAGE_CHECK_FEEDBACK RICF ON RICF.CHECK_ID = RIC.CHECK_ID ");
		   query.append(" where Trunc(vi.updated_date) = TRUNC(SYSDATE) ");
		   query.append(" AND vi.VENDOR_IMAGE_STATUS_CD IN ('MQ_FAILED','CREATIVE_FAILED')");
		   query.append(" AND I.IMAGE_SOURCE_TYPE_CD IN ('BUYER_UPLOADED','VENDOR_UPLOADED') AND C.SOURCE_TYPE_CD = 'OUTFIT' ");
		   query.append(" AND TRUNC(RIC.CREATED_DATE) = TRUNC(SYSDATE)");
		   query.append(" AND CU.STATUS_CD IN ('ACTIVE') ");
		   query.append(" GROUP BY C.CAR_ID, D.DEPT_CD, VI.CREATED_BY, CU.USER_TYPE_CD, I.IMAGE_LOCATION,I.DESCR,"
				      + " VS.VENDOR_STYLE_NUMBER, VI.VENDOR_IMAGE_STATUS_CD, VI.BUYER_APPROVED, C.EXPECTED_SHIP_DATE,"
				      + " VI.COLOR_CODE,I.IMAGE_SOURCE_TYPE_CD, RIC.CHECK_TYPE, CU.FIRST_NAME, CU.LAST_NAME ");
		query.append(" ORDER BY D.DEPT_CD DESC ");
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query
					.toString());
			resultSetList = sQuery.list();
		} catch (Exception e) {
			log.error("Hibernate Exception" + e);
		}

		return mapDBObjectsToDTO(resultSetList, failedImagesMapForOutfitCARS);

	}

	/**
	 * This method populates the failed image details for all Outfit CAR Buyers
	 * @param failedImagesMapForAllCARS 
	 * 
	 * @return Map<String, RRDCheckEmailNotificationDTO>
	 */
	private Map<String, RRDCheckEmailNotificationDTO> populateFailedImageDetailsForOufitCARBuyers(
			Map<String, RRDCheckEmailNotificationDTO> failedImagesMapForOutfitCARS) {
		List<RRDCheckEmailNotificationDTO> resultSetList = new ArrayList<RRDCheckEmailNotificationDTO>();
		StringBuffer query = new StringBuffer();
		query.append("SELECT ")
				.append(" DISTINCT C1.CAR_ID, '000' AS DEPT_CD, dbms_lob.SubStr(wm_concat(DISTINCT D.NAME)), CU.EMAIL_ADDR, CU.USER_TYPE_CD,I.IMAGE_LOCATION,I.DESCR,"
						+ " VS.VENDOR_STYLE_NUMBER, VI.VENDOR_IMAGE_STATUS_CD, VI.BUYER_APPROVED, C1.EXPECTED_SHIP_DATE, VI.COLOR_CODE," +
						  " I.IMAGE_SOURCE_TYPE_CD, RIC.CHECK_TYPE, dbms_lob.SubStr(wm_concat(DISTINCT RICF.FEEDBACK)), CU.FIRST_NAME, CU.LAST_NAME ")
				.append(" FROM VENDOR_IMAGE VI ")
				.append(" INNER JOIN IMAGE I ON I.IMAGE_ID = VI.IMAGE_ID")
				.append(" AND VI.VENDOR_IMAGE_STATUS_CD IN ('MQ_FAILED','CREATIVE_FAILED' )")
				.append(" AND TRUNC(VI.UPDATED_DATE) = TRUNC(SYSDATE)")
				.append(" INNER JOIN CAR_IMAGE CI ON CI.IMAGE_ID = I.IMAGE_ID")
				.append(" INNER JOIN CAR C1 ON C1.CAR_ID = CI.CAR_ID AND C1.SOURCE_TYPE_CD = 'OUTFIT'")
				.append(" INNER JOIN CAR_OUTFIT_CHILD COC ON COC.OUTFIT_CAR_ID = C1.CAR_ID ")
				.append(" INNER JOIN CAR C2 ON C2.CAR_ID = COC.CHILD_CAR_ID AND C2.STATUS_CD='ACTIVE'")
				.append(" INNER JOIN DEPARTMENT D ON C2.DEPT_ID = D.DEPT_ID ")
				.append(" INNER JOIN CAR_USER_DEPARTMENT CUD ON D.DEPT_ID = CUD.DEPT_ID ")
				.append(" INNER JOIN CAR_USER CU ON CUD.CAR_USER_ID = CU.CAR_USER_ID ")
				.append(" INNER JOIN VENDOR_STYLE VS ON VS.VENDOR_STYLE_ID = VI.VENDOR_STYLE_ID ")
				.append(" INNER JOIN RRD_IMAGE_CHECK RIC ON  RIC.VENDOR_IMAGE_ID = VI.VENDOR_IMAGE_ID ")
				.append(" INNER JOIN RRD_IMAGE_CHECK_FEEDBACK RICF ON RICF.CHECK_ID = RIC.CHECK_ID ");
		   query.append(" WHERE I.IMAGE_SOURCE_TYPE_CD = 'VENDOR_UPLOADED' ");
		   query.append(" AND CU.USER_TYPE_CD IN ('BUYER')");
		   query.append(" AND CU.STATUS_CD IN ('ACTIVE')");
		   query.append(" AND TRUNC(RIC.CREATED_DATE) = TRUNC(SYSDATE) ");
		   query.append(" GROUP BY C1.CAR_ID,CU.EMAIL_ADDR,CU.USER_TYPE_CD, I.IMAGE_LOCATION,I.DESCR," +
					 " VS.VENDOR_STYLE_NUMBER, VI.VENDOR_IMAGE_STATUS_CD, VI.BUYER_APPROVED," +
				     " C1.EXPECTED_SHIP_DATE,VI.COLOR_CODE, I.IMAGE_SOURCE_TYPE_CD, RIC.CHECK_TYPE," +
				     " CU.FIRST_NAME, CU.LAST_NAME ");
		//query.append(" ORDER BY D.DEPT_CD DESC ");
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query
					.toString());
			resultSetList = sQuery.list();
		} catch (Exception e) {
			log.error("Hibernate Exception" + e);
		}
		return mapDBObjectsToDTO(resultSetList, failedImagesMapForOutfitCARS);
	}
	
	/**
	 * This method creates the common query used among all the methods used for
	 * populating the vendor image failed details 
	 * 
	 * return commonQuery 
	 */
	private StringBuffer commonQuery() {
	 // used for appending the common query
	 StringBuffer commonQuery = new StringBuffer();
	 commonQuery.append("SELECT C.CAR_ID, D.DEPT_CD, D.NAME, CU.EMAIL_ADDR, CU.USER_TYPE_CD, I.IMAGE_LOCATION, I.DESCR," +
						"VS.VENDOR_STYLE_NUMBER, VI.VENDOR_IMAGE_STATUS_CD, VI.BUYER_APPROVED, " +
						"C.EXPECTED_SHIP_DATE, VI.COLOR_CODE,I.IMAGE_SOURCE_TYPE_CD, RIC.CHECK_TYPE, " +
						"dbms_lob.SubStr(wm_concat(DISTINCT RICF.FEEDBACK)), CU.FIRST_NAME, CU.LAST_NAME ")
				.append(" FROM CAR C ")
				.append(" INNER JOIN DEPARTMENT D ON D.DEPT_ID = C.DEPT_ID")
				.append(" INNER JOIN CAR_USER_DEPARTMENT CUD ON D.DEPT_ID = CUD.DEPT_ID")
				.append(" INNER JOIN CAR_USER CU ON CUD.CAR_USER_ID = CU.CAR_USER_ID")
				.append(" INNER JOIN CAR_IMAGE CI ON C.CAR_ID = CI.CAR_ID")
				.append(" INNER JOIN IMAGE I ON CI.IMAGE_ID = I.IMAGE_ID")
				.append(" INNER JOIN VENDOR_IMAGE VI ON VI.IMAGE_ID = I.IMAGE_ID ")
				.append(" INNER JOIN VENDOR_STYLE VS ON VS.VENDOR_STYLE_ID = VI.VENDOR_STYLE_ID")
				.append(" INNER JOIN RRD_IMAGE_CHECK RIC ON  RIC.VENDOR_IMAGE_ID = VI.VENDOR_IMAGE_ID ")
				.append(" INNER JOIN RRD_IMAGE_CHECK_FEEDBACK RICF ON RICF.CHECK_ID = RIC.CHECK_ID ");
	 
		return commonQuery;
	}
	
	/**
	 * This method creates the common query used among all the methods used for
	 * populating the vendor image failed details 
	 * 
	 * return groupByQuery 
	 */
	private StringBuffer commonGroupByQuery() {
	 // used for appending the common query
	 StringBuffer groupByQuery = new StringBuffer();
	 groupByQuery.append(" GROUP BY C.CAR_ID, D.DEPT_CD, D.DEPT_ID,CU.CAR_USER_ID, D.NAME, CU.EMAIL_ADDR, CU.USER_TYPE_CD," +
	 					" I.IMAGE_LOCATION, I.DESCR,VS.VENDOR_STYLE_NUMBER, " +
	 					" VI.VENDOR_IMAGE_STATUS_CD, VI.BUYER_APPROVED,C.EXPECTED_SHIP_DATE, " +
				 		" VI.COLOR_CODE,I.IMAGE_SOURCE_TYPE_CD, RIC.CHECK_TYPE,RICF.CHECK_ID," +
				 		" CU.FIRST_NAME, CU.LAST_NAME, VI.VENDOR_IMAGE_ID,RIC.VENDOR_IMAGE_ID  ");
	 
		return groupByQuery;
	}
	
	
	/**
	 * This method maps the result set to DTO 
	 *  
	 * @return Map<String, RRDCheckEmailNotificationDTO\
	 * >
	 */
	@SuppressWarnings("rawtypes")
	private Map<String, RRDCheckEmailNotificationDTO> mapDBObjectsToDTO(
			List resultSetList,Map<String, RRDCheckEmailNotificationDTO> mapUserFailedImages) {
		for (Object obj : resultSetList) {
			User user = new User();
			java.math.BigDecimal bigUserId = (java.math.BigDecimal) Array.get(obj, 0);
			Long carId = bigUserId == null ? null : bigUserId.longValue();
			Object o = Array.get(obj, 1);
			String deptCode="";
			if (o.toString().equals("0")) {
				 deptCode=Constants.OUTFIT_DEPT_NUMBER;
			} else {
				 deptCode = o.toString();
			}
			String deptName = (String) Array.get(obj, 2);
			String emailAddress = (String) Array.get(obj, 3);
			String userTypeCode = (String) Array.get(obj, 4);
			String imageName = (String) Array.get(obj, 5);
			String orignalImage = (String) Array.get(obj, 6);
			Object o1 = Array.get(obj, 7);
			String venodrImageId = o1.toString();
			/*
			 * java.math.BigDecimal bigimageId = (java.math.BigDecimal)
			 * Array.get(obj, 6); Long venodrImageId = bigimageId == null ? null
			 * : bigimageId.longValue();
			 */
			String vendorImageStatusCode = (String) Array.get(obj, 8);
			String buyerApproved = (String) Array.get(obj, 9);
			Object update = Array.get(obj, 10);
			String expectedDate = update.toString();
			DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
			Date date = null;
			try {
				date = inputFormat.parse(expectedDate);
			} catch (ParseException e) {
				log.error("ParseException Exception" + e);
			}
			// Format date into output format
			DateFormat outputFormat = new SimpleDateFormat("MM-dd-yyyy");
			String expectedShipDate = outputFormat.format(date);
			// expectedShipDate
			Object c = Array.get(obj, 11);
			String colorCode = c.toString();
			String imageSourceTypeCD = (String) Array.get(obj, 12);
			String failureType = (String) Array.get(obj, 13);
			Object fr = Array.get(obj, 14);
			String failureReasons = fr.toString();
			String firstName = (String) Array.get(obj, 15);
			String lastName = (String) Array.get(obj, 16);
			RRDCheckEmailNotificationDTO rrdCheckEmailNotificationDTO = mapUserFailedImages.get(emailAddress);
			if (rrdCheckEmailNotificationDTO != null) {
				Map<String, FailedImagesDeptDTO> previousFailedImagesMap = rrdCheckEmailNotificationDTO
						.getMapFailedImagesDeptDTO();
				FailedImagesDeptDTO previousFailedImagesDeptDTO = previousFailedImagesMap.get(deptCode);
				if (previousFailedImagesDeptDTO != null) {
					List<FailedImageDTO> failedImageDTOs = previousFailedImagesDeptDTO
							.getFailedImagesDTO();
					FailedImageDTO failedImageDTO = new FailedImageDTO();
					failedImageDTO.setCarId(carId);
					failedImageDTO.setColorCode(colorCode);
					failedImageDTO.setExpectedShipDate(expectedShipDate);
					failedImageDTO.setImageName(imageName);
					failedImageDTO.setOriginalName(orignalImage);
					failedImageDTO.setVendorStyleNo(venodrImageId.toString());
					failedImageDTO.setFailureType(failureType);
					failedImageDTO.setFailureReasons(failureReasons);
					failedImageDTOs.add(failedImageDTO);
				} else {
					// create DTO for new dept
					List<FailedImageDTO> failedImageDTOList = new ArrayList<FailedImageDTO>();
					FailedImageDTO failedImageDTO = new FailedImageDTO();
					failedImageDTO.setCarId(carId);
					failedImageDTO.setColorCode(colorCode);
					failedImageDTO.setExpectedShipDate(expectedShipDate);
					failedImageDTO.setImageName(imageName);
					failedImageDTO.setOriginalName(orignalImage);
					failedImageDTO.setVendorStyleNo(venodrImageId.toString());
					failedImageDTO.setFailureType(failureType);
					failedImageDTO.setFailureReasons(failureReasons);
					failedImageDTOList.add(failedImageDTO);
					FailedImagesDeptDTO failedImagesDeptDTO = new FailedImagesDeptDTO();
					failedImagesDeptDTO.setDeptCd(deptCode);
					failedImagesDeptDTO.setDeptName(deptName);
					failedImagesDeptDTO.setFailedImagesDTO(failedImageDTOList);
					previousFailedImagesMap.put(deptCode, failedImagesDeptDTO);
			  }
			} else {
				// No failed images found for user
				List<FailedImageDTO> failedImageDTOList = new ArrayList<FailedImageDTO>();
				Map<String, FailedImagesDeptDTO> mapFailedImagesDept = new HashMap<String, FailedImagesDeptDTO>();
				RRDCheckEmailNotificationDTO checkEmailNotificationDTO = new RRDCheckEmailNotificationDTO();
				FailedImageDTO failedImageDTO = new FailedImageDTO();
				failedImageDTO.setCarId(carId);
				failedImageDTO.setColorCode(colorCode);
				failedImageDTO.setExpectedShipDate(expectedShipDate);
				failedImageDTO.setOriginalName(orignalImage);
				failedImageDTO.setImageName(imageName);
				failedImageDTO.setVendorStyleNo(venodrImageId.toString());
				failedImageDTO.setFailureType(failureType);
				failedImageDTO.setFailureReasons(failureReasons);
				failedImageDTOList.add(failedImageDTO);
				FailedImagesDeptDTO failedImagesDeptDTO = new FailedImagesDeptDTO();
				failedImagesDeptDTO.setDeptCd(deptCode);
				failedImagesDeptDTO.setDeptName(deptName);
				failedImagesDeptDTO.setFailedImagesDTO(failedImageDTOList);
				mapFailedImagesDept.put(deptCode, failedImagesDeptDTO);
				// Set user details
				user.setEmailAddress(emailAddress);
				user.setUserTypeCd(userTypeCode);
				user.setFirstName(firstName);
				user.setFirstName(lastName);
				checkEmailNotificationDTO.setUserDetailObject(user);
				checkEmailNotificationDTO
						.setMapFailedImagesDeptDTO(mapFailedImagesDept);
				mapUserFailedImages
						.put(emailAddress, checkEmailNotificationDTO);
			}
		}
		return mapUserFailedImages;//merge
	}

}

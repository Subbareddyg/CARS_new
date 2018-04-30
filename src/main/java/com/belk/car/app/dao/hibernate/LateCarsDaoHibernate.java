package com.belk.car.app.dao.hibernate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.appfuse.dao.hibernate.UniversalDaoHibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.belk.car.app.dao.LateCarsDao;
import com.belk.car.app.dto.LateCarsSummaryDTO;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.SourceType;
import com.belk.car.app.model.UserType;

public class LateCarsDaoHibernate extends UniversalDaoHibernate implements LateCarsDao {

	@Override
	public List<LateCarsSummaryDTO> getAllLateCarsForBuyer() {
		StringBuffer query = new StringBuffer();
		   query.append("select {c.*}, ")
	   		    .append(" ur.DMM_NAME as dmmName, ur.DMM_EMAIL as dmmEmail, ur.GMM_NAME as gmmName, ur.GMM_EMAIL as gmmEmail, ")
        	    .append(" cu.car_user_id as userId, cu.first_name as firstName, cu.last_name as lastName, cu.email_addr as emailAddress, ") 
        	    .append(" ca.attr_value as brandName, vs.vendor_number as vendorNumber ") 
		   	    .append(" from car c ")
				.append(" inner join department d on d.dept_id = c.dept_id ")
				.append(" inner join vendor_style vs on vs.vendor_style_id = c.vendor_style_id ")
				.append(" inner join car_user_department cud on c.dept_id = cud.dept_id ")
				.append(" inner join car_user cu on cu.car_user_id = cud.car_user_id")
				.append(" inner join users_rank ur on d.dept_cd = ur.department_code")
				.append(" left outer join car_attribute ca on ca.car_id = c.car_id and ca.attr_id in ")
				.append("(select a.attr_id from attribute a where a.blue_martini_attribute = 'Brand') ")
				.append(" where c.status_cd = 'ACTIVE'")
			    .append(" and c.assigned_to_user_type_cd in ('" + UserType.BUYER + "', '" + UserType.VENDOR + "')")
				.append(" and cu.user_type_cd ='" + UserType.BUYER + "'")
     			.append(" and trunc(current_date) >= trunc(c.due_date)")
     			.append(" and trunc(add_months(current_date,-4)) < trunc(c.due_date)")
				.append(" and cu.is_primary ='Y' ")
				.append(" and cu.status_cd = 'ACTIVE'")
				.append(" AND c.CURRENT_WORKFLOW_STATUS != 'CLOSED' ")
				//.append(" AND c.SOURCE_TYPE_CD != '"+ SourceType.OUTFIT +"' ")
				// Not including the Dropship CARS
				.append(" AND c.SOURCE_TYPE_CD not in ('"+ SourceType.OUTFIT +"','"+ SourceType.DROPSHIP +"') ")
				.append(" order by ur.GMM_EMAIL , ur.DMM_EMAIL, cu.car_user_id, c.dept_id");
		   	List resultSetList = null;
			SessionFactory sf = getHibernateTemplate().getSessionFactory();
			Session session = sf.getCurrentSession();
			System.out.println("query : "+ query);
			try {
				SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query.toString())
									.addEntity("c", Car.class)
							   		.addScalar("dmmName").addScalar("dmmEmail")
							   		.addScalar("gmmName").addScalar("gmmEmail")
							        .addScalar("userId").addScalar("firstName")
							        .addScalar("lastName").addScalar("emailAddress")
									.addScalar("brandName").addScalar("vendorNumber");
									
				resultSetList = sQuery.list();
			} catch (Exception e) {
				log.error("Hibernate Exception caught in getAllLateCars: "+ e.getMessage() + "\n"+ e.getStackTrace());
			}
			if (resultSetList == null) {
				resultSetList = new ArrayList();
			}
			return convertEmailDTOList(resultSetList);
	}
	

	private List<LateCarsSummaryDTO> convertEmailDTOList(List resultSet){
		List <LateCarsSummaryDTO> emailList = new ArrayList<LateCarsSummaryDTO>();
		for (Object obj : resultSet) {
			LateCarsSummaryDTO dto = new LateCarsSummaryDTO();
			Car car  = (Car) Array.get(obj, 0);
			String dmmName = (String) Array.get(obj, 1);
			String dmmEmail = (String) Array.get(obj, 2);
			String gmmName = (String) Array.get(obj, 3);
			String gmmEmail = (String) Array.get(obj, 4);
			java.math.BigDecimal bigUserId = (java.math.BigDecimal) Array.get(obj, 5);
			String firstName = (String) Array.get(obj, 6);
			String lastName = (String) Array.get(obj, 7);
			String emailAddress = (String) Array.get(obj, 8);
			String strBrandName = (String) Array.get(obj, 9);
			String strVendorNumber = (String) Array.get(obj, 10);
			
			Long userId = (bigUserId == null) ? null : bigUserId.longValue();
			dto.setCarId(car.getCarId());
			dto.setCompletionDate(car.getExpectedShipDate());
			dto.setCreatedDate(car.getCreatedDate());
			dto.setDeptCd(car.getDepartment().getDeptCd());
			dto.setSkuCount(car.getVendorSkus().size());
			dto.setVendorName(car.getVendorStyle().getVendor().getName());
			dto.setUserTypeCd(car.getAssignedToUserType().getUserTypeCd());
			dto.setUserId(userId.longValue());
			dto.setFirstName(firstName);
			dto.setLastName(lastName);
			dto.setStatusCd(car.getCurrentWorkFlowStatus().getStatusCd());
			dto.setEmail(emailAddress);
			dto.setDmmEmail(dmmEmail);
			dto.setDmmName(dmmName);
			dto.setGmmEmail(gmmEmail);
			dto.setGmmName(gmmName);
			dto.setBrandName(strBrandName);
			dto.setVendorNumber(strVendorNumber);
			emailList.add(dto);
		}
	return emailList;	
	}
	
	public List<LateCarsSummaryDTO> getAllLateCarsForOtherUser() {
		StringBuffer query = new StringBuffer();
		   query.append("select {c.*}, ")
        	    .append(" cu.car_user_id as userId, cu.first_name as firstName, cu.last_name as lastName, cu.email_addr as emailAddress, ") 
        	    .append(" ca.attr_value as brandName, vs.vendor_number as vendorNumber ") 
		   	    .append(" from car c ")
		   	    .append(" inner join vendor_style vs on vs.vendor_style_id = c.vendor_style_id ")
				.append(" inner join department d on d.dept_id = c.dept_id ")
				.append(" inner join car_user_department cud on c.dept_id = cud.dept_id ")
				.append(" inner join car_user cu on cu.car_user_id = cud.car_user_id")
				.append(" left outer join car_attribute ca on ca.car_id = c.car_id and ca.attr_id in ")
				.append("(select a.attr_id from attribute a where a.blue_martini_attribute = 'Brand') ")
				.append(" where c.status_cd = 'ACTIVE'")
			    .append(" and c.assigned_to_user_type_cd not in ('" + UserType.BUYER + "', '" + UserType.VENDOR + "')")
				.append(" and c.assigned_to_user_type_cd = cu.user_type_cd ")
     			.append(" and trunc(current_date) >= trunc(c.due_date)")
     			.append(" and trunc(add_months(current_date,-4)) < trunc(c.due_date)")
				.append(" and cu.is_primary ='Y' ")
				.append(" and cu.status_cd = 'ACTIVE'")
				.append(" AND c.CURRENT_WORKFLOW_STATUS != 'CLOSED' ")
				//.append(" AND c.SOURCE_TYPE_CD != '"+ SourceType.OUTFIT +"'")
				// Not including the Dropship CARS
			    .append(" AND c.SOURCE_TYPE_CD not in ('"+ SourceType.OUTFIT +"','"+ SourceType.DROPSHIP +"') ")
				.append(" order by cu.car_user_id, c.dept_id");
		   	List resultSetList = null;
			SessionFactory sf = getHibernateTemplate().getSessionFactory();
			Session session = sf.getCurrentSession();
			System.out.println("query getAllLateCarsForOtherUser: "+ query);
			try {
				SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query.toString())
									.addEntity("c", Car.class)
							        .addScalar("userId").addScalar("firstName")
							        .addScalar("lastName").addScalar("emailAddress")
									.addScalar("brandName").addScalar("vendorNumber");
									
				resultSetList = sQuery.list();
			} catch (Exception e) {
				log.error("Hibernate Exception caught in getAllLateCars: "+ e.getMessage() + "\n"+ e.getStackTrace());
			}
			if (resultSetList == null) {
				resultSetList = new ArrayList();
			}
			return convertEmailDTOListForOtherUser(resultSetList);
	}
	private List<LateCarsSummaryDTO> convertEmailDTOListForOtherUser(List resultSet){
		List <LateCarsSummaryDTO> emailList = new ArrayList<LateCarsSummaryDTO>();
		for (Object obj : resultSet) {
			LateCarsSummaryDTO dto = new LateCarsSummaryDTO();
			Car car  = (Car) Array.get(obj, 0);
			java.math.BigDecimal bigUserId = (java.math.BigDecimal) Array.get(obj, 1);
			String firstName = (String) Array.get(obj, 2);
			String lastName = (String) Array.get(obj, 3);
			String emailAddress = (String) Array.get(obj, 4);
			String strBrandName = (String) Array.get(obj, 5);
			String strVendorNumber = (String) Array.get(obj, 6);
			Long userId = (bigUserId == null) ? null : bigUserId.longValue();
			dto.setCarId(car.getCarId());
			dto.setCompletionDate(car.getExpectedShipDate());
			dto.setCreatedDate(car.getCreatedDate());
			dto.setDeptCd(car.getDepartment().getDeptCd());
			dto.setSkuCount(car.getVendorSkus().size());
			dto.setVendorName(car.getVendorStyle().getVendor().getName());
			dto.setUserTypeCd(car.getAssignedToUserType().getUserTypeCd());
			dto.setUserId(userId.longValue());
			dto.setFirstName(firstName);
			dto.setLastName(lastName);
			dto.setStatusCd(car.getCurrentWorkFlowStatus().getStatusCd());
			dto.setEmail(emailAddress);
			dto.setBrandName(strBrandName);
			dto.setVendorNumber(strVendorNumber);
			emailList.add(dto);
		}
	return emailList;	
	}
}
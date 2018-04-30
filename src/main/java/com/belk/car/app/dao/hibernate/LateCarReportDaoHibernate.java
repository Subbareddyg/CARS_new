package com.belk.car.app.dao.hibernate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.appfuse.dao.hibernate.UniversalDaoHibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

import com.belk.car.app.dao.LateCarReportDao;
import com.belk.car.app.dto.DmmDTO;
import com.belk.car.app.dto.GmmDTO;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarHistory;
import com.belk.car.app.model.report.BaseReport;
import com.belk.car.app.model.report.LateCarMonitoringData;
import com.belk.car.app.model.report.LateCarMonitoringReport;
import com.belk.car.util.DateUtils;

/**
 * Implements the LateCarDao interface.
 * Implemented to fetch the Late CAR monitoring report through CARS 
 * 
 * @author afusyq3-Syntel
 */
public class LateCarReportDaoHibernate extends UniversalDaoHibernate
implements LateCarReportDao {
	
	String strStartDueDate=null;
	String strEndDueDate=null;
	String strDeptNumber=null;
	boolean isUserDeptOnlySelected = false;
	boolean includeArchivedCars = false;
	StringBuffer sbQueryClause=null;
	
	/**
	 * Method to fetch the Late cars monitoring report data and Map it to DTO
	 */
	public LateCarMonitoringReport getLateCarMonitoringReport(BaseReport report) {
		
		if (report != null && report.getCriteria() != null
				&& !report.getCriteria().isEmpty()) {
				strStartDueDate = (String) report.getCriteria().get("StartDueDate");
				strEndDueDate = (String) report.getCriteria().get("EndDueDate");
				strDeptNumber=(String) report.getCriteria().get("DeptNumberText");
				isUserDeptOnlySelected = "true".equals((String) report.getCriteria().get("UserDepartmentOnly"));
				includeArchivedCars = "true".equals((String)report.getCriteria().get("IncludeArchiveCars"));
		}
		LateCarMonitoringReport summaryReport = new LateCarMonitoringReport() ;
		
		StringBuffer query = new StringBuffer();
		query
		.append("select {c.*}, {hist.*}, ")
		.append("v.name AS vendorName,")
		.append("v.vendor_number as vendorNumber,ca.ATTR_VALUE AS brand,")
		//.append("cu.first_name || ' ' || cu.last_name as assignedToUser,")
		.append("c.ASSIGNED_TO_USER_TYPE_CD as assignedToUser,")
		.append("urr.dmm_name as dmmName,urr.gmm_name as gmmName, dept.dept_Cd as deptCode, ")
		.append("(select count(*) from vendor_sku sku where c.car_id = sku.car_id) as skuCount ")
		.append(" from car_user_department cud inner join car c ON cud.dept_id=c.dept_id  ")
		.append(" inner join vendor_style vs on c.vendor_style_id = vs.vendor_style_id ")
		.append(" inner join vendor v on vs.vendor_id=v.vendor_id ")
		.append(" inner join car_user cu on cu.car_user_id=cud.car_user_id ")
		.append(" left outer join car_attribute ca on ca.car_id=c.car_id ")
		.append(" inner join attribute a on ca.attr_id=a.attr_id ")
		.append(" inner join department dept on c.dept_id=dept.dept_id ")
		.append(" inner join users_rank urr on dept.dept_cd= urr.department_code ")
		.append(" left outer join car_history hist on c.car_id = hist.car_id ");
		
		sbQueryClause=commonQuery(report);
		//Append the common query to current query
		query.append(sbQueryClause);
		query.append(" order by c.dept_id asc ");
		
		if(log.isDebugEnabled()){
			log.debug("Query for getLateCarMonitoringReport: " + query.toString());
		}
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sQuery = (SQLQuery) session.createSQLQuery(
					query.toString())
					.addEntity("c", Car.class )
					.addJoin("hist", "c.carHistories")
					.addScalar("vendorName", new StringType())
					.addScalar("vendorNumber", new StringType())
					.addScalar("brand", new StringType())
					.addScalar("assignedToUser", new StringType())
					.addScalar("dmmName", new StringType())
					.addScalar("gmmName", new StringType())
					.addScalar("deptCode", new StringType())
					.addScalar("skuCount", new IntegerType());
			System.out.println("sQuery====================>"+sQuery);	 	
			summaryReport = transformLateCarDTO(sQuery.list());
		}
		catch (Exception e) {
			e.printStackTrace();
			log.error("Hibernate Exception caught in getLateCarMonitoringReport: "
					+ e.getMessage());
			log.error("Query for getLateCarMonitoringReport: " + query.toString());
		}
		
		return summaryReport;
	}
	
	public StringBuffer commonQuery(BaseReport report){

		StringBuffer query=new StringBuffer();

		//Get the late cars for given department number
		if(StringUtils.isNotEmpty(strDeptNumber) && StringUtils.isNotBlank(strDeptNumber)) {
			query.append(" where dept.dept_cd="+strDeptNumber);
		}
		//Get the late cars for all the department numbers
		else if (isUserDeptOnlySelected){
			query.append(" where cud.dept_id = c.dept_id ");
		} 

		query.append(" and c.status_cd = 'ACTIVE' ");
		//Include Archive CARS in the report
		if(!includeArchivedCars){
			log.debug("Adding Check for archived car");
			query.append(" and c.archived = 'N'");
		}else{
			log.debug(" adding archived car in late cars report");
		}

		query.append(" and cud.car_user_id=").append(report.getCurrentUser().getId())
		.append(" and c.current_workflow_status <>'CLOSED' ")
		.append(" and c.source_type_cd IN ('PO','MANUAL')")
		.append(" and c.due_date < SYSDATE ");

		if (StringUtils.isNotBlank(strStartDueDate)&& StringUtils.isNotBlank(strEndDueDate)) {
			query.append(" and c.due_date BETWEEN to_date('"
					+ strStartDueDate + "','mm/dd/yyyy') AND to_date('"
					+ strEndDueDate + "','mm/dd/yyyy')");
		}
		else if (StringUtils.isBlank(strStartDueDate)&& StringUtils.isBlank(strEndDueDate)) {

			//Create Calendar instance
			Calendar now = Calendar.getInstance();
			String currentDate=(now.get(Calendar.MONTH) + 1 )+ "/" + 
					now.get(Calendar.DATE)+ "/" + now.get(Calendar.YEAR);
			//Get before four months date
			now.add(Calendar.MONTH, -4);
			String pastDate=(now.get(Calendar.MONTH) + 1 )+ "/" + 
					now.get(Calendar.DATE)+ "/" + now.get(Calendar.YEAR);

			//Retrieve the late CARS for the last four months
			query.append(" and c.due_date BETWEEN to_date('"
					+ pastDate + "','mm/dd/yyyy') AND to_date('"
					+ currentDate + "','mm/dd/yyyy')");
			
			/*	System.out.println("startDueDate:"+startDueDate);
				System.out.println("endDueDate:"+endDueDate);
				System.out.println("currentDate:"+currentDate);
				System.out.println("pastDate:"+pastDate);
			 */
		} 
		else if (StringUtils.isNotBlank(strStartDueDate)) {
			query.append(" and c.due_date >= to_date('"
					+ strStartDueDate + "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(strEndDueDate)) {
			query.append(" and c.due_date <= to_date('"
					+ strEndDueDate + "','mm/dd/yyyy')");
		}

		query.append(" and a.blue_martini_attribute = 'Brand' ");

		return query;
	}
	
	/**
	 * Method to map the late cars report data to DTO 
	 * @param resultset list
	 * @return list of DTO
	 */
	private LateCarMonitoringReport transformLateCarDTO(List<Object[]> list){
		
		log.debug("inside transformLateCarDTO method to map late cars report dta to DTO");
		LateCarMonitoringReport summaryReport = new LateCarMonitoringReport() ;
		List<LateCarMonitoringData> lateCarDTOList = new ArrayList<LateCarMonitoringData>();
		Map<String, GmmDTO> gmmDTOMap = new HashMap<String,GmmDTO>();
		Map<String, DmmDTO> dmmDTOMap = new HashMap<String,DmmDTO>();
		List<Long> carIdList = new ArrayList<Long>();
		long carId=0;
		
		for(Object obj : list){
			LateCarMonitoringData lateCarDTO = new LateCarMonitoringData();
			Car c = (Car) Array.get(obj, 0);
			carId =  c.getCarId();
			if(carIdList.contains(carId)){
				continue;
			}
			carIdList.add(Long.valueOf(carId));
			lateCarDTO.setCarId(carId);
			String strVendorName = (String) Array.get(obj, 1);
			String strVendorNumber = (String)Array.get(obj, 2);
			String strBrand = (String)Array.get(obj, 3);
			String strAssignedToUser = (String)Array.get(obj, 4);
			String strDmmName = (String)Array.get(obj, 5);
			String strGmmName = (String)Array.get(obj, 6);
			String strDeptCode = (String)Array.get(obj, 7);
			Integer intSkuCount = (Integer)Array.get(obj, 8);
			//Integer strSkuCount = 1;
			lateCarDTO.setVendorName(strVendorName);
			lateCarDTO.setVendorNumber(strVendorNumber);
			lateCarDTO.setBrand(strBrand);
			lateCarDTO.setAssignedToUser(strAssignedToUser);
			lateCarDTO.setDmmName(strDmmName);
			lateCarDTO.setGmmName(strGmmName);
			lateCarDTO.setDeptCd(strDeptCode);
			
			lateCarDTO.setWorkflowStatus(c.getWorkFlow().getStatusCode());
			lateCarDTO.setExpectedShipDate(c.getExpectedShipDateFormatted());
			lateCarDTO.setCarGeneratedDate(DateUtils.formatDate(c.getCreatedDate()));
			
			List<CarHistory> carHistorys = c.getCarHistories();
			
			//Looping through car history to count the number of days for each car status
			if(carHistorys != null){
				for(CarHistory hist: carHistorys ){
					String strUserType = hist.getAssignedToUserType().getUserTypeCd();
					String strWorkflow  = hist.getWorkflowStatus().getName();
					int intNumberOfDays = hist.getDaysWithUser();
					if(-1 == intNumberOfDays){
						Date createdDate = (Date)hist.getCreatedDate();
						long msDiff = new Date().getTime() - createdDate.getTime();
						intNumberOfDays = (int) (msDiff==0? 0 : msDiff/(24 * 60 * 60 * 1000));
					}
					if("BUYER".equalsIgnoreCase(strUserType)){
						if("INITIATED".equalsIgnoreCase(strWorkflow)){
							lateCarDTO.setDaysWithBuyerIntiated(intNumberOfDays + lateCarDTO.getDaysWithBuyerIntiated());
						}else if("READY_FOR_REVIEW".equalsIgnoreCase(strWorkflow)){
							lateCarDTO.setDaysWithBuyerReview(intNumberOfDays + lateCarDTO.getDaysWithBuyerReview());
						}else if ("WAITING_FOR_SAMPLE".equalsIgnoreCase(strWorkflow)){ 
							lateCarDTO.setDaysWithBuyerWaitingSamples(intNumberOfDays + lateCarDTO.getDaysWithBuyerWaitingSamples());
						}else if ("NEED_MORE_INFO".equalsIgnoreCase(strWorkflow)) {
							lateCarDTO.setDaysWithBuyerNeedMoreInfo(intNumberOfDays + lateCarDTO.getDaysWithBuyerNeedMoreInfo()); }
					}else if("VENDOR".equalsIgnoreCase(strUserType)){
						lateCarDTO.setDaysWithVendor(intNumberOfDays + lateCarDTO.getDaysWithVendor());
					}else if("ART_DIRECTOR".equalsIgnoreCase(strUserType)){
						lateCarDTO.setDaysWithArtDirector(intNumberOfDays + lateCarDTO.getDaysWithArtDirector());
					}else if("SAMPLE_COORDINATOR".equalsIgnoreCase(strUserType)){
						if("SAMPLE MANAGEMENT".equalsIgnoreCase(strWorkflow)){
						lateCarDTO.setDaysWithSampleCoordinator(intNumberOfDays + lateCarDTO.getDaysWithSampleCoordinator());
					}
					}else if("CONTENT_MANAGER".equalsIgnoreCase(strUserType)){
						if("APPROVED".equalsIgnoreCase(strWorkflow)){
						lateCarDTO.setDaysWithContentManager(intNumberOfDays + lateCarDTO.getDaysWithContentManager());
					}
					}else if("FINANCE".equalsIgnoreCase(strUserType)){
						lateCarDTO.setDaysWithFinance(intNumberOfDays + lateCarDTO.getDaysWithFinance());
					}else if("VENDOR_PROVIDED_IMAGE".equalsIgnoreCase(strUserType)){
						if("PENDING".equalsIgnoreCase(strWorkflow)){
						lateCarDTO.setDaysWithVendorProvidedImage(intNumberOfDays + lateCarDTO.getDaysWithVendorProvidedImage());
					}
					}				
				}
			}	
		  lateCarDTOList.add(lateCarDTO);
		  
		  DmmDTO dmm = dmmDTOMap.get(strDmmName);
		  if(null == dmm){
			  dmm = new DmmDTO();
		  }
		  //Adding the car and sku count to the DMM DTO
		  dmm.setLateCarsCount(1 + dmm.getLateCarsCount());
		  dmm.setSkuCount(dmm.getSkuCount() +intSkuCount);
		  dmm.setDmmName(strDmmName);
		  dmmDTOMap.put(strDmmName, dmm);	 
		  
		  GmmDTO gmm = gmmDTOMap.get(strGmmName);
		  if(null == gmm){
			  gmm = new GmmDTO();
		  }
		  
		//Adding the car and sku count to the GMM DTO
		  gmm.setLateCarsCount(1 + gmm.getLateCarsCount());
		  gmm.setSkuCount(gmm.getSkuCount() +intSkuCount);
		  gmm.setGmmName(strGmmName);
		  gmmDTOMap.put(strGmmName, gmm);
    	}	
		
		Collection<DmmDTO> dmm = new ArrayList<DmmDTO>();
		Collection<GmmDTO> gmm = new ArrayList<GmmDTO>();
		summaryReport.setLateCarMonitoringData(lateCarDTOList);
		if((dmm=dmmDTOMap.values()) != null && (gmm=gmmDTOMap.values()) != null){
			summaryReport.setDmmDTOData(new ArrayList<DmmDTO>(dmm));
			summaryReport.setGmmDTOData(new ArrayList<GmmDTO>(gmm));
		}
		if(log.isInfoEnabled()){
			log.info(" Number of late cars forund in late car monitoring report" + lateCarDTOList.size());
			log.info("number of DMM's in late car monitoring report: "+ dmm.size() );
			log.info("number of GMM's in late car monitoring report: "+ gmm.size() );
		}
	  return summaryReport;
	}

	
}

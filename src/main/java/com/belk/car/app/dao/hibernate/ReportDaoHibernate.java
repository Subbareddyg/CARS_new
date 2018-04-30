/**
 * Class Name : ReportDaoHibernate.java Version Information : v1.0 Date :
 * Copyright Notice : All rights are reserved to Syntel.
 */

package com.belk.car.app.dao.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.appfuse.dao.hibernate.UniversalDaoHibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.ShortType;
import org.hibernate.type.StringType;

import com.belk.car.app.dao.ReportDao;
import com.belk.car.app.model.report.BaseReport;
import com.belk.car.app.model.report.CarSummaryData;
import com.belk.car.app.model.report.CarSummaryStatisticsData;
import com.belk.car.app.model.view.AttributeView;
import com.belk.car.app.model.view.CarAttributeSummaryView;
import com.belk.car.app.model.view.CarAttributeView;
import com.belk.car.app.model.view.CarCmpStatusView;
import com.belk.car.app.model.view.CarExecutiveSummaryView;
import com.belk.car.app.model.view.CarPendingImagesView;
import com.belk.car.app.model.view.OMACostExceptions;
import com.belk.car.app.model.view.OMAHandlingFeeExceptions;
import com.belk.car.app.model.view.OMAHandlingFees;
import com.belk.car.app.model.view.OMARestockingFees;
import com.belk.car.app.model.view.OMAStylesSKUs;
import com.belk.car.app.model.view.VendorSetupByMonth;
import com.belk.car.app.model.view.VendorStyleHexView;

/**
 * Class ReportDaoHibernate implements the methods of ReportDao and responsible
 * for all the transactions for Report generation.
 * @author vsundar
 */
public class ReportDaoHibernate extends UniversalDaoHibernate
implements
ReportDao {

	@SuppressWarnings("unchecked")
	public List<CarExecutiveSummaryView> getCarExecutiveSummary(
			BaseReport report) {
		List<CarExecutiveSummaryView> data = new ArrayList<CarExecutiveSummaryView>();
		String startExpShipDate = null;
		String endExpShipDate = null;
		String startTurninDate = null;
		String endTurninDate = null;
		boolean userDepartmentOnly = false;
		boolean includeClosedCars = false;
		//Added for archive CAR
		boolean includeArchivedCars = false;
		String CAR_SAMPLE_COORDINATOR_TEMPLATE = "CAR Generate Sample Coordinator Template";
		String reportType = report.getName();
		String imageProvider = "(select distinct ip.image_provider_name from sample s inner join car_sample cs on cs.sample_id = s.sample_id inner join image_provider ip on ip.image_provider_id = s.image_provider_id where cs.car_id = c.car_id) as imageProvider, ";
		reportType = reportType == null ? "" : reportType;
		String imageProviderSQL = reportType
		.equals(CAR_SAMPLE_COORDINATOR_TEMPLATE) ? imageProvider : "";
		if (report != null && report.getCriteria() != null
				&& !report.getCriteria().isEmpty()) {
			startExpShipDate = (String) report.getCriteria().get(
			"StartExpectedShipDate");
			endExpShipDate = (String) report.getCriteria().get(
			"EndExpectedShipDate");
			startTurninDate = (String) report.getCriteria().get(
			"StartTurninDate");
			endTurninDate = (String) report.getCriteria().get("EndTurninDate");
			userDepartmentOnly = "true".equals((String) report.getCriteria()
					.get("UserDepartmentOnly"));
			includeClosedCars = "true".equals((String) report.getCriteria()
					.get("IncludeClosedCars"));
			includeArchivedCars = "true".equals((String)report.getCriteria().get("IncludeArchiveCars"));
		}
		StringBuffer query = new StringBuffer();
		query
		.append("with car_colors AS(select c.car_id as carId, ")
		.append( " listagg(vsku.color_name, ',') within group(order by vsku.color_code) as colorName,")
		.append( " listagg(vsku.color_code, ',') within group(order by vsku.color_code) as colorCode ")
		.append( "  FROM car c LEFT JOIN ( SELECT DISTINCT car_id, color_code, color_name FROM vendor_sku ) vsku   ")
		.append( "    ON c.car_id = vsku.car_id    GROUP BY c.car_id) ")
		.append("select distinct(c.car_id) as carId, ")
		.append("dept.dept_cd as deptCd, ")
		.append("dept.name as deptName, ")
		.append("to_char(cls.belk_class_number) as classNumber, ")
		.append("cls.name as className, ")
		.append("vs.vendor_number as vendorNumber,")
		.append("v.name as vendorName,")
		.append("vs.vendor_style_number as vendorStyleNumber,")
		.append("vs.vendor_style_name as vendorStyleName,")
		.append("vs.vendor_style_type_cd as vendorStyleTypeCd,")
		.append("ws.name as workflowStatus,")
		.append("ut.name as assignedToUser,")
		.append("c.expected_ship_date as expectedShipDate, ")
		.append("c.turnin_date as turninDate, ")
		.append(imageProviderSQL)
		.append(" decode(cc.colorName,'NO COLOR','','No Color','','no color','',null,'',cc.colorName) as colorName,")
		.append(" cc.colorCode,")
		.append(
		"(select count(car_sku_id) from vendor_sku where car_id = c.car_id) as numberOfSkus ")
		.append(
		" from car c inner join vendor_style vs on vs.vendor_style_id = c.vendor_style_id")
		.append(
		" inner join department dept on dept.dept_id = c.dept_id")
		.append(" inner join vendor v on v.vendor_id = vs.vendor_id")
		.append(" inner join classification cls on cls.class_id = vs.class_id ")		
		.append(
		" inner join workflow_status ws on ws.workflow_status_cd = c.current_workflow_status")
		.append(
		" inner join user_type ut on ut.user_type_cd = c.assigned_to_user_type_cd");
		if (userDepartmentOnly) {
			query
			.append(
			" inner join car_user_department cud on cud.dept_id = c.dept_id ")
			.append(" and cud.car_user_id=").append(
					report.getCurrentUser().getId());
		}
		query.append(" join car_colors cc on c.car_id = cc.carId");
		query.append(" where c.status_cd = 'ACTIVE'");
		if (!includeClosedCars) {
			query.append(" and c.current_workflow_status != 'CLOSED'");
		}
		//Add the check for include the archived cars in Report
		//If do not include archived CARs -- Selct CARS having archived status as 'N'
		//otherwise do nothing. this will automatically include all the cars.
		log.debug("IncludeArchivedCars:"+includeArchivedCars);
		if(!includeArchivedCars){
			log.debug("Adding Check");
			//means do not include the archived CARS
			query.append(" and c.archived = 'N'");
		}else{
			log.debug("Adding Nothing");
			//do nothing as report will automatically pull all the cars
		}
		if (StringUtils.isNotBlank(startExpShipDate)
				&& StringUtils.isNotBlank(endExpShipDate)) {
			query.append(" and c.expected_ship_date BETWEEN to_date('"
					+ startExpShipDate + "','mm/dd/yyyy') AND to_date('"
					+ endExpShipDate + "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(startExpShipDate)) {
			query.append(" and c.expected_ship_date >= to_date('"
					+ startExpShipDate + "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(endExpShipDate)) {
			query.append(" and c.expected_ship_date <= to_date('"
					+ endExpShipDate + "','mm/dd/yyyy')");
		}
		if (StringUtils.isNotBlank(startTurninDate)
				&& StringUtils.isNotBlank(endTurninDate)) {
			query.append(" and c.turnin_date BETWEEN to_date('"
					+ startTurninDate + "','mm/dd/yyyy') AND to_date('"
					+ endTurninDate + "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(startTurninDate)) {
			query.append(" and c.turnin_date >= to_date('" + startTurninDate
					+ "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(endTurninDate)) {
			query.append(" and c.turnin_date <= to_date('" + endTurninDate
					+ "','mm/dd/yyyy')");
		}
		// to include outfit cars in the report
		if (userDepartmentOnly) {
			query.append("union all ")
			
		.append("select distinct(c.car_id) as carId, ")
		.append("dept.dept_cd as deptCd, ")
		.append("dept.name as deptName, ")
		.append("to_char(cls.belk_class_number) as classNumber, ")
		.append("cls.name as className, ")
		.append("vs.vendor_number as vendorNumber,")
		.append("v.name as vendorName,")
		.append("vs.vendor_style_number as vendorStyleNumber,")
		.append("vs.vendor_style_name as vendorStyleName,")
		.append("vs.vendor_style_type_cd as vendorStyleTypeCd,")
		.append("ws.name as workflowStatus,")
		.append("ut.name as assignedToUser,")
		.append("c.expected_ship_date as expectedShipDate, ")
		.append("c.turnin_date as turninDate, ")
		.append(imageProviderSQL)
		.append(" decode(cc.colorName,'NO COLOR','','No Color','','no color','',null,'',cc.colorName) as colorName,")
		.append(" cc.colorCode,")
		.append(
		"(select count(car_sku_id) from vendor_sku where car_id = c.car_id) as numberOfSkus ")
		.append(
		" from car c inner join vendor_style vs on vs.vendor_style_id = c.vendor_style_id")
		.append(
		" inner join department dept on dept.dept_id = c.dept_id")
		.append(" inner join vendor v on v.vendor_id = vs.vendor_id")
		.append(" inner join classification cls on cls.class_id = vs.class_id ")		
		.append(
		" inner join workflow_status ws on ws.workflow_status_cd = c.current_workflow_status")
		.append(
		" inner join user_type ut on ut.user_type_cd = c.assigned_to_user_type_cd");		
		query.append(" join car_colors cc on c.car_id = cc.carId");
		query.append(" where c.status_cd = 'ACTIVE'");
		if (!includeClosedCars) {
			query.append(" and c.current_workflow_status != 'CLOSED'");
		}
		//Add the check for include the archived cars in Report
		//If do not include archived CARs -- Selct CARS having archived status as 'N'
		//otherwise do nothing. this will automatically include all the cars.
		log.debug("IncludeArchivedCars:"+includeArchivedCars);
		if(!includeArchivedCars){
			log.debug("Adding Check");
			//means do not include the archived CARS
			query.append(" and c.archived = 'N'");
		}else{
			log.debug("Adding Nothing");
			//do nothing as report will automatically pull all the cars
		}
		if (StringUtils.isNotBlank(startExpShipDate)
				&& StringUtils.isNotBlank(endExpShipDate)) {
			query.append(" and c.expected_ship_date BETWEEN to_date('"
					+ startExpShipDate + "','mm/dd/yyyy') AND to_date('"
					+ endExpShipDate + "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(startExpShipDate)) {
			query.append(" and c.expected_ship_date >= to_date('"
					+ startExpShipDate + "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(endExpShipDate)) {
			query.append(" and c.expected_ship_date <= to_date('"
					+ endExpShipDate + "','mm/dd/yyyy')");
		}
		if (StringUtils.isNotBlank(startTurninDate)
				&& StringUtils.isNotBlank(endTurninDate)) {
			query.append(" and c.turnin_date BETWEEN to_date('"
					+ startTurninDate + "','mm/dd/yyyy') AND to_date('"
					+ endTurninDate + "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(startTurninDate)) {
			query.append(" and c.turnin_date >= to_date('" + startTurninDate
					+ "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(endTurninDate)) {
			query.append(" and c.turnin_date <= to_date('" + endTurninDate
					+ "','mm/dd/yyyy')");
		}
		
		query.append(" and c.car_id in ")
		.append( "( select unique(coc.outfit_car_id) as car_id from  car_outfit_child coc inner join car c ON c.car_id = coc.CHILD_CAR_ID ") 
		.append(" inner join department d on d.dept_id = c.dept_id ")  
		.append(" inner join vendor_style vs on vs.vendor_style_id = c.vendor_style_id ") 
				.append(" inner join classification cls on cls.class_id = vs.class_id ")
						.append(" inner join car_user_department cud on cud.dept_id = c.dept_id  where coc.status_cd ='ACTIVE' and cud.car_user_id =")
						.append(report.getCurrentUser().getId())
						.append(" )");
		}
		query.append(" order by deptcd, expectedShipDate, carid");
		log.info("QUERY:" + query);
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sQuery = (SQLQuery) session.createSQLQuery(
					query.toString()).addScalar("carId", new LongType())
					.addScalar("deptCd", new StringType()).addScalar(
							"deptName", new StringType()).addScalar(
							"classNumber", new ShortType()).addScalar(
							"className", new StringType()).addScalar(
							"vendorNumber", new StringType()).addScalar(
							"vendorName", new StringType()).addScalar(
							"vendorStyleNumber", new StringType()).addScalar(
							"vendorStyleName", new StringType()).addScalar(
							//Added by AFUSYS3
							"colorName", new StringType()).addScalar(
							"colorCode", new StringType()).addScalar(
							"workflowStatus", new StringType()).addScalar(
							"assignedToUser", new StringType()).addScalar(
							"expectedShipDate", new DateType()).addScalar(
							"turninDate", new DateType())
					// .addScalar("imageProvider", new StringType()) //added by
					// Santosh
					.addScalar("numberOfSkus", new IntegerType());
			sQuery = reportType.equals(CAR_SAMPLE_COORDINATOR_TEMPLATE) ? sQuery
					.addScalar("imageProvider", new StringType())
					: sQuery;
					sQuery.setResultTransformer(Transformers
							.aliasToBean(CarExecutiveSummaryView.class));
					data = sQuery.list();
		}
		catch (Exception e) {
			log.error("Hibernate Exception caught in getCarExecutiveSummary: "
					+ e.getMessage());
			log.error("Query for getCarExecutiveSummary: " + query.toString());
		}
		return data;
	}

	@SuppressWarnings("unchecked")
	public List<CarSummaryData> getCarSummary(BaseReport report) {
		List<CarSummaryData> data = new ArrayList<CarSummaryData>();
		String startExpShipDate = null;
		String endExpShipDate = null;
		boolean userDepartmentOnly = false;
		//Added for archive CAR
		boolean includeArchivedCars = false;
		if (report != null && report.getCriteria() != null
				&& !report.getCriteria().isEmpty()) {
			startExpShipDate = (String) report.getCriteria().get(
			"StartExpectedShipDate");
			endExpShipDate = (String) report.getCriteria().get(
			"EndExpectedShipDate");
			userDepartmentOnly = "true".equals((String) report.getCriteria()
					.get("UserDepartmentOnly"));
			includeArchivedCars = "true".equals((String)report.getCriteria().get("IncludeArchiveCars"));
		}

		StringBuffer query = new StringBuffer();
		query
		.append(
		"select d.dept_cd deptCd, d.name deptName, c.current_workflow_status workflowStatusCd, ws.name workflowStatusName, count(*) count")
		.append(
		" from car c inner join vendor_style vs on vs.vendor_style_id = c.vendor_style_id")
		.append(
		" inner join classification cls on cls.class_id = vs.class_id")
		.append(" inner join department d on d.dept_id = c.dept_id")
		.append(
		" inner join workflow_status ws on ws.workflow_status_cd = c.current_workflow_status");

		if (userDepartmentOnly) {
			query
			.append(
			" inner join car_user_department cud on cud.dept_id = c.dept_id ")
			.append(" and cud.car_user_id=").append(
					report.getCurrentUser().getId());
		}

		query.append(" where c.status_cd = 'ACTIVE'");

		if (StringUtils.isNotBlank(startExpShipDate)
				&& StringUtils.isNotBlank(endExpShipDate)) {
			query.append(" and c.expected_ship_date BETWEEN to_date('"
					+ startExpShipDate + "','mm/dd/yyyy') AND to_date('"
					+ endExpShipDate + "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(startExpShipDate)) {
			query.append(" and c.expected_ship_date >= to_date('"
					+ startExpShipDate + "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(endExpShipDate)) {
			query.append(" and c.expected_ship_date <= to_date('"
					+ endExpShipDate + "','mm/dd/yyyy')");
		}
		//Add the check for include the archived cars in Report
		//If do not include archived CARs -- Select CARS having archived status as 'N'
		//otherwise do nothing. this will automatically include all the cars.
		log.debug("IncludeArchivedCars:"+includeArchivedCars);
		if(!includeArchivedCars){
			log.debug("Adding Check");
			//means do not include the archived CARS
			query.append(" and c.archived = 'N'");
		}else{
			log.debug("Adding Nothing");
			//do nothing as report will automatically pull all the cars
		}
		query
		.append(
		" group by d.dept_cd, d.name, c.current_workflow_status, ws.name")
		.append(" order by d.dept_cd, ws.name");

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sQuery = (SQLQuery) session.createSQLQuery(
					query.toString()).addScalar("deptCd").addScalar("deptName")
					.addScalar("workflowStatusCd").addScalar(
					"workflowStatusName").addScalar("count",
							new IntegerType()).setResultTransformer(
									Transformers.aliasToBean(CarSummaryData.class));
			data = sQuery.list();
		} catch (Exception e) {
			log.error("Hibernate Exception caught in getCarSummary: "
					+ e.getMessage());
		}

		return data;
	}

	@SuppressWarnings("unchecked")
	public List<CarSummaryStatisticsData> getCarSummaryStatistics(
			BaseReport report) {
		List<CarSummaryStatisticsData> data = new ArrayList<CarSummaryStatisticsData>();

		String startExpShipDate = null;
		String endExpShipDate = null;
		boolean userDepartmentOnly = false;
		if (report != null && report.getCriteria() != null
				&& !report.getCriteria().isEmpty()) {
			startExpShipDate = (String) report.getCriteria().get(
			"StartExpectedShipDate");
			endExpShipDate = (String) report.getCriteria().get(
			"EndExpectedShipDate");
			userDepartmentOnly = "true".equals((String) report.getCriteria()
					.get("UserDepartmentOnly"));
		}

		StringBuffer query = new StringBuffer();
		query
		.append(
		"select c.car_id as carId, c.expected_ship_date as expectedShipDate, dept.dept_cd as deptCd, vs.vendor_style_type_cd as vendorStyleTypeCd,")
		.append(
		"decode(c.content_status_cd,'IN-PROGRESS','Yes','No') as contentInProgress,")
		.append(
		"decode(c.current_workflow_status,'INITIATED','Yes','No') as initiated,")
		.append(
		"decode(c.current_workflow_status,'SAMPLE_MANAGEMENT','Yes',decode(c.ASSIGNED_TO_USER_TYPE_CD,'ART_DIRECTOR', 'Yes', 'No')) as finalized,")
		.append(
		"(select count(distinct vendor_style_id) from vendor_sku where car_id = c.car_id) as numProductsInCar,")
		.append(
		"(select count(car_sku_id) from vendor_sku where car_id = c.car_id) as numSkusInCar,")
		.append(
		"(select count(vendor_style_id) from vendor_style where parent_vendor_style_id = vs.vendor_style_id) as numProductsInPattern,")
		.append(
		"(select count(distinct vendor_style_id || '-' || color_code) from vendor_sku where car_id = c.car_id) as numProductColorsInCar,")
		.append(
		"(select count(s.sample_id) from sample s inner join car_sample cs on cs.sample_id = s.sample_id where cs.car_id = c.car_id and sample_source_type_cd != 'NEITHER') as numSamplesRequested,")
		.append(
		"(select count(s.sample_id) from sample s inner join car_sample cs on cs.sample_id = s.sample_id where cs.car_id = c.car_id and sample_source_type_cd!= 'NEITHER' and sample_tracking_status_cd = 'RECEIVED' ) as numSamplesReceived,")
		.append(
		"(select count(i.image_id) from image i inner join car_image ci on ci.image_id = i.image_id where ci.car_id = c.car_id and image_tracking_status_cd = 'REQUESTED') as numImagesRequested,")
		.append(
		"(select count(i.image_id) from image i inner join car_image ci on ci.image_id = i.image_id where ci.car_id = c.car_id and image_tracking_status_cd = 'APPROVED') as numImagesApproved,")
		.append(
		"(select count(i.image_id) from image i inner join car_image ci on ci.image_id = i.image_id where ci.car_id = c.car_id and image_tracking_status_cd = 'RECEIVED' and image_source_type_cd != 'FROM_SAMPLE') as numPickupImagesReceived,")
		.append(
		"(select count(i.image_id) from image i inner join car_image ci on ci.image_id = i.image_id where ci.car_id = c.car_id and image_tracking_status_cd = 'RECEIVED' and image_source_type_cd = 'FROM_SAMPLE') as numRRDImagesReceived")
		.append(
		" from car c inner join vendor_style vs on vs.vendor_style_id = c.vendor_style_id")
		.append(
		" inner join department dept on dept.dept_id = c.dept_id");

		if (userDepartmentOnly) {
			query
			.append(
			" inner join car_user_department cud on cud.dept_id = c.dept_id ")
			.append(" and cud.car_user_id=").append(
					report.getCurrentUser().getId());
		}

		query.append(" where c.status_cd = 'ACTIVE'");

		if (StringUtils.isNotBlank(startExpShipDate)
				&& StringUtils.isNotBlank(endExpShipDate)) {
			query.append(" and c.expected_ship_date BETWEEN to_date('"
					+ startExpShipDate + "','mm/dd/yyyy') AND to_date('"
					+ endExpShipDate + "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(startExpShipDate)) {
			query.append(" and c.expected_ship_date >= to_date('"
					+ startExpShipDate + "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(endExpShipDate)) {
			query.append(" and c.expected_ship_date <= to_date('"
					+ endExpShipDate + "','mm/dd/yyyy')");
		}

		query.append(" order by dept.dept_cd, c.expected_ship_date, c.car_id");

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sQuery = (SQLQuery) session
			.createSQLQuery(query.toString())
			.addScalar("carId", new LongType())
			.addScalar("deptCd")
			.addScalar("expectedShipDate", new DateType())
			.addScalar("vendorStyleTypeCd")
			.addScalar("contentInProgress")
			.addScalar("initiated")
			.addScalar("finalized")
			.addScalar("numProductsInCar", new IntegerType())
			.addScalar("numSkusInCar", new IntegerType())
			.addScalar("numProductsInPattern", new IntegerType())
			.addScalar("numProductColorsInCar", new IntegerType())
			.addScalar("numSamplesRequested", new IntegerType())
			.addScalar("numSamplesReceived", new IntegerType())
			.addScalar("numImagesRequested", new IntegerType())
			.addScalar("numImagesApproved", new IntegerType())
			.addScalar("numPickupImagesReceived", new IntegerType())
			.addScalar("numRRDImagesReceived", new IntegerType())
			.setResultTransformer(
					Transformers
					.aliasToBean(CarSummaryStatisticsData.class));
			data = sQuery.list();
		}
		catch (Exception e) {
			log.error("Hibernate Exception caught in getCarSummaryStatistics: "
					+ e.getMessage());
		}

		return data;
	}

	@SuppressWarnings("unchecked")
	public List<VendorStyleHexView> getVendorStyleHexValues() {
		List<VendorStyleHexView> data = new ArrayList<VendorStyleHexView>();
		StringBuffer query = new StringBuffer();
		query
		.append(
		"select vs.vendor_number vendorNumber, vs.vendor_style_number styleNumber, vsk.color_code colorCode, csa.attr_value hexValue")
		.append(" from vendor_sku vsk ")
		.append(
		" inner join car_sku_attribute csa on csa.car_sku_id = vsk.car_sku_id ")
		.append(
		" inner join vendor_style vs on vs.vendor_style_id = vsk.vendor_style_id ")
		.append(" where attr_id = 2992 ");
		// query.append(" and (attr_value is not null and attr_value != ' ' and color_code != '000' and length(attr_value) >= 6) ");
		query
		.append(" and (attr_value is not null and attr_value != ' ' and color_code != '000') ");
		query
		.append(" and (length(replace(attr_value, ' ',''))= 6 OR (length(replace(attr_value, ' ',''))>6 and substr(replace(attr_value, ' ',''),7,1) = ','))");
		query
		.append(" order by vs.vendor_number, vs.vendor_style_number, vsk.color_code");

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sQuery = (SQLQuery) session.createSQLQuery(
					query.toString()).addScalar("vendorNumber").addScalar(
					"styleNumber").addScalar("colorCode").addScalar("hexValue")
					.setResultTransformer(
							Transformers.aliasToBean(VendorStyleHexView.class));
			data = sQuery.list();
		} catch (Exception e) {
			log.error("Hibernate Exception caught in getVendorStyleHexValue: "
					+ e.getMessage());
		}

		return data;
	}

	@SuppressWarnings("unchecked")
	public List<AttributeView> getAttributes() {
		List<AttributeView> data = new ArrayList<AttributeView>();

		StringBuffer query = new StringBuffer();
		query
		.append(
		"select distinct attr.blue_martini_attribute attrName, attr.blue_martini_attribute attrValue")
		.append(" from attribute attr ");

		query.append(" where attr.status_cd = 'ACTIVE' ");
		query.append(" order by attr.blue_martini_attribute ");
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sQuery = (SQLQuery) session.createSQLQuery(
					query.toString()).addScalar("attrName").addScalar(
					"attrValue").setResultTransformer(
							Transformers.aliasToBean(AttributeView.class));
			data = sQuery.list();
		}
		catch (Exception e) {
			log.error("Hibernate Exception caught in getAttributes: "
					+ e.getMessage());
		}

		return data;
	}

	@SuppressWarnings("unchecked")
	public List<CarAttributeSummaryView> getCarAttributeSummaryValues(
			BaseReport report) {
		List<CarAttributeSummaryView> data = new ArrayList<CarAttributeSummaryView>();

		String startExpShipDate = null;
		String endExpShipDate = null;
		boolean userDepartmentOnly = false;
		//Added for archive CAR
		boolean includeArchivedCars = false;
		if (report != null && report.getCriteria() != null
				&& !report.getCriteria().isEmpty()) {
			startExpShipDate = (String) report.getCriteria().get(
			"StartExpectedShipDate");
			endExpShipDate = (String) report.getCriteria().get(
			"EndExpectedShipDate");
			userDepartmentOnly = "true".equals((String) report.getCriteria()
					.get("UserDepartmentOnly"));
			includeArchivedCars = "true".equals((String)report.getCriteria().get("IncludeArchiveCars"));
		}

		StringBuffer query = new StringBuffer();
		query
		.append(
		"select attr.blue_martini_attribute attrName, ca.attr_value attrValue, count(*) count")
		.append(
		" from car c inner join car_attribute ca on ca.car_id = c.car_id ")
		.append(
		" inner join attribute attr on attr.attr_id = ca.attr_id ");

		if (userDepartmentOnly) {
			query
			.append(
			" inner join car_user_department cud on cud.dept_id = c.dept_id ")
			.append(" and cud.car_user_id=").append(
					report.getCurrentUser().getId());
		}

		query
		.append(
		" where c.status_cd = 'ACTIVE' and attr.blue_martini_attribute = '")
		.append(report.getCriteria().get("AttributeName"))
		.append(
		"' and (ca.attr_value is not null and ca.attr_value != ' ' and ca.attr_value != 'None') ");

		if (StringUtils.isNotBlank(startExpShipDate)
				&& StringUtils.isNotBlank(endExpShipDate)) {
			query.append(" and c.expected_ship_date BETWEEN to_date('"
					+ startExpShipDate + "','mm/dd/yyyy') AND to_date('"
					+ endExpShipDate + "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(startExpShipDate)) {
			query.append(" and c.expected_ship_date >= to_date('"
					+ startExpShipDate + "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(endExpShipDate)) {
			query.append(" and c.expected_ship_date <= to_date('"
					+ endExpShipDate + "','mm/dd/yyyy')");
		}
		
		//Add the check for include the archived cars in Report
		//If do not include archived CARs -- Select CARS having archived status as 'N'
		//otherwise do nothing. this will automatically include all the cars.
		log.debug("IncludeArchivedCars:"+includeArchivedCars);
		if(!includeArchivedCars){
			log.debug("Adding Check");
			//means do not include the archived CARS
			query.append(" and c.archived = 'N'");
		}else{
			log.debug("Adding Nothing");
			//do nothing as report will automatically pull all the cars
		}
		query.append(" group by attr.blue_martini_attribute, ca.attr_value");
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sQuery = (SQLQuery) session
			.createSQLQuery(query.toString())
			.addScalar("attrName")
			.addScalar("attrValue")
			.addScalar("count", new IntegerType())
			.setResultTransformer(
					Transformers
					.aliasToBean(CarAttributeSummaryView.class));
			data = sQuery.list();
		}
		catch (Exception e) {
			log
			.error("Hibernate Exception caught in getCarAttributeSummaryView: "
					+ e.getMessage());
		}

		return data;
	}

	@SuppressWarnings("unchecked")
	public List<CarAttributeView> getCarAttributeValues(BaseReport report) {
		List<CarAttributeView> data = new ArrayList<CarAttributeView>();

		String startExpShipDate = null;
		String endExpShipDate = null;
		boolean userDepartmentOnly = false;
		//Added for archive CAR
		boolean includeArchivedCars = false;
		if (report != null && report.getCriteria() != null
				&& !report.getCriteria().isEmpty()) {
			startExpShipDate = (String) report.getCriteria().get(
			"StartExpectedShipDate");
			endExpShipDate = (String) report.getCriteria().get(
			"EndExpectedShipDate");
			userDepartmentOnly = "true".equals((String) report.getCriteria()
					.get("UserDepartmentOnly"));
			includeArchivedCars = "true".equals((String)report.getCriteria().get("IncludeArchiveCars"));
			
		}

		StringBuffer query = new StringBuffer();
		query
		.append(
		"select c.car_id carId, dept.dept_cd deptCd, to_char(class.belk_class_number) classNumber, pt.name productType, vs.vendor_number vendorNumber, v.name vendorName, vs.vendor_style_number styleNumber, vs.vendor_style_name styleName, attr.attr_id attrId, attr.name attrName, attr.blue_martini_attribute bmAttrName, ca.attr_value attrValue ")
		.append(
		" from car c inner join car_attribute ca on ca.car_id = c.car_id ")
		.append(
		" inner join attribute attr on attr.attr_id = ca.attr_id ")
		.append(
		" inner join department dept on dept.dept_id = c.dept_id ")
		.append(
		" inner join vendor_style vs on vs.vendor_style_id = c.vendor_style_id ")
		.append(
		" inner join classification class on class.class_id = vs.class_id ")
		.append(
		" inner join product_type pt on pt.product_type_id = vs.product_type_id ")
		.append(" inner join vendor v on v.vendor_id = vs.vendor_id ");

		if (userDepartmentOnly) {
			query
			.append(
			" inner join car_user_department cud on cud.dept_id = c.dept_id ")
			.append(" and cud.car_user_id=").append(
					report.getCurrentUser().getId());
		}

		query
		.append(
		" where c.status_cd = 'ACTIVE' and attr.blue_martini_attribute = '")
		.append(report.getCriteria().get("AttributeName"))
		.append(
		"' and (ca.attr_value is not null and ca.attr_value != ' ' and ca.attr_value != 'None') ");

		if (StringUtils.isNotBlank(startExpShipDate)
				&& StringUtils.isNotBlank(endExpShipDate)) {
			query.append(" and c.expected_ship_date BETWEEN to_date('"
					+ startExpShipDate + "','mm/dd/yyyy') AND to_date('"
					+ endExpShipDate + "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(startExpShipDate)) {
			query.append(" and c.expected_ship_date >= to_date('"
					+ startExpShipDate + "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(endExpShipDate)) {
			query.append(" and c.expected_ship_date <= to_date('"
					+ endExpShipDate + "','mm/dd/yyyy')");
		}
		//Add the check for include the archived cars in Report
		//If do not include archived CARs -- Select CARS having archived status as 'N'
		//otherwise do nothing. this will automatically include all the cars.
		log.debug("IncludeArchivedCars:"+includeArchivedCars);
		if(!includeArchivedCars){
			log.debug("Adding Check");
			//means do not include the archived CARS
			query.append(" and c.archived = 'N'");
		}else{
			log.debug("Adding Nothing");
			//do nothing as report will automatically pull all the cars
		}
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sQuery = (SQLQuery) session.createSQLQuery(
					query.toString()).addScalar("carId", new LongType())
					.addScalar("deptCd", new StringType()).addScalar(
							"classNumber", new ShortType()).addScalar(
							"productType", new StringType()).addScalar(
							"vendorNumber", new StringType()).addScalar(
							"vendorName", new StringType()).addScalar(
							"styleNumber", new StringType()).addScalar(
							"styleName", new StringType()).addScalar(
							"attrName", new StringType()).addScalar(
							"attrValue", new StringType()).addScalar(
							"bmAttrName", new StringType()).addScalar("attrId",
							new LongType()).setResultTransformer(
							Transformers.aliasToBean(CarAttributeView.class));
			data = sQuery.list();
		}
		catch (Exception e) {
			log.error("Hibernate Exception caught in getVendorStyleHexValue: "
					+ e.getMessage());
		}

		return data;
	}

	@SuppressWarnings("unchecked")
	public List<CarPendingImagesView> getCarPendingImages(BaseReport report) {
		List<CarPendingImagesView> data = new ArrayList<CarPendingImagesView>();

		String startExpShipDate = null;
		String endExpShipDate = null;
		boolean userDepartmentOnly = false;
		boolean onlyCompletedContent = false;
		boolean onlyOpenCars = false;
		if (report != null && report.getCriteria() != null
				&& !report.getCriteria().isEmpty()) {
			startExpShipDate = (String) report.getCriteria().get(
			"StartExpectedShipDate");
			endExpShipDate = (String) report.getCriteria().get(
			"EndExpectedShipDate");
			userDepartmentOnly = "true".equals((String) report.getCriteria()
					.get("UserDepartmentOnly"));
			onlyCompletedContent = "true".equals((String) report.getCriteria()
					.get("OnlyCompletedContent"));
			onlyOpenCars = "true".equals((String) report.getCriteria().get(
			"OnlyOpenCars"));
		}

		StringBuffer query = new StringBuffer();
		query
		.append(
		"select c.car_id as carId, d.dept_cd as deptCd, vs.vendor_number as vendorNumber, vs.vendor_style_number as styleNumber, ws.name as workflowStatus, c.expected_ship_date as expectedShipDate, cs.name as contentStatus,")
		.append(
		"(select distinct ip.image_provider_name from sample s inner join car_sample cs on cs.sample_id = s.sample_id inner join image_provider ip on ip.image_provider_id = s.image_provider_id where cs.car_id = c.car_id and sample_source_type_cd != 'NEITHER') imageProvider,")
		.append(
		"(select count(s.sample_id) from sample s inner join car_sample cs on cs.sample_id = s.sample_id where cs.car_id = c.car_id and sample_source_type_cd != 'NEITHER') numSampleRequested,")
		.append(
		"(select count(s.sample_id) from sample s inner join car_sample cs on cs.sample_id = s.sample_id where cs.car_id = c.car_id and sample_source_type_cd != 'NEITHER' and not exists (select 1 from image where image.sample_id = s.sample_id)) numSampleWithoutImage,")
		.append(
		"(select count(i.image_id) from image i inner join car_image ci on ci.image_id = i.image_id where ci.car_id = c.car_id and image_tracking_status_cd = 'RECEIVED' and image_source_type_cd = 'FROM_SAMPLE') as numSampleFromStudio,")
		.append(
		"(select count(i.image_id) from image i inner join car_image ci on ci.image_id = i.image_id where ci.car_id = c.car_id and image_tracking_status_cd NOT IN ('REJECTED') and image_source_type_cd != 'FROM_SAMPLE') as numPickupRequested,")
		.append(
		"(select count(i.image_id) from image i inner join car_image ci on ci.image_id = i.image_id where ci.car_id = c.car_id and image_tracking_status_cd = 'RECEIVED' and image_source_type_cd != 'FROM_SAMPLE') as numPickupReceived,")
		.append(
		"(select count(*) from vendor_sku where car_id = c.car_id) as numSkus")
		.append(" from car c ")
		.append(
		" inner join vendor_style vs on vs.vendor_style_id = c.vendor_style_id")
		.append(" inner join department d on d.dept_id = c.dept_id")
		.append(
		" inner join workflow_status ws on ws.workflow_status_cd = c.current_workflow_status")
		.append(
		" inner join content_status cs on cs.content_status_cd = c.content_status_cd");

		if (userDepartmentOnly) {
			query
			.append(
			" inner join car_user_department cud on cud.dept_id = c.dept_id ")
			.append(" and cud.car_user_id=").append(
					report.getCurrentUser().getId());
		}

		query.append(" where c.status_cd = 'ACTIVE'");

		if (StringUtils.isNotBlank(startExpShipDate)
				&& StringUtils.isNotBlank(endExpShipDate)) {
			query.append(" and c.expected_ship_date BETWEEN to_date('"
					+ startExpShipDate + "','mm/dd/yyyy') AND to_date('"
					+ endExpShipDate + "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(startExpShipDate)) {
			query.append(" and c.expected_ship_date >= to_date('"
					+ startExpShipDate + "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(endExpShipDate)) {
			query.append(" and c.expected_ship_date <= to_date('"
					+ endExpShipDate + "','mm/dd/yyyy')");
		}

		if (onlyOpenCars) {
			query.append(" and current_workflow_status != 'CLOSED'");
		}

		if (onlyCompletedContent) {
			query.append(" and c.content_status_cd != 'IN-PROGRESS'");
		}

		query.append(" order by d.dept_cd, c.expected_ship_date, c.car_id");

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sQuery = (SQLQuery) session.createSQLQuery(
					query.toString()).addScalar("carId", new LongType())
					.addScalar("deptCd", new StringType()).addScalar(
							"vendorNumber", new StringType()).addScalar(
							"styleNumber", new StringType()).addScalar(
							"workflowStatus", new StringType()).addScalar(
							"expectedShipDate", new DateType()).addScalar(
							"contentStatus", new StringType()).addScalar(
							"imageProvider", new StringType()).addScalar(
							"numSampleRequested", new IntegerType()).addScalar(
							"numSampleWithoutImage", new IntegerType())
					.addScalar("numSampleFromStudio", new IntegerType())
					.addScalar("numPickupRequested", new IntegerType())
					.addScalar("numPickupReceived", new IntegerType())
					.addScalar("numSkus", new IntegerType())
					.setResultTransformer(
							Transformers
									.aliasToBean(CarPendingImagesView.class));
			data = sQuery.list();
		}
		catch (Exception e) {
			log.error("Hibernate Exception caught in CarPendingImagesView: "
					+ e.getMessage());
		}

		return data;
	}

	@SuppressWarnings("unchecked")
	public List<CarCmpStatusView> getCarCmpStatus(BaseReport report) {
		List<CarCmpStatusView> data = new ArrayList<CarCmpStatusView>();

		String startExpShipDate = null;
		String endExpShipDate = null;
		boolean userDepartmentOnly = false;
		//Added for archive CAR
		boolean includeArchivedCars = false;
		if (report != null && report.getCriteria() != null
				&& !report.getCriteria().isEmpty()) {
			startExpShipDate = (String) report.getCriteria().get(
			"StartExpectedShipDate");
			endExpShipDate = (String) report.getCriteria().get(
			"EndExpectedShipDate");
			userDepartmentOnly = "true".equals((String) report.getCriteria()
					.get("UserDepartmentOnly"));
			includeArchivedCars = "true".equals((String)report.getCriteria().get("IncludeArchiveCars"));
			
		}

		List carsList = new ArrayList();
		StringBuffer cQuery = new StringBuffer();
		cQuery
				.append(
						"SELECT car_id FROM(SELECT cs.car_id, Count(DISTINCT s.image_provider_id)ct")
				.append(
						" FROM car_sample cs inner join sample s ON cs.sample_id = s.sample_id ")
				.append(" GROUP BY cs.car_id) WHERE ct>1");

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery tQuery = (SQLQuery) session.createSQLQuery(cQuery
					.toString());
			carsList = tQuery.list();
		} catch (Exception e) {
			log.error("Hibernate Exception caught in CarCmpStatusView: "
					+ e.getMessage());
		}

		StringBuffer query = new StringBuffer();
		query
		.append(
		"select c.car_id as carId, d.dept_cd as deptCd, vs.vendor_number as vendorNumber, vs.vendor_style_number as styleNumber, ws.name as workflowStatus, c.expected_ship_date as expectedShipDate, cs.name as contentStatus,")
		.append(
		"(select distinct ip.image_provider_name from sample s inner join car_sample cs on cs.sample_id = s.sample_id inner join image_provider ip on ip.image_provider_id = s.image_provider_id where cs.car_id = c.car_id and sample_source_type_cd != 'NEITHER') imageProvider,")
		.append(
		"(select count(s.sample_id) from sample s inner join car_sample cs on cs.sample_id = s.sample_id where cs.car_id = c.car_id and sample_source_type_cd != 'NEITHER') numSampleRequested,")
		.append(
		"(select count(s.sample_id) from sample s inner join car_sample cs on cs.sample_id = s.sample_id where cs.car_id = c.car_id and sample_source_type_cd != 'NEITHER' and not exists (select 1 from image where image.sample_id = s.sample_id)) numSampleWithoutImage,")
		.append(
		"(select count(i.image_id) from image i inner join car_image ci on ci.image_id = i.image_id where ci.car_id = c.car_id and image_tracking_status_cd = 'RECEIVED' and image_source_type_cd = 'FROM_SAMPLE') as numSampleFromStudio,")
		.append(
		"(select count(i.image_id) from image i inner join car_image ci on ci.image_id = i.image_id where ci.car_id = c.car_id and image_tracking_status_cd NOT IN ('REJECTED') and image_source_type_cd != 'FROM_SAMPLE') as numPickupRequested,")
		.append(
		"(select count(i.image_id) from image i inner join car_image ci on ci.image_id = i.image_id where ci.car_id = c.car_id and image_tracking_status_cd = 'RECEIVED' and image_source_type_cd != 'FROM_SAMPLE') as numPickupReceived,")
		.append(
		"(select count(*) from vendor_sku where car_id = c.car_id) as numSkus,")
		//new code added for inventory column----Siddhi
					.append("(SELECT sum(ON_HAND_QTY) FROM tmp_sku_avail bia inner join vendor_sku vsku on vsku.belk_sku = bia.sku WHERE car_id=c.car_id) as inv, ")
					//new code ends
		.append(
		"decode(cmp_task.status_cd, 'A','Active','I','Inactive', cmp_task.status_cd) as cmpStatus,")
		.append(
		"decode(cmp_task.approved,'T','Yes','No') as approvalStatus,")
		.append("cmp_task.first_publish_dt as firstPublishedDate,")
		.append("cmp_task.last_publish_dt as lastPublishedDate,")
		.append("cmp_task.parent_task as parentTask,")
		.append(
		"cmp_task.parent_task_create_dt as parentTaskCreateDate,")
		.append(
		"cmp_task.parent_task_last_modify_dt as parentTaskLastUpdateDate,")
		.append("cmp_task.parent_task_status as parentTaskStatus,")
		.append("cmp_task.copy_task as copyTask,")
		.append("cmp_task.copy_task_create_dt as copyTaskCreateDate,")
		.append(
		"cmp_task.copy_task_last_modify_dt as copyTaskLastUpdateDate,")
		.append("cmp_task.copy_task_status as copyTaskStatus,")
		.append("cmp_task.copy_task_assigned_to as copyTaskAssignedTo,")
		.append("cmp_task.image_task  as imageTask,")
		.append("cmp_task.image_task_create_dt as imageTaskCreateDate,")
		.append(
		"cmp_task.image_task_last_modify_dt as imageTaskLastUpdateDate,")
		.append("cmp_task.image_task_status as imageTaskStatus,")
		.append(
		"cmp_task.image_task_assigned_to as imageTaskAssignedTo")
		.append(" from car c ")
		.append(
		" inner join vendor_style vs on vs.vendor_style_id = c.vendor_style_id")
		.append(" inner join department d on d.dept_id = c.dept_id")
		.append(
		" inner join workflow_status ws on ws.workflow_status_cd = c.current_workflow_status")
		.append(
		" inner join content_status cs on cs.content_status_cd = c.content_status_cd")
		.append(
		" left outer join v_consolidated_prod_tasks@belkcesp cmp_task on cmp_task.productcode = (vs.vendor_number || vs.vendor_style_number)");

		if (userDepartmentOnly) {
			query
			.append(
			" inner join car_user_department cud on cud.dept_id = c.dept_id ")
			.append(" and cud.car_user_id=").append(
					report.getCurrentUser().getId());
		}

		query.append(" where c.status_cd = 'ACTIVE' ");

		if (StringUtils.isNotBlank(startExpShipDate)
				&& StringUtils.isNotBlank(endExpShipDate)) {
			query.append(" and c.expected_ship_date BETWEEN to_date('"
					+ startExpShipDate + "','mm/dd/yyyy') AND to_date('"
					+ endExpShipDate + "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(startExpShipDate)) {
			query.append(" and c.expected_ship_date >= to_date('"
					+ startExpShipDate + "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(endExpShipDate)) {
			query.append(" and c.expected_ship_date <= to_date('"
					+ endExpShipDate + "','mm/dd/yyyy')");
		}
		//Add the check for include the archived cars in Report
		//If do not include archived CARs -- Select CARS having archived status as 'N'
		//otherwise do nothing. this will automatically include all the cars.
		log.debug("IncludeArchivedCars:"+includeArchivedCars);
		if(!includeArchivedCars){
			log.debug("Adding Check");
			//means do not include the archived CARS
			query.append(" and c.archived = 'N'");
		}else{
			log.debug("Adding Nothing");
			//do nothing as report will automatically pull all the cars
		}
		
		if (carsList != null) {
			Iterator it = carsList.iterator();
			int count = 0;
			StringBuffer subq = new StringBuffer();

			while (it.hasNext()) {
				if (count == 0) {
					query.append(" and c.car_id NOT IN (");
					subq.append("and c.car_id IN (");
				} else {
					query.append(",");
					subq.append(",");
				}
				BigDecimal car_id = (BigDecimal) (BigDecimal) it.next();
				query.append(car_id.toString());
				subq.append(car_id.toString());
				count++;
			}
			query.append(")");
			subq.append(")");
			query.append(" UNION ");
			query
					.append(
							"select c.car_id as carId, d.dept_cd as deptCd, vs.vendor_number as vendorNumber, vs.vendor_style_number as styleNumber, ws.name as workflowStatus, c.expected_ship_date as expectedShipDate, cs.name as contentStatus,")
					.append(
							"(select 'RR Donnelly Studio and Belk Pineville Studio' from dual) imageProvider,")
					.append(
							"(select count(s.sample_id) from sample s inner join car_sample cs on cs.sample_id = s.sample_id where cs.car_id = c.car_id and sample_source_type_cd != 'NEITHER') numSampleRequested,")
					.append(
							"(select count(s.sample_id) from sample s inner join car_sample cs on cs.sample_id = s.sample_id where cs.car_id = c.car_id and sample_source_type_cd != 'NEITHER' and not exists (select 1 from image where image.sample_id = s.sample_id)) numSampleWithoutImage,")
					.append(
							"(select count(i.image_id) from image i inner join car_image ci on ci.image_id = i.image_id where ci.car_id = c.car_id and image_tracking_status_cd = 'RECEIVED' and image_source_type_cd = 'FROM_SAMPLE') as numSampleFromStudio,")
					.append(
							"(select count(i.image_id) from image i inner join car_image ci on ci.image_id = i.image_id where ci.car_id = c.car_id and image_tracking_status_cd NOT IN ('REJECTED') and image_source_type_cd != 'FROM_SAMPLE') as numPickupRequested,")
					.append(
							"(select count(i.image_id) from image i inner join car_image ci on ci.image_id = i.image_id where ci.car_id = c.car_id and image_tracking_status_cd = 'RECEIVED' and image_source_type_cd != 'FROM_SAMPLE') as numPickupReceived,")
					.append(
							"(select count(*) from vendor_sku where car_id = c.car_id) as numSkus,")
							//new code added for inventory column----Siddhi
					.append("(SELECT sum(ON_HAND_QTY) FROM tmp_sku_avail bia inner join vendor_sku vsku on vsku.belk_sku = bia.sku WHERE car_id=c.car_id) as inv, ")
					//new code ends
					.append(
							"decode(cmp_task.status_cd, 'A','Active','I','Inactive', cmp_task.status_cd) as cmpStatus,")
					.append(
							"decode(cmp_task.approved,'T','Yes','No') as approvalStatus,")
					.append("cmp_task.first_publish_dt as firstPublishedDate,")
					.append("cmp_task.last_publish_dt as lastPublishedDate,")
					.append("cmp_task.parent_task as parentTask,")
					.append(
							"cmp_task.parent_task_create_dt as parentTaskCreateDate,")
					.append(
							"cmp_task.parent_task_last_modify_dt as parentTaskLastUpdateDate,")
					.append("cmp_task.parent_task_status as parentTaskStatus,")
					.append("cmp_task.copy_task as copyTask,")
					.append(
							"cmp_task.copy_task_create_dt as copyTaskCreateDate,")
					.append(
							"cmp_task.copy_task_last_modify_dt as copyTaskLastUpdateDate,")
					.append("cmp_task.copy_task_status as copyTaskStatus,")
					.append(
							"cmp_task.copy_task_assigned_to as copyTaskAssignedTo,")
					.append("cmp_task.image_task  as imageTask,")
					.append(
							"cmp_task.image_task_create_dt as imageTaskCreateDate,")
					.append(
							"cmp_task.image_task_last_modify_dt as imageTaskLastUpdateDate,")
					.append("cmp_task.image_task_status as imageTaskStatus,")
					.append(
							"cmp_task.image_task_assigned_to as imageTaskAssignedTo")
					.append(" from car c ")
					.append(
							" inner join vendor_style vs on vs.vendor_style_id = c.vendor_style_id")
					.append(" inner join department d on d.dept_id = c.dept_id")
					.append(
							" inner join workflow_status ws on ws.workflow_status_cd = c.current_workflow_status")
					.append(
							" inner join content_status cs on cs.content_status_cd = c.content_status_cd")
					.append(
							" left outer join v_consolidated_prod_tasks@belkcesp cmp_task on cmp_task.productcode = (vs.vendor_number || vs.vendor_style_number)");

			if (userDepartmentOnly) {
				query
						.append(
								" inner join car_user_department cud on cud.dept_id = c.dept_id ")
						.append(" and cud.car_user_id=").append(
								report.getCurrentUser().getId());
			}

			query.append(" where c.status_cd = 'ACTIVE' ");

			if (StringUtils.isNotBlank(startExpShipDate)
					&& StringUtils.isNotBlank(endExpShipDate)) {
				query.append(" and c.expected_ship_date BETWEEN to_date('"
						+ startExpShipDate + "','mm/dd/yyyy') AND to_date('"
						+ endExpShipDate + "','mm/dd/yyyy')");
			} else if (StringUtils.isNotBlank(startExpShipDate)) {
				query.append(" and c.expected_ship_date >= to_date('"
						+ startExpShipDate + "','mm/dd/yyyy')");
			} else if (StringUtils.isNotBlank(endExpShipDate)) {
				query.append(" and c.expected_ship_date <= to_date('"
						+ endExpShipDate + "','mm/dd/yyyy')");
			}

			query.append(subq.toString());
		}

		query.append(" order by deptCd, expectedShipDate, carId");
		

		try {
			SQLQuery sQuery = (SQLQuery) session.createSQLQuery(
					query.toString()).addScalar("carId", new LongType())
					.addScalar("deptCd", new StringType()).addScalar(
							"vendorNumber", new StringType()).addScalar(
							"styleNumber", new StringType()).addScalar(
							"workflowStatus", new StringType()).addScalar(
							"expectedShipDate", new DateType()).addScalar(
							"contentStatus", new StringType()).addScalar(
							"imageProvider", new StringType()).addScalar(
							"numSampleRequested", new IntegerType()).addScalar(
							"numSampleWithoutImage", new IntegerType())
					.addScalar("numSampleFromStudio", new IntegerType())
					.addScalar("numPickupRequested", new IntegerType())
					.addScalar("numPickupReceived", new IntegerType())
					.addScalar("numSkus", new IntegerType()).addScalar(
							"inv", new StringType()).addScalar(
							"cmpStatus", new StringType()).addScalar(
							"approvalStatus", new StringType()).addScalar(
							"firstPublishedDate", new DateType()).addScalar(
							"lastPublishedDate", new DateType()).addScalar(
							"parentTask", new StringType()).addScalar(
							"parentTaskCreateDate", new DateType()).addScalar(
							"parentTaskLastUpdateDate", new DateType())
					.addScalar("parentTaskStatus", new StringType()).addScalar(
							"copyTask", new StringType()).addScalar(
							"copyTaskCreateDate", new DateType()).addScalar(
							"copyTaskLastUpdateDate", new DateType())
					.addScalar("copyTaskStatus", new StringType()).addScalar(
							"copyTaskAssignedTo", new StringType()).addScalar(
							"imageTask", new StringType()).addScalar(
							"imageTaskCreateDate", new DateType()).addScalar(
							"imageTaskLastUpdateDate", new DateType())
					.addScalar("imageTaskStatus", new StringType()).addScalar(
							"imageTaskAssignedTo", new StringType())
					.setResultTransformer(
							Transformers.aliasToBean(CarCmpStatusView.class));
			data = sQuery.list();
		} catch (Exception e) {
			log.error("Hibernate Exception caught in CarCmpStatusView: "
					+ e.getMessage());
		}

		return data;
	}
		// =============== New Method Added by Syntel For Dropship =============//
		/**
		 * @param report
		 * @return List of VendorSetupByMonth 
		 * This method will get all the data for the report VendorSetupByMonth.
		 */

		@SuppressWarnings("unchecked")
		public List<VendorSetupByMonth> getVendorSetupByMonth(BaseReport report) {
			List<VendorSetupByMonth> vendorSetupByMonthList = 
				new ArrayList<VendorSetupByMonth>();
			SessionFactory sessionFactory = getHibernateTemplate()
			.getSessionFactory();
			Session session = sessionFactory.getCurrentSession();
			StringBuilder query = new StringBuilder();

			query.append(" SELECT FM.DESCR AS fullfilmentMethodName,")
			.append(" FS.FULFMNT_SERVICE_NAME AS fullfilmentSeviceName,")
			.append(" V.NAME AS vendorName ,V.VENDOR_NUMBER AS vendorNbr ,")
			.append(" FSV.CREATED_DATE AS createdDate")
			.append(" FROM FULFMNT_SERVICE_VENDOR FSV, FULFMNT_SERVICE FS ,")
			.append(" FULFMNT_METHOD FM , VENDOR V  WHERE ")
			.append(" FSV.FULFMNT_SERVICE_ID = FS.FULFMNT_SERVICE_ID")
			.append(" AND FS.FULFMNT_METHOD_CD =  FM.FULFMNT_METHOD_CD")
			.append(" AND FSV.VENDOR_ID  = V.VENDOR_ID ");	 
			// Appending where clause
			query = appendDateWhereClause(report, query, "FSV");
			query.append(" ORDER BY FM.DESCR , FS.FULFMNT_SERVICE_NAME");
			log.info(" SQL Query Vendor Setup By Month" + query.toString());
			log.debug("Report: Before Getting the records for Vendor Setup By Month time :" + System.currentTimeMillis());
			try {
				SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query
						.toString());
				sQuery = addCommonScalar(sQuery);
				sQuery.addScalar("createdDate", new DateType())
				.setResultTransformer(
						Transformers.aliasToBean(VendorSetupByMonth.class));
				vendorSetupByMonthList = sQuery.list();
			}
			catch (Exception e) {
				log.error("Hibernate Exception caught in VendorSetupByMonth: "
						+ e.getMessage());

			}
			log.debug("Report: After Getting the records for Vendor Setup By Month time :" + System.currentTimeMillis());
			return vendorSetupByMonthList;
		}

		/**
		 * @param report
		 * @return List of OMAStylesSKUs 
		 * This method will get all the data for the report OMA Styles/SKUs available through Drop Ship.
		 */

		@SuppressWarnings("unchecked")
		public List<OMAStylesSKUs> getOMAStylesSKUs(BaseReport report) {
			List<OMAStylesSKUs> omaStylesSkusList = new ArrayList<OMAStylesSKUs>();
			SessionFactory sessionFactory = getHibernateTemplate()
			.getSessionFactory();
			Session session = sessionFactory.getCurrentSession();
			StringBuilder query = new StringBuilder();
			query.append("SELECT DISTINCT FM.DESCR AS fullfilmentMethodName, FS.FULFMNT_SERVICE_NAME AS fullfilmentSeviceName,  ")
			.append("V.NAME AS vendorName ,")
			.append("V.VENDOR_NUMBER AS vendorNbr, nvl(T3.totalActiveSKUs,0)AS totalActiveSKUs, " +
			"nvl(T3.totalActiveStyles,0)AS  ").append("totalActiveStyles FROM  ")

			.append("( SELECT  T1.VENDOR_ID,totalActiveStyles,totalActiveSKUs  FROM ")
			.append("  ( SELECT  vendor_id , Count(*) AS totalActiveStyles FROM (")
			.append("    SELECT  vendor_id,vendor_style_id  ")
			.append("    from VIFR_STYLE V1, VNDR_ITEM_FULFMNT_RQST V2 ")
			.append("     WHERE  V1.VNDR_ITEM_FULFMNT_RQST_ID=V2.VNDR_ITEM_FULFMNT_RQST_ID ")
			.append("     AND STATUS_CD='ACTIVE' AND   v1.vndr_id=v2.vendor_id  ");
			query=appendDateWhereClause(report, query, "V1") 
			.append("     group BY  vendor_id ,vendor_style_id   ")
			.append("    ) ")
			.append(" GROUP BY vendor_id )T1 ,")
			.append("   (SELECT vendor_id , Count(*) AS totalActiveSKUs   from")
			.append("(SELECT VENDOR_ID,belk_upc")
			.append("   FROM  VIFR_STYLE_SKU V1,VNDR_ITEM_FULFMNT_RQST V2")
			.append("    WHERE v1.VNDR_ITEM_FULFMNT_RQST_ID=V2.VNDR_ITEM_FULFMNT_RQST_ID ")
			.append("    AND STATUS_CD ='ACTIVE'  " );
			query=appendDateWhereClause(report, query, "V1")
			.append(" GROUP BY V2.VENDOR_ID , belk_upc)")
			.append("     group BY vendor_id")
			.append(")T2 ")
			.append("   WHERE T1.vendor_id = T2.vendor_id(+) ) T3 , ")

			.append("   VNDR_ITEM_FULFMNT_RQST VIFR, FULFMNT_METHOD FM, FULFMNT_SERVICE FS , VENDOR V  ")
			.append("    WHERE T3.VENDOR_ID = VIFR.VENDOR_ID  AND VIFR.VENDOR_ID = V.VENDOR_ID")
			.append(" AND FS.FULFMNT_SERVICE_ID=VIFR.FULFMNT_SERVICE_ID AND FS.FULFMNT_METHOD_CD =" +
			"  FM.FULFMNT_METHOD_CD ORDER ") .append("BY fullfilmentMethodName,fullfilmentSeviceName "); 

			log.info(" SQL Query Style/SKUSs " + query.toString());
			log.debug("Report: Before Getting the records for SQL Query Style/SKUSs  time :" + System.currentTimeMillis());
			try {
				SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query
						.toString());
				sQuery = addCommonScalar(sQuery);
				sQuery.addScalar("totalActiveSKUs", new LongType()).addScalar(
						"totalActiveStyles", new LongType()).setResultTransformer(
								Transformers.aliasToBean(OMAStylesSKUs.class));
				omaStylesSkusList = sQuery.list();
			}
			catch (Exception e) {

				log.error("Hibernate Exception caught in OMAStylesSkus: "
						+ e.getMessage());
			}
			log.debug("Report: After Getting the records for SQL Query Style/SKUSs time :" + System.currentTimeMillis());
			return omaStylesSkusList;


		}

		/**
		 * @param report
		 * @return List of OMAHandlingFees 
		 * This method will get all the data for the report OMA Handling Fees by Vendor.
		 */
		@SuppressWarnings("unchecked")
		public List<OMAHandlingFees> getOMAHandlingFees(BaseReport report) {

			List<OMAHandlingFees> omaHandlingFeesList = 
				new ArrayList<OMAHandlingFees>();
			SessionFactory sessionFactory = getHibernateTemplate()
			.getSessionFactory();
			Session session = sessionFactory.getCurrentSession();
			StringBuilder query = new StringBuilder(); // Need to change Query
			query.append(" SELECT DISTINCT FM.DESCR AS fullfilmentMethodName,")
			.append(" FS.FULFMNT_SERVICE_NAME AS fullfilmentSeviceName,")
			.append(" V.NAME AS vendorName ,V.VENDOR_NUMBER AS vendorNbr,")
			.append(" VF.PER_ITEM_AMOUNT AS perItemAmount,")
			.append(" VF.PER_ORDER_AMOUNT AS perOrderAmount, ")
			.append(" T1.effectiveDate AS effectiveDate ")
			.append(" FROM VNDR_FULFMNT_SERV_FEE VFSF,")

			.append("(SELECT FSV.VENDOR_ID AS VENDOR_ID ,")
			.append(" Max(VFR.EFFECTIVE_DATE)AS effectiveDate,")
			.append(" Max(VENDOR_FEE_RQST_ID) AS VENDOR_FEE_RQST_ID ,")
			.append(" FSV.FULFMNT_SERVICE_ID ")
			.append(" FROM VNDR_FULFMNT_SERV_FEE VFSF,VENDOR_FEE_RQST VFR,")
			.append(" FULFMNT_SERVICE_VENDOR FSV ")
			.append(" WHERE  VFSF.VNDR_FEE_REQUEST_ID =  VFR.VENDOR_FEE_RQST_ID")
			.append(" AND VFSF.VENDOR_ID = FSV.VENDOR_ID AND VFSF.VENDOR_ID = FSV.VENDOR_ID")
			.append(" AND FSV.STATUS_CD='ACTIVE' ")
			.append(" GROUP BY FSV.VENDOR_ID,To_Char(VFR.EFFECTIVE_DATE,'DD-MON-YYYY'),")
			.append(" FSV.FULFMNT_SERVICE_ID ")
			.append(" ORDER BY  FSV.VENDOR_ID) T1,")
			// End of Inner select query
			.append(" VNDR_FEE VF,FULFMNT_METHOD FM,")
			.append(" FULFMNT_SERVICE FS ,VENDOR V ")

			// where clause
			.append(" WHERE  VFSF.VNDR_FEE_REQUEST_ID=T1.VENDOR_FEE_RQST_ID")
			.append(" AND T1.FULFMNT_SERVICE_ID=FS.FULFMNT_SERVICE_ID")
			.append(" AND FS.FULFMNT_METHOD_CD =  FM.FULFMNT_METHOD_CD")
			.append(" AND VFSF.VENDOR_FEE_ID =VF.VNDR_FEE_ID")
			.append(" AND  VF.FEE_CD=1 ")
			.append(" AND (PER_ORDER_AMOUNT>0 OR  PER_ITEM_AMOUNT >0)")
			.append(" AND T1.VENDOR_ID= V.VENDOR_ID")
			.append(" ORDER BY fullfilmentMethodName,fullfilmentSeviceName,")
			.append(" vendorName,effectiveDate");

			log.info(" SQL Query OMA Handling Fees " + query.toString());
			log.debug("Report: Before Getting the records for OMA Handling Fees  time :" + System.currentTimeMillis());
			try {
				SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query
						.toString());
				sQuery = addCommonScalar(sQuery);
				sQuery.addScalar("effectiveDate", new DateType()).addScalar(
						"perOrderAmount", new DoubleType()).addScalar(
								"perItemAmount", new DoubleType()).setResultTransformer(
										Transformers.aliasToBean(OMAHandlingFees.class));
				omaHandlingFeesList = sQuery.list();
			}
			catch (Exception e) {
				log.error("Hibernate Exception caught in OMAHandlingFees: "
						+ e.getMessage());
			}
			log.debug("Report: After Getting the records for OMA Handling Fees  time :" + System.currentTimeMillis());
			return omaHandlingFeesList;


		}

		/**
		 * @param report
		 * @return List of OMARestockingFees 
		 * This method will get all the data for the report OMA Restocking Fees by Vendor
		 */
		@SuppressWarnings("unchecked")
		public List<OMARestockingFees> getOMARestockingFees(BaseReport report) {

			List<OMARestockingFees> omaRestockingList =
				new ArrayList<OMARestockingFees>();
			SessionFactory sessionFactory = getHibernateTemplate()
			.getSessionFactory();
			Session session = sessionFactory.getCurrentSession();
			StringBuilder query = new StringBuilder();
			query.append(" SELECT DISTINCT FM.DESCR AS fullfilmentMethodName,")
			.append(" FS.FULFMNT_SERVICE_NAME AS fullfilmentSeviceName,")
			.append(" V.NAME AS vendorName ,V.VENDOR_NUMBER AS vendorNbr,")
			.append(" VF.PER_ITEM_AMOUNT AS perItemAmount,")
			.append(" VF.PER_ORDER_AMOUNT AS perOrderAmount, ")
			.append(" T1.effectiveDate AS effectiveDate ")
			.append(" FROM VNDR_FULFMNT_SERV_FEE VFSF,")

			.append("(SELECT FSV.VENDOR_ID AS VENDOR_ID ,")
			.append(" Max(VFR.EFFECTIVE_DATE)AS effectiveDate,")
			.append(" Max(VENDOR_FEE_RQST_ID) AS VENDOR_FEE_RQST_ID ,")
			.append(" FSV.FULFMNT_SERVICE_ID ")
			.append(" FROM VNDR_FULFMNT_SERV_FEE VFSF,VENDOR_FEE_RQST VFR,")
			.append(" FULFMNT_SERVICE_VENDOR FSV ")
			.append(" WHERE  VFSF.VNDR_FEE_REQUEST_ID =  VFR.VENDOR_FEE_RQST_ID")
			.append(" AND VFSF.VENDOR_ID = FSV.VENDOR_ID AND VFSF.VENDOR_ID = FSV.VENDOR_ID")
			.append(" AND FSV.STATUS_CD='ACTIVE' ")
			.append(" GROUP BY FSV.VENDOR_ID,To_Char(VFR.EFFECTIVE_DATE,'DD-MON-YYYY'),")
			.append(" FSV.FULFMNT_SERVICE_ID ")
			.append(" ORDER BY  FSV.VENDOR_ID) T1,")
			// End of Inner select query
			.append(" VNDR_FEE VF,FULFMNT_METHOD FM,")
			.append(" FULFMNT_SERVICE FS ,VENDOR V ")

			// where clause
			.append(" WHERE  VFSF.VNDR_FEE_REQUEST_ID=T1.VENDOR_FEE_RQST_ID")
			.append(" AND T1.FULFMNT_SERVICE_ID=FS.FULFMNT_SERVICE_ID")
			.append(" AND FS.FULFMNT_METHOD_CD =  FM.FULFMNT_METHOD_CD")
			.append(" AND VFSF.VENDOR_FEE_ID =VF.VNDR_FEE_ID")
			.append(" AND  VF.FEE_CD=2 ")
			.append(" AND (PER_ORDER_AMOUNT>0 OR  PER_ITEM_AMOUNT >0)")
			.append(" AND T1.VENDOR_ID= V.VENDOR_ID")
			.append(" ORDER BY fullfilmentMethodName,fullfilmentSeviceName,")
			.append(" vendorName,effectiveDate");

			log.info(" SQL Query OMA Restocking Fees " + query.toString());
			log.debug("Report: Before Getting the records for OMA Restocking Fees  time :" + System.currentTimeMillis());
			try {
				SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query
						.toString());
				sQuery = addCommonScalar(sQuery);
				sQuery.addScalar("effectiveDate", new DateType()).addScalar(
						"perOrderAmount", new DoubleType()).addScalar(
								"perItemAmount", new DoubleType()).setResultTransformer(
										Transformers.aliasToBean(OMARestockingFees.class));
				omaRestockingList = sQuery.list();
			}
			catch (Exception e) {
				log.error("Hibernate Exception caught in OMARestockingFees: "
						+ e.getMessage());
			}
			log.debug("Report: After Getting the records for OMA Restocking Fees  time :" + System.currentTimeMillis());
			return omaRestockingList;

		}

		/**
		 * @param report
		 * @return List of OMACostExceptions 
		 * This method will get all the data for the report OMA Cost Exceptions by Vendor
		 */
		@SuppressWarnings("unchecked")
		public List<OMACostExceptions> getOMACostExceptions(BaseReport report) {

			List<OMACostExceptions> omaCostExceList = 
				new ArrayList<OMACostExceptions>();
			SessionFactory sessionFactory = getHibernateTemplate()
			.getSessionFactory();
			Session session = sessionFactory.getCurrentSession();

			StringBuilder query = new StringBuilder(); // Need to change Query
			query.append(" SELECT DISTINCT FM.DESCR AS fullfilmentMethodName,")
			.append("  FS.FULFMNT_SERVICE_NAME AS fullfilmentSeviceName,") 
			.append(" V.NAME AS vendorName ,V.VENDOR_NUMBER AS vendorNbr,") 
			.append(" VS.VENDOR_STYLE_ID AS styleNbr,VS.STYLE_DESCR AS styleDesc,")
			.append(" VSS.UNIT_COST AS idbCost ,VSS.OVERRIDE_COST AS cost, ")
			.append(" IDSF.COLOR AS color,IDSF.SIZE_DESCR AS itemSize , ")
			.append(" IDSF.VENDOR_UPC AS vendorUPC,IDSF.BELK_UPC AS belkUPC ")
			.append(" FROM    VIFR_STYLE_SKU VSS ,  VIFR_STYLE VS,")
			.append(" IDB_DROP_SHIP_FEED IDSF ,  VNDR_ITEM_FULFMNT_RQST  VIFR,")
			.append(" FULFMNT_METHOD FM,     FULFMNT_SERVICE FS ,")
			.append(" VENDOR V   ")
			.append(" WHERE  VS.VENDOR_STYLE_ID  =  IDSF.VENDOR_SYTLE_ID AND VSS.STATUS_CD ='ACTIVE' ")
			.append(" AND VSS.UNIT_COST<>VSS.OVERRIDE_COST AND VSS.OVERRIDE_COST>0 ")
			.append(" AND VSS.OVERRIDE_COST IS NOT NULL AND  VIFR.VNDR_ITEM_FULFMNT_RQST_ID =VSS.VNDR_ITEM_FULFMNT_RQST_ID ")
			.append(" AND VS.VNDR_ID = V.VENDOR_ID AND FS.FULFMNT_SERVICE_ID= VIFR.FULFMNT_SERVICE_ID")
			.append(" AND VSS.VNDR_ITEM_FULFMNT_RQST_ID=VS.VNDR_ITEM_FULFMNT_RQST_ID ")
			.append(" AND VS.VENDOR_STYLE_ID=VSS.VENDOR_STYLE_ID   AND FS.FULFMNT_METHOD_CD =  FM.FULFMNT_METHOD_CD ")
			.append(" ORDER BY fullfilmentMethodName,fullfilmentSeviceName,vendorName, styleNbr,color,itemSize,vendorUPC");

			log.info(" SQL Query OMA CostExceptions " + query.toString());
			log.debug("Report: After Getting the records for OMA CostExceptions  time :" + System.currentTimeMillis());
			try {
				SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query
						.toString());
				sQuery = addCommonScalar(sQuery);
				sQuery = addVendorScalar(sQuery);
				sQuery.addScalar("belkUPC", new StringType()).addScalar("cost",
						new DoubleType()).addScalar("idbCost", new DoubleType())
						.setResultTransformer(
								Transformers.aliasToBean(OMACostExceptions.class));
				omaCostExceList = sQuery.list();
			}
			catch (Exception e) {
				log.error("Hibernate Exception caught in OMACostExceptions: "
						+ e.getMessage());
			}
			log.debug("Report: After Getting the records for OMA CostExceptions  time :" + System.currentTimeMillis());
			return omaCostExceList;


		}

		/**
		 * @param report
		 * @return List of OMAHandlingFeeExceptions 
		 * This method will get all the data for the report OMA Handling Fee Exceptions by Vendor
		 */
		@SuppressWarnings("unchecked")
		public List<OMAHandlingFeeExceptions> getOMAHandlingFeeExceptions(
				BaseReport report) {

			List<OMAHandlingFeeExceptions> omaHandlingFeeExceList = new ArrayList<OMAHandlingFeeExceptions>();
			SessionFactory sessionFactory = getHibernateTemplate()
			.getSessionFactory();
			Session session = sessionFactory.getCurrentSession();
			StringBuilder query = new StringBuilder();
			query.append(" SELECT DISTINCT FM.DESCR AS fullfilmentMethodName,")
			.append("FS.FULFMNT_SERVICE_NAME AS fullfilmentSeviceName,") 
			.append("V.NAME AS vendorName ,V.VENDOR_NUMBER AS vendorNbr,") 
			.append("VS.VENDOR_STYLE_ID AS styleNbr,VS.STYLE_DESCR AS styleDesc, ")
			.append("VSS.UNIT_HANDLING_FEE AS vendorItemFee ,VSS.OVERRIDE_HANDLING_FEE ")
			.append("AS exceptionItemFee, IDSF.COLOR AS color,IDSF.SIZE_DESCR AS itemSize , ")
			.append("IDSF.VENDOR_UPC AS vendorUPC ")
			.append("FROM VIFR_STYLE VS ,")
			.append("VIFR_STYLE_SKU VSS ,")
			.append("IDB_DROP_SHIP_FEED IDSF , ")
			.append("VNDR_ITEM_FULFMNT_RQST  VIFR,")
			.append("FULFMNT_METHOD FM, FULFMNT_SERVICE FS ,")
			.append("VENDOR V ")
			.append(" WHERE  VS.VENDOR_STYLE_ID  = IDSF.VENDOR_SYTLE_ID ")
			.append(" AND VSS.STATUS_CD ='ACTIVE' ")
			.append(" AND  VSS.UNIT_HANDLING_FEE<>VSS.OVERRIDE_HANDLING_FEE ")
			.append(" AND VSS.VNDR_ITEM_FULFMNT_RQST_ID=VS.VNDR_ITEM_FULFMNT_RQST_ID ")
			.append(" AND VS.VENDOR_STYLE_ID=VSS.VENDOR_STYLE_ID ")
			.append(" AND VSS.OVERRIDE_HANDLING_FEE>0 AND VSS.OVERRIDE_HANDLING_FEE IS NOT NULL ")
			.append(" AND VIFR.VNDR_ITEM_FULFMNT_RQST_ID =VSS.VNDR_ITEM_FULFMNT_RQST_ID ")
			.append(" AND VS.VNDR_ID = V.VENDOR_ID AND FS.FULFMNT_SERVICE_ID= VIFR.FULFMNT_SERVICE_ID ")
			.append(" AND FS.FULFMNT_METHOD_CD =  FM.FULFMNT_METHOD_CD ")
			.append(" ORDER BY fullfilmentMethodName,fullfilmentSeviceName,vendorName, styleNbr,color,itemSize,vendorUPC");

			log.info(" SQL Query OMA Handling Fee Exceptions" + query.toString());
			log.debug("Report: Before Getting the records for OMA Handling Fee Exceptions time :" + System.currentTimeMillis());

			try {
				SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query
						.toString());
				sQuery = addCommonScalar(sQuery);
				sQuery = addVendorScalar(sQuery);
				sQuery.addScalar("vendorItemFee", new DoubleType()).addScalar(
						"exceptionItemFee", new DoubleType()).setResultTransformer(
								Transformers.aliasToBean(OMAHandlingFeeExceptions.class));
				omaHandlingFeeExceList = sQuery.list();
			}
			catch (Exception e) {
				log.error("Hibernate Exception caught in OMAHandlingFeeExceptions: "
						+ e.getMessage());
			}
			log.debug("Report: After Getting the records for OMA Handling Fee Exceptions time :" + System.currentTimeMillis());
			return omaHandlingFeeExceList;

		}

		/**
		 * @param SQLQuery sQuery
		 * @return SQLQuery This is used as a common method to append common scalar
		 *         of vendor attribute.
		 */
		private SQLQuery addCommonScalar(SQLQuery sQuery) {
			sQuery.addScalar("fullfilmentMethodName", new StringType()).addScalar(
					"fullfilmentSeviceName", new StringType()).addScalar(
							"vendorNbr", new LongType()).addScalar("vendorName",
									new StringType());
			return sQuery;
		}

		/**
		 * @param SQLQuery sQuery
		 * @return SQLQuery This is used as a common method to append common scalar
		 *         of vendor style.
		 */
		private SQLQuery addVendorScalar(SQLQuery sQuery) {
			sQuery.addScalar("styleNbr", new StringType()).addScalar("styleDesc",
					new StringType()).addScalar("color", new StringType())
					.addScalar("itemSize", new StringType()).addScalar("vendorUPC",
							new StringType());
			return sQuery;
		}

		/**
		 * @param BaseReport report
		 * @param StringBuilder query
		 * @param String alias
		 * @return StringBuilder This is used as a common method to append Data
		 *         where clause condition for the reports.
		 */
		private StringBuilder appendDateWhereClause(
				BaseReport report, StringBuilder query, String alias) {
			String startDate = null;
			String endDate = null;
			if (report != null && report.getCriteria() != null
					&& !report.getCriteria().isEmpty()) {
				startDate = (String) report.getCriteria().get("startDate");
				endDate = (String) report.getCriteria().get("endDate");
			}
			if (StringUtils.isNotBlank(startDate)
					&& StringUtils.isNotBlank(endDate)) {

				query.append(" and ").append(alias).append(
				".CREATED_DATE BETWEEN to_date('").append(startDate)
				.append("','mm/dd/yyyy') AND to_date('").append(endDate)
				.append("','mm/dd/yyyy')");
			}
			else if (StringUtils.isNotBlank(startDate)) {
				query.append(" and ").append(alias).append(
				".CREATED_DATE >= to_date('").append(startDate).append(
				"','mm/dd/yyyy')");
			}
			else if (StringUtils.isNotBlank(endDate)) {
				query.append(" and ").append(alias).append(
				".CREATED_DATE <= to_date('").append(endDate).append(
				"','mm/dd/yyyy')");
			}
			return query;
		}

		/**
		 * @author afusy45
		 * Method to drop TMPSKUAvail table
		 */
		public void deleteTmpSKUAvailTable(){
			log.info("In method to delete tmp_Sku_Table");
			SessionFactory sessionFactory = getHibernateTemplate()
			.getSessionFactory();
			Session session = sessionFactory.getCurrentSession();
			StringBuilder query = new StringBuilder();
			
			List list = null;
			try{
			query.append("DROP TABLE tmp_Sku_Avail purge") ;
			//int i = session.createSQLQuery(query.toString()).executeUpdate();
			
			SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query
					.toString());
			list = sQuery.list();
			}catch(Exception dae){
				log.error("Exception while dropping tmp_Sku_Avail table"+dae);
				
			}
			
		}
		
		/**
		 * @author afusy45
		 * Method to create temp sku avail table
		 */
		public void createTmpSkuAvail(String avail_query){
			log.info("In method to create tmp_Sku_Table");
			SessionFactory sessionFactory = getHibernateTemplate()
			.getSessionFactory();
			Session session = sessionFactory.getCurrentSession();
			StringBuilder query = new StringBuilder();
			query.append(avail_query);
			List list = null;
			try{
			SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query
					.toString()).addScalar("SKU",new StringType()).addScalar("ON_HAND_QTY",new StringType());
			 sQuery.executeUpdate();
			}catch(Exception dae){
				log.error("Exception while creating tmp_Sku_Avail table"+dae);
				
			}
			//return list;
		}
}

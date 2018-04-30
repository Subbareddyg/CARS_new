package com.belk.car.app.dao.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.appfuse.dao.hibernate.UniversalDaoHibernate;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;

import com.belk.car.app.dao.CarLookupDao;
import com.belk.car.app.dto.LateCarsAssociationDTO;
import com.belk.car.app.exceptions.DaoException;
import com.belk.car.app.model.AttributeValueProcessStatus;
import com.belk.car.app.model.Classification;
import com.belk.car.app.model.ContentStatus;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.HtmlDisplayType;
import com.belk.car.app.model.ImageLocationType;
import com.belk.car.app.model.ImageSourceType;
import com.belk.car.app.model.ImageType;
import com.belk.car.app.model.LateCarsGroup;
import com.belk.car.app.model.MediaCompassImage;
import com.belk.car.app.model.NoteType;
import com.belk.car.app.model.NotificationType;
import com.belk.car.app.model.PatternProcessingRule;
import com.belk.car.app.model.ProductGroup;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.model.SampleSourceType;
import com.belk.car.app.model.SampleTrackingStatus;
import com.belk.car.app.model.SourceType;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.ValidationExpression;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.VendorStyleType;
import com.belk.car.app.model.car.ManualCarProcessStatus;

public class CarLookupDaoHibernate extends UniversalDaoHibernate implements
		CarLookupDao {

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<Role> getRoles() {
		if (log.isDebugEnabled())
			log.debug("Retrieving all role names...");
		List<Role> rolesList = getHibernateTemplate().find(
				"from Role order by name");
		return rolesList;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public UserType getUserType(String userTypeCd) {
		if (log.isDebugEnabled())
			log.debug("Retrieving usertype for code:" + userTypeCd);

		List userList = getHibernateTemplate().find(
				"FROM UserType ut WHERE ut.userTypeCd = ?", userTypeCd);

		if (userList == null || userList.isEmpty()) {
			throw new DaoException("user Type '" + userList + "' not found...");
		} else {
			return (UserType) userList.get(0);
		}
	}

	public List<UserType> getActiveUserTypes() {
		if (log.isDebugEnabled())
			log.debug("Retrieving all active user types...");
		return getHibernateTemplate().find(
				"from UserType ut where ut.statusCd=? order by ut.name",
				"ACTIVE");
	}

	public Role getRole(String roleCd) {
		if (log.isDebugEnabled())
			log.debug("Retrieving role for code:" + roleCd);

		return (Role) getHibernateTemplate().get(Role.class, roleCd);

	}

	public List<Department> getAllDepartments() {
		return getHibernateTemplate().find(
				"from Department dept order by dept.deptCd");
	}

	public Department getDepartmentById(Long departmentId) {
		return (Department) getHibernateTemplate().get(Department.class,
				departmentId);
	}

	public Vendor getVendorById(Long vendorId) {
		return (Vendor) getHibernateTemplate().get(Vendor.class, vendorId);
	}

	public List<Vendor> getAllVendors() {
		return getHibernateTemplate().find(
				"from Vendor vend order by vend.name");
	}

	public List<VendorStyle> getAllVendorStyles(){
		return getHibernateTemplate().find(
				"from VendorStyle vs order by vs.vendorStyleName");
	}
	
	public NoteType getNoteType(String noteId) {
		return (NoteType) getHibernateTemplate().get(NoteType.class, noteId);
	}

	public List<NoteType> getAllNoteTypes() {
		return getHibernateTemplate().find(
				"from NoteType nt order by nt.createdDate desc");
	}

	public Classification getClassificationById(Long classificationId) {
		return (Classification) getHibernateTemplate().get(
				Classification.class, classificationId);
	}

	public ProductType getProductTypeById(Long productTypeId) {
		return (ProductType) getHibernateTemplate().get(ProductType.class,
				productTypeId);
	}

	public List<Classification> getAllClassifications() {
		return getHibernateTemplate().find(
				"from Classification cls order by cls.belkClassNumber");
	}

	public List<ProductType> getAllProductTypes() {
		return getHibernateTemplate().find(
				"from ProductType pt order by pt.name");
	}

	public NotificationType getNotificationType(String notificationTypeCode) {
		return (NotificationType) getHibernateTemplate().get(
				NotificationType.class, notificationTypeCode);
	}

	public List<ImageLocationType> getAllImageLocationTypes() {
		return getHibernateTemplate().find(
				"from ImageLocationType ilt order by ilt.name");
	}

	public List<ImageSourceType> getAllImageSourceTypes() {
		return getHibernateTemplate().find(
				"from ImageSourceType ist order by ist.name");
	}

	public List<ImageType> getAllImageTypes() {
		return getHibernateTemplate()
				.find("from ImageType it order by it.name");
	}

	public List<SampleSourceType> getAllSampleSourceTypes() {
		return getHibernateTemplate().find(
				"from SampleSourceType sst order by sst.name");
	}

	public List<HtmlDisplayType> getAllHtmlDisplayTypes() {
		return getHibernateTemplate().find(
				"from HtmlDisplayType hdt order by hdt.name");
	}

	public List<ValidationExpression> getAllValidationExpressions() {
		return getHibernateTemplate().find(
				"from ValidationExpression ve order by ve.name");
	}

	public List<SampleTrackingStatus> getAllSampleTrackingStatus() {
		return getHibernateTemplate().find("from SampleTrackingStatus sts");
	}

	public List<ManualCarProcessStatus> getAllManualCarProcessStatus() {
		return getHibernateTemplate().find(
				"from ManualCarProcessStatus ve order by ve.name");
	}

	public ImageLocationType getImageLocationType(String imageLocationType) {
		return (ImageLocationType) getHibernateTemplate()
				.find(
						"from ImageLocationType isst where isst.imageLocationTypeCd = ?",
						imageLocationType).get(0);
	}

	public ImageSourceType getImageSourceType(String imageSourceType) {
		return (ImageSourceType) getHibernateTemplate().find(
				"from ImageSourceType isst where isst.imageSourceTypeCd = ?",
				imageSourceType).get(0);
	}

	public ImageType getImageType(String imageType) {
		return (ImageType) getHibernateTemplate().find(
				"from ImageType it where it.imageTypeCd = ?", imageType).get(0);
	}

	public SampleSourceType getSampleSourceType(String sampleSourceType) {
		return (SampleSourceType) getHibernateTemplate().find(
				"from SampleSourceType sst where sst.sampleSourceTypeCd = ?",
				sampleSourceType).get(0);
	}

	public HtmlDisplayType getHtmlDisplayType(String htmlDisplayTypeCd) {
		return (HtmlDisplayType) getHibernateTemplate().find(
				"from HtmlDisplayType html where html.htmlDisplayTypeCd = ?",
				htmlDisplayTypeCd).get(0);
	}

	public ValidationExpression getValidationExpression(
			String validationExpressionCd) {
		return (ValidationExpression) getHibernateTemplate()
				.find(
						"from ValidationExpression ve where ve.validationExpressionCd = ?",
						validationExpressionCd).get(0);
	}

	public SampleTrackingStatus getSampleTrackingStatus(
			String sampleTrackingStatusCd) {
		return (SampleTrackingStatus) getHibernateTemplate()
				.find(
						"from SampleTrackingStatus sts where sts.sampleTrackingStatusCd = ?",
						sampleTrackingStatusCd).get(0);
	}

	public ManualCarProcessStatus getManualCarProcessStatus(
			String manualCarProcessStatusCd) {
		return (ManualCarProcessStatus) getHibernateTemplate().find(
				"from ManualCarProcessStatus sts where sts.statusCd = ?",
				manualCarProcessStatusCd).get(0);
	}

	public List<Classification> searchClassifications(String classificationId,
			String classificationName)  {
		
		Formatter stringFormat = new Formatter();
		String likeFormat = "%%%1$s%%";
		
		StringBuffer sqlB = new StringBuffer("from Classification");
		
		// StringBuffer strB = new StringBuffer() ;
		ArrayList<String> query = new ArrayList<String>();
		ArrayList<Object> values = new ArrayList<Object>();
		
		if (StringUtils.isNotBlank(classificationId)) {
			query.add("belkClassNumber = ?");
			values.add(Short.valueOf(classificationId).shortValue());		
		}
		
		if (StringUtils.isNotBlank(classificationName)) {
			query.add("name LIKE ?");
			values.add(String.format(likeFormat,classificationName.toUpperCase()).toString());			
		}

		
		if (!query.isEmpty()) {
			sqlB.append(" WHERE ") ;
			int i=0;
			for(String s: query) {
				if (i>0) {
					sqlB.append(" AND ");
				}
				sqlB.append(s);
				i++;
			}
		}
		
		sqlB.append(" ORDER BY belkClassNumber");
		Object valArray[] = values.toArray();
		
		
		return getHibernateTemplate().find(sqlB.toString(), valArray);

	}

	public List<ProductType> searchProductTypes(String productTypeName,
			Long classificationId) {
		
		Formatter stringFormat = new Formatter();
		String likeFormat = "%%%1$s%%";
		
		StringBuffer sqlB = new StringBuffer("from ProductType pt ");
		
		// StringBuffer strB = new StringBuffer() ;
		ArrayList<String> query = new ArrayList<String>();
		ArrayList<Object> values = new ArrayList<Object>();
		
		if (classificationId != null) {
			sqlB.append("inner join fetch pt.classifications as c");
			query.add("c.belkClassNumber = ?");
			values.add(classificationId.shortValue());		
		}
		
		if (StringUtils.isNotBlank(productTypeName)) {
			query.add("upper(pt.name) LIKE ?");
			values.add(String.format(likeFormat,productTypeName.toUpperCase()).toString());
		}
		
		if (!query.isEmpty()) {
			sqlB.append(" WHERE ") ;
			int i=0;
			for(String s: query) {
				if (i>0) {
					sqlB.append(" AND ");
				}
				sqlB.append(s);
				i++;
			}
		}
		sqlB.append(" order by pt.name");	
		return  getHibernateTemplate().find(sqlB.toString(), values.toArray());	
	}

	public Object getById(Class cls, Serializable id) {
		return this.getHibernateTemplate().get(cls, id);
	}
	
	public Statistics getCacheStatistics() {
		return getHibernateTemplate().getSessionFactory().getStatistics();
	}

	/*
	 * 	  This method is used for getting the ACTIVE Vendors.
	 */
	public List<User> getVendors() {
		return getHibernateTemplate().find("from User usr where usr.userType.userTypeCd='VENDOR' and usr.statusCd='ACTIVE' ");
	}

	public AttributeValueProcessStatus getAttributeValueProcessStatus(String attValueProcessStatusCd) {		
		return  (AttributeValueProcessStatus) getHibernateTemplate().find(
				"from AttributeValueProcessStatus where processStatusCd = ?",
				attValueProcessStatusCd).get(0);
	}

	public List<ContentStatus> getAllContentStatuses() {
		return getHibernateTemplate().find("from ContentStatus cs");
	}
	
	public ContentStatus getContentStatus(String contentStatusCd) {		
		return  (ContentStatus) getHibernateTemplate().find(
				"from ContentStatus where code = ?",
				contentStatusCd).get(0);
	}

	public List<PatternProcessingRule> getAllPatternProcessingRules() {
		return getHibernateTemplate().find("from PatternProcessingRule ppr");
	}

	public List<VendorStyleType> getAllVendorStyleTypes() {
		return getHibernateTemplate().find("from VendorStyleType vst where code != '" + VendorStyleType.PRODUCT + "'");
	}
	
	public List<VendorStyleType> getPatternTypes(boolean isSuperAdmin) {
		if(isSuperAdmin) {
			return getHibernateTemplate().find("from VendorStyleType vst where code like 'PATTERN%'");
		} else {
			return getHibernateTemplate().find("from VendorStyleType vst where code = '" + VendorStyleType.PATTERN_SGBS_VS + "'");
		}
	}	
	
	//===============Added by Syntel for Dropship Requirement================//
	/**
	 * @return List<SourceType>
	 * This method is used to load All Source Type for search requirement.
	 */
	@SuppressWarnings("unchecked")
	public List<SourceType> getAllSourceType() {
		return getHibernateTemplate().find("from SourceType st");
	}

	/**
	 * Gets the name of the product group and returns a list of product group with 
	 * that name.
	 * 
	 * @param name
	 * @return List<ProductGroup>
	 */
	@SuppressWarnings("unchecked")
	public List<ProductGroup> getProductGroup(String name) {
		String likeFormat = "%%%1$s%%";
		String nameSearch = String.format(likeFormat,name.toUpperCase()).toString();
		log.debug("In product group search :" + nameSearch);		
		return getHibernateTemplate().find("from ProductGroup where upper(name) like ?",nameSearch);
	}
	
	/**
	 * Gets all the product types based on the String ids that were passed 
	 * as the parameter.
	 * 
	 * @param productTypeIds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProductType> getProductTypes(String productTypeIds) {
		
		StringBuffer sqlB = new StringBuffer("from ProductType pt ");
		sqlB.append(" where pt.productTypeId in(")
		.append(productTypeIds)
		.append(")");
		return  getHibernateTemplate().find(sqlB.toString());	
	}
	
	public List<User> getUsers() {
		List<User> userList=null;
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		StringBuffer sb=new StringBuffer("select * from car_user usr where usr.status_Cd='ACTIVE' order by upper(first_name) asc");
		try {
			userList = session.createSQLQuery(sb.toString()).addEntity(User.class).setFirstResult(0).setMaxResults(25).list();
			//userList = session.createSQLQuery(sb.toString()).addEntity(User.class).list();
		} catch (Exception e) {
			if (log.isErrorEnabled())
				log.error("Hibernate Exception", e);
		}

		if (userList == null) {
			userList = new ArrayList<User>();
		}
		return userList;
		
		//return getHibernateTemplate().find("from User usr where usr.statusCd='ACTIVE' order by upper(first_name) asc");
	}

	/**
	 *This method is used to find out total user count.
	 ***/
	public Long getUsersCount(String firstName,String lastName,String emailId) {
		Long countUsers=0L;
		StringBuffer sb=new StringBuffer("select count (*) from User usr where usr.statusCd='ACTIVE' ");
		try{
			if (StringUtils.isNotBlank(firstName)) {
				sb.append(" and upper(usr.firstName) like '%" + firstName.toUpperCase() + "%'");
			}

			if (StringUtils.isNotBlank(lastName)) {
				sb.append(" and upper(usr.lastName) like '%"+ lastName.toUpperCase() +"%'");
			}

			if (StringUtils.isNotBlank(emailId)) {
				sb.append(" and upper(usr.emailAddress) like '%"+ emailId.toUpperCase() +"%'");
			}
			countUsers=(Long)getHibernateTemplate().find(sb.toString()).get(0);
		}catch(Exception e){
			e.printStackTrace();
		}
		return countUsers;
	}


	public List<MediaCompassImage> getAllMCImages(String strImageStatus){
		return getHibernateTemplate().find("from MediaCompassImage mci where mci.imageStatus='" + strImageStatus + "'");
	}
	
	public int getLateCarsAssocByDeptId(Long LateCarsGroupId, Long deptId) {

		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		int result = 0;
		if (deptId==null) {
			String hql = "UPDATE LateCarsAssociation set statusCd = :status "
					+ "WHERE dept.deptId IS NULL and lateCarsGroup.lateCarsGroupId = :latecarsgroup_id and statusCd = :originalStatus";
			Query query = session.createQuery(hql);
			query.setParameter("status", "DELETED");
			query.setParameter("latecarsgroup_id", LateCarsGroupId);
			query.setParameter("originalStatus", "ACTIVE");
			result = query.executeUpdate();
		} else {
			String hql = "UPDATE LateCarsAssociation set statusCd = :status "
					+ "WHERE dept.deptId = :dept_id and lateCarsGroup.lateCarsGroupId = :latecarsgroup_id and statusCd = :originalStatus";
			Query query = session.createQuery(hql);
			query.setParameter("status", "DELETED");
			query.setParameter("dept_id", deptId);
			query.setParameter("latecarsgroup_id", LateCarsGroupId);
			query.setParameter("originalStatus", "ACTIVE");
			result = query.executeUpdate();
		}
		return result;
	}
	
	
	@SuppressWarnings("rawtypes")
	public List<LateCarsAssociationDTO> getLateCarsAssocById(
			Long lateCarsGroupId) {
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		String sql = " SELECT DEPT_CD,DEPT_NAME,DEPT_ID,REPLACE (ltrim(max(sys_connect_by_path(VENDOR_NUMBER, ',' )), ','),',','<br>') VENDOR_NUMBER, "
				+ " REPLACE(ltrim(max(sys_connect_by_path(VENDOR_NAME, ',' )), ','),',','<br>')" +
				  " VENDOR_NAME FROM (SELECT Nvl(D.DEPT_CD,'NA') AS DEPT_CD,Nvl(D.NAME,'NA') AS DEPT_NAME,"
				+ " D.DEPT_ID AS DEPT_ID,Nvl(WM_CONCAT(DISTINCT V.NAME),'Null') AS VENDOR_NAME," +
				  " Nvl(WM_CONCAT(DISTINCT V.VENDOR_NUMBER),'Null') AS VENDOR_NUMBER," +
				  " row_number() OVER (PARTITION by D.DEPT_CD order by D.NAME) rn FROM LATE_CARS_ASSOCIATION  LCA "
				+ " left OUTER JOIN  VENDOR V ON V.VENDOR_ID=LCA.VENDOR_ID "
				+ " left OUTER  JOIN DEPARTMENT D ON D.DEPT_ID=LCA.DEPT_ID "
				+ " WHERE LCA.LATE_CARS_GROUP_ID = "
				+ lateCarsGroupId
				+ " AND LCA.STATUS_CD = 'ACTIVE' "
				+ " GROUP BY D.DEPT_CD,D.NAME,V.VENDOR_NUMBER,D.DEPT_ID) "
				+ " START WITH rn = 1"
				+ " CONNECT BY PRIOR rn = rn-1"
				+ " AND PRIOR DEPT_CD = DEPT_CD"
				+ " GROUP BY DEPT_NAME,DEPT_CD,DEPT_ID";

		SQLQuery query = session.createSQLQuery(sql);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List data = query.list();
		List<LateCarsAssociationDTO> lateCarsAssociationDetailsList = new ArrayList<LateCarsAssociationDTO>(
				0);
		if (data != null) {
			for (Object values : data) {
				Map row = (Map) values;
				LateCarsAssociationDTO lateCarsAssociationDTO = new LateCarsAssociationDTO();
				if (row.get("DEPT_CD").toString().equalsIgnoreCase("NA")) {
					lateCarsAssociationDTO.setDeptNO("");
				} else {
					lateCarsAssociationDTO.setDeptNO(row.get("DEPT_CD")
							.toString());
				}
				if (row.get("DEPT_NAME").toString().equalsIgnoreCase("NA")) {
					lateCarsAssociationDTO.setDeptName("");
				} else {
					lateCarsAssociationDTO.setDeptName(row.get("DEPT_NAME")
							.toString());
				}
				if (row.get("DEPT_ID") == null) {
					lateCarsAssociationDTO.setDeptIds("0");
				} else {
					lateCarsAssociationDTO.setDeptIds(row.get("DEPT_ID")
							.toString());
				}
				if (row.get("VENDOR_NUMBER") == null) {
					lateCarsAssociationDTO.setVendorNo("");
				} else {
					lateCarsAssociationDTO.setVendorNo(row.get("VENDOR_NUMBER")
							.toString());
				}
				if (row.get("VENDOR_NAME") == null) {
					lateCarsAssociationDTO.setVendorName("");
				} else {
					lateCarsAssociationDTO.setVendorName(row.get("VENDOR_NAME")
							.toString());
				}
				lateCarsAssociationDTO
						.setLateCarsAssociationID(lateCarsGroupId);
				lateCarsAssociationDetailsList.add(lateCarsAssociationDTO);
			}
		}

		return lateCarsAssociationDetailsList;
	}
	
	public LateCarsGroup getLateCarsGroupById(Long lateCarsGroupId) {
		return (LateCarsGroup) getHibernateTemplate().get(LateCarsGroup.class,
				lateCarsGroupId);
	}
	
	public List<Vendor> getAllVendorsByVendorName(String vendorName,
			String vendorNumber) {
		StringBuffer queryBuff = new StringBuffer();
		if (vendorName == null)
			vendorName = "";
		if (vendorNumber == null)
			vendorNumber = "";
		if(vendorName.length() > 0 && vendorName.contains("'")){
			vendorName=vendorName.replaceAll("'", "''");
		}
		queryBuff.append("from Vendor  ");
		queryBuff.append("where NAME LIKE Upper('%");
		queryBuff.append(vendorName.trim()).append("%')");
		queryBuff.append(" and ");
		queryBuff.append(" VENDOR_NUMBER LIKE '%");
		queryBuff.append(vendorNumber.trim()).append("%'");
		return getHibernateTemplate().find(queryBuff.toString());
	}
	 
	
}

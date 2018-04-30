package com.belk.car.app.dao.hibernate;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.appfuse.model.User;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.belk.car.app.Constants;
import com.belk.car.app.dao.CarDao;
import com.belk.car.app.dto.CarsDTO;
import com.belk.car.app.dto.DetailNotificationUserDTO;
import com.belk.car.app.dto.NotificationUserDTO;
import com.belk.car.app.exceptions.DaoException;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.AttributeDatatype;
import com.belk.car.app.model.AttributeType;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarImage;
import com.belk.car.app.model.CarNote;
import com.belk.car.app.model.CarOutfitChild;
import com.belk.car.app.model.CarReopenPetDetails;
import com.belk.car.app.model.CarSearchCriteria;
import com.belk.car.app.model.CarSkuAttribute;
import com.belk.car.app.model.CarUserVendorDepartment;
import com.belk.car.app.model.ChangeType;
import com.belk.car.app.model.ClassAttribute;
import com.belk.car.app.model.Classification;
import com.belk.car.app.model.CollectionSkus;
import com.belk.car.app.model.ContentStatus;
import com.belk.car.app.model.DBPromotionCollectionSkus;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.DepartmentAttribute;
import com.belk.car.app.model.Image;
import com.belk.car.app.model.ImageSourceType;
import com.belk.car.app.model.ImageTrackingStatus;
import com.belk.car.app.model.NoteType;
import com.belk.car.app.model.PoUnitDetail;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.model.SampleSourceType;
import com.belk.car.app.model.SampleTrackingStatus;
import com.belk.car.app.model.SampleType;
import com.belk.car.app.model.ShippingType;
import com.belk.car.app.model.SourceType;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.WorkFlow;
import com.belk.car.app.model.workflow.WorkflowStatus;
import com.belk.car.app.to.IdbCarDataTO;
import com.belk.car.util.DateUtils;

public class CarDaoHibernate extends UniversalDaoHibernate implements CarDao {

	
	List<CarsDTO> list2 =null;
	/**
	 * Retrieve a list of no more than 100 Car objects that are new (that is,
	 * Cars with a current workflow status that is INITIATED)
	 * 
	 * @return a list of new cars
	 */
	public List<Car> getNewCars() {

		HibernateTemplate template = getHibernateTemplate();
		// template.setMaxResults(100);
		return (List<Car>) template
				.find("from Car ca where ca.currentWorkFlowStatus = 'INITIATED' and ca.statusCd= 'ACTIVE'");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.belk.car.app.dao.hibernate.CarDaoHb#getAttributesForCar(com.belk.
	 * car.app.model.Car)
	 */
	public List<CarAttribute> getAttributesForCar(Car car) {
		return (List<CarAttribute>) getHibernateTemplate().find(
				"from CarAttribute where car=?", car);
	}

	public List<Department> getAllDepartments() {
		return getHibernateTemplate().find("from Department order by name");
	}

	public List<VendorSku> getSkusForStyle(VendorStyle vs) {
		return getHibernateTemplate().find(
				"from VendorSku where vendorStyle.vendorStyleId =?",
				vs.getVendorStyleId());
	}
	
	/**
	 * Method to retrieve list of skus for a vendor tyle and color combination.
	 * 
	 * @param vendorStyleId
	 * @param colorCd
	 * @return
	 */
	public List<VendorSku> getSkusForStyleColorCd(long vendorStyleId,String colorCd,Car car)throws Exception{
	    Object arr[] = { vendorStyleId, colorCd,car.getCarId() };
        return getHibernateTemplate().find(
                "from VendorSku where vendorStyle.vendorStyleId =? and colorCode=? and car.carId=?",
                arr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.belk.car.app.dao.hibernate.CarDaoHb#getAllDepartments()
	 */
	public List<Attribute> getAllAttributes() {

		return getHibernateTemplate()
				.find(
						"from Attribute attr where attr.statusCode='ACTIVE' order by attr.name");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.belk.car.app.dao.hibernate.CarDaoHb#getCarsForUser(org.appfuse.model
	 * .User)
	 */
	public List<Car> getCarsForUser(User usr) {
		List<Car> carList = null;
		CarSearchCriteria searchCriteria = new CarSearchCriteria(usr);
		searchCriteria.setStatusCd(Status.ACTIVE);
		// Do not display Closed Cars
		searchCriteria.setIgnoreClosedCars(true);

		String query = this.getCarQueryString(searchCriteria,false);
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery q = session.createSQLQuery(query)
					.addEntity("c", Car.class).addJoin("vs", "c.vendorStyle")
					.addJoin("v", "vs.vendor").addJoin("d", "c.department");
			// q.setCacheable(false) ;
			carList = convertToCarsList(q.list());
		} catch (Exception e) {
			log.error("Hibernate Exception" + e.getMessage());
		}

		/*
		 * if (carList == null) { carList = new ArrayList<Car>(); }
		 */
		return carList;

	}

	/**
	 * This method fetches list of CARS assigned to the given user.
	 */
	public List<CarsDTO> getDashboardCarsForUser(CarSearchCriteria searchCriteria1,User usr) {
		List<CarsDTO> carList = null;
		CarSearchCriteria searchCriteria = new CarSearchCriteria(usr);
		searchCriteria.setStatusCd(Status.ACTIVE);

		// Do not display Closed Cars
		searchCriteria.setIgnoreClosedCars(true);
		searchCriteria.setArchive(false);
		searchCriteria.setIncludeOutFitCars(true);
		searchCriteria.setIncludePYGCars(true);
     	if(null != searchCriteria1 && null != searchCriteria1.getSortColumnNm() && null != searchCriteria1.getSortOrder()){
			searchCriteria.setSortColumnNm(searchCriteria1.getSortColumnNm());
			searchCriteria.setSortOrder(searchCriteria1.getSortOrder());
		}
		String query = this.getCarQueryString(searchCriteria,false);
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery q = session.createSQLQuery(query)
					.addEntity("c", Car.class).addJoin("vs", "c.vendorStyle")
					.addJoin("v", "vs.vendor").addJoin("d", "c.department")
					.addScalar("due_date", Hibernate.STRING).addScalar(
							"e_date", Hibernate.STRING);
			 if(searchCriteria1.getCurrentPage() !=null && !searchCriteria1.getCurrentPage().isEmpty() && !StringUtils.isNotBlank(searchCriteria.getCarId()))
			 {
				   q.setFirstResult(250*(Integer.parseInt(searchCriteria1.getCurrentPage())-1)).setMaxResults(250);
			 }
			 carList = this.createCarDTOList(q.list(), usr);
	 	} catch (Exception e) {
			log.error("Hibernate Exception" + e.getMessage());
		}
		return carList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.belk.car.app.dao.hibernate.CarDaoHb#getAttributeDataTypes()
	 */
	public List<AttributeDatatype> getAttributeDataTypes() {
		return getHibernateTemplate().find(
				"from AttributeDatatype where statusCode='A'");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.belk.car.app.dao.hibernate.CarDaoHb#getAttributeTypes()
	 */
	public List<AttributeType> getAttributeTypes() {
		return getHibernateTemplate().find(
				"from AttributeType where statusCode='A'");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.belk.car.app.dao.hibernate.CarDaoHb#getChangeTypes()
	 */
	public List<ChangeType> getChangeTypes() {
		return getHibernateTemplate().find(
				"from ChangeType where statusCode='A'");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.belk.car.app.dao.hibernate.CarDaoHb#getNoteTypes()
	 */
	public List<NoteType> getNoteTypes() {
		return getHibernateTemplate().find(
				"from CommentType where statusCode='A'");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.belk.car.app.dao.hibernate.CarDaoHb#getSampleTypes()
	 */
	public List<SampleType> getSampleTypes() {
		return getHibernateTemplate().find(
				"from SampleType where statusCode='A'");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.belk.car.app.dao.hibernate.CarDaoHb#getShippingTypes()
	 */
	public List<ShippingType> getShippingTypes() {
		return getHibernateTemplate().find(
				"from ShippingType where statusCode='A'");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.belk.car.app.dao.hibernate.CarDaoHb#getSourceTypes()
	 */
	public List<SourceType> getSourceTypes() {
		return getHibernateTemplate().find(
				"from SourceType where statusCode='" + Status.ACTIVE + "'");
	}

	public Department getDepartmentByNumber(String departmentNumber) {
		List<Department> col = getHibernateTemplate().find(
				"from Department where deptCd=?", departmentNumber);
		if (col != null && !col.isEmpty())
			return col.get(0);

		return null;
	}

	public Department getDepartmentByName(String departmentName) {
		Object ob = getHibernateTemplate().find("from Department where name=?",
				departmentName);
		Collection<Department> col = (Collection) ob;
		for (Department dept : col) {
			return dept;
		}
		return null;
	}

	public Classification getClassificationByNumber(String classNumber,
			Department dept) {
		Object arr[] = { classNumber, dept };
		Object ob = getHibernateTemplate().find(
				"from Classification where belkClassNumber=? and department=?",
				arr);
		Collection<Classification> col = (Collection) ob;
		for (Classification classification : col) {
			return classification;
		}
		return null;
	}

	public Classification getClassificationByName(String className,
			Department dept) {
		Object arr[] = { className, dept };
		Object ob = getHibernateTemplate().find(
				"from Classification where name=? and department=?", arr);
		Collection<Classification> col = (Collection) ob;
		for (Classification classification : col) {
			return classification;
		}
		return null;
	}

	public Vendor getVendorByNumber(String vendorNumber) {
		Object ob = getHibernateTemplate().find(
				"from Vendor where vendorNumber=?", vendorNumber);
		Collection<Vendor> col = (Collection) ob;
		for (Vendor vendor : col) {
			return vendor;
		}
		return null;
	}

	public Vendor getVendorByName(String vendorName) {
		Object ob = getHibernateTemplate().find("from Vendor where name=?",
				vendorName);
		Collection<Vendor> col = (Collection) ob;
		for (Vendor vendor : col) {
			return vendor;
		}
		return null;
	}

	public VendorStyle getVendorStyleByNumber(String vendorStyleNumber,
			Vendor vendor) {
		Object arr[] = { vendorStyleNumber, vendor };
		Object ob = getHibernateTemplate().find(
				"from VendorStyle where vendorStyleNumber=? and vendor=?", arr);
		Collection<VendorStyle> col = (Collection) ob;
		for (VendorStyle vendorStyle : col) {
			return vendorStyle;
		}
		return null;
	}

	public VendorStyle getVendorStyleByName(String vendorStyleName,
			Vendor vendor) {
		Object arr[] = { vendorStyleName, vendor };
		Object ob = getHibernateTemplate().find(
				"from VendorStyle where name=? and vendor=?", arr);
		Collection<VendorStyle> col = (Collection) ob;
		for (VendorStyle vendorStyle : col) {
			return vendorStyle;
		}
		return null;
	}

	/**
	 * 
	 */
	public WorkFlow getWorkFlowByName(String workFlowName) {
		List workFlowList = getHibernateTemplate().find(
				"from WorkFlow where name=?", workFlowName);
		if (workFlowList == null || workFlowList.isEmpty()) {
			throw new DaoException("workflow '" + workFlowName
					+ "' not found...");
		} else {
			return (WorkFlow) workFlowList.get(0);
		}
	}

	public Department createDepartment(String departmentName,
			String departmentNumber) {
		Department dept = new Department();
		dept.setName(departmentName);
		dept.setDeptCd(departmentNumber);
		dept.setDescription(departmentName);
		dept.setStatusCd("ACTIVE");
		this.getHibernateTemplate().save(dept);
		this.getHibernateTemplate().flush();
		return dept;
	}

	public Classification createClassification(String name, String number,
			Department dept) {
		Classification ob = new Classification();
		ob.setName(name);
		ob.setBelkClassNumber(Short.parseShort(number));
		ob.setDescr(name);
		ob.setDepartment(dept);
		ob.setStatusCd("ACTIVE");
		this.getHibernateTemplate().save(ob);
		this.getHibernateTemplate().flush();
		return ob;
	}

	public Vendor createVendor(String name, String number) {
		Vendor ob = new Vendor();
		ob.setName(name);
		ob.setVendorNumber(number);
		ob.setDescr(name);
		ob.setStatusCd("ACTIVE");
		this.getHibernateTemplate().save(ob);
		this.getHibernateTemplate().flush();
		return ob;
	}

	public VendorStyle createVendorStyle(String vendorName, String name,
			String vendorNumber, String vendorStyleNumber,
			String vendorStyleName, Vendor vendor, Classification classification) {
		VendorStyle ob = new VendorStyle();
		ob.setVendorNumber(vendorNumber);
		ob.setVendorStyleName(name);
		ob.setVendorStyleNumber(vendorStyleNumber);
		ob.setDescr(name);
		ob.setStatusCd("ACTIVE");
		ob.setVendor(vendor);
		ob.setClassification(classification);
		this.getHibernateTemplate().save(ob);
		this.getHibernateTemplate().flush();
		return ob;
	}

	/*
	 * public Department getDepartment(String name, String number) { Department
	 * department = getDepartmentByName(name); if (department == null) {
	 * department = getDepartmentByNumber(number); } if (department == null) {
	 * department = createDepartment(name, number); } return department; }
	 */

	public Classification getClassification(String name, String number,
			Department dept) {
		Classification classification = getClassificationByName(name, dept);
		if (classification == null) {
			classification = getClassificationByNumber(number, dept);
		}
		if (classification == null) {
			classification = createClassification(name, number, dept);
		}
		return classification;
	}

	public VendorStyle getVendorStyle(String name, String number,
			String vendorNumber, String vendorName,
			Classification classification) {
		Vendor vendor = getVendor(vendorName, vendorNumber);
		VendorStyle vendorStyle = getVendorStyleByName(name, vendor);
		if (vendorStyle == null) {
			vendorStyle = getVendorStyleByNumber(number, vendor);
		}
		if (vendorStyle == null) {
			vendorStyle = createVendorStyle(vendorName, name, vendorNumber,
					number, name, vendor, classification);
		}
		return vendorStyle;
	}

	public Vendor getVendor(String name, String number) {
		Vendor vendor = getVendorByName(name);
		if (vendor == null) {
			vendor = getVendorByNumber(number);
		}
		if (vendor == null) {
			vendor = createVendor(name, number);
		}
		return vendor;
	}

	// todo add product type into the mix on create
	public void doCarAttributes(Long carId, Long classId, Long deptId,
			Long productTypeId) {
		String sql = "begin;do_attributes(" + carId.toString() + ","
				+ classId.toString() + "," + deptId.toString() + ");end;";
		Session sess = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		Query query = sess.createSQLQuery(sql);
		query.executeUpdate();
		sess.close();
	}

	public Attribute getAttributeForName(String name) {
		Object ob = getHibernateTemplate().find("from Attribute where name=?",
				name);
		Collection<Attribute> col = (Collection) ob;
		for (Attribute attribute : col) {
			return attribute;
		}
		return null;
	}

	/*
	 * private CarSkuAttribute addCarSkuAttribute(Attribute attribute, VendorSku
	 * vendorSku, String loginName) { CarSkuAttribute csa = new
	 * CarSkuAttribute(loginName, loginName); csa.setAttribute(attribute);
	 * csa.setVendorSku(vendorSku); getHibernateTemplate().save(csa); return
	 * csa; }
	 */

	private Car createCarLogic(IdbCarDataTO idbCarTO, SourceType sourceType,
			User user, boolean runAttributes) {
		Car car = new Car();
		// car.setDepartment(getDepartment(idbCarTO.getDepartmentName(),
		// idbCarTO
		// .getDepartmentNumber()));
		// Classification classification = getClassification(idbCarTO
		// .getClassName(), idbCarTO.getClassNumber(), car.getDepartment());
		// car.setSourceType(sourceType);
		// car.setVendorStyle(getVendorStyle(idbCarTO.getVendorStyleDescription(),
		// idbCarTO.getVendorStyle(), idbCarTO.getVendorNumber(), idbCarTO
		// .getVendorName(), classification));
		// Date tempDate = new Date();
		// // car.setAssignedToRoleId(1);
		// car.setWorkFlow(getWorkFlowByName("TEST"));// temporary
		// car.setCarUserByLoggedByUserId(user);
		// //car.setCurrentWorkFlowStatus("TEST");// temporary TBD
		// car.setCreatedBy(user.getUsername());
		// car.setCreatedDate(tempDate);// temporary TBD
		// car.setIsUrgent("N");
		// car.setDueDate(tempDate);// temporary TBD
		// car.setIsProductTypeRequired("N");
		// car.setUpdatedBy(user.getUsername());
		// car.setUpdatedDate(new Date());
		// getHibernateTemplate().save(car);
		// String arr[] = { "COLOR", "COLORNAME", "SIZECODE", "SIZE" };
		// for (IdbCarSkuTO cs : idbCarTO.getSkuInfo()) {
		//
		// VendorSku vendorSku = new VendorSku();
		// vendorSku.setCar(car);
		// vendorSku.setBelkSku(cs.getBelkUPC());// todo change when we get
		// // Belk SKU
		// vendorSku.setBelkUpc(cs.getBelkUPC());
		// vendorSku.setCreatedBy(user.getUsername());
		// vendorSku.setUpdatedBy(user.getUsername());
		// vendorSku.setCreatedDate(new Date());
		// vendorSku.setUpdatedDate(new Date());
		// vendorSku.setStatusCd("ACTIVE");
		// getHibernateTemplate().save(vendorSku);
		// for (int i = 0; i < 4; i++) {
		// addCarSkuAttribute(getAttributeForName(arr[i]), vendorSku, user
		// .getUsername());
		// }
		//
		// }
		// getHibernateTemplate().flush();
		// if (runAttributes) {
		// doCarAttributes(car.getCarId(), classification.getClassId(),
		// classification.getDepartment().getDeptId(), null);
		// }
		return car;
	}

	public Car createCar(IdbCarDataTO idbCarTO, SourceType sourceType, User user) {
		return createCarLogic(idbCarTO, sourceType, user, true);
	}

	public Car updateCar(Car car, String updatedBy) {
		car.setUpdatedBy(updatedBy);
		car.setUpdatedDate(new Date());
		getHibernateTemplate().saveOrUpdate(car);
		getHibernateTemplate().flush();
		return car;
	}

	public Car getCarFromId(Long carNumber) {
		HibernateTemplate template = this.getHibernateTemplate();
		template.setCacheQueries(true);
		return (Car) template.get(Car.class, carNumber);
	}

	public Car assignProductTypeToCar(ProductType productType, Car car) {
		car.setIsProductTypeRequired("Y");
		doCarAttributes(car.getCarId(), car.getVendorStyle()
				.getClassification().getClassId(), car.getDepartment()
				.getDeptId(), productType.getProductTypeId());
		getHibernateTemplate().update(car);
		return car;
	}

	public CarAttribute getCarAttributeById(Long carAttributeId) {
		return (CarAttribute) getHibernateTemplate().get(CarAttribute.class,
				carAttributeId);
	}

	public Car updateAttributeValueById(Car car, long carAttributeId,
			String value) {
		getCarAttributeById(carAttributeId).setAttrValue(value);
		this.getHibernateTemplate().flush();
		return car;
	}

	public NoteType getNoteTypeFromCode(String noteTypeCd) {
		return (NoteType) getHibernateTemplate()
				.get(NoteType.class, noteTypeCd);
	}

	public Car addCarNote(Car car, boolean externallyDisplayed,
			String noteText, String noteTypeCd, String updatedBy) {
		CarNote carNote = new CarNote();
		carNote.setCar(car);
		carNote.setCreatedBy(updatedBy);
		carNote.setUpdatedBy(updatedBy);
		carNote.setCreatedDate(new Date());
		carNote.setUpdatedDate(new Date());
		if (externallyDisplayed) {
			carNote.setIsExternallyDisplayable("Y");
		} else {
			carNote.setIsExternallyDisplayable("N");
		}
		carNote.setNoteText(noteText);
		carNote.setNoteType(getNoteTypeFromCode(noteTypeCd));
		carNote.setStatusCd("ACTIVE");
		getHibernateTemplate().update(carNote);
		return car;
	}

	private CarAttribute addAttributeToCar(Car car, Attribute attribute,
			String value, String loginName) {
		CarAttribute ca = new CarAttribute();
		ca.setCar(car);
		ca.setAttribute(attribute);
		ca.setAttrValue(value);
		ca.setCreatedBy(loginName);
		ca.setUpdatedBy(loginName);
		ca.setCreatedDate(new Date());
		ca.setUpdatedDate(new Date());
		return ca;
	}

	/**
	 * {@inheritDoc}
	 */
	public User saveUser(User user) {
		log.debug("user's id: " + user.getId());
		getHibernateTemplate().saveOrUpdate(user);
		// necessary to throw a DataIntegrityViolation and catch it in
		// UserManager
		getHibernateTemplate().flush();
		return user;
	}

	public List<Car> getAllCars() {
		return getHibernateTemplate().find("from Car");
	}

	public List<Car> searchCars(CarSearchCriteria criteria) {

		List<Car> carList = null;
		String query = getCarQueryString(criteria,false);
		if(log.isDebugEnabled()){
			log.debug("search Cars Query:"+query);
		}
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery q = session.createSQLQuery(query)
					.addEntity("c", Car.class).addJoin("vs", "c.vendorStyle")
					.addJoin("v", "vs.vendor").addJoin("d", "c.department");
			// q.setCacheable(true);
			carList = convertToCarsList(q.list());
		} catch (Exception e) {
			log.error("Hibernate Exception");
		}

		/*
		 * if (carList == null) { carList = new ArrayList<Car>(); }
		 */
		
		return carList;
	}

	/**
	 * @param criteria
	 * @return carList
	 */
	@Override
	public List<Object> searchCarsForLastSearchReport(CarSearchCriteria criteria) {
		List<Object> lastSearchResults = null;
		String query = getCarQueryString(criteria,false);
		if(log.isDebugEnabled()){
			log.debug("search Cars Query:"+query);
		}
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery q =(SQLQuery) session.createSQLQuery(query)
					.addEntity("c", Car.class).addJoin("vs", "c.vendorStyle")
					.addJoin("v", "vs.vendor").addJoin("d", "c.department")
					.addScalar("colorName", new StringType());
			lastSearchResults = q.list();
		} catch (Exception e) {
			log.error("Hibernate Exception"+e);
		}
		return lastSearchResults;
	}
	
	/**
	 * function created to support the creation of an List becuase Hibernate
	 * does not support join on Native SQL queries
	 * 
	 * @param list
	 * @return
	 */
	private List<Car> convertToCarsList(List<Object> list) {

		if (list == null || list.isEmpty()) {
			return new ArrayList<Car>();
		}

		List<Car> filteredCars = new ArrayList<Car>(list.size());
		// Object[] objArray = list.toArray();
		for (Object obj : list) {
			// Object obj = objArray[i];
			int arrayLength = Array.getLength(obj);
			carLoop: for (int j = 0; j < arrayLength; j++) {
				Object o = Array.get(obj, j);
				if (o.getClass() == Car.class) {
					filteredCars.add((Car) o);
					break carLoop;
				}
			}
		}

		return filteredCars;

	}
	
	private String getCarQueryString(CarSearchCriteria criteria,boolean count) {
		StringBuffer queryBuf = new StringBuffer();
		if(count){
			queryBuf.append("select Count(c.car_id)  ");
		}else{
			queryBuf.append("select DISTINCT {c.*},{vs.*}, {d.*}, {v.*},to_char(c.due_date,'DD/MM/YYYY') due_date,to_char(c.expected_ship_date,'DD/MM/YYYY') e_date,c.expected_ship_date eShipDate,c.DUE_DATE dueDate,v.Name as vendorName,vs.VENDOR_STYLE_NAME as vendorStyleName,c.ASSIGNED_TO_USER_TYPE_CD assignedUserCd,c.CURRENT_WORKFLOW_STATUS statuscd,c.car_id  car ,c.SOURCE_ID source_cd,d.DEPT_CD depCd,c.SOURCE_TYPE_CD sourcecd ");
			if(StringUtils.isNotBlank(criteria.getReportName()) && criteria.getReportName().equals("Car Listing Report")){  
				  queryBuf.append(",(select decode(dbms_lob.SubStr(wm_concat(DISTINCT color_name)),'NO COLOR','','No Color','','no color','',null,'',dbms_lob.SubStr(wm_concat(DISTINCT color_name))) from vendor_sku where car_id = c.car_id) as colorName ");
			}
		}
			//String orderBy = " order by c.due_date asc, c.updated_date desc";
				String orderBy="";
				if(criteria.getSortColumnNm() != null && criteria.getSortColumnNm().length()>0 
						&& criteria.getSortOrder()!=null && criteria.getSortOrder().length()>0 && !count){
					orderBy= getOrderBy(criteria);
				}else{
					orderBy= "";
				}
					if (!criteria.isIgnoreUser()) {
	
					if(criteria.isIncludePromoType()){
						queryBuf.append("from Promo_TYPE pt,Car c ");
					}else{
						queryBuf.append(" from car c ");
					}
						queryBuf.append(" inner join department d on d.dept_id = c.dept_id ")
						.append(" inner join vendor_style vs on vs.vendor_style_id = c.vendor_style_id ")
						.append(" inner join vendor v on v.vendor_id = vs.vendor_id ")
						.append(" inner join classification cls on cls.class_id = vs.class_id ")
						.append(" inner join car_user_department cud on cud.dept_id = c.dept_id and cud.car_user_id="+ criteria.getUser().getId());
				if (StringUtils.isNotBlank(criteria.getProductType())) {
					queryBuf.append(" inner join product_type pt on pt.product_type_id = vs.product_type_id ");
				 }
				if (criteria.getUser().isVendor()) {
					queryBuf.append(" inner join car_user_vendor cuv on cuv.vendor_id = v.vendor_id and cuv.car_user_id = cud.car_user_id");
				}
				queryBuf.append(" where 0 = 0 ");
				if(criteria.isIncludePromoType()){
					if(criteria.getPromotionType().equals(Constants.PROMOTYPE_GWP)){
					queryBuf.append(" and c.car_id=pt.car_id and pt.is_gwp='Yes' ");
					}
					if(criteria.getPromotionType().equals(Constants.PROMOTYPE_PYG)){
					queryBuf.append(" and c.car_id=pt.car_id and pt.is_pyg='Yes' ");
					}
				}
				// Adding the code for Copy CAR functionality
				// Avoid vendor from searching for the cars of different dept,
				// on copy car
				if (criteria.isCopyCar() && criteria.getUser().isVendor()) {
					// To ensure the other dept cars will not be displayed.
					criteria.setCopyCar(false);
				}
				queryBuf = buildQueryFromCriteria(criteria, queryBuf);
				
				//Include outfit cars to search criteria only if includeOutFitCars flag is set.
				//It is set to true in SEarchREsultController and GetCarsController.java
				if(criteria.isIncludeOutFitCars() && (StringUtils.isBlank(criteria.getSourceTypeCd()) || SourceType.OUTFIT.equals(criteria.getSourceTypeCd()))){
				 if(!( criteria.getIsFromDashBoardAndSearchCar() && StringUtils.isBlank(criteria.getParentVendorStyleNumber())
						 && StringUtils.isNotBlank(criteria.getVendorStyleNumber())))
					 queryBuf.append(" UNION ALL (" +buildQueryForOutfit(criteria,count) + ")");
				}
				if(criteria.isIncludePYGCars() && (StringUtils.isBlank(criteria.getSourceTypeCd()) ||SourceType.PYG.equals(criteria.getSourceTypeCd()))){
					 if(!( criteria.getIsFromDashBoardAndSearchCar() && StringUtils.isBlank(criteria.getParentVendorStyleNumber())
							 && StringUtils.isNotBlank(criteria.getVendorStyleNumber())))
						queryBuf.append(" UNION ALL (" +buildQueryForPYG(criteria,count) + ")");	
				}
		} else {
			queryBuf.append(" from car c ")
					.append(" inner join department d on d.dept_id = c.dept_id ")
					.append(" inner join vendor_style vs on vs.vendor_style_id = c.vendor_style_id ")
					.append(" inner join vendor v on v.vendor_id = vs.vendor_id ");
					if (StringUtils.isNotBlank(criteria.getProductType())) {
						queryBuf.append(" inner join product_type pt on pt.product_type_id = vs.product_type_id ");
					}
			queryBuf.append(" inner join classification cls on cls.class_id = vs.class_id ")
			
					.append(" where 0 = 0 ");
			if (criteria.isCopyCar()){
				queryBuf.append(" and d.dept_cd != '" + Constants.OUTFIT_DEPT_NUMBER + "' " );
			}
			queryBuf = buildQueryFromCriteria(criteria, queryBuf);
		}
		if(log.isDebugEnabled()){
			log.debug(queryBuf.toString());
		}
		return queryBuf.append(orderBy).toString();
		//return queryBuf.toString(); 
	}

	private String getOrderBy(CarSearchCriteria criteria) {
		// TODO Auto-generated method stub
		String order="";
		String orderby="";
		if(criteria.getSortOrder().equals("ascending")){
			orderby="asc";
		}else{
			orderby="desc";
		}
		if(criteria.getSortColumnNm() !=null && criteria.getSortOrder() != null){
			switch(Integer.parseInt(criteria.getSortColumnNm())){
				case 3:
					order=" order by car "+orderby;
					break;
				case 4:
					order=" order by source_cd "+orderby;
					break;
				case 5:
					order=" order by  depCd "+orderby;
					break;
				case 6:
					order=" order by vendorName  "+orderby;
					break;
				case 7:
					order=" order by vendorStyleName "+orderby;
					break;
				case 8:
					order=" order by sourcecd "+orderby;
					break;
				case 9:
					order=" order by statuscd "+orderby;
					break;
				case 10:
					order=" order by assignedUserCd "+orderby;
					break;
				case 11:
					order=" order by dueDate "+orderby;
					break;
				case 12:
					order=" order by eShipDate "+orderby;
					break;
				
			}
		}
		return order;
	}

	/**
	 * @param criteria
	 * @param queryBuf
	 */
	private StringBuffer buildQueryFromCriteria(CarSearchCriteria criteria,
			StringBuffer queryBuf) {

		if (criteria.getUser() != null && !criteria.isIgnoreUser()) {
			if (criteria.getUser().isContentManager()
					|| criteria.getUser().isContentWriter()) {
				// Content Managers can see only published content
				if (StringUtils.isBlank(criteria.getContentStatus())
						&& !criteria.isFromSearch()) {
					criteria.setContentStatus(ContentStatus.PUBLISHED);
				}
			} else if (criteria.getUser().isSampleCoordinator()
					|| criteria.getUser().isVendor()) {
				if (StringUtils.isBlank(criteria.getCurrentUserType())) {
					criteria.setCurrentUserType(criteria.getUser()
							.getUserType().getUserTypeCd());
				}
			} else if (criteria.getUser().isArtDirector()) {
				if (StringUtils.isBlank(criteria.getCurrentUserType())) {
					if (!criteria.isFromSearch()) {
						criteria.setCurrentUserType(criteria.getUser()
								.getUserType().getUserTypeCd());
					}
				}
			} else if (criteria.getUser().isBuyer()) {
				if (!criteria.isFromSearch()) {
					queryBuf.append(" and (").append(
							"(c.assigned_to_user_type_cd = '").append(
							UserType.BUYER).append("')").append(" OR ").append(
							"(c.assigned_to_user_type_cd = '").append(
							UserType.VENDOR).append("')").append(")");
				}
			}
		}

		if (StringUtils.isNotBlank(criteria.getStatusCd())) {
			queryBuf.append(" and c.status_cd = '" + criteria.getStatusCd()
					+ "'");
			if (criteria.getStatusCd().equalsIgnoreCase("DELETED")) {
				criteria.setEnableDelete("N");
			} else {
				criteria.setEnableDelete("Y");
			}
		}
		
        // If a group is inactivated, but there are still styles in that group
        // that are active, then only the group should be hidden.
        // This change should not affect the regular products.
        if (StringUtils.isNotBlank(criteria.getVendorStyleStatusCd())) {
            queryBuf.append(" and ((vs.vendor_style_type_cd !='PRODUCT' and vs.status_cd = 'ACTIVE'");
            queryBuf.append(")");
            queryBuf.append(" or (vs.vendor_style_type_cd ='PRODUCT'");
            queryBuf.append(")");
            queryBuf.append(")");
        }

		if (StringUtils.isNotBlank(criteria.getSourceId())) {
			queryBuf.append(" and c.source_id = '" + criteria.getSourceId()
					+ "'");
		}

		if (StringUtils.isNotBlank(criteria.getSourceTypeCd())) {
			queryBuf.append(" and c.source_type_cd = '"
					+ criteria.getSourceTypeCd() + "'");
		}

		if (StringUtils.isNotBlank(criteria.getCarId())) {
			queryBuf.append(" and c.car_id = " + criteria.getCarId());
			//queryBuf.append(" OR C.SOURCE_TYPE_CD ='OUTFIT' )");
		}

		if (null != criteria.getArchive() && criteria.getArchive()) {
			queryBuf.append(" and c.archived='Y'");
			criteria.setEnableArchive("N");
		} else if (null != criteria.getArchive() && !criteria.getArchive()) {
			queryBuf.append(" and c.archived='N'");
			criteria.setEnableArchive("Y");
		}

		if (StringUtils.isNotBlank(criteria.getClassNumber())
				&& !criteria.isCopyCar()) {
			queryBuf.append(" and cls.belk_class_number = "	+ criteria.getClassNumber());
		}

		if (StringUtils.isNotBlank(criteria.getDeptCd())) {
			queryBuf.append(" and d.dept_cd = " + criteria.getDeptCd());
		}

		// Changed to accomodate for for CHILD Product being with a different
		// VENDOR NUMBER
		if(criteria.getIsFromDashBoardAndSearchCar() && StringUtils.isNotBlank(criteria.getVendorNumber()) && StringUtils.isNotBlank(criteria.getVendorStyleNumber())
				&& StringUtils.isNotBlank(criteria.getParentVendorStyleNumber())){
			queryBuf.append(" and exists (select 1 from vendor_style vs2 where vs2.parent_vendor_style_id = vs.vendor_style_id and vs2.vendor_style_number = '"
					+ StringUtils.replace(criteria.getVendorStyleNumber().toUpperCase(), "'", "''") + "')");
			queryBuf.append(" and (vs.vendor_style_number = '"+ StringUtils.replace(criteria.getParentVendorStyleNumber().toUpperCase(), "'", "''") + "')");
			queryBuf.append(" and (vs.vendor_number = '").append(criteria.getVendorNumber());
			queryBuf.append("')");	
		}else if (!criteria.getIsFromDashBoardAndSearchCar() && StringUtils.isNotBlank(criteria.getVendorNumber()) && StringUtils.isNotBlank(criteria.getVendorStyleNumber())) {
			queryBuf.append(" and (vs.vendor_number = '").append(criteria.getVendorNumber())
					.append("' and vs.vendor_style_number = '"+ StringUtils.replace(criteria.getVendorStyleNumber().toUpperCase(), "'",	"''") + "'");
			if (criteria.alsoSearchChildVendorStyle()) {
				//Added for improve performance
				StringBuffer sb=new StringBuffer();
				List<BigDecimal> carIds=null;
				carIds=getCarByVendorNumberAndVendorStyleNumber(criteria.getVendorNumber(), criteria.getVendorStyleNumber());
				if(carIds!=null && carIds.size()>0){
					for(BigDecimal str:carIds){
               	 		sb.append(str.toString()+",");
					}
					if(sb.toString().endsWith(",")) {
						sb.setLength(sb.length() - 1);  
					}
					//Ends Here
					if(sb.length()>0){
						queryBuf
							.append(
									" or c.car_id IN ("+sb		
									+ ")");
					}
				}
			}
			queryBuf.append(")");
		} else {
			if (StringUtils.isNotBlank(criteria.getVendorNumber())) {
				queryBuf.append(" and (vs.vendor_number = '"+ criteria.getVendorNumber() + "'");
				if (criteria.alsoSearchChildVendorStyle()) {
					queryBuf
							.append(" or exists (select 1 from vendor_style vs2 where vs2.parent_vendor_style_id = vs.vendor_style_id and vs2.vendor_number = '"
									+ criteria.getVendorNumber() + "'))");
				}
			}
			if(criteria.isCopyCar()){
				if (StringUtils.isNotBlank(criteria.getVendorStyleNumber())) {
					queryBuf.append(" and (vs.vendor_style_number = '"+ StringUtils.replace(criteria.getVendorStyleNumber().toUpperCase(), "'", "''") + "'");
					
					if (criteria.alsoSearchChildVendorStyle()) {
						queryBuf.append(" or exists (select 1 from vendor_style vs2 where vs2.parent_vendor_style_id = vs.vendor_style_id and vs2.vendor_style_number = '"
										+ StringUtils.replace(criteria.getVendorStyleNumber().toUpperCase(), "'", "''") + "'))");
					}
				}
			}else{
				if(StringUtils.isNotBlank(criteria.getVendorStyleNumber()) && StringUtils.isNotBlank(criteria.getParentVendorStyleNumber())){
					queryBuf.append(" and (vs.vendor_style_number = '"+ StringUtils.replace(criteria.getParentVendorStyleNumber().toUpperCase(), "'", "''") + "'");
					queryBuf.append(" and  exists (select 1 from vendor_style vs2 where vs2.parent_vendor_style_id = vs.vendor_style_id"+ ")");
					queryBuf.append(")");
					queryBuf.append("and c.car_id IN (");
					queryBuf.append("SELECT DISTINCT car_id FROM vendor_style v1, vendor_sku v2 WHERE v1.vendor_style_id = v2.vendor_style_id"); 
					queryBuf.append(" and v1.vendor_style_number = '"+ criteria.getVendorStyleNumber().toUpperCase()+"'");
					queryBuf.append(")");
					/*StringBuffer sb=new StringBuffer();
					List<BigDecimal> carIds=null;
					carIds=getCarByVendorNumberAndVendorStyleNumber(criteria.getVendorNumber(), criteria.getVendorStyleNumber());
					if(carIds!=null && carIds.size()>0){
						for(BigDecimal str:carIds){
	               	 		sb.append(str.toString()+",");
						}
						if(sb.toString().endsWith(",")) {
							sb.setLength(sb.length() - 1);  
						}
						if(sb.length()>0){
							queryBuf
								.append(
										" and c.car_id IN ("+sb		
										+ ")");
						}
					}*/
					
				}else if (StringUtils.isNotBlank(criteria.getParentVendorStyleNumber())) {
					queryBuf.append(" and (vs.vendor_style_number = '"+ StringUtils.replace(criteria.getParentVendorStyleNumber().toUpperCase(), "'", "''") + "'");
					queryBuf.append(" and  exists (select 1 from vendor_style vs2 where vs2.parent_vendor_style_id = vs.vendor_style_id"+ ")");
					queryBuf.append(")");
				}else if (StringUtils.isNotBlank(criteria.getVendorStyleNumber())) {
					queryBuf.append(" and ((vs.vendor_style_number = '"+ StringUtils.replace(criteria.getVendorStyleNumber().toUpperCase(), "'", "''") + "'");
					queryBuf.append(" and Not exists (select 1 from vendor_style vs2 where vs2.parent_vendor_style_id = vs.vendor_style_id"+ "))");
					StringBuffer sb=new StringBuffer();
					List<BigDecimal> carIds=null;
					carIds=getCarByVendorNumberAndVendorStyleNumber(criteria.getVendorNumber(), criteria.getVendorStyleNumber());
					if(carIds!=null && carIds.size()>0){
						for(BigDecimal str:carIds){
	               	 		sb.append(str.toString()+",");
						}
						if(sb.toString().endsWith(",")) {
							sb.setLength(sb.length() - 1);  
						}
						if(sb.length()>0){
							queryBuf
								.append(
										" or c.car_id IN ("+sb		
										+ ")");
						}
					}
					queryBuf.append(")");
				}
			}
		}

		if (criteria.isReadyToSendToCMP()) {
			queryBuf
					.append(" and (")
					.append(
							" (c.content_status_cd IN ('"
									+ ContentStatus.PUBLISHED + "','"
									+ ContentStatus.RESEND_TO_CMP + "','"
									+ ContentStatus.RESEND_DATA_TO_CMP + "','"
									+ ContentStatus.DISABLE_IN_CMP + "','"									
									+ ContentStatus.ENABLE_IN_CMP + "')) ")
					.append(" or ")
					.append(
							" ( exists (select 1 from car_image ci inner join image i on i.image_id = ci.image_id where ci.car_id = c.car_id and i.image_tracking_status_cd = '")
					.append(ImageTrackingStatus.RECEIVED).append(
							"' and i.image_processing_status_cd = '").append(
							Image.PROCESSING_STATUS_PENDING).append("'))")
					.append(")");
		}

		if (criteria.isReadyToClose()) {
			queryBuf
					.append(" and (")
					.append(
							" (c.content_status_cd = '"
									+ ContentStatus.SENT_TO_CMP + "') ")
					.append(" and ")
					.append(
							" ( not exists (select 1 from car_image ci inner join image i on i.image_id = ci.image_id where ci.car_id = c.car_id and i.status_cd != 'DELETED' and i.image_tracking_status_cd IN ('")
					.append(ImageTrackingStatus.REQUESTED).append("','")
					.append(ImageTrackingStatus.APPROVED).append("')))")
					.append(")");
		}

		if (StringUtils.isNotBlank(criteria.getContentStatus())) {
			queryBuf.append(" and c.content_status_cd = '"
					+ criteria.getContentStatus() + "'");
		} else if (criteria.getContentStatusList() != null && !criteria.getContentStatusList().isEmpty()) {
                    queryBuf.append(" and c.content_status_cd in (");
                    queryBuf.append("'").append(criteria.getContentStatusList().get(0)).append("'");
                    for (int i = 1; i < criteria.getContentStatusList().size(); i++) {
                        queryBuf.append(",'").append(criteria.getContentStatusList().get(i)).append("'");
                    }
                    queryBuf.append(") ");
                }

		if (StringUtils.isNotBlank(criteria.getVendorName())) {
			queryBuf.append(" and upper(v.name) like '%"
					+ StringUtils.replace(StringUtils.upperCase(criteria
							.getVendorName()), "'", "''") + "%'");
		}
		if (StringUtils.isNotBlank(criteria.getProductType())) {
			queryBuf.append(" and upper(pt.name) like '%"
					+ StringUtils.replace(StringUtils.upperCase(criteria
							.getProductType()), "'", "''") + "%'");
		}

		if (StringUtils.isNotBlank(criteria.getVendorUpc())) {
			if (!criteria.getVendorUpc().startsWith("'")) {
				criteria.setVendorUpc("'" + criteria.getVendorUpc() + "'");
			}
			queryBuf
					.append(" and c.car_id in (select distinct car_id from vendor_sku where vendor_upc in ("
							+ criteria.getVendorUpc() + "))");
		}
		if (StringUtils.isNotBlank(criteria.getBelkUPC())) {
			if (!criteria.getBelkUPC().startsWith("'")) {
				criteria.setBelkUPC("'" + criteria.getBelkUPC() + "'");
			}
			queryBuf
					.append(" and c.car_id in (select distinct car_id from vendor_sku where belk_upc in ("
							+ criteria.getBelkUPC() + "))");
		}
		if (StringUtils.isNotBlank(criteria.getWorkflowStatus())) {
			if ("LATE".equals(criteria.getWorkflowStatus())) {
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DATE, -1);
				Date toDate = c.getTime();
				queryBuf.append(" and c.due_date <= to_date('"+ DateUtils.formatDate(toDate) + "','mm/dd/yyyy')");
				queryBuf.append(" and c.current_workflow_status <> '" + WorkflowStatus.CLOSED + "'");
				if (criteria.getUser().getUserTypeCd().equalsIgnoreCase(UserType.BUYER)) {
					queryBuf.append(" and c.assigned_to_user_type_cd in ('"	+ UserType.BUYER + "','" + UserType.VENDOR + "')");
				} else if (criteria.getUser().getUserTypeCd().equalsIgnoreCase(UserType.VENDOR)) {
					queryBuf.append(" and c.assigned_to_user_type_cd = '"+ UserType.VENDOR + "'");
				}
			}else if(criteria.getWorkflowStatus().equalsIgnoreCase("IMAGE_FAILED_IN_CC")){

	            queryBuf.append(" and c.current_workflow_status IN ('" + WorkflowStatus.IMAGE_FAILED_IN_CC +"','" + WorkflowStatus.IMAGE_FAILED_IN_MC +"')");
            }
			else {
				queryBuf.append(" and c.current_workflow_status = '"
						+ criteria.getWorkflowStatus() + "'");
			}
		}
		
		if (criteria.isIgnoreClosedCars()) {
			queryBuf.append(" and c.current_workflow_status != '"
					+ WorkflowStatus.CLOSED + "'");
		}

		if (StringUtils.isNotBlank(criteria.getCurrentUserType())) {
			if (!(criteria.getUser().getUserTypeCd().equalsIgnoreCase(UserType.BUYER)
					&& criteria.getCurrentUserType().equalsIgnoreCase(UserType.BUYER))) {
			  queryBuf.append(" and c.assigned_to_user_type_cd = '").append(
					criteria.getCurrentUserType()).append("' ");
			} else if(criteria.getUser().getUserTypeCd().equalsIgnoreCase(UserType.BUYER)
					&& criteria.getCurrentUserType().equalsIgnoreCase(UserType.BUYER) && !criteria.isFromSearch()){
				queryBuf.append(" and c.assigned_to_user_type_cd in ('"	+ UserType.BUYER + "','" + UserType.VENDOR + "')");
								
		   } else if(criteria.getUser().getUserTypeCd().equalsIgnoreCase(UserType.BUYER)
					&& criteria.getCurrentUserType().equalsIgnoreCase(UserType.BUYER) && criteria.isFromSearch()) {
			   queryBuf.append(" and c.assigned_to_user_type_cd = '").append(
						criteria.getCurrentUserType()).append("' ");
		   } else {
			   queryBuf.append(" and c.assigned_to_user_type_cd = '").append(
						criteria.getCurrentUserType()).append("' ");
		   }
		}

		if ((StringUtils.isNotBlank(criteria.getDueDateFrom()) && StringUtils
				.isNotBlank(criteria.getDueDateTo()))) {
			queryBuf.append(" and c.due_date BETWEEN to_date('"
					+ criteria.getDueDateFrom()
					+ "','mm/dd/yyyy') AND to_date('" + criteria.getDueDateTo()
					+ "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(criteria.getDueDateFrom())) {
			queryBuf.append(" and c.due_date >= to_date('"
					+ criteria.getDueDateFrom() + "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(criteria.getDueDateTo())) {
			queryBuf.append(" and c.due_date <= to_date('"
					+ criteria.getDueDateTo() + "','mm/dd/yyyy')");
		}

		if ((StringUtils.isNotBlank(criteria.getExpShipDateFrom()) && StringUtils
				.isNotBlank(criteria.getExpShipDateTo()))) {
			queryBuf.append(" and c.expected_ship_date BETWEEN to_date('"
					+ criteria.getExpShipDateFrom()
					+ "','mm/dd/yyyy') AND to_date('"
					+ criteria.getExpShipDateTo() + "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(criteria.getExpShipDateFrom())) {
			queryBuf.append(" and c.expected_ship_date >= to_date('"
					+ criteria.getExpShipDateFrom() + "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(criteria.getExpShipDateTo())) {
			queryBuf.append(" and c.expected_ship_date <= to_date('"
					+ criteria.getExpShipDateTo() + "','mm/dd/yyyy')");
		}

		if (StringUtils.isNotBlank(criteria.getCreateDate())) {
			queryBuf.append(" and trunc(c.created_date) = to_date('"
					+ criteria.getCreateDate() + "','mm/dd/yyyy')");
		}

		if (StringUtils.isNotBlank(criteria.getUpdateDate())) {
			if (criteria.isNotUpdatedSince()) {
				queryBuf.append(" and trunc(c.updated_date) <= to_date('"
						+ criteria.getUpdateDate() + "','mm/dd/yyyy')");
			} else {
				queryBuf.append(" and trunc(c.updated_date) = to_date('"
						+ criteria.getUpdateDate() + "','mm/dd/yyyy')");
			}
		}

		if (criteria.hasRecievedImages()) {
			queryBuf
					.append(
							" and exists (select 1 from car_image ci inner join image i on i.image_id = ci.image_id where ci.car_id = c.car_id and i.image_tracking_status_cd = '")
					.append(ImageTrackingStatus.RECEIVED).append("')");
		}

		if (criteria.hasRequestedImages()) {
			queryBuf
					.append(
							" and exists (select 1 from car_image ci inner join image i on i.image_id = ci.image_id where ci.car_id = c.car_id ")
					.append(" and (")
					.append(" (i.image_tracking_status_cd = '").append(
							ImageTrackingStatus.REQUESTED).append(
							"' and i.image_source_type_cd = '").append(
							ImageSourceType.VENDOR).append("')").append(" or ")
					.append(" (i.image_tracking_status_cd = '").append(
							ImageTrackingStatus.APPROVED).append(
							"' and i.image_source_type_cd = '").append(
							ImageSourceType.ON_HAND).append("')").append("))");
		}
		return queryBuf;
	}
	
	/**
	 * 
	 * @param criteria
	 * @return Outfit cars search query 
	 * This method will return only the outfit cars as a search result [which user has access to]
	 * THis query is in two parts and those are joined with UNION - 
	 * First part return the outfit cars base on child cars search criteria 
	 *  eg: If you search which child car vendor style you will get OUTFIT car which that child car belong to
	 * Second part returns the search criteria according to outfit cars search criteria
	 * 	eg: searching with outfit vendor style number will return the outfit car
	 */
	private String buildQueryForOutfit(CarSearchCriteria criteria,boolean fCount){
		
		StringBuffer strBuffOutfitQuery = new StringBuffer();
		if(fCount){
			strBuffOutfitQuery.append("	select Count(c.car_id) ");	
		}else{
			strBuffOutfitQuery.append("	select DISTINCT {c.*},{vs.*}, {d.*}, {v.*},to_char(c.due_date,'DD/MM/YYYY') due_date,to_char(c.expected_ship_date,'DD/MM/YYYY') e_date ,c.expected_ship_date eShipDate,c.DUE_DATE dueDate,v.Name as vendorName,vs.VENDOR_STYLE_NAME as vendorStyleName,c.ASSIGNED_TO_USER_TYPE_CD assignedUserCd,c.CURRENT_WORKFLOW_STATUS statuscd,c.car_id  car,c.SOURCE_ID source_cd ,c.SOURCE_TYPE_CD sourcecd,d.DEPT_CD depCd");
			if(StringUtils.isNotBlank(criteria.getReportName()) && criteria.getReportName().equals("Car Listing Report")) {
		    	  strBuffOutfitQuery.append(",(select decode(dbms_lob.SubStr(wm_concat(DISTINCT color_name)),'NO COLOR','','No Color','','no color','',null,'',dbms_lob.SubStr(wm_concat(DISTINCT color_name))) from vendor_sku where car_id = c.car_id) as colorName ");
		      }
		}		
		
					      if(criteria.isIncludePromoType()){
					    	  strBuffOutfitQuery.append(" from Promo_Type pt,car c  inner join department d on d.dept_id = c.dept_id ") ; 
					      }else{
					    	  strBuffOutfitQuery.append(" from car c  inner join department d on d.dept_id = c.dept_id") ; 
					      }
					      strBuffOutfitQuery.append(" inner join vendor_style vs on vs.vendor_style_id = c.vendor_style_id")  
					      .append(" inner join vendor v on v.vendor_id = vs.vendor_id")  
					      .append(" inner join classification cls on cls.class_id = vs.class_id")
					      .append(" where 0 = 0 ");
					      if(criteria.isIncludePromoType()){
								if(criteria.getPromotionType().equals(Constants.PROMOTYPE_GWP)){
								strBuffOutfitQuery.append(" and c.car_id=pt.car_id and pt.is_gwp='Yes' ");
								}
								if(criteria.getPromotionType().equals(Constants.PROMOTYPE_PYG)){
								strBuffOutfitQuery.append(" and c.car_id=pt.car_id and pt.is_pyg='Yes' ");
								}
							}
					      
					        // If a group is inactivated, but there are still styles in that group
					        // that are active, then only the group should be hidden.
					        // This change should not affect the regular products.
					        if (StringUtils.isNotBlank(criteria.getVendorStyleStatusCd())) {
					            strBuffOutfitQuery.append(" and ((vs.vendor_style_type_cd !='PRODUCT' and vs.status_cd = 'ACTIVE'");
					            strBuffOutfitQuery.append(")");
					            strBuffOutfitQuery.append(" or (vs.vendor_style_type_cd ='PRODUCT'");
					            strBuffOutfitQuery.append(")");
					            strBuffOutfitQuery.append(")");
					        }
					        
					if(criteria.getIsFromDashBoardAndSearchCar() && StringUtils.isNotBlank(criteria.getVendorStyleNumber()) && StringUtils.isNotBlank(criteria.getParentVendorStyleNumber())){
							strBuffOutfitQuery.append(" and c.car_id in 	");
							//	Second part of query  starts here :second part of query gives outfit cars based on search criteria
							//Eg: searchig with outfit vendor style number will give you outfit car
							strBuffOutfitQuery.append("(( select unique(c.car_id) as car_id ")
						  		.append(" from (  select coc.outfit_car_id as car_id ")
						  		.append(" from  car_outfit_child coc " );
							if(criteria.getUser().isVendor()){
								strBuffOutfitQuery.append(" inner join car_user_vendor cuv on cuv.vendor_id = coc.vendor_id ");	
								strBuffOutfitQuery.append(" inner join car c ON c.car_id = coc.CHILD_CAR_ID ");	
							}else{
								strBuffOutfitQuery.append(" inner join car c ON c.car_id = coc.CHILD_CAR_ID")
			  				  		.append(" inner join department d on d.dept_id = c.dept_id   ")
			  				  		.append(" inner join car_user_department cud on cud.dept_id = c.dept_id ");
							}
							strBuffOutfitQuery.append(" inner join vendor_style vs on vs.vendor_style_id = c.vendor_style_id ");
							strBuffOutfitQuery.append("where 0=0 ");
							if(Status.ACTIVE.equalsIgnoreCase(criteria.getStatusCd())){
								strBuffOutfitQuery.append(" and coc.status_cd ='ACTIVE'");
							}
							if(criteria.getUser().isVendor()){
								strBuffOutfitQuery.append(" and cuv.car_user_id = "+ criteria.getUser().getId());
							}else{
								strBuffOutfitQuery.append(" and cud.car_user_id = "+ criteria.getUser().getId());
							}
							if(StringUtils.isNotBlank(criteria.getVendorStyleNumber())){
								strBuffOutfitQuery.append(" and vs.vendor_style_number = '"+ criteria.getVendorStyleNumber().toUpperCase() +"'");
							}
							strBuffOutfitQuery.append("  )child_cars ")
						  		.append(" inner join car c on c.car_id = child_cars.car_id ")
						  		.append(" inner join vendor_style vs on vs.vendor_style_id = c.vendor_style_id  ")
						  		.append(" inner join classification cls on cls.class_id = vs.class_id  ");
							if (StringUtils.isNotBlank(criteria.getProductType())) {
								strBuffOutfitQuery.append(" inner join product_type pt on pt.product_type_id = vs.product_type_id ");
							}
							strBuffOutfitQuery.append(" where 0=0 ");
							if (StringUtils.isNotBlank(criteria.getProductType())) {
								strBuffOutfitQuery.append(" and upper(pt.name) like '%"	+ StringUtils.replace(StringUtils.upperCase(criteria.getProductType()), "'", "''") + "%'");
							}
							if(StringUtils.isNotBlank(criteria.getParentVendorStyleNumber())){
								strBuffOutfitQuery.append(" and vs.vendor_style_number = '"+ criteria.getParentVendorStyleNumber().toUpperCase() +"'");
							}
							if (StringUtils.isNotBlank(criteria.getVendorNumber())) {
								strBuffOutfitQuery.append(" and vs.vendor_number = '"+ criteria.getVendorNumber() + "'");
							}
							//createOutfitChildCriteria(criteria, strBuffOutfitQuery);
							strBuffOutfitQuery.append(" ))");
							//second part of query ends here
					}else if(criteria.getIsFromDashBoardAndSearchCar() && StringUtils.isNotBlank(criteria.getVendorStyleNumber())){
							strBuffOutfitQuery.append(" and c.car_id in 	");
							//this is start of : first part of query which will give outfit cars based on child cars search
							// Eg: Searching with outfit child cars vendor style number will give you the outfit car
							strBuffOutfitQuery.append("(( select unique(coc.outfit_car_id) as car_id")
						  		.append(" from  car_outfit_child coc inner join car c ON c.car_id = coc.outfit_car_id ")
						  		.append(" inner join department d on d.dept_id = c.dept_id  ")
						  		.append(" inner join vendor_style vs on vs.vendor_style_id = c.vendor_style_id ")
						  		.append(" inner join classification cls on cls.class_id = vs.class_id ")
						  		.append(" inner join car_user_department cud on cud.dept_id = c.dept_id ");
							if (StringUtils.isNotBlank(criteria.getProductType())) {
					    	  strBuffOutfitQuery.append(" inner join product_type pt on pt.product_type_id = vs.product_type_id ");
							}
		
							//for outfit cars vendor will be able to see outfits which are specially assigned to them by referring to 
							//vendor_id column of CAR_OUTFIT_CHILD table - so taking join on coc.vendor_id
							if(criteria.getUser().isVendor()){
								strBuffOutfitQuery.append(" inner join vendor v on v.vendor_id = coc.vendor_id ")
							  	.append(" inner join car_user_vendor cuv on cuv.vendor_id = v.vendor_id ");
							}
							strBuffOutfitQuery.append(" where 0 = 0 " );
							if(Status.ACTIVE.equalsIgnoreCase(criteria.getStatusCd())){
								strBuffOutfitQuery.append("and coc.status_cd ='ACTIVE'");
							}
							//Vendor will be able to see only cars of his department and assigned to his vendor number 
							if(criteria.getUser().isVendor()){
								strBuffOutfitQuery.append(" and cuv.car_user_id = "+ criteria.getUser().getId()); 
							}else{
								strBuffOutfitQuery.append(" and cud.car_user_id = "+ criteria.getUser().getId());  
							}
							if (StringUtils.isNotBlank(criteria.getProductType())) {
								strBuffOutfitQuery.append(" and upper(pt.name) like '%"	+ StringUtils.replace(StringUtils.upperCase(criteria.getProductType()), "'", "''") + "%'");
							}	
							if(StringUtils.isNotBlank(criteria.getVendorStyleNumber())){
								strBuffOutfitQuery.append(" and vs.vendor_style_number = '"+ criteria.getVendorStyleNumber().toUpperCase() +"'");
							}
							if (StringUtils.isNotBlank(criteria.getVendorNumber())) {
								strBuffOutfitQuery.append(" and vs.vendor_number = '"+ criteria.getVendorNumber() + "'");
							}
							//createOutfitChildCriteria(criteria, strBuffOutfitQuery);
							// this is end of first of query
							strBuffOutfitQuery.append(" )) "); 
					}else if(criteria.getIsFromDashBoardAndSearchCar() && StringUtils.isNotBlank(criteria.getParentVendorStyleNumber())){
							strBuffOutfitQuery.append(" and c.car_id in 	");
							//	Second part of query  starts here :second part of query gives outfit cars based on search criteria
							//Eg: searchig with outfit vendor style number will give you outfit car
							strBuffOutfitQuery.append("(( select unique(c.car_id) as car_id ")
						  		.append(" from (  select coc.outfit_car_id as car_id ")
						  		.append(" from  car_outfit_child coc " );
							if(criteria.getUser().isVendor()){
								strBuffOutfitQuery.append(" inner join car_user_vendor cuv on cuv.vendor_id = coc.vendor_id ");					
							}else{
								strBuffOutfitQuery.append(" inner join car c ON c.car_id = coc.CHILD_CAR_ID")
			  				  		.append(" inner join department d on d.dept_id = c.dept_id   ")
			  				  		.append(" inner join car_user_department cud on cud.dept_id = c.dept_id ");
							}
							strBuffOutfitQuery.append("where 0=0 ");
							if(Status.ACTIVE.equalsIgnoreCase(criteria.getStatusCd())){
								strBuffOutfitQuery.append(" and coc.status_cd ='ACTIVE'");
							}
							if(criteria.getUser().isVendor()){
								strBuffOutfitQuery.append(" and cuv.car_user_id = "+ criteria.getUser().getId());
							}else{
								strBuffOutfitQuery.append(" and cud.car_user_id = "+ criteria.getUser().getId());
							}
							strBuffOutfitQuery.append("  )child_cars ")
						  		.append(" inner join car c on c.car_id = child_cars.car_id ")
						  		.append(" inner join vendor_style vs on vs.vendor_style_id = c.vendor_style_id  ")
						  		.append(" inner join classification cls on cls.class_id = vs.class_id  ");
							if (StringUtils.isNotBlank(criteria.getProductType())) {
								strBuffOutfitQuery.append(" inner join product_type pt on pt.product_type_id = vs.product_type_id ");
							}
							strBuffOutfitQuery.append(" where 0=0 ");
							if (StringUtils.isNotBlank(criteria.getProductType())) {
								strBuffOutfitQuery.append(" and upper(pt.name) like '%"	+ StringUtils.replace(StringUtils.upperCase(criteria.getProductType()), "'", "''") + "%'");
							}
							if(StringUtils.isNotBlank(criteria.getParentVendorStyleNumber())){
								strBuffOutfitQuery.append(" and vs.vendor_style_number = '"+ criteria.getParentVendorStyleNumber().toUpperCase() +"'");
							}
							if (StringUtils.isNotBlank(criteria.getVendorNumber())) {
								strBuffOutfitQuery.append(" and vs.vendor_number = '"+ criteria.getVendorNumber() + "'");
							}
							//createOutfitChildCriteria(criteria, strBuffOutfitQuery);
							strBuffOutfitQuery.append(" ))");
							//second part of query ends here
						}else{	
							strBuffOutfitQuery.append(" and c.car_id in 	");
							//this is start of : first part of query which will give outfit cars based on child cars search
							// Eg: Searching with outfit child cars vendor style number will give you the outfit car
							strBuffOutfitQuery.append("(( select unique(coc.outfit_car_id) as car_id")
						  		.append(" from  car_outfit_child coc inner join car c ON c.car_id = coc.CHILD_CAR_ID ")
						  		.append(" inner join department d on d.dept_id = c.dept_id  ")
						  		.append(" inner join vendor_style vs on vs.vendor_style_id = c.vendor_style_id ")
						  		.append(" inner join classification cls on cls.class_id = vs.class_id ")
						  		.append(" inner join car_user_department cud on cud.dept_id = c.dept_id ");
							if (StringUtils.isNotBlank(criteria.getProductType())) {
								strBuffOutfitQuery.append(" inner join product_type pt on pt.product_type_id = vs.product_type_id ");
							}
		
							//for outfit cars vendor will be able to see outfits which are specially assigned to them by referring to 
							//vendor_id column of CAR_OUTFIT_CHILD table - so taking join on coc.vendor_id
							if(criteria.getUser().isVendor()){
								strBuffOutfitQuery.append(" inner join vendor v on v.vendor_id = coc.vendor_id ")
							  		.append(" inner join car_user_vendor cuv on cuv.vendor_id = v.vendor_id ");
							}
							strBuffOutfitQuery.append(" where 0 = 0 " );
							if(Status.ACTIVE.equalsIgnoreCase(criteria.getStatusCd())){
								strBuffOutfitQuery.append("and coc.status_cd ='ACTIVE'");
							}
							//Vendor will be able to see only cars of his department and assigned to his vendor number 
							if(criteria.getUser().isVendor()){
								strBuffOutfitQuery.append(" and cuv.car_user_id = "+ criteria.getUser().getId()); 
							}else{
								strBuffOutfitQuery.append(" and cud.car_user_id = "+ criteria.getUser().getId());  
							}
							if (StringUtils.isNotBlank(criteria.getProductType())) {
								strBuffOutfitQuery.append(" and upper(pt.name) like '%"	+ StringUtils.replace(StringUtils.upperCase(criteria.getProductType()), "'", "''") + "%'");
							}	
							if(StringUtils.isNotBlank(criteria.getVendorStyleNumber())){
								strBuffOutfitQuery.append(" and vs.vendor_style_number = '"+ criteria.getVendorStyleNumber().toUpperCase() +"'");
							}
							if (StringUtils.isNotBlank(criteria.getVendorNumber())) {
								strBuffOutfitQuery.append(" and vs.vendor_number = '"+ criteria.getVendorNumber() + "'");
							}
							//createOutfitChildCriteria(criteria, strBuffOutfitQuery);
							// this is end of first of query
		
							strBuffOutfitQuery.append(")  union all ( ");
		
							//	Second part of query  starts here :second part of query gives outfit cars based on search criteria
							//Eg: searchig with outfit vendor style number will give you outfit car
							strBuffOutfitQuery.append(" select unique(c.car_id) as car_id ")
						  		.append(" from (  select coc.outfit_car_id as car_id ")
						  		.append(" from  car_outfit_child coc " );
							if(criteria.getUser().isVendor()){
								strBuffOutfitQuery.append(" inner join car_user_vendor cuv on cuv.vendor_id = coc.vendor_id ");					
							}else{
								strBuffOutfitQuery.append(" inner join car c ON c.car_id = coc.CHILD_CAR_ID")
			  				  		.append(" inner join department d on d.dept_id = c.dept_id   ")
			  				  		.append(" inner join car_user_department cud on cud.dept_id = c.dept_id ");
							}
							strBuffOutfitQuery.append("where 0=0 ");
							if(Status.ACTIVE.equalsIgnoreCase(criteria.getStatusCd())){
								strBuffOutfitQuery.append(" and coc.status_cd ='ACTIVE'");
							}
							if(criteria.getUser().isVendor()){
								strBuffOutfitQuery.append(" and cuv.car_user_id = "+ criteria.getUser().getId());
							}else{
								strBuffOutfitQuery.append(" and cud.car_user_id = "+ criteria.getUser().getId());
							}
							strBuffOutfitQuery.append("  )child_cars ")
						  		.append(" inner join car c on c.car_id = child_cars.car_id ")
						  		.append(" inner join vendor_style vs on vs.vendor_style_id = c.vendor_style_id  ")
						  		.append(" inner join classification cls on cls.class_id = vs.class_id  ");
							if (StringUtils.isNotBlank(criteria.getProductType())) {
								strBuffOutfitQuery.append(" inner join product_type pt on pt.product_type_id = vs.product_type_id ");
							}
							strBuffOutfitQuery.append(" where 0=0 ");
							if (StringUtils.isNotBlank(criteria.getProductType())) {
								strBuffOutfitQuery.append(" and upper(pt.name) like '%"	+ StringUtils.replace(StringUtils.upperCase(criteria.getProductType()), "'", "''") + "%'");
							}
							if(StringUtils.isNotBlank(criteria.getVendorStyleNumber())){
								strBuffOutfitQuery.append(" and vs.vendor_style_number = '"+ criteria.getVendorStyleNumber().toUpperCase() +"'");
							}
							if (StringUtils.isNotBlank(criteria.getVendorNumber())) {
								strBuffOutfitQuery.append(" and vs.vendor_number = '"+ criteria.getVendorNumber() + "'");
							}
							//createOutfitChildCriteria(criteria, strBuffOutfitQuery);
							strBuffOutfitQuery.append(" ))");
							//second part of query ends here
						}
		if (StringUtils.isNotBlank(criteria.getCarId())) {
			strBuffOutfitQuery.append(" and c.car_id = " + criteria.getCarId());
		}
		if (criteria.getUser() != null && !criteria.isIgnoreUser()) {
			if (criteria.getUser().isContentManager()
					|| criteria.getUser().isContentWriter()) {
				// Content Managers can see only published content
				if (StringUtils.isBlank(criteria.getContentStatus())
						&& !criteria.isFromSearch()) {
					criteria.setContentStatus(ContentStatus.PUBLISHED);
				}
			} else if (criteria.getUser().isSampleCoordinator()
					|| criteria.getUser().isVendor()) {
				if (StringUtils.isBlank(criteria.getCurrentUserType())) {
					criteria.setCurrentUserType(criteria.getUser()
							.getUserType().getUserTypeCd());
				}
			} else if (criteria.getUser().isArtDirector()) {
				if (StringUtils.isBlank(criteria.getCurrentUserType())) {
					if (!criteria.isFromSearch()) {
						criteria.setCurrentUserType(criteria.getUser()
								.getUserType().getUserTypeCd());
					}
				}
			} else if (criteria.getUser().isBuyer()) {
				if (!criteria.isFromSearch()) {
					strBuffOutfitQuery.append(" and (").append("(c.assigned_to_user_type_cd = '")
										.append(UserType.BUYER).append("')").append(" OR ").append("(c.assigned_to_user_type_cd = '")
										.append(UserType.VENDOR).append("')").append(")");
				}
			}
		}

		if (StringUtils.isNotBlank(criteria.getStatusCd())) {
			strBuffOutfitQuery.append(" and c.status_cd = '" + criteria.getStatusCd()+ "'");
		}
		if (null != criteria.getArchive() && criteria.getArchive()) {
			strBuffOutfitQuery.append(" and c.archived='Y'");
		} else if (null != criteria.getArchive() && !criteria.getArchive()) {
			strBuffOutfitQuery.append(" and c.archived='N'");
		}
		createOutfitChildCriteria(criteria, strBuffOutfitQuery);
		if (StringUtils.isNotBlank(criteria.getContentStatus())) {
			strBuffOutfitQuery.append(" and c.content_status_cd = '"+ criteria.getContentStatus() + "'");
		}
		
		if (StringUtils.isNotBlank(criteria.getWorkflowStatus())) {
			if ("LATE".equals(criteria.getWorkflowStatus())) {
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DATE, -1);
				Date toDate = c.getTime();
				strBuffOutfitQuery.append(" and c.due_date <= to_date('"+ DateUtils.formatDate(toDate) + "','mm/dd/yyyy')");
				strBuffOutfitQuery.append(" and c.current_workflow_status <> '" + WorkflowStatus.CLOSED + "'");
				if (criteria.getUser().getUserTypeCd().equalsIgnoreCase(UserType.BUYER)) {
					strBuffOutfitQuery.append(" and c.assigned_to_user_type_cd in ('"	+ UserType.BUYER + "','" + UserType.VENDOR + "')");
				} else if (criteria.getUser().getUserTypeCd().equalsIgnoreCase(UserType.VENDOR)) {
					strBuffOutfitQuery.append(" and c.assigned_to_user_type_cd = '"+ UserType.VENDOR + "'");
				}
			}else if(criteria.getWorkflowStatus().equalsIgnoreCase("IMAGE_FAILED_IN_CC")){

				strBuffOutfitQuery.append(" and c.current_workflow_status IN ('" + WorkflowStatus.IMAGE_FAILED_IN_CC +"','" + WorkflowStatus.IMAGE_FAILED_IN_MC +"')");
            } else {
				strBuffOutfitQuery.append(" and c.current_workflow_status = '"+ criteria.getWorkflowStatus() + "'");
			}
		}

		if (StringUtils.isNotBlank(criteria.getCurrentUserType())) {
			if(criteria.getUser().getUserTypeCd().equalsIgnoreCase(UserType.BUYER)
					&& criteria.getCurrentUserType().equalsIgnoreCase(UserType.BUYER) && !criteria.isFromSearch()){
				strBuffOutfitQuery.append(" and c.current_workflow_status <> '" + WorkflowStatus.CLOSED + "'");
								
		   }  
			strBuffOutfitQuery.append(" and c.assigned_to_user_type_cd = '").append(
					criteria.getCurrentUserType()).append("' ");
		}
		if(criteria.getUser().getUserTypeCd().equalsIgnoreCase(UserType.BUYER)
				 && !criteria.isFromSearch()) {
		   strBuffOutfitQuery.append(" and c.current_workflow_status <> '" + WorkflowStatus.CLOSED + "'");
	   }

		if ((StringUtils.isNotBlank(criteria.getDueDateFrom()) && StringUtils
				.isNotBlank(criteria.getDueDateTo()))) {
			strBuffOutfitQuery.append(" and c.due_date BETWEEN to_date('"
					+ criteria.getDueDateFrom()
					+ "','mm/dd/yyyy') AND to_date('" + criteria.getDueDateTo()
					+ "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(criteria.getDueDateFrom())) {
			strBuffOutfitQuery.append(" and c.due_date >= to_date('"
					+ criteria.getDueDateFrom() + "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(criteria.getDueDateTo())) {
			strBuffOutfitQuery.append(" and c.due_date <= to_date('"
					+ criteria.getDueDateTo() + "','mm/dd/yyyy')");
		}

		if ((StringUtils.isNotBlank(criteria.getExpShipDateFrom()) && StringUtils
				.isNotBlank(criteria.getExpShipDateTo()))) {
			strBuffOutfitQuery.append(" and c.expected_ship_date BETWEEN to_date('"
					+ criteria.getExpShipDateFrom()
					+ "','mm/dd/yyyy') AND to_date('"
					+ criteria.getExpShipDateTo() + "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(criteria.getExpShipDateFrom())) {
			strBuffOutfitQuery.append(" and c.expected_ship_date >= to_date('"
					+ criteria.getExpShipDateFrom() + "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(criteria.getExpShipDateTo())) {
			strBuffOutfitQuery.append(" and c.expected_ship_date <= to_date('"
					+ criteria.getExpShipDateTo() + "','mm/dd/yyyy')");
		}

		if (StringUtils.isNotBlank(criteria.getCreateDate())) {
			strBuffOutfitQuery.append(" and trunc(c.created_date) = to_date('"
					+ criteria.getCreateDate() + "','mm/dd/yyyy')");
		}

		if (StringUtils.isNotBlank(criteria.getUpdateDate())) {
			if (criteria.isNotUpdatedSince()) {
				strBuffOutfitQuery.append(" and trunc(c.updated_date) <= to_date('"
						+ criteria.getUpdateDate() + "','mm/dd/yyyy')");
			} else {
				strBuffOutfitQuery.append(" and trunc(c.updated_date) = to_date('"
						+ criteria.getUpdateDate() + "','mm/dd/yyyy')");
			}
		}
		if (criteria.hasRecievedImages()) {
			strBuffOutfitQuery
					.append(
							" and exists (select 1 from car_image ci inner join image i on i.image_id = ci.image_id where ci.car_id = c.car_id and i.image_tracking_status_cd = '")
					.append(ImageTrackingStatus.RECEIVED).append("')");
		}

		if (criteria.hasRequestedImages()) {
			strBuffOutfitQuery
					.append(" and exists (select 1 from car_image ci inner join image i on i.image_id = ci.image_id where ci.car_id = c.car_id ")
					.append(" and (")
					.append(" (i.image_tracking_status_cd = '").append(
							ImageTrackingStatus.REQUESTED).append(
							"' and i.image_source_type_cd = '").append(
							ImageSourceType.VENDOR).append("')").append(" or ")
					.append(" (i.image_tracking_status_cd = '").append(
							ImageTrackingStatus.APPROVED).append(
							"' and i.image_source_type_cd = '").append(
							ImageSourceType.ON_HAND).append("')").append("))");
		}

		return strBuffOutfitQuery.toString();

	}

	private void createOutfitChildCriteria(CarSearchCriteria criteria,
			StringBuffer strBuffOutfitQuery) {
		/*if(StringUtils.isNotBlank(criteria.getVendorStyleNumber())){
			strBuffOutfitQuery.append(" and vs.vendor_style_number = '"+ criteria.getVendorStyleNumber() +"'");
		}else if(StringUtils.isNotBlank(criteria.getParentVendorStyleNumber())){
			strBuffOutfitQuery.append(" and vs.vendor_style_number = '"+ criteria.getParentVendorStyleNumber() +"'");
		}*/
		/*if (StringUtils.isNotBlank(criteria.getVendorNumber())) {
			strBuffOutfitQuery.append(" and vs.vendor_number = '"+ criteria.getVendorNumber() + "'");
		}*/
		if (StringUtils.isNotBlank(criteria.getVendorName())) {
			strBuffOutfitQuery.append(" and upper(v.name) like '%" + StringUtils.replace(StringUtils.upperCase(criteria.getVendorName()), "'", "''") + "%'");
		}
		if (StringUtils.isNotBlank(criteria.getClassNumber()) && !criteria.isCopyCar()) {
			strBuffOutfitQuery.append(" and cls.belk_class_number = "	+ criteria.getClassNumber());
		}
		if (StringUtils.isNotBlank(criteria.getDeptCd())) {
			strBuffOutfitQuery.append(" and d.dept_cd = " + criteria.getDeptCd());
		}
		/*if (StringUtils.isNotBlank(criteria.getProductType())) {
			strBuffOutfitQuery.append(" and upper(pt.name) like '%"	+ StringUtils.replace(StringUtils.upperCase(criteria.getProductType()), "'", "''") + "%'");
		}*/
		if (StringUtils.isNotBlank(criteria.getSourceId())) {
			strBuffOutfitQuery.append(" and c.source_id = '" + criteria.getSourceId()+ "'");
		}
		if (StringUtils.isNotBlank(criteria.getBelkUPC())) {
			if (!criteria.getBelkUPC().startsWith("'")) {
				criteria.setBelkUPC("'" + criteria.getBelkUPC() + "'");
			}			
			strBuffOutfitQuery.append(" and c.car_id in (select distinct car_id from vendor_sku where belk_upc in ("
							+ criteria.getBelkUPC() + "))");
		}
	}
	public List<ClassAttribute> getAllClassificationAttributes(long classId) {
		return getHibernateTemplate().find(
				"from ClassAttribute ca left outer join fetch ca.attribute as att where ca.classification.classId= ?",
				new Object[] { classId });
	}

	public List<DepartmentAttribute> getAllDepartmentAttributes(long deptId) {
		return getHibernateTemplate().find(
				"from DepartmentAttribute da left outer join fetch da.attribute as att where da.department.deptId = ?",
				new Object[] { deptId });
	}

	public List<CarAttribute> getAllCarAttribute(long carId) {
		return getHibernateTemplate().find(
				"from CarAttribute ca where ca.car.carId= ?",
				new Object[] { carId });
	}

	public void remove(Image image) {
		this.getHibernateTemplate().delete(image);
	}

	public void remove(CarSkuAttribute carSkuAttr) {
		this.getHibernateTemplate().delete(carSkuAttr);
	}

	public List<NotificationUserDTO> getCarNotificationList(boolean isVendor) {

		List<NotificationUserDTO> userList = new ArrayList<NotificationUserDTO>();
		StringBuffer query = new StringBuffer();

		if (isVendor) {
			query
					.append("SELECT")
					.append(
							" CU.FIRST_NAME as firstName, CU.LAST_NAME as lastName,CU.EMAIL_ADDR AS emailAddress,")
					.append(
							" COUNT (C.CREATED_DATE) AS createdCount, COUNT(C.UPDATED_DATE) as UpdatedCount")
					.append(" FROM CAR C ")
					.append(
							" INNER JOIN VENDOR_STYLE VS ON VS.VENDOR_STYLE_ID = C.VENDOR_STYLE_ID ")
					.append(
							" INNER JOIN CAR_USER_DEPARTMENT CUD ON CUD.DEPT_ID = C.DEPT_ID ")
					.append(
							" INNER JOIN CAR_USER CU ON CU.CAR_USER_ID = CUD.CAR_USER_ID AND CU.USER_TYPE_CD = 'VENDOR' ")
					.append(
							" INNER JOIN CAR_USER_VENDOR CUV ON CUV.VENDOR_ID = VS.VENDOR_ID AND CUV.CAR_USER_ID = CUD.CAR_USER_ID ")
					.append(" WHERE")
					.append(" C.STATUS_CD = 'ACTIVE' AND")
					.append(" CU.STATUS_CD = 'ACTIVE' AND")
					.append(" C.ASSIGNED_TO_USER_TYPE_CD = 'VENDOR' AND")
					.append(
							" TRUNC(C.LAST_WF_STATUS_UPDATE_DATE) = TRUNC(CURRENT_DATE - 1) AND")
					// added the check so that this query should not fetch the
					// newly generated cars which are directly assigned to
					// Vendor
					.append(" C.LAST_WF_STATUS_UPDATE_DATE > C.CREATED_DATE")
					.append(
							" GROUP BY CU.EMAIL_ADDR,CU.FIRST_NAME, CU.LAST_NAME")

					// added union to get the vendor details for an outfitcar
					// which is assigned to
					.append(" UNION ")

					.append(" SELECT CU.FIRST_NAME     AS firstName, ")
					.append(" CU.LAST_NAME           AS lastName, ")
					.append(" CU.EMAIL_ADDR          AS emailAddress, ")
					.append(" COUNT (C.CREATED_DATE) AS createdCount, ")
					.append(" COUNT(C.UPDATED_DATE)  AS UpdatedCount ")

					.append(" FROM CAR C ")
					.append(
							" INNER JOIN  CAR_OUTFIT_CHILD COC ON  C.CAR_ID = COC.OUTFIT_CAR_ID ")
					.append(
							" INNER JOIN CAR_USER_VENDOR CUV ON CUV.VENDOR_ID  = COC.VENDOR_ID ")
					.append(
							" INNER JOIN CAR_USER CU ON CU.CAR_USER_ID   = CUV.CAR_USER_ID AND CU.USER_TYPE_CD = 'VENDOR' ")
					.append(" WHERE C.STATUS_CD = 'ACTIVE' ")
					.append(" AND CU.STATUS_CD = 'ACTIVE' ")
					.append(" AND COC.STATUS_CD = 'ACTIVE' ")
					.append(" AND C.ASSIGNED_TO_USER_TYPE_CD = 'VENDOR' ")
					.append(
							" AND TRUNC(C.LAST_WF_STATUS_UPDATE_DATE) = TRUNC(CURRENT_DATE - 1) ")
					.append(
							" AND C.LAST_WF_STATUS_UPDATE_DATE > C.CREATED_DATE ")
					.append(
							" GROUP BY CU.EMAIL_ADDR, CU.FIRST_NAME,  CU.LAST_NAME ");

		} else {
			// removed the first part of union query which sends email on car
			// creation
			// This query will fetch the CARS which are updated
			query
					.append(" SELECT ")
					.append(
							" CU.FIRST_NAME AS firstName, CU.LAST_NAME AS lastName,")
					.append(" CU.EMAIL_ADDR AS emailAddress,")
					.append(" 0 AS createdCount,")
					.append(" COUNT (C.UPDATED_DATE) AS updatedCount")
					.append(" FROM ")
					.append("CAR_USER CU,")
					.append("CAR C,")
					.append(" CAR_USER_DEPARTMENT CUD")
					.append(" WHERE")
					.append(" C.DEPT_ID = CUD.DEPT_ID AND")
					.append(" CUD.CAR_USER_ID = CU.CAR_USER_ID AND")
					.append(" CU.USER_TYPE_CD <> 'VENDOR' AND")
					.append(
							" (( CU.USER_TYPE_CD = 'BUYER' AND C.ASSIGNED_TO_USER_TYPE_CD IN ('BUYER','VENDOR')) OR  (CU.USER_TYPE_CD = C.ASSIGNED_TO_USER_TYPE_CD)) AND")
					.append(" C.STATUS_CD = 'ACTIVE' AND")
					.append(" CU.STATUS_CD = 'ACTIVE' AND")
					.append(
							" TRUNC(C.LAST_WF_STATUS_UPDATE_DATE) = TRUNC(CURRENT_DATE - 1) AND")
					.append(
							" C.LAST_WF_STATUS_UPDATE_DATE > C.CREATED_DATE AND C.CURRENT_WORKFLOW_STATUS <> 'INITIATED'")
					// added the condition to avoid initiated cars
					.append(
							" GROUP BY CU.EMAIL_ADDR,CU.FIRST_NAME, CU.LAST_NAME");
		}

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sQuery = (SQLQuery) session
					.createSQLQuery(query.toString())
					.addScalar("firstName")
					.addScalar("lastName")
					.addScalar("emailAddress")
					.addScalar("createdCount")
					.addScalar("updatedCount")
					.setResultTransformer(
							Transformers.aliasToBean(NotificationUserDTO.class));
			userList = sQuery.list();
		} catch (Exception e) {
			log.error("Hibernate Exception caught in getCarNotificationList");
		}

		if (userList == null) {
			userList = new ArrayList<NotificationUserDTO>();
		}
		return userList;
	}

	public List<NotificationUserDTO> getVendorCarEscalationList() {

		List<NotificationUserDTO> userList = new ArrayList<NotificationUserDTO>();
		StringBuffer query = new StringBuffer();
		query
				.append("SELECT")
				.append(
						" CU.FIRST_NAME as firstName,CU.LAST_NAME as lastName,CU.EMAIL_ADDR as emailAddress, COUNT(CAR_ID) AS carCount")
				.append(" FROM CAR C ")
				.append(
						" INNER JOIN VENDOR_STYLE VS ON VS.VENDOR_STYLE_ID = C.VENDOR_STYLE_ID ")
				.append(
						" INNER JOIN CAR_USER_DEPARTMENT CUD ON CUD.DEPT_ID = C.DEPT_ID ")
				.append(
						" INNER JOIN CAR_USER CU ON CU.CAR_USER_ID = CUD.CAR_USER_ID AND CU.USER_TYPE_CD = 'VENDOR' ")
				.append(
						" INNER JOIN CAR_USER_VENDOR CUV ON CUV.VENDOR_ID = VS.VENDOR_ID AND CUV.CAR_USER_ID = CUD.CAR_USER_ID ")
				.append(" WHERE C.STATUS_CD = 'ACTIVE' AND ").append(
						" CU.STATUS_CD = 'ACTIVE' AND ").append(
						" C.ASSIGNED_TO_USER_TYPE_CD = 'VENDOR' AND").append(
						" TRUNC(C.ESCALATION_DATE) = TRUNC(CURRENT_DATE)")
				.append(" GROUP BY CU.EMAIL_ADDR,CU.FIRST_NAME,CU.LAST_NAME");

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sQuery = (SQLQuery) session
					.createSQLQuery(query.toString())
					.addScalar("firstName")
					.addScalar("lastName")
					.addScalar("emailAddress")
					.addScalar("carCount")
					.setResultTransformer(
							Transformers.aliasToBean(NotificationUserDTO.class));
			userList = sQuery.list();
		} catch (Exception e) {
			log
					.error("Hibernate Exception caught in getVendorsWithOpenCarsList");
		}

		if (userList == null) {
			userList = new ArrayList<NotificationUserDTO>();
		}
		return userList;
	}

	public List<NotificationUserDTO> getVendorSampleEscalationList() {

		List<NotificationUserDTO> userList = new ArrayList<NotificationUserDTO>();
		StringBuffer query = new StringBuffer();
		query
				.append(
						"SELECT CU.FIRST_NAME as firstName,CU.LAST_NAME as lastName,CU.EMAIL_ADDR as emailAddress,C.CAR_ID as carNumber, TRUNC(C.DUE_DATE) as dueDate ")
				.append(" FROM ")
				.append(
						"   CAR C INNER JOIN VENDOR_STYLE VS ON VS.VENDOR_STYLE_ID = C.VENDOR_STYLE_ID ")
				.append(
						" INNER JOIN CAR_USER_DEPARTMENT CUD ON CUD.DEPT_ID = C.DEPT_ID ")
				.append(
						" INNER JOIN CAR_USER CU ON CU.CAR_USER_ID = CUD.CAR_USER_ID AND CU.USER_TYPE_CD = '"
								+ UserType.VENDOR + "' ")
				.append(
						" INNER JOIN CAR_USER_VENDOR CUV ON CUV.VENDOR_ID = VS.VENDOR_ID AND CUV.CAR_USER_ID = CU.CAR_USER_ID ")
				.append(" WHERE C.STATUS_CD = 'ACTIVE' AND ")
				.append(" CU.STATUS_CD = 'ACTIVE' AND ")
				.append(
						" C.ASSIGNED_TO_USER_TYPE_CD = '" + UserType.VENDOR
								+ "' AND ")
				.append(
						" C.CURRENT_WORKFLOW_STATUS = '"
								+ WorkflowStatus.WITH_VENDOR + "' AND ")
				.append(" C.DUE_DATE = TRUNC(CURRENT_DATE+3) AND ")
				.append(" EXISTS (SELECT 1 FROM SAMPLE S ")
				.append(
						" INNER JOIN CAR_SAMPLE CS ON CS.SAMPLE_ID = S.SAMPLE_ID ")
				.append(
						" WHERE S.SAMPLE_TRACKING_STATUS_CD = '"
								+ SampleTrackingStatus.REQUESTED + "'  ")
				.append(
						" AND S.SAMPLE_SOURCE_TYPE_CD = '"
								+ SampleSourceType.VENDOR + "' ").append(
						" AND CS.CAR_ID = C.CAR_ID) ");

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sQuery = (SQLQuery) session
					.createSQLQuery(query.toString())
					.addScalar("firstName")
					.addScalar("lastName")
					.addScalar("emailAddress")
					.addScalar("carNumber")
					.addScalar("dueDate")
					.setResultTransformer(
							Transformers.aliasToBean(NotificationUserDTO.class));
			userList = sQuery.list();
		} catch (Exception e) {
			log
					.error("Hibernate Exception caught in getVendorSampleEscalationList: "
							+ e.getMessage());
		}

		if (userList == null) {
			userList = new ArrayList<NotificationUserDTO>();
		}
		return userList;
	}

	public CarAttribute updateCarAttributeValue(CarAttribute carAttr) {
		getHibernateTemplate().saveOrUpdate(carAttr);
		getHibernateTemplate().flush();
		return carAttr;
	}

	public void updateAttributeValueProcessStatuses(long carId) {
		String sqlQuery = "UPDATE CAR_ATTRIBUTE CA SET CA.ATTR_VALUE_PROCESS_STATUS_CD = 'IN_VALUE' WHERE CA.CAR_ID = :CAR_ID AND CA.ATTR_VALUE_PROCESS_STATUS_CD = 'CHECK_REQUIRED' and CA.ATTR_VALUE is not null and exists ( select 1 from ATTRIBUTE_LOOKUP_VALUE ALV where ALV.VALUE = CA.ATTR_VALUE AND ALV.ATTR_ID = CA.ATTR_ID )";
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			Query query = session.createSQLQuery(sqlQuery);
			query.setLong("CAR_ID", carId);
			query.executeUpdate();
		} catch (Exception e) {
			log
					.error("Hibernate Exception caught in updateAttributeValueProcessStatuses()");
		}
	}

	
	public Integer searchCarsForNewDashBoardCount(CarSearchCriteria criteria,
			User user) {
		List<Integer> countList = null;
		Integer count=0;
		String query = getCarQueryString(criteria,true);
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		Statement st=null;
		ResultSet rs=null;
		try {
			Query q = session.createSQLQuery(query)
							.addEntity("c", Car.class)
							.addJoin("vs", "c.vendorStyle")
							.addJoin("v", "vs.vendor")
							.addJoin("d", "c.department")
							.addScalar("due_date",Hibernate.STRING)
							.addScalar("e_date", Hibernate.STRING);
			try{
				st=session.connection().createStatement();
				rs=st.executeQuery(q.getQueryString());
				String co="";
				while(rs.next()){
					co=rs.getString(1);
					count=count+Integer.parseInt(co);
				}
			} catch (SQLException sqe) {
				sqe.printStackTrace();
				log.error("Hibernate Exception" + sqe);
			}finally{
				rs.close();
				st.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Hibernate Exception" + e);
		}
		return count;
	}

	
	
	public List<CarsDTO> searchCarsForNewDashBoard(CarSearchCriteria criteria,
			User user) {
		List<CarsDTO> carList = null;
		String query = getCarQueryString(criteria,false);
		
		
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		String page=criteria.getCurrentPage();
		try {
			Query q = session.createSQLQuery(query)
							.addEntity("c", Car.class)
							.addJoin("vs", "c.vendorStyle")
							.addJoin("v", "vs.vendor")
							.addJoin("d", "c.department")
							.addScalar("due_date",Hibernate.STRING)
							.addScalar("e_date", Hibernate.STRING);
			
		
			 if(page !=null && !page.isEmpty() && !StringUtils.isNotBlank(criteria.getCarId()))
			 {
				   q.setFirstResult(250*(Integer.parseInt(page)-1)).setMaxResults(250);
			  }	
			carList = this.createCarDTOList(q.list(), user);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Hibernate Exception" + e);
		}

		return carList;
	}

	public void updateCars(String query) throws Exception {
		// getHibernateTemplate().saveOrUpdateAll(list);
		Session session = getHibernateTemplate().getSessionFactory()
				.getCurrentSession();
		SQLQuery query2 = session.createSQLQuery(query);
		query2.executeUpdate();
	}

	public List<Car> getCarsByIds(String ids) {
		return getHibernateTemplate().find(
				"from Car WHERE carId IN (" + ids + ")");
	}

	/**
	 * This method creates list of CarsDTO from list of CAR model objects.
	 * 
	 * @param list
	 * @param user
	 * @return
	 */
	private List<CarsDTO> createCarDTOList(List<Object[]> list, User user)
			throws Exception {
		list2 = new ArrayList<CarsDTO>();
		SimpleDateFormat dformat = new SimpleDateFormat("MM/dd/yyyy");
		String DATEFORMAT = "MM/dd/yyyy";
		Date currentDate = getFormattedDate(dformat.format(new Date()));
		for (Object obj : list) {
			Object o = Array.get(obj, 0);
			Object dueDate = Array.get(obj, 1);
			Object expectedDate = Array.get(obj, 2);
			Car car = (Car) o;
			CarsDTO carsDTO = new CarsDTO();
			User userA = car.getCarUserByAssignedToUserId();
			String prodTypeStr = (car.getVendorStyle().getProductType() == null) ? "set"
					: "edit";
			if(car.getIsProductTypeRequired().equalsIgnoreCase(Constants.FLAG_YES)){
				prodTypeStr="set";
			}
			carsDTO.setCarId(car.getCarId());
			carsDTO.setSource(car.getSourceId());
			carsDTO.setDeptNo(car.getDepartment().getDeptCd());
			carsDTO.setVendor(car.getVendorStyle().getVendor().getName());
			carsDTO.setStyle(car.getVendorStyle().getVendorStyleName());
			carsDTO.setRequestType(car.getSourceType().getSourceTypeCd());
			carsDTO.setSourceName(car.getSourceType().getName());
			carsDTO.setStatus(car.getCurrentWorkFlowStatus().getName());
			carsDTO.setAssignedTo(car.getAssignedToUserType().getName());
			if (null != dueDate) {
				carsDTO.setDueDate(getformattedDateString(dueDate));
			}
			carsDTO.setCompletionDate(getformattedDateString(expectedDate));
			// Changing to Date
			if (null != dueDate) {
				carsDTO.setDueDatte(getFormattedDate(carsDTO.getDueDate()));
				//SimpleDateFormat dformat = new SimpleDateFormat("MM/dd/yyyy");
				//Date currentDate = getFormattedDate(dformat.format(new Date()));
				
				//Added for yellow flag for status due date				
				long msDiff = carsDTO.getDueDatte().getTime() - currentDate.getTime();
				long daysDiff = msDiff==0? 0 : msDiff/(24 * 60 * 60 * 1000);
				
				if (carsDTO.getDueDatte().before(currentDate)
						&& !("CLOSED".equalsIgnoreCase(carsDTO.getStatus()))) {
					carsDTO.setDueFlag("T");
				} else {
					carsDTO.setDueFlag("F");
				}
				
				if(daysDiff < 3 && daysDiff >= 0 && !("CLOSED".equalsIgnoreCase(carsDTO.getStatus()))){
					carsDTO.setStatusDueFlag("T");
				}else{
					carsDTO.setStatusDueFlag("F");	
				}				
			}
			carsDTO.setCompletionDatte(getFormattedDate(carsDTO
					.getCompletionDate()));
			// Change Date
			// Code added for VIP to set status as Immediate
			if (carsDTO.getCompletionDatte() != null
					&& car.getImagesFailedInCCPending() != null
					&& car.getImagesFailedInCCPending().trim().length() > 0) {
				// Get current date
				Calendar now = Calendar.getInstance();
				// Get the six weeks after date from current date
				now.add(Calendar.WEEK_OF_YEAR, +6);
				String sixWeeksAfter = (now.get(Calendar.MONTH) + 1) + "/"
						+ now.get(Calendar.DATE) + "/" + now.get(Calendar.YEAR);
				Date sixWeeksAfterDt = DateUtils.parseDate(sixWeeksAfter,DATEFORMAT);
				// Set the flag for displaying in the blue color on dashboard,
				// if CAR workflow status is IMAGE_FAILED_IN_CC and 
				//completion date less or equal to six currentDt+6 weeks 
				if (WorkflowStatus.IMAGE_FAILED_IN_CC.equalsIgnoreCase(car
						.getCurrentWorkFlowStatus().getStatusCd())
						&& sixWeeksAfterDt.after(carsDTO.getCompletionDatte())) {
					//reset other flags
					carsDTO.setDueFlag("F");
					carsDTO.setStatusDueFlag("F");
					//set the flag for displaying blue color
					carsDTO.setStrImmediate("Immediate");
					carsDTO.setStrImmediateFlag("T");
				}
			}
			carsDTO.setLastUpdatedBy(car.getUpdatedBy());
			carsDTO.setStyleTypeCd(car.getVendorStyle().getVendorStyleType()
					.getCode());
			carsDTO.setContentStatus(car.getContentStatus().getCode());
			carsDTO.setUserTypeCd(user.getUserType().getUserTypeCd());
			// carsDTO.setLockUnlock((userA == null) ? "Lock" : "Unlock");
			if (car.getArchive().equalsIgnoreCase("Y")) {
				carsDTO.setArchived("Y");
			} else {
				carsDTO.setArchived("");
			}
			//long carId=car.getCarId();
			//Car carTemp = this.getCarManager().getCarFromId(new Long(carId));
			carsDTO.setReadyToSendToCMPFlag("true");
			if (car.getSourceType().getSourceTypeCd().equals(SourceType.OUTFIT)){
				boolean readyToSendToCMPFlag = true;
				Set<CarOutfitChild> outfitChildCars = car.getCarOutfitChild();
				for(CarOutfitChild carOutfitChild: outfitChildCars)	{
					Car childCar = carOutfitChild.getChildCar();
					if ( !(ContentStatus.SENT_TO_CMP.equals(childCar.getContentStatus().getCode())
						 || ContentStatus.PUBLISHED.equals(childCar.getContentStatus().getCode())
			   	         || ContentStatus.RESEND_TO_CMP.equals(childCar.getContentStatus().getCode()) 
			   	         || ContentStatus.RESEND_DATA_TO_CMP.equals(childCar.getContentStatus().getCode())
			   	         || ContentStatus.DISABLE_IN_CMP.equals(childCar.getContentStatus().getCode())
			   	         || ContentStatus.ENABLE_IN_CMP.equals(childCar.getContentStatus().getCode()) ) ){
						readyToSendToCMPFlag = false;
					      break;
					}
			    }
				if(!readyToSendToCMPFlag){
					carsDTO.setReadyToSendToCMPFlag("false");
				}
			}
			
			if(UserType.BUYER.equals(car.getAssignedToUserType().getUserTypeCd()) && "Y".equalsIgnoreCase(car.getBuyerApprovalPending()) && ("NONE").equalsIgnoreCase(car.getImageMCPendingByUser())){
				 carsDTO.setBuyerApprovalFlag("green");
			}
			
			// carsDTO.setArchived(car.getArchive());
			if ((null == userA)
					|| (car.getLock().equalsIgnoreCase("N"))
					|| (userA.getUsername()
							.equalsIgnoreCase(user.getUsername()))) {
				//Below code added on 12/20/2012 as a part of VIP
				if(!("NONE").equalsIgnoreCase(car.getImageMCPendingByUser()) &&
									!user.getUserType().getUserTypeCd().equalsIgnoreCase(car.getImageMCPendingByUser())){
					carsDTO.setSetEdit("gray");
				}else{
					carsDTO.setSetEdit(prodTypeStr);
				}
			} else {
				carsDTO.setSetEdit("gray");
			}
			list2.add(carsDTO);
		}
		return list2;
	}

	/**
	 * Returns back the date in MM/dd/yyyy format
	 * 
	 * @param dateToBeFormatted
	 *            Object having value as a date in format dd/MM/yyyy
	 * @return String Sting having value of date in MM/dd/yyyy format
	 */
	private String getformattedDateString(Object dateToBeFormatted) {
		String date = dateToBeFormatted.toString();
		// Current date format is DD/MM/yyy so splitting it.
		StringBuilder newFormattedDate = new StringBuilder();
		// Day :"+date.substring(0, 2)
		// Month :"+date.substring(3, 5)
		// Year :"+date.substring(6,date.length())
		newFormattedDate.append(date.substring(3, 5)).append("/");
		newFormattedDate.append(date.substring(0, 2)).append("/").append(
				date.substring(6, date.length()));
		return newFormattedDate.toString();
	}

	private Date getFormattedDate(String dateToBeFormatted) {
		// String date = datte.toString();
		DateFormat formatter;
		formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date dateToBeDisplayed = null;
		try {
			dateToBeDisplayed = (Date) formatter.parse(dateToBeFormatted);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// Calendar calc = Calendar.getInstance();
		// int year = Integer.parseInt(date.substring(6,date.length()));
		// int month = Integer.parseInt(date.substring(3, 5));
		// int dt = Integer.parseInt(date.substring(0, 2));
		// calc.set(year, month, dt);
		// log.debug("Cal Object :"+ calc.toString());
		// Date dt1 = calc.getTime();

		return dateToBeDisplayed;

	}

	public List<DetailNotificationUserDTO> getGeneratedCarList(boolean isVendor) {
		StringBuffer query = new StringBuffer();
		query
				.append(
						"select {c.*}, {d.*}, {vs.*}, cu.car_user_id as userId, cu.user_type_cd as userType, cu.first_name as firstName, cu.last_name as lastName, cu.email_addr as emailAddress ")
				.append(" from car c ")
				.append(" inner join department d on d.dept_id = c.dept_id ")
				.append(
						" inner join vendor_style vs on vs.vendor_style_id = c.vendor_style_id ")
				.append(
						" inner join car_user_department cud on c.dept_id = cud.dept_id ")
				.append(
						" inner join car_user cu on cu.car_user_id = cud.car_user_id");

		if (isVendor) {
			query
					.append(
							" inner join car_user_vendor cuv on cuv.car_user_id = cud.car_user_id and cuv.vendor_id = vs.vendor_id")
					.append(
							" where c.assigned_to_user_type_cd in ('"
									+ UserType.VENDOR + "')").append(
							" and cu.user_type_cd ='" + UserType.VENDOR + "'");
		} else {
			query.append(
					" where c.assigned_to_user_type_cd in ('" + UserType.BUYER
							+ "', '" + UserType.VENDOR + "')").append(
					" and cu.user_type_cd ='" + UserType.BUYER + "'");
		}

		query.append(" and trunc (c.created_date) = trunc(current_date)")
				.append(" and c.status_cd = 'ACTIVE'").append(
						" and cu.status_cd = 'ACTIVE'").append(
						" order by cu.car_user_id, c.dept_id");

		List resultSetList = null;
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery sQuery = (SQLQuery) session.createSQLQuery(
					query.toString()).addEntity("c", Car.class).addJoin("d",
					"c.department").addJoin("vs", "c.vendorStyle").addScalar(
					"userId").addScalar("userType").addScalar("firstName")
					.addScalar("lastName").addScalar("emailAddress");
			resultSetList = sQuery.list();
		} catch (Exception e) {
			log.error("Hibernate Exception caught in getGeneratedCarList: "
					+ e.getMessage());
		}

		if (resultSetList == null) {
			resultSetList = new ArrayList();
		}
		return convertEmailDTOList(resultSetList);
	}

	private List<DetailNotificationUserDTO> convertEmailDTOList(List emailList) {
		List<DetailNotificationUserDTO> userList = new ArrayList<DetailNotificationUserDTO>();
		Map<Long, List<Car>> userCarMap = new HashMap<Long, List<Car>>();

		for (Object obj : emailList) {
			Car c = (Car) Array.get(obj, 0);
			java.math.BigDecimal bigUserId = (java.math.BigDecimal) Array.get(
					obj, 1);
			Long userId = bigUserId == null ? null : bigUserId.longValue();
			String userType = (String) Array.get(obj, 2);
			String firstName = (String) Array.get(obj, 3);
			String lastName = (String) Array.get(obj, 4);
			String emailAddress = (String) Array.get(obj, 5);
			List<Car> carList = null;
			if (userId != null && c != null) {
				if ((carList = userCarMap.get(userId)) == null) {
					DetailNotificationUserDTO userDetail = new DetailNotificationUserDTO();
					userDetail.setUserId(userId);
					userDetail.setEmailAddress(emailAddress);
					userDetail.setUserType(userType);
					userDetail.setFirstName(firstName);
					userDetail.setLastName(lastName);
					userList.add(userDetail);
					carList = new ArrayList<Car>();
				}
				carList.add(c);
				userCarMap.put(userId, carList);
			}
		}

		for (DetailNotificationUserDTO userDto : userList) {
			Long id = userDto.getUserId();
			userDto.setCars(userCarMap.get(id));
		}

		return userList;
	}

	@SuppressWarnings("unchecked")
	public void saveVendorDepartment(CarUserVendorDepartment cvd) {
		if (log.isInfoEnabled()) {
			log
					.info("Trying to save vendor-department mapping  for vendor ID: "
							+ cvd.getVendor().getVendorId()
							+ " and dept ID "
							+ cvd.getDept().getDeptId());
		}
		List<CarUserVendorDepartment> carUserVendorList = getHibernateTemplate()
				.find(
						"from CarUserVendorDepartment where statusCd = '"
								+ Status.ACTIVE + "' and vendor.vendorId ="
								+ (cvd.getVendor().getVendorId())
								+ " and dept.deptId="
								+ cvd.getDept().getDeptId());
		if (carUserVendorList != null && carUserVendorList.size() == 0) {
			getHibernateTemplate().save(cvd);
			getHibernateTemplate().flush();
		} else {
			log.info("Associaiton Already Exists for vendor id:"
					+ cvd.getVendor().getVendorId() + " and dept id:"
					+ cvd.getDept().getDeptId());
		}
	}

	public void deleteVendorDepartment(Long carUserVendorDepartmentId) {

		CarUserVendorDepartment cuvd = (CarUserVendorDepartment) getHibernateTemplate()
				.get(CarUserVendorDepartment.class, carUserVendorDepartmentId);
		cuvd.setStatusCd(Status.DELETED);
		getHibernateTemplate().saveOrUpdate(cuvd);
		getHibernateTemplate().flush();

	}

	@SuppressWarnings("unchecked")
	public List<CarUserVendorDepartment> getVendorDepartments(Long vendorId) {
		return getHibernateTemplate().find(
				"from CarUserVendorDepartment where statusCd = '"
						+ Status.ACTIVE + "' and vendor_number=" + vendorId);
	}

	@SuppressWarnings("unchecked")
	public List<CarUserVendorDepartment> getVendorsByDeptId(Long deptId) {
		/*return getHibernateTemplate().find(
				"from CarUserVendorDepartment WHERE dept.deptId IN (" + deptId
						+ ")");*/
		//Fix for "Send CARS Directly To Vendor" functionality sending cars to unassigned vendors. 
		return getHibernateTemplate().find("from CarUserVendorDepartment WHERE dept.deptId IN ("+ deptId + ") and statusCd = '"+Status.ACTIVE+"'");  
	}

	public void unlockCar(long carId) throws Exception {
		Car car = (Car) getHibernateTemplate().get(Car.class, carId);
		car.setLock("N");
		getHibernateTemplate().update(car);
		getHibernateTemplate().flush();
	}

	public void unlockCar(String userEmail) throws Exception {
        try{
			   SessionFactory sf = getHibernateTemplate().getSessionFactory();
			   Session session = sf.getCurrentSession();
			   String query = "UPDATE CAR C SET C.LOCKED = 'N' WHERE C.LOCKED = 'Y' AND C.UPDATED_BY = :USEREMAIL";
			   int updateRows =  session.createSQLQuery(query)
			   		   					.setString("USEREMAIL", userEmail)
			   		   					.executeUpdate();
			   log.info("Unlocked "+ updateRows + " CARS for user " + userEmail);
        }catch(Exception e){
        	log.error("Got exception while unlocking the CARS for user : " + e.getMessage());
        }
   
	}

	public CarAttribute getCarAttributeByBMName(Car c, String strBMName){
		Object[] arr= {c, strBMName};
		List list = getHibernateTemplate().find("from CarAttribute ca where ca.car= ? and ca.attribute.blueMartiniAttribute=? and ca.statusCd='ACTIVE'", arr);
		CarAttribute carAttr = null;
		if(list!=null && list.size()>0){
			carAttr  = (CarAttribute) list.get(0);
		}
		return carAttr;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.belk.car.app.dao.CarDao#getAttributeByName(java.lang.String)
	 * Method to get the Attribute by attribute name
	 */
	public Attribute getAttributeByName(String attrName){
		List list = getHibernateTemplate().find("from Attribute where name='" + attrName + "' and statusCd='ACTIVE'");
		Attribute attr = null;
		if(list!=null && list.size()>0){
			attr  = (Attribute) list.get(0);
		}
		return attr;
	}


	@Override
	public List<String> getCollectionSkusByVS(VendorStyle vs) {
		String prodCode = String.valueOf(vs.getVendorNumber().concat(vs.getVendorStyleNumber()));
		List list = getHibernateTemplate().find("from CollectionSkus cs where cs.prodCode= ?", prodCode);
		List<String> skuList = new ArrayList<String>();
		CollectionSkus collectionSkus = null;
		for (Object object : list) {
			collectionSkus = (CollectionSkus)object;
			skuList.add(String.valueOf(collectionSkus.getSkuCode()));
		}
		return skuList;
	}

	@Override
	public void savePOUnitdetails(List<PoUnitDetail> poUnitDetailList) {
		this.getHibernateTemplate().saveOrUpdateAll(poUnitDetailList);
		getHibernateTemplate().flush();
	}
	
	// added on 4/6/2013
	@Override
	public List<String> getDBPromotionCollectionSkusByVS(VendorStyle vs) {
		String prodCode = String.valueOf(vs.getVendorNumber().concat(vs.getVendorStyleNumber()));
		List list = getHibernateTemplate().find("from DBPromotionCollectionSkus cs where cs.prodCode= ?", prodCode);
		List<String> promoSkuList = new ArrayList<String>();
		DBPromotionCollectionSkus dbPromotioncollectionSkus = null;
		for (Object object : list) {
			dbPromotioncollectionSkus = (DBPromotionCollectionSkus )object;
		promoSkuList.add(String.valueOf(dbPromotioncollectionSkus.getSkuCode()));
		}
		return promoSkuList;
		}
		
	
	private String buildQueryForPYG(CarSearchCriteria criteria,boolean fCount){
		StringBuffer strBuffPromoQuery = new StringBuffer();
		//strBuffPromoQuery.append("	select {c.*},{vs.*}, {d.*}, {v.*},to_char(c.due_date,'DD/MM/YYYY') due_date,to_char(c.expected_ship_date,'DD/MM/YYYY') e_date ");  
			
		if(fCount){
			strBuffPromoQuery.append("	select Count(c.car_id) ");	
		}else{
			strBuffPromoQuery.append("	select DISTINCT {c.*},{vs.*}, {d.*}, {v.*},to_char(c.due_date,'DD/MM/YYYY') due_date,to_char(c.expected_ship_date,'DD/MM/YYYY') e_date ,c.expected_ship_date eShipDate,c.DUE_DATE dueDate,v.Name as vendorName,vs.VENDOR_STYLE_NAME as vendorStyleName,c.ASSIGNED_TO_USER_TYPE_CD assignedUserCd,c.CURRENT_WORKFLOW_STATUS statuscd,c.car_id  car,c.SOURCE_ID source_cd ,c.SOURCE_TYPE_CD sourcecd,d.DEPT_CD depCd");
			if(StringUtils.isNotBlank(criteria.getReportName()) && criteria.getReportName().equals("Car Listing Report")) {
		    	  strBuffPromoQuery.append(",(select decode(dbms_lob.SubStr(wm_concat(DISTINCT color_name)),'NO COLOR','','No Color','','no color','',null,'',dbms_lob.SubStr(wm_concat(DISTINCT color_name))) from vendor_sku where car_id = c.car_id) as colorName ");
		      }
			}
					      if(criteria.isIncludePromoType()){
					    	  strBuffPromoQuery.append(" from Promo_Type pt,car c  inner join department d on d.dept_id = c.dept_id ") ; 
					      }else{
					      strBuffPromoQuery.append(" from car c  inner join department d on d.dept_id = c.dept_id") ; 
					      }
					      
					      strBuffPromoQuery.append(" inner join vendor_style vs on vs.vendor_style_id = c.vendor_style_id")  
					      .append(" inner join vendor v on v.vendor_id = vs.vendor_id")  
					      .append(" inner join classification cls on cls.class_id = vs.class_id")
					       
					      .append(" where 0=0 ");
					      if(criteria.isIncludePromoType()){
								if(criteria.getPromotionType().equals(Constants.PROMOTYPE_GWP)){
								strBuffPromoQuery.append(" and c.car_id=pt.car_id and pt.is_gwp='Yes' ");
								}
								if(criteria.getPromotionType().equals(Constants.PROMOTYPE_PYG)){
								strBuffPromoQuery.append(" and c.car_id=pt.car_id and pt.is_pyg='Yes' ");
								}
							}
					      
					      // If a group is inactivated, but there are still styles in that group
                          // that are active, then only the group should be hidden.
                          // This change should not affect the regular products.
                          if (StringUtils.isNotBlank(criteria.getVendorStyleStatusCd())) {
                              strBuffPromoQuery.append(" and ((vs.vendor_style_type_cd !='PRODUCT' and vs.status_cd = 'ACTIVE'");
                              strBuffPromoQuery.append(")");
                              strBuffPromoQuery.append(" or (vs.vendor_style_type_cd ='PRODUCT'");
                              strBuffPromoQuery.append(")");
                              strBuffPromoQuery.append(")");
                          }
					      
	//Start : Code added for Performance Tuning
			if(criteria.getIsFromDashBoardAndSearchCar() && StringUtils.isNotBlank(criteria.getVendorStyleNumber()) && StringUtils.isNotBlank(criteria.getParentVendorStyleNumber())){

				strBuffPromoQuery.append(" and c.car_id in 	");
				//				Second part of query  starts here :second part of query gives promo PYG cars based on search criteria
				//Eg: searchig with outfit vendor style number will give you outfit car
				strBuffPromoQuery.append("(( select unique(c.car_id) as car_id ")
								  .append(" from (  select cpc.promo_car_id as car_id ")
								  .append(" from  car_promotion_child cpc " );
				if(criteria.getUser().isVendor()){
					strBuffPromoQuery.append(" inner join car_user_vendor cuv on cuv.vendor_id = cpc.vendor_id ");
					strBuffPromoQuery.append(" inner join car c ON c.car_id = cpc.CHILD_CAR_ID ");	
				}else{
					strBuffPromoQuery.append(" inner join car c ON c.car_id = cpc.CHILD_CAR_ID")
					  				  .append(" inner join department d on d.dept_id = c.dept_id   ")
					  				  .append(" inner join car_user_department cud on cud.dept_id = c.dept_id ");
				}
				strBuffPromoQuery.append(" inner join vendor_style vs on vs.vendor_style_id = c.vendor_style_id  ");
				strBuffPromoQuery.append("where 0=0 ");
				if(Status.ACTIVE.equalsIgnoreCase(criteria.getStatusCd())){
					strBuffPromoQuery.append(" and cpc.status_cd ='ACTIVE'");
				}
				if(criteria.getUser().isVendor()){
					strBuffPromoQuery.append(" and cuv.car_user_id = "+ criteria.getUser().getId());
				}else{
					strBuffPromoQuery.append(" and cud.car_user_id = "+ criteria.getUser().getId());
				}
				if(StringUtils.isNotBlank(criteria.getVendorStyleNumber())){
					strBuffPromoQuery.append(" and vs.vendor_style_number = '"+ criteria.getVendorStyleNumber().toUpperCase() +"'");
				}
				strBuffPromoQuery.append("  )child_cars ")
								  .append(" inner join car c on c.car_id = child_cars.car_id ")
								  .append(" inner join vendor_style vs on vs.vendor_style_id = c.vendor_style_id  ")
								  .append(" inner join classification cls on cls.class_id = vs.class_id  ");
				if (StringUtils.isNotBlank(criteria.getProductType())) {
					strBuffPromoQuery.append(" inner join product_type pt on pt.product_type_id = vs.product_type_id ");
				  }
				strBuffPromoQuery.append(" where 0=0 ");
				if (StringUtils.isNotBlank(criteria.getProductType())) {
					strBuffPromoQuery.append(" and upper(pt.name) like '%"	+ StringUtils.replace(StringUtils.upperCase(criteria.getProductType()), "'", "''") + "%'");
				}
				
				if(StringUtils.isNotBlank(criteria.getParentVendorStyleNumber())){
					strBuffPromoQuery.append(" and vs.vendor_style_number = '"+ criteria.getParentVendorStyleNumber().toUpperCase() +"'");
				}
				if (StringUtils.isNotBlank(criteria.getVendorNumber())) {
					strBuffPromoQuery.append(" and vs.vendor_number = '"+ criteria.getVendorNumber() + "'");
				}
				//createPromotionChildCriteria(criteria, strBuffPromoQuery);
				strBuffPromoQuery.append(" ))");
				//second part of query ends here
			}else if(criteria.getIsFromDashBoardAndSearchCar() && StringUtils.isNotBlank(criteria.getVendorStyleNumber())){
				strBuffPromoQuery.append(" and c.car_id in 	");
				//this is start of : first part of query which will give outfit cars based on child cars search
				// Eg: Searching with outfit child cars vendor style number will give you the outfit car
							      strBuffPromoQuery.append("(( select unique(cpc.promo_car_id) as car_id")
								  .append(" from  car_promotion_child cpc inner join car c ON c.car_id = cpc.promo_car_id ")
								  .append(" inner join department d on d.dept_id = c.dept_id  ")
								  .append(" inner join vendor_style vs on vs.vendor_style_id = c.vendor_style_id ")
								  .append(" inner join classification cls on cls.class_id = vs.class_id ")
								  .append(" inner join car_user_department cud on cud.dept_id = c.dept_id ");
				if (StringUtils.isNotBlank(criteria.getProductType())) {
					strBuffPromoQuery.append(" inner join product_type pt on pt.product_type_id = vs.product_type_id ");
				  }
				
				//for outfit cars vendor will be able to see outfits which are specially assigned to them by referring to 
				//vendor_id column of CAR_PROMOTION_CHILD table - so taking join on cpc.vendor_id
				if(criteria.getUser().isVendor()){
					strBuffPromoQuery.append(" inner join vendor v on v.vendor_id = cpc.vendor_id ")
									  .append(" inner join car_user_vendor cuv on cuv.vendor_id = v.vendor_id ");
				}
				strBuffPromoQuery.append(" where 0 = 0 " );
				if(Status.ACTIVE.equalsIgnoreCase(criteria.getStatusCd())){
					strBuffPromoQuery.append("and cpc.status_cd ='ACTIVE'");
				}
				//Vendor will be able to see only cars of his department and assigned to his vendor number 
				if(criteria.getUser().isVendor()){
					strBuffPromoQuery.append(" and cuv.car_user_id = "+ criteria.getUser().getId()); 
			    }else{
			    	strBuffPromoQuery.append(" and cud.car_user_id = "+ criteria.getUser().getId());  
			    }
				if (StringUtils.isNotBlank(criteria.getProductType())) {
					strBuffPromoQuery.append(" and upper(pt.name) like '%"	+ StringUtils.replace(StringUtils.upperCase(criteria.getProductType()), "'", "''") + "%'");
				}
				
				if(StringUtils.isNotBlank(criteria.getVendorStyleNumber())){
					strBuffPromoQuery.append(" and vs.vendor_style_number = '"+ criteria.getVendorStyleNumber().toUpperCase() +"'");
				}
				if (StringUtils.isNotBlank(criteria.getVendorNumber())) {
					strBuffPromoQuery.append(" and vs.vendor_number = '"+ criteria.getVendorNumber() + "'");
				}
				//createPromotionChildCriteria(criteria, strBuffPromoQuery);
				// this is end of first of query
				strBuffPromoQuery.append(" ))");
			}else if(criteria.getIsFromDashBoardAndSearchCar() && StringUtils.isNotBlank(criteria.getParentVendorStyleNumber())){


				strBuffPromoQuery.append(" and c.car_id in 	");
				//				Second part of query  starts here :second part of query gives promo PYG cars based on search criteria
				//Eg: searchig with outfit vendor style number will give you outfit car
				strBuffPromoQuery.append("(( select unique(c.car_id) as car_id ")
								  .append(" from (  select cpc.promo_car_id as car_id ")
								  .append(" from  car_promotion_child cpc " );
				if(criteria.getUser().isVendor()){
					strBuffPromoQuery.append(" inner join car_user_vendor cuv on cuv.vendor_id = cpc.vendor_id ");					
				}else{
					strBuffPromoQuery.append(" inner join car c ON c.car_id = cpc.CHILD_CAR_ID")
					  				  .append(" inner join department d on d.dept_id = c.dept_id   ")
					  				  .append(" inner join car_user_department cud on cud.dept_id = c.dept_id ");
				}
				strBuffPromoQuery.append("where 0=0 ");
				if(Status.ACTIVE.equalsIgnoreCase(criteria.getStatusCd())){
					strBuffPromoQuery.append(" and cpc.status_cd ='ACTIVE'");
				}
				if(criteria.getUser().isVendor()){
					strBuffPromoQuery.append(" and cuv.car_user_id = "+ criteria.getUser().getId());
				}else{
					strBuffPromoQuery.append(" and cud.car_user_id = "+ criteria.getUser().getId());
				}
				strBuffPromoQuery.append("  )child_cars ")
								  .append(" inner join car c on c.car_id = child_cars.car_id ")
								  .append(" inner join vendor_style vs on vs.vendor_style_id = c.vendor_style_id  ")
								  .append(" inner join classification cls on cls.class_id = vs.class_id  ");
				if (StringUtils.isNotBlank(criteria.getProductType())) {
					strBuffPromoQuery.append(" inner join product_type pt on pt.product_type_id = vs.product_type_id ");
				  }
				strBuffPromoQuery.append(" where 0=0 ");
				if (StringUtils.isNotBlank(criteria.getProductType())) {
					strBuffPromoQuery.append(" and upper(pt.name) like '%"	+ StringUtils.replace(StringUtils.upperCase(criteria.getProductType()), "'", "''") + "%'");
				}
				if(StringUtils.isNotBlank(criteria.getParentVendorStyleNumber())){
					strBuffPromoQuery.append(" and vs.vendor_style_number = '"+ criteria.getParentVendorStyleNumber().toUpperCase() +"'");
				}
				if (StringUtils.isNotBlank(criteria.getVendorNumber())) {
					strBuffPromoQuery.append(" and vs.vendor_number = '"+ criteria.getVendorNumber() + "'");
				}
				//createPromotionChildCriteria(criteria, strBuffPromoQuery);
				strBuffPromoQuery.append(" ))");
				//second part of query ends here
			
			}else{
     
					      strBuffPromoQuery.append(" and c.car_id in 	");
		
		//this is start of : first part of query which will give outfit cars based on child cars search
		// Eg: Searching with outfit child cars vendor style number will give you the outfit car
					      strBuffPromoQuery.append("(( select unique(cpc.promo_car_id) as car_id")
						  .append(" from  car_promotion_child cpc inner join car c ON c.car_id = cpc.CHILD_CAR_ID ")
						  .append(" inner join department d on d.dept_id = c.dept_id  ")
						  .append(" inner join vendor_style vs on vs.vendor_style_id = c.vendor_style_id ")
						  .append(" inner join classification cls on cls.class_id = vs.class_id ")
						  .append(" inner join car_user_department cud on cud.dept_id = c.dept_id ");
		if (StringUtils.isNotBlank(criteria.getProductType())) {
			strBuffPromoQuery.append(" inner join product_type pt on pt.product_type_id = vs.product_type_id ");
		  }
		
		//for outfit cars vendor will be able to see outfits which are specially assigned to them by referring to 
		//vendor_id column of CAR_PROMOTION_CHILD table - so taking join on cpc.vendor_id
		if(criteria.getUser().isVendor()){
			strBuffPromoQuery.append(" inner join vendor v on v.vendor_id = cpc.vendor_id ")
							  .append(" inner join car_user_vendor cuv on cuv.vendor_id = v.vendor_id ");
		}
		strBuffPromoQuery.append(" where 0 = 0 " );
		if(Status.ACTIVE.equalsIgnoreCase(criteria.getStatusCd())){
			strBuffPromoQuery.append("and cpc.status_cd ='ACTIVE'");
		}
		//Vendor will be able to see only cars of his department and assigned to his vendor number 
		if(criteria.getUser().isVendor()){
			strBuffPromoQuery.append(" and cuv.car_user_id = "+ criteria.getUser().getId()); 
	    }else{
	    	strBuffPromoQuery.append(" and cud.car_user_id = "+ criteria.getUser().getId());  
	    }
		if (StringUtils.isNotBlank(criteria.getProductType())) {
			strBuffPromoQuery.append(" and upper(pt.name) like '%"	+ StringUtils.replace(StringUtils.upperCase(criteria.getProductType()), "'", "''") + "%'");
		}		  
		if(StringUtils.isNotBlank(criteria.getVendorStyleNumber())){
			strBuffPromoQuery.append(" and vs.vendor_style_number = '"+ criteria.getVendorStyleNumber().toUpperCase() +"'");
		}
		if (StringUtils.isNotBlank(criteria.getVendorNumber())) {
			strBuffPromoQuery.append(" and vs.vendor_number = '"+ criteria.getVendorNumber() + "'");
		}
		//createPromotionChildCriteria(criteria, strBuffPromoQuery);
		// this is end of first of query
		
		strBuffPromoQuery.append(")  union all ( ");
		
		//	Second part of query  starts here :second part of query gives promo PYG cars based on search criteria
		//Eg: searchig with outfit vendor style number will give you outfit car
		strBuffPromoQuery.append(" select unique(c.car_id) as car_id ")
						  .append(" from (  select cpc.promo_car_id as car_id ")
						  .append(" from  car_promotion_child cpc " );
		if(criteria.getUser().isVendor()){
			strBuffPromoQuery.append(" inner join car_user_vendor cuv on cuv.vendor_id = cpc.vendor_id ");					
		}else{
			strBuffPromoQuery.append(" inner join car c ON c.car_id = cpc.CHILD_CAR_ID")
			  				  .append(" inner join department d on d.dept_id = c.dept_id   ")
			  				  .append(" inner join car_user_department cud on cud.dept_id = c.dept_id ");
		}
		strBuffPromoQuery.append("where 0=0 ");
		if(Status.ACTIVE.equalsIgnoreCase(criteria.getStatusCd())){
			strBuffPromoQuery.append(" and cpc.status_cd ='ACTIVE'");
		}
		if(criteria.getUser().isVendor()){
			strBuffPromoQuery.append(" and cuv.car_user_id = "+ criteria.getUser().getId());
		}else{
			strBuffPromoQuery.append(" and cud.car_user_id = "+ criteria.getUser().getId());
		}
		strBuffPromoQuery.append("  )child_cars ")
						  .append(" inner join car c on c.car_id = child_cars.car_id ")
						  .append(" inner join vendor_style vs on vs.vendor_style_id = c.vendor_style_id  ")
						  .append(" inner join classification cls on cls.class_id = vs.class_id  ");
		if (StringUtils.isNotBlank(criteria.getProductType())) {
			strBuffPromoQuery.append(" inner join product_type pt on pt.product_type_id = vs.product_type_id ");
		  }
		strBuffPromoQuery.append(" where 0=0 ");
		if (StringUtils.isNotBlank(criteria.getProductType())) {
			strBuffPromoQuery.append(" and upper(pt.name) like '%"	+ StringUtils.replace(StringUtils.upperCase(criteria.getProductType()), "'", "''") + "%'");
		}
		if(StringUtils.isNotBlank(criteria.getVendorStyleNumber())){
			strBuffPromoQuery.append(" and vs.vendor_style_number = '"+ criteria.getVendorStyleNumber().toUpperCase() +"'");
		}
		if (StringUtils.isNotBlank(criteria.getVendorNumber())) {
			strBuffPromoQuery.append(" and vs.vendor_number = '"+ criteria.getVendorNumber() + "'");
		}
		//createPromotionChildCriteria(criteria, strBuffPromoQuery);
		strBuffPromoQuery.append(" ))");
		//second part of query ends here
	}
//End : Code added for Performance Tuning
		
		if (StringUtils.isNotBlank(criteria.getCarId())) {
			strBuffPromoQuery.append(" and c.car_id = " + criteria.getCarId());
		}
		if (criteria.getUser() != null && !criteria.isIgnoreUser()) {
			if (criteria.getUser().isContentManager()
					|| criteria.getUser().isContentWriter()) {
				// Content Managers can see only published content
				if (StringUtils.isBlank(criteria.getContentStatus())
						&& !criteria.isFromSearch()) {
					criteria.setContentStatus(ContentStatus.PUBLISHED);
				}
			} else if (criteria.getUser().isSampleCoordinator()
					|| criteria.getUser().isVendor()) {
				if (StringUtils.isBlank(criteria.getCurrentUserType())) {
					criteria.setCurrentUserType(criteria.getUser()
							.getUserType().getUserTypeCd());
				}
			} else if (criteria.getUser().isArtDirector()) {
				if (StringUtils.isBlank(criteria.getCurrentUserType())) {
					if (!criteria.isFromSearch()) {
						criteria.setCurrentUserType(criteria.getUser()
								.getUserType().getUserTypeCd());
					}
				}
			} else if (criteria.getUser().isBuyer()) {
				if (!criteria.isFromSearch()) {
					strBuffPromoQuery.append(" and (").append("(c.assigned_to_user_type_cd = '")
										.append(UserType.BUYER).append("')").append(" OR ").append("(c.assigned_to_user_type_cd = '")
										.append(UserType.VENDOR).append("')").append(")");
				}
			}
		}

		if (StringUtils.isNotBlank(criteria.getStatusCd())) {
			strBuffPromoQuery.append(" and c.status_cd = '" + criteria.getStatusCd()+ "'");
		}
		if (null != criteria.getArchive() && criteria.getArchive()) {
			strBuffPromoQuery.append(" and c.archived='Y'");
		} else if (null != criteria.getArchive() && !criteria.getArchive()) {
			strBuffPromoQuery.append(" and c.archived='N'");
		}
		createPromotionChildCriteria(criteria, strBuffPromoQuery);
		if (StringUtils.isNotBlank(criteria.getContentStatus())) {
			strBuffPromoQuery.append(" and c.content_status_cd = '"+ criteria.getContentStatus() + "'");
		}
		
		if (StringUtils.isNotBlank(criteria.getWorkflowStatus())) {
			if ("LATE".equals(criteria.getWorkflowStatus())) {
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DATE, -1);
				Date toDate = c.getTime();
				strBuffPromoQuery.append(" and c.due_date <= to_date('"+ DateUtils.formatDate(toDate) + "','mm/dd/yyyy')");
				strBuffPromoQuery.append(" and c.current_workflow_status <> '" + WorkflowStatus.CLOSED + "'");
				if (criteria.getUser().getUserTypeCd().equalsIgnoreCase(UserType.BUYER)) {
					strBuffPromoQuery.append(" and c.assigned_to_user_type_cd in ('"	+ UserType.BUYER + "','" + UserType.VENDOR + "')");
				} else if (criteria.getUser().getUserTypeCd().equalsIgnoreCase(UserType.VENDOR)) {
					strBuffPromoQuery.append(" and c.assigned_to_user_type_cd = '"+ UserType.VENDOR + "'");
				}
			}else if(criteria.getWorkflowStatus().equalsIgnoreCase("IMAGE_FAILED_IN_CC")){

				strBuffPromoQuery.append(" and c.current_workflow_status IN ('" + WorkflowStatus.IMAGE_FAILED_IN_CC +"','" + WorkflowStatus.IMAGE_FAILED_IN_MC +"')");
            } else {
            	strBuffPromoQuery.append(" and c.current_workflow_status = '"+ criteria.getWorkflowStatus() + "'");
			}
		}

		if (StringUtils.isNotBlank(criteria.getCurrentUserType())) {
			strBuffPromoQuery.append(" and c.assigned_to_user_type_cd = '").append(
					criteria.getCurrentUserType()).append("' ");
		}

		if ((StringUtils.isNotBlank(criteria.getDueDateFrom()) && StringUtils
				.isNotBlank(criteria.getDueDateTo()))) {
			strBuffPromoQuery.append(" and c.due_date BETWEEN to_date('"
					+ criteria.getDueDateFrom()
					+ "','mm/dd/yyyy') AND to_date('" + criteria.getDueDateTo()
					+ "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(criteria.getDueDateFrom())) {
			strBuffPromoQuery.append(" and c.due_date >= to_date('"
					+ criteria.getDueDateFrom() + "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(criteria.getDueDateTo())) {
			strBuffPromoQuery.append(" and c.due_date <= to_date('"
					+ criteria.getDueDateTo() + "','mm/dd/yyyy')");
		}

		if ((StringUtils.isNotBlank(criteria.getExpShipDateFrom()) && StringUtils
				.isNotBlank(criteria.getExpShipDateTo()))) {
			strBuffPromoQuery.append(" and c.expected_ship_date BETWEEN to_date('"
					+ criteria.getExpShipDateFrom()
					+ "','mm/dd/yyyy') AND to_date('"
					+ criteria.getExpShipDateTo() + "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(criteria.getExpShipDateFrom())) {
			strBuffPromoQuery.append(" and c.expected_ship_date >= to_date('"
					+ criteria.getExpShipDateFrom() + "','mm/dd/yyyy')");
		} else if (StringUtils.isNotBlank(criteria.getExpShipDateTo())) {
			strBuffPromoQuery.append(" and c.expected_ship_date <= to_date('"
					+ criteria.getExpShipDateTo() + "','mm/dd/yyyy')");
		}

		if (StringUtils.isNotBlank(criteria.getCreateDate())) {
			strBuffPromoQuery.append(" and trunc(c.created_date) = to_date('"
					+ criteria.getCreateDate() + "','mm/dd/yyyy')");
		}

		if (StringUtils.isNotBlank(criteria.getUpdateDate())) {
			if (criteria.isNotUpdatedSince()) {
				strBuffPromoQuery.append(" and trunc(c.updated_date) <= to_date('"
						+ criteria.getUpdateDate() + "','mm/dd/yyyy')");
			} else {
				strBuffPromoQuery.append(" and trunc(c.updated_date) = to_date('"
						+ criteria.getUpdateDate() + "','mm/dd/yyyy')");
			}
		}
		if (criteria.hasRecievedImages()) {
			strBuffPromoQuery
					.append(
							" and exists (select 1 from car_image ci inner join image i on i.image_id = ci.image_id where ci.car_id = c.car_id and i.image_tracking_status_cd = '")
					.append(ImageTrackingStatus.RECEIVED).append("')");
		}

		if (criteria.hasRequestedImages()) {
			strBuffPromoQuery
					.append(" and exists (select 1 from car_image ci inner join image i on i.image_id = ci.image_id where ci.car_id = c.car_id ")
					.append(" and (")
					.append(" (i.image_tracking_status_cd = '").append(
							ImageTrackingStatus.REQUESTED).append(
							"' and i.image_source_type_cd = '").append(
							ImageSourceType.VENDOR).append("')").append(" or ")
					.append(" (i.image_tracking_status_cd = '").append(
							ImageTrackingStatus.APPROVED).append(
							"' and i.image_source_type_cd = '").append(
							ImageSourceType.ON_HAND).append("')").append("))");
		}

		return strBuffPromoQuery.toString();

	}
	private void createPromotionChildCriteria(CarSearchCriteria criteria,
		StringBuffer strBuffPromoQuery) {
		//System.out.println("Child PYG criteria method");
	/*if(StringUtils.isNotBlank(criteria.getVendorStyleNumber())){
		strBuffPromoQuery.append(" and vs.vendor_style_number = '"+ criteria.getVendorStyleNumber() +"'");
	}else if(StringUtils.isNotBlank(criteria.getParentVendorStyleNumber())){
		strBuffPromoQuery.append(" and vs.vendor_style_number = '"+ criteria.getParentVendorStyleNumber() +"'");
	}
	if (StringUtils.isNotBlank(criteria.getVendorNumber())) {
		strBuffPromoQuery.append(" and vs.vendor_number = '"+ criteria.getVendorNumber() + "'");
	}*/
	if (StringUtils.isNotBlank(criteria.getVendorName())) {
		strBuffPromoQuery.append(" and upper(v.name) like '%" + StringUtils.replace(StringUtils.upperCase(criteria.getVendorName()), "'", "''") + "%'");
	}
	if (StringUtils.isNotBlank(criteria.getClassNumber()) && !criteria.isCopyCar()) {
		strBuffPromoQuery.append(" and cls.belk_class_number = "	+ criteria.getClassNumber());
	}
	if (StringUtils.isNotBlank(criteria.getDeptCd())) {
		strBuffPromoQuery.append(" and d.dept_cd = " + criteria.getDeptCd());
	}
	/*if (StringUtils.isNotBlank(criteria.getProductType())) {
		strBuffPromoQuery.append(" and upper(pt.name) like '%"	+ StringUtils.replace(StringUtils.upperCase(criteria.getProductType()), "'", "''") + "%'");
	}*/
	if (StringUtils.isNotBlank(criteria.getSourceId())) {
		strBuffPromoQuery.append(" and c.source_id = '" + criteria.getSourceId()+ "'");
	}
	if (StringUtils.isNotBlank(criteria.getBelkUPC())) {
		if (!criteria.getBelkUPC().startsWith("'")) {
			criteria.setBelkUPC("'" + criteria.getBelkUPC() + "'");
		}			
		strBuffPromoQuery.append(" and c.car_id in (select distinct car_id from vendor_sku where belk_upc in ("
						+ criteria.getBelkUPC() + "))");
	}
}
	/**
	 *This method is used get Cars Based on Vendor Number and Vendor Style Number.
	 ***/
	public List<BigDecimal> getCarByVendorNumberAndVendorStyleNumber(String vendorNumber,String vendorStyleNumber) {
		List<BigDecimal> cars=null;
		StringBuffer sb=new StringBuffer("SELECT DISTINCT car_id FROM vendor_style v1, vendor_sku v2 WHERE v1.vendor_style_id = v2.vendor_style_id  ");
			if (StringUtils.isNotBlank(vendorNumber)) {
				sb.append(" and v1.vendor_number = '" + vendorNumber + "'");
			}

			if (StringUtils.isNotBlank(vendorStyleNumber)) {
				sb.append(" and v1.vendor_style_number = '"+ vendorStyleNumber.toUpperCase()+"'");
			}

			SessionFactory sf = getHibernateTemplate().getSessionFactory();
			Session session = sf.getCurrentSession();
			try {
				SQLQuery q = session.createSQLQuery(sb.toString());
				cars = q.list(); 
			} catch (Exception e) {
				log.error("Hibernate Exception");
				e.printStackTrace();
			}
			if(cars==null)
				cars=new ArrayList<BigDecimal>();
			//System.err.println("List of Cars "+cars);
		return cars;
	}

	/**
	 * Method to retrieve the PET Details for a given orin.
	 */
    @Override
    public CarReopenPetDetails getCarReopenPetDetails(long orin) {
        Object ob = getHibernateTemplate().find(
                "from CarReopenPetDetails where orin=?", orin);
            Collection<CarReopenPetDetails> col = (Collection) ob;
            for (CarReopenPetDetails petDetail : col) {
                return petDetail;
            }
            
        return null;
    }

    /**
     * Method to retrieve the PET Details for a given vendorNumber,vendorStyleNUmber.
     */
    @Override
    public CarReopenPetDetails getCarReopenPetDetails(String vendorNumber,String vendorStyleNUmber) {
        Object arr[] = { vendorNumber, vendorStyleNUmber };
        Object ob = getHibernateTemplate().find(
                "from CarReopenPetDetails where vendorNumber=? and vendorStyleNumber=?", arr);
            Collection<CarReopenPetDetails> col = (Collection) ob;
            for (CarReopenPetDetails petDetail : col) {
                return petDetail;
            }
            
        return null;
    }
    
    /**
     * Method to save or update PET details.
     */
    @Override
    public void saveCarReopenPetDetails(CarReopenPetDetails reopenPetDetails) {
        this.getHibernateTemplate().merge(reopenPetDetails);
        this.getHibernateTemplate().flush();
    }	
    
	public void loadChildCarImagesintoPatternCAR(Car car, Car patternCar) {
		Session session = null;
		try {
			String sqlString = "update car_image set car_id=" + String.valueOf(patternCar.getCarId())
					+ " where car_id=" + String.valueOf(car.getCarId());
			session = this.getHibernateTemplate().getSessionFactory().openSession();
			session.beginTransaction();
			Query query = session.createSQLQuery(sqlString);
			query.executeUpdate();
			session.getTransaction().commit();
			session.flush();

		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("Error while reprointing the image to pattern CAR", e);
			}
		} finally {
			try {
				if (session != null) {
					session.close();
				}
			} catch (Exception e2) {
				if (log.isErrorEnabled()) {
					log.error("Error while reprointing the image to pattern CAR", e2);
				}
			}

		}

	}
	
}

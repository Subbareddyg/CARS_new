package com.belk.car.app.dao.hibernate;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.appfuse.dao.hibernate.UniversalDaoHibernate;
import org.appfuse.model.User;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.belk.car.app.Constants;
import com.belk.car.app.dao.CarManagementDao;
import com.belk.car.app.exceptions.DaoException;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarNote;
import com.belk.car.app.model.CarSample;
import com.belk.car.app.model.CarSampleId;
import com.belk.car.app.model.CarSkuAttribute;
import com.belk.car.app.model.CarUserNote;
import com.belk.car.app.model.Classification;
import com.belk.car.app.model.ClosedCarAttrSync;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.ImageLocationType;
import com.belk.car.app.model.ImageProvider;
import com.belk.car.app.model.ImageType;
import com.belk.car.app.model.NoteType;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.model.ProductTypeAttribute;
import com.belk.car.app.model.Sample;
import com.belk.car.app.model.SampleSourceType;
import com.belk.car.app.model.SampleTrackingStatus;
import com.belk.car.app.model.SampleType;
import com.belk.car.app.model.ShippingType;
import com.belk.car.app.model.SizeSynchDataHolderView;
import com.belk.car.app.model.SkuAttributeDelete;
import com.belk.car.app.model.SourceType;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.SuperColorSynchDataHolderView;
import com.belk.car.app.model.TempSizeSynchDataHolder;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.UsersRank;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorSkuDelete;
import com.belk.car.app.model.VendorSkuPIMAttribute;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.VendorStyleSearchCriteria;
import com.belk.car.app.model.VendorStyleType;
import com.belk.car.app.model.WorkFlow;
import com.belk.car.app.model.vendorimage.VendorImage;
import com.belk.car.app.to.IdbCarDataTO;
import com.belk.car.app.to.IdbCarSkuTO;

public class CarManagementDaoHibernate extends UniversalDaoHibernate implements
		CarManagementDao {
	
	public Collection<ImageLocationType> getAllImageLocationTypes() {
		Session sess = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = sess.createQuery("from ImageLocationType");
		return query.setCacheable(true).list();
		//return this.getHibernateTemplate().find("from ImageLocationType");
	}

	public Collection<ImageType> getAllImageTypes() {
		Session sess = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = sess.createQuery("from ImageType");
		return query.setCacheable(true).list();
		//return this.getHibernateTemplate().find("from ImageType");
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
		Short classNum=0;
		try{
		   classNum = new Short(classNumber);
		}catch (Exception e){
			return  null;
		}
		Object arr[] = { classNum, dept };
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
				"from VendorStyle where vendorStyleName=? and vendor=?", arr);
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
			String departmentNumber, String loggedInUser) {
		Department dept = new Department();
		dept.setName(departmentName);
		dept.setDeptCd(departmentNumber);
		dept.setDescription(departmentName);
		dept.setStatusCd("ACTIVE");
		dept.setCreatedBy(loggedInUser);
		dept.setUpdatedBy(loggedInUser);
		dept.setCreatedDate(new Date());
		dept.setUpdatedDate(new Date());

		this.getHibernateTemplate().save(dept);
		this.getHibernateTemplate().flush();
        this.getHibernateTemplate().clear();
		return dept;
	}

	public Classification createClassification(String name, String number,
			String loggedInUser, Department dept) {
		Classification ob = new Classification();
		ob.setName(name);
		try{
		ob.setBelkClassNumber(Short.parseShort(number));
		}catch(Exception e){}
		ob.setDescr(name);
		ob.setDepartment(dept);
		ob.setStatusCd("ACTIVE");
		ob.setIsProductTypeRequired("N");
		ob.setCreatedBy(loggedInUser);
		ob.setUpdatedBy(loggedInUser);
		ob.setCreatedDate(new Date());
		ob.setUpdatedDate(new Date());
		this.getHibernateTemplate().save(ob);
		this.getHibernateTemplate().flush();
		return ob;
	}

	public Vendor createVendor(String name, String number,
			String contactEmailAddr, String loggedInUser) {
		Vendor ob = new Vendor();
		ob.setName(name);
		ob.setVendorNumber(number);
		ob.setDescr(name);
		ob.setStatusCd("ACTIVE");
		ob.setContactEmailAddr(contactEmailAddr);
		ob.setCreatedBy(loggedInUser);
		ob.setUpdatedBy(loggedInUser);
		ob.setUpdatedDate(new Date());
		ob.setCreatedDate(new Date());
                ob.setIsDisplayable("Y");
		this.getHibernateTemplate().save(ob);
		this.getHibernateTemplate().flush();
		return ob;
	}

	public VendorStyle createVendorStyle(String vendorName, String name,
			String vendorNumber, String vendorStyleNumber,
			String vendorStyleName, Vendor vendor,
			Classification classification, String loggedInName) {
		VendorStyle ob = new VendorStyle();
		ob.setVendorNumber(vendorNumber);
		ob.setVendorStyleName(name);
		ob.setVendorStyleNumber(vendorStyleNumber);
		ob.setDescr(name);
		ob.setStatusCd("ACTIVE");
		ob.setCreatedBy(loggedInName);
		ob.setUpdatedBy(loggedInName);
		ob.setCreatedDate(new Date());
		ob.setUpdatedDate(new Date());
		ob.setVendor(vendor);
		ob.setClassification(classification);
		this.getHibernateTemplate().save(ob);
		this.getHibernateTemplate().flush();
		return ob;
	}

	public Classification getClassification(String name, String number,
			String loggedInUser, Department dept) {
		Classification classification = getClassificationByName(name, dept);
		if (classification == null) {
			classification = getClassificationByNumber(number, dept);
		}
		if (classification == null) {
			classification = createClassification(name, number, loggedInUser,
					dept);
		}
		return classification;
	}

	public VendorStyle getVendorStyle(String name, String number,
			String vendorNumber, String vendorName, String emailAddr,
			Classification classification, String loggedInUser) {
		Vendor vendor = getVendor(vendorName, vendorNumber, emailAddr,
				loggedInUser);
		VendorStyle vendorStyle = getVendorStyleByName(name, vendor);
		if (vendorStyle == null) {
			vendorStyle = getVendorStyleByNumber(number, vendor);
		}
		if (vendorStyle == null) {
			vendorStyle = createVendorStyle(vendorName, name, vendorNumber,
					number, name, vendor, classification, loggedInUser);
		}
		return vendorStyle;
	}

	public Vendor getVendor(String name, String number, String emailAddr,
			String loggedInUser) {
		Vendor vendor = getVendorByName(name);
		if (vendor == null) {
			vendor = getVendorByNumber(number);
		}
		if (vendor == null) {
			vendor = createVendor(name, number, emailAddr, loggedInUser);
		}
		return vendor;
	}

	// todo add product type into the mix on create
	public void doCarAttributes(Long carId, Long classId, Long deptId,
			Long productTypeId) {
		Session sess=null;
		try{
		String sql = "{call do_attributes(" + carId.toString() + ","
				+ deptId.toString() + "," + classId.toString()
				+ ")}";
		sess = null;
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		sess = sf.openSession();
		Query query = sess.createSQLQuery(sql);
		query.executeUpdate();
		this.getHibernateTemplate().flush();
		}catch(Exception e){
			log.info("Can't create the car",e);
		}finally{
			sess.close();
		}
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

	private boolean isDuplicateCar(IdbCarDataTO idbCarTO){
    	for (IdbCarSkuTO cs : idbCarTO.getSkuInfo()) {
    		Collection colVs=(Collection) getHibernateTemplate().find("from VendorSku where belkUpc=?",cs.getBelkUPC());
    		if (colVs.size()>0){
    			return true;
    		}
    	}
    	return false;
    }

	public Car updateCar(Car car, String updatedBy) {
		car.setUpdatedBy(updatedBy);
		car.setUpdatedDate(new Date());
		getHibernateTemplate().saveOrUpdate(car);
		getHibernateTemplate().flush();
		return car;
	}
	
	public Car updateCar(Car car, String updatedBy, boolean doCommit) {
		HibernateTemplate hbt =  getHibernateTemplate();
		Session sess =  hbt.getSessionFactory().openSession();
		Transaction tx = null;
		car.setUpdatedBy(updatedBy);
		car.setUpdatedDate(new Date());
		try {			
			
		    tx = sess.beginTransaction();	
			hbt.saveOrUpdate(car);
			if(!tx.wasCommitted()){
				tx.commit();
			}
			tx=null;
		} catch(Exception ex) {
			if (tx!=null) tx.rollback();
			log.error("Caught Transaction Exception while saving the object" +ex);
		}finally{
			sess.close();
		}
		return car;
	}

	public Car getCarFromId(Long carNumber) {
		return (Car) this.getHibernateTemplate().get(Car.class, carNumber);
	}

	public Car assignProductTypeToCar(ProductType productType, Car car) {
		car.setIsProductTypeRequired(ProductType.FLAG_YES);
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

	public CarSkuAttribute getCarSkuAttributeById(Long carSkuAttributeId) {
		return (CarSkuAttribute) getHibernateTemplate().get(
				CarSkuAttribute.class, carSkuAttributeId);
	}

	public Car updateAttributeValueById(Car car, long carAttributeId,
			String value) {
		getCarAttributeById(carAttributeId).setAttrValue(value);
		this.getHibernateTemplate().flush();
		return car;
	}

	public Car updateSkuAttributeValueById(Car car, long carSkuAttributeId,
			String value) {
		getCarSkuAttributeById(carSkuAttributeId).setAttrValue(value);
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
			carNote.setIsExternallyDisplayable(CarNote.FLAG_YES);
		} else {
			carNote.setIsExternallyDisplayable(CarNote.FLAG_NO);
		}
		carNote.setNoteText(noteText);
		carNote.setNoteType(getNoteTypeFromCode(noteTypeCd));
		carNote.setStatusCd(Status.ACTIVE);
		getHibernateTemplate().saveOrUpdate(carNote);
		car.getCarNotes().add(carNote);
		getHibernateTemplate().saveOrUpdate(car);
		getHibernateTemplate().flush();
		return car;
	}
	
	public Car removeCarNote(Car car, CarNote carNote) {
		
		getHibernateTemplate().saveOrUpdate(carNote);
		getHibernateTemplate().saveOrUpdate(car);
		getHibernateTemplate().flush();
		return car;
	}

	private CarAttribute addAttributeToCar(Car car, Attribute attribute,
			String value, String loginName) {
		CarAttribute ca = new CarAttribute();
		ca.setCar(car);
		ca.setStatusCd(Status.ACTIVE);
		ca.setAttribute(attribute);
		ca.setAttrValue(value);
		ca.setCreatedBy(loginName);
		ca.setUpdatedBy(loginName);
		ca.setCreatedDate(new Date());
		ca.setUpdatedDate(new Date());
		return ca;
	}

	public SourceType getSourceTypeForCode(String code) {
		return (SourceType) getHibernateTemplate().get(SourceType.class, code);
	}

	public User getUserForUsername(String username) {
		Object ob = getHibernateTemplate().find("from User where username=?",
				username);
		Collection<User> col = (Collection) ob;
		for (User user : col) {
			return user;
		}
		return null;
	}

	public CarAttribute getCarAttributeFromId(Long carAttributeId) {
		return (CarAttribute) this.getHibernateTemplate().get(
				CarAttribute.class, carAttributeId);
	}

	public Collection<ProductType> getPossibleProductTypesForCar(Car car) {
		Object arr[] = { car.getVendorStyle().getClassification().getClassId(), "ACTIVE" };
		StringBuffer sqlB = new StringBuffer("from ProductType pt ");
		sqlB.append(" inner join fetch pt.classifications as c");
		sqlB.append(" where c.classId = ? and pt.statusCd = ?");

		return getHibernateTemplate().find(sqlB.toString(), arr);
	}

	public Object getFromId(Class cls, Serializable id) {
		return this.getHibernateTemplate().get(cls, id);
	}

	public Object save(Object ob) {
		this.getHibernateTemplate().saveOrUpdate(ob);
		this.getHibernateTemplate().flush();
		return ob;
	}

	public int merge(int count) {
	
			if(this.getHibernateTemplate().getSessionFactory().getCurrentSession().isOpen()){
				 this.getHibernateTemplate().getSessionFactory().getCurrentSession().flush();
				 this.getHibernateTemplate().getSessionFactory().getCurrentSession().clear();
			}
			return count;
		}
		public Object save(Object ob,int count) {
			this.getHibernateTemplate().saveOrUpdate(ob);
			this.getHibernateTemplate().flush();
		   return ob;
		}
	
	public Object saveOrUpdate(Object ob) {
		this.getHibernateTemplate().saveOrUpdate(ob);
		this.getHibernateTemplate().flush();
		return ob;
	}
	
	public Collection<CarAttribute> getActiveAttributesForCar(Car car) {
		Object arr[] = { car, Status.ACTIVE};
		return (Collection<CarAttribute>) getHibernateTemplate().find("from CarAttribute ca where ca.car=? and ca.statusCd=? order by ca.displaySeq",
				arr);
	}

	public void assignProductType(Car car, ProductType prodType, String user) {

		for (ProductTypeAttribute pta : prodType.getProductTypeAttributes()) {
			Object arr[] = { car, pta.getAttribute() };
			Collection<CarAttribute> col = this.getHibernateTemplate().find(
					"from CarAttribute where car=? and attribute=?", arr);
			if (col.size() > 0) {
				for (CarAttribute ca : col) {
					ca.setStatusCd("INACTIVE");
					this.getHibernateTemplate().saveOrUpdate(ca);
				}
			} else {
				CarAttribute ca = new CarAttribute();
				ca.setAttribute(pta.getAttribute());
				ca.setCar(car);
				ca.setAttrValue(pta.getDefaultAttrValue());
				ca.setCreatedBy(user);
				ca.setCreatedDate(new Date());
				ca.setDisplaySeq(pta.getDisplaySeq());
				ca.setHasChanged("N");
				ca.setIsChangeRequired("N");
				ca.setStatusCd("ACTIVE");
				ca.setUpdatedBy(user);
				ca.setUpdatedDate(new Date());
				this.getHibernateTemplate().saveOrUpdate(ca);
			}
		}
		
		car.getVendorStyle().setProductType(prodType) ;
		car.setIsProductTypeRequired("N");
		this.getHibernateTemplate().save(car);
		this.getHibernateTemplate().flush();
	}

	public List<CarNote> getCarNotesByDate(Car car, Integer numToReturn,
			String noteTypeCode) {
		List<CarNote> cnAList = new ArrayList<CarNote>(numToReturn);
		Object arr[] = { car, "ACTIVE", noteTypeCode };
		List<CarNote> carNotes = this.getHibernateTemplate().find(
				"from CarNote " + "where car=? " + "and statusCd=? "
						+ "and noteType.noteTypeCd=?"
						+ "order by updatedDate desc", arr);
		if (numToReturn == null) {
			return carNotes;
		} else {
			CarNote[] cnArr = new CarNote[numToReturn];
			carNotes.toArray(cnArr);
			for (int i = 0; i < numToReturn && carNotes.size() > i; i++) {
				cnAList.add(i, cnArr[i]);
			}
		}
		return cnAList;
	}
	
	/**
	 * Method to retrieve all the active failure notes for the car which
	 * have externally displayable flag set to Y.
	 * 
	 * @param car
	 * @param noteTypeCode
	 * @return
	 */
	public List<CarNote> getDisplayableFailureNotes(Car car, String noteTypeCode){

        Object arr[] = { car, "ACTIVE", noteTypeCode, "Y" };
        List<CarNote> carNotes = this.getHibernateTemplate().find(
                "from CarNote " + "where car=? " + "and statusCd=? "
                        + "and noteType.noteTypeCd=? and isExternallyDisplayable=?"
                        + "order by updatedDate desc", arr);
        return carNotes;
	}

	public Collection<CarUserNote> getUserNotesByDate(Car car,
			Integer numToReturn, String noteTypeCode) {
		List<CarUserNote> cnAList = new ArrayList<CarUserNote>(numToReturn);
		Object arr[] = { car, "ACTIVE", noteTypeCode };
		Collection<CarUserNote> carUserNotes = this.getHibernateTemplate()
				.find(
						"from CarUserNote " + "where car=? "
								+ "and statusCd=? " + "and noteType=? "
								+ "order by updatedDate desc", arr);
		if (numToReturn == null) {
			return carUserNotes;
		} else {
			CarUserNote[] cnArr = new CarUserNote[numToReturn];
			carUserNotes.toArray(cnArr);
			for (int i = 0; i < numToReturn && carUserNotes.size() > i; i++) {
				cnAList.add(i, cnArr[i]);
			}
		}
		return cnAList;
	}

	public Car processNeedSamples(Car car, 
			                      String user,
			                      String color,
			                      Date expectedShipDate,
			                      SampleType sampleType,
			                      ImageProvider imageProvider,
			                      SampleSourceType sampleSourceType,
			                      String carrierNum,
			                      ShippingType shippingType, VendorStyle vendorStyle) {
		
		//Check if Color and Vendor Style Alrady Is there in the Database
		/*for (CarSample carSamp:car.getCarSamples()){
			if (vendorStyle.getVendorStyleId() == 
					carSamp.getSample().getVendorStyle().getVendorStyleId() && 
					carSamp.getSample().getSwatchColor().equals(color)){
				return car;
			}
		}*/
		
		Sample sample = new Sample();
		if (color != null) {
			sample.setSilhoutteRequired(Sample.FLAG_NO);
			sample.setIsReturnable(Sample.FLAG_NO);
			sample.setStatusCd(Status.ACTIVE);
			sample.setSwatchColor(color);
			sample.setUpdatedBy(user);
			sample.setCreatedBy(user);
			sample.setShippingType(shippingType);
			sample.setShipperAccountNumber(carrierNum);
			sample.setExpectedShipDate(expectedShipDate);
			sample.setImageProvider(imageProvider);
			sample.setSampleType(sampleType);
			sample.setSampleSourceType(sampleSourceType);
			sample.setImageProvider(imageProvider);
			SampleTrackingStatus status = (SampleTrackingStatus) this
					.getFromId(SampleTrackingStatus.class,
							SampleTrackingStatus.REQUESTED);
            sample.setSampleTrackingStatus(status);
            sample.setVendorStyle(vendorStyle);
			sample.setUpdatedDate(new Date());
			sample.setCreatedDate(new Date());
			getHibernateTemplate().save(sample);
			getHibernateTemplate().flush();

			CarSample carSample = new CarSample();
			carSample.setCar(car);
			carSample.setSample(sample);
			carSample.setCreatedBy(user);
			carSample.setCreatedDate(new Date());
			carSample.setUpdatedBy(user);
			carSample.setUpdatedDate(new Date());
			//this.getHibernateTemplate().save(carSample);
			car.getCarSamples().add(carSample);

			CarSampleId id = new CarSampleId();
			id.setCarId(car.getCarId());
			id.setSampleId(sample.getSampleId());
			carSample.setId(id);

			//this.getHibernateTemplate().save(car);

		}
		return car;
	}

	public List<ImageProvider> getAllImageProviders() {
		Session sess = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = sess.createQuery("from ImageProvider");
		return query.setCacheable(true).list();
	}
	
	public List<ShippingType> getAllShippingTypes() {
		Session sess = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = sess.createQuery("from ShippingType");
		return query.setCacheable(true).list();
	}
	
	public List<WorkFlow> getAllWorkflow() {
		return getHibernateTemplate().find("from WorkFlow order by workflowId");
	}
	
	public List<String> getRequestSampleValues(Car car){
		List<String> colors = getHibernateTemplate()
				.find(
						"select distinct csa.attrValue "
								+ " from CarSkuAttribute csa, "
								+ " Attribute a," + " Car c," + " VendorSku vs"
								+ " where "
								+ " a.attributeId = csa.attribute.attributeId "
								+ " and csa.vendorSku.carSkuId=vs.carSkuId"
								+ " and vs.car.carId=c.carId "
								+ " and a.name='SKU_COLOR' " + " and c.carId=? "
								+ " and csa.attrValue is not null ",
						car.getCarId());
		if (car.getCarSamples() == null || car.getCarSamples().size() < 1) {
			return colors;
		}

		List<String> retList = new ArrayList<String>(0);
		for (String skuColor : colors) {
			for (CarSample carSample : car.getCarSamples()) {
				if (!skuColor.equals(carSample.getSample().getSwatchColor())) {
					retList.add(skuColor);
				}
			}
		}
		return retList;
	}

	public Car createCar(Car car) {	
		this.getHibernateTemplate().saveOrUpdate(car);
		this.getHibernateTemplate().flush();
		return car;
		}

	public CarSkuAttribute createCarSkuAttribute(CarSkuAttribute carSkuAttribute) {
		this.getHibernateTemplate().saveOrUpdate(carSkuAttribute);
		this.getHibernateTemplate().flush();
		return carSkuAttribute;
	}

	public CarAttribute createCarAttribute(CarAttribute carAttribute){
	    this.getHibernateTemplate().merge(carAttribute);
        this.getHibernateTemplate().flush();
        return carAttribute;
	}
	public Classification createClassification(Classification classification) {
		this.getHibernateTemplate().saveOrUpdate(classification);
		this.getHibernateTemplate().flush();
		return classification;
	}

	public Department createDepartment(Department dept) {
		this.getHibernateTemplate().saveOrUpdate(dept);
        this.getHibernateTemplate().flush();
		return dept;
	}

	public VendorStyle createVendorStyle(VendorStyle vendorStyle) {
		this.getHibernateTemplate().saveOrUpdate(vendorStyle);
		this.getHibernateTemplate().flush();
		return vendorStyle;
	}

	public VendorSku createVendorSku(VendorSku vendorSku) {
		this.getHibernateTemplate().saveOrUpdate(vendorSku);
        this.getHibernateTemplate().flush();
		return vendorSku;
	}

	public Vendor createVendor(Vendor vendor) {
                vendor.setIsDisplayable("Y");
		this.getHibernateTemplate().saveOrUpdate(vendor);
		this.getHibernateTemplate().flush();
		return vendor;
	}
	
	public Car getCar(long carId) {
	    return (Car) this.getHibernateTemplate().get(Car.class, carId);
	}

	public Car getCarAndAttributes(long carId) {
	    Car car = (Car) this.getHibernateTemplate().get(Car.class, carId);
	    this.getHibernateTemplate().initialize(car.getCarAttributes());
	    this.getHibernateTemplate().initialize(car.getVendorStyle().getVendorStylePIMAttribute());
	    this.getHibernateTemplate().initialize(car.getCarNotes());
	    
        Set<VendorSku> vSkus = car.getVendorSkus();
        for(VendorSku vSku : vSkus){
            this.getHibernateTemplate().initialize(vSku.getCarSkuAttributes());
            this.getHibernateTemplate().initialize(vSku.getSkuPIMAttributes());
        }
		return car;
	}

	public Car getCarByVendorStyle(long vendorStyleId) {
		List<Car> cars = this.getHibernateTemplate().find(
		"FROM Car WHERE vendorStyle.vendorStyleId = ?", new Object[] {vendorStyleId});
		
		if (cars != null && !cars.isEmpty()) {
			return cars.get(0) ;
		}
		
		return null;
	}

	
	public List<Car> getCarByParentVendorStyle(long vendorStyleId) {
		List<Car> cars = this.getHibernateTemplate().find(
		"FROM Car WHERE vendorStyle.vendorStyleId = ?", new Object[] {vendorStyleId});
		
		if (cars != null && !cars.isEmpty()) {
			return cars ;
		}
		
		return null;
	}
	
	/**
	 * Method to return all active cars associated to the parent vendor style.
	 * 
	 * @param vendorStyleId
	 * @return
	 */
	public List<Car> getActiveCarsByParentVendorStyle(long vendorStyleId) {
	    Object arr[] = { "ACTIVE", vendorStyleId };
        List<Car> cars = this.getHibernateTemplate().find(
        "FROM Car WHERE statusCd=? and vendorStyle.vendorStyleId = ?", arr);
        
        if (cars != null && !cars.isEmpty()) {
            return cars ;
        }
        
        return null;
    }
	
	public Vendor getVendor(String vendorNumber) {
		List<Vendor> vendors = this.getHibernateTemplate().find(
				"FROM Vendor WHERE vendorNumber = ?", new Object[] {vendorNumber});
				
		if (vendors != null && !vendors.isEmpty()) {
			return vendors.get(0) ;
		}
		
		return null ;
	}

	public Classification getClassification(short classId) {
		List<Classification> classes = this.getHibernateTemplate().find(
				"FROM Classification WHERE belkClassNumber = ?", new Object[] {classId});
				
		if (classes != null && !classes.isEmpty()) {
			return classes.get(0) ;
		}
		
		return null ;
	}

	public Department getDepartment(String deptCd) {
		List<Department> depts = this.getHibernateTemplate().find(
				"FROM Department WHERE deptCd = ?", new Object[] {deptCd});
				
		if (depts != null && !depts.isEmpty()) {
			return depts.get(0) ;
		}
		
		return null ;
	}

	public VendorSku getSku(String belkUpc) {
		List<VendorSku> skus = this.getHibernateTemplate().find(
				"FROM VendorSku WHERE belkUpc = ?", new Object[] {belkUpc});
				
		if (skus != null && !skus.isEmpty()) {
			return skus.get(0) ;
		}
		
		return null ;
	}
	
	
	public VendorSku getActiveCarSkus(String longSku) {
		List<VendorSku> skus = this.getHibernateTemplate().find(
				"from VendorSku as v left outer join fetch v.car as c WHERE v.statusCd= 'ACTIVE' AND c.statusCd= 'ACTIVE' AND v.belkUpc = ?", new Object[] {longSku});
				
		if (skus != null && !skus.isEmpty()) {
			return skus.get(0) ;
		}
		
		return null ;
	}

	public VendorSku getSku(long vendorSkuId) {
		return (VendorSku) this.getHibernateTemplate().get(VendorSku.class, vendorSkuId);
	}

	public List<VendorSku> getSkus(long vendorStyleId) {
		return this.getHibernateTemplate().find(
				"FROM VendorSku WHERE vendorStyle.vendorStyleId = ?", new Object[] {new Long(vendorStyleId)});
	}
	public VendorStyle getVendorStyle(String vendorNumber, String styleNumber) {
		
		List<VendorStyle> vendorStyles = this.getHibernateTemplate().find(
				"FROM VendorStyle vs left outer join fetch vs.vendorStylePIMAttribute as vspa left outer join fetch vs.classification as cls WHERE vs.vendorNumber = ? and vs.vendorStyleNumber = ?", new Object[] {vendorNumber, styleNumber});
				
		if (vendorStyles != null && !vendorStyles.isEmpty()) {
			return vendorStyles.get(0) ;
		}
		
		return null ;
	}

	public VendorStyle getVendorStyle(long vendorStyleId) {
		return (VendorStyle) this.getHibernateTemplate().get(VendorStyle.class, vendorStyleId);
	}
	
	

	public List<VendorStyle> searchVendorStyle(VendorStyleSearchCriteria criteria) {
		//StringBuffer queryBuf = new StringBuffer("select {vs.*}, {cls.*}, {d.*}, {v.*} ");
		StringBuffer queryBuf = new StringBuffer("select {vs.*} ");
		String orderBy = " order by vs.vendor_number asc, vs.vendor_style_number asc";
 
		queryBuf.append(" from vendor_style vs ")
		.append(" inner join vendor v on v.vendor_id = vs.vendor_id ")
		.append(" inner join classification cls on cls.class_id = vs.class_id ")
		.append(" inner join department d on d.dept_id = cls.dept_id ")
		.append(" where 0 = 0 ");
		
		if (StringUtils.isNotBlank(criteria.getVendorNumber())) {
			queryBuf.append(" and v.vendor_number = '").append(criteria.getVendorNumber()).append("'") ;
		}
		
		if (StringUtils.isNotBlank(criteria.getVendorStyleName())) {
			queryBuf.append(" and upper(vs.vendor_style_name) LIKE '%").append(criteria.getVendorStyleName().toUpperCase()).append("%'") ;
		}

		if (StringUtils.isNotBlank(criteria.getVendorStyleNumber())) {
			queryBuf.append(" and (upper(vs.vendor_style_number) LIKE '%").append(criteria.getVendorStyleNumber().toUpperCase()).append("%'") ;
			if (criteria.alsoSearchChildVendorStyle()) {
				queryBuf.append(
						" or exists (select 1 from vendor_style vs2 where vs2.parent_vendor_style_id = vs.vendor_style_id and upper(vs2.vendor_style_number) LIKE '%")
						.append(criteria.getVendorStyleNumber().toUpperCase()).append("%')");
			}
			queryBuf.append(")");
		}
		
		if(criteria.isPatternsOnly()) {
			queryBuf.append(" and vs.vendor_style_type_cd != '").append(VendorStyleType.PRODUCT).append("'");
			
            if(criteria.getPatternType() != null) {
                queryBuf.append(" and vs.vendor_style_type_cd = '").append(criteria.getPatternType()).append("'") ;
            }
		}

		if(criteria.showChildrenOnly() && criteria.getVendorStyleId() > 0) {
			queryBuf.append(" and vs.parent_vendor_style_id = ").append(criteria.getVendorStyleId()) ;
		}
		
		if(criteria.getUser() != null) {
			queryBuf.append(" and exists (select 1 from car_user_department cud where cud.dept_id = d.dept_id and cud.car_user_id = ").append(criteria.getUser().getId()).append(")") ;
		}

		if(StringUtils.isNotBlank(criteria.getStatusCd())) {
			queryBuf.append(" and vs.status_cd = '").append(criteria.getStatusCd()).append("'") ;
		}

		queryBuf.append(orderBy) ;

		List<VendorStyle> vendorStyles = null;
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery q = session.createSQLQuery(queryBuf.toString())
			.addEntity("vs", VendorStyle.class);
/*			.addJoin("v", "vs.vendor")
			.addJoin("cls", "vs.classification")
			.addJoin("d", "cls.department");*/
			//q.setCacheable(true);
			vendorStyles = q.list() ;//convertToVendorStyleList(q.list());
		} catch (Exception e) {
			log.error("Hibernate Exception");
			e.printStackTrace();
		}

		return vendorStyles;

	}

	private List<VendorStyle> convertToVendorStyleList(List<Object> list) {
		
		if (list == null || list.isEmpty()) {
			return new ArrayList<VendorStyle>();
		}

		List<VendorStyle> vendorStyles = new ArrayList<VendorStyle>(list.size());
		//Object[] objArray = list.toArray();
		for (Object obj : list) {
			//Object obj = objArray[i];
			int arrayLength = Array.getLength(obj);
			vsLoop:
			for (int j = 0; j < arrayLength; j++) {
				Object o = Array.get(obj, j);
				if (o.getClass() == VendorStyle.class) {
					vendorStyles.add((VendorStyle) o);
					break vsLoop;
				}
			}
		}
		
		return vendorStyles;
	}

	public List <User> getUsersForVendorAndDept(long vendorId, long deptId){
	  List<User> userList = null;
	  StringBuffer queryBuf = new StringBuffer() ;
	  queryBuf.append("SELECT U.CAR_USER_ID id, U.USER_TYPE_CD userTypeCd, U.LOGIN_NAME username, " +
	  		"U.FIRST_NAME firstName,U.LAST_NAME lastName,U.PASSWORD password,U.EMAIL_ADDR emailAddress, ")
	  .append("U.PHONE phone, U.ALT_PHONE altPhone, U.ADDR1 addr1, U.ADDR2 addr2, U.CITY city, " +
	  		"U.STATE_CD stateCd, U.ZIPCODE zipcode, U.LOCALE locale, ")
	  .append("U.IS_LOCKED isLocked, U.IS_ADMINISTRATOR administrator, U.STATUS_CD statusCd, " )
	  .append("U.LAST_LOGIN_DATE lastLoginDate,U.CREATED_BY createdBy,U.UPDATED_BY updatedBy,U.CREATED_DATE createdDate,")
	  .append("U.UPDATED_DATE updatedDate, U.IS_PRIMARY primary")
	  .append(" FROM car_user U, car_user_vendor CUV, car_user_department CUD ")
	  .append(" WHERE ")
	  .append(" U.car_user_id = CUV.car_user_id ")
	  .append(" and U.car_user_id = CUD.car_user_id ")
	  .append(" and U.status_cd = '").append(Status.ACTIVE).append("'")
	  .append(" and U.user_type_cd = '").append(UserType.VENDOR).append("'")
	  .append(" and CUD.dept_id = ").append(deptId)
	  .append(" and CUV.vendor_id = ").append(vendorId);
	  SessionFactory sf = getHibernateTemplate().getSessionFactory();	  
	  Session session = sf.getCurrentSession();
	  try {
	  		 SQLQuery q = (SQLQuery) session.createSQLQuery(queryBuf.toString())
	  		 	 .addScalar("id", Hibernate.LONG).addScalar("userTypeCd")
	  		 	 .addScalar("username").addScalar("firstName")
	  		     .addScalar("lastName").addScalar("password")
	  		     .addScalar("emailAddress").addScalar("phone")
	  		     .addScalar("altPhone").addScalar("addr1")
	  		     .addScalar("addr2").addScalar("city")
	  		     .addScalar("stateCd").addScalar("zipcode")
	  		     .addScalar("locale").addScalar("isLocked")
	  		     .addScalar("administrator").addScalar("statusCd")
	  		     .addScalar("lastLoginDate").addScalar("createdBy")
	  		     .addScalar("updatedBy").addScalar("createdDate")
	  		     .addScalar("updatedDate").addScalar("primary")
	  		     .setResultTransformer(Transformers.aliasToBean(User.class));
	  		userList = q.list();
	  }catch(Exception e){
		  log.error("Hibernate Exception",e);
	  }
	  if (userList == null) {
		  userList = new ArrayList<User>();
	  }
	  return userList;
	}

	public List<ProductType> getProductTypesByClass(short classNumber) {
		Object arr[] = {classNumber, "ACTIVE" };
		StringBuffer sqlB = new StringBuffer("from ProductType pt ");
		sqlB.append(" inner join fetch pt.classifications as c");
		sqlB.append(" where c.belkClassNumber = ? and pt.statusCd = ?");

		return getHibernateTemplate().find(sqlB.toString(), arr);
	}
	
	public Car getCarForStyle(long styleId){
		Object arr[] = { "ACTIVE", styleId };
		List<Car> cars = this.getHibernateTemplate().find(
				"from Car " + "where statusCd=? "
							+ "and vendorStyle.vendorStyleId=? "
							+ "and rownum = 1", arr);
		if(cars != null && cars.size() > 0){
			return cars.get(0);
		}
		return null;
	}
	
	
	public List<Car> getActiveCarForPO(String poNumber){
		Object arr[] = { "ACTIVE", poNumber,"IN-PROGRESS" };
		List<Car> cars = this.getHibernateTemplate().find(
				"from Car " + "where statusCd=? "
							+ "and sourceId=? "
						    + "and contentStatus.code =? ", arr);
				
		return cars;
	
}
	
	public List<Car> getAllCarForStyle(long styleId){
		Object arr[] = { "ACTIVE", styleId };
		List<Car> cars = this.getHibernateTemplate().find(
				"from Car " + "where statusCd=? "
							+ "and vendorStyle.vendorStyleId=? ", arr);
		return cars;
	}

	/*
	 * (non-Javadoc)
	 * @see com.belk.car.app.dao.CarManagementDao#getDeletedSku(java.lang.String)
	 * Method to get the delete sku detail from VendorSkuDelete
	 */
	public VendorSkuDelete getDeletedSku(String belkUpc) {
		List<VendorSkuDelete> skus = this.getHibernateTemplate().find(
				"FROM VendorSkuDelete WHERE statusCd='ACTIVE' and belkUpc = ? order by createdDate ", new Object[] {belkUpc});
		if (skus != null && !skus.isEmpty()) {
			return skus.get(0) ;
		}
		return null ;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.belk.car.app.dao.CarManagementDao#getDeletedCarSku(java.lang.String)
	 * Method to get the deleted car sku from Vendor Sku
	 */
	public VendorSku getDeletedCarSku(String belkUpc) {
		Car car=null;
		List<VendorSku> skus = this.getHibernateTemplate().find(
				" FROM VendorSku WHERE statusCd='ACTIVE' and belkUpc = ? and updatedDate = " +
				"  (select max(updatedDate) from VendorSku where belkUpc = ?) ", new Object[] {belkUpc,belkUpc});
		for(VendorSku sku:skus){
			car=sku.getCar();
			if(Status.DELETED.equals(car.getStatusCd())){
				return sku;
			}
		}
		return null ;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.belk.car.app.dao.CarManagementDao#getSkuAttributesDelete(long)
	 * Method to get the list of sku attributes deleted for the sku
	 */
	public List<SkuAttributeDelete> getSkuAttributesDelete(long skuid){ //statusCd='ACTIVE' and
		List<SkuAttributeDelete> skus = this.getHibernateTemplate().find(
				"FROM SkuAttributeDelete WHERE  carSkuId = ? ", new Object[] {skuid});
		if (skus != null && !skus.isEmpty()) {
			return skus ;
		}
		return null;
	}
	
	// Added for CARS Faceted Navigation
	//YogeshVedak/FacetedNavigation/method fetches vendor skus with its color code falling in a given range 
	//currently  used for supercolor1 sych job
	@SuppressWarnings("unchecked")
	@Override
	public List<VendorSku> getVendorSkusInRange(String colorCodeBegin,String colorCodeEnd){
		final StringBuffer query = new StringBuffer("Select vs.* from Vendor_Sku vs where vs.color_code between "+colorCodeBegin+" and "+colorCodeEnd);
		//query.append(" and (car_sku_id= 227651 OR car_sku_Id= 38447)");  //this line for testing
		List <VendorSku> skuList = new ArrayList<VendorSku>();
		skuList = (List<VendorSku>)getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException {
						SQLQuery sq =session.createSQLQuery(query.toString()).addEntity(VendorSku.class);
						return sq.list();
					}
				});


		return skuList;
	}




	/**
	 * Returns a Vendor Sku object based on given sql 
	 * Yogesh.Vedak/FacetedNavigation/ 
	 */
	@SuppressWarnings("unchecked")
	public List<VendorSku> getVendorSkusByQuery(final String query){
		List<VendorSku> skuList = null;
		skuList = (List<VendorSku>)getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException {
						SQLQuery sq =session.createSQLQuery(query).addEntity(VendorSku.class);
						return sq.list();
					}
				});
		return skuList;
	}  

	/**
	 * Returns a Vendor Sku object based on belk Upc 
	 * Yogesh.Vedak/FacetedNavigation/ 
	 */
	public  VendorSku getVendorSkuByBelkUPC(String belkUpc){
		VendorSku sku = null;
		//List <VendorSku> vendorSku = getHibernateTemplate().find("from VendorSku vs where vs.belkUpc = "+belkUpc);
		List<VendorSku> vendorSku = getVendorSkusByQuery("select * from vendor_sku where belk_upc="+belkUpc); 
		if((vendorSku != null) && (!vendorSku.isEmpty())){
			sku = vendorSku.get(0);
		}
		return sku ;
	}

	/**@author Yogesh.Vedak
	 * Method to clear the deleted color rule's refrences set on vendor skus  
	 * Tables names used :size_conversion_Master,vendor_sku
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Integer clearSizeRuleReferencesFromSku(String ruleStatusCd){
		final StringBuffer query = new StringBuffer("UPDATE Vendor_sku vs SET vs.size_rule_id = NULL WHERE vs.size_rule_id IN(SELECT scm.scm_id FROM size_conversion_Master scm WHERE scm.status_cd='"+ruleStatusCd+"')");
		return executeSQLQuery(query.toString());
	}


	/**
	 *@author Yogesh.Vedak
	 * Method to clear the deleted color rule's refrences set on vendor skus  
	 * Tables names used :color_mapping_Master,vendor_sku
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Integer clearColorRuleReferencesFromSku(String ruleStatusCd){
		final StringBuffer query = new StringBuffer("UPDATE Vendor_sku vs SET vs.color_rule_id = NULL WHERE vs.color_rule_id IN(SELECT cmm.cmm_id FROM color_mapping_Master cmm WHERE cmm.status_cd='"+ruleStatusCd+"')");
		return executeSQLQuery(query.toString());
	}


	//FacetedNavigation
	/**
	 * @author Yogesh.Vedak
	 * Generalized method which gets sql query as string and returns the affected row-count
	 * @param query
	 * @return
	 */
	private Integer executeSQLQuery(final String query){
		Integer updatedRecordCount = (Integer)getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException {
						Transaction tx =  session.beginTransaction();
						SQLQuery sq =session.createSQLQuery(query.toString());
						int cnt = sq.executeUpdate();
						log.debug("Query in executeSQLQuery() methd: "+query);
						session.flush();
						log.debug("Flush Count"+cnt);
						return cnt;
					}
				});
		return updatedRecordCount;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<VendorSku> getVendorSkusHavingNoSizeRule() {
		StringBuffer query = new StringBuffer("from VendorSku vs where vs.sizeRule IS NULL and vs.car.statusCd <> 'DELETED'");
		return  getHibernateTemplate().find(query.toString());
		//return getVendorSkusByQuery(query.toString());
	}




	/**
	 * Returns updated count for SIze rule entries by reading values fom vendor skus for those skue whose size name mappings were not present in size rule table
	 * @author Yogesh.Vedak
	 */
	public Integer bulkInsertInSizeRuleForNewSkuSizeNames(){ //if we are running this, then separately 1st we could need to add unique constraint for eg ALTER TABLE SIZE_CONVERSION_MASTER ADD CONSTRAINT uc_sizename_dvc UNIQUE (SCM_SIZE_NAME,SCM_DEPT_ID,,SCM_VENDOR_ID,SCM_CLASS_ID) to avoid same entries of D,V,C with same size name
		StringBuffer query= new StringBuffer("INSERT INTO size_conversion_Master(scm_id,scm_size_name,scm_conversion_size_name,scm_dept_id,scm_vendor_id,scm_class_id) ");
		query.append(" select CMM_SEQ.NEXTVAL,sku.size_name,sku.size_name,c.dept_id,vs.vendor_id,vs.class_id FROM vendor_sku sku, car c, vendor_style vs");
		query.append(" where ");
		query.append(" sku.car_id = c.car_id and sku.vendor_style_id = vs.vendor_style_id AND c.status_cd <> 'DELETED' AND sku.size_rule_id is NULL");
		return executeSQLQuery(query.toString());
	}

	/**
	 * This method inserts super-color-1 attribute  for vendor-skus in a batch
	 * @author Yogesh.Vedak
	 */
	public int[] executeSkuSynchBatchUpdateForSuperColor(List<SuperColorSynchDataHolderView> skuView) throws SQLException{
		StatelessSession session = getHibernateTemplate().getSessionFactory().openStatelessSession();
		//Session session = getHibernateTemplate().getSessionFactory().openSession();
		Connection con = session.connection();
		//con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
		boolean initialAutocommitSetting =  con.getAutoCommit();
		con.setAutoCommit(false);
		Attribute attrSuperColor1 = getAttributeByName(Constants.ATTR_SUPERCOLOR1);
		String sqlQuery="UPDATE car_sku_attribute SET attr_value = ?,updated_by= ?,updated_date = sysdate WHERE attr_id= ? and car_sku_id = ?";
		PreparedStatement stmt = con.prepareStatement(sqlQuery);
		for (SuperColorSynchDataHolderView currentViewRow : skuView) {
			stmt.setString(1, currentViewRow.getSuperColorName());
			stmt.setString(2,Constants.SCHEDULER_UPDATEDBY);
			stmt.setLong(3, new Long(attrSuperColor1.getAttributeId()).longValue());
			stmt.setLong(4, new Long(currentViewRow.getCarSkuId()).longValue());  
			stmt.addBatch();
		}
		int [] updatesCount=stmt.executeBatch(); 
		con.commit();
		con.setAutoCommit(initialAutocommitSetting );
		con.close();
		session.close();
		
		return updatesCount; //
	}


	/**
	 * This method updates super-color-1 attribute  vendor-skus in a batch
	 * @author Yogesh.Vedak
	 */
	public int[] executeSkuSynchBatchInsertForSuperColor(List<SuperColorSynchDataHolderView> skuView) throws SQLException,IOException{
		int[] updatesCount=new int[skuView.size()];
		Attribute superColor1 = getAttributeByName(Constants.ATTR_SUPERCOLOR1);
		StatelessSession session = getHibernateTemplate().getSessionFactory().openStatelessSession();
		//Session session = getHibernateTemplate().getSessionFactory().openSession();
		Connection con =session.connection();
		boolean initialAutocommitSetting =  con.getAutoCommit();
		con.setAutoCommit(false);
		String sqlQuery="Insert into car_sku_attribute values (CAR_SKU_ATTRIBUTE_SEQ.NEXTVAL,?,?,?,?,?,sysdate,sysdate)";
		PreparedStatement stmt = con.prepareStatement(sqlQuery);
		for (SuperColorSynchDataHolderView currentViewRow : skuView) {
			stmt.setLong(1, superColor1.getAttributeId());
			stmt.setLong(2, new Long(currentViewRow.getCarSkuId()).longValue());  
			stmt.setString(3, currentViewRow.getSuperColorName());  
			stmt.setString(4, Constants.SCHEDULER_CREATEDBY);  
			stmt.setString(5, Constants.SCHEDULER_CREATEDBY);  
			log.debug("Color-SQL FORMED FOR BATCH :"+sqlQuery);	
			stmt.addBatch();
		}
		updatesCount=stmt.executeBatch(); 
		con.commit();
		con.setAutoCommit(initialAutocommitSetting );
		con.close();
		return updatesCount; //
	}

	/**
	 * This method updates size names of vendor-skus in a batch
	 * @author Yogesh.Vedak
	 */
	public int[] executeSkuSynchBatchUpdateForSize(List<SizeSynchDataHolderView> skuView) throws SQLException{
		StatelessSession session = getHibernateTemplate().getSessionFactory().openStatelessSession();
		//Session session = getHibernateTemplate().getSessionFactory().openSession();
		Connection con =session.connection();
		boolean initialAutocommitSetting =  con.getAutoCommit();
		con.setAutoCommit(false);
		String sqlQuery="update vendor_sku set size_name = ?,size_rule_id = ?,updated_by = ?,updated_date = sysdate where car_sku_id = ? ";
		PreparedStatement stmt = con.prepareStatement(sqlQuery);
		for (SizeSynchDataHolderView currentViewRow : skuView) {
			if(currentViewRow.getScmConversionSizeName()!=null && !"".equals(currentViewRow.getScmConversionSizeName())){
				stmt.setString(1, currentViewRow.getScmConversionSizeName());
				stmt.setLong(2, currentViewRow.getScmId());  
				stmt.setString(3,Constants.SCHEDULER_UPDATEDBY);
				stmt.setLong(4,currentViewRow.getCarSkuId());
				stmt.addBatch();
			}
		}
		int [] updatesCount=stmt.executeBatch(); 
		con.commit();
		con.setAutoCommit(initialAutocommitSetting );
		//con.close();   //try commenting
		return updatesCount; //
	}

	public int[] executeBatchSettingColorRuleOnSkus(List<SuperColorSynchDataHolderView> skuView) throws SQLException{
		StatelessSession session = getHibernateTemplate().getSessionFactory().openStatelessSession();
		Connection con =session.connection();
		boolean initialAutocommitSetting =  con.getAutoCommit();
		con.setAutoCommit(false);
		String sqlQuery="update vendor_sku set color_rule_id = ?,updated_by = ?,updated_date= sysdate where car_sku_id = ? ";
		PreparedStatement stmt = con.prepareStatement(sqlQuery);
		for (SuperColorSynchDataHolderView currentViewRow : skuView) {
			stmt.setLong(1,currentViewRow.getCmmId());  //value is colorRuleId
			stmt.setString(2,Constants.SCHEDULER_UPDATEDBY);
			stmt.setLong(3,new Long(currentViewRow.getCarSkuId()).longValue());		//key is carSkuId
			stmt.addBatch();
		}
		int [] updatesCount=stmt.executeBatch(); 
		con.commit();
		con.setAutoCommit(initialAutocommitSetting );
		con.close();   //try commenting
		return updatesCount; //
	}

	public int[] executeBatchUsingStatement(List<String> queryList) throws IOException,SQLException{

		StatelessSession session = getHibernateTemplate().getSessionFactory().openStatelessSession();
		//Session session = getHibernateTemplate().getSessionFactory().openSession();
		Connection con =session.connection();
		boolean initialAutocommitSetting =  con.getAutoCommit();
		con.setAutoCommit(false);
		Statement stmt = con.createStatement();
		for (String sqlQuery  : queryList) {
			stmt.addBatch(sqlQuery);
		}
		int [] updatesCount=stmt.executeBatch(); 
		con.commit();
		con.setAutoCommit(initialAutocommitSetting );
		return updatesCount; //
	}


	public Attribute getAttributeByName(String attrName){
		List list = getHibernateTemplate().find("from Attribute where name='" + attrName + "' and statusCd='ACTIVE'");
		Attribute attr = null;
		if(list!=null && list.size()>0){
			attr  = (Attribute) list.get(0);
		}
		return attr;
	}

	/**
	 *@author Yogesh.Vedak
	 * Method to clear the super color1set on SKUs for deleted colors .  
	 * 
	 */
	// write sql query in place of hql
	
	/*@SuppressWarnings("unchecked")
	public Integer bulkRemoveSuperColor1FromSkus(){
		Attribute attrSuperColor1 = getAttributeByName(Constants.ATTR_SUPERCOLOR1);
		StringBuffer query = new StringBuffer("Delete CarSkuAttribute csa");
		query.append(" where ");
		query.append("csa.vendorSku.carSkuId ");
		query.append(" IN ");
		query.append("(select vs.carSkuId from VendorSku vs,ColorMappingMaster cmm where vs.colorRule.cmmId = cmm.cmmId and cmm.statusCode = '"+Constants.SUPERCOLOR_STATUS_DELETED+"' and vs.car.statusCd <> 'Deleted')");
		query.append(" and ");
		query.append("csa.attribute.id = ").append(attrSuperColor1.getAttributeIdAsString());
		log.debug("The Delete Query formed to bulk-remove super Color 1 attribute from SKUs is :"+query);
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		Transaction txn = session.beginTransaction();
		int updationCount	= getHibernateTemplate().bulkUpdate(query.toString());	
		log.debug("CarSkuAttribute rows deleted count(Befor calling flush) "+updationCount);
		txn.commit();
		session.flush();
		session.close();
		log.debug("CarSkuAttribute rows deleted count(After calling flush) "+updationCount);
		return updationCount;
	}*/
	
	/**
	 * This method returns the specified list of BelkUpcs  for  which Color rule set on their SKUs has been deleted. 
	 * @return List of {@link VendorSku}
	 * @author Yogesh.Vedak
	 */
	//@BatchSize(size=20)
	@SuppressWarnings("unchecked")
	public List<String> getBelkUpcOFSkuReferencingDeletedColorRules(final int offset,final int batchSize) {
		String setQuery = "select vs.belkUpc from VendorSku vs,ColorMappingMaster cmm where vs.colorRule.cmmId = cmm.cmmId and cmm.statusCode = '"+Constants.SUPERCOLOR_STATUS_DELETED+"' and vs.car.statusCd <> 'Deleted'";
		log.debug("Query Generated to get BelkUpc OF Skus Referencing Deleted Color Rules :"+setQuery);
		return getBelkUpcs(setQuery,offset,batchSize);
	}

	/**
	 * This method returns the specified list of BelkUPc as string objects in a list using state-less session. 
	 * @return List of {@link VendorSku}
	 * @author Yogesh.Vedak
	 */
	//@BatchSize(size=20)
	@SuppressWarnings("unchecked")
	public List<String> getBelkUpcs(String hql, final int offset,final int batchSize) {
		List<String> searchedUPCList = new ArrayList<String>();
		StatelessSession session = getHibernateTemplate().getSessionFactory().openStatelessSession();
		Transaction tx = session.beginTransaction();
		Query setQuery = session.createQuery(hql);
		setQuery.setFirstResult(offset); 
		setQuery.setMaxResults(batchSize);
		setQuery.setFetchSize(batchSize);
		ScrollableResults serachedSkusSet  = setQuery.scroll(ScrollMode.FORWARD_ONLY);
		while ( serachedSkusSet.next() ) {
			String upc = (String) serachedSkusSet.get(0);
			searchedUPCList.add(upc);
			getHibernateTemplate().evict(upc);
		}
		tx.commit();
		session.close();
		return searchedUPCList;
	}

	private Long getRowCount(final String hql){
		Long cnt = (Long) getHibernateTemplate().find(hql).get(0);
		return cnt;
	}

	public Long getSkuCountRefrencingDeletedColorRules(){
		Long count = getRowCount("select count(vs) from VendorSku vs,ColorMappingMaster cmm where vs.colorRule.cmmId = cmm.cmmId and cmm.statusCode = '"+Constants.SUPERCOLOR_STATUS_DELETED+"' and vs.car.statusCd <> 'Deleted'");
		getHibernateTemplate().evict(VendorSku.class);
		return count;
	}


	public void executeSizeSynchrnizationUsingStoreProedure(){
		String query = "{ Call PROCESS_SIZE_RULES.PROCESS_ALL() }";
		executeSQLQuery(query);
	}


	public List<String>  getSkuSizeSynchRecordForBMIGeneration(final int offset,final int batchSize){
		StringBuffer  sqlQuery = new StringBuffer("select 'S|OBJECT_ATTRIBUTE|SKU|'||belk_upc||'|'||atr_name||'|'||(case when atr_val is not null then atr_val else '<NULL>' end)");
		sqlQuery.append(" from ");
		sqlQuery.append(" ( ");
		sqlQuery.append(" select car_sku_id, belk_upc, ");
		sqlQuery.append(" decode(lvl, 1, 'Size_Component_1', 2, 'Size_Component_2', 3, 'Size_Component_3', 4, 'Size_Sub_Component_1', 5, 'Size_Sub_Component_2', 6, 'size_desc') atr_name, ");
		sqlQuery.append(" decode(lvl, 1, facet_size_1, 2, facet_size_2, 3, facet_size_3, 4, facet_sub_size_1, 5, facet_sub_size_2, 6, scm_conversion_size_name) atr_val" );
		sqlQuery.append(" from ");
		sqlQuery.append(" ( ");
		sqlQuery.append(" select * from tmp_size_rules, (select level as lvl from dual connect by level <= 6) ");
		sqlQuery.append(" ) "); 
		sqlQuery.append(" order by belk_upc ) "); 
		log.debug("Query formed in getSkuSizeSynchRecordForBMIGeneration() is: "+sqlQuery);
		//this is the query formed above: "select 'S|OBJECT_ATTRIBUTE|SKU|'||belk_upc||'|'||atr_name||'|'||(case when atr_val is not null then atr_val else '<NULL>' end) from (  select car_sku_id, belk_upc,  decode(lvl, 1, 'size_component_1', 2, 'size_component_2', 3, 'size_component_3', 4, 'size_sub_component_1', 5, 'size_sub_component_2', 6, 'size_desc') atr_name,  decode(lvl, 1, facet_size_1, 2, facet_size_2, 3, facet_size_3, 4, facet_sub_size_1, 5, facet_sub_size_2, 6, size_name) atr_val  from   (    select * from tmp_size_rules, (select level as lvl from dual connect by level <= 6)  ))";
		return getColumnValuesByColumnIndex(sqlQuery.toString(), offset, batchSize, 0);
	}

	private List<String> getColumnValuesByColumnIndex(String sql, final int offset,final int batchSize,int columnIndex){
		List<String> searchedList = new ArrayList<String>();
		StatelessSession session = getHibernateTemplate().getSessionFactory().openStatelessSession();
		Transaction tx = session.beginTransaction();
		SQLQuery setQuery = session.createSQLQuery(sql);
		setQuery.setFirstResult(offset); 
		setQuery.setMaxResults(batchSize);
		setQuery.setFetchSize(batchSize);
		ScrollableResults serachedSkusSet  = setQuery.scroll(ScrollMode.FORWARD_ONLY);
		while ( serachedSkusSet.next() ) {
			String column = (String) serachedSkusSet.get(columnIndex);
			searchedList.add(column);
		}
		tx.commit();
		session.close();
		return searchedList;
	}
	
	
	public Long getTempSizeSynchCount(){
		Long count = getRowCount("select count(ts) from TempSizeSynchDataHolder ts");
		getHibernateTemplate().evict(TempSizeSynchDataHolder.class);
		return count;
	}
	
	public Integer bulkRemoveSuperColor1FromSkus(){
		Attribute attrSuperColor1 = getAttributeByName(Constants.ATTR_SUPERCOLOR1);
		int updationCount=0;
		StringBuffer queryBuf = new StringBuffer(" DELETE FROM CAR_SKU_ATTRIBUTE WHERE (CAR_SKU_ID IN ");
		queryBuf.append("  (SELECT VS.CAR_SKU_ID FROM VENDOR_SKU VS, COLOR_MAPPING_MASTER CMM, ");
		queryBuf.append("  CAR C WHERE     VS.CAR_ID = C.CAR_ID  AND VS.COLOR_RULE_ID = CMM.CMM_ID ");
		queryBuf.append(" AND CMM.STATUS_CD = 'D' AND C.STATUS_CD <> 'Deleted')) AND ATTR_ID = "+ attrSuperColor1.getAttributeIdAsString() );
		
		SessionFactory sf = getHibernateTemplate().getSessionFactory();	  
		Session session = sf.getCurrentSession();
		try {
			SQLQuery q = (SQLQuery) session.createSQLQuery(queryBuf.toString());
			updationCount=q.executeUpdate();
		} catch(Exception e){
			e.printStackTrace();
		}
		return updationCount;
	}

	
	@SuppressWarnings("unchecked")
	public List<User> getAllArtAndSampleUsers() {
		List<User> users=this.getHibernateTemplate().find(
				"FROM User user left outer join fetch user.departments as dt WHERE user.userType IN ('ART_DIRECTOR','SAMPLE_COORDINATOR') AND user.statusCd='ACTIVE'");
		return users;
	}
	/* Method added by AFUSYS3 for VIP 
	 * This method is used to generate a list of car images */
	public Car getCarById(Long carId) {
		Car car = null;
		List<Car> carList = null;
		String query = "";
		query = "from Car as c left outer join fetch c.carImages as ci left outer join fetch c.carSamples as cs " +
				" left outer join fetch c.carOutfitChild as co left outer join fetch c.carHistories as ch where c.carId = ?";
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			Query q = session.createQuery(query);
			q.setLong(0,carId);				
			carList = q.list();
			
			if(carList==null || carList.size() >0){
				car = (Car)carList.get(0);
			}
		} catch (Exception e) {
			log.error("Hibernate Exception" );
			e.printStackTrace();
		}
		return car;

	}
	/* Method added by AFUSYS3 for VIP 
	 * This method is used to generate a list of vendor provided images */
	public List<Car> getVendorImage(Long carId){		
		List<Car> carList = null;				
		String query = "";				
		//inner join fetch i.vendorImage as vi 
		query = "from Car as c inner join fetch c.carImages as ci inner join fetch ci.image as i" +
				" inner join fetch i.vendorImage as vi where  c.carId = ?";			
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			Query q = session.createQuery(query);		
			log.info("Query"+query);
			q.setLong(0,carId);				
			carList = q.list();					
		} catch (Exception e) {
			log.error("Hibernate Exception" );
			e.printStackTrace();
		}
		return carList;

	}
	/* Method added by AFUSYS3 for VIP 
	 * This method is used to generate a list of vendor provided images */
	public List<Car> getCarImageByVendor(Long carId){		
		List<Car> carList = null;				
		String query = "";				
		//inner join fetch i.vendorImage as vi 
		query = "from Car as c inner join fetch c.carImages as ci inner join fetch ci.image as i" +
				" inner join fetch i.vendorImage as vi where  c.carId = ?";			
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			Query q = session.createQuery(query);		
			log.info("Query"+query);
			q.setLong(0,carId);				
			carList = q.list();					
		} catch (Exception e) {
			log.error("Hibernate Exception",e);			
		}
		return carList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Car> getBuyerApprovalPendingCars() {
		return getHibernateTemplate()
				.find("from Car where ASSIGNED_TO_USER_TYPE_CD = 'BUYER' and BUYER_APPROVAL_PENDING = 'Y' and STATUS_CD LIKE 'ACTIVE'");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VendorImage> getVImagesPendingForBuyerApproval(long carID) {
		return getHibernateTemplate()
				.find("from VendorImage as vi inner join fetch vi.image as i inner join fetch i.carImages as ci where vi.buyerApproved ='NONE' and vi.isImageOnMC = 'Y' and ci.car ='"
						+ carID + "'");

	}
	

	public void deleteSampleAndCarSample(long sampleId){
		StringBuffer queryBuf = new StringBuffer(" DELETE FROM CAR_SAMPLE WHERE SAMPLE_ID = '");
		queryBuf.append(sampleId);
		queryBuf.append("'");
		StringBuffer queryBuf_1 = new StringBuffer(" DELETE FROM SAMPLE WHERE SAMPLE_ID = '");
		queryBuf_1.append(sampleId);
		queryBuf_1.append("'");
		SessionFactory sf = getHibernateTemplate().getSessionFactory();	  
		Session session = sf.getCurrentSession();
		try {
			SQLQuery q = (SQLQuery) session.createSQLQuery(queryBuf.toString());
			q.executeUpdate();
			q = (SQLQuery) session.createSQLQuery(queryBuf_1.toString());
			q.executeUpdate();
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<VendorSku> getColorCodeForStyle(long carID,
			String trackOldColorCode, long VendorStyleId) {
		return getHibernateTemplate().find(
				"from VendorSku where CAR_ID = '" + carID
						+ "' and COLOR_CODE = '" + trackOldColorCode
						+ "' and VENDOR_STYLE_ID = '" + VendorStyleId + "' ");
	}
	
	public CarAttribute getCarAttributeByQuery(long carId) {
		String sql = "Select * from CAR_ATTRIBUTE where CAR_ATTR_ID='"+String.valueOf(carId)+"'";
		List<CarAttribute> carAttrsList= getCarAttributesByQuery(sql);
		for (CarAttribute CarAttribute : carAttrsList) {
			return CarAttribute;
		}
		return null;
	}
	public List<CarAttribute> getCarAttributesByQuery(String sql) {
		List<CarAttribute> carAttributes =null;
		
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			carAttributes = session.createSQLQuery(sql).addEntity(CarAttribute.class).list();
		} catch (Exception e) {
			log.error("Hibernate Exception", e);
		}
		return carAttributes;
	}
	
	public void evictCarFromSession(){
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		session.evict(Car.class);
	}
	@Override
	public UsersRank getUsersRank(long deptCd) {

		List<UsersRank> usersRank = this.getHibernateTemplate().find(
				"FROM UsersRank  WHERE departmentCode = ?", new Object[] {deptCd});
										
		if (usersRank != null && !usersRank.isEmpty()) {
			return usersRank.get(0) ;
		}
		
		return null;
	}

	@Override
	public List<Long> getPackVSOrinList(Long carId) {
		// TODO Auto-generated method stub
		
		
		List<VendorStyle> vendorStyles = this.getHibernateTemplate().find(
				"FROM VendorStyle vs left outer join fetch vs.vendorSkus as s  left outer join fetch s.car as c WHERE s.setFlag = ? and c.carId = ?", new Object[] {"Y", carId});
			List<Long> orinlist = new ArrayList<Long>();	
		for(VendorStyle vs:vendorStyles){
			orinlist.add(vs.getOrinNumber());
			
		}
		
		
		
		return orinlist;
	}
	
	@SuppressWarnings("unchecked")
	public List<CarAttribute> getupadtedAttributeforActiveCARS() {
		try {
			String query = "";
			query = " from CarAttribute as ca left outer join fetch ca.attribute as a"
					+ " inner join fetch ca.car as c inner join fetch c.vendorStyle as vs"
					+ " where a.name in ('IS_PWP','IS_GWP')"
					+ " and trunc(ca.updatedDate) = trunc(SYSDATE) and c.statusCd='ACTIVE'"
					+ " and c.contentStatus in ('SENT_TO_CMP','RESEND_TO_CMP','RESEND_DATA_TO_CMP') ";

			return getHibernateTemplate().find(query);
		} catch (Exception e) {
			log.error("----------------> Error ouccured while fetching the PWP and GWP attribute details <-----------------"+e.getMessage());
		}

		return null;
	}
        
        public List<Car> getActiveCarsBySkuOrins(List<String>skuOrins) {
            List<Car> carList = null;
			// HQL to select all ACTIVE/IN-PROGRESS CARS that are 
			// associated with an input list of SKU orin numbers.
            String query = "select distinct c \n" +
                    "from VendorSku vs \n" +
                    "inner join vs.car as c \n" +
                    "inner join c.contentStatus as cs \n" +
                    "where vs.orinNumber in (:orins) \n" +
                    "and c.statusCd = 'ACTIVE' \n" +
                    "and cs.code = 'IN-PROGRESS'";
            
            List<Long> skuOrinsLong = new ArrayList<Long>();
            for (String s : skuOrins) {
                try {
                    skuOrinsLong.add(Long.parseLong(s));
                } catch (NumberFormatException e) {
                    log.warn("Bad sku orin input: " + s);
                }
            }
            
            try  {
                SessionFactory sf = getHibernateTemplate().getSessionFactory();
                Session session = sf.getCurrentSession();
                Query q =  session.createQuery(query);
                q.setParameterList("orins", skuOrinsLong);
                carList = q.list();
            } catch (HibernateException e) {
                log.error("Error in query: " + e.getMessage());
            }
            return carList;
        }
        
        public void updateUncreatedCarsBySkuOrins(List<String> skuOrins, Date poShipDate) {
            
            try {
                String query = "update cars_po_message_esb \n" +
                                "set not_before_date = :shipdate \n" +
                                "where sku_orin in (:orins) \n" +
                                "and processed_indicator = 'N'";
                
                List<Long> skuOrinsLong = new ArrayList<Long>();
                for (String s : skuOrins) {
                    try {
                        skuOrinsLong.add(Long.parseLong(s));
                    } catch (NumberFormatException e) {
                        log.warn("Bad sku orin input: " + s);
                    }
                }
                
                SessionFactory sf = getHibernateTemplate().getSessionFactory();
                Session session = sf.getCurrentSession();
                SQLQuery q = session.createSQLQuery(query);
                q.setParameterList("orins", skuOrinsLong);
                q.setDate("shipdate", poShipDate);
                int rows = q.executeUpdate();
                log.info("updateUncreatedCarsBySkuOrins completed - updated " + rows + " rows.");
            } catch (HibernateException e) {
                log.error("Error in updateUncreatedCarsBySkuOrins: " + e.getMessage());
            }
            
        }
        
        public void executeSkuPackParentResyncProcedure() {
	
            Session session = getHibernateTemplate().getSessionFactory().openSession();
            try{
                    String sql = "{call sync_pack_sku_with_parent()}";
                    Query query = session.createSQLQuery(sql);
                    query.executeUpdate();
                    this.getHibernateTemplate().flush();
                    
                    if (log.isInfoEnabled()) {
                        log.info("sync_pack_sku_with_parent() completed.");
                    }
                    
            }catch(Exception e){
                    log.error("Sku parent relation resync failed: ",e);
            }finally{
                    session.close();
            }
        }
		
    /**
     * Method to retrieve the vendor sku object for a given orin number.
     * @param orinNumber
     * @return
     */
    @SuppressWarnings("unchecked")
    public VendorSku getVendorSkuByOrin(long orinNumber){
        VendorSku vendorSku = null;
        List<VendorSku> vendorSkus = this.getHibernateTemplate().find(
                "from VendorSku as v left outer join fetch v.car as c WHERE v.statusCd= 'ACTIVE' AND c.statusCd= 'ACTIVE' AND v.orinNumber = ?", orinNumber);
                
        if (vendorSkus != null && !vendorSkus.isEmpty()) {
            vendorSku = vendorSkus.get(0) ;
        }
        
        return vendorSku ;
    }
    
    /**
     * Method to get all the closed cars which were updated and not yet processed to be sent to CMP.
     */
    @SuppressWarnings("unchecked")
    public List<ClosedCarAttrSync> getUpdatedClosedCars()throws Exception{
        return this.getHibernateTemplate().find(
                "from ClosedCarAttrSync where jobProcessed='N'");
    }
    
    /**
     * Method to get all the closed cars which were updated and not yet processed to be sent to CMP.
     */
    @SuppressWarnings("unchecked")
    public ClosedCarAttrSync getClosedCarAttrSyncFromDB(long carId) throws Exception{
        List<ClosedCarAttrSync> closedCars =  this.getHibernateTemplate().find(
                "from ClosedCarAttrSync where carId=?", carId);
        if (closedCars!=null && !closedCars.isEmpty()) {
            return closedCars.get(0);
        }
        return null;
    }
    
    /**
     * Method to persist closed cars whose attributes were updated from PIM.  
     */
    public ClosedCarAttrSync createOrUpdateClosedCarAttrSync(ClosedCarAttrSync closedCar)throws Exception{
        this.getHibernateTemplate().saveOrUpdate(closedCar);
        this.getHibernateTemplate().flush();
        return closedCar;
    }
    
}


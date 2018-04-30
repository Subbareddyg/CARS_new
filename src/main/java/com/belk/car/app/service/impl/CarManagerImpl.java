package com.belk.car.app.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections.iterators.ArrayListIterator;
import org.apache.commons.lang.StringUtils;
import org.appfuse.model.LabelValue;
import org.appfuse.model.User;
import org.appfuse.service.impl.UniversalManagerImpl;
import org.springframework.dao.DataAccessException;

import com.belk.car.app.Constants;
import com.belk.car.app.dao.CarDao;
import com.belk.car.app.dao.CarManagementDao;
import com.belk.car.app.dao.LateCarsDao;
import com.belk.car.app.dao.PIMAttributeDao;
import com.belk.car.app.dao.VendorImageEmailNotificationDao;
import com.belk.car.app.dto.CarsDTO;
import com.belk.car.app.dto.DetailNotificationUserDTO;
import com.belk.car.app.dto.LateCarsSummaryDTO;
import com.belk.car.app.dto.NotificationUserDTO;
import com.belk.car.app.dto.RRDCheckEmailNotificationDTO;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.AttributeValueProcessStatus;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarDBPromotionChild;
import com.belk.car.app.model.CarImage;
import com.belk.car.app.model.CarNote;
import com.belk.car.app.model.CarOutfitChild;
import com.belk.car.app.model.CarReopenPetDetails;
import com.belk.car.app.model.CarSearchCriteria;
import com.belk.car.app.model.CarSkuAttribute;
import com.belk.car.app.model.CarUserNote;
import com.belk.car.app.model.CarUserVendorDepartment;
import com.belk.car.app.model.ClassAttribute;
import com.belk.car.app.model.Classification;
import com.belk.car.app.model.ClosedCarAttrSync;
import com.belk.car.app.model.Config;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.DepartmentAttribute;
import com.belk.car.app.model.Image;
import com.belk.car.app.model.ImageLocationType;
import com.belk.car.app.model.ImageProvider;
import com.belk.car.app.model.ImageType;
import com.belk.car.app.model.PoUnitDetail;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.model.ProductTypeAttribute;
import com.belk.car.app.model.SampleSourceType;
import com.belk.car.app.model.SampleType;
import com.belk.car.app.model.ShippingType;
import com.belk.car.app.model.SizeSynchDataHolderView;
import com.belk.car.app.model.SkuAttributeDelete;
import com.belk.car.app.model.SourceType;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.SuperColorSynchDataHolderView;
import com.belk.car.app.model.UsersRank;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorSkuDelete;
import com.belk.car.app.model.VendorSkuPIMAttribute;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.VendorStylePIMAttribute;
import com.belk.car.app.model.VendorStyleSearchCriteria;
import com.belk.car.app.model.WorkFlow;
import com.belk.car.app.model.vendorimage.VendorImage;
import com.belk.car.app.model.vendorimage.VendorImageStatus;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.to.IdbCarDataTO;
import com.belk.car.app.util.DBPromotionUtil;
import com.belk.car.app.util.PropertyLoader;
import com.belk.car.util.DateUtils;

public class CarManagerImpl extends UniversalManagerImpl implements CarManager {

	private CarDao carDao;
	
	private CarManagementDao carMgmtDao;
	
	private CarLookupManager lookupManager;

	private LateCarsDao lateCarDao;
	
	private PIMAttributeDao pimAttributeDao;
	
	private VendorImageEmailNotificationDao vendorImageEmailNotificationDao;
	
	public VendorImageEmailNotificationDao getVendorImageEmailNotificationDao() {
		return vendorImageEmailNotificationDao;
	}

	public void setVendorImageEmailNotificationDao(
			VendorImageEmailNotificationDao vendorImageEmailNotificationDao) {
		this.vendorImageEmailNotificationDao = vendorImageEmailNotificationDao;
	}

	public void setLateCarDao(LateCarsDao lateCarDao){
		this.lateCarDao = lateCarDao;
	}
	
	public PIMAttributeDao getPimAttributeDao() {
        return pimAttributeDao;
    }

    public void setPimAttributeDao(PIMAttributeDao pimAttributeDao) {
        this.pimAttributeDao = pimAttributeDao;
    }

    public CarLookupManager getLookupManager() {
		return lookupManager;
	}

	public void setLookupManager(CarLookupManager lookupManager) {
		this.lookupManager = lookupManager;
	}

	/**
	 * Method that allows setting the DAO to talk to the data store with.
	 * @param dao the dao implementation
	 */
	public void setCarDao(CarDao carDao) {
		this.carDao = carDao;
	}

	public CarManagerImpl() {

	}

	public List<LabelValue> getAdminCreatedFields() {

		List<LabelValue> list = new ArrayList<LabelValue>();
		return list;
	}

	public List<Attribute> getAllAttributes() {

		return carDao.getAllAttributes();
	}

	/**
	 * 
	 * @param usr
	 * @param disableAssociations - Used to allow the user to retrieve complete object graphs or a simple entity object
	 * @return
	 */
	public List<Car> getCarsForUser(User usr, boolean disableAssociations) {
		return carDao.getCarsForUser(usr);
	}
	
	/**
	 * This method fetches list of CARS assigned to the given user.
	 */
	public List<CarsDTO> getCarsForUser(CarSearchCriteria criteria,User usr) {
		List<CarsDTO> list = carDao.getDashboardCarsForUser(criteria,usr);
		return list;
	}

	/**
	 * Retrieve a list of Car objects that are new (that is, Cars with a current workflow status that is INITIATED)
	 * 
	 * @return a list of new cars
	 */
	public List<Car> getNewCars() {
		return this.carDao.getNewCars();
	}

	public WorkFlow getWorkFlowByName(String workFlowName) throws DataAccessException {
		return carDao.getWorkFlowByName(workFlowName);
	}

	public Car addCarNote(Car car, boolean externallyDisplayed, String noteText, String noteTypeCd, String updatedBy) {
		return carMgmtDao.addCarNote(car, externallyDisplayed, noteText, noteTypeCd, updatedBy);
	}
	
	public Car removeCarNote(Car car, CarNote carNote) {
		return carMgmtDao.removeCarNote(car, carNote);
	}
	public Car assignProductTypeToCar(ProductType productType, Car car) {
		return carMgmtDao.assignProductTypeToCar(productType, car);
	}

	public Car createCar(IdbCarDataTO idbCarTO, SourceType sourceType, User user) {
		//return carMgmtDao.createCar(idbCarTO, sourceType, user);
		return null;
	}

	public void doCarAttributes(Long carId, Long classId, Long deptId, Long productTypeId) {
		carMgmtDao.doCarAttributes(carId, classId, deptId, productTypeId);
	}

	public CarAttribute getCarAttribute(long carAttributeId) {
		return carMgmtDao.getCarAttributeFromId(carAttributeId);
	}

	public Car updateAttributeValueById(Car car, long carAttributeId, String value) {
		return carMgmtDao.updateAttributeValueById(car, carAttributeId, value);
	}

	public Car updateCar(Car car, String updatedBy) {
		return carMgmtDao.updateCar(car, updatedBy);
	}
	
	public Car updateCar(Car car, String updatedBy, boolean doCommit){
		return carMgmtDao.updateCar(car, updatedBy, doCommit);
	}

	public Car copyCar(Car oldCar, SourceType sourceType, User user) {
		//return carMgmtDao.copyCar(oldCar, sourceType, user);
		return null;
	}

	public User saveUser(User user) {
		return carDao.saveUser(user);
	}

	public Car getCarFromId(Long carNumber) {
		return carMgmtDao.getCarFromId(carNumber);
	}

	public CarManagementDao getCarMgmtDao() {
		return carMgmtDao;
	}

	public void setCarMgmtDao(CarManagementDao carMgmtDao) {
		this.carMgmtDao = carMgmtDao;
		/* Following might be required while checking roles.
		buyer = (UserType) this.carMgmtDao.getFromId(UserType.class, BUYER);
		webmerch = (UserType) this.carMgmtDao.getFromId(UserType.class, WEBMERCH);
		vendor = (UserType) this.carMgmtDao.getFromId(UserType.class, VENDOR);
		contentmgr = (UserType) this.carMgmtDao.getFromId(UserType.class, CONTENTMGR);
		sampCoord = (UserType) this.carMgmtDao.getFromId(UserType.class, SAMPCOORD);
		untouched = (WorkFlow) this.carMgmtDao.getFromId(WorkFlow.class, new Long(UNTOUCHED));
		touched = (WorkFlow) this.carMgmtDao.getFromId(WorkFlow.class, new Long(TOUCHED));
		getVendorInfo = (WorkFlow) this.carMgmtDao.getFromId(WorkFlow.class, new Long(GETVENDORINFO));
		imageApproval = (WorkFlow) this.carMgmtDao.getFromId(WorkFlow.class, new Long(IMAGEAPPROVAL));
		isPublished = (WorkFlow) this.carMgmtDao.getFromId(WorkFlow.class, new Long(ISPUBLISHED));
		isClosed = (WorkFlow) this.carMgmtDao.getFromId(WorkFlow.class, new Long(ISCLOSED));
		*/
	}

	public CarAttribute getCarAttributeFromId(Long carAttributeId) {
		return carMgmtDao.getCarAttributeFromId(carAttributeId);
	}

	public CarSkuAttribute getCarSkuAttributeById(Long carSkuAttributeId) {
		return this.carMgmtDao.getCarSkuAttributeById(carSkuAttributeId);
	}

	public Car updateSkuAttributeValueById(Car car, long carSkuAttributeId, String value) {
		return this.carMgmtDao.updateSkuAttributeValueById(car, carSkuAttributeId, value);
	}

	public Collection<ProductType> getPossibleProductTypesForCar(Car car) {
		return this.carMgmtDao.getPossibleProductTypesForCar(car);
	}

	public void assignProductType(Car car, ProductType prodType, String user) {
		this.carMgmtDao.assignProductType(car, prodType, user);
	}

	public Object getFromId(Class cls, Serializable id) {
		return this.carMgmtDao.getFromId(cls, id);
	}

	public Object save(Object ob) {
		return this.carMgmtDao.save(ob);
	}
	
	public int merge(int prodlen){
		return this.carMgmtDao.merge(prodlen);
	}
	public Object save(Object ob,int count) {
		return this.carMgmtDao.save(ob,count);
	}
	
	public Collection<CarAttribute> getActiveAttributesForCar(Car car) {
		return this.carMgmtDao.getActiveAttributesForCar(car);
	}

	public Collection<CarUserNote> getUserNotesByDate(Car car, Integer numToReturn, String noteTypeCode) {
		return this.carMgmtDao.getUserNotesByDate(car, numToReturn, noteTypeCode);
	}

	public List<CarNote> getCarNotesByDate(Car car, Integer numToReturn, String noteTypeCode) {
		return this.carMgmtDao.getCarNotesByDate(car, numToReturn, noteTypeCode);
	}

	public List<Car> getAllCars() {
		return carDao.getAllCars();
	}
	
	
	public Collection<ImageLocationType> getAllImageLocationTypes() {
		return carMgmtDao.getAllImageLocationTypes();
	}

	public Collection<ImageType> getAllImageTypes() {
		return carMgmtDao.getAllImageTypes();
	}

	public List<ImageProvider> getAllImageProviders() {
		return carMgmtDao.getAllImageProviders();
	}

	public Car processNeedSamples(Car car, String user, String color, Date expectedShipDate, SampleType sampleType, ImageProvider imageProvider,
			SampleSourceType sampleSourceType, String carrierNum, ShippingType shippingType, VendorStyle vendorStyle) {
		return carMgmtDao.processNeedSamples(car, user, color, expectedShipDate, sampleType, imageProvider, sampleSourceType, carrierNum, shippingType, vendorStyle);
	}

	public List<ShippingType> getAllShippingTypes() {
		return this.carMgmtDao.getAllShippingTypes();
	}

	public List<WorkFlow> getAllWorkflow() {
		return this.carMgmtDao.getAllWorkflow();
	}


	/*
	 * Gets the source type for A code(non-Javadoc)
	 * @see com.belk.car.app.service.CarManager#getSourceTypeForCode(java.lang.String)
	 */
	public SourceType getSourceTypeForCode(String code) {
		return carMgmtDao.getSourceTypeForCode(code);
	}

	public User getUserForUsername(String username) {
		return carMgmtDao.getUserForUsername(username);
	}

	public List<String> getRequestSampleValues(Car car) {
		return carMgmtDao.getRequestSampleValues(car);
	}

	public Car createCar(Car car) {
		return carMgmtDao.createCar(car);
	}

	public Classification createClassification(Classification classification) {
		return carMgmtDao.createClassification(classification);
	}

	public Department createDepartment(Department dept) {
		return carMgmtDao.createDepartment(dept);
	}

	public VendorStyle createVendorStyle(VendorStyle vendorStyle) {
		return carMgmtDao.createVendorStyle(vendorStyle);
	}

	public Vendor createVendor(Vendor vendor) {
		return carMgmtDao.createVendor(vendor);
	}

	public VendorSku createVendorSku(VendorSku vendorSku) {
		return carMgmtDao.createVendorSku(vendorSku);
	}

	public CarSkuAttribute createCarSkuAttribute(CarSkuAttribute carSkuAttribute) {
		return carMgmtDao.createCarSkuAttribute(carSkuAttribute);
	}
	
	public CarAttribute createCarAttribute(CarAttribute carAttribute){
	    return carMgmtDao.createCarAttribute(carAttribute);
	}

	public Car getCar(long carId) {
		return carMgmtDao.getCar(carId);
	}
	
	public Car getCarAndAttributes(long carId) {
	    return carMgmtDao.getCarAndAttributes(carId);
	}

	public Car getCarByVendorStyle(long vendorStyleId) {
		return carMgmtDao.getCarByVendorStyle(vendorStyleId);
	}
	
	public List<Car> 	getCarByParentVendorStyle(long vendorStyleId){
		return carMgmtDao.getCarByParentVendorStyle(vendorStyleId);
	}
	public Classification getClassification(short classId) {
		return carMgmtDao.getClassification(classId);
	}

	public Department getDepartment(String deptCd) {
		return carMgmtDao.getDepartment(deptCd);
	}

	public Department getDepartmentByName(String deptName) {
		return carMgmtDao.getDepartmentByName(deptName);
	}

	public VendorSku getSku(String belkUpc) {
		return carMgmtDao.getSku(belkUpc);
	}

	
	public VendorSku getActiveCarSkus(String longSku) {
		return carMgmtDao.getActiveCarSkus(longSku);
	}

	public VendorSku getSku(long vendorSkuId) {
		return carMgmtDao.getSku(vendorSkuId);
	}

	public VendorStyle getVendorStyle(String vendorNumber, String styleNumber) {
		return carMgmtDao.getVendorStyle(vendorNumber, styleNumber);
	}

	public Vendor getVendor(String vendorNumber) {
		return carMgmtDao.getVendor(vendorNumber);
	}

	public VendorStyle getVendorStyle(long vendorStyleId) {
		return carMgmtDao.getVendorStyle(vendorStyleId);
	}

	public List<VendorStyle> searchVendorStyle(VendorStyleSearchCriteria criteria) {
		return carMgmtDao.searchVendorStyle(criteria);
	}


	public List<User> getUsersForVendorAndDept(long vendorId, long deptId) {
		return carMgmtDao.getUsersForVendorAndDept(vendorId, deptId);
	}

	public List<Car> searchCars(CarSearchCriteria criteria) {
		return carDao.searchCars(criteria);
	}
	
	public List<VendorSku> getSkusForStyle(VendorStyle vs){
		
		return carDao.getSkusForStyle(vs);
	}
	
	public List<CarsDTO> searchCarsForNewDashBoard(CarSearchCriteria criteria, User user) {
		List<CarsDTO> list = carDao.searchCarsForNewDashBoard(criteria, user);
		return list;
	}

	public Integer searchCarsForNewDashBoardCount(CarSearchCriteria criteria, User user) {
		Integer count = carDao.searchCarsForNewDashBoardCount(criteria, user);
		return count;
	}
	
	public List<ClassAttribute> getAllClassificationAttributes(long classId) {
		return carDao.getAllClassificationAttributes(classId);
	}

	public List<DepartmentAttribute> getAllDepartmentAttributes(long deptId) {
		return carDao.getAllDepartmentAttributes(deptId);
	}
    
	public List<CarAttribute> getAllCarAttribute(long carId){
		return carDao.getAllCarAttribute(carId);
	}
	public void remove(Image image) {
		carDao.remove(image);
	}

	public void remove(CarSkuAttribute carSkuAttr) {
		carDao.remove(carSkuAttr);
	}

	public List<NotificationUserDTO> getCarNotificationList(boolean isVendor) throws DataAccessException {
		return carDao.getCarNotificationList(isVendor);
	}

	public List<NotificationUserDTO> getVendorCarEscalationList() throws DataAccessException {
		return carDao.getVendorCarEscalationList();
	}

	public List<NotificationUserDTO> getVendorSampleEscalationList() throws DataAccessException {
		return carDao.getVendorSampleEscalationList();
	}

	public CarAttribute updateCarAttributeValue(CarAttribute carAttr) throws DataAccessException {
		carDao.updateCarAttributeValue(carAttr);
		return carAttr;
	}

	public void updateAttributeValueProcessStatuses(long carId) {		
		 carDao.updateAttributeValueProcessStatuses(carId);
	}

	public List<ProductType> getProductTypesByClass(short classNumber) {
		return this.carMgmtDao.getProductTypesByClass(classNumber);
	}

	public void resyncAttributes(Car car, User loggedInUser)  {
		List<DepartmentAttribute> departmentAttributes = this.getAllDepartmentAttributes(car.getDepartment().getDeptId());
		List<ClassAttribute> classificationAttributes = this.getAllClassificationAttributes(car.getVendorStyle().getClassification()
				.getClassId());

		Map<String, Attribute> attributeMap = new HashMap<String, Attribute>();
		Map<String, String> attributeDefaultValueMap = new HashMap<String, String>();
		log.debug("\t Adding department attribute");
		for (DepartmentAttribute deptAttr : departmentAttributes) {
			if (deptAttr.getAttribute().isActive()) {
				attributeMap.put(deptAttr.getAttribute().getBlueMartiniAttribute(), deptAttr.getAttribute());
				attributeDefaultValueMap.put(deptAttr.getAttribute().getBlueMartiniAttribute(), StringUtils.defaultString(deptAttr.getDefaultAttrValue()));
			}
		}

		ProductType productType = car.getVendorStyle().getProductType() ;
		log.debug("\t Adding product type attribute");
		//Setup Product Type Attributes
		if (productType != null) {
			for (ProductTypeAttribute ptAttr : productType.getProductTypeAttributes()) {
				if (ptAttr.getAttribute().isActive()) {
					if (attributeMap.containsKey(ptAttr.getAttribute().getBlueMartiniAttribute())) {
						attributeMap.remove(ptAttr.getAttribute().getBlueMartiniAttribute());
						attributeDefaultValueMap.remove(ptAttr.getAttribute().getBlueMartiniAttribute());
					}
					attributeMap.put(ptAttr.getAttribute().getBlueMartiniAttribute(), ptAttr.getAttribute());
					attributeDefaultValueMap.put(ptAttr.getAttribute().getBlueMartiniAttribute(), StringUtils.defaultString(ptAttr.getDefaultAttrValue()));
				}
			}
			
		}
		
		log.debug("\t Adding classification attribute");
		for (ClassAttribute classAttr : classificationAttributes) {
			if (classAttr.getAttribute().isActive()) {
				if (attributeMap.containsKey(classAttr.getAttribute().getBlueMartiniAttribute())) {
					attributeMap.remove(classAttr.getAttribute().getBlueMartiniAttribute());
					attributeDefaultValueMap.remove(classAttr.getAttribute().getBlueMartiniAttribute());
				}
				attributeMap.put(classAttr.getAttribute().getBlueMartiniAttribute(), classAttr.getAttribute());
				attributeDefaultValueMap.put(classAttr.getAttribute().getBlueMartiniAttribute(), StringUtils.defaultString(classAttr.getDefaultAttrValue()));
			}
		}
		log.debug("\t Adding car attribute");
		Set<Attribute> attrInCar = new HashSet<Attribute>() ;
		Set<CarAttribute> removeCarAttribute = new HashSet<CarAttribute>() ;
		Collection <Attribute> attributeMapValues = attributeMap.values();
		for (CarAttribute carAttr: car.getCarAttributes()) {
			attrInCar.add(carAttr.getAttribute()) ;
			if (!containsAttribute(attributeMapValues, carAttr.getAttribute())) {
				removeCarAttribute.add(carAttr) ;
			}
		}
		
		AttributeValueProcessStatus checkRequired = this.getLookupManager().getAttributeValueProcessStatus(AttributeValueProcessStatus.CHECK_REQUIRED);
		AttributeValueProcessStatus noCheckRequired = this.getLookupManager().getAttributeValueProcessStatus(AttributeValueProcessStatus.NO_CHECK_REQUIRED);
		log.debug("\t Synchronizing attribute");
		
		if (!removeCarAttribute.isEmpty()) {
			log.info("removing "+ removeCarAttribute.size() + "  attributes from CAR");
			for (CarAttribute ca : removeCarAttribute) {
				car.getCarAttributes().remove(ca) ;
			}
		}
		
		for (Attribute attribute : attributeMap.values()) {
			if(!containsAttribute(attrInCar,attribute)) {
				CarAttribute carAttribute = new CarAttribute();
				carAttribute.setAttribute(attribute);
				carAttribute.setCar(car);
				if (attribute.getAttributeConfig().getHtmlDisplayType().isAutocomplete()) {
					carAttribute.setAttributeValueProcessStatus(checkRequired) ;
				} else {
					carAttribute.setAttributeValueProcessStatus(noCheckRequired) ;										
				}
	
				// Setting to blank for now. Need to get it from the association
				carAttribute.setAttrValue(attributeDefaultValueMap.get(attribute.getBlueMartiniAttribute()));
				carAttribute.setDisplaySeq((short) 0);
				carAttribute.setHasChanged(Constants.FLAG_NO);
				carAttribute.setIsChangeRequired(Constants.FLAG_YES);
				carAttribute.setStatusCd(Status.ACTIVE);
				carAttribute.setAuditInfo(loggedInUser);
				car.getCarAttributes().add(carAttribute);
				log.info("added new attribute to car: "+ car.getCarId() + "   attr_id : "+ attribute.getAttributeId());
			}
		}
		
	}
	
	public boolean containsAttribute(Collection<Attribute> attrSet, Attribute a){
	    if(a != null){
		for(Attribute attr: attrSet){
			if(attr.getAttributeId() == a.getAttributeId()){
				log.debug("containsAttribute: "+ a.getAttributeId());
				return true;
			}
		  }
	    }
		return false;
  }
	
	/*public CarHistory auditCar(Car car, User currentUser, ChangeType changeType, String changeNotes) {
		CarHistory history = new CarHistory() ;
		history.setCar(car) ;
		if (car.getCarUserByAssignedToUserId() != null)
			history.setAssignedTo(car.getCarUserByAssignedToUserId()) ;
		history.setLoggedBy(currentUser) ;
		history.setAuditInfo(currentUser) ;
		history.setChangeType(changeType);
		history.setComment(changeNotes) ;
		history.setStatusCd(Status.ACTIVE) ;
		history.setWorkflowStatus(car.getWorkFlow().getStatusCode()) ;
		this.save(history);
		return history;
	}*/


	public CarsDTO getDtoById(Long id) {
		Car car = this.carMgmtDao.getCar(id);
		CarsDTO carsDTO = this.createCarsDto(car);
		return carsDTO;
	}
	

	private CarsDTO createCarsDto(Car car) {
		CarsDTO carsDTO = new CarsDTO();
		carsDTO.setStyleTypeCd(car.getVendorStyle().getVendorStyleType().getCode());
		carsDTO.setCarId(car.getCarId());
		carsDTO.setDeptNo(car.getDepartment().getDeptCd());
		carsDTO.setClassi(car.getVendorStyle().getClassification().getBelkClassNumber() + "-" +car.getVendorStyle().getClassification().getName());
		carsDTO.setVendor(car.getVendorStyle().getVendor().getName());
		carsDTO.setVendorNo(car.getVendorStyle().getVendorNumber());
		carsDTO.setStyle(car.getVendorStyle().getVendorStyleName());
		carsDTO.setStyleNo(car.getVendorStyle().getVendorStyleNumber());
		carsDTO.setStatus(car.getStatusCd());
		carsDTO.setContentStatus(car.getContentStatus().getName());
		carsDTO.setWorkFlowStatus(car.getCurrentWorkFlowStatus().getName());
		carsDTO.setDueDate(DateUtils.formatDate(car.getDueDate()));
		carsDTO.setCompletionDate(DateUtils.formatDate(car.getExpectedShipDate()));
		carsDTO.setAssignedTo(car.getAssignedToUserType().getName());
		carsDTO.setStyleDesc(car.getVendorStyle().getDescr());
		if (null != car.getVendorStyle().getProductType()) {
			carsDTO.setProductType(car.getVendorStyle().getProductType().getName());
		}
		carsDTO.setPatternType(car.getVendorStyle().getVendorStyleType().getName());
		return carsDTO;
	}
	
	public List<Car> createCars(Collection<IdbCarDataTO> idbData, User user) {
		return null;
	}

	/* -- method modified for removing the outfit car -- */
	
	public void updateCarStatus(String operation, String ids) throws Exception {
		
		StringBuffer query = new StringBuffer("UPDATE CAR ");
		if ("delete".equals(operation)){
			String[] carIds =ids.split(",");
			ArrayListIterator iterator = new ArrayListIterator(carIds);
	            while ( iterator.hasNext( ) ) {       
				String outfitCarId = (String) iterator.next( );
				Car outfitCar = getCarFromId(new Long(outfitCarId));
				  if (outfitCar.getSourceType().getSourceTypeCd().equals("OUTFIT")){
					     
					  removeOutfit(new Long(outfitCarId));
				  } 
			 }
	    }
	    if("archive".equals(operation)) {
			query.append("SET ARCHIVED='Y' ");
		} else if ("unarchive".equals(operation)) {
			query.append("SET ARCHIVED='N' ");
		} else {
			query.append("SET STATUS_CD='DELETED' ");
		}
		query.append("WHERE CAR_ID IN(").append(ids).append(")");
		carDao.updateCars(query.toString());
	}

	public Car getCarForStyle(long styleId){
		return carMgmtDao.getCarForStyle(styleId);
	}
	
	public List<Car> getActiveCarForPO(String poNumber){
		return carMgmtDao.getActiveCarForPO(poNumber);
	}
	

	public List<Car> getAllCarForStyle(long styleId){
		return carMgmtDao.getAllCarForStyle(styleId);
	}
	
	/*
	 * (non-Javadoc)
	 * Updated as part of the CARS SDF June 
	 * Unlock the records for the Catalogs and Order Management Vendor
	 */
	public void unlockAllCars() throws Exception{
		 String query = "UPDATE CAR SET LOCKED='N' WHERE LOCKED='Y'";
 		 carDao.updateCars(query.toString());
		 if(log.isInfoEnabled()){
			 log.info("CarManagerImpl: unlockAllCars(): Unlocked the cars");
		 }
		 
		 String query1 = "update vendor_catalog set locked_by=null";
		 carDao.updateCars(query1.toString());
		 if(log.isInfoEnabled()){
			 log.info("CarManagerImpl: unlockAllCars(): Unlocked the vendor Catalog");
		 }
		 
		 String query2= "update fulfmnt_serv_vndr_cntc set locked_by=null";
		 carDao.updateCars(query2.toString());
		 if(log.isInfoEnabled()){
			 log.info("CarManagerImpl: unlockAllCars(): Unlocked the fulfillment serivce vendor contact");
		 }
		 
		 String query3 = "update fulfmnt_service_vendor set locked_by=null,  is_locked=null ";
		 carDao.updateCars(query3.toString());
		 if(log.isInfoEnabled()){
			 log.info("CarManagerImpl: unlockAllCars(): Unlocked the fulfillment service vendor");
		 }
		 
		 String query4 = "update fulfmnt_service_contact set locked_by=null";
		 carDao.updateCars(query4.toString());
		 if(log.isInfoEnabled()){
			 log.info("CarManagerImpl: unlockAllCars(): Unlocked the fulfillment service");
		 }
		 
    }
	
	public void unlockCar(long carId) throws Exception{
		carDao.unlockCar(carId);
	}
	
	public void unlockCar(String userEmail) throws Exception{
		carDao.unlockCar(userEmail);
	}
	
	public List<DetailNotificationUserDTO> getGeneratedCarList(boolean isVendor){
		return carDao.getGeneratedCarList(isVendor);
		
	}

	public List<CarUserVendorDepartment> getVendorDepartments(Long vendorId) {
		return carDao.getVendorDepartments(vendorId);
	}

	public void deleteVendorDepartment(Long id) {
		
		carDao.deleteVendorDepartment(id);
	}

	public void saveVendorDepartment(CarUserVendorDepartment cvd) {

		carDao.saveVendorDepartment(cvd);
	}

	public List<CarUserVendorDepartment> getVendorsByDeptId(Long id) {
		return carDao.getVendorsByDeptId(id);
	}
	
	
	/**
	 * This method will return all the late car for Buyer and vendor
	 * @return LateCarsSummaryDTO list 
	 */
	public List<LateCarsSummaryDTO> getAllLateCarsForBuyer(){
		return lateCarDao.getAllLateCarsForBuyer();
	}
	
	/**
	 * This method will return all the late car for art director/content manager 
	 * and sample coordinator 
	 * @return LateCarsSummaryDTO list
	 */
	public List<LateCarsSummaryDTO> getAllLateCarsForOtherUser(){
		return lateCarDao.getAllLateCarsForOtherUser();
	}
	
	/**
	 * This method removes outfitCar
	 * 
	 * @param carId
	 */
	public void removeOutfit(long outfitCarId)
	throws Exception {
		
	if (log.isDebugEnabled()) 
	    log.debug("In List Outfit Controller : remove: outfitCarId:" + outfitCarId );

	  Car outfitCar = getCarFromId(new Long(outfitCarId));
	  String strOutfitProductCode = outfitCar.getVendorStyle().getVendorNumber()+ outfitCar.getVendorStyle().getVendorStyleNumber();
	  outfitCar.setStatusCd(Status.DELETED);
	  carMgmtDao.save(outfitCar);
	  Set<CarOutfitChild> carOutfitChild=outfitCar.getCarOutfitChild();
	    for(CarOutfitChild outfitChild : carOutfitChild){
        outfitChild.setStatusCd(Status.DELETED);
        Car childCar = outfitChild.getChildCar();
        CarAttribute ca = carDao.getCarAttributeByBMName(childCar, Constants.OUTFIT_PARENT_PRODUCTS);
	       if(ca != null){
	          String strParentProducts = ca.getAttrValue();
	          StringBuffer sb= new StringBuffer(); 
	          String resultAttrValue1 = null;
	          String resultAttrValue2 = null;
	          String resultAttrValue3 = null;
	          boolean isContains = strParentProducts.contains(strOutfitProductCode);
	          if(isContains){
	             resultAttrValue1=strParentProducts.replaceAll(strOutfitProductCode,"");
	             resultAttrValue2=resultAttrValue1.replaceAll(",,",",");
	             if(resultAttrValue2.startsWith(",")){
	                resultAttrValue3= resultAttrValue2.substring(1);
	              	sb.append(resultAttrValue3);
	             } else  
	              	  { sb.append(resultAttrValue2); }
	             
	             if(sb.toString().endsWith(",")) {
	              	sb.setLength(sb.length() - 1);  
	              }
	           }
	             ca.setAttrValue(sb.toString());
	             carMgmtDao.save(ca);
	         }

	       carMgmtDao.save(outfitChild);
	   }
	  
	}


	public Object saveOrUpdate(Object ob) {
		return this.carMgmtDao.saveOrUpdate(ob);
	}
	
	// added methods for dropship phase 2
	public Attribute getAttributeByName(String attrName){
		return carDao.getAttributeByName(attrName);
	}
	
	public VendorSkuDelete getDeletedSku(String belkUpc){
		return  carMgmtDao.getDeletedSku(belkUpc);
	}
	
	public VendorSku getDeletedCarSku(String belkUpc){
		return  carMgmtDao.getDeletedCarSku(belkUpc);
	}
	public List<SkuAttributeDelete> getSkuAttributesDelete(long skuid){
		return  carMgmtDao.getSkuAttributesDelete(skuid);
	}
	// Added for CARS Faceted Navigation
	/**
	 * @author Yogesh.Vedak
	 * This method returns the VendorSku objects for the ones whose color code belongs to the range specified in method arguments ie between colorCodeBegin and colorCodeEnd.
	 * @param colorCodeBegin
	 * @param colorCodeEnd
	 * @return VendorSku
	 */
		public List<VendorSku> getVendorSkusByColorCode(String colorCodeBegin,String colorCodeEnd){
			  return getCarMgmtDao().getVendorSkusInRange(colorCodeBegin,colorCodeEnd);
		  }
	  
	  public VendorSku getVendorSkuByBelkUPC(String belkUpc){
		  return getCarMgmtDao().getVendorSkuByBelkUPC(belkUpc);
	  }

	  /**
	   * Method to clear the deleted size rule's refrences set on vendor skus  
	   * @author Yogesh.Vedak
	   */
	  @Override
	  public Integer clearColorRuleReferencesFromSku(String ruleStatusCd) {
		return getCarMgmtDao().clearColorRuleReferencesFromSku(ruleStatusCd);
		
	  }
	  
	  /**
	   * Method to clear the deleted size rule's refrences set on vendor skus  
	   * @author Yogesh.Vedak
	   */
	  @Override
	  public Integer clearSizeRuleReferencesFromSku(String ruleStatusCd) {
		return getCarMgmtDao().clearSizeRuleReferencesFromSku(ruleStatusCd);
		
	  }

	  @Override
		public List<VendorSku> getVendorSkusHavingNoSizeRule() {
		return getCarMgmtDao().getVendorSkusHavingNoSizeRule();
	  }
	  
		public Integer bulkInsertInSizeRuleForNewSkuSizeNames(){
		return getCarMgmtDao().bulkInsertInSizeRuleForNewSkuSizeNames();
		}
		
		@Override
		public int[] executeSkuSynchBatchUpdateForSize(List<SizeSynchDataHolderView> skuView) throws SQLException,IOException{
		return 	getCarMgmtDao().executeSkuSynchBatchUpdateForSize(skuView);
		}

		@Override
		public int[] executeSkuSynchBatchUpdateForSuperColor(List<SuperColorSynchDataHolderView> skuView) throws SQLException,IOException {
			return getCarMgmtDao().executeSkuSynchBatchUpdateForSuperColor(skuView);
		}
		
		public int[] executeSkuSynchBatchInsertForSuperColor(List<SuperColorSynchDataHolderView> skuView) throws SQLException,IOException{
		return getCarMgmtDao().executeSkuSynchBatchInsertForSuperColor(skuView);
	}

		@Override
		public int[] executeBatchSettingColorRuleOnSkus(List<SuperColorSynchDataHolderView> colorRuleOnSku)	throws SQLException {
			return getCarMgmtDao().executeBatchSettingColorRuleOnSkus(colorRuleOnSku);
		}
		

		@Override
		public Integer bulkRemoveSuperColor1FromSkus() {
			return getCarMgmtDao().bulkRemoveSuperColor1FromSkus();
		}
		
		@Override
		public Long getSkuCountRefrencingDeletedColorRules(){
			return getCarMgmtDao().getSkuCountRefrencingDeletedColorRules();
		}
		
		public List<String> getBelkUpcOFSkuReferencingDeletedColorRules(final int offset,final int batchSize){
			return getCarMgmtDao().getBelkUpcOFSkuReferencingDeletedColorRules(offset, batchSize);
		}

		public void executeSizeSynchrnizationUsingStoreProedure(){
			getCarMgmtDao().executeSizeSynchrnizationUsingStoreProedure();
		}
		
		@Override
		public List<String> getSkuSizeSynchRecordForBMIGeneration(int offset,int batchSize) {
			return getCarMgmtDao().getSkuSizeSynchRecordForBMIGeneration(offset, batchSize);
		}

		@Override
		public Long getTempSizeSynchCount() {
			return getCarMgmtDao().getTempSizeSynchCount();
		}
	@Override
	public List<User> getAllArtAndSampleUsers() {
		return  carMgmtDao.getAllArtAndSampleUsers();
	}

	@Override
	public List<Object> searchCarsForLastSearchReport(CarSearchCriteria criteria) {
	    return carDao.searchCarsForLastSearchReport(criteria);
	}

	public Car getCarById(Long CarId){
		return carMgmtDao.getCarById(CarId);
	}	
	 // Added for VIP -AFUSYS3
	 public List<Car> getCarImageByVendor(Long carId){
		 return carMgmtDao.getCarImageByVendor(carId);
	 }	
	 
	
	@Override
	public List<Car> getBuyerApprovalPendingCars() {
		return carMgmtDao.getBuyerApprovalPendingCars();
	}

	@Override
	public List<VendorImage> getAllVendorImages(long carID) {
		return carMgmtDao.getVImagesPendingForBuyerApproval(carID);
	}

	@Override
	public List<String> getCollectionSkuByVS(VendorStyle vs) {
		return carDao.getCollectionSkusByVS(vs);
	}
	
	@Override
	public List<String> getDBPromotionCollectionSkuByVS(VendorStyle vs) {
		return carDao.getDBPromotionCollectionSkusByVS(vs);
	}
	/**
	 * This method will return true/Y if buyer approval is pending for one of
	 * image in CAR
	 */
	public String checkBuyerApprovalPendingFlag(Car car) {
		List<CarImage> carImages = car.getCarVendorImages();
		List<String> imagePendingForMCStatusList = Arrays.asList(
				VendorImageStatus.CREATIVE_FAILED, VendorImageStatus.MQ_FAILED,
				VendorImageStatus.DELETED);
		for (CarImage ci : carImages) {
			VendorImage vim = ci.getImage().getVendorImage();
			if (null == vim) {
				continue;
			}
			String strVenImageStatus = vim.getVendorImageStatus()
					.getVendorImageStatusCd();
			if ("NONE".equals(vim.getBuyerApproved())
					&& !imagePendingForMCStatusList.contains(strVenImageStatus)) {
				return "Y";
			}
		}
		return "N";
	}

	/**
	 * 
	 * @return this method return true if any image in CAR is waiting for mechanical check else false; 
	 */
	public boolean checkImageMCPendingByUserFlag(Car car){
		List<CarImage> carImages = car.getCarVendorImages();
		List<String>  imagePendingForMCStatusList = Arrays.asList(VendorImageStatus.UPLOADED, VendorImageStatus.REUPLOADED, VendorImageStatus.SENT_TO_MQ);
		for(CarImage ci : carImages){
			VendorImage vim = ci.getImage().getVendorImage();
			if(null == vim){continue;}
			String strVenImageStatus = vim.getVendorImageStatus().getVendorImageStatusCd();
			if(imagePendingForMCStatusList.contains(strVenImageStatus)){
				log.info("Image pending for mechanical check feedaback-  image: "+ vim.getVendorImageId() + "  status: "+ strVenImageStatus);
				return true;
			}
		}
	    return false;
	}
	
	@Override
	public Map<String, RRDCheckEmailNotificationDTO> populateEmailListForAllCARS() {
		return vendorImageEmailNotificationDao.populateEmailListForAllCARS();
	}
	
	
	public void deleteSampleAndCarSample(long sampleId){
		carMgmtDao.deleteSampleAndCarSample(sampleId);
	}
	
	
	/**
	 * this method renames the vendor images in VENDOR_Image, IMAGE table. as well as it renames physical images on BELKMACL
	 * @return returns true if image is updated, else false
	 */
	public boolean updateVendorImageColor(Car car, VendorSku sku, String strColorCode,User user){
		VendorImage vi =null;
		Image img =null;
		String[] alidVenImgStatusArray = {VendorImageStatus.UPLOADED,VendorImageStatus.REUPLOADED};
		List<String> validImageStatusList = Arrays.asList(alidVenImgStatusArray);
		List<CarImage> carVendorImageList = car.getCarVendorImages();
		boolean isImageUpdated=false;
		if(strColorCode==null || carVendorImageList.isEmpty()){
			return false; //return if car doesnot have any vendor images.
		}
		try{
			String strUploadImageDir = this.getVendorImageUploadDir();
			for(CarImage ci: carVendorImageList){
				img= ci.getImage();
				vi = img.getVendorImage();
				//do not rename image if vendor image is not in UPLOADED or REUPLOADED status
			    if(vi==null || !validImageStatusList.contains(vi.getVendorImageStatus().getVendorImageStatusCd())) {continue;}
			    
			    //if current vendorImage color is same as sku old color code then change the color code of this image
			    if(sku.getVendorStyle().getVendorStyleNumber().equalsIgnoreCase(vi.getVendorStyle().getVendorStyleNumber()) && sku.getColorCode().equals(vi.getColorCode())){
				  
				   //1. update the VendorImage table data
				   vi.setColorCode(strColorCode);
				   vi.setColorName(sku.getColorName());
				   vi.setAuditInfo(user);
				   
				   //2. Update the Image table data
				   String strOldImgName = img.getImageLocation();
				   img.setImageLocation(strOldImgName.replace("_"+sku.getColorCode(), "_"+strColorCode));
				   String strOldImgFinal = img.getImageFinalUrl();
				   img.setImageFinalUrl(strOldImgFinal.replace("_"+sku.getColorCode(), "_"+strColorCode));
				   img.setAuditInfo(user);
				   
				   //3. update actual image name on //belkmacl if image is in (RE)UPLOADED status
				   String oldFilePath = strUploadImageDir +"/"+ strOldImgName;
				   File f = new File(oldFilePath);
				   File sentFile = new File(oldFilePath+".SENT");
				   File newFile =  new File(strUploadImageDir +"/"+img.getImageLocation());
				   
				   if(f.exists()){
					   f.renameTo(newFile);
				   }else if(sentFile.exists()){
					   sentFile.renameTo(newFile);
				   }
				  isImageUpdated=true;
				  
			   }
			}
		}catch(IOException ioe){
			log.error("Error while renaming the physica images on belkmacl "+ioe);
		}catch(Exception e){
			log.error("Error while renaming the images "+e);
		}
		return isImageUpdated;
	}
	
	public String getVendorImageUploadDir() throws Exception {
		Properties properties = PropertyLoader.loadProperties("ftp.properties");
		return properties.getProperty("vendorImageUploadDir");
	}


	/**
	 * This method removes DB Promotional car
	 * 
	 * @param carId
	 */
	public void removeDBPromotion(long dbPromotionCarId)	throws Exception {
		
	if (log.isDebugEnabled()) 
	    log.debug("In removeDBPromotion()  : To remove promotion CarId:" + dbPromotionCarId );

	  Car dbPromotionCar = getCarFromId(new Long(dbPromotionCarId));
	  String strCurrentParentProductCode = DBPromotionUtil.getProductCode(dbPromotionCar);
	  dbPromotionCar.setStatusCd(Status.DELETED);
	  carMgmtDao.save(dbPromotionCar);
	  Set<CarDBPromotionChild> carDBPromotionChild=dbPromotionCar.getCarDBPromotionChild();
	    for(CarDBPromotionChild dbPromotionChild : carDBPromotionChild){
        dbPromotionChild.setStatusCd(Status.DELETED);
        Car childCar = dbPromotionChild.getChildCar();
        CarAttribute ca = carDao.getCarAttributeByBMName(childCar, Constants.DBPROMOTION_PARENT_PRODUCTS);
	       if(ca != null){
	          String strParentProducts = ca.getAttrValue();
	          StringBuffer sb= new StringBuffer(); 
	          String resultAttrValue1 = null;
	          String resultAttrValue2 = null;
	          String resultAttrValue3 = null;
	          boolean isContains = strParentProducts!=null?strParentProducts.contains(strCurrentParentProductCode):false;
	          if(isContains){
	             resultAttrValue1=strParentProducts.replaceAll(strCurrentParentProductCode,"");
	             resultAttrValue2=resultAttrValue1.replaceAll(",,",",");
	             if(resultAttrValue2.startsWith(",")){
	                resultAttrValue3= resultAttrValue2.substring(1);
	              	sb.append(resultAttrValue3);
	             } else  
	              	  { sb.append(resultAttrValue2); }
	             
	             if(sb.toString().endsWith(",")) {
	              	sb.setLength(sb.length() - 1);  
	              }
	           }
	             ca.setAttrValue(sb.toString());
	             carMgmtDao.save(ca);
	         }

	       carMgmtDao.save(dbPromotionChild);
	   }
	  
	}
	/**
	 * this method deletes the physical images from BELKMACL, 
	 * if user deletes the vendor images.
	 */
	@Override
	public void deleteBelkMaclVendorImage(Image img) {
		String imageName = img.getImageLocation();
		String vendorImageUploadDir = "";
		try {
			vendorImageUploadDir = this.getVendorImageUploadDir();
		} catch (Exception e) {
			log.info("Unable to find the Path/Directory");
		}
		String strFilePath = vendorImageUploadDir + imageName;
		File f = new File(strFilePath);
		if (f.exists()) {
			boolean imgDeleted = f.delete();
			log.info("Image: " + imageName + " Deleted - " + imgDeleted);
		} else {
			log.info("Image:" + imageName + " not found in the Directory: "
					+ vendorImageUploadDir);
		}
	
	}

	
	/**
	 * This method returns the number of vendorSkus associated with color code
	 */
	@Override
	public List<VendorSku> getColorCodeForStyle(long carID,	String trackOldColorCode, long vendorStyleId) {
		return carMgmtDao.getColorCodeForStyle(carID, trackOldColorCode,vendorStyleId);
	}	
	
	@Override
	public void savePOUnitdetails(List<PoUnitDetail> poUnitDetailList) {
		carDao.savePOUnitdetails(poUnitDetailList);
	}

	@Override
	public UsersRank getUsersRank(long deptCd) {
		return carMgmtDao.getUsersRank(deptCd);
	}

	@Override
	public List<Long> getPackVSOrinList(Long carId) {
		return carMgmtDao.getPackVSOrinList(carId);
	}
	
	
	public List<CarAttribute> getupadtedAttributeforActiveCARS(){
		return carMgmtDao.getupadtedAttributeforActiveCARS();
		
	}
	
	
	
	
	public CarAttribute getCarAttributeByQuery(long carAttributeId) {
		return carMgmtDao.getCarAttributeByQuery(carAttributeId);
	}
	
	
	/*
	 * This is actually a sort of Car's utility method 
	 * */
	public Map<String,String> getBackupAttrVals(Set<CarAttribute> carAttributes){
		
		Map<String,String> backupMap = new HashMap<String,String>();
		
		for(CarAttribute ca : carAttributes){
			
			backupMap.put(String.valueOf(ca.getCarAttrId()),ca.getAttrValue());
			
		}
		
		return backupMap;
		
	}
	
	public void evictCarFromSession(){
		carMgmtDao.evictCarFromSession();
	}

    public List<Car> getActiveCarsBySkuOrins(List<String> skuOrins) {
        return carMgmtDao.getActiveCarsBySkuOrins(skuOrins);
    }

	/**
	 * Check if car was created after cutover date for PIM integration.
	 * Returns true if car was created after cutover date.
	 * 
	 * If cutover date is not defined, assumes car is pre-cutover.
	 */
	public boolean isPostCutoverCar(Car car) {
		Config config = (Config) lookupManager.getById(Config.class, Config.PIM_INTEGRATION_CUTOVER_DATE);
        if (car == null || config == null) {
        	return false;
        }
    	String postCutoverString = config.getValue();
        if (StringUtils.isBlank(postCutoverString)) {
        	return false; // return false by default if cutover date doesn't exist.
        }
        Date postCutoverDate = DateUtils.parseDate(postCutoverString,"MM/dd/yyyy HH:mm:ss");
        return car.getCreatedDate().after(postCutoverDate);
	}
	
	/**
     * Check if Vendor style was created after cutover date for PIM integration.
     * Returns true if Vendor style was created after cutover date.
     * 
     * If cutover date is not defined, assumes Vendor style is pre-cutover.
     */
    public boolean isPostCutoverVendorStyle(VendorStyle vs) {
        Config config = (Config) lookupManager.getById(Config.class, Config.PIM_INTEGRATION_CUTOVER_DATE);
        if (vs == null || config == null) {
            return false;
        }
        String postCutoverString = config.getValue();
        if (StringUtils.isBlank(postCutoverString)) {
            return false; // return false by default if cutover date doesn't exist.
        }
        Date postCutoverDate = DateUtils.parseDate(postCutoverString,"MM/dd/yyyy HH:mm:ss");
        return vs.getCreatedDate().after(postCutoverDate);
    }
	
	/**
	 * Method to retrieve skus for a vendor style and color code combination.
	 */
	public List<VendorSku> getSkusForStyleColorCd(long vendorStyleId,String colorCd,Car car)throws Exception{
	    return carDao.getSkusForStyleColorCd(vendorStyleId, colorCd,car);
	}

    public void updateUncreatedCarsBySkuOrins(List<String> skuOrins, Date poShipDate) {
        carMgmtDao.updateUncreatedCarsBySkuOrins(skuOrins, poShipDate);
    }
    
    public void executeSkuPackParentResyncProcedure() {
        carMgmtDao.executeSkuPackParentResyncProcedure();
    }
    
    public List<ClosedCarAttrSync> getUpdatedClosedCars()throws Exception{
        return carMgmtDao.getUpdatedClosedCars();
    }
    
    public ClosedCarAttrSync getClosedCarAttrSyncFromDB(long carId) throws Exception {
        return carMgmtDao.getClosedCarAttrSyncFromDB(carId);
    }
    
    public ClosedCarAttrSync createOrUpdateClosedCarAttrSync(ClosedCarAttrSync closedCar)throws Exception{
        return carMgmtDao.createOrUpdateClosedCarAttrSync(closedCar);
    }
    
    public CarReopenPetDetails getCarReopenPetDetails(long orin){
        return carDao.getCarReopenPetDetails(orin);
    }
    
    public void saveCarReopenPetDetails(CarReopenPetDetails reopenPetDetails){
        carDao.saveCarReopenPetDetails(reopenPetDetails);
    }
    
    public CarReopenPetDetails getCarReopenPetDetails(String vendorNumber,String vendorStyleNUmber){
        return carDao.getCarReopenPetDetails(vendorNumber, vendorStyleNUmber);
    }
    
    public List<Car> getActiveCarsByParentVendorStyle(long vendorStyleId) {
        return carMgmtDao.getActiveCarsByParentVendorStyle(vendorStyleId);
    }
    
    public void loadChildCarImagesintoPatternCAR(Car car, Car patternCar) {
    	 carDao.loadChildCarImagesintoPatternCAR(car,patternCar);
    }
    
    public void deletevspalist(List<VendorStylePIMAttribute> vsPimAttList){
        pimAttributeDao.deletevspalist(vsPimAttList);
    }
    
    public void deletevskupalist(List<VendorSkuPIMAttribute> vskuPimAttList){
        pimAttributeDao.deletevskupalist(vskuPimAttList);
    }
}
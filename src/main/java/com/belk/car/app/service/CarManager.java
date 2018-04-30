package com.belk.car.app.service;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.appfuse.model.LabelValue;
import org.appfuse.model.User;
import org.appfuse.service.UniversalManager;
import org.springframework.dao.DataAccessException;

import com.belk.car.app.dto.CarsDTO;
import com.belk.car.app.dto.DetailNotificationUserDTO;
import com.belk.car.app.dto.LateCarsSummaryDTO;
import com.belk.car.app.dto.NotificationUserDTO;
import com.belk.car.app.dto.RRDCheckEmailNotificationDTO;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarNote;
import com.belk.car.app.model.CarReopenPetDetails;
import com.belk.car.app.model.CarSearchCriteria;
import com.belk.car.app.model.CarSkuAttribute;
import com.belk.car.app.model.CarUserNote;
import com.belk.car.app.model.CarUserVendorDepartment;
import com.belk.car.app.model.ClassAttribute;
import com.belk.car.app.model.Classification;
import com.belk.car.app.model.ClosedCarAttrSync;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.DepartmentAttribute;
import com.belk.car.app.model.Image;
import com.belk.car.app.model.ImageLocationType;
import com.belk.car.app.model.ImageProvider;
import com.belk.car.app.model.ImageType;
import com.belk.car.app.model.PoUnitDetail;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.model.SampleSourceType;
import com.belk.car.app.model.SampleType;
import com.belk.car.app.model.ShippingType;
import com.belk.car.app.model.SizeSynchDataHolderView;
import com.belk.car.app.model.SkuAttributeDelete;
import com.belk.car.app.model.SourceType;
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
import com.belk.car.app.model.SuperColorSynchDataHolderView;
import com.belk.car.app.model.SizeSynchDataHolderView;
import com.belk.car.app.to.IdbCarDataTO;


/**
 * Business Service Interface to talk to persistence layer and retrieve values
 * for drop-down choice lists.
 * 
 */
public interface CarManager extends UniversalManager {
    /**
	 * Retrieves all possible roles from persistence layer
	 * 
	 * @return List of LabelValue objects
	 */
    List<LabelValue> getAdminCreatedFields() throws DataAccessException;
    
    List<Attribute> getAllAttributes() throws DataAccessException;
    
    List<Car> getCarsForUser(User usr,boolean disableAssociations) throws DataAccessException;
    
    /**
     * This method fetches list of CARS assigned to the given user.
     * @param usr
     * @return
     * @throws DataAccessException
     */
    
    List<CarsDTO> getCarsForUser(CarSearchCriteria criteria,User usr) throws DataAccessException;
    
	/**
	 * Retrieve a list of Car objects that are new 
	 * @return a list of new cars
	 */
	List<Car> getNewCars();

	WorkFlow getWorkFlowByName(String workFlowName) throws DataAccessException;
    
	public Car addCarNote(Car car, boolean externallyDisplayed,
			String noteText, String noteTypeCd, String updatedBy) ;

	public Car removeCarNote(Car car,CarNote carNote);
	public Car assignProductTypeToCar(ProductType productType, Car car) ;


	public Car createCar(IdbCarDataTO idbCarTO, SourceType sourceType, User user);


	public void doCarAttributes(Long carId, Long classId, Long deptId,
			Long productTypeId) ;


	public Car updateAttributeValueById(Car car, long carAttributeId,
			String value) ;


	public Car updateCar(Car car, String updatedBy) ;
	
	public Car updateCar(Car car, String updatedBy, boolean doCommit);
	
	public Car copyCar(Car oldCar, SourceType sourceType, User user);
	
	public User saveUser(User user);
	
	public Car getCarFromId(Long id);
	
	public CarAttribute getCarAttributeFromId(Long carAttributeId);
	
	public Car updateSkuAttributeValueById(Car car, long carSkuAttributeId, String value);

	public CarSkuAttribute getCarSkuAttributeById(Long carSkuAttributeId);
	
	public Collection <ProductType> getPossibleProductTypesForCar(Car car);
	
	public void assignProductType(Car car, ProductType prodType,String user);
	
	public Object getFromId(Class cls,Serializable id);
	
    public Object save(Object ob);	
    
    public Object save(Object ob,int count);
    public int merge(int ob);
	public Collection <CarAttribute> getActiveAttributesForCar(Car car);
	
	public Collection <CarUserNote> getUserNotesByDate(Car car,
            Integer numToReturn,
            String noteTypeCode);
	
	public List <CarNote> getCarNotesByDate(Car car,
            Integer numToReturn,
            String noteTypeCode);
	
	public List<Car> getAllCars();
	

	public Collection <ImageLocationType> getAllImageLocationTypes();
	
	public Collection <ImageType> getAllImageTypes();
	
	public List <ImageProvider> getAllImageProviders();
	
	public Car processNeedSamples(Car car, 
            String user,
            String color,
            Date expectedShipDate,
            SampleType sampleType,
            ImageProvider imageProvider,
            SampleSourceType sampleSourceType,
            String carrierNum,
            ShippingType shippingType,
            VendorStyle vendorStyle);
	
	public List<ShippingType> getAllShippingTypes();
	
	public List<WorkFlow> getAllWorkflow();
	
	//public WorkFlow processWorkflow(Car car,WorkFlow wf,User user,boolean sendToApproval);
	
	public SourceType getSourceTypeForCode(String code);
	
	public User getUserForUsername(String username);
	

	public List<String> getRequestSampleValues(Car car);
	
	/**
	 * Refactored Code
	 */
	
	VendorStyle getVendorStyle(String vendorNumber, String styleNumber) ;
	
	VendorStyle getVendorStyle(long vendorStyleId);
	
	List<VendorStyle> searchVendorStyle(VendorStyleSearchCriteria criteria) ;
	
	VendorStyle createVendorStyle(VendorStyle vendorStyle) ;

	Vendor getVendor(String vendorNumber);
	
	Vendor createVendor(Vendor vendor) ;

	VendorSku getSku(String belkUpc) ;
	
	VendorSku getActiveCarSkus(String longSku);
		
	VendorSku getSku(long vendorSkuId) ;

	VendorSku createVendorSku(VendorSku vendorSku) ;

	Department getDepartment(String deptCd) ;
	
	Department getDepartmentByName(String deptName) ;
	
	Department createDepartment(Department dept) ;
	
	Classification getClassification(short classId) ;
	
	Classification createClassification(Classification classification) ;
	
	Car createCar(Car car) ;

	List<Car> createCars(Collection<IdbCarDataTO> idbData, User user) ;

	Car getCar(long carId) ;
	
	Car getCarAndAttributes(long carId);
	
	Car getCarByVendorStyle(long vendorStyleId) ;
	List<Car> getCarByParentVendorStyle(long vendorStyleId);
	CarSkuAttribute createCarSkuAttribute(CarSkuAttribute csa) ;
	
	CarAttribute createCarAttribute(CarAttribute ca);

	List <User> getUsersForVendorAndDept(long vendorId, long deptId);
	
	List<Car> searchCars(CarSearchCriteria criteria);
	
	List<VendorSku> getSkusForStyle(VendorStyle vs);
	
	List<CarsDTO> searchCarsForNewDashBoard(CarSearchCriteria criteria, User user);
	
	Integer searchCarsForNewDashBoardCount(CarSearchCriteria criteria, User user);
	
	List<DepartmentAttribute> getAllDepartmentAttributes(long deptId);
	
	List<ClassAttribute> getAllClassificationAttributes(long classId);
	
	List<CarAttribute> getAllCarAttribute(long carId);

	void remove(Image image) ;

	void remove(CarSkuAttribute carSkuAttr) ;

	List<NotificationUserDTO> getCarNotificationList(boolean isVendor) throws DataAccessException;
	
	List<NotificationUserDTO>  getVendorCarEscalationList() throws DataAccessException; 
	
	List<NotificationUserDTO> getVendorSampleEscalationList() throws DataAccessException;
	
	CarAttribute updateCarAttributeValue(CarAttribute carAttr) throws DataAccessException;
	
	void updateAttributeValueProcessStatuses(long carId);

	List<ProductType> getProductTypesByClass(short classNumber);
	
	void resyncAttributes(Car car, User loggedInUser) ;
	
//	CarHistory auditCar(Car car, User currentUser, ChangeType changeType, String changeNotes) ;
	
	public CarsDTO getDtoById(Long id);
	
	public void updateCarStatus(String operation, String ids) throws Exception;
	
	public Car getCarForStyle(long styleId);
	public List<Car> getActiveCarForPO(String poNumber);
	
	
	public void unlockAllCars() throws Exception;
	public void unlockCar(long carId) throws Exception;
	public void unlockCar(String userEmail) throws Exception;
	
	public List<DetailNotificationUserDTO> getGeneratedCarList(boolean isVendor);
	
	public List<CarUserVendorDepartment> getVendorDepartments(Long vendorId);
    
    public List<CarUserVendorDepartment> getVendorsByDeptId(Long id);
    
    public void deleteVendorDepartment(Long id);
    public void saveVendorDepartment(CarUserVendorDepartment cvd);
    public List<LateCarsSummaryDTO> getAllLateCarsForBuyer();
    public List<LateCarsSummaryDTO> getAllLateCarsForOtherUser();
    public void removeOutfit(long outfitCarId) throws Exception ;

    public Object saveOrUpdate(Object ob);	
    
    // added method for drop ship phase 2
    public Attribute getAttributeByName(String attrName);
    public VendorSkuDelete getDeletedSku(String belkUpc);  
    public VendorSku getDeletedCarSku(String belkUpc);
    public List<SkuAttributeDelete> getSkuAttributesDelete( long skuid);
    
    public List<Car> getAllCarForStyle(long styleId);
    
    // Added for CARS Faceted Navigation
    public Integer bulkInsertInSizeRuleForNewSkuSizeNames();
    List<VendorSku> getVendorSkusByColorCode(String colorCodeBegin,String colorCodeEnd);
    VendorSku getVendorSkuByBelkUPC(String belkUpc);
    List<VendorSku> getVendorSkusHavingNoSizeRule();
    Integer clearColorRuleReferencesFromSku(String ruleStatusCd);
    Integer clearSizeRuleReferencesFromSku(String ruleStatusCd);
	public int[] executeSkuSynchBatchUpdateForSize(List<SizeSynchDataHolderView> skuView) throws SQLException,IOException;
	public int[] executeSkuSynchBatchUpdateForSuperColor(List<SuperColorSynchDataHolderView> skuView) throws SQLException,IOException;
	public int[] executeSkuSynchBatchInsertForSuperColor(List<SuperColorSynchDataHolderView> skuView) throws SQLException,IOException;
	public int[] executeBatchSettingColorRuleOnSkus(List<SuperColorSynchDataHolderView> colorRuleOnSku) throws SQLException;
	public Integer bulkRemoveSuperColor1FromSkus();
	public Long getSkuCountRefrencingDeletedColorRules();
	public List<String> getBelkUpcOFSkuReferencingDeletedColorRules(final int offset,final int batchSize);
	void executeSizeSynchrnizationUsingStoreProedure();
	Long getTempSizeSynchCount();
	List<String> getSkuSizeSynchRecordForBMIGeneration(final int offset,final int batchSize);
    
    // Added for CARS Phase II - Afusyq3
    public List<User> getAllArtAndSampleUsers();
    
    // Added for CARS Phase II - Afusyq3
    List<Object> searchCarsForLastSearchReport(CarSearchCriteria criteria);
    
    // Added for CARS Phase II 
    public Car getCarById(Long CarId);
    // Added for Collections Skus
    public List<String> getCollectionSkuByVS(VendorStyle vs);
    public List<String> getDBPromotionCollectionSkuByVS(VendorStyle vs);
       
    public List<Car> getBuyerApprovalPendingCars();
    public List<VendorImage> getAllVendorImages(long carID);

    public List<Car> getCarImageByVendor(Long carId);   
    public String checkBuyerApprovalPendingFlag(Car car);
    public boolean checkImageMCPendingByUserFlag(Car car);

	Map<String, RRDCheckEmailNotificationDTO> populateEmailListForAllCARS();

	public void deleteSampleAndCarSample(long sampleId);
	public boolean updateVendorImageColor(Car car, VendorSku sku,  String strColorCode,User user);
	public String getVendorImageUploadDir() throws Exception;
	public void removeDBPromotion(long dbPromotionCarId)throws Exception ;
	
	public void deleteBelkMaclVendorImage(Image img);

	public List<VendorSku> getColorCodeForStyle(long carID, String trackOldColorCode,long vendorStyleId);
	
	public void savePOUnitdetails(List<PoUnitDetail> poUnitDetailList);

	public UsersRank getUsersRank(long deptCd);

	public List<Long> getPackVSOrinList(Long carId);	
	
	public List<CarAttribute> getupadtedAttributeforActiveCARS();
	
	public CarAttribute getCarAttributeByQuery(long carId);
	
	public Map<String,String> getBackupAttrVals(Set<CarAttribute> carAttributes);
	
	public void evictCarFromSession();
	
    public List<Car> getActiveCarsBySkuOrins(List<String> skuOrins);

	public boolean isPostCutoverCar(Car car);
	
	public boolean isPostCutoverVendorStyle(VendorStyle vs);

    public void updateUncreatedCarsBySkuOrins(List<String> skuOrins, Date poShipDate);
	
	public void executeSkuPackParentResyncProcedure();
	
	public List<VendorSku> getSkusForStyleColorCd(long vendorStyleId,String colorCd,Car car)throws Exception;
	
	public List<ClosedCarAttrSync> getUpdatedClosedCars()throws Exception;
	
    public ClosedCarAttrSync getClosedCarAttrSyncFromDB(long carId) throws Exception;
	    
	public ClosedCarAttrSync createOrUpdateClosedCarAttrSync(ClosedCarAttrSync closedCar)throws Exception;
	
    public CarReopenPetDetails getCarReopenPetDetails(long orin);
    
    public CarReopenPetDetails getCarReopenPetDetails(String vendorNumber,String vendorStyleNUmber);
    
    public void saveCarReopenPetDetails(CarReopenPetDetails reopnPetDetails);
    
    public List<Car> getActiveCarsByParentVendorStyle(long vendorStyleId);

	public void loadChildCarImagesintoPatternCAR(Car car, Car patternCar);
	
    public void deletevspalist(List<VendorStylePIMAttribute> vsPimAttList);
    
    public void deletevskupalist(List<VendorSkuPIMAttribute> vskuPimAttList);
}

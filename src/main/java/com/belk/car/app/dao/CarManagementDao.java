package com.belk.car.app.dao;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.appfuse.model.User;

import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarNote;
import com.belk.car.app.model.CarSkuAttribute;
import com.belk.car.app.model.CarUserNote;
import com.belk.car.app.model.Classification;
import com.belk.car.app.model.ClosedCarAttrSync;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.ImageLocationType;
import com.belk.car.app.model.ImageProvider;
import com.belk.car.app.model.ImageType;
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
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.VendorStyleSearchCriteria;
import com.belk.car.app.model.WorkFlow;
import com.belk.car.app.model.vendorimage.VendorImage;

public interface CarManagementDao {

	Object getFromId(Class cls, Serializable id);

	Car updateCar(Car car, String updatedBy);
	public int merge(int ob);
	public Car updateCar(Car car, String updatedBy, boolean doCommit);

	void doCarAttributes(Long carId, Long classId, Long deptId, Long productTypeId);

	Car assignProductTypeToCar(ProductType productType, Car car);

	Car updateAttributeValueById(Car car, long carAttributeId, String value);

	Car addCarNote(Car car, boolean externallyDisplayed, String noteText, String noteTypeCd, String updatedBy);

	// Car copyCar(Car oldCar,SourceType sourceType,User user );

	Car getCarFromId(Long carNumber);

	CarAttribute getCarAttributeFromId(Long carAttributeId);

	Car updateSkuAttributeValueById(Car car, long carSkuAttributeId, String value);

	CarSkuAttribute getCarSkuAttributeById(Long carSkuAttributeId);

	Collection<ProductType> getPossibleProductTypesForCar(Car car);

	void assignProductType(Car car, ProductType prodType, String user);

	Object save(Object ob);
	
	Object save(Object ob,int count);

	Collection<CarAttribute> getActiveAttributesForCar(Car car);

	Collection<CarUserNote> getUserNotesByDate(Car car, Integer numToReturn, String noteTypeCode);

	List<CarNote> getCarNotesByDate(Car car, Integer numToReturn, String noteTypeCode);

	Collection<ImageLocationType> getAllImageLocationTypes();

	Collection<ImageType> getAllImageTypes();

	List<ImageProvider> getAllImageProviders();

	Car processNeedSamples(Car car, String user, String color, Date expectedShipDate, SampleType sampleType, ImageProvider imageProvider,
			SampleSourceType sampleSourceType, String carrierNum, ShippingType shippingType, VendorStyle vendorStyle);

	List<ShippingType> getAllShippingTypes();

	List<WorkFlow> getAllWorkflow();

	SourceType getSourceTypeForCode(String code);

	User getUserForUsername(String username);

	List<String> getRequestSampleValues(Car car);

	/*
	 * Refactored Code
	 */

	/**
	 * 
	 * @param vendorNumber
	 * @param styleNumber
	 * @return
	 */
	VendorStyle getVendorStyle(String vendorNumber, String styleNumber);

	VendorStyle getVendorStyle(long vendorStyleId);

	List<VendorStyle> searchVendorStyle(VendorStyleSearchCriteria criteria);

	VendorStyle createVendorStyle(VendorStyle vendorStyle);

	Vendor getVendor(String vendorNumber);

	Vendor createVendor(Vendor vendor);

	VendorSku getSku(String belkUpc);
	
	VendorSku getActiveCarSkus(String longSku);

	VendorSku getSku(long vendorSkuId);

	List<VendorSku> getSkus(long vendorStyleId);

	VendorSku createVendorSku(VendorSku vendorSku);

	Department getDepartment(String deptCd);

	Department getDepartmentByName(String deptName);

	Department createDepartment(Department dept);

	Classification getClassification(short classId);

	Classification createClassification(Classification classification);

	Car createCar(Car car);

	Car getCar(long carId);
	
	Car getCarAndAttributes(long carId);

	Car getCarByVendorStyle(long vendorStyleId);

	CarSkuAttribute createCarSkuAttribute(CarSkuAttribute carSkuAttribute);
	
	CarAttribute createCarAttribute(CarAttribute carAttribute);

	List<User> getUsersForVendorAndDept(long vendorId, long deptId);
	
	List<ProductType> getProductTypesByClass(short classNumber);

	Car getCarForStyle(long styleId);
	List<Car> getActiveCarForPO(String poNumber);
	
	List<Car> getAllCarForStyle(long styleId);

	Object saveOrUpdate(Object ob);
	
	// Added methods for Drpship Pahse 2 
	// for maintaining delete sku
	public VendorSkuDelete getDeletedSku(String belkUpc);
	public VendorSku getDeletedCarSku(String belkUpc);
	public List<SkuAttributeDelete> getSkuAttributesDelete(long skuid);

	List<User> getAllArtAndSampleUsers();
	
	//Added methods for CARS Faceted Navigation
	List<VendorSku> getVendorSkusInRange(String colorCodeBegin,String colorCodeEnd);
	List<VendorSku> getVendorSkusByQuery(final String query);//not using delete
	VendorSku getVendorSkuByBelkUPC(String belkUpc);
	public List<VendorSku> getVendorSkusHavingNoSizeRule();
	Integer clearColorRuleReferencesFromSku(String ruleStatusCd);
	Integer clearSizeRuleReferencesFromSku(String ruleStatusCd);
	public Integer bulkInsertInSizeRuleForNewSkuSizeNames();
	public int[] executeSkuSynchBatchUpdateForSize(List<SizeSynchDataHolderView> skuView) throws SQLException,IOException;
	public int[] executeSkuSynchBatchUpdateForSuperColor(List<SuperColorSynchDataHolderView> skuView) throws SQLException,IOException;
	public int[] executeSkuSynchBatchInsertForSuperColor(List<SuperColorSynchDataHolderView> skuView) throws SQLException,IOException;
	public int[] executeBatchSettingColorRuleOnSkus(List<SuperColorSynchDataHolderView> colorRuleOnSku)throws SQLException;
	public Integer bulkRemoveSuperColor1FromSkus();
	public Long getSkuCountRefrencingDeletedColorRules();
	public List<String> getBelkUpcOFSkuReferencingDeletedColorRules(final int offset,final int batchSize);
	void executeSizeSynchrnizationUsingStoreProedure();
	Long getTempSizeSynchCount();
	List<String> getSkuSizeSynchRecordForBMIGeneration(final int offset,final int batchSize);
	

	public Car getCarById(Long CarId);	
	// Added for VIP -AFUSYS3
	public List<Car> getCarImageByVendor(Long carId);
	public List<Car> getBuyerApprovalPendingCars();
	public List<VendorImage> getVImagesPendingForBuyerApproval(long carID);
	
	public void deleteSampleAndCarSample(long sampleId);
	
	public List<VendorSku> getColorCodeForStyle(long carID, String trackOldColorCode,long VendorStyleId);
	
	public CarAttribute getCarAttributeByQuery(long carId) ;
		
	public List<CarAttribute> getCarAttributesByQuery(String sql) ;
	
	public void evictCarFromSession();
	
	public UsersRank getUsersRank(long deptCd);

	public List<Long> getPackVSOrinList(Long carId);
	
	public List<CarAttribute> getupadtedAttributeforActiveCARS();

    public List<Car> getActiveCarsBySkuOrins(List<String>skuOrins);

    public void updateUncreatedCarsBySkuOrins(List<String> skuOrins, Date poShipDate);

    public void executeSkuPackParentResyncProcedure();
	
	public List<CarNote> getDisplayableFailureNotes(Car car, String noteTypeCode);
	
	public VendorSku getVendorSkuByOrin(long orinNumber);
	
	public List<ClosedCarAttrSync> getUpdatedClosedCars()throws Exception;
	
	public ClosedCarAttrSync getClosedCarAttrSyncFromDB(long carId) throws Exception;
	
	public ClosedCarAttrSync createOrUpdateClosedCarAttrSync(ClosedCarAttrSync closedCar)throws Exception;

	public Car removeCarNote(Car car, CarNote carNote);

	List<Car> getCarByParentVendorStyle(long vendorStyleId);  
	
	public List<Car> getActiveCarsByParentVendorStyle(long vendorStyleId);
}

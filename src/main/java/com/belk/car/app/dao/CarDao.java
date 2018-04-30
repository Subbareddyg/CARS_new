package com.belk.car.app.dao;

import java.util.List;

import org.appfuse.dao.UniversalDao;
import org.appfuse.model.User;

import com.belk.car.app.dto.CarsDTO;
import com.belk.car.app.dto.DetailNotificationUserDTO;
import com.belk.car.app.dto.NotificationUserDTO;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.AttributeDatatype;
import com.belk.car.app.model.AttributeType;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarReopenPetDetails;
import com.belk.car.app.model.CarSearchCriteria;
import com.belk.car.app.model.CarSkuAttribute;
import com.belk.car.app.model.CarUserVendorDepartment;
import com.belk.car.app.model.ChangeType;
import com.belk.car.app.model.ClassAttribute;
import com.belk.car.app.model.DepartmentAttribute;
import com.belk.car.app.model.Image;
import com.belk.car.app.model.NoteType;
import com.belk.car.app.model.PoUnitDetail;
import com.belk.car.app.model.SampleType;
import com.belk.car.app.model.ShippingType;
import com.belk.car.app.model.SourceType;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.WorkFlow;
import com.belk.car.app.model.CarOutfitChild;

public interface CarDao extends UniversalDao {

	
	/**
	 * Retrieve a list of Car objects that are new (that is, Cars with a current workflow status that is INITIATED.
	 * 
	 * @return a list of new cars
	 */
	List<Car> getNewCars();
	
	public List<CarsDTO> searchCarsForNewDashBoard(CarSearchCriteria criteria, User user);
	
	public Integer searchCarsForNewDashBoardCount(CarSearchCriteria criteria, User user);
	
    List<CarAttribute> getAttributesForCar(Car car);

    /**
	 * This function has been migrated to the AttributeDao  class
	 * 
	 * @deprecated
	 */
	List<Attribute> getAllAttributes();

	/**
	 * This function has been migrated to the AttributeDao  class
	 * 
	 * @deprecated
	 */
	List<AttributeDatatype> getAttributeDataTypes();

	/**
	 * This function has been migrated to the AttributeDao  class
	 * 
	 * @deprecated
	 */
	List<AttributeType> getAttributeTypes();

	List<Car> getCarsForUser(User usr);
	
	/**
	 * This method fetches list of CARS assigned to the given user.
	 * @param usr
	 * @return
	 */
	
	List<CarsDTO> getDashboardCarsForUser(CarSearchCriteria criteria,User usr);
	
	List<ChangeType> getChangeTypes();

	List<NoteType> getNoteTypes();

	List<SampleType> getSampleTypes();

	List<ShippingType> getShippingTypes();

	List<SourceType> getSourceTypes();
	
	WorkFlow getWorkFlowByName(String workFlowName);
	
	User saveUser(User user);
	
	List<Car> getAllCars();
	
	/**
	Method for archive and delete CARS enhancement
    */
	List<Car> getCarsByIds(String ids);
	
	List<VendorSku> getSkusForStyle(VendorStyle vs);
	
	List<Car> searchCars(CarSearchCriteria criteria);
	
	List<DepartmentAttribute> getAllDepartmentAttributes(long deptId);
	
	List<ClassAttribute> getAllClassificationAttributes(long classId);
	
	List<CarAttribute> getAllCarAttribute(long carId);
	
	void remove(Image image) ;

	void remove(CarSkuAttribute carSkuAttr) ;
	
	List<NotificationUserDTO> getCarNotificationList(boolean isVendor);
	
	List<NotificationUserDTO> getVendorCarEscalationList();
	
	List<NotificationUserDTO> getVendorSampleEscalationList();
	
	CarAttribute updateCarAttributeValue(CarAttribute carAttr);
	
	void updateAttributeValueProcessStatuses(long carId);

    public Car updateCar(Car car, String updatedBy);
    
    /**Method for archive and delete operation*/
    public void updateCars(String query) throws Exception;
    
    public void unlockCar(long carId) throws Exception;
    
    public void unlockCar(String userEmail) throws Exception;
    
    public List<DetailNotificationUserDTO> getGeneratedCarList(boolean isVendor);

    public List<CarUserVendorDepartment> getVendorDepartments(Long vendorId);
    
    public List<CarUserVendorDepartment> getVendorsByDeptId(Long id);
    
    public void deleteVendorDepartment(Long id);
    
    public void saveVendorDepartment(CarUserVendorDepartment cvd);

    public CarAttribute getCarAttributeByBMName(Car c, String strBMName);
    
    // added for the Dropship Phase2 
    public Attribute getAttributeByName(String attrName);

	public List<Object> searchCarsForLastSearchReport(CarSearchCriteria criteria);

	public List<String> getCollectionSkusByVS(VendorStyle vs);
	
	public List<String> getDBPromotionCollectionSkusByVS(VendorStyle vs);

	public void savePOUnitdetails(List<PoUnitDetail> poUnitDetailList);
    
	public List<VendorSku> getSkusForStyleColorCd(long vendorStyleId,String colorCd,Car car)throws Exception;
	
	public CarReopenPetDetails getCarReopenPetDetails(long orin);
	
	public CarReopenPetDetails getCarReopenPetDetails(String vendorNumber,String vendorStyleNUmber);
	
	public void saveCarReopenPetDetails(CarReopenPetDetails reopnPetDetails);

	public void loadChildCarImagesintoPatternCAR(Car car, Car patternCar); 
}

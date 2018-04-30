package com.belk.car.app.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;

import com.belk.car.app.Constants;
import com.belk.car.app.dto.PackDTO;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarReopenPetDetails;
import com.belk.car.app.model.CarSample;
import com.belk.car.app.model.CarsPimItemUpdate;
import com.belk.car.app.model.ClosedCarAttrSync;
import com.belk.car.app.model.Config;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.workflow.WorkflowStatus;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.PIMAttributeManager;
import com.belk.car.app.service.PIMFtpImageManager;
import com.belk.car.app.service.impl.QuartzJobManagerForPIMJobsImpl;
import com.belk.car.product.integration.response.data.AttributeData;
import com.belk.car.product.integration.response.data.IntegrationResponseData;

public class UpdateAttributesFromPIMThread implements Runnable {
    
    private CarManager carManager;
    private CarLookupManager carLookupManager;
    private PIMAttributeManager pimAttributeManager;
    private PIMFtpImageManager pimFtpImageManager;
    
    private User systemUser=null;
    private transient final Log log = LogFactory
            .getLog(QuartzJobManagerForPIMJobsImpl.class);

    private List<CarsPimItemUpdate> itemsUpdated;
    private List<CarsPimItemUpdate> failedItems;
    private boolean isPetUpdated = false;

    private UserType buyer;
    
    public UpdateAttributesFromPIMThread(CarManager carManager, CarLookupManager carLookupManager,
            PIMAttributeManager pimAttributeManager, PIMFtpImageManager pimFtpImageManager, User systemUser,
            List<CarsPimItemUpdate> itemsUpdated, List<CarsPimItemUpdate> failedItems) {
        super();
        this.carManager = carManager;
        this.carLookupManager = carLookupManager;
        this.pimAttributeManager = pimAttributeManager;
        this.pimFtpImageManager = pimFtpImageManager;
        this.systemUser = systemUser;
        this.itemsUpdated = itemsUpdated;
        this.failedItems = failedItems;
    }

    @Override
    public void run() {
        if (log.isInfoEnabled()) {
            log.info("Starting Thread!  Number of records to process: " + itemsUpdated.size());
        }
        
        if (itemsUpdated == null || itemsUpdated.isEmpty()) {
            if (log.isInfoEnabled()) {
                log.info("Thread Finished!  Nothing to process.");
            }
            return; // nothing to process
        }
        buyer = (UserType) this.carLookupManager.getById(UserType.class, UserType.BUYER);
        Iterator<CarsPimItemUpdate> itemsUpdatedItr = itemsUpdated.iterator();
        while (itemsUpdatedItr.hasNext()) {
            CarsPimItemUpdate itemUpdate = (CarsPimItemUpdate) itemsUpdatedItr.next();
            if (itemUpdate == null) {
                if (log.isInfoEnabled()) {
                    log.info("Invalid CarsPimItemUpdate record!");
                }
                synchronized (failedItems) {
                    failedItems.add(itemUpdate);
                }
                continue;
            }
            if (CarsPimItemUpdate.FLAG_YES.equals(itemUpdate.getMerchHierarchyFlag())) {
                if (log.isInfoEnabled()) {
                    log.info("Only Dept/Class update.  Ignoring ItemId: " + itemUpdate.getId());
                }
                synchronized (failedItems) {
                    failedItems.add(itemUpdate);
                }
                continue; //ignore update if this is the only change.
            }
            
            String itemType = itemUpdate.getItemType();
            
            boolean success = false;
            if (CarsPimItemUpdate.TYPE_SKU.equalsIgnoreCase(itemType)) {
                success = processAttributesAndImagesForSku(itemUpdate);
            }
            else {
                success = processAttributesAndImagesForItem(itemUpdate,itemType);
            }
            if (!success) {
                synchronized (failedItems) {
                    failedItems.add(itemUpdate);
                }
            }
        }
        if (log.isInfoEnabled()) {
            log.info("Thread Finished!");
        }
    }

    /**
     * Update the attributes and images for all items from PIM.  
     * 
     * For STYLE_COLOR & PACK_COLOR types, only images are updated.
     * 
     * @param itemsUpdated,
     *            the list of items to process.
     * @param itemType,
     *            the type of update item.
     *            ex.) STYLE, PACK, STYLE_COLOR, or PACK_COLOR
     * @return failedItems,
     *            the list of items that failed to process.
     * @throws Exception 
     */
    private boolean processAttributesAndImagesForItem(CarsPimItemUpdate itemUpdate,
            String itemType) {
        boolean isSuccess = false;
        VendorStyle vendorStyle = getVendorStyleForItem(itemUpdate);
        if (vendorStyle == null) {
            if (log.isInfoEnabled()) {
                log.info("No VendorStyle found for OrinNumber && VendorNumber/VendorStyleNumber: "
                        + itemUpdate.getOrin() + ", " + itemUpdate.getVendorNumber() + "/"
                        + itemUpdate.getVendorStyleNumber());
            }
            return isSuccess;
        }

        List<Car> cars = carManager.getAllCarForStyle(vendorStyle.getVendorStyleId());
        if(cars!=null && !cars.isEmpty()){
            for(Car car : cars){
                isSuccess = processItemUpdate(itemUpdate, car);
                if(!isSuccess){
                    return isSuccess;
                }
            }
            
        } else {
            try {
                if (log.isInfoEnabled()) {
                    log.info("No CAR found for record: " + itemUpdate.getId());
                }
                if (vendorStyle.getParentVendorStyle() != null) {
                    List<Car> patternCars = carManager.getActiveCarsByParentVendorStyle(vendorStyle.getParentVendorStyle()
                            .getVendorStyleId());
                    Car newCar = null;
                    for (Car patternChildCar : patternCars) {
                        
                        if(itemType.equalsIgnoreCase(CarsPimItemUpdate.TYPE_STYLE_COLOR)
                                || itemType.equalsIgnoreCase(CarsPimItemUpdate.TYPE_PACK_COLOR)){
                            patternChildCar = carManager.getCarById(patternChildCar.getCarId());
                            //String style=itemUpdate.getOrin().substring(0, itemUpdate.getOrin().length() - 3);
                               String colorCode=itemUpdate.getOrin().substring(itemUpdate.getOrin().length() - 3, itemUpdate.getOrin().length());
                            if (!patternChildCar.getCarSamples().isEmpty()) {
                                Iterator<CarSample> iterator = patternChildCar.getCarSamples().iterator();
                                while (iterator.hasNext()) {
                                    
                                    CarSample carSample = (CarSample) iterator.next();
                                        if (carSample !=null &&
                                                carSample.getSample() !=null && carSample.getSample().getVendorStyle().getVendorStyleNumber()
                                                .equalsIgnoreCase(itemUpdate.getVendorStyleNumber()) 
                                                && carSample.getSample().getSwatchColor() !=null 
                                                && carSample.getSample().getSwatchColor().equalsIgnoreCase(colorCode)
                                                ) {
                                            newCar = patternChildCar;
                                            break ;
                                        }
                                }
                                if(newCar!=null){
                                    break;
                                }

                            }
                        }else{
                            List<VendorStyle> vendorStyles = patternChildCar.getVendorStyles();
                            for (VendorStyle vs : vendorStyles) {
                                if (vs!=null && vs.getVendorStyleId()==vendorStyle.getVendorStyleId()) {
                                    newCar = patternChildCar;
                                    break;
                                }
                            }
                            if (newCar!=null) {
                                break;
                            }
                        }
                    }
                        
                    if (newCar == null) {
                        return isSuccess;
                    }
                    isSuccess = processItemUpdate(itemUpdate, newCar);
                    if(!isSuccess){
                        return isSuccess;
                    }
                } else {
                    return isSuccess;
                }
            }catch (Exception e) {
                if (log.isErrorEnabled()) {
                    log.error("Exception while finding car for style: "
                            +itemUpdate.getVendorStyleNumber()+" orin "+itemUpdate.getOrin()+
                            " vendor number "+itemUpdate.getVendorNumber(),e);
                }
                return isSuccess;
           }
        }
        
        return isSuccess;
    }

    /**
     * Method to perform business logic based on the pet updated time stamp and post cutover cars.
     * For STYLE_COLOR & PACK_COLOR 
     *           Update image is called only if pet is updated.Car is reopened if its currently closed.
     * For STYLE,PACK
     *           New getItem is invoked and attributes are updated. If the pet is not changed and 
     *           the car status is closed, then it is reopened.           
     * 
     * @param itemUpdate
     * @param itemType
     * @param car
     */
    private boolean processItemUpdate(CarsPimItemUpdate itemUpdate,
            Car car) {
    	String imageUpdate;
        if (ignoreClosedCar(car)){
            if (log.isInfoEnabled()) {
                log.info("PIM_UPDATE_IGNORE_CLOSED_CARS killswitch is enabled. "
                        + "Ignoring closed Car ("+car.getCarId()+").");
            }
            return false;
        }

        String orin = itemUpdate.getOrin();
        String itemType = itemUpdate.getItemType();
        CarReopenPetDetails petDetails = carManager.getCarReopenPetDetails(Long.valueOf(orin).longValue());
        if(petDetails==null){
            petDetails = carManager.getCarReopenPetDetails(itemUpdate.getVendorNumber(),itemUpdate.getVendorStyleNumber());
        }
        Date petCreateDt = new Date();
        if(petDetails!=null){
            petCreateDt = petDetails.getLastPetUpdateTimeStamp();
        }
        Date petUpdateDt = itemUpdate.getPetUpdatedTimestamp();
        if(petCreateDt!=null && petUpdateDt!=null && petUpdateDt.after(petCreateDt)){
            isPetUpdated = true;
        }

        if(log.isInfoEnabled()){
            log.info("processItemUpdate - UpdateAttributesFromPIMThread : " +" orin - " +orin +" itemType- "
                    +itemType +" petCreateDt - "+ petCreateDt +" petUpdateDt : " +petUpdateDt
                            +" isPetUpdated : " +isPetUpdated);
        }
        if (carManager.isPostCutoverCar(car)) {
            if (itemType.equalsIgnoreCase(CarsPimItemUpdate.TYPE_STYLE_COLOR)
                        || itemType.equalsIgnoreCase(CarsPimItemUpdate.TYPE_PACK_COLOR)) {
                 imageUpdate=updateImagesForCar(car);
                if(isPetUpdated && WorkflowStatus.CLOSED.equals(car.getCurrentWorkFlowStatus().getStatusCd()) &&
                        Constants.TRUE.equalsIgnoreCase(imageUpdate)){
                    updateStatusForPostCutoverCar(car, itemUpdate);
                }
                if(petDetails!=null && petUpdateDt!=null){
                    petDetails.setLastPetUpdateTimeStamp(petUpdateDt);
                    carManager.saveCarReopenPetDetails(petDetails);
                }
                
            }
            //car is closed and pet is not updated
            if (WorkflowStatus.CLOSED.equals(car.getCurrentWorkFlowStatus().getStatusCd())) {
                if (log.isInfoEnabled()){
                    log.info("car is closed and pet is not updated - processItemUpdate- UpdateAttributesFromPIMThread:"
                            +" carId: "+car.getCarId());
                }
                // Create the ClosedCarAttrSync record before calling Attribute Sync from PIM
                ClosedCarAttrSync closedCar = createClosedCarAttrSync(car);
                
                try {
                    if (closedCar!=null) {
                        carManager.createOrUpdateClosedCarAttrSync(closedCar);
                    }
                } catch (Exception e) {
                    if(log.isErrorEnabled()){
                        log.error("Exception while creating ClosedCar Attr Sync record");
                    }
                    return false;
                }
                if (itemType.equalsIgnoreCase(CarsPimItemUpdate.TYPE_STYLE)) {
                    boolean success = updateAttributesForStyleUpdate(itemUpdate.getOrin(), car);
                    if (!success) {
                        return false;
                    }
                }
                if (itemType.equalsIgnoreCase(CarsPimItemUpdate.TYPE_PACK)) {
                    boolean success = updateAttributesForPackUpdate(createPackForItemUpdate(itemUpdate), car);
                    if (!success) {
                        return false;
                    }
                }

                
            }else if(WorkflowStatus.CLOSED.equals(car.getCurrentWorkFlowStatus().getStatusCd()) && isPetUpdated){//car is closed and pet is updated
                if (itemType.equalsIgnoreCase(CarsPimItemUpdate.TYPE_STYLE)) {
                    boolean success = updateAttributesForStyleUpdate(itemUpdate.getOrin(), car);
                    if (!success) {
                        return false;
                    }
                }
                if (itemType.equalsIgnoreCase(CarsPimItemUpdate.TYPE_PACK)) {
                    boolean success = updateAttributesForPackUpdate(createPackForItemUpdate(itemUpdate), car);
                    if (!success) {
                        return false;
                    }
                }
                //reopening the car
                updateStatusForPostCutoverCar(car, itemUpdate);
                if(petDetails!=null && petUpdateDt!=null){
                    petDetails.setLastPetUpdateTimeStamp(petUpdateDt);
                    carManager.saveCarReopenPetDetails(petDetails);
                }
                
                
            }else {// car is open
                if (itemType.equalsIgnoreCase(CarsPimItemUpdate.TYPE_STYLE)) {
                    boolean success = updateAttributesForStyleUpdate(itemUpdate.getOrin(), car);
                    if (!success) {
                        return false;
                    }
                }
                if (itemType.equalsIgnoreCase(CarsPimItemUpdate.TYPE_PACK)) {
                    boolean success = updateAttributesForPackUpdate(createPackForItemUpdate(itemUpdate), car);
                    if (!success) {
                        return false;
                    }
                }
            }
            
        } else {
            // if car is pre-cutover, ignore.
            if (log.isInfoEnabled()) {
                log.info("CAR (id: " + car.getCarId() + ") is pre-cutover for item record (" 
                        + itemUpdate.getId() + ").  Ignored.");
            }
        }
        return true;
    }

    /**
     * Update the attributes and images for all SKU items from PIM.  Logic for 
     * SKU items varies from Style, StyleColor, Pack, & PackColor items. There is 
     * additional logic added for pre-cutover cars.
     * 
     * @param itemsUpdated,
     *            the list of items to process.
     * @throws Exception 
     */
    private boolean processAttributesAndImagesForSku(CarsPimItemUpdate itemUpdate) {
        VendorSku vendorSku = getVendorSkuForItem(itemUpdate);
        if (vendorSku == null) {
            if (log.isInfoEnabled()) {
                log.info("No VendorSku found for OrinNumber && belkUpc: " + itemUpdate.getOrin() 
                    + ", " +itemUpdate.getBelkUpc());
            }
            return false;
        }
        Car car = vendorSku.getCar();
        if (car == null) {
            if (log.isInfoEnabled()) {
                log.info("No CAR found for record: " + itemUpdate.getId());
            }
            return false;
        }
        if (ignoreClosedCar(car) && isNullForAllFlags(itemUpdate)) {
            if (log.isInfoEnabled()) {
                log.info("PIM_UPDATE_IGNORE_CLOSED_CARS killswitch is enabled. "
                        + "Ignoring closed Car ("+car.getCarId()+").");
            }
            return false; // if killswitch enabled, all flags are null, & car is closed, ignore.
        }
        if (carManager.isPostCutoverCar(car)) {

            if (WorkflowStatus.CLOSED.equals(car.getCurrentWorkFlowStatus().getStatusCd())) {
                // Create the ClosedCarAttrSync record before calling Attribute Sync from PIM
                ClosedCarAttrSync closedCar = createClosedCarAttrSync(car);
                
                try {
                    if (closedCar!=null) {
                        carManager.createOrUpdateClosedCarAttrSync(closedCar);
                    }
                } catch (Exception e) {
                    if(log.isErrorEnabled()){
                        log.error("Exception while creating ClosedCar Attr Sync record");
                    }
                }
                boolean success = updateAttributesForSkuUpdate(itemUpdate.getOrin(), car);
                if (!success) {
                    return false;
                }

            }
            else {
                boolean success = updateAttributesForSkuUpdate(itemUpdate.getOrin(), car);
                if (!success) {
                    return false;
                }
            }
            
        } else {
            // for precutover cars with dropship value, only dropship 
            // and online_only attribute should be updated.
            if (WorkflowStatus.CLOSED.equals(car.getCurrentWorkFlowStatus().getStatusCd())
                    && (Constants.TRUE.equalsIgnoreCase(itemUpdate.getDropshipUpdate())
                            || Constants.FALSE.equalsIgnoreCase(itemUpdate.getDropshipUpdate()))) {
                // If there is change to dropship and online_only attribute,
                // create the ClosedCarAttrSync record.
                ClosedCarAttrSync closedCar = createClosedCarAttrSync(car);
                
                try {
                    if (closedCar!=null) {
                        carManager.createOrUpdateClosedCarAttrSync(closedCar);
                    }
                } catch (Exception e) {
                    if(log.isErrorEnabled()){
                        log.error("Exception while creating ClosedCar Attr Sync record");
                    }
                }
            }
            
            if (Constants.TRUE.equalsIgnoreCase(itemUpdate.getDropshipUpdate())) {
                boolean success = pimAttributeManager.updateDropshipAttributeForSku(vendorSku, Constants.VALUE_YES, this.systemUser);
                if (!success) {
                    return false;
                }
            }
            else if (Constants.FALSE.equalsIgnoreCase(itemUpdate.getDropshipUpdate())) {
                boolean success = pimAttributeManager.updateDropshipAttributeForSku(vendorSku, Constants.VALUE_NO, this.systemUser);
                if (!success) {
                    return false;
                }
            }
            else {
                // if car is pre-cutover && dropship indicator is null, ignore.
                if (log.isInfoEnabled()) {
                    log.info("CAR (id: " + car.getCarId() + ") is pre-cutover && "
                                    + "DROP_SHIP_UPDATE is null or invalid.  Ignored.");
                }
                return false;
            }
        }
        return true;
    }
    
    /**
     * Create PackDTO object for given itemUpdate record
     * @param itemUpdate
     * @return PackDTO object
     */
    private PackDTO createPackForItemUpdate(CarsPimItemUpdate itemUpdate) {
        PackDTO packDto = new PackDTO();
        packDto.setVendorNumber(itemUpdate.getVendorNumber());
        packDto.setVendorStyleNumber(itemUpdate.getVendorStyleNumber());
        return packDto;
    }
    
    /**
     * Based on the itemUpdate flags & car's current status, update status for post-cutover car.
     * @param car
     * @param itemUpdate
     */
    private void updateStatusForPostCutoverCar(Car car, CarsPimItemUpdate itemUpdate) {
        // if item update is from PET, reopen car.
        if (itemUpdate.getItemType().equalsIgnoreCase(CarsPimItemUpdate.TYPE_STYLE_COLOR)
                || itemUpdate.getItemType().equalsIgnoreCase(CarsPimItemUpdate.TYPE_PACK_COLOR)) {
            car.setCurrentWorkFlowStatus((WorkflowStatus) carLookupManager.getById(WorkflowStatus.class,
                    WorkflowStatus.READY_FOR_REVIEW));
            car.setAssignedToUserType(buyer);
            car.setLastWorkflowStatusUpdateDate(new Date());
            carManager.save(car);
        }
    }

    /**
     * Calls new Get Image interface to update Images for List of Car IDs.
     * 
     * @param carIds
     */
    private String updateImagesForCar(Car car) {
        if (car != null) {
            try {
                Map<String,IntegrationResponseData>getImageResponse =
                        pimFtpImageManager.uploadOrDeletePimImagesByCar(car);
                if (null != getImageResponse && !getImageResponse.isEmpty() &&
                        getImageResponse.containsKey("imageChanged")){
                    return getImageResponse.get("imageChanged").getImageChanged();
                }
            } catch (Exception e) {
                if (log.isErrorEnabled()) {
                    log.error("Error while updating images for car_id: " + car.getCarId(),e);
                }
            }
        } else {
            if (log.isInfoEnabled()) {
                log.info("No valid car to update images.  Ignoring Image Sync.");
            }
        }    
        return null;
    }

    /**
     * Calls new Get Item Webservice to update PIM attributes for List of CARs
     * at STYLE level
     * 
     * @param orinNumber,
     *            the orin number used to fetch attribute mapping from
     *            SMART
     * @param car
     *            the car where all attributes should be synced from PIM
     * @return boolean,
     *            true if attribute was successfully updated.  else false.
     */
    private boolean updateAttributesForStyleUpdate(String orinNumber, Car car) {
        try {
            ArrayList<String> orinNumbers = new ArrayList<String>();
            orinNumbers.add(orinNumber);
            Map<Long, List<AttributeData>> attributeMap = 
                    pimAttributeManager.getAdditionalStylesDetailsFromSMART(car, orinNumbers,Constants.PROCESS_UPDATE_ITEM);
            syncPIMAttributes(attributeMap, car, CarsPimItemUpdate.TYPE_STYLE);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Error while updating attributes for styles from SMART for car: " + car.getCarId(),e);
            }
            return false;
        }
        return true;
    }

    /**
     * Calls new Get Item Webservice to update PIM attributes for List of CARs
     * at SKU level. 
     * 
     * @param orinNumbers,
     *            the orin numbers are used to fetch attribute mapping from
     *            SMART
     * @param cars
     * @return boolean,
     *            true if attribute was successfully updated.  else false.
     */
    private boolean updateAttributesForSkuUpdate(String orinNumber, Car car) {
        try {
            ArrayList<String> orinNumbers = new ArrayList<String>();
            orinNumbers.add(orinNumber);
            Map<Long, List<AttributeData>> attributeMap = 
                    pimAttributeManager.getAdditionalSkuDetailsFromSMART(car,orinNumbers,this.systemUser);
            syncPIMAttributes(attributeMap, car, CarsPimItemUpdate.TYPE_SKU);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Error while updating attributes for skus from SMART for car: " + car.getCarId(),e);
            }
            return false;
        }
        return true;
    }
    
    /**
     * Calls new Get Item Webservice to update PIM attributes for List of CARs
     * at PACK level
     * 
     * @param orinNumbers,
     *            the orin numbers are used to fetch attribute mapping from
     *            SMART
     * @param cars,
     *            the cars where all attributes should be synced from PIM
     * @return boolean,
     *            true if attribute was successfully updated.  else false.
     */
    private boolean updateAttributesForPackUpdate(PackDTO pack, Car car) {
        try {
            ArrayList<PackDTO> packs = new ArrayList<PackDTO>();
            packs.add(pack);
            Map<Long, List<AttributeData>> attributeMap = 
                    pimAttributeManager.getAdditionalPackDetailsFromSMART(car, packs,Constants.PROCESS_UPDATE_ITEM);
            syncPIMAttributes(attributeMap, car, "pack");
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Error while updating attributes for packs from SMART for car: " + car.getCarId(),e);
            }
            return false;
        }
        return true;
    }
    
    /**
     * Based on workflow status of car, different sync method from pim is called.
     * 
     * @param attributeMap
     * @param car
     * @param itemType
     * @throws Exception
     */
    private void syncPIMAttributes(Map<Long, List<AttributeData>> attributeMap, Car car, String itemType) throws Exception {
        // for closed cars, call syncAdditionalPIMAttributesForUpdate() to sync only changes.
        if (WorkflowStatus.CLOSED.equals(car.getCurrentWorkFlowStatus().getStatusCd())) {
            pimAttributeManager.syncAdditionalPIMAttributesForUpdate(car, attributeMap,
                    itemType, this.systemUser);
        } else {
            pimAttributeManager.syncAdditionalPIMAttributes(car,attributeMap, 
                    itemType,this.systemUser);
        }
    }

    /**
     * Checks if the kill switch to ignore closed cars is enabled.
     * @return
     */
    private boolean ignoreClosedCarsKillSwitchEnabled() {
        Config config = (Config) carLookupManager.getById(Config.class, Config.PIM_UPDATE_IGNORE_CLOSED_CARS);
        if (config == null) {
            return false;
        }
        return Constants.TRUE.equalsIgnoreCase(config.getValue());
    }

    /**
     * This method creates a ClosedCarAttrSync record.  If record already
     * exist in the database with flag 'N', don't create record.
     * 
     * @param car
     * @param vendorStyle
     * @param vendorSku
     * @return
     */
    private ClosedCarAttrSync createClosedCarAttrSync(Car car) {
        ClosedCarAttrSync closedCar = null;
        try {
            // check if record exists in the database. 
            closedCar = carManager.getClosedCarAttrSyncFromDB(car.getCarId());
            
            if (closedCar!=null && ClosedCarAttrSync.FLAG_NO.equals(closedCar.getJobProcessed())) {
                return null; //don't create record if already exist
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Error while querying from closed_car_attr_sync table for car_id: " + car.getCarId(),e);
            }
        }
        
        //create new record if doesn't exist from db.
        if (closedCar==null) {
            closedCar = new ClosedCarAttrSync();
            closedCar.setCarId(car.getCarId());
        } 
        closedCar.setJobProcessed(ClosedCarAttrSync.FLAG_NO);
        closedCar.setLastUpdatedTimestamp(new Date());
        return closedCar;
    }

    /**
     * This method returns the VendorStyle for the given itemUpdate.
     * 
     * @param itemUpdate
     * @return
     */
    private VendorStyle getVendorStyleForItem(CarsPimItemUpdate itemUpdate) {
        VendorStyle vendorStyle = null;
        try {
            // first attempt to get VendorStyle by orin number, except packs.  Pack don't have orin number.
            if (!CarsPimItemUpdate.TYPE_PACK.equalsIgnoreCase(itemUpdate.getItemType())) {
                String orinString = itemUpdate.getOrin();
                
                // in itemType is STYLE_COLOR, strip the last 3 digits that represent the color code.
                if (CarsPimItemUpdate.TYPE_STYLE_COLOR.equalsIgnoreCase(itemUpdate.getItemType())) {
                    orinString = orinString.substring(0, orinString.length()-3);
                }
                Long orinNumber = new Long(orinString);
                vendorStyle = pimAttributeManager.getLastUpdatedVendorStyleByOrin(orinNumber);
            }
        }
        catch (NumberFormatException e) {
            // do nothing
        }
        if (vendorStyle == null) {
            // attempt to get VendorStyle again if could not find by orin number.
            vendorStyle = carManager.getVendorStyle(itemUpdate.getVendorNumber(),
                    itemUpdate.getVendorStyleNumber());
        }
        return vendorStyle;
    }

    /**
     * This method returns the VendorSku for the given itemUpdate.
     * 
     * @param itemUpdate
     * @return
     */
    private VendorSku getVendorSkuForItem(CarsPimItemUpdate itemUpdate) {
        VendorSku vendorSku = null;
        try {
            // first attempt to get VendorSku by orin number.
            Long orinNumber = new Long(itemUpdate.getOrin());
            vendorSku = pimAttributeManager.getVendorSkuByOrin(orinNumber);
        }
        catch (NumberFormatException e) {
            // do nothing
        }
        if (vendorSku == null) {
            // attempt to get VendorSku again if could not find by orin number.
            vendorSku = carManager.getActiveCarSkus(itemUpdate.getBelkUpc());
        }
        return vendorSku;
    }

    /**
     * This method returns true if the IgnoreClosedCar flag is enabled &&
     * the given car is closed.
     * 
     * @param car
     * @return
     */
    private boolean ignoreClosedCar(Car car) {
        if (ignoreClosedCarsKillSwitchEnabled() &&
                WorkflowStatus.CLOSED.equals(car.getCurrentWorkFlowStatus().getStatusCd())) {
            return true;
        }
        return false;
    }
    
    /**
     * This method returns true if all the flags for the given itemUpdate
     * are set to null.
     * 
     * @param itemUpdate
     * @return
     */
    private boolean isNullForAllFlags(CarsPimItemUpdate itemUpdate) {
        if (itemUpdate.getDropshipUpdate()==null ||
                itemUpdate.getMerchHierarchyFlag()==null) {
            return true; 
        }
        return false;
    }
}
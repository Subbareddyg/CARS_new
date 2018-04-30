package com.belk.car.app.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.belk.car.app.Constants;
import com.belk.car.app.dao.OutfitDao;
import com.belk.car.app.dao.PatternAndCollectionDao;
import com.belk.car.app.dto.StyleAndStyleColorDTO;
import com.belk.car.app.exceptions.GroupCreateException;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.AttributeValueProcessStatus;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarOutfitChild;
import com.belk.car.app.model.CarSearchCriteria;
import com.belk.car.app.model.CarsPimGroupMapping;
import com.belk.car.app.model.Classification;
import com.belk.car.app.model.CollectionSkus;
import com.belk.car.app.model.ContentStatus;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.GroupCreateMsgFailure;
import com.belk.car.app.model.HtmlDisplayType;
import com.belk.car.app.model.OutfitCarAttribute;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.model.SourceType;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.VendorStyleSearchCriteria;
import com.belk.car.app.model.VendorStyleType;
import com.belk.car.app.model.WorkFlow;
import com.belk.car.app.model.workflow.WorkflowStatus;
import com.belk.car.app.model.workflow.WorkflowType;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.OutfitManager;
import com.belk.car.app.service.PIMAttributeManager;
import com.belk.car.app.service.PIMFtpImageManager;
import com.belk.car.app.service.PatternAndCollectionManager;
import com.belk.car.app.service.WorkflowManager;
import com.belk.car.integrations.pim.xml.GroupingComponent;
import com.belk.car.integrations.pim.xml.GroupingParent;
import com.belk.car.util.DateUtils;

public class PatternAndCollectionManagerImpl implements PatternAndCollectionManager {
    
    private PatternAndCollectionDao patternCollectionDao;
    private CarManager carManager;
    private CarLookupManager carLookupManager;
    private OutfitDao outfitDao;
    private OutfitManager outfitManager;
    private WorkflowManager workflowManager;
    private PIMAttributeManager pimAttributeManager;
    private PIMFtpImageManager pimFtpImageManager;
    
    private AttributeValueProcessStatus checkRequired = null;
    private AttributeValueProcessStatus noCheckRequired = null;
    
    private transient final Log log = LogFactory
            .getLog(PatternAndCollectionManagerImpl.class);
    
    public void setContextParameters() {
        if (this.checkRequired==null) {
            this.checkRequired = carLookupManager.getAttributeValueProcessStatus(AttributeValueProcessStatus.CHECK_REQUIRED);
        }
        if (this.noCheckRequired==null) {
            this.noCheckRequired = carLookupManager.getAttributeValueProcessStatus(AttributeValueProcessStatus.NO_CHECK_REQUIRED);
        }
    }
    
    public void setPatternCollectionDao(PatternAndCollectionDao patternCollectionDao) {
        this.patternCollectionDao = patternCollectionDao;
    }

    public void setCarManager(CarManager carManager) {
        this.carManager = carManager;
    }

    public void setCarLookupManager(CarLookupManager carLookupManager) {
        this.carLookupManager = carLookupManager;
    }

    public void setOutfitDao(OutfitDao outfitDao) {
        this.outfitDao = outfitDao;
    }


    public void setOutfitManager(OutfitManager outfitManager) {
        this.outfitManager = outfitManager;
    }

    public void setWorkflowManager(WorkflowManager workflowManager) {
        this.workflowManager = workflowManager;
    }

    public void setPimAttributeManager(PIMAttributeManager pimAttributeManager) {
        this.pimAttributeManager = pimAttributeManager;
    }

    public void setPimFtpImageManager(PIMFtpImageManager pimFtpImageManager) {
        this.pimFtpImageManager = pimFtpImageManager;
    }

    /**
     * This method will create the following objects associated with a pattern from PIM:
     *   1. VendorStyle
     *   2. Vendor (if does not exist)
     *   3. Parent-child relationship (via VendorStyle object)
     *   4. Car (including attribute & image updates)
     */
    @Override
    public void populateAndSavePattern(GroupingParent parent, List<GroupingComponent> children, 
            String vendorStyleType, VendorStyle existingVS, User user) throws GroupCreateException {
        if (!atLeast1ChildComponentHasCar(children, vendorStyleType)) {
            if (log.isInfoEnabled()) {
                log.info("No Child CAR exists for any of the child component(s)!!!  Ignoring style_number:" + parent.getId());
            }
            List<String> styleOrins = new ArrayList<String>();
            for (GroupingComponent child : children) {
                styleOrins.add(getStyleOrinFromChildComponent(child));
            }
            throw new GroupCreateException("NO_CHILD_CARS_FOUND!", styleOrins);  //do not create Pattern if there are no children cars.
        }
        try {

            if (existingVS == null) {
                VendorStyle parentVS = generateParentVendorStyle(parent, vendorStyleType, existingVS, user);
                if (parentVS==null) {
                    if (log.isWarnEnabled()) {
                        log.warn("Could not create pattern vendor_style due to invalid class_number/dept_cd!  Style: " + parent.getVendorStyle());
                        return;
                    }
                }
                carManager.saveOrUpdate(parentVS);
                addChildrenToPattern(parentVS, children, vendorStyleType, user, false);
            } else {
                // update name/desc if changed
                syncGroupingVendorStyle(existingVS, parent);
                // update group type if changed
                syncGroupingType(existingVS, parent, user);
                addChildrenToPattern(existingVS, children, vendorStyleType, user, false);
            }
        }
        catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.info("Error occurred while creating pattern!!!", e);
            }
        }
    }

    /**
     * This method will create the following objects associated with a collection from PIM:
     *   1. VendorStyle
     *   2. Vendor (if does not exist)
     *   3. Parent-child relationship (via CarOutfitChild object)
     *   4. CollectionSkus (to store the selected colors to include in the collection)
     *   4. Car (including attribute & image updates)
     */
    @Override
    public void populateAndSaveCollection(GroupingParent parent, List<GroupingComponent> children, String vendorStyleType, User user) throws GroupCreateException {
        if (!hasMandatoryCollectionAttributes(parent)) {
            if (log.isInfoEnabled()) {
                log.info("Mandatory Collection Attribute(s) missing!!!  Ignoring.");
            }
            return; //do not create Collection if mandatory attributes are missing.
        }
        if (!atLeast1ChildComponentHasCar(children, vendorStyleType)) {
            if (log.isInfoEnabled()) {
                log.info("No Child CAR exists for any of the child component(s)!!!  Ignoring.");
            }
            List<String> styleOrins = new ArrayList<String>();
            for (GroupingComponent child : children) {
                styleOrins.add(getStyleOrinFromChildComponent(child));
            }
            throw new GroupCreateException("NO_CHILD_CARS_FOUND!", styleOrins);  //do not create Collection if there are no children cars.
        }
        try {
            Date effectiveStartDate = getDateFromString(parent.getEffectiveStartDate());
            if (effectiveStartDate==null) {
                if (log.isInfoEnabled()) {
                    log.info("Effective_Start_Date format is invalid!!!  Must be in format: " 
                            + Constants.PIM_DATE_FORMAT + ".  Ignoring.");
                }
                return; //do not create Collection if date format is invalid.
            }
            
            /* create outfit car*/
            VendorStyle parentVS = generateParentVendorStyle(parent, vendorStyleType, null, user);
            carManager.saveOrUpdate(parentVS);
            Date expectedShipDate = getDateFromString(parent.getEffectiveShipDate());
            Car outfitCar = createOutfitCar(parentVS, parent.getDeptCd(), expectedShipDate, user);
            if (outfitCar != null) {
                VendorStyle defaultChild = null;
                Object[] returnValue = addChildrenToCollection(outfitCar, children, user, false);
                
                // add the mandatory collection attributes
                if (returnValue[0]!=null) {
                    addCarAttribute(outfitCar, Constants.OUTFIT_CHILD_PRODUCTS, (String) returnValue[0], user);
                }
                addCarAttribute(outfitCar, Constants.COLLECTION_OUTFIT_TYPE, Constants.ATTR_COLLECTION, user);
                addCarAttribute(outfitCar, Constants.COLLECTION_IS_PRODUCT_SEARCHABLE, Constants.FLAG_N, user);
                addCarAttribute(outfitCar, Constants.COLLECTION_EFFECTIVE_DATE,
                        DateUtils.formatDate(effectiveStartDate, "MM/dd/yyyy"), user);

                if (returnValue[1]!=null) {
                    defaultChild = (VendorStyle) returnValue[1];
                }
                if (defaultChild==null) {
                    // For outfits, just pick one of the children if defaultChild is still null.
                    CarOutfitChild carOutfitChild = outfitCar.getCarOutfitChild().iterator().next();
                    if (carOutfitChild!=null) {
                        defaultChild = carOutfitChild.getChildCar().getVendorStyle();
                    }
                }
                
                // Finally, update images & attributes from PIM.
                // can only update attributes/images if product-type is defined.
                if (Constants.FLAG_N.equals(outfitCar.getIsProductTypeRequired())) {
                    List<Car> outfitCars = new ArrayList<Car>();
                    outfitCars.add(outfitCar);
                    updateAttributesAndImagesForCar(outfitCars, defaultChild, vendorStyleType, true, user);
                }
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.info("Error occurred while creating collection!!!", e);
            }
        }
    }

    /**
     * This method returns the orin_number from the GroupingComponent.
     * @param childDtl
     * @return
     */
    private String getStyleOrinFromChildComponent(GroupingComponent childDtl) {
        String styleOrin = childDtl.getId();
        if (childDtl.getComponentType().equalsIgnoreCase(Constants.STYLE_COLOR)) {
            styleOrin = StringUtils.substring(childDtl.getId(), 0, 9);
        }
        return styleOrin;
    }
    
    /**
     * This method determines if a pattern is eligible for "merging" cars based
     * on the list of children VendorStyles provided.
     * @param childrenVS
     * @return true if pattern is eligible for merging all cars into single car.
     */
    private boolean isPatternEligibleForMerge(List<VendorStyle> childrenVS) {
        CarSearchCriteria carCriteria = null;
        for (VendorStyle childVS : childrenVS) {
            if (!VendorStyleType.PRODUCT.equals(childVS.getVendorStyleType().getCode())) {
                return false; //cannot merge if one of the children is a Pattern
            }
            
            carCriteria = new CarSearchCriteria();
            carCriteria.setVendorNumber(childVS.getVendorNumber());
            carCriteria.setVendorStyleNumber(childVS.getVendorStyleNumber());
            carCriteria.setWorkflowStatus(WorkflowStatus.CLOSED);
            List<String> statusCriteria = new ArrayList<String>();
            statusCriteria.add(ContentStatus.SENT_TO_CMP);
            statusCriteria.add(ContentStatus.DISABLE_IN_CMP);
            statusCriteria.add(ContentStatus.ENABLE_IN_CMP);
            statusCriteria.add(ContentStatus.RESEND_DATA_TO_CMP);
            statusCriteria.add(ContentStatus.RESEND_TO_CMP);
            carCriteria.setContentStatusList(statusCriteria);
            carCriteria.setStatusCd(Status.ACTIVE);
            carCriteria.setIgnoreUser(true);
            //Search based on the Pattern Child Search
            carCriteria.setSearchChildVendorStyle(false);
            List<Car> cars = carManager.searchCars(carCriteria);
            if (!cars.isEmpty()) {
                return false; //cannot merge if one of the children's car is sent-to-cmp
            }
        }
        return true;
    }

    /**
     * This method "merges" all the cars at the child vendorStyle level into
     * a single car at the parent vendorStyle level.
     * 
     * @param parentVS
     * @return
     */
    private Car mergePattern(Car patternCar, VendorStyle parentVS, List<VendorStyle> childrenVS, User user) {
        
        if (patternCar == null) {
            log.warn("mergePattern called without a specified Car. May cause improper merge into CMP-sent Car.");
        }
        
        // need to edit this to accommodate new merge strategy.
        if (log.isInfoEnabled()) {
            log.info("Merging Pattern for vendor_style_number: " + parentVS.getVendorStyleNumber());
        }
        
        Map<String, VendorSku> skuMap = new HashMap<String, VendorSku>();
        Map<String, VendorSku> skuMapAdd = new HashMap<String, VendorSku>();
        
        if (patternCar != null) {
            for (VendorSku sku: patternCar.getVendorSkus()) {
                skuMap.put(sku.getBelkSku(), sku) ;
            }
        }

        CarSearchCriteria carCriteria;
        List<Car> cars;
        for (VendorStyle childVS : childrenVS) {
            carCriteria = new CarSearchCriteria();
            carCriteria.setVendorNumber(childVS.getVendorNumber());
            carCriteria.setVendorStyleNumber(childVS.getVendorStyleNumber());
            carCriteria.setStatusCd(Status.ACTIVE); 
            List<String> statuses = new ArrayList<String>();
            statuses.add(ContentStatus.APPROVAL_REQUESTED);
            statuses.add(ContentStatus.APPROVED);
            statuses.add(ContentStatus.IN_PROGRESS);
            statuses.add(ContentStatus.PUBLISHED);
            statuses.add(ContentStatus.REJECTED);
            carCriteria.setContentStatusList(statuses);
            carCriteria.setIgnoreUser(true);
            //Search based on the Pattern Child Search
            carCriteria.setSearchChildVendorStyle(false);
            cars = carManager.searchCars(carCriteria);
            
            if (cars != null && !cars.isEmpty()) {
                for (Car car : cars) {
                    //If there is still no car assigned to the pattern, then assign the first car to the pattern
                    //Update the VendorStyle for the CAR to the Pattern CAR
                    if (patternCar == null) {
                        patternCar = car;
                        patternCar.setVendorStyle(parentVS);
                        for (VendorSku sku: patternCar.getVendorSkus()) {
                            skuMap.put(sku.getBelkSku(), sku) ;
                        }
                    } else {
                        //Otherwise, delete all the children's cars
                        car.setStatusCd(Status.DELETED);
                        car.setAuditInfo(user);
                        carManager.saveOrUpdate(car);
                        
                        if(car.getCarImages()!=null && !car.getCarImages().isEmpty() ) {
                            //Re-point all the uploaded CAR images into parent car_id
                            carManager.loadChildCarImagesintoPatternCAR(car, patternCar);
                        }
                        
                        //In addition, move the SKUs to the patternCar
                        Set<VendorSku> skus = car.getVendorSkus();
                        for (VendorSku sku : skus) {
                            if (!skuMap.containsKey(sku.getBelkSku()) && !skuMapAdd.containsKey(sku.getBelkSku())) {
                                skuMapAdd.put(sku.getBelkSku(), sku) ;
                            }
                        }
                    }
                }
            }
        }
        
        if (patternCar != null) {
            if (parentVS.getProductType()==null) {
                patternCar.setIsProductTypeRequired(Constants.FLAG_Y);
            }
            else {
                patternCar.setIsProductTypeRequired(Constants.FLAG_N);
            }
            if (!skuMapAdd.isEmpty() || !skuMap.isEmpty()) {
                if (!skuMapAdd.isEmpty()) {
                    for (VendorSku sku : skuMapAdd.values()) {
                        sku.setCar(patternCar) ;
                        patternCar.getVendorSkus().add(sku) ;
                    }
                }
                //Clear Car Samples
                patternCar.getCarSamples().clear();
                
                if (patternCar.getContentStatus().getCode().equals(ContentStatus.SENT_TO_CMP)) {
                    patternCar.setContentStatus((ContentStatus) carLookupManager.getById(ContentStatus.class, ContentStatus.RESEND_TO_CMP));
                }
                patternCar.setAuditInfo(user);
                carManager.saveOrUpdate(patternCar);
            }
            if (log.isInfoEnabled()) {
                log.info("Car created for merged pattern: " + patternCar.getCarId());
            }
      }
      return patternCar;
    }

    /**
     * This method "converts" all the cars at the child vendorStyle level
     * to reference the parent vendorStyle.
     * @param parentVS
     * @return
     */
    private List<Car> convertPattern(VendorStyle parentVS, List<VendorStyle> childrenVS, User user) {
        if (log.isInfoEnabled()) {
            log.info("Converting Pattern for grouping Id: " + parentVS.getGroupId());
        }
        List<Car> childrenCars = new ArrayList<Car>();
        CarSearchCriteria carCriteria = null;
        for (VendorStyle childVS : childrenVS) {
            carCriteria = new CarSearchCriteria();
            carCriteria.setVendorNumber(childVS.getVendorNumber());
            carCriteria.setVendorStyleNumber(childVS.getVendorStyleNumber());
            carCriteria.setStatusCd(Status.ACTIVE);
            carCriteria.setIgnoreUser(true);
            //Search based on the Pattern Child Search
            carCriteria.setSearchChildVendorStyle(false);

            //Search for Vendor Style Cars
            List<Car> cars = carManager.searchCars(carCriteria);
            if (cars != null && !cars.isEmpty()) {
                for (Car car : cars) {
                    if (parentVS.getProductType()==null) {
                        car.setIsProductTypeRequired(Constants.FLAG_Y);
                    }
                    else {
                        car.setIsProductTypeRequired(Constants.FLAG_N);
                        childrenCars.add(car); //only add CARs which have product-type
                    }
                    //Update the VendorStyle for the CAR to Pattern CAR
                    car.setVendorStyle(parentVS);
                    car.setAuditInfo(user);
                    carManager.saveOrUpdate(car);
                    
                    if (log.isInfoEnabled()) {
                        log.info("Car converted for pattern: " + car.getCarId());
                    }
                }
            }
        }
        return childrenCars;
    }

    /**
     * This method triggers the getGroup call for car's pattern vendorStyle 
     * to fetch associated images for the car.
     * @param car
     */
    private void updateImagesByGrouping(Car car) {
        try {
            pimFtpImageManager.uploadOrDeletePimImagesByCarForGroups(car); //update images for pattern car
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Error while updating images for car_id: " + car.getCarId(),e);
            }
        }
    }
    
    /**
     * This method triggers the getItem call for car's style_color to fetch
     * associated images for the car.
     * @param car
     */
    private void updateImagesByStyleColor(Car car) {
        try {
            pimFtpImageManager.uploadOrDeletePimImagesByCar(car);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Error while updating images for car_id: " + car.getCarId(),e);
            }
        }
    }

    /**
     * This method generates the parent vendorStyle object.
     * @param parent
     * @param vendorStyleType
     * @param user
     * @return
     */
    public VendorStyle generateParentVendorStyle(GroupingParent parent, String vendorStyleType, VendorStyle existingVS, User user) {
        if (StringUtils.isBlank(parent.getClassNumber()) || !StringUtils.isNumeric(parent.getClassNumber())) {
            return null; // don't generate vendor_style with invalid data.
        }
        //Have to create Vendor if it does not exist
        Vendor vendor = carManager.getVendor(parent.getVendorNumber()) ;     
        if (vendor == null) {
            vendor = new Vendor();
            vendor.setVendorNumber(parent.getVendorNumber().toUpperCase());
            vendor.setDescr(parent.getVendorNumber().toUpperCase()) ;
            vendor.setStatusCd(Status.ACTIVE) ;
            vendor.setContactEmailAddr(Constants.VENDOR_CONTACT);
            vendor.setAuditInfo(user);
            vendor.setIsDisplayable(Constants.FLAG_Y);
            if (parent.getVendorName()==null) {
                vendor.setName(Constants.VENDOR);
            }
            else {
                vendor.setName(parent.getVendorName()) ;
            }
            vendor = carManager.createVendor(vendor) ;
        }
        
        if (log.isInfoEnabled()) {
            log.info("Creating VendorStyle for grouping Id: " + parent.getId());
        }
        VendorStyle pattern = existingVS; // update existing vendorStyle if exists
        if (pattern==null) {
            pattern = new VendorStyle();
        }
        pattern.setGroupId(Long.parseLong(parent.getId()));
        pattern.setVendor(vendor);
        pattern.setVendorStyleName(parent.getName()) ;
        pattern.setVendorNumber(parent.getVendorNumber().toUpperCase());
        pattern.setVendorStyleNumber(parent.getVendorStyle());
        pattern.setStatusCd(StringUtils.upperCase(parent.getStatus()));
        pattern.setDescr(parent.getDescription());
        VendorStyleType type = (VendorStyleType) carLookupManager.getById(VendorStyleType.class, vendorStyleType);
        pattern.setVendorStyleType(type);
        Classification classification = carManager.getClassification(Short.parseShort(parent.getClassNumber()));
        pattern.setClassification(classification);
        List<ProductType> productTypes = carManager.getProductTypesByClass(Short.parseShort(parent.getClassNumber()));
        if (productTypes.size() == 1) {
            pattern.setProductType(productTypes.get(0));
        }
        pattern.setAuditInfo(user);
        return pattern;
    }

    /**
     * This method generates the child component vendorStyle object.
     * @param component
     * @param parentVS
     * @param user
     * @return
     */
    public VendorStyle generateComponentVendorStyle(GroupingComponent component, VendorStyle parentVS, User user) {
        VendorStyle child = new VendorStyle();
        child.setClassification(parentVS.getClassification());
        child.setProductType(parentVS.getProductType());
        child.setVendor(parentVS.getVendor());
        child.setVendorNumber(component.getVendorNumber());
        child.setVendorStyleNumber(component.getVendorStyle());
        child.setVendorStyleName(Constants.DUMMY_PRODUCT_NAME);
        child.setStatusCd(StringUtils.upperCase(component.getStatus()));
        child.setIsDefault(Constants.FLAG_N);
        
        if ((Constants.STYLE).equalsIgnoreCase(component.getComponentType())
                || Constants.STYLE_COLOR.equalsIgnoreCase(component.getComponentType())) {
            String orin = getStyleOrinFromChildComponent(component);
            child.setOrinNumber(Long.parseLong(orin));
            //set type as "PRODUCT"
            child.setVendorStyleType((VendorStyleType) carLookupManager.getById(VendorStyleType.class, VendorStyleType.PRODUCT));
        }
        else {
            //component is a grouping
            child.setGroupId(Long.parseLong(component.getId()));
            //set type as group
            String carGroupType = getCarGroupTypeByPimGroupType(component.getComponentType(), null);
            if (carGroupType==null) {
                return null; // cannot create component for invalid carGroupType.
            }
            child.setVendorStyleType((VendorStyleType) carLookupManager.getById(VendorStyleType.class, carGroupType));
        }
        child.setAuditInfo(user);
        return child;
    }
    
    /**
     * This method determines the equivalent CARS grouping-type based on the 
     * pimGroupType provided.
     * @param pimGroupType, the groupingType from PIM
     * @param carGroupTypeFromPim, the legacy car group type from PIM if exists
     */
    @Override
    public String getCarGroupTypeByPimGroupType(String pimGroupType, String carGroupTypeFromPim) {
        List<CarsPimGroupMapping> pimGroupMappings = patternCollectionDao.getValidPIMGroupTypes();
        for (CarsPimGroupMapping mapping : pimGroupMappings) {
            if (mapping.getPimGroupName().equals(pimGroupType)) {
            	
            	 if (log.isInfoEnabled()) {
                     log.info("PIM GROUP TYPE: " + pimGroupType);
                 }
                
                if (CarsPimGroupMapping.TYPE_REGULAR_COLLECTION.equals(pimGroupType)) {
                    // if pimGroupType is Regular Collection, get carGroupType from
                    // pim's CARS_Legagcy_groupType.
                    if (mapping.getCarsGroupName().equals(carGroupTypeFromPim)) {
                        return mapping.getCarsGroupName();
                    }
                }
                else {
                    return mapping.getCarsGroupName();
                }
            }
        }
        return null;
    }
    
    /**
     * This method checks if all the collection mandatory attributes were provided.
     * The mandatory collection attributes are:
     *  1. isSearchable
     *  2. effectiveStartDate
     * @param parent
     * @return true if all mandatory collection attributes exist.
     */
    private boolean hasMandatoryCollectionAttributes(GroupingParent parent) {
        String effectiveStartDate = parent.getEffectiveStartDate();
        
        if (StringUtils.isNotBlank(effectiveStartDate) 
                && StringUtils.isNotBlank(parent.getClassNumber())
                && StringUtils.isNumeric(parent.getClassNumber())
                && StringUtils.isNotBlank(parent.getDeptCd())
                && StringUtils.isNumeric(parent.getDeptCd())) {
            return true;
        }
        return false;
    }
    
    /**
     * This method checks if at least one of the children provided have a 
     * car associated with it already & is not assigned to another group for 
     * patterns.
     * @param children, to verify if car exist
     * @param vendorStyleType
     * @return true if at least one child component has existing car.
     */
    private boolean atLeast1ChildComponentHasCar(List<GroupingComponent> children,
            String vendorStyleType) {
        for (GroupingComponent childDtl : children) {
            VendorStyle childVS = getChildVendorStyleIfExists(childDtl);
            if (VendorStyleType.OUTFIT.equals(vendorStyleType)) {
                if (childVS!=null) {
                    Car car = carManager.getCarForStyle(childVS.getVendorStyleId());
                    if (car!=null) {
                        return true;
                    }
                }
            }
            else {
                // verify that child is not assigned to another parent for patterns
                if (childVS!=null && childVS.getParentVendorStyle()==null) {
                    Car car = carManager.getCarForStyle(childVS.getVendorStyleId());
                    if (car!=null) {
                        return true;
                    }
                }
            }
            
        }
        return false;
    }
    
    /**
     * This method gets the existing vendorStyle object from the database
     * for the GroupingComponent.
     * @param childDtl
     * @return
     */
    private VendorStyle getChildVendorStyleIfExists(GroupingComponent childDtl) {
        VendorStyle childVS = null;
        if ((Constants.STYLE).equalsIgnoreCase(childDtl.getComponentType())
                || Constants.STYLE_COLOR.equalsIgnoreCase(childDtl.getComponentType())) {
            String orin = getStyleOrinFromChildComponent(childDtl);
            if (StringUtils.isNumeric(orin)) {
                childVS = pimAttributeManager.getVendorStyleByOrin(Long.parseLong(orin));
            }
            if (childVS == null) {
                if (StringUtils.isNotBlank(childDtl.getVendorNumber()) 
                        && StringUtils.isNotBlank(childDtl.getVendorStyle())) {
                    // attempt to get VendorStyle again if could not find by orin number.
                    childVS = carManager.getVendorStyle(childDtl.getVendorNumber(),
                            childDtl.getVendorStyle());
                }
            }
        }
        else {
            childVS = getVendorStyleIfExists(childDtl.getId(), childDtl.getVendorNumber(),
                    childDtl.getVendorStyle(), childDtl.getProductCode());
        }
        return childVS;
    }
    
    /**
     * This method returns VendorStyle object for given groupID or productcode.
     * @param groupId
     * @param vendorNumber
     * @param vendorStyleNumber
     * @return
     */
    public VendorStyle getVendorStyleIfExists(String groupId, String vendorNumber, 
            String vendorStyleNumber, String productCode) {
        VendorStyle vs = null;
        if (StringUtils.isNumeric(groupId)) {
            vs = patternCollectionDao.getVendorStyleByGroupId(Long.parseLong(groupId));
        }
        if (vs==null) {
            if (StringUtils.isNotBlank(vendorNumber) && StringUtils.isNotBlank(vendorStyleNumber)) {
                vs = patternCollectionDao.getVendorStyle(vendorNumber, vendorStyleNumber);
            }
        }
        if (vs==null) {
            if (StringUtils.isNotBlank(productCode)) {
                vs = patternCollectionDao.getVendorStyleByProductCode(productCode);
            }
        }
        return vs;
    }
    
    /**
     * This method converts the date from a string format to date object.
     *  Expected String format: yyyy-MM-dd
     * @param dateString
     * @return
     */
    private Date getDateFromString(String dateString) {
        if (StringUtils.isBlank(dateString)) {
            return null; // return null by default if argument is blank.
        }
        Date newDate = DateUtils.parseDate(dateString,Constants.PIM_DATE_FORMAT);
        return newDate;

    }
    
    /**
     * This method creates a car object for the outfit product.
     * @param parentVS
     * @param deptCd
     * @param shipDate
     * @param user
     * @return
     */
    private Car createOutfitCar(VendorStyle parentVS, String deptCd, Date shipDate, User user){
        Car car = new Car();
        try{
            //Set OUTFIT car details
            WorkflowStatus initiated = (WorkflowStatus) carLookupManager.getById(WorkflowStatus.class, WorkflowStatus.INITIATED);
            UserType buyer = (UserType) carLookupManager.getById(UserType.class, UserType.BUYER);
            SourceType sourceOutfitCar = carManager.getSourceTypeForCode(SourceType.OUTFIT);
            WorkFlow defaultWorkflow = workflowManager.getWorkFlow(workflowManager.getWorkflowType(WorkflowType.CAR));
            ContentStatus contentInProgress = (ContentStatus) carLookupManager.getById(ContentStatus.class, ContentStatus.IN_PROGRESS);
            Department dept = carManager.getDepartment(deptCd);
            
            car.setVendorStyle(parentVS);
            car.setDepartment(dept);
            car.setSourceType(sourceOutfitCar);
            car.setWorkFlow(defaultWorkflow);
            car.setSourceId(user.getEmailAddress());
            car.setCarUserByLoggedByUserId(user);
            car.setExpectedShipDate(shipDate);
            //for outfit car due date is same as ship date
            car.setDueDate(shipDate);
            car.setEscalationDate(shipDate);
            car.setArchive(Constants.FLAG_NO);
            car.setCurrentWorkFlowStatus(initiated);
            //all ouftit cars are assigned to buyer by default
            car.setAssignedToUserType(buyer);
            car.setStatusCd(Status.ACTIVE);
            if (parentVS.getProductType()==null) {
                car.setIsProductTypeRequired(Constants.FLAG_Y);
            }
            else {
                car.setIsProductTypeRequired(Constants.FLAG_NO);
            }
            car.setIsUrgent(Constants.FLAG_NO);
            car.setContentStatus(contentInProgress);
            car.setLastWorkflowStatusUpdateDate(new Date());
            car.setTurninDate(null);
            car.setArchive(Constants.FLAG_NO);
            car.setLock(Constants.FLAG_NO);
            car.setAuditInfo(user);
            //save outfit car
            carManager.saveOrUpdate(car);  
            
            if (log.isInfoEnabled()) {
                log.info("Car Id created for outfit (style_number: "+parentVS.getVendorStyleNumber()+"): " + car.getCarId());
            }
        } 
        catch(Exception e) {
            if (log.isErrorEnabled()) {
                log.info("Error occurred while creating outfit car!!!", e);
            }
        }
        return car;
    }
    
    /**
     * This method creates the car_outfit_child table entries for all child cars of outfit.
     */
    public CarOutfitChild generateCarOutfitChild(Car outfitCar, Car childCar, String priority, User user){
        try {
            // first check if carOutfitChild already exists for childCar.
            CarOutfitChild outfitChild = null;
            for (CarOutfitChild c : outfitCar.getCarOutfitChild()) {
                if (c.getChildCar().getCarId()==childCar.getCarId()) {
                    outfitChild = c;
                    break;
                }
            }
            
            // else, create new one
            if (outfitChild==null) {
                VendorSku defaultSku = null;
                Set<VendorSku> skuSet = childCar.getVendorSkus();
                if (!skuSet.isEmpty()) {
                    defaultSku = skuSet.iterator().next(); //assign first sku as default
                }
                
                if (defaultSku!=null) {
                    // Add child component to outfit
                    outfitChild = new CarOutfitChild();
                    outfitChild.setDefaultColorSku(defaultSku);
                    outfitChild.setChildCar(childCar);
                    outfitChild.setPriority(Integer.parseInt(priority));
                    outfitChild.setStatusCd(Status.ACTIVE);
                    outfitChild.setAuditInfo(user);
                    outfitChild.setOutfitCar(outfitCar);
                }
            }
            
            return outfitChild;
        }
        catch (NumberFormatException nfe) {
            if (log.isInfoEnabled()) {
                log.info("Failed to create car_outfit_child object for "  + outfitCar.getCarId() 
                    + " (parent car) & " + childCar.getCarId() + " (child car).", nfe);
            }
        } 
        return null;
    }
    
    /**
     * This method add's the appropriate attributes to outfit and child cars.
     * Attribute on outfit cars depends on child cars attributes.
     * Outfit car will have all the attribute of child cars which are marked as 'IS_OUTFIT : Y'.
     * Two child cars can have attribute with same blue martini name (eg: Brand), to maintain this relationship we are using table Outfit_Car_Attribute.
     * For regular car we have only one attribute with one car_attribute however for outfit car we can have multiple attribute with one car_attribute.
     */
    public void addAttributesToOutfit(Car outfitCar, Car childCar, User user) {
        List<CarAttribute> childAttrs = new ArrayList<CarAttribute>();
        Map<String, Set<Attribute>> outfitAttrMap = new HashMap<String, Set<Attribute>>();

        //create the map of is_outfit attributes by attribute_bluemartini_name (eg: Brand)
        childAttrs = outfitDao.getAllOutfitCarAttributes(childCar);
        for(CarAttribute ca: childAttrs){
                String attrBMName = ca.getAttribute().getBlueMartiniAttribute();
                Set<Attribute> attrs = outfitAttrMap.get(attrBMName);
                if(attrs==null){
                    attrs = new HashSet<Attribute>();
                }
                attrs.add(ca.getAttribute());
                outfitAttrMap.put(attrBMName, attrs);
        }
        
        //Create the car_attribute for outfit car
        for(String attrBMName: outfitAttrMap.keySet()) {
            Set<Attribute> attrs = outfitAttrMap.get(attrBMName);
            List<Attribute> attrList = new ArrayList<Attribute>(attrs);
            CarAttribute carAttribute = new CarAttribute();
            //Get the highest priority attribute from attrList, Priority is based on attribute type [eg: checkbox has highest priority]
            List<Attribute> attrPriorityList= prioritizeAttributes(attrList);
            Attribute attribute = attrPriorityList.get(0);
            carAttribute.setAttribute(attribute);
            carAttribute.setCar(outfitCar);

            if (attribute.getAttributeConfig().getHtmlDisplayType().isAutocomplete()) {
                carAttribute.setAttributeValueProcessStatus(checkRequired);
            } else {
                carAttribute.setAttributeValueProcessStatus(noCheckRequired);
            }
            
            // Setting to blank for now
            carAttribute.setAttrValue("");
            carAttribute.setDisplaySeq((short) 0);
            carAttribute.setHasChanged(Constants.FLAG_NO);
            carAttribute.setIsChangeRequired(Constants.FLAG_YES);
            carAttribute.setStatusCd(Status.ACTIVE);
            carAttribute.setAuditInfo(user);
            
            //Each CarAttribute for outfit can have multiple attributes associated to it.
            //Create entry in Outfit_Car_Attribute table for each car-attribute 
            for(Attribute attr : attrList){
                OutfitCarAttribute outfitCarAttr = new OutfitCarAttribute();
                outfitCarAttr.setAttribute(attr);
                outfitCarAttr.setCarAttribute(carAttribute);
                carAttribute.getOutfitCarAttributes().add(outfitCarAttr);
            }
            outfitCar.addCarAttribute(carAttribute);
        }
    }
    
    /**
     * This method assigns the outfit_parent_products attribute to each child car.
     * This attribute stores all the outfit products the child is assigned to.
     * @param childCar
     * @param outfitProductCode
     * @param user
     */
    private void addParentProductAttributeToChild(Car childCar, String outfitProductCode, User user) {
        /* 
         * set OUTFIT_PARENT_PRODUCTS for the child car.
         * OUTFIT_PARENT_PRODUCTS : this attribute is added to all child products of outfit car and its value will be outfit products code delimited by comma
         */
         Attribute attrParentProducts = outfitDao.getAttributeByName(Constants.OUTFIT_PARENT_PRODUCTS);
         CarAttribute parentProdCarAttr = outfitDao.findAttributeInCar(childCar, attrParentProducts);
         if(parentProdCarAttr == null){
             parentProdCarAttr = new CarAttribute();
             parentProdCarAttr.setDisplaySeq((short) 0);
             parentProdCarAttr.setHasChanged(Constants.FLAG_NO);
             parentProdCarAttr.setIsChangeRequired(Constants.FLAG_YES);
             parentProdCarAttr.setStatusCd(Status.ACTIVE);
             parentProdCarAttr.setAttributeValueProcessStatus(noCheckRequired);
             parentProdCarAttr.setAuditInfo(user);
             parentProdCarAttr.setAttribute(attrParentProducts);
             parentProdCarAttr.setAttrValue(outfitProductCode);
             parentProdCarAttr.setCar(childCar);
         }else{
             String strParentProducts=parentProdCarAttr.getAttrValue();
             if(strParentProducts!=null){
                 if (!strParentProducts.contains(outfitProductCode)) {
                     strParentProducts = strParentProducts.concat(",").concat(outfitProductCode);
                 }
             } else {
                 strParentProducts = outfitProductCode;
             }
             parentProdCarAttr.setAttrValue(strParentProducts);
             parentProdCarAttr.setAuditInfo(user);
        }
        carManager.saveOrUpdate(parentProdCarAttr);
    }
    
    private void removeParentProductAttributeFromChild(Car childCar, String productCode, User user) {
        Attribute attrParentProducts = outfitDao.getAttributeByName(Constants.OUTFIT_PARENT_PRODUCTS);
        CarAttribute parentProdCarAttr = outfitDao.findAttributeInCar(childCar, attrParentProducts);
        if (parentProdCarAttr != null) {
            String oldValue = parentProdCarAttr.getAttrValue();
            String[] values = new String[1];
            if (oldValue != null) values = oldValue.split(",");
            String newValue = "";
            // reconstruct without this parent product code
            for (String s : values) {
                if (s != null && !s.equals(productCode)) {
                    newValue += s + ",";
                } else {
                    if (log.isInfoEnabled()) log.info("Removed parent product attribute value: " + s);
                }
            }
            
            if (newValue.isEmpty()) {
                // remove the whole car attribute record
                childCar.getCarAttributes().remove(parentProdCarAttr);
                carManager.saveOrUpdate(childCar);
            } else {
                // remove trailing comma
                newValue = newValue.substring(newValue.length()-1);
                parentProdCarAttr.setAttrValue(newValue);
                carManager.saveOrUpdate(parentProdCarAttr);
            }
        }
        
    }
    
    /**
     * This method assigns the outfit_child_products attribute to the outfit car.
     * This attribute stores all the child products assigned to the outfit.
     * @param outfitCar
     * @param outfitChildCodes
     * @param user
     */
    private void addChildProductAttributeToParent(Car outfitCar, String outfitChildCodes, User user) {
        /* 
         * set OUTFIT_CHILD_PRODUCTS for the child car.
         * OUTFIT_CHILD_PRODUCTS : this attribute is added to all child products of outfit car and its value will be outfit products code delimited by comma
         */
         Attribute attrParentProducts = outfitDao.getAttributeByName(Constants.OUTFIT_CHILD_PRODUCTS);
         CarAttribute childProdCarAttr = outfitDao.findAttributeInCar(outfitCar, attrParentProducts);
         if(childProdCarAttr == null){
             childProdCarAttr = new CarAttribute();
             childProdCarAttr.setDisplaySeq((short) 0);
             childProdCarAttr.setHasChanged(Constants.FLAG_NO);
             childProdCarAttr.setIsChangeRequired(Constants.FLAG_YES);
             childProdCarAttr.setStatusCd(Status.ACTIVE);
             childProdCarAttr.setAttributeValueProcessStatus(noCheckRequired);
             childProdCarAttr.setAuditInfo(user);
             childProdCarAttr.setAttribute(attrParentProducts);
             childProdCarAttr.setAttrValue(outfitChildCodes);
             childProdCarAttr.setCar(outfitCar);
         }else{
             String strParentProducts=childProdCarAttr.getAttrValue();
             if(strParentProducts!=null){
                 String[] outfitChildrenArr = outfitChildCodes.split(",");
                 for (int i=0;i<outfitChildrenArr.length;i++) {
                     if (!strParentProducts.contains(outfitChildrenArr[i])) {
                         strParentProducts = strParentProducts.concat(",").concat(outfitChildrenArr[i]);
                     }
                 }
             } else {
                 strParentProducts = outfitChildCodes;
             }
             childProdCarAttr.setAttrValue(strParentProducts);
             childProdCarAttr.setUpdatedBy(user.getEmailAddress());
             childProdCarAttr.setUpdatedDate(new Date());
        }
        carManager.saveOrUpdate(childProdCarAttr);
    }
            
    /**
     * This method returns the highest priority attributes, priority is based on attribute display type
     * eg: Checkbox has highest priority
     * @param  attrList: List of attributes
     */
    private List<Attribute> prioritizeAttributes(List<Attribute> attrList){
        if(attrList.size() <= 1){
            return attrList;
        }
        Map <String, List<Attribute>>attributeTypeMap = new HashMap<String, List<Attribute>>();
        List<Attribute> sameTypeAttrList =null;
        
        for(Attribute attr: attrList){
            String attrType = attr.getAttributeConfig().getHtmlDisplayType().getHtmlDisplayTypeCd();
            if((sameTypeAttrList= attributeTypeMap.get(attrType)) == null){
                sameTypeAttrList = new ArrayList<Attribute>();
            }
            sameTypeAttrList.add(attr);
            attributeTypeMap.put(attrType, sameTypeAttrList);
        }
        if((sameTypeAttrList=attributeTypeMap.get(HtmlDisplayType.CHECKBOX))!=null){
            return sameTypeAttrList;
        }else if((sameTypeAttrList=attributeTypeMap.get(HtmlDisplayType.TEXT_WITH_AUTOCOMPLETE)) != null){
            return sameTypeAttrList;
        }else if((sameTypeAttrList=attributeTypeMap.get(HtmlDisplayType.DROP_DOWN)) != null){
            return sameTypeAttrList;
        }else if((sameTypeAttrList=attributeTypeMap.get(HtmlDisplayType.RADIO_BUTTONS)) != null){
            return sameTypeAttrList;
        }else if((sameTypeAttrList=attributeTypeMap.get(HtmlDisplayType.HTML_EDITOR)) != null){
            return sameTypeAttrList;
        }else{
            return attrList;
        }
    }
    
    /**
     * This method sorts all the skus according to priority from PIM.
     * @param skuMap
     * @return string of skucodes according to the priority from PIM.
     */
    private String sortSkusAccordingToPriority(Map<String,List<VendorSku>> skuMap, int maxPriority) {
        StringBuilder sortedSkus = new StringBuilder();
        
        for (int i=Constants.PRIORITY_START_VALUE; i<=maxPriority; i++) {
            List<VendorSku> skuList = skuMap.get(String.valueOf(i));
            if (skuList==null) {
                continue;
            }
            for (VendorSku sku: skuList) {
                sortedSkus.append(sku.getBelkSku());
                sortedSkus.append(",");
            }
        }
        if (sortedSkus.length()>0) {
            sortedSkus.deleteCharAt(sortedSkus.length()-1); //delete last comma
        }
        return sortedSkus.toString();
    }

    /**
     * This method adds all the skus which should be included in the collection
     * into a skuMap.
     * @param skuMap
     * @param childCar
     * @param componentType
     * @param orin
     * @param priority
     * @return
     */
    private Map<String,List<VendorSku>> addSelectedColorSkusToSkuMap(Map<String,List<VendorSku>> skuMap, Car childCar, 
            String componentType, String orin, String priority) {
        
        try {
            List<VendorSku> skuList = new ArrayList<VendorSku>();
            if (Constants.STYLE_COLOR.equalsIgnoreCase(componentType)) {
                //If child component is type "StyleColor," only add the skus
                //associated with given color.
                String colorCode = StringUtils.substring(orin, 9);
                Set<VendorSku> skuSet = childCar.getVendorSkus();
                for (VendorSku sku : skuSet) {
                    if (sku.getColorCode().equals(colorCode)) {
                        if (log.isInfoEnabled()) log.info("Child component is a StyleColor component. Adding only one sku associated with StyleColor.");
                        if (log.isInfoEnabled()) log.info("Adding sku: " + sku.getBelkSku() + " color code: " + sku.getColorCode());
                        skuList.add(sku);
                        break; // add any one sku associated with the style color
                    }
                }
            } else {
                for (VendorSku v : childCar.getVendorSkus()) {
                    if (log.isInfoEnabled()) log.info("Child outfit component is a full VendorStyle. Adding any one sku associated with given Style.");
                    if (log.isInfoEnabled()) log.info("Adding sku: " + v.getBelkSku());
                    skuList.add(v);
                    break; // add any one sku
                }
            }
            if (log.isInfoEnabled()) log.info("Number of Skus added: " + skuList.size() + " / " + childCar.getVendorSkus().size() + " total.");
                
            if (skuMap.get(priority)==null) {
                skuMap.put(priority, skuList);
            } else {
                // if there is already skus for given priority, combine the sku lists.
                skuMap.get(priority).addAll(skuList);
            }
        }
        catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Exception occurred will trying to add skus to collection.", e);
            }
        }
        return skuMap;
    }
    
    /**
     * This method creates the Collection_Skus table entries for the outfit
     * @param outfitProductCode
     * @param outfitChildProductsSku
     */
    private void addCollectionSkus(String outfitProductCode, String outfitChildProductsSku) {
        // Only add entries if it doesn't already exist.
        List<CollectionSkus> existingCollectionSkus = outfitManager.getCollectionSkus(outfitProductCode);
        StringBuilder collectionSkuCodes = new StringBuilder();
        for (CollectionSkus s: existingCollectionSkus) {
            collectionSkuCodes.append(s.getSkuCode()).append(",");
        }
        
        String[] outfitChildProductSkusArr = StringUtils.split(outfitChildProductsSku, ",");
        for (int i=0; i<outfitChildProductSkusArr.length; i++) {
            if (!collectionSkuCodes.toString().contains(outfitChildProductSkusArr[i])) {
                outfitManager.createCollectionSkus(outfitProductCode, outfitChildProductSkusArr[i]);
            }
        }
    }

    /**
     * This method compares the Grouping from the GroupMod message with the exiting Grouping
     * in the CARS database.  If there are any differences, attempts to sync grouping.
     * 
     * @param parentStyle
     * @param components
     * @param user
     */
    public void syncCollectionProducts(VendorStyle parentStyle, List<GroupingComponent> components, User user) {
        Car outfitCar = carManager.getCarForStyle(parentStyle.getVendorStyleId());
        VendorStyle defaultChild = null;
        
        if (outfitCar == null) {
            if (log.isErrorEnabled()) {
                log.error("Outfit CAR has not yet been for the vendor style.");
            }
            return;
        }
        
        // generate list from CARS first.  Key is product_code (vn+vs).
        Map<String,StyleAndStyleColorDTO> stylesToRemoveFromCars =new HashMap<String,StyleAndStyleColorDTO>();
        Set<CarOutfitChild> carOutfitChildren = outfitCar.getCarOutfitChild();
        for (CarOutfitChild child : carOutfitChildren) {
            VendorStyle existingVS = child.getChildCar().getVendorStyle();
            StyleAndStyleColorDTO dto = stylesToRemoveFromCars.get(existingVS.getVendorNumber()+existingVS.getVendorStyleNumber());
            if (dto==null) {
                dto = new StyleAndStyleColorDTO();
                dto.setVendorNumber(existingVS.getVendorNumber());
                dto.setVendorStyleNumber(existingVS.getVendorStyleNumber());
                dto.getCarOutfitChildren().add(child);
            }
            else {
                dto.getCarOutfitChildren().add(child);
            }
            
            if (VendorStyleType.PRODUCT.equals(existingVS.getVendorStyleType().getCode())) {
                //Need to add all the associated colors also.
                Set<VendorSku> skus = child.getChildCar().getVendorSkus();
                for (VendorSku s : skus) {
                    dto.getStyleColors().add(s.getColorCode());
                }
                stylesToRemoveFromCars.put(existingVS.getVendorNumber()+existingVS.getVendorStyleNumber(),dto);
            }
            else {
                //existingVS is actually a pattern.  First add pattern.
                stylesToRemoveFromCars.put(existingVS.getVendorNumber()+existingVS.getVendorStyleNumber(),dto);
            }
        }
        
        // init to empty list if its null to prevent errs
        if (components == null) {
            components = new ArrayList<GroupingComponent>();
        }
        
        // next, compare CARS list with items from PIM
        List<GroupingComponent> componentsToAdd = new ArrayList<GroupingComponent>();
        for (GroupingComponent component: components) {
            String productCode = component.getVendorNumber()+component.getVendorStyle();
            StyleAndStyleColorDTO dto = stylesToRemoveFromCars.get(productCode);
            
            if (dto != null) {
                checkPriorityAndUpdate(dto, component);
            }
            
            if (Constants.STYLE.equalsIgnoreCase(component.getComponentType())) {
                if (dto==null) {
                    // Style doesn't exist in CARS.  Need to add.
                    componentsToAdd.add(component);
                }
                else {
                    // Style exists in CARS.  Do not remove.
                    stylesToRemoveFromCars.remove(productCode);
                }
            }
            else if (Constants.STYLE_COLOR.equalsIgnoreCase(component.getComponentType())) {
                String colorCode = StringUtils.substring(component.getId(), 9);
                if (dto==null) {
                    // Style_color belongs to a style that doesn't exist in CARS.  Need to add.
                    componentsToAdd.add(component);
                }
                else {
                    dto.setIsStyleColor(true);
                    if (dto.getStyleColors().contains(colorCode)) {
                        if (dto.getStyleColors().size()==1) {
                            // Style_color belongs to a style that exists in CARS.  Do not remove.
                            stylesToRemoveFromCars.remove(productCode);
                        }
                        else {
                            dto.getStyleColors().remove(colorCode);
                        }
                    }
                    else {
                        // Add Style_color only.
                        componentsToAdd.add(component);
                    }
                }
            }
            else {
                // component is a group.
                if (dto==null) {
                    // Group doesn't exist in CARS.  Need to add.
                    componentsToAdd.add(component);
                }
                else {
                    // Style exists in CARS.  Do not remove.
                    stylesToRemoveFromCars.remove(productCode);
                }
            }
        }

        if (log.isInfoEnabled()) {
            log.info("Found " + stylesToRemoveFromCars.size() + " group/styles/style_colors to remove from collection.");
            log.info("Found " + componentsToAdd.size() + " components eligible to add to collection.");
        }
        
        if (!stylesToRemoveFromCars.isEmpty()) {
            removeChildrenFromCollection(outfitCar, stylesToRemoveFromCars, user);
        }
        
        if (!componentsToAdd.isEmpty()) {
            Object[] returnValue = addChildrenToCollection(outfitCar, componentsToAdd, user, true);
            if (returnValue[0]!=null) {
                addChildProductAttributeToParent(outfitCar, (String) returnValue[0], user); //update outfit_child_products
            }
            if (returnValue[1]!=null) {
                defaultChild = (VendorStyle) returnValue[1];
            }
        }
        
        if (defaultChild==null && !outfitCar.getCarOutfitChild().isEmpty()) {
            // For outfits, just pick one of the children if defaultChild is still null.
            CarOutfitChild carOutfitChild = outfitCar.getCarOutfitChild().iterator().next();
            if (carOutfitChild!=null) {
                defaultChild = carOutfitChild.getChildCar().getVendorStyle();
            }
        }
        
        // finally, update the attributes & images for all impacted cars.
        List<Car> outfitCars = new ArrayList<Car>();
        outfitCars.add(outfitCar);
        updateAttributesAndImagesForCar(outfitCars, defaultChild, parentStyle.getVendorStyleType().getCode(), true, user);
    }

    /**
     * This method compares the Grouping from the GroupMod message with the exiting Grouping
     * in the CARS database.  If there are any differences, attempts to sync grouping.
     * 
     * @param parentStyle
     * @param components
     * @param user
     */
    public void syncPatternProducts(VendorStyle parentStyle, List<GroupingComponent> components, String vendorStyleType, User user) {
        if (components == null) {
            components = new ArrayList<GroupingComponent>(); // init to empty list if its null to prevent errs
        }
        // compare contents of components and outfit children - determine if any need to be added / removed.
        List<GroupingComponent> stylesToAdd = new ArrayList<GroupingComponent>();
        Set<VendorStyle> childrenToRemove = new HashSet<VendorStyle>();
        
        stylesToAdd.addAll(components); // prime the list with components, remove items that already exist in carOutfitChildren
        
        
        List<VendorStyle> patternStyles = getChildPatternStyles(parentStyle);
        childrenToRemove.addAll(patternStyles); // prime the list, then remove items that dont exist in components
        
            
        if (log.isInfoEnabled()) {
            log.info("Within syncPatternProducts - VendorStyle: " + parentStyle.getVendorNumber() + parentStyle.getVendorStyleNumber());
            if (components != null) {
                log.info("Looking through styles in XML compared to CARS - xml components:" +components.size()
                + ", current CARS children: " + patternStyles.size());
            }
        }
        
        // go through each existing style in CARS, see if its in the update xml from PIM.
        // if its not in there, keep it in the list to remove.
        for (VendorStyle vs : patternStyles) {
            String existingVenNum = vs.getVendorNumber();
            String existingVenStyle = vs.getVendorStyleNumber();
            boolean exists = false;
            for (GroupingComponent xmlComponent : components) {
                String newVenNum = xmlComponent.getVendorNumber();
                String newVenStyle = xmlComponent.getVendorStyle();
                if (existingVenNum.equals(newVenNum) && existingVenStyle.equals(newVenStyle)) {
                    exists = true; 
                    break;
                }
            }
            if (exists) {
                childrenToRemove.remove(vs);
            }
        }
        if (log.isInfoEnabled()) {
            log.info("Found " + childrenToRemove.size() + " styles to remove from pattern.");
        }
        // go through each new style in the xml, see if its not in cars already - if so, keep in the add list.
        // if it is in CARS already and associated to the collection, we don't need to add.
        for (GroupingComponent xmlComponent : components) {
            String newVenNum = xmlComponent.getVendorNumber();
            String newVenStyle = xmlComponent.getVendorStyle();
            boolean exists = false;
            for (VendorStyle vs : patternStyles) {
                String existingVenNum = vs.getVendorNumber();
                String existingVenStyle = vs.getVendorStyleNumber();
                if (existingVenNum.equals(newVenNum) && existingVenStyle.equals(newVenStyle)) {
                    exists = true; 
                    break;
                }
            }
            if (exists) {
                stylesToAdd.remove(xmlComponent);
            }
        }
        if (log.isInfoEnabled()) {
            log.info("Found " + stylesToAdd.size() + " styles to add to pattern.");
        }
        List<Car> patternCars = carManager.getActiveCarsByParentVendorStyle(parentStyle.getVendorStyleId());
        if (log.isInfoEnabled()) {
            log.info("Pattern Cars: " + patternCars.size());
            log.info("Children to remove: " + childrenToRemove.size());
        }
        if (!childrenToRemove.isEmpty()) {
            removeChildrenFromPattern(patternCars.get(0), childrenToRemove, user);
        }
        
        if (!stylesToAdd.isEmpty()) {
            addChildrenToPattern(parentStyle, stylesToAdd, vendorStyleType, user, true);
        } else {
            // ensure attributes are updated anyways.
            VendorStyle defaultStyle = getDefaultChildOfPattern(parentStyle);
            boolean merged = isPatternMerged(patternCars);
            updateAttributesAndImagesForCar(patternCars, defaultStyle, vendorStyleType, merged, user);
        }
        
        
    }

    /**
     * Merges the PIM grouping message's type into the existing VendorStyle's VendorStyleType.
     * @param parentStyle
     * @param parentGrouping 
     */
    public String syncGroupingType(VendorStyle parentStyle, GroupingParent parentGrouping, User user) {
        if (log.isInfoEnabled()) {
            log.info("Syncing Group Type");
        }
        String newGroupType = getCarGroupTypeByPimGroupType(parentGrouping.getGroupingType(), parentGrouping.getCARSGroupType());
        String existingGroupType = "";
        
        if (parentStyle.getVendorStyleType() != null)
            existingGroupType = parentStyle.getVendorStyleType().getCode();
        
        if (log.isInfoEnabled()) {
            log.info("New group type: " + newGroupType + " | Existing group type: " + existingGroupType);
        }
        if (newGroupType != null && (parentStyle.getVendorStyleType() == null || !existingGroupType.equals(newGroupType))) {
            
            if (!(existingGroupType.equals(VendorStyleType.PATTERN_SPLIT_VS) && newGroupType.equals(VendorStyleType.PATTERN_SSKU_VS) ||
                   newGroupType.equals(VendorStyleType.PATTERN_SPLIT_VS) && existingGroupType.equals(VendorStyleType.PATTERN_SSKU_VS)) ) {
                if (log.isErrorEnabled()) {
                    log.error("Attempted to change incompatable group types for group_id: " + parentStyle.getGroupId());
                }
                return existingGroupType; // do nothing.
            }
            
            if (log.isInfoEnabled()) {
                log.info("Changing Group Type from " + existingGroupType + " to " + newGroupType);
            }
            
            VendorStyleType vst = (VendorStyleType) carLookupManager.getById(VendorStyleType.class, newGroupType);
            if (vst != null) {
                parentStyle.setVendorStyleType(vst);
                parentStyle.setAuditInfo(user);
                carManager.saveOrUpdate(parentStyle);
            }
        } else {
            if (log.isInfoEnabled()) {
                log.info("Leaving Group Type unchanged.");
            }
        }
        return parentStyle.getVendorStyleType().getCode();
        
    }

    /**
     * This method removes the set of children from the outfitCar.
     * 
     * @param outfitCar
     * @param childrenToRemove
     */
    private void removeChildrenFromCollection(Car outfitCar, Map<String,StyleAndStyleColorDTO> childrenToRemove, User user) {
        // compile a list of sku to remove from the OUTFIT_PRODUCT_CHILD attribute list.
        Set<String> skusToRemove = new HashSet<String>();
        String parentProductCode = outfitCar.getVendorStyle().getVendorNumber() + outfitCar.getVendorStyle().getVendorStyleNumber();
        for (String productCode : childrenToRemove.keySet()) {
            StyleAndStyleColorDTO dto = childrenToRemove.get(productCode);
            
            if (dto.isStyleColor()) {
                // remove only the cars associated with style_color(s)
                for (CarOutfitChild c : dto.getCarOutfitChildren()) {
                    Set<VendorSku> carSkus = c.getChildCar().getVendorSkus();
                    int count = 0;
                    if (log.isInfoEnabled()) {
                        log.info("Removing style color(s) from collection: " + productCode + " - " + dto.getStyleColors().toString());
                    }
                    for (VendorSku s: carSkus) {
                        if (dto.getStyleColors().contains(s.getColorCode())) {
                            count++;
                            skusToRemove.add(s.getBelkSku());
                        }
                    }
                    if (log.isInfoEnabled()) {
                        log.info(count + " skus removed for style color(s).");
                    }
                    if (skusToRemove.size()==carSkus.size()) {
                        //remove car also if all skus are removed
                        if (log.isInfoEnabled()) {
                            log.info("Removing entire child because all skus were removed.");
                        }
                        outfitCar.getCarOutfitChild().remove(c);
                        removeParentProductAttributeFromChild(c.getChildCar(), parentProductCode, user);
                    }
                }
            }
            else {
                // remove all cars associated with style or group.
                for (CarOutfitChild c : dto.getCarOutfitChildren()) {
                    
                    
                    for (VendorSku sku : c.getChildCar().getVendorSkus()) {
                        skusToRemove.add(sku.getBelkSku());
                    }
                    removeParentProductAttributeFromChild(c.getChildCar(), parentProductCode, user);
                    outfitCar.getCarOutfitChild().remove(c);
                }
            }
        }
        removeSkusFromCollection(outfitCar, skusToRemove, user);
        outfitCar.setAuditInfo(user);
        carManager.saveOrUpdate(outfitCar);
    }
    
    /**
     * This method adds the list of children to the outfitCar.
     * 
     * @param outfitCar
     * @param childrenToAdd
     * @param defaultChild
     * @param user
     * @return comma-delimited string of all the skus assigned to given outfitCar.
     */
    private Object[] addChildrenToCollection(Car outfitCar, List<GroupingComponent> childrenToAdd, User user, boolean update) {
        Object[] returnValue = new Object[2];  // 0: sku_codes, 1: defaultChild
        VendorStyle defaultChild = null;
        
        String outfitProductCode = outfitCar.getVendorStyle().getVendorNumber()+outfitCar.getVendorStyle().getVendorStyleNumber();
        Set<CarOutfitChild> carOutfitChildren = new HashSet<CarOutfitChild>();
        Map<String, List<VendorSku>> skuMap = new HashMap<String, List<VendorSku>>();
        int maxPriority = 1;
        for (GroupingComponent childDtl: childrenToAdd) {
            int priority = Integer.parseInt(childDtl.getPriority());
            if (maxPriority < priority) {
                maxPriority = priority;
            }
            VendorStyle childVS = getChildVendorStyleIfExists(childDtl);
            if (childVS!=null) {
                if (Constants.TRUE.equalsIgnoreCase(childDtl.getIsDefault())) {
                    defaultChild = childVS;
                }
                // child vendor_style can be part more than one car.
                List<Car> childrenCars = carManager.getActiveCarsByParentVendorStyle(childVS.getVendorStyleId());
                if (childrenCars==null) {
                    if (log.isDebugEnabled()) {
                        log.debug("No car for child component vendor_style_id: " + childVS.getVendorStyleId() + ".  Ignoring.");
                    }
                    continue;
                }
                for (Car childCar : childrenCars) {
                    CarOutfitChild outfitChild = generateCarOutfitChild(outfitCar, childCar, childDtl.getPriority(), user);
                    if (outfitChild==null) {
                        continue;
                    }
                    carOutfitChildren.add(outfitChild);
                    if (!update) {
                        addAttributesToOutfit(outfitCar,childCar, user); //add outfit attributes
                    }
                    addParentProductAttributeToChild(childCar, outfitProductCode, user); //add parent_product attribute
                    skuMap = addSelectedColorSkusToSkuMap(skuMap, childCar, childDtl.getComponentType(), childDtl.getId(), childDtl.getPriority());
                    addCollectionSkus(outfitProductCode, getChildCarSkus(childCar, childDtl.getComponentType(), childDtl.getId()));
                }
            }
        }
        if (log.isInfoEnabled()) {
            log.info("Number of car(s) added to collection (style_number: " + outfitCar.getVendorStyle().getVendorStyleNumber()
                + "): " + carOutfitChildren.size());
        }
        outfitCar.getCarOutfitChild().addAll(carOutfitChildren); // add child cars
        String outfitChildProductsSku = sortSkusAccordingToPriority(skuMap, maxPriority);
        outfitCar.setAuditInfo(user);
        carManager.saveOrUpdate(outfitCar);
        
        // update return value
        returnValue[0] = outfitChildProductsSku;
        returnValue[1] = defaultChild;
        return returnValue;
    }

    /**
     * This method removes the set of children from the pattern car.
     * 
     * @param patternParent
     * @param childrenToRemove
     */
    private void removeChildrenFromPattern(Car patternParent, Set<VendorStyle> childrenToRemove, User user) {
        
        
        if (log.isInfoEnabled()) {
            log.info("removeChildrenFromPattern");
        }
        // need to unpattern this vendor style
        for (VendorStyle child : childrenToRemove) {
            Set<VendorSku> skusToTransfer = child.getVendorSkus();
            child.setParentVendorStyle(null);
            if (log.isInfoEnabled()) {
                log.info("Depatterning vendor style: " + child.getVendorNumber()+child.getVendorStyleNumber() + " from parent pattern car: " + patternParent.getCarId());
            }
            // reactivate child's car if it exists, else create manual car for it
            Car childCarToReactivate = carManager.getCarByVendorStyle(child.getVendorStyleId());

            if (childCarToReactivate != null) {
                if (log.isInfoEnabled()) {
                    log.info("Reactivating existing child car: " + childCarToReactivate.getCarId());
                }
                childCarToReactivate.setStatusCd(Status.ACTIVE);
                childCarToReactivate.setAuditInfo(user);
                
                if (log.isInfoEnabled()) log.info("Transfering " + skusToTransfer.size() + " skus to car: " + childCarToReactivate.getCarId());
                for (VendorSku vs : skusToTransfer) {

                    
                    if (vs.getCar().getStatusCd().equals(Status.DELETED)) {
                        if (log.isInfoEnabled()) log.info("Not removing sku: " + vs.getBelkUpc() + " - parent car is DELETED.");
                        continue;
                    }
                    
                    Set<VendorSku> existingSkus = childCarToReactivate.getVendorSkus();
                    boolean skuExists = false;
                    for (VendorSku vs2 : existingSkus) {
                        if (vs.getBelkUpc().equals(vs2.getBelkUpc())) {
                            skuExists = true;
                            break;
                        }

                    }

                    if (!skuExists) {
                        vs.setCar(childCarToReactivate);
                        childCarToReactivate.getVendorSkus().add(vs);
                    }else {
                            Car skuCar = vs.getCar();
                            if (skuCar.getVendorSkus().size() == 1) {
                                log.info("Deleted duplicate sku's car: " +skuCar.getCarId());
                                skuCar.setStatusCd(Status.DELETED);
                            } else {
                                log.info("Attempting to remove sku from car: " + skuCar.getCarId());
                                try {
                                    skuCar.getVendorSkus().remove(vs);
                                    carManager.save(skuCar);
                                } catch (Exception e) {
                                    log.warn("Could not remove sku from car. Error: " + e.getMessage());
                                }
                            } 
                            
                        }  
                }
                carManager.saveOrUpdate(childCarToReactivate);
            } else {
                if (log.isInfoEnabled()) {
                    log.info("No existing child car for de-patterned style. Creating manual car.");
                }
                Car car = new Car();
                WorkflowStatus initiated = (WorkflowStatus) carLookupManager.getById(WorkflowStatus.class, WorkflowStatus.INITIATED);
                UserType buyer = (UserType) carLookupManager.getById(UserType.class, UserType.BUYER);
                SourceType sourceOutfitCar = carManager.getSourceTypeForCode(SourceType.MANUAL);
                WorkFlow defaultWorkflow = workflowManager.getWorkFlow(workflowManager.getWorkflowType(WorkflowType.CAR));
                ContentStatus contentInProgress = (ContentStatus) carLookupManager.getById(ContentStatus.class, ContentStatus.IN_PROGRESS);
                Department dept = patternParent.getDepartment();

                car.setVendorStyle(child);
                car.setDepartment(dept);
                car.setSourceType(sourceOutfitCar);
                car.setWorkFlow(defaultWorkflow);
                car.setSourceId(patternParent.getSourceId());
                car.setCarUserByLoggedByUserId(patternParent.getCarUserByLoggedByUserId());
                car.setExpectedShipDate(patternParent.getExpectedShipDate());
                //for outfit car due date is same as ship date
                car.setDueDate(patternParent.getDueDate());
                car.setEscalationDate(patternParent.getEscalationDate());
                car.setArchive(Constants.FLAG_NO);
                car.setCurrentWorkFlowStatus(initiated);
                //all ouftit cars are assigned to buyer by default
                car.setAssignedToUserType(buyer);
                car.setStatusCd(Status.ACTIVE);
                if (child.getProductType()==null) {
                    car.setIsProductTypeRequired(Constants.FLAG_Y);
                }
                else {
                    car.setIsProductTypeRequired(Constants.FLAG_NO);
                }
                car.setIsUrgent(Constants.FLAG_NO);
                car.setContentStatus(contentInProgress);
                car.setLastWorkflowStatusUpdateDate(new Date());
                car.setTurninDate(null);
                car.setArchive(Constants.FLAG_NO);
                car.setLock(Constants.FLAG_NO);
                //save outfit car
                car.setAuditInfo(user);
                car.setVendorSkus(new HashSet<VendorSku>());
                log.info("Transfering " + skusToTransfer.size() + " skus to car: " + car.getCarId());
                    for (VendorSku vs : skusToTransfer) {
                        
                        
                        if (vs.getCar().getStatusCd().equals(Status.DELETED)) {
                            if (log.isInfoEnabled()) log.info("Not removing sku: " + vs.getBelkUpc() + " - parent car is DELETED.");
                            continue;
                        }
                        
                        
                        boolean skuExists = false;
                        for (VendorSku vs2 : car.getVendorSkus()) {
                            if (vs.getBelkUpc().equals(vs2.getBelkUpc())) {
                                skuExists = true;
                                break;
                            }

                        }
                        if (!skuExists) {
                            vs.setCar(car);
                            car.getVendorSkus().add(vs);
                        } else {
                            Car skuCar = vs.getCar();
                            if (skuCar.getVendorSkus().size() == 1) {
                                log.info("Deleted duplicate sku's car: " +skuCar.getCarId());
                                skuCar.setStatusCd(Status.DELETED);
                            } else {
                                log.info("Attempting to remove sku from car: " + skuCar.getCarId());
                                try {
                                    skuCar.getVendorSkus().remove(vs);
                                    carManager.save(skuCar);
                                } catch (Exception e) {
                                    log.warn("Could not remove sku from car. Error: " + e.getMessage());
                                }
                            } 
                            
                        }
                        
                    }
                carManager.saveOrUpdate(car);  
                
                if (log.isInfoEnabled()) {
                    log.info("Manual CAR created: " + car.getCarId());
                }
                
                if (log.isInfoEnabled()) log.info("Adding dept/class/product type attributes to the new car.");
                carManager.resyncAttributes(car, user); 
                
            }
            child.setAuditInfo(user);
            carManager.saveOrUpdate(child);
        }
    }

    /**
     * This method adds the children GroupingComponents to the pattern VendorStyle.
     * Also handles attribute updates 
     * @param pattern, the parent pattern
     * @param children, the children to add
     * @param vendorStyleType, the pattern's group type
     * @param user
     */
    private void addChildrenToPattern(VendorStyle pattern, List<GroupingComponent> children, String vendorStyleType, User user, boolean update) {
        List<VendorStyle> childrenToAdd = new ArrayList<VendorStyle>();
        VendorStyle defaultChild = null;
        for (GroupingComponent childDtl : children) {

                if (log.isInfoEnabled()) {
                    log.info("Adding child to pattern: " + childDtl.getVendorNumber() + childDtl.getVendorStyle());
                }
            
            VendorStyle childVS = getChildVendorStyleIfExists(childDtl);
            if (childVS==null) {
                childVS = generateComponentVendorStyle(childDtl, pattern, user);
            }
            if (childVS==null) {
                if (log.isWarnEnabled()) {
                    log.warn("Child component could not be generated!  Ignoring component: " + childDtl.getId());
                }
                continue; //skip.
            }
            if (childVS.getParentVendorStyle()!=null) {
                if (log.isWarnEnabled()) {
                    log.warn("Child will not be added because it is already assigned to another pattern.  Orin:" + childVS.getOrinNumber());
                }
                continue; //do not add child if child is already assigned to another pattern.
            }
            if (Constants.TRUE.equalsIgnoreCase(childDtl.getIsDefault())) {
                childVS.setIsDefault(Constants.FLAG_Y);
                defaultChild = childVS;
            }
            childVS.setParentVendorStyle(pattern);
            childVS.setAuditInfo(user);
            patternCollectionDao.merge(childVS);
            childrenToAdd.add(childVS);
        }
        
        if (log.isInfoEnabled()) {
            log.info("Number of children added to pattern (style_number: " + pattern.getVendorStyleNumber()
                + "): " + childrenToAdd.size() + " / " + children.size() + " total.");
        }
        
        if (defaultChild==null) {
            //if defaultChild is still null, get existing one from db.
            defaultChild = getDefaultChildOfPattern(pattern);
        }

        Car mergeEligibleCar = getMergeEilgibleCar(pattern, childrenToAdd); // can return null
        
        mergePattern(mergeEligibleCar, pattern, childrenToAdd, user); // create or merge pattern car
        List<Car> patternCars = new ArrayList<Car>();
        patternCars.add(mergeEligibleCar);
        updateAttributesAndImagesForCar(patternCars, defaultChild, vendorStyleType, true, user);
    }
    
    /**
     * This method determines which request to call based on the vendorStyleType
     * and if pattern is merged or converted, to update the attributes and images.
     * 
     * @param cars, the list of cars for which to update
     * @param defaultChild, the default style for which to fetch the attributes
     * @param vendorStyleType, the pattern's group type
     * @param isMergedPattern, boolean to determine if pattern was merged.
     * @param user
     */
    private void updateAttributesAndImagesForCar(List<Car> cars, VendorStyle defaultChild, 
            String vendorStyleType, boolean isMergedPattern, User user) {
        try {
            if (isMergedPattern) {
                // this is a merged pattern
                Car car = cars.get(0);
                if (VendorStyleType.PATTERN_CONS_VS.equals(vendorStyleType)) {
                    //images are fetched from style_color & attributes are fetched from the group
                    pimAttributeManager.refreshCarPIMAttributesForPattern(car, null, user);
                }
                else {
                    //images are fetched from the grouping & attributes are fetched from the defaultChild
                    pimAttributeManager.refreshCarPIMAttributesForPattern(car, defaultChild, user);
                }
            }
            else {
                // this is a converted pattern
                for (Car car : cars) {
                    if (VendorStyleType.PATTERN_CONS_VS.equals(vendorStyleType)) {
                        updateImagesByStyleColor(car); //images are fetched from style_color
                    }
                    else {
                        updateImagesByGrouping(car); //images are fetched from the grouping
                    }
                    //attributes are fetched from child vendor_style.
                    pimAttributeManager.refreshCarPIMAttributesForPatternChildren(car, user);
                }
            }
        }
        catch (Exception e) {
            // do not allow attribute/image update failures to cause the grouping car from 
            // being persisted to db.  simply log exception.
            if (log.isErrorEnabled()) {
                log.error("Exception occurred while updating attributes & images for car(s)!", e);
            }
        }
    }

    /**
     * This method will add car_attribute to given car.
     * 
     * @param carId
     * @param attrValueSkus
     * @param attributeName
     * @param user
     */
    public void addCarAttribute(Car car, String attrName, String attrValue, User user){
        Attribute attribute = outfitDao.getAttributeByName(attrName);
        CarAttribute carAttribute = new CarAttribute();
        carAttribute.setCar(car);
        carAttribute.setAttribute(attribute);
        carAttribute.setAttrValue(attrValue);
        carAttribute.setHasChanged(Constants.FLAG_NO);
        carAttribute.setIsChangeRequired(Constants.FLAG_YES);
        carAttribute.setStatusCd(Status.ACTIVE);
        carAttribute.setDisplaySeq((short) 0);
        carAttribute.setAuditInfo(user);
        carAttribute.setAttributeValueProcessStatus(noCheckRequired);
        carManager.save(carAttribute);
    }

    /**
     * This method will remove the associated skus from outfit car.
     * 
     * @param outfitCar
     * @param skusToRemove
     * @param user
     */
    private void removeSkusFromCollection(Car outfitCar, Set<String> skusToRemove, User user) {
        
        if (log.isInfoEnabled()) {
            StringBuilder sb = new StringBuilder();
            for (String s : skusToRemove) {
                sb.append(s).append(" ");
            }
            log.info("Removing skus from OUTFIT_CHILD_PRODUCTS: " + sb.toString());
        }
        // first remove the skus out of the ordered list OUTFIT_CHILD_PRODUCTS attribute
        Attribute attrChildProducts = outfitDao.getAttributeByName(Constants.OUTFIT_CHILD_PRODUCTS);
        CarAttribute outfitChildProducts = outfitDao.findAttributeInCar(outfitCar, attrChildProducts);
        
        if (outfitChildProducts == null) {
            if (log.isWarnEnabled()) {
                log.warn("Unable to find OUTFIT_CHILD_PRODUCTS attribute for outfit car: " + outfitCar.getCarId());
            }
        } else {
            String value = outfitChildProducts.getAttrValue();
            
            if (log.isInfoEnabled()) {
                log.info("OUTFIT_CHILD_PRODUCTS : " + value);
            }
            
            StringBuilder newValue = new StringBuilder();
            if (!StringUtils.isEmpty(value)) {
                String[] skuCodes = value.split(",");
                if (log.isInfoEnabled()) {
                    log.info("OUTFIT_CHILD_PRODUCTS size: " + skuCodes.length);
                }
                for (String s : skuCodes) {
                    if (!skusToRemove.contains(s)) {
                        newValue.append(s).append(",");
                        if (log.isDebugEnabled()) {
                            log.debug("Keeping Sku: " + s);
                        }
                    } else {
                        if (log.isDebugEnabled()) {
                            log.debug("Removing Sku: " + s);
                        }
                    }
                }
            }
            
            if (newValue.length()>0){
                newValue.deleteCharAt(newValue.length()-1); //delete last comma
            }
            
            outfitChildProducts.setAttrValue(newValue.toString());
            outfitChildProducts.setAuditInfo(user);
            carManager.saveOrUpdate(outfitChildProducts);
            
        }
        // next remove Collection Sku records from the db
        List<String> skuListToRemove = new ArrayList<String>();
        skuListToRemove.addAll(skusToRemove);
        String productCode = outfitCar.getVendorStyle().getVendorNumber() + outfitCar.getVendorStyle().getVendorStyleNumber();
        if (!skuListToRemove.isEmpty()) {
            patternCollectionDao.removeCollectionSkus(productCode, skuListToRemove);
        }
    }

    /**
     * Removes all products from a given grouping.
     * 
     * @param groupId 
     */
    public void deleteGrouping(VendorStyle groupStyle, User user) {
        // determine if collection or pattern
        String typeCd = groupStyle.getVendorStyleType().getCode();
        groupStyle.setStatusCd(Status.INACTIVE);
        carManager.saveOrUpdate(groupStyle);
        
        if (typeCd.equals(VendorStyleType.OUTFIT)) {
            // remove all children from collection
            Car outfitCar = carManager.getCarByVendorStyle(groupStyle.getVendorStyleId());
            Map<String,StyleAndStyleColorDTO> stylesToRemoveFromCars =new HashMap<String,StyleAndStyleColorDTO>();
            Set<CarOutfitChild> carOutfitChildren = outfitCar.getCarOutfitChild();
            for (CarOutfitChild child : carOutfitChildren) {
                VendorStyle existingVS = child.getChildCar().getVendorStyle();
                StyleAndStyleColorDTO dto = stylesToRemoveFromCars.get(existingVS.getVendorNumber()+existingVS.getVendorStyleNumber());
                if (dto==null) {
                    dto = new StyleAndStyleColorDTO();
                    dto.setVendorNumber(existingVS.getVendorNumber());
                    dto.setVendorStyleNumber(existingVS.getVendorStyleNumber());
                    dto.getCarOutfitChildren().add(child);
                }
                else {
                    dto.getCarOutfitChildren().add(child);
                }
                
                if (VendorStyleType.PRODUCT.equals(existingVS.getVendorStyleType().getCode())) {
                    //Need to add all the associated colors also.
                    Set<VendorSku> skus = child.getChildCar().getVendorSkus();
                    for (VendorSku s : skus) {
                        dto.getStyleColors().add(s.getColorCode());
                    }
                    stylesToRemoveFromCars.put(existingVS.getVendorNumber()+existingVS.getVendorStyleNumber(),dto);
                }
                else {
                    //existingVS is actually a pattern.  add pattern.
                    stylesToRemoveFromCars.put(existingVS.getVendorNumber()+existingVS.getVendorStyleNumber(),dto);
                }
            }
            removeChildrenFromCollection(outfitCar, stylesToRemoveFromCars, user); // removes full set of caroutfitchild objects
            
        } else {
            Car patternParentCar = carManager.getCarByVendorStyle(groupStyle.getVendorStyleId());
            Set<VendorStyle> patternStyles = new HashSet<VendorStyle>();
            patternStyles.addAll(getChildPatternStyles(groupStyle));
            removeChildrenFromPattern(patternParentCar, patternStyles, user);
        }
        
    }

    /**
     * This method returns the defaultChild of the pattern
     * @param parentStyle
     * @return
     */
    public VendorStyle getDefaultChildOfPattern(VendorStyle parentStyle) {
        VendorStyle defaultStyle = null;
        List<VendorStyle> patternStyles = getChildPatternStyles(parentStyle);
        if (patternStyles!=null) {
            for (VendorStyle child : patternStyles) {
                if (defaultStyle == null || Constants.FLAG_Y.equals(child.getIsDefault())) {
                    defaultStyle = child;
                }
            }
        }
        return defaultStyle;
    }

    /**
     * This method returns true if pattern was merged.  Else, returns false.
     * @param patternCars
     * @return
     */
    private boolean isPatternMerged(List<Car> patternCars) {
        if (patternCars.size() > 1) {
            return false;
        } else {
            List<VendorStyle> childStyles = new ArrayList<VendorStyle>();
            childStyles.add(patternCars.get(0).getVendorStyle());
            
            return isPatternEligibleForMerge(childStyles); // assume if there's one car, if its eligible for merge, its been merged.
        }
    }

    /**
     * This method returns all the pattern's children
     * @param parentStyle
     * @return
     */
    private List<VendorStyle> getChildPatternStyles(VendorStyle parentStyle) {
        //Get all the pattern's children
        VendorStyleSearchCriteria criteria = new VendorStyleSearchCriteria();
        criteria.setVendorStyleId(parentStyle.getVendorStyleId());
        criteria.setChildrenOnly(true);
        return  carManager.searchVendorStyle(criteria);
    }

    /**
     * This method syncs the parent vendor_style object from PIM to CARS.
     */
    public void syncGroupingVendorStyle(VendorStyle parentStyle, GroupingParent parentGrouping) {
        
        if (log.isInfoEnabled()) {
            log.info("Syncing VendorStyle name and description for: " + parentStyle.getVendorNumber()+parentStyle.getVendorStyleNumber());
        }
        boolean modified = false;
        if (parentGrouping.getDescription()!=null &&
                !parentGrouping.getDescription().equals(parentStyle.getDescr())) {
            if (log.isInfoEnabled()) {
                log.info("Updating description: " + parentStyle.getDescr()+" -> " +parentGrouping.getDescription());
            }
            parentStyle.setDescr(parentGrouping.getDescription());
            modified = true;
        }
        
        if (parentGrouping.getName()!=null &&
                !parentGrouping.getName().equals(parentStyle.getVendorStyleName())) {
            if (log.isInfoEnabled()) {
                log.info("Updating VendorStyle name: " + parentStyle.getVendorStyleName()+" -> " +parentGrouping.getName());
            }
            parentStyle.setVendorStyleName(parentGrouping.getName());
            modified = true;
        }
        
        if (modified) {
            carManager.saveOrUpdate(parentStyle);
        }
    }
    
    /**
     * This method checks the current priority of outfit child cars and updates if there is a
     * change in the grouping component
     */
    public void checkPriorityAndUpdate(StyleAndStyleColorDTO dto, GroupingComponent component) {
        String componentProductCode = component.getVendorNumber() + component.getVendorStyle();
        for (CarOutfitChild child : dto.getCarOutfitChildren()) {
            VendorStyle vs = child.getChildCar().getVendorStyle();
            String vendorStyleProductCode = vs.getVendorNumber() + vs.getVendorStyleNumber();
            if (componentProductCode.equalsIgnoreCase(vendorStyleProductCode)) {
                // update outfit child car
                int componentPriority = Integer.parseInt(component.getPriority());

                if (Constants.STYLE.equalsIgnoreCase(component.getComponentType())) {
                    if (child.getPriority() != componentPriority) {
                        child.setPriority(componentPriority);
                        carManager.saveOrUpdate(child);
                    }
                } else if (Constants.STYLE_COLOR.equalsIgnoreCase(component.getComponentType())) {
                    if (!dto.isStyleColor()) {
                        dto.setIsStyleColor(true);
                        if (child.getPriority() != componentPriority) {
                            child.setPriority(componentPriority);
                            carManager.saveOrUpdate(child);
                        }
                    } else if (child.getPriority() > componentPriority) {
                        child.setPriority(componentPriority);
                        carManager.saveOrUpdate(child);
                    }
                } else {
                    // component is a group
                    if (child.getPriority() != componentPriority) {
                        child.setPriority(componentPriority);
                        carManager.saveOrUpdate(child);
                    }
                }
            }
        }
    }

    /**
     * This method logs the groupingMessage XML into db table.
     */
    public void saveGroupingFailureInDB(List<String> styleOrins,String groupingMessage) {
        patternCollectionDao.saveGroupingFailureInDB(styleOrins, groupingMessage);
    }
    
    /**
     * This method returns groupingMessage XML from db, if exists.
     */
    public String getGroupingMessageForStyleOrin(String styleOrin) {
        return patternCollectionDao.getGroupingMessageForStyleOrin(styleOrin);
    }
    
    /**
     * This method updates groupCreateMsgFailure record for given style_orin.
     * @param styleOrin
     * @param flag
     */
    public void setGroupingFailureAsProcessed(String styleOrin, boolean flag) {
        if (flag) {
            patternCollectionDao.setGroupingFailureAsProcessed(styleOrin, Constants.FLAG_Y);
        }
        else {
            patternCollectionDao.setGroupingFailureAsProcessed(styleOrin, Constants.FLAG_N);
        }
    }

    public void syncEffectiveStartDate(VendorStyle parent, String date, User user) {
        Car parentCar = carManager.getCarForStyle(parent.getVendorStyleId());
        Date startDate;
        
        try {
            startDate = getDateFromString(date);
        } catch (Exception e) {
            log.warn("Improperly formatted Effective_Start_Date field came from pim: " + date + " -- Ignoring.");
            return;
        }
        if (startDate == null) { // handle basic data sanitization of the field
            log.warn("No Effective_Start_Date field came in the GroupMod - not updating the effective start date in CARS.");
            return;
        }
        
        CarAttribute effectiveDateCarAttr = outfitManager.getCarAttributeByAttributeName(parentCar, Constants.COLLECTION_EFFECTIVE_DATE);
        try {
            String formattedDate = DateUtils.formatDate(startDate, "MM/dd/yyyy");
            if (effectiveDateCarAttr == null) {
                // if it doesnt exist, we should add it.
                if (log.isInfoEnabled()) log.info("Adding non-existant effective date attr in GroupMod flow " + Constants.COLLECTION_EFFECTIVE_DATE + " - " + formattedDate);
                addCarAttribute(parentCar, Constants.COLLECTION_EFFECTIVE_DATE, formattedDate, user);
            } else {
                if (log.isInfoEnabled()) log.info("Updating effective date attr in GroupMod flow " + Constants.COLLECTION_EFFECTIVE_DATE + " - " + formattedDate);
                effectiveDateCarAttr.setAttrValue(formattedDate);
                carManager.save(effectiveDateCarAttr);
            }
        } catch (Exception e) {
            log.warn("Failed to update CAR attribute: " + Constants.COLLECTION_EFFECTIVE_DATE);
        }
    }

    public void updateCarsPoMessageEsbRetry(List<String> orinsToRetry) {
        patternCollectionDao.updateCarsPoMessageEsbRetry(orinsToRetry);
    }

    private String getChildCarSkus(Car childCar, String componentType, String orin) {
    
        StringBuffer sb = new StringBuffer();
        for (VendorSku vs : childCar.getVendorSkus()) {
            
            if (!Constants.STYLE_COLOR.equalsIgnoreCase(componentType)) {
                sb.append(vs.getBelkSku()); // if its a style record just add all skus
            } else if (StringUtils.substring(orin, 9).equals(vs.getColorCode())) {
                sb.append(vs.getBelkSku()); // else style color - add only skus that match the color code
            }
            sb.append(",");
            
        }
        if (sb.length()>0) {
            sb.deleteCharAt(sb.length()-1); //delete last comma
        }
        return sb.toString();
    
    }
	
    public Car getMergeEilgibleCar(VendorStyle pattern, List<VendorStyle> children) {
        //Check to see if there are CARS already associate with Pattern
        Car patternCar = null;
        CarSearchCriteria carCriteria = new CarSearchCriteria();
        carCriteria.setVendorNumber(pattern.getVendorNumber());
        carCriteria.setVendorStyleNumber(pattern.getVendorStyleNumber());
        carCriteria.setStatusCd(Status.ACTIVE);
        carCriteria.setIgnoreUser(true);
        //Do not search based on the Pattern Child Search
        carCriteria.setSearchChildVendorStyle(false);
        carCriteria.setSortColumnNm("3");
        carCriteria.setSortOrder("desc");
        List<Car> cars = carManager.searchCars(carCriteria);
        
        // get the first active car associated with the pattern that hasnt been sent to cmp
        for (Car c : cars) {
            if (!c.getContentStatus().getCode().contains("CMP")) {
                patternCar = c;
                break;
            }
        }
        
        if (patternCar == null && (children != null && !children.isEmpty())) {
            if (log.isInfoEnabled()) log.info("No existing, active converted cars that haven't been sent to CMP exist. "
                                            + "Looking for child cars to convert among children.");
            for (VendorStyle vs : children) {
                carCriteria = new CarSearchCriteria();
                carCriteria.setVendorNumber(vs.getVendorNumber());
                carCriteria.setVendorStyleNumber(vs.getVendorStyleNumber());
                carCriteria.setStatusCd(Status.ACTIVE);
                carCriteria.setIgnoreUser(true);
                //Search based on the Pattern Child Search
                carCriteria.setSearchChildVendorStyle(false);
                cars = carManager.searchCars(carCriteria); 
                
                for (Car car : cars) {
                    if (!car.getContentStatus().getCode().contains("CMP")) {
                        if (log.isInfoEnabled()) log.info("Found CAR to convert and use as the merge elegible car: " + car.getCarId());
                        patternCar = car;
                        patternCar.setVendorStyle(pattern); // convert VS
                        carManager.save(patternCar);
                        break;
                    }
                }
                if (patternCar != null) {
                    break;
                }
            }
        } else if (patternCar == null) {
            log.error("Couldn't find any cars eligible for merging or converting.");
        }
        
        
        if (log.isInfoEnabled()) {
            if (patternCar == null) {
                log.info("No cars eligible for merging this pattern, will convert one child and merge the rest into it.");
            } else {
                log.info("Eligible car found for merging pattern: " + patternCar);
            }
        }
        return patternCar; // returns null if no cars in pattern
    }
}
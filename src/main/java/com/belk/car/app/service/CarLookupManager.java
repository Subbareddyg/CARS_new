package com.belk.car.app.service;

import java.io.Serializable;
import java.util.List;

import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.appfuse.service.UniversalManager;

import com.belk.car.app.dto.LateCarsAssociationDTO;
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
import com.belk.car.app.model.statistics.CacheStats;


/**
 * Business Service Interface to talk to persistence layer and
 * retrieve values for drop-down choice lists.
 *
 * @author 	Antonio Gonzalez
 */
public interface CarLookupManager extends UniversalManager {
	
	 /**
     * Retrieves all possible roles from persistence layer
     * @return List of LabelValue objects
     */
    List<Role> getAllRoles();
	
    UserType getUserType(String userTypeCd) ;

    List<UserType> getActiveUserTypes() ;

    Role getRole(String roleCd) ;
    
    List<Department> getAllDepartments();
    
    List<LateCarsAssociationDTO> getLateCarsAssocById(Long LateCarsGroupId);
    
    int getLateCarsAssocByDeptId(Long LateCarsGroupId,Long deptId);
    
    /**
     * Retrieves a department by departmentId.  An exception is thrown if department not found
     *
     * @param departmentId the identifier for the department
     * @return Department
     */   
    Department getDepartmentById(Long departmentId);
    
    /**
     * Retrieves a vendor by vendorId.  An exception is thrown if a vendor is not found
     * 
     * @param vendorId the identifier for the vendor
     * @return	Vendor
     */
    Vendor getVendorById(Long vendorId);
    
    LateCarsGroup getLateCarGroupById(Long lateCarsGroupId);
    
    /**
     * Retrieves all available and active vendors
     * 
     * @return List<Vendor>
     */
    List<Vendor> getAllVendors();
    
    /**
     * Retrieves all available and active vendor style
     * 
     * @return List<VendorStyle>
     */
    List<VendorStyle> getAllVendorStyles();

    /**
     * Retrieves a note by noteId.  An exception is thrown if a note is not found
     * 
     * @param noteId the identifier for the note
     * @return	NoteType
     */
    NoteType getNoteType(String noteType);
    
    
    /**
     * 
     * @return List of NoteTypes
     */
    List<NoteType> getAllNoteTypes();
    
    
    /**
     * 
     * @return list of all ProductTypes
     */
    List<ProductType> getAllProductTypes();
    
    /**
     * 
     * @return list of all Classifications
     */
    List<Classification> getAllClassifications();
    
    /**
     * 
     * @param productTypeId
     * @return ProductType
     */
    ProductType getProductTypeById(Long productTypeId);
    
    /**
     * 
     * @param classificationId
     * @return Classificaton
     */
    Classification getClassificationById(Long classificationId);
    
    /**
     * Retrieves a notificationType by notificationTypeCode.  An exception is thrown if a notificationType is not found
     * 
     * @param notificationTypeCode the identifier for the notificationType
     * @return	NotificationType
     */
    NotificationType getNotificationType(String notificationTypeCode);


	List<ImageType> getAllImageTypes() ;
	
	ImageType getImageType(String imageType) ;
	
	List<ImageLocationType> getAllImageLocationTypes();

	ImageLocationType getImageLocationType(String imageLocationType) ;

	List<ImageSourceType> getAllImageSourceTypes() ;
	
	ImageSourceType getImageSourceType(String imageSourceType) ;

	List<SampleSourceType> getAllSampleSourceTypes() ;
	
	SampleSourceType getSampleSourceType(String sampleSourceType) ;

	List<HtmlDisplayType> getAllHtmlDisplayTypes() ;
	
	HtmlDisplayType getHtmlDisplayType(String htmlDisplayTypeCd) ;

	List<ValidationExpression> getAllValidationExpressions() ;
	
	ValidationExpression getValidationExpression(String validationExpressionCd) ;

	List<SampleTrackingStatus> getAllSampleTrackingStatus() ;
	
	SampleTrackingStatus getSampleTrackingStatus(String sampleTrackingStatusCd) ;

	List<ManualCarProcessStatus> getAllManualCarProcessStatus() ;
	
	ManualCarProcessStatus getManualCarProcessStatus(String manualCarProcessStatusCd) ;
	
	List<Classification> searchClassifications(String classificationId,String classificationName);
	
	List<ProductType> searchProductTypes(String productTypeName, Long classificationId);

	Object getById(Class cls, Serializable id);
	
	CacheStats getCacheStatistics();
	
	List<User> getVendors();
	
	AttributeValueProcessStatus getAttributeValueProcessStatus(String attValueProcessStatusCd);
	
	List<ContentStatus> getAllContentStatuses();
	
	ContentStatus getContentStatus(String contentStatusCd);

	List<PatternProcessingRule> getAllPatternProcessingRules();

	List<VendorStyleType> getAllVendorStyleTypes();
	
	List<VendorStyleType> getPatternTypes(boolean isSuperAdmin);
	
	//===============Added by Syntel for Dropship Requirement================//
	/**
	 * @return List<SourceType>
	 * This method is used to load All Source Type for search requirement.
	 */
	List<SourceType> getAllSourceType();
	
	/**
	 * Gets the name of the product group and returns a list of product group with 
	 * that name.
	 * 
	 * @param name
	 * @return List<ProductGroup>
	 */
	List<ProductGroup> getProductGroup(String name);

	List<ProductType> getProductTypes(String productTypeIds);
	
	/*
	 * This method is used for getting the ACTIVE users.
	 */
	List<User> getUsers();
	/*
	 * This method is used for getting the ACTIVE users count.
	 */
	Long getUsersCount(String userFName,String userLName,String emailId);
	
	List<MediaCompassImage> getAllMCImages(String strImageStatus);
	/*
	 *this method is added to search vendor by Name or Number. 
	 */
	List<Vendor> getAllVendorsByVendorName(String vendorName, String vendorNumber);
}

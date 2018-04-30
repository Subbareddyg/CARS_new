package com.belk.car.app.dao;

import java.io.Serializable;
import java.util.List;

import org.appfuse.dao.UniversalDao;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.hibernate.stat.Statistics;

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

public interface CarLookupDao extends UniversalDao {

	Object getById(Class cls, Serializable id);
	
	/**
	 * Returns all Roles ordered by name
	 * @return populated list of roles
	 */
	List<Role> getRoles();

	Role getRole(String roleCd);

	UserType getUserType(String userTypeCd);

	List<UserType> getActiveUserTypes();

	List<Department> getAllDepartments();
	
	List<LateCarsAssociationDTO> getLateCarsAssocById(Long lateCarsGroupId); 
	
	int getLateCarsAssocByDeptId(Long LateCarsGroupId,Long deptId);

	Department getDepartmentById(Long departmentId);

	Vendor getVendorById(Long vendorId);
	
	LateCarsGroup getLateCarsGroupById(Long lateCarsGroupId);

	List<Vendor> getAllVendors();
	
	List<VendorStyle> getAllVendorStyles();

	NoteType getNoteType(String noteId);

	List<NoteType> getAllNoteTypes();

	Classification getClassificationById(Long classificationId);

	ProductType getProductTypeById(Long productTypeId);
	
    List<ProductType> getAllProductTypes();
    
    List<Classification> getAllClassifications();

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

	Statistics getCacheStatistics();
	
	List<User> getVendors();
	
	AttributeValueProcessStatus getAttributeValueProcessStatus(String attrValueProcessStatusCd);
	
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
	List<SourceType> getAllSourceType() ;
	
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
	 * This method is used to get the total user count.
	 */
	Long getUsersCount(String firstName,String lastName ,String email);
	
	//get media_compass_images from given status
	public List<MediaCompassImage> getAllMCImages(String strImageStatus);
	
	/*
	 * This method is added to vendor based on name and number.
	 */
	public List<Vendor> getAllVendorsByVendorName(String vendorName, String vendorNumber);
}

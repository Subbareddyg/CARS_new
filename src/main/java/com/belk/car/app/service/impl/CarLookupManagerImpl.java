/**
 * 
 */
package com.belk.car.app.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.CacheManager;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.appfuse.service.impl.UniversalManagerImpl;
import org.hibernate.stat.SecondLevelCacheStatistics;
import org.hibernate.stat.Statistics;

import com.belk.car.app.dao.CarLookupDao;
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
import com.belk.car.app.service.CarLookupManager;

/**
 * @author antoniog
 * 
 */
public class CarLookupManagerImpl extends UniversalManagerImpl implements
		CarLookupManager {

	private CarLookupDao dao;

	/**
	 * Method that allows setting the DAO to talk to the data store with.
	 * 
	 * @param dao
	 *            the dao implementation
	 */

	public void setDao(CarLookupDao dao) {
		this.dao = dao;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Role> getAllRoles() {
		return dao.getRoles();
	}

	/**
	 * 
	 */
	public Object getById(Class cls, Serializable id) {
		return dao.getById(cls, id);
	}

	/**
	 * {@inheritDoc}
	 */
	public UserType getUserType(String userTypeCd) {
		return dao.getUserType(userTypeCd);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<UserType> getActiveUserTypes() {
		return dao.getActiveUserTypes();
	}

	public Role getRole(String roleCd) {
		return dao.getRole(roleCd);
	}

	public List<Department> getAllDepartments() {
		return dao.getAllDepartments();
	}
	
	@Override
	public List<LateCarsAssociationDTO> getLateCarsAssocById(
			Long LateCarsGroupId) {
		return dao.getLateCarsAssocById(LateCarsGroupId);
	}
	
	@Override
	public int getLateCarsAssocByDeptId(Long LateCarsGroupId, Long deptId) {
		return dao.getLateCarsAssocByDeptId(LateCarsGroupId, deptId);
	}

	public Department getDepartmentById(Long departmentId) {
		return dao.getDepartmentById(departmentId);
	}

	public Vendor getVendorById(Long vendorId) {
		return dao.getVendorById(vendorId);
	}
	
	public  LateCarsGroup getLateCarGroupById(Long lateCarsGroupId) {
		return dao.getLateCarsGroupById(lateCarsGroupId);
	}

	public List<Vendor> getAllVendors() {
		return dao.getAllVendors();
	}

	public List<VendorStyle> getAllVendorStyles() {
		return dao.getAllVendorStyles();
	}

	public NoteType getNoteType(String noteId) {
		return dao.getNoteType(noteId);
	}

	public List<NoteType> getAllNoteTypes() {
		return dao.getAllNoteTypes();
	}

	public Classification getClassificationById(Long classificationId) {
		return dao.getClassificationById(classificationId);
	}

	public ProductType getProductTypeById(Long productTypeId) {
		return dao.getProductTypeById(productTypeId);
	}

	public List<Classification> getAllClassifications() {
		return dao.getAllClassifications();
	}

	public List<ProductType> getAllProductTypes() {
		return dao.getAllProductTypes();
	}

	public NotificationType getNotificationType(String notificationTypeCode) {
		return dao.getNotificationType(notificationTypeCode);
	}

	public List<ImageLocationType> getAllImageLocationTypes() {
		return dao.getAllImageLocationTypes();
	}

	public List<ImageSourceType> getAllImageSourceTypes() {
		return dao.getAllImageSourceTypes();
	}

	public List<ImageType> getAllImageTypes() {
		return dao.getAllImageTypes();
	}

	public List<SampleSourceType> getAllSampleSourceTypes() {
		return dao.getAllSampleSourceTypes();
	}

	public ImageLocationType getImageLocationType(String imageLocationType) {
		return dao.getImageLocationType(imageLocationType);
	}

	public ImageSourceType getImageSourceType(String imageSourceType) {
		return dao.getImageSourceType(imageSourceType);
	}

	public ImageType getImageType(String imageType) {
		return dao.getImageType(imageType);
	}

	public SampleSourceType getSampleSourceType(String sampleSourceType) {
		return dao.getSampleSourceType(sampleSourceType);
	}

	public List<HtmlDisplayType> getAllHtmlDisplayTypes() {
		return dao.getAllHtmlDisplayTypes();
	}

	public List<ValidationExpression> getAllValidationExpressions() {
		return dao.getAllValidationExpressions();
	}

	public HtmlDisplayType getHtmlDisplayType(String htmlDisplayTypeCd) {
		return dao.getHtmlDisplayType(htmlDisplayTypeCd);
	}

	public ValidationExpression getValidationExpression(
			String validationExpressionCd) {
		return dao.getValidationExpression(validationExpressionCd);
	}

	public List<SampleTrackingStatus> getAllSampleTrackingStatus() {
		return dao.getAllSampleTrackingStatus();
	}

	public SampleTrackingStatus getSampleTrackingStatus(
			String sampleTrackingStatusCd) {
		return dao.getSampleTrackingStatus(sampleTrackingStatusCd);
	}

	public List<ManualCarProcessStatus> getAllManualCarProcessStatus() {
		return dao.getAllManualCarProcessStatus();
	}

	public ManualCarProcessStatus getManualCarProcessStatus(
			String manualCarProcessStatusCd) {
		return dao.getManualCarProcessStatus(manualCarProcessStatusCd);
	}

	public List<Classification> searchClassifications(String classificationId,
			String classificationName) {
		return dao.searchClassifications(classificationId, classificationName);
	}

	public List<ProductType> searchProductTypes(String productTypeName,
			Long classificationId) {
		return dao.searchProductTypes(productTypeName, classificationId);
	}

	public CacheStats getCacheStatistics() {

		CacheStats cacheStats = null;
		Statistics stats = dao.getCacheStatistics();

		if (stats != null) {
			SecondLevelCacheStatistics sLevelStats = null;
			stats.setStatisticsEnabled(true);
			// Set global counts
			cacheStats = new CacheStats(CacheStats.TOTAL, stats
					.getSecondLevelCacheHitCount(), stats
					.getSecondLevelCacheMissCount());
			// Get all regions
			CacheManager cacheManager = CacheManager.getInstance();
			if (cacheManager != null) {
				for (String regionName : cacheManager.getCacheNames()) {
					if (StringUtils.isNotBlank(regionName)) {
						sLevelStats = stats
								.getSecondLevelCacheStatistics(regionName);
						// Got cached entity now set the values;
						if (sLevelStats != null) {
							CacheStats stats1 = new CacheStats(regionName, sLevelStats
									.getHitCount(), sLevelStats
									.getMissCount());
							stats1.setInMemoryCount(sLevelStats.getElementCountInMemory());
							cacheStats.getDomainStats().add(stats1);
						}

					}
				}

			}
			return cacheStats;
		} else {
			return new CacheStats(CacheStats.TOTAL, 0, 0);
		}

	}

	public List<User> getVendors() {		
		return dao.getVendors();
	}

	public AttributeValueProcessStatus getAttributeValueProcessStatus(String attValueProcessStatusCd) {		
		return dao.getAttributeValueProcessStatus(attValueProcessStatusCd);
	}
	
	public List<ContentStatus> getAllContentStatuses() {
		return dao.getAllContentStatuses();
	}
	
	public ContentStatus getContentStatus(String contentStatusCd) {
		return dao.getContentStatus(contentStatusCd);
	}

	public List<PatternProcessingRule> getAllPatternProcessingRules() {
		return dao.getAllPatternProcessingRules();
	}

	public List<VendorStyleType> getAllVendorStyleTypes() {
		return dao.getAllVendorStyleTypes();
	}
	
	public List<VendorStyleType> getPatternTypes(boolean isSuperAdmin) {
		return dao.getPatternTypes(isSuperAdmin);
	}
	
	//===============Added by Syntel for Dropship Requirement================//
	/**
	 * @return List<SourceType>
	 * This method is used to load All Source Type for search requirement.
	 */
	public List<SourceType> getAllSourceType() {
		return dao.getAllSourceType();
	}
	
	/**
	 * Gets the name of the product group and returns a list of product group with 
	 * that name.
	 * 
	 * @param name
	 * @return List<ProductGroup>
	 */
	public List<ProductGroup> getProductGroup(String name){
		return dao.getProductGroup(name);
	}

	/**
	 * Gets all the product types based on the String ids that were passed 
	 * as the parameter.
	 * 
	 * @param productTypeIds
	 * @return
	 */
	public List<ProductType> getProductTypes(String productTypeIds){
		return dao.getProductTypes(productTypeIds);
	}
	
	/*
	 * This method is used for getting the ACTIVE users.
	 */
	
	public List<User> getUsers() {		
		return dao.getUsers();
	}
	
	public Long getUsersCount(String firstName,String lastName,String email) {		
		return dao.getUsersCount(firstName,lastName,email);
	}
	
	public List<MediaCompassImage> getAllMCImages(String strImageStatus){
		return dao.getAllMCImages(strImageStatus);
	}
	/*
	 * (non-Javadoc)
	 * @see com.belk.car.app.service.CarLookupManager#getAllVendorsByVendorName(java.lang.String, java.lang.String)
	 * This method is added to search vendor based on Name and Number.
	 */
	@Override
	public List<Vendor> getAllVendorsByVendorName(String vendorName, String vendorNumber) {
		
		List<Vendor> sampleVendor=dao.getAllVendorsByVendorName(vendorName, vendorNumber);
		
		if(sampleVendor==null)
		{
			sampleVendor=new ArrayList<Vendor>();
		}
		
		return sampleVendor;
	}
	
	
}

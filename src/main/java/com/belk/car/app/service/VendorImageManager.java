package com.belk.car.app.service;

import java.util.Date;
import java.util.List;

import org.appfuse.model.User;
import org.appfuse.service.UniversalManager;
import org.springframework.dao.DataAccessException;

import com.belk.car.app.dto.UploadImagesDTO;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.Image;
import com.belk.car.app.model.MediaCompassImage;
import com.belk.car.app.model.vendorimage.VendorImage;

/**
 * Business Service Interface to talk to persistence layer and 
 * manage upload form .
 * 
 */
public interface VendorImageManager extends UniversalManager {

    
    List<Car> getCarsForUser(User usr,boolean disableAssociations) throws DataAccessException;
    
    public List<Image> getImagesByVendorImageStatus(String[] vendorImageStatus) throws Exception;	
    
    public List<Image> getImagesByVendorImageStatus(String[] vendorImageStatus, Date date) throws Exception;
    
    public Object save(Object ob);
    
    public Object saveOrUpdate(Object ob);
    
    public List<Image> getRemovedVendorImages(String strLastJobRunTime);
    
	Long getImageIdFromSeq();
	
	public boolean saveImageData(UploadImagesDTO uploadImagesDTO);	
	
	public Car getCarByVendorImageId(long vendorImageId);

	public String getVendorImageUploadDir() throws Exception;
	public String getPimImageUploadDir() throws Exception;

	public String getPimImageArchiveDir() throws Exception;

	public String getRRDImageUploadedDir() throws Exception;

    public boolean UpdateImageData(UploadImagesDTO uploadImagesDTO, String imageId); 
    
	public VendorImage getVendorImageDetailsById(long vendorImageId);
	
	public VendorImage getVendorImageDetailsByPimImageId(String vendorPimImageId);

	List<String> getNextShotTypeCodeList(long vendorStyleId, String colorCode,Long carId);
	
	public MediaCompassImage getMediaComapssByCarId(long carId);
}

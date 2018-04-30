package com.belk.car.app.dao;

import java.util.Date;
import java.util.List;

import org.appfuse.dao.UniversalDao;
import org.appfuse.model.User;

import com.belk.car.app.model.Car;
import com.belk.car.app.model.Image;
import com.belk.car.app.model.MediaCompassImage;
import com.belk.car.app.model.vendorimage.VendorImage;

public interface VendorImageManagementDao extends UniversalDao{

	List<Car> getCarsForUser(User usr);
	
	public List<Image> getImagesByVendorImageStatus(String[] vendorImageStatus) throws Exception;
	
	public List<Image> getImagesByVendorImageStatus(String[] vendorImageStatus, Date date) throws Exception;
	
	public Object save(Object ob);
	
	public List<Image> getRemovedVendorImages(String strLastJobRunTime);
	
	Long getImageIdFromSeq();

	public Object saveOrUpdate(Object ob);
	
	public Car getCarByVendorImageId(long vendorImageId);

	public VendorImage getVendorImageDetailsById(long vendorImageId);

	public List<String> getNextShotTypeCodeList(long vendorStyleId, String colorCode,Long carId);
	
	//below method added for Production issue
	public MediaCompassImage getMediaComapssByCarId(long carId);
	//get vendor image based on the pim image id
	public VendorImage getVendorImageDetailsByPimImageId(String vendorPimImageId);
	 
}

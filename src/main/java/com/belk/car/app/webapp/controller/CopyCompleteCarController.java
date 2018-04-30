package com.belk.car.app.webapp.controller;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.json.JSONObject;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarImage;
import com.belk.car.app.model.CarImageId;
import com.belk.car.app.model.CarSample;
import com.belk.car.app.model.CarSampleId;
import com.belk.car.app.model.CarSkuAttribute;
import com.belk.car.app.model.SkuAttributeDelete;
import com.belk.car.app.model.SourceType;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorSkuDelete;

public class CopyCompleteCarController extends BaseController {

	private static final String CARID = "carId";

	private transient final Log log = LogFactory.getLog(CopyCompleteCarController.class);
	
	public ModelAndView handleRequest(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Long carId = ServletRequestUtils.getLongParameter(request, CARID);
		if (carId == null) {
			if(log.isDebugEnabled()){
				log.debug("CAR id was null. Redirecting...");
			}
            return new ModelAndView("redirect:dashBoard.html");
        }
		if(log.isDebugEnabled()){
			log.debug("Copy Complete CAR ..CarId :"+ carId);
		}
		Car car = this.carManager.getCarFromId(new Long(carId));
		
		
		User user = this.getLoggedInUser();
		request.setAttribute("user", user);
		Map model = new HashMap();
		String result ="";
		if (car != null) {
			result=processCopyCompleteCar(car,request);
			if("Complete".equals(result)){
				model.put("Complete", "Yes");
				if(log.isDebugEnabled()){
					log.debug("---> copCompleteyCar() success.");
				}
			} else if("NoSkus".equals(result)){
				model.put("Complete", "NoSkus");
				if(log.isDebugEnabled()){
					log.debug("---> copCompleteyCar() not success...No Skus found");
				}
				
			}
		}
		request.setAttribute("jsonObj", new JSONObject(model));
		return new ModelAndView(getAjaxView());
	}
	
	/*
	 * car- Car where the copy applied
	 * request - request attribute
	 */
	private String processCopyCompleteCar(Car car,HttpServletRequest request){
		
		
		Set<VendorSku> carSkus=car.getVendorSkus();
		if(log.isDebugEnabled()){
			log.debug("number of skus in car "+car.getCarId() +" :"+carSkus.size());
		}
		Car fromCar=null;
		
		VendorSkuDelete deletedSku=null;
		VendorSku toSku=null;
		VendorSku deletedCarSku=null;
		long carId= 0L;
		String belkUpc=null;
		List<String> skus=new ArrayList<String>();
		List<Car> completedCars=new ArrayList<Car>();
		List<Long> carList=new ArrayList<Long>();
		// Identify the deleted sku
		for(VendorSku carSku:carSkus){
			 belkUpc=carSku.getBelkUpc();
			 if(log.isDebugEnabled()){
				 log.debug("searching for belkUpc :"+ belkUpc);
			 }
			// check if the sku present in temp deleted sku table
			deletedSku=carManager.getDeletedSku(belkUpc);
			if(log.isDebugEnabled()){
				log.debug("deleted sku from temp table :"+ deletedSku);
			}
			if(deletedSku!=null){
				carId=deletedSku.getCarId();
				if(log.isDebugEnabled()){
					log.debug("CAR id from the deleted sku:"+carId);
				}
				skus.add(deletedSku.getBelkUpc());
			}  else {
				// check if the sku present in deleted car (Car status DELETED)
				deletedCarSku=carManager.getDeletedCarSku(belkUpc);
				if(log.isDebugEnabled()){
					log.debug("deleted Car  :"+ deletedCarSku);
				}
				if(deletedCarSku!= null){
					carId=deletedCarSku.getCar().getCarId();
					if(log.isDebugEnabled()){
						log.debug("CAR id from the deleted Car:"+carId);
					}
					skus.add(deletedCarSku.getBelkSku());
				}
			}
			
			
			if(carId!=0){
				// getting the Car from car id 
				fromCar=getCarManager().getCarFromId(carId);
				completedCars.add(fromCar);
				// getting the Sku
				toSku=carManager.getSku(belkUpc);
				String toSource="";
				String fromSource="";
				if(log.isDebugEnabled()){
						fromSource=fromCar.getSourceType().getSourceTypeCd();
						log.debug("Source type fromCar :"+fromSource);
						toSource=car.getSourceType().getSourceTypeCd();
						log.debug("Source type toCar :"+toSource);
					}
				
				// consider the source type of a CAR : copy complete will be applied to  po-dropship CAR and dropship-po CAR only,
				// not po CAR -po CAR and dropsip CAR -dropship CAR
				if(!fromSource.equals(toSource)) {
						if(log.isDebugEnabled()){
								log.debug("Source type of two CARS are different");
						}
							// Copy the Attributes
						for (CarAttribute carAttrFrom : fromCar.getCarAttributes()) {
							for (CarAttribute carAttrTo : car.getCarAttributes()) {
								if (carAttrFrom.getAttribute().getAttributeId() == carAttrTo.getAttribute().getAttributeId()) {
									carAttrTo.setAttrValue(carAttrFrom.getAttrValue());
									}
								}
						}
						if(log.isDebugEnabled()){
							log.debug("Copy attributes completed");
						}
						
						if(deletedSku!=null){
							if(log.isDebugEnabled()){
								log.debug(" copy the deleted sku attributes");
							}
							copyDeletedSkuAttributes(deletedSku,toSku,fromSource,toSource);
						} else if( deletedCarSku !=null){
							if(log.isDebugEnabled()){
								log.debug("copy the sku attributes from deleted car");
							} 
							copySkuAttributesfromDeletedCar(deletedCarSku,toSku,fromSource,toSource);
						}
							
						// Copy the car samples
						Set<CarSample> fromSamples=fromCar.getCarSamples();
						List<Long> sampleIdList=new ArrayList<Long>();
						Set<CarSample> existingSamples= car.getCarSamples();
						for(CarSample ext:existingSamples){
							long sampleId=ext.getSample().getSampleId();
							sampleIdList.add(sampleId);
						}
						// check if same car id found for the sku
						if(fromSamples!=null && !carList.contains(carId)){
							CarSample carSample=null;
							CarSampleId csId =null;
							for(CarSample csf:fromSamples){
									if(log.isDebugEnabled()){
										log.debug("Sample id:"+csf.getSample().getSampleId() + " from car id:"+fromCar.getCarId());
									}
									if(!sampleIdList.contains(csf.getSample().getSampleId())){
										csId=new CarSampleId();
										csId.setCarId(car.getCarId());
										csId.setSampleId(csf.getSample().getSampleId());
										
										carSample = new CarSample();
										carSample.setId(csId);
										carSample.setCar(car);
										carSample.setSample(csf.getSample());
										setAuditInfo(request, carSample);
										this.getCarManager().save(carSample) ;
										if (car.getCarSamples()!= null) {
											car.getCarSamples().add(carSample);
										}
									}else {
										if(log.isDebugEnabled()){
											log.debug("Sample id:"+csf.getSample().getSampleId() + " already found in car "+car.getCarId());
										}
									}
								}
							}
						
							
						// Copy the car images
						Set<CarImage> fromImages=fromCar.getCarImages();
						List<Long> imageIdList=new ArrayList<Long>();
						Set<CarImage> existingImages= car.getCarImages();
						for(CarImage ext:existingImages){
							long imageId=ext.getImage().getImageId();
							imageIdList.add(imageId);
						}
						// check if same car id found for the sku
						if(fromImages!=null && !carList.contains(carId)){
							CarImage ci=null;
							CarImageId cId=null;
							for(CarImage csi:fromImages){
									if(log.isDebugEnabled()){
										log.debug("Image id:"+csi.getImage().getImageId()+ " from car id:"+fromCar.getCarId());
									}
									if(!imageIdList.contains(csi.getImage().getImageId())){
										cId= new CarImageId();
										cId.setCarId(car.getCarId());
										
										cId.setImageId(csi.getImage().getImageId());
										ci = new CarImage();
										ci.setId(cId);
										ci.setCar(car);
										ci.setImage(csi.getImage());
										this.setAuditInfo(request, ci) ;
										this.getCarManager().save(ci) ;
										if (car.getCarImages()!= null){
											car.getCarImages().add(ci);
										}
									} else {
										if(log.isDebugEnabled()){
											log.debug("Image id:"+csi.getImage().getImageId()+ " already found in car :"+car.getCarId());
										}
									}
							}
						}
							
						//Copy the Product Name and Description
						if (fromCar.getVendorStyle() != null && car.getVendorStyle() != null) {
							if (StringUtils.isNotBlank(fromCar.getVendorStyle().getVendorStyleName())) {
								car.getVendorStyle().setVendorStyleName(fromCar.getVendorStyle().getVendorStyleName());
							}
							if (StringUtils.isNotBlank(fromCar.getVendorStyle().getDescr())) {
								car.getVendorStyle().setDescr(fromCar.getVendorStyle().getDescr());
							}
						}
						carManager.save(car);
						if(log.isDebugEnabled()){
								log.debug("Copy completed ...............for car: "+car.getCarId());
							    }
						}// source type check 
						carList.add(carId);
				} else { // car id not found
					if(log.isDebugEnabled()){
						log.debug("Deleted Sku not found");
					}
				}
			
		}// for each vendor sku
		
		
		if(skus.isEmpty()){
			log.debug("No skus found");
			return "NoSkus";
		}
		
		if(log.isDebugEnabled()){
			log.debug("Copy Completed from the list of cars:"+completedCars +" to car: "+car.getCarId());
		}
		
		return "Complete";
	}
	
	
	
	
	private void copyDeletedSkuAttributes(VendorSkuDelete vsd,VendorSku toSku,String fromSource, String toSource){
		List<SkuAttributeDelete> skuAtrs=carManager.getSkuAttributesDelete(vsd.getCarSkuId());
		CarSkuAttribute csAtrs=null;
		Map<Long, CarSkuAttribute> saMap = toSku.getAttributeMap() ;
		if(skuAtrs!=null && skuAtrs.size() > 0){
			for(SkuAttributeDelete skuAtr:skuAtrs){
				long attrId=skuAtr.getAttribute().getAttributeId();
				String attrName=skuAtr.getAttribute().getName();
				// if the existing sku attribute already exists update attribute value
				if (saMap.containsKey(attrId)) {
					csAtrs = saMap.get(attrId);
					// Do not update the drop ship value for the newly created sku
					// either in Dropship or PO
					if(!"IS_DROPSHIP".equals(attrName)){
						// set online only attribute as N if Dropship to PO 
						// do not copy the attribute value Y for the online only attribute if exists at sku level
						if(SourceType.DROPSHIP.equals(fromSource) && SourceType.PO.equals(toSource)){
							if(skuAtr.getAttribute().getBlueMartiniAttribute().indexOf("On Line") > 0){
								csAtrs.setAttrValue("No");
							}
						} else {
							csAtrs.setAttrValue(skuAtr.getAttrValue());
						}
					  }
				} else {
					csAtrs=new CarSkuAttribute();
					csAtrs.setAttribute(skuAtr.getAttribute());
					csAtrs.setAttrValue(skuAtr.getAttrValue());
					csAtrs.setAuditInfo(getLoggedInUser());
					csAtrs.setVendorSku(toSku);
				}
				
				carManager.save(csAtrs);
				toSku.getCarSkuAttributes().add(csAtrs);
				if(log.isDebugEnabled()){
					log.debug("Copy Deleted Sku attributes completed");
				}
			}
		} else {
			if(log.isDebugEnabled()){
				log.debug("No DeletedSkuAttributes found to copy...");
			}
		}
	}
	
	
	
	private void copySkuAttributesfromDeletedCar(VendorSku deletedCarSku,VendorSku toSku,String fromSource, String toSource){
		Set<CarSkuAttribute> csAtrs=deletedCarSku.getCarSkuAttributes();
		CarSkuAttribute cs=null;
		Map<Long, CarSkuAttribute> saMap = toSku.getAttributeMap() ;
		if(csAtrs!=null && !csAtrs.isEmpty()){
			for(CarSkuAttribute csa:csAtrs){
				long attrId=csa.getAttribute().getAttributeId();
				String attrName=csa.getAttribute().getName();
				if (saMap.containsKey(attrId)) {
					cs = saMap.get(attrId);
					// Do not update the drop ship value for the newly created sku
					if(!"IS_DROPSHIP".equals(attrName)){
						// set online only attribute as N if Dropship to PO 
						// do not copy the attribute value Y for the online only attribute if exists at sku level
						if(SourceType.DROPSHIP.equals(fromSource) && SourceType.PO.equals(toSource)){
							if(csa.getAttribute().getBlueMartiniAttribute().indexOf("On Line") > 0){
								cs.setAttrValue("No");
							}
						} else {
							cs.setAttrValue(csa.getAttrValue());
						}
					}
				} else {
					cs=new CarSkuAttribute();
					cs.setAttribute(csa.getAttribute());
					cs.setAttrValue(csa.getAttrValue());
					cs.setAuditInfo(getLoggedInUser());
					cs.setVendorSku(toSku);
				}
				carManager.save(cs);
				toSku.getCarSkuAttributes().add(cs);
				if(log.isDebugEnabled()){
					log.debug("Copy Deleted CAR sku attributes completed");
				}
			}
		} else {
			if(log.isDebugEnabled()){
				log.debug("No Deleted CAR sku attributes found to copy...");
			}
		}
		
	}
	
}



	
	

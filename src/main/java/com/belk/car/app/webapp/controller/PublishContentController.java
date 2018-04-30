package com.belk.car.app.webapp.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarDBPromotionChild;
import com.belk.car.app.model.CarOutfitChild;
import com.belk.car.app.model.ContentStatus;
import com.belk.car.app.model.SourceType;
import com.belk.car.app.model.UserType;

public class PublishContentController extends BaseController {
	private static final Log log = LogFactory
	.getLog(PublishContentController.class);
	
	public static Set<String> privileges = new HashSet<String>();
	static{
		privileges.add(UserType.BUYER+"_"+ContentStatus.IN_PROGRESS+"_"+ContentStatus.PUBLISHED);
		privileges.add(UserType.BUYER+"_"+ContentStatus.IN_PROGRESS+"_"+ContentStatus.SENT_TO_CMP);
		privileges.add(UserType.BUYER+"_"+ContentStatus.IN_PROGRESS+"_"+ContentStatus.RESEND_DATA_TO_CMP);
		privileges.add(UserType.BUYER+"_"+ContentStatus.SENT_TO_CMP+"_"+ContentStatus.RESEND_TO_CMP);
		privileges.add(UserType.BUYER+"_"+ContentStatus.SENT_TO_CMP+"_"+ContentStatus.RESEND_DATA_TO_CMP);
		privileges.add(UserType.CONTENT_WRITER+"_"+ContentStatus.PUBLISHED+"_"+ContentStatus.APPROVAL_REQUESTED);
		privileges.add(UserType.CONTENT_WRITER+"_"+ContentStatus.REJECTED+"_"+ContentStatus.APPROVAL_REQUESTED);
		privileges.add(UserType.CONTENT_MANAGER+"_"+ContentStatus.PUBLISHED+"_"+ContentStatus.APPROVAL_REQUESTED);
		privileges.add(UserType.CONTENT_MANAGER+"_"+ContentStatus.APPROVAL_REQUESTED+"_"+ContentStatus.APPROVED);
		privileges.add(UserType.CONTENT_MANAGER+"_"+ContentStatus.APPROVAL_REQUESTED+"_"+ContentStatus.REJECTED);
	}
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		boolean success=false;
		
		String carId = request.getParameter("car_id");
		String newStatus = request.getParameter("status");
		log.info("values in PublishCtrlr are"+carId +"  "+newStatus);
		
		if(StringUtils.isNotBlank(carId) && StringUtils.isNotBlank(newStatus)){
			Car car = this.getCarManager().getCarFromId(new Long(carId));
			log.info("type of car:"+car.getSourceType().getSourceTypeCd());
		    String requestedPrivilege= this.getLoggedInUser().getUserType().getUserTypeCd()+
													"_"+car.getContentStatus().getCode()+"_"+newStatus;
		    
		    /* -- Start of code modified for Outfit Management -- */
		    if(privileges.contains(requestedPrivilege)){
		    	 if (car.getSourceType().getSourceTypeCd().equals("OUTFIT") && 
						 (newStatus.equals(ContentStatus.PUBLISHED) ||
						  newStatus.equals(ContentStatus.SENT_TO_CMP) ||
						  newStatus.equals(ContentStatus.RESEND_TO_CMP) ||
						  newStatus.equals(ContentStatus.RESEND_DATA_TO_CMP))){
				  	
					if(log.isInfoEnabled()){
						log.info("updating the content for outfit car based on the child car's status");
					}
					User user = this.getLoggedInUser();
					boolean readyToSendToCMP = true;
					ContentStatus resendDataToCMP = (ContentStatus) this.getCarLookupManager().getById(ContentStatus.class, ContentStatus.RESEND_DATA_TO_CMP);
					//ContentStatus published = (ContentStatus) this.getCarLookupManager().getById(ContentStatus.class, ContentStatus.PUBLISHED);
					//listing the all child outfit car's for parent outfit car
					Set<CarOutfitChild> outfitChildCars = car.getCarOutfitChild();
					for(CarOutfitChild carOutfitChild: outfitChildCars)	{
						Car childCar = carOutfitChild.getChildCar();
						if ( !(ContentStatus.SENT_TO_CMP.equals(childCar.getContentStatus().getCode())
							   || ContentStatus.PUBLISHED.equals(childCar.getContentStatus().getCode())
					   	       || ContentStatus.RESEND_TO_CMP.equals(childCar.getContentStatus().getCode()) 
					   	       || ContentStatus.RESEND_DATA_TO_CMP.equals(childCar.getContentStatus().getCode()) ) ){
								if(log.isInfoEnabled()){
									log.info("outfit car content cannot be updated as one of the child car pending ");
								}
								readyToSendToCMP = false;
							   break;
							   
						}
					}
					if (readyToSendToCMP != false) {
					Set<CarOutfitChild> outfitChildCarsTemp = car.getCarOutfitChild();
					for(CarOutfitChild carOutfitChild: outfitChildCarsTemp)	{
						Car childCar = carOutfitChild.getChildCar();
						// if child car status is in published or resend to CMP - do not update
						if(log.isDebugEnabled()){
							log.debug("updating child car id: "+childCar.getCarId()+" content status: "+childCar.getContentStatus().getCode());
						}
						//send outfit child car data only to CMP if child car already exists in CMP 
						if(ContentStatus.SENT_TO_CMP.equals(childCar.getContentStatus().getCode())){
						    childCar.setContentStatus(resendDataToCMP);
						    childCar.setAuditInfo(user);
						    this.getCarManager().save(childCar);
						    if(log.isInfoEnabled()){
							  log.info("Updated the outfit child car " + childCar.getCarId() + "status to RESEND_DATA_TO_CMP " );
						    }
						    
						}
					  } //sending outfit car to CMP
					    if(log.isInfoEnabled()){
					       log.info("updating the outfit content status: "+ newStatus);
					    }
						car.setContentStatus((ContentStatus) this.getCarLookupManager().getById(ContentStatus.class, newStatus));
						this.setAuditInfo(request, car);
						this.getCarManager().save(car);
				  }
				}else if (car.getSourceType().getSourceTypeCd().equals(SourceType.PYG) && 
						 (newStatus.equals(ContentStatus.PUBLISHED) ||
								  newStatus.equals(ContentStatus.SENT_TO_CMP) ||
								  newStatus.equals(ContentStatus.RESEND_TO_CMP) ||
								  newStatus.equals(ContentStatus.RESEND_DATA_TO_CMP))){
					
					updateDBPromotionContentStatus(request, car, newStatus);
					
				}else { 
					log.info("updating the content status: "+ newStatus);
					car.setContentStatus((ContentStatus) this.getCarLookupManager().getById(ContentStatus.class, newStatus));
					this.setAuditInfo(request, car);
					this.getCarManager().save(car);
					}
				 
				success=true;
				
				/* -- End of code modified for Outfit Management -- */
			}
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("success", success);
		request.setAttribute("jsonObj", new JSONObject(model));
		
		return new ModelAndView(this.getAjaxView());
	}
	
	public void updateDBPromotionContentStatus(HttpServletRequest request,Car car,String newStatus){
		
		if(log.isInfoEnabled()){
			log.info("updating the content for DB Promotional car based on the child car's status for Promotion Car :"+car.getCarId());
		}
		User user = this.getLoggedInUser();
		boolean readyToSendToCMP = true;
		ContentStatus resendDataToCMP = (ContentStatus) this.getCarLookupManager().getById(ContentStatus.class, ContentStatus.RESEND_DATA_TO_CMP);
		//ContentStatus published = (ContentStatus) this.getCarLookupManager().getById(ContentStatus.class, ContentStatus.PUBLISHED);
		//listing the all child  car's for parent pyg car
		Set<CarDBPromotionChild> dbPromotionChildCars = car.getCarDBPromotionChild();
		for(CarDBPromotionChild cardbPromotionChild: dbPromotionChildCars)	{
			Car childCar = cardbPromotionChild.getChildCar();
			if ( !(ContentStatus.SENT_TO_CMP.equals(childCar.getContentStatus().getCode())
				   || ContentStatus.PUBLISHED.equals(childCar.getContentStatus().getCode())
		   	       || ContentStatus.RESEND_TO_CMP.equals(childCar.getContentStatus().getCode()) 
		   	       || ContentStatus.RESEND_DATA_TO_CMP.equals(childCar.getContentStatus().getCode()) ) ){
					if(log.isInfoEnabled()){
						log.info("Promotion car content cannot be updated as one of the child car pending ");
					}
					readyToSendToCMP = false;
				   break;
				   
			}
		}
		if (readyToSendToCMP != false) {
		Set<CarDBPromotionChild> dbPromotionChildCarsTemp = car.getCarDBPromotionChild();
		for(CarDBPromotionChild carDBPromotionChild: dbPromotionChildCarsTemp)	{
			Car childCar = carDBPromotionChild.getChildCar();
			// if child car status is in published or resend to CMP - do not update
			if(log.isDebugEnabled()){
				log.debug("updating child car id: "+childCar.getCarId()+" content status: "+childCar.getContentStatus().getCode());
			}
			//send promo child car data only to CMP if child car already exists in CMP 
			if(ContentStatus.SENT_TO_CMP.equals(childCar.getContentStatus().getCode())){
			    childCar.setContentStatus(resendDataToCMP);
			    childCar.setAuditInfo(user);
			    this.getCarManager().save(childCar);
			    if(log.isInfoEnabled()){
				  log.info("Updated the Promotional child car " + childCar.getCarId() + "status to RESEND_DATA_TO_CMP " );
			    }
			    
			}
		  } //sending promotional car to CMP
		    if(log.isInfoEnabled()){
		       log.info("updating the Promotion content status: "+ newStatus);
		    }
			car.setContentStatus((ContentStatus) this.getCarLookupManager().getById(ContentStatus.class, newStatus));
			this.setAuditInfo(request, car);
			this.getCarManager().save(car);
	  }
	
		
		
	}
	
}
		
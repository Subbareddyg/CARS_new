package com.belk.car.app.webapp.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarSkuAttribute;
import com.belk.car.app.model.VendorSku;

public class EditSkuDetailsController extends BaseController  {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Map<String, Object> model = new HashMap<String, Object>();
		String view="";
		String carId = request.getParameter("car_id");
		String skuId = request.getParameter("sku_id");
		
		Car car=null;
		if(StringUtils.isNotBlank(carId)){
			car = this.getCarManager().getCarFromId(new Long(carId));
			VendorSku sku = this.getCarManager().getSku(Long.parseLong(skuId));
			if(request.getMethod().equalsIgnoreCase("get")){ // show the form
				view=this.getSuccessView();
				model.put("detailCar",car);
				model.put("sku",sku);
			}
			else{ // handle posted form
				view=this.getAjaxView();
				Set<CarAttribute> carAttrs = car.getCarAttributes() ;
				Set<CarSkuAttribute> sa = sku.getCarSkuAttributes() ;
				Map<Long, CarSkuAttribute> saMap = sku.getAttributeMap() ;
				boolean skuToBeSaved = false ;
				for(CarAttribute ca : carAttrs) {
					CarSkuAttribute carSkuAttr = null ;
					Long attrId = new Long(ca.getAttribute().getAttributeId()) ;
					String attrValue = request.getParameter("attribute:" + attrId) ;
					String attrChanged = request.getParameter("skuAttributeCheck:" + attrId) ;
					if (attrId.toString().equals(attrChanged)) {
						//Existing Sku Attribute Override
						if (saMap.containsKey(attrId)) {
							//Update
							carSkuAttr = saMap.get(attrId);
							if (attrValue != null && !attrValue.equals(carSkuAttr.getAttrValue() == null ? "" : carSkuAttr.getAttrValue())) { 
								carSkuAttr = saMap.get(attrId);
								carSkuAttr.setAttrValue(attrValue);
								this.setAuditInfo(request, carSkuAttr);
								skuToBeSaved = true ;
							}
						} else {
							//Create
							carSkuAttr = new CarSkuAttribute();
							carSkuAttr.setAttribute(ca.getAttribute());
							carSkuAttr.setAttrValue(attrValue) ;
							carSkuAttr.setVendorSku(sku);
							this.setAuditInfo(request, carSkuAttr);
							sku.getCarSkuAttributes().add(carSkuAttr) ;
							skuToBeSaved = true ;
						}
					}
				}
				
				
				// Online only attribute is editable
				// To reset Dropship online only attribute at sku level 
				// Because the above code for sku attributes depend on the product level attributes
				for(CarSkuAttribute skuAtr:sa){
					if("SDF_Online Only".equals(skuAtr.getAttribute().getName())){
						Long skuAttrId = new Long(skuAtr.getAttribute().getAttributeId()) ;
						String skuAttrValue = request.getParameter("attribute:" + skuAttrId) ;
						String skuAttrChanged = request.getParameter("skuAttributeCheck:" + skuAttrId) ;
						if (skuAttrId.toString().equals(skuAttrChanged)) {
							if (skuAttrValue != null && !skuAttrValue.equals(skuAtr.getAttrValue() == null ? "" : skuAtr.getAttrValue())) { 
								skuAtr.setAttrValue(skuAttrValue);
								this.setAuditInfo(request, skuAtr);
								skuToBeSaved = true ;
							}
						 }
					}
				}
				
				//Remove
				String[] prevAttrIds = request.getParameterValues("skuAttributeInp");
				if (prevAttrIds != null) {
					for(String prevAttrId : prevAttrIds) {
						String attrChecked = request.getParameter("skuAttributeCheck:" + prevAttrId) ;
						if (!prevAttrId.toString().equals(attrChecked)) {
							CarSkuAttribute csa = saMap.get(new Long(prevAttrId)) ;
							sku.getCarSkuAttributes().remove(csa);
							carManager.remove(csa);
							skuToBeSaved = true ;
						}
					}
				}
				
				//Save the SKU
				if (skuToBeSaved) {
					this.carManager.save(sku);
				}

				JSONObject json = new JSONObject();
				json.putOpt("success", true);
				model.put("jsonObj",json.toString());
			}
		}

		return new ModelAndView(view,model);
	}
}

package com.belk.car.app.webapp.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.dto.AttributeBMMappingDTO;
import com.belk.car.app.service.AttributeManager;
/**
 * Controller class to populate the attribute name to BM attribute name mapping page
 *  under Admin main menu list.
 *  
 * @author afusys9
 *
 */
public class BMAttributeMappingController extends BaseController{

    private transient final Log log = LogFactory
            .getLog(BMAttributeMappingController.class);
    
    private AttributeManager attributeManager;
    
    private String successView;
    
    public String getSuccessView() {
        return successView;
    }

    public void setSuccessView(String successView) {
        this.successView = successView;
    }

    public AttributeManager getAttributeManager() {
        return attributeManager;
    }

    public void setAttributeManager(AttributeManager attributeManager) {
        this.attributeManager = attributeManager;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        if(log.isDebugEnabled()){
            log.debug("Inside BMAttributeMappingController ");
        }
        ModelAndView modelAndView = new ModelAndView(getSuccessView());
        List<AttributeBMMappingDTO> mappingDTOList = new ArrayList<AttributeBMMappingDTO>();
        mappingDTOList = attributeManager.getAttributeBMMapping();
        if(mappingDTOList == null){
            mappingDTOList = new ArrayList<AttributeBMMappingDTO>();
        }
        
        if(log.isDebugEnabled()){
            log.debug("size of mapping list "+mappingDTOList.size());
        }
        modelAndView.addObject("mappingList", mappingDTOList);
        return modelAndView;
    }

}

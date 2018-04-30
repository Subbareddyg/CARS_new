package com.belk.car.app.webapp.controller.catalog;

import java.io.File;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.model.catalog.CatalogTemplate;
import com.belk.car.app.model.catalog.CatalogTemplateConfig;
import com.belk.car.app.service.CatalogImportManager;
import com.belk.car.app.webapp.controller.BaseFormController;

public class ImportCosmeticsDataController extends BaseFormController implements Controller{

    private CatalogImportManager catalogImportManager;
    private String importFileName;
    private String importDirPath;
    
	public void setCatalogImportManager(CatalogImportManager catalogImportManager) {
		this.catalogImportManager = catalogImportManager;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		List<CatalogTemplate> templates = catalogImportManager.getAllCatalogTemplates();
		if (!templates.isEmpty()) {
			for(CatalogTemplate template: templates) {
				Set<CatalogTemplateConfig> configs = template.getConfigurations();
				if (configs != null) {
					int i = configs.size();
					log.debug("size of config: " + i);
				}
			}
		}
		
		File f = new File(this.importDirPath+this.importFileName);
		if (f.exists()) {
			//CosmeticFileImporter cfi = new CosmeticFileImporter();
			//cfi.setCosmeticFile(f) ;
			//cfi.doImport();
		}
		
		//Map<String, Object> context = new HashMap<String, Object>();
        //context.put("flag", manualCarManager.exportManualCarsToFile(exportDirPath, exportFileName));

        //File file = new File(exportDirPath+exportFileName);
        //context.put("path", file.getAbsolutePath());
		
		//return new ModelAndView(getSuccessView(), context);
		return new ModelAndView(getSuccessView());
	}

	public void setImportFileName(String importFileName) {
		this.importFileName = importFileName;
	}

	public void setImportDirPath(String importDirPath) {
		this.importDirPath = importDirPath;
	}
}

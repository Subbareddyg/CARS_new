package com.belk.car.app.webapp.controller.catalog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.model.Config;
import com.belk.car.app.model.catalog.CatalogImport;
import com.belk.car.app.service.CatalogImportManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.integrations.cosmetics.CosmeticFileImporter;

public class ImportCatalogDataFormController extends BaseFormController {

    private CatalogImportManager catalogImportManager;
    private String importDirPath;
    
	public void setCatalogImportManager(CatalogImportManager catalogImportManager) {
		this.catalogImportManager = catalogImportManager;
	}

    public ImportCatalogDataFormController() {
        setCommandName("catalogFileUpload");
        setCommandClass(CatalogFileUpload.class);
    }
    
    public ModelAndView processFormSubmission(HttpServletRequest request,
                                              HttpServletResponse response,
                                              Object command,
                                              BindException errors)
    throws Exception {
        if (request.getParameter("cancel") != null) {
            return new ModelAndView(getCancelView());
        }

        return super.processFormSubmission(request, response, command, errors);
    }

    public ModelAndView onSubmit(HttpServletRequest request,
                                 HttpServletResponse response, Object command,
                                 BindException errors)
    throws Exception {
        CatalogFileUpload fileUpload = (CatalogFileUpload) command;

        // validate a file was entered
        if (fileUpload.getFile().length == 0) {
            Object[] args = 
                new Object[] { getText("uploadForm.file", request.getLocale()) };

            errors.rejectValue("file", "errors.required", args, "File");
            
            return showForm(request, response, errors);
        }

        MultipartHttpServletRequest multipartRequest =
            (MultipartHttpServletRequest) request;
        CommonsMultipartFile file =
            (CommonsMultipartFile) multipartRequest.getFile("file");

        // the directory to upload to
//        String uploadDir =
//            getServletContext().getRealPath("/resources") + "/" +
//            request.getRemoteUser() + "/";
        
		Config uploadDirConfig = (Config) this.getCarLookupManager().getById(Config.class, Config.CATALOG_UPLOAD_DIR);
        String uploadDir = uploadDirConfig.getValue();
        if (!uploadDir.endsWith(String.valueOf(File.separatorChar))) {
        	uploadDir = uploadDir + File.separatorChar;
        }

        // Create the directory if it doesn't exist
        File dirPath = new File(uploadDir);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        //retrieve the file data
        InputStream stream = file.getInputStream();

        //write the file to the file specified
        String uploadFile = uploadDir + (new SimpleDateFormat("yyyymmddhhmmss")).format(new Date())+"_"+file.getOriginalFilename();
        OutputStream bos =
            new FileOutputStream(uploadFile);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];

        while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }

        bos.close();

        //close the stream
        stream.close();

        File inputFile = new File(uploadFile) ;
		if (inputFile.exists()) {
			CatalogImport ci = new CatalogImport() ;
			this.setAuditInfo(request, ci) ;
			ci.setVendorNumber(fileUpload.getVendorNumber());
			ci.setImportFileName(inputFile.getName());
			ci.setTemplate(catalogImportManager.getCatalogTemplate(fileUpload.getTemplateId()));
			CosmeticFileImporter cfi = new CosmeticFileImporter(ci, inputFile, ci.getTemplate().getProcessor());
			cfi.doImport(catalogImportManager, this.getCarManager(), "Y".equals(fileUpload.getCreateOnly()));
		}

        // place the data into the request for retrieval on next page
        //request.setAttribute("friendlyName", fileUpload.getName());
        //request.setAttribute("fileName", file.getOriginalFilename());
        //request.setAttribute("contentType", file.getContentType());
        //request.setAttribute("size", file.getSize() + " bytes");
        //request.setAttribute("location",
        //                    dirPath.getAbsolutePath() + Constants.FILE_SEP +
        //                     file.getOriginalFilename());

        //String link =
        //    request.getContextPath() + "/resources" + "/" +
        //    request.getRemoteUser() + "/";

        //request.setAttribute("link", link + file.getOriginalFilename());

        return new ModelAndView(getSuccessView());
    }
    
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws ServletRequestBindingException {

		Map model = new HashMap();
		model.put("templates", catalogImportManager.getAllCatalogTemplates());
		return model;
	}

	protected Object formBackingObject(HttpServletRequest request)
	throws Exception {
		return new CatalogFileUpload();
	}
}

package com.belk.car.app.webapp.controller.car;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.belk.car.app.service.ManualCarManager;
import com.belk.car.app.webapp.controller.BaseFormController;

public class ExportManualCarController extends BaseFormController implements
		Controller {

	private ManualCarManager manualCarManager;
	private String exportFileName;
	private String exportDirPath;

	public void setManualCarManager(ManualCarManager manualCarManager) {
		this.manualCarManager = manualCarManager;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		/*Map<String, Object> context = new HashMap<String, Object>();
		context.put("flag", manualCarManager.exportManualCarsToFile(
				exportDirPath, exportFileName));

		File file = new File(exportDirPath + exportFileName);
		context.put("path", file.getAbsolutePath());
*/
		return new ModelAndView(getSuccessView(), null);
	}

	public void setExportFileName(String exportFileName) {
		this.exportFileName = exportFileName;
	}

	public void setExportDirPath(String exportDirPath) {
		this.exportDirPath = exportDirPath;
	}
}

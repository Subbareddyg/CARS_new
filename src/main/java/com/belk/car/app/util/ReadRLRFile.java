package com.belk.car.app.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.belk.car.app.dto.DepartmentClassUpload;

public class ReadRLRFile {

	public static final int START_DEMAND_CENTER_CODE = 0;
	public static final int END_DEMAND_CENTER_CODE = START_DEMAND_CENTER_CODE + 2;

	public static final int START_DEPARTMENT_CODE = END_DEMAND_CENTER_CODE;
	public static final int END_DEPARTMENT_CODE = START_DEPARTMENT_CODE + 3;

	public static final int START_DEPARTMENT_NAME = END_DEPARTMENT_CODE;
	public static final int END_DEPARTMENT_NAME = START_DEPARTMENT_NAME + 25;

	public static final int START_CLASS_CODE = END_DEPARTMENT_NAME;
	public static final int END_CLASS_CODE = START_CLASS_CODE + 4;

	public static final int START_CLASS_NAME = END_CLASS_CODE ;
	public static final int END_CLASS_NAME = START_CLASS_NAME + 30;
	

	private static transient final Log log = LogFactory
			.getLog(ReadRLRFile.class);

	public static Collection<DepartmentClassUpload> process(File fl)
			throws IOException {

		if (log.isInfoEnabled()) {
			log.info("-------------> Begin Processing File <--------------");
			if (fl != null) {
				log.info("Processing file: Location=" + fl.getAbsolutePath() + " Filename=" + fl.getName());
			}
		}
		
		if (fl == null) {
			if (log.isErrorEnabled())
				log.error("Invalid file... File is null!!!");
			return null;
		}

		List list = FileUtils.readLines(fl, "UTF-8");
		List<DepartmentClassUpload> data = new ArrayList<DepartmentClassUpload>(list.size());
		java.util.Iterator it = list.iterator();
		while (it.hasNext()) {
			String str = (String) it.next();
			String demandCenterCode = str.substring(START_DEMAND_CENTER_CODE, END_DEMAND_CENTER_CODE);
			String deptCode = str.substring(START_DEPARTMENT_CODE, END_DEPARTMENT_CODE);
			String deptName = str.substring(START_DEPARTMENT_NAME, END_DEPARTMENT_NAME);
			String classCode = str.substring(START_CLASS_CODE, END_CLASS_CODE);
			String className = str.substring(START_CLASS_NAME, END_CLASS_NAME);

			DepartmentClassUpload dc = new DepartmentClassUpload() ;
			dc.setDemandCenterCode(StringUtils.trim(demandCenterCode));
			dc.setDeptCode(StringUtils.trim(deptCode));
			dc.setDeptName(StringUtils.trim(deptName));
			dc.setClassCode(StringUtils.trim(classCode)) ;
			dc.setClassName(StringUtils.trim(className)) ;
			data.add(dc) ;
		}
		return data;
	}
}

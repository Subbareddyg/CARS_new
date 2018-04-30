package com.belk.car.app.util;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.belk.car.app.dto.UserRankDTO;



public class ReadUserRankFile {

	public static final int STARTDEPTCODE = 0;
	public static final int ENDDEPTCODE = STARTDEPTCODE + 3;

	public static final int STARTDMMNAME = ENDDEPTCODE;
	public static final int ENDDMMNAME = STARTDMMNAME + 39;
	
	public static final int STARTDMMEMAIL = ENDDMMNAME;
	public static final int ENDDMMEMAIL = STARTDMMEMAIL + 40;
	
	public static final int STARTGMMNAME = ENDDMMEMAIL;
	public static final int ENDGMMNAME = STARTGMMNAME + 40;
	
	public static final int STARTGMMEMAIL = ENDGMMNAME;
	public static final int ENDGMMEMAIL = STARTGMMEMAIL + 40;
	
	public static final int STARTDEMANDCENTER = ENDGMMEMAIL;
	public static final int ENDDEMANDCENTER = STARTDEMANDCENTER + 3;
	
	public static final int STARTDCDESC = ENDDEMANDCENTER;
	public static final int ENDDCDESC = STARTDCDESC + 25;
	private transient final static Log log = LogFactory.getLog(ReadUserRankFile.class);

	public static Collection<UserRankDTO> process(File file) throws IOException {
		if (log.isInfoEnabled())
			log.info("Porcess UserRank Data file "+file.getAbsolutePath()+"/"+file.getName());

		List list = FileUtils.readLines(file, "UTF-8");
		HashMap<String, UserRankDTO> data = new HashMap<String, UserRankDTO>();
		java.util.Iterator it = list.iterator();

		nextRow: while (it.hasNext()) {
			String input = (String) it.next();
			if (input == null)
				continue nextRow;
			String deptno = StringUtils.substring(input,
					ReadUserRankFile.STARTDEPTCODE,
					ReadUserRankFile.ENDDEPTCODE);
			String dmmName = StringUtils.substring(input,
					ReadUserRankFile.STARTDMMNAME, ReadUserRankFile.ENDDMMNAME);
			String dmmEmail = StringUtils.substring(input,
					ReadUserRankFile.STARTDMMEMAIL,
					ReadUserRankFile.ENDDMMEMAIL);
			String gmmName = StringUtils.substring(input,
					ReadUserRankFile.STARTGMMNAME, ReadUserRankFile.ENDGMMNAME);
			String gmmEmail = StringUtils.substring(input,
					ReadUserRankFile.STARTGMMEMAIL,
					ReadUserRankFile.ENDGMMEMAIL);
			String demandcenter = StringUtils.substring(input,
					ReadUserRankFile.STARTDEMANDCENTER,
					ReadUserRankFile.ENDDEMANDCENTER);
			String dcDesc = StringUtils.substring(input,
					ReadUserRankFile.STARTDCDESC, ReadUserRankFile.ENDDCDESC);
			UserRankDTO userRankDTO = null;
			if (isAllFieldsDataAvailable(Integer.parseInt(deptno), dmmName, dmmEmail,
					gmmName, gmmEmail, demandcenter, dcDesc)) {
				userRankDTO = buildUserRankObj(Integer.parseInt(deptno),
						dmmName, dmmEmail, gmmName, gmmEmail, demandcenter,
						dcDesc);
			}
			if (userRankDTO != null) {
				//TODO Dept number should not duplicate - validate data file properly.
				data.put(deptno, userRankDTO);
			}

		}
		return data.values();
	}

	private static boolean isAllFieldsDataAvailable(Integer deptno2, String dmmName2,
			String dmmEmail2, String gmmName2, String gmmEmail2,
			String demandcenter2, String dcDesc2) {
		// TODO validation
		if(deptno2 ==null || dmmName2 ==null || gmmName2 ==null || dmmEmail2 ==null
				||gmmEmail2 ==null || demandcenter2 == null   || dcDesc2 == null ) {
			return false;
		}
		return true;
	}

	private static UserRankDTO buildUserRankObj(Integer deptno, String dmmName,
			String dmmEmail, String gmmName, String gmmEmail,
			String demandcenter, String dcDesc) {
		UserRankDTO userRankDTO = new UserRankDTO();
		userRankDTO.setDeptNo(deptno);
		userRankDTO.setDmmName(dmmName);
		userRankDTO.setDmmEmail(dmmEmail);
		userRankDTO.setGmmName(gmmName);
		userRankDTO.setGmmEmail(gmmEmail);
		userRankDTO.setDemandCenter(demandcenter);
		userRankDTO.setDcDesc(dcDesc);
		return userRankDTO;
	}
}

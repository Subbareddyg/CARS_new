/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.belk.car.app.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.parser.ParserInitializationException;
import org.appfuse.model.User;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.dao.DropshipDao;
import com.belk.car.app.dao.VendorCatalogDao;
import com.belk.car.app.dto.CatalogHeaderDTO;
import com.belk.car.app.dto.CatalogMappedFieldDTO;
import com.belk.car.app.dto.CatalogVendorDTO;
import com.belk.car.app.dto.FieldMappingDataDTO;
import com.belk.car.app.dto.NameValue;
import com.belk.car.app.dto.VendorCatalogDTO;
import com.belk.car.app.dto.VendorCatalogStyleDTO;
import com.belk.car.app.dto.VendorStyleImageDTO;
import com.belk.car.app.dto.VendorStylePropertiesDTO;
import com.belk.car.app.dto.VendorUpcDTO;
import com.belk.car.app.model.AttributeLookupValue;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.vendorcatalog.CatalogGroupTemplate;
import com.belk.car.app.model.vendorcatalog.CatalogMasterAttribute;
import com.belk.car.app.model.vendorcatalog.CompositeKeyForDataFldMapping;
import com.belk.car.app.model.vendorcatalog.CompositeKeyForVndrCatlDept;
import com.belk.car.app.model.vendorcatalog.MasterAttributeMapping;
import com.belk.car.app.model.vendorcatalog.VendorCatalog;
import com.belk.car.app.model.vendorcatalog.VendorCatalogDepartment;
import com.belk.car.app.model.vendorcatalog.VendorCatalogFieldDataMapping;
import com.belk.car.app.model.vendorcatalog.VendorCatalogFieldMapping;
import com.belk.car.app.model.vendorcatalog.VendorCatalogFileFormat;
import com.belk.car.app.model.vendorcatalog.VendorCatalogImage;
import com.belk.car.app.model.vendorcatalog.VendorCatalogImageLocation;
import com.belk.car.app.model.vendorcatalog.VendorCatalogImport;
import com.belk.car.app.model.vendorcatalog.VendorCatalogNote;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSku;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSkuId;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSkuImage;
import com.belk.car.app.model.vendorcatalog.VendorCatalogTemplate;
import com.belk.car.app.model.vendorcatalog.VendorCatalogUpdateAction;
import com.belk.car.app.service.DropshipManager;
import com.belk.car.app.service.VendorCatalogManager;
import com.belk.car.app.util.FtpUtil;
import com.belk.car.app.util.PropertyLoader;
import com.belk.car.app.util.SFtpUtil;
import com.belk.car.app.webapp.forms.VendorCatalogForm;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
/**
 *
 * @author AFUSY85
 */
/**
 * @author AFUSYA2
 *
 */
public class VendorCatalogManagerImpl implements VendorCatalogManager, DropShipConstants {

	private static final int FILE_BUFFER_SIZE = 8192;

	private static transient final Log log = LogFactory.getLog(VendorCatalogManagerImpl.class);

	private VendorCatalogDao catalogDao;
	private DropshipManager dropshipManager;
        private DropshipDao dropshipDao;

	public void setCatalogDao(VendorCatalogDao catalogDao) {
		this.catalogDao = catalogDao;
	}

	/**
	 * @return the dropshipManager
	 */
	public DropshipManager getDropshipManager() {
		return dropshipManager;
	}

	/**
	 * @param dropshipManager
	 *            the dropshipManager to set
	 */
	public void setDropshipManager(DropshipManager dropshipManager) {
		this.dropshipManager = dropshipManager;
	}

        public DropshipDao getDropshipDao() {
            return dropshipDao;
        }

        public void setDropshipDao(DropshipDao dropshipDao) {
            this.dropshipDao = dropshipDao;
        }



	public List<CatalogVendorDTO> getAllVendors() {
		CatalogVendorDTO catalogDTO = new CatalogVendorDTO();
		return catalogDao.getCatalogVendors(catalogDTO);
	}
	 /**
         * Get Catalog Vendors List who has catalogs.
         * @return List<CatalogVendorDTO>
         */
	public List<CatalogVendorDTO> getAllVendors(String vendorNo) {
		CatalogVendorDTO catalogDTO = new CatalogVendorDTO();
		catalogDTO.setVendorNumber(Long.parseLong(vendorNo));
		return catalogDao.getCatalogVendors(catalogDTO);
	}
	/**
     * Get Catalog Vendors List who are Active.
     * @return List<CatalogVendorDTO>
     */
public List<CatalogVendorDTO> getAllVendors(int isDisplayable) {
	CatalogVendorDTO catalogDTO = new CatalogVendorDTO();
	if(isDisplayable == 1){
	catalogDTO.setIsDisplayable("Y");
	}
	return catalogDao.getCatalogVendors(catalogDTO);
}
        /**
         * Get VendorCatalogDao
         * @return VendorCatalogDao
         */
	public VendorCatalogDao getVendorCatalogDao() {
		return catalogDao;
	}

	// Vendor Catalog Form Start
	/**
	 * This method is used to check whether catalog name is present for Vendor.
	 *
	 * @param VendorCatalogForm
	 *            vendorCatalogForm
	 * @return boolean
	 */
	public boolean getCatalogNameForVendor(VendorCatalogForm vendorCatalogForm) throws Exception {
		log.debug("inside getCatalogNameForVendor of impl");
		Boolean catalogNameExist = Boolean.FALSE;
		List<VendorCatalog> list = new ArrayList<VendorCatalog>();
		list = catalogDao.getCatalogNameForVendor(vendorCatalogForm.getVendorNumber(),
				vendorCatalogForm.getCatalogName());
		if (null != list && !list.isEmpty()) {
			catalogNameExist = Boolean.TRUE;
		}
		return catalogNameExist;
	}

	/**
	 * This method gets the Vendor for vendorNumber
	 */
	public Vendor getVendorForCatalog(String vendorNumber) {
		return catalogDao.getVendor(vendorNumber);
	}

	/**
	 * This method saves vendor catalog import fields, vendor catalog and
	 * catalog departments into vendor_catalog, vendor_catalog_import and
	 * vendor_catalog_dept table
	 *
	 * @param VendorCatalogForm
	 *            vendorCatalogForm
	 * @param boolean isNewCatalog
	 * @param List
	 *            removedDept
	 * @param List
	 *            addedDept
	 * @return VendorCatalog
	 */
	public VendorCatalog saveVendorCatalog(VendorCatalogForm vendorCatalogForm,
			boolean isNewCatalog, List<Department> removedDeptList, List<Department> addedDeptList)
			throws Exception {
		if (log.isInfoEnabled()) {
			log.info("---------------->  Begin saving VendorCatalog Data <-----------------");
		}
		if (log.isDebugEnabled()) {
			log.debug("inside VendorCatalogManagerImpl save Vendor Catalog ");
		}
		VendorCatalog vendorCatalog = new VendorCatalog();

		vendorCatalog.setVendor(catalogDao.getVendor(vendorCatalogForm.getVendorNumber()));
		vendorCatalog.setVendorCatalogName(vendorCatalogForm.getCatalogName().trim());
		vendorCatalog.setStatusCD(IMPORTING);
		vendorCatalog.setAuditInfo(getLoggedInUser());
		vendorCatalog.setVendorCatalogID(Long.valueOf(vendorCatalogForm.getCatalogId()));
		vendorCatalog.setSource("Manual Upload");
		// save Vendor Catalog in Vendor_Catalog table
		VendorCatalog catalog = catalogDao.save(vendorCatalog);

		/** started save catalog import functionality */
		VendorCatalogImport vendorCatalogImport = catalogDao.getVendorCatalogImport(catalog
				.getVendorCatalogID());
		// check for new catalog
		if (vendorCatalogImport == null) {
			vendorCatalogImport = new VendorCatalogImport();
		}
		vendorCatalogImport.setVendorCatalogFileName(vendorCatalogForm.getFilePath().trim());
		vendorCatalogImport.setFileFormatID(catalogDao.getFileFormat(vendorCatalogForm
				.getFileFormat()));
		vendorCatalogImport.setUpdateActionID(catalogDao.getUpdateAction(vendorCatalogForm
				.getUpdateAction()));
		vendorCatalogImport.setImageLocationID(catalogDao.getImageLocation(vendorCatalogForm
				.getImgLocn()));
		vendorCatalogImport.setFtpUrl(vendorCatalogForm.getFtpUrl().trim());
		vendorCatalogImport.setFtpUname(vendorCatalogForm.getFtpUname().trim());
		vendorCatalogImport.setFtpPassword(vendorCatalogForm.getFtpPwd().trim());
		/**
		 * check for file format if not text format then set file delimiter as
		 * blank
		 */
		if (null != vendorCatalogForm.getFileFormat()
				&& vendorCatalogForm.getFileFormat().equalsIgnoreCase(TEXT_FILES_TXT_CSV)) {
			vendorCatalogImport.setDataFileDelimeter(vendorCatalogForm.getDelimeter());
		} else {
			vendorCatalogImport.setDataFileDelimeter(BLANK);
		}
		vendorCatalogImport.setVendorCatalogID(catalog);
		if (StringUtils.isBlank(vendorCatalogForm.getPreviousCatalogID())) {
			vendorCatalogImport.setPrevCatalogId(Long.valueOf(ZERO));
		} else {
			vendorCatalogImport.setPrevCatalogId(Long.valueOf(vendorCatalogForm
					.getPreviousCatalogID()));
		}
		vendorCatalogImport.setImportedDate(new java.util.Date());
		// save catalog import in Vendor_Catalog_Import table
		catalogDao.save(vendorCatalogImport);
		/** end of save catalog import functionality */

		/** started save catalog department functionality */
		List<Department> deptList = null;
		if (null != vendorCatalogForm.getDepartments()
				&& !vendorCatalogForm.getDepartments().isEmpty()) {
			deptList = vendorCatalogForm.getDepartments();
		}
		List<VendorCatalogDepartment> vendorCatalogDepartmentList = new ArrayList<VendorCatalogDepartment>();
		CompositeKeyForVndrCatlDept compositeKeyForVndrCatlDept = null;
		if (deptList != null && !deptList.isEmpty()) {
			for (Department dept : deptList) {
				VendorCatalogDepartment catalogDepartment = catalogDao.getVendorCatalogDepartment(
						(Long) dept.getDeptId(), catalog);
				if (catalogDepartment == null) {
					catalogDepartment = new VendorCatalogDepartment();
					compositeKeyForVndrCatlDept = new CompositeKeyForVndrCatlDept();
				} else {
					compositeKeyForVndrCatlDept = catalogDepartment
							.getCompositeKeyForVndrCatlDept();
				}
				compositeKeyForVndrCatlDept.setDeptId(dept.getDeptId());
				compositeKeyForVndrCatlDept.setVendorCatalog(catalog);
				catalogDepartment.setCompositeKeyForVndrCatlDept(compositeKeyForVndrCatlDept);
				catalogDepartment.setStatusCD(ACTIVE);
				catalogDepartment.setAuditInfo(getLoggedInUser());
				vendorCatalogDepartmentList.add(catalogDepartment);
			}
		}
		// update the department list with removed departments as Inactive
		if (null != removedDeptList && !removedDeptList.isEmpty()) {
			log.debug("caatlog departments to be removed:" + removedDeptList.size());
			for (Department removedDept : removedDeptList) {
				VendorCatalogDepartment catalogDepartment = catalogDao.getVendorCatalogDepartment(
						(Long) removedDept.getDeptId(), catalog);
				if (catalogDepartment == null) {
					continue;
				}
				catalogDepartment.setStatusCD("Inactive");
				vendorCatalogDepartmentList.add(catalogDepartment);
			}
		}
		// update the department list with added departments as Inactive
		if (null != addedDeptList && !addedDeptList.isEmpty()) {
			log.debug("catalog departments to be added:" + addedDeptList.size());
			for (Department addedDept : addedDeptList) {
				VendorCatalogDepartment catalogDepartment = catalogDao.getVendorCatalogDepartment(
						(Long) addedDept.getDeptId(), catalog);
				if (catalogDepartment == null) {
					continue;
				}
				catalogDepartment.setStatusCD(ACTIVE);
				vendorCatalogDepartmentList.add(catalogDepartment);
			}
		}
		/** save Vendor Catalog Department in Vendor_Catalog_Dept table */
		catalogDao.saveVndrCatlDept(vendorCatalogDepartmentList);
		/** end of save catalog department functionality */

		if (log.isInfoEnabled()) {
			log.info("---------------->  End saving VendorCatalog Data <-----------------");
		}
		return vendorCatalog;
	}

	/**
	 * This method is used for getting Created By and Updated By fields.
	 *
	 * @return User
	 */
	public User getLoggedInUser() {
		User user = null;
		Authentication auth = ((SecurityContext) SecurityContextHolder.getContext())
				.getAuthentication();
		if (auth.getPrincipal() instanceof UserDetails) {
			user = (User) auth.getPrincipal();
		}
		return user;
	}

	/**
	 * This method gets the vendor catalog template id from
	 * VendorCatalogTemplate table
	 *
	 * @param Long
	 *            vendorCatalogTmplId
	 * @return VendorCatalogTemplate
	 */
	public VendorCatalogTemplate getVendorCatalogTemplate(Long vendorCatalogTemplateId) {
		log.debug("inside VendorCatalogManagerImpl getVendorCatalogTemplate");
		return catalogDao.getVendorCatalogTemplate(vendorCatalogTemplateId);

	}

	/**
	 * This method gets the image location List from VendorCatalogImageLocation
	 * table used as input to image location drop down
	 *
	 * @return List VendorCatalogImageLocation
	 */
	public List<VendorCatalogImageLocation> getVendorCatalogImageLocations() throws Exception {
		return catalogDao.getVendorCatalogImageLocations();
	}

	/**
	 * This method gets the update action List from VendorCatalogUpdateAction
	 * table used as input to update action drop down
	 *
	 * @return List VendorCatalogUpdateAction
	 */
	public List<VendorCatalogUpdateAction> getVendorCatalogUpdateActions() throws Exception {
		if (log.isDebugEnabled())
			log.debug("inside VendorCatalogManagerImpl getVendorCatalogUpdateActions");
		return catalogDao.getVendorCatalogUpdateActions();
	}

	/**
	 * This method gets the file format List from VendorCatalogFileFormat table
	 * used as input to file format drop down
	 *
	 * @return List VendorCatalogFileFormat
	 */
	public List<VendorCatalogFileFormat> getVendorCatalogFileFormats() throws Exception {
		return catalogDao.getVendorCatalogFileFormats();
	}

	/**
	 * This method checks/validates FTP locations
	 *
	 * @param String
	 *            destinationFtp
	 * @param String
	 *            sourceFtp
	 * @param String
	 *            ftpUname
	 * @param String
	 *            ftpPwd
	 * @return boolean
	 */
	public boolean methodInvokingImportFTPImages(String destinationDirectory,
			Properties properties, VendorCatalogForm vendorCatalogForm) throws IOException {
		try {
			if (log.isDebugEnabled()) {
				log.debug("vendor FTP option selected and the destination dir for images is:"
						+ destinationDirectory);
			}
			String sourceFtp = vendorCatalogForm.getFtpUrl();
			String ftpUname = vendorCatalogForm.getFtpUname();
			String ftpPwd = vendorCatalogForm.getFtpPwd();
			String sourceFtpHost = null;
			String sourceFolder = null;

			// check FTPClient for source
			if (sourceFtp == null) {
				return false;
			} else {
				sourceFtpHost = sourceFtp.substring("ftp://".length(), sourceFtp.length());
				if (sourceFtpHost.indexOf(SLASH) != -1) {
					sourceFolder = sourceFtpHost.substring(sourceFtpHost.indexOf(SLASH));
					sourceFtpHost = sourceFtpHost.substring(0, sourceFtpHost.indexOf(SLASH));
				}
			}
			/** FTP credentials for destination */
			if (!FtpUtil.checkFTPConnection(sourceFtpHost, ftpUname,
						ftpPwd)) {
					throw new IOException("InValid UserID/Password");
			} else if(!FtpUtil.checkFTPPath(sourceFtpHost, ftpUname, ftpPwd, sourceFolder)) { 
				return false;
			} else {
				return true;
			}
		} catch (ParserInitializationException pie) {
			if (log.isErrorEnabled()) {
				log.error("Ftp Connection is not established or invalid." + pie);
			}
			return false;
		} 
	}

	/**
	 * This method saves image name list into (VendorCatalogImage) VNDR_CATL_IMG
	 * table image name list comes from excel/txt/xml file. This method also
	 * gets called for Previous catalog ID selected from upload image drop down
	 *
	 * @param List
	 *            imageList
	 * @param long vendorCatalogId
	 */
	@SuppressWarnings("unchecked")
	public void saveImage(List imageList, Long vendorCatalogId) throws Exception {
		/** Save Vendor Catalog Image in vndr_catl_img */
		if (log.isInfoEnabled()) {
			log.info("---------------->  Begin saving VendorCatalog Image Data <-----------------");
		}
		List imageNameList = imageList;
		List<VendorCatalogImage> vendorCatalogImage = new ArrayList<VendorCatalogImage>();
		VendorCatalogImage catalogImage = null;
		for (int i = 0; i < imageNameList.size(); i++) {
			catalogImage = new VendorCatalogImage();
			catalogImage.setImgName(imageNameList.get(i).toString());
			catalogImage.setVendorCatalogId(vendorCatalogId);
			catalogImage.setAuditInfo(getLoggedInUser());
			vendorCatalogImage.add(catalogImage);
		}
		if (log.isDebugEnabled()) {
			log.debug("name list to save..." + imageNameList);
			log.debug("catalog id to save..." + vendorCatalogId);
		}
		catalogDao.saveVndrCatlImg(vendorCatalogImage);
		if (log.isInfoEnabled()) {
			log.info("---------------->  End saving VendorCatalog Image Data <-----------------");
		}
	}

	/**
	 * This method gets the vendor catalog for catalog id
	 *
	 * @param long vendorCatalogID
	 * @return VendorCatalog
	 */
	public VendorCatalog getVendorCatalog(Long vendorCatalogID) {
		return catalogDao.getVendorCatalog(vendorCatalogID);
	}
	
	/**
	 * Method to get list of previous catalog files for speciifc vendor ID
	 */
	public List<VendorCatalogImport> getPreviousCatalogsList(Long vendorId){
		return catalogDao.getPreviousCatalogsList(vendorId);
	}

	/**
	 * This method gets the vendor catalog for catalog id
	 *
	 * @param long vendorCatalogID
	 * @return VendorCatalog
	 */
	public VendorCatalogImport getVendorCatalogImportDetails(long vendorCatalogID) throws Exception {
		return catalogDao.getVendorCatalogImpDetails(vendorCatalogID);
	}

	public List getVendorCatalogDeptDetails(VendorCatalog vendorCatalog) throws Exception {
		return catalogDao.getVendorCatalogDeptDetails(vendorCatalog);
	}

	/**
	 * This method gets image list for the vendorcatalog id This method gets
	 * called when upload images from previous catalog id is selected from
	 * upload image drop down
	 *
	 * @param long vendorCatalogID
	 * @return List
	 */
	public List<VendorCatalogImage> getVendorCatalogImageList(long vendorCatalogID)
			throws Exception {
		return catalogDao.getVendorCatalogImageList(vendorCatalogID);
	}

	/**
	 * This method gets the catalog id from sequence Written this method to
	 * create image directory with name venNum+catalogId
	 *
	 * @return long
	 */
	public long getCatalogIdFromSeq() {
		return catalogDao.getCatalogIdFromSeq();
	}

	/**
	 * This method creates destination image directory with name combination of
	 * vendor number+catalog id on click of Verify button
	 *
	 * @param String
	 *            imageDirectoryName
	 * @return String
	 * @throws IOException
	 */
	
	public String createNewImageDirectory(Properties properties, String venNum, Long catalogSeqId)
			throws IOException {
		String destinationFtpHost = properties.getProperty(FTPHOST);
		String destinationFtpUname = properties.getProperty(FTPUSER);
		String destinationFtpPasswd = properties.getProperty(FTPPASSWORD);
		String destinationFtpDir = properties.getProperty(FTPPATH);

		// directory created with name vndrNumber+catalogId
		String imageDirectoryName = venNum + UNDERSCORE + catalogSeqId;

		if (log.isDebugEnabled()) {
			log.debug("ftpHost name " + destinationFtpHost);
			log.debug("ftpUserName " + destinationFtpUname);
			log.debug("ftpPassword " + destinationFtpPasswd);
			log.debug("destinationImageDirectory " + destinationFtpDir);
		}
		String workingDirectory = null;
		if (destinationFtpHost != null && StringUtils.isNotBlank(destinationFtpHost)) {
			Session session;
			try {
				session =SFtpUtil.openSftpSession(destinationFtpHost, destinationFtpUname, destinationFtpPasswd, 22, "no");
				Channel channel = session.openChannel("sftp");
	            channel.connect();
	            ChannelSftp  sftpChannel = (ChannelSftp) channel;
	            sftpChannel.cd(destinationFtpDir);
	            sftpChannel.mkdir(imageDirectoryName);
	            sftpChannel.cd(destinationFtpDir.concat(SLASH) + imageDirectoryName);
	            sftpChannel.mkdir(UNMAPPED);
	            // working directory set to vndrNo+catalodId+Unmapped
	            sftpChannel.cd(destinationFtpDir.concat(SLASH)
						+ imageDirectoryName.concat(SLASH) + UNMAPPED);
	            log.debug("image directory with new name created..." + sftpChannel.pwd());
				workingDirectory = sftpChannel.pwd();
				SFtpUtil.closeSftpConnection(session,sftpChannel);
	            
			} catch (Exception e) {
				log.error("image directory with new name creation failed:"+e.getMessage());
			} 
		}
		log.debug("image directory with new name created..." + workingDirectory);
		return workingDirectory;
	}

	// Vendor Catalog Form End

	// Vendor Catalog Note Start
	/**
	 * This method gets the vendor catalog notes List from VendorCatalogNote
	 * table
	 *
	 * @param long vendorCatalogId
	 * @return List VendorCatalogNote
	 */
	public List<VendorCatalogNote> getVendorCatalogNotes(Long vendorCatalogId) {
		log.debug("inside VendorCatalogManagerImpl getVendorCatalogNotes list");
		return catalogDao.getVendorCatalogNotesList(vendorCatalogId);
	}

	/**
	 * This method gets the catalog notes by noteID from VendorCatalogNote table
	 *
	 * @param long id
	 * @return VendorCatalogNote
	 */
	public VendorCatalogNote getVendorCatalogNoteByID(long vendorCatalogNoteId) {
		log.debug("inside VendorCatalogManagerImpl getVendorCatalogNoteByID");
		return catalogDao.getVendorCatalogNoteByID(vendorCatalogNoteId);
	}

	/**
	 * This method finds the catalog notes for subject entered from
	 * VendorCatalogNote table
	 *
	 * @param String
	 *            noteSubject
	 * @param long vendorCatalogId
	 * @return List VendorCatalogNote
	 */
	public List<VendorCatalogNote> searchNotes(String noteSubject, long vendorCatalogId) {
		log.debug("inside VendorCatalogManagerImpl searchNotes");
		return catalogDao.searchNotes(noteSubject, vendorCatalogId);
	}

	/**
	 * This method adds a note into VendorCatalogNote table
	 *
	 * @param VendorCatalogNote
	 *            vendorCatalogNote
	 * @return VendorCatalogNote
	 */
	public VendorCatalogNote addNote(VendorCatalogNote vendorCatalogNote) {
		log.debug("inside VendorCatalogManagerImpl addNote");
		return catalogDao.addNote(vendorCatalogNote);
	}

	// Vendor Catalog Note End

	/**
	 * Gets the Vendor Catalog details based on the Catalog Id edited in the
	 * session.
	 *
	 * @param String
	 *            catalogID
	 * @return List VendorCatalog
	 */
	public List<VendorCatalog> getVendorCatalogHeader(String catalogID) {
		// Gets the Catalog details of the Vendor based on the Id
		return null;
	}

        /**
         * Search  Catalog Vendors
         * @param catalogVendorDTO CatalogVendorDTO
         * @return List<CatalogVendorDTO>
         */
	public List<CatalogVendorDTO> searchCatalogVendors(CatalogVendorDTO catalogVendorDTO) {
		return this.catalogDao.getCatalogVendors(catalogVendorDTO);
	}

	public CatalogVendorDTO getVendorInfo(String vendorId) {
		return catalogDao.getVednorInfo(vendorId);
	}

         /**
         * Search Vendor Catlogs according to Search Criteria.
         * @param catalogVendorDTO
         * @return List<VendorCatalogDTO>
         */
	public List<VendorCatalogDTO> searchVendorCatalogs(CatalogVendorDTO catalogVendorDTO) {
		return catalogDao.searchVendorCatalogs(catalogVendorDTO);
	}
        /**
         * Get The List of All Open Catalogs.
         * @return List<VendorCatalog>
         */
	public List<VendorCatalog> getOpenCatalogs() {
		return catalogDao.getOpenCatalogs();
	}
        /**
         * Get The List of Open Catalogs using Search Crteria.
         * @return List<VendorCatalog>
         */
	public List<VendorCatalog> searchOpenCatalogs(CatalogVendorDTO searchCriteria) {
		return catalogDao.searchOpenCatalogs(searchCriteria);
	}
        /**
         * Lock/Unlock a Vendor Catalog.
         * @param catalogVendorDTO CatalogVendorDTO
         * @param vendorCatalogId String
         * @param user User
         * @param mode String
         * @return List<VendorCatalogDTO>
         */
	/*public List<VendorCatalogDTO> lockUnlockCatalog(CatalogVendorDTO catalogVendorDTO,
			String vendorCatalogId, User user, String mode) {
		return catalogDao.lockUnlockCatalog(catalogVendorDTO, vendorCatalogId, user, mode);
	}*/
        /**
         * Lock/Unlock a Open Catalog.
         * @param catalogVendorDTO CatalogVendorDTO
         * @param vendorCatalogId String
         * @param user User
         * @param mode String
         * @return List<VendorCatalog>
         */
	/*public List<VendorCatalog> lockUnlockOpenCatalog(CatalogVendorDTO catalogVendorDTO,
			String vendorCatalogId, User user, String mode) {
		return catalogDao.lockUnlockOpenCatalog(catalogVendorDTO, vendorCatalogId, user, mode);
	}*/
	/**
	 * This Method returns the catalog information.
	 * @param long
	 * @return VendorCatalog
	 * */
	public VendorCatalog getCatalogInfo(long catalogId) {
		return catalogDao.getCatalogInfo(catalogId);
	}
	/**
	 * This Method returns the list of BM attributes.
	 * @param String
	 * @param Long
	 * @returns List
	 * */
	public List<String> getBlueMartiniAttribute(String productGroupIds, Long catalogID) {
		return catalogDao.getBlueMartiniAttribute(productGroupIds, catalogID);
	}
        /**
         *Get  the Vendor Catalog Styles.
         * @param catalogStyleDTO VendorCatalogStyleDTO
         * @return List<VendorCatalogStyleDTO>
         */
	public List<VendorCatalogStyleDTO> getVendorStyles(VendorCatalogStyleDTO catalogStyleDTO) {
		return catalogDao.getVendorStyles(catalogStyleDTO);
	}
	/**
	 * This Method returns the list of Catalog Master Attribute
	 * @return List.
	 * */
	public List<CatalogMasterAttribute> getCatalogMasterAttr(String filter) {
		return catalogDao.getCatalogMasterAttr(filter);
	}

	public List<VendorCatalogStyleDTO> getVendorCatalogStyles(VendorCatalogStyleDTO styleDTO) {
		return catalogDao.getVendorCatalogStyles(styleDTO);
	}
        /**
         *
         * @param catalogStyleDTO VendorCatalogStyleDTO
         * @param vendorStyleId String
         * @param vendorCatalogId String
         * @param user User
         * @param mode String
         * @return List<VendorCatalogStyleDTO>
         */
	/*public List<VendorCatalogStyleDTO> lockUnlockVendorStyles(VendorCatalogStyleDTO styleDTO,
			String vendorStyleId, String vendorCatalogId, User user, String mode) {
		return catalogDao.lockUnlockVendorStyles(styleDTO, vendorStyleId, vendorCatalogId, user
				.getUsername(), mode);
	}*/

	public int getCountForPreviousUploads(Map<String, Object> param) {
		return catalogDao.getCountForPreviousUploads(param);
	}

	public boolean uploadCatalogStyleSkuImage(Object file,String uploadDir) throws Exception {
		log.debug("Inside uploadCatalogStyleSkuImage() method.");
		CommonsMultipartFile multipartFile = (CommonsMultipartFile) file;

		
		log.debug("File will be uploaded at the location :" + uploadDir);
		
		/** Create the directory if it doesn't exist */
		File dirPath = new File(uploadDir);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		/** retrieve the file data */

		InputStream stream = multipartFile.getInputStream();

		String uploadFile = uploadDir + multipartFile.getOriginalFilename();
		log.debug("Original File Name:" + multipartFile.getOriginalFilename()
				+ " And file will get uploaded at: " + uploadFile);
		OutputStream bos = new FileOutputStream(uploadFile);
		int bytesRead = 0;
		byte[] buffer = new byte[FILE_BUFFER_SIZE];

		while ((bytesRead = stream.read(buffer, 0, FILE_BUFFER_SIZE)) != -1) {
			bos.write(buffer, 0, bytesRead);
		}
		bos.close();
		/** close the stream */
		stream.close();

		return true;
	}
         /**
          *Get Vendor Style properties.
          * @param String
          * @param String
          * @param String
          * @param String
          * @param String
          * @param String
          * @return VendorStylePropertiesDTO
          */
	public VendorStylePropertiesDTO getVendorStyleProperties(String vendorStyleId,
		String vendorCatalogId, String recordNumber, String catalogTemplateId, String vendorId,String vendorUpc) {
                 if(log.isDebugEnabled()) {
                     log.debug("Start 'getVendorStyleProperties' method");
                 }
		VendorStylePropertiesDTO stylePropertiesDTO = new VendorStylePropertiesDTO();
		String mappedFieldSeq = "";
		String unMappedFieldSeq = "";
                // Get the Vendor Style Info
                stylePropertiesDTO.setStyleInfo(catalogDao.getStyleInfo(vendorStyleId, vendorCatalogId, vendorUpc));
		// STEP 1 Get the all the coulmn values from the catalog record table
		// for the style
		List<String> recordValueList = catalogDao.getCatalogRecord(vendorCatalogId, recordNumber);
		
		// STEP 2 Get the Master attribute mapping.

		List<NameValue> masterAttributeMappingList = catalogDao.getMasterAttributeMapping(
				catalogTemplateId, vendorCatalogId);

		// STEP 3 Get the Blue Marttini Attribute Mapping

		List<NameValue> blueMartiniAttributeMappingList = catalogDao
				.getCatalogAttributeMapping(catalogTemplateId);

		List<CatalogHeaderDTO> unmappedAttributesList = catalogDao
				.getTemplateVendorCatalogHeaderList(Long.parseLong(vendorCatalogId), Long.parseLong(catalogTemplateId));

		// Set the values of Master attributes in NameValue object
		Iterator itr1 = masterAttributeMappingList.iterator();
                int fieldCnt =0;
		while (itr1.hasNext()) {
			NameValue temp = (NameValue) itr1.next();
			int headerNum = Integer.parseInt(temp.getHeaderNumber());
			temp.setAttributeValue(recordValueList.get(headerNum - 1));
			mappedFieldSeq = mappedFieldSeq + headerNum + ",";
                        if(temp.getAttributeName().equals(DropShipConstants.VENDOR_STYLE_DESC)) {
                            stylePropertiesDTO.setUpdateDescription(DropShipConstants.TRUE);
                            stylePropertiesDTO.setDescriptionFieldNo(DropShipConstants.EMPTY_STRING+fieldCnt);
                        }
                        if(temp.getAttributeName().equals(DropShipConstants.COLOR)) {
                            stylePropertiesDTO.setUpdateColor(DropShipConstants.TRUE);
                            stylePropertiesDTO.setColorFieldNo(DropShipConstants.EMPTY_STRING+fieldCnt);
                        }
                        fieldCnt++;

		}

		// Set the values of Blue Martini attributes in NameValue object
		Iterator itr2 = blueMartiniAttributeMappingList.iterator();
		while (itr2.hasNext()) {
			NameValue temp = (NameValue) itr2.next();
			int headerNum = Integer.parseInt(temp.getHeaderNumber());
			temp.setAttributeValue(recordValueList.get(headerNum - 1));
		}

		// Set Mapped Attributes
		List<NameValue> mappedAttributeList = new ArrayList<NameValue>();

		mappedAttributeList.addAll(masterAttributeMappingList);
		Iterator itr3 = blueMartiniAttributeMappingList.iterator();
		while (itr3.hasNext()) {
			NameValue temp = (NameValue) itr3.next();
			int headerNum = Integer.parseInt(temp.getHeaderNumber());
			if (temp.getBlueMartiniAttribute() != null) {
				temp.setAttributeName(temp.getBlueMartiniAttribute());
				mappedAttributeList.add(temp);
				mappedFieldSeq = mappedFieldSeq + headerNum + ",";
			} else {
				unMappedFieldSeq = unMappedFieldSeq + headerNum + ",";

			}
		}
		List unMappedFields = new ArrayList();
		Iterator itr = unmappedAttributesList.iterator();
		while (itr.hasNext()) {
			CatalogHeaderDTO catalogHeaderDTO = (CatalogHeaderDTO) itr.next();
			int headerNum = catalogHeaderDTO.getVendorCatalogFieldNum().intValue();
			NameValue temp = new NameValue();
			temp.setAttributeName(catalogHeaderDTO.getVendorCatalogHeaderFieldName());
			temp.setAttributeValue(recordValueList.get(headerNum - 1));
			unMappedFieldSeq = unMappedFieldSeq + headerNum + ",";
			unMappedFields.add(temp);

		}
		

		// Get Style Images
		List<VendorStyleImageDTO> imageList = catalogDao.getStyleSKUImages(vendorCatalogId,
				vendorStyleId, "");
		List<VendorStyleImageDTO> removeImageList = new ArrayList();
		List<String> tempList1 = new ArrayList<String>();
		List<String> tempList2 = new ArrayList<String>();
		Iterator itr4 = imageList.iterator();
		int cnt = 1;
		while (itr4.hasNext()) {
			VendorStyleImageDTO imageDTO = (VendorStyleImageDTO) itr4.next();
			if (imageDTO.getImageType().equals(DropShipConstants.MAIN)
					&& !tempList1.contains(DropShipConstants.MAIN)) {
				tempList1.add(DropShipConstants.MAIN);
			} else if (imageDTO.getImageType().equals(DropShipConstants.SWATCH)
					&& !tempList1.contains(DropShipConstants.SWATCH)) {
				tempList1.add(DropShipConstants.SWATCH);
			} else if (imageDTO.getImageType().equals(DropShipConstants.ALTERNATE)
					&& !tempList1.contains(ALTERNATE5)) {
				tempList1.add("ALT" + cnt);
				cnt++;
			} else {
				removeImageList.add(imageDTO);
			}

		}
		imageList.removeAll(removeImageList);
		List<VendorUpcDTO> upcList = catalogDao.getUPCList(vendorId, vendorStyleId);

		stylePropertiesDTO.setMappedFields(mappedAttributeList);
		stylePropertiesDTO.setUnmappedList(unMappedFields);
		stylePropertiesDTO.setStyleImages(imageList);
		stylePropertiesDTO.setUpcList(upcList);
		stylePropertiesDTO.setMappedFieldSeq(mappedFieldSeq);
		stylePropertiesDTO.setUnMappedFieldSeq(unMappedFieldSeq);
		stylePropertiesDTO.setVendorCatalogId(vendorCatalogId);
		stylePropertiesDTO.setRecordNum(recordNumber);
		stylePropertiesDTO.setVendorStyleId(vendorStyleId);
                if(log.isDebugEnabled()) {
                     log.debug("End 'getVendorStyleProperties' method");
                 }
		return stylePropertiesDTO;

	}
        /**
         * Get Style Or SKU Images
         * @param vendorCatalogId
         * @param vendorStyleId
         * @param vendorUPC
         * @return
         */
	public List<VendorStyleImageDTO> getStyleSKUImages(String vendorCatalogId,
			String vendorStyleId, String vendorUPC) {
		return catalogDao.getStyleSKUImages(vendorCatalogId, vendorStyleId, vendorUPC);
	}

	public VendorCatalogTemplate saveTemplate(VendorCatalogTemplate vendorCatalogTemplate) {
		return catalogDao.saveTemplate(vendorCatalogTemplate);
	}

	public VendorCatalog save(VendorCatalog vendorCatalog) {
		return catalogDao.save(vendorCatalog);
	}
        /**
         *  Add Image to Style Or Sku.
         * @param vendorCatalogStyleSkuImage
         * @return
         */
	public VendorCatalogStyleSkuImage saveVendorCatalogStyleSkuImageObject(
			VendorCatalogStyleSkuImage vendorCatalogStyleSkuImage) {
		return catalogDao.saveVendorCatalogStyleSkuImageObject(vendorCatalogStyleSkuImage);
	}

	public List<VendorCatalogFieldMapping> saveVendorCatalogFieldMapping(
			List<VendorCatalogFieldMapping> lstVendorCatalogFieldMapping) {
		return catalogDao.saveVendorCatalogFieldMapping(lstVendorCatalogFieldMapping);
	}

	public CatalogGroupTemplate saveGroupTemplate(CatalogGroupTemplate catalogGroupTemplate) {
		return catalogDao.saveGroupTemplate(catalogGroupTemplate);
	}

	public List<MasterAttributeMapping> saveMasterMapping(
			List<MasterAttributeMapping> lstMasterAttributeMapping, Long templateId) {
		return catalogDao.saveMasterMapping(lstMasterAttributeMapping, templateId);
	}

	public List<NameValue> getUPCHeaderNumber(String catalogTemplateId) {
		return catalogDao.getUPCHeaderNumber(catalogTemplateId);
	}

	public boolean saveVendorStyleProperties(VendorStylePropertiesDTO stylePropertiesDTO, User user) {
		return catalogDao.saveStyleProperties(stylePropertiesDTO, user);
	}
        /**
         * Remove Image associated with Style Or SKu
         * @param imageId
         * @param user
         * @return
         */
	public boolean removeImage(String imageId, User user) {
		return catalogDao.removeImage(imageId, user);
	}

	public List<CatalogMasterAttribute> getTemplateCatalogMasterAttr(Long vendorCatalogID, String filter) {
		return catalogDao.getTemplateCatalogMasterAttr(vendorCatalogID, filter);
	}

	public List<String> getTemplateBlueMartiniAttribute(String productGroupIds, Long vendorCatalogID) {
		return catalogDao.getTemplateBlueMartiniAttribute(productGroupIds, vendorCatalogID);
	}

	public List<MasterAttributeMapping> getTemplateMasterAttributeMapping(Long catalogTemplateID) {
		return catalogDao.getTemplateMasterAttributeMapping(catalogTemplateID);
	}

	/**
	 * Gets the Vendor Catalog header values that have not yet been mapped.
	 * @param vendorCatalogID
	 * @param vendorCatalogTemplateID.
	 * @return List<CatalogHeaderDTO>
	 */
	public List<CatalogHeaderDTO> getTemplateVendorCatalogHeaderList(Long vendorCatalogID, Long vendorCatalogTemplateID) {
		return catalogDao.getTemplateVendorCatalogHeaderList(vendorCatalogID, vendorCatalogTemplateID);
	}
	/**
	 * This method returns the Mapped Values from the DB.
	 * @param vendorCatalogID Long.
	 * @param vendorCatalogTemplateID Long.
	 * @return List.
	 * */
	public List<CatalogMappedFieldDTO> getTemplateMappedValueList(Long vendorCatalogID, Long vendorCatalogTemplateID) {
		return catalogDao.getTemplateMappedValueList(vendorCatalogID, vendorCatalogTemplateID);
	}

	public List<String> getVendorFldMappingDataValue(Long vendorCataloID, Long catalogHeaderFldNum) {
		return catalogDao.getVendorFldMappingDataValue(vendorCataloID, catalogHeaderFldNum);
	}

	public List<String> getCarFldMappingDatavalue(String blueMartiniAttribute) {
		return catalogDao.getCarFldMappingDatavalue(blueMartiniAttribute);
	}

	public List<AttributeLookupValue> getAttributeLookUpValues(String attributeValue) {
		return catalogDao.getAttributeLookUpValues(attributeValue);
	}

	public VendorCatalogFieldDataMapping saveDataFieldMapping(
			VendorCatalogFieldDataMapping vendorCatalogFldDataMapping) {
		return catalogDao.saveDataFieldMapping(vendorCatalogFldDataMapping);
	}

	/**
	 * Update Vendor Properties whetehr to display the vendor in catalog vendor
	 * list or not.
	 *
	 * @param vendorId
	 *            String
	 * @param display
	 *            String
	 * @return boolean
	 */
	public boolean saveVendorProperties(String vendorId, String display) {
		return catalogDao.saveVendorProperties(vendorId, display);
	}

	public List<FieldMappingDataDTO> getSavedFieldMappingData(Long vendorCatalogID,
			String vendorFieldHeader) {
		return catalogDao.getSavedFieldMappingData(vendorCatalogID, vendorFieldHeader);
	}

	public List<VendorCatalogTemplate> getCatlogTemplateForVendor(Long vendorID) {
		return catalogDao.getCatlogTemplateForVendor(vendorID);
	}

	public List<VendorCatalogTemplate> getGlobalTemplates() {
		return catalogDao.getGlobalTemplates();
	}

	public String getVendorCatalogTemplateId(String vendorCatalogId) {
		return catalogDao.getVendorCatalogTemplateId(vendorCatalogId);
	}

	public List<Vendor> getOtherVendors(Long vendorID){
		return catalogDao.getOtherVendors(vendorID);
	}

	public int getCountOfTemplateName(String templateName){
		return catalogDao.getCountOfTemplateName(templateName);
	}
	 /** This Method is used to upload the file to the other destination machine.
	 * @return boolean.
	 * */
	public boolean upLoadFile(Properties properties, CommonsMultipartFile multipartFile, String vendorNumber, String catalogId)
			throws IOException {

		String uploadDir = properties.getProperty("catalogFilePath");
		//String uploadDir="C://cars//data//catalogs//dev";
		//Appending the Vendor Number in Path
		uploadDir = uploadDir + "/"+vendorNumber + "/" + catalogId;
		if(log.isDebugEnabled()){
			log.debug("File will be uploaded to :"+uploadDir );
		}
		/** Create the directory if it doesn't exist */
		File dirPath = new File(uploadDir);
		if (!dirPath.exists()) {
			log.debug("directory path does not exist so create");
			dirPath.mkdirs();
		}

		/** retrieve the file data */
		InputStream stream = multipartFile.getInputStream();
		String uploadFile = uploadDir + File.separator + multipartFile.getOriginalFilename();
		OutputStream bos = new FileOutputStream(uploadFile);
		int bytesRead = 0;
		byte[] buffer = new byte[FILE_BUFFER_SIZE];

		while ((bytesRead = stream.read(buffer, 0, FILE_BUFFER_SIZE)) != -1) {
			bos.write(buffer, 0, bytesRead);
		}
		bos.close();
		/** close the stream */
		stream.close();

		return true;

	}
	/**
	 * This method returns the Master Attribute Mapping.
	 * @param Long
	 * @param Long
	 * */
	public List<MasterAttributeMapping> getMasterMappingAttribute(Long catalogTemplateID, String fieldName) {
		return catalogDao.getMasterMappingAttribute(catalogTemplateID, fieldName);
	}
	/**
	 * This method gets the vendor catalog template  from
	 * VendorCatalogTemplate table
	 * 
	 * @param String
	 *            vendorCatalogTmplName
	 * @return VendorCatalogTemplate
	 */
	public VendorCatalogTemplate getVendorCatalogTemplate(String vendorCatalogTemplateName) {
		log.debug("inside VendorCatalogManagerImpl getVendorCatalogTemplate");
		return catalogDao.getVendorCatalogTemplate(vendorCatalogTemplateName);

	}
	/**
	 * This method removes the mapping records from the database.
	 * @param catalogTemplate VendorCatalogTemplate.
	 * */
	public void removeMappingAttribute(VendorCatalogTemplate catalogTemplate)  {
		catalogDao.removeMappingAttribute(catalogTemplate);
	}

	/**
	 * @param cls
	 * @param id
	 * @return
	 * @see com.belk.car.app.service.VendorCatalogManager#getById(java.lang.Class, java.io.Serializable)
	 * @Enclosing_Method  getById
	 * @Date Mar 17, 2010 
	 * @User afusy07 - Priyanka Gadia
	 * @TODO
	 */
	public Object getById(Class cls, Serializable id) {
		return catalogDao.getById(cls, id);
	}

         public void addImageToStyleSku(String vendorStyleId,String vendorCatalogId,String vendorUpc,char imageType,String fileName,String vendorNumber,String user) {

                  VendorCatalogStyleSku catalogStyleSku =  new VendorCatalogStyleSku();
                  VendorCatalogStyleSkuId catalogStyleSkuId =  new VendorCatalogStyleSkuId();
                  long catalogId =Long.parseLong(vendorCatalogId);

                  catalogStyleSkuId.setVendorCatalogId(catalogId);
                  catalogStyleSkuId.setVendorStyleId(vendorStyleId);
                  catalogStyleSkuId.setVendorUPC(vendorUpc);
                  catalogStyleSku =  (VendorCatalogStyleSku)getById(VendorCatalogStyleSku.class,catalogStyleSkuId);
                  processImage(catalogId, vendorNumber, vendorStyleId, vendorStyleId, vendorUpc, vendorStyleId, imageType, fileName,user);


        }


        /**
	 * @param catalogId
	 * @param vendorNumber
	 * @param styleId
	 * @param vendorStyleNum
	 * @param vendorUpc
	 * @param color
	 * @param imageType
	 * @param imageName
	 * @return void
	 * @Enclosing_Method processImage
	 * @TODO Copy image from unmapped folder to mapped folder with proper naming
	 *       convention. Save image record in VNDR_CATL_STY_SKU_IMAGE table
	 */
	public void processImage(
			long catalogId, String vendorNumber, String styleId, String vendorStyleNum,
			String vendorUpc, String color, char imageType, String imageName,String user) {
		// Read image from the generated path for vendor number,catalog id
		Properties properties = PropertyLoader.loadProperties("ftp.properties");
		String destinationPath = properties.getProperty("imagePath");
		StringBuffer sourceDir =new StringBuffer(destinationPath).append(vendorNumber);

		sourceDir.append("_");
		sourceDir.append(catalogId);
		sourceDir.append(File.separator);
		sourceDir.append("Unmapped");
		sourceDir.append(File.separator);
		sourceDir.append(imageName);

		String extention = getExtention(imageName);
		if(log.isDebugEnabled()){
			log.debug("Extention===" + extention);
		}
		File srcFile = new File(sourceDir.toString());
		
		if (srcFile.isDirectory() || !srcFile.exists()) {

			if(log.isDebugEnabled()){
				log.debug("File not found returning......." + sourceDir.toString());
			}
			return;
		}

		// Generate the new file name for the image recieved
		// Image name - mapped/vendorNum_VendorStyleNum_ImageType_ColorCode.ext

		StringBuffer destinationDir =new StringBuffer(destinationPath).append(vendorNumber);

		destinationDir.append("_");
		destinationDir.append(catalogId);
		destinationDir.append(File.separator);
		destinationDir.append("mapped");
		File destdir = new File(destinationDir.toString());
		if (!destdir.exists()) {
			destdir.mkdirs();
		}
		StringBuffer fileName = new StringBuffer(vendorNumber);
		fileName.append("_");
		fileName.append(vendorStyleNum);
		fileName.append("_");

		switch (imageType) {
			case 'M':
				fileName.append("A");
				fileName.append("_");
				break;
			case 'S':
				fileName.append("SW");
				fileName.append("_");
				break;
			case '1':
				fileName.append("B");
				fileName.append("_");
				break;
			case '2':
				fileName.append("C");
				fileName.append("_");
				break;
			case '3':
				fileName.append("D");
				fileName.append("_");
				break;
			case '4':
				fileName.append("E");
				fileName.append("_");
				break;
			case '5':
				fileName.append("F");
				fileName.append("_");
				break;
			default:
				if(log.isDebugEnabled()){
					log.debug("Invalid image type for vendor style." + styleId + " and vendorUpc="
							+ vendorUpc);
				}
			break;

		}
		if (!StringUtils.isBlank(color)) {
			fileName.append(color);

		}
		else {
			fileName.append("000");

		}
		fileName.append(extention);
		if(("dummy".equals(imageName) && imageType=='M') || (sourceDir.indexOf("dummy")>-1) ){
			fileName = new StringBuffer("dummy").append(".jpg");
			log.debug("filename dummy="+fileName);
		}

		File destFile = new File(destdir, fileName.toString());
		FileInputStream fis = null;
		FileChannel srcChannel = null;
		FileOutputStream fos = null;
		FileChannel destChannel = null;

		// Move the file from source to destination.

		try {
			fis = new FileInputStream(srcFile);
			fos = new FileOutputStream(destFile);
			srcChannel = fis.getChannel();
			destChannel = fos.getChannel();
			srcChannel.transferTo(0, srcFile.length(), destChannel);
		}
		catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		catch (IOException e) {

			e.printStackTrace();
		}
		finally {
			try {
				srcChannel.close();
				destChannel.close();
				fis.close();
				fos.close();
			}
			catch (IOException e) {

				e.printStackTrace();
			}
		}

		// Save Record to image table

		VendorCatalogStyleSkuImage styleSkuImageModel = new VendorCatalogStyleSkuImage();
		styleSkuImageModel.setVendorCatalogId(catalogId);
		styleSkuImageModel.setVendorStyleId(styleId);
		styleSkuImageModel.setVendorUpc(vendorUpc);
		int i = destFile.getAbsolutePath().indexOf(vendorNumber);

		// Get the image path excluding root path
		String path = (i > -1) ? destFile.getAbsolutePath().substring(i ,
				destFile.getAbsolutePath().length()) : destFile.getAbsolutePath();
		if("dummy".equals(imageName) && imageType=='M'){
			path=destinationDir.toString()+File.separator+imageName;
			log.debug("path="+path);
		}
		styleSkuImageModel.setImageFileName(path);
		styleSkuImageModel.setStatus(Status.ACTIVE);

		switch (imageType) {
			case 'M':
				styleSkuImageModel.setImageType("MAIN");
				break;
			case 'S':
				styleSkuImageModel.setImageType("SWATCH");
				break;
			case '1':
				styleSkuImageModel.setImageType("ALT");
				break;
			case '2':
				styleSkuImageModel.setImageType("ALT");
				break;
			case '3':
				styleSkuImageModel.setImageType("ALT");
				break;
			case '4':
				styleSkuImageModel.setImageType("ALT");
				break;
			case '5':
				styleSkuImageModel.setImageType("ALT");
				break;
			default:
				if(log.isDebugEnabled()){
					log.debug("Invalid image type for vendor style." + styleId + " and vendorUpc="
							+ vendorUpc);
				}
			break;

		}
                styleSkuImageModel.setCreatedBy(user);
                styleSkuImageModel.setUpdatedBy(user);
		dropshipDao.saveImageForStyleSku(styleSkuImageModel);

	}

        /**
	 * @param imageType
	 * @return
	 * @return String
	 * @Enclosing_Method getExtention
	 * @TODO To get extention of the image files.
	 */
	private String getExtention(String imageType) {
		int i = imageType.lastIndexOf('.');
		return (i > -1) ? imageType.substring(i, imageType.length()) : imageType;

	}

		public VendorCatalogFieldDataMapping getMappingObjectById(
				CompositeKeyForDataFldMapping key) {
			VendorCatalogFieldDataMapping mapping = this.catalogDao.getMappingObjectById(key);
			return mapping;
		}

		/**
		 * Method to move images from previous vendor catalog folder to new vendor catalog unmapped folder
		 * @author afusy45
		 * @param
		 * @
		 */
		public void methodToMovePreviousCatalogImages(String vcID,String vendorNumber, VendorCatalog oldVendorCatalog) {
			if(log.isDebugEnabled()){
				log.debug("Inside method to move images from previous catalog folder to new catalog folder");
			}
			Properties properties = PropertyLoader.loadProperties("ftp.properties");
			String destinationPath = properties.getProperty("imagePath");
			String oldVendorImagesPath = destinationPath + oldVendorCatalog.getVendor().getVendorNumber() + "_" + oldVendorCatalog.getVendorCatalogID() + SLASH +UNMAPPED;
			String newVendorImagesPath = destinationPath + vendorNumber + "_" + vcID + SLASH + UNMAPPED;
			if(log.isInfoEnabled()){
				log.info("destinationPath"+destinationPath);
				log.info("oldVendorImagesPath"+oldVendorImagesPath);
				log.info("newVendorImagesPath"+newVendorImagesPath);
			}
			File srcFile = new File(oldVendorImagesPath);
			if (!srcFile.isDirectory() || !srcFile.exists()) {

				if (log.isDebugEnabled()) {
					log.info("File not found returning......." + srcFile.toString());
				}
				
			}
			File destdir = new File(newVendorImagesPath);
			if (!destdir.exists()) {
				destdir.mkdirs();
				log.info("Creating the directory-" + destdir.getPath());
			}

			File destFile = null;
			FileInputStream fis = null;
			FileChannel srcChannel = null;
			FileOutputStream fos = null;
			FileChannel destChannel = null;

			// Move the file from source to destination.

			
			File[] files = srcFile.listFiles(); // listing images files present at
			// source location
			if (files != null) {

				// ** copying images from source to destination *//*
				List<File> imageFiles = FtpUtil.getImageFilesFromDirectory(files);
				if(imageFiles != null){
					for (int i = 0; i < imageFiles.size(); i++) {
					
						try {
							destFile = new File(destdir, imageFiles.get(i).getName());
							log.info("Moving file " + imageFiles.get(i).getName() + " to "
									+ destFile.getAbsoluteFile());
							fis = new FileInputStream(imageFiles.get(i));
							fos = new FileOutputStream(destFile);
							srcChannel = fis.getChannel();
							destChannel = fos.getChannel();
							srcChannel.transferTo(0, imageFiles.get(i).length(), destChannel);
						}
						catch (FileNotFoundException e) {

							e.printStackTrace();
						}
						catch (IOException e) {

							e.printStackTrace();
						}
						finally {
							try {

								srcChannel.close();
								destChannel.close();
								fis.close();
								fos.close();
							}
							catch (IOException e) {
								log.error("Exception closing channels"+e);
							} 
						}

					}//for loop
					
				}//if image files not null
			}//If files is not null

			
		}

		public List<String> getCarFldMappingDatavalue(
				String blueMartiniAttribute, String[] selectedProductGroups, Long catalogId) {
			
			return this.catalogDao.getCarFldMappingDatavalue(blueMartiniAttribute, selectedProductGroups, catalogId);
		}
		
		 
		 public void lockCatalog(String vendorCatalogId,User user){
			 catalogDao.lockCatalog(vendorCatalogId,user);
		 }
		 
		public void unlockCatalogs(String userName){
			catalogDao.unlockCatalogs(userName);
		}
		
		public void removeVendorCatalog(String vendorCatalogId){
			catalogDao.removeVendorCatalog(vendorCatalogId);
		}
		
}

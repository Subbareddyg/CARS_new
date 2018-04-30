/**
 * This class provides a methods to fetch details from SMART systems as well as corresponding records from cars db.
 */
package com.belk.car.app.service.impl;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.impl.UniversalManagerImpl;

import com.belk.car.app.Constants;
import com.belk.car.app.dao.CarManagementDao;
import com.belk.car.app.dao.PIMAttributeDao;
import com.belk.car.app.dao.PIMAttributeDao.Tables;
import com.belk.car.app.dao.VendorSkuPIMAttributeDao;
import com.belk.car.app.dto.PackDTO;
import com.belk.car.app.dto.SkuDTO;
import com.belk.car.app.dto.StyleDTO;
import com.belk.car.app.dto.vendorStylePIMAttributeDTO;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.AttributeLookupValue;
import com.belk.car.app.model.AttributeValueProcessStatus;
import com.belk.car.app.model.BaseAuditableModel;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarNote;
import com.belk.car.app.model.CarOutfitChild;
import com.belk.car.app.model.CarReopenPetDetails;
import com.belk.car.app.model.CarSkuAttribute;
import com.belk.car.app.model.CarsPIMAttributeMapping;
import com.belk.car.app.model.CarsPIMGlobalAttributeMapping;
import com.belk.car.app.model.CarsPimItemUpdate;
import com.belk.car.app.model.Config;
import com.belk.car.app.model.NoteType;
import com.belk.car.app.model.PIMAttribute;
import com.belk.car.app.model.PoUnitDetail;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorSkuPIMAttribute;
import com.belk.car.app.model.VendorSkuPIMAttributeId;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.VendorStylePIMAttribute;
import com.belk.car.app.model.VendorStylePIMAttributeId;
import com.belk.car.app.model.VendorStyleType;
import com.belk.car.app.service.AttributeManager;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.CarsPIMAttributeMappingManager;
import com.belk.car.app.service.CarsPIMGlobalAttributeMappingManager;
import com.belk.car.app.service.PIMAttributeExclusionListManager;
import com.belk.car.app.service.PIMAttributeManager;
import com.belk.car.app.service.PIMFtpImageManager;
import com.belk.car.app.util.PropertyLoader;
import com.belk.car.product.integration.exception.BelkProductIntegrationException;
import com.belk.car.product.integration.request.data.GetGroupRequestType;
import com.belk.car.product.integration.request.data.GetRequestType;
import com.belk.car.product.integration.request.data.GroupIntegrationRequestData;
import com.belk.car.product.integration.request.data.IntegrationRequestData;
import com.belk.car.product.integration.request.data.PackGroupRequestData;
import com.belk.car.product.integration.request.data.PackItemRequestData;
import com.belk.car.product.integration.response.data.AttributeData;
import com.belk.car.product.integration.response.data.GroupCatalogData;
import com.belk.car.product.integration.response.data.GroupCollectionSpecData;
import com.belk.car.product.integration.response.data.GroupIntegrationResponseData;
import com.belk.car.product.integration.response.data.IntegrationResponseData;
import com.belk.car.product.integration.response.data.ItemCatalogData;
import com.belk.car.product.integration.response.data.ItemCatalogSpecData;
import com.belk.car.product.integration.response.data.ItemComplexPackSpecData;
import com.belk.car.product.integration.response.data.ItemDifferentiatorsData;
import com.belk.car.product.integration.response.data.ItemSkuSpecData;
import com.belk.car.product.integration.response.data.ItemSupplierData;
import com.belk.car.product.integration.service.BelkGroupService;
import com.belk.car.product.integration.service.BelkProductService;
import com.belk.car.util.DateUtils;
import com.belkinc.services.api.VendorNameService;
import com.belkinc.services.product.BelkProductServiceStub;
import com.belkinc.services.product.BelkProductServiceStub.ColorCode_type0;
import com.belkinc.services.product.BelkProductServiceStub.Differentiators_type0;
import com.belkinc.services.product.BelkProductServiceStub.Differentiators_type2;
import com.belkinc.services.product.BelkProductServiceStub.Item_Catalog_type0;
import com.belkinc.services.product.BelkProductServiceStub.Item_Ctg_Spec_type0;
import com.belkinc.services.product.BelkProductServiceStub.Item_SKU_Spec_type0;
import com.belkinc.services.product.BelkProductServiceStub.Level_type1;
import com.belkinc.services.product.BelkProductServiceStub.PackType;
import com.belkinc.services.product.BelkProductServiceStub.SecondaryCategory_type0;
import com.belkinc.services.product.BelkProductServiceStub.SizeCode_type0;
import com.belkinc.services.product.BelkProductServiceStub.SkuList_type0;
import com.belkinc.services.product.BelkProductServiceStub.Sku_type1;
import com.belkinc.services.product.BelkProductServiceStub.StyleList_type0;
import com.belkinc.services.product.BelkProductServiceStub.Style_type0;
import com.belkinc.services.product.BelkProductServiceStub.Supplier_type0;
import com.belkinc.services.product.BelkProductServiceStub.UPCs_type0;
import com.belkinc.services.product.BelkProductServiceStub.VendorNumber_type0;
import com.belkinc.services.product.BelkProductServiceStub.VendorProductNumber_type0;
import com.belkinc.services.product.BelkProductServiceStub.VendorStyleList_type0;
import com.belkinc.services.product.BelkProductServiceStub.VendorStyle_type0;
import com.belkinc.services.product.FaultMsg;



/**
 * @author antoniog
 *
 */
public class PIMAttributeManagerImpl extends UniversalManagerImpl implements
		PIMAttributeManager {

	private PIMAttributeDao pimAttributeDao;
    private CarManager carManager;
    private CarLookupManager carLookupManager;
    private AttributeManager attributeManager;
    private PIMAttributeExclusionListManager pimAttributeExclusionListManager;
	private CarsPIMAttributeMappingManager carsPIMAttributeMappingManager;
	private CarsPIMGlobalAttributeMappingManager carsPIMGlobalAttributeMappingManager;
	private VendorSkuPIMAttributeDao vendorSkuPIMAttributeDao;
	private CarManagementDao carManagementDao;
	private PIMFtpImageManager pimFtpImageManager;

    public void setPimFtpImageManager(PIMFtpImageManager pimFtpImageManager) {
        this.pimFtpImageManager = pimFtpImageManager;
    }


    public void setCarManagementDao(CarManagementDao carManagementDao) {
        this.carManagementDao = carManagementDao;
    }


    public void setVendorSkuPIMAttributeDao(
            VendorSkuPIMAttributeDao vendorSkuPIMAttributeDao) {
        this.vendorSkuPIMAttributeDao = vendorSkuPIMAttributeDao;
    }


    public void setCarsPIMGlobalAttributeMappingManager(
            CarsPIMGlobalAttributeMappingManager carsPIMGlobalAttributeMappingManager) {
        this.carsPIMGlobalAttributeMappingManager = carsPIMGlobalAttributeMappingManager;
    }


    public void setCarsPIMAttributeMappingManager(
			CarsPIMAttributeMappingManager carsPIMAttributeMappingManager) {
		this.carsPIMAttributeMappingManager = carsPIMAttributeMappingManager;
	}


	public void setPimAttributeExclusionListManager(
			PIMAttributeExclusionListManager pimAttributeExclusionListManager) {
		this.pimAttributeExclusionListManager = pimAttributeExclusionListManager;
	}


	public void setPimAttributeDao(PIMAttributeDao pimAttributeDao) {
		this.pimAttributeDao = pimAttributeDao;
	}


	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}

    public void setAttributeManager(AttributeManager attributeManager) {
        this.attributeManager = attributeManager;
    }

	public void setCarLookupManager(CarLookupManager carLookupManager) {
        this.carLookupManager = carLookupManager;
    }



    private transient final Log log = LogFactory.getLog(PIMAttributeManagerImpl.class);

	/**
	 *  This method is used to get the vendor style details including style attributes from SMART system by passing ORIN number.
	 * @param List of style ORIN numbers
	 * @return Map of <StyleOrin, styleDTO>
	 * @throws Exception
	 *
	 * */
	public Map<String, StyleDTO> getStylesDetailsFromSMART(List<String> vStyleOrinList) throws Exception {

		Map<String,StyleDTO> styleDtoMap = new HashMap<String,StyleDTO>();
		BelkProductServiceStub.GetItemResponse styleResponse = null;
		//return null if vStyleOrinList list is empty
		if(vStyleOrinList==null || vStyleOrinList.size()<1){
			return null;
		}
		try {
			log.info(" get Style details from  SMART ");
			BelkProductServiceStub stub = new BelkProductServiceStub();
			//get the vendor webservice request object
			BelkProductServiceStub.GetItemRequest styleRequest = new  BelkProductServiceStub.GetItemRequest();
			StyleList_type0  styleList = new StyleList_type0();

			//create List of styles to pass to webservice
			for(String strStyle : vStyleOrinList){
				Style_type0 style = new Style_type0();
				style.setStyle_type0(strStyle);
				styleList.addStyle(style);
			}
			styleRequest.setStyleList(styleList);

			//call webervice to get style details
			styleResponse=stub.getItem(styleRequest) ;
			Item_Catalog_type0[] styleCatlogList = styleResponse.getItem_Catalog();

			for(Item_Catalog_type0 styleCatalog: styleCatlogList){
				if(!"Style".equalsIgnoreCase(styleCatalog.getType().toString())){
					continue;
				}
				StyleDTO styleDto = new StyleDTO();
				try{
					//get the Merchandise_Hierarchy from WS response
					SecondaryCategory_type0[] secondCatagoryList = styleCatalog.getSecondaryCategory();
					SecondaryCategory_type0 secondCatagory = secondCatagoryList[0];
					Level_type1[] levels = secondCatagory.getLevels().getLevel();
					for(Level_type1 level: levels){
						if("Department".equals(level.getName())){
							//we only set the department number, we do not receive dept name from SMART
							styleDto.setDepartmentNumber(level.toString());
						}else if("Class".equals(level.getName())){
							//we only set the Class number, we do not receive class name from SMART
							styleDto.setClassNumber(level.toString());
						}
					}

					//Get style details from WS response
					Item_Ctg_Spec_type0 styleCatgSpec = styleCatalog.getItem_Ctg_Spec();
					styleDto.setOrin(styleCatgSpec.getId());
					//styleDto.setVendorStyle(styleCatgSpec.getDisplay());
					styleDto.setVendorStyleDescription(styleCatgSpec.getDescription().getLong());

					//Get vendor details from WS response
					Supplier_type0 supplier = null;
					Supplier_type0[]  suppliers = styleCatgSpec.getSupplier();
					// Create the CAR only for primary supplier
					for (Supplier_type0 Supplier_type : suppliers) {
						if (Supplier_type.getPrimary_Flag()) {
							supplier = Supplier_type;
							break;
						}
					}
					styleDto.setVendorNumber(supplier.getId());
					styleDto.setVendorStyle(supplier.getVPN());
					//styleDto.setVendorName(supplier.getPlanning_Brand().toString());
					if(StringUtils.isNotBlank(supplier.getId()))
					styleDto.setVendorName(getVendorNameFromSMART(supplier.getId()));
					//Logic for populating UDA attributes will be moved to new getItem.
					// Setting empty list to avoid NPE.
					List<vendorStylePIMAttributeDTO> pimAttrDTOList = new ArrayList<vendorStylePIMAttributeDTO>();
					styleDto.setPimAttributeDTOList(pimAttrDTOList);
					//add the style to styleDTOList
					styleDtoMap.put(styleDto.getOrin(), styleDto);
				}catch(Exception e){
					log.error("Exception while fetching data from webservice response " +e.getMessage());
					throw e;
				}
			}
		} catch (FaultMsg e) {
			log.error("Received unwanted reponse from webservice "+e.getMessage());
			throw e;
		}catch (AxisFault e) {
			log.error("AxisFault while calling the webservice  "+e.getMessage());
			log.error("fault reason: "+ e.getReason());
			throw e;
		}catch (RemoteException e) {
			log.error("not able to connect to Webservice  "+e.getMessage());
			throw e;
		}
		catch(Exception e){
			log.error("Exception while fetching data from webservice response " +e.getMessage());
			throw e;
		}
		return styleDtoMap;
	}


	/*
	 * This method is used to insert values to PIM_ATTRIBUTE table if any new pimAttribute name received from smart system
	 * After adding the PIM attribute to DB it will get the pim_attribute_id from DB
	 * and map it to  the corresponding pimattribute name received.
	 *
	 *TODO Santosh- method Comments doesnot match to the actual code functionality
	 * */

	@Override
	public VendorStyle syncPIMAttributes(VendorStyle vendorStyle,
			List<vendorStylePIMAttributeDTO> vsPimAttrlist, User systemUser,
			boolean isReterivePIMAttr) throws Exception {

		try {
			log.info("Entered the method syncPIMAttributesUpdate"
					+ vendorStyle.getVendorStyleId());
			Set<VendorStylePIMAttribute> vsPimAttributeList = vendorStyle
					.getVendorStylePIMAttribute();
			boolean isVendorStyleExistsInDB = false;
			boolean update = false;
			Map<Long, String> pimAttrMap = new HashMap<Long, String>();
			List<Long> pimAttrIdlist = new ArrayList<Long>();
			// Setting the isVendorStyleExistsInDB flag to true if the vendor
			// style already exists in DB.
			if (vendorStyle.getVendorStyleId() > 0) {
				isVendorStyleExistsInDB = true;
				// setting the map of pim_id and value of the exiting style in
				// DB
				for (VendorStylePIMAttribute vspa : vsPimAttributeList) {
					pimAttrMap.put(vspa.getPimAttribute().getPimAttrId(), vspa
							.getValue());
					pimAttrIdlist.add(vspa.getPimAttribute().getPimAttrId());
					log.info("Existing PIM attribute ID from DB for vendorstyle: "
									+ vspa.getPimAttribute().getPimAttrId());// debug
				}
			} else {
				vendorStyle = (VendorStyle) pimAttributeDao.save(vendorStyle);
				log.info("VendorStyle created with Id : "
						+ vendorStyle.getVendorStyleId());
			}
			if (vendorStyle.getVendorStylePIMAttribute() == null)
				vendorStyle
						.setVendorStylePIMAttribute(new HashSet<VendorStylePIMAttribute>());
			// get all the pim attributes from the DB and put name and Id in MAP
			List<PIMAttribute> pimAttrlist = pimAttributeDao
					.getAllPIMAttributes();
			Map<String, Long> pimAttrlistMap = new HashMap<String, Long>();
			for (PIMAttribute pAtt : pimAttrlist) {
				pimAttrlistMap.put(pAtt.getName(), pAtt.getPimAttrId());
			}
			List<Long> pimAttrDtoIdList = new ArrayList<Long>();
			log.info("isVendorStyleExistsInDB==>" + isVendorStyleExistsInDB);
			int count = -1;
			if(log.isDebugEnabled())
			log
					.debug("Size of vendor style Pim Attribute list from smart for this VendorStyle"
							+ vsPimAttrlist.size());// debug
			List<VendorStylePIMAttribute> vsPimAttrList = new ArrayList<VendorStylePIMAttribute>();
			// Iterating the styleDTO from smart for processing
			for (vendorStylePIMAttributeDTO vsPimNameList : vsPimAttrlist) {
				String tmpValue = vsPimNameList.getValue();
				if (StringUtils.isEmpty(tmpValue) || tmpValue == null) {
					continue;
				}
				VendorStylePIMAttribute vendorStylePIMAttribute = new VendorStylePIMAttribute();

				// setting the updated by and date for the
				// vendorstyle_pim_attribute Table.
				if (isVendorStyleExistsInDB) {
					vendorStylePIMAttribute.setUpdatedBy(systemUser
							.getEmailAddress());
					vendorStylePIMAttribute.setUpdatedDate(new Date());
				} else {
					vendorStylePIMAttribute.setCreatedBy(systemUser
							.getEmailAddress());
					vendorStylePIMAttribute.setCreatedDate(new Date());
					vendorStylePIMAttribute.setUpdatedBy(systemUser
							.getEmailAddress());
					vendorStylePIMAttribute.setUpdatedDate(new Date(1970));
				}

				// if pim attribute name is already present in cars DB if
				// present add it to to the vendor style
				if (pimAttrlistMap.containsKey(vsPimNameList
						.getPimAttributeName())) {
					count++;
					if(log.isDebugEnabled())
					log.debug("PIM attribute Exixtes in DB :   ==>"
							+ vsPimNameList.getPimAttributeName() + "=value="
							+ vsPimNameList.getValue()); // debug

					if (isVendorStyleExistsInDB) {
						long pimid = pimAttrlistMap.get(vsPimNameList
								.getPimAttributeName());

						pimAttrDtoIdList.add(pimid);
						if (pimAttrMap.containsKey(pimid)) {
							if ((null != pimAttrMap.get(pimid) && !""
									.equals(pimAttrMap.get(pimid)))
									&& null != vsPimNameList.getValue()
									&& !"".equals(vsPimNameList.getValue())) {
								if (pimAttrMap.get(pimid).equals(
										vsPimNameList.getValue())) {
									continue;
								}
							} else if ((null == pimAttrMap.get(pimid) || ""
									.equals(pimAttrMap.get(pimid)))
									&& (null == vsPimNameList.getValue() || ""
											.equals(vsPimNameList.getValue()))) {
								continue;
							}

						}
						if(log.isDebugEnabled())
						log.debug("DB value :" + pimAttrMap.get(pimid)
								+ ":smart value:" + vsPimNameList.getValue());
					}
					update = true;
					VendorStylePIMAttributeId vendorStylePIMAttributeId = new VendorStylePIMAttributeId(
							vendorStyle.getVendorStyleId(), pimAttrlistMap
									.get(vsPimNameList.getPimAttributeName()));
					vendorStylePIMAttribute.setId(vendorStylePIMAttributeId);
					vendorStylePIMAttribute.setVendorStyle(vendorStyle);
					vendorStylePIMAttribute.setPimAttribute(pimAttrlist
							.get(count));
					vendorStylePIMAttribute.setValue(vsPimNameList.getValue());
					this.setAuditInfo(systemUser, vendorStylePIMAttribute);
					vsPimAttrList.add(vendorStylePIMAttribute);
					vendorStyle.getVendorStylePIMAttribute().add(
							vendorStylePIMAttribute);

				} else {
					log.info("New PIM attribute not in DB"
							+ vsPimNameList.getPimAttributeName()); // debug
					// if new pim attribute name then create new pim attribute
					// and then add it to vendor style
					PIMAttribute patt = new PIMAttribute();
					patt.setName(vsPimNameList.getPimAttributeName());
					patt.setDescr(vsPimNameList.getPimAttributeName());
					patt.setIsPimAttrDisplayble(Constants.FLAG_NO);
					this.setAuditInfo(systemUser, patt);
					patt = (PIMAttribute) pimAttributeDao.save(patt);
					if (log.isDebugEnabled())
						log.debug("New PIM attribute saved id :"
								+ patt.getPimAttrId()); // debug
					if (isVendorStyleExistsInDB) {
						pimAttrDtoIdList.add(patt.getPimAttrId());
					}
					update = true;
					VendorStylePIMAttributeId vendorStylePIMAttributeId = new VendorStylePIMAttributeId(
							vendorStyle.getVendorStyleId(), patt.getPimAttrId());
					vendorStylePIMAttribute.setId(vendorStylePIMAttributeId);
					vendorStylePIMAttribute.setVendorStyle(vendorStyle);
					vendorStylePIMAttribute.setPimAttribute(patt);
					vendorStylePIMAttribute.setValue(vsPimNameList.getValue());
					this.setAuditInfo(systemUser, vendorStylePIMAttribute);
					vsPimAttrList.add(vendorStylePIMAttribute);
					vendorStyle.getVendorStylePIMAttribute().add(
							vendorStylePIMAttribute);
				}

			}
			// Updating the VendorSstyle with vendorStylePIMAttribute.
			List<VendorStylePIMAttribute> vsPimAttList = new ArrayList<VendorStylePIMAttribute>();
			// Removing the Existing vendorStylePIMAttribute which is not
			// received from smart for the corresponding vendorStyle.
			if (isVendorStyleExistsInDB && pimAttrIdlist.size() > 0) {
				if(log.isDebugEnabled())
				log.debug("pimAttrIdlist.size()" + pimAttrIdlist.size()
						+ "pimAttrDtoIdList.size()" + pimAttrDtoIdList.size());
				pimAttrIdlist.removeAll(pimAttrDtoIdList);
				Set<VendorStylePIMAttribute> vsPimAttset = new HashSet<VendorStylePIMAttribute>(
						vendorStyle.getVendorStylePIMAttribute());
				for (VendorStylePIMAttribute vsPimAttr : vsPimAttset) {
					if (pimAttrIdlist
							.contains(vsPimAttr.getId().getPimAttrId())) {
						log
								.info("Existing vendorStylePIMAttribute removed from smart for the vendorstyle:"
										+ vsPimAttr.getId().getPimAttrId());// debugg
						// pimAttributeDao.delete(vsPimAttr);
						vsPimAttList.add(vsPimAttr);
						vendorStyle.getVendorStylePIMAttribute().remove(
								vsPimAttr);
						if (!isReterivePIMAttr) {
							vsPimAttr.setValue(null);
							vendorStyle.getVendorStylePIMAttribute().add(
									vsPimAttr);
						}
						update = true;
					}
				}
			}
			if (update) {
				log.info("updating vendorstyle pim attributes ,size: "
						+ vendorStyle.getVendorStylePIMAttribute().size());
				pimAttributeDao.saveVendoorStyle(vendorStyle);
			}
			if (!vsPimAttList.isEmpty() && isReterivePIMAttr) {
				log.info("Deleting vendorstyle pim attributes ,size: "
						+ vsPimAttList.size());
				pimAttributeDao.deletevspalist(vsPimAttList);
			}
		} catch (Exception e) {
			log
					.error("Exception while creating/updating the vendorStyle of PIMAttribute or vendorStylePIMAttribute "
							+ e.getMessage());
			// e.printStackTrace();
			throw e; // remove
		}
		return vendorStyle;
	}


	@Override
	public List<PIMAttribute> getAllPIMAttributesFromCars() {
		List<PIMAttribute> lPIMAttribute = null;
		lPIMAttribute=pimAttributeDao.getAllPIMAttributes();
		if(lPIMAttribute == null){
			return lPIMAttribute;
		}else {
			return new ArrayList<PIMAttribute>();
		}
	}

	@Override
	public Object save(Object ob) {
		return this.pimAttributeDao.save(ob);
	}

	@Override
	public List<PIMAttribute> getPIMAttributesFromCars(String name) {
		return pimAttributeDao.searchPIMAttributes(name);
	}

	/**
	 * this method fetch the sku details from smart web service
	 * @param list of skus
	 * @return map of <skuOrin, SkuDTO>
	 * @throws Exception
	 */
	public Map<String, SkuDTO> getSKUDetailsFromSMART(List<String> strSkuList) throws Exception {

		//return null if strSkuList list is empty
		if(strSkuList==null || strSkuList.size()<1){
			return null;
		}
		BelkProductServiceStub.GetItemResponse skuResponse = null;
		Map<String,SkuDTO> skuDtoMap = new HashMap<String,SkuDTO>();
		try {
			BelkProductServiceStub stub = new BelkProductServiceStub();
			//get the  webservice request object
			BelkProductServiceStub.GetItemRequest skuRequest = new  BelkProductServiceStub.GetItemRequest();
			SkuList_type0  skuTypeList = new SkuList_type0();
			//create List of skus to pass to webservice
			for(String strSkuOrin : strSkuList){
				Sku_type1 sku = new Sku_type1();
				sku.setSku_type1(strSkuOrin);
				skuTypeList.addSku(sku);
			}
			skuRequest.setSkuList(skuTypeList);

			//call webervice to get sku details
			skuResponse=stub.getItem(skuRequest);
			Item_Catalog_type0[] skuCatlogList = skuResponse.getItem_Catalog();

			for(Item_Catalog_type0 skuCatalog: skuCatlogList){
				SkuDTO skuDto = new SkuDTO();
				try {
					Item_Ctg_Spec_type0 skuCategorySpec = skuCatalog.getItem_Ctg_Spec();
					//set the SKU details
					skuDto.setOrin(skuCategorySpec.getId());
					skuDto.setBelkSku(skuCategorySpec.getIDB_Id());
					skuDto.setDescription(skuCategorySpec.getDescription().getLong());
                                        try {
					skuDto.setRetailPrice(Double.parseDouble(skuCategorySpec.getOriginal_Retail()));
                                        } catch (Exception e) {
                                            skuDto.setRetailPrice(0.0);
                                            log.warn("Received a non numeric retail price for sku orin: "+skuCategorySpec.getId()+" - defaulting to 0.0");
                                        }
					Supplier_type0[] supplierTypes = skuCategorySpec.getSupplier();
					Supplier_type0 supplier = null;
                    // Create the CAR only for primary supplier
					for (Supplier_type0 Supplier_type : supplierTypes) {
						if (Supplier_type.getPrimary_Flag()) {
							supplier = Supplier_type;
							break;
						}
					}
					// Create the CAR only for primary upc
					UPCs_type0[] upcTypes = supplier.getUPCs();
					UPCs_type0 upcType = null;
					for (UPCs_type0 upcs : upcTypes) {
						if (upcs.getPrimary_Flag()) {
							upcType = upcs;
							break;
						}
					}
					skuDto.setVendorUpc(upcType.getUPC());
					skuDto.setLongSku(skuDto.getVendorUpc());
					skuDto.setStyleNumber(supplier.getVPN());

					Item_SKU_Spec_type0 skuSPecType = skuCatalog.getItem_SKU_Spec();
					for(Differentiators_type0 diffrentiators :  skuSPecType.getDifferentiators()){
						if(Constants.SIZE.equalsIgnoreCase(diffrentiators.getType().toString())){
							skuDto.setSizeCode(diffrentiators.getCode());
							//System.out.println("Size Code--------->"+ diffrentiators.getCode());
							skuDto.setSizeName(diffrentiators.getVendor_Description());
						}else if(Constants.COLOR.equalsIgnoreCase(diffrentiators.getType().toString())){
							skuDto.setColorCode(diffrentiators.getCode());
						//	System.out.println("COLOR Code--------->"+ diffrentiators.getCode());
							skuDto.setColorName(diffrentiators.getVendor_Description());
						}
					}
					//Item_UDA_Spec_type0 skuUDAList = skuCatalog.getItem_UDA_Spec();
					//TODO get the isSet and parentUPC fields from SMART - current web service does not support this.
					//skuDtoList.add(skuDto);
					skuDtoMap.put(skuDto.getOrin(),skuDto);
				}catch(Exception e){
					//handled generic exception, if one style response is incorrect skip to next style form the list
					log.error("Exception while fetching data from webservice response ", e);
					throw e;
				}
			}

		} catch (FaultMsg e) {
			log.error("Receved unwanted reponse from webservice ", e);
			throw e;
		} catch (AxisFault e) {
			log.error("AxisFault while calling the webservice  ");
			log.error("fault reason: " + e.getReason());
			throw e;
		} catch (RemoteException e) {
			log.error("not able to connect to Webservice  ", e);
			throw e;
		}
		catch(Exception e){
			//handled generic exception, if one style response is incorrect skip to next style form the list
			log.error("Exception while fetching data from webservice response ", e);
			throw e;
		}
		return skuDtoMap;
	}



	@Override
	public List<vendorStylePIMAttributeDTO> getVendorStylePimAttribute(
			String VendorStyle) {

		List<vendorStylePIMAttributeDTO> lVSPIMAdto = new ArrayList<vendorStylePIMAttributeDTO>();
		return lVSPIMAdto;
	}

	@Override
	public PIMAttribute getPIMAttributeByID(String pimId) {
		return this.pimAttributeDao.getPIMAttributeByID(pimId);
	}


	/*
	 * This method will create an Native SQL to update the IS_PIM_ATTR_DISPLAYABLE column in PIM_ATTRIBUTE table.
	 * */
	@Override
	public int updatePIMAttributes(String ids,String flage,String user){

		ids=StringUtils.remove(ids, '[');
		ids=StringUtils.remove(ids, ']');
		StringBuffer query = new StringBuffer("UPDATE PIM_ATTRIBUTE SET IS_PIM_ATTR_DISPLAYABLE='").append(flage);
		query.append("' , DATE_DISPLAYABLE=sysdate , UPDATED_DATE= sysdate");
		query.append(" , UPDATED_BY='").append(user);
		query.append("' WHERE PIM_ATTR_ID IN (").append(ids).append(")");
		int updatecnt=pimAttributeDao.updatePIMAttributes(query.toString());

		return updatecnt;
	}


	public void setAuditInfo(User user, BaseAuditableModel model) {

		if (StringUtils.isBlank(model.getCreatedBy())) {
			model.setCreatedBy(user.getUsername());
			model.setCreatedDate(new Date());
		}
		model.setUpdatedBy(user.getUsername());
		model.setUpdatedDate(new Date());
	}


   @Override
    public void refreshCarPIMAttributes(String carId, User Systemuser) {

        log.info("--------> Begin refreshCarPIMAttributes() for the Car id :"+carId + "<--------");

        try {
            if (StringUtils.isNotBlank(carId)) {
                Car car = carManager.getCarFromId(new Long(carId));

                List<VendorStyle> stylelist = car.getVendorStyles();
                refreshPreCutOverStyles(stylelist,Systemuser);

            }

            log.info("---------------->  End refreshCar PIMAttributes <-----------------");

        } catch (Exception ex) {
            log.error("Error while refreshing the pimattribute for the car Id:"
                    + carId, ex);
        }
    }


    /**
     * Method containing the core logic to invoke existing getItem proxy for pre cut over vendor styles.
     *
     * @param Systemuser
     * @param stylelist
     * @throws Exception
     */
    private void refreshPreCutOverStyles(List<VendorStyle> stylelist, User Systemuser)
            throws Exception {
        List<String> vStyleOrinList = new ArrayList<String>();
        List<PackDTO> packList = new ArrayList<PackDTO>();

        Map<String, VendorStyle> vendorStyleMap = new HashMap<String, VendorStyle>();
        Map<String, VendorStyle> vendorStylePackMap = new HashMap<String, VendorStyle>();
        for (VendorStyle vStyle : stylelist) {
            PackDTO packDTO = new PackDTO();
            Long tmpOrin = vStyle.getOrinNumber();
            if (tmpOrin != null) {
                vStyleOrinList.add(String.valueOf(vStyle
                        .getOrinNumber()));

                vendorStyleMap.put(vStyle.getVendorStyleNumber(),
                        vStyle);
                log.info("Vendorstyle Orin Number====>"
                        + vStyle.getOrinNumber());
            } else {
                packDTO.setVendorNumber(vStyle.getVendorNumber());
                packDTO.setVendorStyleNumber(vStyle
                        .getVendorStyleNumber());
                vendorStylePackMap.put(vStyle.getVendorStyleNumber(),
                        vStyle);
                log.info("Vendorstyle Number====>"
                        + vStyle.getVendorStyleNumber());
                packList.add(packDTO);
            }
        }
        if (log.isDebugEnabled())
            log.debug("vStyleOrinList:" + vStyleOrinList.size()
                    + "packList:" + packList.size());
        if (!vStyleOrinList.isEmpty()) {
            Map<String, StyleDTO> styledtolist = getStylesDetailsFromSMART(vStyleOrinList);

            // Iterating the vendor style and calling the
            // syncPIMAttributes method for vendorstyle update.

            for (StyleDTO styledto : styledtolist.values()) {
                if (styledto.getVendorStyle() != null) {
                    String vendorStyleNumber = styledto
                            .getVendorStyle();
                    if (log.isDebugEnabled())
                        log
                                .debug("vendorStyle refresh : "
                                        + vendorStyleMap
                                                .containsKey(vendorStyleNumber));
                    if (vendorStyleMap.containsKey(vendorStyleNumber)) {
                        VendorStyle vStyle = vendorStyleMap
                                .get(vendorStyleNumber);
                        List<vendorStylePIMAttributeDTO> vsPimAttrlist = styledto
                                .getPimAttributeDTOList();
                        vStyle = syncPIMAttributes(vStyle,
                                vsPimAttrlist, Systemuser, true);
                    }
                }
            }
        }

        if (!packList.isEmpty()) {
            List<StyleDTO> styledtolist = getPackDetailsFromSMART(packList);
            for (StyleDTO styledto : styledtolist) {
                if (styledto.getVendorStyle() != null) {
                    String vendorStyleNumber = styledto
                            .getVendorStyle();
                    log.info("Pack vendor Style refresh "
                            + vendorStylePackMap
                                    .containsKey(vendorStyleNumber));
                    if (vendorStylePackMap
                            .containsKey(vendorStyleNumber)) {
                        VendorStyle vStyle = vendorStylePackMap
                                .get(vendorStyleNumber);
                        log.info("vspimattlist Size from style dto ==>"
                                + styledto.getPimAttributeDTOList()
                                        .size());
                        List<vendorStylePIMAttributeDTO> vsPimAttrlist = styledto
                                .getPimAttributeDTOList();
                        vStyle = syncPIMAttributes(vStyle,
                                vsPimAttrlist, Systemuser, true);
                    }
                }
            }
        }
    }

    /**
     * New method added to refresh the Car PIM Attributes for post cutover cars.
     */
	@Override
	public void refreshAdditionalCarPIMAttributes(String carId, User Systemuser) {

		log.info("--------> Begin refreshAdditionalCarPIMAttributes() for the Car id :"+carId + "<--------");
		Car car=null;

		try {
			if (StringUtils.isNotBlank(carId)) {
				car = carManager.getCarFromId(new Long(carId));

				List<VendorStyle> stylelist = car.getVendorStyles();
				refreshPostCutOverStyles(car, stylelist, Systemuser);

			}

			log.info("---------------->  End refreshAdditionalCar PIMAttributes <-----------------");

		} catch (Exception ex) {
			log.error("Error while refreshing the pimattribute for the car Id:"
					+ carId, ex);
			if(!isFailureNotesExist(car)){
			    createUpdateFailureNotes(car,Systemuser,true,Status.ACTIVE);
			}
		}
	}


    /**
     * Method containing the core logic to invoke new getItem for post cutover vendor styles.
     *
     * @param Systemuser
     * @param car
     * @param stylelist
     * @throws Exception
     */
    private void refreshPostCutOverStyles(Car car,
            List<VendorStyle> stylelist,User Systemuser) throws Exception {
        List<String> vStyleOrinList = new ArrayList<String>();
        List<String> vSkuOrinList = new ArrayList<String>();
        List<PackDTO> packList = new ArrayList<PackDTO>();

        Map<String, VendorStyle> vendorStyleMap = new HashMap<String, VendorStyle>();
        Map<String, VendorStyle> vendorStylePackMap = new HashMap<String, VendorStyle>();
        for (VendorStyle vStyle : stylelist) {
        	PackDTO packDTO = new PackDTO();
        	Long tmpOrin = vStyle.getOrinNumber();
        	if (tmpOrin != null) {
        		vStyleOrinList.add(String.valueOf(vStyle
        				.getOrinNumber()));

        		vendorStyleMap.put(vStyle.getVendorStyleNumber(),
        				vStyle);
        		log.info("Vendorstyle Orin Number====>"
        				+ vStyle.getOrinNumber());
        	} else {
        		packDTO.setVendorNumber(vStyle.getVendorNumber());
        		packDTO.setVendorStyleNumber(vStyle
        				.getVendorStyleNumber());
        		vendorStylePackMap.put(vStyle.getVendorStyleNumber(),
        				vStyle);
        		log.info("Vendorstyle Number====>"
        				+ vStyle.getVendorStyleNumber());
        		packList.add(packDTO);
        	}
        }
        Set<VendorSku> skuList = car.getVendorSkus();
        for(VendorSku vSku : skuList){
            Long tempSkuOrin = vSku.getOrinNumber();
            if (tempSkuOrin != null) {
                vSkuOrinList.add(String.valueOf(tempSkuOrin));

                log.info("Vendorsku Orin Number====>"+ tempSkuOrin);
            }
        }
        if (log.isDebugEnabled() && vSkuOrinList!=null && vStyleOrinList!=null && packList!=null){
        	log.debug("vStyleOrinList:" + vStyleOrinList.size()
        			+ "packList:" + packList.size()+" vSkuOrinList: "+vSkuOrinList.size());
        }

        if (!vStyleOrinList.isEmpty()) {
            Map<Long ,List<AttributeData>> stylePimMap = getAdditionalStylesDetailsFromSMART(car,vStyleOrinList,Constants.PROCESS_RETRIEVE_ITEM);
            syncAdditionalPIMAttributes(car,stylePimMap,"style",Systemuser);
        }

        if (!packList.isEmpty()) {
            Map<Long, List<AttributeData>> stylePimMap = getAdditionalPackDetailsFromSMART(car,packList,Constants.PROCESS_RETRIEVE_ITEM);
            syncAdditionalPIMAttributes(car,stylePimMap,"pack",Systemuser);
        }
        boolean isPack=false;
        Map<Long,List<AttributeData>> styleColorMap = new HashMap<Long,List<AttributeData>>();
        //Invoking Image refresh
        Map<String,IntegrationResponseData>getImageResponse = pimFtpImageManager.uploadOrDeletePimImagesByCar(car);
        if(getImageResponse!=null){
            IntegrationResponseData response = getImageResponse.get("SUCCESS");
            if(response!=null && response.getResponseList() !=null){
        		for (ItemCatalogData itemCatalogData : response.getResponseList()) {
        			// TODO pack color attribute save , to proceed not
        			// processing the pack color attribute save
        			if (itemCatalogData.getResponseType().equals(Constants.PACK_COLOR)) {
        				isPack = true;
                         break;
        			}

        		}
        		styleColorMap = processGetImageResponse(response,Systemuser,car,Constants.PROCESS_RETRIEVE_ITEM);
            }else{
                if(log.isInfoEnabled()){
                    log.info("Get Image response is empty for SUCCESS event from Refresh car flow ");
                }
            }
        }

        if(vSkuOrinList!=null && vSkuOrinList.size()>0){
        	if(!isPack) {
                Map<Long,List<AttributeData>> skuPimMap = getAdditionalSkuDetailsFromSMART(car,vSkuOrinList,Systemuser);
                //merging style color and sku maps
                skuPimMap = mergeStyleColorMapWithSkuMap(skuPimMap,styleColorMap);
                syncAdditionalPIMAttributes(car,skuPimMap,"sku",Systemuser);
        	}
        }

        //Inactivate the failure notes for car if it already exists
        if(isFailureNotesExist(car)){
            createUpdateFailureNotes(car,Systemuser,false,Status.INACTIVE);
        }
    }

	private Map<Long, List<AttributeData>> mergeStyleColorMapWithSkuMap(
            Map<Long, List<AttributeData>> skuPimMap,
            Map<Long, List<AttributeData>> styleColorMap) {
	    Map<Long, List<AttributeData>>mergedMap = new HashMap<Long, List<AttributeData>>();
        // TODO Auto-generated method stub
        if(skuPimMap!=null && skuPimMap.size()>0){
            if(styleColorMap!=null && styleColorMap.size()>0){
                for(Map.Entry<Long,List<AttributeData>> mapEntry : skuPimMap.entrySet()){
                    Long skuOrin = mapEntry.getKey();
                    List<AttributeData> skuAttrList = mapEntry.getValue();
                    log.info("size of sku attr list before merge "+skuAttrList.size());
                    
                    if(styleColorMap.containsKey(skuOrin)){
                        List<AttributeData> styleColorAttrList = styleColorMap.get(skuOrin);
                        log.info("size of style color attr list "+styleColorAttrList.size());
                        if(skuAttrList!=null && styleColorAttrList!=null){
                            skuAttrList.addAll(styleColorAttrList);
                        }
                    }
                    log.info("size of sku attr list after merge "+skuAttrList.size());
                    mergedMap.put(skuOrin, skuAttrList);
                }
            }else{
                mergedMap.putAll(skuPimMap);
            }
        }else if(styleColorMap!=null && styleColorMap.size()>0){
            mergedMap.putAll(styleColorMap);
        }
        
        return mergedMap;
    }


    /**
	 * New method added to refresh the Car PIM Attributes for pattern cars.
	 * This will handle both pre & post pattern cut over cars.
	 *
	 * @param carId
	 * @param Systemuser
	 */
	public void refreshCarPIMAttributesForPattern(String carId, User Systemuser) {
	    if(log.isInfoEnabled()){
	        log.info("--------> Begin refreshCarPIMAttributesForPattern() for the Car id :"+carId + "<--------");
	    }

        Car car=null;

        try {
            if (StringUtils.isNotBlank(carId)) {
                car = carManager.getCarFromId(new Long(carId));

                // First update group attributes
                String type = car.getVendorStyle().getVendorStyleType().getCode();
                if (VendorStyleType.PATTERN_CONS_VS.equals(type)) {
                    refreshCarPIMAttributesForPattern(car, null, Systemuser);
                }
                else {
                    VendorStyle defaultChild = null;
                    if (VendorStyleType.OUTFIT.equals(type)) {
                        // For outfits, just pick one of the children.
                        CarOutfitChild carOutfitChild = car.getCarOutfitChild().iterator().next();
                        if (carOutfitChild!=null) {
                            defaultChild = carOutfitChild.getChildCar().getVendorStyle();
                        }
                    }
                    else {
                        // For Patterns, get default child if exists.
                        defaultChild = getDefaultChildOfPatternCar(car);
                    }

                    if (defaultChild!=null) {
                        refreshCarPIMAttributesForPattern(car, defaultChild, Systemuser);
                    }
                }

                // Then update children component attributes, if any.
                refreshCarPIMAttributesForPatternChildren(car, Systemuser);
            }
            if(log.isInfoEnabled()){
                log.info("---------------->  End refreshCarPIMAttributesForPattern <-----------------");
            }

        }catch (Exception ex) {
            log.error("Error while refreshing the pimattribute for the car Id:"
                    + carId, ex);
            if(!isFailureNotesExist(car)){
                createUpdateFailureNotes(car,Systemuser,true,Status.ACTIVE);
            }
        }
	}

	/**
     * New method added to refresh the Car PIM Attributes for pattern cars.
     * This will handle both pre & post pattern cutover cars.
     *
     * @param car
     * @throws Exception
     */
	@Override
    public void refreshCarPIMAttributesForPattern(Car car, VendorStyle defaultChild, User Systemuser) throws Exception {
         // when defaultChild is null, this is a "PATTERN-CONS-VS" type pattern.
         if (defaultChild==null) {
            try {
                // update images via getItemRequest.
                pimFtpImageManager.uploadOrDeletePimImagesByCar(car);
            }
            catch (Exception e) {
                if(log.isErrorEnabled()){
                    log.error("Error while refreshingCarPIMAttributesForPattern!", e);
                }
            }

            try {
                boolean isConvertedGrouping = false;
                if (car.getVendorStyle().getGroupId() == null) {
                    isConvertedGrouping = true;
                }
                ArrayList<String> groupIds = new ArrayList<String>();
                if (isConvertedGrouping) {
                    // convertedGroupings do not have group_id. Use
                    // vendor_number/vendor_style_number instead.
                    groupIds.add(
                            car.getVendorStyle().getVendorNumber() + "," + car.getVendorStyle().getVendorStyleNumber());
                } else {
                    groupIds.add(car.getVendorStyle().getGroupId().toString());
                }

                // update attributes via getGroupRequest.
                Map<Long, List<AttributeData>> attributeMap =
                        getAdditionalGroupDetailsFromSMART(groupIds,isConvertedGrouping);
                syncAdditionalPIMAttributesForGroup(car,attributeMap,Constants.GROUPING,Systemuser);
            }
            catch (Exception e) {
                if(log.isErrorEnabled()){
                    log.error("Error while refreshingCarPIMAttributesForPattern!", e);
                }
            }
	    }
	    else {
	        try {
	            // update images via getGroupRequest
	            pimFtpImageManager.uploadOrDeletePimImagesByCarForGroups(car);
	        }
	        catch (Exception e) {
	            if(log.isErrorEnabled()){
                    log.error("Error while refreshingCarPIMAttributesForPattern!", e);
                }
	        }

	        try {
                ArrayList<String> ids = new ArrayList<String>();
	            if (VendorStyleType.OUTFIT.equals(car.getVendorStyle().getVendorStyleType().getCode())) {
	                VendorStyle patternVS = car.getVendorStyle();
	                // update attributes via getGroupRequest also
	                boolean isConvertedGrouping = false;
                    if (patternVS.getGroupId()==null) {
                        isConvertedGrouping = true; // default child is a converted grouping
                    }
                    
                    if (isConvertedGrouping) {
                        // convertedGroupings do not have group_id. Use
                        // vendor_number/vendor_style_number instead.
                        ids.add(patternVS.getVendorNumber() + "," + patternVS.getVendorStyleNumber());
                    }
                    else {
                        ids.add(patternVS.getGroupId().toString());
                    }
                    
                    Map<String, String> bcgAttrMap = 
                            getAdditionalBCGGroupDetailsFromSMART(ids, isConvertedGrouping);
                    syncAdditionalPIMAttributesForBCGGroup(car,bcgAttrMap,Systemuser);
	            }
	            else {
	                // update attributes via getItemRequest from default child
	                if (defaultChild.getOrinNumber()!=null) {
	                    // default child is a style
	                    ids.add(defaultChild.getOrinNumber().toString());
	                    Map<Long, List<AttributeData>> attributeMap = 
	                            getAdditionalStylesDetailsFromSMART(car, ids,Constants.PROCESS_CREATE);
	                    syncAdditionalPIMAttributesForGroup(car,attributeMap,Constants.STYLE,Systemuser);
	                }
	                else if (VendorStyleType.PATTERN_CONS_VS.equalsIgnoreCase(defaultChild.getVendorStyleType().getCode())) {
	                    boolean isConvertedGrouping = false;
	                    if (defaultChild.getGroupId()==null) {
	                        isConvertedGrouping = true; // default child is a converted grouping
	                    }
	                    
	                    if (isConvertedGrouping) {
	                        // convertedGroupings do not have group_id. Use
	                        // vendor_number/vendor_style_number instead.
	                        ids.add(defaultChild.getVendorNumber() + "," + defaultChild.getVendorStyleNumber());
	                    }
	                    else {
	                        ids.add(defaultChild.getGroupId().toString());
	                    }
	                    
	                    Map<Long, List<AttributeData>> attributeMap = 
	                            getAdditionalGroupDetailsFromSMART(ids, isConvertedGrouping);
	                    syncAdditionalPIMAttributesForGroup(car,attributeMap,Constants.GROUPING,Systemuser);
	                }
	                else {
	                    throw new Exception ("Default Child of Beauty Collection is invalid type for fetching attributes!");
	                }
	            }
	        }
	        catch (Exception e) {
	            if(log.isErrorEnabled()){
                    log.error("Error while refreshingCarPIMAttributesForPattern!", e);
                }
	        }
	    }

        // Inactivate the failure notes for car if it already exists
        if (isFailureNotesExist(car)) {
            createUpdateFailureNotes(car, Systemuser, false, Status.INACTIVE);
        }
    }

	/**
	 * New method added to refresh the Car PIM Attributes for pattern cars.
	 * This will handle both pre & post pattern cutover cars.
	 *
	 * @param car
	 * @throws Exception
	 */
    @Override
	public void refreshCarPIMAttributesForPatternChildren(Car car, User Systemuser) throws Exception {
        List<VendorStyle> preCutOverStyleList = new ArrayList<VendorStyle>();
        List<VendorStyle> postCutOverStyleList = new ArrayList<VendorStyle>();

        String type = car.getVendorStyle().getVendorStyleType().getCode();
        if (VendorStyleType.OUTFIT.equals(type)) {
            Set<CarOutfitChild> carOutfitChildren = car.getCarOutfitChild();
            for(CarOutfitChild carOutfitChild : carOutfitChildren){
                List<VendorStyle> childrenVS = carOutfitChild.getChildCar().getVendorStyles();
                for (VendorStyle childVS : childrenVS) {
                    if(childVS!=null && carManager.isPostCutoverVendorStyle(childVS)){
                        if (!postCutOverStyleList.contains(childVS)) {
                            postCutOverStyleList.add(childVS);
                        }
                    }else{
                        if (!preCutOverStyleList.contains(childVS)) {
                            preCutOverStyleList.add(childVS);
                        }
                    }
                }
            }
        }
        else {
            List<VendorStyle> stylelist = car.getVendorStyles();
            for(VendorStyle vs : stylelist){
                if(vs!=null && carManager.isPostCutoverVendorStyle(vs)){
                    postCutOverStyleList.add(vs);
                }else{
                    preCutOverStyleList.add(vs);
                }
            }
        }

        if(preCutOverStyleList!=null && !preCutOverStyleList.isEmpty()){
            if(log.isDebugEnabled()){
                log.debug("preCutOverStyleList size inside refreshCarPIMAttributesForPattern  "+preCutOverStyleList.size());
            }
            try{
                refreshPreCutOverStyles(preCutOverStyleList,Systemuser);
            }catch(Exception e){
                log.error("Error while calling getItem proxy in refreshCarPIMAttributesForPattern for carId "
                        + car.getCarId(), e);
            }

        }

        if(postCutOverStyleList!=null && !postCutOverStyleList.isEmpty()){
            if(log.isDebugEnabled()){
                log.debug("postCutOverStyleList size inside refreshCarPIMAttributesForPattern  "+postCutOverStyleList.size());
            }
            refreshPostCutOverStyles(car,postCutOverStyleList,Systemuser);
        }

        // Inactivate the failure notes for car if it already exists
        if (isFailureNotesExist(car)) {
            createUpdateFailureNotes(car, Systemuser, false, Status.INACTIVE);
        }
	}


	/**
	 * Method to save failure notes whenever there is an error while retrieving
	 * additional attributes from PIM using new getItem service.
	 *
	 * @param car
	 * @param systemuser
	 * @param flagYes
	 */
	private void createUpdateFailureNotes(Car car, User systemuser, boolean displayFlag,String status) {

	    if(car!=null){
	        if(status!=null && status.equalsIgnoreCase(Status.INACTIVE)){
	            if(car.getCarNotes()!=null && !car.getCarNotes().isEmpty()){
	                for(CarNote note : car.getCarNotes()){
	                    if(note.getNoteType().getNoteTypeCd().equals(NoteType.ITEM_FAILURE_NOTES)){
	                        note.setStatusCd(status);
	                        note.setIsExternallyDisplayable("N");
	                        carManager.removeCarNote(car, note);
	                    }
	                }
	            }
	        }else {
	            carManager.addCarNote(car, displayFlag, Constants.GET_ITEM_FAILURE_TEXT, NoteType.ITEM_FAILURE_NOTES, systemuser.getUsername());
	        }

	    }
    }

	/**
	 * Method to check if there is a active failure note already existing for a given car.
	 *
	 * @param car
	 * @return
	 */
	private boolean isFailureNotesExist(Car car){
	    boolean fNoteExists = false;
	    List<CarNote> fNotes = carManagementDao.getDisplayableFailureNotes(car, NoteType.ITEM_FAILURE_NOTES);
	    if(fNotes!=null && fNotes.size()>0){
	        fNoteExists = true;
	    }
	    return fNoteExists;
	}


    /**
	 * This method get the style and sku details from PIM by calling webservice
	 * @return styleDTO
	 * @throws Exception
	 */
	@Override
	public StyleDTO getManualCarStyleFromSMART(String strVendorNumber,
			String strVendorStyleNumber, String strColorCode, String strSizecode) throws Exception {

		BelkProductServiceStub.GetItemResponse skuResponse = null;
		// return null if vendor or style is null
		if (strVendorNumber == null || strVendorStyleNumber == null) {
			return null;
		}
		StyleDTO styleDto = null;
		List<SkuDTO> skuDtoList = new ArrayList<SkuDTO>();
		String strStyleOrin = null;
		StringBuilder sbLogger = new StringBuilder();
		sbLogger.append("calling webservice to get SKu details from SMART for")
				.append("Vendor Number ").append(strVendorNumber).append(
						"style number :").append(strVendorStyleNumber).append(
						"color code :").append(strColorCode).append(
						"Size code :").append(strSizecode);
		log.info(sbLogger.toString());

		try {
			BelkProductServiceStub stub = new BelkProductServiceStub();
			// get the vendor webservice request object
			BelkProductServiceStub.GetItemRequest styleRequest = new BelkProductServiceStub.GetItemRequest();

			VendorStyleList_type0 vendorStyleList = new VendorStyleList_type0();
			VendorStyle_type0 vendorStyle = new VendorStyle_type0();
			// set vendor number to request
			VendorNumber_type0 vendorNumber = new VendorNumber_type0();
			vendorNumber.setVendorNumber_type0(strVendorNumber);
			vendorStyle.setVendorNumber(vendorNumber);
			// set style number to request
			VendorProductNumber_type0 vendorStyleNumber = new VendorProductNumber_type0();
			vendorStyleNumber
					.setVendorProductNumber_type0(strVendorStyleNumber);
			vendorStyle.setVendorProductNumber(vendorStyleNumber);
			// set the color code to request if color code is not empty.
			if (strColorCode != null && !strColorCode.isEmpty()) {
				log.info("setting the color code :" + strColorCode + ":");
				ColorCode_type0 colorCode = new ColorCode_type0();
				colorCode.setColorCode_type0(strColorCode);
				vendorStyle.setColorCode(colorCode);
			}
			// add size to request if size code is not empty
			if (strSizecode != null && !strSizecode.isEmpty()) {
				log.info("setting the size code :" + strSizecode + ":");
				SizeCode_type0 sizeCode = new SizeCode_type0();
				sizeCode.setSizeCode_type0(strSizecode);
				vendorStyle.setSizeCode(sizeCode);
			}
			vendorStyleList.addVendorStyle(vendorStyle);
			styleRequest.setVendorStyleList(vendorStyleList);

			// call webervice to get style details
			skuResponse = stub.getItem(styleRequest);

			Item_Catalog_type0[] skuCatlogList = skuResponse.getItem_Catalog();

			for (Item_Catalog_type0 catalog : skuCatlogList) {

				if (Constants.ELEMENT_SKU.equalsIgnoreCase(catalog.getType().toString())) {
					// if it is the SKU details catalog
					SkuDTO skuDto = new SkuDTO();
					try {
						Item_Ctg_Spec_type0 skuCategorySpec = catalog
								.getItem_Ctg_Spec();
						// set the SKU details
						skuDto.setOrin(skuCategorySpec.getId());
						skuDto.setBelkSku(skuCategorySpec.getIDB_Id());
						skuDto.setDescription(skuCategorySpec.getDescription()
								.getLong());
						skuDto.setRetailPrice(Double
								.parseDouble(skuCategorySpec
										.getOriginal_Retail()));
						Supplier_type0[] supplierTypes = skuCategorySpec
								.getSupplier();
						Supplier_type0 supplier = null;
						// Create the CAR only for primary supplier
						for (Supplier_type0 Supplier_type : supplierTypes) {
							if (Supplier_type.getPrimary_Flag()) {
								supplier = Supplier_type;
								break;
							}
						}
						// Create the CAR only for primary upc
						UPCs_type0[] upcTypes = supplier.getUPCs();
						UPCs_type0 upcType = null;
						for (UPCs_type0 upcs : upcTypes) {
							if (upcs.getPrimary_Flag()) {
								upcType = upcs;
								break;
							}
						}
						skuDto.setVendorUpc(upcType.getUPC());
						skuDto.setLongSku(skuDto.getVendorUpc());
						skuDto.setStyleNumber(supplier.getVPN());
						// Manually requested skus are not part of any set and
						// considered as regular skus
						skuDto.setParentBelkSKU(null);
						skuDto.setSetFlag(Constants.FLAG_NO);

						Item_SKU_Spec_type0 skuSPecType = catalog
								.getItem_SKU_Spec();
						for (Differentiators_type0 diffrentiators : skuSPecType
								.getDifferentiators()) {
							if (Constants.SIZE.equalsIgnoreCase(diffrentiators
									.getType().toString())) {
								skuDto.setSizeCode(diffrentiators.getCode());
								skuDto.setSizeName(diffrentiators
										.getVendor_Description());
							} else if (Constants.COLOR.equalsIgnoreCase(diffrentiators
									.getType().toString())) {
								skuDto.setColorCode(diffrentiators.getCode());
								skuDto.setColorName(diffrentiators
										.getVendor_Description());
							}
						}
						if (log.isDebugEnabled()) {
							log.debug("Found details for sku: "
									+ skuDto.getBelkSku());
						}
						if (strStyleOrin == null || strStyleOrin.isEmpty()) {
							Item_SKU_Spec_type0 itemSkuSpec = catalog
									.getItem_SKU_Spec();
							strStyleOrin = itemSkuSpec.getStyle_Id();
							if (log.isDebugEnabled()) {
								log.debug("setting ORIN for style: "
										+ strStyleOrin);
							}
						}
						skuDtoList.add(skuDto);
					} catch (Exception e) {
						log.error("Exception while fetching 1 SKU data from manual car webservice response ",e);
					}
				}
			}

			// call the getStyle webservice to get the STyleDto
			if (strStyleOrin == null || strStyleOrin.isEmpty()) {
				log.error("StyleOrin number for sku found null in SMART while creating manual car");
				// throw exception if styleOrin number is null
				//throw new AxisFault("StyleOrin is empty");
			} else {
				if (log.isDebugEnabled()) {
					log.debug("calling the webservice to get the style details for manual cars: "
									+ strStyleOrin);
				}
				Map<String, StyleDTO> styleMap = null;
				styleMap = getStylesDetailsFromSMART(Arrays
						.asList(strStyleOrin));
				styleDto = styleMap.get(strStyleOrin);
			}

			// set the SKU list in StyleDto
			if (styleDto != null) {
				styleDto.setSkuInfo(skuDtoList);
				log.info("Found the requested manual car style with "
						+ skuDtoList.size() + " skus in it");
			}

		} catch (FaultMsg e) {
			log.error("Received unwanted reponse from webservice ", e);
			throw e;
			// e.printStackTrace();
		} catch (AxisFault e) {
			log.error("AxisFault while calling the webservice  ");
			log.error("fault reason: " + e.getReason());
			throw e;
			// e.printStackTrace();
		} catch (RemoteException e) {
			log.error("not able to connect to Webservice  ", e);
			// e.printStackTrace();
			throw e;
		}
		catch(Exception e){
			log.error("Exception while fetching data from webservice response " +e.getMessage());
			throw e;
		}
		return styleDto;

	}

	public List<StyleDTO> getPackDetailsFromSMART(List<PackDTO> packDtoList) throws Exception {

		BelkProductServiceStub.GetPackResponse packResponse = null;
		// return null if vendor or style is null
		if (packDtoList.isEmpty()) {
			return null;
		}
		List<StyleDTO> styleDTOList = new ArrayList<StyleDTO>();
		// String strStyleOrin = null;

		try {
			BelkProductServiceStub stub = new BelkProductServiceStub();
			// get the vendor webservice request object
			BelkProductServiceStub.GetPackRequest packRequest = new BelkProductServiceStub.GetPackRequest();
			VendorStyleList_type0 vendorStyleList = new VendorStyleList_type0();
			for (PackDTO packDTO : packDtoList) {
				StringBuilder sbLogger = new StringBuilder();
				sbLogger.append(
						"calling webservice to get SKu details from SMART for")
						.append("Vendor Number ").append(
								packDTO.getVendorNumber()).append(
								"style number :").append(
								packDTO.getVendorStyleNumber()).append(
								"color code :").append(
								packDTO.getColorDescription()).append(
								"Size code :").append(
								packDTO.getSizeDescription());
				log.info(sbLogger.toString());
				if (!StringUtils.isNotBlank(packDTO.getVendorNumber())
						&& !StringUtils.isNotBlank(packDTO
								.getVendorStyleNumber())) {
					continue;
				}

				VendorStyle_type0 vendorStyle = new VendorStyle_type0();
				// set vendor number to request
				VendorNumber_type0 vendorNumber = new VendorNumber_type0();
				vendorNumber.setVendorNumber_type0(packDTO.getVendorNumber());
				vendorStyle.setVendorNumber(vendorNumber);
				// set style number to request
				VendorProductNumber_type0 vendorStyleNumber = new VendorProductNumber_type0();
				vendorStyleNumber.setVendorProductNumber_type0(packDTO
						.getVendorStyleNumber());
				vendorStyle.setVendorProductNumber(vendorStyleNumber);
				// set the color code to request if color code is not empty.
				String strColorCode = packDTO.getColorDescription();
				if (strColorCode != null && !strColorCode.isEmpty()) {
					log.info("setting the color code :"
							+ packDTO.getColorDescription() + ":");
					ColorCode_type0 colorCode = new ColorCode_type0();
					colorCode.setColorCode_type0(packDTO.getColorDescription());
					vendorStyle.setColorCode(colorCode);
				}
				// add size to request if size code is not empty
				String strSizecode = packDTO.getSizeDescription();
				if (strSizecode != null && !strSizecode.isEmpty()) {
					log.info("setting the size code :"
							+ packDTO.getSizeDescription() + ":");
					SizeCode_type0 sizeCode = new SizeCode_type0();
					sizeCode.setSizeCode_type0(packDTO.getSizeDescription());
					vendorStyle.setSizeCode(sizeCode);
				}
				vendorStyleList.addVendorStyle(vendorStyle);

			}
			packRequest.setVendorStyleList(vendorStyleList);
			// call webervice to get style details
			if (log.isDebugEnabled())
				log.debug("Before calling web service");
			packResponse = stub.getPack(packRequest);

			if (log.isDebugEnabled())
				log.debug("packCatlogList size"
						+ packResponse.getItem_Catalog().length);
			Item_Catalog_type0[] packCatlogList = packResponse
					.getItem_Catalog();

			for (Item_Catalog_type0 catalog : packCatlogList) {
				List<SkuDTO> skuDtoList = new ArrayList<SkuDTO>();

				SkuDTO skuDto = new SkuDTO();
				StyleDTO styleDTO = new StyleDTO();
				SecondaryCategory_type0[] secondCatagoryList = catalog
						.getSecondaryCategory();
				SecondaryCategory_type0 secondCatagory = secondCatagoryList[0];
				Level_type1[] levels = secondCatagory.getLevels().getLevel();
				for (Level_type1 level : levels) {
					if (Constants.DEPARTMENT.equals(level.getName())) {
						// we only set the department number, we do not receive
						// dept name from SMART
						styleDTO.setDepartmentNumber(level.toString());
					} else if (Constants.CLASS.equals(level.getName())) {
						// we only set the Class number, we do not receive class
						// name from SMART
						styleDTO.setClassNumber(level.toString());
					}
				}

				if (Constants.COMPLEX_PACK.equalsIgnoreCase(catalog.getType()
						.toString())) {
					// if it is the SKU details catalog

					try {
						Item_Ctg_Spec_type0 packCategorySpec = catalog
								.getItem_Ctg_Spec();
						styleDTO.setVendorStyleDescription(packCategorySpec
								.getDescription().getLong());
						if(log.isInfoEnabled()) {
							log.info("Processing complex pack with ID: "+packCategorySpec.getId());
						}
						styleDTO.setPackId(packCategorySpec.getId());
						skuDto.setOrin(packCategorySpec.getId());

                                                Double origRetailPrice = 0.0;

                                                try {
                                                    origRetailPrice = Double.parseDouble(packCategorySpec.getOriginal_Retail());
                                                } catch (Exception e) {
                                                    log.warn("Recieved no original price from PIM for sku orin: " + packCategorySpec.getId() + " and will be defaulted to 0.0");
                                                }

						if (log.isDebugEnabled())
							log.debug("Belk Sku "
									+ packCategorySpec.getIDB_Id()
									+ " Description: "
									+ packCategorySpec.getDescription()
											.getLong()
									+ " Retail Price: "
									+ origRetailPrice);
						skuDto.setBelkSku(packCategorySpec.getIDB_Id());

						skuDto.setDescription(packCategorySpec.getDescription().getLong());
						skuDto.setRetailPrice(origRetailPrice);
						Supplier_type0[] supplierTypes = packCategorySpec
								.getSupplier();
							Supplier_type0 supplier = null;
		                    // Create the CAR only for primary supplier
							for (Supplier_type0 Supplier_type : supplierTypes) {
								if (Supplier_type.getPrimary_Flag()) {
									supplier = Supplier_type;
									break;
								}
							}
							// Create the CAR only for primary upc
							UPCs_type0[] upcTypes = supplier.getUPCs();
							UPCs_type0 upcType = null;
							/** START SUP-683 **/
							String strSupplierDiscontinueDate = supplier.getDiscontinue_Date();
							if(upcTypes==null && strSupplierDiscontinueDate!=null && !strSupplierDiscontinueDate.isEmpty()) {
								Date discontinueDate = DateUtils.parseDate(strSupplierDiscontinueDate, "MM/dd/yyyy");
								Date currentDate = new Date();
								if(discontinueDate.before(currentDate)) {
									if(log.isInfoEnabled()) {
										log.info("UPC info not found in discontinued complex pack with ID: "+packCategorySpec.getId()
												+", discontinued on "+discontinueDate+", skipping complex pack");
									}
									continue;
								}
							}
							/** END SUP-683 **/
							for (UPCs_type0 upcs : upcTypes) {
								if (upcs.getPrimary_Flag()) {
									upcType = upcs;
									break;
								}
							}

						styleDTO.setVendorNumber(supplier.getId());
						if (StringUtils.isNotBlank(supplier.getId()))
							styleDTO
									.setVendorName(getVendorNameFromSMART(supplier
											.getId()));
						if (log.isDebugEnabled())
							log.debug("Vendor Upc" + upcType.getUPC()
									+ "Belk SKU" + skuDto.getBelkSku()
									+ "Style No" + supplier.getVPN());
						skuDto.setVendorUpc(upcType.getUPC());
						skuDto.setLongSku(skuDto.getVendorUpc());
						skuDto.setLongSku(skuDto.getVendorUpc());
						skuDto.setStyleNumber(supplier.getVPN());
						styleDTO.setVendorStyle(supplier.getVPN());
						styleDTO.setPackUPC(skuDto.getBelkSku());
						// Manually requested skus are not part of any set and
						// considered as regular skus
						skuDto.setParentBelkSKU(null);
						skuDto.setSetFlag(Constants.FLAG_YES);
						PackType packSpecType = catalog
								.getItem_Complex_Pack_Spec();
						// Item_SKU_Spec_type0 skuSPecType =
						// catalog.getItem_SKU_Spec();
						styleDTO.setSellable(packSpecType.getSellable_Flag());
						for (Differentiators_type2 diffrentiators : packSpecType
								.getDifferentiators()) {
							if (Constants.SIZE.equalsIgnoreCase(diffrentiators
									.getType().toString())) {
								skuDto.setSizeCode(diffrentiators.getCode());
								skuDto.setSizeName(diffrentiators
										.getVendor_Description());
							} else if (Constants.COLOR.equalsIgnoreCase(diffrentiators
									.getType().toString())) {
								skuDto.setColorCode(diffrentiators.getCode());
								skuDto.setColorName(diffrentiators
										.getVendor_Description());
							}
						}
						if (log.isDebugEnabled()) {
							log.debug("Found details for sku: "
									+ skuDto.getBelkSku());
						}
                        //Logic for populating UDA attributes will be moved to new getItem.
					    // Setting empty list to avoid NPE.
						List<vendorStylePIMAttributeDTO> pimAttrDTOList = new ArrayList<vendorStylePIMAttributeDTO>();
						styleDTO.setPimAttributeDTOList(pimAttrDTOList);

						skuDtoList.add(skuDto);
						styleDTO.setSkuInfo(skuDtoList);
					} catch (Exception e) {
						log.error("Exception while fetching 1 SKU data from manual car webservice response ",e);
						throw e;
					}
				}
				styleDTOList.add(styleDTO);
			}

		} catch (FaultMsg e) {
			log.error("Received unwanted reponse from webservice ", e);
			throw e;
		} catch (AxisFault e) {
			log.error("AxisFault while calling the webservice  ");
			log.error("fault reason: " + e.getReason());
			throw e;
		} catch (RemoteException e) {
			log.error("not able to connect to Webservice  ", e);
			throw e;
		}
		return styleDTOList;

	}


	public String getVendorNameFromSMART(String vendorNumber) throws Exception
	{
		String vendorName = null;
		String URL = "";
		StringBuffer sbFullURL = new StringBuffer();

		Properties properties = PropertyLoader.loadProperties("jms.properties");
		URL = properties.getProperty("VendorNameWSURL");

		sbFullURL.append(URL);
		sbFullURL.append("legacyid=");
		sbFullURL.append(vendorNumber);
		sbFullURL
				.append("&responsefields=Supplier_Ctg_Spec.Name&type=supplier");

		if (log.isDebugEnabled())
			log.debug("Vendor NAme webserviceequest complete URL: "
					+ sbFullURL.toString());

		VendorNameService vendorNameService = new VendorNameService();

		vendorName = vendorNameService.getVendorName(sbFullURL.toString());

		if (log.isDebugEnabled())
			log.debug("Vendor name from SMART is : " + vendorName);

		return vendorName;
	}

	public List<PoUnitDetail> getSkuExistInPOUnitDetails(String belkSKU){
		return pimAttributeDao.getSkuExistInPOUnitDetailsDao(belkSKU);
	}

	/**
	 * Method to process additional attributes from SMART by invoking the new
	 * getItem/getPack web services.
	 *
	 */
	public void processAdditionalPimAttributes(List<Car> cars,User user) throws Exception{
		List<String> vStyleOrinList = new ArrayList<String>();
		List<String> vSkuOrinList = new ArrayList<String>();
		if(log.isInfoEnabled()){
            log.info("processAdditionalPimAttributes() : Cars to process  "+cars.size());
        }
		for(Car car : cars){
		    try{

	            List<VendorStyle> stylelist = car.getVendorStyles();
	            for (VendorStyle vStyle : stylelist) {
	                Long tmpOrin = vStyle.getOrinNumber();
	                if (tmpOrin != null) {
	                    vStyleOrinList.add(String.valueOf(vStyle
	                            .getOrinNumber()));
	                    if(log.isInfoEnabled()){
	                        log.info("Vendorstyle Orin Number====>"
	                            + vStyle.getOrinNumber());
	                    }
	                }
	            }

	            if(vStyleOrinList!=null && vStyleOrinList.size()>0){
	                Map<Long ,List<AttributeData>> stylePimMap = getAdditionalStylesDetailsFromSMART(car,vStyleOrinList,Constants.PROCESS_CREATE);
	                syncAdditionalPIMAttributes(car,stylePimMap,"style",user);
	            }

	            Set<VendorSku> skuList = car.getVendorSkus();
	            for(VendorSku vSku : skuList){
	                Long tempSkuOrin = vSku.getOrinNumber();
	                if (tempSkuOrin != null) {
	                    vSkuOrinList.add(String.valueOf(tempSkuOrin));

	                    log.info("Vendorsku Orin Number====>"+ tempSkuOrin);
	                }
	            }
	            Map<Long,List<AttributeData>> styleColorMap = new HashMap<Long,List<AttributeData>>();
	            try  {
                        //Invoking Image update
                        Map<String,IntegrationResponseData>getImageResponse = pimFtpImageManager.uploadOrDeletePimImagesByCar(car);
                        if(getImageResponse!=null){
                            IntegrationResponseData response = getImageResponse.get("SUCCESS");
                            if(response!=null){
                                try{
                                    styleColorMap = processGetImageResponse(response,user,car,Constants.PROCESS_CREATE);
                                }catch(Exception e){
                                    if(log.isErrorEnabled()){
                                        log.error("Error while retrieving Image details "+e);
                                    }
                                }

                            }else{
                                if(log.isInfoEnabled()){
                                    log.info("Get Image response is empty for SUCCESS event ");
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.error("Caught error in GetImage call within processAdditionalPimAttributes : " + e.getMessage());
                    }

	            if(vSkuOrinList!=null && vSkuOrinList.size()>0){
	                Map<Long,List<AttributeData>> skuPimMap = getAdditionalSkuDetailsFromSMART(car,vSkuOrinList,user);
	                //merging style color and sku maps
	                skuPimMap = mergeStyleColorMapWithSkuMap(skuPimMap,styleColorMap);
	                syncAdditionalPIMAttributes(car,skuPimMap,"sku",user);
	            }

	            //Inactivate the failure notes for car if it already exists
                if(isFailureNotesExist(car)){
                    createUpdateFailureNotes(car,user,false,Status.INACTIVE);
                }

		    }catch(Exception e){
		        if(log.isErrorEnabled()){
		            log.error("Error while retrieving attribute and Image details from new getItem service in PO car create flow"+e);
		        }
		        //create a failure note for the car if one doesn't already exist
		        if(!isFailureNotesExist(car)){
		            createUpdateFailureNotes(car,user,true,Status.ACTIVE);
		        }
		    }
		}
	}
	
    /**
     * Method to process additional PIM attributes for component-level style that was merged with existing pattern car.
     */
    public void processAdditionalPimAttributesForPattern(Car patternCar, List<String> childrenStyleOrins, List<String> skuUpcs, User user) throws Exception {
        if(log.isInfoEnabled()){
            log.info("processAdditionalPimAttributesForPattern() : car_id :  "+patternCar.getCarId());
        }
        
        try{
            Set<VendorSku> skuList = new HashSet<VendorSku>();
            if(childrenStyleOrins!=null && childrenStyleOrins.size()>0){
                Map<Long ,List<AttributeData>> stylePimMap = getAdditionalStylesDetailsFromSMART(patternCar,childrenStyleOrins,Constants.PROCESS_CREATE);
                syncAdditionalPIMAttributesForMergedPattern(patternCar,stylePimMap,"style",user);
                
                for (String skuUpc : skuUpcs) {
                    VendorSku sku = carManager.getSku(skuUpc);
                    if (sku != null) {
                        if (log.isInfoEnabled()) log.info("Sku to get attributes: " + sku.getBelkUpc());
                        skuList.add(sku); 
                    } else {
                        log.error("Sku was not persisted yet - can't update attribute" + skuUpc);
                    }
                }
            }
    
            List<String> vSkuOrinList = new ArrayList<String>();
            for(VendorSku vSku : skuList){
                Long tempSkuOrin = vSku.getOrinNumber();
                if (tempSkuOrin != null) {
                    vSkuOrinList.add(String.valueOf(tempSkuOrin));
    
                    log.info("Vendorsku Orin Number====>"+ tempSkuOrin);
                }
            }
            Map<Long,List<AttributeData>> styleColorMap = new HashMap<Long,List<AttributeData>>();
            try  {
                //Invoking Image update
                Map<String,IntegrationResponseData>getImageResponse = pimFtpImageManager.uploadOrDeletePimImagesByCar(patternCar);
                if(getImageResponse!=null){
                    IntegrationResponseData response = getImageResponse.get("SUCCESS");
                    if(response!=null){
                        try{
                            styleColorMap = processGetImageResponse(response,user,patternCar,Constants.PROCESS_CREATE);
                        }catch(Exception e){
                            if(log.isErrorEnabled()){
                                log.error("Error while retrieving Image details for car_id "+patternCar.getCarId()+" error message: "+e);
                            }
                        }
    
                    }else{
                        if(log.isInfoEnabled()){
                            log.info("Get Image response is empty for SUCCESS event for car_id " + patternCar.getCarId());
                        }
                    }
                }
            } catch (Exception e) {
                log.error("Caught error in GetImage call within processAdditionalPimAttributes for car_id "+patternCar.getCarId()+" : " + e.getMessage());
            }
    
            if(vSkuOrinList!=null && vSkuOrinList.size()>0){
                Map<Long,List<AttributeData>> skuPimMap = getAdditionalSkuDetailsFromSMART(patternCar,vSkuOrinList,user);
              //merging style color and sku maps
                skuPimMap = mergeStyleColorMapWithSkuMap(skuPimMap,styleColorMap);
                syncAdditionalPIMAttributesForMergedPattern(patternCar,skuPimMap,"sku",user);
            }
    
            //Inactivate the failure notes for car if it already exists
            if(isFailureNotesExist(patternCar)){
                createUpdateFailureNotes(patternCar,user,false,Status.INACTIVE);
            }
    
        } catch (Exception e){
            if(log.isErrorEnabled()){
                log.error("Error while retrieving attribute and Image details from new getItem service in PO car create flow"+e);
                e.printStackTrace();
            }
            //create a failure note for the car if one doesn't already exist
            if(!isFailureNotesExist(patternCar)){
                createUpdateFailureNotes(patternCar,user,true,Status.ACTIVE);
            }
        }
    }


	/**
	 * Method to process additional PIM attributes for pack.
	 */
    public void processAdditionalPackPimAttributes(List<Car> cars, User user)
            throws Exception {
        List<PackDTO> packList = new ArrayList<PackDTO>();
        for(Car car : cars){
            try{
                List<VendorStyle> stylelist = car.getVendorStyles();
                for (VendorStyle vStyle : stylelist) {
                    Long tmpOrin = vStyle.getOrinNumber();
                    PackDTO packDTO = new PackDTO();
                    if (tmpOrin == null) {
                        packDTO.setVendorNumber(vStyle.getVendorNumber());
                        packDTO.setVendorStyleNumber(vStyle
                                .getVendorStyleNumber());
                        packList.add(packDTO);
                    }
                }

                //Invoking Image update
                Map<String,IntegrationResponseData>getImageResponse = pimFtpImageManager.uploadOrDeletePimImagesByCar(car);
                if(getImageResponse!=null){
                    IntegrationResponseData response = getImageResponse.get("SUCCESS");
                    if(response!=null){
                        try{
                            processGetImageResponse(response,user,car,Constants.PROCESS_CREATE);
                        }catch(Exception e){
                            if(log.isErrorEnabled()){
                                log.error("Error while retrieving style color attributes from Pack "+e);
                            }
                        }

                    }else{
                        if(log.isInfoEnabled()){
                            log.info("Get Image response is empty for SUCCESS event in pack ");
                        }
                    }
                }

                if(packList!=null && packList.size()>0){
                    Map<Long, List<AttributeData>> stylePimMap = getAdditionalPackDetailsFromSMART(car,packList,Constants.PROCESS_CREATE);
                    syncAdditionalPIMAttributes(car,stylePimMap,"pack",user);
                }


                //Inactivate the failure notes for car if it already exists
                if(isFailureNotesExist(car)){
                    createUpdateFailureNotes(car,user,false,Status.INACTIVE);
                }
            }catch(Exception ex){
                if(log.isErrorEnabled()){
                    log.error("Error while retrieving pack and Image details from new getPack service in PO car create flow"+ex);
                }
                //create a failure note for the car if it doesn't already exist
                if(!isFailureNotesExist(car)){
                    createUpdateFailureNotes(car,user,true,Status.ACTIVE);
                }
            }
        }
    }

    /**
     * Method to process the response from getImage service and save the style color attributes.
     * @param car
     *
     * @param itemCatalogDataList
     * @throws Exception
     */
    public Map<Long, List<AttributeData>> processGetImageResponse(IntegrationResponseData response,User user, Car car,String process) throws Exception{
        List<ItemCatalogData> itemCatalogDataList = response.getResponseList();
        Map<Long, List<AttributeData>> consolidatedMap = new HashMap<Long, List<AttributeData>>();
        if (itemCatalogDataList != null) {
            for (ItemCatalogData itemCatalogData : itemCatalogDataList) {
            	String petCreateDate = itemCatalogData.getPimEntry().getEntries().getItemPetCatalogData().getAuditData().getLastModifiedOn().getValue();
            	String dateFormat = itemCatalogData.getPimEntry().getEntries().getItemPetCatalogData().getAuditData().getLastModifiedOn().getFormat();
            	Map<Long, List<AttributeData>> childMap = new HashMap<Long, List<AttributeData>>();
            	Long orin = itemCatalogData.getPimEntry().getItemHeaderInformation()
                        .getPrimaryKey();
            	String type = itemCatalogData.getResponseType();
            	String vendorNumber = null;
            	String vendorStyleNumber = null;
            	if(itemCatalogData.getResponseType().equals(Constants.STYLE_COLOR)) {
                    Long styleColorNum = itemCatalogData.getPimEntry().getItemHeaderInformation()
                        .getPrimaryKey();
                    Long styleOrin = itemCatalogData.getPimEntry().getEntries().getItemStyleColorSpecData().getStyleId();
                    VendorStyle vs = getVendorStyleByOrin(styleOrin);
                    if(vs!=null){
                        vendorNumber = vs.getVendorNumber();
                        vendorStyleNumber = vs.getVendorStyleNumber();
                    }
                    childMap = saveStyleColorAttributes(itemCatalogData,styleColorNum,user,car);
                    if(childMap!=null && childMap.size()>0){
                        consolidatedMap.putAll(childMap);
                    }

            	} else if(itemCatalogData.getResponseType().equals(Constants.PACK_COLOR)){
            	    Long packColorNum = itemCatalogData.getPimEntry().getItemHeaderInformation()
                            .getPrimaryKey();
            	    Long packOrin = itemCatalogData.getPimEntry().getEntries().getItemPackColorSpecData().getPackId();
            	    VendorStyle vs = getVendorStyleByOrin(packOrin);
            	    if(vs!=null){
            	        vendorNumber = vs.getVendorNumber();
                        vendorStyleNumber = vs.getVendorStyleNumber();
            	    }
            	    savePackColorAttributes(itemCatalogData,packColorNum,user,car);
            	}
            	if(process!=null && (process.equalsIgnoreCase(Constants.PROCESS_CREATE)||process.equalsIgnoreCase(Constants.PROCESS_RETRIEVE_ITEM))){
            	    savePetReopenDetails(orin,vendorNumber,vendorStyleNumber,petCreateDate,dateFormat,type);
            	}

            }
        }else{
            if(log.isInfoEnabled()){
                log.info("Response from getItem service is null");
            }
        }
        
        return consolidatedMap;
    }

	/**
	 * Method to call a new web service to call additional attributes
	 * for given skuOrin list.
	 * @param car
	 * @param vSkuOrinList
	 * @return
	 */
    public Map<Long, List<AttributeData>> getAdditionalSkuDetailsFromSMART(Car car,
            List<String> vSkuOrinList, User systemUser) {
        IntegrationRequestData requestData = new IntegrationRequestData();
        Map<Long, List<AttributeData>> consolidatedSkuMap = new HashMap<Long, List<AttributeData>>();
        List<ItemCatalogData>itemCatList = new ArrayList<ItemCatalogData>();
        try {
            if (vSkuOrinList != null && !vSkuOrinList.isEmpty()) {
                requestData.setInputData(vSkuOrinList);
                requestData.setRequestType(GetRequestType.SKU.toString());
                //calling new getItem web service from PIM
                BelkProductService integrationService = new BelkProductService(requestData);
                IntegrationResponseData response = integrationService.getResponse();
                if(response!=null){
                    if(response.getErrorResponseData()!=null){
                        if(log.isErrorEnabled()){
                            log.error("Error while getting response from new getItem web service from PIM");
                        }
                        throw new BelkProductIntegrationException("Error while getting response from PIM "+response.getErrorResponseData());
                    }else if(response.getResponseList()!=null){
                        itemCatList = response.getResponseList();
                        for(ItemCatalogData catalog : itemCatList){
                            handleItemSkuSpec(car,catalog,systemUser);
                            Map<Long, List<AttributeData>>skuDetailsMap = catalog.getPimEntry().getAllAttributes();
                            consolidatedSkuMap.putAll(skuDetailsMap);
                            log.info("consolidatedSkuMap size in getAdditionalSkuDetailsFromSMART  "+consolidatedSkuMap.size());
                        }
                    }
                }
            }
        } catch (BelkProductIntegrationException e) {
            log.fatal(e.getMessage() , e);
        }
        return consolidatedSkuMap;
    }


    /**
	 *  This method is used to get additional vendor style details including style attributes from
	 *   SMART system by passing ORIN number using the new service.
	 * @param car
	 * @param List of style ORIN numbers
	 * @return Map of <StyleOrin, styleDTO>
	 * */
	public Map<Long, List<AttributeData>> getAdditionalStylesDetailsFromSMART(Car car,List<String> vStyleOrinList,String process) throws Exception {
		IntegrationRequestData requestData = new IntegrationRequestData();
		Map<Long, List<AttributeData>> consolidatedStyleMap = new HashMap<Long, List<AttributeData>>();
		List<ItemCatalogData>itemCatList = new ArrayList<ItemCatalogData>();
        try {
            if (vStyleOrinList != null && !vStyleOrinList.isEmpty()) {
                requestData.setInputData(vStyleOrinList);
                requestData.setRequestType(GetRequestType.STYLE.toString());
                BelkProductService integrationService = new BelkProductService(requestData);
                IntegrationResponseData response = integrationService.getResponse();
                if(response!=null){
                    if(response.getErrorResponseData()!=null){
                        if(log.isErrorEnabled()){
                            log.error("Error while getting response from new getPack web service from PIM");
                        }
                        throw new BelkProductIntegrationException("Error while getting response from PIM "+response.getErrorResponseData());
                    }else if(response.getResponseList()!=null){
                        itemCatList = response.getResponseList();
                        for(ItemCatalogData catalog : itemCatList){
                            Map<Long, List<AttributeData>>styleDetailsMap = catalog.getPimEntry().getAllAttributes();
                            consolidatedStyleMap.putAll(styleDetailsMap);
                            if(process!=null && (process.equalsIgnoreCase(Constants.PROCESS_CREATE)||process.equalsIgnoreCase(Constants.PROCESS_RETRIEVE_ITEM))){
                                String petCreateDate = catalog.getPimEntry().getEntries().getItemPetCatalogData().getAuditData().getLastModifiedOn().getValue();
                                String dateFormat = catalog.getPimEntry().getEntries().getItemPetCatalogData().getAuditData().getLastModifiedOn().getFormat();
                                Long orin = catalog.getPimEntry().getItemHeaderInformation()
                                        .getPrimaryKey();
                                String type = catalog.getResponseType();
                                String vendorNumber = null;
                                String vendorStyleNumber = null;
                                VendorStyle vs = getVendorStyleByOrin(orin);
                                if(vs!=null){
                                    vendorNumber = vs.getVendorNumber();
                                    vendorStyleNumber = vs.getVendorStyleNumber();
                                }
                                savePetReopenDetails(orin,vendorNumber,vendorStyleNumber,petCreateDate,dateFormat,type);
                            }
                        }
                    }
                }
            }
        } catch (BelkProductIntegrationException e) {
            log.fatal(e.getMessage() , e);
        }
        return consolidatedStyleMap;
    }

    /**
     * Method to make a web service call to pim for retrieving additional pack
     * details for a given pack list.
     * @param car
     * @param packList
     * @return Map of style id and list of attribute data objects.
     */
    public Map<Long, List<AttributeData>> getAdditionalPackDetailsFromSMART(Car car,
            List<PackDTO> packList,String process) throws Exception{
        IntegrationRequestData requestData = new IntegrationRequestData();
        Map<Long, List<AttributeData>> consolidatedPackMap = new HashMap<Long, List<AttributeData>>();
        List<ItemCatalogData>itemCatList = new ArrayList<ItemCatalogData>();
        List<PackItemRequestData> packItemReqList = new ArrayList<PackItemRequestData>();
        try {
            if (packList != null && !packList.isEmpty()) {
                for(PackDTO pack : packList){
                    PackItemRequestData reqData = new PackItemRequestData();
                    reqData.setVendorNumber(pack.getVendorNumber());
                    reqData.setVendorProductNumber(pack.getVendorStyleNumber());
                    packItemReqList.add(reqData);
                }
                requestData.setInputPackData(packItemReqList);
                requestData.setRequestType(GetRequestType.PACK.toString());
                BelkProductService integrationService = new BelkProductService(requestData);
                IntegrationResponseData response = integrationService.getResponse();
                if(response!=null){
                    if(response.getErrorResponseData()!=null){
                        if(log.isErrorEnabled()){
                            log.error("Error while getting response from new getPack web service from PIM");
                        }
                        throw new BelkProductIntegrationException("Error while getting response from PIM "+response.getErrorResponseData());
                    }else if(response.getResponseList()!=null){
                        itemCatList = response.getResponseList();
                        for(ItemCatalogData catalog : itemCatList){
                            handleItemPackSpec(car,catalog);
                            Map<Long, List<AttributeData>>packDetailsMap = catalog.getPimEntry().getAllAttributes();
                            consolidatedPackMap.putAll(packDetailsMap);
                            if(process!=null && (process.equalsIgnoreCase(Constants.PROCESS_CREATE) || process.equalsIgnoreCase(Constants.PROCESS_RETRIEVE_ITEM))){
                                String petCreateDate = catalog.getPimEntry().getEntries().getItemPetCatalogData().getAuditData().getLastModifiedOn().getValue();
                                String dateFormat = catalog.getPimEntry().getEntries().getItemPetCatalogData().getAuditData().getLastModifiedOn().getFormat();
                                Long orin = catalog.getPimEntry().getItemHeaderInformation()
                                        .getPrimaryKey();
                                String type = catalog.getResponseType();
                                String vendorNumber = null;
                                String vendorStyleNumber = null;
                                VendorStyle vs = getVendorStyleByOrin(orin);
                                if(vs!=null){
                                    vendorNumber = vs.getVendorNumber();
                                    vendorStyleNumber = vs.getVendorStyleNumber();
                                }
                                savePetReopenDetails(orin,vendorNumber,vendorStyleNumber,petCreateDate,dateFormat,type);
                            }

                        }
                    }

                }
            }
        } catch (BelkProductIntegrationException e) {
            log.fatal(e.getMessage() , e);
        }
        return consolidatedPackMap;
    }


    /**
     *  This method is used to get additional vendor style details including Group attributes from
     *   SMART system by passing Grouping Id using the new service.
     * @param car
     * @param List of Grouping Ids
     * @return Map of <GroupId, styleDTO>
     * */
    public Map<Long, List<AttributeData>> getAdditionalGroupDetailsFromSMART(List<String> groupIds, boolean isConvertedGrouping) throws Exception {
        if(log.isInfoEnabled()){
             log.info("Entered getAdditionalGroupDetailsFromSMART method ");
        }
        GroupIntegrationRequestData requestData = new GroupIntegrationRequestData();
        Map<Long, List<AttributeData>> consolidatedGroupMap = new HashMap<Long, List<AttributeData>>();
        List<GroupCatalogData> groupCatList = new ArrayList<GroupCatalogData>();
        try {
            if (groupIds != null && !groupIds.isEmpty()) {
                if(log.isInfoEnabled()){
                    log.info("groupIds is not null and not Empty");
            }
                if (isConvertedGrouping) {
                    if(log.isInfoEnabled()){
                        log.info("isConvertedGrouasdping::"+isConvertedGrouping);
                    }
                    List<PackGroupRequestData> packDataList = new ArrayList<PackGroupRequestData>();
                    for (String groupId : groupIds) {
                        String[] vendorNumVendorStyleArr = groupId.split(",");
                        if(log.isInfoEnabled()){
                            log.info("vendorNumVendorStyleArr::"+groupId.split(","));
                        }
                        if (vendorNumVendorStyleArr.length>=2) {
                            PackGroupRequestData packData = new PackGroupRequestData();
                            packData.setVendorNumber(vendorNumVendorStyleArr[0]);
                            packData.setVendorProductNumber(vendorNumVendorStyleArr[1]);
                            packDataList.add(packData);
                        }
                    }

                    requestData.setInputPackData(packDataList);
                    requestData.setRequestType(GetGroupRequestType.VENDORSTYLE.toString());
                }
                else {
                   if(log.isInfoEnabled()){
                       log.info("isConvertedGrouping::"+isConvertedGrouping);
                    }
                    requestData.setInputData(groupIds);
                    requestData.setRequestType(GetGroupRequestType.GROUPID.toString());
                }
                BelkGroupService integrationService = new BelkGroupService(requestData);
                GroupIntegrationResponseData response = integrationService.getResponse();
                if(response!=null){
                    if(log.isInfoEnabled()){
                        log.info("PIM attribute response is not null");
                    }
                    if(response.getErrorResponseData()!=null){
                        if(log.isInfoEnabled()){
                            log.info("getErrorResponseData::"+response.getErrorResponseData());
                          }
                        if(log.isErrorEnabled()){
                            log.error("Error while getting response from new getGroupRequest web service from PIM");
                        }
                        throw new BelkProductIntegrationException("Error while getting response from PIM "+response.getErrorResponseData());
                    }else if(response.getResponseList()!=null){
                        if(log.isInfoEnabled()){
                            log.info("Response List from XML ::"+response.getResponseList());
                        }
                        groupCatList = response.getResponseList();
                        for(GroupCatalogData catalog : groupCatList){
                        	if(catalog.getPimEntry().getAllAttributes() != null) {
                              if(log.isInfoEnabled()){
                                   log.info("catalog.getPimEntry().getAllAttributes() is not null");
                               }
                        		Map<Long, List<AttributeData>>groupDetailsMap = catalog.getPimEntry().getAllAttributes();
                        		consolidatedGroupMap.putAll(groupDetailsMap);
                        	}
                        }
                    }
                }
            }
        } catch (BelkProductIntegrationException e) {
            log.fatal(e.getMessage() , e);
        }
        return consolidatedGroupMap;
    }

    /**
     * This method is used to get additional BCG Group Details from SMART
     * @param groupIds
     * @param isConvertedGrouping
     * @return 
     * @throws Exception
     */
    public Map<String, String> getAdditionalBCGGroupDetailsFromSMART(List<String> groupIds, boolean isConvertedGrouping) throws Exception {
        Map<String,String> bcgAttrMap = new HashMap<String,String>();
        GroupIntegrationRequestData requestData = new GroupIntegrationRequestData();
        List<GroupCatalogData> groupCatList = new ArrayList<GroupCatalogData>();
        try {
            if (groupIds != null && !groupIds.isEmpty()) {
                if (isConvertedGrouping) {
                    List<PackGroupRequestData> packDataList = new ArrayList<PackGroupRequestData>();
                    for (String groupId : groupIds) {
                        String[] vendorNumVendorStyleArr = groupId.split(",");
                        if (vendorNumVendorStyleArr.length>=2) {
                            PackGroupRequestData packData = new PackGroupRequestData();
                            packData.setVendorNumber(vendorNumVendorStyleArr[0]);
                            packData.setVendorProductNumber(vendorNumVendorStyleArr[1]);
                            packDataList.add(packData);
                        }
                    }
                    
                    requestData.setInputPackData(packDataList);
                    requestData.setRequestType(GetGroupRequestType.VENDORSTYLE.toString());
                }
                else {
                    requestData.setInputData(groupIds);
                    requestData.setRequestType(GetGroupRequestType.GROUPID.toString());
                }
                BelkGroupService integrationService = new BelkGroupService(requestData);
                GroupIntegrationResponseData response = integrationService.getResponse();
                if(response!=null){
                    if(response.getErrorResponseData()!=null){
                        if(log.isErrorEnabled()){
                            log.error("Error while getting response from new getGroupRequest web service from PIM");
                        }
                        throw new BelkProductIntegrationException("Error while getting response from PIM "+response.getErrorResponseData());
                    }else if(response.getResponseList()!=null){                        
                        groupCatList = response.getResponseList();
                        for(GroupCatalogData catalog : groupCatList){
                                GroupCollectionSpecData collectionSpec = catalog.getPimEntry().getEntries().getGroupCollectionSpecData();
                                if(StringUtils.isBlank(collectionSpec.getOutfitNavigation())){
	                                bcgAttrMap.put("Outfit_Navigation", getBCGOutfitNavigation(catalog.getPimEntry()
                                            .getAttributesInformation()));
                                }
                                bcgAttrMap.put("CarBrand", collectionSpec.getCarBrand());
                        }
                    }
                }
            }
        } catch (BelkProductIntegrationException e) {
            log.fatal(e.getMessage() , e);
        }
        return bcgAttrMap;
    }

    /**
     * Returns the outfitnavigation value for BGC from <sec_spec_*-Beauty> --><_*_GBL_OUTFITNAV>.
     *
     * @param attributeDataMap
     * @return
     */
    private String getBCGOutfitNavigation(Map<String, List<AttributeData>> attributeDataMap) {
        String outfitNav = null;
        List<AttributeData> attributeDataList;
        try {
            if (attributeDataMap != null) {
                for (String tagName : attributeDataMap.keySet()) {
                    if (tagName.startsWith("sec_spec_") && tagName.endsWith("-Beauty")) {
                        attributeDataList = attributeDataMap.get(tagName);
                        if (attributeDataList != null && !attributeDataList.isEmpty()) {
                            for (AttributeData curEle : attributeDataList) {
                                if (curEle.getAttributeName().endsWith("GBL_OUTFITNAV")) {
                                    outfitNav = curEle.getAttributeValue();
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            log.fatal(ex.getMessage(), ex);
        }
        return outfitNav;
    }

    /**
	 * Method to persist the data with the new attribute values obtained from SMART.
	 *
	 */
	public void syncAdditionalPIMAttributes(Car car,Map<Long, List<AttributeData>> styleAttrMap,String type,User systemUser)throws Exception{
        if(log.isInfoEnabled()){
             log.info("Entered syncAdditionalPIMAttributes method");
        }
		long pimAttrId;
		long attrId;
		String pimAttrName;
		String attrSpecName;
		String pimAttrValue;
		PIMAttribute pimAttrObj;
        String carTableName;
		long deptId = car.getDepartment().getDeptId();
		VendorStyle cStyle = car.getVendorStyle();

		long classId = cStyle.getClassification().getClassId();
		long prodTypeId = cStyle.getProductType().getProductTypeId();
		boolean isExcluded = false;
		long vsId;
		List<AttributeData> pimAttrList = new ArrayList<AttributeData>();
		if(log.isInfoEnabled()){
		    log.info("syncing additional attributes for car Id "+car.getCarId()+"with classId "+classId+"department id "+deptId+"productTypeId "+prodTypeId+" type"+type);
		}
		//retrieve car pim attribute mapping details
		List<CarsPIMAttributeMapping>carMappingList = carsPIMAttributeMappingManager.getCarsPIMMappingDetails(classId, deptId, prodTypeId);
		//Retrieve all global attributes
		List<CarsPIMGlobalAttributeMapping> globalAttrList = carsPIMGlobalAttributeMappingManager.getAllCarsPimGlobalAttributes();

		if(styleAttrMap!=null && styleAttrMap.size()>0){
            if(log.isInfoEnabled()){
                 log.info("styleAttrMap is not null and size greater than 0");
           }
		    for(Map.Entry<Long,List<AttributeData>> mapEntry : styleAttrMap.entrySet()){
		        List<VendorStylePIMAttribute> vsPimAttrList = new ArrayList<VendorStylePIMAttribute>();
		        Set<VendorSkuPIMAttribute> vSkuPimAttrSet = new HashSet<VendorSkuPIMAttribute>();
		        pimAttrList = mapEntry.getValue();
		        if(log.isInfoEnabled()){
                    log.info("size of AttributeData list in map "+pimAttrList.size());
                }
		        vsId = mapEntry.getKey();
		        VendorStyle vStyle = getVendorStyleByOrin(new Long(vsId));
		        VendorSku vSku = new VendorSku();
		        if(type.equalsIgnoreCase("sku")){
		            vSku = getVendorSkuByOrin(new Long(vsId));
		        }else if(type.equalsIgnoreCase("pack")){
		            vSku = getVendorSkuByOrin(new Long(vsId));
		            vStyle = vSku.getVendorStyle();
		        }
		        if(log.isDebugEnabled()){
		            log.debug("processing for vstyle/sku id "+vsId);
		        }
        		if(pimAttrList!=null && pimAttrList.size()>0){
                  if(log.isInfoEnabled()){
                       log.info("pimAttrList is not null and size greater than 0 ");
                  }
        			for(AttributeData entry : pimAttrList){
        				pimAttrObj = new PIMAttribute();
        				pimAttrName = entry.getAttributeName();
        				pimAttrValue = entry.getAttributeValue();
        				attrSpecName = entry.getQName().toString();
        				if(log.isDebugEnabled()){
                            log.debug("Attribute name "+pimAttrName+" attribute value "+pimAttrValue+" type "+type);
                        }
        				
        				pimAttrObj = pimAttributeDao.getPIMAttributeByName(pimAttrName);
        				if(pimAttrObj == null){
                             if(log.isInfoEnabled()){
                                  log.info("pimAttrObj is null");
                               }
        				    String nameWithPath = attrSpecName+"/"+pimAttrName;
        				    if(log.isDebugEnabled()){
        				        log.debug("nameWithPath value is "+nameWithPath);
        				    }
        				    pimAttrObj = pimAttributeDao.getPIMAttributeByName(nameWithPath);
        				}
        				//in case of a new attribute create one
        				if(pimAttrObj == null){
        				    pimAttrObj = new PIMAttribute();
        				    pimAttrObj.setName(pimAttrName);
        				    pimAttrObj.setDescr(pimAttrName);
        				    pimAttrObj.setIsPimAttrDisplayble(Constants.FLAG_YES);
                            this.setAuditInfo(systemUser, pimAttrObj);
                            if(log.isDebugEnabled()){
                                log.debug("Creating anew pim_attribute since not already existing "+pimAttrObj);
                            }
                            pimAttrObj = (PIMAttribute) pimAttributeDao.save(pimAttrObj);
        				}
        				pimAttrId = pimAttrObj.getPimAttrId();
        				if(log.isDebugEnabled()){
                            log.debug("processing pim attribute Id  "+pimAttrId+" and pim attr name "+pimAttrName);
                        }
        				//check if the attribute is excluded
        				isExcluded = pimAttributeExclusionListManager.isAttributeExcluded(pimAttrId);
        				if(isExcluded){
        				    if(log.isInfoEnabled()){
                                log.info("pim attribute "+pimAttrId+" is excluded ");
                            }
        					continue;
        				}

                        Map<Long,List<CarsPIMAttributeMapping>> carsMap = getCarPIMMap(carMappingList);
                        //if mapping exists in cars pim mapping table
                        if(carsMap!=null && carsMap.size()>0 && carsMap.containsKey(pimAttrId)){
                            if(log.isInfoEnabled()){
                               log.info("carsMap is not null and size greater than 0 and carsMap.containsKey("+pimAttrId+")");
                            }
                        List<CarsPIMAttributeMapping> carPimAttrList = carsMap.get(pimAttrId);
                            if(carPimAttrList!=null){
                                if (isPimBrandAttribute(pimAttrObj)) {
                                    pimAttrValue = encodeBrandWithSpecialCharacters(pimAttrValue);
                                }
                                for(CarsPIMAttributeMapping carPIMAttributeMapping : carPimAttrList){
                                    attrId = carPIMAttributeMapping.getId().getAttrId();
                                    carTableName = carPIMAttributeMapping.getCarTableNm();
                                    Long lAttrId = new Long(attrId);
                                    if(log.isDebugEnabled()){
                                        log.debug("car pim attribute mapping found for pim_attribute id "+pimAttrId);
                                    }
                                    //Business requirement is to avoid updating car level attributes with Item_UDA_Spec data for sku type call
                                    if(carTableName.equalsIgnoreCase(Constants.CAR_ATTRIBUTE) && !(type.equalsIgnoreCase("sku") && attrSpecName.equals(Constants.ITEM_UDA_SPEC))){
                                        if(isCarAttributeExits(car,lAttrId)){
                                            updateAttributeValue(Constants.CAR_ATTRIBUTE, Constants.ATTR_VALUE_COLUMN, pimAttrId, pimAttrValue,lAttrId,car,vStyle,vSku,systemUser);
                                        }else{
                                            addCarAttribute(pimAttrValue,lAttrId,car,systemUser);
                                        }

                                    }else if(carTableName.equalsIgnoreCase(Constants.CAR_SKU_ATTRIBUTE)){
                                        if(isCarSkuAttributeExits(vSku,lAttrId)){
                                            updateAttributeValue(Constants.CAR_SKU_ATTRIBUTE, Constants.ATTR_VALUE_COLUMN, pimAttrId, pimAttrValue,lAttrId,car,vStyle,vSku,systemUser);
                                        }else{
                                            addCarSkuAttribute(pimAttrValue,lAttrId,vSku,systemUser);
                                        }

                                    }else{
                                        if(type.equalsIgnoreCase("sku") && pimAttrValue==null){
                                            pimAttrValue = getExistingAttributeValue(car,lAttrId);
                                        }

                                        if(!(type.equalsIgnoreCase("sku") && attrSpecName.equals(Constants.ITEM_UDA_SPEC))){
                                            if(isCarAttributeExits(car,lAttrId)){
                                                updateAttributeValue(Constants.CAR_ATTRIBUTE, Constants.ATTR_VALUE_COLUMN, pimAttrId, pimAttrValue,lAttrId,car,vStyle,vSku,systemUser);
                                            }else{
                                                addCarAttribute(pimAttrValue,lAttrId,car,systemUser);
                                            }
                                        }
                                        //call to update zbm & iph attributes at sku level as well in case of BOTH table name
                                        //these spec's are ignored during the sku call
                                        if(type.equalsIgnoreCase("style") && (attrSpecName.startsWith("sec_spec")||attrSpecName.startsWith("zbm_sec_spec"))){
                                            upsertAttributeValueForAllSkus(car,lAttrId,pimAttrValue,systemUser);
                                        }

                                        if(vSku!=null && vSku.getCarSkuId()>0){
                                            if(isCarSkuAttributeExits(vSku,lAttrId)){
                                                updateAttributeValue(Constants.CAR_SKU_ATTRIBUTE, Constants.ATTR_VALUE_COLUMN, pimAttrId, pimAttrValue,lAttrId,car,vStyle,vSku,systemUser);
                                            }else{
                                                addCarSkuAttribute(pimAttrValue,lAttrId,vSku,systemUser);
                                            }

                                        }
                                    }

                                    mergeAttributeLookUpValues(attributeManager.getAttribute(lAttrId),pimAttrValue,systemUser);
                                }
                            }

    					}else if(isGlobalAttribute(pimAttrId,globalAttrList)){//check if attribute is part of global attribute list
    					    if(log.isInfoEnabled()){
                                log.info("global attribute mapping found for pim_attribute id "+pimAttrId);
                            }
    					    for(CarsPIMGlobalAttributeMapping glblMapping : globalAttrList){
    					        if(glblMapping.getPimAttrId()==pimAttrId){
    					            String tabName = glblMapping.getCarTableNm();
                                    String colName = glblMapping.getColumnNm();
                                    Long cAttrId = glblMapping.getCarAttrId();
                                    //Business requirement is to avoid updating car level attributes with Item_UDA_Spec data for sku type call
                                    if(tabName.equalsIgnoreCase(Constants.CAR_ATTRIBUTE) && !(type.equalsIgnoreCase("sku") && attrSpecName.equals(Constants.ITEM_UDA_SPEC))){
                                        if(isCarAttributeExits(car,cAttrId)){
                                            updateAttributeValue(tabName, colName, pimAttrId, pimAttrValue,cAttrId,car,vStyle,vSku,systemUser);
                                        }else{
                                            addCarAttribute(pimAttrValue,cAttrId,car,systemUser);
                                        }
                                    }else if(tabName.equalsIgnoreCase(Constants.CAR_SKU_ATTRIBUTE) && vSku!=null && vSku.getCarSkuId()>0){
                                        if(isCarSkuAttributeExits(vSku,cAttrId)){
                                            updateAttributeValue(tabName, colName, pimAttrId, pimAttrValue,cAttrId,car,vStyle,vSku,systemUser);
                                        }else{
                                            addCarSkuAttribute(pimAttrValue,cAttrId,vSku,systemUser);
                                        }

                                    }else if((tabName.equalsIgnoreCase(Constants.VENDOR_STYLE) && vStyle!=null)||
                                                (tabName.equalsIgnoreCase(Constants.VENDOR_SKU) && vSku!=null && vSku.getCarSkuId()>0)){
                                        updateAttributeValue(tabName, colName, pimAttrId, pimAttrValue,cAttrId,car,vStyle,vSku,systemUser);
                                    }else if(tabName.equalsIgnoreCase(Constants.BOTH)){
                                        if(type.equalsIgnoreCase("sku") && pimAttrValue==null){
                                            pimAttrValue = getExistingAttributeValue(car,cAttrId);
                                        }
                                        if(!(type.equalsIgnoreCase("sku") && attrSpecName.equals(Constants.ITEM_UDA_SPEC))){
                                            if(isCarAttributeExits(car,cAttrId)){
                                                updateAttributeValue(Constants.CAR_ATTRIBUTE, Constants.ATTR_VALUE_COLUMN, pimAttrId, pimAttrValue,cAttrId,car,vStyle,vSku,systemUser);
                                            }else{
                                                addCarAttribute(pimAttrValue,cAttrId,car,systemUser);
                                            }
                                        }
                                        //call to update zbm & iph attributes at sku level as well in case of BOTH table name
                                        //these spec's are ignored during the sku call
                                        if(type.equalsIgnoreCase("style") && (attrSpecName.startsWith("sec_spec")||attrSpecName.startsWith("zbm_sec_spec"))){
                                            upsertAttributeValueForAllSkus(car,cAttrId,pimAttrValue,systemUser);
                                        }
                                        if(vSku!=null && vSku.getCarSkuId()>0){
                                            if(isCarSkuAttributeExits(vSku,cAttrId)){
                                                updateAttributeValue(Constants.CAR_SKU_ATTRIBUTE, Constants.ATTR_VALUE_COLUMN, pimAttrId, pimAttrValue,cAttrId,car,vStyle,vSku,systemUser);
                                            }else{
                                                addCarSkuAttribute(pimAttrValue,cAttrId,vSku,systemUser);
                                            }

                                        }
                                    }

                                    if(cAttrId!=null){
                                        mergeAttributeLookUpValues(attributeManager.getAttribute(cAttrId),pimAttrValue,systemUser);
                                    }
    					        }
    					    }
    					}else{//new attribute

    					    if(type.equalsIgnoreCase("style") || type.equalsIgnoreCase("pack")){
    					        if(log.isDebugEnabled()){
                                    log.debug("No mapping found for pim_attribute id "+pimAttrId+" inserting a new attribute in VENDORSTYLE_PIM_ATTRIBUTE table with value "+pimAttrValue);
                                    log.debug("For vendor style id "+vStyle.getVendorStyleId()+" with value  "+pimAttrValue);
                                }
    					        VendorStylePIMAttribute vsPimAttr = new VendorStylePIMAttribute();
    					        VendorStylePIMAttributeId vsPimAttrId = new VendorStylePIMAttributeId(vStyle.getVendorStyleId(),pimAttrObj.getPimAttrId());
    					        if(log.isInfoEnabled()){
    					            log.info("vendor style PIM attribute Id being inserted is "+vsPimAttrId);
    					        }
    					        vsPimAttr.setId(vsPimAttrId);
    					        vsPimAttr.setPimAttribute(pimAttrObj);
    					        vsPimAttr.setValue(pimAttrValue);
    					        vsPimAttr.setVendorStyle(vStyle);
    					        this.setAuditInfo(systemUser, vsPimAttr);
    					        vsPimAttrList.add(vsPimAttr);
    					    }else if(type.equalsIgnoreCase("sku")){
    					        if(log.isInfoEnabled()){
                                    log.info("No mapping found for pim_attribute id "+pimAttrId+" inserting a new attribute in VENDORSKU_PIM_ATTRIBUTE table with value "+pimAttrValue);
                                }
    					        VendorSkuPIMAttribute vSkuPimAttr = new VendorSkuPIMAttribute();
    					        VendorSkuPIMAttributeId vSkuPimAttrId = new VendorSkuPIMAttributeId(vSku.getCarSkuId(),pimAttrObj.getPimAttrId());
    					        vSkuPimAttr.setId(vSkuPimAttrId);
    					        vSkuPimAttr.setPimAttributeDetails(pimAttrObj);
    					        vSkuPimAttr.setVendorSkuDetails(vSku);
    					        vSkuPimAttr.setAttributeValue(pimAttrValue);
    					        this.setAuditInfo(systemUser, vSkuPimAttr);
    					        vSkuPimAttrSet.add(vSkuPimAttr);
    					    }
    					}

        			}
                    //persist into vendorstyle_pim_attribute table
                    if(vsPimAttrList!=null && vsPimAttrList.size()>0){
                        if(log.isDebugEnabled()){
                            log.debug("Saving "+ vsPimAttrList.size() +" vendor style pim attributes ");
                        }
                        vStyle.getVendorStylePIMAttribute().addAll(vsPimAttrList);
                        pimAttributeDao.saveVendoorStyle(vStyle);
                    }
                    //persist into vendorsku_pim_attribute table
                    if(vSkuPimAttrSet!=null && vSkuPimAttrSet.size()>0){
                        if(log.isDebugEnabled()){
                            log.debug("Saving "+ vSkuPimAttrSet.size() +" vendor sku pim attributes ");
                        }
                        vSku.getSkuPIMAttributes().addAll(vSkuPimAttrSet);
                        pimAttributeDao.saveVendorSku(vSku);
                    }
        		}else{
        			if(log.isErrorEnabled()){
        			    log.error("No Additional attributes from PIM to process  ");
        			}
        		}
    		}
		}
	}
	
	public void syncAdditionalPIMAttributesForMergedPattern(Car car, Map<Long, List<AttributeData>> styleAttrMap,String type,User systemUser)throws Exception{
        long pimAttrId;
        String pimAttrName;
        String attrSpecName;
        String pimAttrValue;
        PIMAttribute pimAttrObj;
        long deptId = car.getDepartment().getDeptId();
        VendorStyle cStyle = car.getVendorStyle();

        long classId = cStyle.getClassification().getClassId();
        long prodTypeId = cStyle.getProductType().getProductTypeId();
        boolean isExcluded = false;
        long vsId;
        List<AttributeData> pimAttrList = new ArrayList<AttributeData>();
        if(log.isInfoEnabled()){
            log.info("syncing additional attributes for car Id "+car.getCarId()+"with classId "+classId+"department id "+deptId+"productTypeId "+prodTypeId+" type"+type);
        }
        //retrieve car pim attribute mapping details
        List<CarsPIMAttributeMapping>carMappingList = carsPIMAttributeMappingManager.getCarsPIMMappingDetails(classId, deptId, prodTypeId);
        //Retrieve all global attributes
        List<CarsPIMGlobalAttributeMapping> globalAttrList = carsPIMGlobalAttributeMappingManager.getAllCarsPimGlobalAttributes();

        if(styleAttrMap!=null && styleAttrMap.size()>0){
            for(Map.Entry<Long,List<AttributeData>> mapEntry : styleAttrMap.entrySet()){
                List<VendorStylePIMAttribute> vsPimAttrList = new ArrayList<VendorStylePIMAttribute>();
                Set<VendorSkuPIMAttribute> vSkuPimAttrSet = new HashSet<VendorSkuPIMAttribute>();
                pimAttrList = mapEntry.getValue();
                if(log.isInfoEnabled()){
                    log.info("size of AttributeData list in map "+pimAttrList.size());
                }
                vsId = mapEntry.getKey();
                VendorStyle vStyle = getVendorStyleByOrin(new Long(vsId));
                VendorSku vSku = new VendorSku();
                if(type.equalsIgnoreCase("sku")){
                    vSku = getVendorSkuByOrin(new Long(vsId));
                }else if(type.equalsIgnoreCase("pack")){
                    vSku = getVendorSkuByOrin(new Long(vsId));
                    vStyle = vSku.getVendorStyle();
                }
                if(log.isDebugEnabled()){
                    log.debug("processing for vstyle/sku id "+vsId);
                }
                if(pimAttrList!=null && pimAttrList.size()>0){
                    for(AttributeData entry : pimAttrList){
                        pimAttrObj = new PIMAttribute();
                        pimAttrName = entry.getAttributeName();
                        pimAttrValue = entry.getAttributeValue();
                        attrSpecName = entry.getQName().toString();
                        if(log.isDebugEnabled()){
                            log.debug("Attribute name "+pimAttrName+" attribute value "+pimAttrValue+" type "+type);
                        }
                        
                        pimAttrObj = pimAttributeDao.getPIMAttributeByName(pimAttrName);
                        if(pimAttrObj == null){
                            String nameWithPath = attrSpecName+"/"+pimAttrName;
                            if(log.isDebugEnabled()){
                                log.debug("nameWithPath value is "+nameWithPath);
                            }
                            pimAttrObj = pimAttributeDao.getPIMAttributeByName(nameWithPath);
                        }
                        //in case of a new attribute create one
                        if(pimAttrObj == null){
                            pimAttrObj = new PIMAttribute();
                            pimAttrObj.setName(pimAttrName);
                            pimAttrObj.setDescr(pimAttrName);
                            pimAttrObj.setIsPimAttrDisplayble(Constants.FLAG_YES);
                            this.setAuditInfo(systemUser, pimAttrObj);
                            if(log.isDebugEnabled()){
                                log.debug("Creating anew pim_attribute since not already existing "+pimAttrObj);
                            }
                            pimAttrObj = (PIMAttribute) pimAttributeDao.save(pimAttrObj);
                        }
                        pimAttrId = pimAttrObj.getPimAttrId();
                        if(log.isDebugEnabled()){
                            log.debug("processing pim attribute Id  "+pimAttrId+" and pim attr name "+pimAttrName);
                        }
                        //check if the attribute is excluded
                        isExcluded = pimAttributeExclusionListManager.isAttributeExcluded(pimAttrId);
                        if(isExcluded){
                            if(log.isInfoEnabled()){
                                log.info("pim attribute "+pimAttrId+" is excluded ");
                            }
                            continue;
                        }

                        Map<Long,List<CarsPIMAttributeMapping>> carsMap = getCarPIMMap(carMappingList);
                        //if mapping exists in cars pim mapping table
                        if(carsMap!=null && carsMap.size()>0 && carsMap.containsKey(pimAttrId)){
                            if (log.isInfoEnabled()) {
                                log.info("This is not the first component style received for pattern car: "+car.getCarId()
                                    +", therefore style attributes which have car attribute mapping will be ignored.");
                            }
                        }else if(isGlobalAttribute(pimAttrId,globalAttrList)){//check if attribute is part of global attribute list
                            if (log.isInfoEnabled()) {
                                log.info("This is not the first component style received for pattern car: "+car.getCarId()
                                    +", therefore style attributes which have global car attribute mapping will be ignored.");
                            }
                        }else{//new attribute

                            if(type.equalsIgnoreCase("style") || type.equalsIgnoreCase("pack")){
                                if(log.isDebugEnabled()){
                                    log.debug("No mapping found for pim_attribute id "+pimAttrId+" inserting a new attribute in VENDORSTYLE_PIM_ATTRIBUTE table with value "+pimAttrValue);
                                    log.debug("For vendor style id "+vStyle.getVendorStyleId()+" with value  "+pimAttrValue);
                                }
                                VendorStylePIMAttribute vsPimAttr = new VendorStylePIMAttribute();
                                VendorStylePIMAttributeId vsPimAttrId = new VendorStylePIMAttributeId(vStyle.getVendorStyleId(),pimAttrObj.getPimAttrId());
                                if(log.isInfoEnabled()){
                                    log.info("vendor style PIM attribute Id being inserted is "+vsPimAttrId);
                                }
                                vsPimAttr.setId(vsPimAttrId);
                                vsPimAttr.setPimAttribute(pimAttrObj);
                                vsPimAttr.setValue(pimAttrValue);
                                vsPimAttr.setVendorStyle(vStyle);
                                this.setAuditInfo(systemUser, vsPimAttr);
                                vsPimAttrList.add(vsPimAttr);
                            }else if(type.equalsIgnoreCase("sku")){
                                if(log.isInfoEnabled()){
                                    log.info("No mapping found for pim_attribute id "+pimAttrId+" inserting a new attribute in VENDORSKU_PIM_ATTRIBUTE table with value "+pimAttrValue);
                                }
                                VendorSkuPIMAttribute vSkuPimAttr = new VendorSkuPIMAttribute();
                                VendorSkuPIMAttributeId vSkuPimAttrId = new VendorSkuPIMAttributeId(vSku.getCarSkuId(),pimAttrObj.getPimAttrId());
                                vSkuPimAttr.setId(vSkuPimAttrId);
                                vSkuPimAttr.setPimAttributeDetails(pimAttrObj);
                                vSkuPimAttr.setVendorSkuDetails(vSku);
                                vSkuPimAttr.setAttributeValue(pimAttrValue);
                                this.setAuditInfo(systemUser, vSkuPimAttr);
                                vSkuPimAttrSet.add(vSkuPimAttr);
                            }
                        }

                    }
                    //persist into vendorstyle_pim_attribute table
                    if(vsPimAttrList!=null && vsPimAttrList.size()>0){
                        if(log.isDebugEnabled()){
                            log.debug("Saving "+ vsPimAttrList.size() +" vendor style pim attributes ");
                        }
                        vStyle.getVendorStylePIMAttribute().addAll(vsPimAttrList);
                        pimAttributeDao.saveVendoorStyle(vStyle);
                    }
                    //persist into vendorsku_pim_attribute table
                    if(vSkuPimAttrSet!=null && vSkuPimAttrSet.size()>0){
                        if(log.isDebugEnabled()){
                            log.debug("Saving "+ vSkuPimAttrSet.size() +" vendor sku pim attributes ");
                        }
                        vSku.getSkuPIMAttributes().addAll(vSkuPimAttrSet);
                        pimAttributeDao.saveVendorSku(vSku);
                    }
                }else{
                    if(log.isErrorEnabled()){
                        log.error("No Additional attributes from PIM to process  ");
                    }
                }
            }
        }
    }

	/**
	 * Method to persist the data with the new attribute values obtained from SMART for Groups.
	 *
	 * If type=STYLE, that means attributes were fetched via a default style component.  Push the
	 * attributes from default style component to Grouping.
	 *
	 * @param car
	 * @param groupAttrMap
	 * @param systemUser
	 * @throws Exception
	 */
	@Override
    public void syncAdditionalPIMAttributesForGroup(Car car, Map<Long, List<AttributeData>> groupAttrMap, String type, User systemUser)throws Exception{
        long pimAttrId;
        long attrId;
        String pimAttrName;
        String attrSpecName;
        String pimAttrValue;
        PIMAttribute pimAttrObj;
        String carTableName;
        long deptId = car.getDepartment().getDeptId();
        VendorStyle patternVS = car.getVendorStyle();
        if (patternVS==null) {
            return; //if VendorStyle still doesn't exist, nothing to update.  Skip.
        }
        long classId = patternVS.getClassification().getClassId();
        long prodTypeId = patternVS.getProductType().getProductTypeId();
        boolean isExcluded = false;

        List<String> excludedPimAttrListForGroup = new ArrayList<String>();
        excludedPimAttrListForGroup.add(Constants.ATTR_PRODUCT_NAME);
        excludedPimAttrListForGroup.add(Constants.ATTR_PRODUCT_DESCR);

        List<AttributeData> pimAttrList = new ArrayList<AttributeData>();
        if(log.isInfoEnabled()){
            log.info("syncing additional attributes for car Id "+car.getCarId()+"with classId "+classId+"department id "+deptId+"productTypeId "+prodTypeId+" type"+type);
        }
        //retrieve car pim attribute mapping details
        List<CarsPIMAttributeMapping>carMappingList = carsPIMAttributeMappingManager.getCarsPIMMappingDetails(classId, deptId, prodTypeId);
        //Retrieve all global attributes
        List<CarsPIMGlobalAttributeMapping> globalAttrList = carsPIMGlobalAttributeMappingManager.getAllCarsPimGlobalAttributes();

        if(groupAttrMap!=null && groupAttrMap.size()>0){
            for(Map.Entry<Long,List<AttributeData>> mapEntry : groupAttrMap.entrySet()){
                List<VendorStylePIMAttribute> vsPimAttrList = new ArrayList<VendorStylePIMAttribute>();
                pimAttrList = mapEntry.getValue();
                if(log.isInfoEnabled()){
                    log.info("size of AttributeData list in map "+pimAttrList.size());
                }
                if(log.isDebugEnabled()){
                    log.debug("processing for vendor_style group_id "+mapEntry.getKey());
                }
                if(pimAttrList!=null && pimAttrList.size()>0){
                    for(AttributeData entry : pimAttrList){
                        pimAttrObj = new PIMAttribute();
                        pimAttrName = entry.getAttributeName();
                        pimAttrValue = entry.getAttributeValue();
                        attrSpecName = entry.getQName().toString();
                        if(log.isDebugEnabled()){
                            log.debug("Attribute name "+pimAttrName+" attribute value "+pimAttrValue+" type "+type);
                        }

                        // for Groupings, product_name & product_description should be ignored.
                        if (excludedPimAttrListForGroup.contains(pimAttrName)) {
                            if(log.isDebugEnabled()){
                                log.debug("Attribute name "+pimAttrName+" will be ignored for Groupings");
                            }
                            continue;
                        }

                        pimAttrObj = pimAttributeDao.getPIMAttributeByName(pimAttrName);
                        if(pimAttrObj == null){
                            String nameWithPath = attrSpecName+"/"+pimAttrName;
                            if(log.isDebugEnabled()){
                                log.debug("nameWithPath value is "+nameWithPath);
                            }
                            pimAttrObj = pimAttributeDao.getPIMAttributeByName(nameWithPath);
                        }
                        //in case of a new attribute create one
                        if(pimAttrObj == null){
                            pimAttrObj = new PIMAttribute();
                            pimAttrObj.setName(pimAttrName);
                            pimAttrObj.setDescr(pimAttrName);
                            pimAttrObj.setIsPimAttrDisplayble(Constants.FLAG_YES);
                            this.setAuditInfo(systemUser, pimAttrObj);
                            if(log.isDebugEnabled()){
                                log.debug("Creating anew pim_attribute since not already existing "+pimAttrObj);
                            }
                            pimAttrObj = (PIMAttribute) pimAttributeDao.save(pimAttrObj);
                        }
                        pimAttrId = pimAttrObj.getPimAttrId();
                        if(log.isDebugEnabled()){
                            log.debug("processing pim attribute Id  "+pimAttrId+" and pim attr name "+pimAttrName);
                        }
                        //check if the attribute is excluded
                        isExcluded = pimAttributeExclusionListManager.isAttributeExcluded(pimAttrId);
                        if(isExcluded){
                            if(log.isInfoEnabled()){
                                log.info("pim attribute "+pimAttrId+" is excluded ");
                            }
                            continue;
                        }

                        Map<Long,List<CarsPIMAttributeMapping>> carsMap = getCarPIMMap(carMappingList);
                        //if mapping exists in cars pim mapping table
                        if(carsMap!=null && carsMap.size()>0 && carsMap.containsKey(pimAttrId)){
                        List<CarsPIMAttributeMapping> carPimAttrList = carsMap.get(pimAttrId);
                            if(carPimAttrList!=null){
                                for(CarsPIMAttributeMapping carPIMAttributeMapping : carPimAttrList){
                                    attrId = carPIMAttributeMapping.getId().getAttrId();
                                    carTableName = carPIMAttributeMapping.getCarTableNm();
                                    Long lAttrId = new Long(attrId);
                                    if(log.isDebugEnabled()){
                                        log.debug("car pim attribute mapping found for pim_attribute id "+pimAttrId);
                                    }
                                    //Business requirement is to avoid updating car level attributes with Item_UDA_Spec data for sku type call
                                    if(carTableName.equalsIgnoreCase(Constants.CAR_ATTRIBUTE)){
                                        if(isCarAttributeExits(car,lAttrId)){
                                            updateAttributeValue(Constants.CAR_ATTRIBUTE, Constants.ATTR_VALUE_COLUMN, pimAttrId, pimAttrValue,lAttrId,car,patternVS,null,systemUser);
                                        }else{
                                            addCarAttribute(pimAttrValue,lAttrId,car,systemUser);
                                        }
                                    }else{
                                        if(isCarAttributeExits(car,lAttrId)){
                                            updateAttributeValue(Constants.CAR_ATTRIBUTE, Constants.ATTR_VALUE_COLUMN, pimAttrId, pimAttrValue,lAttrId,car,patternVS,null,systemUser);
                                        }else{
                                            addCarAttribute(pimAttrValue,lAttrId,car,systemUser);
                                        }
                                    }

                                    mergeAttributeLookUpValues(attributeManager.getAttribute(lAttrId),pimAttrValue,systemUser);
                                }
                            }

                        }else if(isGlobalAttribute(pimAttrId,globalAttrList)){//check if attribute is part of global attribute list
                            if(log.isInfoEnabled()){
                                log.info("global attribute mapping found for pim_attribute id "+pimAttrId);
                            }
                            for(CarsPIMGlobalAttributeMapping glblMapping : globalAttrList){
                                if(glblMapping.getPimAttrId()==pimAttrId){
                                    String tabName = glblMapping.getCarTableNm();
                                    String colName = glblMapping.getColumnNm();
                                    Long cAttrId = glblMapping.getCarAttrId();
                                    //Business requirement is to avoid updating car level attributes with Item_UDA_Spec data for sku type call
                                    if(tabName.equalsIgnoreCase(Constants.CAR_ATTRIBUTE)){
                                        if(isCarAttributeExits(car,cAttrId)){
                                            updateAttributeValue(tabName, colName, pimAttrId, pimAttrValue,cAttrId,car,patternVS,null,systemUser);
                                        }else{
                                            addCarAttribute(pimAttrValue,cAttrId,car,systemUser);
                                        }
                                    }else if((tabName.equalsIgnoreCase(Constants.VENDOR_STYLE) && patternVS!=null)){
                                        updateAttributeValue(tabName, colName, pimAttrId, pimAttrValue,cAttrId,car,patternVS,null,systemUser);
                                    }else if(tabName.equalsIgnoreCase(Constants.BOTH)){
                                        if(isCarAttributeExits(car,cAttrId)){
                                            updateAttributeValue(Constants.CAR_ATTRIBUTE, Constants.ATTR_VALUE_COLUMN, pimAttrId, pimAttrValue,cAttrId,car,patternVS,null,systemUser);
                                        }else{
                                            addCarAttribute(pimAttrValue,cAttrId,car,systemUser);
                                        }
                                    }

                                    if(cAttrId!=null){
                                        mergeAttributeLookUpValues(attributeManager.getAttribute(cAttrId),pimAttrValue,systemUser);
                                    }
                                }
                            }
                        }else{//new attribute
                            if(log.isDebugEnabled()){
                                log.debug("No mapping found for pim_attribute id "+pimAttrId+" inserting a new attribute in VENDORSTYLE_PIM_ATTRIBUTE table with value "+pimAttrValue);
                                log.debug("For vendor style id "+patternVS.getVendorStyleId()+" with value  "+pimAttrValue);
                            }
                            VendorStylePIMAttribute vsPimAttr = new VendorStylePIMAttribute();
                            VendorStylePIMAttributeId vsPimAttrId = new VendorStylePIMAttributeId(patternVS.getVendorStyleId(),pimAttrObj.getPimAttrId());
                            if(log.isInfoEnabled()){
                                log.info("vendor style PIM attribute Id being inserted is "+vsPimAttrId.getPimAttrId());
                            }
                            vsPimAttr.setId(vsPimAttrId);
                            vsPimAttr.setPimAttribute(pimAttrObj);
                            vsPimAttr.setValue(pimAttrValue);
                            vsPimAttr.setVendorStyle(patternVS);
                            this.setAuditInfo(systemUser, vsPimAttr);
                            vsPimAttrList.add(vsPimAttr);
                        }

                    }
                    //persist into vendorstyle_pim_attribute table
                    if(vsPimAttrList!=null && vsPimAttrList.size()>0){
                        if(log.isDebugEnabled()){
                            log.debug("Saving "+ vsPimAttrList.size() +" vendor style pim attributes ");
                        }
                        
                        patternVS.getVendorStylePIMAttribute().addAll(vsPimAttrList);
                        pimAttributeDao.saveVendoorStyle(patternVS);
                    }
                }else{
                    if(log.isErrorEnabled()){
                        log.error("No Additional attributes from PIM to process  ");
                    }
                }
            }
        }
    }

	/**
	 * This method syncs the few attributes for BCG grouping
	 * @param car
	 * @param attributeMap
	 * @param Systemuser
	 * @throws Exception 
	 */
	private void syncAdditionalPIMAttributesForBCGGroup(Car car, Map<String,String> attributeMap, User systemUser) throws Exception {
	    for (Map.Entry<String,String> mapEntry : attributeMap.entrySet()) {
            Attribute attr = null;
	        if ("CarBrand".equals(mapEntry.getKey())) {
	            Set<CarAttribute> cAttrs = car.getCarAttributes();
	            for (CarAttribute ca : cAttrs) {
	                if ("Brand".equals(ca.getAttribute().getBlueMartiniAttribute())) {
	                    attr = ca.getAttribute();
	                    break;
	                }
	            }
	            //attr = carManager.getAttributeByName("GBL_BRAND");
	        }
	        else if ("Outfit_Navigation".equals(mapEntry.getKey())) {
	            attr = carManager.getAttributeByName("GBL_OutfitNav");
	        }
	        
	        if (attr==null) {
	            continue;  // it should exist.  But if it doesn't, nothing to update.
	        }
	        
	        long attrId = attr.getAttributeId();
	        if(isCarAttributeExits(car,attrId)){
                updateCarAttribute(car, attrId, mapEntry.getValue(), systemUser);
            }else{
                addCarAttribute(mapEntry.getValue(),attrId,car,systemUser);
            }
	    }
	}
	
    /**
     * Method to create a new car sku attribute.
     *
     * @param pimAttrValue
     * @param cAttrId
     * @param vSku
     * @param systemUser
     */
    private void addCarSkuAttribute(String pimAttrValue, Long cAttrId,
            VendorSku vSku, User systemUser) {
        // TODO Auto-generated method stub
        if(log.isDebugEnabled()){
            log.debug("No mapping found for car Sku Attribute for attr id "+cAttrId+"  belk UPC "+vSku.getBelkUpc()+" with value "+pimAttrValue);
        }
        CarSkuAttribute carSkuAttr=new CarSkuAttribute();
        Attribute attribute = attributeManager.getAttribute(cAttrId);
        carSkuAttr.setAttribute(attribute);
        carSkuAttr.setAttrValue(pimAttrValue);
        carSkuAttr.setAuditInfo(systemUser);
        carSkuAttr.setVendorSku(vSku);
        carManager.createCarSkuAttribute(carSkuAttr);
        //vSku.getCarSkuAttributes().add(carSkuAttr);
        //pimAttributeDao.saveVendorSku(vSku);
        if(log.isDebugEnabled()){
            log.debug("after saving car sku attribute");
        }
    }


    /**
     * Method to create a new Car  Attribute.
     * @param pimAttrValue
     * @param cAttrId
     * @param car
     * @param systemUser
     */
    private void addCarAttribute(String pimAttrValue, Long cAttrId, Car car,
            User systemUser) {
        if(log.isDebugEnabled()){
            log.debug("No mapping found for car Attribute for attr id "+cAttrId+"  Car Id "+car.getCarId()+" with value "+pimAttrValue);
        }
        CarAttribute carAttribute = new CarAttribute();
        Attribute attribute = attributeManager.getAttribute(cAttrId);
        AttributeValueProcessStatus checkRequired = carLookupManager.getAttributeValueProcessStatus(AttributeValueProcessStatus.CHECK_REQUIRED);
        AttributeValueProcessStatus noCheckRequired = carLookupManager.getAttributeValueProcessStatus(AttributeValueProcessStatus.NO_CHECK_REQUIRED);
        carAttribute.setAttribute(attribute);
        carAttribute.setCar(car);
        if (attribute.getAttributeConfig().getHtmlDisplayType().isAutocomplete()) {
            carAttribute.setAttributeValueProcessStatus(checkRequired) ;
        } else {
            carAttribute.setAttributeValueProcessStatus(noCheckRequired) ;
        }

        // Setting to blank for now. Need to get it from the association
        carAttribute.setAttrValue(pimAttrValue);
        carAttribute.setDisplaySeq((short) 0);
        carAttribute.setHasChanged(Constants.FLAG_NO);
        carAttribute.setIsChangeRequired(Constants.FLAG_YES);
        carAttribute.setStatusCd(Status.ACTIVE);
        carAttribute.setAuditInfo(systemUser);
        carManager.createCarAttribute(carAttribute);
        if(log.isDebugEnabled()){
            log.debug("after saving car attribute ");
        }
    }


    /**
     * Method to persist the data with the new attribute values obtained from SMART.
     * This method will be invoked from update Item flow process only. Attributes
     * which got modified only will be updated.
     *
     */
    public void syncAdditionalPIMAttributesForUpdate(Car car,Map<Long, List<AttributeData>> styleAttrMap,String type,User systemUser)throws Exception{
        long pimAttrId;
        long attrId;
        String pimAttrName;
        String attrSpecName;
        String pimAttrValue;
        PIMAttribute pimAttrObj;
        String carTableName;
        long deptId = car.getDepartment().getDeptId();
        VendorStyle cStyle = car.getVendorStyle();

        long classId = cStyle.getClassification().getClassId();
        long prodTypeId = cStyle.getProductType().getProductTypeId();
        boolean isExcluded = false;
        long vsId;
        List<AttributeData> pimAttrList = new ArrayList<AttributeData>();
        if(log.isInfoEnabled()){
            log.info("syncing additional attributes for car Id "+car.getCarId()+"with classId "+classId+"department id "+deptId+"productTypeId "+prodTypeId);
        }
        //retrieve car pim attribute mapping details
        List<CarsPIMAttributeMapping>carMappingList = carsPIMAttributeMappingManager.getCarsPIMMappingDetails(classId, deptId, prodTypeId);
        //Retrieve all global attributes
        List<CarsPIMGlobalAttributeMapping> globalAttrList = carsPIMGlobalAttributeMappingManager.getAllCarsPimGlobalAttributes();

        if(styleAttrMap!=null && styleAttrMap.size()>0){
            for(Map.Entry<Long,List<AttributeData>> mapEntry : styleAttrMap.entrySet()){
                List<VendorStylePIMAttribute> vsPimAttrList = new ArrayList<VendorStylePIMAttribute>();
                Set<VendorSkuPIMAttribute> vSkuPimAttrSet = new HashSet<VendorSkuPIMAttribute>();
                pimAttrList = mapEntry.getValue();
                vsId = mapEntry.getKey();
                VendorStyle vStyle = getVendorStyleByOrin(new Long(vsId));
                VendorSku vSku = new VendorSku();
                if(type.equalsIgnoreCase("sku")){
                    vSku = getVendorSkuByOrin(new Long(vsId));
                }else if(type.equalsIgnoreCase("pack")){
                    vSku = getVendorSkuByOrin(new Long(vsId));
                    vStyle = vSku.getVendorStyle();
                }
                if(log.isInfoEnabled()){
                    log.info("processing for vstyle/sku id "+vsId);
                }
                if(pimAttrList!=null && pimAttrList.size()>0){
                    for(AttributeData entry : pimAttrList){
                        pimAttrObj = new PIMAttribute();
                        pimAttrName = entry.getAttributeName();
                        pimAttrValue = entry.getAttributeValue();
                        attrSpecName = entry.getQName().toString();
                        pimAttrObj = pimAttributeDao.getPIMAttributeByName(pimAttrName);
                        if(pimAttrObj == null){
                            String nameWithPath = attrSpecName+"/"+pimAttrName;
                            if(log.isDebugEnabled()){
                                log.debug("nameWithPath value is "+nameWithPath);
                            }
                            pimAttrObj = pimAttributeDao.getPIMAttributeByName(nameWithPath);
                        }
                        //in case of a new attribute create one
                        if(pimAttrObj == null){
                            pimAttrObj = new PIMAttribute();
                            pimAttrObj.setName(pimAttrName);
                            pimAttrObj.setDescr(pimAttrName);
                            pimAttrObj.setIsPimAttrDisplayble(Constants.FLAG_YES);
                            this.setAuditInfo(systemUser, pimAttrObj);
                            if(log.isDebugEnabled()){
                                log.debug("Creating anew pim_attribute since not already existing "+pimAttrObj);
                            }
                            pimAttrObj = (PIMAttribute) pimAttributeDao.save(pimAttrObj);
                        }
                        pimAttrId = pimAttrObj.getPimAttrId();
                        if(log.isDebugEnabled()){
                            log.debug("processing pim attribute Id  "+pimAttrId+" and pim attr name "+pimAttrName);
                        }
                        //check if the attribute is excluded
                        isExcluded = pimAttributeExclusionListManager.isAttributeExcluded(pimAttrId);
                        if(isExcluded){
                            if(log.isInfoEnabled()){
                                log.info("pim attribute "+pimAttrId+" is excluded ");
                            }
                            continue;
                        }

                        Map<Long,List<CarsPIMAttributeMapping>> carsMap = getCarPIMMap(carMappingList);
                        //if mapping exists in cars pim mapping table
                        if(carsMap!=null && carsMap.size()>0 && carsMap.containsKey(pimAttrId)){
                        List<CarsPIMAttributeMapping> carPimAttrList = carsMap.get(pimAttrId);
                            if(carPimAttrList!=null){
                                if (isPimBrandAttribute(pimAttrObj)) {
                                    pimAttrValue = encodeBrandWithSpecialCharacters(pimAttrValue);
                                }
                                for(CarsPIMAttributeMapping carPIMAttributeMapping : carPimAttrList){
                                    attrId = carPIMAttributeMapping.getId().getAttrId();
                                    carTableName = carPIMAttributeMapping.getCarTableNm();
                                    Long lAttrId = new Long(attrId);
                                    if(log.isDebugEnabled()){
                                        log.debug("car pim attribute mapping found for pim_attribute id "+pimAttrId);
                                    }
                                    //perform an update only if the value changed
                                    if(carTableName.equalsIgnoreCase(Constants.CAR_ATTRIBUTE) && isCarAttributeValueModified(car,lAttrId,pimAttrValue) && !(type.equalsIgnoreCase("sku") && attrSpecName.equals(Constants.ITEM_UDA_SPEC))){
                                        if(isCarAttributeExits(car,lAttrId)){
                                            updateAttributeValue(Constants.CAR_ATTRIBUTE, Constants.ATTR_VALUE_COLUMN, pimAttrId, pimAttrValue,lAttrId,car,vStyle,vSku,systemUser);
                                        }else{
                                            addCarAttribute(pimAttrValue,lAttrId,car,systemUser);
                                        }

                                    }else if(carTableName.equalsIgnoreCase(Constants.CAR_SKU_ATTRIBUTE) && vSku!=null && vSku.getCarSkuId()>0 && isCarSkuAttributeValueModified(vSku,lAttrId,pimAttrValue)){
                                        if(isCarSkuAttributeExits(vSku,lAttrId)){
                                            updateAttributeValue(Constants.CAR_SKU_ATTRIBUTE, Constants.ATTR_VALUE_COLUMN, pimAttrId, pimAttrValue,lAttrId,car,vStyle,vSku,systemUser);
                                        }else{
                                            addCarSkuAttribute(pimAttrValue,lAttrId,vSku,systemUser);
                                        }

                                    }else{
                                        if(type.equalsIgnoreCase("sku") && pimAttrValue==null){
                                            pimAttrValue = getExistingAttributeValue(car,lAttrId);
                                        }
                                        boolean isAttrModified = false;
                                        isAttrModified = isCarAttributeValueModified(car,lAttrId,pimAttrValue);
                                        if(isAttrModified && !(type.equalsIgnoreCase("sku") && attrSpecName.equals(Constants.ITEM_UDA_SPEC))){
                                            if(isCarAttributeExits(car,lAttrId)){
                                                updateAttributeValue(Constants.CAR_ATTRIBUTE, Constants.ATTR_VALUE_COLUMN, pimAttrId, pimAttrValue,lAttrId,car,vStyle,vSku,systemUser);
                                            }else{
                                                addCarAttribute(pimAttrValue,lAttrId,car,systemUser);
                                            }
                                        }

                                        //call to update zbm & iph attributes at sku level as well in case of BOTH table name
                                        //these spec's are ignored during the sku call
                                        if(type.equalsIgnoreCase("style") && (attrSpecName.startsWith("sec_spec")||attrSpecName.startsWith("zbm_sec_spec")) && isAttrModified){
                                            upsertAttributeValueForAllSkus(car,lAttrId,pimAttrValue,systemUser);
                                        }

                                        if(vSku!=null && vSku.getCarSkuId()>0 && isCarSkuAttributeValueModified(vSku,lAttrId,pimAttrValue)){
                                            if(isCarSkuAttributeExits(vSku,lAttrId)){
                                                updateAttributeValue(Constants.CAR_SKU_ATTRIBUTE, Constants.ATTR_VALUE_COLUMN, pimAttrId, pimAttrValue,lAttrId,car,vStyle,vSku,systemUser);
                                            }else{
                                                addCarSkuAttribute(pimAttrValue,lAttrId,vSku,systemUser);
                                            }

                                        }

                                    }
                                    mergeAttributeLookUpValues(attributeManager.getAttribute(lAttrId),pimAttrValue,systemUser);
                                }
                            }

                        }else if(isGlobalAttribute(pimAttrId,globalAttrList)){//check if attribute is part of global attribute list
                            if(log.isInfoEnabled()){
                                log.info("global attribute mapping found for pim_attribute id from update  "+pimAttrId);
                            }
                            for(CarsPIMGlobalAttributeMapping glblMapping : globalAttrList){
                                if(glblMapping.getPimAttrId()==pimAttrId){
                                    String tabName = glblMapping.getCarTableNm();
                                    String colName = glblMapping.getColumnNm();
                                    Long cAttrId = glblMapping.getCarAttrId();
                                  //perform an update only if the value changed
                                    if(tabName.equalsIgnoreCase(Constants.CAR_ATTRIBUTE) && isCarAttributeValueModified(car,cAttrId,pimAttrValue) && !(type.equalsIgnoreCase("sku") && attrSpecName.equals(Constants.ITEM_UDA_SPEC))){
                                        if(isCarAttributeExits(car,cAttrId)){
                                            updateAttributeValue(tabName, colName, pimAttrId, pimAttrValue,cAttrId,car,vStyle,vSku,systemUser);
                                        }else{
                                            addCarAttribute(pimAttrValue,cAttrId,car,systemUser);
                                        }

                                    }else if(tabName.equalsIgnoreCase(Constants.CAR_SKU_ATTRIBUTE) && isCarSkuAttributeValueModified(vSku,cAttrId,pimAttrValue)){
                                        if(isCarSkuAttributeValueModified(vSku,cAttrId,pimAttrValue)){
                                            updateAttributeValue(tabName, colName, pimAttrId, pimAttrValue,cAttrId,car,vStyle,vSku,systemUser);
                                        }else{
                                            addCarSkuAttribute(pimAttrValue,cAttrId,vSku,systemUser);
                                        }

                                    }else if((tabName.equalsIgnoreCase(Constants.VENDOR_STYLE) && isStyleAttributeValueModified(vStyle,pimAttrValue))||
                                            (tabName.equalsIgnoreCase(Constants.VENDOR_SKU) && vSku!=null && vSku.getCarSkuId()>0 && isSkuAttributeValueModified(vSku,pimAttrValue,colName) )){
                                        updateAttributeValue(tabName, colName, pimAttrId, pimAttrValue,cAttrId,car,vStyle,vSku,systemUser);
                                    }else if(tabName.equalsIgnoreCase(Constants.BOTH)){
                                        if(type.equalsIgnoreCase("sku") && pimAttrValue==null){
                                            pimAttrValue = getExistingAttributeValue(car,cAttrId);
                                        }
                                        if(!(type.equalsIgnoreCase("sku") && attrSpecName.equals(Constants.ITEM_UDA_SPEC))){
                                            if(isCarAttributeExits(car,cAttrId)){
                                                updateAttributeValue(Constants.CAR_ATTRIBUTE, Constants.ATTR_VALUE_COLUMN, pimAttrId, pimAttrValue,cAttrId,car,vStyle,vSku,systemUser);
                                            }else{
                                                addCarAttribute(pimAttrValue,cAttrId,car,systemUser);
                                            }
                                        }

                                        //call to update zbm & iph attributes at sku level as well in case of BOTH table name
                                        //these spec's are ignored during the sku call
                                        if(type.equalsIgnoreCase("style") && (attrSpecName.startsWith("sec_spec")||attrSpecName.startsWith("zbm_sec_spec"))){
                                            upsertAttributeValueForAllSkus(car,cAttrId,pimAttrValue,systemUser);
                                        }

                                        if(vSku!=null && vSku.getCarSkuId()>0){
                                            if(isCarSkuAttributeExits(vSku,cAttrId)){
                                                updateAttributeValue(Constants.CAR_SKU_ATTRIBUTE, Constants.ATTR_VALUE_COLUMN, pimAttrId, pimAttrValue,cAttrId,car,vStyle,vSku,systemUser);
                                            }else{
                                                addCarSkuAttribute(pimAttrValue,cAttrId,vSku,systemUser);
                                            }

                                        }
                                    }

                                    if(cAttrId!=null){
                                        mergeAttributeLookUpValues(attributeManager.getAttribute(cAttrId),pimAttrValue,systemUser);
                                    }
                                }

                            }
                        }else{//new attribute

                            if(type.equalsIgnoreCase("style") || type.equalsIgnoreCase("pack")){
                                if(log.isInfoEnabled()){
                                    log.info("No mapping found for pim_attribute id "+pimAttrId+" inserting a new attribute in VENDORSTYLE_PIM_ATTRIBUTE table");
                                }

                                VendorStylePIMAttributeId vsPimAttrId = new VendorStylePIMAttributeId(vStyle.getVendorStyleId(),pimAttrObj.getPimAttrId());
                                Set<VendorStylePIMAttribute> vsPIMAttrSet = vStyle.getVendorStylePIMAttribute();
                                VendorStylePIMAttribute vStylePIMAttr = vspAttributeExistsAndModified(vsPIMAttrSet,vsPimAttrId,pimAttrValue);
                              //perform an update only if the value changed
                                if(vsPIMAttrSet!=null && !vsPIMAttrSet.isEmpty() && vStylePIMAttr!=null){
                                    if(log.isDebugEnabled()){
                                        log.debug("VendorStylePIMAttribute exists "+vsPimAttrId +" hence updating the attribute value with "+pimAttrValue);
                                    }
                                    vStyle.getVendorStylePIMAttribute().remove(vStylePIMAttr);
                                    vStylePIMAttr.setValue(pimAttrValue);
                                    vStylePIMAttr.setUpdatedDate(new Date());
                                    vStylePIMAttr.setUpdatedBy(systemUser.getUsername());
                                    vStyle.getVendorStylePIMAttribute().add(vStylePIMAttr);
                                    pimAttributeDao.saveVendoorStyle(vStyle);
                                }else{//Insert into vendor style PIM attributes if its a new attribute
                                    if(log.isDebugEnabled()){
                                        log.debug("New VendorStylePIMAttribute coming from update process "+pimAttrValue);
                                    }
                                    VendorStylePIMAttribute vsPimAttr = new VendorStylePIMAttribute();
                                    vsPimAttr.setId(vsPimAttrId);
                                    vsPimAttr.setPimAttribute(pimAttrObj);
                                    vsPimAttr.setValue(pimAttrValue);
                                    vsPimAttr.setVendorStyle(vStyle);
                                    this.setAuditInfo(systemUser, vsPimAttr);
                                    vsPimAttrList.add(vsPimAttr);
                                }

                            }else if(type.equalsIgnoreCase("sku")){
                                if(log.isInfoEnabled()){
                                    log.info("No mapping found for pim_attribute id "+pimAttrId+" inserting a new attribute in VENDORSKU_PIM_ATTRIBUTE table");
                                }
                                Set<VendorSkuPIMAttribute> existingPIMAttrSet = vSku.getSkuPIMAttributes();
                                VendorSkuPIMAttribute vSkuPIMAttr = vskuPAttributeExistsAndModified(existingPIMAttrSet,vSku.getCarSkuId(),pimAttrId,pimAttrValue);
                                if(existingPIMAttrSet!=null && !existingPIMAttrSet.isEmpty() && vSkuPIMAttr!=null){
                                    if(log.isDebugEnabled()){
                                        log.debug("VendorSkuPIMAttribute exists for carSkuId"+vSku.getCarSkuId() +" and PIMAttrId "+pimAttrId+" hence updating the attribute value with "+pimAttrValue);
                                    }
                                    existingPIMAttrSet.remove(vSkuPIMAttr);
                                    vSkuPIMAttr.setAttributeValue(pimAttrValue);
                                    vSkuPIMAttr.setUpdatedBy(systemUser.getUsername());
                                    vSkuPIMAttr.setUpdatedDate(new Date());
                                    vSku.getSkuPIMAttributes().add(vSkuPIMAttr);
                                    pimAttributeDao.saveVendorSku(vSku);
                                }else{
                                    if(log.isInfoEnabled()){
                                        log.info("New VendorSkuPIMAttribute coming from update process "+pimAttrValue);
                                    }
                                    VendorSkuPIMAttribute vSkuPimAttr = new VendorSkuPIMAttribute();
                                    VendorSkuPIMAttributeId vSkuPimAttrId = new VendorSkuPIMAttributeId(vSku.getCarSkuId(),pimAttrObj.getPimAttrId());
                                    vSkuPimAttr.setId(vSkuPimAttrId);
                                    vSkuPimAttr.setPimAttributeDetails(pimAttrObj);
                                    vSkuPimAttr.setVendorSkuDetails(vSku);
                                    vSkuPimAttr.setAttributeValue(pimAttrValue);
                                    this.setAuditInfo(systemUser, vSkuPimAttr);
                                    vSkuPimAttrSet.add(vSkuPimAttr);
                                }

                            }
                        }

                    }
                    //persist into vendorstyle_pim_attribute table
                    if(vsPimAttrList!=null && vsPimAttrList.size()>0){
                        if(log.isDebugEnabled()){
                            log.debug("Saving "+ vsPimAttrList.size() +" vendor style pim attributes ");
                        }
                        vStyle.getVendorStylePIMAttribute().addAll(vsPimAttrList);
                        pimAttributeDao.saveVendoorStyle(vStyle);
                    }
                    //persist into vendorsku_pim_attribute table
                    if(vSkuPimAttrSet!=null && vSkuPimAttrSet.size()>0){
                        if(log.isDebugEnabled()){
                            log.debug("Saving "+ vSkuPimAttrSet.size() +" vendor sku pim attributes ");
                        }
                        vSku.getSkuPIMAttributes().addAll(vSkuPimAttrSet);
                        pimAttributeDao.saveVendorSku(vSku);
                    }
                }else{
                    if(log.isErrorEnabled()){
                        log.error("No Additional attributes from PIM to process  ");
                    }
                }
            }
        }
    }

    /**
     * Method to return an object if a VendorStylePIMAttribute already exists and the attribute value is
     *  different from the one returned by web service.
     *
     * @param vsPIMAttrSet
     * @param vsPimAttrId
     * @param pimAttrValue
     * @return
     */
	private VendorStylePIMAttribute vspAttributeExistsAndModified(
            Set<VendorStylePIMAttribute> vsPIMAttrSet,
            VendorStylePIMAttributeId vsPimAttrId, String pimAttrValue) {
	    VendorStylePIMAttribute vspObject = null;
	    for (VendorStylePIMAttribute vsp : vsPIMAttrSet) {
            if (vsp.getId().equals(vsPimAttrId)) {
                String existingVal = vsp.getValue();
                if(pimAttrValue==null && existingVal!=null){
                    vspObject = vsp;
                }else if(pimAttrValue!=null && existingVal == null){
                    vspObject = vsp;
                }else if(!pimAttrValue.equalsIgnoreCase(existingVal)){
                    vspObject = vsp;
                }
                return vspObject;
            }
        }
        return vspObject;
    }

	/**
	 * Method to return an object if a VendorSkuPIMAttribute already exists and the attribute value is
     *  different from the one returned by web service.
     *
	 * @param vsPIMAttrSet
	 * @param carSkuId
	 * @param pimAttrId
	 * @param pimAttrValue
	 * @return
	 */
	private VendorSkuPIMAttribute vskuPAttributeExistsAndModified(
            Set<VendorSkuPIMAttribute> vsPIMAttrSet,
            long carSkuId, long pimAttrId,String pimAttrValue) {
	    VendorSkuPIMAttribute vskuObject = null;
        for (VendorSkuPIMAttribute vsp : vsPIMAttrSet) {
            if (vsp.getPimAttributeDetails().getPimAttrId() == pimAttrId && vsp.getVendorSkuDetails().getCarSkuId() == carSkuId) {
                String existingVal = vsp.getAttributeValue();
                if(pimAttrValue==null && existingVal!=null){
                    vskuObject = vsp;
                }else if(pimAttrValue!=null && existingVal == null){
                    vskuObject = vsp;
                }else if(!pimAttrValue.equalsIgnoreCase(existingVal)){
                    vskuObject = vsp;
                }
                return vskuObject;
            }
        }
        return vskuObject;
    }


    /**
	 * Method to check if the attribute is part of the global attribute list.
	 *
	 * @param attrId
	 * @param globalAttrList
	 * @return
	 */
	private boolean isGlobalAttribute(long attrId,List<CarsPIMGlobalAttributeMapping>globalAttrList){
	    boolean isglobalAttr = false;
	    if(globalAttrList == null){
	        return isglobalAttr;
	    }
	    for(CarsPIMGlobalAttributeMapping gblMapping : globalAttrList){
	        if(gblMapping.getPimAttrId() == attrId){
	            isglobalAttr = true;
	            break;
	        }
	    }

	    return isglobalAttr;
	}

	/**
	 * Method to generate a Map of pimAttribute Id and corresponding List of CarsPIMAttributeMapping objects.
	 * Purpose of this is to reduce number of iterations which checking to see if pim_attr_id coming from
	 *  service is already mapped.
	 *
	 * @param carMappingList
	 * @return
	 */
	private Map<Long,List<CarsPIMAttributeMapping>> getCarPIMMap(List<CarsPIMAttributeMapping> carMappingList){
	    if(log.isInfoEnabled()){
            log.info("Entered getCarPIMMap method ");
        }
	    Map<Long,List<CarsPIMAttributeMapping>> carPIMMap = new HashMap<Long,List<CarsPIMAttributeMapping>>();
	    for(CarsPIMAttributeMapping carMap : carMappingList){
	        List<CarsPIMAttributeMapping> list = new ArrayList<CarsPIMAttributeMapping>();
	        if(!carPIMMap.containsKey(carMap.getId().getPimAttrId())){
	            if(log.isDebugEnabled()){
	                log.debug("PIM attr Id does'nt exist in the map "+carMap.getId().getPimAttrId());
	            }
	            list.add(carMap);
	            carPIMMap.put(carMap.getId().getPimAttrId(), list);
	        }else{
	            if(log.isDebugEnabled()){
                    log.debug("PIM attr Id exists in the map "+carMap.getId().getPimAttrId());
                }
	            List<CarsPIMAttributeMapping> existingList = carPIMMap.get(carMap.getId().getPimAttrId());
	            existingList.add(carMap);
	            carPIMMap.put(carMap.getId().getPimAttrId(), existingList);
	        }
	    }
	    return carPIMMap;
	}


	/**
	 *
	 * Method to check if the attribute look up value already exists.
	 *
	 * @param attributeLookupValues
	 * @param attrValue
	 * @return
	 */
	public boolean isInLookUpValues(Set<AttributeLookupValue> attributeLookupValues, String attrValue) {
        boolean isInLookUpValues = false;
        for (AttributeLookupValue lValue : attributeLookupValues) {
            if (lValue.getValue().equals(attrValue)) {
                isInLookUpValues = true;
                break;
            }
        }
        if(log.isDebugEnabled()){
            log.debug("lookup value match found "+isInLookUpValues);
        }
        return isInLookUpValues;
    }

	/**
	 * Method to add a new lookup value to Attribute lookup table.
	 *
	 * @param attrObj
	 * @param attrValue
	 * @param user
	 * @throws Exception
	 */
	public void mergeAttributeLookUpValues(Attribute attrObj,String attrValue, User user)throws Exception{
	    try{

    	    if(attrObj!=null && attrValue!=null && !attrValue.isEmpty()){

        	    Set<AttributeLookupValue> lvSet = attrObj.getAttributeLookupValues();
                if(attrObj.getAttributeConfig().getHtmlDisplayType().getHtmlDisplayTypeCd().equals(Constants.CHECKBOX)){
                    String[] valArray = attrValue.split(",");
                    for(String value : valArray){
                        if(!value.equalsIgnoreCase(Constants.NA)){
                            addLookUpValue(attrObj, value, user, lvSet);
                        }
                    }
                }else{
                    addLookUpValue(attrObj, attrValue, user, lvSet);
                }
    	    }
	    }catch(Exception e){
	        if(log.isErrorEnabled()){
	            log.error("Error while adding/merging attribute look up value ");
	        }
	    }
	}


    /**
     * Add look up value if not already existing.
     *
     * @param attrObj
     * @param attrValue
     * @param user
     * @param lvSet
     * @throws Exception
     */
    private void addLookUpValue(Attribute attrObj, String attrValue, User user,
            Set<AttributeLookupValue> lvSet) throws Exception{
        if(!isInLookUpValues(lvSet,attrValue)){
            if(log.isDebugEnabled()){
                log.debug("Merging lookup value for attribute "+ attrObj.getAttributeId()+" for value "+attrValue);
            }
            AttributeLookupValue attrValueObj = new AttributeLookupValue();
            attrValueObj.setValue(attrValue);
            attrValueObj.setAttribute(attrObj);
            attrValueObj.setStatusCd(Status.ACTIVE);
            attrValueObj.setDisplaySequence((short) 0);
            attrValueObj.setAuditInfo(user);
            if(log.isDebugEnabled()){
                log.debug("Attribute lookup value being added/merged is :::   "+attrValueObj);
            }
            attrObj.getAttributeLookupValues().add(attrValueObj);
            attributeManager.save(attrObj);
        }
    }

	@Override
	public VendorStyle getVendorStyleByOrin(Long orinNumber) {
	    return pimAttributeDao.getvendorStyle(orinNumber);
	}

	@Override
    public VendorStyle getLastUpdatedVendorStyleByOrin(Long orinNumber) {
        return pimAttributeDao.getLastUpdatedVendorStyle(orinNumber);
    }

	@Override
	public VendorSku getVendorSkuByOrin(Long orinNumber) {
	    return carManagementDao.getVendorSkuByOrin(orinNumber);
	}

    @Override
    public List<CarsPimItemUpdate> getAllUpdatedItemsFromPim(int batchsize) {
        return pimAttributeDao.getAllUpdatedItems(batchsize);
    }

    @Override
    public void markAllPimItemUpdatesAsProcessedByCar(
            List<CarsPimItemUpdate> items) {
        pimAttributeDao.setProcessedByCarFlagForAllItems(items, CarsPimItemUpdate.FLAG_YES);
    }

    @Override
    public void markAllPimItemUpdatesAsFailed(
            List<CarsPimItemUpdate> items) {
        pimAttributeDao.setProcessedByCarFlagForAllItems(items, CarsPimItemUpdate.FLAG_FAILED);
    }

    @Override
    public void resetFlagsForPimItemUpdate(CarsPimItemUpdate item) {
        pimAttributeDao.resetFlagsForPimItemUpdate(item);
    }

    @Override
    public int getProcessedPimItemUpdateCount() {
        return pimAttributeDao.getProcessedPimItemUpdateCount();
    }

    /**
     * Method to process the global attributes coming from new getItem web service
     *  and update the corresponding vendor sku object. We will be updating colorName,
     *  Size Name and Retail price from getItem.
     *
     * @param car
     * @param catalog
     */
    private void handleItemSkuSpec(Car car,ItemCatalogData catalog, User systemUser){
        ItemSkuSpecData skuSpec = catalog.getPimEntry().getEntries().getItemSkuSpecData();
        ItemCatalogSpecData catSpec = catalog.getPimEntry().getEntries().getItemCatalogSpecData();
        List<ItemDifferentiatorsData>diffData = skuSpec.getDifferentiators();
        ItemSupplierData supData = catSpec.getSupplierData();
        boolean isDirectShipFlag = supData.getIsDirectShipFlag();
        Long orinNum = catalog.getPimEntry().getItemHeaderInformation().getPrimaryKey();
        String dropShipFlag = Constants.VALUE_NO;

        if(isDirectShipFlag){
            dropShipFlag = Constants.VALUE_YES;
        }
        if(log.isDebugEnabled()){
            log.debug("handling item sku spec data for orin number "+orinNum);
        }

        String sizeName = null;
        String retailPrice = null;

        if(catSpec!=null){
            retailPrice = catSpec.getOriginalRetailPrice();
        }
        for(ItemDifferentiatorsData itemDiff : diffData){
            if(itemDiff.getDifferentiatorsType()!=null && Constants.SIZE.equalsIgnoreCase(itemDiff.getDifferentiatorsType())){
                sizeName = itemDiff.getVendorDescription();
            }

        }
        VendorSku vSku = carManagementDao.getVendorSkuByOrin(orinNum);

        if(vSku==null){
            if (log.isDebugEnabled()) {
                log.debug("No VendorSku found for OrinNumber : "
                        + orinNum);
            }
        }else{
            if(vSku.getCar().equals(car)){

                if(sizeName !=null){
                    vSku.setSizeName(sizeName);
                }
                if(retailPrice!=null){
                    try {
                        vSku.setRetailPrice(Double.parseDouble(retailPrice));
                    } catch (Exception e) {
                        vSku.setRetailPrice(0.0);
                        log.warn("Received a non numeric retail price - defaulting to 0.0");
                    }
                }
                if(log.isInfoEnabled()){
                    log.info("attributes values returned from service: size name "+ sizeName +" retail price "+ retailPrice +" for orin number "+orinNum);
                }
                updateDropshipAttributeForSku(vSku,dropShipFlag, systemUser);
            }
       }

    }

    /**
     * Method to process the global attributes coming from new getItem web service
     * and update the corresponding vendor sku and vendor style objects. We will be updating colorName,
     * Size Name and Retail price from getItem for vendor sku and vendor name for vendor style.
     * @param car
     * @param catalog
     */
    private void handleItemPackSpec(Car car, ItemCatalogData catalog) {
        ItemComplexPackSpecData packSpec = catalog.getPimEntry().getEntries().getItemComplexPackSpecData();
        ItemCatalogSpecData pSpec = catalog.getPimEntry().getEntries().getItemCatalogSpecData();
        List<ItemDifferentiatorsData> diffData = packSpec.getDifferentiators();
        Long orinNum = catalog.getPimEntry().getItemHeaderInformation().getPrimaryKey();
        String longDesc = pSpec.getDescriptionData().getLongDescription();
        if(log.isInfoEnabled()){
            log.info("handling item pack data for orin number "+orinNum);
        }

        String sizeName = null;
        String retailPrice = null;

        if(pSpec!=null){
            retailPrice = pSpec.getOriginalRetailPrice();
        }
        for(ItemDifferentiatorsData itemDiff : diffData){
            if(itemDiff.getDifferentiatorsType()!=null && Constants.SIZE.equalsIgnoreCase(itemDiff.getDifferentiatorsType())){
                sizeName = itemDiff.getVendorDescription();
            }

        }
        VendorSku vSku = carManagementDao.getVendorSkuByOrin(orinNum);

        if(vSku==null){
            if (log.isInfoEnabled()) {
                log.info("No VendorSku found for OrinNumber : "
                        + orinNum);
            }
        }else{
            if(vSku.getCar().equals(car)){

                if(sizeName !=null){
                    vSku.setSizeName(sizeName);
                }
                if(retailPrice!=null){
                    try {
                        vSku.setRetailPrice(Double.parseDouble(retailPrice));
                    } catch (Exception e) {
                        vSku.setRetailPrice(0.0);
                        log.warn("Received a non numeric retail price - defaulting to 0.0");
                    }
                }
                if(log.isInfoEnabled()){
                    log.info("attributes values returned from service: size name "+ sizeName +" retail price "+ retailPrice +" for orin number "+orinNum);
                }
                carManagementDao.save(vSku);
            }
       }
    }

    /**
     * Method to save the style color attributes retrieved from the getImage service.
     *
     * @param itemCatalogData
     * @param styleColorNum
     * @param user
     * @param car
     * @throws Exception
     */
    private Map<Long, List<AttributeData>> saveStyleColorAttributes(ItemCatalogData itemCatalogData,
            Long styleColorNum,User user, Car car) throws Exception {
        if(log.isInfoEnabled()){
            log.info("saving style color attributes for :  "+styleColorNum);
        }
        Map<Long, List<AttributeData>>styleColorMap = itemCatalogData.getPimEntry().getAllAttributes();
        Map<Long, List<AttributeData>>skuOrinMap = new HashMap<Long, List<AttributeData>>();
        Long vendorStyleOrin = itemCatalogData.getPimEntry().getEntries().getItemStyleColorSpecData().getStyleId();
        String colorCd = itemCatalogData.getPimEntry().getEntries().getItemStyleColorSpecData().getColorCode();
        VendorStyle vs = null;
        if(vendorStyleOrin!=null){
            vs = pimAttributeDao.getvendorStyle(vendorStyleOrin);
        }

        if(vs!=null && colorCd!=null){
            long vendorStyleId = vs.getVendorStyleId();
            List<VendorSku>vSkus = carManager.getSkusForStyleColorCd(vendorStyleId, colorCd,car);
            if(vSkus !=null){
                for(VendorSku vSku : vSkus){
                    //populate map with sku orin numbers to update sku level attributes(e.g. super color1,super color2 etc.)
                    skuOrinMap.put(vSku.getOrinNumber(), styleColorMap.get(styleColorNum));
                }
            }

        }else{
            if(log.isInfoEnabled()){
                log.info("No vendor style object found for the orin number "+vendorStyleOrin);
            }
        }
        return skuOrinMap;
    }

    /**
     * Method to save the pack color attributes retrieved from the getImage service.
     *
     * @param itemCatalogData
     * @param packColorNum
     * @param user
     * @param car
     * @throws Exception
     */
    private void savePackColorAttributes(ItemCatalogData itemCatalogData,
            Long packColorNum,User user,Car car) throws Exception {
        if(log.isInfoEnabled()){
            log.info("saving pack color attributes for :  "+packColorNum);
        }
        Map<Long, List<AttributeData>>packColorMap = itemCatalogData.getPimEntry().getAllAttributes();
        Map<Long, List<AttributeData>>skuOrinMap = new HashMap<Long, List<AttributeData>>();
        Long packOrinNumber = itemCatalogData.getPimEntry().getEntries().getItemPackColorSpecData().getPackId();

        skuOrinMap.put(packOrinNumber, packColorMap.get(packColorNum));

        if(skuOrinMap!=null && skuOrinMap.size()>0){
            //save attributes
            syncAdditionalPIMAttributes(car, skuOrinMap, Constants.SKU_TYPE, user);
        }
        if(log.isInfoEnabled()){
            log.info("No vendor sku object found for the pack color orin number "+packOrinNumber);
        }

    }

    /**
     * Method to return true if the car attribute value coming from new getItem service is
     * different from the value which is already stored.
     *
     * @param car
     * @param attrId
     * @param attrVal
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public boolean isCarAttributeValueModified(Car car,Long attrId,String attrVal)throws Exception{
        boolean isChanged = false;
        List<CarAttribute> carList = pimAttributeDao.getCarAttributesByAttrId(car, attrId.longValue());
        if(carList!=null && carList.size()>0){
            String existingVal = carList.get(0).getAttrValue();
            if(attrVal==null && existingVal!=null){
                isChanged = true;
            }else if(attrVal!=null && existingVal == null){
                isChanged = true;
            }else if(attrVal!=null && existingVal!=null && !attrVal.equalsIgnoreCase(existingVal)){
                isChanged = true;
            }
        }else{
            isChanged = true;
        }
        return isChanged;
    }

    /**
     * Method to return true if the car sku attribute value coming from new getItem service is
     * different from the value which is already stored.
     * @param vSku
     * @param attrId
     * @param attrVal
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public boolean isCarSkuAttributeValueModified(VendorSku vSku ,Long attrId,String attrVal)throws Exception{
        boolean isChanged = false;
        List<CarSkuAttribute> carList = pimAttributeDao.getCarSkuAttributesByAttrId(vSku, attrId.longValue());
        if(carList!=null && carList.size()>0){
            String existingVal = carList.get(0).getAttrValue();
            if(attrVal==null && existingVal!=null){
                isChanged = true;
            }else if(attrVal!=null && existingVal == null){
                isChanged = true;
            }else if(attrVal!=null && existingVal!=null && !attrVal.equalsIgnoreCase(existingVal)){
                isChanged = true;
            }
        }else{
            isChanged = true;
        }
        return isChanged;
    }

    /**
     * Method to return true if the style name value in VendorStyle coming from new getItem service is
     * different from the value which is already stored.
     * @param vStyle
     * @param attrVal
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public boolean isStyleAttributeValueModified(VendorStyle vStyle,String attrVal)throws Exception{
        boolean isChanged = false;
        String existingVal = null;
        if (vStyle!=null) {
            existingVal = vStyle.getVendorStyleName();
        }

        if(attrVal==null && existingVal!=null){
            isChanged = true;
        }else if(attrVal!=null && existingVal == null){
            isChanged = true;
        }else if(attrVal!=null && existingVal!=null && !attrVal.equalsIgnoreCase(existingVal)){
            isChanged = true;
        }

        return isChanged;
    }

    /**
     * Method to return true if the color name,size name values coming from new getItem service is
     * different from the value which is already stored.
     *
     * @param vSku
     * @param attrVal
     * @param colName
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public boolean isSkuAttributeValueModified(VendorSku vSku,String attrVal,String colName)throws Exception{
        boolean isChanged = false;

        String existingVal=null;
        if (vSku!=null) {
            if(Constants.COLOR_NAME_COLUMN.equalsIgnoreCase(colName)){
                existingVal = vSku.getColorName();
            }else if(Constants.SIZE_NAME_COLUMN.equalsIgnoreCase(colName)){
                existingVal = vSku.getSizeName();
            }
        }

        if(attrVal==null && existingVal!=null){
            isChanged = true;
        }else if(attrVal!=null && existingVal == null){
            isChanged = true;
        }else if(attrVal!=null && existingVal!=null && !attrVal.equalsIgnoreCase(existingVal)){
            isChanged = true;
        }

        return isChanged;
    }

    /**
     * Method to check if a mapping exists for given car and Attribute Id.
     * @param car
     * @param cAttrId
     * @return
     * @throws Exception
     */
    private boolean isCarAttributeExits(Car car, Long cAttrId) throws Exception {
        // TODO Auto-generated method stub
        boolean exists = false;
        List<CarAttribute> carList = pimAttributeDao.getCarAttributesByAttrId(car, cAttrId.longValue());
        if(carList!=null && carList.size()>0){
            exists = true;
        }
        return exists;
    }

    /**
     * Method to check if a mapping exists for a given Vendor Sku and Attribute Id.
     * @param vSku
     * @param cAttrId
     * @return
     * @throws Exception
     */
    private boolean isCarSkuAttributeExits(VendorSku vSku, Long cAttrId) throws Exception {
        // TODO Auto-generated method stub
        boolean exists = false;
        List<CarSkuAttribute> carList = pimAttributeDao.getCarSkuAttributesByAttrId(vSku, cAttrId.longValue());
        if(carList!=null && carList.size()>0){
            exists = true;
        }
        return exists;
    }

    /**
     * For the sku items, only dropship & onlineOnly attribute will be updated.
     *
     * @param sku, the sku to update
     * @param newDropshipValue, the new value to set
     * @param systemUser, for auditing purposes
     */
    public boolean updateDropshipAttributeForSku(VendorSku sku, String newValue, User systemUser) {
        try {
            if (log.isInfoEnabled()) {
                log.info("Setting dropship/onlineOnly attribute for pre-cutover car's sku orin " +
                        sku.getOrinNumber() + " to value " + newValue);
            }
            Attribute dropshipAttr = carManager.getAttributeByName(Constants.IS_DROPSHIP);
            List<CarSkuAttribute> dropshipCarSkuAttributes = pimAttributeDao.getCarSkuAttributesByAttrId(sku, dropshipAttr.getAttributeId());
            if (dropshipCarSkuAttributes != null && !dropshipCarSkuAttributes.isEmpty()) {
                CarSkuAttribute attribute = dropshipCarSkuAttributes.get(0);
                attribute.setAttrValue(newValue);
                this.setAuditInfo(systemUser, attribute);
                carManager.save(attribute);
            } else {
                addCarSkuAttribute(newValue,dropshipAttr.getAttributeId(),sku,systemUser);
            }

            Attribute onlineOnlyAttr = carManager.getAttributeByName(Constants.SDF_ONLINE_ONLY);
            List<CarSkuAttribute> onlineCarSkuAttributes = pimAttributeDao.getCarSkuAttributesByAttrId(sku, onlineOnlyAttr.getAttributeId());
            if (onlineCarSkuAttributes != null && !onlineCarSkuAttributes.isEmpty()) {
                CarSkuAttribute attribute = onlineCarSkuAttributes.get(0);
                attribute.setAttrValue(newValue);
                this.setAuditInfo(systemUser, attribute);
                carManager.save(attribute);
            } else {
                addCarSkuAttribute(newValue,onlineOnlyAttr.getAttributeId(),sku,systemUser);
            }
        }
        catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Failed to update Dropship & OnlineOnly attributes for sku upc: " + sku.getBelkUpc());
            }
            return false;
        }
        return true;
    }

    /**
     * Method to return existing value of the car attribute.
     *
     * @param car
     * @param attrId
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public String getExistingAttributeValue(Car car,Long attrId)throws Exception{
        String existingVal = null;
        List<CarAttribute> carList = pimAttributeDao.getCarAttributesByAttrId(car, attrId.longValue());
        if(carList!=null && carList.size()>0){
            existingVal = carList.get(0).getAttrValue();
        }
        return existingVal;
    }

    /**
     * Method to save car PET reopen details.
     * This data will be save for STYLE,STYLE_COLOR,PACK_COLOR,PACK scenarios.
     *
     * @param orin
     * @param vendorNumber
     * @param vendorStyleNumber
     * @param petCreateTimestamp
     * @param format
     * @throws Exception
     */
    public void savePetReopenDetails(Long orin,String vendorNumber,String vendorStyleNumber,String petCreateTimestamp,String format,String type) throws Exception{
        if(format==null || petCreateTimestamp==null){
            if(log.isErrorEnabled()){
                log.error("Either format or create date are missing from webserveice response ");
            }
            return;
        }

        SimpleDateFormat sdf  = new SimpleDateFormat(format);
        Date petCreateDt = new Date();

        if(petCreateTimestamp!=null){
            try{
                petCreateDt = sdf.parse(petCreateTimestamp);
            }catch(ParseException pe){
                if(log.isErrorEnabled()){
                    log.error("Error while parsing pet last modified on date "+pe);
                }
            }

        }
        CarReopenPetDetails petDetails = new CarReopenPetDetails();
        petDetails.setOrinNumber(orin);
        petDetails.setVendorNumber(vendorNumber);
        petDetails.setVendorStyleNumber(vendorStyleNumber);
        petDetails.setPetCreateTimeStamp(petCreateDt);
        petDetails.setLastPetUpdateTimeStamp(petCreateDt);
        petDetails.setType(type);
        if(log.isDebugEnabled()){
            log.debug("pet create date before saving  "+petDetails.getLastPetUpdateTimeStamp());
        }
        carManager.saveCarReopenPetDetails(petDetails);
    }

    /**
     * Method to update the Attribute value based on the table name.
     *
     * @param tabName
     * @param parameterColumnName
     * @param pimAttrId
     * @param pimAttrValue
     * @param attrId
     * @param car
     * @param vStyle
     * @param vSku
     * @param systemUser
     * @throws Exception
     */
    private void updateAttributeValue(String tabName, String parameterColumnName,long pimAttrId,
            String pimAttrValue, Long attrId, Car car, VendorStyle vStyle,
            VendorSku vSku, User systemUser)throws Exception{
        if(log.isDebugEnabled()){
            log.debug("Inside new updateAttributeValue method");
        }
        if(tabName!=null){
            Tables table = Tables.valueOf(tabName.toUpperCase());
            switch(table){
            case CAR_ATTRIBUTE:
                updateCarAttribute(car,attrId,pimAttrValue,systemUser);
                break;
            case CAR_SKU_ATTRIBUTE:
                updateCarSkuAttribute(vSku,attrId,pimAttrValue,systemUser);
                break;
            case VENDOR_STYLE:
                updateVendorStyle(car,vStyle,pimAttrValue,systemUser,parameterColumnName);
                break;
            case VENDOR_SKU:
                updateVendorSku(car,vSku,pimAttrValue,systemUser,parameterColumnName);
                break;
            }
        }

    }

    /**
     * Method to update Car Attribute.
     *
     * @param car
     * @param attrId
     * @param pimAttrValue
     * @param systemUser
     * @throws Exception
     */
    private void updateCarAttribute(Car car,Long attrId,String pimAttrValue,User systemUser)throws Exception{
        if(log.isDebugEnabled()){
            log.debug("Inside updateCarAttribute method for car Id "+car.getCarId()+" attribute Id "+attrId.longValue()+ " value "+pimAttrValue);
        }
        if(car!=null && attrId !=null && pimAttrValue!=null && !pimAttrValue.isEmpty()){
            List<CarAttribute> carAttrs = pimAttributeDao.getCarAttributesByAttrId(car, attrId.longValue());
            if(carAttrs!=null && !carAttrs.isEmpty()){
                CarAttribute carAttr = carAttrs.get(0);
                carAttr.setAttrValue(pimAttrValue);
                carAttr.setAuditInfo(systemUser);
                carManager.save(carAttr);
            }else{
                if(log.isDebugEnabled()){
                    log.debug("No Car Attribute found for car Id "+car.getCarId()+" and attribute Id "+attrId.longValue());
                }
            }
        }
    }

    /**
     * Method to update Car Sku Attributes based on table name.
     *
     * @param vsku
     * @param attrId
     * @param pimAttrValue
     * @param systemUser
     * @throws Exception
     */
    private void updateCarSkuAttribute(VendorSku vsku,Long attrId,String pimAttrValue,User systemUser)throws Exception{
        if(log.isDebugEnabled()){
            log.debug("Inside updateCarSkuAttribute method for car sku Id "+vsku.getCarSkuId()+" attribute Id "+attrId+" value "+pimAttrValue);
        }
        if(vsku!=null && attrId !=null && pimAttrValue!=null && !pimAttrValue.isEmpty()){
            List<CarSkuAttribute> carAttrs = pimAttributeDao.getCarSkuAttributesByAttrId(vsku, attrId.longValue());
            if(carAttrs!=null && !carAttrs.isEmpty()){
                CarSkuAttribute carSkuAttr = carAttrs.get(0);
                carSkuAttr.setAttrValue(pimAttrValue);
                carSkuAttr.setAuditInfo(systemUser);
                carManager.save(carSkuAttr);
            }else{
                if(log.isDebugEnabled()){
                    log.debug("No Car Sku Attribute found for car Id "+vsku.getCarSkuId()+" and attribute Id "+attrId.longValue());
                }
            }
        }
    }

    /**
     *
     * Method to update vendor style objects.
     *
     * @param car
     * @param vStyle
     * @param attrId
     * @param pimAttrValue
     * @param systemUser
     * @param parameterColumnName
     */
    @SuppressWarnings("unused")
    private void updateVendorStyle(Car car,VendorStyle vStyle,String pimAttrValue,User systemUser,String parameterColumnName){
        if(log.isDebugEnabled()){
            log.debug("Inside updateVendorStyle method for vendor style Id "+vStyle.getVendorStyleId()+" with value "+pimAttrValue);
        }
        if(vStyle!=null){
			if(pimAttrValue!=null && !pimAttrValue.isEmpty()){
            if(parameterColumnName!=null && parameterColumnName.equalsIgnoreCase(Constants.VENDOR_STYLE_NAME_COLUMN)){
                vStyle.setVendorStyleName(pimAttrValue);
                vStyle.setAuditInfo(systemUser);
            }else if(parameterColumnName!=null && parameterColumnName.equalsIgnoreCase(Constants.VENDOR_STYLE_NUMBER_COLUMN)){
                vStyle.setVendorStyleNumber(pimAttrValue);
                vStyle.setAuditInfo(systemUser);
            }else if(parameterColumnName!=null && parameterColumnName.equalsIgnoreCase(Constants.DESCR)){
                vStyle.setDescr(pimAttrValue);
                vStyle.setAuditInfo(systemUser);
            }
        	}else{
        		 if(log.isDebugEnabled()){
                     log.debug("PimAttrValue is null or empty for vendor style Id "+vStyle.getVendorStyleId());
                 }
	  	     }	
         pimAttributeDao.saveVendoorStyle(vStyle);        
		}else{
            if(log.isDebugEnabled()){
                log.debug("No Vendor Style found for vendor style Id "+vStyle.getVendorStyleId());
            }
        }
    }

    /**
     * Method to update Vendor Sku table.
     *
     * @param car
     * @param vSku
     * @param attrId
     * @param pimAttrValue
     * @param systemUser
     * @param parameterColumnName
     */
    @SuppressWarnings("unused")
    private void updateVendorSku(Car car,VendorSku vSku,String pimAttrValue,User systemUser,String parameterColumnName){
        if(log.isDebugEnabled()){
            log.debug("Inside updateVendorSku method for car Id "+car.getCarId()+" car Sku Id "+vSku.getCarSkuId()+" value "+pimAttrValue+" column name "+parameterColumnName);
        }

        if(vSku!=null){
            if(pimAttrValue!=null && !pimAttrValue.isEmpty()){
            if(parameterColumnName!=null && parameterColumnName.equalsIgnoreCase(Constants.SIZE_NAME_COLUMN) && pimAttrValue != null && !pimAttrValue.isEmpty()){
                vSku.setSizeName(pimAttrValue);
                vSku.setAuditInfo(systemUser);
            }
            if(parameterColumnName!=null && parameterColumnName.equalsIgnoreCase(Constants.COLOR_NAME_COLUMN) && pimAttrValue != null && !pimAttrValue.isEmpty()){
                vSku.setColorName(pimAttrValue);
                vSku.setAuditInfo(systemUser);
            }
           }else{
        	   if(log.isDebugEnabled()){
                   log.debug("pimAttrValue is null/empty for vendor sku Id "+vSku.getCarSkuId());
               } 
           }
            pimAttributeDao.saveVendorSku(vSku);

        }else{
            if(log.isDebugEnabled()){
                log.debug("No Vendor Sku found for vendor sku Id "+vSku.getCarSkuId());
            }
        }
    }

    /**
     * Method to update or insert car_sku_attribute with value from PIM
     *  for all skus associated to a car.
     * This method is invoked when the mapping table has table name as BOTH
     *  and data is send as part of style call only.
     *
     * @param car
     * @param lAttrId
     * @param pimAttrValue
     * @param systemUser
     * @throws Exception
     */
    private void upsertAttributeValueForAllSkus(Car car, Long lAttrId,
            String pimAttrValue, User systemUser) throws Exception {

        if(car!=null){
            Set<VendorSku> vSkus = car.getVendorSkus();
            for(VendorSku vSku : vSkus){
                if(log.isDebugEnabled()){
                    log.debug("Inside upsert method for vendor sku Id "+vSku.getCarSkuId());
                }
                if(isCarSkuAttributeExits(vSku,lAttrId)){
                    updateCarSkuAttribute(vSku,lAttrId,pimAttrValue,systemUser);
                }else{
                    addCarSkuAttribute(pimAttrValue,lAttrId,vSku,systemUser);
                }
            }

        }
    }

    /**
     * Looks for any valid special characters in the brand name and returns the equivalent encoded value
     * @param strBrandAttrValue
     * @return strEncodedBrandAttrValue
     */
    public String encodeBrandWithSpecialCharacters(String strBrandAttrValue) {

        if (StringUtils.isEmpty(strBrandAttrValue)) {
            return strBrandAttrValue;
        }

        Config invalidCharsConfig = (Config) carLookupManager.getById(Config.class, Config.INVALID_BRAND_CHARACTERS);

        if (invalidCharsConfig == null) {
            if (log.isInfoEnabled()) {
                log.info("Param INVALID_BRAND_CHARACTERS not found in CONFIG table!");
            }
            return strBrandAttrValue;
        }

        String strInvalidCharsConfigVal = invalidCharsConfig.getValue();
        String strEncodedBrandAttrValue = strBrandAttrValue;
        boolean hasInvalidChars = false;

        if (StringUtils.isNotEmpty(strInvalidCharsConfigVal)) {
            Pattern invalidCharPattern = Pattern.compile(strInvalidCharsConfigVal);
            Matcher invalidCharMatch = invalidCharPattern.matcher(strBrandAttrValue);
            if (invalidCharMatch.find()) {
                hasInvalidChars = true;
            }
        }

        if (!hasInvalidChars) {
            strEncodedBrandAttrValue = StringEscapeUtils.escapeHtml(strBrandAttrValue);
        }

        if (log.isInfoEnabled()) {
            log.info("Brand value from PIM is: "+strBrandAttrValue+", " +
                    "encoded brand value is: "+strEncodedBrandAttrValue);
        }

        return strEncodedBrandAttrValue;
    }

    /**
     * Checks whether a particular PIM attribute is a brand attribute or not
     * @param pimAttrObj
     * @return isBrandAttr
     */
    public boolean isPimBrandAttribute(PIMAttribute pimAttrObj) {

        boolean isBrandAttr = false;
        if (pimAttrObj != null) {
            String pimAttrName = pimAttrObj.getName();
            String pimBmAttrName = pimAttrObj.getBmAttrName();
            if ((StringUtils.isNotEmpty(pimAttrName) && Constants.
                    PIM_CARS_BRAND_ATTRIBUTE.equals(pimAttrName)) ||
                    (StringUtils.isNotEmpty(pimBmAttrName) && Constants.
                            PIM_CARS_BM_BRAND_ATTRIBUTE.equals(pimBmAttrName))) {
                isBrandAttr = true;
            }
        }
        return isBrandAttr;
    }

    /**
     * This method returns the defaultChild of a pattern.
     * Only applicable for pattern-type groupings from PIM.
     *
     * @param patternCar
     * @return
     */
    @Override
    public VendorStyle getDefaultChildOfPatternCar(Car patternCar) {
        VendorStyle defaultChild = null;
        List<VendorStyle> patternStyles = patternCar.getVendorStyles();
        for (VendorStyle child : patternStyles) {
            if (defaultChild == null || Constants.FLAG_Y.equals(child.getIsDefault())) {
                defaultChild = child;
            }
        }
        return defaultChild;
    }
}

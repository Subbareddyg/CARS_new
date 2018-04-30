package com.belk.car.app.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.belk.car.app.Constants;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.BaseAuditableModel;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarImage;
import com.belk.car.app.model.CarSample;
import com.belk.car.app.model.CarSkuAttribute;
import com.belk.car.app.model.ColorMappingMaster;
import com.belk.car.app.model.ContentStatus;
import com.belk.car.app.model.Image;
import com.belk.car.app.model.ImageTrackingStatus;
import com.belk.car.app.model.MediaCompassImage;
import com.belk.car.app.model.PIMAttribute;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.model.Sample;
import com.belk.car.app.model.SampleSourceType;
import com.belk.car.app.model.SizeConversionMaster;
import com.belk.car.app.model.SourceType;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorSkuPIMAttribute;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.VendorStylePIMAttribute;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.SizeConversionJobManager;
import com.belk.car.util.DateUtils;
import com.belk.car.app.service.SuperColorManager;

public class ExportToCESFile {

	private static transient final Log log = LogFactory.getLog(ExportToCESFile.class);
	String username = "carsadmin@belk.com";

	List<Car> cars = null;
	boolean exportContentAttributes = false;

	List<Car> processedCars = null;
	List<Image> processedImages = null;

	CarManager carManager = null;
	CarLookupManager lookupManager = null;
	SizeConversionJobManager sizeConversionJobManager = null;

	Document dom;
	static Set<Sample> requestedSamplesForCar = new HashSet<Sample>();
	public ExportToCESFile(List<Car> cars) {
		this.cars = cars;
	}

	public void export(String fileName) throws Exception {
		if (cars != null && !cars.isEmpty()) {
			this.createDocument();
			if (dom != null) {
				this.createDOMTree();
				try {
					this.writeToFile(fileName);
					if (log.isInfoEnabled()) {
						log.info("--------------------->Feed file write completed successfully<-----------------------");
					}
					//Update Images ONLY if Write is Successful...
					if (processedImages != null) {
						if (log.isInfoEnabled()) {
							log.info("Updating image processing status of all processed images");
						}
						for (Image img : processedImages) {
							img.setImageProcessingStatusCd(Image.PROCESSING_STATUS_PROCESSED);
							this.setAuditInfo(username, img);
							carManager.save(img);
						}
						if (log.isInfoEnabled()) {
							log.info("Total number of images updated: "+processedImages.size());
						}
					}

					//Update Car ONLY if Write is Successful...
					if (processedCars != null) {
						if (log.isInfoEnabled()) {
							log.info("Updating content status of all processed CAR IDs ");
						}
						ContentStatus sentToCmpContentStatus = (ContentStatus) lookupManager.getById(ContentStatus.class, ContentStatus.SENT_TO_CMP);
						for (Car car : processedCars) {
							car.setContentStatus(sentToCmpContentStatus);
							this.setAuditInfo(username, car);
							carManager.save(car);
						}
						if (log.isInfoEnabled()) {
							log.info("Total number of CAR IDs updated:"+processedCars.size());
						}
					}

				} catch (Exception ex) {
					//log.debug("Error in processing ExportToCES: " + ex.);
					ex.printStackTrace();
					throw ex;
				}
			}
		}
	}

	/**
	 * Using JAXP in implementation independent manner create a document object
	 * using which we create a xml tree in memory
	 */
	private void createDocument() {

		//get an instance of factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			//get an instance of builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			//create an instance of DOM
			dom = db.newDocument();

		} catch (ParserConfigurationException pce) {
			//dump it
			if (log.isDebugEnabled())
				log.debug("Error while trying to instantiate DocumentBuilder " + pce);
		}

	}

	/**
	 * The real workhorse which creates the XML structure
	 */
	private void createDOMTree() {

		//create the root element 
		Element rootEle = dom.createElement("products");

		dom.appendChild(rootEle);

		if (log.isDebugEnabled()) {
			log.debug("Processing CES Data: " + cars.size() + " Cars to process!!!");
		}

		int i = 0;
		int size = cars.size();
		processedCars = new ArrayList<Car>(size);
		processedImages = new ArrayList<Image>(size);
		List<ColorMappingMaster> superColors = superColorManager.getAllSuperColorMapping();
		//No enhanced for
		for (Car car : cars) {
			i++;
			//For each Vendor Style object  create  element and attach it to root
			
			if (car.getVendorStyle().getProductType() != null) {
				if (log.isDebugEnabled())
					log.debug("Processing (" + i + " of " + size + ") CAR: " + car.getCarId());
				Element productElem = this.createProductElement(car,superColors);
				if (productElem != null) {
					rootEle.appendChild(productElem);
				}
			}

			//Commenting the code - we will mark car as SENT_TO_CMP only if we send car to CMP.
			/*if (car.getContentStatus().getCode().equals(ContentStatus.PUBLISHED) || car.getContentStatus().getCode().equals(ContentStatus.RESEND_TO_CMP)
					|| car.getContentStatus().getCode().equals(ContentStatus.RESEND_DATA_TO_CMP)) {
				processedCars.add(car);
				
			}*/
		}

	}

	/**
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	private Element createElement(String name, String value) {
		Element elem = dom.createElement(name);
		Text textVal = dom.createTextNode(value == null ? "" : value);
		elem.appendChild(textVal);
		return elem;

	}

	/**
	 * Helper method which creates a XML element 
	 * @param b The book for which we need to create an xml representation
	 * @return XML element snippet representing a CAR
	 */
	private Element createProductElement(Car car,List<ColorMappingMaster> superColors) {

		//Need to check for both content that are Published Or Car where Images are ready to be sent to CMS
		//If neither is true than do not send the CAR to CMP
		//This would happen when some of the Images have been Recieved and Content may be marked for publish
		boolean canPublishContent = (car.getContentStatus().getCode().equals(ContentStatus.PUBLISHED) && car.getVendorStyle().getStatusCd().equalsIgnoreCase("ACTIVE"))
				|| car.getContentStatus().getCode().equals(ContentStatus.RESEND_TO_CMP)
				|| car.getContentStatus().getCode().equals(ContentStatus.RESEND_DATA_TO_CMP);
		
		boolean updateStatusInCMP = (car.getContentStatus().getCode().equals(ContentStatus.DISABLE_IN_CMP) && !car.getVendorStyle().getStatusCd().equalsIgnoreCase("ACTIVE"))
				|| car.getContentStatus().getCode().equals(ContentStatus.ENABLE_IN_CMP);
		boolean ignoreUpdateInCMP = (car.getContentStatus().getCode().equals(ContentStatus.DISABLE_IN_CMP) && car.getVendorStyle().getStatusCd().equalsIgnoreCase("ACTIVE"));
		boolean canPublishImages = ExportToCESFile.haveAllImagesBeenReceived(car);
		
		boolean isMediaCompassImageReceived = false;
		 List<CarImage> carVendorImages=car.getCarVendorImages();
		if ((car.getMediaCompassImage() != null && car.getMediaCompassImage().size() > 0) || (requestedSamplesForCar.size()<1 && (carVendorImages ==null || carVendorImages.isEmpty()))){
			isMediaCompassImageReceived = true;
		}
		boolean isPromotionCar = SourceType.PYG.equals(car.getSourceType().getSourceTypeCd());
		if(!isMediaCompassImageReceived){
			if(log.isInfoEnabled()){
				log.info("Media compass image has not received for CAR: "+ car.getCarId());
			}
		}
		//check media compass image availability before sending car to cmp
		//if all images for car are received then no need to check media compass image availability
	
		boolean canPublishCar = false;
		canPublishCar = (canPublishContent && isMediaCompassImageReceived) || (canPublishImages && !ignoreUpdateInCMP) || updateStatusInCMP;
		Element productElem = null;
		if (canPublishCar) {
			productElem = dom.createElement("product");
			VendorStyle vs = car.getVendorStyle();
			productElem.setAttribute("vendornumber", vs.getVendorNumber());
			productElem.setAttribute("stylenumber", vs.getVendorStyleNumber());
			productElem.setAttribute("patterntype", vs.getVendorStyleType().getCode());
			productElem.setAttribute("dept", car.getDepartment().getDeptCd());
			productElem.setAttribute("deptname", isPromotionCar?Constants.DBPROMOTION_DEPARTMENT:car.getDepartment().getName());
			productElem.setAttribute("class", String.valueOf(vs.getClassification().getBelkClassNumber()));
			productElem.setAttribute("classname", isPromotionCar?Constants.DBPROMOTION_CLASS:vs.getClassification().getName());
			//added the Style ORIN number to product tag
			if (vs.getOrinNumber()!=null) {
				productElem.setAttribute("orin", String.valueOf(vs.getOrinNumber()));
			}	
			else {
				productElem.setAttribute("orin", "");
			}	
			
			productElem.appendChild(createElement("name", stripString(vs.getVendorStyleName())));
			productElem.appendChild(createElement("description", stripString(vs.getDescr())));

			productElem.appendChild(createElement("lastupdatedate", DateUtils.formatDate(car.getUpdatedDate(), "MM/dd/yyyy hh:mm:ss")));
			productElem.appendChild(createElement("publishflag", car.getContentStatus().getCode().equals(ContentStatus.IN_PROGRESS) ? ContentStatus.FLAG_NO
					: ContentStatus.FLAG_YES));
			productElem.appendChild(createElement("contentstatus", car.getContentStatus().getCode()));
			productElem.appendChild(createElement("publishdate", DateUtils.formatDate(car.getUpdatedDate(), "MM/dd/yyyy hh:mm:ss")));
			productElem.appendChild(createElement("vendorname", vs.getVendor().getName()));
			productElem.appendChild(createElement("carid", String.valueOf(car.getCarId())));
			productElem.appendChild(createElement("requesttype", String.valueOf(car.getSourceType().getSourceTypeCd())));
			//08-05-2008
			//Changed Based on Request from Todd to have Expected Ship Date
			//if (car.getSourceType().getSourceTypeCd().equals(SourceType.PO)) {
			productElem.appendChild(createElement("expectedshipdate", DateUtils.formatDate(car.getExpectedShipDate())));
			//added media compass image url in xml file
			Set<MediaCompassImage> mediaCompasImage=car.getMediaCompassImage();
			MediaCompassImage mediaCompass=null;
			if(mediaCompasImage !=null && mediaCompasImage.size()>0){
				for(MediaCompassImage meImage:mediaCompasImage){
					mediaCompass=meImage;
				}
			}	
			String strMCImage = mediaCompass == null? " " : mediaCompass.getImageLocation();
			productElem.appendChild(createElement("mediaCompassImageURL", strMCImage));
			
			
			//}
			productElem.appendChild(createProductTypeElement(car,vs.getProductType()));
			productElem.appendChild(createCollectionSkus(car));
			productElem.appendChild(createStyleAttributes(car));
			
			Map<VendorStyle, List<VendorSku>> skuMap = car.getVendorStyleMap();
			if (vs.isPattern()) {
				if (!skuMap.isEmpty()) {
					Element childProductsEle = dom.createElement("childproducts");
					for (VendorStyle vs2 : skuMap.keySet()) {
						Element childProductElem = createChildProductElement(car, vs2);
						if(!vs.isConsolidatedProduct()) {
						    childProductElem.appendChild(this.createChildProductStyleAttributes(vs2));
						}
						childProductElem.appendChild(this.createSkus(skuMap.get(vs2),superColors));
						childProductsEle.appendChild(childProductElem);
					}
					productElem.appendChild(childProductsEle);
				}
			} else {
				if (!skuMap.isEmpty() && skuMap.get(car.getVendorStyle()) != null) {
					productElem.appendChild(createSkus(skuMap.get(car.getVendorStyle()),superColors));
				}
			}

			if (canPublishImages) {
				productElem.appendChild(this.createImagesElement(car));
			}
			
			//marking car as processed after it is sent to cmp
			processedCars.add(car);
			
		}
		return productElem;
	}
	

	/**
	 * This method which get longtext XML element for Collection car 
	 * @param car contains the CAR object
	 * @return longtext XML element
	 */
	private Element createCollectionSkus(Car car) {
		Element longTextElem = dom.createElement("longtext");
		
		for (CarAttribute ca : car.getCarAttributes()) {
			if(ca.isCollectionAttribute() || car.getSourceType().getSourceTypeCd().equalsIgnoreCase("PYG")){
				longTextElem.appendChild(createCollectionSkuAttribute(car));
				break;
			}
		}
		return longTextElem;
	}

	/**
	 * This method which get collectionskus XML element for Collection car 
	 * Fetch all the Skus from sku xml element
	 * @param vs  contains VendorStyle object
	 * @return    collection of sku element
	 */
	private Element createCollectionSkuAttribute(Car car) {
		Element collAttributeElem = dom.createElement("collectionskus");
		List<String> skuCodes =  new ArrayList<String>();  //carManager.getCollectionSkuByVS(vs);
		
		if (SourceType.PYG.equalsIgnoreCase(car.getSourceType().getSourceTypeCd())){
			 skuCodes = carManager.getDBPromotionCollectionSkuByVS(car.getVendorStyle());
		}else{
			 skuCodes = carManager.getCollectionSkuByVS(car.getVendorStyle());
		}
		
		for (String skuCode : skuCodes) {
			Element valueElem = createElement("sku", skuCode);
			collAttributeElem.appendChild(valueElem);
		}
		return collAttributeElem;
	}

	public static boolean haveAllImagesBeenReceived(Car car) {
		boolean allImagesReceived = true;
		Set<Sample> requestedSamples = new HashSet<Sample>();
		if (car.getCarSamples() != null && !car.getCarSamples().isEmpty()) {
			for (CarSample cs : car.getCarSamples()) {
				Sample s = cs.getSample();
				if (!s.getSampleSourceType().getSampleSourceTypeCd().equals(SampleSourceType.NEITHER)) {
					requestedSamples.add(s);
				}
			}
		}
		requestedSamplesForCar = requestedSamples;
		if (log.isDebugEnabled()) {
			log.debug("There are " + requestedSamples.size() + " sample requested for Car: " + car.getCarId());
		}

		if (!requestedSamples.isEmpty()) {
			Set<Sample> sampleImages = new HashSet<Sample>();
			for (CarImage ci : car.getCarImages()) {
				if (ci.getImage().getSample() != null) {
					if (!sampleImages.contains(ci.getImage().getSample()))
						sampleImages.add(ci.getImage().getSample());
				}
			}

			for (Sample s : requestedSamples) {
				if (!sampleImages.contains(s)) {
					if (log.isDebugEnabled()) {
						log.debug("Image not found for Sample ID: " + s.getSampleId());
					}
					allImagesReceived = false;
					break;
				}
			}
		}

		// Added addition criteria that all REQUESTED and APPROVED Images are recieved
		if (allImagesReceived) {
			for (CarImage ci : car.getAllActiveCarImages()) {
				Image img = ci.getImage();
				if (img.getImageTrackingStatus().getImageTrackingStatusCd().equals(ImageTrackingStatus.REQUESTED)
						|| img.getImageTrackingStatus().getImageTrackingStatusCd().equals(ImageTrackingStatus.APPROVED)) {
					allImagesReceived = false;
					break;
				}
			}
		}
		return allImagesReceived;
	}

	private Element createImagesElement(Car car) {
		Element imagesElem = dom.createElement("images");
		if (log.isDebugEnabled()) {
			log.debug("Number of Images In Car( " + car.getCarId() + "): " + car.getCarImages().size());
		}

		for (CarImage ci : car.getCarImages()) {
			Image img = ci.getImage();
			if (img.getImageTrackingStatus().getImageTrackingStatusCd().equals(ImageTrackingStatus.RECEIVED)
					&& img.getImageProcessingStatusCd().equals(Image.PROCESSING_STATUS_PENDING)) {
				imagesElem.appendChild(this.createImageElement(img));
				
				processedImages.add(img);
			}
		}
		return imagesElem;
	}

	/**
	 * 
	 * @param image
	 * @return
	 */
	private Element createImageElement(Image image) {
		Element imageEle = dom.createElement("image");

		imageEle.appendChild(createElement("source", image.getImageSourceType().getImageSourceTypeCd()));
		imageEle.appendChild(createElement("status", image.getStatusCd()));
		imageEle.appendChild(createElement("name", image.getImageFinalUrl()));
		imageEle.appendChild(createElement("location", image.getImageLocation()));
		imageEle.appendChild(createElement("notes", image.getNotesText()));
		return imageEle;
	}

	/**
	 * 
	 * @param car
	 * @param vs
	 * @return
	 */
	private Element createChildProductElement(Car car, VendorStyle vs) {
		Element productElem = dom.createElement("childproduct");
		productElem.setAttribute("vendornumber", vs.getVendorNumber());
		productElem.setAttribute("stylenumber", vs.getVendorStyleNumber());
		productElem.setAttribute("patterntype", vs.getVendorStyleType().getCode());
		productElem.setAttribute("dept", car.getDepartment().getDeptCd());
		productElem.setAttribute("class", String.valueOf(vs.getClassification().getBelkClassNumber()));
		//create the ORIN number for product tag
		if (vs.getOrinNumber() != null) {
			productElem.setAttribute("orin", String.valueOf(vs.getOrinNumber()));
		} else {
			productElem.setAttribute("orin", "");
		}
		//create name element and description text node and attach it to productElemment
		productElem.appendChild(createElement("name", stripString(vs.getVendorStyleName())));
		productElem.appendChild(createElement("description", stripString(vs.getDescr())));

		productElem.appendChild(createElement("alternatestyle", vs.getAlternateCategory()));
		productElem.appendChild(createElement("vendorname", vs.getVendor().getName()));
		return productElem;
	}

	/**
	 * 
	 * @param productType
	 * @return
	 */
	private Element createProductTypeElement(Car car,ProductType productType) {
		Element pType = null;
		if(SourceType.PYG.equals(car.getSourceType().getSourceTypeCd())){
			pType = this.createElement("producttype","Promotion" );
		}else{
			pType = this.createElement("producttype", productType.getName());
		}
		pType.setAttribute("id", productType.getProductIdAsString());   //productType Id will be same as of OUTFIT for PYG car as PYG cars are decided to be having OUTFIT class n departments.
		return pType;
	}

	/**
	 * 
	 * @param car
	 * @return
	 */
	private Element createStyleAttributes(Car car) {
		Element carAttributeElem = dom.createElement("styleattributes");
		for (CarAttribute ca : car.getCarAttributes()) {
			if ((isExportContentAttributes() || !ca.getAttribute().isContentAttribute()) && !ca.getAttribute().isSkuAttribute()) {
				if (ca.getAttribute().isActive()) {
					carAttributeElem.appendChild(createStyleAttribute(ca));
				}
			}
		}
		VendorStyle vs = car.getVendorStyle();
		if(vs.isPattern()){
		    if(log.isInfoEnabled()){
		        log.info("processing style attributes for pattern vendor style number "+vs.getVendorStyleNumber());
		    }
		    Map<VendorStyle, List<VendorSku>> skuMap = car.getVendorStyleMap();
		    if (!skuMap.isEmpty()) {
                for (VendorStyle vs2 : skuMap.keySet()) {
                    Set<VendorStylePIMAttribute> vsPimAttrSet = vs2.getVendorStylePIMAttribute();
                    for(VendorStylePIMAttribute vsPimAttr:vsPimAttrSet){
                        PIMAttribute pa = vsPimAttr.getPimAttribute();
                        if(pa.getStatusCd().equalsIgnoreCase("ACTIVE")){
                            carAttributeElem.appendChild(createStyleAttribute(pa,vsPimAttr));
                        }
                    }
                }
		    }
		    
		}else{
		    if(log.isInfoEnabled()){
                log.info("processing style attributes for normal vendor style number "+vs.getVendorStyleNumber());
            }
		    Set<VendorStylePIMAttribute> vsPimAttrSet = vs.getVendorStylePIMAttribute();
	        for(VendorStylePIMAttribute vsPimAttr:vsPimAttrSet){
	            PIMAttribute pa = vsPimAttr.getPimAttribute();
	            if(pa.getStatusCd().equalsIgnoreCase("ACTIVE")){
	                carAttributeElem.appendChild(createStyleAttribute(pa,vsPimAttr));
	            }
	        }
		}
		
	    return carAttributeElem;
	}
	
	
	/**
	 * 
	 * @param carAttribute
	 * @return
	 */
	private Element createStyleAttribute(CarAttribute carAttribute) {
		Element carAttrElem = createAttribute(carAttribute.getAttribute());
		carAttrElem.setAttribute("displayorder", String.valueOf(carAttribute.getDisplaySeq()));
		carAttrElem.appendChild(createElement("attributevalue", stripString(carAttribute.getAttrValue())));
		return carAttrElem;
	}
	
	/**
	 * Method to set the style attributes for vendor style pim attributes.
	 * 
	 * @param pimAttribute
	 * @param vspAttr
	 * @return
	 */
	private Element createStyleAttribute(PIMAttribute pimAttribute,VendorStylePIMAttribute vspAttr){
	    Element carAttrElement = dom.createElement("attribute");
	    String name = pimAttribute.getBmAttrName()!=null?pimAttribute.getBmAttrName():pimAttribute.getName();
	    carAttrElement.setAttribute("name", StringUtils.trim(name).replaceAll("[^a-zA-Z0-9]", "_"));
	    carAttrElement.setAttribute("type", Constants.PRODUCT_TYPE);
	    carAttrElement.setAttribute("searchable", Constants.FLAG_N);
	    carAttrElement.setAttribute("displayable", pimAttribute.getIsPimAttrDisplayble());
	    carAttrElement.appendChild(createElement("displaylabel", stripString(name)));
	    carAttrElement.setAttribute("displayorder", Constants.DISPLAY_SEQ_ZERO);
	    carAttrElement.appendChild(createElement("attributevalue", stripString(vspAttr.getValue())));
	    if(log.isInfoEnabled()){
            log.info("creating style attribute for pim attribute "+name);
        }
	    return carAttrElement;
	}

	/**
	 * 
	 * @param skus
	 * @return
	 */
	private Element createSkus(List<VendorSku> skus,List<ColorMappingMaster> superColors) {
		Element skusElem = dom.createElement("skus");
		for (VendorSku sku : skus) {
			skusElem.appendChild(createSku(sku,superColors));
		}
		return skusElem;
	}

	/**
	 * 
	 * @param sku
	 * @return
	 */
	private Element createSku(VendorSku sku,List<ColorMappingMaster> superColors) {
		Element skuElem = dom.createElement("sku");
		skuElem.setAttribute("belkupc", sku.getBelkUpc());
		skuElem.setAttribute("vendorupc", sku.getVendorUpc());
		skuElem.setAttribute("vendornumber", sku.getVendorStyle().getVendorNumber());
		skuElem.setAttribute("stylenumber", sku.getVendorStyle().getVendorStyleNumber());
		skuElem.appendChild(createElement("name", stripString(sku.getName())));
		skuElem.appendChild(createElement("colorcode", sku.getColorCode()));
		skuElem.appendChild(createElement("colorname", stripString(sku.getColorName())));
		skuElem.appendChild(createElement("sizecode", sku.getSizeCode()));
		skuElem.appendChild(createElement("sizename", sku.getSizeName()));
		skuElem.appendChild(createElement("setflag", sku.getSetFlag()));
		// added below code for Faceted Navigation CARS
		List<String> superColorAttributes=new ArrayList<String>(){
			{
				add(Constants.ATTR_SUPERCOLOR1);
				add(Constants.ATTR_SUPERCOLOR2);
				add(Constants.ATTR_SUPERCOLOR3);
				add(Constants.ATTR_SUPERCOLOR4);
			}
		};
		List<String> sizeAttributes=new ArrayList<String>(){
            {
                add(Constants.PIM_FACET_SIZE_1);
                add(Constants.PIM_FACET_SIZE_2);
                add(Constants.PIM_FACET_SIZE_3);
                add(Constants.PIM_FACET_SUB_SIZE_1);
                add(Constants.PIM_FACET_SUB_SIZE_2);
            }
        };
        
		String superColor1Value=getColorCodeSuperColor(superColors,sku.getColorCode());
		log.info("superColor1Value----->"+ superColor1Value);
		Car car = sku.getCar();
		Element skuAttrsElem = dom.createElement("skuattributes");
		if (sku.getCarSkuAttributes() != null && !sku.getCarSkuAttributes().isEmpty()) {
			// add existing color attributes found in Sku attribute
			for (CarSkuAttribute csa : sku.getCarSkuAttributes()) {
				if (csa.getAttribute().isActive()) {
					if(superColorAttributes.contains(csa.getAttribute().getName())){
						superColorAttributes.remove(csa.getAttribute().getName());
					}
                    if (sizeAttributes.contains(csa.getAttribute().getName())) {
                        if (!carManager.isPostCutoverCar(car)) {
                            continue; // do not append size attribute from car_sku_attribute table for pre-cutover cars
                        }
                    }
                    skuAttrsElem.appendChild(createSkuAttribute(csa));
				}
			}
		}
		
		//calling method to populate attributes from vendorsku_pim_attributes
		createVendorSkuPIMAttributes(skuAttrsElem,sku);
		
		// add remaining color attributes and if SUPER_COLOR_1 is present then add derived 
		// value (superColor1) from mapping 
		createSuperColorSkuAttribute(skuAttrsElem,superColorAttributes,superColor1Value);
		// add size facets and sub facets from size conversion master for pre-cutover cars
        if (!carManager.isPostCutoverCar(car)) {
            createFacetSizeAttributes(skuAttrsElem, sku);
        }
		//add ORIN number attribute to sku attribute list
		if (sku.getOrinNumber() != null) {
			skuAttrsElem.appendChild(createSkuAttribute("orin", String
					.valueOf(sku.getOrinNumber())));
		} else {
			skuAttrsElem.appendChild(createSkuAttribute("orin", ""));
		}
		skuElem.appendChild(skuAttrsElem);
		
		return skuElem;
	}


	/**
	 * Method to create attribute elements for vendor sku pim attributes.
	 * 
	 * @param skuAttrsElem
	 * @param sku
	 */
	private void createVendorSkuPIMAttributes(Element skuAttrsElem,
            VendorSku sku) {
	    if(log.isInfoEnabled()){
	        log.info("Inside createVendorSkuPIMAttributes----->");
	    }
	    Set<VendorSkuPIMAttribute> skuPimAttrSet = sku.getSkuPIMAttributes();
	    if(skuPimAttrSet!=null && skuPimAttrSet.size()>0){
	        for(VendorSkuPIMAttribute skuPimAttr : skuPimAttrSet){
	            PIMAttribute pimAttr = skuPimAttr.getPimAttributeDetails();
	            String name = pimAttr.getBmAttrName()!=null?pimAttr.getBmAttrName():pimAttr.getName();
	            if(log.isInfoEnabled()){
	                log.info("creating sku attribute elelment for pim attribute -----> "+name);
	            }
	            skuAttrsElem.appendChild(createSkuAttribute(name,skuPimAttr.getAttributeValue()));
	        }
	    }

        
    }

    /**
	 * 
	 * @param carSkuAttribute
	 * @return
	 */
	private Element createSkuAttribute(CarSkuAttribute carSkuAttribute) {
		Element carAttrElem = createAttribute(carSkuAttribute.getAttribute());
		carAttrElem.setAttribute("displayorder", String.valueOf(0L));
		carAttrElem.appendChild(createElement("attributevalue", stripString(carSkuAttribute.getAttrValue())));
		log.info("CarSkuAttribute-----"+ carSkuAttribute.getAttrValue());
		return carAttrElem;
	}

	/**
	 * 
	 * @param attribute
	 * @return
	 */
	private Element createAttribute(Attribute attribute) {
		Element attrElem = dom.createElement("attribute");
		attrElem.setAttribute("name", StringUtils.trim(attribute.getBlueMartiniAttribute()).replaceAll("[^a-zA-Z0-9]", "_"));
		attrElem.setAttribute("type", attribute.getAttributeType().getAttrTypeCd());
		attrElem.setAttribute("searchable", attribute.getIsSearchable());
		attrElem.setAttribute("displayable", attribute.getIsDisplayable());
		attrElem.appendChild(createElement("displaylabel", stripString(attribute.getAttributeConfig().getDisplayName())));
		return attrElem;

	}

	/**
	 * This method uses Xerces specific classes
	 * prints the XML document to file.
	 */
	private void writeToFile(String fileName) throws Exception {

		try {
			//print
			OutputFormat format = new OutputFormat(dom);
			format.setIndenting(true);

			//to generate output to console use this serializer
			//XMLSerializer serializer = new XMLSerializer(System.out, format);

			//to generate a file output use fileoutputstream instead of system.out
			XMLSerializer serializer = new XMLSerializer(new FileOutputStream(new File(fileName)), format);

			serializer.serialize(dom);

		} catch (IOException ie) {
			ie.printStackTrace();
			throw ie;
		}
	}

	public boolean isExportContentAttributes() {
		return exportContentAttributes;
	}

	public void setExportContentAttributes(boolean exportContentAttributes) {
		this.exportContentAttributes = exportContentAttributes;
	}

	public CarManager getCarManager() {
		return carManager;
	}

	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}

	public CarLookupManager getLookupManager() {
		return lookupManager;
	}

	public void setLookupManager(CarLookupManager lookupManager) {
		this.lookupManager = lookupManager;
	}

	private void setAuditInfo(String username, BaseAuditableModel model) {

		if (StringUtils.isBlank(model.getCreatedBy())) {
			model.setCreatedBy(username);
			model.setCreatedDate(new Date());
		}
		model.setUpdatedBy(username);
		model.setUpdatedDate(new Date());
	}

	private String stripString(String str) {

		String attrVal = str == null ? "" : str.replaceAll("\\P{ASCII}+", "");
		attrVal = stripNonValidXMLCharacters(attrVal);
		if (str != null && !attrVal.equals(str)) {
			log.debug("Special Characters Stripped: ");
			log.debug("Old Descr: " + str);
			log.debug("New Descr: " + attrVal);
		}
		return attrVal;
	}

	/*
	 * added check not to send the N/A as attribute value for attributes (eg. brand)
	 */
	public String stripNonValidXMLCharacters(String in) {
        StringBuffer out = new StringBuffer(); // Used to hold the output.
        char current; // Used to reference the current character.
        if (in == null || ("".equals(in)) || "N/A".equals(in)) return ""; // vacancy test.
        for (int i = 0; i < in.length(); i++) {
            current = in.charAt(i); // NOTE: No IndexOutOfBoundsException
									// caught here; it should not happen.
            if ((current == 0x9) ||
                (current == 0xA) ||
                (current == 0xD) ||
                ((current >= 0x20) && (current <= 0xD7FF)) ||
                ((current >= 0xE000) && (current <= 0xFFFD)) ||
                ((current >= 0x10000) && (current <= 0x10FFFF)))
                out.append(current);
        }
        return out.toString();

    }
	
	SuperColorManager superColorManager=null;
	
	public SuperColorManager getSuperColorManager() {
		return superColorManager;
	}

	public void setSuperColorManager(SuperColorManager superColorManager) {
		this.superColorManager = superColorManager;
	}
	
	public SizeConversionJobManager getSizeConversionJobManager() {
        return sizeConversionJobManager;
    }

    public void setSizeConversionJobManager(SizeConversionJobManager sizeConversionJobManager) {
        this.sizeConversionJobManager = sizeConversionJobManager;
    }

    /*
	 * Creating super color as sku attributes
	 * Added for CARS Faceted Navigation
	 */
	private Element createSuperColorSkuAttribute(Element skuAttrElem,List<String> superColorAttributes,String superColor1Value) {
		
		if(!superColorAttributes.isEmpty()){
			for(String superColor:superColorAttributes){
				// create super color1 attribute with superColor1
				if(superColor.equals(Constants.ATTR_SUPERCOLOR1)){
					skuAttrElem.appendChild(createSkuAttribute(superColor,superColor1Value));
				} else {
					// remaining super color value will be empty
					skuAttrElem.appendChild(createSkuAttribute(superColor,StringUtils.EMPTY));
				}
			}
		}
		return skuAttrElem;
	}
	
	/*
	 *  append the facet to the sku attributes
	 *  added for Faceted CARS
	 */
	private Element createFacetSizeAttributes(Element skuAttrsElem,VendorSku sku) {
        SizeConversionMaster sizeMaster=sku.getSizeRule();
        if(sizeMaster!=null && sizeMaster.getScmId()!=-1){
            skuAttrsElem.appendChild(createSkuAttribute(Constants.FACET_SIZE_1, stripString(sizeMaster.getFacetSize1())));
            skuAttrsElem.appendChild(createSkuAttribute(Constants.FACET_SIZE_2, stripString(sizeMaster.getFacetSize2())));
            skuAttrsElem.appendChild(createSkuAttribute(Constants.FACET_SIZE_3, stripString(sizeMaster.getFacetSize3())));
            skuAttrsElem.appendChild(createSkuAttribute(Constants.FACET_SUB_SIZE_1, stripString(sizeMaster.getFacetSubSize1())));
            skuAttrsElem.appendChild(createSkuAttribute(Constants.FACET_SUB_SIZE_2, stripString(sizeMaster.getFacetSubSize2())));
        } else {
            // add empty facets file if there is no size rule found for the sku 
            skuAttrsElem.appendChild(createSkuAttribute(Constants.FACET_SIZE_1,StringUtils.EMPTY));
            skuAttrsElem.appendChild(createSkuAttribute(Constants.FACET_SIZE_2,StringUtils.EMPTY));
            skuAttrsElem.appendChild(createSkuAttribute(Constants.FACET_SIZE_3,StringUtils.EMPTY));
            skuAttrsElem.appendChild(createSkuAttribute(Constants.FACET_SUB_SIZE_1,StringUtils.EMPTY));
            skuAttrsElem.appendChild(createSkuAttribute(Constants.FACET_SUB_SIZE_2,StringUtils.EMPTY));
        }
		return skuAttrsElem;
	}
	
	/*
	 *  method create sku attribute with strings
	 */
	private Element createSkuAttribute(String attribute,String attributeValue) {
		Element carAttrElem = createAttribute(attribute);
		carAttrElem.setAttribute("displayorder", String.valueOf(0L));
		carAttrElem.appendChild(createElement("attributevalue", attributeValue));
		return carAttrElem;
	}

	/*
	 *  method create  attribute with string
	 */
	
	private Element createAttribute(String attributeName) {
		Element attrElem = dom.createElement("attribute");
		attrElem.setAttribute("name", attributeName);
		attrElem.setAttribute("type", "SKU");
		attrElem.setAttribute("searchable", "N");
		attrElem.setAttribute("displayable", "N");
		attrElem.appendChild(createElement("displaylabel", attributeName));
		return attrElem;

	}

	/*
	 * Method to get the super color name by the given color code
	 * The color code between the begin and end code, get the super color name
	 */
	private String getColorCodeSuperColor(List<ColorMappingMaster> superColors,String colorCode){
		int color=Integer.parseInt(colorCode);
		int beginColor;
		int endColor;
		String superColor=StringUtils.EMPTY;
		for(ColorMappingMaster colorMaster:superColors){
			beginColor=Integer.parseInt(colorMaster.getColorCodeBegin());
			endColor=Integer.parseInt(colorMaster.getColorCodeEnd());
			if(color >= beginColor && color <= endColor){
				superColor=colorMaster.getSuperColorName();
			}
		}
		return superColor;
	}
	
	/**
     * 
     * @param vs
     * @return
     */
    private Element createChildProductStyleAttributes(VendorStyle vs) {
        Element carAttributeElem = dom.createElement("styleattributes");
        if (log.isInfoEnabled()) {
            log.info("processing child product style attributes for vendor style number " + vs.getVendorStyleNumber());
        }

        Set<VendorStylePIMAttribute> vsPimAttrSet = vs.getVendorStylePIMAttribute();
        for (VendorStylePIMAttribute vsPimAttr : vsPimAttrSet) {
            PIMAttribute pa = vsPimAttr.getPimAttribute();
            if (pa.getStatusCd().equalsIgnoreCase("ACTIVE")) {
                carAttributeElem.appendChild(createStyleAttribute(pa, vsPimAttr));
            }
        }

        return carAttributeElem;
    }
}

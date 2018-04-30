package com.belk.car.webapp.taglib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.springframework.context.NoSuchMessageException;

import com.belk.car.app.Constants;
import com.belk.car.app.dao.VendorSkuPIMAttributeDao;
import com.belk.car.app.dao.hibernate.VendorSkuPIMAttributeDaoHibernate;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.AttributeConfig;
import com.belk.car.app.model.AttributeLookupValue;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarSkuAttribute;
import com.belk.car.app.model.DBPromotionCarAttribute;
import com.belk.car.app.model.HtmlDisplayType;
import com.belk.car.app.model.OutfitCarAttribute;
import com.belk.car.app.model.PIMAttribute;
import com.belk.car.app.model.SourceType;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorSkuPIMAttribute;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.VendorStylePIMAttribute;
import com.belk.car.util.GenericComparator;
import com.lowagie.text.html.HtmlEncoder;

/**
 * Generate the Attribute List
 * 
 * @author vsundar
 */

public final class DisplayAttributesTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5251469913549573754L;

	private transient final Log log = LogFactory.getLog(DisplayAttributesTag.class);

	// --------------------------------------------------- Instance Variables

	/**
	 * The key of the session-scope bean we look for.
	 */
	public enum DisplayType {
		CAR_ATTRIBUTE, CAR_SKU_ATTRIBUTE, PIM_ATTRIBUTE
	};

	private String var = null;

	private String scope = null;

	private DisplayType curDisplayType = DisplayType.CAR_ATTRIBUTE;

	private String displayType = "CAR_ATTRIBUTE";

	private String styleClass = "attributes";
	private String yes = "Y";

	private String includeAttributeTypes = "";

	private boolean displayOnly = false;

	// ----------------------------------------------------------- Properties

	// ------------------------------------------------------- Public Methods

	/**
	 * Defer our checking until the end of this tag is encountered.
	 * 
	 * @exception JspException
	 *                if a JSP exception has occurred
	 */
	public int doStartTag() throws JspException {

		String contextPath = ((HttpServletRequest) this.pageContext.getRequest()).getContextPath();
		String FORWARD_SLASH = "/";
		String href = "/editSkuDetails.html?car_id=%1$s&sku_id=%2$s&upc=%3$s";
		String delSkuHref = "/mainMenu.html?param=delSku&car=%1$s&skuId=%2$s";
		User user = (User) pageContext.getRequest().getAttribute("user") ;
		if (StringUtils.isNotBlank(contextPath) && !contextPath.equals(FORWARD_SLASH)) {
			if (href.startsWith(FORWARD_SLASH)) {
				href = contextPath + href;
			}

			if (delSkuHref.startsWith(FORWARD_SLASH)) {
				delSkuHref = contextPath + delSkuHref;
			}
		}
		StringBuffer strB = new StringBuffer();
		// strB.append(this.href).append("?key=").append(key).append("&section=").append(section);
		// this.href = strB.toString();
		//		
		// write(String.format(format, this.href, this.title,
		// this.title, this.cssStyle));
		Car car = (Car) pageContext.findAttribute(var);

		// TODO: This is where we need to find a way to sequence the attribute
		// TODO: Need to add the Display SEQ Logic to the Process
		// Currently Display SEQ are not setable via the Attribute Associaion
		// Process
		ArrayList<String> displayList = new ArrayList<String>();
		boolean readOnly = false;
                if (this.curDisplayType == DisplayType.CAR_ATTRIBUTE) {
			ArrayList<CarAttribute> attrList = new ArrayList<CarAttribute>(car.getCarAttributes());
			Comparator gc = new GenericComparator("attribute.attributeConfig.displayName");
			Collections.sort(attrList, gc);
			displayList = new ArrayList<String>(car.getCarAttributes().size());
			for (CarAttribute carAttr : attrList) {
				if (carAttr.getAttribute().isActive()) {
					if (StringUtils.isBlank(includeAttributeTypes) || StringUtils.contains(includeAttributeTypes, carAttr.getAttribute().getAttributeType().getAttrTypeCd())) {
						String controlId = "carAttribute:" + carAttr.getCarAttrId();
						String formattedDisplayStr = null;
						/*if (log.isDebugEnabled())
							log.debug(" source type:"+car.getSourceType().getSourceTypeCd());
						*/
						if (isDisplayOnly()) {
								formattedDisplayStr = displayAsText(carAttr.getAttribute(), carAttr.getAttrValue());
						} else {
							
							if(SourceType.PYG.equals(car.getSourceType().getSourceTypeCd())){
								formattedDisplayStr = displayAsHtmlControlforDBPromotion(carAttr, controlId, readOnly);
							}else if(SourceType.OUTFIT.equals(car.getSourceType().getSourceTypeCd())){
								//added new method for outfit to combine child cars attribute values and display as HTML elements 
								formattedDisplayStr = displayAsHtmlControlforOutfit(carAttr, controlId, readOnly);
							}else{
								formattedDisplayStr = displayAsHtmlControl(carAttr.getAttribute(), carAttr.getAttrValue(), controlId, readOnly);
							}
						}
						displayList.add(formattedDisplayStr);
					}
				}
			}

			// Output Formatting
			// Need to support 2 Column Or 1 Column OR 3 Column Display
			// TODO: Need to work with Ricky to figure out how he want to do it
			strB.append("<ul class=\"" + styleClass + "\">");
			for (String displayStr : displayList) {
				strB.append("<li>").append(displayStr).append("</li>");
			}
			strB.append("</ul>");

		} else if (this.curDisplayType == DisplayType.CAR_SKU_ATTRIBUTE) {

			//Get Unique Colors
			VendorStyle style = car.getVendorStyle();
			/*List<VendorStyle> styles = new ArrayList<VendorStyle>();
			if (style.isPattern()) {
				Set<VendorStyle> setStyles = style.getChildVendorStyles();
				if (setStyles != null) {
					styles.addAll(setStyles);
					Comparator gc = new GenericComparator("vendorStyleNumber");
					Collections.sort(styles, gc);
				}
			} else {
				styles.add(style);
			}*/
			List<VendorStyle> styles = car.getVendorStyles() ;
			Comparator gcVendorStyle = new GenericComparator("vendorStyleNumber");
			Collections.sort(styles, gcVendorStyle);
			if (isDisplayOnly()) {
				if (style.isPattern()) {
					strB.append("<ul><li>");
					int styleCount = 0;
					for (VendorStyle tStyle : styles) {
						//strB.append("<h2>").append("Vendor Style Information").append("</h2>");
						strB.append("<ul class=\"" + styleClass + "_style\">");
						strB.append("<li>").append("<label>Style #: ").append(tStyle.getVendorStyleNumber()).append("</label>").append("&emsp;&emsp;&emsp;");
						if(tStyle.getOrinNumber()!= null){
						strB.append("<label>Style ORIN #: ").append(tStyle.getOrinNumber()).append("</label>").append("&emsp;&emsp;&emsp;");
						}else{
							strB.append("<label>Style ORIN #: ").append("&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;").append("</label>").append("&emsp;&emsp;&emsp;");	
						}
						strB.append("<li>").append("<label>Style Name: ").append(tStyle.getVendorStyleName()).append("</label>").append("</li><br/>");
						strB
								.append("<li>")
								.append(
										"<label>Start Date:</label>")
								.append(
										"<div class='startdate'><input type=\"text\" class=\"restricted date globalDate_"
												+ styleCount
												+ "\"   name=\"globalStartDate_"
												+ styleCount
												+ "\" maxlength=\"100\"/></div>")
								.append(
										"<input type=\"button\" class=\"btn\" name=\"applyAllGlobalDate_"
												+ styleCount
												+ "\" value=\"ApplyGlobalDate_"
												+ styleCount
												+ "\"   id=\"globalDate_"
												+ styleCount
												+ "\"   />")
								.append("</li>");
						
						strB.append("</ul>");
						styleCount++;
                                                
					}
					strB.append("</li></ul>");
				}
			} else {
				int styleCount = 0;
				for (VendorStyle tStyle : styles) {
					//strB.append("<h2>").append("Vendor Style Information").append("</h2>");
					strB.append("<ul class=\"" + styleClass + "_style\">");
					strB.append("<li>").append("<label>Style #: ").append(tStyle.getVendorStyleNumber()).append("</label>").append("&emsp;&emsp;&emsp;");
					if(tStyle.getOrinNumber()!= null){
					strB.append("<label>Style ORIN #: ").append(tStyle.getOrinNumber()).append("</label>").append("&emsp;&emsp;&emsp;");
					}else{
						strB.append("<label>Style ORIN #: ").append("&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;").append("</label>").append("&emsp;&emsp;&emsp;");
					}
					if(!(style.isPattern())){ //if style size is 1 then its not pattern product so Style Name is non Editable
						strB.append("<li>").append("<label>Style Name: ").append(tStyle.getVendorStyleName()).append("</label> ").append("</li><br/>");
					}else{
						//isPattern()true? So its pattern Product. Style names are made editable.
							strB.append("<BR>");
							strB.append("<li>")
							.append(
									"<div class='styleNameLabel'><label>Style Name:</label></div> ")
							.append(
									"<div class='editStyleNameInput'><input type=\"text\" class=\"EditableStyleName_"
											+ styleCount
											+ "\"   name=\"EditableStyleName_"
											+ styleCount
											+"\"   value=\""+tStyle.getVendorStyleName()+"\""
											+ "\" maxlength=\"100\"/></div>")
							.append("</li>");
							strB.append("<BR>");
					}
					if (!isDisplayOnly() && user != null
							&& (user.isBuyer())) {
						strB
								.append("<li>")
								.append(
										"<div class='startdatelabel'><label>Start Date:</label></div>")
								.append(
										"<div class='startdate'><input type=\"text\" class=\"restricted date globalDate_"
												+ styleCount
												+ "\"   name=\"globalStartDate_"
												+ styleCount
												+ "\" maxlength=\"100\"/></div>")
								.append(
										"<input type=\"button\" class=\"btn\" name=\"applyAllGlobalDate\" value=\"Apply To All\"    id=\"globalDate_"
												+ styleCount
												+ "\"  />")
								.append("</li>");
						
					}
					strB.append("</ul>");

					styleCount++;

					ArrayList<String> colors = new ArrayList<String>();
					Map<String, List<VendorSku>> colorMap = new HashMap<String, List<VendorSku>>();
					String colorCodeDisplayName = "Color";
					String colorNameDisplayName = "Color Name";
					String shadeNameDisplayName = "Shade description";
					for (VendorSku vendorSku : car.getVendorStyleMap().get(tStyle)) {
						String colorCode = vendorSku.getColorCode();
						if (StringUtils.isNotBlank(colorCode)) {
							List<VendorSku> colorNameAttributeList = colorMap.get(colorCode);
							if (colorNameAttributeList == null) {
								colors.add(colorCode);
								colorNameAttributeList = new ArrayList<VendorSku>();
								colorNameAttributeList.add(vendorSku);
								colorMap.put(colorCode, colorNameAttributeList);
							} else {
								colorNameAttributeList.add(vendorSku);
								colorMap.put(colorCode, colorNameAttributeList);
							}
						}
					}

					//Sort the Color List
					Collections.sort(colors);

					StringBuffer colorStrB = new StringBuffer();
					if (!colors.isEmpty()) {
						colorStrB.append("<h2>").append("Colors").append("</h2>");
						colorStrB.append("<ul class=\"" + styleClass + "_color\">");
						for (String colorCode : colors) {
							colorStrB.append("<li>");
							colorStrB.append("<ul>");
							colorStrB.append("<li>").append("<label>").append(colorCodeDisplayName).append("</label>");
							//if (isDisplayOnly())
							//	colorStrB.append(":");

							colorStrB.append(colorCode).append("</li>");
							List<VendorSku> colorNameAttributeList = colorMap.get(colorCode);

							if (!colorNameAttributeList.isEmpty()) {
								VendorSku colorNameAttribute = colorNameAttributeList.get(0);
								StringBuffer carSkuColorNameControlBuf = new StringBuffer("carSkuColorName:").append(colorNameAttribute.getVendorStyle().getVendorStyleId()).append(":").append(colorCode);
								//StringBuffer carSkuColorAttrIdsControlBuf = new StringBuffer("carSkuColorAttributeIds:").append(colorCode);
								//StringBuffer carSkuColorAttrIds = new StringBuffer() ;
								//for (CarSkuAttribute attr:colorNameAttributeList) {
								//	carSkuColorAttrIds.append(attr.getCarSkuAttrId()).append("_");
								//}
								//for (VendorSku sku: colorNameAttributeList) {
								//	carSkuColorAttrIds.append(sku.getCarSkuId()).append("_");
								//}
								StringBuffer carSkuswatchDescControlBuf = new StringBuffer(
										"carSkuSwatchDesc:")
										.append(colorNameAttribute
												.getCarSkuId());

								colorStrB.append("<li>").append("<label>").append(colorNameDisplayName).append("</label>");
								if (isDisplayOnly()) {
									//colorStrB.append(colorNameAttribute.getAttrValue());
									colorStrB.append(
												HtmlEncoder.encode(StringUtils.defaultString(colorNameAttribute.getColorName())));

								} else {
									colorStrB
										.append("<input type=\"text\" name=\"")
										.append(carSkuColorNameControlBuf.toString())
										.append("\" value=\"")
										.append(
											HtmlEncoder.encode(StringUtils.defaultString(colorNameAttribute.getColorName())))
										.append("\" maxlength=\"50\"/>");
									//.append("<input type=\"hidden\" name=\"").append(carSkuColorAttrIdsControlBuf.toString()).append("\" value=\"").append(carSkuColorAttrIds.toString()).append("\"/>");
								}
								colorStrB.append("</li>");
								/* RAM -- START */

								List<VendorSku> skus = car.getVendorStyleMap()
										.get(tStyle);
								String strShadeDescription = null;
								boolean isShadeDescription = false;
								for (VendorSku vendorSku : skus) {
									if (vendorSku.getColorCode() != null
											&& vendorSku.getColorCode().equals(
													colorCode)) {
										isShadeDescription = true;
										break;
									}
									strShadeDescription = displayShadeDescription(
											car, colorNameAttribute);
								}
								if (isShadeDescription == true) {

									colorStrB.append("<li>").append("<label>")
											.append(shadeNameDisplayName)
											.append("</label>");

									String previousColorCode = null;
									String textBoxType = "hidden";
									for (VendorSku vendorSku : skus) {
										if (vendorSku.getColorCode() != null
												&& vendorSku.getColorCode()
														.equals(colorCode)) {
											if (colorCode
													.equals(previousColorCode)) {
												textBoxType = "hidden";
												carSkuswatchDescControlBuf = new StringBuffer(
														"carSkuSwatchDescH:")
														.append(vendorSku
																.getCarSkuId());
											} else {
												textBoxType = "text";
												carSkuswatchDescControlBuf = new StringBuffer(
														"carSkuSwatchDescT:")
														.append(vendorSku
																.getCarSkuId());
											}
											strShadeDescription = displayShadeDescription(
													car, vendorSku);
											colorStrB
													.append(
															"<input type=\""
																	+ textBoxType
																	+ "\" name=\"")
													.append(
															carSkuswatchDescControlBuf
																	.toString())
													.append("\" class=\""+colorCode+", trackshadedesc\"")
													.append("\" value=\"")
													.append(
															HtmlEncoder
																	.encode(StringUtils
																			.defaultString(strShadeDescription)))
													.append(
															"\" maxlength=\"100\"/>");
											previousColorCode = colorCode;
										}
									}

									colorStrB.append("</li>");
									/* RAM -- END */
								}

							}
							colorStrB.append("</ul>");
							colorStrB.append("</li>");
						}
						colorStrB.append("</ul>");
					}

					strB.append(colorStrB);

					// displayList = new
					// ArrayList<String>(car.getCarAttributes().size());
					// TODO: Need to figure out MULTIPLE VENDOR/MULTIPLE SKU ATTRIBUTES
					strB.append("<h2>").append("Sizes").append("</h2>");
					strB.append("<ul class=\"" + styleClass + "_size\">");

					List<VendorSku> skus = car.getVendorStyleMap().get(tStyle) ;//new ArrayList<VendorSku>(tStyle.getVendorSkus());
					Comparator gc1 = new GenericComparator("colorCode");
					Collections.sort(skus, gc1);

					//Comparator gc = new GenericComparator("attribute.attributeConfig.displayName");
					for (VendorSku vendorSku : skus) {
						displayList = new ArrayList();
						String controlIdColorCode = "carSkuColorCode:" + vendorSku.getCarSkuId();//skuAttr.getCarSkuAttrId();
						String controlIdSizeCode = "carSkuSizeCode:" + vendorSku.getCarSkuId();//skuAttr.getCarSkuAttrId();
						//String controlIdSize = "carSkuSizeName:" + vendorSku.getCarSkuId();//skuAttr.getCarSkuAttrId();
						String controlIdName = "carSkuItemName:" + vendorSku.getCarSkuId();//skuAttr.getCarSkuAttrId();
						String controlIdIDBSize = "carSkuIDBSizeName:" + vendorSku.getCarSkuId(); //CARS Size Conversion Issue - Size name overwritten by resync size job
						String controlIdSkuStartDate = "carSkuStartDate:"
								+ vendorSku.getCarSkuId();

						if (!isDisplayOnly() && user != null && ( user.isBuyer())) {
							displayList.add(String.format("<label>%1$s</label> %2$s", "Color", 
									String.format(
											"<input type=\"text\" name=\"%1$s\"  class=\""+vendorSku.getBelkUpc()+", smalltext colorcode trackupdate \" onkeypress=\"return onlyNumber(event);\" id=\"%1$s\" value=\"%2$s\" maxlength=\"3\" size=\"3\"/>",  
											controlIdColorCode, 
											StringUtils.defaultString(vendorSku.getColorCode()))));
						} else {
						    displayList.add(String.format("<label>%1$s</label> %2$s", "Color", StringUtils.defaultString(vendorSku.getColorCode())));
					    }
						// Begin added for Faceted Navigation CARS
						displayList.add(displaySuperColors(car,vendorSku));
						// End added for Faceted Navigation CARS	
						if (!isDisplayOnly() && user != null && user.isBuyer()){
							displayList.add(String.format("<br/><label>%1$s</label> %2$s", "Size Code", 
									String.format(
											"<input type=\"text\" name=\"%1$s\"  class=\""+vendorSku.getBelkUpc()+", smalltext trackupdate \" id=\"%1$s\" value=\"%2$s\" maxlength=\"5\" size=\"5\"/>", 
											controlIdSizeCode, 
											StringUtils.defaultString(vendorSku.getSizeCode()))));
						} else {
							//displayList.add(String.format("<label>%1$s</label> %2$s", "Color", StringUtils.defaultString(vendorSku.getColorCode())));
							displayList.add(String.format("<label>%1$s</label> %2$s", "Size Code", StringUtils.defaultString(vendorSku.getSizeCode())));
						}						

						//CARS Size Conversion Issue - Size name overwritten by resync size job-->
						//Adding new field "Size" in car edit page which shows the actual size name which we received from IDB system.
						//User can update this size name, if any rule satisfies this size name the conversion size name will be applied for it in "Conversion Size" field in car edit page.
						if (isDisplayOnly()) {
														displayList.add(String.format("<label>%1$s</label> %2$s", "Size", HtmlEncoder.encode(StringUtils.defaultString(vendorSku.getIdbSizeName()))));
						} else {
							//formattedDisplayStr = displayAsHtmlControl(attr, skuAttr.getAttrValue(), controlId, readOnly);

							displayList.add(String.format("<label>%1$s</label> %2$s", "Size", String.format(
									"<input type=\"text\" name=\"%1$s\"  class=\""+vendorSku.getBelkUpc()+", text recommended trackupdate \" id=\"%1$s\" value=\"%2$s\" maxlength=\"100\"/>", controlIdIDBSize, HtmlEncoder.encode(StringUtils.defaultString(vendorSku.getIdbSizeName())))));
						}
						
						/*
						 * Commented below code since business team don't want this field to be displayed in cars edit page.
						//Just changed "Size" label name as "Conversion Size" but still the resync job will update the size_name column of vendor_sku table.
						//Changed the label name since its being updated by resync size job with conversion size name value
						if (isDisplayOnly()) {
							displayList.add(String.format("<label>%1$s</label> %2$s", "Conversion Size", HtmlEncoder.encode(StringUtils.defaultString(vendorSku.getSizeName()))));
						} else {
							//formattedDisplayStr = displayAsHtmlControl(attr, skuAttr.getAttrValue(), controlId, readOnly);

							displayList.add(String.format("<label>%1$s</label> %2$s", "Conversion Size", String.format(
									"<input type=\"text\" name=\"%1$s\"  class=\"text recommended\" id=\"%1$s\" value=\"%2$s\" disabled=\"disabled\" maxlength=\"100\"/>", controlIdSize, HtmlEncoder.encode(StringUtils.defaultString(vendorSku.getSizeName())))));
						}
						*/
						
						if (isDisplayOnly()) {
							displayList.add(String.format("<label>%1$s</label> %2$s", "Item Name", HtmlEncoder.encode(StringUtils.defaultString(vendorSku.getName()))));
						} else {
							//formattedDisplayStr = displayAsHtmlControl(attr, skuAttr.getAttrValue(), controlId, readOnly);

							displayList.add(String.format("<label>%1$s</label> %2$s", "Item Name", String.format(
									"<input type=\"text\" name=\"%1$s\"  class=\""+vendorSku.getBelkUpc()+", text recommended trackupdate \" id=\"%1$s\" value=\"%2$s\"  maxlength=\"100\"/>", controlIdName, HtmlEncoder.encode(StringUtils.defaultString(vendorSku.getName())))));
						}
						String skuStartDateVal = null;

						if (tStyle.getGlobalStartDate() == null
								|| ("" + tStyle.getGlobalStartDate())
										.equals("")) {
							skuStartDateVal = displaySkuStartDate(vendorSku);
						} else {
							if (tStyle.getGlobalStartDate() != null
									&& !tStyle.getGlobalStartDate().equals(
											"")) {
								skuStartDateVal = tStyle
										.getGlobalStartDate();
							}
						}

						if (isDisplayOnly() || !(user.isBuyer()) ) {
							//							
							displayList
									.add(String
											.format(
													"<br/><div class='startdatelabel'><label>%1$s</label></div> %2$s",
													"Start Date",
													HtmlEncoder
															.encode(StringUtils
																	.defaultString(skuStartDateVal))));
						} else {

							

							displayList
									.add(String
											.format(
													"<br/><div class='startdatelabel'><label>%1$s</label></div> %2$s",
													"Start Date",
													String
															.format(
																	"<div class='startdate'><input id=\"%1$s\" type=\"text\" class=\""+vendorSku.getBelkUpc()+", restricted date trackstartdate startDate_"
																	+ styleCount+
																	"\"  name=\"%1$s\" value=\"%2$s\"  maxlength=\"100\"/></div>",
																	controlIdSkuStartDate,
																	HtmlEncoder
																			.encode(StringUtils
																					.defaultString(skuStartDateVal)))));
						}

						// Output Formatting
						// Need to support 2 Column Or 1 Column OR 3 Column Display
						// TODO: Need to work with Ricky to figure out how he want to do it
						strB.append("<li><h4>Belk SKU - " + vendorSku.getBelkUpc() + "</h4>");
						if(vendorSku.getOrinNumber()!= null)
							strB.append("<h4>ORIN # - " + vendorSku.getOrinNumber() + "</h4>");
						else
							strB.append("<h4>ORIN # - " + " "+ "</h4>");
						strB.append("<ul class=\"attrs\">");
						for (String displayStr : displayList) {
							strB.append("<li>").append(displayStr).append("</li>");
						}
						String url = String.format(href, car.getCarId(), vendorSku.getCarSkuId(), vendorSku.getBelkUpc());
						String delSkuUrl = String.format(delSkuHref, car.getCarId(), vendorSku.getCarSkuId());
						strB.append("<li>").append("<a href=\"" + url + "\" class=\"edit_sku_details\">" + "Edit" + "</a>");
						if (user != null && user.isBuyer()) {
							strB.append("&nbsp;<a href=\"" + delSkuUrl + "\" class=\"del_sku\" onClick=\"return confirm('Are you sure you want to Delete this SKU');\">" + "Delete" + "</a>");
						}
						strB.append("</li>");
						strB.append("</ul>");
						if (vendorSku.getCarSkuAttributes() != null && !vendorSku.getCarSkuAttributes().isEmpty()) {
							ArrayList<CarSkuAttribute> skuAttrList = new ArrayList<CarSkuAttribute>(vendorSku.getCarSkuAttributes());
							Comparator gc = new GenericComparator("attribute.attributeConfig.displayName");
							Collections.sort(skuAttrList, gc);
							strB.append("<ul class=\"attr_exceptions\">");
							for(CarSkuAttribute skuAttr: skuAttrList) {
								if (skuAttr.getAttribute().isActive() && !Constants.ATTR_SUPERCOLOR1.equals(skuAttr.getAttribute().getName()) 
										  && !Constants.ATTR_SUPERCOLOR2.equals(skuAttr.getAttribute().getName()) 
										  && !Constants.ATTR_SUPERCOLOR3.equals(skuAttr.getAttribute().getName()) 
										  && !Constants.ATTR_SUPERCOLOR4.equals(skuAttr.getAttribute().getName()) 
											  ) {
									strB.append("<li><label>")
										.append(skuAttr.getAttribute().getAttributeConfig().getDisplayName())
										.append("</label>")
										.append(
											HtmlEncoder.encode(StringUtils.defaultString(skuAttr.getAttrValue())))
										.append("</li>");
								}
							}
							strB.append("</ul>");
						}
                        // Displaying vendor sku pim attributes that will be inserted as part of the new
                        // Cars integration with PIM project.
                        Set<VendorSkuPIMAttribute> pimAttributes = vendorSku.getSkuPIMAttributes();
                        if (pimAttributes != null && !pimAttributes.isEmpty()) {
                            strB.append("<h4>Sku PIM Attributes: </h4>");
                            strB.append("<ul class=\"attr_exceptions\">");
                            for (VendorSkuPIMAttribute skuPIMAttribute : pimAttributes) {
                                if (StringUtils.isNotEmpty(skuPIMAttribute.getAttributeValue())) {
                                    strB.append("<li><label>").append(skuPIMAttribute.getPimAttributeDetails().getName())
                                    .append("</label>")
                                    .append(HtmlEncoder.encode(StringUtils.defaultString(skuPIMAttribute.getAttributeValue())))
                                    .append("</li>");
                                }
                            }
    						strB.append("</ul>");
						}
						strB.append("</li>");
                    }
					strB.append("</ul>");
				}
			}
		}else if (this.curDisplayType == DisplayType.PIM_ATTRIBUTE) {

			//This block of code is added to display the PIM attributes for the CAR
			List<VendorStyle> styles = car.getVendorStyles() ;
			Comparator gcVendorStyle = new GenericComparator("vendorStyleNumber");
			Collections.sort(styles, gcVendorStyle);
			String formattedDisplayStr = null;
			for(VendorStyle style: styles){				
				strB.append("<div class=\"pimAttrStyleDiv\">");				
				strB.append("Style #: " +style.getVendorStyleNumber());
				strB.append("</div></br>");
				strB.append("<ul class=\"pimAttrStyleUl\">");
				
				Set<VendorStylePIMAttribute> stylePIMAttributeSet = style.getVendorStylePIMAttribute();
				for(VendorStylePIMAttribute stylePIMAttribute : stylePIMAttributeSet){
					PIMAttribute pimAttr = stylePIMAttribute.getPimAttribute();
					if(yes.equals(pimAttr.getIsPimAttrDisplayble())){
						if(stylePIMAttribute.getValue() == null || stylePIMAttribute.getValue().equals(""))
							continue;
						// no longer showing pim attrs in red.
						strB.append("<li class=\"pimAttrStyleLi\">");
						formattedDisplayStr = displayAsText(stylePIMAttribute.getPimAttribute() , stylePIMAttribute.getValue());
						strB.append(formattedDisplayStr);
						strB.append("</li>");
						
					}
				}
				
				strB.append("</ul>");
				//strB.append("<div class \"abc\">/");				
			}
		}

		this.write(strB.toString());

		this.release();

		return (SKIP_BODY);

	}

	private String displayAsText(Attribute attribute, String defaultValue) {
		String labelFormat = "<label>%1$s</label>";
		StringBuffer strB = new StringBuffer();
		if (StringUtils.isBlank(defaultValue)) {
			defaultValue = "";
		}

		String label = String.format(labelFormat, attribute.getAttributeConfig().getDisplayName());
		//strB.append(label).append(": ").append(HtmlEncoder.encode(defaultValue));
		strB.append(label).append(": ").append(defaultValue);
		return strB.toString();
	}

	private String displayAsText(PIMAttribute pimAttribute, String defaultValue) {
		String labelFormat = "<label class=\"pimAttrLabel\">%1$s</label>";
		StringBuffer strB = new StringBuffer();
		if (StringUtils.isBlank(defaultValue)) {
			defaultValue = "";
		}
		String label = String.format(labelFormat, pimAttribute.getName());
		//strB.append(label).append(": ").append(HtmlEncoder.encode(defaultValue));
		strB.append(label).append("<b>:</b> ").append(defaultValue);
		
		return strB.toString();
	}
	
	private String displayAsHtmlControl(Attribute attribute, String defaultValue, String controlId, boolean readOnly) {
		if (log.isDebugEnabled())
			log.debug("Starting to process display attribute: " + attribute.getName());
		long startTime = System.currentTimeMillis();


		
		StringBuffer strB = new StringBuffer();
		AttributeConfig attrConfig = attribute.getAttributeConfig();
		HtmlDisplayType displayType = attrConfig.getHtmlDisplayType();
		StringBuffer inputB = new StringBuffer();
		// recommended for validating the attribute is required or not
		String recommended="";		
		String labelFormat = "<label >%1$s  </label>";
		String label = String.format(labelFormat, attrConfig.getDisplayName());
		/*if (log.isDebugEnabled()){
			log.debug("Label: "+attrConfig.getDisplayName() +" ....Required:"+attribute.getIsRequired());
		}*/
		// if the attribute is required set recommended and add * to the label
		if("Y".equals(attribute.getIsRequired()))
		{
			recommended="recommended";
			labelFormat = "<label>%1$s  *</label>";
			label = String.format(labelFormat, attrConfig.getDisplayName());
		}
		
		GenericComparator gc = null;
		
		if (StringUtils.isBlank(defaultValue)) {
			defaultValue = "";
		}

		if (displayType.isCheckbox()) {
			
		
			Comparator lookupComparator = new GenericComparator("value");
			
			ArrayList<AttributeLookupValue> lookupList = new ArrayList<AttributeLookupValue>(attribute.getAttributeLookupValues());
			
			Collections.sort(lookupList, lookupComparator);

			String[] selectedValues = StringUtils.split(defaultValue, HtmlDisplayType.CHECKBOX_VALUE_DELIMITER);
			Set<String> selectedValueSet = new HashSet<String>() ;
			if (selectedValues != null && selectedValues.length > 0) {
				for(String value: selectedValues)
					selectedValueSet.add(StringUtils.trim(value)) ;
			}
			inputB.append("<ul class=\"attr_checkbox \">") ;
			
			
			for (AttributeLookupValue lookupVal : lookupList) {
				String selectedNotation = "";
				if (selectedValueSet.contains(lookupVal.getValue())) {
					selectedNotation = "checked=\"checked\"";
				}
				inputB.append("<li>");
				inputB.append(String.format("<input type=\"checkbox\" class=\"checkbox " + recommended	+ " \" name=\"%1$s\" value=\"%2$s\" %3$s/>%4$s", controlId, lookupVal
						.getValue(), selectedNotation, lookupVal.getValue()));
				inputB.append("</li>");
			}
			// Handling the N/A for the check boxes
			String selectNA="";
			if("N/A".equals(defaultValue)){
				selectNA= "checked=\"checked\"";
			}
			inputB.append("<li>");
			inputB.append(String.format("<input type=\"checkbox\" class=\"checkbox " +  recommended
					+ " \" name=\"%1$s\" value=\"%2$s\" %3$s/>%4$s", controlId, "N/A",selectNA,"N/A"));
			inputB.append("</li>");
			
			inputB.append("</ul>") ;
		} else if (displayType.isRadioButton()) {
			Comparator lookupComparator = new GenericComparator("value");
			ArrayList<AttributeLookupValue> lookupList = new ArrayList<AttributeLookupValue>(attribute.getAttributeLookupValues());
			Collections.sort(lookupList, lookupComparator);
			for (AttributeLookupValue lookupVal : lookupList) {
				String selectedNotation = "";
				if (lookupVal.getValue().equals(defaultValue)) {
					selectedNotation = "checked";
				}
				inputB.append(String.format("<input type=\"radio\" class=\"radio "+recommended+ " \" name=\"%1$s\" value=\"%2$s\" %3$s/>%4$s", controlId, lookupVal
						.getValue(), selectedNotation, lookupVal.getValue()));
			}

		} else if (displayType.isDropdown() || displayType.isAutocomplete()) {
			String styleClass = displayType.isDropdown() ? "select" : "autocomplete";
			inputB.append(String.format("<select name=\"%1$s\" class=\"%2$s "+ recommended + "\" id=\"%1$s\">", controlId, styleClass));
            Comparator lookupComparator = new GenericComparator("value");
			ArrayList<AttributeLookupValue> lookupList = new ArrayList<AttributeLookupValue>(attribute.getAttributeLookupValues());
			Collections.sort(lookupList, lookupComparator);
			boolean foundSelected = false;
			String defauleSelectText = "";
			
			
			inputB.append("<option value=\"\">").append(defauleSelectText).append("</option>");
			for (AttributeLookupValue lookupVal : lookupList) {
				String selectedNotation = "";
				if (lookupVal.getValue().equals(defaultValue)){  
					selectedNotation = "selected=\"selected\"";
					foundSelected = true;
				} 
				inputB.append(String.format("<option value=\"%1$s\" %2$s>%3$s</option>", HtmlEncoder.encode(lookupVal.getValue()), selectedNotation, lookupVal.getValue()));
			}
			if (!foundSelected && displayType.isAutocomplete() && !"".equals(defaultValue)) {
				// for autocomplete, if the current value is not in the list then we need to add it as selected
				inputB.append(String.format("<option value=\"%1$s\" %2$s=\"%2$s\">%3$s</option>", defaultValue, "selected", defaultValue));
			}
			// Handling the N/A for the drop down 
			String selectNA="";
			if ("N/A".equals(defaultValue)){  
				selectNA = "selected=\"selected\"";
			}
			inputB.append(String.format("<option value=\"%1$s\" %2$s>%3$s</option>", "N/A", selectNA, "N/A"));
			inputB.append("</select>");

		} else if (displayType.isHtmlEditor()) {
			inputB.append(String.format("<textarea cols=\"20\" rows=\"10\"  class=\"textarea spellcheck "+recommended+" \" name=\"%1$s\" id=\"%1$s\">%2$s</textarea>",
					controlId, HtmlEncoder.encode(StringUtils.defaultString(defaultValue))));
		} else {
			if (readOnly) {
				inputB.append(String.format("<input type=\"text\" name=\"%1$s\"  class=\"text readonly\" id=\"%1$s\" value=\"%2$s\" readonly=\"readonly\"/>",
						controlId, HtmlEncoder.encode(StringUtils.defaultString(defaultValue))));
			} else {
					inputB.append(String.format("<input type=\"text\" name=\"%1$s\"  class=\"text  "+recommended+" \" id=\"%1$s\" value=\"%2$s\"/>", controlId,
						HtmlEncoder.encode(StringUtils.defaultString(defaultValue))));

			}
		}
		strB.append(label).append(inputB.toString());

		if (log.isDebugEnabled())
			log.debug("End processing display attribute: " + attribute.getName() + " time taken: " + (System.currentTimeMillis() - startTime) + " ms");

		return strB.toString();
	}

	/**
	 * Return default exception message.
	 */
	protected String getNoSuchMessageExceptionDescription(NoSuchMessageException ex) {
		return ex.getMessage();
	}

	private void write(String text) {
		try {
			pageContext.getOut().write(text);
		} catch (IOException ioex) {

		}
	}

	/**
	 * Release any acquired resources.
	 */
	public void release() {

		super.release();
		this.var = null;
		this.scope = null;
		this.displayType = "CAR_ATTRIBUTE";
		this.displayOnly = false;
		this.includeAttributeTypes = "";
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		if (DisplayType.CAR_ATTRIBUTE.name().equals(displayType)) {
			this.curDisplayType = DisplayType.CAR_ATTRIBUTE;
		} else if (DisplayType.CAR_SKU_ATTRIBUTE.name().equals(displayType)) {
			this.curDisplayType = DisplayType.CAR_SKU_ATTRIBUTE;
		}else if(DisplayType.PIM_ATTRIBUTE.name().equals(displayType)) {
			this.curDisplayType = DisplayType.PIM_ATTRIBUTE;
		}else {
			this.curDisplayType = DisplayType.CAR_ATTRIBUTE;
		}
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getIncludeAttributeTypes() {
		return includeAttributeTypes;
	}

	public void setIncludeAttributeTypes(String includeAttributeTypes) {
		this.includeAttributeTypes = includeAttributeTypes;
	}

	public boolean isDisplayOnly() {
		return displayOnly;
	}

	public void setDisplayOnly(boolean displayOnly) {
		this.displayOnly = displayOnly;
	}

	private String displayAsHtmlControlforOutfit(CarAttribute carAttr, String controlId, boolean readOnly) {
		Attribute attribute=carAttr.getAttribute();
		String defaultValue=carAttr.getAttrValue();
		if (log.isDebugEnabled()){
			log.debug("Starting to process display attribute: " + attribute.getName() +" defaultValue:"+defaultValue +" control id:"+controlId );
		}
		long startTime = System.currentTimeMillis();
		
		// recommended for validating the attribute is required or not
		String recommended="";		
		String labelFormat = "<label >%1$s  </label>";
		AttributeConfig attrConfig = attribute.getAttributeConfig();
		HtmlDisplayType displayType = attrConfig.getHtmlDisplayType();
		String label = String.format(labelFormat, attrConfig.getDisplayName());
		if (log.isDebugEnabled())
			log.debug("Label: "+attrConfig.getDisplayName() +" ....Required:"+attribute.getIsRequired());
		// if the attribute is required set recommended and add * to the label
		if("Y".equals(attribute.getIsRequired()))
		{
			recommended="recommended";
			labelFormat = "<label>%1$s  *</label>";
			label = String.format(labelFormat, attrConfig.getDisplayName());
		}
		StringBuffer strB = new StringBuffer();
		StringBuffer inputB = new StringBuffer();
		if (StringUtils.isBlank(defaultValue)) {
			defaultValue = "";
		}
		//check for outfit attributes
		ArrayList<AttributeLookupValue> lookupList = new ArrayList<AttributeLookupValue>();
		Set<OutfitCarAttribute> ofattrSet=carAttr.getOutfitCarAttributes();
		if( ofattrSet!=null && ofattrSet.size()>1 ){
			for(OutfitCarAttribute ofattr:ofattrSet){
					Attribute ofatt=ofattr.getAttribute();
					Set<AttributeLookupValue> attrlookupSet=ofatt.getAttributeLookupValues();
					for(AttributeLookupValue attrlookupValue:attrlookupSet){
						lookupList.add(attrlookupValue);
					}
			}
		}else {
			lookupList = new ArrayList<AttributeLookupValue>(attribute.getAttributeLookupValues());
		}
		
		
		if (log.isDebugEnabled())
			log.debug(" Attribute lookupList: "+lookupList);
		
		// Sorting Set with Remove the duplicates 
		SortedSet<String> lookupSet=new TreeSet<String>();
		for (AttributeLookupValue lookupVal : lookupList) {
			lookupSet.add(lookupVal.getValue());
		}
		
		if (log.isDebugEnabled())
			log.debug(" Attribute lookupSet:"+lookupSet);
		
		if (displayType.isCheckbox()) {
		
				String[] selectedValues = StringUtils.split(defaultValue, HtmlDisplayType.CHECKBOX_VALUE_DELIMITER);
				Set<String> selectedValueSet = new HashSet<String>() ;
				if (selectedValues != null && selectedValues.length > 0) {
					for(String value: selectedValues)
						selectedValueSet.add(StringUtils.trim(value)) ;
				}
				inputB.append("<ul class=\"attr_checkbox\">") ;
				
				for (String chkValue : lookupSet) {
					String selectedNotation = "";
					if (selectedValueSet.contains(chkValue)) {
						selectedNotation = "checked=\"checked\"";
					}
					inputB.append("<li>");
					inputB.append(String.format("<input type=\"checkbox\" class=\"checkbox "+ recommended+"\" name=\"%1$s\" value=\"%2$s\" %3$s/>%4$s", controlId, chkValue, selectedNotation, chkValue));
					inputB.append("</li>");
				}
				// Handling the check box for N/A
				String selectNA="";
				if("N/A".equals(defaultValue)){
					selectNA= "checked=\"checked\"";
				}
				inputB.append("<li>");
				inputB.append(String.format("<input type=\"checkbox\" class=\"checkbox " +  recommended
						+ " \" name=\"%1$s\" value=\"%2$s\" %3$s/>%4$s", controlId, "N/A",selectNA,"N/A"));
				inputB.append("</li>");
				inputB.append("</ul>") ;
			
		} 	else if (displayType.isRadioButton()) {

			for (String rdVal : lookupSet) {
				String selectedNotation = "";
				if (rdVal.equals(defaultValue)) {
					selectedNotation = "checked";
				}
				inputB.append(String.format("<input type=\"radio\" class=\"radio"+ recommended+"\" name=\"%1$s\" value=\"%2$s\" %3$s/>%4$s", controlId, rdVal, selectedNotation, rdVal));
			}

		} else if (displayType.isDropdown() || displayType.isAutocomplete()) {
			String styleClass = displayType.isDropdown() ? "select" : "autocomplete";
			inputB.append(String.format("<select name=\"%1$s\" class=\"%2$s  recommended\" id=\"%1$s\">", controlId, styleClass));
			boolean foundSelected = false;
			String defauleSelectText = "";
			inputB.append("<option value=\"\">").append(defauleSelectText).append("</option>");
				
			for(String drpVal:lookupSet){
				String selectedNotation = "";
				if(drpVal.equals(defaultValue)){
					selectedNotation = "selected=\"selected\"";
					foundSelected = true;
				}
				inputB.append(String.format("<option value=\"%1$s\" %2$s>%3$s</option>", HtmlEncoder.encode(drpVal), selectedNotation, drpVal));
			}
			
			if (!foundSelected && displayType.isAutocomplete() && !"".equals(defaultValue)) {
				// for autocomplete, if the current value is not in the list then we need to add it as selected
				inputB.append(String.format("<option value=\"%1$s\" %2$s=\"%2$s\">%3$s</option>", defaultValue, "selected", defaultValue));
			}
			// Handling the N/A for the drop down 
			String selectNA="";
			if ("N/A".equals(defaultValue)){  
				selectNA = "selected=\"selected\"";
			}
			inputB.append(String.format("<option value=\"%1$s\" %2$s>%3$s</option>", "N/A", selectNA, "N/A"));
			inputB.append("</select>");

		} else if (displayType.isHtmlEditor()) {
			inputB.append(String.format("<textarea cols=\"20\" rows=\"10\"  class=\"textarea spellcheck\" name=\"%1$s\" id=\"%1$s\">%2$s</textarea>",
					controlId, HtmlEncoder.encode(StringUtils.defaultString(defaultValue))));
		} else {
			if (readOnly) {
				inputB.append(String.format("<input type=\"text\" name=\"%1$s\"  class=\"text readonly\" id=\"%1$s\" value=\"%2$s\" readonly=\"readonly\"/>",
						controlId, HtmlEncoder.encode(StringUtils.defaultString(defaultValue))));
			} else {
				inputB.append(String.format("<input type=\"text\" name=\"%1$s\"  class=\"text recommended\" id=\"%1$s\" value=\"%2$s\"/>", controlId,
						HtmlEncoder.encode(StringUtils.defaultString(defaultValue))));

			}
		}
		strB.append(label).append(inputB.toString());

		if (log.isDebugEnabled()){
			log.debug("End processing display attribute: " + attribute.getName() + " time taken: " + (System.currentTimeMillis() - startTime) + " ms");
		}

		return strB.toString();
	}
	

	
	
	private String displayAsHtmlControlforDBPromotion(CarAttribute carAttr, String controlId, boolean readOnly) {
		Attribute attribute=carAttr.getAttribute();
		String defaultValue=carAttr.getAttrValue();
		if (log.isDebugEnabled()){
			log.debug("Starting to process Promotions display attribute: " + attribute.getName() +" defaultValue:"+defaultValue +" control id:"+controlId );
		}
		long startTime = System.currentTimeMillis();
		
		// recommended for validating the attribute is required or not
		String recommended="";		
		String labelFormat = "<label >%1$s  </label>";
		AttributeConfig attrConfig = attribute.getAttributeConfig();
		HtmlDisplayType displayType = attrConfig.getHtmlDisplayType();
		String label = String.format(labelFormat, attrConfig.getDisplayName());
		if (log.isDebugEnabled())
			log.debug("Label: "+attrConfig.getDisplayName() +" ....Required:"+attribute.getIsRequired());
		// if the attribute is required set recommended and add * to the label
		if("Y".equals(attribute.getIsRequired()))
		{
			recommended="recommended";
			labelFormat = "<label>%1$s  *</label>";
			label = String.format(labelFormat, attrConfig.getDisplayName());
		}
		StringBuffer strB = new StringBuffer();
		StringBuffer inputB = new StringBuffer();
		if (StringUtils.isBlank(defaultValue)) {
			defaultValue = "";
		}
		//check for outfit attributes
		ArrayList<AttributeLookupValue> lookupList = new ArrayList<AttributeLookupValue>();
		Set<DBPromotionCarAttribute> promoAttrSet=carAttr.getDbPromotionCarAttributes();
		if( promoAttrSet!=null && promoAttrSet.size()>1 ){
			for(DBPromotionCarAttribute ofattr:promoAttrSet){
					Attribute ofatt=ofattr.getAttribute();
					Set<AttributeLookupValue> attrlookupSet=ofatt.getAttributeLookupValues();
					for(AttributeLookupValue attrlookupValue:attrlookupSet){
						lookupList.add(attrlookupValue);
					}
			}
		}else {
			lookupList = new ArrayList<AttributeLookupValue>(attribute.getAttributeLookupValues());
		}
		
		
		if (log.isDebugEnabled())
			log.debug(" Attribute lookupList: "+lookupList);
		
		// Sorting Set with Remove the duplicates 
		SortedSet<String> lookupSet=new TreeSet<String>();
		for (AttributeLookupValue lookupVal : lookupList) {
			lookupSet.add(lookupVal.getValue());
		}
		
		if (log.isDebugEnabled())
			log.debug(" Attribute lookupSet:"+lookupSet);
		
		if (displayType.isCheckbox()) {
		
				String[] selectedValues = StringUtils.split(defaultValue, HtmlDisplayType.CHECKBOX_VALUE_DELIMITER);
				Set<String> selectedValueSet = new HashSet<String>() ;
				if (selectedValues != null && selectedValues.length > 0) {
					for(String value: selectedValues)
						selectedValueSet.add(StringUtils.trim(value)) ;
				}
				inputB.append("<ul class=\"attr_checkbox\">") ;
				
				for (String chkValue : lookupSet) {
					String selectedNotation = "";
					if (selectedValueSet.contains(chkValue)) {
						selectedNotation = "checked=\"checked\"";
					}
					inputB.append("<li>");
					inputB.append(String.format("<input type=\"checkbox\" class=\"checkbox "+ recommended+"\" name=\"%1$s\" value=\"%2$s\" %3$s/>%4$s", controlId, chkValue, selectedNotation, chkValue));
					inputB.append("</li>");
				}
				// Handling the check box for N/A
				String selectNA="";
				if("N/A".equals(defaultValue)){
					selectNA= "checked=\"checked\"";
				}
				inputB.append("<li>");
				inputB.append(String.format("<input type=\"checkbox\" class=\"checkbox " +  recommended
						+ " \" name=\"%1$s\" value=\"%2$s\" %3$s/>%4$s", controlId, "N/A",selectNA,"N/A"));
				inputB.append("</li>");
				inputB.append("</ul>") ;
			
		} 	else if (displayType.isRadioButton()) {

			for (String rdVal : lookupSet) {
				String selectedNotation = "";
				if (rdVal.equals(defaultValue)) {
					selectedNotation = "checked";
				}
				inputB.append(String.format("<input type=\"radio\" class=\"radio"+ recommended+"\" name=\"%1$s\" value=\"%2$s\" %3$s/>%4$s", controlId, rdVal, selectedNotation, rdVal));
			}

		} else if (displayType.isDropdown() || displayType.isAutocomplete()) {
			String styleClass = displayType.isDropdown() ? "select" : "autocomplete";
			inputB.append(String.format("<select name=\"%1$s\" class=\"%2$s  recommended\" id=\"%1$s\">", controlId, styleClass));
			boolean foundSelected = false;
			String defauleSelectText = "";
			inputB.append("<option value=\"\">").append(defauleSelectText).append("</option>");
				
			for(String drpVal:lookupSet){
				String selectedNotation = "";
				if(drpVal.equals(defaultValue)){
					selectedNotation = "selected=\"selected\"";
					foundSelected = true;
				}
				inputB.append(String.format("<option value=\"%1$s\" %2$s>%3$s</option>", HtmlEncoder.encode(drpVal), selectedNotation, drpVal));
			}
			
			if (!foundSelected && displayType.isAutocomplete() && !"".equals(defaultValue)) {
				// for autocomplete, if the current value is not in the list then we need to add it as selected
				inputB.append(String.format("<option value=\"%1$s\" %2$s=\"%2$s\">%3$s</option>", defaultValue, "selected", defaultValue));
			}
			// Handling the N/A for the drop down 
			String selectNA="";
			if ("N/A".equals(defaultValue)){  
				selectNA = "selected=\"selected\"";
			}
			inputB.append(String.format("<option value=\"%1$s\" %2$s>%3$s</option>", "N/A", selectNA, "N/A"));
			inputB.append("</select>");

		} else if (displayType.isHtmlEditor()) {
			inputB.append(String.format("<textarea cols=\"20\" rows=\"10\"  class=\"textarea spellcheck\" name=\"%1$s\" id=\"%1$s\">%2$s</textarea>",
					controlId, HtmlEncoder.encode(StringUtils.defaultString(defaultValue))));
		} else {
			if (readOnly) {
				inputB.append(String.format("<input type=\"text\" name=\"%1$s\"  class=\"text readonly\" id=\"%1$s\" value=\"%2$s\" readonly=\"readonly\"/>",
						controlId, HtmlEncoder.encode(StringUtils.defaultString(defaultValue))));
			} else {
				inputB.append(String.format("<input type=\"text\" name=\"%1$s\"  class=\"text recommended\" id=\"%1$s\" value=\"%2$s\"/>", controlId,
						HtmlEncoder.encode(StringUtils.defaultString(defaultValue))));

			}
		}
		strB.append(label).append(inputB.toString());

		if (log.isDebugEnabled()){
			log.debug("End processing display attribute: " + attribute.getName() + " time taken: " + (System.currentTimeMillis() - startTime) + " ms");
		}

		return strB.toString();
	}
	
	/*
		 * Added for Faceted Navigation CARS
		 */
		private String displaySuperColors(Car car,VendorSku vendorSku){
			
			long skuId=vendorSku.getCarSkuId();
			Map<String,String> superColors=new HashMap<String,String>();
			
			Boolean isSuperColor1Set =false;
			StringBuffer sp1=new StringBuffer();
			superColors=car.getSuperColors();
					
			if (log.isDebugEnabled()){
				log.debug("constructing super colors");
			}
			
			String selectedNotation = "";
			String ColorName1="";
			String ColorName2="";
			String ColorName3="";
			String ColorName4="";
			String hiddenSuperColor1="";
			if (vendorSku.getCarSkuAttributes() != null && !vendorSku.getCarSkuAttributes().isEmpty()) {
				ArrayList<CarSkuAttribute> skuAttrList = new ArrayList<CarSkuAttribute>(vendorSku.getCarSkuAttributes());
				for(CarSkuAttribute skuAttr: skuAttrList) {
					if (Constants.ATTR_SUPERCOLOR1.equals(skuAttr.getAttribute().getName())){
						ColorName1=skuAttr.getAttrValue();
					} else if (Constants.ATTR_SUPERCOLOR2.equals(skuAttr.getAttribute().getName())){
						ColorName2=skuAttr.getAttrValue();
					} else if (Constants.ATTR_SUPERCOLOR3.equals(skuAttr.getAttribute().getName())){
						ColorName3=skuAttr.getAttrValue();
					} else if (Constants.ATTR_SUPERCOLOR4.equals(skuAttr.getAttribute().getName())){
						ColorName4=skuAttr.getAttrValue();
					}
				}
			}
			
			sp1.append(String.format("<br/><label>%1$s</label> %2$s", "Super Color1",
					String.format("<select name=\"%1$s\" class=\""+vendorSku.getBelkUpc()+", exclusiveSColorSelection superColor1 tracksupercolor \" disabled=\"disabled\" style=\"width:100px\" id=\"%1$s\">", "SuperColor1:"+skuId)));
			sp1.append(String.format("<option value ='"+Constants.INDEX_NOTSELECTED+"'>Select</option>")); 
			Iterator it1 = superColors.entrySet().iterator(); 
			
			while(it1.hasNext()){
					 Map.Entry superColor = (Map.Entry)it1.next();
					 if(superColor.getValue().equals(ColorName1)){
						 //superColor1 value from db would be considered to be set only if latest superColor1 value on sku based on color code is not being set ; else just ignore as follows. 
						 if(!isSuperColor1Set){
							 selectedNotation="selected=\"selected\"";
							 hiddenSuperColor1= superColor.getValue().toString(); //to post a value set hidden field for super Color1 since combo box is disabled
							 }
					 } else {
						 if(superColor.getValue().equals(vendorSku.getFirstSuperColor())){
							 selectedNotation="selected=\"selected\"";
							 hiddenSuperColor1= superColor.getValue().toString(); //to post a value set hidden field for super Color1 since combo box is disabled 
							 isSuperColor1Set=true;  					//now in an iteration superColor1 value from database which might get set on sku would be skipped in above 'if' part of this loop ;ie after setting here and later in next any iteration if it's finding a superColor1's match with DB's value saved previously then that db val will not be set
						 }
					 }
					 sp1.append(String.format("<option value=\"%1$s\" %2$s>%3$s</option>", HtmlEncoder.encode(superColor.getValue().toString()), selectedNotation, superColor.getValue()));
					 selectedNotation="";
			}
			sp1.append("</select>");
			sp1.append(String.format("<input type=\"hidden\" name=\"%1$s\" class=\"hiddenSuperColor1\" value=\""+hiddenSuperColor1+"\" style=\"width:100px\" id=\"%1$s\">", "SuperColor1:"+skuId));
			
			sp1.append(String.format("&nbsp;&nbsp;&nbsp;<label>%1$s</label> %2$s", "Super Color2",
						String.format("<select name=\"%1$s\" class=\""+vendorSku.getBelkUpc()+", exclusiveSColorSelection superColor2 tracksupercolor \" style=\"width:100px\"  id=\"%1$s\">", "SuperColor2:"+skuId)));
			sp1.append(String.format("<option value ='-1'>Select</option>")); 
			Iterator it2 = superColors.entrySet().iterator();
			while(it2.hasNext()){
						 Map.Entry superColor = (Map.Entry)it2.next();
						 if(superColor.getValue().equals(ColorName2)){
							 selectedNotation="selected=\"selected\"";
						 }else {
							 selectedNotation = "";
						 }
						 sp1.append(String.format("<option value=\"%1$s\" %2$s>%3$s</option>", HtmlEncoder.encode(superColor.getValue().toString()), selectedNotation, superColor.getValue()));
			}
			sp1.append("</select>");
			
			
			sp1.append(String.format("&nbsp;&nbsp;&nbsp;<label>%1$s</label> %2$s", "Super Color3",
					String.format("<select name=\"%1$s\" class=\""+vendorSku.getBelkUpc()+", exclusiveSColorSelection superColor3 tracksupercolor \" style=\"width:100px\"  id=\"%1$s\">", "SuperColor3:"+skuId)));
			sp1.append(String.format("<option value ='-1'>Select</option>")); 
			Iterator it3 = superColors.entrySet().iterator();
			while(it3.hasNext()){
						 Map.Entry superColor = (Map.Entry)it3.next();
						 if(superColor.getValue().equals(ColorName3)){
							 selectedNotation="selected=\"selected\"";
						 } else {
							 selectedNotation = "";
						 }
						 sp1.append(String.format("<option value=\"%1$s\" %2$s>%3$s</option>", HtmlEncoder.encode(superColor.getValue().toString()), selectedNotation, superColor.getValue()));
			}
			sp1.append("</select>");
		
			
			sp1.append(String.format("&nbsp;&nbsp;&nbsp;<label>%1$s</label> %2$s", "Super Color4",
					String.format("<select name=\"%1$s\" class=\""+vendorSku.getBelkUpc()+", exclusiveSColorSelection superColor4 tracksupercolor \" style=\"width:100px\"  id=\"%1$s\">", "SuperColor4:"+skuId)));
			sp1.append(String.format("<option value ='-1'>Select</option>")); 
			Iterator it4 = superColors.entrySet().iterator();
			while(it4.hasNext()){
						 Map.Entry superColor = (Map.Entry)it4.next();
						 if(superColor.getValue().equals(ColorName4)){
							 selectedNotation="selected=\"selected\"";
						 }else{
							 selectedNotation = "";
						 }
						 sp1.append(String.format("<option value=\"%1$s\" %2$s>%3$s</option>", HtmlEncoder.encode(superColor.getValue().toString()), selectedNotation, superColor.getValue()));
						 
			}
			sp1.append("</select>");
			
			return sp1.toString();
			
		}
	/**
	 * This method used to display the 
	 * swatch description of sku. It perform following steps: 1.get the list of 
	 * sku attribute .2. get the swatch description for each sku.
	 * 
	 * @param car        It contain car object.
	 * @param vendorSku  It contain vendor sku object
	 * @return string containing swatch description.
	 */
	private String displayShadeDescription(Car car, VendorSku vendorSku) {

		if (log.isDebugEnabled()) {
			log.debug("constructing Swatch Description");
		}

		String shadeDescription = "";
		if (vendorSku.getCarSkuAttributes() != null
				&& !vendorSku.getCarSkuAttributes().isEmpty()) {
			ArrayList<CarSkuAttribute> skuAttrList = new ArrayList<CarSkuAttribute>(
					vendorSku.getCarSkuAttributes());			
			for (CarSkuAttribute skuAttr : skuAttrList) {
				if (Constants.ATTR_SHADEDESC.equals(skuAttr.getAttribute()
						.getName())) {
					shadeDescription = skuAttr.getAttrValue();					
				}
			}
		}

		return shadeDescription;
	}

	/**
	 * This method used to display the 
	 * start date of sku. It perform following steps: 1.get the list of sku attribute. 
	 * 2. Get the start date for each sku. 
	 *  
	 * @param vendorSku It contain vendor sku object
	 * @return skuStartDate It return date string.
	 */
	private String displaySkuStartDate(VendorSku vendorSku) {

		if (log.isDebugEnabled()) {
			log.debug("constructing Swatch Description");
		}

		String skuStartDate = "";
		if (vendorSku.getCarSkuAttributes() != null
				&& !vendorSku.getCarSkuAttributes().isEmpty()) {
			ArrayList<CarSkuAttribute> skuAttrList = new ArrayList<CarSkuAttribute>(
					vendorSku.getCarSkuAttributes());			
			for (CarSkuAttribute skuAttr : skuAttrList) {
				if (Constants.ATTR_SKU_START_DATE.equals(skuAttr.getAttribute()
						.getName())) {
					skuStartDate = skuAttr.getAttrValue();
				}
			}
		}

		return skuStartDate;
	}

}

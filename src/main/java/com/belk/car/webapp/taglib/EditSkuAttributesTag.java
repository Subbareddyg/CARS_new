package com.belk.car.webapp.taglib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.NoSuchMessageException;

import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.AttributeConfig;
import com.belk.car.app.model.AttributeLookupValue;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarSkuAttribute;
import com.belk.car.app.model.HtmlDisplayType;
import com.belk.car.app.model.VendorSku;
import com.belk.car.util.GenericComparator;
import com.lowagie.text.html.HtmlEncoder;

/**
 * Generate the Attribute List
 * 
 * @author vsundar
 */

public final class EditSkuAttributesTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -295829024131320960L;

	private transient final Log log = LogFactory
	.getLog(EditSkuAttributesTag.class);

	// --------------------------------------------------- Instance Variables

	private String carVar = null;

	private String skuVar = null;

	private String scope = null;
	
	private String styleClass = "attributes";
	
	private String includeAttributeTypes="PRODUCT";
	
	private boolean displayOnly=false ;

	// ----------------------------------------------------------- Properties

	// ------------------------------------------------------- Public Methods

	/**
	 * Defer our checking until the end of this tag is encountered.
	 * 
	 * @exception JspException
	 *                if a JSP exception has occurred
	 */
	public int doStartTag() throws JspException {

		StringBuffer strB = new StringBuffer();

		VendorSku vs = (VendorSku) pageContext.findAttribute(skuVar);
		Car car = null ;
		if (StringUtils.isNotBlank(carVar)) {
			car = (Car) pageContext.findAttribute(carVar);
		} else {
			car = vs.getCar() ;
		}

		ArrayList<String> displayList = new ArrayList<String>();
		boolean readOnly = false ;
		ArrayList<CarAttribute> attrList = new ArrayList<CarAttribute>(car.getCarAttributes()) ;
		
		ArrayList<CarSkuAttribute> skuAttrList = new ArrayList<CarSkuAttribute>(vs.getCarSkuAttributes());
		Map<Long, CarSkuAttribute> skuAttrMap = vs.getAttributeMap();
		
		if (isDisplayOnly() && !skuAttrMap.isEmpty()) {
			displayList = new ArrayList<String>(skuAttrMap.size());
			Comparator gc = new GenericComparator("attribute.attributeConfig.displayName");
			Collections.sort(skuAttrList, gc);
			for (CarSkuAttribute carSkuAttr: skuAttrList) {
				if (carSkuAttr.getAttribute().isActive()) {
					String attrValue = carSkuAttr.getAttrValue();
					if (skuAttrMap.containsKey(new Long(carSkuAttr.getAttribute().getAttributeId())))
						attrValue = skuAttrMap.get(new Long(carSkuAttr.getAttribute().getAttributeId())).getAttrValue();
					String formattedDisplayStr = displayAsText(carSkuAttr
							.getAttribute(), attrValue);
					displayList.add(formattedDisplayStr);
				}
			}
		} else {
			displayList = new ArrayList<String>(car.getCarAttributes().size());
			Comparator gc = new GenericComparator("attribute.attributeConfig.displayName");
			Collections.sort(attrList, gc);
			for (CarAttribute carAttr : attrList) {
				if (carAttr.getAttribute().isActive()) {
					if (StringUtils.isBlank(includeAttributeTypes)
							|| StringUtils.contains(includeAttributeTypes, carAttr.getAttribute()
									.getAttributeType().getAttrTypeCd())) {
						String controlId = "attribute:" + carAttr.getAttribute().getAttributeId();
						String attrValue = carAttr.getAttrValue();
						readOnly = true ;
						if (skuAttrMap.containsKey(new Long(carAttr.getAttribute().getAttributeId()))) {
							attrValue = skuAttrMap.get(new Long(carAttr.getAttribute().getAttributeId())).getAttrValue();
							readOnly = false ;
						}
		
						String formattedDisplayStr = displayAsHtmlControl(carAttr
									.getAttribute(), attrValue, controlId,
									readOnly);
						displayList.add(formattedDisplayStr);
					}
				}
			}
		}
		
		// Displaying the dropship attributes at sku level 
		if (vs.getCarSkuAttributes() != null && !vs.getCarSkuAttributes().isEmpty()) {
				ArrayList<CarSkuAttribute> carSkuAtrList = new ArrayList<CarSkuAttribute>(vs.getCarSkuAttributes());
				Comparator gc = new GenericComparator("attribute.attributeConfig.displayName");
				Collections.sort(carSkuAtrList, gc);
				for(CarSkuAttribute skuAttr: carSkuAtrList) {
					if (skuAttr.getAttribute().isActive() && ("SDF_Online Only".equals(skuAttr.getAttribute().getName())
							|| ("IS_DROPSHIP".equals(skuAttr.getAttribute().getName())))) {
						String controlId = "attribute:" + skuAttr.getAttribute().getAttributeId();
						String formattedDisplayStr = displayAsHtmlControl(skuAttr.getAttribute(), skuAttr.getAttrValue(), controlId,
								true);
							displayList.add(formattedDisplayStr);
					}
				}
		}

		// Output Formatting
		// Need to support 2 Column Or 1 Column OR 3 Column Display
		// TODO: Need to work with Ricky to figure out how he want to do it
		strB.append("<ul class=\""+styleClass+"\">");
		for (String displayStr : displayList) {
			strB.append("<li>").append(displayStr).append("</li>");
		}
		strB.append("</ul>");

		this.write(strB.toString());

		this.release();

		return (SKIP_BODY);

	}

	private String displayAsText(Attribute attribute, String defaultValue) {
		String labelFormat = "<label>%1$s</label>";
		StringBuffer strB = new StringBuffer() ;
		if (StringUtils.isBlank(defaultValue)) {
			defaultValue = "";
		}

		String label = String.format(labelFormat,attribute.getAttributeConfig().getDisplayName());
		
		strB.append(label).append(": ").append(HtmlEncoder.encode(defaultValue));
		
		return strB.toString();
	}

	private String displayAsHtmlControl(Attribute attribute, String defaultValue, String controlId, boolean readOnly) {
		if (log.isDebugEnabled())
			log.debug("Starting to process display attribute: " + attribute.getName()) ;
		long startTime = System.currentTimeMillis() ;

		//String labelFormat = "<label for=\"txt_%1$s\">%2$s</label>";
		String labelFormat = "<label>%2$s %1$s  *</label>";
		StringBuffer strB = new StringBuffer();
		AttributeConfig attrConfig = attribute.getAttributeConfig();
		HtmlDisplayType displayType = attrConfig.getHtmlDisplayType();
		StringBuffer inputB = new StringBuffer();

		// disable the is dropship sku level attribute
		String disable="";
		if("IS_DROPSHIP".equals(attribute.getName())){
			disable="disabled";
		}
		String chkBox = "<input type=\"checkbox\" name=\"skuAttributeCheck:" + attribute.getAttributeId() + "\" value=\"" + attribute.getAttributeId() + 
		                "\"" + (readOnly?"":" checked=\"checked\"") + disable + " class=\"skuAttrCheckbox\" />" ;
		String inpBox = "<input type=\"hidden\" name=\"skuAttributeInp\" value=\"" + attribute.getAttributeId() + "\"/>" ;
		String label = String.format(labelFormat,attrConfig.getDisplayName(), chkBox);
		if (!readOnly) {
			label = label + inpBox ;
		}
		GenericComparator gc = null;
		if (StringUtils.isBlank(defaultValue)) {
			defaultValue = "";
		}
		if (displayType.isRadioButton()) {
			Comparator lookupComparator = new GenericComparator("value");
			ArrayList<AttributeLookupValue> lookupList = new ArrayList<AttributeLookupValue>(attribute.getAttributeLookupValues());
			Collections.sort(lookupList, lookupComparator);
			for (AttributeLookupValue lookupVal : lookupList) {
				String selectedNotation = "";
				if (lookupVal.getValue().equals(defaultValue)) {
					selectedNotation = "checked";
				}
				String lookupValue = HtmlEncoder.encode(StringUtils.defaultString(lookupVal.getValue()));
				inputB
						.append(String
								.format(
										"<input type=\"radio\" class=\"radio recommended attrVal\" name=\"%1$s\" value=\"%2$s\" %3$s %5$s/>%4$s",
										controlId, lookupValue, selectedNotation,
										lookupValue, (readOnly?"disabled=\"disabled\"":"")));
		   }

		} else if (displayType.isDropdown() || displayType.isAutocomplete()) {
			String styleClass = displayType.isDropdown()?"select":"autocomplete";
			inputB.append(String.format("<select name=\"%1$s\" class=\"%2$s  recommended attrVal\" id=\"%1$s\" %3$s>",
					controlId, styleClass,(readOnly?"disabled=\"disabled\"":"")));
			Comparator lookupComparator = new GenericComparator("value");
			ArrayList<AttributeLookupValue> lookupList = new ArrayList<AttributeLookupValue>(attribute.getAttributeLookupValues());
			Collections.sort(lookupList, lookupComparator);
			boolean foundSelected=false;
			String defauleSelectText = "";
			inputB.append("<option value=\"\">").append(defauleSelectText).append("</option>");
			for (AttributeLookupValue lookupVal : lookupList) {
				String selectedNotation = "";
				if (lookupVal.getValue().equals(defaultValue)) {
					selectedNotation = "selected=\"selected\"";
					foundSelected=true;
				}
				String lookupValue = HtmlEncoder.encode(StringUtils.defaultString(lookupVal.getValue()));
				inputB.append(String.format(
						"<option value=\"%1$s\" %2$s>%3$s</option>", lookupValue, selectedNotation, lookupValue));
			}
			if(!foundSelected && displayType.isAutocomplete() && !"".equals(defaultValue)) {
				// for autocomplete, if the current value is not in the list then we need to add it as selected
				inputB.append(String.format(
						"<option value=\"%1$s\" %2$s=\"%2$s\">%3$s</option>", HtmlEncoder.encode(StringUtils.defaultString(defaultValue)), "selected", HtmlEncoder.encode(StringUtils.defaultString(defaultValue))));
			}
			inputB.append("</select>");

		} else if (displayType.isHtmlEditor()) {
			inputB
					.append(String
							.format(
									"<textarea cols=\"20\" rows=\"10\"  class=\"textarea spellcheck attrVal\" name=\"%1$s\" id=\"%1$s\" %3$s>%2$s</textarea>",
									controlId, HtmlEncoder.encode(StringUtils.defaultString(defaultValue)), (readOnly?"disabled=\"disabled\"":"")));
		} else {
				inputB
				.append(String
						.format(
								"<input type=\"text\" name=\"%1$s\"  class=\"text recommended attrVal\" id=\"%1$s\" value=\"%2$s\" %3$s/>",
								controlId, HtmlEncoder.encode(StringUtils.defaultString(defaultValue)), (readOnly?"disabled=\"disabled\"":"")));
				
		}
		strB.append(label).append(inputB.toString());

		if (log.isDebugEnabled())
			log.debug("End processing display attribute: " + attribute.getName() + " time taken: " + (System.currentTimeMillis() - startTime) + " ms") ;
		

		return strB.toString();
	}

	/**
	 * Return default exception message.
	 */
	protected String getNoSuchMessageExceptionDescription(
			NoSuchMessageException ex) {
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
		this.skuVar = null;
		this.carVar = null;
		this.scope = null;
		this.displayOnly = false;
		this.includeAttributeTypes = "";
	}

	public String getSkuVar() {
		return skuVar;
	}

	public void setSkuVar(String skuVar) {
		this.skuVar = skuVar;
	}

	public String getCarVar() {
		return carVar;
	}

	public void setCarVar(String carVar) {
		this.carVar = carVar;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
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

}

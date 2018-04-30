package com.belk.car.webapp.taglib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import javax.servlet.jsp.JspException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.tags.form.CheckboxesTag;
import org.springframework.web.servlet.tags.form.TagWriter;

import com.belk.car.util.GenericComparator;

/**
 * Generate the Extended Checkboxes tag
 * 
 * @author antoniog
 */

public class ExtendedCheckboxesTag extends CheckboxesTag {

	
	
	private  Object useritems;

	private String sortBy ;

	/**
	 * @return the userItems
	 */
	public Object getUseritems() {
		return useritems;
	}

	/**
	 * @param userItems the userItems to set
	 */
	public void setUseritems(Object useritems) {
		Assert.notNull(useritems, "'useritems' must not be null");
		this.useritems = useritems;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	protected int writeTagContent(TagWriter tagWriter) throws JspException {
		Object items = getItems();
		Object itemsObject = (items instanceof String ? evaluate("items", (String) items) : items);
		Object userItems = getUseritems();
		Object userItemsObject = (items instanceof String ? evaluate("useritems", (String) useritems) : useritems);
		

		String itemValue = getItemValue();
		String itemLabel = getItemLabel();
		String valueProperty =
				(itemValue != null ? ObjectUtils.getDisplayString(evaluate("itemValue", itemValue)) : null);
		String labelProperty =
				(itemLabel != null ? ObjectUtils.getDisplayString(evaluate("itemLabel", itemLabel)) : null);

		if (itemsObject == null) {
			throw new IllegalArgumentException("Attribute 'items' is required and must be a Collection");
		}
		
		if (userItemsObject == null) {
			throw new IllegalArgumentException("Attribute 'useritems' is required and must be a Collection");
		}

		if (!(itemsObject instanceof Collection)) {
			throw new IllegalArgumentException("Attribute 'items' must be a Collection");
		}
		
		if (!(itemsObject instanceof Collection)) {
			throw new IllegalArgumentException("Attribute 'useritems' must be a Collection");
		}

		final Collection optionCollection = (Collection) itemsObject;
		final Collection userOptionCollection = (Collection) userItemsObject;
		
		Collection filteredCollection = CollectionUtils.disjunction(optionCollection, userOptionCollection);
		
		if (StringUtils.isNotBlank(sortBy)) {
			ArrayList l = new ArrayList(filteredCollection);
			Comparator gc = new GenericComparator(sortBy);
			Collections.sort(l, gc);
			filteredCollection = l;
		}
		
		int itemIndex = 0;
		for (Iterator it = filteredCollection.iterator(); it.hasNext(); itemIndex++) {
			Object item = it.next();
			writeObjectEntry(tagWriter, valueProperty, labelProperty, item, itemIndex);
		}
		
		if (!isDisabled()) {
			// Write out the 'field was present' marker.
			tagWriter.startTag("input");
			tagWriter.writeAttribute("type", "hidden");
			tagWriter.writeAttribute("name", WebDataBinder.DEFAULT_FIELD_MARKER_PREFIX + getName());
			tagWriter.writeAttribute("value", "on");
			tagWriter.endTag();
		}

		return EVAL_PAGE;
	}

	private void writeObjectEntry(TagWriter tagWriter, String valueProperty,
			String labelProperty, Object item, int itemIndex) throws JspException {

		BeanWrapper wrapper = new BeanWrapperImpl(item);
		Object renderValue = (valueProperty != null ? wrapper.getPropertyValue(valueProperty) : item);
		Object renderLabel = (labelProperty != null ? wrapper.getPropertyValue(labelProperty) : item);
		writeCheckboxTag(tagWriter, renderValue, renderLabel, itemIndex);
	}


	private void writeCheckboxTag(TagWriter tagWriter, Object value, Object label, int itemIndex) throws JspException {
		tagWriter.startTag(getElement());
		if (itemIndex > 0 && this.getDelimiter() != null) {
			tagWriter.appendValue(ObjectUtils.getDisplayString(evaluate("delimiter", this.getDelimiter())));
		}
		tagWriter.startTag("input");
		writeDefaultAttributes(tagWriter);
		tagWriter.writeAttribute("type", "checkbox");
		renderSingleValue(value, tagWriter);
		tagWriter.appendValue(label.toString());
		tagWriter.endTag();
		tagWriter.endTag();
	}
	
	@Override
	public void release() {
		super.release() ;
		this.useritems = null ;
		this.sortBy = null ;
	}
	
}
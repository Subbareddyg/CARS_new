package com.belk.car.util;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Date;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.taglibs.standard.tag.el.core.ExpressionUtil;
import org.springframework.web.util.ExpressionEvaluationUtils;

public class GenericComparator implements Comparator {
	String cobjMethodName = null;

	/**
	 * Constructs a new GenericComparator for data objects. The data object must
	 * have a matching getter for the method name.
	 * 
	 * @param String
	 *            objMethodName
	 */

	public GenericComparator(String objMethodName) {
		cobjMethodName = objMethodName;
		//StringBuffer strBuffer = new StringBuffer(objMethodName);
		//strBuffer.setCharAt(0, Character.toUpperCase(strBuffer.charAt(0)));
		//cobjMethodName = "get" + strBuffer.toString();

	}

	/**
	 * Compares its two arguments for order. Returns a negative integer, zero,
	 * or a positive integer as the first argument is less than, equal to, or
	 * greater than the second. Assume both elements are both instances of the
	 * same class.
	 *  @ param o1 - the first object to be compared. @ param o2 - the second
	 * object to be compared. @ return a negative integer, zero, or a positive
	 * integer as the first argument is less than, equal to, or greater than the
	 * second.
	 */

	public int compare(Object o1, Object o2) {
		Method objMethod1 = null;
		Method objMethod2 = null;
		Object object1 = null;
		Object object2 = null;
		Class returnType = null;

		try {
			
//			objMethod1 = o1.getClass()
//					.getMethod(cobjMethodName, new Class[] {});
//			objMethod2 = o2.getClass()
//					.getMethod(cobjMethodName, new Class[] {});
//
//			returnType = objMethod1.getReturnType();
//
//			object1 = objMethod1.invoke(o1, new Object[] {});
//			object2 = objMethod2.invoke(o2, new Object[] {});
			object1 = PropertyUtils.getNestedProperty(o1, cobjMethodName);
			object2 = PropertyUtils.getNestedProperty(o2, cobjMethodName);
			
		}
		// If an error occurs then just take the way out and assume they are
		// equal
		catch (Exception e) {
			return 0;
		}

		if (object1 instanceof java.lang.String) {
			return ((String) object1).compareTo((String) object2);
		}
		else if (object1 instanceof java.lang.Double) {
			return ((Double) object1).compareTo((Double) object2);
		}
		else if (object1 instanceof java.lang.Integer) {
			return ((Integer) object1).compareTo((Integer) object2);
		}
		else if (object1 instanceof java.lang.Long) {
			return ((Long) object1).compareTo((Long) object2);
		}
		else if (object1 instanceof java.lang.Short) {
			return ((Short) object1).compareTo((Short) object2);
		}
		else if (object1 instanceof java.util.Date) {
			return ((Date) object1).compareTo((Date) object2);
		}
		// You gave something that isn't an instance of
		else {
			return 0;
		}
	}

}

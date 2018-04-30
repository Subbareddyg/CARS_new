/**
 * @author afusy07-Priyanka Gadia
 * @Date Mar 2, 2010
 * @TODO Sort the Strings having long value.
 */
package com.belk.car.util;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;


@SuppressWarnings("unchecked")
public class StringToLongComparator implements Comparator  {

	/**
	 * @param arg0 First String object
	 * @param arg1 Second string object
	 * @return int  value based on comparison
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 * @Enclosing_Method  compare
	 * @TODO
	 */
	public int compare(Object obj1, Object obj2) {
		if (obj1 instanceof java.lang.String && obj2 instanceof java.lang.String) {
			if(StringUtils.isNumeric((String) obj1) && StringUtils.isNumeric((String) obj2)){
				long num1=(new Long((String)obj1)).longValue();
				long num2=(new Long((String)obj2)).longValue();
				if (num1 == 0){
					return -1;
				}
				else if (num2 == 0){
					return 1;
				}
				else{
					return new Long(num1).compareTo(new Long(num2));
				}
			}
		}
		return -1;
	}

}

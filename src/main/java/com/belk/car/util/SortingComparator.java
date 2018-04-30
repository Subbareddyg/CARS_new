package com.belk.car.util;
import java.util.Comparator;

import com.belk.car.app.webapp.forms.ChildCar;
/**
 * @author afusxg7
 *
 */
public class SortingComparator implements Comparator {


	public int compare(Object obj1, Object obj2) {
		String num1="null";
		if (obj1 != null && obj1 instanceof ChildCar) {
			ChildCar result1 = (ChildCar) obj1;
			num1 = result1.getSkuID();
		}

		String num2="null";
		if (obj2 != null && obj2 instanceof ChildCar) {
			ChildCar result2 = (ChildCar) obj2;
			num2 = result2.getSkuID();
		}

		if (num1 == "null" || num1 == null)
			return -1;
		else if (num2 == "null" || num2 == null)
			return 1;
		else
			return new String(num1).compareTo(new String(num2));
	}

}
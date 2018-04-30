/**
 * 
 */
package com.belk.car.util;

import java.util.Comparator;

import com.belk.car.app.model.Classification;

/**
 * @author antoniog
 *
 */
public class ClassificationComparator implements Comparator {

	public int compare(Object obj1, Object obj2) {

		int num1 = 0;
		if (obj1 != null && obj1 instanceof Classification) {
			Classification result1 = (Classification) obj1;
			num1 = result1.getBelkClassNumber();
		}

		int num2 = 0;
		if (obj2 != null && obj2 instanceof Classification) {
			Classification result2 = (Classification) obj2;
			num2 = result2.getBelkClassNumber();
		}

		if (num1 == 0)
			return -1;
		else if (num2 == 0)
			return 1;
		else
			return new Integer(num1).compareTo(new Integer(num2));
	}

}

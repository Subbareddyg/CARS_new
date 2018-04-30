/**
 * 
 */
package com.belk.car.util;

import java.util.Comparator;

import com.belk.car.app.model.CarNote;
import com.belk.car.app.model.car.ManualCar;

/**
 * @author antoniog
 *
 */
public class LongComparator implements Comparator {

	public int compare(Object obj1, Object obj2) {

		long num1 = 0;
		if (obj1 != null && obj1 instanceof Long) {
			num1 = ((Long) obj1).longValue();
		}

		long num2 = 0;
		if (obj2 != null && obj2 instanceof Long) {
			num2 = ((Long) obj2).longValue();
		}

		if (num1 == 0)
			return -1;
		else if (num2 == 0)
			return 1;
		else
			return new Long(num1).compareTo(new Long(num2));
	}

}

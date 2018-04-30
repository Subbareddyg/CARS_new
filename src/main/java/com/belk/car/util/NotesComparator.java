/**
 * 
 */
package com.belk.car.util;

import java.util.Comparator;

import com.belk.car.app.model.CarNote;

/**
 * @author antoniog
 *
 */
public class NotesComparator implements Comparator {

	public int compare(Object obj1, Object obj2) {

		long num1 = 0;
		if (obj1 != null && obj1 instanceof CarNote) {
			CarNote result1 = (CarNote) obj1;
			num1 = result1.getCarNoteId();
		}

		long num2 = 0;
		if (obj2 != null && obj2 instanceof CarNote) {
			CarNote result2 = (CarNote) obj2;
			num2 = result2.getCarNoteId();
		}

		if (num1 == 0)
			return -1;
		else if (num2 == 0)
			return 1;
		else
			return new Long(num1).compareTo(new Long(num2));
	}

}

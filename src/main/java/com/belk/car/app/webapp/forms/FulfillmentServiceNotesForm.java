/**
 * 
 */
package com.belk.car.app.webapp.forms;

import com.belk.car.app.model.oma.Address;
import com.belk.car.app.model.oma.FulfillmentService;


/**
 * @author 
 *
 */
public class FulfillmentServiceNotesForm extends FulfillmentService  implements java.io.Serializable {
	
	private static final long serialVersionUID = 6099774321313615467L;

		public FulfillmentService fulfillmentService;

		public Address address;


		public FulfillmentServiceNotesForm(){
			address=new Address();
			fulfillmentService= new FulfillmentService();
			
		}
		
	public FulfillmentService getFulfillmentService() {
		return fulfillmentService;
	}
	public void setFulfillmentService(FulfillmentService fulfillmentService) {
		this.fulfillmentService = fulfillmentService;
	}
	

	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}

}

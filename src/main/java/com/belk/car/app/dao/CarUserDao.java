package com.belk.car.app.dao;

import java.util.List;

import org.appfuse.model.User;


public interface CarUserDao {
	
	/*
	 * Page ,sortedOn and sortedOrder are added for implementing external pagination. 
	 */
	public List<User> searchUsers(String firstName, String lastName, String emailId, String userTypeCd,Integer page,String sortedOn,String sortedOrder);
	
	/*
	 * This method is used for updating/removing(Status update to 'DELETE') vendor or user.
	 */
    public void updateVendorOrUser(User vendor);
    
    /*
	 * This method is used for get vendor or user.
	 */
    public User getVendorOrUser(String userName);
}

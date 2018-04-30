package com.belk.car.app.service;

import java.util.List;

import org.appfuse.model.User;
import org.appfuse.service.UniversalManager;

/**
 * Business Service Interface to talk to persistence layer and retrieve values
 * for drop-down choice lists.
 * 
 */
public interface CarUserManager extends UniversalManager {

	public List<User> searchVendors(String firstName, String lastName, String emailId);
	
	/*
	 *Page ,sortedOn and sortedOrder are added to implement external pagination logic. 
	 */

	public List<User> searchUsers(String firstName, String lastName, String emailId,Integer page,String sortedOn,String sortedOrder);
	
	 /**
     * Removes a user from the database by their userId
     * I was forced to create this method to bypass AOP access logic in the UserManager service class.
     * @param userId the user's id
     */
      
	/*
	 * This method is used for updating/removing(Status update to 'DELETE') vendor or user.
	 */
	
    public void updateVendorOrUser(User vendor);
    
    /*
	 * This method is used for get vendor or user.
	 */
    
    public User getVendorOrUser(String userName);
    
   }

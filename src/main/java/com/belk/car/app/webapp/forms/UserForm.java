package com.belk.car.app.webapp.forms;

import java.util.HashSet;
import java.util.Set;

import org.appfuse.model.User;

import com.belk.car.app.model.Department;
import com.belk.car.app.model.Vendor;

public class UserForm {

	private User user;
	private Set<Department> departments = new HashSet<Department>(0);
	private Set<Vendor> vendors = new HashSet<Vendor>(0);
	private String notes;
	private String noteType;
	private boolean searchUser;//This attribute is added to bypass error throwing on search/DisplayAll button.
	
	
	
	public Set<Department> getDepartments() {
		return departments;
	}
	public void setDepartments(Set<Department> departments) {
		this.departments = departments;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Set<Vendor> getVendors() {
		return vendors;
	}
	public void setVendors(Set<Vendor> vendors) {
		this.vendors = vendors;
	}
	public String getNoteType() {
		return noteType;
	}
	public void setNoteType(String noteType) {
		this.noteType = noteType;
	}
	public boolean getSearchUser() {
		return searchUser;
	}
	public void setSearchUser(boolean searchUser) {
		this.searchUser = searchUser;
	}
}

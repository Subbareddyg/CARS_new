package com.belk.car.app.webapp.forms;

import java.util.HashSet;
import java.util.Set;

import com.belk.car.app.model.Department;
import com.belk.car.app.model.LateCarsGroup;
import com.belk.car.app.model.Vendor;

public class LateCarsAssociationForm extends UserForm  {
	
	private LateCarsGroup lateCarsGroup;
	private Set<Department> departments = new HashSet<Department>(0);
	private Set<Vendor> vendors = new HashSet<Vendor>(0);
	private String notes;
	private String noteType;
	private Boolean search;
	private String lateGroupID;
	
	public String getLateGroupID() {
		return lateGroupID;
	}
	public void setLateGroupID(String lateGroupID) {
		this.lateGroupID = lateGroupID;
	}
	public LateCarsGroup getLateCarsGroup() {
		return lateCarsGroup;
	}
	public void setLateCarsGroup(LateCarsGroup lateCarsGroup) {
		this.lateCarsGroup = lateCarsGroup;
	}
	public Set<Department> getDepartments() {
		return departments;
	}
	public void setDepartments(Set<Department> departments) {
		this.departments = departments;
	}
	public Set<Vendor> getVendors() {
		return vendors;
	}
	public void setVendors(Set<Vendor> vendors) {
		this.vendors = vendors;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getNoteType() {
		return noteType;
	}
	public void setNoteType(String noteType) {
		this.noteType = noteType;
	}
	public Boolean getSearch() {
		return search;
	}
	public void setSearch(Boolean search) {
		this.search = search;
	}
}

/**
 * 
 */
package com.belk.car.app.service.impl;

import java.util.List;

import org.appfuse.service.impl.UniversalManagerImpl;

import com.belk.car.app.dao.WorkflowDao;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarImage;
import com.belk.car.app.model.CarSample;
import com.belk.car.app.model.Sample;
import com.belk.car.app.model.SampleSourceType;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.WorkFlow;
import com.belk.car.app.model.WorkflowTransitionInfo;
import com.belk.car.app.model.workflow.WorkflowStatus;
import com.belk.car.app.model.workflow.WorkflowTask;
import com.belk.car.app.model.workflow.WorkflowTransition;
import com.belk.car.app.model.workflow.WorkflowType;
import com.belk.car.app.service.WorkflowManager;

/**
 * @author antoniog
 *
 */
public class WorkflowManagerImpl extends UniversalManagerImpl implements
		WorkflowManager {

	private WorkflowDao workflowDao;

	/**
	 * @param workflowDao the productDao to set
	 */
	public void setWorkflowDao(WorkflowDao workflowDao) {
		this.workflowDao = workflowDao;
	}

	public List<WorkFlow> getAllWorkFlows() {		
		return workflowDao.getAllWorkFlows();
	}

	public WorkFlow getWorkFlow(Long workflowId) {		
		return workflowDao.getWorkFlow(workflowId);
	}

	public WorkFlow getWorkFlow(WorkflowType type) {		
		return workflowDao.getWorkFlow(type);
	}

	public void remove(WorkFlow workflow) {
		workflowDao.remove(workflow);		
	}

	public WorkFlow save(WorkFlow workFlow) {		
		return workflowDao.save(workFlow);
	}

	public List<WorkflowStatus> getAllWorkFlowStatuses() {		
		return workflowDao.getAllWorkFlowStatuses();
	}

	public List<WorkflowTransition> getAllowedTransitionsForWorkflow(Long workflowId) {		
		return workflowDao.getAllowedTransitionsForWorkflow(workflowId);
	}

	public List<WorkflowTask> getAllWorkFlowTasks() {
		return workflowDao.getAllWorkFlowTasks();
	}

	public WorkflowTransition saveWorkflowTransition(
			WorkflowTransition workflowTransition) {		
		return workflowDao.saveWorkflowTransition(workflowTransition);
	}

	public WorkflowTask getWorkflowTask(String workflowTaskId) {		
		return workflowDao.getWorkflowTask(workflowTaskId);
	}

	public WorkflowStatus getWorkFlowStatus(String workflowStatusCd) {
		return workflowDao.getWorkFlowStatus(workflowStatusCd);
	}

	public WorkFlow deleteWorkflowTransition(WorkFlow workflow,
			WorkflowTransition workflowTransition) {
		
		workflow.removeWorkflowTransition(workflowTransition);
		return workflowDao.save(workflow);
	}

	public List<WorkflowType> getAllWorkflowTypes() {		
		return workflowDao.getAllWorkflowTypes();
	}

	public WorkflowTransition getWorkflowTransition(Long workflowTransitionId) {		
		return workflowDao.getWorkflowTransition(workflowTransitionId);
	}

	public WorkflowType getWorkflowType(String workflowTypeCd) {		
		return workflowDao.getWorkflowType(workflowTypeCd);
	}

	public void removeWorkflowType(WorkflowType workflowType) {
		workflowDao.removeWorkflowType(workflowType);
	}

	public WorkflowType saveWorkflowType(WorkflowType workflowType) {		
		return workflowDao.saveWorkflowType(workflowType);
	}

	public List<UserType> getCurrentUserTypeFromNextWorkflowTransition() {		
		return null;
	}

	public List<WorkflowStatus> getCurrentWorkflowStatusFromNextWorkflowTransition() {		
		return null;
	}

	public WorkflowTransitionInfo getNextWorkFlowTransitionInformation(
			UserType currentUserType, WorkflowStatus currentStatus) {
		return workflowDao.getNextWorkFlowTransitionInformation(currentUserType.getUserTypeCd(), currentStatus.getStatusCd());
	}
	
	public WorkflowTransitionInfo getNextWorkFlowTransitionInformation(
			String currentUserType, String currentStatus) {
		return workflowDao.getNextWorkFlowTransitionInformation(currentUserType, currentStatus);
		
	}
	
	public WorkflowTransitionInfo getNextWorkFlowTransitionInformation(
			String currentUserType, String currentStatus, Car car) {
		WorkflowTransitionInfo wti = workflowDao.getNextWorkFlowTransitionInformation(currentUserType, currentStatus);
		
		if(UserType.BUYER.equals(currentUserType)){
			
			if(isCarHasAllVendorImages(car)){
				wti.removeWorkflowTransitionToUserType(UserType.CONTENT_MANAGER);
			}/*else{
				wti.removeWorkflowTransitionToUserType(UserType.VENDOR_PROVIDED_IMAGE);
			}*/
		  }
		
		return wti;
	}
	
	public boolean isCarHasAllVendorImages(Car car){
		
		if(log.isDebugEnabled()){
			log.debug("Start of checkOnlyVendorImageOrNot method "+ car.getCarId());
		}
		try {
			List<CarImage> carImages=car.getAllActiveCarImages();
			List<CarImage> carVendorImages=car.getCarVendorImages();
			List<CarSample> carSamples=car.getCarSampleList();
			
			if(carImages.size()!= carVendorImages.size()){
				return false;
			}
			for (CarSample cs : carSamples) {
				Sample s = cs.getSample();
				if (!s.getSampleSourceType().getSampleSourceTypeCd()
						.equals(SampleSourceType.NEITHER)) {
					return false;
				}
			}
		}catch(Exception e){
			log.error("Exception in checkOnlyVendorImageOrNot    "+e);
		}
		
		if(log.isInfoEnabled()){
			log.info("Car has all vendor provided images -- car id: "+ car.getCarId());
		}
		return true;
  }
}

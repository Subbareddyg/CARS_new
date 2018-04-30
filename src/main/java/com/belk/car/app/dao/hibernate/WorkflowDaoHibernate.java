package com.belk.car.app.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.appfuse.dao.hibernate.UniversalDaoHibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.belk.car.app.Constants;
import com.belk.car.app.dao.WorkflowDao;
import com.belk.car.app.exceptions.DaoException;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.WorkFlow;
import com.belk.car.app.model.WorkflowTransitionInfo;
import com.belk.car.app.model.workflow.WorkflowStatus;
import com.belk.car.app.model.workflow.WorkflowTask;
import com.belk.car.app.model.workflow.WorkflowTransition;
import com.belk.car.app.model.workflow.WorkflowType;

public class WorkflowDaoHibernate extends UniversalDaoHibernate implements
		WorkflowDao {

	public List<WorkFlow> getAllWorkFlows() {
		return getHibernateTemplate().find("from WorkFlow");
	}

	public WorkFlow getWorkFlow(Long workflowId) {
		return (WorkFlow) getHibernateTemplate()
				.get(WorkFlow.class, workflowId);
	}

	public WorkFlow getWorkFlow(WorkflowType type) {

		List<WorkFlow> l = getHibernateTemplate().find(
				"FROM WorkFlow WHERE workflowType.typeCd = ?",
				new Object[] { type.getTypeCd() });
		if (l != null && !l.isEmpty()) {
			return (WorkFlow) l.get(0);
		}
		return null;
	}

	public void remove(WorkFlow workflow) {
		getHibernateTemplate().delete(workflow);
	}

	public WorkFlow save(WorkFlow workFlow) {
		getHibernateTemplate().saveOrUpdate(workFlow);
        getHibernateTemplate().flush();
		return workFlow;
	}

	public List<WorkflowStatus> getAllWorkFlowStatuses() {
		Session sess = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = sess.createQuery("from WorkflowStatus");
		return query.setCacheable(true).list();
		//return getHibernateTemplate().find("from WorkflowStatus");
	}

	public List<WorkflowTransition> getAllowedTransitionsForWorkflow(
			Long workflowTranId) {
		return getHibernateTemplate()
				.find(
						"select wt from WorkflowTransition wt join fetch wt.workflow"
								+ " where wt.workflow.workflowId = ? order by wt.workflow.name",
						workflowTranId.longValue());
	}

	public List<WorkflowTask> getAllWorkFlowTasks() {
		return getHibernateTemplate().find("from WorkflowTask");
	}

	public WorkflowTransition saveWorkflowTransition(
			WorkflowTransition workflowTransition) {
		getHibernateTemplate().saveOrUpdate(workflowTransition);
        getHibernateTemplate().flush();
		return workflowTransition;
	}

	public WorkflowTask getWorkflowTask(String workflowTaskId) {
		List workflowTaskList = getHibernateTemplate().find(
				"from WorkflowTask wt WHERE wt.taskCd = ?", workflowTaskId);

		if (workflowTaskList == null || workflowTaskList.isEmpty()) {
			throw new DaoException("workflow task '" + workflowTaskId
					+ "' not found...");
		} else {
			return (WorkflowTask) workflowTaskList.get(0);
		}
	}

	public WorkflowStatus getWorkFlowStatus(String workflowStatusCd) {
		List workFlowStatusList = getHibernateTemplate().find(
				"from WorkflowStatus ws WHERE ws.statusCd = ?",
				workflowStatusCd);

		if (workFlowStatusList == null || workFlowStatusList.isEmpty()) {
			throw new DaoException("work flow status '" + workflowStatusCd
					+ "' not found...");
		} else {
			return (WorkflowStatus) workFlowStatusList.get(0);
		}
	}

	public List<WorkflowType> getAllWorkflowTypes() {
		return getHibernateTemplate().find("from WorkflowType");
	}

	public WorkflowTransition getWorkflowTransition(Long workflowTransitionId) {
		return (WorkflowTransition) getHibernateTemplate().get(
				WorkflowTransition.class, workflowTransitionId);
	}

	public WorkflowType getWorkflowType(String workflowTypeCd) {
		List workflowTypeList = getHibernateTemplate().find(
				"from WorkflowType wt WHERE wt.typeCd = ?", workflowTypeCd);

		if (workflowTypeList == null || workflowTypeList.isEmpty()) {
			throw new DaoException("work flow type '" + workflowTypeCd
					+ "' not found...");
		} else {
			return (WorkflowType) workflowTypeList.get(0);
		}
	}

	public void removeWorkflowType(WorkflowType workflowType) {
		getHibernateTemplate().delete(workflowType);
	}

	public WorkflowType saveWorkflowType(WorkflowType workflowType) {
		getHibernateTemplate().saveOrUpdate(workflowType);
        getHibernateTemplate().flush();
		return workflowType;
	}

	public WorkflowTransitionInfo getNextWorkFlowTransitionInformation(
			String currentUserType, String currentStatus) {

		WorkflowTransitionInfo cwi = new WorkflowTransitionInfo();
		HibernateTemplate ht = getHibernateTemplate();

		Object[] args = { currentUserType, currentStatus };
		List<WorkflowTransition> workflowTransitionsList ;
		List<WorkflowTransition> workflowTransitionsFinalList=new ArrayList<WorkflowTransition>() ;
		if(currentUserType.equals(UserType.BUYER)) {
			Object[] argsBuyer = { currentUserType, Constants.READY_FOR_REVIEW_WFS, Constants.INITIATED_WFS };
			//Buyer Waiting for sample Send Empty List 
			
			workflowTransitionsList= ht.find(
					"from WorkflowTransition wt where wt.currentUserType.userTypeCd = ? and (wt.currentStatus.statusCd = ? or wt.currentStatus.statusCd = ? )",
					argsBuyer);
			 
              for(WorkflowTransition wt:workflowTransitionsList) {
            		  
            	  if(currentStatus.equalsIgnoreCase(Constants.WAITING_FOR_SAMPLE_WFS) || currentStatus.equalsIgnoreCase(Constants.CLOSED_WFS)){
            		  break;
            	  }
                    		 
            	  // commenting existing logic.  new if condition for PI-103 will handle both scenarios below.
//            	  if(currentStatus.equals(Constants.INITIATED_WFS) && wt.getTransitionName().equalsIgnoreCase(Constants.VENDOR_MORE_INFO)){
//            		  //DOnt add anything
//            	  } else if(!currentStatus.equals(Constants.INITIATED_WFS) && wt.getTransitionName().equalsIgnoreCase(Constants.VENDOR_TO_COMPLETE)) {
//            		  //Do not add anything
//            	  } else {
//            		  workflowTransitionsFinalList.add(wt);
//            	  }
            	  
            	  // PI-103: only display transition if it is not one that gets assigned to the VENDOR.
            	  if (!wt.getTransitionToUserType().getUserTypeCd().equals(UserType.VENDOR)) {
            		  workflowTransitionsFinalList.add(wt);
            	  }
           	  
              }
              cwi.setWorkflowTransitionList(workflowTransitionsFinalList);
		} else {
			
			workflowTransitionsList= ht.find(
				"from WorkflowTransition wt where wt.currentUserType.userTypeCd = ? and wt.currentStatus.statusCd = ? ",
				args);
			cwi.setWorkflowTransitionList(workflowTransitionsList);
		}
		
		return cwi;
	}

}

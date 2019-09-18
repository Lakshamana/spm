/*
 * Created on 29/03/2005
 */
package br.ufpa.labes.spm.service.impl;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;


import br.ufpa.labes.spm.repository.interfaces.activities.IActivityDAO;
import br.ufpa.labes.spm.repository.interfaces.agent.IAgentDAO;
import br.ufpa.labes.spm.repository.interfaces.plainActivities.INormalDAO;
import br.ufpa.labes.spm.repository.interfaces.processModels.IProcessDAO;
import br.ufpa.labes.spm.repository.interfaces.processModels.IProcessModelDAO;
import br.ufpa.labes.spm.repository.interfaces.resources.IResourceDAO;
import br.ufpa.labes.spm.exceptions.DAOException;
import br.ufpa.labes.spm.exceptions.DataBaseException;
import br.ufpa.labes.spm.exceptions.UserException;
import br.ufpa.labes.spm.exceptions.WebapseeException;
import br.ufpa.labes.spm.domain.Activity;
import br.ufpa.labes.spm.domain.Decomposed;
import br.ufpa.labes.spm.domain.Plain;
import br.ufpa.labes.spm.domain.Agent;
import br.ufpa.labes.spm.domain.Group;
import br.ufpa.labes.spm.domain.ArtifactCon;
import br.ufpa.labes.spm.domain.BranchCon;
import br.ufpa.labes.spm.domain.BranchANDCon;
import br.ufpa.labes.spm.domain.BranchConCond;
import br.ufpa.labes.spm.domain.BranchConCondToActivity;
import br.ufpa.labes.spm.domain.BranchConCondToMultipleCon;
import br.ufpa.labes.spm.domain.Connection;
import br.ufpa.labes.spm.domain.Dependency;
import br.ufpa.labes.spm.domain.Feedback;
import br.ufpa.labes.spm.domain.JoinCon;
import br.ufpa.labes.spm.domain.MultipleCon;
import br.ufpa.labes.spm.domain.Sequence;
import br.ufpa.labes.spm.domain.SimpleCon;
import br.ufpa.labes.spm.domain.ModelingActivityEvent;
import br.ufpa.labes.spm.domain.Automatic;
import br.ufpa.labes.spm.domain.EnactionDescription;
import br.ufpa.labes.spm.domain.Normal;
import br.ufpa.labes.spm.domain.ReqAgent;
import br.ufpa.labes.spm.domain.ReqGroup;
import br.ufpa.labes.spm.domain.RequiredPeople;
import br.ufpa.labes.spm.domain.RequiredResource;
import br.ufpa.labes.spm.domain.PolCondition;
import br.ufpa.labes.spm.domain.ActivityEstimation;
import br.ufpa.labes.spm.domain.ActivityMetric;
import br.ufpa.labes.spm.domain.Process;
import br.ufpa.labes.spm.domain.ProcessModel;
import br.ufpa.labes.spm.domain.Consumable;
import br.ufpa.labes.spm.domain.Exclusive;
import br.ufpa.labes.spm.domain.Reservation;
import br.ufpa.labes.spm.domain.Resource;
import br.ufpa.labes.spm.domain.Shareable;
import br.ufpa.labes.spm.domain.ProcessAgenda;
import br.ufpa.labes.spm.domain.Task;
import br.ufpa.labes.spm.domain.TaskAgenda;
import br.ufpa.labes.spm.domain.ActivityType;
import br.ufpa.labes.spm.service.interfaces.EnactmentEngine;
import br.ufpa.labes.spm.service.interfaces.EnactmentEngineLocal;
import br.ufpa.labes.spm.service.interfaces.Logging;
import br.ufpa.labes.spm.service.interfaces.NotificationServices;
import br.ufpa.labes.spm.util.i18n.Messages;


/**
 * @author Breno Franca and Heribert Schlebbe
 */
public class EnactmentEngineImpl implements EnactmentEngine, EnactmentEngineLocal{

	public NotificationServices remote;

    private Logging logging;
   // private SendMail sendMail = null;

	IProcessDAO processDAO;

	IProcessModelDAO processModelDAO;

	INormalDAO normalDAO;

	IActivityDAO activityDAO;

	IAgentDAO agentDAO;

	IResourceDAO resourceDAO;

    /**
     * called by APSEE Manager.
     * begin with rules 1.1 - 1.4
     */
    public void executeProcess (String process_id)
			throws WebapseeException, DAOException {

        Process process = this.processDAO.retrieveBySecondaryKey(process_id);

        if (process == null){
        	throw new DataBaseException(Messages.getString("facades.EnactmentEngine.DBExcProcess")+process_id+Messages.getString("facades.EnactmentEngine.DBExcDoesNotExist")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        // checks related to the state of the system

        if(!process.getPState().equals(Process.NOT_STARTED)){
        	throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcProcess") +process_id+ Messages.getString("facades.EnactmentEngine.UserExcIsAlreEnac")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        // now start the implementation of the rules

        ProcessModel procModel = process.getTheProcessModel();
        process.setPState(Process.ENACTING);
        this.createTasks(procModel);
        this.logging.registerProcessEvent(process, "ToEnacting", "Rule 1.1"); //$NON-NLS-1$ //$NON-NLS-2$

       	this.searchForFiredConnections (procModel.getOid(), ""); //$NON-NLS-1$
       	this.searchForReadyActivities (procModel.getOid());
       	this.determineProcessModelStates(procModel);

       	// Persistence Operations

       	this.processDAO.update(process);

    }

    /**
     * called by APSEE Manager.
     * begin with rules
     */
    public void resetProcess (String process_id)
		throws WebapseeException, DAOException{


    	Process process;
        process = this.processDAO.retrieveBySecondaryKey(process_id);

        if (process == null){
        	throw new DataBaseException(Messages.getString("facades.EnactmentEngine.DBExcProcess")+process_id+Messages.getString("facades.EnactmentEngine.DBExcDoesNotExist")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        // checks related to the state of the system
		ProcessModel processModel = process.getTheProcessModel();

        // now start the implementation of the rules

		process.setPState(Process.NOT_STARTED);
		this.agendaReset(process);
		this.modelReset (processModel);
		this.determineProcessModelStates(processModel);

       	// Persistence Operations
       	this.processDAO.update(process);

    }

    /**
     * called by APSEE Agenda.
     * begin with rule 3.1
     */
    public void beginTask (String agent_id, String act_id)
		throws WebapseeException, DAOException {

    	// checks of parameters
        Normal actNorm = this.normalDAO.retrieveBySecondaryKey(act_id);

        Agent agent = this.agentDAO.retrieveBySecondaryKey(agent_id);

        if (actNorm == null){
        	throw new DataBaseException(Messages.getString("facades.EnactmentEngine.DBExcActivity")+act_id+Messages.getString("facades.EnactmentEngine.DBExcDoesNotExist")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        if (agent == null){
        	throw new DataBaseException(Messages.getString("facades.EnactmentEngine.DBExcAgent")+agent_id+Messages.getString("facades.EnactmentEngine.DBExcDoesNotExist")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        // checks related to the state of the system

        if (! (actNorm instanceof Normal)){
        	throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcActv")+act_id+ //$NON-NLS-1$
		    	Messages.getString("facades.EnactmentEngine.UserExcIsntANormal")); //$NON-NLS-1$
        }

        if (!this.isAgentAllocatedToActivity(agent, actNorm)){
        	throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcAgt")+agent_id+ //$NON-NLS-1$
	    	Messages.getString("facades.EnactmentEngine.UserExcIsNotReqFor")+ act_id); //$NON-NLS-1$
        }

        ProcessModel pmodel = actNorm.getTheProcessModel();
        Process process = this.getTheProcess(pmodel);
        String proc_id = process.getIdent();
        String proc_state = pmodel.getPmState();
        if (! this.isReadyForTask (proc_state)){
        	throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcProcess")+proc_id+ //$NON-NLS-1$
		    	Messages.getString("facades.EnactmentEngine.UserExcIsNotReadyFor")); //$NON-NLS-1$
        }

        // now start the implementation of the rules

        String actState = actNorm.getTheEnactionDescription().getState();

        if (actState.equals(Plain.READY)) {
        	if (this.isAllRequiredResourceAvailable(actNorm)) {
        		this.allocateAllForActivity(actNorm, true);
        		this.plainActivityHasStarted (pmodel.getOid());
        		this.updateAgenda (agent,actNorm, Plain.ACTIVE, "Rule 3.1"); //$NON-NLS-1$
        		EnactionDescription enact = actNorm.getTheEnactionDescription();
        		//TODO enact.setStateWithMessage(Plain.ACTIVE);
        		enact.setActualBegin (new Date());

        		this.logging.registerGlobalActivityEvent(actNorm, "ToActive", "Rule 3.1"); //$NON-NLS-1$ //$NON-NLS-2$
        	}
        	else {
        		// Rule 3.2

        		throw new UserException
				(Messages.getString("facades.EnactmentEngine.UserExcOneOrMoreReq")); //$NON-NLS-1$
        	}
        }
        else{
        	if (actState.equals(Plain.PAUSED)) {
        		// restartTask
        		if (this.isAllRequiredResourceAvailable(actNorm)) {
        			//TODO actNorm.getTheEnactionDescription().setStateWithMessage(Plain.ACTIVE);
        			this.updateAgenda(agent, actNorm, Plain.ACTIVE, "Rule 3.4"); //$NON-NLS-1$
        			this.logging.registerGlobalActivityEvent(actNorm, "ToActive", "Rule 3.4"); //$NON-NLS-1$ //$NON-NLS-2$
        		}
        		else {
        			// Rule 3.5
        			throw new UserException
					(Messages.getString("facades.EnactmentEngine.UserExcOneOrMoreReq")); //$NON-NLS-1$
        		}
        	}
        	else if (actState.equals(Plain.ACTIVE)){

        		this.updateAgenda(agent, actNorm, Plain.ACTIVE, "Rule 3.3"); //$NON-NLS-1$
        	}
        }

        String why = ""; //$NON-NLS-1$
        Collection conns = this.getConnectionsTo(actNorm);
        Iterator iterC = conns.iterator();
        while (iterC.hasNext()) {
        	Connection conn = (Connection) iterC.next();
        	if (conn instanceof JoinCon){
        		Join join = (JoinCon)conn;
        		String dep = joinCon.getTheDependency().getKindDep();
        		if (join.getKindJoinCon().equals("OR")){ //$NON-NLS-1$
        			if (dep.equals("start-start")){ //$NON-NLS-1$
        				why = "Rule 8.12";  //$NON-NLS-1$
        				// equals to the Rule 8.13
        				break;
        			}
        		}
        		else if (join.getKindJoinCon().equals("XOR")) //$NON-NLS-1$
        			if (dep.equals("start-start")){ //$NON-NLS-1$
        				why = "Rule 8.17"; //$NON-NLS-1$
        				break;
        			}
        	}
        }
        this.searchForFiredConnections(actNorm.getTheProcessModel().getOid(), why);
        this.searchForReadyActivities(actNorm.getTheProcessModel().getOid());
        this.determineProcessModelStates(actNorm.getTheProcessModel());

       	// Persistence Operations

       	this.normalDAO.update(actNorm);
       	this.agentDAO.update(agent);

    }

    /**
     * called by APSEE Agenda.
     * begin with rule 3.6
     * And the rules 9.1, 9.2
     */
    public void finishTask (String agent_id, String act_id)
		throws WebapseeException, DAOException{

    	// checks of parameters

        Normal actNorm = this.normalDAO.retrieveBySecondaryKey(act_id);

        Agent agent = this.agentDAO.retrieveBySecondaryKey(agent_id);

        if (actNorm == null){
        	throw new DataBaseException(Messages.getString("facades.EnactmentEngine.DBExcActivity")+act_id+Messages.getString("facades.EnactmentEngine.DBExcDoesNotExist")); //$NON-NLS-1$ //$NON-NLS-2$
        }
        if (agent == null){
    		throw new DataBaseException(Messages.getString("facades.EnactmentEngine.DBExcAgent")+agent_id+Messages.getString("facades.EnactmentEngine.DBExcDoesNotExist")); //$NON-NLS-1$ //$NON-NLS-2$
        }

    	// checks related to the state of the system

        if (! (actNorm instanceof Normal)){
    		throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcActv")+act_id+ //$NON-NLS-1$
		    	Messages.getString("facades.EnactmentEngine.UserExcIsntANormal")); //$NON-NLS-1$
        }

        if (!this.isAgentAllocatedToActivity(agent, actNorm)){
    		throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcAgt")+agent_id+ //$NON-NLS-1$
	    	Messages.getString("facades.EnactmentEngine.UserExcIsNotReqFor")+ act_id); //$NON-NLS-1$
        }

        ProcessModel pmodel = actNorm.getTheProcessModel();
        Process process = this.getTheProcess(pmodel);
        String proc_id = process.getIdent();
        String proc_state = pmodel.getPmState();
        if (! this.isReadyForTask (proc_state)){
        	throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcProcess")+proc_id+ //$NON-NLS-1$
		    	Messages.getString("facades.EnactmentEngine.UserExcIsNotReadyFor")); //$NON-NLS-1$
        }

        // now start the implementation of the rules
        String why = "";
        String state = actNorm.getTheEnactionDescription().getState();
   		if(state.equals(Plain.ACTIVE)){

           if(!this.isActivityFinished(actNorm) && this.isLastToFinish(agent, actNorm)){

           		if (! this.canFinish (actNorm)) {
           			throw new UserException (
           				Messages.getString("facades.EnactmentEngine.UserExcCannFinThis") //$NON-NLS-1$
           				+ Messages.getString("facades.EnactmentEngine.UserActvHasPend")); //$NON-NLS-1$
           		}

           		boolean feedbackTaken = false;

           		if (isFeedbackSource (actNorm)){
           			feedbackTaken = this.executeFeedbacks (actNorm);
           		}

           		// if there was no feedback with a true condition, finish this task
           		if (! feedbackTaken) {
         		   	this.releaseResourcesFromActivity(actNorm);
           		   	//TODO actNorm.getTheEnactionDescription().setStateWithMessage(Plain.FINISHED);
           		   	actNorm.getTheEnactionDescription().setActualEnd(new Date());
           		   	this.updateAgenda(agent, actNorm, Plain.FINISHED, "Rule 3.6"); //$NON-NLS-1$
           		   	this.logging.registerGlobalActivityEvent(actNorm, "ToFinished", "Rule 3.6"); //$NON-NLS-1$ //$NON-NLS-2$

                    this.activityHasFinished (actNorm);

                    Collection conns = this.getConnectionsTo(actNorm);
                    Iterator iterC = conns.iterator();
                    while (iterC.hasNext()) {
            			Connection conn = (Connection) iterC.next();
            			if (conn instanceof JoinCon){
            				Join join = (JoinCon)conn;
            				String dep = joinCon.getTheDependency().getKindDep();
                			if (join.getKindJoinCon().equals("OR")){ //$NON-NLS-1$
                				if (dep.equals("end-start") || //$NON-NLS-1$
                					dep.equals("end-end")){ //$NON-NLS-1$
                					why = "Rule 8.2";  //$NON-NLS-1$
                					break;
                				}
                				else{
                					why = "Rule 8.12"; //$NON-NLS-1$
                					//equals to Rule 8.13
                					break;
                				}
                			}
                			else if (join.getKindJoinCon().equals("XOR")) //$NON-NLS-1$
                				if (dep.equals("end-start") || //$NON-NLS-1$
                   					dep.equals("end-end")){ //$NON-NLS-1$
                					why = "Rule 8.6"; //$NON-NLS-1$
                					break;
                				}
                				else{
                					why = "Rule 8.17"; //$NON-NLS-1$
                					//equals to Rule 8.13
                					break;
                				}
            			}
            		}
           		}
           }
           else if (!state.equals(Plain.FINISHED)){
           	   this.updateAgenda(agent, actNorm, Plain.FINISHED, "Rule 3.7"); //$NON-NLS-1$
          }
   		}
        this.searchForFiredConnections(pmodel.getOid(), why);
        this.searchForReadyActivities(pmodel.getOid());
    	this.determineProcessModelStates(pmodel); // to solve upper propagation problems

       	// Persistence Operations

       	this.normalDAO.update(actNorm);
       	this.agentDAO.update(agent);

    }

    public void finishTask (Normal actNorm/*, Session currentSession*/) throws WebapseeException, DAOException{

    	// checks of parameters

    	if (actNorm == null){
    		throw new DataBaseException(Messages.getString("facades.EnactmentEngine.DBExcActvDoesNot"));
    	}

    	// checks related to the state of the system

    	ProcessModel pmodel = actNorm.getTheProcessModel();
    	Process process = this.getTheProcess(pmodel);
    	String proc_id = process.getIdent();
    	String proc_state = pmodel.getPmState();
    	if (! this.isReadyForTask (proc_state)){
    		throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcProcess")+proc_id+ //$NON-NLS-1$
	    		Messages.getString("facades.EnactmentEngine.UserExcIsNotReadyFor")); //$NON-NLS-1$
    	}

    	// now start the implementation of the rules
    	String why = "";
    	String state = actNorm.getTheEnactionDescription().getState();
		if(state.equals(Plain.ACTIVE)){

       		if (! this.canFinish (actNorm)) {
       			throw new UserException (
       					Messages.getString("facades.EnactmentEngine.UserExcCannFinThisTask") //$NON-NLS-1$
       					+ Messages.getString("facades.EnactmentEngine.UserActvHasPend")); //$NON-NLS-1$
       		}

       		boolean feedbackTaken = false;

       		if (isFeedbackSource (actNorm)){
       			feedbackTaken = this.executeFeedbacks (actNorm);
       		}

       		// if there was no feedback with a true condition, finish this task
       		if (! feedbackTaken) {
     		   	this.releaseResourcesFromActivity(actNorm);
       		   	//TODO actNorm.getTheEnactionDescription().setStateWithMessage(Plain.FINISHED);
       		   	actNorm.getTheEnactionDescription().setActualEnd(new Date());
       		   	this.logging.registerGlobalActivityEvent(actNorm, "ToFinished", "Rule 3.6"); //$NON-NLS-1$ //$NON-NLS-2$

                this.activityHasFinished (actNorm);

                Collection conns = this.getConnectionsTo(actNorm);
                Iterator iterC = conns.iterator();
                while (iterC.hasNext()) {
        			Connection conn = (Connection) iterC.next();
        			if (conn instanceof JoinCon){
        				Join join = (JoinCon)conn;
        				String dep = joinCon.getTheDependency().getKindDep();
            			if (join.getKindJoinCon().equals("OR")){ //$NON-NLS-1$
            				if (dep.equals("end-start") || //$NON-NLS-1$
            					dep.equals("end-end")){ //$NON-NLS-1$
            					why = "Rule 8.2";  //$NON-NLS-1$
            					break;
            				}
            				else{
            					why = "Rule 8.12"; //$NON-NLS-1$
            					//equals to Rule 8.13
            					break;
            				}
            			}
            			else if (join.getKindJoinCon().equals("XOR")) //$NON-NLS-1$
            				if (dep.equals("end-start") || //$NON-NLS-1$
               					dep.equals("end-end")){ //$NON-NLS-1$
            					why = "Rule 8.6"; //$NON-NLS-1$
            					break;
            				}
            				else{
            					why = "Rule 8.17"; //$NON-NLS-1$
            					//equals to Rule 8.13
            					break;
            				}
        			}
        		}
       		}
		}

		this.searchForFiredConnections(pmodel.getOid(), why);
    	this.searchForReadyActivities(pmodel.getOid());
    }

    /**
     * called by APSEE Agenda.
     * begin with rule 3.8
     */
    public void pauseTask (String agent_id, String act_id)
		throws WebapseeException, DAOException {

    	// checks of parameters
        Normal actNorm = this.normalDAO.retrieveBySecondaryKey(act_id);

        Agent agent = this.agentDAO.retrieveBySecondaryKey(agent_id);

        if (actNorm == null){
        	throw new DataBaseException(Messages.getString("facades.EnactmentEngine.DBExcActivity")+act_id+Messages.getString("facades.EnactmentEngine.DBExcDoesNotExist")); //$NON-NLS-1$ //$NON-NLS-2$
        }
        if (agent == null){

        	throw new DataBaseException(Messages.getString("facades.EnactmentEngine.DBExcAgent")+agent_id+Messages.getString("facades.EnactmentEngine.DBExcDoesNotExist")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        // checks related to the state of the system

        if (! (actNorm instanceof Normal)){
        	throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcActv")+act_id+ //$NON-NLS-1$
		    	Messages.getString("facades.EnactmentEngine.UserExcIsntANormal")); //$NON-NLS-1$
        }

        if (!this.isAgentAllocatedToActivity(agent, actNorm)){
        	throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcAgt")+agent_id+ //$NON-NLS-1$
	    	Messages.getString("facades.EnactmentEngine.UserExcIsNotReqFor")+ act_id); //$NON-NLS-1$
        }

        ProcessModel pmodel = actNorm.getTheProcessModel();
        Process process = this.getTheProcess(pmodel);
        String proc_id = process.getIdent();
        String proc_state = pmodel.getPmState();
        if (! this.isReadyForTask (proc_state)){
        	throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcProcess")+proc_id+ //$NON-NLS-1$
		    	Messages.getString("facades.EnactmentEngine.UserExcIsNotReadyFor")); //$NON-NLS-1$
        }

        // now start the implementation of the rules

        if(actNorm.getTheEnactionDescription().getState().equals(Plain.ACTIVE)
        	&& !this.isActivityPaused(actNorm)
        	&& this.isLastToPause(agent, actNorm)){
			this.releaseResourcesFromActivity(actNorm);
    		//TODO actNorm.getTheEnactionDescription().setStateWithMessage(Plain.PAUSED);
			this.updateAgenda(agent, actNorm, Plain.PAUSED, "Rule 3.8"); //$NON-NLS-1$
			this.logging.registerGlobalActivityEvent(actNorm, "ToPaused", "Rule 3.8"); //$NON-NLS-1$ //$NON-NLS-2$
    	}
        else{

        	String taskState = this.getTaskState(agent, actNorm);
        	if(taskState != null
        		&& !taskState.equals("")
        		&& !taskState.equals(Plain.PAUSED))
        		this.updateAgenda(agent, actNorm, Plain.PAUSED, "Rule 3.9"); //$NON-NLS-1$
    	}

       	// Persistence Operations

       	this.normalDAO.update(actNorm);
       	this.agentDAO.update(agent);

    }

    public void pauseActiveTasks(String agentIdent) throws WebapseeException{

		//TODO List result = this.normalDAO.getActiveTasksByAgent(agentIdent);
		//TODO Iterator resultIterator = result.iterator();

		/*while ( resultIterator.hasNext() ){
			String ident = (String) resultIterator.next();
			this.pauseTask(agentIdent, ident);
		}*/

    }

    /**
     * called by APSEE Agenda.
     * begin with rule 3.10
     */
    public void delegateTask (String from_agent_id, String act_id, String to_agent_id)
    		throws WebapseeException, DAOException {

    	// checks of parameters

        Normal actNorm = this.normalDAO.retrieveBySecondaryKey(act_id);

        Agent from_agent;
        Agent to_agent;

        from_agent = this.agentDAO.retrieveBySecondaryKey(from_agent_id);

        to_agent = this.agentDAO.retrieveBySecondaryKey(to_agent_id);


        if (actNorm == null){
        	throw new DataBaseException(Messages.getString("facades.EnactmentEngine.DBExcActivity")+act_id+Messages.getString("facades.EnactmentEngine.DBExcDoesNotExist")); //$NON-NLS-1$ //$NON-NLS-2$
        }
        if (from_agent == null){
        	throw new DataBaseException(Messages.getString("facades.EnactmentEngine.DBExcAgent")+from_agent_id+Messages.getString("facades.EnactmentEngine.DBExcDoesNotExist")); //$NON-NLS-1$ //$NON-NLS-2$
        }
        if (to_agent == null){
        	throw new DataBaseException(Messages.getString("facades.EnactmentEngine.DBExcAgent")+to_agent_id+Messages.getString("facades.EnactmentEngine.DBExcDoesNotExist")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        // checks related to the state of the system

        if (! (actNorm instanceof Normal)){
        	throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcActv")+act_id+ //$NON-NLS-1$
		    	Messages.getString("facades.EnactmentEngine.UserExcIsntANormal")); //$NON-NLS-1$
        }

        if (!this.isAgentAllocatedToActivity(from_agent, actNorm)){
        	throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcAgt")+from_agent_id+ //$NON-NLS-1$
	    	Messages.getString("facades.EnactmentEngine.UserExcIsNotReqFor")+ act_id); //$NON-NLS-1$
        }

        if (this.isAgentAllocatedToActivity(to_agent, actNorm)){

        	throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcAgent")+to_agent_id+
        			Messages.getString("facades.EnactmentEngine.UserExcIsAlrAlloc")+ act_id);
        }

        ProcessModel pmodel = actNorm.getTheProcessModel();
        Process process = this.getTheProcess(pmodel);
        String proc_id = process.getIdent();
        String proc_state = pmodel.getPmState();
        if (! this.isReadyForTask (proc_state)){
        	throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcProcess")+proc_id+ //$NON-NLS-1$
		    	Messages.getString("facades.EnactmentEngine.UserExcIsNotReadyFor")); //$NON-NLS-1$
        }

        // now start the implementation of the rules

        String state = actNorm.getTheEnactionDescription().getState();
        RequiredPeople req = null;
        if(state.equals(Plain.WAITING) && this.hasInvolvedAgents(actNorm)){
        	req = this.delegateTaskToAgent(from_agent, actNorm, to_agent);
        	this.updateAgenda(from_agent, actNorm, "Delegated", "Rule 3.10"); //$NON-NLS-1$ //$NON-NLS-2$
        	this.updateAgenda(to_agent, actNorm, Plain.WAITING, "Rule 3.10"); //$NON-NLS-1$

        	Collection tasks = actNorm.getTheTasks();
   		   	Iterator iter = tasks.iterator();
   		   	while (iter.hasNext()) {
   		   	 	Task task = (Task) iter.next();
   		   	  	if (task.getTheNormal().equals(actNorm)){
   		   	  		this.logging.registerAgendaEvent(task, "ToDelegated", "Rule 3.10"); //$NON-NLS-1$ //$NON-NLS-2$
   		   	  	}
   		   	}
        }
        else if(state.equals(Plain.READY) && this.hasInvolvedAgents(actNorm)){
        	req = this.delegateTaskToAgent(from_agent, actNorm, to_agent);
        	this.updateAgenda(from_agent, actNorm, "Delegated", "Rule 3.11"); //$NON-NLS-1$ //$NON-NLS-2$
        	this.updateAgenda(to_agent, actNorm, Plain.READY, "Rule 3.11"); //$NON-NLS-1$

        	Collection tasks = actNorm.getTheTasks();
   		   	Iterator iter = tasks.iterator();
   		   	while (iter.hasNext()) {
   		   	 	Task task = (Task) iter.next();
   		   	  	if (task.getTheNormal().equals(actNorm)){
   		   	  		this.logging.registerAgendaEvent(task, "ToDelegated", "Rule 3.11"); //$NON-NLS-1$ //$NON-NLS-2$
   		   	  	}
   		   	}
        }
        else{
        	throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcActv") + actNorm.getIdent() + Messages.getString("facades.EnactmentEngine.UserExcCanNotBeDel")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        if(req != null){
        	String message = "<MESSAGE>" +  //$NON-NLS-1$
        						"<NOTIFY>" + "<OID>" + req.getOid()	+ "</OID>" +  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        							"<TYPE>UPT</TYPE>" +  //$NON-NLS-1$
        							"<CLASS>" + req.getClass().getName() + "</CLASS>" +  //$NON-NLS-1$ //$NON-NLS-2$
        							"<BY>" + from_agent_id + "</BY>" +  //$NON-NLS-1$ //$NON-NLS-2$
        						"</NOTIFY>" +  //$NON-NLS-1$
        					"</MESSAGE>"; //$NON-NLS-1$

        	try {
        		if (this.remote == null) {
        			reloadRemote();
        		}
        		if (this.remote != null) {
        			this.remote.sendMessage(message);
        		}
        	} catch (RemoteException e) {
        		// e.printStackTrace();
        		// TODO Auto-generated catch block
        		 System.out
        		 	.println("EnactmentEngine.breateNewVersion() remote reference exception"); //$NON-NLS-1$
        		// e.printStackTrace();
        	}
        }
       	// Persistence Operations

       	this.normalDAO.update(actNorm);
       	this.agentDAO.update(from_agent);
       	this.agentDAO.update(to_agent);
       	try{
           	//Mail - para gerente qdo atv for delegada
           	//this.sendMail.sendDeleAct(currentSession,act_id,process.getIdent(),actNorm.getName(),from_agent_id,to_agent_id);
           	//End Mail
       	}catch(Exception we){
       		//we.printStackTrace();
       	}

    }

    /**
     * called by APSEE Manager.
     * Applies to the Rules 9.5, 9.6, 9.7, 9.8, 9.10
     * 9.38 and 9.39
     */
    public void failActivity (String act_id)
			throws WebapseeException, DAOException {
    	System.out.println(act_id);
    	// checks of parameters
    	Activity activity;

        activity = this.activityDAO.retrieveBySecondaryKey(act_id);


        if (activity == null){
        	throw new DataBaseException(Messages.getString("facades.EnactmentEngine.DBExcActivity")+act_id+Messages.getString("facades.EnactmentEngine.DBExcDoesNotExist")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        // checks related to the state of the system

        String state = this.getState(activity);
       	if(state.equals(Plain.FAILED) ||
       		state.equals(ProcessModel.FAILED)){
       		throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcActv") +act_id+ Messages.getString("facades.EnactmentEngine.UserExcIsAlreFail")); //$NON-NLS-1$ //$NON-NLS-2$
       	}

        ProcessModel pmodel = activity.getTheProcessModel();

        // now start the implementation of the rules

		boolean hasFeedback = this.isFeedbackSource(activity);

		boolean feedCond = false;

		Collection sett = this.getFeedbackSet (activity);
		Iterator iter = sett.iterator();

		while (iter.hasNext()) {
			Feedback feed = (Feedback) iter.next();
			PolCondition condition = feed.getTheCondition();
			if (this.conditionValue (condition)) {
				this.manCleanActivity (activity, Plain.FAILED);
				feedCond = true;
				this.executeFeedback (feed);
			}
		}
		//Rules 9.38 and 9.39

		if (hasFeedback == false) {
			if (activity instanceof Plain)
				this.failGeneralActivity (activity, true, "Rule 9.5"); //$NON-NLS-1$
			else
				this.failGeneralActivity (activity, true, "Rule 9.8"); //$NON-NLS-1$
		}
		else{
			if (feedCond == false){
				if (activity instanceof Plain)
					this.failGeneralActivity (activity, true, "Rule 9.7"); //$NON-NLS-1$
				else
					this.failGeneralActivity (activity, true, "Rule 9.9"); //$NON-NLS-1$
			}
			else
				this.searchForReadyActivities (pmodel.getOid());
		}

		this.searchForReadyActivities (pmodel.getOid());
		this.determineProcessModelStates(pmodel);

       	// Persistence Operations

		this.activityDAO.update(activity);

    }

    /**
     * called by APSEE Manager.
     * begin with rule ?
     */
    public void cancelActivity (String act_id)
			throws WebapseeException, DAOException {

    	// checks of parameters

    	Activity act;
    	act = this.activityDAO.retrieveBySecondaryKey(act_id);


        if (act == null){
        	throw new DataBaseException(Messages.getString("facades.EnactmentEngine.DBExcActivity")+act_id+Messages.getString("facades.EnactmentEngine.DBExcDoesNotExist")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        // checks related to the state of the system

        Activity activity;
        if (act instanceof Normal){
        	activity = (Normal)act;
        }
        else if (act instanceof Automatic){
        	activity = (Automatic)act;
        }
        else{
        	activity = (Decomposed)act;
        }

        String state = this.getState(activity);
        if(state.equals(Plain.CANCELED) ||
        	state.equals(ProcessModel.CANCELED)){
            throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcActv") +act_id+ Messages.getString("facades.EnactmentEngine.UserExcIsAlreCancel")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        // now start the implementation of the rules

		if (state.equals(Plain.WAITING)
			|| state.equals(Plain.READY)
			|| state.equals(ProcessModel.ABSTRACT)
			|| state.equals(ProcessModel.INSTANTIATED)) {
			this.cancelGeneralActivity (activity, true, "Rule Beginning of the Cancel"); //$NON-NLS-1$
		}
		else {
            throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcNotPossibleToCanc") + state + "."); //$NON-NLS-1$ //$NON-NLS-2$
		}

		this.determineProcessModelStates(activity.getTheProcessModel());

		// Persistence Operations

		this.activityDAO.update(activity);
    }

    /**
     * called by APSEE Manager.
     * Applies to the rule 12.3
     */
    public void makeUnavailable (String resource_id)
		throws WebapseeException, DAOException{

    	// checks of parameters
    	Resource resource;

    	resource = this.resourceDAO.retrieveBySecondaryKey(resource_id);


        if (resource == null){
        	throw new DataBaseException(Messages.getString("facades.EnactmentEngine.DBExcResource")+resource_id+Messages.getString("facades.EnactmentEngine.DBExcDoesNotExist")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        // now start the implementation of the rules

        if(resource instanceof Shareable){
    		Shareable sha = (Shareable)resource;
        	if(sha.getState().equals(Shareable.AVAILABLE) ){
        		sha.setState(Shareable.NOT_AVAILABLE);
        		this.logging.registerResourceEvent(sha, null, "ToNotAvailable", "Rule 12.3"); //$NON-NLS-1$ //$NON-NLS-2$

        		Collection possesses = sha.getPossess();
        		Iterator iterPossess = possesses.iterator();
        		while (iterPossess.hasNext()) {
					Resource possessed = (Resource) iterPossess.next();
					if(possessed instanceof Shareable){
						Shareable psha = (Shareable) possessed;
						if(psha.getState().equals(Shareable.AVAILABLE))
							try{
								this.makeUnavailable(psha.getIdent());
							}
							catch(WebapseeException e){
								continue;
							}
					}
				}

        	}
        	else{
            	throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcResource") +resource_id+ Messages.getString("facades.EnactmentEngine.UserExcIsAlreUnavail")); //$NON-NLS-1$ //$NON-NLS-2$
        	}
        }
        else{
        	throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcThisOperatCanBe")); //$NON-NLS-1$
        }

       	// Persistence Operations

		this.resourceDAO.update(resource);
    }

    /**
     * called by APSEE Manager.
     * Applies to the rule 12.4 and 12.10
     */
    public void makeAvailable (String resource_id)
		throws WebapseeException, DAOException{

    	// checks of parameters
    	Resource resource;

    	resource = this.resourceDAO.retrieveBySecondaryKey(resource_id);

        if (resource == null){
        	throw new DataBaseException(Messages.getString("facades.EnactmentEngine.DBExcResource")+resource_id+Messages.getString("facades.EnactmentEngine.DBExcDoesNotExist")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        // now start the implementation of the rules

        if(resource instanceof Shareable){
    		Shareable sha = (Shareable)resource;
        	if(sha.getState().equals(Shareable.NOT_AVAILABLE)
        		|| sha.getState().equals("")){ //$NON-NLS-1$
        		sha.setState(Shareable.AVAILABLE);
        		this.logging.registerResourceEvent(sha, null, "ToAvailable", "Rule 12.4"); //$NON-NLS-1$ //$NON-NLS-2$

        		Collection possesses = sha.getPossess();
        		Iterator iterPossess = possesses.iterator();
        		while (iterPossess.hasNext()) {
					Resource possessed = (Resource) iterPossess.next();
					if(possessed instanceof Shareable){
						Shareable psha = (Shareable) possessed;
						if(psha.getState().equals(Shareable.NOT_AVAILABLE))
							try{
								this.makeAvailable(psha.getIdent());
							}
							catch(WebapseeException e){
								continue;
							}
					}
					else if(possessed instanceof Exclusive){
						Exclusive pexc = (Exclusive)possessed;
						if(pexc.getState().equals(Exclusive.DEFECT))
							try{
								this.makeAvailable(pexc.getIdent());
							}
							catch(WebapseeException e){
								continue;
							}
					}
				}
        	}
        	else{
            	throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcResource") +resource_id+ Messages.getString("facades.EnactmentEngine.UserExcIsAlreAvaila")); //$NON-NLS-1$ //$NON-NLS-2$
        	}
        }
        else if(resource instanceof Exclusive){
        	Exclusive exc = (Exclusive)resource;
        	if(exc.getState().equals(Exclusive.DEFECT)
        		|| exc.getState().equals(Exclusive.LOCKED)
        		|| exc.getState().equals("")){ //$NON-NLS-1$
        		exc.setState(Exclusive.AVAILABLE);
        		this.logging.registerResourceEvent(exc, null,"ToAvailable", "Rule 12.10"); //$NON-NLS-1$ //$NON-NLS-2$

        		Collection possesses = exc.getPossess();
        		Iterator iterPossess = possesses.iterator();
        		while (iterPossess.hasNext()) {
					Resource possessed = (Resource) iterPossess.next();
					if(possessed instanceof Shareable){
						Shareable psha = (Shareable) possessed;
						if(psha.getState().equals(Shareable.NOT_AVAILABLE))
							try{
								this.makeAvailable(psha.getIdent());
							}
							catch(WebapseeException e){
								continue;
							}
					}
					else if(possessed instanceof Exclusive){
						Exclusive pexc = (Exclusive)possessed;
						if(pexc.getState().equals(Exclusive.DEFECT)
							|| exc.getState().equals(Exclusive.LOCKED))
							try{
								this.makeAvailable(pexc.getIdent());
							}
							catch(WebapseeException e){
								continue;
							}
					}
				}
        	}
        	else{
            	throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcResource") +resource_id+ Messages.getString("facades.EnactmentEngine.UserExcIsAlreAvaila")); //$NON-NLS-1$ //$NON-NLS-2$
        	}
        }

       	// Persistence Operations
		this.resourceDAO.update(resource);
    }

    /**
     * called by APSEE Manager.
     * Applies to the rule 12.9
     */
    public void registerDefect (String resource_id)
		throws WebapseeException, DAOException{

    	// checks of parameters
    	Resource resource;

    	resource = this.resourceDAO.retrieveBySecondaryKey(resource_id);


        if (resource == null){
        	throw new DataBaseException(Messages.getString("facades.EnactmentEngine.DBExcResource")+resource_id+Messages.getString("facades.EnactmentEngine.DBExcDoesNotExist")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        // now start the implementation of the rules

        if(resource instanceof Exclusive){
        	Exclusive exc = (Exclusive)resource;
        	if(!exc.getState().equals(Exclusive.DEFECT) ){
        		exc.setState(Exclusive.DEFECT);
        		this.logging.registerResourceEvent(exc, null, "ToDefect", "Rule 12.9"); //$NON-NLS-1$ //$NON-NLS-2$

        		Collection possesses = exc.getPossess();
        		Iterator iterPossess = possesses.iterator();
        		while (iterPossess.hasNext()) {
					Resource possessed = (Resource) iterPossess.next();
					if(possessed instanceof Exclusive){
						Exclusive pexc = (Exclusive)possessed;
						if(!pexc.getState().equals(Exclusive.DEFECT))
							try{
								this.registerDefect(pexc.getIdent());
							}
							catch(WebapseeException e){
								continue;
							}
					}
				}
        	}
        	else{
               	throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcDefectInResour") +resource_id+ Messages.getString("facades.EnactmentEngine.UserExcAlreRegistered")); //$NON-NLS-1$ //$NON-NLS-2$
        	}
        }

       	// Persistence Operations

		this.resourceDAO.update(resource);
    }

    // New Public Methods

    /**
     * called by APSEE Manager.
     * Applies to the rules G10.1 to G10.10
     * (Determines the Process Model's state according to the state its
     * Activities).
     */
    public void determineProcessModelStates (ProcessModel processModel/*, Session currentSession*/)
		throws WebapseeException {

		String currstate = processModel.getPmState();

		Decomposed dec =  processModel.getTheDecomposed();

		if(currstate.equals(ProcessModel.FINISHED)) return; // already finished

		Collection activities = processModel.getTheActivity();
		Iterator iter = activities.iterator();

		String[] types    = new String [activities.size()]; // Type of activity
		String[] states   = new String [activities.size()]; // A_st

		int n = 0;
		while (iter.hasNext()) {
			Activity act = (Activity) iter.next();
			if (act instanceof Decomposed) {
				Decomposed fragment = (Decomposed)act;
				String fstate = fragment.getTheReferedProcessModel().getPmState();
				// 	here process model states are stored
				types[n] = "Decomposed"; //$NON-NLS-1$
				states[n] = fstate;
			}
			else
				if (act instanceof Automatic) {
					Automatic automatic = (Automatic)act;
					types[n] = "Automatic"; //$NON-NLS-1$
					states[n] = automatic.getTheEnactionDescription().getState();
				}
				else
					if (act instanceof Normal) {
						Normal normal = (Normal)act;
						Collection reqPeople = normal.getTheRequiredPeople();
						Iterator iter2 = reqPeople.iterator();
						int reqAgentSize = 0;
						int reqGroupSize = 0;
						while (iter2.hasNext()) {
							RequiredPeople reqP = (RequiredPeople) iter2.next();
							if (reqP instanceof ReqAgent)
								reqAgentSize++;
							else
								reqGroupSize++;
						}
						if (reqAgentSize > 0 || reqGroupSize > 0)
							types[n] = "NormalWithAgent"; //$NON-NLS-1$
						else types[n] = "NormalWithoutAgent"; //$NON-NLS-1$
						states[n] = normal.getTheEnactionDescription().getState();
					}
					else {
						types[n] = "Undefined"; //$NON-NLS-1$
						states[n] = "Undefined"; //$NON-NLS-1$
					}
				n++;
		}

		//Dynamic rules 10.1, 10.2
		boolean hasOnlyRequirements = true;
		for (int i = 0; i < states.length; i++) {
			String state = states[i];
			if (! state.equals(ProcessModel.REQUIREMENTS)) {
				hasOnlyRequirements = false;
				break;
			}
		}
		if (hasOnlyRequirements) {
			if (! currstate.equals (ProcessModel.REQUIREMENTS)){
				//TODO processModel.setPmStateWithMessage(ProcessModel.REQUIREMENTS);
				this.logging.registerProcessModelEvent(processModel,"ToRequirements", "Rules 10.1 and 10.2"); //$NON-NLS-1$ //$NON-NLS-2$
				Decomposed superActDec = processModel.getTheDecomposed();
				if(superActDec == null){
					return;
				}
				else{
					this.determineProcessModelStates(superActDec.getTheProcessModel());
				}
			}
			return;
		}

		//Dynamic rule 10.3 (added waiting, ready, paused)
		boolean hasAnyEnacted = false;
		String auxType = ""; //$NON-NLS-1$
		for (int i = 0; i < states.length; i++) {
			String state = states[i];
			auxType = types[i];
			if (state.equals(ProcessModel.ENACTING) ||
				state.equals(Plain.ACTIVE)   ||
				state.equals(Plain.PAUSED)) {
				hasAnyEnacted = true;
				break;
			}
		}
		if (hasAnyEnacted) {
			if (! currstate.equals (ProcessModel.ENACTING)){
				//TODO processModel.setPmStateWithMessage(ProcessModel.ENACTING);
				this.logging.registerProcessModelEvent (processModel, "ToEnacting", "Rule 10.3"); //$NON-NLS-1$ //$NON-NLS-2$
				if(!auxType.equals("Decomposed")) //$NON-NLS-1$
					this.logging.registerProcessModelEvent (processModel, "ToEnacting", "Rule 4.3"); //$NON-NLS-1$ //$NON-NLS-2$
				else
					this.logging.registerProcessModelEvent (processModel, "ToEnacting", "Rule 4.5"); //$NON-NLS-1$ //$NON-NLS-2$
				Decomposed superActDec = processModel.getTheDecomposed();
				if(superActDec == null){
					return;
				}
				else{
					this.determineProcessModelStates(superActDec.getTheProcessModel());
				}
			}
			return;
		}

		// 	Dynamic rule 10.9
		boolean allFailed = true;
		for (int i = 0; i < states.length; i++) {
			String state = states[i];
			if (!(state.equals(ProcessModel.FAILED) ||
				state.equals(Plain.FAILED))) {
				allFailed = false;
				break;
			}
		}
		if (allFailed) {
			if (! currstate.equals (ProcessModel.FAILED)){
				//TODO processModel.setPmStateWithMessage(ProcessModel.FAILED);
				this.logging.registerProcessModelEvent(processModel, "ToFailed", "Rule 10.9"); //$NON-NLS-1$ //$NON-NLS-2$
				Decomposed superActDec = processModel.getTheDecomposed();
				if(superActDec == null){
					return;
				}
				else{
					this.failPropagation(superActDec);
					this.determineProcessModelStates(superActDec.getTheProcessModel());
				}
			}
			return;
		}

		// 	Dynamic rule 10.10
		boolean allCanceled = true;
		for (int i = 0; i < states.length; i++) {
			String state = states[i];
			if (!(state.equals(ProcessModel.CANCELED) ||
				state.equals(Plain.CANCELED))) {
				allCanceled = false;
				break;
			}
		}
		if (allCanceled) {
			if (! currstate.equals (ProcessModel.CANCELED)){
				//TODO processModel.setPmStateWithMessage(ProcessModel.CANCELED);
				this.logging.registerProcessModelEvent(processModel, "ToCanceled", "Rules G10.10"); //$NON-NLS-1$ //$NON-NLS-2$
				Decomposed superActDec = processModel.getTheDecomposed();
				if(superActDec == null){
					return;
				}
				else{
					this.cancelPropagation(superActDec);
					this.determineProcessModelStates(superActDec.getTheProcessModel());
				}
			}
			return;
		}

		// Dynamic rules 10.4, 10.5
		boolean hasOnlyAbstracts = true,
		isAbstract = true;
		for (int i = 0; i < states.length; i++) {
			String type  = types[i];
			String state = states[i];
			if (!state.equals(ProcessModel.ABSTRACT)) {
				hasOnlyAbstracts = false;
				isAbstract = false;
				break;
			}
			if (isAbstract && (state.equals(ProcessModel.ENACTING)
				|| state.equals(ProcessModel.INSTANTIATED)
				|| type.equals("Automatic")
				|| type.equals("NormalWithAgent"))) {  //$NON-NLS-1$
				isAbstract = false;
				break;
			}
		}
		if (hasOnlyAbstracts || isAbstract) {
			if (! currstate.equals (ProcessModel.ABSTRACT)){
				//TODO processModel.setPmStateWithMessage(ProcessModel.ABSTRACT);
				this.logging.registerProcessModelEvent(processModel, "ToAbstract", "Rules 10.4 and 10.5"); //$NON-NLS-1$ //$NON-NLS-2$
				Decomposed superActDec = processModel.getTheDecomposed();
				if(superActDec == null){
					return;
				}
				else{
					this.determineProcessModelStates(superActDec.getTheProcessModel()/*,currentSession*/);
				}
			}
			return;
		}

		// 	Dynamic rules 10.6, 10.7, 10.8
		boolean hasAnyInstant = false,
		hasAnyEnacting = false,
		hasAnyNormal = false,
		hasAnyAutomatic = false;
		for (int i = 0; i < states.length; i++) {
			String state = states[i];
			String type  = types[i];
			if (state.equals(ProcessModel.INSTANTIATED)) {
				hasAnyInstant = true;
			}
			else
				if (state.equals(ProcessModel.ENACTING) ||
					state.equals(Plain.ACTIVE)) {
					hasAnyEnacting = true;
				}
			if (type.equals ("NormalWithAgent"))  //$NON-NLS-1$
				hasAnyNormal = true;
			else if (type.equals ("Automatic")) //$NON-NLS-1$
				hasAnyAutomatic = true;
		}
		if (! hasAnyEnacting &&
			(hasAnyInstant || hasAnyNormal || hasAnyAutomatic)){
			// Condition added (currstate.equals (ProcessModel.ENACTING)) to avoid state regression.
			// In other words, a process model must not go back from ENACTING to INSTANTIATED!
			if (!currstate.equals (ProcessModel.INSTANTIATED)
				&& !currstate.equals (ProcessModel.ENACTING)){

				//TOOD processModel.setPmStateWithMessage(ProcessModel.INSTANTIATED);
				if(hasAnyInstant)
					this.logging.registerProcessModelEvent(processModel, "ToInstantiated", "Rule 10.6"); //$NON-NLS-1$ //$NON-NLS-2$
				else if (hasAnyNormal)
					this.logging.registerProcessModelEvent(processModel, "ToInstantiated", "Rule 10.7"); //$NON-NLS-1$ //$NON-NLS-2$
				else if (hasAnyAutomatic)
					this.logging.registerProcessModelEvent(processModel, "ToInstantiated", "Rule 10.8"); //$NON-NLS-1$ //$NON-NLS-2$
				Decomposed superActDec = processModel.getTheDecomposed();
				if(superActDec == null){
					return;
				}
				else{
					this.determineProcessModelStates(superActDec.getTheProcessModel());
				}
			}
		}

		// 	Dynamic rule 10.9
		boolean allFinished = true;
		for (int i = 0; i < states.length; i++) {
			String state = states[i];
			if (!(state.equals(ProcessModel.FINISHED) || state.equals(Plain.FINISHED))
				&& !(state.equals(ProcessModel.FAILED) || state.equals(Plain.FAILED))
				&& !(state.equals(ProcessModel.CANCELED) || state.equals(Plain.CANCELED))) {
				allFinished = false;
				break;
			}
		}
		if (allFinished) {
			if (! currstate.equals (ProcessModel.FINISHED)){
				//TODO processModel.setPmStateWithMessage(ProcessModel.FINISHED);
				this.logging.registerProcessModelEvent(processModel, "ToFinished", "Rule 10.x"); //$NON-NLS-1$ //$NON-NLS-2$
				Decomposed superActDec = processModel.getTheDecomposed();
				if(superActDec == null){
					this.rootModelHasFinished(processModel);
					return;
				}
				else{
					this.searchForFiredConnections(superActDec.getTheProcessModel().getOid(), "Rule 10.x");
					this.searchForReadyActivities(superActDec.getTheProcessModel().getOid());
					this.determineProcessModelStates(superActDec.getTheProcessModel());
				}
			}
			return;
		}
	}

	/**
	 * Searches for Activities in the Process Model that are in Waiting,
	 * so changes their state to Ready.
     */
    public void searchForReadyActivities (Integer pmodel_id /*, Session currentSession*/)
		throws WebapseeException {

		/*boolean close = false;
		if(currentSession == null){
			try {
				currentSession = BaseDAO.getNewSession();
				System.out.println("Session reloaded (new session)!");
				ProcessModelDAO dao = new ProcessModelDAO();
				dao.setSession(currentSession);
				pmodel = (ProcessModel) dao.findByPrimaryKey(pmodel.getOid());
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			close = true;
		}*/
    	ProcessModel pmodel = this.processModelDAO.retrieve(pmodel_id);

		Collection activities = pmodel.getTheActivity();
		Iterator iter = activities.iterator();
		while (iter.hasNext()) {
			Activity activity = (Activity) iter.next();

			if (! isReadyToBegin(activity))
				continue;

			if (activity instanceof Normal) {
				//Rule 2.1
				Normal normal = (Normal)activity;
				EnactionDescription enact =	normal.getTheEnactionDescription();

				if (enact.getState().equals (Plain.WAITING)) {
				    // setar ready somente se nao tiver delegated
					//TODO enact.setStateWithMessage (Plain.READY);
					this.changeTasksState (normal, Plain.READY);
					this.logging.registerGlobalActivityEvent (normal, "ToReady", "Rule 2.1"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
			else if (activity instanceof Automatic) {
				//Rule 2.3
				Automatic automatic = (Automatic)activity;
				EnactionDescription enact = automatic.getTheEnactionDescription();
				if (enact.getState().equals (Plain.WAITING)) {
					//TODO enact.setStateWithMessage (Plain.READY);
					this.logging.registerGlobalActivityEvent (automatic, "ToReady", "Rule 2.3"); //$NON-NLS-1$ //$NON-NLS-2$
					this.execAutomaticActivity (automatic);
				}
			}
			else if (activity instanceof Decomposed) {
				//Rule 2.2 and Rule 2.4
				Decomposed decomposed = (Decomposed)activity;
				ProcessModel fragment = decomposed.getTheReferedProcessModel();
				if(fragment.getPmState().equals(ProcessModel.INSTANTIATED)){
					this.searchForReadyActivities (fragment.getOid());
				}
			}
		}
		/*if(close)
			try {
				BaseDAO.closeSession(currentSession);
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
    }

    /**
     * called by APSEE Manager.
     * Applies to the Rules 8.1, 8.10, 8.23, 8.24,
     *
     * Searches for Connections the are ready to be
     * fired.
     */
	public void searchForFiredConnections(Integer pmodel_id, String why)
            throws WebapseeException {

		ProcessModel pmodel = this.processModelDAO.retrieve(pmodel_id);

	    Collection connections = pmodel.getTheConnection();

	    while (true) {
	    	boolean fired = false;
	        Iterator iter = connections.iterator();
	        while (iter.hasNext()) {
	        	Connection connection = (Connection)iter.next();
	        	if (!(connection instanceof MultipleCon))
	        		continue;
	        	MultipleCon multiplecon = (MultipleCon)connection;
        		if (multiplecon.isFired().booleanValue())
	        		continue;
	        	if (multiplecon instanceof JoinCon) {
	        		Join join = (JoinCon)multiplecon;
	        		String dep = joinCon.getTheDependency().getKindDep();
	        		if (this.isJoinSatisfied (joinCon)) {
	        			joinCon.setFired (new Boolean (true));
	        			if(join.getKindJoinCon().equals("AND")) //$NON-NLS-1$
	        				if (dep.equals("end-start")||dep.equals("end-end")) //$NON-NLS-1$ //$NON-NLS-2$
	        					this.logging.registerJoinEvent(joinCon,"Rule 8.1"); //$NON-NLS-1$
	        				else//start-start
	        					this.logging.registerJoinEvent(joinCon,"Rule 8.10"); //$NON-NLS-1$
	        			else if (join.getKindJoinCon().equals("OR")){ //$NON-NLS-1$
	        				Collection pred = this.getPredecessors(joinCon);
	        				Iterator iterPred = pred.iterator();
	        				if (dep.equals("end-start")||dep.equals("end-end")){ //$NON-NLS-1$ //$NON-NLS-2$
	        					while (iterPred.hasNext()) {
									Object obj = (Object) iterPred.next();
									if(obj instanceof MultipleCon){
										MultipleCon multi = (MultipleCon)obj;
										if ((multi instanceof BranchANDCon) &&
											multi.isFired().booleanValue()){
											this.logging.registerJoinEvent(joinCon,"Rule 8.4"); //$NON-NLS-1$
											break;
										}
										else if(multi instanceof BranchConCond &&
											multi.isFired().booleanValue()){
											this.logging.registerJoinEvent(joinCon,"Rule 8.3"); //$NON-NLS-1$
											break;
										}
										else if (multi instanceof JoinCon &&
											multi.isFired().booleanValue()){
											this.logging.registerJoinEvent(joinCon,"Rule 8.5"); //$NON-NLS-1$
											break;
										}
									}
									else{
										this.logging.registerJoinEvent(joinCon,why);
										break;
									}
								}
	        				}
	        				else //start-start
	        				{
	        					while (iterPred.hasNext()) {
									Object obj = (Object) iterPred.next();
									if(obj instanceof MultipleCon){
										MultipleCon multi = (MultipleCon)obj;
										if ((multi instanceof BranchANDCon) &&
											multi.isFired().booleanValue()){
											this.logging.registerJoinEvent(joinCon,"Rule 8.15"); //$NON-NLS-1$
											break;
										}
										else if(multi instanceof BranchConCond &&
											multi.isFired().booleanValue()){
											this.logging.registerJoinEvent(joinCon,"Rule 8.14"); //$NON-NLS-1$
											break;
										}
										else if (multi instanceof JoinCon &&
											multi.isFired().booleanValue()){
											this.logging.registerJoinEvent(joinCon,"Rule 8.16"); //$NON-NLS-1$
											break;
										}
									}
									else{
										this.logging.registerJoinEvent(joinCon,why);
										break;
									}
								}
	        				}
	        			}
	        			else // XOR
	        			{
	        				Collection pred = this.getPredecessors(joinCon);
	        				Iterator iterPred = pred.iterator();
	        				if (dep.equals("end-start")||dep.equals("end-end")){ //$NON-NLS-1$ //$NON-NLS-2$
	        					while (iterPred.hasNext()) {
									Object obj = (Object) iterPred.next();
									if(obj instanceof MultipleCon){
										MultipleCon multi = (MultipleCon)obj;
										if ((multi instanceof BranchANDCon) &&
											multi.isFired().booleanValue()){
											this.logging.registerJoinEvent(joinCon,"Rule 8.7"); //$NON-NLS-1$
											break;
										}
										else if(multi instanceof BranchConCond &&
											multi.isFired().booleanValue()){
											this.logging.registerJoinEvent(joinCon,"Rule 8.9"); //$NON-NLS-1$
											break;
										}
										else if (multi instanceof JoinCon &&
											multi.isFired().booleanValue()){
											this.logging.registerJoinEvent(joinCon,"Rule 8.8"); //$NON-NLS-1$
											break;
										}
									}
									else{
										this.logging.registerJoinEvent(joinCon,why);
										break;
									}
								}
	        				}
	        				else//start-start
	        				{
	        					while (iterPred.hasNext()) {
									Object obj = (Object) iterPred.next();
									if(obj instanceof MultipleCon){
										MultipleCon multi = (MultipleCon)obj;
										if ((multi instanceof BranchANDCon) &&
											multi.isFired().booleanValue()){
											this.logging.registerJoinEvent(joinCon,"Rule 8.22"); //$NON-NLS-1$
											break;
										}
										else if(multi instanceof BranchConCond &&
											multi.isFired().booleanValue()){
											this.logging.registerJoinEvent(joinCon,"Rule 8.20"); //$NON-NLS-1$
											break;
										}
										else if (multi instanceof JoinCon &&
											multi.isFired().booleanValue()){
											this.logging.registerJoinEvent(joinCon,"Rule 8.21"); //$NON-NLS-1$
											break;
										}
									}
									else{
										this.logging.registerJoinEvent(joinCon,why);
										break;
									}
								}
	        				}
	        			}
	        			fired = true;
	        		}
	        	}
	        	else if (multiplecon instanceof BranchCon) {
	        		Branch branch = (BranchCon)multiplecon;
	        		String dep = branchCon.getTheDependency().getKindDep();
	        		if (this.isBranchSatisfied (branchCon)) {
	        			branchCon.setFired (new Boolean (true));
	        			if (dep.equals("end-start")||dep.equals("end-end")) //$NON-NLS-1$ //$NON-NLS-2$
	        				this.logging.registerBranchEvent(branchCon,"Rule 8.23"); //$NON-NLS-1$
	        			else//start-start
	        				this.logging.registerBranchEvent(branchCon,"Rule 8.24"); //$NON-NLS-1$
	        			fired = true;
	        		}
	        	}
	        }
	        if (! fired)
	        	break;
	    }
	}

// ------------------ Here private Methods of the Engine  -----------------------------------------
    				// e.g. implementation of the rules

    // Reset Process implementation.

    /**
     * Reset all the Process Agendas of the Process.
     */
	private void agendaReset (Process process)
		throws WebapseeException {

		Collection agendas = process.getTheProcessAgenda();
		Iterator iter = agendas.iterator();
		while (iter.hasNext()) {
			ProcessAgenda procAgenda = (ProcessAgenda) iter.next();
			if(procAgenda != null){
				Collection tasks = procAgenda.getTheTask();
				Iterator iterTasks = tasks.iterator();
				while (iterTasks.hasNext()) {
					Task task = (Task) iterTasks.next();
					task.clearOcurrence();
					task.clearTheAgendaEvent();
					task.removeFromTheNormal();
					task.removeFromDelegatedTo();
					task.removeFromDelegatedFrom();
				}
				procAgenda.clearTheTask();
			}
		}
	}

	/**
	 * Reset the Process Model and its Activities and Connections.
	 */
	private void modelReset (ProcessModel processModel) throws WebapseeException {

		String procModelState = processModel.getPmState();
		if ((procModelState.equals(ProcessModel.ENACTING)) ||
		    (procModelState.equals(ProcessModel.INSTANTIATED)) ||
		    (procModelState.equals(ProcessModel.MIXED)))
			//TODO
			//processModel.setPmStateWithMessage(ProcessModel.INSTANTIATED);

		this.logging.registerProcessModelEvent(processModel, "To"+ProcessModel.INSTANTIATED, "Rule ?"); //$NON-NLS-1$ //$NON-NLS-2$

		this.activitiesReset(processModel);
		this.connectionsReset(processModel);
	}

	/**
	 * Reset all the Activities from the Process Model.
	 */
	private void activitiesReset(ProcessModel processModel) throws WebapseeException{

		Collection acts = processModel.getTheActivity();
		if (acts.size() > 0) {
			Iterator iter = acts.iterator();
			while (iter.hasNext()) {
				Activity act = (Activity) iter.next();
				// Cleanup of version information
				act.setHasVersions(new LinkedList());
				if (act instanceof Plain) {
					Plain actPlain = (Plain)act;
					actPlain.removeFromTheEnactionDescription();
					actPlain.setTheEnactionDescription(new EnactionDescription());
				}
				else{
					Decomposed actDec = (Decomposed)act;
					this.modelReset(actDec.getTheReferedProcessModel());
				}
			}
		}
	}

	/**
	 * Reset all the Connections from the Process Model.
	 */
	private void connectionsReset(ProcessModel processModel) throws WebapseeException {

		Collection conns = processModel.getTheConnection();
		Iterator iter = conns.iterator();
		if(conns.size()>0){
			while (iter.hasNext()) {
				Connection conn = (Connection) iter.next();
				if (conn instanceof MultipleCon){
					MultipleCon multConn = (MultipleCon)conn;
					multConn.setFired(new Boolean(false));
				}
			}
		}
	}

	// Agents - implementation of the methods that involve Agents.

	/**
	 * Verifies if an Agent is allocated to a Normal Activity.
	 */
	private boolean isAgentAllocatedToActivity(Agent agent, Normal actNorm){

		Collection agents = this.getInvolvedAgents(actNorm);
		return agents.contains(agent);
	}

	/**
	 * Delegates a Task from an Agent to another one, adjusting their
	 * Task Agendas.
	 */
	private RequiredPeople delegateTaskToAgent(Agent from_agent, Normal actNorm,
    	Agent to_agent){

        Process process = this.getTheProcess(actNorm.getTheProcessModel());
        String process_id = process.getIdent();

    	Task from_task = null;
    	RequiredPeople req = null;

    	Collection reqP = actNorm.getTheRequiredPeople();
    	Iterator iter = reqP.iterator();
    	while (iter.hasNext()) {
			RequiredPeople reqPeople = (RequiredPeople) iter.next();
			if (reqPeople instanceof ReqAgent){
				ReqAgent reqAgent = (ReqAgent)reqPeople;
				if(reqAgent != null){
					Agent agent = reqAgent.getTheAgent();
					if(agent != null){
						if(agent.equals(from_agent)){
							req = reqAgent;
					    	//Removing Required Agent
							from_agent.removeFromTheReqAgent(reqAgent);
					    	//Adding Required Agent
							reqAgent.setTheAgent(to_agent);
							to_agent.insertIntoTheReqAgent(reqAgent);
							break;
						}
					}
				}
			}
/*			else{ // is ReqGroup NOT ALLOWED
			}
*/		}

    	// Finding the "From Task"
    	Collection from_procAgendas = from_agent.getTheTaskAgenda().getTheProcessAgenda();
    	Iterator from_iter = from_procAgendas.iterator();
    	while (from_iter.hasNext()) {
			ProcessAgenda from_procAgenda = (ProcessAgenda) from_iter.next();
			if (from_procAgenda.getTheProcess().getIdent().equals(process_id)){
		    	from_task = this.getTask(from_procAgenda, actNorm);
		    	if (from_task != null) break;
			}
		}

		// add task for toAgent
    	Task to_task = this.addTaskToAgent(to_agent, actNorm);
		to_task.setDelegatedFrom(from_agent);
		to_task.setLocalState(from_task.getLocalState());
		this.notifyAgentAbouActivityState(to_task);

    	// Setting Delegated in task from
    	from_task.setDelegatedTo(to_agent);
		from_task.setLocalState("Delegated"); //$NON-NLS-1$
		this.notifyAgentAbouActivityState(from_task);

		return req;
    }

	/**
	 * Verifies if a Normal Activity has Required Agents.
	 */
	private boolean hasInvolvedAgents(Normal actNorm){

    	boolean has = false;

    	Collection reqPeople = actNorm.getTheRequiredPeople();
    	Iterator iter = reqPeople.iterator();
    	while (iter.hasNext()) {
			RequiredPeople reqP = (RequiredPeople) iter.next();
			if(reqP instanceof ReqAgent){
				ReqAgent reqAgent = (ReqAgent)reqP;
				if(reqAgent.getTheAgent() != null){
					has = true;
					break;
				}
			}
			else {
			    ReqGroup reqGroup = (ReqGroup)reqP;
			    Group group  = reqGroup.getTheGroup();
			    if(group != null){
			        Collection agents = group.getTheAgent();
			    	Iterator iter2 = agents.iterator();
			    	while (iter2.hasNext()) {
						Agent agent = (Agent) iter2.next();
						if(agent != null){
							has = true;
							break;
						}
					}

			    	if(has) break;
			    }
			}
    	}
    	return has;
	}

	/**
	 * Adds a Task that refers to a Normal Activity to an Agent.
	 */
	private Task addTaskToAgent(Agent agent, Normal actNorm/*, Session currentSession*/){

		Task returnTask = null;
		TaskAgenda taskAgenda = agent.getTheTaskAgenda();
		Collection procAgendas = taskAgenda.getTheProcessAgenda();
		procAgendas.remove(null);
		if (procAgendas.isEmpty()){
			ProcessAgenda procAg = new ProcessAgenda();
			Process pro = this.getTheProcess(actNorm.getTheProcessModel());
			procAg.setTheProcess(pro);
			pro.getTheProcessAgenda().add(procAg);
			procAg.setTheTaskAgenda(taskAgenda);
			procAgendas.add(procAg);
			//TODO returnTask = procAg.addTask(actNorm, currentSession);
		}
		else{
			Iterator iter = procAgendas.iterator();
			boolean has = false;
			while (iter.hasNext()) {
				ProcessAgenda procAgenda = (ProcessAgenda) iter.next();
				if(procAgenda.getTheProcess().equals(this.getTheProcess(actNorm.getTheProcessModel()))){
					//TODO returnTask = procAgenda.addTask(actNorm, currentSession);
					has = true;
					break;
				}
			}
			if(!has){
			    ProcessAgenda procAg = new ProcessAgenda();
			    Process pro = this.getTheProcess(actNorm.getTheProcessModel());
			    procAg.setTheProcess(pro);
			    pro.getTheProcessAgenda().add(procAg);
			    procAg.setTheTaskAgenda(taskAgenda);
			    procAgendas.add(procAg);
			    //TODO returnTask = procAg.addTask(actNorm, currentSession);
			}
		}

		taskAgenda.setTheProcessAgenda(procAgendas);

		return returnTask;
    }

	/**
	 * Updates the Agent's agenda (Process Agenda) after an Event
	 * (Activity's State Change).
	 */
	private void updateAgenda
	(Agent agent, Normal actNorm, String state, String why) {
		Task task = this.setTaskState(agent, actNorm, state);
		if(task != null){
			this.logging.registerAgendaEvent(task, "To" + state, why); //$NON-NLS-1$
		}
    }

	private void notifyAgentAbouActivityState(Task task){
 		// Notify the agente of the new state of the task!!
        String message = "<MESSAGE>" +  //$NON-NLS-1$
        					"<ACTIVITYSTATE>" +  //$NON-NLS-1$
        						"<OID>" + task.getOid() + "</OID>" +  //$NON-NLS-1$ //$NON-NLS-2$
                                "<CLASS>" + task.getClass().getName()+ "</CLASS>" + //$NON-NLS-1$ //$NON-NLS-2$
        						"<ID>" + task.getTheNormal().getIdent() + "</ID>" +  //$NON-NLS-1$ //$NON-NLS-2$
        						"<NEWSTATE>" + task.getLocalState() + "</NEWSTATE>" +  //$NON-NLS-1$ //$NON-NLS-2$
        						"<BY>APSEE_Manager</BY>" +  //$NON-NLS-1$
        					"</ACTIVITYSTATE>" +  //$NON-NLS-1$
        				"</MESSAGE>"; //$NON-NLS-1$
        try {
        	if(this.remote==null){
        		reloadRemote();

        	}
        	if(this.remote!=null){
        		Agent agent = task.getTheProcessAgenda().getTheTaskAgenda().getTheAgent();
        		this.remote.sendMessageToUser(message, agent.getIdent());
        	}
        } catch (RemoteException e) {
           	//  e.printStackTrace();
           	 System.out.println("EnactmentEngine.updateAgenda() remote reference exception"); //$NON-NLS-1$
        }
	}

	public void reloadRemote(){
		/*try {
			this.remote = SecurityFacade.getInstance();
		} catch (Exception e){

		}*/
	}

	/**
	 * Returns all the Agents that are allocated to an Normal
	 * Activity.
	 */
	private Collection getInvolvedAgents (Normal normal){

		 Collection involvedAgents = new HashSet();
		 Collection reqPeople = normal.getTheRequiredPeople();
		 Iterator iter = reqPeople.iterator();
		 while (iter.hasNext()) {
			RequiredPeople reqP = (RequiredPeople) iter.next();
			if(reqP instanceof ReqAgent){
				ReqAgent reqAgent = (ReqAgent) reqP;
				Agent agent = reqAgent.getTheAgent();
				if(agent != null)
					involvedAgents.add(agent);
			}
			else{
			    ReqGroup reqGroup = (ReqGroup)reqP;
			    Group group  = reqGroup.getTheGroup();
			    if(group != null){
			        Collection agents = group.getTheAgent();
			    	Iterator iter2 = agents.iterator();
			    	while (iter2.hasNext()) {
						Agent agent = (Agent) iter2.next();
						if(agent != null){
							involvedAgents.add(agent);
						}
					}
			    }
			}
		 }
		 return involvedAgents;
	}

	/**
	 * Changes the state of a Task that refers to a Normal Activity.
	 */
	private void changeTasksState (Normal normal, String state) {
		Collection agents = this.getInvolvedAgents (normal);
		Iterator iter = agents.iterator();
		while (iter.hasNext()) {
			Agent agent = (Agent) iter.next();
			if(agent.getTheTaskAgenda() != null) this.setTaskState (agent, normal, state);
		}
	}

	/**
	 * Returns the Task's State, from an Agent's Process Agenda,
	 * that refers to a Normal Activity.
	 */
	private String getTaskState (Agent agent, Normal normal) {
		String state = null;
		TaskAgenda taskagenda = agent.getTheTaskAgenda();
		Collection processagendas = taskagenda.getTheProcessAgenda();
		Iterator iter = processagendas.iterator();
		while (iter.hasNext()) {
			ProcessAgenda processagenda = (ProcessAgenda)iter.next();
			Task task = this.getTask (processagenda, normal);
			if (task != null) {
				state = task.getLocalState();
				break;
			}
		}
		return (state);
	}

	/**
	 * Changes the Task's State, from an Agent's Process Agenda,
	 * that refers to a Normal Activity.
	 */
	private Task setTaskState
	(Agent agent, Normal normal, String state) {
		System.out.println("Agent: " + agent);
		System.out.println("Task agenda: " + agent.getTheTaskAgenda());

		TaskAgenda taskagenda = agent.getTheTaskAgenda();
		Collection processagendas = taskagenda.getTheProcessAgenda();
		Iterator iter = processagendas.iterator();
		while (iter.hasNext()) {
			ProcessAgenda processagenda = (ProcessAgenda)iter.next();
			Task task = this.getTask (processagenda, normal);
			if (task != null) {
				String oldLocalState = task.getLocalState();
			    if (!oldLocalState.equals("Delegated")) {
			    	task.setLocalState(state);
			    }

			    // Date settings
			    if(task.getLocalState().equals(Plain.READY)
			    	|| task.getLocalState().equals(Plain.WAITING)){
			    	task.setBeginDate(null);
			    	task.setEndDate(null);
			    }
			    else if(oldLocalState.equals(Plain.READY)){
			    	task.setBeginDate(new Date());
			    }
			    else if(task.getLocalState().equals(Plain.FINISHED)){
			    	task.setEndDate(new Date());
			    }

				this.notifyAgentAbouActivityState(task);
				return task;
			}
		}
		return null;
	}

	/**
	 * Returns the Task that refers to a Normal Activity from a Process Agenda.
	 */
	private Task getTask(ProcessAgenda procAgenda, Normal normal){

		Task theTask = null;
		Collection tasks = procAgenda.getTheTask();
 		Iterator iter = tasks.iterator();
 		while (iter.hasNext()) {
 			Task task = (Task) iter.next();
 			if (task.getTheNormal().equals(normal)){
 				theTask = task;
 				break;
 			}
 		}
 		return theTask;
	}

    /*  Enactment - implementation of the methods that involve, basically,
     				the executeProcess() method and methods that change the
					state of the Activities, Process Models and Process.
	*/

   /**
    * Creates the Tasks (from the Activities) for the Agents. If the activities
    *  are Decomposed then calls itself again, else calls createTasksNormal().
    */
	public void createTasks(ProcessModel procModel)
            throws WebapseeException {
    	Collection acts = procModel.getTheActivity();
    	acts.remove(null);
    	if (acts.isEmpty()){
    	    return;
    	}

    	//TODO procModel.setPmStateWithMessage(ProcessModel.INSTANTIATED);

    	Iterator iter = acts.iterator();
    	while (iter.hasNext()) {
			Activity act = (Activity) iter.next();
			if(act instanceof Normal){
			   Normal actNorm = (Normal)act;
			   this.createTasksNormal(actNorm);
			}
			else if(act instanceof Automatic){
				Automatic actAuto = (Automatic)act;
				//TODO actAuto.getTheEnactionDescription().setStateWithMessage(Plain.WAITING);
				this.logging.registerGlobalActivityEvent(actAuto, "ToWaiting", "Rule 1.4"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			else if(act instanceof Decomposed){
				Decomposed actDecomp = (Decomposed)act;
				this.createTasks(actDecomp.getTheReferedProcessModel());
			}
		}
    }

   /**
    * Creates the Tasks (from the Normal Activities) for the Agents and set the
    * Tasks' Local state to Waiting.
    */
    public void createTasksNormal(Normal actNorm/*, Session currentSession*/)
            throws WebapseeException {

    	Collection reqPeople = this.getInvolvedAgents(actNorm);
    	Iterator iter = reqPeople.iterator();
    	boolean has = iter.hasNext();
    	while (iter.hasNext()) {
    		Agent agent = (Agent) iter.next();
   			this.addTaskToAgent (agent, actNorm);
   			this.updateAgenda(agent, actNorm, Plain.WAITING, "Rule 1.2");
		}

    	if(has){
    		//TODO actNorm.getTheEnactionDescription().setStateWithMessage(Plain.WAITING);
    		this.logging.registerGlobalActivityEvent(actNorm, "ToWaiting", "Rule 1.2"); //$NON-NLS-1$ //$NON-NLS-2$
    	}
    }

    /**
     * Verifies if a Process Model is ready for the Task, i.e.,
     * The Process Model is Instantiated or Enacting.
     */
    private boolean isReadyForTask(String state) {
    	return (state.equals (ProcessModel.INSTANTIATED) ||
    		state.equals (ProcessModel.ENACTING));
    }

    /**
     * Determine that a Plain Activity has started, propagating to the Process Model.
     */
    private void plainActivityHasStarted (Integer pmodel_id/*, Session currentSession*/) throws WebapseeException {

    	ProcessModel pmodel =  this.processModelDAO.retrieve(pmodel_id);
    	ProcessModel parentModel = pmodel;
		String why = ""; //$NON-NLS-1$

    	if (pmodel.getPmState().equals (ProcessModel.INSTANTIATED)) {
    		//TODO pmodel.setPmStateWithMessage(ProcessModel.ENACTING);
        	if(!this.isTheRootProcess(pmodel)){
        		this.logging.registerProcessModelEvent (pmodel, "ToEnacting", "Rule 4.4"); //$NON-NLS-1$ //$NON-NLS-2$
        		parentModel = pmodel.getTheDecomposed().getTheProcessModel();
    			Collection conns = this.getConnectionsTo(pmodel.getTheDecomposed());
                Iterator iterC = conns.iterator();
                while (iterC.hasNext()) {
        			Connection conn = (Connection) iterC.next();
        			if (conn instanceof JoinCon){
        				Join join = (JoinCon)conn;
        				String dep = joinCon.getTheDependency().getKindDep();
            			if (join.getKindJoinCon().equals("OR")){ //$NON-NLS-1$
            				if(dep.equals("start-start")){ //$NON-NLS-1$
            					why = "Rule 8.11"; //$NON-NLS-1$
            					break;
            				}
            			}
            			else if (join.getKindJoinCon().equals("XOR")) //$NON-NLS-1$
            				if (dep.equals("start-start")){ //$NON-NLS-1$
            					why = "Rule 8.19"; //$NON-NLS-1$
            					break;
            				}
        			}
        		}
        		this.processModelHasStarted(parentModel.getOid(), why);
        	}
        	else
        		this.logging.registerProcessModelEvent (pmodel, "ToEnacting", "Rule 4.6"); //$NON-NLS-1$ //$NON-NLS-2$
    	}
    	this.searchForFiredConnections (parentModel.getOid(), why);
        this.searchForReadyActivities (parentModel.getOid());
    }

    /**
     * Determine that a Process Model has started, propagating to the Parent Process Model.
     */
	private void processModelHasStarted (Integer procModel_id, String why/*, Session currentSession*/)
		throws WebapseeException {

		ProcessModel procModel = this.processModelDAO.retrieve(procModel_id);
		// 	if modelId is not the root model, apply rule for its parent model
		if (!this.isTheRootProcess(procModel)) {
			if (procModel.getPmState().equals (ProcessModel.INSTANTIATED)) {
				//TODO procModel.setPmStateWithMessage(ProcessModel.ENACTING);
				this.logging.registerProcessModelEvent (procModel, "ToEnacting", "Rule 4.5"); //$NON-NLS-1$ //$NON-NLS-2$
				ProcessModel parentModel = procModel.getTheDecomposed().getTheProcessModel();
				this.processModelHasStarted (parentModel.getOid(), why);
			}
			this.searchForFiredConnections (procModel.getOid(), why);
			this.searchForReadyActivities (procModel.getOid());
		}
		else{
			if (procModel.getPmState().equals (ProcessModel.INSTANTIATED)) {
				//TODO procModel.setPmStateWithMessage(ProcessModel.ENACTING);
				this.logging.registerProcessModelEvent (procModel,"ToEnacting", "Rule 4.7"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			this.searchForFiredConnections (procModel.getOid(), ""); //$NON-NLS-1$
			this.searchForReadyActivities (procModel.getOid());
		}
	}

	/**
	 * Verifies if the Process Model is the Root Process Model.
	 */
	private boolean isTheRootProcess(ProcessModel procModel){

		Decomposed decAct = procModel.getTheDecomposed();

		if(decAct == null)
			return true;
		return false;
	}

	/**
	 * Verifies if a Normal Activity is on the respective state.
	 */
    private boolean isNormalActivityOnState(Normal act, String state){

    	Collection agents = this.getInvolvedAgents(act);
    	Iterator iter = agents.iterator();
    	while (iter.hasNext()) {
			Agent agent = (Agent) iter.next();
    		Collection processAgendas = agent.getTheTaskAgenda().getTheProcessAgenda();
	    	Iterator iter2 = processAgendas.iterator();
	    	while (iter2.hasNext()) {
	    		ProcessAgenda procAgenda = (ProcessAgenda) iter2.next();
	    		if(procAgenda != null){
	    			Process actProcess = this.getTheProcess(act.getTheProcessModel());
	    			Process agendaProcess = procAgenda.getTheProcess();

	    			if(actProcess.equals(agendaProcess)){ // optimizing code

	    				Collection tasks = procAgenda.getTheTask();
	    				Iterator iter3 = tasks.iterator();
	    				while (iter3.hasNext()) {
	    					Task task = (Task) iter3.next();
	    					if(task.getTheNormal().equals(act))
	    						if(!task.getLocalState().equals(state)){
	    							return false;
	    						}
	    	   			}
	    			}
	    		}
	    	}
    	}
    	return true;
    }

	/**
     * This method is used as a condition (if a Normal Activity is Paused)
     * in rule 3.8. (in case of true), or it is used as a condition in
     * rule 3.9. (in case of false).
     */
    private boolean isActivityPaused(Normal act){

    	return this.isNormalActivityOnState(act,Plain.PAUSED);
    }

	/**
     * This method is used as a condition (if a Normal Activity is Finished)
     * in rule 3.6. (in case of true), or it is used as a condition in
     * rule 3.7. (in case of false).
     */
    public boolean isActivityFinished(Normal act){

    	return this.isNormalActivityOnState(act,Plain.FINISHED);
    }

    private boolean isSuperProcessModelOK(Activity activity) throws WebapseeException{

    	boolean ok = true;

		Decomposed actDec = activity.getTheProcessModel().getTheDecomposed();
		if(actDec == null){
			return ok;
		}
		else{
			if (ok){
				ok = this.isSuperProcessModelOK(actDec);
			}

			if (ok) {
				// check simple connections to activity
				Collection connections = actDec.getFromSimpleCon();
				Iterator iter = connections.iterator();
				while (iter.hasNext()) {
					SimpleCon simplecon = (SimpleCon) iter.next();
					if (! (simplecon instanceof Sequence))
						continue;
					Activity activityFrom = simplecon.getFromActivity();
					boolean isPlain = activityFrom instanceof Plain;
					String state = getState (activityFrom);
					Sequence sequence = (Sequence)simplecon;
					String kindDep = sequence.getTheDependency().getKindDep();
					if (! isDepSatisfiedToBegin(kindDep, state, isPlain)) {
						ok = false;
						break;
					}
				}
			}

			if (ok) {
				// check also branchCones AND
				// NAC: !fired or dep = (start-start or end-start)
				Collection connections = actDec.getFromBranchANDCon();
				Iterator iter = connections.iterator();
				while (iter.hasNext()) {
					Branch branch = (BranchCon) iter.next();
					String kindDep = branchCon.getTheDependency().getKindDep();
					if ((kindDep.equals ("start-start") || //$NON-NLS-1$
						kindDep.equals ("end-start"))   && //$NON-NLS-1$
						! branchCon.isFired().booleanValue()) {
						ok = false;
						break;
					}
				}
			}

			if (ok) {
				// check also branchCones with condition
				// NAC: !fired or dep = (start-start or end_start)
				Collection connections = actDec.getTheBranchConCondToActivity();
				Iterator iter = connections.iterator();
				while (iter.hasNext()) {
					BranchCondToActivity bcta = (BranchConCondToActivity)iter.next();
					BranchCond branch = bcta.getTheBranchConCond();
					String kindDep = branchCon.getTheDependency().getKindDep();
					if ((kindDep.equals ("start-start") || //$NON-NLS-1$
						kindDep.equals ("end-start") )){   //$NON-NLS-1$
						if(!branchCon.isFired().booleanValue()){
							ok = false;
							break;
						}
						else{ // fired
							if(!this.isConditionSatisfied (branchCon, actDec)){
								ok = false;
							}
						}
					}
				}
			}

			if (ok) {
				// 	check also joinCons
				// NAC: !fired or dep = (start-start or end_start)
				Collection connections = actDec.getFromJoinCon();
				Iterator iter = connections.iterator();
				while (iter.hasNext()) {
					Join join = (JoinCon) iter.next();
					String kindDep = joinCon.getTheDependency().getKindDep();
					if ((kindDep.equals ("start-start") || //$NON-NLS-1$
						kindDep.equals ("end-start")) && //$NON-NLS-1$
						! joinCon.isFired().booleanValue()) {
						ok = false;
						break;
					}
				}
			}
		}
    	return ok;
    }

    /**
	 * 	Verifies if an activity has all dependencies satisfied.
	 */
	private boolean isReadyToBegin (Activity activity/*, Session currentSession*/) throws WebapseeException {
		boolean ready;

		// Implicit condition of Rule 2.1:
		// normal activity must have at least one allocated agent

		if (activity instanceof Normal) {
			Normal normal = (Normal)activity;
			ready = this.hasInvolvedAgents(normal);
		}
		else ready = true;

		if(ready){
			// check process models above
			ready = this.isSuperProcessModelOK(activity);
		}

		if (ready) {
			// check simple connections to activity
			Collection connections = activity.getFromSimpleCon();
			Iterator iter = connections.iterator();
			while (iter.hasNext()) {
				SimpleCon simplecon = (SimpleCon) iter.next();
				if (! (simplecon instanceof Sequence))
					continue;
				Activity activityFrom = simplecon.getFromActivity();
				boolean isPlain = activityFrom instanceof Plain;
				String state = getState (activityFrom);
				Sequence sequence = (Sequence)simplecon;
				String kindDep = sequence.getTheDependency().getKindDep();
				if (! isDepSatisfiedToBegin(kindDep, state, isPlain)) {
					ready = false;
					break;
				}
			}
		}

		if (ready) {
			// check also branchCones AND
			// NAC: !fired or dep = (start-start or end-start)
			Collection connections = activity.getFromBranchANDCon();
			Iterator iter = connections.iterator();
			while (iter.hasNext()) {
				Branch branch = (BranchCon) iter.next();
				String kindDep = branchCon.getTheDependency().getKindDep();
				if ((kindDep.equals ("start-start") || //$NON-NLS-1$
					kindDep.equals ("end-start"))   && //$NON-NLS-1$
					! branchCon.isFired().booleanValue()) {
					ready = false;
					break;
				}
			}
		}

		if (ready) {
			// check also branchCones with condition
			// NAC: !fired or dep = (start-start or end_start)
			Collection connections = activity.getTheBranchConCondToActivity();
			Iterator iter = connections.iterator();
			while (iter.hasNext()) {
				BranchCondToActivity bcta = (BranchConCondToActivity)iter.next();
				BranchCond branch = bcta.getTheBranchConCond();
				String kindDep = branchCon.getTheDependency().getKindDep();
				if ((kindDep.equals ("start-start") || //$NON-NLS-1$
					kindDep.equals ("end-start") )){   //$NON-NLS-1$
					if(!branchCon.isFired().booleanValue()){
						ready = false;
						break;
					}
					else{ // fired
						if(!this.isConditionSatisfied (branchCon, activity)){
							this.cancelBranchSuccessor(activity,branchCon);
							ready = false;
						}
					}
				}
			}
		}

		if (ready) {
			// 	check also joinCons
			// NAC: !fired or dep = (start-start or end_start)
			Collection connections = activity.getFromJoinCon();
			Iterator iter = connections.iterator();
			while (iter.hasNext()) {
				Join join = (JoinCon) iter.next();
				String kindDep = joinCon.getTheDependency().getKindDep();
				if ((kindDep.equals ("start-start") || //$NON-NLS-1$
					kindDep.equals ("end-start")) && //$NON-NLS-1$
					! joinCon.isFired().booleanValue()) {
					ready = false;
					break;
				}
			}
		}

		return (ready);
	}

	/**
	 * Returns the state of an Activity, even if it is Plain
	 * or Decomposed.
	 */
	private String getState (Activity activity) {
		String state;
		if (activity instanceof Plain) {
			Plain plain = (Plain)activity;
			state = plain.getTheEnactionDescription().getState();
		}
		else { // decomposed
			Decomposed decomposed = (Decomposed)activity;
			state = decomposed.getTheReferedProcessModel().getPmState();
		}
		return (state);
	}

	/**
	 * Verifies if the Agent is the last one who needs to
	 * finish the Task to change the state of the Normal Activity.
	 */
	private boolean isLastToFinish (Agent agent, Normal actNorm)
		throws WebapseeException {

		boolean actCanFinish = false;

		Collection agents = this.getInvolvedAgents(actNorm);
		int nAgents = agents.size();

		if(nAgents == 1)
			actCanFinish = true;
		else{
			Iterator iter = agents.iterator();
			while (iter.hasNext()) {
				Agent ag = (Agent) iter.next();
				if (!agent.equals(ag)){
					String taskState = this.getTaskState(ag, actNorm);
					if(taskState.equals(Plain.FINISHED))
						actCanFinish = true;
					else{
						actCanFinish = false;
						break;
					}
				}
			}
		}
		return actCanFinish;
	}

	/**
	 * Verifies if the Agent is the last one who needs to
	 * finish the Task to change the state of the Normal Activity.
	 */
	private boolean isLastToPause (Agent agent, Normal actNorm)
		throws WebapseeException {

		boolean actCanPause = false;

		Collection agents = this.getInvolvedAgents(actNorm);
		int nAgents = agents.size();

		if(nAgents == 1)
			actCanPause = true;
		else{
			Iterator iter = agents.iterator();
			while (iter.hasNext()) {
				Agent ag = (Agent) iter.next();
				if (!agent.equals(ag)){
					String taskState = this.getTaskState(ag, actNorm);
					if(taskState.equals(Plain.PAUSED))
						actCanPause = true;
					else{
						actCanPause = false;
						break;
					}
				}
			}
		}
		return actCanPause;
	}

	/**
	 * Executes an Automatic Activity.
	 */
	private void execAutomaticActivity (Automatic automatic) {
		// not implemented yet
	}

    /**
     * called by APSEE Manager.
     * Applies to the rules 9.3, 9.4
     * Determines that an Activity has finished, propagating to the Process Model.
     */
	private void activityHasFinished (Activity act/*, Session currentSession*/)
		throws WebapseeException {

		String why = "";
		ProcessModel parentModel = act.getTheProcessModel();
		String state = this.getState(act);
		String modState = parentModel.getPmState();

		if (modState.equals (ProcessModel.ENACTING)) {

       		boolean feedbackTaken = false;

       		if(!state.equals(Plain.CANCELED)
       			&& !state.equals(ProcessModel.CANCELED)){
           		if (isFeedbackSource (act)) {
           			feedbackTaken = this.executeFeedbacks (act);
           		}
       		}

       		// if there was no feedback with a true condition, finish this task
       		if (! feedbackTaken) {

       			//Rule 4.1
       			if(this.isProcessModelReadyToFinish(parentModel)){
       				//TODO parentModel.setPmStateWithMessage(ProcessModel.FINISHED);
       				if(act instanceof Plain){
       					boolean isSubOfRoot = !this.isTheRootProcess(parentModel);
       					if(!isSubOfRoot)
       						this.logging.registerProcessModelEvent (parentModel, "ToFinished", "Rule 4.1"); //$NON-NLS-1$ //$NON-NLS-2$
       					else
       						this.logging.registerProcessModelEvent (parentModel, "ToFinished", "Rule 4.3"); //$NON-NLS-1$ //$NON-NLS-2$
       				} // is Decomposed
       				else{
       					this.logging.registerProcessModelEvent (parentModel, "ToFinished", "Rule 4.2"); //$NON-NLS-1$ //$NON-NLS-2$
       					Collection conns = this.getConnectionsTo(act);
       					Iterator iterC = conns.iterator();
       					while (iterC.hasNext()) {
       						Connection conn = (Connection) iterC.next();
       						if (conn instanceof JoinCon){
       							Join join = (JoinCon)conn;
       							String dep = joinCon.getTheDependency().getKindDep();
       							if (join.getKindJoinCon().equals("OR")){ //$NON-NLS-1$
       								if (dep.equals("end-start") || //$NON-NLS-1$
       									dep.equals("end-end")){ //$NON-NLS-1$
       									why = "Rule 8.2";  //$NON-NLS-1$
       									break;
       								}
       								else{
       									why = "Rule 8.11"; //$NON-NLS-1$
       									break;
       								}
       							}
       							else if (join.getKindJoinCon().equals("XOR")) //$NON-NLS-1$
       								if (dep.equals("end-start") || //$NON-NLS-1$
       									dep.equals("end-end")){ //$NON-NLS-1$
       									why = "Rule 8.6"; //$NON-NLS-1$
       									break;
       								}
       								else{
       									why = "Rule 8.19"; //$NON-NLS-1$
       									break;
       								}
       						}
       					}
       				}
       				if (!this.isTheRootProcess(parentModel)) {
       					// 	parent model is a fragment
       					this.activityHasFinished (parentModel.getTheDecomposed());
       				}
       				else {
       					// 	parent model is the root model
       					this.rootModelHasFinished (parentModel);
       				}
       			}
        		else {
        		    this.searchForFiredConnections (parentModel.getOid(),why);
        		    this.searchForReadyActivities (parentModel.getOid());
        		}
       		}
		}
		else {
		    this.searchForFiredConnections (parentModel.getOid(),why);
		    this.searchForReadyActivities (parentModel.getOid());
		}
	}

    /**
     * Determines that the Root Process Model has finished,
     * so it finishes the Process too.
     */
	private void rootModelHasFinished (ProcessModel processModel)
		throws WebapseeException {

		//TODO processModel.setPmStateWithMessage(ProcessModel.FINISHED);
		this.logging.registerProcessModelEvent(processModel, "ToFinished", "Rules G11.11");
		processModel.getTheProcess().setPState(Process.FINISHED);
		this.logging.registerProcessEvent(processModel.getTheProcess(), "ToFinished", "Rule ?");
	}

    /**
     * Determine if an Activity can finish, according to the Connections.
     */
	private boolean canFinish (Activity act){

		boolean actCanFinish = true;

		Collection connections = this.getConnectionsFrom(act);

		Collection actsFrom = new HashSet();

		Iterator iter = connections.iterator();
		while (iter.hasNext()) {
			Connection conn = (Connection) iter.next();
			if(conn instanceof Sequence){
				Sequence seq = (Sequence)conn;
				Dependency dep = seq.getTheDependency();
				Activity fromActivity = seq.getFromActivity();
				int state = this.simpleDepConnState (dep, fromActivity);
				if ((state & TO_ACT_CAN_FINISH) == 0) {
					actCanFinish = false;
					break;
				}
			}
			else if (conn instanceof MultipleCon) {
				MultipleCon multi = (MultipleCon)conn;
				Dependency dep = null;
				if (multi instanceof JoinCon) {
					Join join = (JoinCon)multi;
					Collection preds = this.getPredecessors(joinCon);
					Iterator iterPreds = preds.iterator();
					while (iterPreds.hasNext()) {
						Object predecessor = (Object) iterPreds.next();
						if(predecessor instanceof Activity)	actsFrom.add(predecessor);
					}
					dep = joinCon.getTheDependency();
				}
				else if (multi instanceof BranchCon) {
					Branch branch = (BranchCon)multi;
					Collection preds = this.getPredecessors(branchCon);
					Iterator iterPreds = preds.iterator();
					while (iterPreds.hasNext()) {
						Object predecessor = (Object) iterPreds.next();
						if(predecessor instanceof Activity)	actsFrom.add(predecessor);
					}

					dep = branchCon.getTheDependency();
				}
				else
					continue;

				String depType = dep.getKindDep();
				if (depType.equals ("end-end")){
					boolean depOk = true;
					Iterator iterActsFrom = actsFrom.iterator();
					while (iterActsFrom.hasNext()) {
						Activity actFrom = (Activity) iterActsFrom.next();
						String state = this.getState(actFrom);
						boolean isPlain = actFrom instanceof Plain;
						depOk = this.isDepSatisfiedToFinish(depType, state, isPlain);
						if(!depOk)	break;
					}
					if (!multi.isFired().booleanValue() || !depOk) {
						actCanFinish = false;
						break;
					}
				}
			}
		}

		if(actCanFinish){
			if(!this.areDependenciesOKToFinish(act, act.getTheProcessModel())){
				actCanFinish = false;
			}
		}
		return actCanFinish;
	}

	private boolean areDependenciesOKToFinish(Activity act, ProcessModel procModel){

		if(!this.isTheRootProcess(procModel)){

			// Check if it is the last activity to finish
			if(this.isActivityLastToFinish(act, procModel)){
				// The NACs from the rule 4.2 must be checked.
				if(this.areNACsOk(procModel)){
					Decomposed actDec = procModel.getTheDecomposed();
					return this.areDependenciesOKToFinish(actDec, actDec.getTheProcessModel());
				}
				else return false;
			}
			else{
				// If it is plain, predecessors at same process model were already checked in canFinish.
				if(act instanceof Plain) return true;
				// It is decomposed, the NACs from the rule 4.2 must be checked.
				if(this.areNACsOk(procModel)){
					Decomposed actDec = procModel.getTheDecomposed();
					return this.areDependenciesOKToFinish(actDec, actDec.getTheProcessModel());
				}
				else return false;
			}
		}
		return true;
	}

	private boolean areNACsOk(ProcessModel procModel){
		boolean ret = true;

		// NACs from the rule 4.2 must be checked.
		Decomposed actDec = procModel.getTheDecomposed();
	    Collection froms = this.getConnectionsFrom(actDec);
	    Iterator iterfroms = froms.iterator();
	    while (iterfroms.hasNext()) {
            Connection conn = (Connection) iterfroms.next();
            if(conn instanceof Sequence){ // Rule 4.2 NAC-04
                Sequence seq = (Sequence)conn;
                Activity from = seq.getFromActivity();
                String state = this.getState(from);
                if(seq.getTheDependency().getKindDep().equals("end-end")
                    &&(!(state.equals(Plain.FINISHED)
                    || state.equals(ProcessModel.FINISHED)))){
                    ret = false;
                    break;
                }
            }
            else if(conn instanceof BranchANDCon){ // Rule 4.2 NAC-01
                BranchAND bAND = (BranchANDCon)conn;
                if(!bAND.isFired().booleanValue()
                   && bAND.getTheDependency().getKindDep().equals("end-end")){
                    ret = false;
                    break;
                }
            }
            else if(conn instanceof BranchConCond){
                BranchCond bCond = (BranchConCond)conn;
                if(bCond.getTheDependency().getKindDep().equals("end-end")){
                   if(!bCond.isFired().booleanValue()){ // Rule 4.2 NAC-02
                       ret = false;
                       break;
                   }
                   else{ // Rule 4.2 NAC-03
                       Collection bctas = bCond.getTheBranchConCondToActivity();
                       Iterator iterBctas = bctas.iterator();
                       while (iterBctas.hasNext()) {
                           BranchCondToActivity bcta = (BranchConCondToActivity) iterBctas.next();
                           if(bcta.getTheActivity().equals(actDec)
                               && !this.conditionValue(bcta.getTheCondition())){
                               ret = false;
                               break;
                           }
                       }
                       if(!ret) break;
                   }
                }
            }
            else if(conn instanceof JoinCon){ // Rule 4.2 NAC-01
                Join join = (JoinCon)conn;
                if(!joinCon.isFired().booleanValue()
                   && joinCon.getTheDependency().getKindDep().equals("end-end")){ //$NON-NLS-1$
                   ret = false;
                   break;
                }
            }
        }
	    return ret;
	}

	private boolean isActivityLastToFinish(Activity act, ProcessModel procModel){

		Collection acts = procModel.getTheActivity();
        Iterator iter = acts.iterator();
        while (iter.hasNext()) {
            Activity activity = (Activity) iter.next();
            if(activity.equals(act)) continue;

            String state = this.getState(activity);
            if (!(state.equals(Plain.FINISHED) || state.equals(ProcessModel.FINISHED))
                && !(state.equals(Plain.FAILED) || state.equals(ProcessModel.FAILED))
                && !(state.equals(Plain.CANCELED) || state.equals(ProcessModel.CANCELED))) {

            	return false;
            }
        }
        return true;
	}

	/**
	 * Verifies if the process Model is ready to fish, i.e.,
	 * if the last activities of the Model have finished.
	 */
	private boolean isProcessModelReadyToFinish(ProcessModel procModel){

		boolean ret = true;

		if(!this.isTheRootProcess(procModel)){
		    Decomposed actDec = procModel.getTheDecomposed();
		    Collection froms = this.getConnectionsFrom(actDec);
		    Iterator iterfroms = froms.iterator();
		    while (iterfroms.hasNext()) {
                Connection conn = (Connection) iterfroms.next();

                if(conn instanceof BranchANDCon){ // Rule 4.2 NAC-01
                    BranchAND bAND = (BranchANDCon)conn;
                    if(!bAND.isFired().booleanValue()
                       && bAND.getTheDependency().getKindDep().equals("end-end")){
                        ret = false;
                        break;
                    }
                }
                else if(conn instanceof BranchConCond){
                    BranchCond bCond = (BranchConCond)conn;
                    if(bCond.getTheDependency().getKindDep().equals("end-end")){
                       if(!bCond.isFired().booleanValue()){ // Rule 4.2 NAC-02
                           ret = false;
                           break;
                       }
                       else{ // Rule 4.2 NAC-03
                           Collection bctas = bCond.getTheBranchConCondToActivity();
                           Iterator iterBctas = bctas.iterator();
                           while (iterBctas.hasNext()) {
                               BranchCondToActivity bcta = (BranchConCondToActivity) iterBctas.next();
                               if(bcta.getTheActivity().equals(actDec)
                                   && !this.conditionValue(bcta.getTheCondition())){
                                   ret = false;
                                   break;
                               }
                           }
                           if(!ret)
                               break;
                       }
                    }
                }
                else if(conn instanceof JoinCon){ // Rule 4.2 NAC-01
                    Join join = (JoinCon)conn;
                    if(!joinCon.isFired().booleanValue()
                       && joinCon.getTheDependency().getKindDep().equals("end-end")){
                       ret = false;
                       break;
                    }
                }

                else if(conn instanceof Sequence){ // Rule 4.2 NAC-04
                    Sequence seq = (Sequence)conn;
                    Activity from = seq.getFromActivity();
                    String state = this.getState(from);
                    if(seq.getTheDependency().getKindDep().equals("end-end")
                        &&(!(state.equals(Plain.FINISHED)
                        || state.equals(ProcessModel.FINISHED)))){
                        ret = false;
                        break;
                    }
                }
            }
		}
		if (ret){
		    Collection acts = procModel.getTheActivity();
            Iterator iter = acts.iterator();
            while (iter.hasNext()) {
                Activity activity = (Activity) iter.next();
                String state = this.getState(activity);
                if (!(state.equals(Plain.FINISHED) || state.equals(ProcessModel.FINISHED)
                    || state.equals(Plain.FAILED) || state.equals(ProcessModel.FAILED)
                    || state.equals(Plain.CANCELED) || state.equals(ProcessModel.CANCELED))) {

                    ret = false;
                    break;
                }
            }
        }
		return ret;
	}

	private Process getTheProcess(ProcessModel pmodel){

	    Process process = null;
		Decomposed decAct = pmodel.getTheDecomposed();

		if(decAct == null)
			process = pmodel.getTheProcess();
		else
		    process = this.getTheProcess(decAct.getTheProcessModel());

	    return process;
	}

	/* Fail - implementation of the methods that involve the
	 * 		  fail propagation.
	 */

	/**
	 * Called by the failActivity (called by user).
	 * Fails a generical activity and propagates the fail.
	 */
	private void failGeneralActivity(Activity activity, boolean propagate,
            String why/*, Session currentSession*/) throws WebapseeException {

		boolean failed = false;

		if (activity instanceof Plain) {
			Plain plain = (Plain)activity;
			failed = this.failPlainActivity (plain, why);
		}
		else if (activity instanceof Decomposed) {
			Decomposed decomp = (Decomposed)activity;
			failed = this.failDecomposedActivity (decomp, why);
		}

		if (failed){
			if(!this.toFailOrFinish(activity.getTheProcessModel())){
				this.activityHasFinished (activity);
			}
		}
		if (! (failed && propagate)){
			return;
		}

		// propagate to successor activities
		this.failPropagation(activity);
	}

	private boolean toFailOrFinish(ProcessModel processModel){

		boolean fail = true;
		boolean finish = false;

		Collection activities = processModel.getTheActivity();
		activities.remove(null);
		if(activities.isEmpty()){
			return finish;
		}

		Iterator iterActs = activities.iterator();
		while (iterActs.hasNext()) {
			Activity activity = (Activity) iterActs.next();
			String state = this.getState(activity);
			if(!(state.equals(Plain.FAILED)
				|| state.equals(ProcessModel.FAILED))){
				return finish;
			}
		}
		return fail;
	}

	private void failPropagation(Activity activity/*, Session currentSession*/) throws WebapseeException{

		Collection succ = this.getConnectionsTo(activity);
		Iterator iter = succ.iterator();
		while (iter.hasNext()) {
			Connection conn = (Connection) iter.next();
			if (conn instanceof Sequence) {
				Sequence seq = (Sequence)conn;
				this.failSimpleSuccessor (seq.getToActivity(), seq);
			}
			else if (conn instanceof MultipleCon) {
				MultipleCon multi = (MultipleCon)conn;
				if (multi instanceof JoinCon) {
					Join join = (JoinCon)multi;
					Activity actJoin = joinCon.getToActivity();
					MultipleCon multJoin = joinCon.getToMultipleCon();
					if(actJoinCon != null)
						this.failJoinSuccessor (actJoin, joinCon/*, currentSession*/);
					else if (multJoinCon != null)
						this.failJoinSuccessor(multJoin, joinCon/*, currentSession*/);
				}
				else if (multi instanceof BranchCon) {
					Branch branch = (BranchCon)multi;
					if(branch instanceof BranchANDCon){
						BranchAND branchAND = (BranchAND)branchCon;
						Collection acts = branchANDCon.getToActivity();
						Collection mults = branchANDCon.getToMultipleCon();
						Iterator iterActs = acts.iterator();
						while (iterActs.hasNext()) {
							Activity act = (Activity) iterActs.next();
							this.failBranchSuccessor (act, branchANDCon/*, currentSession*/);
						}
						Iterator iterMults = mults.iterator();
						while (iterMults.hasNext()) {
							MultipleCon mult = (MultipleCon) iterMults.next();
							this.failBranchSuccessor(mult, branchANDCon);
						}
					}
					else {
						BranchCond branchCond = (BranchCond)branchCon;
						Collection acts = branchCond.getTheBranchConCondToActivity();
						Collection mults = branchCond.getTheBranchConCondToMultipleCon();
						Iterator iterTheActs = acts.iterator();
						while (iterTheActs.hasNext()) {
							BranchCondToActivity act = (BranchConCondToActivity) iterTheActs.next();
							this.failBranchSuccessor (act.getTheActivity(), branchCon);
						}
						Iterator iterTheMults = mults.iterator();
						while (iterTheMults.hasNext()) {
							BranchCondToMultipleCon mult = (BranchConCondToMultipleCon) iterTheMults.next();
							this.failBranchSuccessor(mult.getTheMultipleCon(), branchConCond);
						}
					}
				}
			}
		}
	}

	/**
	 * Fails a Plain Activity.
	 */
	private boolean failPlainActivity (Plain actPlain, String why) throws WebapseeException{

		boolean failed;

		String state = actPlain.getTheEnactionDescription().getState();
		if (state.equals (Plain.CANCELED)){
			failed = false;
		}
		else if((state.equals (Plain.FINISHED)
			|| state.equals (Plain.FAILED))
			&& (why.equals("Rule 9.5") //$NON-NLS-1$
			|| why.equals("Rule 9.7"))){ //$NON-NLS-1$
			failed = false;
		}
		else {
			//TODO actPlain.getTheEnactionDescription().setStateWithMessage(Plain.FAILED);
			this.logging.registerGlobalActivityEvent(actPlain, "ToFailed", why); //$NON-NLS-1$
			if (actPlain instanceof Normal) {
				Normal normal = (Normal)actPlain;
				this.releaseResourcesFromActivity(normal);
				this.changeTasksState(normal, Plain.FAILED);
				Collection agents = this.getInvolvedAgents(normal);
				Iterator iter = agents.iterator();
				while (iter.hasNext()) {
                    Agent agent = (Agent) iter.next();
                    if(agent.getTheTaskAgenda() != null) {
	    				Collection procAgendas = agent.getTheTaskAgenda().getTheProcessAgenda();
	    				Iterator iter2 = procAgendas.iterator();
	    				while (iter2.hasNext()) {
	    					ProcessAgenda agenda = (ProcessAgenda) iter2.next();
	    					Task task = this.getTask(agenda,normal);
	    					if (task != null)
	    						this.logging.registerAgendaEvent(task, "ToFailed", why); //$NON-NLS-1$
	    				}
                    }
                }
			}
			failed = true;
		}
		return (failed);
	}

	/**
	 * Fails a Decomposed Activity.
	 * Applies to the Rule 5.3
	 */
	private boolean failDecomposedActivity (Decomposed actDecomp, String why/*, Session currentSession*/) throws WebapseeException{

		boolean failed;

		String state = actDecomp.getTheReferedProcessModel().getPmState();
		if (state.equals (ProcessModel.CANCELED)) {
			failed = false;
		}
		else if((state.equals (ProcessModel.FINISHED)
			|| state.equals (ProcessModel.FAILED))
			&& (why.equals("Rule 9.8") //$NON-NLS-1$
			|| why.equals("Rule 9.9"))){ //$NON-NLS-1$
			failed = false;
		}
		else {
			//TODO actDecomp.getTheReferedProcessModel().setPmStateWithMessage(ProcessModel.FAILED);
			this.logging.registerProcessModelEvent(actDecomp.getTheReferedProcessModel(), "ToFailed", why); //$NON-NLS-1$
			// fail activities in the model
			Collection activities = actDecomp.getTheReferedProcessModel().getTheActivity();
			Iterator iter = activities.iterator();
			while (iter.hasNext()) {
				Activity act = (Activity) iter.next();
				String actState = this.getState(act);
				if (! (actState.equals (Plain.CANCELED) || actState.equals (ProcessModel.CANCELED)) &&
					! (actState.equals (Plain.FAILED) || actState.equals (ProcessModel.FAILED)))
					this.failGeneralActivity (act, false, "Rule 5.3"); //$NON-NLS-1$
			}
			failed = true;
		}
		return (failed);
	}

	/**
	 * Fail the targets of a Multiple Connection, i.e.,
	 * the successors of the Multiple Connection.
	 */
	private void failMultConnTargets (MultipleCon multiconn/*, Session currentSession*/)
			throws WebapseeException {

		if (multiconn instanceof BranchCon) {
			Branch branch = (BranchCon)multiconn;
			Collection succ = new LinkedList();
			if(branch instanceof BranchANDCon){
				BranchAND bAND = (BranchAND)branchCon;
                if(bAND.getToActivity() != null)
                    succ.addAll(bAND.getToActivity());
                if(bAND.getToMultipleCon() != null)
                    succ.addAll(bAND.getToMultipleCon());
			}
			else{
				BranchCond bCond = (BranchCond)branchCon;
				Collection bctmc = bCond.getTheBranchConCondToMultipleCon();
				Collection atbc = bCond.getTheBranchConCondToActivity();
				Iterator iterMulti = bctmc.iterator(),
						 iterAct = atbc.iterator();
				while (iterMulti.hasNext()) {
					BranchCondToMultipleCon multi = (BranchConCondToMultipleCon) iterMulti.next();
                    if(multi.getTheMultipleCon() != null)
                        succ.add(multi.getTheMultipleCon());
				}
				while (iterAct.hasNext()) {
					BranchCondToActivity act = (BranchConCondToActivity) iterAct.next();
                    if(act.getTheActivity() != null)
                        succ.add(act.getTheActivity());
				}
			}
			Iterator iter = succ.iterator();
			while (iter.hasNext()) {
				Object obj = (Object) iter.next();
				this.failBranch (branchCon, obj/*, currentSession*/);
			}
		}
		else if (multiconn instanceof JoinCon) {
			Join join = (JoinCon)multiconn;
			this.failJoin (joinCon);
		}
	}

	/**
	 * Fails a BranchCon connection.
	 * Applies to the Rules 5.13, 5.14, 5.15,
	 * 5.16, 5.17, 5.27, 5.28,
	 */
	private void failBranch (Branch branchCon, Object obj/*, Session currentSession*/)
		throws WebapseeException {

		if (obj instanceof Activity) {
			Activity act = (Activity)obj;
			if (act instanceof Plain){
				String dep = branchCon.getTheDependency().getKindDep();
				if(dep.equals("end-start")||dep.equals("end-end")) //$NON-NLS-1$ //$NON-NLS-2$
					this.failGeneralActivity (act, true, "Rule 5.13"); //$NON-NLS-1$
			}
			else{
				String dep = branchCon.getTheDependency().getKindDep();
				if(dep.equals("end-start")||dep.equals("end-end")) //$NON-NLS-1$ //$NON-NLS-2$
					if(branch instanceof BranchANDCon){
						this.failGeneralActivity (act, true, "Rule 5.14"); //$NON-NLS-1$
					}
					else
						this.failGeneralActivity (act, true, "Rule 5.15"); //$NON-NLS-1$
			}
		}
		else if (obj instanceof MultipleCon) {
			//Rules 5.27, 5.28
			MultipleCon multi = (MultipleCon)obj;
			this.failMultConnTargets (multi);
		}
	}

	/**
	 * Fails a JoinCon connection.
	 * Applies to the Rules 5.18, 5.19,
	 * 5.20, 5.21, 5.22, 5.23, 5.24,
	 * 5.25, 5.26
	 */
	private void failJoin (Join joinCon/*, Session currentSession*/) throws WebapseeException {

		Activity act = joinCon.getToActivity();
		MultipleCon multi = joinCon.getToMultipleCon();
		String joinType = join.getKindJoinCon();
		if (act != null) {
			if (act instanceof Plain){
				if(joinConType.equals("AND")) //$NON-NLS-1$
					this.failGeneralActivity (act, true, "Rule 5.18"); //$NON-NLS-1$
				else if (joinConType.equals("OR")) //$NON-NLS-1$
					this.failGeneralActivity (act, true, "Rule 5.20"); //$NON-NLS-1$
				else if (joinConType.equals("XOR")) //$NON-NLS-1$
					this.failGeneralActivity (act, true, "Rule 5.21"); //$NON-NLS-1$
			}
			else{
				if(joinConType.equals("AND")) //$NON-NLS-1$
					this.failGeneralActivity (act, true, "Rule 5.19"); //$NON-NLS-1$
				else if (joinConType.equals("OR")) //$NON-NLS-1$
					this.failGeneralActivity (act, true, "Rule 5.22"); //$NON-NLS-1$
				else if (joinConType.equals("XOR")) //$NON-NLS-1$
					this.failGeneralActivity (act, true, "Rule 5.23"); //$NON-NLS-1$
			}

		}
		else if (multi!= null) {
			//Rules 5.24, 5.25, 5.26
			this.failMultConnTargets (multi);
		}
	}

	/**
	 * Fails the successor of a Simple Connection.
	 * Applies to the Rules 5.1, 5.2.
	 */
	private void failSimpleSuccessor
		(Activity act, Sequence sequence/*, Session currentSession*/)
			throws WebapseeException {

		String depType = sequence.getTheDependency().getKindDep();
		String state = this.getState(act);
		if (depType.equals ("end-start") || depType.equals ("end-end")) { //$NON-NLS-1$ //$NON-NLS-2$
			if (! (state.equals (Plain.CANCELED) || state.equals (ProcessModel.CANCELED)))
				if (act instanceof Plain)
					this.failGeneralActivity (act, true, "Rule 5.1"); //$NON-NLS-1$
				else
					this.failGeneralActivity (act, true, "Rule 5.2"); //$NON-NLS-1$
		}
	}

	/**
	 * Fails the successor of a JoinCon Connection, when the
	 * predecessor of the JoinCon is an Activity.
	 * Applies to the Rules 5.4, 5.5, 5.7, 5.8,
	 * 5.9, 5.10
	 */
	private void failJoinConSuccessor
		(Activity act, Join joinCon/*, Session currentSession*/)
			throws WebapseeException {

		String state = this.getState(act);
		String joinType = join.getKindJoinCon();
		String depType = joinCon.getTheDependency().getKindDep();

		// test additional conditions for OR and XOR cases
		boolean canFail = true;

		if (joinType.equals ("OR") || joinConType.equals ("XOR")) { //$NON-NLS-1$ //$NON-NLS-2$
			Collection predec = this.getPredecessors(joinCon);
			Iterator iter = predec.iterator();
			while (iter.hasNext()) {
				Object obj = (Object) iter.next();
				if(obj instanceof Activity){
					Activity activity = (Activity)obj;
					String actState = this.getState(activity);
					if ((! actState.equals (Plain.FAILED) && ! actState.equals (Plain.CANCELED))
						|| (! actState.equals(ProcessModel.FAILED) && ! actState.equals(ProcessModel.CANCELED))){
						canFail = false;
						break;
					}
				}
				else if(obj instanceof MultipleCon){
					MultipleCon multi = (MultipleCon)obj;
					// MISSING: waiting for specification
					if (true) {
						canFail = false;
						break;
					}
				}
			}
		}
		if (canFail) {
			if (depType.equals ("end-start") || depType.equals ("end-end")) //$NON-NLS-1$ //$NON-NLS-2$
				if (! (state.equals (Plain.CANCELED)|| state.equals (ProcessModel.CANCELED)))
					if (act instanceof Plain){
						if(joinConType.equals("AND")) //$NON-NLS-1$
							this.failGeneralActivity (act, true, "Rule 5.4"); //$NON-NLS-1$
						else if (joinConType.equals("OR")) //$NON-NLS-1$
							this.failGeneralActivity (act, true, "Rule 5.7"); //$NON-NLS-1$
						else if (joinConType.equals("XOR")) //$NON-NLS-1$
							this.failGeneralActivity (act, true, "Rule 5.8"); //$NON-NLS-1$
					}
					else{
						if(joinConType.equals("AND")) //$NON-NLS-1$
							this.failGeneralActivity (act, true, "Rule 5.5"); //$NON-NLS-1$
						else if (joinConType.equals("OR")) //$NON-NLS-1$
							this.failGeneralActivity (act, true, "Rule 5.9"); //$NON-NLS-1$
						else if (joinConType.equals("XOR")) //$NON-NLS-1$
							this.failGeneralActivity (act, true, "Rule 5.10"); //$NON-NLS-1$
					}
		}
	}

	/**
	 * Fails the successor of a JoinCon Connection, when the
	 * predecessor of the JoinCon is an Multiple Connection.
	 * Applies to the Rule 5.6, 5.11, 5.12,
	 */
	private void failJoinConSuccessor
	(MultipleCon mult, Join joinCon/*, Session currentSession*/)
		throws WebapseeException {

		//String state = this.getState(act);
		String joinType = join.getKindJoinCon();
		String depType = joinCon.getTheDependency().getKindDep();

		// 	Test additional conditions for OR and XOR cases
		boolean canFail = true;

		if (joinType.equals ("OR") || joinConType.equals ("XOR")) { //$NON-NLS-1$ //$NON-NLS-2$
			Collection predec = this.getPredecessors(joinCon);
			Iterator iter = predec.iterator();
			while (iter.hasNext()) {
				Object obj = (Object) iter.next();
				if(obj instanceof Activity){
					Activity activity = (Activity)obj;
					String actState = this.getState(activity);
					if ((! actState.equals (Plain.FAILED) && ! actState.equals (Plain.CANCELED))
						|| (! actState.equals(ProcessModel.FAILED) && ! actState.equals(ProcessModel.CANCELED))){
						canFail = false;
						break;
					}
				}
				else if(obj instanceof MultipleCon){
					MultipleCon multi = (MultipleCon)obj;
					// MISSING: waiting for specification
					if (true) {
						canFail = false;
						break;
					}
				}
			}
		}
		if (canFail) {
			if (depType.equals ("end-start") || depType.equals ("end-end")) //$NON-NLS-1$ //$NON-NLS-2$
				this.failJoin (joinCon);
		}
	}

	/**
	 * Fails the successor of a BranchCon Connection, when the
	 * predecessor of the BranchCon is an Activity.
	 * Applies to the Rules 5.13, 5.14, 5.15,
	 */
	private void failBranchConSuccessor
		(Activity act, Branch branchCon/*, Session currentSession*/)
			throws WebapseeException {

		String state = this.getState(act);
		String depType = branchCon.getTheDependency().getKindDep();
		boolean canFail = true;
		Collection predec = this.getPredecessors(branchCon);
		Iterator iter = predec.iterator();
		while (iter.hasNext()) {
			Object obj = (Object) iter.next();
			if(obj instanceof Activity){
				Activity activity = (Activity)obj;
				String actState = this.getState(activity);
				if ((! actState.equals (Plain.FAILED) && ! actState.equals (Plain.CANCELED))
					|| (! actState.equals(ProcessModel.FAILED) && ! actState.equals(ProcessModel.CANCELED))){
					canFail = false;
					break;
				}
			}
			else if(obj instanceof MultipleCon){
				MultipleCon multi = (MultipleCon)obj;
				// MISSING: waiting for specification
				if (true) {
					canFail = false;
					break;
				}
			}
		}
		if (canFail)
			if (depType.equals ("end-start") || depType.equals ("end-end")) //$NON-NLS-1$ //$NON-NLS-2$
				if (! (state.equals (Plain.CANCELED) && state.equals (ProcessModel.CANCELED)))
					if (act instanceof Plain){
						this.failGeneralActivity (act, true, "Rule 5.13"); //$NON-NLS-1$
					}
					else{
						if(branch instanceof BranchANDCon){
							this.failGeneralActivity (act, true, "Rule 5.14"); //$NON-NLS-1$
						}
						else
							this.failGeneralActivity (act, true, "Rule 5.15"); //$NON-NLS-1$
					}
	}

	/**
	 * Fails the successor of a BranchCon Connection, when the
	 * predecessor of the BranchCon is an Multiple Connection.
	 */
	private void failBranchConSuccessor
	(MultipleCon mult, Branch branchCon/*, Session currentSession*/)
		throws WebapseeException {

		String depType = branchCon.getTheDependency().getKindDep();
		boolean canFail = true;
		Collection predec = this.getPredecessors(branchCon);
		Iterator iter = predec.iterator();
		while (iter.hasNext()) {
			Object obj = (Object) iter.next();
			if(obj instanceof Activity){
				Activity activity = (Activity)obj;
				String actState = this.getState(activity);
				if ((! actState.equals (Plain.FAILED) && ! actState.equals (Plain.CANCELED))
				    || (! actState.equals(ProcessModel.FAILED) && ! actState.equals(ProcessModel.CANCELED))){
					canFail = false;
					break;
				}
			}
			else if(obj instanceof MultipleCon){
				MultipleCon multi = (MultipleCon)obj;
				// MISSING: waiting for specification
				if (true) {
					canFail = false;
					break;
				}
			}
		}
		if (canFail)
			if (depType.equals ("end-start") || depType.equals ("end-end")) //$NON-NLS-1$ //$NON-NLS-2$
					this.failBranch (branchCon, mult);
	}

	/*Cancel - implementation of the methods that involve the
	 * 		    cancel propagation.
	 */

	/**
	 * Called by the cancelActivity (called by user).
	 * Cancells a generical activity and propagates it.
	 */
	private void cancelGeneralActivity(Activity activity, boolean propagate,
        String why/*, Session currentSession*/) throws WebapseeException {

		boolean canceled = false;

		if (activity instanceof Plain) {
			Plain plain = (Plain)activity;
			canceled = this.cancelPlainActivity (plain, why);
		}
		else if (activity instanceof Decomposed) {
			Decomposed decomp = (Decomposed)activity;
			canceled = this.cancelDecomposedActivity (decomp, why);
		}

		if (canceled)
			this.activityHasFinished (activity);
		if (! (canceled && propagate)){
			return;
		}

		// propagate to successor activities
		this.cancelPropagation(activity);
	}

	private void cancelPropagation (Activity activity/*, Session currentSession*/) throws WebapseeException{

		Collection succ = this.getConnectionsTo(activity);
		Iterator iter = succ.iterator();
		while (iter.hasNext()) {
			Connection conn = (Connection) iter.next();
			if (conn instanceof Sequence) {
				Sequence seq = (Sequence)conn;
				this.cancelSimpleSuccessor (seq.getToActivity(), seq);
			}
			else if (conn instanceof MultipleCon) {
				MultipleCon multi = (MultipleCon)conn;
				if (multi instanceof JoinCon) {
					Join join = (JoinCon)multi;
					Activity actJoin = joinCon.getToActivity();
					MultipleCon multJoin = joinCon.getToMultipleCon();
					if(actJoinCon != null)
						this.cancelJoinSuccessor (actJoin, joinCon);
					else if (multJoinCon != null)
						this.cancelJoinSuccessor(multJoin, joinCon);
				}
				else if (multi instanceof BranchCon) {
					Branch branch = (BranchCon)multi;
					if(branch instanceof BranchANDCon){
						BranchAND branchAND = (BranchAND)branchCon;
						Collection acts = branchANDCon.getToActivity();
						Collection mults = branchANDCon.getToMultipleCon();
						Iterator iterActs = acts.iterator();
						while (iterActs.hasNext()) {
							Activity act = (Activity) iterActs.next();
							this.cancelBranchSuccessor (act, branchCon);
						}
						Iterator iterMults = mults.iterator();
						while (iterMults.hasNext()) {
							MultipleCon mult = (MultipleCon) iterMults.next();
							this.cancelBranchSuccessor(mult, branchANDCon);
						}
					}
					else {
						BranchCond branchCond = (BranchCond)branchCon;
						Collection acts = branchCond.getTheBranchConCondToActivity();
						Collection mults = branchCond.getTheBranchConCondToMultipleCon();
						Iterator iterTheActs = acts.iterator();
						while (iterTheActs.hasNext()) {
							BranchCondToActivity act = (BranchConCondToActivity) iterTheActs.next();
							this.cancelBranchSuccessor (act.getTheActivity(), branchCon);
						}
						Iterator iterTheMults = mults.iterator();
						while (iterTheMults.hasNext()) {
							BranchCondToMultipleCon mult = (BranchConCondToMultipleCon) iterTheMults.next();
							this.cancelBranchSuccessor(mult.getTheMultipleCon(), branchConCond);
						}
					}
				}
			}
		}
	}

	/**
	 * Cancells a Plain Activity.
	 */
	private boolean cancelPlainActivity (Plain actPlain, String why) throws WebapseeException{

		boolean canceled;

		String state = actPlain.getTheEnactionDescription().getState();
		if (state.equals (Plain.FAILED)
			|| state.equals (Plain.CANCELED)) {
			canceled = false;
		}
		else {
			//TODO actPlain.getTheEnactionDescription().setStateWithMessage(Plain.CANCELED);
			this.logging.registerGlobalActivityEvent(actPlain, "ToCanceled", why); //$NON-NLS-1$
			if (actPlain instanceof Normal) {
				Normal normal = (Normal)actPlain;
				this.releaseResourcesFromActivity(normal);
				this.changeTasksState(normal, Plain.CANCELED);
				Collection agents = this.getInvolvedAgents(normal);
				Iterator iter = agents.iterator();
				while (iter.hasNext()) {
                    Agent agent = (Agent) iter.next();
    				Collection procAgendas = agent.getTheTaskAgenda().getTheProcessAgenda();
    				Iterator iter2 = procAgendas.iterator();
    				while (iter2.hasNext()) {
    					ProcessAgenda agenda = (ProcessAgenda) iter2.next();
    					Task task = this.getTask(agenda,normal);
    					if (task != null)
    						this.logging.registerAgendaEvent(task, "ToCanceled", why); //$NON-NLS-1$
    				}
                }
			}
			canceled = true;
		}

		return (canceled);
	}

	/**
	 * Cancells a Decomposed Activity.
	 * Applies to the Rule 6.19.
	 */
	private boolean cancelDecomposedActivity(Decomposed actDecomp, String why/*, Session currentSession*/)
        throws WebapseeException {

		boolean canceled;

		String state = actDecomp.getTheReferedProcessModel().getPmState();
		if (state.equals (ProcessModel.FAILED)
			|| state.equals (ProcessModel.CANCELED)) {
			canceled = false;
		}
		else {
			//TODO actDecomp.getTheReferedProcessModel().setPmStateWithMessage(ProcessModel.CANCELED);
			this.logging.registerProcessModelEvent(actDecomp.getTheReferedProcessModel(), "ToCanceled", why); //$NON-NLS-1$

			// cancel activities in the model
			Collection activities = actDecomp.getTheReferedProcessModel().getTheActivity();
			Iterator iter = activities.iterator();
			while (iter.hasNext()) {
				Activity act = (Activity) iter.next();
				String actState = this.getState(act);
				if (! (actState.equals (Plain.CANCELED) || actState.equals (ProcessModel.CANCELED)) &&
					! (actState.equals (Plain.FAILED) || actState.equals (ProcessModel.FAILED)))
					this.cancelGeneralActivity (act, false, "Rule 6.19"); //$NON-NLS-1$
			}
			canceled = true;
		}
		return (canceled);
	}

	/**
	 * Cancells the targets of a Multiple Connection, i.e.,
	 * the successors of the Multiple Connection.
	 */
	private void cancelMultConnTargets (MultipleCon multiconn/*, Session currentSession*/)
			throws WebapseeException {

		if (multiconn instanceof BranchCon) {
			Branch branch = (BranchCon)multiconn;
			Collection succ = new LinkedList();
			if(branch instanceof BranchANDCon){
				BranchAND bAND = (BranchAND)branchCon;
                if(bAND.getToActivity() != null)
                    succ.addAll(bAND.getToActivity());
                if(bAND.getToMultipleCon() != null)
                    succ.addAll(bAND.getToMultipleCon());
			}
			else{
				BranchCond bCond = (BranchCond)branchCon;
				Collection bctmc = bCond.getTheBranchConCondToMultipleCon();
				Collection atbc = bCond.getTheBranchConCondToActivity();
				Iterator iterMulti = bctmc.iterator(),
						 iterAct = atbc.iterator();
				while (iterMulti.hasNext()) {
					BranchCondToMultipleCon multi = (BranchConCondToMultipleCon) iterMulti.next();
					if(multi.getTheMultipleCon() != null)
					    succ.add(multi.getTheMultipleCon());
				}
				while (iterAct.hasNext()) {
					BranchCondToActivity act = (BranchConCondToActivity) iterAct.next();
					if(act.getTheActivity() != null)
					    succ.add(act.getTheActivity());
				}
			}
			Iterator iter = succ.iterator();
			while (iter.hasNext()) {
				Object obj = (Object) iter.next();
				this.cancelBranch (branchCon, obj);
			}
		}
		else if (multiconn instanceof JoinCon) {
			Join join = (JoinCon)multiconn;
			this.cancelJoin (joinCon);
		}
	}

	/**
	 * Cancells a BranchCon connection.
	 * Applies to the Rules 6.13, 6.14,
	 * 6.15, 6.16, 6.17, 6.18, 6.29, 6.30
	 */
	private void cancelBranch (Branch branchCon, Object obj/*, Session currentSession*/)
		throws WebapseeException {

		if (obj instanceof Activity) {
			Activity act = (Activity)obj;
			if (act instanceof Plain){
				if(branch instanceof BranchANDCon)
					this.cancelGeneralActivity (act, true, "Rule 6.13"); //$NON-NLS-1$
				else
					this.cancelGeneralActivity (act, true, "Rule 6.14"); //$NON-NLS-1$
			}
			else{
				if(branch instanceof BranchANDCon)
					this.cancelGeneralActivity (act, true, "Rule 6.15"); //$NON-NLS-1$
				else
					this.cancelGeneralActivity (act, true, "Rule 6.16"); //$NON-NLS-1$
			}
		}
		else if (obj instanceof MultipleCon) {
			MultipleCon multi = (MultipleCon)obj;
			//Rule 6.17, 6.18, 6.29, 6.30
			this.cancelMultConnTargets (multi);
		}
	}

	/**
	 * Cancells a JoinCon connection.
	 * Applies to the Rules 6.7, 6.12
	 * 6.20, 6.21, 6.22, 6.23, 6.24,
	 * 6.25, 6.26, 6.27, 6.28.
	 */
	private void cancelJoin (Join joinCon/*, Session currentSession*/) throws WebapseeException {

		String joinType = join.getKindJoinCon();
		Activity act = joinCon.getToActivity();
		MultipleCon multi = joinCon.getToMultipleCon();
		if (act != null) {
			if (act instanceof Plain){
				if(joinConType.equals("AND")) //$NON-NLS-1$
					this.cancelGeneralActivity (act, true, "Rule 6.20"); //$NON-NLS-1$
				else if (joinConType.equals("OR")) //$NON-NLS-1$
					this.cancelGeneralActivity (act, true, "Rule 6.22"); //$NON-NLS-1$
				else if (joinConType.equals("XOR")) //$NON-NLS-1$
					this.cancelGeneralActivity (act, true, "Rule 6.23"); //$NON-NLS-1$
			}
			else{
				if(joinConType.equals("AND")) //$NON-NLS-1$
					this.cancelGeneralActivity (act, true, "Rule 6.21"); //$NON-NLS-1$
				else if (joinConType.equals("OR")) //$NON-NLS-1$
					this.cancelGeneralActivity (act, true, "Rule 6.24"); //$NON-NLS-1$
				else if (joinConType.equals("XOR")) //$NON-NLS-1$
					this.cancelGeneralActivity (act, true, "Rule 6.25"); //$NON-NLS-1$
			}
		}
		else if (multi!= null) {
			//Rule 6.7, 6.12, 6.26, 6.27, 6.28.
			this.cancelMultConnTargets (multi);
		}
	}

    /**
     * called by APSEE Manager.
     * Applies to the rules 6.1, 6.2
     * 						6.3, 6.4
	 * Cancells the successor of a Simple Connection.
	 */
	private void cancelSimpleSuccessor
		(Activity act, Sequence sequence/*, Session currentSession*/)
			throws WebapseeException {

		String depType = sequence.getTheDependency().getKindDep();
		String state = this.getState(act);
		if (depType.equals ("end-start") || depType.equals ("end-end")) { //$NON-NLS-1$ //$NON-NLS-2$
			if (! (state.equals (Plain.FAILED) || state.equals (ProcessModel.FAILED)))
				if(act instanceof Plain)
					this.cancelGeneralActivity (act, true, "Rule 6.1"); //$NON-NLS-1$
				else
					this.cancelGeneralActivity (act, true, "Rule 6.2"); //$NON-NLS-1$
		}
		else {
			if (! (state.equals (Plain.FAILED) || state.equals (ProcessModel.FAILED)) &&
				! (state.equals (Plain.ACTIVE) || state.equals (ProcessModel.ENACTING)) &&
				! (state.equals (Plain.FINISHED) || state.equals (ProcessModel.FINISHED)) &&
				! state.equals (Plain.PAUSED))
				if(act instanceof Plain)
					this.cancelGeneralActivity (act, true, "Rule 6.3"); //$NON-NLS-1$
				else
					this.cancelGeneralActivity (act, true, "Rule 6.4"); //$NON-NLS-1$
		}
	}

	/**
	 * Cancells the successor of a JoinCon Connection, when the
	 * predecessor of the JoinCon is an Activity.
	 * Applies to the Rules 6.5, 6.20, 6.8, 6.22
	 * 6.9, 6.23, 6.6, 6.21, 6.10, 6.24, 6.11, 6.25
	 */
	private void cancelJoinConSuccessor
		(Activity act, Join joinCon/*, Session currentSession*/)
			throws WebapseeException {

		//String why = "";
		String state = this.getState(act);
		String joinType = join.getKindJoinCon();
		//String depType = joinCon.getTheDependency().getKindDep();

		// Test additional conditions for OR and XOR cases
		boolean canCancel = true;

		if (joinType.equals ("OR") || joinConType.equals ("XOR")) { //$NON-NLS-1$ //$NON-NLS-2$
			Collection predec = this.getPredecessors(joinCon);
			Iterator iter = predec.iterator();
			while (iter.hasNext()) {
				Object obj = (Object) iter.next();
				if(obj instanceof Activity){
					Activity activity = (Activity)obj;
					String actState = this.getState(activity);
					if ((! actState.equals (Plain.FAILED) && ! actState.equals (Plain.CANCELED))
						|| (! actState.equals(ProcessModel.FAILED) && ! actState.equals(ProcessModel.CANCELED))){
						canCancel = false;
						break;
					}
				}
				else if(obj instanceof MultipleCon){
					MultipleCon multi = (MultipleCon)obj;
					// MISSING: waiting for specification
					if (true) {
						canCancel = false;
						break;
					}
				}
			}
		}
		if (canCancel) {
			if (! (state.equals (Plain.FAILED)||state.equals (ProcessModel.FAILED)))
				if (act instanceof Plain){
					if(joinConType.equals("AND")) //$NON-NLS-1$
						this.cancelGeneralActivity (act, true, "Rule 6.5"); //$NON-NLS-1$
					else if (joinConType.equals("OR")) //$NON-NLS-1$
						this.cancelGeneralActivity (act, true, "Rule 6.8"); //$NON-NLS-1$
					else if (joinConType.equals("XOR")) //$NON-NLS-1$
						this.cancelGeneralActivity (act, true, "Rule 6.9"); //$NON-NLS-1$
				}
				else{
					if(joinConType.equals("AND")) //$NON-NLS-1$
						this.cancelGeneralActivity (act, true, "Rule 6.6"); //$NON-NLS-1$
					else if (joinConType.equals("OR")) //$NON-NLS-1$
						this.cancelGeneralActivity (act, true, "Rule 6.10"); //$NON-NLS-1$
					else if (joinConType.equals("XOR")) //$NON-NLS-1$
						this.cancelGeneralActivity (act, true, "Rule 6.11"); //$NON-NLS-1$
				}
		}
	}

	/**
	 * Cancells the successor of a BranchCon Connection, when the
	 * predecessor of the BranchCon is an Activity.
	 * Applies to the Rules 6.13, 6.14, 6.15, 6.16
	 * 7.13, 7.14, 7.15
	 */
	private void cancelBranchConSuccessor
		(Activity act, Branch branchCon/*, Session currentSession*/)
			throws WebapseeException {

		String state = this.getState(act);
		if (! (state.equals (Plain.FAILED) && state.equals (ProcessModel.FAILED))){
			if (act instanceof Plain){
				if(branch instanceof BranchANDCon){
					this.cancelGeneralActivity (act, true, "Rule 6.13"); //$NON-NLS-1$
				}
				else{
					BranchCond bCond = (BranchCond)branchCon;
					String depType = bCond.getTheDependency().getKindDep();
					if (!this.isConditionSatisfied(bCond, act)){
						if(depType.equals("start-start")||depType.equals("end-start")){ //$NON-NLS-1$ //$NON-NLS-2$
							if(this.getState(act).equals(Plain.WAITING)){
								this.cancelGeneralActivity (act, true, "Rule 7.13"); //$NON-NLS-1$
							}
						}
						else if(depType.equals("end-end")){ //$NON-NLS-1$
							if (this.getState(act).equals(Plain.WAITING)
								|| this.getState(act).equals(Plain.READY)){
								this.cancelGeneralActivity (act, true, "Rule 7.14"); //$NON-NLS-1$
							}
							else if (!this.getState(act).equals(Plain.WAITING)
								&& !this.getState(act).equals(Plain.READY)){
								this.cancelGeneralActivity (act, true, "Rule 7.15"); //$NON-NLS-1$
							}
						}
						else{
							this.cancelGeneralActivity (act, true, "Rule 6.14"); //$NON-NLS-1$
						}
					}
				}
			}
			else if(branch instanceof BranchANDCon){
					this.cancelGeneralActivity (act, true, "Rule 6.15"); //$NON-NLS-1$
			}
			else{
				this.cancelGeneralActivity (act, true, "Rule 6.16"); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Cancells the successor of a BranchCon Connection, when the
	 * predecessor of the BranchCon is an Multiple Connection.
	 */
	private void cancelBranchConSuccessor
	(MultipleCon mult, Branch branchCon/*, Session currentSession*/)
		throws WebapseeException {

		//String depType = branchCon.getTheDependency().getKindDep();
		boolean canCancel = true;
		Collection predec = this.getPredecessors(branchCon);
		Iterator iter = predec.iterator();
		while (iter.hasNext()) {
			Object obj = (Object) iter.next();
			if(obj instanceof Activity){
				Activity activity = (Activity)obj;
				String actState = this.getState(activity);
				if ((! actState.equals (Plain.FAILED) && ! actState.equals (Plain.CANCELED))
				    || (! actState.equals(ProcessModel.FAILED) && ! actState.equals(ProcessModel.CANCELED))){
					canCancel = false;
					break;
				}
			}
			else if(obj instanceof MultipleCon){
				MultipleCon multi = (MultipleCon)obj;
				// MISSING: waiting for specification
				if (true) {
					canCancel = false;
					break;
				}
			}
		}
		if (canCancel)
			this.cancelBranch (branchCon, mult);
	}

	/**
	 * Cancells the successor of a JoinCon Connection, when the
	 * predecessor of the JoinCon is an Multiple Connection.
	 */
	private void cancelJoinConSuccessor
	(MultipleCon mult, Join joinCon/*, Session currentSession*/)
		throws WebapseeException {

		String joinType = join.getKindJoinCon();
		String depType = joinCon.getTheDependency().getKindDep();

		// 	test additional conditions for OR and XOR cases
		boolean canCancel = true;

		if (joinType.equals ("OR") || joinConType.equals ("XOR")) { //$NON-NLS-1$ //$NON-NLS-2$
			Collection predec = this.getPredecessors(joinCon);
			Iterator iter = predec.iterator();
			while (iter.hasNext()) {
				Object obj = (Object) iter.next();
				if(obj instanceof Activity){
					Activity activity = (Activity)obj;
					String actState = this.getState(activity);
					if ((! actState.equals (Plain.FAILED) && ! actState.equals (Plain.CANCELED))
						|| (! actState.equals(ProcessModel.FAILED) && ! actState.equals(ProcessModel.CANCELED))){
						canCancel = false;
						break;
					}
				}
				else if(obj instanceof MultipleCon){
					MultipleCon multi = (MultipleCon)obj;
					// MISSING: waiting for specification
					if (true) {
						canCancel = false;
						break;
					}
				}
			}
		}
		if (canCancel) {
			this.cancelJoin (joinCon);
		}
	}

	/*Condition - implementation of the methods that involve the
	 * 		      analisys of the conditions.
	 */

	/**
	 * Evaluates the condition of a Conditional BranchCon,
	 * Feedback connections...
	 */
	private boolean conditionValue(PolCondition cond){

		/*String condition = cond.getCond();
		// MISSING: waiting for conditions implementation
		return (condition.equals ("true")); //$NON-NLS-1$
*/
		try {
			//TODO PolObjValueEval evalCond = ConditionEval.evalCondition("",cond);
			//TODO if (evalCond == null)
			//TODO 	throw new Exception(
			//TODO 			"It was not possible to evaluate the condition.");
			//TODO else {
			//TODO 	return ((Boolean) evalCond.getCurrentObj()).booleanValue();
			//TODO }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/*private boolean conditionValue(String cond){

		// MISSING: waiting for conditions implementation
		return (cond.equals ("true")); //$NON-NLS-1$

	}*/

	/*Feedback - implementation of the methods that involve the
	 * 		     Feedback propagation.
	 */

	/**
	 * Verifies if the Activity is a Feedback connection source.
	 */
	private boolean isFeedbackSource (Activity act) throws WebapseeException {
		Collection set = this.getFeedbackSet (act);
		set.remove(null);
		return (! set.isEmpty());
	}

	/**
	 * Searches for feedback connections having a given activity as a source.
	 */
	private Collection getFeedbackSet (Activity act) throws WebapseeException {

		HashSet set = new HashSet ();
		ProcessModel processModel = act.getTheProcessModel();
		Collection connections = processModel.getTheConnection();
		Iterator iter = connections.iterator();
		while (iter.hasNext()) {
			Connection conn = (Connection) iter.next();
			if (conn instanceof SimpleCon) {
				if (conn instanceof Feedback) {
					Feedback feedback = (Feedback)conn;
					Activity source = feedback.getFromActivity();
					if (source != null && source.equals (act)){
						set.add (feedback);
					}
				}
			}
		}
		return (set);
	}

    /**
     * called by APSEE Manager.
     * Applies to the rules 9.1, 9.2, 9.3, 9.4
     * Executes all the Feedback connections from the Activity.
     */
	private boolean executeFeedbacks (Activity act/*, Session currentSession*/)
		throws WebapseeException {
		boolean executed = false;

		Collection set = this.getFeedbackSet (act);
		Iterator conns = set.iterator();
		while (conns.hasNext()) {
			Feedback nextConn = (Feedback)conns.next();

			if (this.conditionValue(nextConn.getTheCondition())) {
				executed = true;
				this.executeFeedback (nextConn);
			}
		}
		return (executed);
	}

	/**
	 * Begins the execution of a Feedback Connection.
	 */
	private void executeFeedback (SimpleCon simpleCon/*, Session currentSession*/)
		throws WebapseeException {

		Activity _target = simpleCon.getToActivity();
		this.redoActivityAndPropagate (_target);

	}

    /**
     * called by APSEE Manager.
     * Applies to the rules 9.21, 9.22, 9.23, 9.24,
     * 						9.25, 9.26, 9.27, 9.28,
     * 						9.29, 9.30, 9.31, 9.32,
     * 						9.33, 9.34, 9.35, 9.36,
     * 						9.37
     *
     * Redoes an Activity and propagates the "redo" to the
     * following Activities.
     */

	private void redoActivityAndPropagate (Activity act/*, Session currentSession*/)
		throws WebapseeException {

		boolean redone = this.redoActivity (act);

		if(!redone)
			return;
		// 	propagate

		Collection conns = this.getConnectionsTo(act);
		Iterator iter = conns.iterator();
		Collection succ = new LinkedList();
		while (iter.hasNext()) {
			Connection conn = (Connection) iter.next();
			if (conn instanceof Sequence){
				Sequence seq = (Sequence)conn;
                if(seq.getToActivity() != null)
                    succ.add(seq.getToActivity());
			}
			else if (conn instanceof MultipleCon){
				MultipleCon multi = (MultipleCon)conn;
				multi.setFired(new Boolean(false));
				if(multi instanceof JoinCon){
					Join join = (JoinCon)multi;
					if(joinCon.getToActivity() != null)
					    succ.add(joinCon.getToActivity());
				}
				else if(multi instanceof BranchANDCon){
					BranchAND branchAnd = (BranchANDCon)multi;
					if(branchAndCon.getToActivity() != null)
					    succ.addAll(branchAndCon.getToActivity());
				}
				else if (multi instanceof BranchConCond){
					BranchCond branchCond = (BranchConCond)multi;
					Collection acts = branchCond.getTheBranchConCondToActivity();
					Iterator iter2 = acts.iterator();
					while (iter2.hasNext()) {
						BranchCondToActivity actToBC = (BranchConCondToActivity) iter2.next();
						if(actToBC.getTheActivity() != null)
						    succ.add(actToBC.getTheActivity());
					}
				}
			}
		}
		Iterator iter2 = succ.iterator();
		while (iter2.hasNext()) {
			Activity activity = (Activity) iter2.next();
			String state;
			if (activity instanceof Plain){
				Plain plain = (Plain) activity;
				state = plain.getTheEnactionDescription().getState();
				activity = plain;
			}
			else{
				Decomposed dec = (Decomposed) activity;
				state = dec.getTheReferedProcessModel().getPmState();
				activity = dec;
			}
			if (! state.equals (Plain.WAITING)) {

				this.redoActivityAndPropagate (activity);
				this.resetConnection (activity);
			}
		}
	}

    /**
     * called by APSEE Manager.
     * Applies to the rules 9.17, 9.19, 9.20
     * 						9.38, 9.39
     * Chooses what "kind of redo" (redoNormalActivity, redoAutomaticActivity,
     * redoDecomposedActivity) will be called.
     */
	private boolean redoActivity (Activity act/*, Session currentSession*/)
		throws WebapseeException {

		boolean redone = false;
		if (act instanceof Normal){
			Normal normal = (Normal)act;
			redone = this.redoNormalActivity (normal);
		}
		else if (act instanceof Automatic){
			Automatic automatic = (Automatic)act;
			redone = this.redoAutomaticActivity (automatic);
		}
		else if (act instanceof Decomposed){
			Decomposed decomposed = (Decomposed)act;
			redone = this.redoDecomposedActivity (decomposed);
		}
		return redone;
	}

    /**
     * called by APSEE Manager.
     * Applies to the rules 9.11, 9.12, 9.13
     * Resets the Normal Activity that was enacting,
     * creating a new version of it.
     */
	private boolean redoNormalActivity (Normal actNorm)
		throws WebapseeException {

		boolean redone = false;
		String state = actNorm.getTheEnactionDescription().getState();

		if (!state.equals (Plain.WAITING)
			&& !state.equals("")) { //$NON-NLS-1$
			redone = true;
			boolean rule912 = false;
			boolean rule913 = false;
			if (state.equals (Plain.ACTIVE)) {
				rule912 = true;
				this.releaseResourcesFromActivity(actNorm);
			}
			else if (state.equals (Plain.FINISHED)){
				rule913 = true;
			}

			this.versioning(actNorm);

			if(rule912){
				this.logging.registerGlobalActivityEvent (actNorm, "Redone", "Rule 9.12"); //$NON-NLS-1$ //$NON-NLS-2$
				//TODO actNorm.getTheEnactionDescription().setStateWithMessage(Plain.WAITING);
				this.logging.registerGlobalActivityEvent (actNorm, "ToWaiting", "Rule 9.12"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			else if(rule913){
				this.logging.registerGlobalActivityEvent (actNorm, "Redone", "Rule 9.13"); //$NON-NLS-1$ //$NON-NLS-2$
				//TODO actNorm.getTheEnactionDescription().setStateWithMessage(Plain.WAITING);
				this.logging.registerGlobalActivityEvent (actNorm, "ToWaiting", "Rule 9.13"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			else {
				this.logging.registerGlobalActivityEvent (actNorm, "Redone", "Rule 9.11"); //$NON-NLS-1$ //$NON-NLS-2$
				//TODO actNorm.getTheEnactionDescription().setStateWithMessage(Plain.WAITING);
				this.logging.registerGlobalActivityEvent (actNorm, "ToWaiting", "Rule 9.11"); //$NON-NLS-1$
			}

			// reset tasks
			Collection agents = this.getInvolvedAgents(actNorm);
			Iterator iter = agents.iterator();
			while (iter.hasNext()) {
				Agent agent = (Agent) iter.next();

				if(rule912){
					this.updateAgenda(agent, actNorm, Plain.WAITING, "Rule 9.12");
				}
				else if(rule913){
					this.updateAgenda(agent, actNorm, Plain.WAITING, "Rule 9.13");
				}
				else{
					this.updateAgenda(agent, actNorm, Plain.WAITING, "Rule 9.11");
				}

			}
		}
		return redone;
	}

	private Activity versioning(Activity activity){

		Activity actVersion;
		if(activity instanceof Plain){
			Plain plain = (Plain)activity;
			EnactionDescription enaction = plain.getTheEnactionDescription();
			String oldState = enaction.getState();
			Date oldBegin = enaction.getActualBegin();
			Date oldEnd = enaction.getActualEnd();
			plain.getTheEnactionDescription().setActualBegin(null);
			plain.getTheEnactionDescription().setActualEnd(null);

			if(plain instanceof Normal){
				Normal newNormal = new Normal();
				newNormal.getTheEnactionDescription().setState(oldState);
				newNormal.getTheEnactionDescription().setActualBegin(oldBegin);
				newNormal.getTheEnactionDescription().setActualEnd(oldEnd);
				newNormal.setScript( ((Normal)plain).getScript() );
				newNormal.setDelegable( ((Normal)plain).getDelegable() );
				newNormal.setAutoAllocable( ((Normal)plain).getAutoAllocable() );
				newNormal.setHowLong( ((Normal)plain).getHowLong() );
				newNormal.setHowLongUnit( ((Normal)plain).getHowLongUnit() );
				newNormal.setPlannedBegin( ((Normal)plain).getPlannedBegin() );
				newNormal.setPlannedEnd( ((Normal)plain).getPlannedEnd() );
				actVersion = newNormal;
			}
			else{ // intanceof Automatic
				Automatic newAutomatic = new Automatic();
				newAutomatic.getTheEnactionDescription().setState(oldState);
				newAutomatic.getTheEnactionDescription().setActualBegin(oldBegin);
				newAutomatic.getTheEnactionDescription().setActualEnd(oldEnd);
				actVersion = newAutomatic;
			}
		}
		else{
			Decomposed decomposed = (Decomposed)activity;
			String state = decomposed.getTheReferedProcessModel().getPmState();
			Decomposed newDecomposed = new Decomposed();
			newDecomposed.getTheReferedProcessModel().setPmState(state);
			actVersion = newDecomposed;
		}

		this.copyActivityRelationships(activity, actVersion);

		Collection versions = activity.getHasVersions();

		LinkedList versList = new LinkedList();
		Iterator itervers = versions.iterator();
		while (itervers.hasNext()) {
			Activity actv = (Activity) itervers.next();
			if(actv != null){
				versList.addLast(actv);
			}
		}

		activity.clearHasVersions();
		Iterator iterVersList = versList.iterator();
		while (iterVersList.hasNext()) {
			Activity actv = (Activity) iterVersList.next();
			if(actv != null){
				activity.insertIntoHasVersions(actv);
			}
		}

		int size = activity.getHasVersions().size() + 1;
		String	version = "_v" + size; //$NON-NLS-1$
		actVersion.setIdent(activity.getIdent() + version);
		actVersion.setName(activity.getName() + version);
		activity.insertIntoHasVersions(actVersion);

		String message = "<MESSAGE>" +  //$NON-NLS-1$
							"<NOTIFY>" +  //$NON-NLS-1$
								"<OID>" + activity.getOid()	+ "</OID>" +  //$NON-NLS-1$ //$NON-NLS-2$
								"<TYPE>UPT</TYPE>" +  //$NON-NLS-1$
								"<CLASS>" + activity.getClass().getName() + "</CLASS>" +  //$NON-NLS-1$ //$NON-NLS-2$
								"<BY>Apsee</BY>" +  //$NON-NLS-1$
							"</NOTIFY>" +  //$NON-NLS-1$
						"</MESSAGE>"; //$NON-NLS-1$

		try {

			if (this.remote == null) {
				reloadRemote();
			}
			if (this.remote != null) {

				this.remote.sendMessage(message);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
		}

		return actVersion;
	}

	private void copyActivityRelationships(Activity actFrom, Activity actTo){

        if(!actFrom.getActivityEstimation().isEmpty()){

        	ActivityEstimation greater = null;
        	//Collection temp = new HashSet();

        	Collection actEstimations = actFrom.getActivityEstimation();
        	Iterator iterActEstimations = actEstimations.iterator();
        	while (iterActEstimations.hasNext()) {
				ActivityEstimation actEstimation = (ActivityEstimation) iterActEstimations.next();
				if(actEstimation != null){
					if(greater != null){
						if(greater.getOid() < actEstimation.getOid())
							greater = actEstimation;
					}else greater = actEstimation;
					//temp.add(actEstimation);
					//actTo.insertIntoActivityEstimation(actEstimation);
				}
			}
        	//actFrom.getActivityEstimation().removeAll(temp);

        	// The last estimation from the old version must be copied for the new one.
        	if(greater != null){
        		ActivityEstimation lastEst = new ActivityEstimation();
        		lastEst.setUnit(greater.getUnit());
        		lastEst.setValue(greater.getValue());
        		lastEst.insertIntoMetricDefinition(greater.getMetricDefinition());
        		actTo.insertIntoActivityEstimation(lastEst);
        	}
        }
        if(!actFrom.getActivityMetric().isEmpty()){
        	ActivityMetric greater = null;
        	//Collection temp = new HashSet();

        	Collection actMetrics = actFrom.getActivityMetric();
        	Iterator iterActMetrics = actMetrics.iterator();
        	while (iterActMetrics.hasNext()) {
				ActivityMetric actMetric = (ActivityMetric) iterActMetrics.next();
				if(actMetric != null){
					if(greater != null){
						if(greater.getOid() < actMetric.getOid())
							greater = actMetric;
					}else greater = actMetric;
					//temp.add(actMetric);
	                //actMetric.insertIntoActivity(actTo);
				}
			}
        	//actFrom.getActivityMetric().removeAll(temp);

        	// The last metric from the old version must be copied for the new one.
        	if(greater != null){
        		ActivityMetric lastMet = new ActivityMetric();
        		lastMet.setUnit(greater.getUnit());
        		lastMet.setValue(greater.getValue());
        		lastMet.setPeriodBegin(greater.getPeriodBegin());
        		lastMet.setPeriodEnd(greater.getPeriodEnd());
        		lastMet.insertIntoMetricDefinition(greater.getMetricDefinition());
        		actTo.insertIntoActivityMetric(lastMet);
        	}

        }
        if(actFrom.getFromArtifactCon()!= null){
        	Collection temp = new HashSet();

        	Collection fromArtifactCons = actFrom.getFromArtifactCon();
        	Iterator iterFromArtifactCons = fromArtifactCons.iterator();
        	while (iterFromArtifactCons.hasNext()) {
        		ArtifactCon fromArtifactCon = (ArtifactCon) iterFromArtifactCons.next();
        		if(fromArtifactCon!=null){
            		temp.add(fromArtifactCon);
                    fromArtifactCon.getToActivity().add(actTo);
    				actTo.getFromArtifactCon().add(fromArtifactCon);
        		}
			}
        }

        if(actFrom.getTheActivityType()!= null){

        	ActivityType activityType = actFrom.getTheActivityType();
        	actTo.setTheActivityType(activityType);
        	activityType.getTheActivity().add(actTo);
        }
        if(actFrom.getTheModelingActivityEvent()!= null){
        	Collection temp = new HashSet();

        	Collection modelingActivityEvents = actFrom.getTheModelingActivityEvent();
        	Iterator iterModelingActivityEvents = modelingActivityEvents.iterator();
        	while (iterModelingActivityEvents.hasNext()) {
        		ModelingActivityEvent modelingActivityEvent = (ModelingActivityEvent) iterModelingActivityEvents.next();
        		if(modelingActivityEvent != null){
            		temp.add(modelingActivityEvent);
                    modelingActivityEvent.setTheActivity(actTo);
    				actTo.getTheModelingActivityEvent().add(modelingActivityEvent);
        		}
			}
        	actFrom.getTheModelingActivityEvent().removeAll(temp);
        }
        if(actFrom.getToArtifactCon()!= null){
        	Collection temp = new HashSet();

        	Collection toArtifactCons = actFrom.getToArtifactCon();
        	Iterator iterToArtifactCons = toArtifactCons.iterator();
        	while (iterToArtifactCons.hasNext()) {
        		ArtifactCon toArtifactCon = (ArtifactCon) iterToArtifactCons.next();
        		if(toArtifactCon!=null){
            		temp.add(toArtifactCon);
                    toArtifactCon.getFromActivity().add(actTo);
    				actTo.getToArtifactCon().add(toArtifactCon);
        		}
			}
        }
	}


	/**
	 * Rule G1.10 and G1.11
	 */
	public void createNewVersion(String act_id)
			throws WebapseeException {

    	Activity activity = this.activityDAO.retrieveBySecondaryKey(act_id);

        if (activity == null){

        	throw new DataBaseException(Messages.getString("facades.EnactmentEngine.DBExcActivity")+act_id+Messages.getString("facades.EnactmentEngine.DBExcDoesNotExist")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        Process process = this.processDAO.retrieveBySecondaryKey(activity.getIdent().substring(0, activity.getIdent().indexOf(".")));
        String pState = process.getPState();
        if(pState.equals(Process.NOT_STARTED)){
        	throw new WebapseeException(Messages.getString("facades.EnactmentEngine.WAExcRedoNotEnactActivity"));
		}

        ProcessModel processModel = activity.getTheProcessModel();
        String processModelState = processModel.getPmState();

        if(processModelState.equals(ProcessModel.FINISHED)
        	|| processModelState.equals(ProcessModel.FAILED)
        	|| processModelState.equals(ProcessModel.CANCELED)){
        	throw new WebapseeException(Messages.getString("facades.EnactmentEngine.WAExcRedoFinishedProcMod"));
        }

		// Now we start the implementation of the rules
		String state = this.getState(activity);
		System.out.println("Activity: " + activity.getIdent());
		System.out.println("State after: " + state);

		if (state.equals(Plain.WAITING)
			|| state.equals(ProcessModel.REQUIREMENTS)
			|| state.equals(ProcessModel.ABSTRACT)
			|| state.equals(ProcessModel.INSTANTIATED)
			|| state.equals(Plain.READY)
			|| state.equals("")) {

			this.redoActivity(activity);

			//this.rollbackProcessModelState(activity.getTheProcessModel());

			this.searchForFiredConnections (activity.getTheProcessModel().getOid(), ""); //$NON-NLS-1$
	       	this.searchForReadyActivities (activity.getTheProcessModel().getOid());
	       	this.determineProcessModelStates(activity.getTheProcessModel());

		} else if ((state.equals(Plain.ACTIVE)
			|| state.equals(ProcessModel.ENACTING))
			|| (state.equals(Plain.CANCELED)
			|| state.equals(ProcessModel.CANCELED))
			|| (state.equals(Plain.FINISHED)
			|| state.equals(ProcessModel.FINISHED))
			|| (state.equals(Plain.FAILED)
            || state.equals(ProcessModel.FAILED))
			|| (state.equals(Plain.PAUSED))) {

			this.redoActivityAndPropagate(activity);

			//this.rollbackProcessModelState(activity.getTheProcessModel());

			this.searchForFiredConnections (activity.getTheProcessModel().getOid(), ""); //$NON-NLS-1$
	       	this.searchForReadyActivities (activity.getTheProcessModel().getOid());
	       	this.determineProcessModelStates(activity.getTheProcessModel());
		}

		// Persistence Operation
		this.activityDAO.update(activity);
		System.out.println("State now: " + this.getState(activity));
	}

	private void rollbackProcessModelState(ProcessModel processModel/*, Session currentSession*/){
		String state = processModel.getPmState();
		if(state.equals(ProcessModel.FINISHED)
			|| state.equals(ProcessModel.FAILED)){

			boolean toEnacting = false;

			Collection acts = processModel.getTheActivity();
			Iterator iter = acts.iterator();
			while (iter.hasNext()) {
				Activity activity = (Activity) iter.next();
				if(activity != null){
					String actState = this.getState(activity);
					if(actState.equals(Plain.ACTIVE)
						||actState.equals(Plain.PAUSED)
						||actState.equals(Plain.FINISHED)
						||actState.equals(Plain.FAILED)
						||actState.equals(ProcessModel.ENACTING)
						||actState.equals(ProcessModel.FINISHED)
						||actState.equals(ProcessModel.FAILED)){

						toEnacting = true;
						break;
					}
				}
			}

			if(toEnacting){
				//TODO processModel.setPmStateWithMessage(ProcessModel.ENACTING);
				this.logging.registerProcessModelEvent (processModel, "To"+ProcessModel.ENACTING, "Rule G1.10");
			}
			else{
				//TODO processModel.setPmStateWithMessage(ProcessModel.INSTANTIATED);
				this.logging.registerProcessModelEvent (processModel, "To"+ProcessModel.INSTANTIATED, "Rule G1.11");
			}

			// To Upper Levels
			Decomposed upperDecomposed = processModel.getTheDecomposed();
			if(upperDecomposed != null){

				this.rollbackProcessModelState(upperDecomposed.getTheProcessModel());

				Collection connsTo = this.getConnectionsTo(upperDecomposed);
				Iterator iterConnsTo = connsTo.iterator();
				while (iterConnsTo.hasNext()) {
					Connection connTo = (Connection) iterConnsTo.next();
					Collection successors = this.getSuccessors(connTo);
					Iterator iterSuccessors = successors.iterator();
					while (iterSuccessors.hasNext()) {
						Activity successor = (Activity) iterSuccessors.next();
						try {
							this.redoActivityAndPropagate(successor);
						} catch (WebapseeException e) {
							e.printStackTrace();
							continue;
						}
					}
				}
			}
		}
	}


    /**
     * called by APSEE Manager.
     * Applies to the rule 9.14
     * Creates a new version of an Artifact from a Plain
     * Activity.
     * This operatiosn is now handlered by the artifact version control system
     *
     * private void createNewArtifactVersion (Plain actPlain)
     */

    /**
     * called by APSEE Manager.
     * Applies to the rules 9.15
     * Resets the Automatic Activity that was enacting,
     * creating a new version of it.
     */
	private boolean redoAutomaticActivity (Automatic actAutom)
		throws WebapseeException {

		boolean redone = false;
		String state = actAutom.getTheEnactionDescription().getState();

		if (! state.equals (Plain.WAITING)) {
			redone = true;
			if (state.equals (Plain.ACTIVE) || state.equals (Plain.FINISHED)) {
				//TODO actAutom.getTheEnactionDescription().setStateWithMessage(Plain.FAILED);
				this.logging.registerGlobalActivityEvent (actAutom, "ToFailed", "Rule ?"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			else {
				//TODO actAutom.getTheEnactionDescription().setStateWithMessage(Plain.CANCELED);
				this.logging.registerGlobalActivityEvent (actAutom, "ToCanceled", "Rule ?"); //$NON-NLS-1$ //$NON-NLS-2$
			}

			this.versioning(actAutom);

			// set EnactionDesc to a new object
			EnactionDescription enact = new EnactionDescription();
			actAutom.setTheEnactionDescription(enact);

			this.logging.registerGlobalActivityEvent (actAutom, "Re-done", "Rule 9.15"); //$NON-NLS-1$ //$NON-NLS-2$
			//TODO actAutom.getTheEnactionDescription().setStateWithMessage(Plain.WAITING);
			this.logging.registerGlobalActivityEvent (actAutom, "ToWaiting", "Rule 9.15"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return redone;
	}

    /**
     * called by APSEE Manager.
     * Applies to the rules 9.16
     * Resets the Decomposed Activity that was enacting,
     * creating a new version of it.
     */
	private boolean redoDecomposedActivity (Decomposed actDecomp/*, Session currentSession*/)
		throws WebapseeException {

		ProcessModel refProcModel = actDecomp.getTheReferedProcessModel();
		String state = refProcModel.getPmState();

		if (state.equals (ProcessModel.ENACTING) || state.equals (ProcessModel.FINISHED)) {
			//TODO refProcModel.setPmStateWithMessage(ProcessModel.FAILED);
			this.logging.registerProcessModelEvent (refProcModel, "ToFailed", "Rule ?"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		else {
			//TODO refProcModel.setPmStateWithMessage(ProcessModel.CANCELED);
			this.logging.registerProcessModelEvent (refProcModel, "ToCanceled", "Rule ?"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		this.versioning(actDecomp);

		refProcModel.setTheProcessModelEvent(new LinkedList());
		//TODO refProcModel.setPmStateWithMessage(ProcessModel.INSTANTIATED);

		this.logging.registerProcessModelEvent(refProcModel, "Re-done", "Rule 9.16"); //$NON-NLS-1$ //$NON-NLS-2$
		this.logging.registerProcessModelEvent (refProcModel, "ToInstantiated", "Rule 9.16"); //$NON-NLS-1$ //$NON-NLS-2$

		// propagate to child activities
		Collection activities = refProcModel.getTheActivity();

		Iterator iter = activities.iterator();
		while (iter.hasNext()) {
			Activity act = (Activity) iter.next();
			this.redoActivity(act);
			this.resetConnection (act);
		}

		return true;
	}

    /**
     * called by APSEE Manager.
     * Applies to the rules 9.18
     * Set the attribute fired to false from the Multiple Connections
     * that succeed the Activity.
     */
	private void resetConnection (Activity act){

		Collection conns = this.getConnectionsTo(act);
		Iterator iter = conns.iterator();
		while (iter.hasNext()) {
			Connection conn = (Connection) iter.next();
			if (conn instanceof MultipleCon){
				MultipleCon multi = (MultipleCon)conn;
				multi.setFired(new Boolean (false));
			}
		}
	}

	/**
	 * Releases the Resources from the Normal Activity and
	 * set to Failed the state of the Tasks that refers to this
	 * Normal Activity.
	 */
	private void manCleanActivity (Activity act, String state) throws WebapseeException {

		if (act instanceof Normal) { // Rule 9.6
			Normal normal = (Normal)act;
			this.releaseResourcesFromActivity(normal);
			this.changeTasksState (normal, state);
		}
		//else is Decomposed, so Rule 9.8, i.e, return and execute feedbacks

	}

	/*Resources - implementation of the methods that involve the
	 * 		      Resources.
	 */

    /**
     * Called by APSEE Manager.
     * Involves the rules: 12.2, 12.7, 12.8.
     * Release all the resources from a Normal Activity.
     * Changing their states to Available.
     */
    private void releaseResourcesFromActivity(Normal actNorm){

        Collection activityResources = actNorm.getTheRequiredResource();
        Iterator iter = activityResources.iterator();
        while (iter.hasNext()) {
			RequiredResource requiredResource = (RequiredResource) iter.next();
			Resource resource = requiredResource.getTheResource();
			this.releaseResource(actNorm, resource);
        }
    }

    private void releaseResource(Normal actNorm, Resource resource){

		if (resource instanceof Exclusive){
			Exclusive exc = (Exclusive)resource;
			if (exc.getState().equals(Exclusive.LOCKED)){
				exc.setState(Exclusive.AVAILABLE);
				this.logging.registerResourceEvent(exc, actNorm, "ToAvailable", "Rule 12.7"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}else if (resource instanceof Shareable){
			Shareable sha = (Shareable)resource;
			this.logging.registerResourceEvent(sha, actNorm, "Released", "Rule 12.2"); //$NON-NLS-1$ //$NON-NLS-2$
		}
    }

	/**
	 * Verifies if a Exclusive Resource is Reserved.
	 */
	private boolean isExclResourceReserved (Exclusive exclusive, Date actBegin, Date actEnd)
			throws WebapseeException {

		boolean isReserved = false;
		Collection reserv = exclusive.getTheReservation();
		Iterator iter = reserv.iterator();
		while (iter.hasNext()) {
			Reservation reservation = (Reservation) iter.next();
			Date reservBegin = reservation.getFrom();
			Date reservEnd = reservation.getTo();
			if ( (reservBegin.getTime()<= actEnd.getTime()) &&
			     ((reservBegin.getTime() >= actBegin.getTime()) ||
			      (reservEnd.getTime() >= actBegin.getTime()))) {
			    isReserved = true;
			    break;
			}
		}
		return isReserved;
	}

	/**
	 * Verifies if a Resource is Available and calculates the amounted used
	 * to the Comsumable Resource.
	 */
    private boolean isResourceAvailable(RequiredResource reqResource,
            Normal actNorm) throws WebapseeException {

        boolean isAvailable = false;
        Resource resource = reqResource.getTheResource();
        if (resource instanceof Exclusive) {
            Exclusive exclusive = (Exclusive) resource;
            if (exclusive.getState().equals(Exclusive.AVAILABLE)) {
                boolean isReserved = this.isExclResourceReserved(exclusive,
                        actNorm.getPlannedBegin(), actNorm.getPlannedEnd());
                if (!isReserved){
                    isAvailable = true;
                }
            }
        } else if (resource instanceof Shareable) {
        	Shareable shareable = (Shareable)resource;
            if (shareable.getState().equals(Shareable.AVAILABLE))
                isAvailable = true;
        } else if (resource instanceof Consumable) {
            Consumable consumable = (Consumable) resource;
            if (consumable.getState().equals(Consumable.AVAILABLE)) {
                float needed = reqResource.getAmountNeeded().floatValue();
                float total = consumable.getTotalQuantity().floatValue();
                float used = consumable.getAmountUsed().floatValue();
                double available = total - used;
                if (available >= needed)
                    isAvailable = true;
            }
        }

        return isAvailable;
    }

	/**
	 * Verifies if all the Required Resources of a
	 * Normal Activity are available.
	 */
    private boolean isAllRequiredResourceAvailable(Normal actNorm) throws WebapseeException{
    	boolean ret = true;
    	Collection requiredResources = actNorm.getTheRequiredResource();
    	Iterator iter = requiredResources.iterator();
    	while (iter.hasNext()) {
			RequiredResource reqResource = (RequiredResource) iter.next();
			if(!this.isResourceAvailable(reqResource, actNorm)){
				ret = false;
				break;
			}
		}
    	return ret;
    }

	/**
	 * Allocates all the Required Resources for a Normal
	 * Activity.
	 */
    private void allocateAllForActivity(Normal actNorm, boolean allocConsum)
            throws WebapseeException {

    	Collection resources = actNorm.getTheRequiredResource();
    	Iterator iter = resources.iterator();
    	while (iter.hasNext()) {
			RequiredResource reqResource = (RequiredResource) iter.next();
			try {
				this.allocateResource(reqResource, reqResource.getTheResource(), allocConsum);
			}
			catch (WebapseeException e){
				throw new DataBaseException(Messages.getString("facades.EnactmentEngine.DBExcAAccToTheDatabase")); //$NON-NLS-1$
			}
		}
    }

    /**
     * Called by APSEE Manager.
     * Involves the rules: 12.1, 12.5, 12.6
     * Allocates a Required Resource to a Normal Activity,
     * and treating if the Resource is Comsumable.
     */
    private void allocateResource(RequiredResource reqResource, Resource res,
    	boolean allocConsum) throws WebapseeException{

		if(res instanceof Exclusive){

        	Exclusive allocRes = (Exclusive) res;
        	allocRes.setState(Exclusive.LOCKED);
        	this.logging.registerResourceEvent(allocRes, reqResource.getTheNormal(), "ToLocked", "Rule 12.5"); //$NON-NLS-1$ //$NON-NLS-2$

        }
		else if (res instanceof Consumable){

			Consumable allocRes = (Consumable) res;
			Float needed = reqResource.getAmountNeeded();
			Float total = allocRes.getTotalQuantity();
			Float used = allocRes.getAmountUsed();

			if(used < total){
				used = new Float(used.floatValue() + needed.floatValue());

				if (total <= used){
					allocRes.setAmountUsed(total);
					allocRes.setState(Consumable.FINISHED);
				}
				else{
					allocRes.setAmountUsed(used);
				}
			}
        }
		else if(res instanceof Shareable){

        	Shareable allocRes = (Shareable) res;
        	this.logging.registerResourceEvent(allocRes, reqResource.getTheNormal(), "Requested", "Rule 12.1"); //$NON-NLS-1$ //$NON-NLS-2$
		}
    }

	/*Connections - implementation of the methods that involve the
	 * 		        connections.
	 */

	/**
	 * Implements NACS of rules 2.1 and 2.2.
	 * Analises the Dependencies of the Connections.
	 */
	private boolean isDepSatisfiedToBegin
	(String kindDep, String state, boolean isPlain) {
		boolean satisfied;
		if (isPlain) {
			if (kindDep.equals ("end-start")) { //$NON-NLS-1$
				// NAC 08 of Rule 2.1 & 2.2
				satisfied = state.equals (Plain.FINISHED);
			}
			else if (kindDep.equals ("end-end")) { //$NON-NLS-1$
				satisfied = true;
			}
			else if (kindDep.equals ("start-start")) { //$NON-NLS-1$
				// NAC 06 of Rule 2.1 & 2.2
				satisfied = (state.equals (Plain.ACTIVE) ||
						state.equals (Plain.PAUSED) ||
						state.equals (Plain.FINISHED));
			}
			else satisfied = false;
		}
		else { // decomposed
			if (kindDep.equals ("end-start")) { //$NON-NLS-1$
				// NAC 06 of Rule 2.1
				satisfied = state.equals (ProcessModel.FINISHED);
			}
			else if (kindDep.equals ("end-end")) { //$NON-NLS-1$
				satisfied = true;
			}
			else if (kindDep.equals ("start-start")) { //$NON-NLS-1$
				// NAC 07 of Rule 2.1 & 2.2
				satisfied = (state.equals (ProcessModel.ENACTING) ||
						state.equals (ProcessModel.FINISHED));
			}
			else satisfied = false;
		}
		return (satisfied);
	}

	private boolean isDepSatisfiedToFinish
	(String kindDep, String state, boolean isPlain) {
		boolean satisfied;
		if (isPlain) {
			if (kindDep.equals ("end-start")) { //$NON-NLS-1$
				// NAC 08 of Rule 2.1 & 2.2
				satisfied = state.equals (Plain.FINISHED);
			}
			else if (kindDep.equals ("end-end")) { //$NON-NLS-1$
				satisfied = state.equals (Plain.FINISHED);
			}
			else if (kindDep.equals ("start-start")) { //$NON-NLS-1$
				// NAC 06 of Rule 2.1 & 2.2
				satisfied = (state.equals (Plain.ACTIVE) ||
						state.equals (Plain.PAUSED) ||
						state.equals (Plain.FINISHED));
			}
			else satisfied = false;
		}
		else { // decomposed
			if (kindDep.equals ("end-start")) { //$NON-NLS-1$
				// NAC 06 of Rule 2.1
				satisfied = state.equals (ProcessModel.FINISHED);
			}
			else if (kindDep.equals ("end-end")) { //$NON-NLS-1$
				satisfied = state.equals (ProcessModel.FINISHED);
			}
			else if (kindDep.equals ("start-start")) { //$NON-NLS-1$
				// NAC 07 of Rule 2.1 & 2.2
				satisfied = (state.equals (ProcessModel.ENACTING) ||
						state.equals (ProcessModel.FINISHED));
			}
			else satisfied = false;
		}
		return (satisfied);
	}

    /**
     * called by APSEE Manager.
     * Applies to the rules 7.1, 7.2, 7.3, 7.4, 7.5,
     * 7.6, 7.11,
     * Evaluates the conditions of the BranchCones if
     * the successor is an Activity.
     */
	private boolean isConditionSatisfied
			(BranchCond branchConcond, Activity activity) throws WebapseeException{

		boolean satisfied = false;
		Collection conditions = branchcond.getTheBranchConCondToActivity();
		Iterator iter = conditions.iterator();
		while (iter.hasNext()) {
			BranchConCondToActivity condition =
				(BranchConCondToActivity) iter.next();
			if (condition.getTheActivity().equals(activity)) {
				satisfied = this.conditionValue(condition.getTheCondition());
				break;
			}
		}
		return (satisfied);
	}

    /**
     * Called by APSEE Manager.
     * Applies to the rules 7.6, 7.7, 7.8, 7.9, 7.10,
     * 7.11, 7.12,
     * Evaluates the conditions of the BranchCones if
     * the successor is a Multiple Connection.
     */
	private boolean isConditionSatisfied (BranchCond branchConcond, MultipleCon multipleCon) throws WebapseeException{
		boolean satisfied = false;
		Collection conditions = branchcond.getTheBranchConCondToMultipleCon();
		Iterator iter = conditions.iterator();
		while (iter.hasNext()) {
			BranchCondToMultipleCon condition = (BranchConCondToMultipleCon) iter.next();
			if (condition.getTheMultipleCon().equals(multipleCon)) {
				satisfied = this.conditionValue(condition.getTheCondition());
				break;
			}
		}
		return (satisfied);
	}

	/**
	 * Analises if the JoinCon is satisfied according to
	 * the logical operator and the predecessors.
	 */
	private boolean isJoinSatisfied (Join joinCon) throws WebapseeException {
	    boolean satisfied;

	    String kindJoin = join.getKindJoinCon();
	    String kindDep  = joinCon.getTheDependency().getKindDep();
	    Collection fromActivities  = joinCon.getFromActivity();
	    Collection fromMultipleCon = joinCon.getFromMultipleCon();

	    Activity toActivity = joinCon.getToActivity();
	    String toState = ""; //$NON-NLS-1$
	    if(toActivity != null){
	    	toState = this.getState(toActivity);
	    }

    	// Rules 8.1, 8.2, 8.3, 8.10 and NACs
	    if (kindJoinCon.equals ("AND")) { //$NON-NLS-1$
	    	satisfied = false;
	    	Iterator iter = fromActivities.iterator();
            while (iter.hasNext()) {
            	Activity activity = (Activity)iter.next();
            	boolean isPlain = activity instanceof Plain;
            	String state = this.getState (activity);
            	if(toState.equals(Plain.WAITING)
            		|| toState.equals(Plain.READY)
            		|| toState.equals(ProcessModel.INSTANTIATED)){
                	if ( this.isDepSatisfiedToBegin(kindDep, state, isPlain)) {
                		satisfied = true;
                	}
                	else{
                		satisfied = false;
                		break;
                	}
            	}
            	else if(toState.equals(Plain.ACTIVE)
            		|| toState.equals(ProcessModel.ENACTING)){
                	if ( this.isDepSatisfiedToFinish(kindDep, state, isPlain)) {
                		satisfied = true;
                	}
                	else{
                		satisfied = false;
                		break;
                	}
            	}
            }
            if (!satisfied) {
            	iter = fromMultipleCon.iterator();
                while (iter.hasNext()) {
                	MultipleCon multiplecon = (MultipleCon)iter.next();
                	if (! multiplecon.isFired().booleanValue()) {
                		if(multiplecon instanceof BranchConCond)
                 		   satisfied = !this.isConditionSatisfied((BranchCond)multiplecon, joinCon);
                 		else
                 			satisfied = false;
                		break;
                	}
                }
            }
	    }
	    else	// Rules 8.4, 8.5, 8.11, 8.12, 8.13, 8.14, 8.15, 8.16 and NACs
	    if (kindJoinCon.equals ("OR")) { //$NON-NLS-1$
	    	satisfied = false;
	    	Iterator iter = fromActivities.iterator();
            while (iter.hasNext()) {
            	Activity activity = (Activity)iter.next();
            	boolean isPlain = activity instanceof Plain;
            	String state = this.getState (activity);
            	if(toState.equals(Plain.WAITING)
               		|| toState.equals(Plain.READY)
               		|| toState.equals(ProcessModel.INSTANTIATED)){
                   	if (this.isDepSatisfiedToBegin(kindDep, state, isPlain)) {
                   		satisfied = true;
                   		break;
                   	}
               	}
               	else if(toState.equals(Plain.ACTIVE)
               		|| toState.equals(ProcessModel.ENACTING)){
                   	if (this.isDepSatisfiedToFinish(kindDep, state, isPlain)) {
                   		satisfied = true;
                   		break;
                   	}
               	}
            }
            if (! satisfied) {
            	iter = fromMultipleCon.iterator();
                while (iter.hasNext()) {
                	MultipleCon multiplecon = (MultipleCon)iter.next();
                	if (multiplecon.isFired().booleanValue()) {
                		if(multiplecon instanceof BranchConCond)
                		   satisfied = this.isConditionSatisfied((BranchCond)multiplecon, joinCon);
                		else
                			satisfied = true;
                		break;
                	}
                }
            }
	    }
	    else // Rules 8.7, 8.8, 8.9, 8.17, 8.18, 8.19 and NACs
	    if (kindJoinCon.equals ("XOR")) { //$NON-NLS-1$
	    	int trueCount = 0;
	    	Iterator iter = fromActivities.iterator();
            while (iter.hasNext()) {
            	Activity activity = (Activity)iter.next();
                boolean isPlain = activity instanceof Plain;
                String state = this.getState (activity);
                if(toState.equals(Plain.WAITING)
                	|| toState.equals(Plain.READY)
                	|| toState.equals(ProcessModel.INSTANTIATED)){
                   	if (this.isDepSatisfiedToBegin(kindDep, state, isPlain)) {
                        trueCount++;
                   	}
                }
                else if(toState.equals(Plain.ACTIVE)
                	|| toState.equals(ProcessModel.ENACTING)){
                	if (this.isDepSatisfiedToFinish(kindDep, state, isPlain)) {
                        trueCount++;
                    }
                }
       		}
            iter = fromMultipleCon.iterator();
            while (iter.hasNext()) {
                    MultipleCon multiplecon = (MultipleCon)iter.next();
                    if (multiplecon.isFired().booleanValue()){
                		if(multiplecon instanceof BranchConCond)
                 		   if(this.isConditionSatisfied((BranchCond)multiplecon, joinCon))
                 		   		trueCount++;
                    }
            }
            satisfied = (trueCount == 1);
	    }
	    else satisfied = false;

	    return (satisfied);
	}

	/**
	 * Analises if the BranchCon is satisfied according to
	 * the logical operator and the predecessors.
	 */
	private boolean isBranchSatisfied (Branch branchCon) {

	    boolean satisfied;

	    Activity activity = branchCon.getFromActivity();
	    MultipleCon multiplecon = branchCon.getFromMultipleConnection();

	    if (activity != null) {
	    	boolean isPlain = activity instanceof Plain;
	    	String state = this.getState (activity);
	    	String kindDep  = branchCon.getTheDependency().getKindDep();
	    	satisfied = this.isDepSatisfiedToBegin(kindDep, state, isPlain);
	    }
	    else if (multiplecon != null) {
	    	satisfied = multiplecon.isFired().booleanValue();
	    }
	    else satisfied = false;

	    return (satisfied);
	}

	/**
	 * Returns a Collection with the successors of an
	 * Activity.
	 */
	private Collection getConnectionsTo(Activity act){

		Collection connTo = new LinkedList();
		if(act.getToSimpleCon() != null)
		    connTo.addAll(act.getToSimpleCon());
		if(act.getToJoinCon() != null)
		    connTo.addAll(act.getToJoinCon());
		if(act.getToBranchCon() != null)
		    connTo.addAll(act.getToBranchCon());
		return connTo;
	}

	/**
	 * Returns a Collection with the predecessors of an
	 * Activity.
	 */
	private Collection getConnectionsFrom(Activity act){

		Collection connFrom = new LinkedList();
		if(act.getFromSimpleCon() != null)
		    connFrom.addAll(act.getFromSimpleCon());
		if(act.getFromJoinCon() != null)
		    connFrom.addAll(act.getFromJoinCon());
		if(act.getFromBranchANDCon() != null)
		    connFrom.addAll(act.getFromBranchANDCon());
		Collection bctas = act.getTheBranchConCondToActivity();
		Iterator iterBctas = bctas.iterator();
		while (iterBctas.hasNext()) {
		    BranchCondToActivity bcta = (BranchConCondToActivity) iterBctas.next();
		    if(bcta.getTheBranchConCond() != null)
		       connFrom.add(bcta.getTheBranchConCond());
		}
		return connFrom;
	}

	/**
	 * Returns a Collection with the successors (Activities
	 * and Multiple Connection) of a Connection.
	 */
	private Collection getSuccessors(Connection conn){

		Collection succ = new LinkedList();
		if(conn instanceof Sequence){
			Sequence seq = (Sequence)conn;
			if(seq.getToActivity() != null)
			    succ.add(seq.getToActivity());
		}
		else if (conn instanceof BranchCon) {
			Branch branch = (BranchCon)conn;
			if(branch instanceof BranchANDCon){
				BranchAND bAND = (BranchAND)branchCon;
				if(bAND.getToActivity() != null)
				    succ.addAll(bAND.getToActivity());
				if(bAND.getToMultipleCon() != null)
				    succ.addAll(bAND.getToMultipleCon());
			}
			else{
				BranchCond bCond = (BranchCond)branchCon;
				Collection bctmc = bCond.getTheBranchConCondToMultipleCon();
				Collection atbc = bCond.getTheBranchConCondToActivity();
				Iterator iterMulti = bctmc.iterator(),
						 iterAct = atbc.iterator();
				while (iterMulti.hasNext()) {
					BranchCondToMultipleCon multi = (BranchConCondToMultipleCon) iterMulti.next();
					if(multi.getTheMultipleCon() != null)
					    succ.add(multi.getTheMultipleCon());
				}
				while (iterAct.hasNext()) {
					BranchCondToActivity act = (BranchConCondToActivity) iterAct.next();
					if(act.getTheActivity() != null)
					    succ.add(act.getTheActivity());
				}
			}
		}
		else if (conn instanceof JoinCon) {
			Join join = (JoinCon)conn;
			if(joinCon.getToActivity() != null)
			    succ.add(joinCon.getToActivity());
			if(joinCon.getToMultipleCon() != null)
			    succ.add(joinCon.getToMultipleCon());
		}
		return succ;
	}

	/**
	 * Returns a Collection with the predecessors (Activities
	 * and Multiple Connection) of a Connection.
	 */
	private Collection getPredecessors(Connection conn){

		Collection pred = new LinkedList();
		if(conn instanceof Sequence){
			Sequence seq = (Sequence)conn;
            if(seq.getFromActivity() != null)
                pred.add(seq.getFromActivity());
		}
		else if (conn instanceof BranchCon) {
			Branch branch = (BranchCon)conn;
            if(branchCon.getFromActivity() != null)
                pred.add(branchCon.getFromActivity());
            if(branchCon.getFromMultipleConnection() != null)
                pred.add(branchCon.getFromMultipleConnection());
		}
		else if (conn instanceof JoinCon) {
			Join join = (JoinCon)conn;
            if(joinCon.getFromActivity() != null)
                pred.addAll(joinCon.getFromActivity());
            if(joinCon.getFromMultipleCon() != null)
                pred.addAll(joinCon.getFromMultipleCon());
		}
		return pred;
	}

	private static final transient int
	TO_ACT_CAN_START  = 1,
	TO_ACT_CAN_FINISH = 2;

	/**
	 * Determines the state of a dependency based on
	 * the Activity the cames before it.
	 */
	private int simpleDepConnState (Dependency depConn, Activity fromAct){
		String depType = depConn.getKindDep();
		boolean isPlain = fromAct instanceof Plain;
		String state;
		if (isPlain) {
			Plain plain = (Plain)fromAct;
			state = plain.getTheEnactionDescription().getState();
		}
		else {
			Decomposed dec = (Decomposed)fromAct;
			state = dec.getTheReferedProcessModel().getPmState();
		}
		return (this.depConnState (depType, state, isPlain));
	}

	/**
	 * Calculates the state of an connection based on the
	 * Constants defined above (TO_ACT_CAN_START and
	 * TO_ACT_CAN_FINISH).
	 */
	private int depConnState (String depType, String state, boolean isPlain){
		int result = 0;
		if (isPlain) {
			if (depType.equals ("end-start")) { //$NON-NLS-1$
				result |= state.equals (Plain.FINISHED) ? TO_ACT_CAN_START : 0;
				result |= TO_ACT_CAN_FINISH;
			}
			else if (depType.equals ("start-start")) { //$NON-NLS-1$
				result |= (state.equals (Plain.ACTIVE) || state.equals (Plain.PAUSED)
						|| state.equals (Plain.FINISHED)) ? TO_ACT_CAN_START : 0;
				result |= TO_ACT_CAN_FINISH;
			}
			else if (depType.equals ("end-end")) { //$NON-NLS-1$
				result |= TO_ACT_CAN_START;
				result |= state.equals (Plain.FINISHED) ? TO_ACT_CAN_FINISH : 0;
			}
		}
		else {
			if (depType.equals ("end-start")) { //$NON-NLS-1$
				result |= state.equals (ProcessModel.FINISHED) ? TO_ACT_CAN_START : 0;
				result |= TO_ACT_CAN_FINISH;
			}
			else if (depType.equals ("start-start")) { //$NON-NLS-1$
				result |= (state.equals (ProcessModel.ENACTING) || state.equals (ProcessModel.FINISHED))
				? TO_ACT_CAN_START : 0;
				result |= TO_ACT_CAN_FINISH;
			}
			else if (depType.equals ("end-end")) { //$NON-NLS-1$
				result |= TO_ACT_CAN_START;
				result |= state.equals (ProcessModel.FINISHED) ? TO_ACT_CAN_FINISH : 0;
			}
		}
		return (result);
	}

}

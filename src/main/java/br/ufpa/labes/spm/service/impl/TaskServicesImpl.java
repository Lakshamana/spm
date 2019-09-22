package br.ufpa.labes.spm.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Query;

import org.qrconsult.spm.converter.core.Converter;
import org.qrconsult.spm.converter.core.ConverterImpl;
import br.ufpa.labes.spm.exceptions.ImplementationException;
import br.ufpa.labes.spm.repository.interfaces.activities.IActivityDAO;
import br.ufpa.labes.spm.repository.interfaces.agent.IAgentDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IDependencyDAO;
import br.ufpa.labes.spm.repository.interfaces.log.IAgendaEventDAO;
import br.ufpa.labes.spm.repository.interfaces.plainActivities.INormalDAO;
import br.ufpa.labes.spm.repository.interfaces.processModels.IProcessDAO;
import br.ufpa.labes.spm.repository.interfaces.processModels.IProcessModelDAO;
import br.ufpa.labes.spm.repository.interfaces.taskagenda.ITaskDAO;
import br.ufpa.labes.spm.service.dto.AgendaEventDTO;
import br.ufpa.labes.spm.service.dto.AgendaEventsDTO;
import br.ufpa.labes.spm.service.dto.TaskDTO;
import br.ufpa.labes.spm.exceptions.DAOException;
import br.ufpa.labes.spm.exceptions.WebapseeException;
import br.ufpa.labes.spm.domain.Activity;
import br.ufpa.labes.spm.domain.Decomposed;
import br.ufpa.labes.spm.domain.Plain;
import br.ufpa.labes.spm.domain.Agent;
import br.ufpa.labes.spm.domain.WorkGroup;
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
import br.ufpa.labes.spm.domain.AgendaEvent;
import br.ufpa.labes.spm.domain.ModelingActivityEvent;
import br.ufpa.labes.spm.domain.Automatic;
import br.ufpa.labes.spm.domain.EnactionDescription;
import br.ufpa.labes.spm.domain.Normal;
import br.ufpa.labes.spm.domain.ReqAgent;
import br.ufpa.labes.spm.domain.ReqWorkGroup;
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
import br.ufpa.labes.spm.service.interfaces.Logging;
import br.ufpa.labes.spm.service.interfaces.TaskServices;

public class TaskServicesImpl implements TaskServices {

	private static final String TASK_CLASSNAME = Task.class.getName();
	private static final String AGENDAEVENT_CLASSNAME = AgendaEvent.class
			.getName();
	Converter converter = new ConverterImpl();
	private static final transient int TO_ACT_CAN_START = 1,
			TO_ACT_CAN_FINISH = 2;

	ITaskDAO taskDAO;

	IProcessDAO processDAO;

	INormalDAO normalDAO;

	IActivityDAO activityDAO;

	IAgentDAO agentDAO;

	IAgendaEventDAO agendaEventDAO;

	Logging logging;

	IProcessModelDAO processModelDAO;

	IDependencyDAO dependencyDAO;

	private Query query;

	private void updateAgenda(Agent agent, Normal actNorm, String state,
			String why) {
		Task task = this.setTaskState(agent, actNorm, state);
		if (task != null) {
			this.logging.registerAgendaEvent(task, "To" + state, why); //$NON-NLS-1$
		}
    }

	public TaskDTO getTask(String name) {
		String hql = "SELECT task from " + TASK_CLASSNAME + " as task where task.theNormal.name = :name";
		query = taskDAO.getPersistenceContext().createQuery(hql);
		query.setParameter("name", name);

		List<Task> result = query.getResultList();
		TaskDTO taskDTO = new TaskDTO();
		if(!result.isEmpty()) {
			Task t = result.get(0);
			taskDTO.setName(t.getTheNormal().getName());
//			taskDTO.set
		}
		return taskDTO;
	}

    /**
     * called by APSEE Manager.
     * begin with rules 1.1 - 1.4
     */
	@Override
	public void executeProcess(String processId) {

		Object proc;
		proc = processDAO.retrieveBySecondaryKey(processId);
		if (proc == null) {
			//        	throw new DataBaseException(Messages.getString("facades.EnactmentEngine.DBExcProcess")+processId+Messages.getString("facades.EnactmentEngine.DBExcDoesNotExist")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		// checks related to the state of the system

		Process process = (Process) proc;
		if (!process.getpStatus().name().equals(Process.NOT_STARTED)) {
			//        	throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcProcess") +processId+ Messages.getString("facades.EnactmentEngine.UserExcIsAlreEnac")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		// now start the implementation of the rules

		ProcessModel procModel = process.getTheProcessModel();
		process.setPState(Process.ENACTING);
		this.createTasks(procModel);
		this.logging.registerProcessEvent(process, "ToEnacting", "Rule 1.1"); //$NON-NLS-1$ //$NON-NLS-2$

		this.searchForFiredConnections(procModel, ""); //$NON-NLS-1$
		this.searchForReadyActivities(procModel);
		this.determineProcessModelStates(procModel);

		// Persistence Operations

		processDAO.update(process);
	}

	/**
	 * Creates the Tasks (from the Activities) for the Agents. If the activities
	 * are Decomposed then calls itself again, else calls createTasksNormal().
	 */
	@Override
	public void createTasks(ProcessModel processModel) {
		Collection acts = processModel.getTheActivity();
		acts.remove(null);
		if (acts.isEmpty()) {
			return;
		}

		processModel.setPmState(ProcessModel.INSTANTIATED);

		Iterator iter = acts.iterator();
		while (iter.hasNext()) {
			Activity act = (Activity) iter.next();
			if (act instanceof Normal) {
				Normal actNorm = (Normal) act;
				this.createTasksNormal(actNorm);
			} else if (act instanceof Automatic) {
				Automatic actAuto = (Automatic) act;
				actAuto.getTheEnactionDescription().setStateWithMessage(
						Plain.WAITING);
				this.logging.registerGlobalActivityEvent(actAuto,
						"ToWaiting", "Rule 1.4"); //$NON-NLS-1$ //$NON-NLS-2$
			} else if (act instanceof Decomposed) {
				Decomposed actDecomp = (Decomposed) act;
				this.createTasks(actDecomp.getTheReferedProcessModel());
			}
		}
	}

	/**
	 * Creates the Tasks (from the Normal Activities) for the Agents and set the
	 * Tasks' Local state to Waiting.
	 */
	@Override
	public void createTasksNormal(Normal activityNormal) {

		Collection reqPeople = this.getInvolvedAgents(activityNormal);
		Iterator iter = reqPeople.iterator();
		boolean has = iter.hasNext();
		while (iter.hasNext()) {
			Agent agent = (Agent) iter.next();
			this.addTaskToAgent(agent, activityNormal);
			this.updateAgenda(agent, activityNormal, Plain.WAITING, "Rule 1.2");
		}

		if (has) {
			activityNormal.getTheEnactionDescription().setStateWithMessage(
					Plain.WAITING);
			this.logging.registerGlobalActivityEvent(activityNormal,
					"ToWaiting", "Rule 1.2"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * Returns the Task's State, from an Agent's Process Agenda, that refers to
	 * a Normal Activity.
	 */
	private String getTaskState(Agent agent, Normal normal) {
		String state = null;
		TaskAgenda taskagenda = agent.getTheTaskAgenda();
		Collection processagendas = taskagenda.getTheProcessAgenda();
		Iterator iter = processagendas.iterator();
		while (iter.hasNext()) {
			ProcessAgenda processagenda = (ProcessAgenda) iter.next();
			Task task = this.getTask(processagenda, normal);
			if (task != null) {
				state = task.getLocalState();
				break;
			}
		}
		return (state);
	}

	/**
	 * Changes the Task's State, from an Agent's Process Agenda, that refers to
	 * a Normal Activity.
	 */
	private Task setTaskState(Agent agent, Normal normal, String state) {

		TaskAgenda taskagenda = agent.getTheTaskAgenda();
		if (taskagenda == null) {
		}
		Collection processagendas = taskagenda.getTheProcessAgenda();
		Iterator iter = processagendas.iterator();
		while (iter.hasNext()) {
			ProcessAgenda processagenda = (ProcessAgenda) iter.next();
			Task task = this.getTask(processagenda, normal);
			if (task != null) {
				String oldLocalState = task.getLocalState();
				if (!oldLocalState.equals("Delegated")) {
					task.setLocalState(state);
				}

				// Date settings
				if (task.getLocalState().equals(Plain.READY)
						|| task.getLocalState().equals(Plain.WAITING)) {
					task.setBeginDate(null);
					task.setEndDate(null);
				} else if (oldLocalState.equals(Plain.READY)) {
					task.setBeginDate(new Date());
				} else if (task.getLocalState().equals(Plain.FINISHED)) {
					task.setEndDate(new Date());
				}

				// this.notifyAgentAbouActivityState(task);
				return task;
			}
		}
		return null;
	}

	/**
	 * Returns the Task that refers to a Normal Activity from a Process Agenda.
	 */
	private Task getTask(ProcessAgenda procAgenda, Normal normal) {

		Task theTask = null;
		Collection tasks = procAgenda.getTheTask();
		Iterator iter = tasks.iterator();
		while (iter.hasNext()) {
			Task task = (Task) iter.next();
			if (task.getTheNormal().equals(normal)) {
				theTask = task;
				break;
			}
		}
		return theTask;
	}

	@Override
	public void beginTask(String agentId, String activityId) {

		Normal actNorm = normalDAO.retrieveBySecondaryKey(activityId);

		Agent agent = agentDAO.retrieveBySecondaryKey(agentId);

		if (!this.isAgentAllocatedToActivity(agent, actNorm)) {
			//        	throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcAgt")+agentId+ //$NON-NLS-1$
			//	    	Messages.getString("facades.EnactmentEngine.UserExcIsNotReqFor")+ activityId); //$NON-NLS-1$
		}

		ProcessModel pmodel = actNorm.getTheProcessModel();
		Process process = this.getTheProcess(pmodel);
		String proc_id = process.getIdent();
		String proc_state = pmodel.getPmState();
		if (!this.isReadyForTask(proc_state)) {
			//        	throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcProcess")+proc_id+ //$NON-NLS-1$
			//		    	Messages.getString("facades.EnactmentEngine.UserExcIsNotReadyFor")); //$NON-NLS-1$
		}

		// now start the implementation of the rules

		String actState = actNorm.getTheEnactionDescription().getExclusiveStatus().name()();

		if (actState.equals(Plain.READY)) {
			if (this.isAllRequiredResourceAvailable(actNorm)) {
				this.allocateAllForActivity(actNorm, true);
				this.plainActivityHasStarted(pmodel);
				this.updateAgenda(agent, actNorm, Plain.ACTIVE, "Rule 3.1"); //$NON-NLS-1$
				EnactionDescription enact = actNorm.getTheEnactionDescription();
				enact.setStateWithMessage(Plain.ACTIVE);
				enact.setActualBegin(new Date());

				this.logging.registerGlobalActivityEvent(actNorm,
						"ToActive", "Rule 3.1"); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				// Rule 3.2
				// throw new UserException
				//				(Messages.getString("facades.EnactmentEngine.UserExcOneOrMoreReq")); //$NON-NLS-1$
			}
		} else {
			if (actState.equals(Plain.PAUSED)) {
				// restartTask
				if (this.isAllRequiredResourceAvailable(actNorm)) {
					actNorm.getTheEnactionDescription().setStateWithMessage(
							Plain.ACTIVE);

					this.updateAgenda(agent, actNorm, Plain.ACTIVE, "Rule 3.4"); //$NON-NLS-1$
					this.logging.registerGlobalActivityEvent(actNorm,
							"ToActive", "Rule 3.4"); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					// Rule 3.5
					// throw new UserException
					//					(Messages.getString("facades.EnactmentEngine.UserExcOneOrMoreReq")); //$NON-NLS-1$
				}
			} else if (actState.equals(Plain.ACTIVE)) {

				this.updateAgenda(agent, actNorm, Plain.ACTIVE, "Rule 3.3"); //$NON-NLS-1$
			}
		}

		String why = ""; //$NON-NLS-1$
		Collection conns = this.getConnectionsTo(actNorm);
		Iterator iterC = conns.iterator();
		while (iterC.hasNext()) {
			Connection conn = (Connection) iterC.next();
			if (conn instanceof JoinCon) {
				JoinCon join = (JoinCon) conn;
				String dep = joinCon.getTheDependency().getKindDep();
				if (join.getKindJoinCon().equals("OR")) { //$NON-NLS-1$
					if (dep.equals("start-start")) { //$NON-NLS-1$
						why = "Rule 8.12"; //$NON-NLS-1$
						// equals to the Rule 8.13
						break;
					}
				} else if (join.getKindJoinCon().equals("XOR")) //$NON-NLS-1$
					if (dep.equals("start-start")) { //$NON-NLS-1$
						why = "Rule 8.17"; //$NON-NLS-1$
						break;
					}
			}
		}
		this.searchForFiredConnections(actNorm.getTheProcessModel(), why);
		this.searchForReadyActivities(actNorm.getTheProcessModel());
		this.determineProcessModelStates(actNorm.getTheProcessModel());

		// Persistence Operations

		activityDAO.update(actNorm);
		agentDAO.update(agent);
	}

	/**
	 * called by APSEE Agenda. begin with rule 3.8
	 */
	@Override
	public void pauseTask(String agentId, String activityId) {
		// checks of parameters
		Object act;
		act = normalDAO.retrieveBySecondaryKey(activityId);

		Object ag;
		ag = agentDAO.retrieveBySecondaryKey(agentId);

		if (act == null) {
			//        	throw new DataBaseException(Messages.getString("facades.EnactmentEngine.DBExcActivity")+act_id+Messages.getString("facades.EnactmentEngine.DBExcDoesNotExist")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (ag == null) {
			//        	throw new DataBaseException(Messages.getString("facades.EnactmentEngine.DBExcAgent")+agent_id+Messages.getString("facades.EnactmentEngine.DBExcDoesNotExist")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		// checks related to the state of the system

		if (!(act instanceof Normal)) {
			//        	throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcActv")+act_id+ //$NON-NLS-1$
			//		    	Messages.getString("facades.EnactmentEngine.UserExcIsntANormal")); //$NON-NLS-1$
		}

		Agent agent = (Agent) ag;
		Normal actNorm = (Normal) act;
		if (!this.isAgentAllocatedToActivity(agent, actNorm)) {
			//        	throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcAgt")+agent_id+ //$NON-NLS-1$
			//	    	Messages.getString("facades.EnactmentEngine.UserExcIsNotReqFor")+ act_id); //$NON-NLS-1$
		}

		ProcessModel pmodel = actNorm.getTheProcessModel();
		Process process = this.getTheProcess(pmodel);
		String proc_id = process.getIdent();
		String proc_state = pmodel.getPmState();
		if (!this.isReadyForTask(proc_state)) {
			//        	throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcProcess")+proc_id+ //$NON-NLS-1$
			//		    	Messages.getString("facades.EnactmentEngine.UserExcIsNotReadyFor")); //$NON-NLS-1$
		}

		// now start the implementation of the rules

		if (actNorm.getTheEnactionDescription().getExclusiveStatus().name()().equals(Plain.ACTIVE)
				&& !this.isActivityPaused(actNorm)
				&& this.isLastToPause(agent, actNorm)) {

			this.releaseResourcesFromActivity(actNorm);
			actNorm.getTheEnactionDescription().setStateWithMessage(
					Plain.PAUSED);

			this.updateAgenda(agent, actNorm, Plain.PAUSED, "Rule 3.8"); //$NON-NLS-1$
			this.logging.registerGlobalActivityEvent(actNorm,
					"ToPaused", "Rule 3.8"); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			String taskState = this.getTaskState(agent, actNorm);
			if (taskState != null && !taskState.equals("")
					&& !taskState.equals(Plain.PAUSED))
				this.updateAgenda(agent, actNorm, Plain.PAUSED, "Rule 3.9"); //$NON-NLS-1$
		}

		// Persistence Operations

		normalDAO.update(actNorm);
		agentDAO.update(agent);

	}

	public void pauseActiveTasks(String agentIdent) {

		String hql = "select task.theNormal.ident from "
				+ TASK_CLASSNAME
				+ " as task "
				+ "where (task.theProcessAgenda.theTaskAgenda.theAgent.ident=:agentID) and (task.localState='Active') ";

		query = taskDAO.getPersistenceContext().createQuery(hql);
		query.setParameter("agentID", agentIdent);

		List result = query.getResultList();
		Iterator resultIterator = result.iterator();

		while (resultIterator.hasNext()) {
			String ident = (String) resultIterator.next();
			this.pauseTask(agentIdent, ident);
		}
	}

	/**
	 * called by APSEE Agenda. begin with rule 3.6 And the rules 9.1, 9.2
	 */
	@Override
	public void finishTask(String agentId, String activityId) {

		// checks of parameters
		Object act;
		act = normalDAO.retrieveBySecondaryKey(activityId);

		Object ag;
		ag = agentDAO.retrieveBySecondaryKey(agentId);

		if (act == null) {
		}
		if (ag == null) {
		}

		// checks related to the state of the system

		if (!(act instanceof Normal)) {
		}

		Agent agent = agentDAO.retrieveBySecondaryKey(agentId);
		Normal actNorm = normalDAO.retrieveBySecondaryKey(activityId);
		if (!this.isAgentAllocatedToActivity(agent, actNorm)) {
		}

		ProcessModel pmodel = actNorm.getTheProcessModel();
		Process process = this.getTheProcess(pmodel);
		String proc_id = process.getIdent();
		String proc_state = pmodel.getPmState();
		if (!this.isReadyForTask(proc_state)) {
		}

		// now start the implementation of the rules
		String why = "";
		String state = actNorm.getTheEnactionDescription().getExclusiveStatus().name()();

		if (state.equals(Plain.ACTIVE)) {

			try {
				if (!this.isActivityFinished(actNorm)
						&& this.isLastToFinish(agent, actNorm)) {

					if (!this.canFinish(actNorm)) {
					}

					boolean feedbackTaken = false;

					if (isFeedbackSource(actNorm)) {
						feedbackTaken = this.executeFeedbacks(actNorm);
					}

					// if there was no feedback with a true condition, finish
					// this task
					if (!feedbackTaken) {

						this.releaseResourcesFromActivity(actNorm);
						actNorm.getTheEnactionDescription()
								.setStateWithMessage(Plain.FINISHED);
						actNorm.getTheEnactionDescription().setActualEnd(
								new Date());
						this.updateAgenda(agent, actNorm, Plain.FINISHED,
								"Rule 3.6"); //$NON-NLS-1$
						this.logging.registerGlobalActivityEvent(actNorm,
								"ToFinished", "Rule 3.6"); //$NON-NLS-1$ //$NON-NLS-2$

						this.activityHasFinished(actNorm);

						Collection conns = this.getConnectionsTo(actNorm);
						Iterator iterC = conns.iterator();
						while (iterC.hasNext()) {
							Connection conn = (Connection) iterC.next();
							if (conn instanceof JoinCon) {
								JoinCon join = (JoinCon) conn;
								String dep = joinCon.getTheDependency()
										.getKindDep();
								if (join.getKindJoinCon().equals("OR")) { //$NON-NLS-1$
									if (dep.equals("end-start") || //$NON-NLS-1$
											dep.equals("end-end")) { //$NON-NLS-1$
										why = "Rule 8.2"; //$NON-NLS-1$
										break;
									} else {
										why = "Rule 8.12"; //$NON-NLS-1$
										// equals to Rule 8.13
										break;
									}
								} else if (join.getKindJoinCon().equals("XOR")) //$NON-NLS-1$
									if (dep.equals("end-start") || //$NON-NLS-1$
											dep.equals("end-end")) { //$NON-NLS-1$
										why = "Rule 8.6"; //$NON-NLS-1$
										break;
									} else {
										why = "Rule 8.17"; //$NON-NLS-1$
										// equals to Rule 8.13
										break;
									}
							}
						}
					}
				} else if (!state.equals(Plain.FINISHED)) {

					this.updateAgenda(agent, actNorm, Plain.FINISHED,
							"Rule 3.7"); //$NON-NLS-1$
				}
			} catch (WebapseeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.searchForFiredConnections(pmodel, why);
		this.searchForReadyActivities(pmodel);
		this.determineProcessModelStates(pmodel); // to solve upper propagation
													// problems

		// Persistence Operations

		normalDAO.update(actNorm);
		agentDAO.update(agent);
	}

	protected void finishTask(Normal actNorm) throws WebapseeException,
			DAOException {

		// checks of parameters

		if (actNorm == null) {
		}

		// checks related to the state of the system

		ProcessModel pmodel = actNorm.getTheProcessModel();
		Process process = this.getTheProcess(pmodel);
		String proc_id = process.getIdent();
		String proc_state = pmodel.getPmState();
		if (!this.isReadyForTask(proc_state)) {
		}

		// now start the implementation of the rules
		String why = "";
		String state = actNorm.getTheEnactionDescription().getExclusiveStatus().name()();
		if (state.equals(Plain.ACTIVE)) {

			if (!this.canFinish(actNorm)) {
			}

			boolean feedbackTaken = false;

			if (isFeedbackSource(actNorm)) {
				feedbackTaken = this.executeFeedbacks(actNorm);
			}

			// if there was no feedback with a true condition, finish this task
			if (!feedbackTaken) {
				this.releaseResourcesFromActivity(actNorm);
				actNorm.getTheEnactionDescription().setStateWithMessage(
						Plain.FINISHED);
				actNorm.getTheEnactionDescription().setActualEnd(new Date());
				this.logging.registerGlobalActivityEvent(actNorm,
						"ToFinished", "Rule 3.6"); //$NON-NLS-1$ //$NON-NLS-2$

				this.activityHasFinished(actNorm);

				Collection conns = this.getConnectionsTo(actNorm);
				Iterator iterC = conns.iterator();
				while (iterC.hasNext()) {
					Connection conn = (Connection) iterC.next();
					if (conn instanceof JoinCon) {
						JoinCon join = (JoinCon) conn;
						String dep = joinCon.getTheDependency().getKindDep();
						if (join.getKindJoinCon().equals("OR")) { //$NON-NLS-1$
							if (dep.equals("end-start") || //$NON-NLS-1$
									dep.equals("end-end")) { //$NON-NLS-1$
								why = "Rule 8.2"; //$NON-NLS-1$
								break;
							} else {
								why = "Rule 8.12"; //$NON-NLS-1$
								// equals to Rule 8.13
								break;
							}
						} else if (join.getKindJoinCon().equals("XOR")) //$NON-NLS-1$
							if (dep.equals("end-start") || //$NON-NLS-1$
									dep.equals("end-end")) { //$NON-NLS-1$
								why = "Rule 8.6"; //$NON-NLS-1$
								break;
							} else {
								why = "Rule 8.17"; //$NON-NLS-1$
								// equals to Rule 8.13
								break;
							}
					}
				}
			}
		}

		this.searchForFiredConnections(pmodel, why);
		this.searchForReadyActivities(pmodel);
	}

	/**
	 * called by APSEE Agenda. begin with rule 3.10
	 *
	 * @return
	 */
	public boolean delegateTask(String from_agent_id, String act_id,
			String to_agent_id) {

		// checks of parameters
		Object act;
		act = normalDAO.retrieveBySecondaryKey(act_id);

		Object from_ag;
		Object to_ag;

		from_ag = agentDAO.retrieveBySecondaryKey(from_agent_id);

		to_ag = agentDAO.retrieveBySecondaryKey(to_agent_id);

		if (act == null) {
			//        	throw new DataBaseException(Messages.getString("facades.EnactmentEngine.DBExcActivity")+act_id+Messages.getString("facades.EnactmentEngine.DBExcDoesNotExist")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (from_ag == null) {
			//        	throw new DataBaseException(Messages.getString("facades.EnactmentEngine.DBExcAgent")+from_agent_id+Messages.getString("facades.EnactmentEngine.DBExcDoesNotExist")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (to_ag == null) {
			//        	throw new DataBaseException(Messages.getString("facades.EnactmentEngine.DBExcAgent")+to_agent_id+Messages.getString("facades.EnactmentEngine.DBExcDoesNotExist")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		// checks related to the state of the system

		if (!(act instanceof Normal)) {
			//        	throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcActv")+act_id+ //$NON-NLS-1$
			//		    	Messages.getString("facades.EnactmentEngine.UserExcIsntANormal")); //$NON-NLS-1$
		}

		Agent from_agent = (Agent) from_ag;
		Agent to_agent = (Agent) to_ag;
		Normal actNorm = (Normal) act;
		if (!this.isAgentAllocatedToActivity(from_agent, actNorm)) {
			//        	throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcAgt")+from_agent_id+ //$NON-NLS-1$
			//	    	Messages.getString("facades.EnactmentEngine.UserExcIsNotReqFor")+ act_id); //$NON-NLS-1$
		}

		if (this.isAgentAllocatedToActivity(to_agent, actNorm)) {
			// throw new
			// UserException(Messages.getString("facades.EnactmentEngine.UserExcAgent")+to_agent_id+
			// Messages.getString("facades.EnactmentEngine.UserExcIsAlrAlloc")+
			// act_id);
		}

		ProcessModel pmodel = actNorm.getTheProcessModel();
		Process process = this.getTheProcess(pmodel);
		String proc_id = process.getIdent();
		String proc_state = pmodel.getPmState();
		if (!this.isReadyForTask(proc_state)) {
			//        	throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcProcess")+proc_id+ //$NON-NLS-1$
			//		    	Messages.getString("facades.EnactmentEngine.UserExcIsNotReadyFor")); //$NON-NLS-1$
		}

		// now start the implementation of the rules

		String state = actNorm.getTheEnactionDescription().getExclusiveStatus().name()();
		RequiredPeople req = null;
		if (state.equals(Plain.WAITING) && this.hasInvolvedAgents(actNorm)) {
			req = this.delegateTaskToAgent(from_agent, actNorm, to_agent);
			this.updateAgenda(from_agent, actNorm, "Delegated", "Rule 3.10"); //$NON-NLS-1$ //$NON-NLS-2$
			this.updateAgenda(to_agent, actNorm, Plain.WAITING, "Rule 3.10"); //$NON-NLS-1$

			Collection tasks = actNorm.getTheTasks();
			Iterator iter = tasks.iterator();
			while (iter.hasNext()) {
				Task task = (Task) iter.next();
				if (task.getTheNormal().equals(actNorm)) {
					this.logging.registerAgendaEvent(task,
							"ToDelegated", "Rule 3.10"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		} else if (state.equals(Plain.READY) && this.hasInvolvedAgents(actNorm)) {
			req = this.delegateTaskToAgent(from_agent, actNorm, to_agent);
			this.updateAgenda(from_agent, actNorm, "Delegated", "Rule 3.11"); //$NON-NLS-1$ //$NON-NLS-2$
			this.updateAgenda(to_agent, actNorm, Plain.READY, "Rule 3.11"); //$NON-NLS-1$

			Collection tasks = actNorm.getTheTasks();
			Iterator iter = tasks.iterator();
			while (iter.hasNext()) {
				Task task = (Task) iter.next();
				if (task.getTheNormal().equals(actNorm)) {
					this.logging.registerAgendaEvent(task,
							"ToDelegated", "Rule 3.11"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		} else {
			//        	throw new UserException(Messages.getString("facades.EnactmentEngine.UserExcActv") + actNorm.getIdent() + Messages.getString("facades.EnactmentEngine.UserExcCanNotBeDel")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		if (req != null) {
			String message = "<MESSAGE>" + //$NON-NLS-1$
					"<NOTIFY>" + "<OID>" + req.getId() + "</OID>" + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					"<TYPE>UPT</TYPE>" + //$NON-NLS-1$
					"<CLASS>" + req.getClass().getName() + "</CLASS>" + //$NON-NLS-1$ //$NON-NLS-2$
					"<BY>" + from_agent_id + "</BY>" + //$NON-NLS-1$ //$NON-NLS-2$
					"</NOTIFY>" + //$NON-NLS-1$
					"</MESSAGE>"; //$NON-NLS-1$
		}
		// Persistence Operations

		normalDAO.update(actNorm);
		agentDAO.update(from_agent);
		agentDAO.update(to_agent);
		try {
			// Mail - para gerente qdo atv for delegada
			// this.sendMail.sendDeleAct(currentSession,act_id,process.getIdent(),actNorm.getName(),from_agent_id,to_agent_id);
			// End Mail
		} catch (Exception we) {
			// we.printStackTrace();
		}

		return true;
	}

	/**
	 * Adds a Task that refers to a Normal Activity to an Agent.
	 */
	private Task addTaskToAgent(Agent agent, Normal actNorm) {

		Task returnTask = null;
		TaskAgenda taskAgenda = agent.getTheTaskAgenda();

		Collection<ProcessAgenda> procAgendas = taskAgenda
				.getTheProcessAgenda();
		procAgendas.remove(null);
		if (procAgendas.isEmpty()) {

			ProcessAgenda procAg = new ProcessAgenda();
			Process pro = this.getTheProcess(actNorm.getTheProcessModel());
			procAg.setTheProcess(pro);
			pro.getTheProcessAgenda().add(procAg);
			procAg.setTheTaskAgenda(taskAgenda);
			procAgendas.add(procAg);
			returnTask = this.addTask(procAg, actNorm);
		} else {
			Iterator iter = procAgendas.iterator();
			boolean has = false;
			while (iter.hasNext()) {
				ProcessAgenda procAgenda = (ProcessAgenda) iter.next();
				if (procAgenda.getTheProcess().equals(
						this.getTheProcess(actNorm.getTheProcessModel()))) {
					returnTask = this.addTask(procAgenda, actNorm);
					has = true;
					break;
				}
			}
			if (!has) {

				ProcessAgenda procAg = new ProcessAgenda();
				Process pro = this.getTheProcess(actNorm.getTheProcessModel());
				procAg.setTheProcess(pro);
				pro.getTheProcessAgenda().add(procAg);
				procAg.setTheTaskAgenda(taskAgenda);
				procAgendas.add(procAg);
				returnTask = this.addTask(procAg, actNorm);
			}
		}

		taskAgenda.setTheProcessAgenda(procAgendas);

		return returnTask;
	}

	public Task addTask(ProcessAgenda processAgenda, Normal actNorm) {

		Collection tasks = processAgenda.getTheTask();
		Iterator iter = tasks.iterator();
		boolean has = false;
		Task returnTask = null;
		while (iter.hasNext()) {
			Task taskAux = (Task) iter.next();
			if (taskAux.getTheNormal().getIdent().equals(actNorm.getIdent())) {
				returnTask = taskAux;
				has = true;
				break;
			}
		}

		if (!has) {
			Task task = new Task();
			task.setTheNormal(actNorm);
			actNorm.getTheTasks().add(task);
			task.setTheProcessAgenda(processAgenda);
			processAgenda.getTheTask().add(task);

			task = (Task) taskDAO.daoSave(task);

			returnTask = task;
		}
		return returnTask;
	}



	/**
	 * Delegates a Task from an Agent to another one, adjusting their Task
	 * Agendas.
	 */
	private RequiredPeople delegateTaskToAgent(Agent from_agent,
			Normal actNorm, Agent to_agent) {
		Process process = this.getTheProcess(actNorm.getTheProcessModel());
		String process_id = process.getIdent();

		Task from_task = null;
		RequiredPeople req = null;

		Collection reqP = actNorm.getTheRequiredPeople();
		Iterator iter = reqP.iterator();
		while (iter.hasNext()) {
			RequiredPeople reqPeople = (RequiredPeople) iter.next();
			if (reqPeople instanceof ReqAgent) {
				ReqAgent reqAgent = (ReqAgent) reqPeople;
				if (reqAgent != null) {
					Agent agent = reqAgent.getTheAgent();
					if (agent != null) {
						if (agent.equals(from_agent)) {
							req = reqAgent;
							// Removing Required Agent
							from_agent.removeFromTheReqAgent(reqAgent);
							// Adding Required Agent
							reqAgent.setTheAgent(to_agent);
							to_agent.insertIntoTheReqAgent(reqAgent);
							break;
						}
					}
				}
			}
			/*
			 * else{ // is ReqWorkGroup NOT ALLOWED }
			 */}
		Collection from_procAgendas = from_agent.getTheTaskAgenda()
				.getTheProcessAgenda();

		Iterator from_iter = from_procAgendas.iterator();
		while (from_iter.hasNext()) {
			ProcessAgenda from_procAgenda = (ProcessAgenda) from_iter.next();
			if (from_procAgenda.getTheProcess().getIdent().equals(process_id)) {
				from_task = this.getTask(from_procAgenda, actNorm);
				if (from_task != null)
					break;
			}
		}
		System.out.println("nome agent " + to_agent.getName());
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

	private void notifyAgentAbouActivityState(Task task) {
		// Notify the agente of the new state of the task!!
		String message = "<MESSAGE>" + //$NON-NLS-1$
				"<ACTIVITYSTATE>" + //$NON-NLS-1$
				"<OID>" + task.getId() + "</OID>" + //$NON-NLS-1$ //$NON-NLS-2$
				"<CLASS>" + task.getClass().getName() + "</CLASS>" + //$NON-NLS-1$ //$NON-NLS-2$
				"<ID>" + task.getTheNormal().getIdent() + "</ID>" + //$NON-NLS-1$ //$NON-NLS-2$
				"<NEWSTATE>" + task.getLocalState() + "</NEWSTATE>" + //$NON-NLS-1$ //$NON-NLS-2$
				"<BY>APSEE_Manager</BY>" + //$NON-NLS-1$
				"</ACTIVITYSTATE>" + //$NON-NLS-1$
				"</MESSAGE>"; //$NON-NLS-1$
		// try {
		// if(this.remote==null){
		//
		// }
		// if(this.remote!=null){
		// Agent agent =
		// task.getTheProcessAgenda().getTheTaskAgenda().getTheAgent();
		// this.remote.sendMessageToUser(message, agent.getIdent());
		// }
		// // e.printStackTrace();
		//           	 System.out.println("EnactmentEngine.updateAgenda() remote reference exception"); //$NON-NLS-1$
		// }
	}

	/**
	 * called by APSEE Manager. Applies to the rules G10.1 to G10.10 (Determines
	 * the Process Model's state according to the state its Activities).
	 */
	@Override
	public void determineProcessModelStates(ProcessModel processModel) {

		String currstate = processModel.getPmState();

		Decomposed dec = processModel.getTheDecomposed();

		if (currstate.equals(ProcessModel.FINISHED))
			return; // already finished

		Collection activities = processModel.getTheActivity();
		Iterator iter = activities.iterator();

		String[] types = new String[activities.size()]; // Type of activity
		String[] states = new String[activities.size()]; // A_st

		int n = 0;
		while (iter.hasNext()) {
			Activity act = (Activity) iter.next();
			if (act instanceof Decomposed) {
				Decomposed fragment = (Decomposed) act;
				String fstate = fragment.getTheReferedProcessModel()
						.getPmState();
				// here process model states are stored
				types[n] = "Decomposed"; //$NON-NLS-1$
				states[n] = fstate;
			} else if (act instanceof Automatic) {
				Automatic automatic = (Automatic) act;
				types[n] = "Automatic"; //$NON-NLS-1$
				states[n] = automatic.getTheEnactionDescription().getExclusiveStatus().name()();
			} else if (act instanceof Normal) {
				Normal normal = (Normal) act;
				Collection reqPeople = normal.getTheRequiredPeople();
				Iterator iter2 = reqPeople.iterator();
				int reqAgentSize = 0;
				int reqWorkGroupSize = 0;
				while (iter2.hasNext()) {
					RequiredPeople reqP = (RequiredPeople) iter2.next();
					if (reqP instanceof ReqAgent)
						reqAgentSize++;
					else
						reqWorkGroupSize++;
				}
				if (reqAgentSize > 0 || reqWorkGroupSize > 0)
					types[n] = "NormalWithAgent"; //$NON-NLS-1$
				else
					types[n] = "NormalWithoutAgent"; //$NON-NLS-1$
				states[n] = normal.getTheEnactionDescription().getExclusiveStatus().name()();
			} else {
				types[n] = "Undefined"; //$NON-NLS-1$
				states[n] = "Undefined"; //$NON-NLS-1$
			}
			n++;
		}

		// Dynamic rules 10.1, 10.2
		boolean hasOnlyRequirements = true;
		for (int i = 0; i < states.length; i++) {
			String state = states[i];
			if (!state.equals(ProcessModel.REQUIREMENTS)) {
				hasOnlyRequirements = false;
				break;
			}
		}
		if (hasOnlyRequirements) {
			if (!currstate.equals(ProcessModel.REQUIREMENTS)) {
				processModel.setPmState(ProcessModel.REQUIREMENTS);
				this.logging.registerProcessModelEvent(processModel,
						"ToRequirements", "Rules 10.1 and 10.2"); //$NON-NLS-1$ //$NON-NLS-2$
				Decomposed superActDec = processModel.getTheDecomposed();
				if (superActDec == null) {
					return;
				} else {
					this.determineProcessModelStates(superActDec
							.getTheProcessModel());
				}
			}
			return;
		}

		// Dynamic rule 10.3 (added waiting, ready, paused)
		boolean hasAnyEnacted = false;
		String auxType = ""; //$NON-NLS-1$
		for (int i = 0; i < states.length; i++) {
			String state = states[i];
			auxType = types[i];
			if (state.equals(ProcessModel.ENACTING)
					|| state.equals(Plain.ACTIVE) || state.equals(Plain.PAUSED)) {
				hasAnyEnacted = true;
				break;
			}
		}
		if (hasAnyEnacted) {
			if (!currstate.equals(ProcessModel.ENACTING)) {
				processModel.setPmState(ProcessModel.ENACTING);
				this.logging.registerProcessModelEvent(processModel,
						"ToEnacting", "Rule 10.3"); //$NON-NLS-1$ //$NON-NLS-2$
				if (!auxType.equals("Decomposed")) //$NON-NLS-1$
					this.logging.registerProcessModelEvent(processModel,
							"ToEnacting", "Rule 4.3"); //$NON-NLS-1$ //$NON-NLS-2$
				else
					this.logging.registerProcessModelEvent(processModel,
							"ToEnacting", "Rule 4.5"); //$NON-NLS-1$ //$NON-NLS-2$
				Decomposed superActDec = processModel.getTheDecomposed();
				if (superActDec == null) {
					return;
				} else {
					this.determineProcessModelStates(superActDec
							.getTheProcessModel());
				}
			}
			return;
		}

		// Dynamic rule 10.9
		boolean allFailed = true;
		for (int i = 0; i < states.length; i++) {
			String state = states[i];
			if (!(state.equals(ProcessModel.FAILED) || state
					.equals(Plain.FAILED))) {
				allFailed = false;
				break;
			}
		}
		if (allFailed) {
			if (!currstate.equals(ProcessModel.FAILED)) {
				processModel.setPmState(ProcessModel.FAILED);
				this.logging.registerProcessModelEvent(processModel,
						"ToFailed", "Rule 10.9"); //$NON-NLS-1$ //$NON-NLS-2$
				Decomposed superActDec = processModel.getTheDecomposed();
				if (superActDec == null) {
					return;
				} else {
					this.failPropagation(superActDec);
					this.determineProcessModelStates(superActDec
							.getTheProcessModel());
				}
			}
			return;
		}

		// Dynamic rule 10.10
		boolean allCanceled = true;
		for (int i = 0; i < states.length; i++) {
			String state = states[i];
			if (!(state.equals(ProcessModel.CANCELED) || state
					.equals(Plain.CANCELED))) {
				allCanceled = false;
				break;
			}
		}
		if (allCanceled) {
			if (!currstate.equals(ProcessModel.CANCELED)) {
				processModel.setPmState(ProcessModel.CANCELED);
				this.logging.registerProcessModelEvent(processModel,
						"ToCanceled", "Rules G10.10"); //$NON-NLS-1$ //$NON-NLS-2$
				Decomposed superActDec = processModel.getTheDecomposed();
				if (superActDec == null) {
					return;
				} else {
					this.cancelPropagation(superActDec);
					this.determineProcessModelStates(superActDec
							.getTheProcessModel());
				}
			}
			return;
		}

		// Dynamic rules 10.4, 10.5
		boolean hasOnlyAbstracts = true, isAbstract = true;
		for (int i = 0; i < states.length; i++) {
			String type = types[i];
			String state = states[i];
			if (!state.equals(ProcessModel.ABSTRACT)) {
				hasOnlyAbstracts = false;
				isAbstract = false;
				break;
			}
			if (isAbstract
					&& (state.equals(ProcessModel.ENACTING)
							|| state.equals(ProcessModel.INSTANTIATED)
							|| type.equals("Automatic") || type
								.equals("NormalWithAgent"))) { //$NON-NLS-1$
				isAbstract = false;
				break;
			}
		}
		if (hasOnlyAbstracts || isAbstract) {
			if (!currstate.equals(ProcessModel.ABSTRACT)) {
				processModel.setPmState(ProcessModel.ABSTRACT);
				this.logging.registerProcessModelEvent(processModel,
						"ToAbstract", "Rules 10.4 and 10.5"); //$NON-NLS-1$ //$NON-NLS-2$
				Decomposed superActDec = processModel.getTheDecomposed();
				if (superActDec == null) {
					return;
				} else {
					this.determineProcessModelStates(superActDec
							.getTheProcessModel());
				}
			}
			return;
		}

		// Dynamic rules 10.6, 10.7, 10.8
		boolean hasAnyInstant = false, hasAnyEnacting = false, hasAnyNormal = false, hasAnyAutomatic = false;
		for (int i = 0; i < states.length; i++) {
			String state = states[i];
			String type = types[i];
			if (state.equals(ProcessModel.INSTANTIATED)) {
				hasAnyInstant = true;
			} else if (state.equals(ProcessModel.ENACTING)
					|| state.equals(Plain.ACTIVE)) {
				hasAnyEnacting = true;
			}
			if (type.equals("NormalWithAgent")) //$NON-NLS-1$
				hasAnyNormal = true;
			else if (type.equals("Automatic")) //$NON-NLS-1$
				hasAnyAutomatic = true;
		}
		if (!hasAnyEnacting
				&& (hasAnyInstant || hasAnyNormal || hasAnyAutomatic)) {
			// Condition added (currstate.equals (ProcessModel.ENACTING)) to
			// avoid state regression.
			// In other words, a process model must not go back from ENACTING to
			// INSTANTIATED!
			if (!currstate.equals(ProcessModel.INSTANTIATED)
					&& !currstate.equals(ProcessModel.ENACTING)) {

				processModel.setPmState(ProcessModel.INSTANTIATED);
				if (hasAnyInstant)
					this.logging.registerProcessModelEvent(processModel,
							"ToInstantiated", "Rule 10.6"); //$NON-NLS-1$ //$NON-NLS-2$
				else if (hasAnyNormal)
					this.logging.registerProcessModelEvent(processModel,
							"ToInstantiated", "Rule 10.7"); //$NON-NLS-1$ //$NON-NLS-2$
				else if (hasAnyAutomatic)
					this.logging.registerProcessModelEvent(processModel,
							"ToInstantiated", "Rule 10.8"); //$NON-NLS-1$ //$NON-NLS-2$
				Decomposed superActDec = processModel.getTheDecomposed();
				if (superActDec == null) {
					return;
				} else {
					this.determineProcessModelStates(superActDec
							.getTheProcessModel());
				}
			}
		}

		// Dynamic rule 10.9
		boolean allFinished = true;
		for (int i = 0; i < states.length; i++) {
			String state = states[i];
			if (!(state.equals(ProcessModel.FINISHED) || state
					.equals(Plain.FINISHED))
					&& !(state.equals(ProcessModel.FAILED) || state
							.equals(Plain.FAILED))
					&& !(state.equals(ProcessModel.CANCELED) || state
							.equals(Plain.CANCELED))) {
				allFinished = false;
				break;
			}
		}
		if (allFinished) {
			if (!currstate.equals(ProcessModel.FINISHED)) {
				processModel.setPmState(ProcessModel.FINISHED);
				this.logging.registerProcessModelEvent(processModel,
						"ToFinished", "Rule 10.x"); //$NON-NLS-1$ //$NON-NLS-2$
				Decomposed superActDec = processModel.getTheDecomposed();
				if (superActDec == null) {
					this.rootModelHasFinished(processModel);
					return;
				} else {
					this.searchForFiredConnections(
							superActDec.getTheProcessModel(), "Rule 10.x");
					this.searchForReadyActivities(superActDec
							.getTheProcessModel());
					this.determineProcessModelStates(superActDec
							.getTheProcessModel());
				}
			}
			return;
		}
	}

	/**
	 * Determine if an Activity can finish, according to the Connections.
	 */
	private boolean canFinish(Activity act) {

		boolean actCanFinish = true;

		Collection connections = this.getConnectionsFrom(act);

		Collection actsFrom = new HashSet();

		Iterator iter = connections.iterator();
		while (iter.hasNext()) {
			Connection conn = (Connection) iter.next();
			if (conn instanceof Sequence) {
				Sequence seq = (Sequence) conn;
				Dependency dep = seq.getTheDependency();
				Activity fromActivity = seq.getFromActivity();
				int state = this.simpleDepConnState(dep, fromActivity);
				if ((state & TO_ACT_CAN_FINISH) == 0) {
					actCanFinish = false;
					break;
				}
			} else if (conn instanceof MultipleCon) {
				MultipleCon multi = (MultipleCon) conn;
				Dependency dep = null;
				if (multi instanceof JoinCon) {
					JoinCon join = (JoinCon) multi;
					Collection preds = this.getPredecessors(joinCon);
					Iterator iterPreds = preds.iterator();
					while (iterPreds.hasNext()) {
						Object predecessor = (Object) iterPreds.next();
						if (predecessor instanceof Activity)
							actsFrom.add(predecessor);
					}
					dep = joinCon.getTheDependency();
				} else if (multi instanceof BranchCon) {
					BranchCon branch = (BranchCon) multi;
					Collection preds = this.getPredecessors(branchCon);
					Iterator iterPreds = preds.iterator();
					while (iterPreds.hasNext()) {
						Object predecessor = (Object) iterPreds.next();
						if (predecessor instanceof Activity)
							actsFrom.add(predecessor);
					}

					dep = branchCon.getTheDependency();
				} else
					continue;

				String depType = dep.getKindDep();
				if (depType.equals("end-end")) {
					boolean depOk = true;
					Iterator iterActsFrom = actsFrom.iterator();
					while (iterActsFrom.hasNext()) {
						Activity actFrom = (Activity) iterActsFrom.next();
						String state = this.getExclusiveStatus().name()(actFrom);
						boolean isPlain = actFrom instanceof Plain;
						depOk = this.isDepSatisfiedToFinish(depType, state,
								isPlain);
						if (!depOk)
							break;
					}
					if (!multi.isFired().booleanValue() || !depOk) {
						actCanFinish = false;
						break;
					}
				}
			}
		}

		if (actCanFinish) {
			if (!this.areDependenciesOKToFinish(act, act.getTheProcessModel())) {
				actCanFinish = false;
			}
		}
		return actCanFinish;
	}

	/**
	 * Determines the state of a dependency based on the Activity the cames
	 * before it.
	 */
	private int simpleDepConnState(Dependency depConn, Activity fromAct) {
		String depType = depConn.getKindDep();
		boolean isPlain = fromAct instanceof Plain;
		String state;
		if (isPlain) {
			Plain plain = (Plain) fromAct;
			state = plain.getTheEnactionDescription().getExclusiveStatus().name()();
		} else {
			Decomposed dec = (Decomposed) fromAct;
			state = dec.getTheReferedProcessModel().getPmState();
		}
		return (this.depConnState(depType, state, isPlain));
	}

	/**
	 * Calculates the state of an connection based on the Constants defined
	 * above (TO_ACT_CAN_START and TO_ACT_CAN_FINISH).
	 */
	private int depConnState(String depType, String state, boolean isPlain) {
		int result = 0;
		if (isPlain) {
			if (depType.equals("end-start")) { //$NON-NLS-1$
				result |= state.equals(Plain.FINISHED) ? TO_ACT_CAN_START : 0;
				result |= TO_ACT_CAN_FINISH;
			} else if (depType.equals("start-start")) { //$NON-NLS-1$
				result |= (state.equals(Plain.ACTIVE)
						|| state.equals(Plain.PAUSED) || state
						.equals(Plain.FINISHED)) ? TO_ACT_CAN_START : 0;
				result |= TO_ACT_CAN_FINISH;
			} else if (depType.equals("end-end")) { //$NON-NLS-1$
				result |= TO_ACT_CAN_START;
				result |= state.equals(Plain.FINISHED) ? TO_ACT_CAN_FINISH : 0;
			}
		} else {
			if (depType.equals("end-start")) { //$NON-NLS-1$
				result |= state.equals(ProcessModel.FINISHED) ? TO_ACT_CAN_START
						: 0;
				result |= TO_ACT_CAN_FINISH;
			} else if (depType.equals("start-start")) { //$NON-NLS-1$
				result |= (state.equals(ProcessModel.ENACTING) || state
						.equals(ProcessModel.FINISHED)) ? TO_ACT_CAN_START : 0;
				result |= TO_ACT_CAN_FINISH;
			} else if (depType.equals("end-end")) { //$NON-NLS-1$
				result |= TO_ACT_CAN_START;
				result |= state.equals(ProcessModel.FINISHED) ? TO_ACT_CAN_FINISH
						: 0;
			}
		}
		return (result);
	}

	private boolean areDependenciesOKToFinish(Activity act,
			ProcessModel procModel) {

		if (!this.isTheRootProcess(procModel)) {

			// Check if it is the last activity to finish
			if (this.isActivityLastToFinish(act, procModel)) {
				// The NACs from the rule 4.2 must be checked.
				if (this.areNACsOk(procModel)) {
					Decomposed actDec = procModel.getTheDecomposed();
					return this.areDependenciesOKToFinish(actDec,
							actDec.getTheProcessModel());
				} else
					return false;
			} else {
				// If it is plain, predecessors at same process model were
				// already checked in canFinish.
				if (act instanceof Plain)
					return true;
				// It is decomposed, the NACs from the rule 4.2 must be checked.
				if (this.areNACsOk(procModel)) {
					Decomposed actDec = procModel.getTheDecomposed();
					return this.areDependenciesOKToFinish(actDec,
							actDec.getTheProcessModel());
				} else
					return false;
			}
		}
		return true;
	}

	private boolean isActivityLastToFinish(Activity act, ProcessModel procModel) {

		Collection acts = procModel.getTheActivity();
		Iterator iter = acts.iterator();
		while (iter.hasNext()) {
			Activity activity = (Activity) iter.next();
			if (activity.equals(act))
				continue;

			String state = this.getExclusiveStatus().name()(activity);
			if (!(state.equals(Plain.FINISHED) || state
					.equals(ProcessModel.FINISHED))
					&& !(state.equals(Plain.FAILED) || state
							.equals(ProcessModel.FAILED))
					&& !(state.equals(Plain.CANCELED) || state
							.equals(ProcessModel.CANCELED))) {

				return false;
			}
		}
		return true;
	}

	private boolean areNACsOk(ProcessModel procModel) {
		boolean ret = true;

		// NACs from the rule 4.2 must be checked.
		Decomposed actDec = procModel.getTheDecomposed();
		Collection froms = this.getConnectionsFrom(actDec);
		Iterator iterfroms = froms.iterator();
		while (iterfroms.hasNext()) {
			Connection conn = (Connection) iterfroms.next();
			if (conn instanceof Sequence) { // Rule 4.2 NAC-04
				Sequence seq = (Sequence) conn;
				Activity from = seq.getFromActivity();
				String state = this.getExclusiveStatus().name()(from);
				if (seq.getTheDependency().getKindDep().equals("end-end")
						&& (!(state.equals(Plain.FINISHED) || state
								.equals(ProcessModel.FINISHED)))) {
					ret = false;
					break;
				}
			} else if (conn instanceof BranchANDCon) { // Rule 4.2 NAC-01
				BranchANDCon bAND = (BranchANDCon) conn;
				if (!bAND.isFired().booleanValue()
						&& bAND.getTheDependency().getKindDep()
								.equals("end-end")) {
					ret = false;
					break;
				}
			} else if (conn instanceof BranchConCond) {
				BranchConCond bCond = (BranchConCond) conn;
				if (bCond.getTheDependency().getKindDep().equals("end-end")) {
					if (!bCond.isFired().booleanValue()) { // Rule 4.2 NAC-02
						ret = false;
						break;
					} else { // Rule 4.2 NAC-03
						Collection bctas = bCond.getTheBranchConCondToActivity();
						Iterator iterBctas = bctas.iterator();
						while (iterBctas.hasNext()) {
							BranchConCondToActivity bcta = (BranchConCondToActivity) iterBctas
									.next();
							if (bcta.getTheActivity().equals(actDec)
									&& !this.conditionValue(bcta
											.getTheCondition())) {
								ret = false;
								break;
							}
						}
						if (!ret)
							break;
					}
				}
			} else if (conn instanceof JoinCon) { // Rule 4.2 NAC-01
				JoinCon join = (JoinCon) conn;
				if (!joinCon.isFired().booleanValue()
						&& joinCon.getTheDependency().getKindDep()
								.equals("end-end")) { //$NON-NLS-1$
					ret = false;
					break;
				}
			}
		}
		return ret;
	}

	/**
	 * Determine that a Plain Activity has started, propagating to the Process
	 * Model.
	 */
	private void plainActivityHasStarted(ProcessModel pmodel) {

		ProcessModel parentModel = pmodel;
		String why = ""; //$NON-NLS-1$

		if (pmodel.getPmState().equals(ProcessModel.INSTANTIATED)) {
			pmodel.setPmState(ProcessModel.ENACTING);
			if (!this.isTheRootProcess(pmodel)) {
				this.logging.registerProcessModelEvent(pmodel,
						"ToEnacting", "Rule 4.4"); //$NON-NLS-1$ //$NON-NLS-2$
				parentModel = pmodel.getTheDecomposed().getTheProcessModel();
				Collection conns = this.getConnectionsTo(pmodel
						.getTheDecomposed());
				Iterator iterC = conns.iterator();
				while (iterC.hasNext()) {
					Connection conn = (Connection) iterC.next();
					if (conn instanceof JoinCon) {
						JoinCon join = (JoinCon) conn;
						String dep = joinCon.getTheDependency().getKindDep();
						if (join.getKindJoinCon().equals("OR")) { //$NON-NLS-1$
							if (dep.equals("start-start")) { //$NON-NLS-1$
								why = "Rule 8.11"; //$NON-NLS-1$
								break;
							}
						} else if (join.getKindJoinCon().equals("XOR")) //$NON-NLS-1$
							if (dep.equals("start-start")) { //$NON-NLS-1$
								why = "Rule 8.19"; //$NON-NLS-1$
								break;
							}
					}
				}
				this.processModelHasStarted(parentModel, why);
			} else
				this.logging.registerProcessModelEvent(pmodel,
						"ToEnacting", "Rule 4.6"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		this.searchForFiredConnections(parentModel, why);
		this.searchForReadyActivities(parentModel);
	}

	/**
	 * Verifies if the Process Model is the Root Process Model.
	 */
	private boolean isTheRootProcess(ProcessModel procModel) {

		Decomposed decAct = procModel.getTheDecomposed();

		if (decAct == null)
			return true;
		return false;
	}

	private boolean isAgentAllocatedToActivity(Agent agent, Normal actNorm) {

		Collection agents = this.getInvolvedAgents(actNorm);
		return agents.contains(agent);
	}

	/**
	 * Verifies if a Normal Activity is on the respective state.
	 */
	private boolean isNormalActivityOnState(Normal act, String state) {

		Collection agents = this.getInvolvedAgents(act);
		Iterator iter = agents.iterator();
		while (iter.hasNext()) {
			Agent agent = (Agent) iter.next();
			Collection processAgendas = agent.getTheTaskAgenda()
					.getTheProcessAgenda();
			Iterator iter2 = processAgendas.iterator();
			while (iter2.hasNext()) {
				ProcessAgenda procAgenda = (ProcessAgenda) iter2.next();
				if (procAgenda != null) {
					Process actProcess = this.getTheProcess(act
							.getTheProcessModel());
					Process agendaProcess = procAgenda.getTheProcess();

					if (actProcess.equals(agendaProcess)) { // optimizing code

						Collection tasks = procAgenda.getTheTask();
						Iterator iter3 = tasks.iterator();
						while (iter3.hasNext()) {
							Task task = (Task) iter3.next();
							if (task.getTheNormal().equals(act))
								if (!task.getLocalState().equals(state)) {
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
	 * This method is used as a condition (if a Normal Activity is Paused) in
	 * rule 3.8. (in case of true), or it is used as a condition in rule 3.9.
	 * (in case of false).
	 */
	private boolean isActivityPaused(Normal act) {

		return this.isNormalActivityOnState(act, Plain.PAUSED);
	}

	/**
	 * This method is used as a condition (if a Normal Activity is Finished) in
	 * rule 3.6. (in case of true), or it is used as a condition in rule 3.7.
	 * (in case of false).
	 */
	protected boolean isActivityFinished(Normal act) {

		return this.isNormalActivityOnState(act, Plain.FINISHED);
	}

	/**
	 * Verifies if the Agent is the last one who needs to finish the Task to
	 * change the state of the Normal Activity.
	 */
	private boolean isLastToFinish(Agent agent, Normal actNorm)
			throws WebapseeException {

		boolean actCanFinish = false;

		Collection agents = this.getInvolvedAgents(actNorm);
		int nAgents = agents.size();

		if (nAgents == 1)
			actCanFinish = true;
		else {
			Iterator iter = agents.iterator();
			while (iter.hasNext()) {
				Agent ag = (Agent) iter.next();
				if (!agent.equals(ag)) {
					String taskState = this.getTaskState(ag, actNorm);
					if (taskState.equals(Plain.FINISHED))
						actCanFinish = true;
					else {
						actCanFinish = false;
						break;
					}
				}
			}
		}
		return actCanFinish;
	}

	/**
	 * Verifies if the Agent is the last one who needs to finish the Task to
	 * change the state of the Normal Activity.
	 */
	private boolean isLastToPause(Agent agent, Normal actNorm) {

		boolean actCanPause = false;

		Collection agents = this.getInvolvedAgents(actNorm);
		int nAgents = agents.size();

		if (nAgents == 1)
			actCanPause = true;
		else {
			Iterator iter = agents.iterator();
			while (iter.hasNext()) {
				Agent ag = (Agent) iter.next();
				if (!agent.equals(ag)) {
					String taskState = this.getTaskState(ag, actNorm);
					if (taskState.equals(Plain.PAUSED))
						actCanPause = true;
					else {
						actCanPause = false;
						break;
					}
				}
			}
		}
		return actCanPause;
	}

	/**
	 * Determine that a Process Model has started, propagating to the Parent
	 * Process Model.
	 */
	private void processModelHasStarted(ProcessModel procModel, String why) {

		// if modelId is not the root model, apply rule for its parent model
		if (!this.isTheRootProcess(procModel)) {
			if (procModel.getPmState().equals(ProcessModel.INSTANTIATED)) {
				procModel.setPmState(ProcessModel.ENACTING);
				this.logging.registerProcessModelEvent(procModel,
						"ToEnacting", "Rule 4.5"); //$NON-NLS-1$ //$NON-NLS-2$
				ProcessModel parentModel = procModel.getTheDecomposed()
						.getTheProcessModel();
				this.processModelHasStarted(parentModel, why);
			}
			this.searchForFiredConnections(procModel, why);
			this.searchForReadyActivities(procModel);
		} else {
			if (procModel.getPmState().equals(ProcessModel.INSTANTIATED)) {
				procModel.setPmState(ProcessModel.ENACTING);
				this.logging.registerProcessModelEvent(procModel,
						"ToEnacting", "Rule 4.7"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			this.searchForFiredConnections(procModel, ""); //$NON-NLS-1$
			this.searchForReadyActivities(procModel);
		}
	}

	/**
	 * Searches for Activities in the Process Model that are in Waiting, so
	 * changes their state to Ready.
	 */
	@Override
	public void searchForReadyActivities(ProcessModel processModel) {

		System.out.println("Session reloaded (new session)!");
		processModel = (ProcessModel) processModelDAO.retrieve(processModel
				.getId());
		Collection activities = processModel.getTheActivity();
		Iterator iter = activities.iterator();
		while (iter.hasNext()) {
			Activity activity = (Activity) iter.next();

			if (!isReadyToBegin(activity))
				continue;

			if (activity instanceof Normal) {
				// Rule 2.1
				Normal normal = (Normal) activity;
				EnactionDescription enact = normal.getTheEnactionDescription();

				if (enact.getExclusiveStatus().name()().equals(Plain.WAITING)) {
					// setar ready somente se nao tiver delegated
					enact.setStateWithMessage(Plain.READY);
					this.changeTasksState(normal, Plain.READY);
					this.logging.registerGlobalActivityEvent(normal,
							"ToReady", "Rule 2.1"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			} else if (activity instanceof Automatic) {
				// Rule 2.3
				Automatic automatic = (Automatic) activity;
				EnactionDescription enact = automatic
						.getTheEnactionDescription();
				if (enact.getExclusiveStatus().name()().equals(Plain.WAITING)) {
					enact.setStateWithMessage(Plain.READY);
					this.logging.registerGlobalActivityEvent(automatic,
							"ToReady", "Rule 2.3"); //$NON-NLS-1$ //$NON-NLS-2$
					this.execAutomaticActivity(automatic);
				}
			} else if (activity instanceof Decomposed) {
				// Rule 2.2 and Rule 2.4
				Decomposed decomposed = (Decomposed) activity;
				ProcessModel fragment = decomposed.getTheReferedProcessModel();
				if (fragment.getPmState().equals(ProcessModel.INSTANTIATED)) {
					this.searchForReadyActivities(fragment);
				}
			}
		}
	}

	/**
	 * Verifies if an activity has all dependencies satisfied.
	 */
	private boolean isReadyToBegin(Activity activity) {
		boolean ready;

		// Implicit condition of Rule 2.1:
		// normal activity must have at least one allocated agent

		if (activity instanceof Normal) {
			Normal normal = (Normal) activity;
			ready = this.hasInvolvedAgents(normal);
		} else
			ready = true;

		if (ready) {
			// check process models above
			ready = this.isSuperProcessModelOK(activity);
		}

		if (ready) {
			// check simple connections to activity
			Collection connections = activity.getFromSimpleCon();
			Iterator iter = connections.iterator();
			while (iter.hasNext()) {
				SimpleCon simplecon = (SimpleCon) iter.next();
				if (!(simplecon instanceof Sequence))
					continue;
				Activity activityFrom = simplecon.getFromActivity();
				boolean isPlain = activityFrom instanceof Plain;
				String state = getExclusiveStatus().name()(activityFrom);
				Sequence sequence = (Sequence) simplecon;
				String kindDep = sequence.getTheDependency().getKindDep();
				if (!isDepSatisfiedToBegin(kindDep, state, isPlain)) {
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
				BranchCon branch = (BranchCon) iter.next();
				String kindDep = branchCon.getTheDependency().getKindDep();
				if ((kindDep.equals("start-start") || //$NON-NLS-1$
						kindDep.equals("end-start")) && //$NON-NLS-1$
						!branchCon.isFired().booleanValue()) {
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
				BranchConCondToActivity bcta = (BranchConCondToActivity) iter.next();
				BranchConCond branch = bcta.getTheBranchConCond();
				String kindDep = branchCon.getTheDependency().getKindDep();
				if ((kindDep.equals("start-start") || //$NON-NLS-1$
				kindDep.equals("end-start"))) { //$NON-NLS-1$
					if (!branchCon.isFired().booleanValue()) {
						ready = false;
						break;
					} else { // fired
						if (!this.isConditionSatisfied(branchCon, activity)) {
							this.cancelBranchSuccessor(activity, branchCon);
							ready = false;
						}
					}
				}
			}
		}

		if (ready) {
			// check also joinCons
			// NAC: !fired or dep = (start-start or end_start)
			Collection connections = activity.getFromJoinCon();
			Iterator iter = connections.iterator();
			while (iter.hasNext()) {
				JoinCon join = (JoinCon) iter.next();
				String kindDep = joinCon.getTheDependency().getKindDep();
				if ((kindDep.equals("start-start") || //$NON-NLS-1$
						kindDep.equals("end-start")) && //$NON-NLS-1$
						!joinCon.isFired().booleanValue()) {
					ready = false;
					break;
				}
			}
		}

		return (ready);
	}

	/**
	 * Cancells the successor of a BranchCon Connection, when the predecessor of
	 * the BranchCon is an Activity. Applies to the Rules 6.13, 6.14, 6.15, 6.16
	 * 7.13, 7.14, 7.15
	 */
	private void cancelBranchSuccessor(Activity act, BranchCon branchCon) {

		String state = this.getExclusiveStatus().name()(act);
		if (!(state.equals(Plain.FAILED) && state.equals(ProcessModel.FAILED))) {
			if (act instanceof Plain) {
				if (branch instanceof BranchANDCon) {
					this.cancelGeneralActivity(act, true, "Rule 6.13"); //$NON-NLS-1$
				} else {
					BranchConCond bCond = (BranchConCond) branchCon;
					String depType = bCond.getTheDependency().getKindDep();
					if (!this.isConditionSatisfied(bCond, act)) {
						if (depType.equals("start-start") || depType.equals("end-start")) { //$NON-NLS-1$ //$NON-NLS-2$
							if (this.getExclusiveStatus().name()(act).equals(Plain.WAITING)) {
								this.cancelGeneralActivity(act, true,
										"Rule 7.13"); //$NON-NLS-1$
							}
						} else if (depType.equals("end-end")) { //$NON-NLS-1$
							if (this.getExclusiveStatus().name()(act).equals(Plain.WAITING)
									|| this.getExclusiveStatus().name()(act).equals(Plain.READY)) {
								this.cancelGeneralActivity(act, true,
										"Rule 7.14"); //$NON-NLS-1$
							} else if (!this.getExclusiveStatus().name()(act)
									.equals(Plain.WAITING)
									&& !this.getExclusiveStatus().name()(act).equals(Plain.READY)) {
								this.cancelGeneralActivity(act, true,
										"Rule 7.15"); //$NON-NLS-1$
							}
						} else {
							this.cancelGeneralActivity(act, true, "Rule 6.14"); //$NON-NLS-1$
						}
					}
				}
			} else if (branch instanceof BranchANDCon) {
				this.cancelGeneralActivity(act, true, "Rule 6.15"); //$NON-NLS-1$
			} else {
				this.cancelGeneralActivity(act, true, "Rule 6.16"); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Called by the cancelActivity (called by user). Cancells a generical
	 * activity and propagates it.
	 */
	private void cancelGeneralActivity(Activity activity, boolean propagate,
			String why) {

		boolean canceled = false;

		if (activity instanceof Plain) {
			Plain plain = (Plain) activity;
			canceled = this.cancelPlainActivity(plain, why);
		} else if (activity instanceof Decomposed) {
			Decomposed decomp = (Decomposed) activity;
			canceled = this.cancelDecomposedActivity(decomp, why);
		}

		if (canceled)
			this.activityHasFinished(activity);
		if (!(canceled && propagate)) {
			return;
		}

		// propagate to successor activities
		this.cancelPropagation(activity);
	}

	private void cancelPropagation(Activity activity) {

		Collection succ = this.getConnectionsTo(activity);
		Iterator iter = succ.iterator();
		while (iter.hasNext()) {
			Connection conn = (Connection) iter.next();
			if (conn instanceof Sequence) {
				Sequence seq = (Sequence) conn;
				this.cancelSimpleSuccessor(seq.getToActivity(), seq);
			} else if (conn instanceof MultipleCon) {
				MultipleCon multi = (MultipleCon) conn;
				if (multi instanceof JoinCon) {
					JoinCon join = (JoinCon) multi;
					Activity actJoin = joinCon.getToActivity();
					MultipleCon multJoin = joinCon.getToMultipleCon();
					if (actJoinCon != null)
						this.cancelJoinSuccessor(actJoin, joinCon);
					else if (multJoinCon != null)
						this.cancelJoinSuccessor(multJoin, joinCon);
				} else if (multi instanceof BranchCon) {
					BranchCon branch = (BranchCon) multi;
					if (branch instanceof BranchANDCon) {
						BranchANDCon branchAND = (BranchANDCon) branchCon;
						Collection acts = branchANDCon.getToActivity();
						Collection mults = branchANDCon.getToMultipleCon();
						Iterator iterActs = acts.iterator();
						while (iterActs.hasNext()) {
							Activity act = (Activity) iterActs.next();
							this.cancelBranchSuccessor(act, branchCon);
						}
						Iterator iterMults = mults.iterator();
						while (iterMults.hasNext()) {
							MultipleCon mult = (MultipleCon) iterMults.next();
							this.cancelBranchSuccessor(mult, branchANDCon);
						}
					} else {
						BranchConCond branchCond = (BranchConCond) branchCon;
						Collection acts = branchConCond
								.getTheBranchConCondToActivity();
						Collection mults = branchConCond
								.getTheBranchConCondToMultipleCon();
						Iterator iterTheActs = acts.iterator();
						while (iterTheActs.hasNext()) {
							BranchConCondToActivity act = (BranchConCondToActivity) iterTheActs
									.next();
							this.cancelBranchConSuccessor(act.getTheActivity(),
									branchCon);
						}
						Iterator iterTheMults = mults.iterator();
						while (iterTheMults.hasNext()) {
							BranchConCondToMultipleCon mult = (BranchConCondToMultipleCon) iterTheMults
									.next();
							this.cancelBranchConSuccessor(
									mult.getTheMultipleCon(), branchConCond);
						}
					}
				}
			}
		}
	}

	/**
	 * Cancells a Plain Activity.
	 */
	private boolean cancelPlainActivity(Plain actPlain, String why) {

		boolean canceled;

		String state = actPlain.getTheEnactionDescription().getExclusiveStatus().name()();
		if (state.equals(Plain.FAILED) || state.equals(Plain.CANCELED)) {
			canceled = false;
		} else {
			actPlain.getTheEnactionDescription().setStateWithMessage(
					Plain.CANCELED);
			this.logging.registerGlobalActivityEvent(actPlain,
					"ToCanceled", why); //$NON-NLS-1$
			if (actPlain instanceof Normal) {
				Normal normal = (Normal) actPlain;
				this.releaseResourcesFromActivity(normal);
				this.changeTasksState(normal, Plain.CANCELED);
				Collection agents = this.getInvolvedAgents(normal);
				Iterator iter = agents.iterator();
				while (iter.hasNext()) {
					Agent agent = (Agent) iter.next();
					Collection procAgendas = agent.getTheTaskAgenda()
							.getTheProcessAgenda();
					Iterator iter2 = procAgendas.iterator();
					while (iter2.hasNext()) {
						ProcessAgenda agenda = (ProcessAgenda) iter2.next();
						Task task = this.getTask(agenda, normal);
						if (task != null)
							this.logging.registerAgendaEvent(task,
									"ToCanceled", why); //$NON-NLS-1$
					}
				}
			}
			canceled = true;
		}

		return (canceled);
	}

	/**
	 * Cancells a Decomposed Activity. Applies to the Rule 6.19.
	 */
	private boolean cancelDecomposedActivity(Decomposed actDecomp, String why) {

		boolean canceled;

		String state = actDecomp.getTheReferedProcessModel().getPmState();
		if (state.equals(ProcessModel.FAILED)
				|| state.equals(ProcessModel.CANCELED)) {
			canceled = false;
		} else {
			actDecomp.getTheReferedProcessModel().setPmState(
					ProcessModel.CANCELED);
			this.logging.registerProcessModelEvent(
					actDecomp.getTheReferedProcessModel(), "ToCanceled", why); //$NON-NLS-1$

			// cancel activities in the model
			Collection activities = actDecomp.getTheReferedProcessModel()
					.getTheActivity();
			Iterator iter = activities.iterator();
			while (iter.hasNext()) {
				Activity act = (Activity) iter.next();
				String actState = this.getExclusiveStatus().name()(act);
				if (!(actState.equals(Plain.CANCELED) || actState
						.equals(ProcessModel.CANCELED))
						&& !(actState.equals(Plain.FAILED) || actState
								.equals(ProcessModel.FAILED)))
					this.cancelGeneralActivity(act, false, "Rule 6.19"); //$NON-NLS-1$
			}
			canceled = true;
		}
		return (canceled);
	}

	/**
	 * Cancells the targets of a Multiple Connection, i.e., the successors of
	 * the Multiple Connection.
	 */
	private void cancelMultConnTargets(MultipleCon multiconn) {

		if (multiconn instanceof BranchCon) {
			BranchCon branch = (BranchCon) multiconn;
			Collection succ = new LinkedList();
			if (branch instanceof BranchANDCon) {
				BranchANDCon bAND = (BranchANDCon) branchCon;
				if (bAND.getToActivity() != null)
					succ.addAll(bAND.getToActivity());
				if (bAND.getToMultipleCon() != null)
					succ.addAll(bAND.getToMultipleCon());
			} else {
				BranchConCond bCond = (BranchConCond) branchCon;
				Collection bctmc = bCond.getTheBranchConCondToMultipleCon();
				Collection atbc = bCond.getTheBranchConCondToActivity();
				Iterator iterMulti = bctmc.iterator(), iterAct = atbc
						.iterator();
				while (iterMulti.hasNext()) {
					BranchConCondToMultipleCon multi = (BranchConCondToMultipleCon) iterMulti
							.next();
					if (multi.getTheMultipleCon() != null)
						succ.add(multi.getTheMultipleCon());
				}
				while (iterAct.hasNext()) {
					BranchConCondToActivity act = (BranchConCondToActivity) iterAct
							.next();
					if (act.getTheActivity() != null)
						succ.add(act.getTheActivity());
				}
			}
			Iterator iter = succ.iterator();
			while (iter.hasNext()) {
				Object obj = (Object) iter.next();
				this.cancelBranch(branchCon, obj);
			}
		} else if (multiconn instanceof JoinCon) {
			JoinCon join = (JoinCon) multiconn;
			this.cancelJoin(joinCon);
		}
	}

	/**
	 * Cancells a BranchCon connection. Applies to the Rules 6.13, 6.14, 6.15,
	 * 6.16, 6.17, 6.18, 6.29, 6.30
	 */
	private void cancelBranch(BranchCon branchCon, Object obj) {

		if (obj instanceof Activity) {
			Activity act = (Activity) obj;
			if (act instanceof Plain) {
				if (branch instanceof BranchANDCon)
					this.cancelGeneralActivity(act, true, "Rule 6.13"); //$NON-NLS-1$
				else
					this.cancelGeneralActivity(act, true, "Rule 6.14"); //$NON-NLS-1$
			} else {
				if (branch instanceof BranchANDCon)
					this.cancelGeneralActivity(act, true, "Rule 6.15"); //$NON-NLS-1$
				else
					this.cancelGeneralActivity(act, true, "Rule 6.16"); //$NON-NLS-1$
			}
		} else if (obj instanceof MultipleCon) {
			MultipleCon multi = (MultipleCon) obj;
			// Rule 6.17, 6.18, 6.29, 6.30
			this.cancelMultConnTargets(multi);
		}
	}

	/**
	 * Cancells a JoinCon connection. Applies to the Rules 6.7, 6.12 6.20, 6.21,
	 * 6.22, 6.23, 6.24, 6.25, 6.26, 6.27, 6.28.
	 */
	private void cancelJoin(JoinCon joinCon) {

		String joinType = join.getKindJoinCon();
		Activity act = joinCon.getToActivity();
		MultipleCon multi = joinCon.getToMultipleCon();
		if (act != null) {
			if (act instanceof Plain) {
				if (joinConType.equals("AND")) //$NON-NLS-1$
					this.cancelGeneralActivity(act, true, "Rule 6.20"); //$NON-NLS-1$
				else if (joinConType.equals("OR")) //$NON-NLS-1$
					this.cancelGeneralActivity(act, true, "Rule 6.22"); //$NON-NLS-1$
				else if (joinConType.equals("XOR")) //$NON-NLS-1$
					this.cancelGeneralActivity(act, true, "Rule 6.23"); //$NON-NLS-1$
			} else {
				if (joinConType.equals("AND")) //$NON-NLS-1$
					this.cancelGeneralActivity(act, true, "Rule 6.21"); //$NON-NLS-1$
				else if (joinConType.equals("OR")) //$NON-NLS-1$
					this.cancelGeneralActivity(act, true, "Rule 6.24"); //$NON-NLS-1$
				else if (joinConType.equals("XOR")) //$NON-NLS-1$
					this.cancelGeneralActivity(act, true, "Rule 6.25"); //$NON-NLS-1$
			}
		} else if (multi != null) {
			// Rule 6.7, 6.12, 6.26, 6.27, 6.28.
			this.cancelMultConnTargets(multi);
		}
	}

	/**
	 * called by APSEE Manager. Applies to the rules 6.1, 6.2 6.3, 6.4 Cancells
	 * the successor of a Simple Connection.
	 */
	private void cancelSimpleSuccessor(Activity act, Sequence sequence) {

		String depType = sequence.getTheDependency().getKindDep();
		String state = this.getExclusiveStatus().name()(act);
		if (depType.equals("end-start") || depType.equals("end-end")) { //$NON-NLS-1$ //$NON-NLS-2$
			if (!(state.equals(Plain.FAILED) || state
					.equals(ProcessModel.FAILED)))
				if (act instanceof Plain)
					this.cancelGeneralActivity(act, true, "Rule 6.1"); //$NON-NLS-1$
				else
					this.cancelGeneralActivity(act, true, "Rule 6.2"); //$NON-NLS-1$
		} else {
			if (!(state.equals(Plain.FAILED) || state
					.equals(ProcessModel.FAILED))
					&& !(state.equals(Plain.ACTIVE) || state
							.equals(ProcessModel.ENACTING))
					&& !(state.equals(Plain.FINISHED) || state
							.equals(ProcessModel.FINISHED))
					&& !state.equals(Plain.PAUSED))
				if (act instanceof Plain)
					this.cancelGeneralActivity(act, true, "Rule 6.3"); //$NON-NLS-1$
				else
					this.cancelGeneralActivity(act, true, "Rule 6.4"); //$NON-NLS-1$
		}
	}

	/**
	 * Cancells the successor of a JoinCon Connection, when the predecessor of the
	 * JoinCon is an Activity. Applies to the Rules 6.5, 6.20, 6.8, 6.22 6.9, 6.23,
	 * 6.6, 6.21, 6.10, 6.24, 6.11, 6.25
	 */
	private void cancelJoinSuccessor(Activity act, JoinCon joinCon) {

		// String why = "";
		String state = this.getExclusiveStatus().name()(act);
		String joinType = join.getKindJoinCon();
		// String depType = joinCon.getTheDependency().getKindDep();

		// Test additional conditions for OR and XOR cases
		boolean canCancel = true;

		if (joinType.equals("OR") || joinConType.equals("XOR")) { //$NON-NLS-1$ //$NON-NLS-2$
			Collection predec = this.getPredecessors(joinCon);
			Iterator iter = predec.iterator();
			while (iter.hasNext()) {
				Object obj = (Object) iter.next();
				if (obj instanceof Activity) {
					Activity activity = (Activity) obj;
					String actState = this.getExclusiveStatus().name()(activity);
					if ((!actState.equals(Plain.FAILED) && !actState
							.equals(Plain.CANCELED))
							|| (!actState.equals(ProcessModel.FAILED) && !actState
									.equals(ProcessModel.CANCELED))) {
						canCancel = false;
						break;
					}
				} else if (obj instanceof MultipleCon) {
					MultipleCon multi = (MultipleCon) obj;
					// MISSING: waiting for specification
					if (true) {
						canCancel = false;
						break;
					}
				}
			}
		}
		if (canCancel) {
			if (!(state.equals(Plain.FAILED) || state
					.equals(ProcessModel.FAILED)))
				if (act instanceof Plain) {
					if (joinConType.equals("AND")) //$NON-NLS-1$
						this.cancelGeneralActivity(act, true, "Rule 6.5"); //$NON-NLS-1$
					else if (joinConType.equals("OR")) //$NON-NLS-1$
						this.cancelGeneralActivity(act, true, "Rule 6.8"); //$NON-NLS-1$
					else if (joinConType.equals("XOR")) //$NON-NLS-1$
						this.cancelGeneralActivity(act, true, "Rule 6.9"); //$NON-NLS-1$
				} else {
					if (joinConType.equals("AND")) //$NON-NLS-1$
						this.cancelGeneralActivity(act, true, "Rule 6.6"); //$NON-NLS-1$
					else if (joinConType.equals("OR")) //$NON-NLS-1$
						this.cancelGeneralActivity(act, true, "Rule 6.10"); //$NON-NLS-1$
					else if (joinConType.equals("XOR")) //$NON-NLS-1$
						this.cancelGeneralActivity(act, true, "Rule 6.11"); //$NON-NLS-1$
				}
		}
	}

	/**
	 * Cancells the successor of a JoinCon Connection, when the predecessor of the
	 * JoinCon is an Multiple Connection.
	 */
	private void cancelJoinSuccessor(MultipleCon mult, JoinCon joinCon) {

		String joinType = join.getKindJoinCon();
		String depType = joinCon.getTheDependency().getKindDep();

		// test additional conditions for OR and XOR cases
		boolean canCancel = true;

		if (joinType.equals("OR") || joinConType.equals("XOR")) { //$NON-NLS-1$ //$NON-NLS-2$
			Collection predec = this.getPredecessors(joinCon);
			Iterator iter = predec.iterator();
			while (iter.hasNext()) {
				Object obj = (Object) iter.next();
				if (obj instanceof Activity) {
					Activity activity = (Activity) obj;
					String actState = this.getExclusiveStatus().name()(activity);
					if ((!actState.equals(Plain.FAILED) && !actState
							.equals(Plain.CANCELED))
							|| (!actState.equals(ProcessModel.FAILED) && !actState
									.equals(ProcessModel.CANCELED))) {
						canCancel = false;
						break;
					}
				} else if (obj instanceof MultipleCon) {
					MultipleCon multi = (MultipleCon) obj;
					// MISSING: waiting for specification
					if (true) {
						canCancel = false;
						break;
					}
				}
			}
		}
		if (canCancel) {
			this.cancelJoin(joinCon);
		}
	}

	/**
	 * Cancells the successor of a BranchCon Connection, when the predecessor of
	 * the BranchCon is an Multiple Connection.
	 */
	private void cancelBranchSuccessor(MultipleCon mult, BranchCon branchCon) {

		// String depType = branchCon.getTheDependency().getKindDep();
		boolean canCancel = true;
		Collection predec = this.getPredecessors(branchCon);
		Iterator iter = predec.iterator();
		while (iter.hasNext()) {
			Object obj = (Object) iter.next();
			if (obj instanceof Activity) {
				Activity activity = (Activity) obj;
				String actState = this.getExclusiveStatus().name()(activity);
				if ((!actState.equals(Plain.FAILED) && !actState
						.equals(Plain.CANCELED))
						|| (!actState.equals(ProcessModel.FAILED) && !actState
								.equals(ProcessModel.CANCELED))) {
					canCancel = false;
					break;
				}
			} else if (obj instanceof MultipleCon) {
				MultipleCon multi = (MultipleCon) obj;
				// MISSING: waiting for specification
				if (true) {
					canCancel = false;
					break;
				}
			}
		}
		if (canCancel)
			this.cancelBranch(branchCon, mult);
	}

	/**
	 * Called by the failActivity (called by user). Fails a generical activity
	 * and propagates the fail.
	 */
	private void failGeneralActivity(Activity activity, boolean propagate,
			String why) {

		boolean failed = false;

		if (activity instanceof Plain) {
			Plain plain = (Plain) activity;
			failed = this.failPlainActivity(plain, why);
		} else if (activity instanceof Decomposed) {
			Decomposed decomp = (Decomposed) activity;
			failed = this.failDecomposedActivity(decomp, why);
		}

		if (failed) {
			if (!this.toFailOrFinish(activity.getTheProcessModel())) {
				this.activityHasFinished(activity);
			}
		}
		if (!(failed && propagate)) {
			return;
		}

		// propagate to successor activities
		this.failPropagation(activity);
	}

	private boolean toFailOrFinish(ProcessModel processModel) {

		boolean fail = true;
		boolean finish = false;

		Collection activities = processModel.getTheActivity();
		activities.remove(null);
		if (activities.isEmpty()) {
			return finish;
		}

		Iterator iterActs = activities.iterator();
		while (iterActs.hasNext()) {
			Activity activity = (Activity) iterActs.next();
			String state = this.getExclusiveStatus().name()(activity);
			if (!(state.equals(Plain.FAILED) || state
					.equals(ProcessModel.FAILED))) {
				return finish;
			}
		}
		return fail;
	}

	private void failPropagation(Activity activity) {

		Collection succ = this.getConnectionsTo(activity);
		Iterator iter = succ.iterator();
		while (iter.hasNext()) {
			Connection conn = (Connection) iter.next();
			if (conn instanceof Sequence) {
				Sequence seq = (Sequence) conn;
				this.failSimpleSuccessor(seq.getToActivity(), seq);
			} else if (conn instanceof MultipleCon) {
				MultipleCon multi = (MultipleCon) conn;
				if (multi instanceof JoinCon) {
					JoinCon join = (JoinCon) multi;
					Activity actJoin = joinCon.getToActivity();
					MultipleCon multJoin = joinCon.getToMultipleCon();
					if (actJoinCon != null)
						this.failJoinSuccessor(actJoin, joinCon);
					else if (multJoinCon != null)
						this.failJoinSuccessor(multJoin, joinCon);
				} else if (multi instanceof BranchCon) {
					BranchCon branch = (BranchCon) multi;
					if (branch instanceof BranchANDCon) {
						BranchANDCon branchAND = (BranchANDCon) branchCon;
						Collection acts = branchANDCon.getToActivity();
						Collection mults = branchANDCon.getToMultipleCon();
						Iterator iterActs = acts.iterator();
						while (iterActs.hasNext()) {
							Activity act = (Activity) iterActs.next();
							this.failBranchSuccessor(act, branchANDCon);
						}
						Iterator iterMults = mults.iterator();
						while (iterMults.hasNext()) {
							MultipleCon mult = (MultipleCon) iterMults.next();
							this.failBranchSuccessor(mult, branchANDCon);
						}
					} else {
						BranchConCond branchCond = (BranchConCond) branchCon;
						Collection acts = branchConCond
								.getTheBranchConCondToActivity();
						Collection mults = branchConCond
								.getTheBranchConCondToMultipleCon();
						Iterator iterTheActs = acts.iterator();
						while (iterTheActs.hasNext()) {
							BranchConCondToActivity act = (BranchConCondToActivity) iterTheActs
									.next();
							this.failBranchConSuccessor(act.getTheActivity(),
									branchCon);
						}
						Iterator iterTheMults = mults.iterator();
						while (iterTheMults.hasNext()) {
							BranchConCondToMultipleCon mult = (BranchConCondToMultipleCon) iterTheMults
									.next();
							this.failBranchConSuccessor(mult.getTheMultipleCon(),
									branchConCond);
						}
					}
				}
			}
		}
	}

	/**
	 * Fails a Plain Activity.
	 */
	private boolean failPlainActivity(Plain actPlain, String why) {

		boolean failed;

		String state = actPlain.getTheEnactionDescription().getExclusiveStatus().name()();
		if (state.equals(Plain.CANCELED)) {
			failed = false;
		} else if ((state.equals(Plain.FINISHED) || state.equals(Plain.FAILED))
				&& (why.equals("Rule 9.5") //$NON-NLS-1$
				|| why.equals("Rule 9.7"))) { //$NON-NLS-1$
			failed = false;
		} else {
			actPlain.getTheEnactionDescription().setStateWithMessage(
					Plain.FAILED);
			this.logging.registerGlobalActivityEvent(actPlain, "ToFailed", why); //$NON-NLS-1$
			if (actPlain instanceof Normal) {
				Normal normal = (Normal) actPlain;
				this.releaseResourcesFromActivity(normal);
				this.changeTasksState(normal, Plain.FAILED);
				Collection agents = this.getInvolvedAgents(normal);
				Iterator iter = agents.iterator();
				while (iter.hasNext()) {
					Agent agent = (Agent) iter.next();
					Collection procAgendas = agent.getTheTaskAgenda()
							.getTheProcessAgenda();
					Iterator iter2 = procAgendas.iterator();
					while (iter2.hasNext()) {
						ProcessAgenda agenda = (ProcessAgenda) iter2.next();
						Task task = this.getTask(agenda, normal);
						if (task != null)
							this.logging.registerAgendaEvent(task,
									"ToFailed", why); //$NON-NLS-1$
					}
				}
			}
			failed = true;
		}
		return (failed);
	}

	/**
	 * Fails a Decomposed Activity. Applies to the Rule 5.3
	 */
	private boolean failDecomposedActivity(Decomposed actDecomp, String why) {

		boolean failed;

		String state = actDecomp.getTheReferedProcessModel().getPmState();
		if (state.equals(ProcessModel.CANCELED)) {
			failed = false;
		} else if ((state.equals(ProcessModel.FINISHED) || state
				.equals(ProcessModel.FAILED)) && (why.equals("Rule 9.8") //$NON-NLS-1$
				|| why.equals("Rule 9.9"))) { //$NON-NLS-1$
			failed = false;
		} else {
			actDecomp.getTheReferedProcessModel().setPmState(
					ProcessModel.FAILED);
			this.logging.registerProcessModelEvent(
					actDecomp.getTheReferedProcessModel(), "ToFailed", why); //$NON-NLS-1$
			// fail activities in the model
			Collection activities = actDecomp.getTheReferedProcessModel()
					.getTheActivity();
			Iterator iter = activities.iterator();
			while (iter.hasNext()) {
				Activity act = (Activity) iter.next();
				String actState = this.getExclusiveStatus().name()(act);
				if (!(actState.equals(Plain.CANCELED) || actState
						.equals(ProcessModel.CANCELED))
						&& !(actState.equals(Plain.FAILED) || actState
								.equals(ProcessModel.FAILED)))
					this.failGeneralActivity(act, false, "Rule 5.3"); //$NON-NLS-1$
			}
			failed = true;
		}
		return (failed);
	}

	/**
	 * Fail the targets of a Multiple Connection, i.e., the successors of the
	 * Multiple Connection.
	 */
	private void failMultConnTargets(MultipleCon multiconn) {

		if (multiconn instanceof BranchCon) {
			BranchCon branch = (BranchCon) multiconn;
			Collection succ = new LinkedList();
			if (branch instanceof BranchANDCon) {
				BranchANDCon bAND = (BranchANDCon) branchCon;
				if (bAND.getToActivity() != null)
					succ.addAll(bAND.getToActivity());
				if (bAND.getToMultipleCon() != null)
					succ.addAll(bAND.getToMultipleCon());
			} else {
				BranchConCond bCond = (BranchConCond) branchCon;
				Collection bctmc = bCond.getTheBranchConCondToMultipleCon();
				Collection atbc = bCond.getTheBranchConCondToActivity();
				Iterator iterMulti = bctmc.iterator(), iterAct = atbc
						.iterator();
				while (iterMulti.hasNext()) {
					BranchConCondToMultipleCon multi = (BranchConCondToMultipleCon) iterMulti
							.next();
					if (multi.getTheMultipleCon() != null)
						succ.add(multi.getTheMultipleCon());
				}
				while (iterAct.hasNext()) {
					BranchConCondToActivity act = (BranchConCondToActivity) iterAct
							.next();
					if (act.getTheActivity() != null)
						succ.add(act.getTheActivity());
				}
			}
			Iterator iter = succ.iterator();
			while (iter.hasNext()) {
				Object obj = (Object) iter.next();
				this.failBranch(branchCon, obj);
			}
		} else if (multiconn instanceof JoinCon) {
			JoinCon join = (JoinCon) multiconn;
			this.failJoin(joinCon);
		}
	}

	/**
	 * Fails a BranchCon connection. Applies to the Rules 5.13, 5.14, 5.15, 5.16,
	 * 5.17, 5.27, 5.28,
	 */
	private void failBranch(BranchCon branchCon, Object obj) {

		if (obj instanceof Activity) {
			Activity act = (Activity) obj;
			if (act instanceof Plain) {
				String dep = branchCon.getTheDependency().getKindDep();
				if (dep.equals("end-start") || dep.equals("end-end")) //$NON-NLS-1$ //$NON-NLS-2$
					this.failGeneralActivity(act, true, "Rule 5.13"); //$NON-NLS-1$
			} else {
				String dep = branchCon.getTheDependency().getKindDep();
				if (dep.equals("end-start") || dep.equals("end-end")) //$NON-NLS-1$ //$NON-NLS-2$
					if (branch instanceof BranchANDCon) {
						this.failGeneralActivity(act, true, "Rule 5.14"); //$NON-NLS-1$
					} else
						this.failGeneralActivity(act, true, "Rule 5.15"); //$NON-NLS-1$
			}
		} else if (obj instanceof MultipleCon) {
			// Rules 5.27, 5.28
			MultipleCon multi = (MultipleCon) obj;
			this.failMultConnTargets(multi);
		}
	}

	/**
	 * Fails a JoinCon connection. Applies to the Rules 5.18, 5.19, 5.20, 5.21,
	 * 5.22, 5.23, 5.24, 5.25, 5.26
	 */
	private void failJoin(JoinCon joinCon) {

		Activity act = joinCon.getToActivity();
		MultipleCon multi = joinCon.getToMultipleCon();
		String joinType = join.getKindJoinCon();
		if (act != null) {
			if (act instanceof Plain) {
				if (joinConType.equals("AND")) //$NON-NLS-1$
					this.failGeneralActivity(act, true, "Rule 5.18"); //$NON-NLS-1$
				else if (joinConType.equals("OR")) //$NON-NLS-1$
					this.failGeneralActivity(act, true, "Rule 5.20"); //$NON-NLS-1$
				else if (joinConType.equals("XOR")) //$NON-NLS-1$
					this.failGeneralActivity(act, true, "Rule 5.21"); //$NON-NLS-1$
			} else {
				if (joinConType.equals("AND")) //$NON-NLS-1$
					this.failGeneralActivity(act, true, "Rule 5.19"); //$NON-NLS-1$
				else if (joinConType.equals("OR")) //$NON-NLS-1$
					this.failGeneralActivity(act, true, "Rule 5.22"); //$NON-NLS-1$
				else if (joinConType.equals("XOR")) //$NON-NLS-1$
					this.failGeneralActivity(act, true, "Rule 5.23"); //$NON-NLS-1$
			}

		} else if (multi != null) {
			// Rules 5.24, 5.25, 5.26
			this.failMultConnTargets(multi);
		}
	}

	/**
	 * Fails the successor of a Simple Connection. Applies to the Rules 5.1,
	 * 5.2.
	 */
	private void failSimpleSuccessor(Activity act, Sequence sequence) {

		String depType = sequence.getTheDependency().getKindDep();
		String state = this.getExclusiveStatus().name()(act);
		if (depType.equals("end-start") || depType.equals("end-end")) { //$NON-NLS-1$ //$NON-NLS-2$
			if (!(state.equals(Plain.CANCELED) || state
					.equals(ProcessModel.CANCELED)))
				if (act instanceof Plain)
					this.failGeneralActivity(act, true, "Rule 5.1"); //$NON-NLS-1$
				else
					this.failGeneralActivity(act, true, "Rule 5.2"); //$NON-NLS-1$
		}
	}

	/**
	 * Fails the successor of a JoinCon Connection, when the predecessor of the
	 * JoinCon is an Activity. Applies to the Rules 5.4, 5.5, 5.7, 5.8, 5.9, 5.10
	 */
	private void failJoinSuccessor(Activity act, JoinCon joinCon) {

		String state = this.getExclusiveStatus().name()(act);
		String joinType = join.getKindJoinCon();
		String depType = joinCon.getTheDependency().getKindDep();

		// test additional conditions for OR and XOR cases
		boolean canFail = true;

		if (joinType.equals("OR") || joinConType.equals("XOR")) { //$NON-NLS-1$ //$NON-NLS-2$
			Collection predec = this.getPredecessors(joinCon);
			Iterator iter = predec.iterator();
			while (iter.hasNext()) {
				Object obj = (Object) iter.next();
				if (obj instanceof Activity) {
					Activity activity = (Activity) obj;
					String actState = this.getExclusiveStatus().name()(activity);
					if ((!actState.equals(Plain.FAILED) && !actState
							.equals(Plain.CANCELED))
							|| (!actState.equals(ProcessModel.FAILED) && !actState
									.equals(ProcessModel.CANCELED))) {
						canFail = false;
						break;
					}
				} else if (obj instanceof MultipleCon) {
					MultipleCon multi = (MultipleCon) obj;
					// MISSING: waiting for specification
					if (true) {
						canFail = false;
						break;
					}
				}
			}
		}
		if (canFail) {
			if (depType.equals("end-start") || depType.equals("end-end")) //$NON-NLS-1$ //$NON-NLS-2$
				if (!(state.equals(Plain.CANCELED) || state
						.equals(ProcessModel.CANCELED)))
					if (act instanceof Plain) {
						if (joinConType.equals("AND")) //$NON-NLS-1$
							this.failGeneralActivity(act, true, "Rule 5.4"); //$NON-NLS-1$
						else if (joinConType.equals("OR")) //$NON-NLS-1$
							this.failGeneralActivity(act, true, "Rule 5.7"); //$NON-NLS-1$
						else if (joinConType.equals("XOR")) //$NON-NLS-1$
							this.failGeneralActivity(act, true, "Rule 5.8"); //$NON-NLS-1$
					} else {
						if (joinConType.equals("AND")) //$NON-NLS-1$
							this.failGeneralActivity(act, true, "Rule 5.5"); //$NON-NLS-1$
						else if (joinConType.equals("OR")) //$NON-NLS-1$
							this.failGeneralActivity(act, true, "Rule 5.9"); //$NON-NLS-1$
						else if (joinConType.equals("XOR")) //$NON-NLS-1$
							this.failGeneralActivity(act, true, "Rule 5.10"); //$NON-NLS-1$
					}
		}
	}

	/**
	 * Fails the successor of a JoinCon Connection, when the predecessor of the
	 * JoinCon is an Multiple Connection. Applies to the Rule 5.6, 5.11, 5.12,
	 */
	private void failJoinSuccessor(MultipleCon mult, JoinCon joinCon) {

		// String state = this.getExclusiveStatus().name()(act);
		String joinType = join.getKindJoinCon();
		String depType = joinCon.getTheDependency().getKindDep();

		// Test additional conditions for OR and XOR cases
		boolean canFail = true;

		if (joinType.equals("OR") || joinConType.equals("XOR")) { //$NON-NLS-1$ //$NON-NLS-2$
			Collection predec = this.getPredecessors(joinCon);
			Iterator iter = predec.iterator();
			while (iter.hasNext()) {
				Object obj = (Object) iter.next();
				if (obj instanceof Activity) {
					Activity activity = (Activity) obj;
					String actState = this.getExclusiveStatus().name()(activity);
					if ((!actState.equals(Plain.FAILED) && !actState
							.equals(Plain.CANCELED))
							|| (!actState.equals(ProcessModel.FAILED) && !actState
									.equals(ProcessModel.CANCELED))) {
						canFail = false;
						break;
					}
				} else if (obj instanceof MultipleCon) {
					MultipleCon multi = (MultipleCon) obj;
					// MISSING: waiting for specification
					if (true) {
						canFail = false;
						break;
					}
				}
			}
		}
		if (canFail) {
			if (depType.equals("end-start") || depType.equals("end-end")) //$NON-NLS-1$ //$NON-NLS-2$
				this.failJoin(joinCon);
		}
	}

	/**
	 * Fails the successor of a BranchCon Connection, when the predecessor of the
	 * BranchCon is an Activity. Applies to the Rules 5.13, 5.14, 5.15,
	 */
	private void failBranchSuccessor(Activity act, BranchCon branchCon) {

		String state = this.getExclusiveStatus().name()(act);
		String depType = branchCon.getTheDependency().getKindDep();
		boolean canFail = true;
		Collection predec = this.getPredecessors(branchCon);
		Iterator iter = predec.iterator();
		while (iter.hasNext()) {
			Object obj = (Object) iter.next();
			if (obj instanceof Activity) {
				Activity activity = (Activity) obj;
				String actState = this.getExclusiveStatus().name()(activity);
				if ((!actState.equals(Plain.FAILED) && !actState
						.equals(Plain.CANCELED))
						|| (!actState.equals(ProcessModel.FAILED) && !actState
								.equals(ProcessModel.CANCELED))) {
					canFail = false;
					break;
				}
			} else if (obj instanceof MultipleCon) {
				MultipleCon multi = (MultipleCon) obj;
				// MISSING: waiting for specification
				if (true) {
					canFail = false;
					break;
				}
			}
		}
		if (canFail)
			if (depType.equals("end-start") || depType.equals("end-end")) //$NON-NLS-1$ //$NON-NLS-2$
				if (!(state.equals(Plain.CANCELED) && state
						.equals(ProcessModel.CANCELED)))
					if (act instanceof Plain) {
						this.failGeneralActivity(act, true, "Rule 5.13"); //$NON-NLS-1$
					} else {
						if (branch instanceof BranchANDCon) {
							this.failGeneralActivity(act, true, "Rule 5.14"); //$NON-NLS-1$
						} else
							this.failGeneralActivity(act, true, "Rule 5.15"); //$NON-NLS-1$
					}
	}

	/**
	 * Fails the successor of a BranchCon Connection, when the predecessor of the
	 * BranchCon is an Multiple Connection.
	 */
	private void failBranchSuccessor(MultipleCon mult, BranchCon branchCon) {

		String depType = branchCon.getTheDependency().getKindDep();
		boolean canFail = true;
		Collection predec = this.getPredecessors(branchCon);
		Iterator iter = predec.iterator();
		while (iter.hasNext()) {
			Object obj = (Object) iter.next();
			if (obj instanceof Activity) {
				Activity activity = (Activity) obj;
				String actState = this.getExclusiveStatus().name()(activity);
				if ((!actState.equals(Plain.FAILED) && !actState
						.equals(Plain.CANCELED))
						|| (!actState.equals(ProcessModel.FAILED) && !actState
								.equals(ProcessModel.CANCELED))) {
					canFail = false;
					break;
				}
			} else if (obj instanceof MultipleCon) {
				MultipleCon multi = (MultipleCon) obj;
				// MISSING: waiting for specification
				if (true) {
					canFail = false;
					break;
				}
			}
		}
		if (canFail)
			if (depType.equals("end-start") || depType.equals("end-end")) //$NON-NLS-1$ //$NON-NLS-2$
				this.failBranch(branchCon, mult);
	}

	/**
	 * called by APSEE Manager. Applies to the rules 9.3, 9.4 Determines that an
	 * Activity has finished, propagating to the Process Model.
	 */
	private void activityHasFinished(Activity act) {

		String why = "";
		ProcessModel parentModel = act.getTheProcessModel();
		String state = this.getExclusiveStatus().name()(act);
		String modState = parentModel.getPmState();

		if (modState.equals(ProcessModel.ENACTING)) {

			boolean feedbackTaken = false;

			if (!state.equals(Plain.CANCELED)
					&& !state.equals(ProcessModel.CANCELED)) {
				if (isFeedbackSource(act)) {
					feedbackTaken = this.executeFeedbacks(act);
				}
			}

			// if there was no feedback with a true condition, finish this task
			if (!feedbackTaken) {

				// Rule 4.1
				if (this.isProcessModelReadyToFinish(parentModel)) {
					parentModel.setPmState(ProcessModel.FINISHED);
					if (act instanceof Plain) {
						boolean isSubOfRoot = !this
								.isTheRootProcess(parentModel);
						if (!isSubOfRoot)
							this.logging.registerProcessModelEvent(parentModel,
									"ToFinished", "Rule 4.1"); //$NON-NLS-1$ //$NON-NLS-2$
						else
							this.logging.registerProcessModelEvent(parentModel,
									"ToFinished", "Rule 4.3"); //$NON-NLS-1$ //$NON-NLS-2$
					} // is Decomposed
					else {
						this.logging.registerProcessModelEvent(parentModel,
								"ToFinished", "Rule 4.2"); //$NON-NLS-1$ //$NON-NLS-2$
						Collection conns = this.getConnectionsTo(act);
						Iterator iterC = conns.iterator();
						while (iterC.hasNext()) {
							Connection conn = (Connection) iterC.next();
							if (conn instanceof JoinCon) {
								JoinCon join = (JoinCon) conn;
								String dep = joinCon.getTheDependency()
										.getKindDep();
								if (join.getKindJoinCon().equals("OR")) { //$NON-NLS-1$
									if (dep.equals("end-start") || //$NON-NLS-1$
											dep.equals("end-end")) { //$NON-NLS-1$
										why = "Rule 8.2"; //$NON-NLS-1$
										break;
									} else {
										why = "Rule 8.11"; //$NON-NLS-1$
										break;
									}
								} else if (join.getKindJoinCon().equals("XOR")) //$NON-NLS-1$
									if (dep.equals("end-start") || //$NON-NLS-1$
											dep.equals("end-end")) { //$NON-NLS-1$
										why = "Rule 8.6"; //$NON-NLS-1$
										break;
									} else {
										why = "Rule 8.19"; //$NON-NLS-1$
										break;
									}
							}
						}
					}
					if (!this.isTheRootProcess(parentModel)) {
						// parent model is a fragment
						this.activityHasFinished(parentModel.getTheDecomposed());
					} else {
						// parent model is the root model
						this.rootModelHasFinished(parentModel);
					}
				} else {
					this.searchForFiredConnections(parentModel, why);
					this.searchForReadyActivities(parentModel);
				}
			}
		} else {
			this.searchForFiredConnections(parentModel, why);
			this.searchForReadyActivities(parentModel);
		}
	}

	/**
	 * Determines that the Root Process Model has finished, so it finishes the
	 * Process too.
	 */
	private void rootModelHasFinished(ProcessModel processModel) {

		processModel.setPmState(ProcessModel.FINISHED);
		this.logging.registerProcessModelEvent(processModel, "ToFinished",
				"Rules G11.11");
		processModel.getTheProcess().setPState(Process.FINISHED);
		this.logging.registerProcessEvent(processModel.getTheProcess(),
				"ToFinished", "Rule ?");
	}

	/**
	 * Verifies if the Activity is a Feedback connection source.
	 */
	private boolean isFeedbackSource(Activity act) {
		Collection set = this.getFeedbackSet(act);
		set.remove(null);
		return (!set.isEmpty());
	}

	/**
	 * Searches for feedback connections having a given activity as a source.
	 */
	private Collection getFeedbackSet(Activity act) {

		HashSet set = new HashSet();
		ProcessModel processModel = act.getTheProcessModel();
		Collection connections = processModel.getTheConnection();
		Iterator iter = connections.iterator();
		while (iter.hasNext()) {
			Connection conn = (Connection) iter.next();
			if (conn instanceof SimpleCon) {
				if (conn instanceof Feedback) {
					Feedback feedback = (Feedback) conn;
					Activity source = feedback.getFromActivity();
					if (source != null && source.equals(act)) {
						set.add(feedback);
					}
				}
			}
		}
		return (set);
	}

	/**
	 * called by APSEE Manager. Applies to the rules 9.1, 9.2, 9.3, 9.4 Executes
	 * all the Feedback connections from the Activity.
	 */
	private boolean executeFeedbacks(Activity act) {
		boolean executed = false;

		Collection set = this.getFeedbackSet(act);
		Iterator conns = set.iterator();
		while (conns.hasNext()) {
			Feedback nextConn = (Feedback) conns.next();

			if (this.conditionValue(nextConn.getTheCondition())) {
				executed = true;
				this.executeFeedback(nextConn);
			}
		}
		return (executed);
	}

	/**
	 * Begins the execution of a Feedback Connection.
	 */
	private void executeFeedback(SimpleCon simpleCon) {

		Activity _target = simpleCon.getToActivity();
		this.redoActivityAndPropagate(_target);
	}

	/**
	 * called by APSEE Manager. Applies to the rules 9.21, 9.22, 9.23, 9.24,
	 * 9.25, 9.26, 9.27, 9.28, 9.29, 9.30, 9.31, 9.32, 9.33, 9.34, 9.35, 9.36,
	 * 9.37
	 *
	 * Redoes an Activity and propagates the "redo" to the following Activities.
	 */
	private void redoActivityAndPropagate(Activity act) {

		boolean redone = this.redoActivity(act);

		if (!redone)
			return;
		// propagate

		Collection conns = this.getConnectionsTo(act);
		Iterator iter = conns.iterator();
		Collection succ = new LinkedList();
		while (iter.hasNext()) {
			Connection conn = (Connection) iter.next();
			if (conn instanceof Sequence) {
				Sequence seq = (Sequence) conn;
				if (seq.getToActivity() != null)
					succ.add(seq.getToActivity());
			} else if (conn instanceof MultipleCon) {
				MultipleCon multi = (MultipleCon) conn;
				multi.setFired(new Boolean(false));
				if (multi instanceof JoinCon) {
					JoinCon join = (JoinCon) multi;
					if (joinCon.getToActivity() != null)
						succ.add(joinCon.getToActivity());
				} else if (multi instanceof BranchANDCon) {
					BranchANDCon branchAnd = (BranchANDCon) multi;
					if (branchAndCon.getToActivity() != null)
						succ.addAll(branchAndCon.getToActivity());
				} else if (multi instanceof BranchConCond) {
					BranchConCond branchCond = (BranchConCond) multi;
					Collection acts = branchCond.getTheBranchConCondToActivity();
					Iterator iter2 = acts.iterator();
					while (iter2.hasNext()) {
						BranchConCondToActivity actToBC = (BranchConCondToActivity) iter2
								.next();
						if (actToBC.getTheActivity() != null)
							succ.add(actToBC.getTheActivity());
					}
				}
			}
		}
		Iterator iter2 = succ.iterator();
		while (iter2.hasNext()) {
			Activity activity = (Activity) iter2.next();
			String state;
			if (activity instanceof Plain) {
				Plain plain = (Plain) activity;
				state = plain.getTheEnactionDescription().getExclusiveStatus().name()();
				activity = plain;
			} else {
				Decomposed dec = (Decomposed) activity;
				state = dec.getTheReferedProcessModel().getPmState();
				activity = dec;
			}
			if (!state.equals(Plain.WAITING)) {

				this.redoActivityAndPropagate(activity);
				this.resetConnection(activity);
			}
		}
	}

	/**
	 * called by APSEE Manager. Applies to the rules 9.17, 9.19, 9.20 9.38, 9.39
	 * Chooses what "kind of redo" (redoNormalActivity, redoAutomaticActivity,
	 * redoDecomposedActivity) will be called.
	 */
	private boolean redoActivity(Activity act) {

		boolean redone = false;
		if (act instanceof Normal) {
			Normal normal = (Normal) act;
			redone = this.redoNormalActivity(normal);
		} else if (act instanceof Automatic) {
			Automatic automatic = (Automatic) act;
			redone = this.redoAutomaticActivity(automatic);
		} else if (act instanceof Decomposed) {
			Decomposed decomposed = (Decomposed) act;
			redone = this.redoDecomposedActivity(decomposed);
		}
		return redone;
	}

	/**
	 * called by APSEE Manager. Applies to the rules 9.11, 9.12, 9.13 Resets the
	 * Normal Activity that was enacting, creating a new version of it.
	 */
	private boolean redoNormalActivity(Normal actNorm) {

		boolean redone = false;
		String state = actNorm.getTheEnactionDescription().getExclusiveStatus().name()();

		if (!state.equals(Plain.WAITING) && !state.equals("")) { //$NON-NLS-1$
			redone = true;
			boolean rule912 = false;
			boolean rule913 = false;
			if (state.equals(Plain.ACTIVE)) {
				rule912 = true;
				this.releaseResourcesFromActivity(actNorm);
			} else if (state.equals(Plain.FINISHED)) {
				rule913 = true;
			}

			this.versioning(actNorm);

			if (rule912) {
				this.logging.registerGlobalActivityEvent(actNorm,
						"Redone", "Rule 9.12"); //$NON-NLS-1$ //$NON-NLS-2$
				actNorm.getTheEnactionDescription().setStateWithMessage(
						Plain.WAITING);
				this.logging.registerGlobalActivityEvent(actNorm,
						"ToWaiting", "Rule 9.12"); //$NON-NLS-1$ //$NON-NLS-2$
			} else if (rule913) {
				this.logging.registerGlobalActivityEvent(actNorm,
						"Redone", "Rule 9.13"); //$NON-NLS-1$ //$NON-NLS-2$
				actNorm.getTheEnactionDescription().setStateWithMessage(
						Plain.WAITING);
				this.logging.registerGlobalActivityEvent(actNorm,
						"ToWaiting", "Rule 9.13"); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				this.logging.registerGlobalActivityEvent(actNorm,
						"Redone", "Rule 9.11"); //$NON-NLS-1$ //$NON-NLS-2$
				actNorm.getTheEnactionDescription().setStateWithMessage(
						Plain.WAITING);
				this.logging.registerGlobalActivityEvent(actNorm,
						"ToWaiting", "Rule 9.11"); //$NON-NLS-1$
			}

			// reset tasks
			Collection agents = this.getInvolvedAgents(actNorm);
			Iterator iter = agents.iterator();
			while (iter.hasNext()) {
				Agent agent = (Agent) iter.next();

				if (rule912) {
					this.updateAgenda(agent, actNorm, Plain.WAITING,
							"Rule 9.12");
				} else if (rule913) {
					this.updateAgenda(agent, actNorm, Plain.WAITING,
							"Rule 9.13");
				} else {
					this.updateAgenda(agent, actNorm, Plain.WAITING,
							"Rule 9.11");
				}

			}
		}
		return redone;
	}

	private Activity versioning(Activity activity) {

		Activity actVersion;
		if (activity instanceof Plain) {
			Plain plain = (Plain) activity;
			EnactionDescription enaction = plain.getTheEnactionDescription();
			String oldState = enaction.getExclusiveStatus().name()();
			Date oldBegin = enaction.getActualBegin();
			Date oldEnd = enaction.getActualEnd();
			plain.getTheEnactionDescription().setActualBegin(null);
			plain.getTheEnactionDescription().setActualEnd(null);

			if (plain instanceof Normal) {
				Normal newNormal = new Normal();
				newNormal.getTheEnactionDescription().setState(oldState);
				newNormal.getTheEnactionDescription().setActualBegin(oldBegin);
				newNormal.getTheEnactionDescription().setActualEnd(oldEnd);
				newNormal.setScript(((Normal) plain).getScript());
				newNormal.setDelegable(((Normal) plain).getDelegable());
				newNormal.setAutoAllocable(((Normal) plain).getAutoAllocable());
				newNormal.setHowLong(((Normal) plain).getHowLong());
				newNormal.setHowLongUnit(((Normal) plain).getHowLongUnit());
				newNormal.setPlannedBegin(((Normal) plain).getPlannedBegin());
				newNormal.setPlannedEnd(((Normal) plain).getPlannedEnd());
				actVersion = newNormal;
			} else { // intanceof Automatic
				Automatic newAutomatic = new Automatic();
				newAutomatic.getTheEnactionDescription().setState(oldState);
				newAutomatic.getTheEnactionDescription().setActualBegin(
						oldBegin);
				newAutomatic.getTheEnactionDescription().setActualEnd(oldEnd);
				actVersion = newAutomatic;
			}
		} else {
			Decomposed decomposed = (Decomposed) activity;
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
			if (actv != null) {
				versList.addLast(actv);
			}
		}

		activity.clearHasVersions();
		Iterator iterVersList = versList.iterator();
		while (iterVersList.hasNext()) {
			Activity actv = (Activity) iterVersList.next();
			if (actv != null) {
				activity.insertIntoHasVersions(actv);
			}
		}

		int size = activity.getHasVersions().size() + 1;
		String version = "_v" + size; //$NON-NLS-1$
		actVersion.setIdent(activity.getIdent() + version);
		actVersion.setName(activity.getName() + version);
		activity.insertIntoHasVersions(actVersion);

		String message = "<MESSAGE>" + //$NON-NLS-1$
				"<NOTIFY>" + //$NON-NLS-1$
				"<OID>" + activity.getId() + "</OID>" + //$NON-NLS-1$ //$NON-NLS-2$
				"<TYPE>UPT</TYPE>" + //$NON-NLS-1$
				"<CLASS>" + activity.getClass().getName() + "</CLASS>" + //$NON-NLS-1$ //$NON-NLS-2$
				"<BY>Apsee</BY>" + //$NON-NLS-1$
				"</NOTIFY>" + //$NON-NLS-1$
				"</MESSAGE>"; //$NON-NLS-1$

		return actVersion;
	}

	private void copyActivityRelationships(Activity actFrom, Activity actTo) {

		if (!actFrom.getActivityEstimation().isEmpty()) {

			ActivityEstimation greater = null;
			// Collection temp = new HashSet();

			Collection actEstimations = actFrom.getActivityEstimation();
			Iterator iterActEstimations = actEstimations.iterator();
			while (iterActEstimations.hasNext()) {
				ActivityEstimation actEstimation = (ActivityEstimation) iterActEstimations
						.next();
				if (actEstimation != null) {
					if (greater != null) {
						if (greater.getId() < actEstimation.getId())
							greater = actEstimation;
					} else
						greater = actEstimation;
					// temp.add(actEstimation);
					// actTo.insertIntoActivityEstimation(actEstimation);
				}
			}
			// actFrom.getActivityEstimation().removeAll(temp);

			// The last estimation from the old version must be copied for the
			// new one.
			if (greater != null) {
				ActivityEstimation lastEst = new ActivityEstimation();
				lastEst.setUnit(greater.getUnit());
				lastEst.setValue(greater.getValue());
				lastEst.insertIntoMetricDefinition(greater
						.getMetricDefinition());
				actTo.insertIntoActivityEstimation(lastEst);
			}
		}
		if (!actFrom.getActivityMetric().isEmpty()) {
			ActivityMetric greater = null;
			// Collection temp = new HashSet();

			Collection actMetrics = actFrom.getActivityMetric();
			Iterator iterActMetrics = actMetrics.iterator();
			while (iterActMetrics.hasNext()) {
				ActivityMetric actMetric = (ActivityMetric) iterActMetrics
						.next();
				if (actMetric != null) {
					if (greater != null) {
						if (greater.getId() < actMetric.getId())
							greater = actMetric;
					} else
						greater = actMetric;
					// temp.add(actMetric);
					// actMetric.insertIntoActivity(actTo);
				}
			}
			// actFrom.getActivityMetric().removeAll(temp);

			// The last metric from the old version must be copied for the new
			// one.
			if (greater != null) {
				ActivityMetric lastMet = new ActivityMetric();
				lastMet.setUnit(greater.getUnit());
				lastMet.setValue(greater.getValue());
				lastMet.setPeriodBegin(greater.getPeriodBegin());
				lastMet.setPeriodEnd(greater.getPeriodEnd());
				lastMet.insertIntoMetricDefinition(greater
						.getMetricDefinition());
				actTo.insertIntoActivityMetric(lastMet);
			}

		}
		if (actFrom.getFromArtifactCon() != null) {
			Collection temp = new HashSet();

			Collection fromArtifactCons = actFrom.getFromArtifactCon();
			Iterator iterFromArtifactCons = fromArtifactCons.iterator();
			while (iterFromArtifactCons.hasNext()) {
				ArtifactCon fromArtifactCon = (ArtifactCon) iterFromArtifactCons
						.next();
				if (fromArtifactCon != null) {
					temp.add(fromArtifactCon);
					fromArtifactCon.getToActivity().add(actTo);
					actTo.getFromArtifactCon().add(fromArtifactCon);
				}
			}
		}

		if (actFrom.getTheActivityType() != null) {

			ActivityType activityType = actFrom.getTheActivityType();
			actTo.setTheActivityType(activityType);
			activityType.getTheActivity().add(actTo);
		}
		if (actFrom.getTheModelingActivityEvent() != null) {
			Collection temp = new HashSet();

			Collection modelingActivityEvents = actFrom
					.getTheModelingActivityEvent();
			Iterator iterModelingActivityEvents = modelingActivityEvents
					.iterator();
			while (iterModelingActivityEvents.hasNext()) {
				ModelingActivityEvent modelingActivityEvent = (ModelingActivityEvent) iterModelingActivityEvents
						.next();
				if (modelingActivityEvent != null) {
					temp.add(modelingActivityEvent);
					modelingActivityEvent.setTheActivity(actTo);
					actTo.getTheModelingActivityEvent().add(
							modelingActivityEvent);
				}
			}
			actFrom.getTheModelingActivityEvent().removeAll(temp);
		}
		if (actFrom.getToArtifactCon() != null) {
			Collection temp = new HashSet();

			Collection toArtifactCons = actFrom.getToArtifactCon();
			Iterator iterToArtifactCons = toArtifactCons.iterator();
			while (iterToArtifactCons.hasNext()) {
				ArtifactCon toArtifactCon = (ArtifactCon) iterToArtifactCons
						.next();
				if (toArtifactCon != null) {
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
	public void createNewVersion(String act_id) {
		Object act;
		act = activityDAO.retrieveBySecondaryKey(act_id);

		if (act == null) {
			//        	throw new DataBaseException(Messages.getString("facades.EnactmentEngine.DBExcActivity")+act_id+Messages.getString("facades.EnactmentEngine.DBExcDoesNotExist")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		Activity activity = (Activity) act;

		Process process = (Process) processDAO.retrieveBySecondaryKey(activity
				.getIdent().substring(0, activity.getIdent().indexOf(".")));
		String pState = process.getpStatus().name();
		if (pState.equals(Process.NOT_STARTED)) {
			// throw new
			// WebapseeException(Messages.getString("facades.EnactmentEngine.WAExcRedoNotEnactActivity"));
		}

		ProcessModel processModel = activity.getTheProcessModel();
		String processModelState = processModel.getPmState();

		if (processModelState.equals(ProcessModel.FINISHED)
				|| processModelState.equals(ProcessModel.FAILED)
				|| processModelState.equals(ProcessModel.CANCELED)) {
			// throw new
			// WebapseeException(Messages.getString("facades.EnactmentEngine.WAExcRedoFinishedProcMod"));
		}

		// Now we start the implementation of the rules
		String state = this.getExclusiveStatus().name()(activity);

		if (state.equals(Plain.WAITING)
				|| state.equals(ProcessModel.REQUIREMENTS)
				|| state.equals(ProcessModel.ABSTRACT)
				|| state.equals(ProcessModel.INSTANTIATED)
				|| state.equals(Plain.READY) || state.equals("")) {

			this.redoActivity(activity);

			// this.rollbackProcessModelState(activity.getTheProcessModel());

			this.searchForFiredConnections(activity.getTheProcessModel(), ""); //$NON-NLS-1$
			this.searchForReadyActivities(activity.getTheProcessModel());
			this.determineProcessModelStates(activity.getTheProcessModel());

		} else if ((state.equals(Plain.ACTIVE) || state
				.equals(ProcessModel.ENACTING))
				|| (state.equals(Plain.CANCELED) || state
						.equals(ProcessModel.CANCELED))
				|| (state.equals(Plain.FINISHED) || state
						.equals(ProcessModel.FINISHED))
				|| (state.equals(Plain.FAILED) || state
						.equals(ProcessModel.FAILED))
				|| (state.equals(Plain.PAUSED))) {

			this.redoActivityAndPropagate(activity);

			// this.rollbackProcessModelState(activity.getTheProcessModel());

			this.searchForFiredConnections(activity.getTheProcessModel(), ""); //$NON-NLS-1$
			this.searchForReadyActivities(activity.getTheProcessModel());
			this.determineProcessModelStates(activity.getTheProcessModel());
		}

		// Persistence Operation
		activityDAO.update(activity);
	}

	private void rollbackProcessModelState(ProcessModel processModel)
			throws WebapseeException {
		String state = processModel.getPmState();
		if (state.equals(ProcessModel.FINISHED)
				|| state.equals(ProcessModel.FAILED)) {

			boolean toEnacting = false;

			Collection acts = processModel.getTheActivity();
			Iterator iter = acts.iterator();
			while (iter.hasNext()) {
				Activity activity = (Activity) iter.next();
				if (activity != null) {
					String actState = this.getExclusiveStatus().name()(activity);
					if (actState.equals(Plain.ACTIVE)
							|| actState.equals(Plain.PAUSED)
							|| actState.equals(Plain.FINISHED)
							|| actState.equals(Plain.FAILED)
							|| actState.equals(ProcessModel.ENACTING)
							|| actState.equals(ProcessModel.FINISHED)
							|| actState.equals(ProcessModel.FAILED)) {

						toEnacting = true;
						break;
					}
				}
			}

			if (toEnacting) {
				processModel.setPmState(ProcessModel.ENACTING);
				this.logging.registerProcessModelEvent(processModel, "To"
						+ ProcessModel.ENACTING, "Rule G1.10");
			} else {
				processModel.setPmState(ProcessModel.INSTANTIATED);
				this.logging.registerProcessModelEvent(processModel, "To"
						+ ProcessModel.INSTANTIATED, "Rule G1.11");
			}

			// To Upper Levels
			Decomposed upperDecomposed = processModel.getTheDecomposed();
			if (upperDecomposed != null) {

				this.rollbackProcessModelState(upperDecomposed
						.getTheProcessModel());

				Collection connsTo = this.getConnectionsTo(upperDecomposed);
				Iterator iterConnsTo = connsTo.iterator();
				while (iterConnsTo.hasNext()) {
					Connection connTo = (Connection) iterConnsTo.next();
					Collection successors = this.getSuccessors(connTo);
					Iterator iterSuccessors = successors.iterator();
					while (iterSuccessors.hasNext()) {
						Activity successor = (Activity) iterSuccessors.next();
						this.redoActivityAndPropagate(successor);
					}
				}
			}
		}
	}

	/**
	 * Returns a Collection with the successors (Activities and Multiple
	 * Connection) of a Connection.
	 */
	private Collection getSuccessors(Connection conn) {

		Collection succ = new LinkedList();
		if (conn instanceof Sequence) {
			Sequence seq = (Sequence) conn;
			if (seq.getToActivity() != null)
				succ.add(seq.getToActivity());
		} else if (conn instanceof BranchCon) {
			BranchCon branch = (BranchCon) conn;
			if (branch instanceof BranchANDCon) {
				BranchANDCon bAND = (BranchANDCon) branchCon;
				if (bAND.getToActivity() != null)
					succ.addAll(bAND.getToActivity());
				if (bAND.getToMultipleCon() != null)
					succ.addAll(bAND.getToMultipleCon());
			} else {
				BranchConCond bCond = (BranchConCond) branchCon;
				Collection bctmc = bCond.getTheBranchConCondToMultipleCon();
				Collection atbc = bCond.getTheBranchConCondToActivity();
				Iterator iterMulti = bctmc.iterator(), iterAct = atbc
						.iterator();
				while (iterMulti.hasNext()) {
					BranchConCondToMultipleCon multi = (BranchConCondToMultipleCon) iterMulti
							.next();
					if (multi.getTheMultipleCon() != null)
						succ.add(multi.getTheMultipleCon());
				}
				while (iterAct.hasNext()) {
					BranchConCondToActivity act = (BranchConCondToActivity) iterAct
							.next();
					if (act.getTheActivity() != null)
						succ.add(act.getTheActivity());
				}
			}
		} else if (conn instanceof JoinCon) {
			JoinCon join = (JoinCon) conn;
			if (joinCon.getToActivity() != null)
				succ.add(joinCon.getToActivity());
			if (joinCon.getToMultipleCon() != null)
				succ.add(joinCon.getToMultipleCon());
		}
		return succ;
	}

	/**
	 * called by APSEE Manager. Applies to the rule 9.14 Creates a new version
	 * of an Artifact from a Plain Activity. This operatiosn is now handlered by
	 * the artifact version control system
	 *
	 * private void createNewArtifactVersion (Plain actPlain)
	 */

	/**
	 * called by APSEE Manager. Applies to the rules 9.15 Resets the Automatic
	 * Activity that was enacting, creating a new version of it.
	 */
	private boolean redoAutomaticActivity(Automatic actAutom) {

		boolean redone = false;
		String state = actAutom.getTheEnactionDescription().getExclusiveStatus().name()();

		if (!state.equals(Plain.WAITING)) {
			redone = true;
			if (state.equals(Plain.ACTIVE) || state.equals(Plain.FINISHED)) {
				actAutom.getTheEnactionDescription().setStateWithMessage(
						Plain.FAILED);
				this.logging.registerGlobalActivityEvent(actAutom,
						"ToFailed", "Rule ?"); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				actAutom.getTheEnactionDescription().setStateWithMessage(
						Plain.CANCELED);
				this.logging.registerGlobalActivityEvent(actAutom,
						"ToCanceled", "Rule ?"); //$NON-NLS-1$ //$NON-NLS-2$
			}

			this.versioning(actAutom);

			// set EnactionDesc to a new object
			EnactionDescription enact = new EnactionDescription();
			actAutom.setTheEnactionDescription(enact);

			this.logging.registerGlobalActivityEvent(actAutom,
					"Re-done", "Rule 9.15"); //$NON-NLS-1$ //$NON-NLS-2$
			actAutom.getTheEnactionDescription().setStateWithMessage(
					Plain.WAITING);
			this.logging.registerGlobalActivityEvent(actAutom,
					"ToWaiting", "Rule 9.15"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return redone;
	}

	/**
	 * called by APSEE Manager. Applies to the rules 9.16 Resets the Decomposed
	 * Activity that was enacting, creating a new version of it.
	 */
	private boolean redoDecomposedActivity(Decomposed actDecomp) {

		ProcessModel refProcModel = actDecomp.getTheReferedProcessModel();
		String state = refProcModel.getPmState();

		if (state.equals(ProcessModel.ENACTING)
				|| state.equals(ProcessModel.FINISHED)) {
			refProcModel.setPmState(ProcessModel.FAILED);
			this.logging.registerProcessModelEvent(refProcModel,
					"ToFailed", "Rule ?"); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			refProcModel.setPmState(ProcessModel.CANCELED);
			this.logging.registerProcessModelEvent(refProcModel,
					"ToCanceled", "Rule ?"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		this.versioning(actDecomp);

		refProcModel.setTheProcessModelEvent(new LinkedList());
		refProcModel.setPmState(ProcessModel.INSTANTIATED);

		this.logging.registerProcessModelEvent(refProcModel,
				"Re-done", "Rule 9.16"); //$NON-NLS-1$ //$NON-NLS-2$
		this.logging.registerProcessModelEvent(refProcModel,
				"ToInstantiated", "Rule 9.16"); //$NON-NLS-1$ //$NON-NLS-2$

		// propagate to child activities
		Collection activities = refProcModel.getTheActivity();

		Iterator iter = activities.iterator();
		while (iter.hasNext()) {
			Activity act = (Activity) iter.next();
			this.redoActivity(act);
			this.resetConnection(act);
		}

		return true;
	}

	/**
	 * called by APSEE Manager. Applies to the rules 9.18 Set the attribute
	 * fired to false from the Multiple Connections that succeed the Activity.
	 */
	private void resetConnection(Activity act) {

		Collection conns = this.getConnectionsTo(act);
		Iterator iter = conns.iterator();
		while (iter.hasNext()) {
			Connection conn = (Connection) iter.next();
			if (conn instanceof MultipleCon) {
				MultipleCon multi = (MultipleCon) conn;
				multi.setFired(new Boolean(false));
			}
		}
	}

	/**
	 * Releases the Resources from the Normal Activity and set to Failed the
	 * state of the Tasks that refers to this Normal Activity.
	 */
	private void manCleanActivity(Activity act, String state) {

		if (act instanceof Normal) { // Rule 9.6
			Normal normal = (Normal) act;
			this.releaseResourcesFromActivity(normal);
			this.changeTasksState(normal, state);
		}
		// else is Decomposed, so Rule 9.8, i.e, return and execute feedbacks
	}

	/**
	 * Verifies if the process Model is ready to fish, i.e., if the last
	 * activities of the Model have finished.
	 */
	private boolean isProcessModelReadyToFinish(ProcessModel procModel) {

		boolean ret = true;

		if (!this.isTheRootProcess(procModel)) {
			Decomposed actDec = procModel.getTheDecomposed();
			Collection froms = this.getConnectionsFrom(actDec);
			Iterator iterfroms = froms.iterator();
			while (iterfroms.hasNext()) {
				Connection conn = (Connection) iterfroms.next();

				if (conn instanceof BranchANDCon) { // Rule 4.2 NAC-01
					BranchANDCon bAND = (BranchANDCon) conn;
					if (!bAND.isFired().booleanValue()
							&& bAND.getTheDependency().getKindDep()
									.equals("end-end")) {
						ret = false;
						break;
					}
				} else if (conn instanceof BranchConCond) {
					BranchConCond bCond = (BranchConCond) conn;
					if (bCond.getTheDependency().getKindDep().equals("end-end")) {
						if (!bCond.isFired().booleanValue()) { // Rule 4.2
																// NAC-02
							ret = false;
							break;
						} else { // Rule 4.2 NAC-03
							Collection bctas = bCond
									.getTheBranchConCondToActivity();
							Iterator iterBctas = bctas.iterator();
							while (iterBctas.hasNext()) {
								BranchConCondToActivity bcta = (BranchConCondToActivity) iterBctas
										.next();
								if (bcta.getTheActivity().equals(actDec)
										&& !this.conditionValue(bcta
												.getTheCondition())) {
									ret = false;
									break;
								}
							}
							if (!ret)
								break;
						}
					}
				} else if (conn instanceof JoinCon) { // Rule 4.2 NAC-01
					JoinCon join = (JoinCon) conn;
					if (!joinCon.isFired().booleanValue()
							&& joinCon.getTheDependency().getKindDep()
									.equals("end-end")) {
						ret = false;
						break;
					}
				}

				else if (conn instanceof Sequence) { // Rule 4.2 NAC-04
					Sequence seq = (Sequence) conn;
					Activity from = seq.getFromActivity();
					String state = this.getExclusiveStatus().name()(from);
					if (seq.getTheDependency().getKindDep().equals("end-end")
							&& (!(state.equals(Plain.FINISHED) || state
									.equals(ProcessModel.FINISHED)))) {
						ret = false;
						break;
					}
				}
			}
		}
		if (ret) {
			Collection acts = procModel.getTheActivity();
			Iterator iter = acts.iterator();
			while (iter.hasNext()) {
				Activity activity = (Activity) iter.next();
				String state = this.getExclusiveStatus().name()(activity);
				if (!(state.equals(Plain.FINISHED)
						|| state.equals(ProcessModel.FINISHED)
						|| state.equals(Plain.FAILED)
						|| state.equals(ProcessModel.FAILED)
						|| state.equals(Plain.CANCELED) || state
							.equals(ProcessModel.CANCELED))) {

					ret = false;
					break;
				}
			}
		}
		return ret;
	}

	/**
	 * Returns a Collection with the predecessors of an Activity.
	 */
	private Collection getConnectionsFrom(Activity act) {

		Collection connFrom = new LinkedList();
		if (act.getFromSimpleCon() != null)
			connFrom.addAll(act.getFromSimpleCon());
		if (act.getFromJoinCon() != null)
			connFrom.addAll(act.getFromJoinCon());
		if (act.getFromBranchANDCon() != null)
			connFrom.addAll(act.getFromBranchANDCon());
		Collection bctas = act.getTheBranchConCondToActivity();
		Iterator iterBctas = bctas.iterator();
		while (iterBctas.hasNext()) {
			BranchConCondToActivity bcta = (BranchConCondToActivity) iterBctas.next();
			if (bcta.getTheBranchConCond() != null)
				connFrom.add(bcta.getTheBranchConCond());
		}
		return connFrom;
	}

	/**
	 * Called by APSEE Manager. Involves the rules: 12.2, 12.7, 12.8. Release
	 * all the resources from a Normal Activity. Changing their states to
	 * Available.
	 */
	private void releaseResourcesFromActivity(Normal actNorm) {

		Collection activityResources = actNorm.getTheRequiredResource();
		Iterator iter = activityResources.iterator();
		while (iter.hasNext()) {
			RequiredResource requiredResource = (RequiredResource) iter.next();
			Resource resource = requiredResource.getTheResource();
			this.releaseResource(actNorm, resource);
		}
	}

	private void releaseResource(Normal actNorm, Resource resource) {

		if (resource instanceof Exclusive) {
			Exclusive exc = (Exclusive) resource;
			if (exc.getExclusiveStatus().name()().equals(Exclusive.LOCKED)) {
				exc.setState(Exclusive.AVAILABLE);
				this.logging.registerResourceEvent(exc, actNorm,
						"ToAvailable", "Rule 12.7"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else if (resource instanceof Shareable) {
			Shareable sha = (Shareable) resource;
			this.logging.registerResourceEvent(sha, actNorm,
					"Released", "Rule 12.2"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	private boolean isSuperProcessModelOK(Activity activity) {

		boolean ok = true;

		Decomposed actDec = activity.getTheProcessModel().getTheDecomposed();
		if (actDec == null) {
			return ok;
		} else {
			if (ok) {
				ok = this.isSuperProcessModelOK(actDec);
			}

			if (ok) {
				// check simple connections to activity
				Collection connections = actDec.getFromSimpleCon();
				Iterator iter = connections.iterator();
				while (iter.hasNext()) {
					SimpleCon simplecon = (SimpleCon) iter.next();
					if (!(simplecon instanceof Sequence))
						continue;
					Activity activityFrom = simplecon.getFromActivity();
					boolean isPlain = activityFrom instanceof Plain;
					String state = getExclusiveStatus().name()(activityFrom);
					Sequence sequence = (Sequence) simplecon;
					String kindDep = sequence.getTheDependency().getKindDep();
					if (!isDepSatisfiedToBegin(kindDep, state, isPlain)) {
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
					BranchCon branch = (BranchCon) iter.next();
					String kindDep = branchCon.getTheDependency().getKindDep();
					if ((kindDep.equals("start-start") || //$NON-NLS-1$
							kindDep.equals("end-start")) && //$NON-NLS-1$
							!branchCon.isFired().booleanValue()) {
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
					BranchConCondToActivity bcta = (BranchConCondToActivity) iter
							.next();
					BranchConCond branch = bcta.getTheBranchConCond();
					String kindDep = branchCon.getTheDependency().getKindDep();
					if ((kindDep.equals("start-start") || //$NON-NLS-1$
					kindDep.equals("end-start"))) { //$NON-NLS-1$
						if (!branchCon.isFired().booleanValue()) {
							ok = false;
							break;
						} else { // fired
							if (!this.isConditionSatisfied(branchCon, actDec)) {
								ok = false;
							}
						}
					}
				}
			}

			if (ok) {
				// check also joinCons
				// NAC: !fired or dep = (start-start or end_start)
				Collection connections = actDec.getFromJoinCon();
				Iterator iter = connections.iterator();
				while (iter.hasNext()) {
					JoinCon join = (JoinCon) iter.next();
					String kindDep = joinCon.getTheDependency().getKindDep();
					if ((kindDep.equals("start-start") || //$NON-NLS-1$
							kindDep.equals("end-start")) && //$NON-NLS-1$
							!joinCon.isFired().booleanValue()) {
						ok = false;
						break;
					}
				}
			}
		}
		return ok;
	}

	/**
	 * Verifies if a Normal Activity has Required Agents.
	 */
	private boolean hasInvolvedAgents(Normal actNorm) {

		boolean has = false;

		Collection reqPeople = actNorm.getTheRequiredPeople();
		Iterator iter = reqPeople.iterator();
		while (iter.hasNext()) {
			RequiredPeople reqP = (RequiredPeople) iter.next();
			if (reqP instanceof ReqAgent) {
				ReqAgent reqAgent = (ReqAgent) reqP;
				if (reqAgent.getTheAgent() != null) {
					has = true;
					break;
				}
			} else {
				ReqWorkGroup reqWorkGroup = (ReqWorkGroup) reqP;
				WorkGroup WorkGroup = reqWorkGroup.getTheWorkGroup();
				if (WorkGroup != null) {
					Collection agents = WorkGroup.getTheAgents();
					Iterator iter2 = agents.iterator();
					while (iter2.hasNext()) {
						Agent agent = (Agent) iter2.next();
						if (agent != null) {
							has = true;
							break;
						}
					}

					if (has)
						break;
				}
			}
		}
		return has;
	}

	/**
	 * Changes the state of a Task that refers to a Normal Activity.
	 */
	private void changeTasksState(Normal normal, String state) {
		Collection agents = this.getInvolvedAgents(normal);
		Iterator iter = agents.iterator();
		while (iter.hasNext()) {
			Agent agent = (Agent) iter.next();
			this.setTaskState(agent, normal, state);
		}
	}

	/**
	 * Executes an Automatic Activity.
	 */
	private void execAutomaticActivity(Automatic automatic) {
		// not implemented yet
	}

	/**
	 * Returns all the Agents that are allocated to an Normal Activity.
	 */
	private Collection getInvolvedAgents(Normal normal) {

		Collection involvedAgents = new HashSet();
		Collection reqPeople = new HashSet<RequiredPeople>();
		if (normal != null) {
			reqPeople = normal.getTheRequiredPeople();
		}
		Iterator iter = reqPeople.iterator();
		while (iter.hasNext()) {
			RequiredPeople reqP = (RequiredPeople) iter.next();
			if (reqP instanceof ReqAgent) {
				ReqAgent reqAgent = (ReqAgent) reqP;
				Agent agent = reqAgent.getTheAgent();
				if (agent != null)
					involvedAgents.add(agent);
			} else {
				ReqWorkGroup reqWorkGroup = (ReqWorkGroup) reqP;
				WorkGroup WorkGroup = reqWorkGroup.getTheWorkGroup();
				if (WorkGroup != null) {
					Collection agents = WorkGroup.getTheAgents();
					Iterator iter2 = agents.iterator();
					while (iter2.hasNext()) {
						Agent agent = (Agent) iter2.next();
						if (agent != null) {
							involvedAgents.add(agent);
						}
					}
				}
			}
		}
		return involvedAgents;
	}

	private Process getTheProcess(ProcessModel pmodel) {

		Process process = null;
		Decomposed decAct = pmodel.getTheDecomposed();

		if (decAct == null)
			process = pmodel.getTheProcess();
		else
			process = this.getTheProcess(decAct.getTheProcessModel());

		return process;
	}

	private boolean isReadyForTask(String state) {
		return (state.equals(ProcessModel.INSTANTIATED) || state
				.equals(ProcessModel.ENACTING));
	}

	/**
	 * Verifies if all the Required Resources of a Normal Activity are
	 * available.
	 */
	private boolean isAllRequiredResourceAvailable(Normal actNorm) {
		boolean ret = true;
		Collection requiredResources = actNorm.getTheRequiredResource();
		Iterator iter = requiredResources.iterator();
		while (iter.hasNext()) {
			RequiredResource reqResource = (RequiredResource) iter.next();
			if (!this.isResourceAvailable(reqResource, actNorm)) {
				ret = false;
				break;
			}
		}
		return ret;
	}

	/**
	 * Verifies if a Resource is Available and calculates the amounted used to
	 * the Comsumable Resource.
	 */
	private boolean isResourceAvailable(RequiredResource reqResource,
			Normal actNorm) {

		boolean isAvailable = false;
		Resource resource = reqResource.getTheResource();
		if (resource instanceof Exclusive) {
			Exclusive exclusive = (Exclusive) resource;
			if (exclusive.getExclusiveStatus().name()().equals(Exclusive.AVAILABLE)) {
				boolean isReserved = this.isExclResourceReserved(exclusive,
						actNorm.getPlannedBegin(), actNorm.getPlannedEnd());
				if (!isReserved) {
					isAvailable = true;
				}
			}
		} else if (resource instanceof Shareable) {
			Shareable shareable = (Shareable) resource;
			if (shareable.getExclusiveStatus().name()().equals(Shareable.AVAILABLE))
				isAvailable = true;
		} else if (resource instanceof Consumable) {
			Consumable consumable = (Consumable) resource;
			if (consumable.getExclusiveStatus().name()().equals(Consumable.AVAILABLE)) {
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
	 * Verifies if a Exclusive Resource is Reserved.
	 */
	private boolean isExclResourceReserved(Exclusive exclusive, Date actBegin,
			Date actEnd) {

		boolean isReserved = false;
		Collection reserv = exclusive.getTheReservation();
		Iterator iter = reserv.iterator();
		while (iter.hasNext()) {
			Reservation reservation = (Reservation) iter.next();
			Date reservBegin = reservation.getFrom();
			Date reservEnd = reservation.getTo();
			if ((reservBegin.getTime() <= actEnd.getTime())
					&& ((reservBegin.getTime() >= actBegin.getTime()) || (reservEnd
							.getTime() >= actBegin.getTime()))) {
				isReserved = true;
				break;
			}
		}
		return isReserved;
	}

	/**
	 * Allocates all the Required Resources for a Normal Activity.
	 */
	private void allocateAllForActivity(Normal actNorm, boolean allocConsum) {

		Collection resources = actNorm.getTheRequiredResource();
		Iterator iter = resources.iterator();
		while (iter.hasNext()) {
			RequiredResource reqResource = (RequiredResource) iter.next();
			this.allocateResource(reqResource, reqResource.getTheResource(),
					allocConsum);
		}
	}

	/**
	 * Called by APSEE Manager. Involves the rules: 12.1, 12.5, 12.6 Allocates a
	 * Required Resource to a Normal Activity, and treating if the Resource is
	 * Comsumable.
	 */
	private void allocateResource(RequiredResource reqResource, Resource res,
			boolean allocConsum) {

		if (res instanceof Exclusive) {

			Exclusive allocRes = (Exclusive) res;
			allocRes.setState(Exclusive.LOCKED);
			this.logging.registerResourceEvent(allocRes,
					reqResource.getTheNormal(), "ToLocked", "Rule 12.5"); //$NON-NLS-1$ //$NON-NLS-2$

		} else if (res instanceof Consumable) {

			Consumable allocRes = (Consumable) res;
			Float needed = reqResource.getAmountNeeded();
			Float total = allocRes.getTotalQuantity();
			Float used = allocRes.getAmountUsed();

			if (used < total) {
				used = new Float(used.floatValue() + needed.floatValue());

				if (total <= used) {
					allocRes.setAmountUsed(total);
					allocRes.setState(Consumable.FINISHED);
				} else {
					allocRes.setAmountUsed(used);
				}
			}
		} else if (res instanceof Shareable) {

			Shareable allocRes = (Shareable) res;
			this.logging.registerResourceEvent(allocRes,
					reqResource.getTheNormal(), "Requested", "Rule 12.1"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * Returns a Collection with the successors of an Activity.
	 */
	private Collection getConnectionsTo(Activity act) {

		Collection connTo = new LinkedList();
		if (act.getToSimpleCon() != null)
			connTo.addAll(act.getToSimpleCon());
		if (act.getToJoinCon() != null)
			connTo.addAll(act.getToJoinCon());
		if (act.getToBranchCon() != null)
			connTo.addAll(act.getToBranchCon());
		return connTo;
	}

	/**
	 * called by APSEE Manager. Applies to the Rules 8.1, 8.10, 8.23, 8.24,
	 *
	 * Searches for Connections the are ready to be fired.
	 */
	public void searchForFiredConnections(ProcessModel pmodel, String why) {

		Collection connections = pmodel.getTheConnection();

		while (true) {
			boolean fired = false;
			Iterator iter = connections.iterator();
			while (iter.hasNext()) {
				Connection connection = (Connection) iter.next();
				if (!(connection instanceof MultipleCon))
					continue;
				MultipleCon multiplecon = (MultipleCon) connection;
				if (multiplecon.isFired().booleanValue())
					continue;
				if (multiplecon instanceof JoinCon) {
					JoinCon join = (JoinCon) multiplecon;
					String dep = joinCon.getTheDependency().getKindDep();
					if (this.isJoinSatisfied(joinCon)) {
						joinCon.setFired(new Boolean(true));
						if (join.getKindJoinCon().equals("AND")) //$NON-NLS-1$
							if (dep.equals("end-start") || dep.equals("end-end")) //$NON-NLS-1$ //$NON-NLS-2$
								this.logging
										.registerJoinEvent(joinCon, "Rule 8.1"); //$NON-NLS-1$
							else
								// start-start
								this.logging.registerJoinEvent(joinCon,
										"Rule 8.10"); //$NON-NLS-1$
						else if (join.getKindJoinCon().equals("OR")) { //$NON-NLS-1$
							Collection pred = this.getPredecessors(joinCon);
							Iterator iterPred = pred.iterator();
							if (dep.equals("end-start") || dep.equals("end-end")) { //$NON-NLS-1$ //$NON-NLS-2$
								while (iterPred.hasNext()) {
									Object obj = (Object) iterPred.next();
									if (obj instanceof MultipleCon) {
										MultipleCon multi = (MultipleCon) obj;
										if ((multi instanceof BranchANDCon)
												&& multi.isFired()
														.booleanValue()) {
											this.logging.registerJoinConEvent(
													joinCon, "Rule 8.4"); //$NON-NLS-1$
											break;
										} else if (multi instanceof BranchConCond
												&& multi.isFired()
														.booleanValue()) {
											this.logging.registerJoinConEvent(
													joinCon, "Rule 8.3"); //$NON-NLS-1$
											break;
										} else if (multi instanceof JoinCon
												&& multi.isFired()
														.booleanValue()) {
											this.logging.registerJoinConEvent(
													joinCon, "Rule 8.5"); //$NON-NLS-1$
											break;
										}
									} else {
										this.logging.registerJoinEvent(joinCon,
												why);
										break;
									}
								}
							} else // start-start
							{
								while (iterPred.hasNext()) {
									Object obj = (Object) iterPred.next();
									if (obj instanceof MultipleCon) {
										MultipleCon multi = (MultipleCon) obj;
										if ((multi instanceof BranchANDCon)
												&& multi.isFired()
														.booleanValue()) {
											this.logging.registerJoinConEvent(
													joinCon, "Rule 8.15"); //$NON-NLS-1$
											break;
										} else if (multi instanceof BranchConCond
												&& multi.isFired()
														.booleanValue()) {
											this.logging.registerJoinConEvent(
													joinCon, "Rule 8.14"); //$NON-NLS-1$
											break;
										} else if (multi instanceof JoinCon
												&& multi.isFired()
														.booleanValue()) {
											this.logging.registerJoinConEvent(
													joinCon, "Rule 8.16"); //$NON-NLS-1$
											break;
										}
									} else {
										this.logging.registerJoinEvent(joinCon,
												why);
										break;
									}
								}
							}
						} else // XOR
						{
							Collection pred = this.getPredecessors(joinCon);
							Iterator iterPred = pred.iterator();
							if (dep.equals("end-start") || dep.equals("end-end")) { //$NON-NLS-1$ //$NON-NLS-2$
								while (iterPred.hasNext()) {
									Object obj = (Object) iterPred.next();
									if (obj instanceof MultipleCon) {
										MultipleCon multi = (MultipleCon) obj;
										if ((multi instanceof BranchANDCon)
												&& multi.isFired()
														.booleanValue()) {
											this.logging.registerJoinConEvent(
													joinCon, "Rule 8.7"); //$NON-NLS-1$
											break;
										} else if (multi instanceof BranchConCond
												&& multi.isFired()
														.booleanValue()) {
											this.logging.registerJoinConEvent(
													joinCon, "Rule 8.9"); //$NON-NLS-1$
											break;
										} else if (multi instanceof JoinCon
												&& multi.isFired()
														.booleanValue()) {
											this.logging.registerJoinConEvent(
													joinCon, "Rule 8.8"); //$NON-NLS-1$
											break;
										}
									} else {
										this.logging.registerJoinEvent(joinCon,
												why);
										break;
									}
								}
							} else// start-start
							{
								while (iterPred.hasNext()) {
									Object obj = (Object) iterPred.next();
									if (obj instanceof MultipleCon) {
										MultipleCon multi = (MultipleCon) obj;
										if ((multi instanceof BranchANDCon)
												&& multi.isFired()
														.booleanValue()) {
											this.logging.registerJoinConEvent(
													joinCon, "Rule 8.22"); //$NON-NLS-1$
											break;
										} else if (multi instanceof BranchConCond
												&& multi.isFired()
														.booleanValue()) {
											this.logging.registerJoinConEvent(
													joinCon, "Rule 8.20"); //$NON-NLS-1$
											break;
										} else if (multi instanceof JoinCon
												&& multi.isFired()
														.booleanValue()) {
											this.logging.registerJoinConEvent(
													joinCon, "Rule 8.21"); //$NON-NLS-1$
											break;
										}
									} else {
										this.logging.registerJoinEvent(joinCon,
												why);
										break;
									}
								}
							}
						}
						fired = true;
					}
				} else if (multiplecon instanceof BranchCon) {
					BranchCon branch = (BranchCon) multiplecon;
					String dep = branchCon.getTheDependency().getKindDep();
					if (this.isBranchSatisfied(branchCon)) {
						branchCon.setFired(new Boolean(true));
						if (dep.equals("end-start") || dep.equals("end-end")) //$NON-NLS-1$ //$NON-NLS-2$
							this.logging.registerBranchEvent(branchCon,
									"Rule 8.23"); //$NON-NLS-1$
						else
							// start-start
							this.logging.registerBranchEvent(branchCon,
									"Rule 8.24"); //$NON-NLS-1$
						fired = true;
					}
				}
			}
			if (!fired)
				break;
		}
	}

	/**
	 * Analises if the JoinCon is satisfied according to the logical operator and
	 * the predecessors.
	 */
	private boolean isJoinSatisfied(JoinCon joinCon) {
		boolean satisfied;

		String kindJoin = join.getKindJoinCon();
		String kindDep = joinCon.getTheDependency().getKindDep();
		Collection fromActivities = joinCon.getFromActivity();
		Collection fromMultipleCon = joinCon.getFromMultipleCon();

		Activity toActivity = joinCon.getToActivity();
		String toState = ""; //$NON-NLS-1$
		if (toActivity != null) {
			toState = this.getExclusiveStatus().name()(toActivity);
		}

		// Rules 8.1, 8.2, 8.3, 8.10 and NACs
		if (kindJoinCon.equals("AND")) { //$NON-NLS-1$
			satisfied = false;
			Iterator iter = fromActivities.iterator();
			while (iter.hasNext()) {
				Activity activity = (Activity) iter.next();
				boolean isPlain = activity instanceof Plain;
				String state = this.getExclusiveStatus().name()(activity);
				if (toState.equals(Plain.WAITING)
						|| toState.equals(Plain.READY)
						|| toState.equals(ProcessModel.INSTANTIATED)) {
					if (this.isDepSatisfiedToBegin(kindDep, state, isPlain)) {
						satisfied = true;
					} else {
						satisfied = false;
						break;
					}
				} else if (toState.equals(Plain.ACTIVE)
						|| toState.equals(ProcessModel.ENACTING)) {
					if (this.isDepSatisfiedToFinish(kindDep, state, isPlain)) {
						satisfied = true;
					} else {
						satisfied = false;
						break;
					}
				}
			}
			if (!satisfied) {
				iter = fromMultipleCon.iterator();
				while (iter.hasNext()) {
					MultipleCon multiplecon = (MultipleCon) iter.next();
					if (!multiplecon.isFired().booleanValue()) {
						if (multiplecon instanceof BranchConCond)
							satisfied = !this.isConditionSatisfied(
									(BranchConCond) multiplecon, joinCon);
						else
							satisfied = false;
						break;
					}
				}
			}
		} else // Rules 8.4, 8.5, 8.11, 8.12, 8.13, 8.14, 8.15, 8.16 and NACs
		if (kindJoinCon.equals("OR")) { //$NON-NLS-1$
			satisfied = false;
			Iterator iter = fromActivities.iterator();
			while (iter.hasNext()) {
				Activity activity = (Activity) iter.next();
				boolean isPlain = activity instanceof Plain;
				String state = this.getExclusiveStatus().name()(activity);
				if (toState.equals(Plain.WAITING)
						|| toState.equals(Plain.READY)
						|| toState.equals(ProcessModel.INSTANTIATED)) {
					if (this.isDepSatisfiedToBegin(kindDep, state, isPlain)) {
						satisfied = true;
						break;
					}
				} else if (toState.equals(Plain.ACTIVE)
						|| toState.equals(ProcessModel.ENACTING)) {
					if (this.isDepSatisfiedToFinish(kindDep, state, isPlain)) {
						satisfied = true;
						break;
					}
				}
			}
			if (!satisfied) {
				iter = fromMultipleCon.iterator();
				while (iter.hasNext()) {
					MultipleCon multiplecon = (MultipleCon) iter.next();
					if (multiplecon.isFired().booleanValue()) {
						if (multiplecon instanceof BranchConCond)
							satisfied = this.isConditionSatisfied(
									(BranchConCond) multiplecon, joinCon);
						else
							satisfied = true;
						break;
					}
				}
			}
		} else // Rules 8.7, 8.8, 8.9, 8.17, 8.18, 8.19 and NACs
		if (kindJoinCon.equals("XOR")) { //$NON-NLS-1$
			int trueCount = 0;
			Iterator iter = fromActivities.iterator();
			while (iter.hasNext()) {
				Activity activity = (Activity) iter.next();
				boolean isPlain = activity instanceof Plain;
				String state = this.getExclusiveStatus().name()(activity);
				if (toState.equals(Plain.WAITING)
						|| toState.equals(Plain.READY)
						|| toState.equals(ProcessModel.INSTANTIATED)) {
					if (this.isDepSatisfiedToBegin(kindDep, state, isPlain)) {
						trueCount++;
					}
				} else if (toState.equals(Plain.ACTIVE)
						|| toState.equals(ProcessModel.ENACTING)) {
					if (this.isDepSatisfiedToFinish(kindDep, state, isPlain)) {
						trueCount++;
					}
				}
			}
			iter = fromMultipleCon.iterator();
			while (iter.hasNext()) {
				MultipleCon multiplecon = (MultipleCon) iter.next();
				if (multiplecon.isFired().booleanValue()) {
					if (multiplecon instanceof BranchConCond)
						if (this.isConditionSatisfied((BranchConCond) multiplecon,
								joinCon))
							trueCount++;
				}
			}
			satisfied = (trueCount == 1);
		} else
			satisfied = false;

		return (satisfied);
	}

	/**
	 * Analises if the BranchCon is satisfied according to the logical operator and
	 * the predecessors.
	 */
	private boolean isBranchSatisfied(BranchCon branchCon) {

		boolean satisfied;

		Activity activity = branchCon.getFromActivity();
		MultipleCon multiplecon = branchCon.getFromMultipleConnection();

		if (activity != null) {
			boolean isPlain = activity instanceof Plain;
			String state = this.getExclusiveStatus().name()(activity);
			String kindDep = branchCon.getTheDependency().getKindDep();
			satisfied = this.isDepSatisfiedToBegin(kindDep, state, isPlain);
		} else if (multiplecon != null) {
			satisfied = multiplecon.isFired().booleanValue();
		} else
			satisfied = false;

		return (satisfied);
	}

	/**
	 * Returns a Collection with the predecessors (Activities and Multiple
	 * Connection) of a Connection.
	 */
	private Collection getPredecessors(Connection conn) {

		Collection pred = new LinkedList();
		if (conn instanceof Sequence) {
			Sequence seq = (Sequence) conn;
			if (seq.getFromActivity() != null)
				pred.add(seq.getFromActivity());
		} else if (conn instanceof BranchCon) {
			BranchCon branch = (BranchCon) conn;
			if (branchCon.getFromActivity() != null)
				pred.add(branchCon.getFromActivity());
			if (branchCon.getFromMultipleConnection() != null)
				pred.add(branchCon.getFromMultipleConnection());
		} else if (conn instanceof JoinCon) {
			JoinCon join = (JoinCon) conn;
			if (joinCon.getFromActivity() != null)
				pred.addAll(joinCon.getFromActivity());
			if (joinCon.getFromMultipleCon() != null)
				pred.addAll(joinCon.getFromMultipleCon());
		}
		return pred;
	}

	/**
	 * Returns the state of an Activity, even if it is Plain or Decomposed.
	 */
	private String getExclusiveStatus().name()(Activity activity) {
		String state;
		if (activity instanceof Plain) {
			Plain plain = (Plain) activity;
			state = plain.getTheEnactionDescription().getExclusiveStatus().name()();
		} else { // decomposed
			Decomposed decomposed = (Decomposed) activity;
			state = decomposed.getTheReferedProcessModel().getPmState();
		}
		return (state);
	}

	/**
	 * Implements NACS of rules 2.1 and 2.2. Analises the Dependencies of the
	 * Connections.
	 */
	private boolean isDepSatisfiedToBegin(String kindDep, String state,
			boolean isPlain) {
		boolean satisfied;
		if (isPlain) {
			if (kindDep.equals("end-start")) { //$NON-NLS-1$
				// NAC 08 of Rule 2.1 & 2.2
				satisfied = state.equals(Plain.FINISHED);
			} else if (kindDep.equals("end-end")) { //$NON-NLS-1$
				satisfied = true;
			} else if (kindDep.equals("start-start")) { //$NON-NLS-1$
				// NAC 06 of Rule 2.1 & 2.2
				satisfied = (state.equals(Plain.ACTIVE)
						|| state.equals(Plain.PAUSED) || state
						.equals(Plain.FINISHED));
			} else
				satisfied = false;
		} else { // decomposed
			if (kindDep.equals("end-start")) { //$NON-NLS-1$
				// NAC 06 of Rule 2.1
				satisfied = state.equals(ProcessModel.FINISHED);
			} else if (kindDep.equals("end-end")) { //$NON-NLS-1$
				satisfied = true;
			} else if (kindDep.equals("start-start")) { //$NON-NLS-1$
				// NAC 07 of Rule 2.1 & 2.2
				satisfied = (state.equals(ProcessModel.ENACTING) || state
						.equals(ProcessModel.FINISHED));
			} else
				satisfied = false;
		}
		return (satisfied);
	}

	private boolean isDepSatisfiedToFinish(String kindDep, String state,
			boolean isPlain) {
		boolean satisfied;
		if (isPlain) {
			if (kindDep.equals("end-start")) { //$NON-NLS-1$
				// NAC 08 of Rule 2.1 & 2.2
				satisfied = state.equals(Plain.FINISHED);
			} else if (kindDep.equals("end-end")) { //$NON-NLS-1$
				satisfied = state.equals(Plain.FINISHED);
			} else if (kindDep.equals("start-start")) { //$NON-NLS-1$
				// NAC 06 of Rule 2.1 & 2.2
				satisfied = (state.equals(Plain.ACTIVE)
						|| state.equals(Plain.PAUSED) || state
						.equals(Plain.FINISHED));
			} else
				satisfied = false;
		} else { // decomposed
			if (kindDep.equals("end-start")) { //$NON-NLS-1$
				// NAC 06 of Rule 2.1
				satisfied = state.equals(ProcessModel.FINISHED);
			} else if (kindDep.equals("end-end")) { //$NON-NLS-1$
				satisfied = state.equals(ProcessModel.FINISHED);
			} else if (kindDep.equals("start-start")) { //$NON-NLS-1$
				// NAC 07 of Rule 2.1 & 2.2
				satisfied = (state.equals(ProcessModel.ENACTING) || state
						.equals(ProcessModel.FINISHED));
			} else
				satisfied = false;
		}
		return (satisfied);
	}

	/**
	 * called by APSEE Manager. Applies to the rules 7.1, 7.2, 7.3, 7.4, 7.5,
	 * 7.6, 7.11, Evaluates the conditions of the BranchCones if the successor is
	 * an Activity.
	 */
	private boolean isConditionSatisfied(BranchConCond branchConcond,
			Activity activity) {

		boolean satisfied = false;
		Collection conditions = branchcond.getTheBranchConCondToActivity();
		Iterator iter = conditions.iterator();
		while (iter.hasNext()) {
			BranchConCondToActivity condition = (BranchConCondToActivity) iter.next();
			if (condition.getTheActivity().equals(activity)) {
				satisfied = this.conditionValue(condition.getTheCondition());
				break;
			}
		}
		return (satisfied);
	}

	/**
	 * Called by APSEE Manager. Applies to the rules 7.6, 7.7, 7.8, 7.9, 7.10,
	 * 7.11, 7.12, Evaluates the conditions of the BranchCones if the successor is
	 * a Multiple Connection.
	 */
	private boolean isConditionSatisfied(BranchConCond branchConcond,
			MultipleCon multipleCon) {
		boolean satisfied = false;
		Collection conditions = branchcond.getTheBranchConCondToMultipleCon();
		Iterator iter = conditions.iterator();
		while (iter.hasNext()) {
			BranchConCondToMultipleCon condition = (BranchConCondToMultipleCon) iter
					.next();
			if (condition.getTheMultipleCon().equals(multipleCon)) {
				satisfied = this.conditionValue(condition.getTheCondition());
				break;
			}
		}
		return (satisfied);
	}

	/**
	 * Evaluates the condition of a Conditional BranchCon, Feedback connections...
	 */
	private boolean conditionValue(PolCondition cond) {

		/*
		 * String condition = cond.getCond(); // MISSING: waiting for conditions
		 * implementation return (condition.equals ("true")); //$NON-NLS-1$
		 */
		try {
			// PolObjValueEval evalCond = ConditionEval.evalCondition("",cond);
			// if (evalCond == null)
			// throw new Exception(
			// "It was not possible to evaluate the condition.");
			// else {
			// return ((Boolean) evalCond.getCurrentObj()).booleanValue();
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private AgendaEventsDTO convertAgentsToAgentsDTO(List<AgendaEvent> agentList ){
		List<AgendaEventDTO> agente =  new ArrayList<AgendaEventDTO>();
		for (AgendaEvent agent : agentList) {
			agente.add(this.convertAgentToAgentDTO(agent));
		}

		return new AgendaEventsDTO(agente);



	}

	private AgendaEventDTO convertAgentToAgentDTO(AgendaEvent agent) {
		AgendaEventDTO agentDTO = new AgendaEventDTO();
		try {
			agentDTO = (AgendaEventDTO) converter.getDTO(agent, AgendaEventDTO.class);
			return agentDTO;

		} catch (ImplementationException e) {
			e.printStackTrace();
		}

		return null;
	}
	public List<AgendaEventDTO> getAgendaEventsForTask(String normalIdent,String agentIdent) {
		List<AgendaEventDTO> agendaEventDTO = new ArrayList<AgendaEventDTO>();
		String hql_project = "SELECT agenda FROM "
				+ AGENDAEVENT_CLASSNAME
				+ " as agenda where agenda.theTask.theProcessAgenda.theTaskAgenda.theAgent.ident=:agentID  "
				+ "and agenda.theTask.theNormal.ident=:normalID order by agenda.oid";

		query = agendaEventDAO.getPersistenceContext().createQuery(hql_project);
		query.setParameter("agentID", agentIdent);
		query.setParameter("normalID", normalIdent);
		List<AgendaEvent> result = query.getResultList();
		for (AgendaEvent task : result) {
				AgendaEventDTO t = new AgendaEventDTO(task.getTheCatalogEvents().getDescription(),task.getWhen());
				agendaEventDTO.add(t);
		}

		System.out.println(query.getResultList());

		return agendaEventDTO;

	}

}

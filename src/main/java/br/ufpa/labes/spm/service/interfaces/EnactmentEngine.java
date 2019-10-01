/*
 * Created on 29/03/2005
 */
package br.ufpa.labes.spm.service.interfaces;


import br.ufpa.labes.spm.exceptions.WebapseeException;

/**
 * @author Breno Franca (brenofranca@yahoo.com.br) and Heribert Schlebbe�
 *         (schlebbe@informatik.uni-stuttgart.de)
 *         LABES - Software Engineering Laboratory, Federal University of Para, Belem, Para, Brazil
 *         Faculty of Computer Science,
 *         University of Stuttgart, Stuttgart,Baden-W�rttemberg, Germany
 *
 * @since April/2005
 * @version 1.0
 */

public interface EnactmentEngine {

	/**
	 * This method is called by the Process Manager. It will create the tasks in
	 * the Task Agendas of the Agents (Developers) with state Waiting, set the
	 * state of the Process to Enacting, and, according to the Multiple
	 * Connections and Activities that are ready to begin, the Process Model
	 * state is determined. So, the execution continues till the state of the
	 * Process has been setted to Finished.
	 *
	 * @return void
	 * @param String
	 *            process_id
	 */
	public void executeProcess(String process_id) throws WebapseeException;

	/**
	 * This method is called by the Process Manager. It will reset all the
	 * components of the Process, including the Process Model, Acivities,
	 * Connections, Resources, Process Agendas, and setting their states to the
	 * initial state.
	 *
	 * @return void
	 * @param String
	 *            process_id
	 */
	public void resetProcess(String process_id) throws WebapseeException;

	/**
	 * This method is called by an Agent of the Process to begin a Task in his
	 * Process Agenda. It will set the Task state to Active in his Agenda and in
	 * the Proces Model if it's not Active. And, if the Activity is paused, the
	 * Activity is re-started.
	 *
	 * @return void
	 * @param String
	 *            agent_id, String act_id
	 */
	public void beginTask(String agent_id, String act_id) throws WebapseeException;

	/**
	 * This method is called by an Agent of the Process to finish a Task in his
	 * Process Agenda. It will set the Task state to Finished in his Agenda, and
	 * on the Proces Model if all the Agents that are required for this Activity
	 * finish the Task also. In this method there is a particular situation.
	 * When an Activity finishes, if it is a Feedback Connection source and the
	 * associated condition of the Feedback Connection is satisfied (true),
	 * then, Feedback will be executed till the condition turns to not satisfied
	 * (false).
	 *
	 * @return void
	 * @param String
	 *            agent_id, String act_id
	 */
	public void finishTask(String agent_id, String act_id) throws WebapseeException;

	/**
	 * This method is called by an Agent of the Process to pause a Task in his
	 * Process Agenda. It will set the Task state to Paused in his Agenda, and
	 * on the Proces Model if all the Agents that are required for this Activity
	 * pause the Task also.
	 *
	 * @return void
	 * @param String
	 *            agent_id, String act_id
	 */
	public void pauseTask(String agent_id, String act_id) throws WebapseeException;

	public void pauseActiveTasks(String agentIdent) throws WebapseeException;

	/**
	 * This method is called by an Agent of the Process to delegate a Task in
	 * his Process Agenda to another Agent. It will set the Task state to
	 * Delegated in his Agenda, and created on the Process Agenda of the other
	 * Agent.
	 *
	 * @return void
	 * @param String
	 *            from_agent_id, String act_id, String to_agent_id
	 */
	public void delegateTask(String from_agent_id, String act_id, String to_agent_id) throws WebapseeException;

	/**
	 * This method is called by the Process Manager to fail an Activity. It will
	 * set the Activity state to Failed and will propagate the fail for the
	 * successors according to their state, in case of an Activity. And, fail
	 * the Connections in case of a Multiple Connection (BranchCon or JoinCon). In
	 * this method there is a particular situation also. When an Activity fail,
	 * if it's a Feedback Connection source and the condition of the Feedback
	 * Connection is satisfied (true), then, Feedback will be executed till the
	 * condition turns to not satisfied (false). To Fail an Activity it should
	 * be Active or Paused.
	 *
	 * @return void
	 * @param String
	 *            act_id
	 */
	public void failActivity(String act_id) throws WebapseeException;

	/**
	 * This method is called by the Process Manager to cancel an Activity. It
	 * will set the Activity state to Canceled and will propagate the cancel for
	 * the successors according to their state, in case of an Activity. And,
	 * cancel the Connections in case of a Multiple Connection (BranchCon or JoinCon).
	 * To Cancel an Activity it should be Waiting or Ready.
	 *
	 * @return void
	 * @param String
	 *            act_id
	 */
	public void cancelActivity(String act_id) throws WebapseeException;

	/**
	 * This method is called by the Process Manager to make a Shareable Resource
	 * Unavailable. It will set the Resource state to Not Available.
	 *
	 * @return void
	 * @param String
	 *            resource_id
	 */
	public void makeUnavailable(String resource_id) throws WebapseeException;

	/**
	 * This method is called by the Process Manager to make a Shareable Resource
	 * Available. It will set the Resource state to Available.
	 *
	 * @return void
	 * @param String
	 *            resource_id
	 */
	public void makeAvailable(String resource_id) throws WebapseeException;

	/**
	 * This method is called by the Process Manager to register a defect on a
	 * Exclusive Resource. It will set the Resource state to Defect.
	 *
	 * @return void
	 * @param String
	 *            resource_id
	 */
	public void registerDefect(String resource_id) throws WebapseeException;

	// New Public Methods

	public void createNewVersion(String act_id) throws WebapseeException;
}

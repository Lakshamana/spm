package br.ufpa.labes.spm.service.interfaces;

import javax.ejb.Local;

import br.ufpa.labes.spm.exceptions.DAOException;
import br.ufpa.labes.spm.exceptions.WebapseeException;
import br.ufpa.labes.spm.domain.Normal;
import br.ufpa.labes.spm.domain.ProcessModel;

@Local
public interface EnactmentEngineLocal {

	public void searchForFiredConnections(Integer pmodel_id, String why) throws WebapseeException;

	public void searchForReadyActivities (Integer pmodel_id) throws WebapseeException;

	public void determineProcessModelStates (ProcessModel processModel)	throws WebapseeException;

	public boolean isActivityFinished(Normal act);

	public void finishTask (Normal actNorm) throws WebapseeException, DAOException;

	public void createTasks(ProcessModel procModel) throws WebapseeException;

	public void createTasksNormal(Normal actNorm) throws WebapseeException;




	public void executeProcess (String process_id) throws WebapseeException, DAOException;

	/**
	 * This method is called by the Process Manager to fail an Activity. It will
	 * set the Activity state to Failed and will propagate the fail for the
	 * successors according to their state, in case of an Activity. And, fail
	 * the Connections in case of a Multiple Connection (Branch or JoinCon). In
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
	 * Rule G1.10 and G1.11
	 */
	public void createNewVersion(String act_id) throws WebapseeException;

	/**
     * called by APSEE Manager.
     * begin with rules 1.1 - 1.4
     */


}

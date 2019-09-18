package br.ufpa.labes.spm.service.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;


import br.ufpa.labes.spm.repository.impl.plainActivities.AutomaticDAO;
import br.ufpa.labes.spm.repository.impl.types.ArtifactTypeDAO;
import br.ufpa.labes.spm.repository.interfaces.activities.IActivityDAO;
import br.ufpa.labes.spm.repository.interfaces.activities.IDecomposedDAO;
import br.ufpa.labes.spm.repository.interfaces.agent.IAgentDAO;
import br.ufpa.labes.spm.repository.interfaces.agent.IGroupDAO;
import br.ufpa.labes.spm.repository.interfaces.agent.IRoleDAO;
import br.ufpa.labes.spm.repository.interfaces.artifacts.IArtifactDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IArtifactConDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IBranchCondToActivityDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IBranchCondToMultipleConDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IBranchDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IConnectionDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IJoinDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IMultipleConDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.ISimpleConDAO;
import br.ufpa.labes.spm.repository.interfaces.plainActivities.IAutomaticDAO;
import br.ufpa.labes.spm.repository.interfaces.plainActivities.IInvolvedArtifactsDAO;
import br.ufpa.labes.spm.repository.interfaces.plainActivities.INormalDAO;
import br.ufpa.labes.spm.repository.interfaces.plainActivities.IParametersDAO;
import br.ufpa.labes.spm.repository.interfaces.plainActivities.IReqAgentDAO;
import br.ufpa.labes.spm.repository.interfaces.plainActivities.IReqGroupDAO;
import br.ufpa.labes.spm.repository.interfaces.plainActivities.IRequiredResourceDAO;
import br.ufpa.labes.spm.repository.interfaces.policies.staticPolicies.IPolConditionDAO;
import br.ufpa.labes.spm.repository.interfaces.processModels.IProcessDAO;
import br.ufpa.labes.spm.repository.interfaces.processModels.IProcessModelDAO;
import br.ufpa.labes.spm.repository.interfaces.resources.IConsumableDAO;
import br.ufpa.labes.spm.repository.interfaces.resources.IResourceDAO;
import br.ufpa.labes.spm.repository.interfaces.taskagenda.IProcessAgendaDAO;
import br.ufpa.labes.spm.repository.interfaces.taskagenda.ITaskDAO;
import br.ufpa.labes.spm.repository.interfaces.tools.ISubroutineDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IArtifactTypeDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IGroupTypeDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IResourceTypeDAO;
import br.ufpa.labes.spm.service.dto.WebapseeObjectDTO;
import br.ufpa.labes.spm.exceptions.DAOException;
import br.ufpa.labes.spm.exceptions.ModelingException;
import br.ufpa.labes.spm.exceptions.ModelingWarning;
import br.ufpa.labes.spm.exceptions.WebapseeException;
import br.ufpa.labes.spm.domain.Activity;
import br.ufpa.labes.spm.domain.Decomposed;
import br.ufpa.labes.spm.domain.Plain;
import br.ufpa.labes.spm.domain.Agent;
import br.ufpa.labes.spm.domain.AgentPlaysRole;
import br.ufpa.labes.spm.domain.Group;
import br.ufpa.labes.spm.domain.Role;
import br.ufpa.labes.spm.domain.Artifact;
import br.ufpa.labes.spm.domain.ArtifactCon;
import br.ufpa.labes.spm.domain.Branch;
import br.ufpa.labes.spm.domain.BranchAND;
import br.ufpa.labes.spm.domain.BranchCond;
import br.ufpa.labes.spm.domain.BranchCondToActivity;
import br.ufpa.labes.spm.domain.BranchCondToMultipleCon;
import br.ufpa.labes.spm.domain.Connection;
import br.ufpa.labes.spm.domain.Dependency;
import br.ufpa.labes.spm.domain.Feedback;
import br.ufpa.labes.spm.domain.Join;
import br.ufpa.labes.spm.domain.MultipleCon;
import br.ufpa.labes.spm.domain.Sequence;
import br.ufpa.labes.spm.domain.SimpleCon;
import br.ufpa.labes.spm.domain.ModelingActivityEvent;
import br.ufpa.labes.spm.domain.Automatic;
import br.ufpa.labes.spm.domain.EnactionDescription;
import br.ufpa.labes.spm.domain.InvolvedArtifacts;
import br.ufpa.labes.spm.domain.Normal;
import br.ufpa.labes.spm.domain.Parameters;
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
import br.ufpa.labes.spm.domain.Resource;
import br.ufpa.labes.spm.domain.Shareable;
import br.ufpa.labes.spm.domain.ProcessAgenda;
import br.ufpa.labes.spm.domain.Task;
import br.ufpa.labes.spm.domain.TaskAgenda;
import br.ufpa.labes.spm.domain.ClassMethodCall;
import br.ufpa.labes.spm.domain.Script;
import br.ufpa.labes.spm.domain.Subroutine;
import br.ufpa.labes.spm.domain.ActivityType;
import br.ufpa.labes.spm.domain.ArtifactType;
import br.ufpa.labes.spm.domain.GroupType;
import br.ufpa.labes.spm.domain.ResourceType;
import br.ufpa.labes.spm.domain.Type;
import br.ufpa.labes.spm.service.interfaces.DynamicModeling;
import br.ufpa.labes.spm.service.interfaces.EnactmentEngineLocal;
import br.ufpa.labes.spm.service.interfaces.Logging;
import br.ufpa.labes.spm.service.interfaces.NotificationServices;
import org.qrconsult.spm.util.i18n.Messages;

public class DynamicModelingImpl implements DynamicModeling {

	// private static String UPT = "UPT";
	private static String ADD = "ADD";
	private static String DEL = "DEL";
	private static String DIRECTION_OUT = "OUT";
	private static String DIRECTION_IN = "IN";

	EnactmentEngineLocal enactmentEngine;
	private Logging logging;
	public NotificationServices remote;
	IProcessDAO procDAO;
	IDecomposedDAO decDAO;
	IActivityDAO actDAO;
	INormalDAO normDAO;
	IAutomaticDAO autoDAO;
	IArtifactDAO artDAO;
	IProcessModelDAO pmodelDAO;
	ISubroutineDAO subDAO;
	IParametersDAO paramDAO;
	IArtifactConDAO artConDAO;
	IArtifactTypeDAO artTypeDAO;
	IInvolvedArtifactsDAO invArtDAO;
	IMultipleConDAO multiDAO;
	IConnectionDAO conDAO;
	IPolConditionDAO polConditionDAO;
	IBranchCondToMultipleConDAO bctmcDAO;
	IJoinDAO joinDAO;
	IBranchDAO branchDAO;
	IGroupTypeDAO groupTypeDAO;
	IRoleDAO roleDAO;
	IReqAgentDAO reqAgentDAO;
	IAgentDAO agentDAO;
	ITaskDAO taskDAO;
	IGroupDAO groupDAO;
	IReqGroupDAO reqGroupDAO;
	IResourceTypeDAO resTypeDAO;
	IRequiredResourceDAO reqResDAO;
	IResourceDAO resDAO;
	IConsumableDAO consumableDAO;
	IBranchCondToActivityDAO branchCondToActivityDAO;
	ISimpleConDAO simpleDAO;
	IProcessAgendaDAO pAgendaDAO;

	@Override
	public void imprimirNoConsole(String mensagem) {
		System.out.println("Mensagem vinda do Flex!");
		System.out.println(mensagem);
	}

	@Override
	public Integer newNormalActivity(String level_id, String new_id) throws WebapseeException {
		// Checks for the parameters
		StringTokenizer st = new StringTokenizer(level_id, "."); //$NON-NLS-1$
		String process_id = st.nextToken();

		Object proc;
		try {
			proc = procDAO.retrieveBySecondaryKey(process_id);
		} catch (Exception/* era DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccProcess") + //$NON-NLS-1$
					process_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (proc == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcProcess") + process_id + Messages.getString("facades.DynamicModeling.DaoExc_NotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Process process = (Process) proc;

		ProcessModel pmodel = null;

		Decomposed actDecomposed = null; // it is used only if the new activity
		// Is not in the root process model.
		if (st.hasMoreTokens()) {
			String currentModel = process_id;
			while (st.hasMoreTokens()) {
				currentModel += "." + st.nextToken(); //$NON-NLS-1$
			}
			Object dec = null;
			try {
				dec = decDAO.retrieveBySecondaryKey(currentModel);
			} catch (Exception/* DAOException */e1) {
				throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActiv") + //$NON-NLS-1$
						currentModel + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e1); //$NON-NLS-1$
			}

			if (dec == null)
				throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDecomActv") + currentModel //$NON-NLS-1$
						+ Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$

			actDecomposed = (Decomposed) dec;

			pmodel = actDecomposed.getTheReferedProcessModel();
		} else {
			pmodel = process.getTheProcessModel();
			System.out.println("modelo: "+pmodel.getPmState());
		}

		// End Checks for the parameters

		// Now we start the implementation of the rules

		String pmState = pmodel.getPmState();
		if (!pmState.equals(ProcessModel.INSTANTIATED) && !pmState.equals(ProcessModel.ENACTING) && !pmState.equals(ProcessModel.ABSTRACT)
				&& !pmState.equals(ProcessModel.REQUIREMENTS)) {
			throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcNotAdded")); //$NON-NLS-1$
		}

		if ((process.getPState().equals(Process.ENACTING) || process.getPState().equals(Process.NOT_STARTED))) {

			if (!this.isActivityInProcessModel(level_id + "." + new_id, pmodel)) { //$NON-NLS-1$
System.out.println("salva :"+pmodel.getPmState());
				Normal actNorm = new Normal();
				actNorm.setIdent(level_id + "." + new_id); //$NON-NLS-1$
				actNorm.setName(new_id);
				actNorm.setTheProcessModel(pmodel);
				pmodel.getTheActivity().add(actNorm);

				// Persistence Operations

				procDAO.update(process);
				if (actDecomposed != null)
					decDAO.update(actDecomposed);

				return actNorm.getOid();

			} else {

				throw new ModelingException(
						Messages.getString("facades.DynamicModeling.ModelingExcActv") + new_id + Messages.getString("facades.DynamicModeling.ModelingExcIsAlrInModel")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else {

			throw new ModelingException(
					Messages.getString("facades.DynamicModeling.DaoExcProcess") + process_id + Messages.getString("facades.DynamicModeling.ModelingExcIsNOtReadyToIn")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}


	@Override
	public Integer newDecomposedActivity(String level_id, String new_id) throws WebapseeException {

		// Checks for the parameters

		StringTokenizer st = new StringTokenizer(level_id, "."); //$NON-NLS-1$
		String process_id = st.nextToken();

		Object proc;
		try {
			proc = procDAO.retrieveBySecondaryKey(process_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccProcess") + //$NON-NLS-1$
					process_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (proc == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcProcess") + process_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Process process = (Process) proc;

		ProcessModel pmodel = null;

		Decomposed actDecomposed = null; // it is used only if the new activity
		// Is not in the root process model.
		if (st.hasMoreTokens()) {
			String currentModel = process_id;
			while (st.hasMoreTokens()) {
				currentModel += "." + st.nextToken(); //$NON-NLS-1$
			}
			Object dec = null;
			try {
				dec = decDAO.retrieveBySecondaryKey(currentModel);
			} catch (Exception/* DAOException */e1) {
				throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActiv") + //$NON-NLS-1$
						currentModel + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e1); //$NON-NLS-1$
			}

			if (dec == null)
				throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDecomActv") + currentModel //$NON-NLS-1$
						+ Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$

			actDecomposed = (Decomposed) dec;

			pmodel = actDecomposed.getTheReferedProcessModel();
		} else {
			pmodel = process.getTheProcessModel();
		}

		// End Checks for the parameters

		// Now we start the implementation of the rules

		String pmState = pmodel.getPmState();
		if (!pmState.equals(ProcessModel.INSTANTIATED) && !pmState.equals(ProcessModel.ENACTING) && !pmState.equals(ProcessModel.ABSTRACT)
				&& !pmState.equals(ProcessModel.REQUIREMENTS)) {
			throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcNotAdded")); //$NON-NLS-1$
		}

		if ((process.getPState().equals(Process.ENACTING) || process.getPState().equals(Process.NOT_STARTED))) {

			if (!this.isActivityInProcessModel(level_id + "." + new_id, pmodel)) { //$NON-NLS-1$

				Decomposed actDec = new Decomposed();
				actDec.setIdent(level_id + "." + new_id); //$NON-NLS-1$
				actDec.setName(new_id);
				actDec.setTheProcessModel(pmodel);
				pmodel.getTheActivity().add(actDec);

				ProcessModel refProcModel = actDec.getTheReferedProcessModel();
				refProcModel.setTheDecomposed(actDec);

				// Persistence Operations

				procDAO.update(process);
				if (actDecomposed != null)
					decDAO.update(actDecomposed);

				return actDec.getOid();
			} else {

				throw new ModelingException(
						Messages.getString("facades.DynamicModeling.ModelingExcActv") + new_id + Messages.getString("facades.DynamicModeling.ModelingExcIsAlrInModel")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else {

			throw new ModelingException(
					Messages.getString("facades.DynamicModeling.DaoExcProcess") + process_id + Messages.getString("facades.DynamicModeling.ModelingExcIsNOtReadyToIn")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}


	@Override
	public Integer newAutomaticActivity(String level_id, String new_id) throws WebapseeException {

		// Checks for the parameters

		StringTokenizer st = new StringTokenizer(level_id, "."); //$NON-NLS-1$
		String process_id = st.nextToken();

		Object proc;
		try {
			proc = procDAO.retrieveBySecondaryKey(process_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccProcess") + //$NON-NLS-1$
					process_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (proc == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcProcess") + process_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Process process = (Process) proc;

		ProcessModel pmodel = null;

		Decomposed actDecomposed = null; // it is used only if the new activity
		// Is not in the root process model.
		if (st.hasMoreTokens()) {
			String currentModel = process_id;
			while (st.hasMoreTokens()) {
				currentModel += "." + st.nextToken(); //$NON-NLS-1$
			}
			Object dec = null;
			try {
				dec = decDAO.retrieveBySecondaryKey(currentModel);
			} catch (Exception/* DAOException */e1) {
				throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActiv") + //$NON-NLS-1$
						currentModel + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e1); //$NON-NLS-1$
			}

			if (dec == null)
				throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDecomActv") + currentModel //$NON-NLS-1$
						+ Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$

			actDecomposed = (Decomposed) dec;

			pmodel = actDecomposed.getTheReferedProcessModel();
		} else {
			pmodel = process.getTheProcessModel();
		}

		// End Checks for the parameters

		// Now we start the implementation of the rules

		String pmState = pmodel.getPmState();
		if (!pmState.equals(ProcessModel.INSTANTIATED) && !pmState.equals(ProcessModel.ENACTING) && !pmState.equals(ProcessModel.ABSTRACT)
				&& !pmState.equals(ProcessModel.REQUIREMENTS)) {
			throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcNotAdded")); //$NON-NLS-1$
		}

		if (process.getPState().equals(Process.NOT_STARTED)) {

			if (!this.isActivityInProcessModel(level_id + "." + new_id, pmodel)) { //$NON-NLS-1$

				Automatic actAuto = new Automatic();
				actAuto.setIdent(level_id + "." + new_id); //$NON-NLS-1$
				actAuto.setName(new_id);
				actAuto.setTheProcessModel(pmodel);
				pmodel.getTheActivity().add(actAuto);

				// Persistence Operations

				procDAO.update(process);
				if (actDecomposed != null)
					decDAO.update(actDecomposed);

				return actAuto.getOid();

			} else {
				throw new ModelingException(
						Messages.getString("facades.DynamicModeling.ModelingExcActv") + new_id + Messages.getString("facades.DynamicModeling.ModelingExcIsAlrInModel")); //$NON-NLS-1$ //$NON-NLS-2$
			}

		} else if (process.getPState().equals(Process.ENACTING)) {

			if (!this.isActivityInProcessModel(level_id + "." + new_id, pmodel)) { //$NON-NLS-1$

				Automatic actAuto = new Automatic();
				actAuto.setIdent(level_id + "." + new_id); //$NON-NLS-1$
				actAuto.setName(new_id);
				actAuto.getTheEnactionDescription().setStateWithMessage(Plain.WAITING);
				actAuto.setTheProcessModel(pmodel);
				pmodel.getTheActivity().add(actAuto);

				// Persistence Operations

				procDAO.update(process);
				if (actDecomposed != null)
					decDAO.update(actDecomposed);

				return actAuto.getOid();

			} else {
				throw new ModelingException(
						Messages.getString("facades.DynamicModeling.ModelingExcActv") + new_id + Messages.getString("facades.DynamicModeling.ModelingExcIsAlrInModel")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else {
			throw new ModelingException(
					Messages.getString("facades.DynamicModeling.DaoExcProcess") + process_id + Messages.getString("facades.DynamicModeling.ModelingExcIsNOtReadyToIn")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}


	@Override
	public Integer defineAsDecomposed(String act_id) throws DAOException, ModelingException {

		// Checks for the parameters

		Object act;
		try {
			act = actDAO.retrieveBySecondaryKey(act_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActv") + //$NON-NLS-1$
					act_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (act == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Activity activity = (Activity) act;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		if (activity instanceof Decomposed)
			throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcActv") + activity.getIdent() //$NON-NLS-1$
					+ Messages.getString("facades.DynamicModeling.ModelingExcIsAlrDecActv")); //$NON-NLS-1$

		else if (activity instanceof Normal) { // Rule G1.4
			Normal actNorm = (Normal) activity;

			if (actNorm.getTheEnactionDescription().getState().equals("")) { //$NON-NLS-1$

				if (this.hasInvolvedAgents(actNorm)) {
					actNorm.setTheRequiredPeople(new HashSet());
				}
				Collection set = actNorm.getTheRequiredResource();
				set.remove(null);
				if (!set.isEmpty()) {
					this.releaseResourcesFromActivity(actNorm);
					actNorm.setTheRequiredResource(new HashSet());
				}

				String identAux = actNorm.getIdent();
				actNorm.setIdent(""); //$NON-NLS-1$

				// Persistence Operation
				actDAO.update(actNorm);

				Decomposed actDecomp = new Decomposed();
				actDecomp.setIdent(identAux);
				actDecomp.setName(actNorm.getName());

				this.copyActivityRelationships(actNorm, actDecomp);

				ProcessModel refProcModel = actDecomp.getTheReferedProcessModel();
				refProcModel.setTheDecomposed(actDecomp);

				// Persistence Operations

				normDAO.delete(actNorm);

				actDAO.save(actDecomp);

				return actDecomp.getOid();
			} else {

				throw new ModelingException(
						Messages.getString("facades.DynamicModeling.ModelingExcActv") + activity.getIdent() + Messages.getString("facades.DynamicModeling.ModelingExcIsAlrRun")); //$NON-NLS-1$ //$NON-NLS-2$
			}

		} else if (activity instanceof Automatic) { // Rule G1.5
			Automatic actAuto = (Automatic) activity;

			if (actAuto.getTheEnactionDescription().getState().equals("") //$NON-NLS-1$
					&& actAuto.getTheSubroutine() == null) {

				String identAux = actAuto.getIdent();
				actAuto.setIdent(""); //$NON-NLS-1$

				// Persistence Operation
				actDAO.update(actAuto);

				Decomposed actDecomp = new Decomposed();
				actDecomp.setIdent(identAux);
				actDecomp.setName(actAuto.getName());

				this.copyActivityRelationships(actAuto, actDecomp);

				ProcessModel refProcModel = actDecomp.getTheReferedProcessModel();
				refProcModel.setTheDecomposed(actDecomp);

				// Persistence Operations

				autoDAO.delete(actAuto);

				actDAO.save(actDecomp);

				return actDecomp.getOid();
			} else {
				throw new ModelingException(
						Messages.getString("facades.DynamicModeling.ModelingExcActv") + activity.getIdent() + Messages.getString("facades.DynamicModeling.ModelingExcIsAlrRun")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return null;
	}


	@Override
	public Integer defineAsAutomatic(String act_id) throws DAOException, ModelingException {

		// Checks for the parameters

		Object act;
		try {
			act = actDAO.retrieveBySecondaryKey(act_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActiv") + //$NON-NLS-1$
					act_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (act == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Activity activity = (Activity) act;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		if (activity instanceof Automatic)
			throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcActv") + activity.getIdent() //$NON-NLS-1$
					+ Messages.getString("facades.DynamicModeling.ModelingExcIsAlrAutoActv")); //$NON-NLS-1$
		else if (activity instanceof Normal) { // Rule G1.6
			Normal actNorm = (Normal) activity;

			if (actNorm.getTheEnactionDescription().getState().equals("")) { //$NON-NLS-1$

				if (this.hasInvolvedAgents(actNorm)) {
					actNorm.setTheRequiredPeople(new HashSet());
				}
				Collection set = actNorm.getTheRequiredResource();
				set.remove(null);

				if (!set.isEmpty()) {
					this.releaseResourcesFromActivity(actNorm);
					actNorm.setTheRequiredResource(new HashSet());
				}

				String identAux = actNorm.getIdent();
				actNorm.setIdent(""); //$NON-NLS-1$

				// Persistence Operation
				actDAO.update(actNorm);

				Automatic actAuto = new Automatic();
				actAuto.setIdent(identAux);
				actAuto.setName(actNorm.getName());

				this.copyActivityRelationships(actNorm, actAuto);

				// Persistence Operations

				normDAO.delete(actNorm);

				actDAO.save(actAuto);

				return actAuto.getOid();
			} else {
				throw new ModelingException(
						Messages.getString("facades.DynamicModeling.ModelingExcActv") + activity.getIdent() + Messages.getString("facades.DynamicModeling.ModelingExcIsAlrRun")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else if (activity instanceof Decomposed) { // Rule G1.7
			Decomposed actDecomp = (Decomposed) activity;
			ProcessModel pmodel = actDecomp.getTheReferedProcessModel();

			if (pmodel == null) {
				throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcActv") + activity.getIdent() //$NON-NLS-1$
						+ Messages.getString("facades.DynamicModeling.ModelingExcHasNoRefProcMod")); //$NON-NLS-1$
			} else {
				Collection set = pmodel.getTheActivity();
				set.remove(null);

				if (!set.isEmpty()) {
					throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcActv") //$NON-NLS-1$
							+ activity.getIdent() + Messages.getString("facades.DynamicModeling.ModelingExcHasRefProcModDef")); //$NON-NLS-1$
				} else {

					String identAux = actDecomp.getIdent();
					actDecomp.setIdent(""); //$NON-NLS-1$

					// Persistence Operation
					actDAO.update(actDecomp);

					Automatic actAuto = new Automatic();
					actAuto.setIdent(identAux);
					actAuto.setName(actDecomp.getName());

					this.copyActivityRelationships(actDecomp, actAuto);
					// Persistence Operations

					decDAO.delete(actDecomp);

					actDAO.save(actAuto);

					return actAuto.getOid();
				}
			}
		}
		return null;
	}


	@Override
	public Integer defineAsNormal(String act_id) throws DAOException, ModelingException {

		// Checks for the parameters

		Object act;
		try {
			act = actDAO.retrieveBySecondaryKey(act_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActiv") + //$NON-NLS-1$
					act_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (act == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Activity activity = (Activity) act;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		if (activity instanceof Normal) {
			throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcActv") + activity.getIdent() //$NON-NLS-1$
					+ Messages.getString("facades.DynamicModeling.ModelingExcIsAlrNorActv")); //$NON-NLS-1$
		}

		else if (activity instanceof Decomposed) { // Rule G1.8
			Decomposed actDecomp = (Decomposed) activity;

			ProcessModel pmodel = actDecomp.getTheReferedProcessModel();

			if (pmodel == null) {
				throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcActv") + activity.getIdent() //$NON-NLS-1$
						+ Messages.getString("facades.DynamicModeling.ModelingExcHasNoRefProcMod")); //$NON-NLS-1$
			} else {
				Collection set = pmodel.getTheActivity();
				set.remove(null);

				if (!set.isEmpty()) {
					throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcActv") //$NON-NLS-1$
							+ activity.getIdent() + Messages.getString("facades.DynamicModeling.ModelingExcHasRefProcModDef")); //$NON-NLS-1$
				} else {
					String identAux = actDecomp.getIdent();
					actDecomp.setIdent(""); //$NON-NLS-1$

					// Persistence Operation
					actDAO.update(actDecomp);

					Normal actNorm = new Normal();
					actNorm.setIdent(identAux);
					actNorm.setName(actDecomp.getName());

					this.copyActivityRelationships(actDecomp, actNorm);

					// Persistence Operations
					actDAO.save(actNorm);

					decDAO.delete(actDecomp);

					return actNorm.getOid();
				}
			}
		} else if (activity instanceof Automatic) { // Rule G1.9
			Automatic actAuto = (Automatic) activity;

			if (actAuto.getTheEnactionDescription().getState().equals("") //$NON-NLS-1$
					&& actAuto.getTheSubroutine() == null) {

				String identAux = actAuto.getIdent();
				actAuto.setIdent(""); //$NON-NLS-1$

				// Persistence Operation
				actDAO.update(actAuto);

				Normal actNorm = new Normal();
				actNorm.setIdent(identAux);
				actNorm.setName(actAuto.getName());

				this.copyActivityRelationships(actAuto, actNorm);

				// Persistence Operations

				AutomaticDAO autoDAO = new AutomaticDAO();
				autoDAO.delete(actAuto);

				actDAO.save(actNorm);

				return actNorm.getOid();
			} else {
				throw new ModelingException(
						Messages.getString("facades.DynamicModeling.ModelingExcActv") + activity.getIdent() + Messages.getString("facades.DynamicModeling.ModelingExcIsAlrRun")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return null;
	}

	/**
	 * Rules G1.10 and G1.11 are implemented in facades.EnactmentEngine
	 */
	/*
	 * public Activity createNewVersion(String act_id){
	 *
	 * }
	 */

	@Override
	public void defineInputArtifact(String act_id, String art_id) throws DAOException, ModelingException {

		// Checks for the parameters

		Object act;
		try {
			act = normDAO.retrieveBySecondaryKey(act_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActiv") + //$NON-NLS-1$
					act_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (act == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Normal actNorm = (Normal) act;

		Object art;
		try {
			art = artDAO.retrieveBySecondaryKey(art_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccArtf") + //$NON-NLS-1$
					art_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (art == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcArtf") + art_id + Messages.getString("facades.DynamicModeling.DaoExc_NotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Artifact artifact = (Artifact) art;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		String state = actNorm.getTheEnactionDescription().getState();
		if (state.equals("")) { // Rule G1.12 //$NON-NLS-1$
			if (!this.hasTheFromArtifact(actNorm, artifact)) {

				InvolvedArtifacts inv = new InvolvedArtifacts();
				inv.setTheArtifact(artifact);
				artifact.getTheInvolvedArtifacts().add(inv);
				inv.setInInvolvedArtifacts(actNorm);
				actNorm.getInvolvedArtifactToNormal().add(inv);
				inv.setTheArtifactType(artifact.getTheArtifactType());
				artifact.getTheArtifactType().getTheInvolvedArtifacts().add(inv);
				// Persistence Operations

				normDAO.update(actNorm);
				artDAO.update(artifact);

			} else {
				throw new ModelingException(
						Messages.getString("facades.DynamicModeling.ModelingExcActv") + actNorm.getIdent() + Messages.getString("facades.DynamicModeling.ModelingExcAlreadyCont") //$NON-NLS-1$ //$NON-NLS-2$
								+ Messages.getString("facades.DynamicModeling.ModelingExcThisArtfInput")); //$NON-NLS-1$
			}
		} else if (!state.equals("") //$NON-NLS-1$
				&& !state.equals(Plain.CANCELED) && !state.equals(Plain.FAILED) && !state.equals(Plain.FINISHED)) { // Rule
																													// G1.13

			if (!this.hasTheFromArtifact(actNorm, artifact)) {

				InvolvedArtifacts inv = new InvolvedArtifacts();
				inv.setTheArtifact(artifact);
				artifact.getTheInvolvedArtifacts().add(inv);
				inv.setInInvolvedArtifacts(actNorm);
				actNorm.getInvolvedArtifactToNormal().add(inv);
				inv.setTheArtifactType(artifact.getTheArtifactType());
				artifact.getTheArtifactType().getTheInvolvedArtifacts().add(inv);

			} else {
				throw new ModelingException(
						Messages.getString("facades.DynamicModeling.ModelingExcActv") + actNorm.getIdent() + Messages.getString("facades.DynamicModeling.ModelingExcAlreadyCont") //$NON-NLS-1$ //$NON-NLS-2$
								+ Messages.getString("facades.DynamicModeling.ModelingExcThisArtfInput")); //$NON-NLS-1$
			}

			if (this.hasInvolvedAgents(actNorm))
				this.notifyAgents(actNorm, Messages.getString("facades.DynamicModeling.NotifyAgtNewInpArtf"), artifact.getOid(), DynamicModelingImpl.ADD,
						artifact.getClass(), artifact.getIdent(), DynamicModelingImpl.DIRECTION_IN); //$NON-NLS-1$

			// Persistence Operations

			normDAO.update(actNorm);
			artDAO.update(artifact);
		}
	}


	@Override
	public void defineOutputArtifact(String act_id, String art_id) throws DAOException, ModelingException {

		// Checks for the parameters

		Object act;
		try {
			act = normDAO.retrieveBySecondaryKey(act_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActiv") + //$NON-NLS-1$
					act_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (act == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.118") + act_id + Messages.getString("facades.DynamicModeling.DaoExc_NotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Normal actNorm = (Normal) act;

		Object art;
		try {
			art = artDAO.retrieveBySecondaryKey(art_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccArtf") + //$NON-NLS-1$
					art_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (art == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcArtf") + art_id + Messages.getString("facades.DynamicModeling.DaoExc_NotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Artifact artifact = (Artifact) art;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		String state = actNorm.getTheEnactionDescription().getState();
		if (state.equals("")) { // Rule G1.14 //$NON-NLS-1$
			if (!this.hasTheToArtifact(actNorm, artifact)) {

				InvolvedArtifacts inv = new InvolvedArtifacts();

				inv.setTheArtifact(artifact);
				artifact.getTheInvolvedArtifacts().add(inv);
				inv.setOutInvolvedArtifacts(actNorm);
				actNorm.getInvolvedArtifactFromNormal().add(inv);
				inv.setTheArtifactType(artifact.getTheArtifactType());
				artifact.getTheArtifactType().getTheInvolvedArtifacts().add(inv);

				// Persistence Operations

				normDAO.update(actNorm);
				artDAO.update(artifact);

			} else {
				throw new ModelingException(
						Messages.getString("facades.DynamicModeling.ModelingExcActv") + actNorm.getIdent() + Messages.getString("facades.DynamicModeling.ModelingExcAlreadyCont") //$NON-NLS-1$ //$NON-NLS-2$
								+ Messages.getString("facades.DynamicModeling.ModelingExcThisArtfOut")); //$NON-NLS-1$
			}
		} else if (!state.equals("") //$NON-NLS-1$
				&& !state.equals(Plain.CANCELED) && !state.equals(Plain.FAILED) && !state.equals(Plain.FINISHED)) { // Rule
																													// G1.15
			if (!this.hasTheToArtifact(actNorm, artifact)) {

				InvolvedArtifacts inv = new InvolvedArtifacts();

				inv.setTheArtifact(artifact);
				artifact.getTheInvolvedArtifacts().add(inv);
				inv.setOutInvolvedArtifacts(actNorm);
				actNorm.getInvolvedArtifactFromNormal().add(inv);
				inv.setTheArtifactType(artifact.getTheArtifactType());
				artifact.getTheArtifactType().getTheInvolvedArtifacts().add(inv);

			} else {
				throw new ModelingException(
						Messages.getString("facades.DynamicModeling.ModelingExcActv") + actNorm.getIdent() + Messages.getString("facades.DynamicModeling.ModelingExcAlreadyCont") //$NON-NLS-1$ //$NON-NLS-2$
								+ Messages.getString("facades.DynamicModeling.ModelingExcThisArtfOut")); //$NON-NLS-1$
			}

			if (this.hasInvolvedAgents(actNorm))
				this.notifyAgents(actNorm, Messages.getString("facades.DynamicModeling.NotifyAgtNewOutArtf"), artifact.getOid(), DynamicModelingImpl.ADD,
						artifact.getClass(), artifact.getIdent(), DynamicModelingImpl.DIRECTION_OUT); //$NON-NLS-1$

			// Persistence Operations

			normDAO.update(actNorm);
			artDAO.update(artifact);

		} else {
			throw new ModelingException(
					Messages.getString("facades.DynamicModeling.ModelingExcTheActv") + actNorm.getIdent() + Messages.getString("facades.DynamicModeling.ModelingExcStateNotOk") //$NON-NLS-1$ //$NON-NLS-2$
							+ Messages.getString("facades.DynamicModeling.ModelingExcForNewOutArtf")); //$NON-NLS-1$
		}
	}


	@Override
	public Integer defineAutomatic(String auto_id, Script script, Collection parameters) throws DAOException {// Oid
																												// Automatic

		Object act;
		try {
			act = autoDAO.retrieveBySecondaryKey(auto_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActiv") + //$NON-NLS-1$
					auto_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (act == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + auto_id + Messages.getString("facades.DynamicModeling.DaoExc_NotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Automatic automatic = (Automatic) act;

		// Now we start the implementation of the rules

		automatic.setTheSubroutine(script);
		script.setTheAutomatic(automatic);

		Iterator iter = parameters.iterator();
		while (iter.hasNext()) {
			Parameters param = (Parameters) iter.next();
			param.setTheAutomatic(automatic);
			automatic.getTheParameters().add(param);
		}

		// Persistence Operations

		autoDAO.update(automatic);

		return automatic.getOid();
	}


	@Override
	public Integer defineAutomatic(String auto_id, ClassMethodCall cmc, Collection parameters) throws DAOException {// recupera
																													// Oid
																													// Automatic

		Object act;
		try {
			act = autoDAO.retrieveBySecondaryKey(auto_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActiv") + //$NON-NLS-1$
					auto_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (act == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + auto_id + Messages.getString("facades.DynamicModeling.DaoExc_NotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Automatic automatic = (Automatic) act;

		// Now we start the implementation of the rules

		automatic.setTheSubroutine(cmc);
		automatic.setTheParameters(parameters);
		cmc.setTheAutomatic(automatic);
		Iterator iter = parameters.iterator();
		while (iter.hasNext()) {
			Parameters param = (Parameters) iter.next();
			param.setTheAutomatic(automatic);
		}

		// Persistence Operations

		autoDAO.update(automatic);

		return automatic.getOid();
	}


	@Override
	public void deleteActivity(String act_id) throws DAOException, WebapseeException {

		// Checks for the parameters
		Object act;

		try {
			act = actDAO.retrieveBySecondaryKey(act_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActiv") + //$NON-NLS-1$
					act_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		System.out.println("Ident: " + act_id);
		System.out.println(act);
		if (act == null) {
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		Activity activity = (Activity) act;

		ProcessModel processModel = activity.getTheProcessModel();
		// End Checks for the parameters

		// Now we start the implementation of the rules

		if (activity instanceof Decomposed) {
			Decomposed actDec = (Decomposed) activity;
			ProcessModel referedProcessModel = actDec.getTheReferedProcessModel();
			if (referedProcessModel != null) {
				String state = referedProcessModel.getPmState();
				if (state.equals(ProcessModel.REQUIREMENTS) || state.equals(ProcessModel.ABSTRACT) || state.equals(ProcessModel.INSTANTIATED)) {

					Collection set = referedProcessModel.getTheActivity();
					if (set.isEmpty()) {
						this.removeAllConnectionsFromActivity(actDec);
						processModel.getTheActivity().remove(actDec);

						// Remove from the model
						decDAO.delete(actDec);

						// Dynamic Changes related code
						String processState = this.getTheProcess(processModel).getPState();
						if (processState.equals(Process.ENACTING)) {
							this.enactmentEngine.searchForFiredConnections(processModel.getOid(), "Rule G1.17-G1.32"); //$NON-NLS-1$
							this.enactmentEngine.searchForReadyActivities(processModel.getOid()/*
																							 * ,
																							 * currentSession
																							 */);
							this.enactmentEngine.determineProcessModelStates(processModel/*
																					 * ,
																					 * currentSession
																					 */);
						}
					} else {
						Collection acts = actDec.getTheReferedProcessModel().getTheActivity();
						Iterator iterActivity = acts.iterator();
						while (iterActivity.hasNext()) {
							deleteActivity(((Activity) iterActivity.next()).getIdent());
						}
						deleteActivity(actDec.getIdent());
						return;
					}
				} else {
					throw new ModelingException(
							Messages.getString("facades.DynamicModeling.ModelingExcActv") + actDec.getIdent() + Messages.getString("facades.DynamicModeling.ModelingExcHasAlrStart")); //$NON-NLS-1$ //$NON-NLS-2$
				}
			} else {
				// equals to empty process model
				this.removeAllConnectionsFromActivity(actDec/* , currentSession */);
				processModel.getTheActivity().remove(actDec);

				// Remove from the model

				decDAO.delete(actDec);

				// Dynamic Changes related code
				String processState = this.getTheProcess(processModel).getPState();
				if (processState.equals(Process.ENACTING)) {
					this.enactmentEngine.searchForFiredConnections(processModel.getOid(), "Rule G1.17-G1.32"); //$NON-NLS-1$
					this.enactmentEngine.searchForReadyActivities(processModel.getOid()/*
																					 * ,
																					 * currentSession
																					 */);
					this.enactmentEngine.determineProcessModelStates(processModel/*
																			 * ,
																			 * currentSession
																			 */);
				}

			}
		} else if (activity instanceof Automatic) {
			Automatic actAuto = (Automatic) activity;

			String state = actAuto.getTheEnactionDescription().getState();
			if (state.equals("") || state.equals(Plain.WAITING)) { //$NON-NLS-1$

				this.removeAllConnectionsFromActivity(actAuto/* , currentSession */);

				processModel.getTheActivity().remove(actAuto);
				actAuto.setTheProcessModel(null);
				// Remove from the model
				Subroutine subr = actAuto.getTheSubroutine();
				if (subr != null) {

					subDAO.delete(subr);
				}

				Collection parameters = actAuto.getTheParameters();
				Iterator iterParam = parameters.iterator();
				while (iterParam.hasNext()) {
					Parameters param = (Parameters) iterParam.next();
					paramDAO.delete(param);
				}

				// Remove from the model
				autoDAO.delete(actAuto);

				// Dynamic Changes related code
				String processState = this.getTheProcess(processModel).getPState();
				if (processState.equals(Process.ENACTING)) {
					this.enactmentEngine.searchForFiredConnections(processModel.getOid(), "Rule G1.33-G1.48"); //$NON-NLS-1$
					this.enactmentEngine.searchForReadyActivities(processModel.getOid()/*
																					 * ,
																					 * currentSession
																					 */);
					this.enactmentEngine.determineProcessModelStates(processModel/*
																			 * ,
																			 * currentSession
																			 */);
				}
			} else {
				throw new ModelingException(
						Messages.getString("facades.DynamicModeling.ModelingExcActv") + actAuto.getIdent() + Messages.getString("facades.DynamicModeling.ModelingExcHasAlrStart")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else if (activity instanceof Normal) {
			Normal actNorm = (Normal) activity;
			String state = actNorm.getTheEnactionDescription().getState();

			if (state.equals("") || state.equals(Plain.WAITING) || state.equals(Plain.READY)) {

				this.removeAllConnectionsFromActivity(actNorm/* , currentSession */);
				processModel.removeFromTheActivity(actNorm);

				// Remove from the model

				normDAO.delete(actNorm);

				// Dynamic Changes related code
				String processState = this.getTheProcess(processModel).getPState();
				if (processState.equals(Process.ENACTING)) {
					this.enactmentEngine.searchForFiredConnections(processModel.getOid(), "Rule G1.49-G1.98");
					this.enactmentEngine.searchForReadyActivities(processModel.getOid()/*
																					 * ,
																					 * currentSession
																					 */);
					this.enactmentEngine.determineProcessModelStates(processModel/*
																			 * ,
																			 * currentSession
																			 */);
				}
			} else {
				throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcActv") + actNorm.getIdent()
						+ Messages.getString("facades.DynamicModeling.ModelingExcHasAlrStart"));
			}
		}

		// Persistence Operations
		pmodelDAO.update(processModel);
	}

	/**
	 * Related to section 2
	 */
	@Override
	public Integer newArtifactConnection(String level_id) throws DAOException {

		return newArtifactConnection_Internal(level_id).getOid();

	}

	private ArtifactCon newArtifactConnection_Internal(String level_id) throws DAOException {
		// retorna
		// Oid
		// de
		// ArtifactCon

		// Checks for the parameters
		StringTokenizer st = new StringTokenizer(level_id, "."); //$NON-NLS-1$
		String process_id = st.nextToken();

		Object proc;
		try {
			proc = procDAO.retrieveBySecondaryKey(process_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccProcess") + //$NON-NLS-1$
					process_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (proc == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcProcess") + process_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Process process = (Process) proc;

		ProcessModel pmodel = null;

		Decomposed actDecomposed = null;
		// it is used only if the new activity
		// Is not in the root process model.
		if (st.hasMoreTokens()) {
			String currentModel = process_id;
			while (st.hasMoreTokens()) {
				currentModel += "." + st.nextToken(); //$NON-NLS-1$
			}
			Activity activity = null;
			try {
//				dec = decDAO.retrieveBySecondaryKey(currentModel);
				activity = actDAO.retrieveBySecondaryKey(currentModel);

				System.out.println("==============>  Activity id: " + currentModel);
			} catch (Exception/* DAOException */e1) {
				throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActiv") + //$NON-NLS-1$
						currentModel + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e1); //$NON-NLS-1$
			}

			if (activity == null)
				throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDecomActv") + currentModel //$NON-NLS-1$
						+ Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$

			if(activity instanceof Decomposed) {
				actDecomposed = (Decomposed) activity;
				pmodel = actDecomposed.getTheReferedProcessModel();
			} else {
				pmodel = activity.getTheProcessModel();
			}
		} else {
			pmodel = process.getTheProcessModel();
		}

		// End Checks for the parameters

		// Now we start the implementation of the rules

		ArtifactCon artifactCon = new ArtifactCon();
		artifactCon.setIdent(level_id);

		artifactCon.setTheProcessModel(pmodel);
		pmodel.getTheConnection().add(artifactCon);

		// Persistence Operations
		artConDAO.save(artifactCon);
		if (actDecomposed != null)
			decDAO.update(actDecomposed);

		return artifactCon;
	}


	@Override
	public Integer newArtifactConnection(String level_id, String art_id) throws DAOException, ModelingException {// retorna
		Artifact artifactAux = (Artifact) artDAO.retrieveBySecondaryKey(art_id);
		String type_id = artifactAux.getTheArtifactType().getIdent();

		ArtifactCon artifactCon = this.newArtifactConnection_Internal(level_id);
		artifactCon = this.defineType_ArtifactConnection_Internal(artifactCon.getIdent(), type_id);
		artifactCon = this.defineInstance_ArtifactConnection_Internal(artifactCon.getIdent(), art_id);

		return artifactCon.getOid();
	}


	@Override
	public Integer defineType_ArtifactConnection(String con_id, String type) throws DAOException {
		return defineType_ArtifactConnection_Internal(con_id, type).getOid();
	}

	private ArtifactCon defineType_ArtifactConnection_Internal(String con_id, String type) throws DAOException { // retorna

		// Checks for the parameters

		Object artCon;
		try {
			artCon = artConDAO.retrieveBySecondaryKey(con_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccArtfConn") + //$NON-NLS-1$
					con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (artCon == null)
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcArtfConn") + con_id
					+ Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		ArtifactCon artifactCon = (ArtifactCon) artCon;

		Object artType;
		try {
			artType = artTypeDAO.retrieveBySecondaryKey(type);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccArtfType") + //$NON-NLS-1$
					con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (artType == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcArtfType") + con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		ArtifactType artifactType = (ArtifactType) artType;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		artifactCon.setTheArtifactType(artifactType);

		artifactType.getTheArtifactCon().add(artifactCon);

		// Persistence Operations
		artConDAO.update(artifactCon);
		artTypeDAO.update(artifactType);

		return artifactCon;
	}


	@Override
	public Integer changeType_ArtifactConnection(String con_id, String newType) throws DAOException {// retorna
																										// Oid
																										// de
																										// ArtifactCon

		// Checks for the parameters

		Object artCon;
		try {
			artCon = artConDAO.retrieveBySecondaryKey(con_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccArtfConn") + //$NON-NLS-1$
					con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (artCon == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcArtfConn") + con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		ArtifactCon artifactCon = (ArtifactCon) artCon;

		ArtifactTypeDAO artTypeDAO = new ArtifactTypeDAO();
		Object artType;
		try {
			artType = artTypeDAO.retrieveBySecondaryKey(newType);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccArtfType") + //$NON-NLS-1$
					con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (artType == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcArtfTyp") + con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		ArtifactType newArtifactType = (ArtifactType) artType;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		Artifact artifact = artifactCon.getTheArtifact();
		if (artifact != null) {
			// Rule G2.3
			artifact.getTheArtifactCon().remove(artifactCon);
			artifactCon.setTheArtifact(null);
			artifactCon.setTheArtifactType(newArtifactType);
			newArtifactType.getTheArtifactCon().add(artifactCon);
		} else {// Rule G2.4
			artifactCon.setTheArtifactType(newArtifactType);
			newArtifactType.getTheArtifactCon().add(artifactCon);
		}

		// Persistence Operations
		artConDAO.update(artifactCon);
		artTypeDAO.update(newArtifactType);

		return artifactCon.getOid();
	}


	@Override
	public Integer defineInstance_ArtifactConnection(String con_id, String artifact_id) throws DAOException, ModelingException {
		return defineInstance_ArtifactConnection_Internal(con_id, artifact_id).getOid();

	}

	private ArtifactCon defineInstance_ArtifactConnection_Internal(String con_id, String artifact_id) throws DAOException, ModelingException { // retorna

		// Checks for the parameters
		Object artCon;
		try {
			artCon = artConDAO.retrieveBySecondaryKey(con_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccArtfConn") + //$NON-NLS-1$
					con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (artCon == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcArtfConn") + con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		ArtifactCon artifactCon = (ArtifactCon) artCon;

		Object art;
		try {
			art = artDAO.retrieveBySecondaryKey(artifact_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccArtf") + //$NON-NLS-1$
					artifact_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (art == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcArtf") + artifact_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Artifact artifact = (Artifact) art;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		Artifact artifactFromCon = artifactCon.getTheArtifact();
		ArtifactType artifactTypeFromCon = artifactCon.getTheArtifactType();

		if (artifactFromCon == null) {

			if (artifactTypeFromCon == null) {
				// Rule G2.5
				artifactCon.insertIntoTheArtifact(artifact);
				ArtifactType type = artifact.getTheArtifactType();
				artifactCon.insertIntoTheArtifactType(type);

				this.createInvolvedArtifacts(artifactCon, artifact, artifactFromCon, type);

			} else if (artifactTypeFromCon.equals(artifact.getTheArtifactType())
					|| this.isSubType(artifact.getTheArtifactType(), artifactTypeFromCon)) {

				// Rule G2.7
				artifactCon.insertIntoTheArtifact(artifact);

				this.createInvolvedArtifacts(artifactCon, artifact, artifactFromCon, artifactTypeFromCon);

			} else {

				throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheArtfTypeFromArtf")); //$NON-NLS-1$
			}
		} else {

			//TODO Verificar porque o metodo removeFromArtifactCon esta sendo chamado
			if (artifactTypeFromCon == null) {
				// Rule G2.6
				artifactFromCon.removeFromTheArtifactCon(artifactCon);
				artifactCon.insertIntoTheArtifact(artifact);
				ArtifactType type = artifact.getTheArtifactType();
				artifactCon.insertIntoTheArtifactType(type);

				this.createInvolvedArtifacts(artifactCon, artifact, artifactFromCon, type);

			} else if (artifactTypeFromCon.equals(artifact.getTheArtifactType())
					|| this.isSubType(artifact.getTheArtifactType(), artifactTypeFromCon)) {
				// Rule G2.8
				artifactFromCon.removeFromTheArtifactCon(artifactCon);
				artifactCon.insertIntoTheArtifact(artifact);

				this.createInvolvedArtifacts(artifactCon, artifact, artifactFromCon, artifactTypeFromCon);

			} else {

				throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheArtfTypeFromArtf")); //$NON-NLS-1$
			}
		}

		// Persistence Operations
		artDAO.update(artifact);
		artConDAO.update(artifactCon);

		return artifactCon;
	}


	@Override
	public Integer removeInstance_ArtifactConnection(String con_id) throws DAOException {// retorna
																							// Oid
																							// ArtifactCon

		// Checks for the parameters

		Object artCon;
		try {
			artCon = artConDAO.retrieveBySecondaryKey(con_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccArtfConn") + //$NON-NLS-1$
					con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (artCon == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcArtfConn") + con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		ArtifactCon artifactCon = (ArtifactCon) artCon;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		Artifact artifact = artifactCon.getTheArtifact();
		artifact.removeFromTheArtifactCon(artifactCon);

		// Removing from normal activities their input artifacts
		Collection<Activity> toActivities = artifactCon.getToActivity();
		Iterator iterToActivities = toActivities.iterator();
		while (iterToActivities.hasNext()) {
			Activity toActivity = (Activity) iterToActivities.next();
			if (toActivity != null && toActivity instanceof Normal) {

				Normal toNormal = (Normal) toActivity;
				Collection inInvArts = toNormal.getInvolvedArtifactToNormal();
				Iterator iterInInvArts = inInvArts.iterator();
				while (iterInInvArts.hasNext()) {
					InvolvedArtifacts inInvArt = (InvolvedArtifacts) iterInInvArts.next();
					if (inInvArt != null) {
						Artifact artFromInv = inInvArt.getTheArtifact();
						if (artFromInv != null && artFromInv.equals(artifact)) {

							inInvArt.removeFromTheArtifact();
							inInvArt.removeFromTheArtifactType();
							inInvArt.removeFromInInvolvedArtifacts();

							invArtDAO.delete(inInvArt);
							break;
						}
					}
				}
			}
		}

		// Removing from normal activities their output artifacts
		Collection<Activity> fromActivities = artifactCon.getFromActivity();
		Iterator iterFromActivities = fromActivities.iterator();
		while (iterFromActivities.hasNext()) {
			Activity fromActivity = (Activity) iterFromActivities.next();
			if (fromActivity != null && fromActivity instanceof Normal) {

				Normal fromNormal = (Normal) fromActivity;
				Collection outInvArts = fromNormal.getInvolvedArtifactFromNormal();
				Iterator iterOutInvArts = outInvArts.iterator();
				while (iterOutInvArts.hasNext()) {
					InvolvedArtifacts outInvArt = (InvolvedArtifacts) iterOutInvArts.next();
					if (outInvArt != null) {
						Artifact artFromInv = outInvArt.getTheArtifact();
						if (artFromInv != null && artFromInv.equals(artifact)) {

							outInvArt.removeFromTheArtifact();
							outInvArt.removeFromTheArtifactType();
							outInvArt.removeFromInInvolvedArtifacts();

							invArtDAO.delete(outInvArt);
							break;
						}
					}
				}
			}
		}

		// Persistence Operations
		artConDAO.update(artifactCon);

		return artifactCon.getOid();
	}


	@Override
	public Integer defineOutput_ArtifactConnection(String con_id, String act_id) throws DAOException, ModelingException, WebapseeException {
		return defineOutput_ArtifactConnection_Internal(con_id, act_id).getOid();
	}

	private ArtifactCon defineOutput_ArtifactConnection_Internal(String con_id, String act_id) throws DAOException, ModelingException,
			WebapseeException { // retorna Oid ArtifactCon

		// Checks for the parameters

		Object artCon;
		try {
			artCon = artConDAO.retrieveBySecondaryKey(con_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccArtfConn") + //$NON-NLS-1$
					con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (artCon == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcArtfConn") + con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		ArtifactCon artifactCon = (ArtifactCon) artCon;

		Object act;
		try {
			act = actDAO.retrieveBySecondaryKey(act_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActiv") + //$NON-NLS-1$
					act_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (act == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Activity activity = (Activity) act;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		String state = this.getState(activity);

		Artifact artifactFromCon = artifactCon.getTheArtifact();
		ArtifactType artifactTypeFromCon = artifactCon.getTheArtifactType();

		if (artifactTypeFromCon != null) {
			if (activity instanceof Normal) {
				Normal actNorm = (Normal) activity;

				boolean normalContainsToArtifact = actNorm.getToArtifactCon().contains(artifactCon);

				if (!normalContainsToArtifact) {
					if (!this.hasTheToArtifact(actNorm, artifactFromCon)) {

						if (state.equals("")) { // Rule G2.10 //$NON-NLS-1$
							artifactCon.getFromActivity().add(actNorm);
							actNorm.getToArtifactCon().add(artifactCon);

							InvolvedArtifacts invArt = new InvolvedArtifacts();

							if (artifactFromCon != null) {
								invArt.setTheArtifact(artifactFromCon);
								artifactFromCon.getTheInvolvedArtifacts().add(invArt);
							}

							invArt.setOutInvolvedArtifacts(actNorm);
							actNorm.getInvolvedArtifactFromNormal().add(invArt);
							artifactTypeFromCon.getTheInvolvedArtifacts().add(invArt);
							invArt.setTheArtifactType(artifactTypeFromCon);

							// Persistence Operations
							artConDAO.update(artifactCon);
							actDAO.update(activity);

							return artifactCon;

						} else if (state.equals(Plain.WAITING) || state.equals(Plain.READY) || state.equals(Plain.ACTIVE)
								|| state.equals(Plain.PAUSED)) { // Rule G2.11

							artifactCon.getFromActivity().add(actNorm);
							actNorm.getToArtifactCon().add(artifactCon);

							InvolvedArtifacts invArt = new InvolvedArtifacts();

							if (artifactFromCon != null) {
								invArt.setTheArtifact(artifactFromCon);
								artifactFromCon.getTheInvolvedArtifacts().add(invArt);
							}

							invArt.setOutInvolvedArtifacts(actNorm);
							actNorm.getInvolvedArtifactFromNormal().add(invArt);
							invArt.setTheArtifactType(artifactTypeFromCon);
							artifactTypeFromCon.getTheInvolvedArtifacts().add(invArt);

							if (this.hasInvolvedAgents(actNorm))
								this.notifyAgents(actNorm, Messages.getString("facades.DynamicModeling.NotifyAgtNewOutArtf"),
										artifactFromCon.getOid(), DynamicModelingImpl.ADD, artifactFromCon.getClass(), artifactFromCon.getIdent(),
										DynamicModelingImpl.DIRECTION_OUT); //$NON-NLS-1$

							// Persistence Operations
							artConDAO.update(artifactCon);
							actDAO.update(activity);

							return artifactCon;
						} else
							throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheActvStateDoes")); //$NON-NLS-1$
					} else {// The activity doesn't have the artifact instance
							// defined
						// in involved artifact.

						if (state.equals("")) { // Rule G2.12 //$NON-NLS-1$
							artifactCon.getFromActivity().add(actNorm);
							actNorm.getToArtifactCon().add(artifactCon);

							InvolvedArtifacts invArt = new InvolvedArtifacts();

							if (artifactFromCon != null) {
								invArt.setTheArtifact(artifactFromCon);
								artifactFromCon.getTheInvolvedArtifacts().add(invArt);
							}
							invArt.setOutInvolvedArtifacts(actNorm);
							actNorm.getInvolvedArtifactFromNormal().add(invArt);
							artifactTypeFromCon.getTheInvolvedArtifacts().add(invArt);
							invArt.setTheArtifactType(artifactTypeFromCon);

							java.lang.System.out.println("State vazio");
							// Persistence Operations
							artConDAO.update(artifactCon);
							actDAO.update(activity);

							return artifactCon;

						} else if (state.equals(Plain.WAITING) || state.equals(Plain.READY) || state.equals(Plain.ACTIVE)
								|| state.equals(Plain.PAUSED)) { // Rule G2.13

							artifactCon.getFromActivity().add(actNorm);
							actNorm.getToArtifactCon().add(artifactCon);

							InvolvedArtifacts invArt = new InvolvedArtifacts();

							if (artifactFromCon != null) {
								invArt.setTheArtifact(artifactFromCon);
								artifactFromCon.getTheInvolvedArtifacts().add(invArt);
							}
							invArt.setOutInvolvedArtifacts(actNorm);
							actNorm.getInvolvedArtifactFromNormal().add(invArt);
							invArt.setTheArtifactType(artifactTypeFromCon);
							artifactTypeFromCon.getTheInvolvedArtifacts().add(invArt);

							if (this.hasInvolvedAgents(actNorm))
								this.notifyAgents(actNorm, Messages.getString("facades.DynamicModeling.NotifyAgtNewOutArtf"),
										artifactFromCon.getOid(), DynamicModelingImpl.ADD, artifactFromCon.getClass(), artifactFromCon.getIdent(),
										DynamicModelingImpl.DIRECTION_OUT); //$NON-NLS-1$

							// Persistence Operations
							artConDAO.update(artifactCon);
							actDAO.update(activity);

							return artifactCon;
						} else
							throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheActvStateDoes")); //$NON-NLS-1$
					}
				} else {
					throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheActvAlrHasArtf")); //$NON-NLS-1$
				}
			} else if (activity instanceof Decomposed) { // Rule G2.14
				Decomposed actDec = (Decomposed) activity;
				ProcessModel refProcModel = actDec.getTheReferedProcessModel();

				if (!state.equals(ProcessModel.FINISHED) && !state.equals(ProcessModel.CANCELED) && !state.equals(ProcessModel.FAILED)
						&& (refProcModel != null) && !actDec.getToArtifactCon().contains(artifactCon)) {

					actDec.getToArtifactCon().add(artifactCon);
					artifactCon.getFromActivity().add(actDec);

					ArtifactCon newArtifactCon = new ArtifactCon();
					newArtifactCon.setIdent(actDec.getIdent());
					newArtifactCon = (ArtifactCon) artConDAO.save(newArtifactCon);

					newArtifactCon.setTheArtifact(artifactFromCon);
					newArtifactCon.setTheArtifactType(artifactFromCon.getTheArtifactType());

					artifactFromCon.getTheArtifactCon().add(newArtifactCon);
					artifactFromCon.getTheArtifactType().getTheArtifactCon().add(newArtifactCon);

					refProcModel.getTheConnection().add(newArtifactCon);
					newArtifactCon.setTheProcessModel(refProcModel);

					// Persistence Operations
					artConDAO.update(artifactCon);
					actDAO.update(activity);

					return artifactCon;
				} else
					throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheActvStateDoes")); //$NON-NLS-1$
			} else
				throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheActvShouldNormDecomp")); //$NON-NLS-1$
		} else {
			throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheArtfTypeOfArtfConn")); //$NON-NLS-1$
		}
	}


	@Override
	public Integer removeOutput_ArtifactConnection(String con_id, String act_id) throws DAOException, ModelingException {// retorna
																															// Oid
																															// ArtifactCon

		// Checks for the parameters

		Object artCon;
		try {
			artCon = artConDAO.retrieveBySecondaryKey(con_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccArtfConn") + //$NON-NLS-1$
					con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (artCon == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcArtfConn") + con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		ArtifactCon artifactCon = (ArtifactCon) artCon;

		Object act;
		try {
			act = actDAO.retrieveBySecondaryKey(act_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActiv") + //$NON-NLS-1$
					act_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (act == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Activity activity = (Activity) act;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		String state = this.getState(activity);

		if ((!state.equals(Plain.FINISHED) || !state.equals(ProcessModel.FINISHED))
				&& (!state.equals(Plain.FAILED) || !state.equals(ProcessModel.FAILED))
				&& (!state.equals(Plain.FINISHED) || !state.equals(ProcessModel.FINISHED))) {

			if (activity.getToArtifactCon().contains(artifactCon) || artifactCon.getFromActivity().contains(activity)) {

				activity.getToArtifactCon().remove(artifactCon);
				artifactCon.getFromActivity().remove(activity);

				if (activity instanceof Normal) {
					Normal actNorm = (Normal) activity;
					Artifact artifact = artifactCon.getTheArtifact();
					if (artifact != null) {
						if (this.hasTheToArtifact(actNorm, artifactCon.getTheArtifact())) {
							Collection invArts = actNorm.getInvolvedArtifactFromNormal();
							Iterator iter = invArts.iterator();
							InvolvedArtifacts inv = null;
							while (iter.hasNext()) {
								InvolvedArtifacts aux = (InvolvedArtifacts) iter.next();
								if (aux.getTheArtifact().equals(artifact)) {
									inv = aux;
									break;
								}
							}
							if (inv != null) {
								inv.removeFromOutInvolvedArtifacts();

								if (this.hasInvolvedAgents(actNorm))
									this.notifyAgents(actNorm, Messages.getString("facades.DynamicModeling.NotifyAgtRemOutArtf"), artifact.getOid(),
											DynamicModelingImpl.DEL, artifact.getClass(), artifact.getIdent(), DynamicModelingImpl.DIRECTION_OUT); //$NON-NLS-1$
							}
						}
					}
				}

				// Persistence Operations

				artConDAO.update(artifactCon);
				actDAO.update(activity);

				return artifactCon.getOid();

			} else {
				throw new ModelingException(
						Messages.getString("facades.DynamicModeling.ModelingExcTheArtfConn") + artifactCon.getIdent() + Messages.getString("facades.DynamicModeling.ModelingExcCannBeResolv") //$NON-NLS-1$ //$NON-NLS-2$
								+ Messages.getString("facades.DynamicModeling.ModelingExcItsNotConnActv") + activity.getIdent() + "!"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else {
			throw new ModelingException(
					Messages.getString("facades.DynamicModeling.ModelingExcTheArtfConn") + artifactCon.getIdent() + Messages.getString("facades.DynamicModeling.ModelingExcCannBeResolv") //$NON-NLS-1$ //$NON-NLS-2$
							+ Messages.getString("facades.DynamicModeling.ModelingExcItsSourceEnact")); //$NON-NLS-1$
		}
	}


	@Override
	public Integer defineInput_ArtifactConnection_Activity(String con_id, String act_id) throws DAOException, ModelingException {
		return defineInput_ArtifactConnection_Activity_Internal(con_id, act_id).getOid();
	}

	private ArtifactCon defineInput_ArtifactConnection_Activity_Internal(String con_id, String act_id) throws DAOException, ModelingException {// retorna
																																				// Oid
																																				// ArtifactCon

		// Checks for the parameters

		Object artCon;
		try {
			artCon = artConDAO.retrieveBySecondaryKey(con_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccArtfConn") + //$NON-NLS-1$
					con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (artCon == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcArtfConn") + con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		ArtifactCon artifactCon = (ArtifactCon) artCon;

		Object act;
		try {
			act = actDAO.retrieveBySecondaryKey(act_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActiv") + //$NON-NLS-1$
					act_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (act == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Activity activity = (Activity) act;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		String state = this.getState(activity);
		Artifact artifactFromCon = artifactCon.getTheArtifact();
		ArtifactType artifactTypeFromCon = artifactCon.getTheArtifactType();

		if (artifactTypeFromCon != null) {
			if (activity instanceof Normal) {
				Normal actNorm = (Normal) activity;

				if (!actNorm.getFromArtifactCon().contains(artifactCon)) {
//					String newInputArtifact = Messages.getString("facades.DynamicModeling.NotifyAgtNewInpArtf");
					String newInputArtifact = "Notify Agent - New Input Artifact";
					if (!this.hasTheFromArtifact(actNorm, artifactFromCon)) {

						if (state.equals("")) { // Rule G2.10 //$NON-NLS-1$
							artifactCon.getToActivity().add(actNorm);
							actNorm.getFromArtifactCon().add(artifactCon);

							InvolvedArtifacts invArt = new InvolvedArtifacts();

							if (artifactFromCon != null) {
								invArt.setTheArtifact(artifactFromCon);
								artifactFromCon.getTheInvolvedArtifacts().add(invArt);
							}

							invArt.setInInvolvedArtifacts(actNorm);
							actNorm.getInvolvedArtifactToNormal().add(invArt);
							invArt.setTheArtifactType(artifactTypeFromCon);
							artifactTypeFromCon.getTheInvolvedArtifacts().add(invArt);

							// Persistence Operations
							artConDAO.update(artifactCon);
							actDAO.update(activity);

							return artifactCon;

						} else if (state.equals(Plain.WAITING) || state.equals(Plain.READY) || state.equals(Plain.ACTIVE)
								|| state.equals(Plain.PAUSED)) { // Rule G2.11

							artifactCon.getToActivity().add(actNorm);
							actNorm.getFromArtifactCon().add(artifactCon);

							InvolvedArtifacts invArt = new InvolvedArtifacts();

							if (artifactFromCon != null) {
								invArt.setTheArtifact(artifactFromCon);
								artifactFromCon.getTheInvolvedArtifacts().add(invArt);
							}

							invArt.setInInvolvedArtifacts(actNorm);
							actNorm.getInvolvedArtifactToNormal().add(invArt);
							invArt.setTheArtifactType(artifactTypeFromCon);
							artifactTypeFromCon.getTheInvolvedArtifacts().add(invArt);

							if (this.hasInvolvedAgents(actNorm))
								this.notifyAgents(actNorm, newInputArtifact,
										artifactFromCon.getOid(), DynamicModelingImpl.ADD, artifactFromCon.getClass(), artifactFromCon.getIdent(),
										DynamicModelingImpl.DIRECTION_IN); //$NON-NLS-1$

							// Persistence Operations
							artConDAO.update(artifactCon);
							actDAO.update(activity);

							return artifactCon;
						} else
							throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheActvStateDoes")); //$NON-NLS-1$
					} else {// The activity doesn't have the artifact instance
							// defined
						// in involved artifact.

						if (state.equals("")) { // Rule G2.12 //$NON-NLS-1$
							artifactCon.getToActivity().add(actNorm);
							actNorm.getFromArtifactCon().add(artifactCon);

							InvolvedArtifacts invArt = new InvolvedArtifacts();

							if (artifactFromCon != null) {
								invArt.setTheArtifact(artifactFromCon);
								artifactFromCon.getTheInvolvedArtifacts().add(invArt);
							}

							invArt.setInInvolvedArtifacts(actNorm);
							actNorm.getInvolvedArtifactToNormal().add(invArt);
							invArt.setTheArtifactType(artifactTypeFromCon);
							artifactTypeFromCon.getTheInvolvedArtifacts().add(invArt);

							// Persistence Operations
							artConDAO.update(artifactCon);
							actDAO.update(activity);

							return artifactCon;

						} else if (state.equals(Plain.WAITING) || state.equals(Plain.READY) || state.equals(Plain.ACTIVE)
								|| state.equals(Plain.PAUSED)) { // Rule G2.13

							artifactCon.getToActivity().add(actNorm);
							actNorm.getFromArtifactCon().add(artifactCon);

							InvolvedArtifacts invArt = new InvolvedArtifacts();

							if (artifactFromCon != null) {
								invArt.setTheArtifact(artifactFromCon);
								artifactFromCon.getTheInvolvedArtifacts().add(invArt);
							}

							invArt.setInInvolvedArtifacts(actNorm);
							actNorm.getInvolvedArtifactToNormal().add(invArt);
							invArt.setTheArtifactType(artifactTypeFromCon);
							artifactTypeFromCon.getTheInvolvedArtifacts().add(invArt);

							if (this.hasInvolvedAgents(actNorm))
								this.notifyAgents(actNorm, newInputArtifact,
										artifactFromCon.getOid(), DynamicModelingImpl.ADD, artifactFromCon.getClass(), artifactFromCon.getIdent(),
										DynamicModelingImpl.DIRECTION_IN); //$NON-NLS-1$

							// Persistence Operations
							artConDAO.update(artifactCon);
							actDAO.update(activity);

							return artifactCon;
						} else
							throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheActvStateDoes")); //$NON-NLS-1$
					}
				} else {
					throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheActvAlrHasArtf")); //$NON-NLS-1$
				}
			} else if (activity instanceof Decomposed) {
				Decomposed actDec = (Decomposed) activity;

				ProcessModel procModel = actDec.getTheProcessModel();
				if (!state.equals(ProcessModel.FINISHED) && !state.equals(ProcessModel.CANCELED) && !state.equals(ProcessModel.FAILED)
						&& (procModel != null) && !actDec.getFromArtifactCon().contains(artifactCon)) {

					actDec.getFromArtifactCon().add(artifactCon);
					artifactCon.getToActivity().add(actDec);

					ProcessModel refProcModel = actDec.getTheReferedProcessModel();
					if ((refProcModel != null) && !refProcModel.getTheConnection().contains(artifactCon)) {

						ArtifactCon newArtifactCon = new ArtifactCon();
						newArtifactCon.setIdent(actDec.getIdent());
						newArtifactCon = (ArtifactCon) artConDAO.save(newArtifactCon);

						newArtifactCon.setTheArtifactType(artifactTypeFromCon);
						artifactTypeFromCon.getTheArtifactCon().add(newArtifactCon);

						if (artifactFromCon != null) {

							newArtifactCon.setTheArtifact(artifactFromCon);
							artifactFromCon.getTheArtifactCon().add(newArtifactCon);
						}

						refProcModel.getTheConnection().add(newArtifactCon);
						newArtifactCon.setTheProcessModel(refProcModel);

						// Persistence Operations

						artConDAO.update(newArtifactCon);
						actDAO.update(activity);

						return artifactCon;
					} else
						throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheActvAlreadyConn")); //$NON-NLS-1$
				} else
					throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheDecompActvState")); //$NON-NLS-1$
			} else
				throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheActvShouldNormDecomp")); //$NON-NLS-1$
		} else {
			throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheArtfTypeOfArtfConn")); //$NON-NLS-1$
		}
	}


	@Override
	public Integer removeInput_ArtifactConnection_Activity(String con_id, String act_id) throws DAOException, ModelingException { // retorna
																																	// Oid
																																	// de
																																	// ArtifactCon

		// Checks for the parameters

		Object artCon;
		try {
			artCon = artConDAO.retrieveBySecondaryKey(con_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccArtfConn") + //$NON-NLS-1$
					con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (artCon == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcArtfConn") + con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		ArtifactCon artifactCon = (ArtifactCon) artCon;

		Object act;
		try {
			act = actDAO.retrieveBySecondaryKey(act_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActiv") + //$NON-NLS-1$
					act_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (act == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Activity activity = (Activity) act;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		String state = this.getState(activity);

		if ((!state.equals(Plain.FINISHED) || !state.equals(ProcessModel.FINISHED))
				&& (!state.equals(Plain.FAILED) || !state.equals(ProcessModel.FAILED))
				&& (!state.equals(Plain.FINISHED) || !state.equals(ProcessModel.FINISHED))) {

			Collection artifactConnections = activity.getFromArtifactCon();
			Collection toactivities = artifactCon.getToActivity();

			if (artifactConnections.contains(artifactCon) || toactivities.contains(activity)) {

				artifactConnections.remove(artifactCon);
				toactivities.remove(activity);

				if (activity instanceof Normal) {
					Normal actNorm = (Normal) activity;
					Artifact artifact = artifactCon.getTheArtifact();
					if (artifact != null) {
						if (this.hasTheFromArtifact(actNorm, artifactCon.getTheArtifact())) {
							Collection invArts = actNorm.getInvolvedArtifactToNormal();
							Iterator iter = invArts.iterator();
							InvolvedArtifacts inv = null;
							while (iter.hasNext()) {
								InvolvedArtifacts aux = (InvolvedArtifacts) iter.next();
								if (aux.getTheArtifact().equals(artifact)) {
									inv = aux;
									break;
								}
							}
							if (inv != null) {
								inv.removeFromInInvolvedArtifacts();

								if (this.hasInvolvedAgents(actNorm))
									this.notifyAgents(actNorm, Messages.getString("facades.DynamicModeling.NotifyAgtRemInpArtf"), artifact.getOid(),
											DynamicModelingImpl.DEL, artifact.getClass(), artifact.getIdent(), DynamicModelingImpl.DIRECTION_IN); //$NON-NLS-1$

							}
						}
					}
				}

				// Persistence Operations

				artConDAO.update(artifactCon);
				actDAO.update(activity);

				return artifactCon.getOid();
			} else {
				throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheArtfConn") //$NON-NLS-1$
						+ artifactCon.getIdent() + Messages.getString("facades.DynamicModeling.ModelingExcCannBeResolv") //$NON-NLS-1$
						+ Messages.getString("facades.DynamicModeling.ModelingExcIsNOtReadyToIn") + activity.getIdent() + "!"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else {
			throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheArtfConn") //$NON-NLS-1$
					+ artifactCon.getIdent() + Messages.getString("facades.DynamicModeling.ModelingExcCannBeResolv") //$NON-NLS-1$
					+ Messages.getString("facades.DynamicModeling.ModelingExcItsSourceEnact")); //$NON-NLS-1$
		}
	}


	@Override
	public Integer defineInput_ArtifactConnection_Multiple(String con_id, String mcon_id) throws DAOException, ModelingException {
		return defineInput_ArtifactConnection_Multiple_Internal(con_id, mcon_id).getOid();
	}

	private ArtifactCon defineInput_ArtifactConnection_Multiple_Internal(String con_id, String mcon_id) throws DAOException, ModelingException {// retorna
																																				// Oid
																																				// do
																																				// ArtifactCon

		// Checks for the parameters

		Object artCon;
		try {
			artCon = artConDAO.retrieveBySecondaryKey(con_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccArtfConn") + //$NON-NLS-1$
					con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (artCon == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcArtfConn") + con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		ArtifactCon artifactCon = (ArtifactCon) artCon;

		Object multi;
		try {
			multi = multiDAO.retrieveBySecondaryKey(mcon_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccArtfConn") + //$NON-NLS-1$
					mcon_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (multi == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcMultConn") + mcon_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		MultipleCon multipleCon = (MultipleCon) multi;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		Artifact artifact = artifactCon.getTheArtifact();
		ArtifactType artifactType = artifactCon.getTheArtifactType();

		if (!multipleCon.getFromArtifactCon().contains(artifactCon) || !artifactCon.getToMultipleCon().contains(multipleCon)) {

			multipleCon.getFromArtifactCon().add(artifactCon); // Rule G2.21
			artifactCon.getToMultipleCon().add(multipleCon);

			Collection suc = this.getSuccessors(multipleCon);
			suc.remove(null);
			if (!suc.isEmpty()) {
				Iterator iter = suc.iterator();
				while (iter.hasNext()) {
					Object obj = (Object) iter.next();
					if (obj instanceof Activity) {
						Activity activity = (Activity) obj;
						if (activity instanceof Normal) {
							Normal actNorm = (Normal) obj;
							if (!this.hasTheFromArtifact(actNorm, artifact)) {
								String state = actNorm.getTheEnactionDescription().getState();
								if (state.equals("")) { // Rule G.22 //$NON-NLS-1$
									InvolvedArtifacts invArt = new InvolvedArtifacts();
									invArt.setInInvolvedArtifacts(actNorm);
									actNorm.getInvolvedArtifactToNormal().add(invArt);
									if (artifact != null) {
										invArt.setTheArtifact(artifact);
										artifact.getTheInvolvedArtifacts().add(invArt);
									}
									invArt.setTheArtifactType(artifactType);
									artifactType.getTheInvolvedArtifacts().add(invArt);

								} else if (!state.equals(Plain.FINISHED) && !state.equals(Plain.FAILED) && !state.equals(Plain.CANCELED)) { // Rule
																																			// G2.23

									InvolvedArtifacts invArt = new InvolvedArtifacts();
									invArt.setInInvolvedArtifacts(actNorm);
									actNorm.getInvolvedArtifactToNormal().add(invArt);
									if (artifact != null) {
										invArt.setTheArtifact(artifact);
										artifact.getTheInvolvedArtifacts().add(invArt);
									}
									invArt.setTheArtifactType(artifactType);
									artifactType.getTheInvolvedArtifacts().add(invArt);

									if (this.hasInvolvedAgents(actNorm))
										this.notifyAgents(actNorm, Messages.getString("facades.DynamicModeling.NotifyAgtNewInpArtf"),
												artifact.getOid(), DynamicModelingImpl.ADD, artifact.getClass(), artifact.getIdent(),
												DynamicModelingImpl.DIRECTION_IN); //$NON-NLS-1$
								}
							}
						} else if (activity instanceof Decomposed) { // Rule
																		// G2.24
							Decomposed actDec = (Decomposed) obj;

							ProcessModel refProcModel = actDec.getTheReferedProcessModel();
							if ((refProcModel != null) && !refProcModel.getTheConnection().contains(artifactCon)) {

								String state = refProcModel.getPmState();
								if (!state.equals("") //$NON-NLS-1$
										&& !state.equals(ProcessModel.FINISHED)
										&& !state.equals(ProcessModel.FAILED)
										&& !state.equals(ProcessModel.CANCELED)) {

									ArtifactCon newArtifactCon = new ArtifactCon();
									newArtifactCon.setIdent(actDec.getIdent());
									newArtifactCon.setTheArtifact(artifact);
									artifact.getTheArtifactCon().add(newArtifactCon);
									newArtifactCon.setTheArtifactType(artifactType);
									artifactType.getTheArtifactCon().add(newArtifactCon);
									refProcModel.getTheConnection().add(newArtifactCon);
									newArtifactCon.setTheProcessModel(refProcModel);
								}
							}
						}
					} else if (obj instanceof MultipleCon) { // Rule G2.25
						MultipleCon newMulti = (MultipleCon) obj;
						// Calling this method recursivelly.
						this.defineInput_ArtifactConnection_Multiple(artifactCon.getIdent(), newMulti.getIdent());
					}
				}
			}
		}
		// Persistence Operations
		artConDAO.update(artifactCon);
		multiDAO.update(multipleCon);

		return artifactCon;
	}


	@Override
	public Integer removeInput_ArtifactConnection_Multiple(String con_id, String multi_id) throws DAOException, ModelingException {// retorna
																																	// Oid
																																	// ArtifactCon

		// Checks for the parameters

		Object artCon;
		try {
			artCon = artConDAO.retrieveBySecondaryKey(con_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccArtfConn") + //$NON-NLS-1$
					con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (artCon == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcArtfConn") + con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		ArtifactCon artifactCon = (ArtifactCon) artCon;

		Object multi;
		try {
			multi = multiDAO.retrieveBySecondaryKey(multi_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccArtfConn") + //$NON-NLS-1$
					multi_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (multi == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcMultConn") + multi_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		MultipleCon multipleCon = (MultipleCon) multi;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		if (multipleCon.getFromArtifactCon().contains(artifactCon) || artifactCon.getToMultipleCon().contains(multipleCon)) {

			multipleCon.getFromArtifactCon().remove(artifactCon);
			artifactCon.getToMultipleCon().remove(multipleCon);

			// Persistence Operations
			artConDAO.update((ArtifactCon) artCon);
			multiDAO.update(multipleCon);

			return artifactCon.getOid();
		} else {
			throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheArtfConn") //$NON-NLS-1$
					+ artifactCon.getIdent() + Messages.getString("facades.DynamicModeling.ModelingExcCannBeResolv") //$NON-NLS-1$
					+ Messages.getString("facades.DynamicModeling.ModelingExcItsSourNotContains")); //$NON-NLS-1$
		}
	}


	@Override
	public void deleteConnection(String con_id) throws DAOException, WebapseeException {

		// Checks for the parameters

		Object con;
		try {
			con = conDAO.retrieveBySecondaryKey(con_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccConn") + //$NON-NLS-1$
					con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (con == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcConnection") + con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Connection connection = (Connection) con;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		ProcessModel pmodel = connection.getTheProcessModel();

		String pmState = pmodel.getPmState();

		if (pmState.equals(ProcessModel.REQUIREMENTS) || pmState.equals(ProcessModel.ABSTRACT) || pmState.equals(ProcessModel.INSTANTIATED)
				|| pmState.equals(ProcessModel.ENACTING)) { // Rule G2.27

			if (connection instanceof Sequence) {
				Sequence sequence = (Sequence) connection;

				Activity to = sequence.getToActivity();
				if (to != null) {
					String state = this.getState(to);
					if (to instanceof Normal) { // Rule G2.31
						if (state.equals("") //$NON-NLS-1$
								|| state.equals(Plain.WAITING) || state.equals(Plain.READY)) {
							to.getFromSimpleCon().remove(sequence);
							sequence.setToActivity(null);
						} else {
							throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcNotDelConAlrStart"));
						}
					} else if (to instanceof Automatic) { // Rule G2.32
						if (state.equals("") //$NON-NLS-1$
								|| state.equals(Plain.WAITING)) {
							to.getFromSimpleCon().remove(sequence);
							sequence.setToActivity(null);
						} else {
							throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcNotDelConAlrStart"));
						}
					} else if (to instanceof Decomposed) { // Rule G2.33
						to.getFromSimpleCon().remove(sequence);
						sequence.setToActivity(null);
					}
				}
				Activity from = sequence.getFromActivity();
				if (from != null) {
					String state = this.getState(from);
					if (from instanceof Normal) { // Rule G2.28
						if (state.equals("") //$NON-NLS-1$
								|| state.equals(Plain.WAITING) || state.equals(Plain.READY)) {
							from.getToSimpleCon().remove(sequence);
							sequence.setFromActivity(null);
						} else {
							throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcNotDelConAlrStart"));
						}
					} else if (from instanceof Automatic) { // Rule G2.29
						if (state.equals("") //$NON-NLS-1$
								|| state.equals(Plain.WAITING)) {
							from.getToSimpleCon().remove(sequence);
							sequence.setFromActivity(null);
						} else {
							throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcNotDelConAlrStart"));
						}
					} else if (from instanceof Decomposed) { // Rule G2.30
						from.getToSimpleCon().remove(sequence);
						sequence.setFromActivity(null);
					}
				}

			} else if (connection instanceof Feedback) {
				Feedback feedback = (Feedback) connection;

				Activity to = feedback.getToActivity();
				if (to != null) {
					String state = this.getState(to);
					if (to instanceof Normal) { // Rule G2.31
						if (state.equals("") //$NON-NLS-1$
								|| state.equals(Plain.WAITING) || state.equals(Plain.READY)) {
							to.getFromSimpleCon().remove(feedback);
							feedback.setToActivity(null);
						} else {
							throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcNotDelConAlrStart"));
						}
					} else if (to instanceof Automatic) { // Rule G2.32
						if (state.equals("") //$NON-NLS-1$
								|| state.equals(Plain.WAITING)) {
							to.getFromSimpleCon().remove(feedback);
							feedback.setToActivity(null);
						} else {
							throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcNotDelConAlrStart"));
						}
					} else if (to instanceof Decomposed) { // Rule G2.33
						to.getFromSimpleCon().remove(feedback);
						feedback.setToActivity(null);
					}
				}
				Activity from = feedback.getFromActivity();
				if (from != null) {
					String state = this.getState(from);
					if (from instanceof Normal) { // Rule G2.28
						if (state.equals("") //$NON-NLS-1$
								|| state.equals(Plain.WAITING) || state.equals(Plain.READY)) {
							from.getToSimpleCon().remove(feedback);
							feedback.setFromActivity(null);
						} else {

							throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcNotDelConAlrStart"));
						}
					} else if (from instanceof Automatic) { // Rule G2.29
						if (state.equals("") //$NON-NLS-1$
								|| state.equals(Plain.WAITING)) {
							from.getToSimpleCon().remove(feedback);
							feedback.setFromActivity(null);
						} else {

							throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcNotDelConAlrStart"));
						}
					} else if (from instanceof Decomposed) { // Rule G2.30
						from.getToSimpleCon().remove(feedback);
						feedback.setFromActivity(null);
					}
				}
				PolCondition polCondition = feedback.getTheCondition();
				if (polCondition != null) {

					feedback.removeFromTheCondition();
					polConditionDAO.delete(polCondition);
				}
			} else if (connection instanceof Join) {
				Join join = (Join) connection;

				// Deleting Successors and Predecessors instances of Activity
				Activity to = join.getToActivity();
				if (to != null) {
					String state = this.getState(to);
					if (to instanceof Normal) { // Rule G2.37
						if (state.equals("") //$NON-NLS-1$
								|| state.equals(Plain.WAITING) || state.equals(Plain.READY)) {
							to.getFromJoin().remove(join);
							join.setToActivity(null);
						}
					} else if (to instanceof Automatic) { // Rule G2.38
						if (state.equals("") //$NON-NLS-1$
								|| state.equals(Plain.WAITING)) {
							to.getFromJoin().remove(join);
							join.setToActivity(null);
						}
					} else if (to instanceof Decomposed) { // Rule G2.39
						to.getFromJoin().remove(join);
						join.setToActivity(null);
					}
				}
				Collection froms = join.getFromActivity();
				Iterator iterFroms = froms.iterator();

				// Auxiliar Collection
				Collection aux = new HashSet();
				while (iterFroms.hasNext()) {
					Activity from = (Activity) iterFroms.next();
					if (from != null) {
						aux.add(from);
					}
				}
				Iterator iterAux = aux.iterator();
				while (iterAux.hasNext()) {
					Activity from = (Activity) iterAux.next();
					if (from != null) {
						String state = this.getState(from);
						if (from instanceof Normal) { // Rule G2.34
							if (state.equals("") //$NON-NLS-1$
									|| state.equals(Plain.WAITING) || state.equals(Plain.READY)) {
								from.getToJoin().remove(join);
								join.getFromActivity().remove(from);
							}
						} else if (from instanceof Automatic) { // Rule G2.35
							if (state.equals("") //$NON-NLS-1$
									|| state.equals(Plain.WAITING)) {
								from.getToJoin().remove(join);
								join.getFromActivity().remove(from);
							}
						} else if (from instanceof Decomposed) { // Rule G2.36
							from.getToJoin().remove(join);
							join.getFromActivity().remove(from);
						}
					}
				}

				// Deleting Successors and Predecessors instances of Multiple
				// Connection
				MultipleCon toMC = join.getToMultipleCon();
				if (toMC != null) { // Rule G2.40
					if (!toMC.isFired().booleanValue()) {
						if (toMC instanceof Join) {
							Join toJoin = (Join) toMC;
							toJoin.getFromMultipleCon().remove(join);
							join.setToMultipleCon(null);
						} else { // is Branch
							Branch toBranch = (Branch) toMC;
							toBranch.setFromMultipleConnection(null);
							join.setToMultipleCon(null);
						}
					}
				}
				Collection fromsMC = join.getFromMultipleCon();
				Iterator iterFromsMC = fromsMC.iterator();
				// Auxiliar Collection
				Collection aux2 = new HashSet();
				while (iterFromsMC.hasNext()) {
					MultipleCon from = (MultipleCon) iterFromsMC.next();
					if (from != null) {
						aux2.add(from);
					}
				}
				Iterator iterAux2 = aux2.iterator();
				while (iterAux2.hasNext()) {
					MultipleCon fromMC = (MultipleCon) iterAux2.next();
					if (fromMC != null) { // Rule G2.41
						if (!fromMC.isFired().booleanValue()) {
							if (fromMC instanceof Join) {
								Join fromJoin = (Join) fromMC;
								fromJoin.setToMultipleCon(null);
								join.getFromMultipleCon().remove(fromJoin);
							} else { // is Branch
								Branch fromBranch = (Branch) fromMC;
								if (fromBranch instanceof BranchAND) {
									BranchAND fromBranchAND = (BranchAND) fromBranch;
									fromBranchAND.getToMultipleCon().remove(join);
									join.getFromMultipleCon().remove(fromBranchAND);
								} else { // is BranchCond
									BranchCond fromBranchCond = (BranchCond) fromBranch;
									Collection fromBCTMCs = fromBranchCond.getTheBranchCondToMultipleCon();
									Iterator iterFromBCTMCs = fromBCTMCs.iterator();
									while (iterFromBCTMCs.hasNext()) {
										BranchCondToMultipleCon bctmc = (BranchCondToMultipleCon) iterFromBCTMCs.next();
										if (bctmc.getTheMultipleCon().equals(join)) {
											join.getFromMultipleCon().remove(fromBranchCond);
											bctmc.setTheBranchCond(null);
											bctmc.setTheMultipleCon(null);
											fromBranchCond.getTheBranchCondToMultipleCon().remove(bctmc);

											// Removing BranchCondToMultiplecon
											// from Model

											bctmcDAO.delete(bctmc);
										}
									}
								}
							}
						}
					}
				}
			} else if (connection instanceof BranchAND) {
				BranchAND branchAND = (BranchAND) connection;

				Collection tos = branchAND.getToActivity();
				Iterator iterTos = tos.iterator();
				// Auxiliar Collection
				Collection aux = new HashSet();
				while (iterTos.hasNext()) {
					Activity from = (Activity) iterTos.next();
					if (from != null) {
						aux.add(from);
					}
				}
				Iterator iterAux = aux.iterator();
				while (iterAux.hasNext()) {
					Activity to = (Activity) iterAux.next();
					if (to != null) {
						String state = this.getState(to);
						if (to instanceof Normal) { // Rule G2.37
							if (state.equals("") //$NON-NLS-1$
									|| state.equals(Plain.WAITING) || state.equals(Plain.READY)) {
								to.getFromBranchAND().remove(branchAND);
								branchAND.getToActivity().remove(to);
							}
						} else if (to instanceof Automatic) { // Rule G2.38
							if (state.equals("") //$NON-NLS-1$
									|| state.equals(Plain.WAITING)) {
								to.getFromBranchAND().remove(branchAND);
								branchAND.getToActivity().remove(to);
							}
						} else if (to instanceof Decomposed) { // Rule G2.39
							to.getFromBranchAND().remove(branchAND);
							branchAND.getToActivity().remove(to);
						}
					}
				}
				Activity from = branchAND.getFromActivity();
				if (from != null) {
					String state = this.getState(from);
					if (from instanceof Normal) { // Rule G2.34
						if (state.equals("") //$NON-NLS-1$
								|| state.equals(Plain.WAITING) || state.equals(Plain.READY))
							from.getToBranch().remove(branchAND);
					} else if (from instanceof Automatic) { // Rule G2.35
						if (state.equals("") //$NON-NLS-1$
								|| state.equals(Plain.WAITING))
							from.getToBranch().remove(branchAND);
					} else if (from instanceof Decomposed) // Rule G2.36
						from.getToBranch().remove(branchAND);
				}

				// Deleting Successors and Predecessors instances of Multiple
				// Connection

				MultipleCon fromMC = branchAND.getFromMultipleConnection();
				if (fromMC != null) { // Rule G2.40
					if (!fromMC.isFired().booleanValue()) {
						if (fromMC instanceof Join) {
							Join fromJoin = (Join) fromMC;
							fromJoin.setToMultipleCon(null);
							branchAND.setFromMultipleConnection(null);
						} else { // is Branch
							Branch fromBranch = (Branch) fromMC;
							if (fromBranch instanceof BranchAND) {
								BranchAND fromBranchAND = (BranchAND) fromBranch;
								fromBranchAND.getToMultipleCon().remove(branchAND);
								branchAND.setFromMultipleConnection(null);
							} else { // is BranchCond
								BranchCond fromBranchCond = (BranchCond) fromBranch;
								Collection fromBCTMCs = fromBranchCond.getTheBranchCondToMultipleCon();
								Iterator iterFromBCTMCs = fromBCTMCs.iterator();
								while (iterFromBCTMCs.hasNext()) {
									BranchCondToMultipleCon bctmc = (BranchCondToMultipleCon) iterFromBCTMCs.next();
									if (bctmc.getTheMultipleCon().equals(branchAND)) {
										branchAND.setFromMultipleConnection(null);
										bctmc.setTheBranchCond(null);
										bctmc.setTheMultipleCon(null);
										fromBranchCond.getTheBranchCondToMultipleCon().remove(bctmc);

										// Removing BranchCondToMultiplecon from
										// Model

										bctmcDAO.delete(bctmc);
									}
								}
							}
						}
					}
				}
				Collection tosMC = branchAND.getToMultipleCon();
				Iterator iterTosMC = tosMC.iterator();
				// Auxiliar Collection
				Collection aux2 = new HashSet();
				while (iterTosMC.hasNext()) {
					MultipleCon fromMulti = (MultipleCon) iterTosMC.next();
					if (fromMulti != null) {
						aux2.add(fromMulti);
					}
				}
				Iterator iterAux2 = aux2.iterator();
				while (iterAux2.hasNext()) {
					MultipleCon toMC = (MultipleCon) iterAux2.next();
					if (toMC != null) { // Rule G2.41
						if (!toMC.isFired().booleanValue()) {
							if (toMC instanceof Join) {
								Join toJoin = (Join) toMC;
								branchAND.getToMultipleCon().remove(toJoin);
								toJoin.getFromMultipleCon().remove(branchAND);
							} else { // is Branch
								Branch toBranch = (Branch) toMC;
								toBranch.setFromMultipleConnection(null);
								branchAND.getToMultipleCon().remove(toBranch);
							}
						}
					}
				}
			} else if (connection instanceof BranchCond) {
				BranchCond branchCond = (BranchCond) connection;

				Collection bctas = branchCond.getTheBranchCondToActivity();
				Iterator iterBctas = bctas.iterator();
				// Auxiliar Collection
				Collection aux = new HashSet();
				while (iterBctas.hasNext()) {
					BranchCondToActivity to = (BranchCondToActivity) iterBctas.next();
					if (to != null) {
						aux.add(to);
					}
				}
				Iterator iterAux = aux.iterator();
				while (iterAux.hasNext()) {
					BranchCondToActivity bcta = (BranchCondToActivity) iterAux.next();
					Activity to = bcta.getTheActivity();
					if (to != null) {
						String state = this.getState(to);
						if (to instanceof Normal) { // Rule G2.37
							if (state.equals("") //$NON-NLS-1$
									|| state.equals(Plain.WAITING) || state.equals(Plain.READY)) {
								to.getTheBranchCondToActivity().remove(bcta);
								bcta.setTheActivity(null);
								bcta.setTheBranchCond(null);
								branchCond.getTheBranchCondToActivity().remove(bcta);

							}
						} else if (to instanceof Automatic) { // Rule G2.38
							if (state.equals("") //$NON-NLS-1$
									|| state.equals(Plain.WAITING)) {
								to.getTheBranchCondToActivity().remove(bcta);
								bcta.setTheActivity(null);
								bcta.setTheBranchCond(null);
								branchCond.getTheBranchCondToActivity().remove(bcta);

							}
						} else if (to instanceof Decomposed) { // Rule G2.39
							to.getTheBranchCondToActivity().remove(bcta);
							bcta.setTheActivity(null);
							bcta.setTheBranchCond(null);
							branchCond.getTheBranchCondToActivity().remove(bcta);

						}
					}
				}
				Activity from = branchCond.getFromActivity();
				if (from != null) {
					String state = this.getState(from);
					if (from instanceof Normal) { // Rule G2.34
						if (state.equals("") //$NON-NLS-1$
								|| state.equals(Plain.WAITING) || state.equals(Plain.READY)) {
							from.getToBranch().remove(branchCond);
							branchCond.setFromActivity(null);
						}
					} else if (from instanceof Automatic) { // Rule G2.35
						if (state.equals("") //$NON-NLS-1$
								|| state.equals(Plain.WAITING)) {
							from.getToBranch().remove(branchCond);
							branchCond.setFromActivity(null);
						}
					} else if (from instanceof Decomposed) { // Rule G2.36
						from.getToBranch().remove(branchCond);
						branchCond.setFromActivity(null);
					}
				}

				// Deleting Successors and Predecessors instances of Multiple
				// Connection

				Collection bctmcs = branchCond.getTheBranchCondToMultipleCon();
				Iterator iterBctmcs = bctmcs.iterator();
				// Auxiliar Collection
				Collection aux2 = new HashSet();
				while (iterBctmcs.hasNext()) {
					BranchCondToMultipleCon fromBCTMC = (BranchCondToMultipleCon) iterBctmcs.next();
					if (fromBCTMC != null) {
						aux2.add(fromBCTMC);
					}
				}
				Iterator iterAux2 = aux2.iterator();
				while (iterAux2.hasNext()) {
					BranchCondToMultipleCon bctmc = (BranchCondToMultipleCon) iterAux2.next();
					MultipleCon to = bctmc.getTheMultipleCon();
					if (to != null) {
						boolean fired = to.isFired().booleanValue();
						if (!fired) { // Rule G2.40
							if (to instanceof Join) {
								Join toJoin = (Join) to;
								toJoin.getFromMultipleCon().remove(bctmc.getTheBranchCond());
								bctmc.setTheBranchCond(null);
								bctmc.setTheBranchCond(null);
								branchCond.getTheBranchCondToMultipleCon().remove(bctmc);

							} else { // is Branch
								Branch toBranch = (Branch) to;
								toBranch.setFromMultipleConnection(null);
								bctmc.setTheBranchCond(null);
								bctmc.setTheBranchCond(null);
								branchCond.getTheBranchCondToMultipleCon().remove(bctmc);

							}
						}
					}
				}
				MultipleCon fromMC = branchCond.getFromMultipleConnection();
				if (fromMC != null) {
					boolean fired = fromMC.isFired().booleanValue();
					if (!fired) { // Rule G2.41
						if (fromMC instanceof Join) {
							Join fromJoin = (Join) fromMC;
							fromJoin.setToMultipleCon(null);
							branchCond.setFromMultipleConnection(null);
						} else { // is Branch
							Branch fromBranch = (Branch) fromMC;
							if (fromBranch instanceof BranchAND) {
								BranchAND fromBranchAND = (BranchAND) fromBranch;
								fromBranchAND.getToMultipleCon().remove(branchCond);
								branchCond.setFromMultipleConnection(null);
							} else {// is BranchCond
								BranchCond fromBranchCond = (BranchCond) fromBranch;
								Collection branchCondToMultipleCons = fromBranchCond.getTheBranchCondToMultipleCon();
								Iterator iterBranchCondToMultipleCons = branchCondToMultipleCons.iterator();
								while (iterBranchCondToMultipleCons.hasNext()) {
									BranchCondToMultipleCon branchCondToMultipleCon = (BranchCondToMultipleCon) iterBranchCondToMultipleCons.next();
									if (branchCondToMultipleCon.getTheBranchCond().equals(branchCond)) {
										branchCond.setFromMultipleConnection(null);
										branchCondToMultipleCon.setTheBranchCond(null);
										branchCondToMultipleCon.setTheMultipleCon(null);
										fromBranchCond.getTheBranchCondToActivity().remove(branchCondToMultipleCon);

									}
								}
							}
						}
					}
				}
			} else if (connection instanceof ArtifactCon) {
				ArtifactCon artifactCon = (ArtifactCon) connection;

				Collection actsTo = artifactCon.getToActivity();
				Iterator iterActsTo = actsTo.iterator();
				// Auxiliar Collection
				Collection aux = new HashSet();
				while (iterActsTo.hasNext()) {
					Activity to = (Activity) iterActsTo.next();
					if (to != null) {
						aux.add(to);
					}
				}
				Iterator iterAux = aux.iterator();
				while (iterAux.hasNext()) {
					Activity act = (Activity) iterAux.next();
					act.getFromArtifactCon().remove(artifactCon);
					artifactCon.getToActivity().remove(act);

					if (act instanceof Normal) {
						Normal actNorm = (Normal) act;
						Artifact artifact = artifactCon.getTheArtifact();
						if (artifact != null) {
							if (this.hasTheFromArtifact(actNorm, artifact)) {
								Collection invArts = actNorm.getInvolvedArtifactToNormal();
								Iterator iter = invArts.iterator();
								InvolvedArtifacts inv = null;
								while (iter.hasNext()) {
									InvolvedArtifacts auxInv = (InvolvedArtifacts) iter.next();
									if (auxInv != null) {
										if (auxInv.getTheArtifact() != null) {

											if (auxInv.getTheArtifact().equals(artifact)) {
												inv = auxInv;
												break;
											}
										}
									}
								}
								if (inv != null) {
									inv.removeFromInInvolvedArtifacts();

									Collection invAgents = this.getInvolvedAgents(actNorm);
									Collection<String> ids = new HashSet<String>();
									Iterator iterInvAgents = invAgents.iterator();
									while (iterInvAgents.hasNext()) {
										Agent agent = (Agent) iterInvAgents.next();
										if (agent != null) {
											ids.add(agent.getIdent());
										}
									}

									if (!invAgents.isEmpty())
										this.notifyAgents(actNorm, Messages.getString("facades.DynamicModeling.NotifyAgtRemInpArtf"), ids,
												artifact.getOid(), DynamicModelingImpl.DEL, artifact.getClass(), artifact.getIdent(),
												DynamicModelingImpl.DIRECTION_IN); //$NON-NLS-1$
								}
							}
						}
					}
				}
				Collection actsFrom = artifactCon.getFromActivity();
				Iterator iterActsFrom = actsFrom.iterator();
				// Auxiliar Collection
				Collection aux2 = new HashSet();
				while (iterActsFrom.hasNext()) {
					Activity from = (Activity) iterActsFrom.next();
					if (from != null) {
						aux2.add(from);
					}
				}
				Iterator iterAux2 = aux2.iterator();
				while (iterAux2.hasNext()) {
					Activity act = (Activity) iterAux2.next();
					act.getToArtifactCon().remove(artifactCon);
					artifactCon.getFromActivity().remove(act);

					if (act instanceof Normal) {
						Normal actNorm = (Normal) act;
						Artifact artifact = artifactCon.getTheArtifact();
						if (artifact != null) {

							if (this.hasTheToArtifact(actNorm, artifact)) {

								Collection invArts = actNorm.getInvolvedArtifactFromNormal();
								Iterator iter = invArts.iterator();
								InvolvedArtifacts inv = null;
								while (iter.hasNext()) {

									InvolvedArtifacts auxInv = (InvolvedArtifacts) iter.next();
									if (auxInv != null) {
										if (auxInv.getTheArtifact() != null) {

											if (auxInv.getTheArtifact().equals(artifact)) {
												inv = auxInv;
												break;
											}
										}
									}
								}
								if (inv != null) {
									inv.removeFromOutInvolvedArtifacts();

									Collection invAgents = this.getInvolvedAgents(actNorm);
									Collection<String> ids = new HashSet<String>();
									Iterator iterInvAgents = invAgents.iterator();
									while (iterInvAgents.hasNext()) {
										Agent agent = (Agent) iterInvAgents.next();
										if (agent != null) {
											ids.add(agent.getIdent());
										}
									}

									if (!invAgents.isEmpty())
										this.notifyAgents(actNorm, Messages.getString("facades.DynamicModeling.NotifyAgtRemOutArtf"), ids,
												artifact.getOid(), DynamicModelingImpl.DEL, artifact.getClass(), artifact.getIdent(),
												DynamicModelingImpl.DIRECTION_OUT); //$NON-NLS-1$
								}
							}
						}
					}

				}

				Collection multisTo = artifactCon.getToMultipleCon();
				Iterator iterMultisTo = multisTo.iterator();
				// Auxiliar Collection
				Collection aux3 = new HashSet();
				while (iterMultisTo.hasNext()) {
					MultipleCon to = (MultipleCon) iterMultisTo.next();
					if (to != null) {
						aux3.add(to);
					}
				}
				Iterator iterAux3 = aux3.iterator();
				while (iterAux3.hasNext()) {
					MultipleCon multiTo = (MultipleCon) iterAux3.next();
					artifactCon.getToMultipleCon().remove(multiTo);
					multiTo.getFromArtifactCon().remove(artifactCon);
				}
			}

			pmodel.getTheConnection().remove(connection);
			connection.setTheProcessModel(null);

			// Persistence Operations
			conDAO.delete(connection);

			// Dynamic Changes related code
			String processState = this.getTheProcess(pmodel).getPState();
			if (processState.equals(Process.ENACTING)) {
				this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G2.27-G2.41"); //$NON-NLS-1$
				this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																		 * ,
																		 * currentSession
																		 */);
				this.enactmentEngine.determineProcessModelStates(pmodel/*
																	 * ,
																	 * currentSession
																	 */);
			}

			pmodelDAO.update(pmodel);

		} else {
			throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheProcessModelState")); //$NON-NLS-1$
		}
	}

	@Override
	public Integer newInputArtifact(String level_id, Artifact artifact, Activity activity) throws WebapseeException, ModelingException {// retorna
		Artifact artifactAux = (Artifact) artDAO.retrieveBySecondaryKey(artifact.getIdent());
		String type_id = artifactAux.getTheArtifactType().getIdent();

		ArtifactCon artifactCon = this.newArtifactConnection_Internal(level_id);
		artifactCon = this.defineType_ArtifactConnection_Internal(artifactCon.getIdent(), type_id);
		artifactCon = this.defineInstance_ArtifactConnection_Internal(artifactCon.getIdent(), artifact.getIdent());
		artifactCon = this.defineInput_ArtifactConnection_Activity_Internal(artifactCon.getIdent(), activity.getIdent());

		return artifactCon.getOid();
	}

	//Para o Editor em Flex
	@Override
	public String newInputArtifact(String level_id, String artifactIdent, String activityIdent) throws WebapseeException, ModelingException {// retorna
		Artifact artifactAux = (Artifact) artDAO.retrieveBySecondaryKey(artifactIdent);
		Activity activity = actDAO.retrieveBySecondaryKey(activityIdent);
		String type_id = artifactAux.getTheArtifactType().getIdent();

		ArtifactCon artifactCon = this.newArtifactConnection_Internal(level_id);
		System.out.println("1-ArtifactCon: " + artifactCon.getIdent() + "; " + artifactCon.getTheArtifact());
		artifactCon = this.defineType_ArtifactConnection_Internal(artifactCon.getIdent(), type_id);
		System.out.println("2-ArtifactCon: " + artifactCon.getIdent() + "; " + artifactCon.getTheArtifact());
		artifactCon = this.defineInstance_ArtifactConnection_Internal(artifactCon.getIdent(), artifactIdent);
		System.out.println("3-ArtifactCon: " + artifactCon.getIdent() + "; " + artifactCon.getTheArtifact());
		artifactCon = this.defineInput_ArtifactConnection_Activity_Internal(artifactCon.getIdent(), activity.getIdent());
		System.out.println("4-ArtifactCon: " + artifactCon.getIdent() + "; " + artifactCon.getTheArtifact());

		activity = actDAO.retrieveBySecondaryKey(activityIdent);
		System.out.println("------------ From");
		for (ArtifactCon con : activity.getFromArtifactCon()) {
			System.out.println(con.getTheArtifact().getIdent());
		}
		System.out.println("------------ To");
		for (ArtifactCon con : activity.getToArtifactCon()) {
			System.out.println(con.getTheArtifact().getIdent());
		}

		return artifactCon.getIdent();
	}

	//Para o Editor em Flex
	@Override
	public String newOutputArtifact(String level_id, String artifactIdent, String activityIdent) throws WebapseeException, ModelingException {
		// retorna ArtifactCon Oid

		Artifact artifactAux = (Artifact) artDAO.retrieveBySecondaryKey(artifactIdent);
		Activity activity = actDAO.retrieveBySecondaryKey(activityIdent);
		String type_id = artifactAux.getTheArtifactType().getIdent();


		ArtifactCon artifactCon = artConDAO.retrieveBySecondaryKey(level_id);
		if(artifactCon == null) artifactCon = this.newArtifactConnection_Internal(level_id);
		artifactCon = this.defineType_ArtifactConnection_Internal(artifactCon.getIdent(), type_id);

		artifactCon = this.defineInstance_ArtifactConnection_Internal(artifactCon.getIdent(), artifactIdent);

		artifactCon = this.defineOutput_ArtifactConnection_Internal(artifactCon.getIdent(), activity.getIdent());

		return artifactCon.getIdent();
	}

	@Override
	public Integer newInputArtifact(String level_id, ArtifactType artifactType, Activity activity) throws WebapseeException, ModelingException { // retorna

		ArtifactCon artifactCon = this.newArtifactConnection_Internal(level_id);
		artifactCon = this.defineType_ArtifactConnection_Internal(artifactCon.getIdent(), artifactType.getIdent());
		artifactCon = this.defineInput_ArtifactConnection_Activity_Internal(artifactCon.getIdent(), activity.getIdent());

		return artifactCon.getOid();
	}


	@Override
	public Integer newInputArtifact(String level_id, Artifact artifact, MultipleCon multipleCon) throws WebapseeException, ModelingException {// ArtifactCon

		Artifact artifactAux = (Artifact) artDAO.retrieveBySecondaryKey(artifact.getIdent());
		String type_id = artifactAux.getTheArtifactType().getIdent();

		ArtifactCon artifactCon = this.newArtifactConnection_Internal(level_id);
		artifactCon = this.defineType_ArtifactConnection_Internal(artifactCon.getIdent(), type_id);
		artifactCon = this.defineInstance_ArtifactConnection_Internal(artifactCon.getIdent(), artifact.getIdent());
		artifactCon = this.defineInput_ArtifactConnection_Multiple_Internal(artifactCon.getIdent(), multipleCon.getIdent());

		return artifactCon.getOid();
	}


	@Override
	public Integer newInputArtifact(String level_id, ArtifactType artifactType, MultipleCon multipleCon) throws WebapseeException, ModelingException {// ArtifactCon
																																						// retorna
																																						// Oid

		ArtifactCon artifactCon = this.newArtifactConnection_Internal(level_id);
		artifactCon = this.defineType_ArtifactConnection_Internal(artifactCon.getIdent(), artifactType.getIdent());
		artifactCon = this.defineInput_ArtifactConnection_Activity_Internal(artifactCon.getIdent(), multipleCon.getIdent());

		return artifactCon.getOid();
	}


	@Override
	public Integer newOutputArtifact(String level_id, Artifact artifact, Activity activity) throws WebapseeException, ModelingException {
		// retorna ArtifactCon Oid

		Artifact artifactAux = (Artifact) artDAO.retrieveBySecondaryKey(artifact.getIdent());
		String type_id = artifactAux.getTheArtifactType().getIdent();

		ArtifactCon artifactCon = this.newArtifactConnection_Internal(level_id);
		artifactCon = this.defineType_ArtifactConnection_Internal(artifactCon.getIdent(), type_id);
		artifactCon = this.defineInstance_ArtifactConnection_Internal(artifactCon.getIdent(), artifact.getIdent());
		artifactCon = this.defineOutput_ArtifactConnection_Internal(artifactCon.getIdent(), activity.getIdent());

		return artifactCon.getOid();
	}


	@Override
	public Integer newOutputArtifact(String level_id, ArtifactType artifactType, Activity activity) throws WebapseeException, ModelingException {// retorna
																																					// Oid
																																					// ArtifactCon

		ArtifactCon artifactCon = this.newArtifactConnection_Internal(level_id);
		artifactCon = this.defineType_ArtifactConnection_Internal(artifactCon.getIdent(), artifactType.getIdent());
		artifactCon = this.defineOutput_ArtifactConnection_Internal(artifactCon.getIdent(), activity.getIdent());

		return artifactCon.getOid();
	}

	/**
	 * Related to section 3
	 */
	@Override
	public Integer addSimpleConnection(String act_id_from, String act_id_to, String dependency) throws DAOException, WebapseeException {
		// retorna Oid Sequence
		// Checks for the parameters

		Object act_from;
		try {
			act_from = actDAO.retrieveBySecondaryKey(act_id_from);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActiv") + //$NON-NLS-1$
					act_id_from + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (act_from == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id_from + Messages.getString("facades.DynamicModeling.DaoExc_NotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Activity activity_from = (Activity) act_from;

		Object act_to;
		try {
			act_to = actDAO.retrieveBySecondaryKey(act_id_to);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActiv") + //$NON-NLS-1$
					act_id_to + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (act_to == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id_to + Messages.getString("facades.DynamicModeling.DaoExc_NotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Activity activity_to = (Activity) act_to;

		if (activity_from.equals(activity_to))
			throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheSourceDestinyConn")); //$NON-NLS-1$

		// End Checks for the parameters

		// Now we start the implementation of the rules

		String state_from = this.getState(activity_from);
		String state_to = this.getState(activity_to);

		ProcessModel pmodel = activity_to.getTheProcessModel();

		// Rule G3.1
		if (((state_from.equals("") && (state_to.equals("")) //$NON-NLS-1$ //$NON-NLS-2$
				|| (state_from.equals("") && state_to.equals(ProcessModel.REQUIREMENTS)) //$NON-NLS-1$
				|| (state_from.equals(ProcessModel.REQUIREMENTS) && state_to.equals("")) //$NON-NLS-1$
		|| (state_from.equals(ProcessModel.REQUIREMENTS) && state_to.equals(ProcessModel.REQUIREMENTS))))
				&& !this.controlFlow(activity_to, activity_from) && !this.areConnected(activity_from, activity_to)) {

			Dependency dep = new Dependency();
			dep.setKindDep(dependency);

			Sequence seq = new Sequence();
			seq.setIdent(this.getPath(act_id_to));

			seq.setTheProcessModel(pmodel);
			pmodel.getTheConnection().add(seq);
			seq.setTheDependency(dep);
			dep.setTheSequence(seq);

			seq.setFromActivity(activity_from);
			seq.setToActivity(activity_to);

			activity_from.getToSimpleCon().add(seq);
			activity_to.getFromSimpleCon().add(seq);

			Process process = this.getTheProcess(pmodel);
			if (process.getPState().equals(Process.ENACTING)) {
				// Dynamic Changes related code
				this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G3.1"); //$NON-NLS-1$
				this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																		 * ,
																		 * currentSession
																		 */);
				this.enactmentEngine.determineProcessModelStates(pmodel/*
																	 * ,
																	 * currentSession
																	 */);

			}

			// Persistence Operations
			conDAO.save(seq);

			return seq.getOid();
		}
		// Rule G3.2
		else if (((!(state_from.equals(Plain.CANCELED) || state_from.equals(ProcessModel.CANCELED)) && !(state_from.equals(Plain.FAILED) || state_from
				.equals(ProcessModel.FAILED))) && (state_to.equals("") || state_to.equals(Plain.WAITING) || state_to.equals(ProcessModel.REQUIREMENTS) //$NON-NLS-1$
				|| state_to.equals(ProcessModel.ABSTRACT) || state_to.equals(ProcessModel.INSTANTIATED)))
				&& !this.controlFlow(activity_to, activity_from) && !this.areConnected(activity_from, activity_to)) {

			if (dependency.equals("end-start"))
				this.makeWaiting(activity_to, "Rule G3.2");

			Dependency dep = new Dependency();
			dep.setKindDep(dependency);

			Sequence seq = new Sequence();
			seq.setIdent(this.getPath(act_id_to));
			seq.setTheProcessModel(pmodel);
			pmodel.getTheConnection().add(seq);
			seq.setTheDependency(dep);
			dep.setTheSequence(seq);

			seq.setFromActivity(activity_from);
			activity_from.getToSimpleCon().add(seq);
			seq.setToActivity(activity_to);
			activity_to.getFromSimpleCon().add(seq);

			// Dynamic Changes related code
			this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G3.2"); //$NON-NLS-1$
			this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																	 * ,
																	 * currentSession
																	 */);
			this.enactmentEngine.determineProcessModelStates(pmodel/*
																 * ,
																 * currentSession
																 */);

			// Persistence Operations

			conDAO.save(seq);

			return seq.getOid();
		}
		// Rule G3.3
		else if (((state_from.equals(Plain.FINISHED) || state_from.equals(ProcessModel.FINISHED)) && (!(state_to.equals(Plain.CANCELED) || state_to
				.equals(ProcessModel.CANCELED)) && !(state_to.equals(Plain.FAILED) || state_to.equals(ProcessModel.FAILED))))
				&& !this.controlFlow(activity_to, activity_from) && !this.areConnected(activity_from, activity_to)) {

			Dependency dep = new Dependency();
			dep.setKindDep(dependency);

			Sequence seq = new Sequence();
			seq.setIdent(this.getPath(act_id_to));

			seq.setTheProcessModel(pmodel);
			pmodel.getTheConnection().add(seq);
			seq.setTheDependency(dep);
			dep.setTheSequence(seq);

			seq.setFromActivity(activity_from);
			seq.setToActivity(activity_to);

			activity_from.getToSimpleCon().add(seq);
			activity_to.getFromSimpleCon().add(seq);

			// Dynamic Changes related code
			this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G3.3"); //$NON-NLS-1$
			this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																	 * ,
																	 * currentSession
																	 */);
			this.enactmentEngine.determineProcessModelStates(pmodel/*
																 * ,
																 * currentSession
																 */);

			// Persistence Operations

			conDAO.save(seq);

			return seq.getOid();
		}
		// Rule G3.4
		else if (((!(state_from.equals(Plain.CANCELED) || state_from.equals(ProcessModel.CANCELED)) && !(state_from.equals(Plain.FAILED) || state_from
				.equals(ProcessModel.FAILED))) && (state_to.equals(Plain.READY) || state_to.equals(Plain.ACTIVE) || state_to.equals(Plain.PAUSED)
				|| state_to.equals(ProcessModel.ENACTING) || state_to.equals(ProcessModel.INSTANTIATED)))
				&& dependency.equals("end-end") && !this.controlFlow(activity_to, activity_from) //$NON-NLS-1$
				&& !this.areConnected(activity_from, activity_to)) {

			Dependency dep = new Dependency();
			dep.setKindDep(dependency);

			Sequence seq = new Sequence();
			seq.setIdent(this.getPath(act_id_to));

			seq.setTheProcessModel(pmodel);
			pmodel.getTheConnection().add(seq);
			seq.setTheDependency(dep);
			dep.setTheSequence(seq);
			seq.setFromActivity(activity_from);
			seq.setToActivity(activity_to);

			activity_from.getToSimpleCon().add(seq);

			activity_to.getFromSimpleCon().add(seq);

			// Dynamic Changes related code
			this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G3.4"); //$NON-NLS-1$
			this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																	 * ,
																	 * currentSession
																	 */);
			this.enactmentEngine.determineProcessModelStates(pmodel/*
																 * ,
																 * currentSession
																 */);

			// Persistence Operations

			conDAO.save(seq);
			return seq.getOid();
		}
		// Rule G3.5
		else if (((state_from.equals(Plain.ACTIVE) || state_from.equals(Plain.PAUSED) || state_from.equals(ProcessModel.ENACTING)) && (state_to
				.equals(Plain.READY) || state_to.equals(ProcessModel.INSTANTIATED))) && dependency.equals("start-start") //$NON-NLS-1$
				&& !this.controlFlow(activity_to, activity_from) && !this.areConnected(activity_from, activity_to)) {

			Dependency dep = new Dependency();
			dep.setKindDep(dependency);

			Sequence seq = new Sequence();
			seq.setIdent(this.getPath(act_id_to));

			seq.setTheProcessModel(pmodel);
			pmodel.getTheConnection().add(seq);
			seq.setTheDependency(dep);
			dep.setTheSequence(seq);
			seq.setFromActivity(activity_from);
			seq.setToActivity(activity_to);
			activity_from.getToSimpleCon().add(seq);

			activity_to.getFromSimpleCon().add(seq);

			// Dynamic Changes related code
			this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G3.5"); //$NON-NLS-1$
			this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																	 * ,
																	 * currentSession
																	 */);
			this.enactmentEngine.determineProcessModelStates(pmodel/*
																 * ,
																 * currentSession
																 */);

			// Persistence Operations

			conDAO.save(seq);

			return seq.getOid();
		}
		// Rule G3.6
		else if ((!(state_from.equals(Plain.CANCELED) || state_from.equals(ProcessModel.CANCELED))
				&& !(state_from.equals(Plain.FAILED) || state_from.equals(ProcessModel.FAILED))
				&& !(state_from.equals(Plain.FINISHED) || state_from.equals(ProcessModel.FINISHED)) && (state_to.equals(ProcessModel.INSTANTIATED) || state_to
				.equals(Plain.READY))) // (activity_to instanceof Normal &&
										// state_to.equals(Plain.READY)))
				&& (dependency.equals("end-start") || dependency.equals("start-start")) //$NON-NLS-1$ //$NON-NLS-2$
				&& !this.controlFlow(activity_to, activity_from) && !this.areConnected(activity_from, activity_to)) {

			if (dependency.equals("end-start"))
				this.makeWaiting(activity_to, "Rule G3.6");

			Dependency dep = new Dependency();
			dep.setKindDep(dependency);

			Sequence seq = new Sequence();
			seq.setIdent(this.getPath(act_id_to));

			seq.setTheProcessModel(activity_to.getTheProcessModel());
			pmodel.getTheConnection().add(seq);
			seq.setTheDependency(dep);
			dep.setTheSequence(seq);

			seq.setFromActivity(activity_from);
			seq.setToActivity(activity_to);
			activity_from.getToSimpleCon().add(seq);
			activity_to.getFromSimpleCon().add(seq);

			// Dynamic Changes related code
			this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G3.6"); //$NON-NLS-1$
			this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																	 * ,
																	 * currentSession
																	 */);
			this.enactmentEngine.determineProcessModelStates(pmodel/*
																 * ,
																 * currentSession
																 */);

			// Persistence Operations

			conDAO.save(seq);

			return seq.getOid();
		} else {
			throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheCombinActvState")); //$NON-NLS-1$
		}
	}


	@Override
	public Integer addFeedbackConnection(String act_id_from, String act_id_to) throws DAOException, ModelingException, ModelingWarning {// retorna
																																		// Oid
																																		// Feedback

		// Checks for the parameters

		Object act_from;
		try {
			act_from = actDAO.retrieveBySecondaryKey(act_id_from);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActiv") + //$NON-NLS-1$
					act_id_from + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (act_from == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id_from + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Activity activity_from = (Activity) act_from;

		Object act_to;
		try {
			act_to = actDAO.retrieveBySecondaryKey(act_id_to);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActiv") + //$NON-NLS-1$
					act_id_to + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (act_to == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id_to + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Activity activity_to = (Activity) act_to;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		String state_from = this.getState(activity_from);
		String state_to = this.getState(activity_to);

		ProcessModel pmodel = activity_to.getTheProcessModel();

		// Rule G3.7

		if (((state_from.equals("") && state_to.equals("")) //$NON-NLS-1$ //$NON-NLS-2$
				|| (state_from.equals("") && state_to.equals(ProcessModel.REQUIREMENTS)) //$NON-NLS-1$
				|| (state_from.equals(ProcessModel.REQUIREMENTS) && state_to.equals("")) //$NON-NLS-1$
		|| (state_from.equals(ProcessModel.REQUIREMENTS) && state_to.equals(ProcessModel.REQUIREMENTS)))) {

			boolean cf = this.controlFlow(activity_to, activity_from);
			if (!cf) {
				throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcThereControlFlowBetw")); //$NON-NLS-1$
			}

			Feedback feed = new Feedback();
			feed.setIdent(this.getPath(act_id_to));

			feed.setTheProcessModel(pmodel);
			pmodel.getTheConnection().add(feed);

			feed.setFromActivity(activity_from);
			activity_from.getToSimpleCon().add(feed);
			feed.setToActivity(activity_to);
			activity_to.getFromSimpleCon().add(feed);

			// Persistence Operations

			conDAO.save(feed);

			return feed.getOid();

		} else if ((!(state_from.equals(Plain.FINISHED) || state_from.equals(ProcessModel.FINISHED))
				&& !(state_from.equals(Plain.CANCELED) || state_from.equals(ProcessModel.CANCELED)) && !(state_from.equals(Plain.FAILED) || state_from
				.equals(ProcessModel.FAILED)))) {

			boolean cf = this.controlFlow(activity_to, activity_from);
			if (!cf) {
				throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcThereControlFlowBetw")); //$NON-NLS-1$
			}

			Feedback feed = new Feedback();
			feed.setIdent(this.getPath(act_id_to));

			feed.setTheProcessModel(pmodel);
			feed.setFromActivity(activity_from);
			activity_from.getToSimpleCon().add(feed);
			feed.setToActivity(activity_to);
			activity_to.getFromSimpleCon().add(feed);

			// Persistence Operations

			conDAO.save(feed);

			return feed.getOid();
		} else {
			throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheCombinActvStates")); //$NON-NLS-1$
		}
	}


	@Override
	public Integer newJoinAND(String dependency, String level_id) throws DAOException { // retorna
																						// Oid
																						// Join

		// Checks for the parameters

		StringTokenizer st = new StringTokenizer(level_id, "."); //$NON-NLS-1$
		String process_id = st.nextToken();

		Object proc;
		try {
			proc = procDAO.retrieveBySecondaryKey(process_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccProcess") + //$NON-NLS-1$
					process_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (proc == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcProcess") + process_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Process process = (Process) proc;

		ProcessModel pmodel = null;

		Decomposed actDecomposed = null; // it is used only if the new activity
		// Is not in the root process model.
		if (st.hasMoreTokens()) {
			String currentModel = process_id;
			while (st.hasMoreTokens()) {
				currentModel += "." + st.nextToken(); //$NON-NLS-1$
			}
			Object dec = null;
			try {
				dec = decDAO.retrieveBySecondaryKey(currentModel);
			} catch (Exception/* DAOException */e1) {
				throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActiv") + //$NON-NLS-1$
						currentModel + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e1); //$NON-NLS-1$
			}

			if (dec == null)
				throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDecomActv") + currentModel //$NON-NLS-1$
						+ Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$

			actDecomposed = (Decomposed) dec;

			pmodel = actDecomposed.getTheReferedProcessModel();
		} else {
			pmodel = process.getTheProcessModel();
		}

		// End Checks for the parameters

		// Now we start the implementation of the rules

		Dependency dep = new Dependency();
		dep.setKindDep(dependency);

		Join joinAND = new Join();

		joinAND.setIdent(level_id);
		joinAND.setFired(new Boolean(false));
		joinAND.setKindJoin("AND"); //$NON-NLS-1$

		joinAND.setTheProcessModel(pmodel);
		pmodel.getTheConnection().add(joinAND);

		joinAND.setTheDependency(dep);
		dep.setTheMultipleCon(joinAND);

		// Persistence Operations
		conDAO.save(joinAND);

		return joinAND.getOid();
	}


	@Override
	public Integer newJoinOR(String dependency, String level_id) throws DAOException {// retorna
																						// Oid
																						// Join

		// Checks for the parameters

		StringTokenizer st = new StringTokenizer(level_id, "."); //$NON-NLS-1$
		String process_id = st.nextToken();

		Object proc;
		try {
			proc = procDAO.retrieveBySecondaryKey(process_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccProcess") + //$NON-NLS-1$
					process_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (proc == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcProcess") + process_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Process process = (Process) proc;

		ProcessModel pmodel = null;

		Decomposed actDecomposed = null; // it is used only if the new activity
		// Is not in the root process model.
		if (st.hasMoreTokens()) {
			String currentModel = process_id;
			while (st.hasMoreTokens()) {
				currentModel += "." + st.nextToken(); //$NON-NLS-1$
			}
			Object dec = null;
			try {
				dec = decDAO.retrieveBySecondaryKey(currentModel);
			} catch (Exception/* DAOException */e1) {
				throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActiv") + //$NON-NLS-1$
						currentModel + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e1); //$NON-NLS-1$
			}

			if (dec == null)
				throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDecomActv") + currentModel //$NON-NLS-1$
						+ Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$

			actDecomposed = (Decomposed) dec;

			pmodel = actDecomposed.getTheReferedProcessModel();
		} else {
			pmodel = process.getTheProcessModel();
		}

		// End Checks for the parameters

		// Now we start the implementation of the rules

		Dependency dep = new Dependency();
		dep.setKindDep(dependency);

		Join joinOR = new Join();

		joinOR.setIdent(level_id);
		joinOR.setFired(new Boolean(false));
		joinOR.setKindJoin("OR"); //$NON-NLS-1$

		joinOR.setTheProcessModel(pmodel);
		pmodel.getTheConnection().add(joinOR);

		joinOR.setTheDependency(dep);
		dep.setTheMultipleCon(joinOR);

		// Persistence Operations
		conDAO.save(joinOR);

		return joinOR.getOid();
	}


	@Override
	public Integer newJoinXOR(String dependency, String level_id) throws DAOException {// retorna
																						// Oid
																						// Join

		// Checks for the parameters

		StringTokenizer st = new StringTokenizer(level_id, "."); //$NON-NLS-1$
		String process_id = st.nextToken();

		Object proc;
		try {
			proc = procDAO.retrieveBySecondaryKey(process_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccProcess") + //$NON-NLS-1$
					process_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (proc == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcProcess") + process_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Process process = (Process) proc;

		ProcessModel pmodel = null;

		Decomposed actDecomposed = null; // it is used only if the new activity
		// Is not in the root process model.
		if (st.hasMoreTokens()) {
			String currentModel = process_id;
			while (st.hasMoreTokens()) {
				currentModel += "." + st.nextToken(); //$NON-NLS-1$
			}
			Object dec = null;
			try {
				dec = decDAO.retrieveBySecondaryKey(currentModel);
			} catch (Exception/* DAOException */e1) {
				throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActiv") + //$NON-NLS-1$
						currentModel + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e1); //$NON-NLS-1$
			}

			if (dec == null)
				throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDecomActv") + currentModel //$NON-NLS-1$
						+ Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$

			actDecomposed = (Decomposed) dec;

			pmodel = actDecomposed.getTheReferedProcessModel();
		} else {
			pmodel = process.getTheProcessModel();
		}

		// End Checks for the parameters

		// Now we start the implementation of the rules

		Dependency dep = new Dependency();
		dep.setKindDep(dependency);

		Join joinXOR = new Join();

		joinXOR.setIdent(level_id);
		joinXOR.setFired(new Boolean(false));
		joinXOR.setKindJoin("XOR"); //$NON-NLS-1$

		joinXOR.setTheProcessModel(pmodel);
		pmodel.getTheConnection().add(joinXOR);

		joinXOR.setTheDependency(dep);
		dep.setTheMultipleCon(joinXOR);

		// Persistence Operations
		conDAO.save(joinXOR);

		return joinXOR.getOid();
	}


	@Override
	public Integer newBranchAND(String dependency, String level_id) throws DAOException {// retorna
																							// Oid
																							// BranchAND

		// Checks for the parameters

		StringTokenizer st = new StringTokenizer(level_id, "."); //$NON-NLS-1$
		String process_id = st.nextToken();

		Object proc;
		try {
			proc = procDAO.retrieveBySecondaryKey(process_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccProcess") + //$NON-NLS-1$
					process_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (proc == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcProcess") + process_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Process process = (Process) proc;

		ProcessModel pmodel = null;

		Decomposed actDecomposed = null; // it is used only if the new activity
		// Is not in the root process model.
		if (st.hasMoreTokens()) {
			String currentModel = process_id;
			while (st.hasMoreTokens()) {
				currentModel += "." + st.nextToken(); //$NON-NLS-1$
			}
			Object dec = null;
			try {
				dec = decDAO.retrieveBySecondaryKey(currentModel);
			} catch (Exception/* DAOException */e1) {
				throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActiv") + //$NON-NLS-1$
						currentModel + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e1); //$NON-NLS-1$
			}

			if (dec == null)
				throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDecomActv") + currentModel //$NON-NLS-1$
						+ Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$

			actDecomposed = (Decomposed) dec;

			pmodel = actDecomposed.getTheReferedProcessModel();
		} else {
			pmodel = process.getTheProcessModel();
		}

		// End Checks for the parameters

		// Now we start the implementation of the rules

		Dependency dep = new Dependency();
		dep.setKindDep(dependency);

		BranchAND branchAND = new BranchAND();

		branchAND.setIdent(level_id);
		branchAND.setFired(new Boolean(false));

		branchAND.setTheProcessModel(pmodel);
		pmodel.getTheConnection().add(branchAND);

		branchAND.setTheDependency(dep);
		dep.setTheMultipleCon(branchAND);

		// Persistence Operations
		conDAO.save(branchAND);

		return branchAND.getOid();
	}


	@Override
	public Integer newBranchOR(String dependency, String level_id) throws DAOException { // retorna
																							// Oid
																							// BranchCond

		// Checks for the parameters

		StringTokenizer st = new StringTokenizer(level_id, "."); //$NON-NLS-1$
		String process_id = st.nextToken();

		Object proc;
		try {
			proc = procDAO.retrieveBySecondaryKey(process_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccProcess") + //$NON-NLS-1$
					process_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (proc == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcProcess") + process_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Process process = (Process) proc;

		ProcessModel pmodel = null;

		Decomposed actDecomposed = null; // it is used only if the new activity
		// Is not in the root process model.
		if (st.hasMoreTokens()) {
			String currentModel = process_id;
			while (st.hasMoreTokens()) {
				currentModel += "." + st.nextToken(); //$NON-NLS-1$
			}
			Object dec = null;
			try {
				dec = decDAO.retrieveBySecondaryKey(currentModel);
			} catch (Exception/* DAOException */e1) {
				throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActiv") + //$NON-NLS-1$
						currentModel + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e1); //$NON-NLS-1$
			}

			if (dec == null)
				throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDecomActv") + currentModel //$NON-NLS-1$
						+ Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$

			actDecomposed = (Decomposed) dec;

			pmodel = actDecomposed.getTheReferedProcessModel();
		} else {
			pmodel = process.getTheProcessModel();
		}

		// End Checks for the parameters

		// Now we start the implementation of the rules

		Dependency dep = new Dependency();
		dep.setKindDep(dependency);

		BranchCond branchOR = new BranchCond();

		branchOR.setIdent(level_id);
		branchOR.setFired(new Boolean(false));
		branchOR.setKindBranch("OR"); //$NON-NLS-1$

		branchOR.setTheProcessModel(pmodel);
		pmodel.getTheConnection().add(branchOR);

		branchOR.setTheDependency(dep);
		dep.setTheMultipleCon(branchOR);

		// Persistence Operations
		conDAO.save(branchOR);

		return branchOR.getOid();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.qrconsult.spm.services.impl.DynamicModelingExtraida#newBranchXOR(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public Integer newBranchXOR(String dependency, String level_id) throws DAOException {// retorna
																							// Oid
																							// BranchCond

		// Checks for the parameters

		StringTokenizer st = new StringTokenizer(level_id, "."); //$NON-NLS-1$
		String process_id = st.nextToken();

		Object proc;
		try {
			proc = procDAO.retrieveBySecondaryKey(process_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccProcess") + //$NON-NLS-1$
					process_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (proc == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcProcess") + process_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Process process = (Process) proc;

		ProcessModel pmodel = null;

		Decomposed actDecomposed = null; // it is used only if the new activity
		// Is not in the root process model.
		if (st.hasMoreTokens()) {
			String currentModel = process_id;
			while (st.hasMoreTokens()) {
				currentModel += "." + st.nextToken(); //$NON-NLS-1$
			}
			Object dec = null;
			try {
				dec = decDAO.retrieveBySecondaryKey(currentModel);
			} catch (Exception/* DAOException */e1) {
				throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActiv") + //$NON-NLS-1$
						currentModel + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e1); //$NON-NLS-1$
			}

			if (dec == null)
				throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDecomActv") + currentModel //$NON-NLS-1$
						+ Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$

			actDecomposed = (Decomposed) dec;

			pmodel = actDecomposed.getTheReferedProcessModel();
		} else {
			pmodel = process.getTheProcessModel();
		}

		// End Checks for the parameters

		// Now we start the implementation of the rules

		Dependency dep = new Dependency();
		dep.setKindDep(dependency);

		BranchCond branchXOR = new BranchCond();

		branchXOR.setIdent(level_id);
		branchXOR.setFired(new Boolean(false));
		branchXOR.setKindBranch("XOR"); //$NON-NLS-1$

		branchXOR.setTheProcessModel(pmodel);
		pmodel.getTheConnection().add(branchXOR);

		branchXOR.setTheDependency(dep);
		dep.setTheMultipleCon(branchXOR);

		// Persistence Operations
		conDAO.save(branchXOR);

		return branchXOR.getOid();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.qrconsult.spm.services.impl.DynamicModelingExtraida#
	 * removeMultiConPredecessorConnection(java.lang.String, java.lang.String)
	 */
	@Override
	public void removeMultiConPredecessorConnection(String con_id, String from_con_id) throws DAOException, WebapseeException {

		// Checks for the parameters

		Object multi;
		try {
			multi = multiDAO.retrieveBySecondaryKey(con_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccMultConn") + //$NON-NLS-1$
					con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (multi == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcMultConn") + con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		MultipleCon multipleCon = (MultipleCon) multi;

		Object from_multi;
		try {
			from_multi = multiDAO.retrieveBySecondaryKey(from_con_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccMultConn") + //$NON-NLS-1$
					from_con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (from_multi == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcMultConn") + from_con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		MultipleCon multipleCon_from = (MultipleCon) from_multi;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		if (multipleCon_from instanceof Branch) {
			Branch fromBranch = (Branch) multipleCon_from;
			if (fromBranch instanceof BranchAND) {
				BranchAND fromBranchAND = (BranchAND) fromBranch;

				if (multipleCon instanceof Join) {
					Join join = (Join) multipleCon;
					join.getFromMultipleCon().remove(fromBranchAND);
					fromBranchAND.getToMultipleCon().remove(join);
				} else {
					Branch branch = (Branch) multipleCon;
					branch.setFromMultipleConnection(null);
					fromBranchAND.getToMultipleCon().remove(branch);
				}

			} else if (fromBranch instanceof BranchCond) {
				BranchCond fromBranchCond = (BranchCond) fromBranch;

				if (multipleCon instanceof Join) {
					Join join = (Join) multipleCon;
					Collection fromBCTMCs = fromBranchCond.getTheBranchCondToMultipleCon();
					Iterator iter2 = fromBCTMCs.iterator();
					while (iter2.hasNext()) {
						BranchCondToMultipleCon fromBCTMC = (BranchCondToMultipleCon) iter2.next();
						if (fromBCTMC.getTheMultipleCon().equals(join)) {
							fromBCTMC.setTheMultipleCon(null);
							fromBCTMC.setTheBranchCond(null);
							fromBCTMCs.remove(fromBCTMC);
							join.getFromMultipleCon().remove(fromBranchCond);

							// Dynamic Changes related code
							ProcessModel pmodel = multipleCon.getTheProcessModel();
							String processState = this.getTheProcess(pmodel).getPState();
							if (processState.equals(Process.ENACTING)) {
								this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G3.15"); //$NON-NLS-1$
								this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																						 * ,
																						 * currentSession
																						 */);
								this.enactmentEngine.determineProcessModelStates(pmodel/*
																					 * ,
																					 * currentSession
																					 */);
							}
							break;
						}
					}
				} else {
					Branch branch = (Branch) multipleCon;
					Collection fromBCTMCs = fromBranchCond.getTheBranchCondToMultipleCon();
					Iterator iter2 = fromBCTMCs.iterator();
					while (iter2.hasNext()) {
						BranchCondToMultipleCon fromBCTMC = (BranchCondToMultipleCon) iter2.next();
						if (fromBCTMC.getTheMultipleCon().equals(branch)) {
							fromBCTMC.setTheMultipleCon(null);
							fromBCTMC.setTheBranchCond(null);
							fromBCTMCs.remove(fromBCTMC);
							branch.setFromMultipleConnection(null);

							// Dynamic Changes related code
							ProcessModel pmodel = multipleCon.getTheProcessModel();
							String processState = this.getTheProcess(pmodel).getPState();
							if (processState.equals(Process.ENACTING)) {
								this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G3.15"); //$NON-NLS-1$
								this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																						 * ,
																						 * currentSession
																						 */);
								this.enactmentEngine.determineProcessModelStates(pmodel/*
																					 * ,
																					 * currentSession
																					 */);
							}
							break;
						}
					}
				}
			}
		} else if (multipleCon_from instanceof Join) {
			Join fromJoin = (Join) multipleCon_from;

			if (multipleCon instanceof Join) {
				Join join = (Join) multipleCon;
				join.getFromMultipleCon().remove(fromJoin);
				fromJoin.setToMultipleCon(null);
			} else { // is Branch
				Branch branch = (Branch) multipleCon;
				branch.setFromMultipleConnection(null);
				fromJoin.setToMultipleCon(null);
			}

			// Dynamic Changes related code
			ProcessModel pmodel = multipleCon.getTheProcessModel();
			String processState = this.getTheProcess(pmodel).getPState();
			if (processState.equals(Process.ENACTING)) {
				this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G3.15"); //$NON-NLS-1$
				this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																		 * ,
																		 * currentSession
																		 */);
				this.enactmentEngine.determineProcessModelStates(pmodel/*
																	 * ,
																	 * currentSession
																	 */);
			}
		}

		// Persistence Operations
		multiDAO.update(multipleCon);
		multiDAO.update(multipleCon_from);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.qrconsult.spm.services.impl.DynamicModelingExtraida#
	 * removeMultiConPredecessorActivity(java.lang.String, java.lang.String)
	 */
	@Override
	public void removeMultiConPredecessorActivity(String con_id, String act_id) throws DAOException, WebapseeException {

		// Checks for the parameters

		Object multi;
		try {
			multi = multiDAO.retrieveBySecondaryKey(con_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccMultConn") + //$NON-NLS-1$
					con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (multi == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcMultConn") + con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		MultipleCon multipleCon = (MultipleCon) multi;

		Object from_act;
		try {
			from_act = actDAO.retrieveBySecondaryKey(act_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActivi") + //$NON-NLS-1$
					act_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (from_act == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Activity activity_from = (Activity) from_act;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		if (multipleCon instanceof Branch) {
			Branch branch = (Branch) multipleCon;
			activity_from.getToBranch().remove(branch);
			branch.setFromActivity(null);

			// Dynamic Changes related code
			ProcessModel pmodel = multipleCon.getTheProcessModel();
			String processState = this.getTheProcess(pmodel).getPState();
			if (processState.equals(Process.ENACTING)) {
				this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G3.16"); //$NON-NLS-1$
				this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																		 * ,
																		 * currentSession
																		 */);
				this.enactmentEngine.determineProcessModelStates(pmodel/*
																	 * ,
																	 * currentSession
																	 */);
			}
		} else if (multipleCon instanceof Join) {
			Join join = (Join) multipleCon;

			activity_from.getToJoin().remove(join);
			join.getFromActivity().remove(activity_from);

			// Dynamic Changes related code
			ProcessModel pmodel = multipleCon.getTheProcessModel();
			String processState = this.getTheProcess(pmodel).getPState();
			if (processState.equals(Process.ENACTING)) {
				this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G3.16"); //$NON-NLS-1$
				this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																		 * ,
																		 * currentSession
																		 */);
				this.enactmentEngine.determineProcessModelStates(pmodel/*
																	 * ,
																	 * currentSession
																	 */);
			}
		}

		// Persistence Operations
		multiDAO.update(multipleCon);
		actDAO.update(activity_from);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.qrconsult.spm.services.impl.DynamicModelingExtraida#
	 * removeMultiConSuccessorConnection(java.lang.String, java.lang.String)
	 */
	@Override
	public void removeMultiConSuccessorConnection(String con_id, String to_con_id) throws DAOException, WebapseeException {

		// Checks for the parameters

		Object multi;
		try {
			multi = multiDAO.retrieveBySecondaryKey(con_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccMultConn") + //$NON-NLS-1$
					con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (multi == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcMultConn") + con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		MultipleCon multipleCon = (MultipleCon) multi;

		Object to_multi;
		try {
			to_multi = multiDAO.retrieveBySecondaryKey(to_con_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccMultConn") + //$NON-NLS-1$
					to_con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (to_multi == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcMultConn") + to_con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		MultipleCon multipleCon_to = (MultipleCon) to_multi;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		if (multipleCon instanceof Branch) {
			Branch branch = (Branch) multipleCon;
			if (branch instanceof BranchCond) { // Rule G5.33
				BranchCond branchCond = (BranchCond) branch;
				Collection bctmcs = branchCond.getTheBranchCondToMultipleCon();
				Iterator iter = bctmcs.iterator();
				while (iter.hasNext()) {
					BranchCondToMultipleCon bctmc = (BranchCondToMultipleCon) iter.next();
					if (bctmc.getTheMultipleCon().equals(multipleCon_to)) {
						bctmc.setTheMultipleCon(null);
						//bctmc.setCondition(""); //$NON-NLS-1$

						if (multipleCon_to instanceof Branch) {
							Branch toBranch = (Branch) multipleCon_to;
							toBranch.setFromMultipleConnection(null);

						} else if (multipleCon_to instanceof Join) {
							Join toJoin = (Join) multipleCon_to;
							toJoin.getFromMultipleCon().remove(multipleCon);
						}

						// Dynamic Changes related code
						ProcessModel pmodel = multipleCon.getTheProcessModel();
						String processState = this.getTheProcess(pmodel).getPState();
						if (processState.equals(Process.ENACTING)) {
							this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G5.33"); //$NON-NLS-1$
							this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																					 * ,
																					 * currentSession
																					 */);
							this.enactmentEngine.determineProcessModelStates(pmodel/*
																				 * ,
																				 * currentSession
																				 */);
						}
						break;
					}
				}
			} else {
				BranchAND branchAND = (BranchAND) branch;
				if (multipleCon_to instanceof Join) {
					Join joinTo = (Join) multipleCon_to;
					joinTo.getFromMultipleCon().remove(branchAND);
					branchAND.getToMultipleCon().remove(joinTo);
				} else { // is Branch
					Branch branchTo = (Branch) multipleCon_to;
					branchTo.setFromMultipleConnection(null);
					branchAND.getToMultipleCon().remove(branchTo);
				}

				// Dynamic Changes related code
				ProcessModel pmodel = multipleCon.getTheProcessModel();
				String processState = this.getTheProcess(pmodel).getPState();
				if (processState.equals(Process.ENACTING)) {
					this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G3.17"); //$NON-NLS-1$
					this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																			 * ,
																			 * currentSession
																			 */);
					this.enactmentEngine.determineProcessModelStates(pmodel/*
																		 * ,
																		 * currentSession
																		 */);
				}
			}
		} else {
			Join join = (Join) multipleCon;
			if (multipleCon_to instanceof Branch) {
				Branch toBranch = (Branch) multipleCon_to;
				toBranch.setFromMultipleConnection(null);
				join.setToMultipleCon(null);

				// Dynamic Changes related code
				ProcessModel pmodel = multipleCon.getTheProcessModel();
				String processState = this.getTheProcess(pmodel).getPState();
				if (processState.equals(Process.ENACTING)) {
					this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G3.17"); //$NON-NLS-1$
					this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																			 * ,
																			 * currentSession
																			 */);
					this.enactmentEngine.determineProcessModelStates(pmodel/*
																		 * ,
																		 * currentSession
																		 */);
				}
			} else if (multipleCon_to instanceof Join) {
				Join toJoin = (Join) multipleCon_to;
				toJoin.getFromMultipleCon().remove(join);
				join.setToMultipleCon(null);

				// Dynamic Changes related code
				ProcessModel pmodel = multipleCon.getTheProcessModel();
				String processState = this.getTheProcess(pmodel).getPState();
				if (processState.equals(Process.ENACTING)) {
					this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G3.17"); //$NON-NLS-1$
					this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																			 * ,
																			 * currentSession
																			 */);
					this.enactmentEngine.determineProcessModelStates(pmodel/*
																		 * ,
																		 * currentSession
																		 */);
				}
			}
		}
		// Persistence Operations
		multiDAO.update(multipleCon);
		multiDAO.update(multipleCon_to);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.qrconsult.spm.services.impl.DynamicModelingExtraida#
	 * removeMultiConSuccessorActivity(java.lang.String, java.lang.String)
	 */
	@Override
	public void removeMultiConSuccessorActivity(String con_id, String act_id) throws DAOException, WebapseeException {

		// Checks for the parameters

		Object multi;
		try {
			multi = multiDAO.retrieveBySecondaryKey(con_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccMultConn") + //$NON-NLS-1$
					con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (multi == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcMultConn") + con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		MultipleCon multipleCon = (MultipleCon) multi;

		Object to_act;
		try {
			to_act = actDAO.retrieveBySecondaryKey(act_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActivi") + //$NON-NLS-1$
					act_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (to_act == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Activity activity_to = (Activity) to_act;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		if (multipleCon instanceof Branch) {
			Branch branch = (Branch) multipleCon;
			if (branch instanceof BranchAND) {
				BranchAND branchAND = (BranchAND) branch;
				branchAND.getToActivity().remove(activity_to);
				activity_to.getToBranch().remove(branchAND);

				// Dynamic Changes related code
				ProcessModel pmodel = multipleCon.getTheProcessModel();
				String processState = this.getTheProcess(pmodel).getPState();
				if (processState.equals(Process.ENACTING)) {
					this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G3.18"); //$NON-NLS-1$
					this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																			 * ,
																			 * currentSession
																			 */);
					this.enactmentEngine.determineProcessModelStates(pmodel/*
																		 * ,
																		 * currentSession
																		 */);
				}

			} else if (branch instanceof BranchCond) { // Rule G5.34
				BranchCond branchCond = (BranchCond) branch;
				Collection aTBCs = branchCond.getTheBranchCondToActivity();
				Iterator iter = aTBCs.iterator();
				BranchCondToActivity aTBC = null;
				while (iter.hasNext()) {
					BranchCondToActivity aux = (BranchCondToActivity) iter.next();
					if (aux.getTheActivity().equals(activity_to)) {
						aTBC = aux;
						break;
					}
				}
				if (aTBC != null) {
					aTBC.setTheActivity(null);
					activity_to.getTheBranchCondToActivity().remove(aTBC);
					aTBC.setTheBranchCond(null);
					branchCond.getTheBranchCondToActivity().remove(aTBC);

					// Dynamic Changes related code
					ProcessModel pmodel = multipleCon.getTheProcessModel();
					String processState = this.getTheProcess(pmodel).getPState();
					if (processState.equals(Process.ENACTING)) {
						this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G5.34"); //$NON-NLS-1$
						this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																				 * ,
																				 * currentSession
																				 */);
						this.enactmentEngine.determineProcessModelStates(pmodel/*
																			 * ,
																			 * currentSession
																			 */);
					}
				}
			}
		} else if (multipleCon instanceof Join) {
			Join join = (Join) multipleCon;
			join.setToActivity(activity_to);
			activity_to.getFromJoin().remove(join);

			// Dynamic Changes related code
			ProcessModel pmodel = multipleCon.getTheProcessModel();
			String processState = this.getTheProcess(pmodel).getPState();
			if (processState.equals(Process.ENACTING)) {
				this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G3.18"); //$NON-NLS-1$
				this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																		 * ,
																		 * currentSession
																		 */);
				this.enactmentEngine.determineProcessModelStates(pmodel/*
																	 * ,
																	 * currentSession
																	 */);
			}
		}
		// Persistence Operations
		multiDAO.update(multipleCon);
		actDAO.update(activity_to);
	}

	/**
	 * Related to section 4
	 */

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.qrconsult.spm.services.impl.DynamicModelingExtraida#defineJoinTo_Activity
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public WebapseeObjectDTO defineJoinTo_Activity(String con_id, String act_id) throws DAOException, WebapseeException {// retornava
																															// Object

		// Checks for the parameters

		Object j;
		try {
			j = joinDAO.retrieveBySecondaryKey(con_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccJoin") + //$NON-NLS-1$
					con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (j == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcJoin") + con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Join join = (Join) j;

		Object to_act;
		try {
			to_act = actDAO.retrieveBySecondaryKey(act_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActivi") + //$NON-NLS-1$
					act_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (to_act == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Activity activity_to = (Activity) to_act;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		String state = this.getState(activity_to);

		Collection suc = this.getSuccessors(join);
		suc.remove(null);
		if (suc.isEmpty() && !this.controlFlow(activity_to, join)) {

			if (!join.isFired().booleanValue() && (state.equals("") || state.equals(ProcessModel.REQUIREMENTS))) { // Rule G4.1 //$NON-NLS-1$

				join.setToActivity(activity_to);
				activity_to.getFromJoin().add(join);

				// Dynamic Changes related code
				ProcessModel pmodel = join.getTheProcessModel();
				String processState = this.getTheProcess(pmodel).getPState();
				if (processState.equals(Process.ENACTING)) {
					this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G4.1"); //$NON-NLS-1$
					this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																			 * ,
																			 * currentSession
																			 */);
					this.enactmentEngine.determineProcessModelStates(pmodel/*
																		 * ,
																		 * currentSession
																		 */);
				}

			} else if (state.equals(Plain.WAITING) || state.equals(ProcessModel.REQUIREMENTS) || state.equals(ProcessModel.ABSTRACT)
					|| state.equals(ProcessModel.INSTANTIATED)) { // ||
																	// state.equals(ProcessModel.INSTANTIATED)
				// Rule G4.2

				join.setToActivity(activity_to);
				activity_to.getFromJoin().add(join);

				if (activity_to instanceof Decomposed) {
					this.makeDecomposedWaiting(((Decomposed) activity_to).getTheReferedProcessModel(), "Rule G4.2");
				}

				// Dynamic Changes related code
				ProcessModel pmodel = join.getTheProcessModel();
				this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G4.2"); //$NON-NLS-1$
				this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																		 * ,
																		 * currentSession
																		 */);
				this.enactmentEngine.determineProcessModelStates(pmodel/*
																	 * ,
																	 * currentSession
																	 */);

			} else if (activity_to instanceof Normal || activity_to instanceof Decomposed) {

				if (!join.isFired().booleanValue() && (state.equals(Plain.READY) || state.equals(ProcessModel.INSTANTIATED))) {

					if (join.getTheDependency().getKindDep().equals("end-end")) { //$NON-NLS-1$
						// Rule G4.3

						join.setToActivity(activity_to);
						activity_to.getFromJoin().add(join);

						// Dynamic Changes related code
						ProcessModel pmodel = join.getTheProcessModel();
						this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G4.3"); //$NON-NLS-1$
						this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																				 * ,
																				 * currentSession
																				 */);
						this.enactmentEngine.determineProcessModelStates(pmodel/*
																			 * ,
																			 * currentSession
																			 */);
					} else {
						// Rule G4.4

						this.makeWaiting(activity_to, "Rule G4.4");
						join.setToActivity(activity_to);
						activity_to.getFromJoin().add(join);

						// Dynamic Changes related code
						ProcessModel pmodel = join.getTheProcessModel();
						this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G4.4"); //$NON-NLS-1$
						this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																				 * ,
																				 * currentSession
																				 */);
						this.enactmentEngine.determineProcessModelStates(pmodel/*
																			 * ,
																			 * currentSession
																			 */);
					}

				} else if (join.isFired().booleanValue() && !(state.equals(Plain.FAILED) || state.equals(ProcessModel.FAILED))
						&& !(state.equals(Plain.CANCELED) || state.equals(ProcessModel.CANCELED))) {
					// Rule G4.5

					join.setToActivity(activity_to);
					activity_to.getFromJoin().add(join);

					// Dynamic Changes related code
					ProcessModel pmodel = join.getTheProcessModel();
					this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G4.5"); //$NON-NLS-1$
					this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																			 * ,
																			 * currentSession
																			 */);
					this.enactmentEngine.determineProcessModelStates(pmodel/*
																		 * ,
																		 * currentSession
																		 */);
				}
			}

			// Persistence Operations
			joinDAO.update(join);
			actDAO.update(activity_to);

		} else if ((join.getToActivity() != null) && !this.controlFlow(activity_to, join)) {
			if ((state.equals("") || state.equals(ProcessModel.REQUIREMENTS)) //$NON-NLS-1$
					&& !join.isFired().booleanValue()) {
				// Rule G4.6

				Activity toAct = join.getToActivity();

				toAct.getFromJoin().remove(join);
				join.setToActivity(activity_to);
				activity_to.getFromJoin().add(join);

				ProcessModel pmodel = join.getTheProcessModel();
				String processState = this.getTheProcess(pmodel).getPState();
				if (processState.equals(Process.ENACTING)) {
					this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G4.6"); //$NON-NLS-1$
					this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																			 * ,
																			 * currentSession
																			 */);
					this.enactmentEngine.determineProcessModelStates(pmodel/*
																		 * ,
																		 * currentSession
																		 */);
				}

				// Persistence Operations
				joinDAO.update(join);
				actDAO.update(activity_to);

				return newWebapseeObjectDTO(toAct.getOid(), toAct.getClass().getName());

			} else if (state.equals(Plain.WAITING) || state.equals(ProcessModel.REQUIREMENTS) || state.equals(ProcessModel.ABSTRACT)
					|| state.equals(ProcessModel.INSTANTIATED)) {
				// Rule G4.7

				Activity toAct = join.getToActivity();

				toAct.getFromJoin().remove(join);
				join.setToActivity(activity_to);
				activity_to.getFromJoin().add(join);

				ProcessModel pmodel = join.getTheProcessModel();
				this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G4.7"); //$NON-NLS-1$
				this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																		 * ,
																		 * currentSession
																		 */);
				this.enactmentEngine.determineProcessModelStates(pmodel/*
																	 * ,
																	 * currentSession
																	 */);

				// Persistence Operations
				joinDAO.update(join);
				actDAO.update(activity_to);

				return newWebapseeObjectDTO(toAct.getOid(), toAct.getClass().getName());

			} else if (activity_to instanceof Normal || activity_to instanceof Decomposed) {

				if (!join.isFired().booleanValue() && (state.equals(Plain.READY) || state.equals(ProcessModel.INSTANTIATED))) {

					if (join.getTheDependency().getKindDep().equals("end-end")) { //$NON-NLS-1$
						// Rule G4.8

						Activity toAct = join.getToActivity();
						toAct.getFromJoin().remove(join);
						join.setToActivity(activity_to);
						activity_to.getFromJoin().add(join);

						ProcessModel pmodel = join.getTheProcessModel();
						this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G4.8"); //$NON-NLS-1$
						this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																				 * ,
																				 * currentSession
																				 */);
						this.enactmentEngine.determineProcessModelStates(pmodel/*
																			 * ,
																			 * currentSession
																			 */);

						// Persistence Operations
						joinDAO.update(join);
						actDAO.update(activity_to);

						return newWebapseeObjectDTO(toAct.getOid(), toAct.getClass().getName());
					} else {
						// Rule G4.9

						Activity toAct = join.getToActivity();
						toAct.getFromJoin().remove(join);
						join.setToActivity(activity_to);
						activity_to.getFromJoin().add(join);

						this.makeWaiting(activity_to, "Rule G4.9");

						ProcessModel pmodel = join.getTheProcessModel();
						this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G4.9"); //$NON-NLS-1$
						this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																				 * ,
																				 * currentSession
																				 */);
						this.enactmentEngine.determineProcessModelStates(pmodel/*
																			 * ,
																			 * currentSession
																			 */);

						// Persistence Operations
						joinDAO.update(join);
						actDAO.update(activity_to);

						return newWebapseeObjectDTO(toAct.getOid(), toAct.getClass().getName());
					}
				}
			} else if (join.isFired().booleanValue() && !(state.equals(Plain.FAILED) || state.equals(ProcessModel.FAILED))
					&& !(state.equals(Plain.CANCELED) || state.equals(ProcessModel.CANCELED))) {
				// Rule G4.10

				Activity toAct = join.getToActivity();
				toAct.getFromJoin().remove(join);
				join.setToActivity(activity_to);
				activity_to.getFromJoin().add(join);

				ProcessModel pmodel = join.getTheProcessModel();
				this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G4.10"); //$NON-NLS-1$
				this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																		 * ,
																		 * currentSession
																		 */);
				this.enactmentEngine.determineProcessModelStates(pmodel/*
																	 * ,
																	 * currentSession
																	 */);

				// Persistence Operations
				joinDAO.update(join);
				actDAO.update(activity_to);

				return newWebapseeObjectDTO(toAct.getOid(), toAct.getClass().getName());
			}
		} else if ((join.getToMultipleCon() != null) && !this.controlFlow(activity_to, join)) {
			if ((state.equals("") || state.equals(ProcessModel.REQUIREMENTS)) //$NON-NLS-1$
					&& !join.isFired().booleanValue()) {
				// Rule G4.11
				MultipleCon toMulti = join.getToMultipleCon();
				if (toMulti instanceof Join) {
					Join toJoin = (Join) toMulti;
					toJoin.getFromMultipleCon().remove(join);
				} else { // is Branch
					Branch toBranch = (Branch) toMulti;
					toBranch.setFromMultipleConnection(null);
				}

				join.setToMultipleCon(null);
				join.setToActivity(activity_to);
				activity_to.getFromJoin().add(join);

				ProcessModel pmodel = join.getTheProcessModel();
				String processState = this.getTheProcess(pmodel).getPState();
				if (processState.equals(Process.ENACTING)) {
					this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G4.11"); //$NON-NLS-1$
					this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																			 * ,
																			 * currentSession
																			 */);
					this.enactmentEngine.determineProcessModelStates(pmodel/*
																		 * ,
																		 * currentSession
																		 */);
				}

				// Persistence Operations
				joinDAO.update(join);
				actDAO.update(activity_to);

				return newWebapseeObjectDTO(toMulti.getOid(), toMulti.getClass().getName());
			} else if (state.equals(Plain.WAITING) || state.equals(ProcessModel.REQUIREMENTS) || state.equals(ProcessModel.ABSTRACT)) { // ||
																																		// state.equals(ProcessModel.INSTANTIATED)
				// Rule G4.12

				MultipleCon toMulti = join.getToMultipleCon();
				if (toMulti instanceof Join) {
					Join toJoin = (Join) toMulti;
					toJoin.getFromMultipleCon().remove(join);
				} else { // is Branch
					Branch toBranch = (Branch) toMulti;
					toBranch.setFromMultipleConnection(null);
				}
				join.setToMultipleCon(null);
				join.setToActivity(activity_to);
				activity_to.getFromJoin().add(join);

				ProcessModel pmodel = join.getTheProcessModel();
				this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G4.12"); //$NON-NLS-1$
				this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																		 * ,
																		 * currentSession
																		 */);
				this.enactmentEngine.determineProcessModelStates(pmodel/*
																	 * ,
																	 * currentSession
																	 */);

				// Persistence Operations
				joinDAO.update(join);
				actDAO.update(activity_to);

				return newWebapseeObjectDTO(toMulti.getOid(), toMulti.getClass().getName());
			} else if (activity_to instanceof Normal || activity_to instanceof Decomposed) {

				if (!join.isFired().booleanValue() && (state.equals(Plain.READY) || state.equals(ProcessModel.INSTANTIATED))) {

					if (join.getTheDependency().getKindDep().equals("end-end")) { //$NON-NLS-1$
						// Rule G4.13

						MultipleCon toMulti = join.getToMultipleCon();
						if (toMulti instanceof Join) {
							Join toJoin = (Join) toMulti;
							toJoin.getFromMultipleCon().remove(join);
						} else { // is Branch
							Branch toBranch = (Branch) toMulti;
							toBranch.setFromMultipleConnection(null);
						}
						join.setToMultipleCon(null);
						join.setToActivity(activity_to);
						activity_to.getFromJoin().add(join);

						ProcessModel pmodel = join.getTheProcessModel();
						this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G4.13"); //$NON-NLS-1$
						this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																				 * ,
																				 * currentSession
																				 */);
						this.enactmentEngine.determineProcessModelStates(pmodel/*
																			 * ,
																			 * currentSession
																			 */);

						// Persistence Operations
						joinDAO.update(join);
						actDAO.update(activity_to);

						return newWebapseeObjectDTO(toMulti.getOid(), toMulti.getClass().getName());
					} else {
						// Rule G4.14

						MultipleCon toMulti = join.getToMultipleCon();
						if (toMulti instanceof Join) {
							Join toJoin = (Join) toMulti;
							toJoin.getFromMultipleCon().remove(join);
						} else { // is Branch
							Branch toBranch = (Branch) toMulti;
							toBranch.setFromMultipleConnection(null);
						}
						join.setToMultipleCon(null);
						join.setToActivity(activity_to);
						activity_to.getFromJoin().add(join);

						this.makeWaiting(activity_to, "Rule G4.14"); //$NON-NLS-1$

						ProcessModel pmodel = join.getTheProcessModel();
						this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G4.14"); //$NON-NLS-1$
						this.enactmentEngine.searchForReadyActivities(pmodel.getOid()/*
																				 * ,
																				 * currentSession
																				 */);
						this.enactmentEngine.determineProcessModelStates(pmodel/*
																			 * ,
																			 * currentSession
																			 */);

						// Persistence Operations
						joinDAO.update(join);
						actDAO.update(activity_to);

						return newWebapseeObjectDTO(toMulti.getOid(), toMulti.getClass().getName());
					}
				}
			} else if (join.isFired().booleanValue() && !(state.equals(Plain.FAILED) || state.equals(ProcessModel.FAILED))
					&& !(state.equals(Plain.CANCELED) || state.equals(ProcessModel.CANCELED))) {
				// Rule 4.15

				MultipleCon toMulti = join.getToMultipleCon();
				if (toMulti instanceof Join) {
					Join toJoin = (Join) toMulti;
					toJoin.getFromMultipleCon().remove(join);
				} else { // is Branch
					Branch toBranch = (Branch) toMulti;
					toBranch.setFromMultipleConnection(null);
				}
				join.setToMultipleCon(null);
				join.setToActivity(activity_to);
				activity_to.getFromJoin().add(join);

				ProcessModel pmodel = join.getTheProcessModel();
				this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G4.15"); //$NON-NLS-1$
				this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
				this.enactmentEngine.determineProcessModelStates(pmodel);

				// Persistence Operations
				joinDAO.update(join);
				actDAO.update(activity_to);

				return newWebapseeObjectDTO(toMulti.getOid(), toMulti.getClass().getName());
			}
		} else {

			throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcThereControlFlow")); //$NON-NLS-1$
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.qrconsult.spm.services.impl.DynamicModelingExtraida#
	 * defineJoinTo_Connection(java.lang.String, java.lang.String)
	 */
	@Override
	public Integer defineJoinTo_Connection(String con_id, String to_con_id) throws DAOException, WebapseeException { // retorna
																														// Oid
																														// Object

		// Checks for the parameters

		Object j;
		try {
			j = joinDAO.retrieveBySecondaryKey(con_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccJoin") + //$NON-NLS-1$
					con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (j == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcJoin") + con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Join join = (Join) j;

		Object to_multi;
		try {
			to_multi = multiDAO.retrieveBySecondaryKey(to_con_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccMultConn") + //$NON-NLS-1$
					to_con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (to_multi == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcMultConn") + to_con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		MultipleCon multipleCon_to = (MultipleCon) to_multi;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		Collection suc = this.getSuccessors(join);
		suc.remove(null);
		if (suc.isEmpty()) {

			if (!this.controlFlow(multipleCon_to, join)) {
				if (!join.isFired().booleanValue() && !multipleCon_to.isFired().booleanValue()) {
					// Rule G4.16

					if (multipleCon_to instanceof Join) {
						Join toJoin = (Join) multipleCon_to;
						toJoin.getFromMultipleCon().add(join);
						join.setToMultipleCon(toJoin);
					} else { // is Branch
						Branch toBranch = (Branch) multipleCon_to;
						toBranch.setFromMultipleConnection(join);
						join.setToMultipleCon(toBranch);
					}

					// Dynamic Changes related code
					ProcessModel pmodel = join.getTheProcessModel();
					String processState = this.getTheProcess(pmodel).getPState();
					if (processState.equals(Process.ENACTING)) {
						this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G4.16"); //$NON-NLS-1$
						this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
						this.enactmentEngine.determineProcessModelStates(pmodel);
					}
				} else if (join.isFired().booleanValue()) {
					// Rule G4.18

					if (multipleCon_to instanceof Join) {
						Join toJoin = (Join) multipleCon_to;
						toJoin.getFromMultipleCon().add(join);
						join.setToMultipleCon(toJoin);
					} else { // is Branch
						Branch toBranch = (Branch) multipleCon_to;
						toBranch.setFromMultipleConnection(join);
						join.setToMultipleCon(toBranch);
					}

					// Dynamic Changes related code
					ProcessModel pmodel = join.getTheProcessModel();
					String processState = this.getTheProcess(pmodel).getPState();
					if (processState.equals(Process.ENACTING)) {
						this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G4.18"); //$NON-NLS-1$
						this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
						this.enactmentEngine.determineProcessModelStates(pmodel);
					}
				}
				// Persistence Operations
				joinDAO.update(join);
				multiDAO.update(multipleCon_to);

			} else {

				throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcThereControlFlow")); //$NON-NLS-1$
			}
		} else if (join.getToActivity() != null) {
			if (!this.controlFlow(multipleCon_to, join)) {
				if (!join.isFired().booleanValue() && !multipleCon_to.isFired().booleanValue()) {
					// Rule G4.17

					Activity toAct = join.getToActivity();
					toAct.getFromJoin().remove(join);
					join.setToActivity(null);
					if (multipleCon_to instanceof Join) {
						Join toJoin = (Join) multipleCon_to;
						toJoin.getFromMultipleCon().add(join);
						join.setToMultipleCon(toJoin);
					} else { // is Branch
						Branch toBranch = (Branch) multipleCon_to;
						toBranch.setFromMultipleConnection(join);
						join.setToMultipleCon(toBranch);
					}

					// Dynamic Changes related code
					ProcessModel pmodel = join.getTheProcessModel();
					String processState = this.getTheProcess(pmodel).getPState();
					if (processState.equals(Process.ENACTING)) {
						this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G4.17"); //$NON-NLS-1$
						this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
						this.enactmentEngine.determineProcessModelStates(pmodel);
					}

					// Persistence Operations
					joinDAO.update(join);
					multiDAO.update(multipleCon_to);

					return toAct.getOid();
				} else if (join.isFired().booleanValue()) {
					// Rule G4.19

					Activity toAct = join.getToActivity();
					toAct.getFromJoin().remove(join);
					join.setToActivity(null);
					if (multipleCon_to instanceof Join) {
						Join toJoin = (Join) multipleCon_to;
						toJoin.getFromMultipleCon().add(join);
						join.setToMultipleCon(toJoin);
					} else { // is Branch
						Branch toBranch = (Branch) multipleCon_to;
						toBranch.setFromMultipleConnection(join);
						join.setToMultipleCon(toBranch);
					}

					// Dynamic Changes related code
					ProcessModel pmodel = join.getTheProcessModel();
					String processState = this.getTheProcess(pmodel).getPState();
					if (processState.equals(Process.ENACTING)) {
						this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G4.19"); //$NON-NLS-1$
						this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
						this.enactmentEngine.determineProcessModelStates(pmodel);
					}

					// Persistence Operations
					joinDAO.update(join);
					multiDAO.update(multipleCon_to);
					return toAct.getOid();
				}
			} else {
				throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcThereControlFlow")); //$NON-NLS-1$
			}
		} else if (join.getToMultipleCon() != null) {
			if (!this.controlFlow(multipleCon_to, join)) {
				if (!join.isFired().booleanValue() && !multipleCon_to.isFired().booleanValue()) {
					// Rule G4.20
					MultipleCon oldMulti = join.getToMultipleCon();

					if (multipleCon_to instanceof Join) {
						Join toJoin = (Join) multipleCon_to;

						if (oldMulti instanceof Join) {
							Join oldJoin = (Join) oldMulti;
							oldJoin.getFromMultipleCon().remove(join);
						} else { // is Branch
							Branch oldBranch = (Branch) multipleCon_to;
							oldBranch.setFromMultipleConnection(null);
						}
						toJoin.getFromMultipleCon().add(join);
						join.setToMultipleCon(toJoin);
					} else { // is Branch
						Branch toBranch = (Branch) multipleCon_to;
						if (oldMulti instanceof Join) {
							Join oldJoin = (Join) oldMulti;
							oldJoin.getFromMultipleCon().remove(join);
						} else { // is Branch
							Branch oldBranch = (Branch) multipleCon_to;
							oldBranch.setFromMultipleConnection(null);
						}
						toBranch.setFromMultipleConnection(join);
						join.setToMultipleCon(toBranch);
					}

					// Dynamic Changes related code
					ProcessModel pmodel = join.getTheProcessModel();
					String processState = this.getTheProcess(pmodel).getPState();
					if (processState.equals(Process.ENACTING)) {
						this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G4.20"); //$NON-NLS-1$
						this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
						this.enactmentEngine.determineProcessModelStates(pmodel);
					}

					// Persistence Operations
					joinDAO.update(join);
					multiDAO.update(multipleCon_to);

					return oldMulti.getOid();
				} else if (join.isFired().booleanValue()) {
					// Rule G4.21

					MultipleCon oldMulti = join.getToMultipleCon();

					if (multipleCon_to instanceof Join) {
						Join toJoin = (Join) multipleCon_to;

						if (oldMulti instanceof Join) {
							Join oldJoin = (Join) oldMulti;
							oldJoin.getFromMultipleCon().remove(join);
						} else { // is Branch
							Branch oldBranch = (Branch) multipleCon_to;
							oldBranch.setFromMultipleConnection(null);
						}
						toJoin.getFromMultipleCon().add(join);
						join.setToMultipleCon(toJoin);
					} else { // is Branch
						Branch toBranch = (Branch) multipleCon_to;
						if (oldMulti instanceof Join) {
							Join oldJoin = (Join) oldMulti;
							oldJoin.getFromMultipleCon().remove(join);
						} else { // is Branch
							Branch oldBranch = (Branch) multipleCon_to;
							oldBranch.setFromMultipleConnection(null);
						}
						toBranch.setFromMultipleConnection(join);
						join.setToMultipleCon(toBranch);
					}

					// Dynamic Changes related code
					ProcessModel pmodel = join.getTheProcessModel();
					String processState = this.getTheProcess(pmodel).getPState();
					if (processState.equals(Process.ENACTING)) {
						this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G4.21"); //$NON-NLS-1$
						this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
						this.enactmentEngine.determineProcessModelStates(pmodel);
					}

					// Persistence Operations
					joinDAO.update(join);
					multiDAO.update(multipleCon_to);

					return oldMulti.getOid();
				}
			} else {
				throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcThereControlFlow")); //$NON-NLS-1$
			}
		} else {
			throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcJoinIsNotOK")); //$NON-NLS-1$
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.qrconsult.spm.services.impl.DynamicModelingExtraida#
	 * defineJoinFrom_Connection(java.lang.String, java.lang.String)
	 */
	@Override
	public void defineJoinFrom_Connection(String con_id, String from_con_id) throws DAOException, WebapseeException {

		// Checks for the parameters

		Object j;
		try {
			j = joinDAO.retrieveBySecondaryKey(con_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccJoin") + //$NON-NLS-1$
					con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (j == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcJoin") + con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Join join = (Join) j;

		Object from_multi;
		try {
			from_multi = multiDAO.retrieveBySecondaryKey(from_con_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccMultConn") + //$NON-NLS-1$
					from_con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (from_multi == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcMultConn") + from_con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		MultipleCon multipleCon_from = (MultipleCon) from_multi;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		if (!this.isInPredecessors(multipleCon_from, join) && !this.controlFlow(join, multipleCon_from)) {

			if ((!join.isFired().booleanValue())) {

				if (join.getKindJoin().equals("XOR")) { //$NON-NLS-1$
					// Rule G4.28
					Collection preds = this.getPredecessors(multipleCon_from);
					Iterator iter = preds.iterator();
					boolean has = false;
					while (iter.hasNext()) {
						Object obj = (Object) iter.next();
						if (obj instanceof BranchAND) {
							has = true;
							break;
						}
					}
					if (!has) {
						if (multipleCon_from instanceof Join) {
							Join fromJoin = (Join) multipleCon_from;
							fromJoin.setToMultipleCon(join);
							join.getFromMultipleCon().add(fromJoin);
						} else { // is Branch
							Branch fromBranch = (Branch) multipleCon_from;
							if (fromBranch instanceof BranchAND) {
								BranchAND fromBranchAND = (BranchAND) fromBranch;
								fromBranchAND.getToMultipleCon().add(join);
								join.getFromMultipleCon().add(fromBranchAND);
							} else { // is BranchCond
								BranchCond fromBranchCond = (BranchCond) fromBranch;
								BranchCondToMultipleCon bctmc = new BranchCondToMultipleCon();
								bctmc.setTheBranchCond(fromBranchCond);
								bctmc.setTheMultipleCon(join);
								fromBranchCond.getTheBranchCondToMultipleCon().add(bctmc);
								join.getFromMultipleCon().add(fromBranchCond);
							}
						}
						// Dynamic Changes related code
						ProcessModel pmodel = join.getTheProcessModel();
						String processState = this.getTheProcess(pmodel).getPState();
						if (processState.equals(Process.ENACTING)) {
							this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G4.28"); //$NON-NLS-1$
							this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
							this.enactmentEngine.determineProcessModelStates(pmodel);
						}
					}
				} else {
					// Rule G4.22
					if (multipleCon_from instanceof Join) {
						Join fromJoin = (Join) multipleCon_from;
						fromJoin.setToMultipleCon(join);
						join.getFromMultipleCon().add(fromJoin);
					} else { // is Branch
						Branch fromBranch = (Branch) multipleCon_from;
						if (fromBranch instanceof BranchAND) {
							BranchAND fromBranchAND = (BranchAND) fromBranch;
							fromBranchAND.getToMultipleCon().add(join);
							join.getFromMultipleCon().add(fromBranchAND);
						} else { // is BranchCond
							BranchCond fromBranchCond = (BranchCond) fromBranch;
							BranchCondToMultipleCon bctmc = new BranchCondToMultipleCon();
							bctmc.setTheBranchCond(fromBranchCond);
							bctmc.setTheMultipleCon(join);
							fromBranchCond.getTheBranchCondToMultipleCon().add(bctmc);
							join.getFromMultipleCon().add(fromBranchCond);
						}
					}

					// Dynamic Changes related code
					ProcessModel pmodel = join.getTheProcessModel();
					String processState = this.getTheProcess(pmodel).getPState();
					if (processState.equals(Process.ENACTING)) {
						this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G4.22"); //$NON-NLS-1$
						this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
						this.enactmentEngine.determineProcessModelStates(pmodel);
					}
				}
			} else {
				if ((multipleCon_from.isFired().booleanValue())) {
					if (join.getKindJoin().equals("XOR")) { //$NON-NLS-1$
						// Rule G4.29
						Collection preds = this.getPredecessors(multipleCon_from);
						Iterator iter = preds.iterator();
						boolean has = false;
						while (iter.hasNext()) {
							Object obj = (Object) iter.next();
							if (obj instanceof BranchAND) {
								has = true;
								break;
							}
						}
						if (!has) {
							join.setFired(new Boolean(false));
							if (multipleCon_from instanceof Join) {
								Join fromJoin = (Join) multipleCon_from;
								fromJoin.setToMultipleCon(join);
								join.getFromMultipleCon().add(fromJoin);
							} else { // is Branch
								Branch fromBranch = (Branch) multipleCon_from;
								if (fromBranch instanceof BranchAND) {
									BranchAND fromBranchAND = (BranchAND) fromBranch;
									fromBranchAND.getToMultipleCon().add(join);
									join.getFromMultipleCon().add(fromBranchAND);
								} else { // is BranchCond
									BranchCond fromBranchCond = (BranchCond) fromBranch;
									BranchCondToMultipleCon bctmc = new BranchCondToMultipleCon();
									bctmc.setTheBranchCond(fromBranchCond);
									bctmc.setTheMultipleCon(join);
									fromBranchCond.getTheBranchCondToMultipleCon().add(bctmc);
									join.getFromMultipleCon().add(fromBranchCond);
								}
							}

							// Dynamic Changes related code
							ProcessModel pmodel = join.getTheProcessModel();
							String processState = this.getTheProcess(pmodel).getPState();
							if (processState.equals(Process.ENACTING)) {
								this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G4.29"); //$NON-NLS-1$
								this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
								this.enactmentEngine.determineProcessModelStates(pmodel);
							}
						}
					}
				} else {
					// Rule G4.23
					if (multipleCon_from instanceof Join) {
						Join fromJoin = (Join) multipleCon_from;
						fromJoin.setToMultipleCon(join);
						join.getFromMultipleCon().add(fromJoin);
					} else { // is Branch
						Branch fromBranch = (Branch) multipleCon_from;
						if (fromBranch instanceof BranchAND) {
							BranchAND fromBranchAND = (BranchAND) fromBranch;
							fromBranchAND.getToMultipleCon().add(join);
							join.getFromMultipleCon().add(fromBranchAND);
						} else { // is BranchCond
							BranchCond fromBranchCond = (BranchCond) fromBranch;
							BranchCondToMultipleCon bctmc = new BranchCondToMultipleCon();
							bctmc.setTheBranchCond(fromBranchCond);
							bctmc.setTheMultipleCon(join);
							fromBranchCond.getTheBranchCondToMultipleCon().add(bctmc);
							join.getFromMultipleCon().add(fromBranchCond);
						}
					}

					// Dynamic Changes related code
					ProcessModel pmodel = join.getTheProcessModel();
					String processState = this.getTheProcess(pmodel).getPState();
					if (processState.equals(Process.ENACTING)) {
						this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G4.23"); //$NON-NLS-1$
						this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
						this.enactmentEngine.determineProcessModelStates(pmodel);
					}
				}
			}

			// Persistence Operations
			joinDAO.update(join);
			multiDAO.update(multipleCon_from);

		} else {

			throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcThereControlFlow")); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.qrconsult.spm.services.impl.DynamicModelingExtraida#
	 * defineJoinFrom_Activity(java.lang.String, java.lang.String)
	 */
	@Override
	public void defineJoinFrom_Activity(String con_id, String act_id) throws DAOException, WebapseeException {

		// Checks for the parameters

		Object j;
		try {
			j = joinDAO.retrieveBySecondaryKey(con_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccJoin") + //$NON-NLS-1$
					con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (j == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcJoin") + con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Join join = (Join) j;

		Object from_act;
		try {
			from_act = actDAO.retrieveBySecondaryKey(act_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActivi") + //$NON-NLS-1$
					act_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (from_act == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Activity activity_from = (Activity) from_act;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		String state = this.getState(activity_from);

		if (!this.isInPredecessors(activity_from, join) && !this.controlFlow(join, activity_from)) {

			if ((!join.isFired().booleanValue())) {

				if (join.getKindJoin().equals("XOR")) { //$NON-NLS-1$
					Collection preds = this.getConnectionsFrom(activity_from);
					Iterator iter = preds.iterator();
					boolean has = false;
					while (iter.hasNext()) {
						Connection conn = (Connection) iter.next();
						if (conn instanceof BranchAND) {
							has = true;
							break;
						}
					}
					if (!has) {
						if ((state.equals("") || state.equals(ProcessModel.REQUIREMENTS)) // Rule G4.30 //$NON-NLS-1$
								|| (!(state.equals(Plain.FAILED) || state.equals(ProcessModel.FAILED)) // Rule
																										// G4.31
								&& !(state.equals(Plain.CANCELED) || state.equals(ProcessModel.CANCELED)))) {

							join.getFromActivity().add(activity_from);
							activity_from.getToJoin().add(join);

							// Dynamic Changes related code
							ProcessModel pmodel = join.getTheProcessModel();
							String processState = this.getTheProcess(pmodel).getPState();
							if (processState.equals(Process.ENACTING)) {
								this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G4.31"); //$NON-NLS-1$
								this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
								this.enactmentEngine.determineProcessModelStates(pmodel);
							}
						}
					} else {
						throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcActsFromBANDToJXOR"));
					}
				} else {
					if ((state.equals("") || state.equals(ProcessModel.REQUIREMENTS))// Rule G4.24 //$NON-NLS-1$
							|| (!(state.equals(Plain.FAILED) || state.equals(ProcessModel.FAILED)) // Rule
																									// G4.25
							&& !(state.equals(Plain.CANCELED) || state.equals(ProcessModel.CANCELED)))) {

						join.getFromActivity().add(activity_from);
						activity_from.getToJoin().add(join);

						// Dynamic Changes related code
						ProcessModel pmodel = join.getTheProcessModel();
						String processState = this.getTheProcess(pmodel).getPState();
						if (processState.equals(Process.ENACTING)) {
							this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G4.25"); //$NON-NLS-1$
							this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
							this.enactmentEngine.determineProcessModelStates(pmodel);
						}
					}
				}
			} else {
				if (join.getKindJoin().equals("XOR")) { //$NON-NLS-1$
					Collection preds = this.getConnectionsFrom(activity_from);
					Iterator iter = preds.iterator();
					boolean has = false;
					while (iter.hasNext()) {
						Connection conn = (Connection) iter.next();
						if (conn instanceof BranchAND) {
							has = true;
							break;
						}
					}
					if (!has) {
						if ((state.equals(Plain.FINISHED) || state.equals(ProcessModel.FINISHED)) // Rule
																									// G4.32
								|| (state.equals(Plain.ACTIVE) && state.equals(ProcessModel.ENACTING) // Rule
																										// G4.33
										&& state.equals(Plain.PAUSED) && join.getTheDependency().getKindDep().equals("start-start"))) { //$NON-NLS-1$

							join.getFromActivity().add(activity_from);
							activity_from.getToJoin().add(join);

							// Dynamic Changes related code
							ProcessModel pmodel = join.getTheProcessModel();
							String processState = this.getTheProcess(pmodel).getPState();
							if (processState.equals(Process.ENACTING)) {
								this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G4.33"); //$NON-NLS-1$
								this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
								this.enactmentEngine.determineProcessModelStates(pmodel);
							}
						}
					} else {
						throw new ModelingException(state + Messages.getString("facades.DynamicModeling.ModelingExcActsFromBANDToJXORStart"));
					}

				} else {
					if ((state.equals(Plain.FINISHED) || state.equals(ProcessModel.FINISHED)) // Rule
																								// G4.26
							|| (state.equals(Plain.ACTIVE) && state.equals(ProcessModel.ENACTING) // Rule
																									// G4.27
									&& state.equals(Plain.PAUSED) && join.getTheDependency().getKindDep().equals("start-start"))) { //$NON-NLS-1$

						join.getFromActivity().add(activity_from);
						activity_from.getToJoin().add(join);

						// Dynamic Changes related code
						ProcessModel pmodel = join.getTheProcessModel();
						String processState = this.getTheProcess(pmodel).getPState();
						if (processState.equals(Process.ENACTING)) {
							this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G4.27"); //$NON-NLS-1$
							this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
							this.enactmentEngine.determineProcessModelStates(pmodel);
						}
					}
				}
			}
			// Persistence Operations
			joinDAO.update(join);
			actDAO.update(activity_from);

		} else {
			throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcThereControlFlow")); //$NON-NLS-1$
		}
	}

	/**
	 * Related to section 5
	 */

	/*
	 * (non-Javadoc)
	 *
	 * @see org.qrconsult.spm.services.impl.DynamicModelingExtraida#
	 * defineBranchFromActivity(java.lang.String, java.lang.String)
	 */
	@Override
	public WebapseeObjectDTO defineBranchFromActivity(String con_id, String act_id) throws DAOException, WebapseeException { // retornava
																																// Object

		// Checks for the parameters

		Object b;
		try {
			b = branchDAO.retrieveBySecondaryKey(con_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccJoin") + //$NON-NLS-1$
					con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (b == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcBranch") + con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Branch branch = (Branch) b;

		Object from_act;
		try {
			from_act = actDAO.retrieveBySecondaryKey(act_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActivi") + //$NON-NLS-1$
					act_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (from_act == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Activity activity_from = (Activity) from_act;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		String state = this.getState(activity_from);

		Collection pred = this.getPredecessors(branch);
		pred.remove(null);

		if (pred.isEmpty() && !this.controlFlow(branch, activity_from)) {

			if (!branch.isFired().booleanValue() && ((state.equals("") || state.equals(ProcessModel.REQUIREMENTS)) //$NON-NLS-1$
					|| !(state.equals(Plain.FAILED) || state.equals(ProcessModel.FAILED))
							&& !(state.equals(Plain.CANCELED) || state.equals(ProcessModel.CANCELED)))) {
				// Rule G5.1 and G5.2

				branch.setFromActivity(activity_from);
				activity_from.getToBranch().add(branch);

				// Dynamic Changes related code
				ProcessModel pmodel = branch.getTheProcessModel();
				String processState = this.getTheProcess(pmodel).getPState();
				if (processState.equals(Process.ENACTING)) {
					this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G5.2"); //$NON-NLS-1$
					this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
					this.enactmentEngine.determineProcessModelStates(pmodel);
				}

			} else if (branch.isFired().booleanValue() && (state.equals(Plain.FINISHED) || state.equals(ProcessModel.FINISHED))) {
				// Rule G5.3

				branch.setFromActivity(activity_from);
				activity_from.getToBranch().add(branch);

				// Dynamic Changes related code
				ProcessModel pmodel = branch.getTheProcessModel();
				this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G5.3"); //$NON-NLS-1$
				this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
				this.enactmentEngine.determineProcessModelStates(pmodel);

			} else if (branch.isFired().booleanValue() && branch.getTheDependency().getKindDep().equals("start-start") //$NON-NLS-1$
					&& (state.equals(Plain.ACTIVE) || state.equals(Plain.PAUSED) || state.equals(ProcessModel.ENACTING))) {
				// Rule G5.4

				branch.setFromActivity(activity_from);
				activity_from.getToBranch().add(branch);

				// Dynamic Changes related code
				ProcessModel pmodel = branch.getTheProcessModel();
				this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G5.4"); //$NON-NLS-1$
				this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
				this.enactmentEngine.determineProcessModelStates(pmodel);
			}

			// Persistence Operations
			branchDAO.update(branch);
			actDAO.update(activity_from);

		} else if (branch.getFromActivity() != null && !this.controlFlow(branch, activity_from)) {

			Object ret = null;

			if (!branch.isFired().booleanValue() && ((state.equals("") || state.equals(ProcessModel.REQUIREMENTS)) //$NON-NLS-1$
					|| !(state.equals(Plain.FAILED) || state.equals(ProcessModel.FAILED))
							&& !(state.equals(Plain.CANCELED) || state.equals(ProcessModel.CANCELED)))) {
				// Rule G5.5 and G5.6

				if (branch instanceof BranchAND) {
					BranchAND branchAND = (BranchAND) branch;
					Activity fromAct = branchAND.getFromActivity();
					fromAct.getToBranch().remove(branchAND);

					ret = fromAct;

				} else {
					BranchCond branchCond = (BranchCond) branch;
					Activity fromAct = branchCond.getFromActivity();
					fromAct.getToBranch().remove(branchCond);

					ret = fromAct;
				}
				branch.setFromActivity(activity_from);
				activity_from.getToBranch().add(branch);

				// Dynamic Changes related code
				ProcessModel pmodel = branch.getTheProcessModel();
				String processState = this.getTheProcess(pmodel).getPState();
				if (processState.equals(Process.ENACTING)) {
					this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G5.6"); //$NON-NLS-1$
					this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
					this.enactmentEngine.determineProcessModelStates(pmodel);
				}

				// Persistence Operations
				branchDAO.update(branch);
				actDAO.update(activity_from);

				return newWebapseeObjectDTO(((Activity) ret).getOid(), ((Activity) ret).getClass().getName());

			} else if (branch.isFired().booleanValue() && (state.equals(Plain.FINISHED) || state.equals(ProcessModel.FINISHED))) {
				// Rule G5.7

				if (branch instanceof BranchAND) {
					BranchAND branchAND = (BranchAND) branch;
					Activity fromAct = branchAND.getFromActivity();
					fromAct.getToBranch().remove(branchAND);

					ret = fromAct;

				} else {
					BranchCond branchCond = (BranchCond) branch;
					Activity fromAct = branchCond.getFromActivity();
					fromAct.getToBranch().remove(branchCond);

					ret = fromAct;
				}
				branch.setFromActivity(activity_from);
				activity_from.getToBranch().add(branch);

				// Dynamic Changes related code
				ProcessModel pmodel = branch.getTheProcessModel();
				this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G5.7"); //$NON-NLS-1$
				this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
				this.enactmentEngine.determineProcessModelStates(pmodel);

				// Persistence Operations
				branchDAO.update(branch);
				actDAO.update(activity_from);

				return newWebapseeObjectDTO(((Activity) ret).getOid(), ((Activity) ret).getClass().getName());

			} else if (branch.isFired().booleanValue() && branch.getTheDependency().getKindDep().equals("start-start") //$NON-NLS-1$
					&& (state.equals(Plain.ACTIVE) || state.equals(Plain.PAUSED) || state.equals(ProcessModel.ENACTING))) {
				// Rule G5.8

				if (branch instanceof BranchAND) {
					BranchAND branchAND = (BranchAND) branch;
					Activity fromAct = branchAND.getFromActivity();
					fromAct.getToBranch().remove(branchAND);

					ret = fromAct;

				} else {
					BranchCond branchCond = (BranchCond) branch;
					Activity fromAct = branchCond.getFromActivity();
					fromAct.getToBranch().remove(branchCond);

					ret = fromAct;
				}
				branch.setFromActivity(activity_from);
				activity_from.getToBranch().add(branch);

				// Dynamic Changes related code
				ProcessModel pmodel = branch.getTheProcessModel();
				this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G5.8"); //$NON-NLS-1$
				this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
				this.enactmentEngine.determineProcessModelStates(pmodel);

				// Persistence Operations
				branchDAO.update(branch);
				actDAO.update(activity_from);

				return newWebapseeObjectDTO(((Activity) ret).getOid(), ((Activity) ret).getClass().getName());
			}
		} else if (branch.getFromMultipleConnection() != null && !this.controlFlow(branch, activity_from)) {

			if (!branch.isFired().booleanValue() && ((state.equals("") || state.equals(ProcessModel.REQUIREMENTS)) //$NON-NLS-1$
					|| !(state.equals(Plain.FAILED) || state.equals(ProcessModel.FAILED))
							&& !(state.equals(Plain.CANCELED) || state.equals(ProcessModel.CANCELED)))) {
				// Rule G5.9 and G5.10

				MultipleCon fromMulti = branch.getFromMultipleConnection();
				if (fromMulti instanceof Join) {
					Join fromJoin = (Join) fromMulti;
					fromJoin.setToMultipleCon(null);
					branch.setFromMultipleConnection(null);
					branch.setFromActivity(activity_from);
					activity_from.getToBranch().add(branch);
				} else { // is Branch
					Branch fromBranch = (Branch) fromMulti;
					if (fromBranch instanceof BranchAND) {
						BranchAND fromBranchAND = (BranchAND) fromBranch;
						fromBranchAND.getToMultipleCon().remove(branch);
						branch.setFromMultipleConnection(null);
						branch.setFromActivity(activity_from);
						activity_from.getToBranch().add(branch);
					} else { // is BranchCond
						BranchCond fromBranchCond = (BranchCond) fromBranch;
						Collection bctmcs = fromBranchCond.getTheBranchCondToMultipleCon();
						Iterator iterBctmcs = bctmcs.iterator();
						while (iterBctmcs.hasNext()) {
							BranchCondToMultipleCon bctmc = (BranchCondToMultipleCon) iterBctmcs.next();
							if (bctmc.getTheMultipleCon().equals(branch)) {
								bctmc.setTheBranchCond(null);
								bctmc.setTheMultipleCon(null);
								bctmcs.remove(bctmc);
								branch.setFromMultipleConnection(null);
								branch.setFromActivity(activity_from);
								activity_from.getToBranch().add(branch);

								// Removing from the model

								bctmcDAO.delete(bctmc);
								break;
							}
						}
					}
				}

				// Dynamic Changes related code
				ProcessModel pmodel = branch.getTheProcessModel();
				String processState = this.getTheProcess(pmodel).getPState();
				if (processState.equals(Process.ENACTING)) {
					this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G5.10"); //$NON-NLS-1$
					this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
					this.enactmentEngine.determineProcessModelStates(pmodel);
				}

				// Persistence Operations
				branchDAO.update(branch);
				actDAO.update(activity_from);

				return newWebapseeObjectDTO(fromMulti.getOid(), fromMulti.getClass().getName());

			} else if (branch.isFired().booleanValue() && (state.equals(Plain.FINISHED) || state.equals(ProcessModel.FINISHED))) {
				// Rule G5.11

				MultipleCon fromMulti = branch.getFromMultipleConnection();
				if (fromMulti instanceof Join) {
					Join fromJoin = (Join) fromMulti;
					fromJoin.setToMultipleCon(null);
					branch.setFromMultipleConnection(null);
					branch.setFromActivity(activity_from);
					activity_from.getToBranch().add(branch);
				} else { // is Branch
					Branch fromBranch = (Branch) fromMulti;
					if (fromBranch instanceof BranchAND) {
						BranchAND fromBranchAND = (BranchAND) fromBranch;
						fromBranchAND.getToMultipleCon().remove(branch);
						branch.setFromMultipleConnection(null);
						branch.setFromActivity(activity_from);
						activity_from.getToBranch().add(branch);
					} else { // is BranchCond
						BranchCond fromBranchCond = (BranchCond) fromBranch;
						Collection bctmcs = fromBranchCond.getTheBranchCondToMultipleCon();
						Iterator iterBctmcs = bctmcs.iterator();
						while (iterBctmcs.hasNext()) {
							BranchCondToMultipleCon bctmc = (BranchCondToMultipleCon) iterBctmcs.next();
							if (bctmc.getTheMultipleCon().equals(branch)) {
								bctmc.setTheBranchCond(null);
								bctmc.setTheMultipleCon(null);
								bctmcs.remove(bctmc);
								branch.setFromMultipleConnection(null);
								branch.setFromActivity(activity_from);
								activity_from.getToBranch().add(branch);

								// Removing from the model
								bctmcDAO.delete(bctmc);

								break;
							}
						}
					}
				}

				// Dynamic Changes related code
				ProcessModel pmodel = branch.getTheProcessModel();
				this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G5.11"); //$NON-NLS-1$
				this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
				this.enactmentEngine.determineProcessModelStates(pmodel);

				// Persistence Operations
				branchDAO.update(branch);
				actDAO.update(activity_from);

				return newWebapseeObjectDTO(fromMulti.getOid(), fromMulti.getClass().getName());

			} else if (branch.isFired().booleanValue() && branch.getTheDependency().getKindDep().equals("start-start") //$NON-NLS-1$
					&& (state.equals(Plain.ACTIVE) || state.equals(Plain.PAUSED) || state.equals(ProcessModel.ENACTING))) {
				// Rule G5.12

				MultipleCon fromMulti = branch.getFromMultipleConnection();
				if (fromMulti instanceof Join) {
					Join fromJoin = (Join) fromMulti;
					fromJoin.setToMultipleCon(null);
					branch.setFromMultipleConnection(null);
					branch.setFromActivity(activity_from);
					activity_from.getToBranch().add(branch);
				} else { // is Branch
					Branch fromBranch = (Branch) fromMulti;
					if (fromBranch instanceof BranchAND) {
						BranchAND fromBranchAND = (BranchAND) fromBranch;
						fromBranchAND.getToMultipleCon().remove(branch);
						branch.setFromMultipleConnection(null);
						branch.setFromActivity(activity_from);
						activity_from.getToBranch().add(branch);
					} else { // is BranchCond
						BranchCond fromBranchCond = (BranchCond) fromBranch;
						Collection bctmcs = fromBranchCond.getTheBranchCondToMultipleCon();
						Iterator iterBctmcs = bctmcs.iterator();
						while (iterBctmcs.hasNext()) {
							BranchCondToMultipleCon bctmc = (BranchCondToMultipleCon) iterBctmcs.next();
							if (bctmc.getTheMultipleCon().equals(branch)) {
								bctmc.setTheBranchCond(null);
								bctmc.setTheMultipleCon(null);
								bctmcs.remove(bctmc);
								branch.setFromMultipleConnection(null);
								branch.setFromActivity(activity_from);
								activity_from.getToBranch().add(branch);

								// Removing from the model
								bctmcDAO.delete(bctmc);

								break;
							}
						}
					}
				}

				// Dynamic Changes related code
				ProcessModel pmodel = branch.getTheProcessModel();
				this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G5.12"); //$NON-NLS-1$
				this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
				this.enactmentEngine.determineProcessModelStates(pmodel);

				// Persistence Operations
				branchDAO.update(branch);
				actDAO.update(activity_from);

				return newWebapseeObjectDTO(fromMulti.getOid(), fromMulti.getClass().getName());
			}
		} else {
			throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcThereControlFlow")); //$NON-NLS-1$
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.qrconsult.spm.services.impl.DynamicModelingExtraida#
	 * defineBranchFromConnection(java.lang.String, java.lang.String)
	 */
	@Override
	public WebapseeObjectDTO defineBranchFromConnection(String con_id, String from_con_id) throws DAOException, WebapseeException {// retornava
																																	// Object

		// Checks for the parameters

		Object b;
		try {
			b = branchDAO.retrieveBySecondaryKey(con_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccJoin") + //$NON-NLS-1$
					con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (b == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcBranch") + con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Branch branch = (Branch) b;

		Object from_multi;
		try {
			from_multi = multiDAO.retrieveBySecondaryKey(from_con_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccMultConn") + //$NON-NLS-1$
					from_con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (from_multi == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcMultConn") + from_con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		MultipleCon multipleCon_from = (MultipleCon) from_multi;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		Collection pred = this.getPredecessors(branch);
		pred.remove(null);

		if (pred.isEmpty()) {

			if ((!branch.isFired().booleanValue() || (branch.isFired().booleanValue() && multipleCon_from.isFired().booleanValue()))
					&& !this.controlFlow(branch, multipleCon_from)) {
				// Rule G5.13 and G5.16

				if (multipleCon_from instanceof Join) {
					Join fromJoin = (Join) multipleCon_from;
					fromJoin.setToMultipleCon(branch);
					branch.setFromMultipleConnection(fromJoin);
				} else { // is Branch
					Branch fromBranch = (Branch) multipleCon_from;
					if (fromBranch instanceof BranchAND) {
						BranchAND fromBranchAND = (BranchAND) fromBranch;
						fromBranchAND.getToMultipleCon().add(branch);
						branch.setFromMultipleConnection(fromBranchAND);
					} else { // is BranchCond
						BranchCond fromBranchCond = (BranchCond) fromBranch;
						BranchCondToMultipleCon bctmc = new BranchCondToMultipleCon();
						bctmc.setTheBranchCond(fromBranchCond);
						bctmc.setTheMultipleCon(branch);
						fromBranchCond.getTheBranchCondToMultipleCon().add(bctmc);
						branch.setFromMultipleConnection(fromBranchCond);
					}
				}

				// Dynamic Changes related code
				// Dynamic Changes related code
				ProcessModel pmodel = branch.getTheProcessModel();
				String processState = this.getTheProcess(pmodel).getPState();
				if (processState.equals(Process.ENACTING)) {
					this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G5.13"); //$NON-NLS-1$
					this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
					this.enactmentEngine.determineProcessModelStates(pmodel);
				}

				// Persistence Operations
				branchDAO.update(branch);
				multiDAO.update(multipleCon_from);
			} else {
				throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcThereControlFlow")); //$NON-NLS-1$
			}

		} else if (branch.getFromActivity() != null) {
			if ((!branch.isFired().booleanValue() || (branch.isFired().booleanValue() && multipleCon_from.isFired().booleanValue()))
					&& !this.controlFlow(branch, multipleCon_from)) {
				// Rule G5.14 and G5.17

				Activity fromAct = branch.getFromActivity();
				fromAct.getToBranch().remove(branch);
				if (multipleCon_from instanceof Join) {
					Join fromJoin = (Join) multipleCon_from;
					fromJoin.setToMultipleCon(branch);
					branch.setFromMultipleConnection(fromJoin);
				} else { // is Branch
					Branch fromBranch = (Branch) multipleCon_from;
					if (fromBranch instanceof BranchAND) {
						BranchAND fromBranchAND = (BranchAND) fromBranch;
						fromBranchAND.getToMultipleCon().add(branch);
						branch.setFromMultipleConnection(fromBranchAND);
					} else { // is BranchCond
						BranchCond fromBranchCond = (BranchCond) fromBranch;
						BranchCondToMultipleCon bctmc = new BranchCondToMultipleCon();
						bctmc.setTheBranchCond(fromBranchCond);
						bctmc.setTheMultipleCon(branch);
						fromBranchCond.getTheBranchCondToMultipleCon().add(bctmc);
						branch.setFromMultipleConnection(fromBranchCond);
					}
				}

				// Dynamic Changes related code
				// Dynamic Changes related code
				ProcessModel pmodel = branch.getTheProcessModel();
				String processState = this.getTheProcess(pmodel).getPState();
				if (processState.equals(Process.ENACTING)) {
					this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G5.14"); //$NON-NLS-1$
					this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
					this.enactmentEngine.determineProcessModelStates(pmodel);
				}

				// Persistence Operations
				branchDAO.update(branch);
				multiDAO.update(multipleCon_from);

				return newWebapseeObjectDTO(fromAct.getOid(), fromAct.getClass().getName());
			} else {
				throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcThereControlFlow")); //$NON-NLS-1$
			}
		} else if (!(branch.getFromMultipleConnection() == null)) {
			if ((!branch.isFired().booleanValue() || (branch.isFired().booleanValue() && multipleCon_from.isFired().booleanValue()))
					&& !this.controlFlow(branch, multipleCon_from)) {
				// Rule G5.15 and G5.18

				// Removing old Multiple Connection from Branch
				MultipleCon fromMulti = branch.getFromMultipleConnection();
				if (fromMulti instanceof Join) {
					Join fromJoin = (Join) fromMulti;
					fromJoin.setToMultipleCon(null);
				} else { // is Branch
					Branch fromBranch = (Branch) fromMulti;
					if (fromBranch instanceof BranchAND) {
						BranchAND fromBranchAND = (BranchAND) fromBranch;
						fromBranchAND.getToMultipleCon().remove(branch);
					} else { // is BranchCond
						BranchCond fromBranchCond = (BranchCond) fromBranch;
						Collection bctmcs = fromBranchCond.getTheBranchCondToMultipleCon();
						Iterator iterBctmcs = bctmcs.iterator();
						while (iterBctmcs.hasNext()) {
							BranchCondToMultipleCon bctmc = (BranchCondToMultipleCon) iterBctmcs.next();
							if (bctmc.getTheMultipleCon().equals(branch)) {
								bctmc.setTheBranchCond(null);
								bctmc.setTheMultipleCon(null);
								bctmcs.remove(bctmc);

								// Removing from the model
								bctmcDAO.delete(bctmc);
							}
						}
					}
				}

				// Adding New Multiple Connection from Branch
				if (multipleCon_from instanceof Join) {
					Join fromJoin = (Join) multipleCon_from;
					fromJoin.setToMultipleCon(branch);
					branch.setFromMultipleConnection(fromJoin);
				} else { // is Branch
					Branch fromBranch = (Branch) multipleCon_from;
					if (fromBranch instanceof BranchAND) {
						BranchAND fromBranchAND = (BranchAND) fromBranch;
						fromBranchAND.getToMultipleCon().add(branch);
						branch.setFromMultipleConnection(fromBranchAND);
					} else { // is BranchCond
						BranchCond fromBranchCond = (BranchCond) fromBranch;
						BranchCondToMultipleCon bctmc = new BranchCondToMultipleCon();
						bctmc.setTheBranchCond(fromBranchCond);
						bctmc.setTheMultipleCon(branch);
						fromBranchCond.getTheBranchCondToMultipleCon().add(bctmc);
						branch.setFromMultipleConnection(fromBranchCond);
					}
				}

				// Dynamic Changes related code
				ProcessModel pmodel = branch.getTheProcessModel();
				String processState = this.getTheProcess(pmodel).getPState();
				if (processState.equals(Process.ENACTING)) {
					this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G5.15"); //$NON-NLS-1$
					this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
					this.enactmentEngine.determineProcessModelStates(pmodel);
				}

				// Persistence Operations
				branchDAO.update(branch);
				multiDAO.update(multipleCon_from);

				return newWebapseeObjectDTO(fromMulti.getOid(), fromMulti.getClass().getName());
			} else {
				throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcThereControlFlow")); //$NON-NLS-1$
			}
		} else {
			throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcBranchIsNotOK")); //$NON-NLS-1$
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.qrconsult.spm.services.impl.DynamicModelingExtraida#
	 * defineBranchToActivity(java.lang.String, java.lang.String)
	 */
	@Override
	public void defineBranchToActivity(String con_id, String act_id) throws DAOException, WebapseeException {

		// Checks for the parameters

		Object b;
		try {
			b = branchDAO.retrieveBySecondaryKey(con_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccJoin") + //$NON-NLS-1$
					con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (b == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcBranch") + con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Branch branch = (Branch) b;

		Object to_act;
		try {
			to_act = actDAO.retrieveBySecondaryKey(act_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActivi") + //$NON-NLS-1$
					act_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (to_act == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Activity activity_to = (Activity) to_act;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		String state = this.getState(activity_to);

		if (!branch.isFired().booleanValue() && (state.equals("") || state.equals(ProcessModel.REQUIREMENTS)) //$NON-NLS-1$
				&& !this.controlFlow(activity_to, branch)) {

			if (branch instanceof BranchAND) { // Rule G5.19
				BranchAND bAND = (BranchAND) branch;
				Collection acts = bAND.getToActivity();
				if (!acts.contains(activity_to)) {
					bAND.getToActivity().add(activity_to);
					activity_to.getFromBranchAND().add(bAND);

					// Dynamic Changes related code
					ProcessModel pmodel = branch.getTheProcessModel();
					String processState = this.getTheProcess(pmodel).getPState();
					if (processState.equals(Process.ENACTING)) {
						this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G5.19"); //$NON-NLS-1$
						this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
						this.enactmentEngine.determineProcessModelStates(pmodel);
					}
				} else {
					throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcThisActvAlreadyConn")); //$NON-NLS-1$
				}
			} else {
				// Rule G5.26
				BranchCond bCond = (BranchCond) branch;
				Collection actsTBC = bCond.getTheBranchCondToActivity();
				Iterator iter = actsTBC.iterator();
				boolean has = false;
				while (iter.hasNext()) {
					BranchCondToActivity aTBC = (BranchCondToActivity) iter.next();
					if (aTBC.getTheActivity().equals(activity_to)) {
						has = true;
						break;
					}
				}
				if (!has) {
					BranchCondToActivity activityToBranchCond = new BranchCondToActivity();
					// activityToBranchCond.setCondition(condition);
					activityToBranchCond.setTheBranchCond(bCond);
					activityToBranchCond.setTheActivity(activity_to);
					activity_to.getTheBranchCondToActivity().add(activityToBranchCond);
					bCond.getTheBranchCondToActivity().add(activityToBranchCond);

					// Dynamic Changes related code
					ProcessModel pmodel = branch.getTheProcessModel();
					String processState = this.getTheProcess(pmodel).getPState();
					if (processState.equals(Process.ENACTING)) {
						this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G5.26"); //$NON-NLS-1$
						this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
						this.enactmentEngine.determineProcessModelStates(pmodel);
					}
				} else {
					throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcThisActvAlreadyConn")); //$NON-NLS-1$
				}
			}
		} else if (!branch.isFired().booleanValue()
				&& (state.equals(Plain.WAITING) || state.equals(ProcessModel.REQUIREMENTS) || state.equals(ProcessModel.ABSTRACT)) // ||
																																	// state.equals(ProcessModel.INSTANTIATED)
				&& !this.controlFlow(activity_to, branch)) {

			if (branch instanceof BranchAND) { // Rule G5.20
				BranchAND bAND = (BranchAND) branch;
				Collection acts = bAND.getToActivity();
				if (!acts.contains(activity_to)) {
					bAND.getToActivity().add(activity_to);
					activity_to.getFromBranchAND().add(bAND);

					// Dynamic Changes related code
					ProcessModel pmodel = branch.getTheProcessModel();
					this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G5.20"); //$NON-NLS-1$
					this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
					this.enactmentEngine.determineProcessModelStates(pmodel);
				} else {
					throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcThisActvAlreadyConn")); //$NON-NLS-1$
				}
			} else {
				// Rule G5.27
				BranchCond bCond = (BranchCond) branch;
				Collection actsTBC = bCond.getTheBranchCondToActivity();
				Iterator iter = actsTBC.iterator();
				boolean has = false;
				while (iter.hasNext()) {
					BranchCondToActivity aTBC = (BranchCondToActivity) iter.next();
					if (aTBC.getTheActivity().equals(activity_to)) {
						has = true;
						break;
					}
				}
				if (!has) {
					BranchCondToActivity activityToBranchCond = new BranchCondToActivity();
					// activityToBranchCond.setCondition(condition);
					activityToBranchCond.setTheBranchCond(bCond);
					activityToBranchCond.setTheActivity(activity_to);
					activity_to.getTheBranchCondToActivity().add(activityToBranchCond);
					bCond.getTheBranchCondToActivity().add(activityToBranchCond);

					// Dynamic Changes related code
					ProcessModel pmodel = branch.getTheProcessModel();
					this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G5.27"); //$NON-NLS-1$
					this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
					this.enactmentEngine.determineProcessModelStates(pmodel);
				} else {
					throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcThisActvAlreadyConn")); //$NON-NLS-1$
				}
			}
		} else if (!branch.isFired().booleanValue()
				&& ((state.equals(Plain.READY) || state.equals(ProcessModel.INSTANTIATED)) && branch.getTheDependency().getKindDep()
						.equals("end-end")) //$NON-NLS-1$
				&& !this.controlFlow(activity_to, branch)) {

			if (branch instanceof BranchAND) { // Rule G5.21
				BranchAND bAND = (BranchAND) branch;
				Collection acts = bAND.getToActivity();
				if (!acts.contains(activity_to)) {
					bAND.getToActivity().add(activity_to);
					activity_to.getFromBranchAND().add(bAND);

					// Dynamic Changes related code
					ProcessModel pmodel = branch.getTheProcessModel();
					this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G5.21"); //$NON-NLS-1$
					this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
					this.enactmentEngine.determineProcessModelStates(pmodel);
				} else {
					throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcThisActvAlreadyConn")); //$NON-NLS-1$
				}
			} else {
				// Rule G5.28
				BranchCond bCond = (BranchCond) branch;
				Collection actsTBC = bCond.getTheBranchCondToActivity();
				Iterator iter = actsTBC.iterator();
				boolean has = false;
				while (iter.hasNext()) {
					BranchCondToActivity aTBC = (BranchCondToActivity) iter.next();
					if (aTBC.getTheActivity().equals(activity_to)) {
						has = true;
						break;
					}
				}
				if (!has) {
					BranchCondToActivity activityToBranchCond = new BranchCondToActivity();
					// activityToBranchCond.setCondition(condition);
					activityToBranchCond.setTheBranchCond(bCond);
					activityToBranchCond.setTheActivity(activity_to);
					activity_to.getTheBranchCondToActivity().add(activityToBranchCond);
					bCond.getTheBranchCondToActivity().add(activityToBranchCond);

					// Dynamic Changes related code
					ProcessModel pmodel = branch.getTheProcessModel();
					this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G5.28"); //$NON-NLS-1$
					this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
					this.enactmentEngine.determineProcessModelStates(pmodel);
				} else {
					throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcThisActvAlreadyConn")); //$NON-NLS-1$
				}
			}
		} else if (!branch.isFired().booleanValue() && (state.equals(Plain.READY) || state.equals(ProcessModel.INSTANTIATED))
				&& (branch.getTheDependency().getKindDep().equals("end-start") //$NON-NLS-1$
				|| branch.getTheDependency().getKindDep().equals("start-start")) //$NON-NLS-1$
				&& !this.controlFlow(activity_to, branch)) {

			if (branch instanceof BranchAND) { // Rule G5.22
				BranchAND bAND = (BranchAND) branch;
				Collection acts = bAND.getToActivity();
				if (!acts.contains(activity_to)) {
					bAND.getToActivity().add(activity_to);
					activity_to.getFromBranchAND().add(bAND);

					this.makeWaiting(activity_to, "Rule G5.22"); //$NON-NLS-1$

					// Dynamic Changes related code
					ProcessModel pmodel = branch.getTheProcessModel();
					this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G5.22"); //$NON-NLS-1$
					this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
					this.enactmentEngine.determineProcessModelStates(pmodel);
				} else {
					throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcThisActvAlreadyConn")); //$NON-NLS-1$
				}
			} else {
				// Rule G5.29
				BranchCond bCond = (BranchCond) branch;
				Collection actsTBC = bCond.getTheBranchCondToActivity();
				Iterator iter = actsTBC.iterator();
				boolean has = false;
				while (iter.hasNext()) {
					BranchCondToActivity aTBC = (BranchCondToActivity) iter.next();
					if (aTBC.getTheActivity().equals(activity_to)) {
						has = true;
						break;
					}
				}
				if (!has) {
					BranchCondToActivity activityToBranchCond = new BranchCondToActivity();
					// activityToBranchCond.setCondition(condition);
					activityToBranchCond.setTheBranchCond(bCond);
					activityToBranchCond.setTheActivity(activity_to);
					activity_to.getTheBranchCondToActivity().add(activityToBranchCond);
					bCond.getTheBranchCondToActivity().add(activityToBranchCond);

					this.makeWaiting(activity_to, "Rule G5.29"); //$NON-NLS-1$

					// Dynamic Changes related code
					ProcessModel pmodel = branch.getTheProcessModel();
					this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G5.29"); //$NON-NLS-1$
					this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
					this.enactmentEngine.determineProcessModelStates(pmodel);
				} else {
					throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcThisActvAlreadyConn")); //$NON-NLS-1$
				}
			}
		} else if (branch.isFired().booleanValue()
				&& (!(state.equals(Plain.FAILED) || state.equals(ProcessModel.FAILED)) && !(state.equals(Plain.CANCELED) || state
						.equals(ProcessModel.CANCELED))) && !this.controlFlow(activity_to, branch)) {

			if (branch instanceof BranchAND) { // Rule G5.23
				BranchAND bAND = (BranchAND) branch;
				Collection acts = bAND.getToActivity();
				if (!acts.contains(activity_to)) {
					bAND.getToActivity().add(activity_to);
					activity_to.getFromBranchAND().add(bAND);

					// Dynamic Changes related code
					ProcessModel pmodel = branch.getTheProcessModel();
					this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G5.23"); //$NON-NLS-1$
					this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
					this.enactmentEngine.determineProcessModelStates(pmodel);
				} else {
					throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcThisActvAlreadyConn")); //$NON-NLS-1$
				}
			} else {
				// Rule G5.30
				BranchCond bCond = (BranchCond) branch;
				Collection actsTBC = bCond.getTheBranchCondToActivity();
				Iterator iter = actsTBC.iterator();
				boolean has = false;
				while (iter.hasNext()) {
					BranchCondToActivity aTBC = (BranchCondToActivity) iter.next();
					if (aTBC.getTheActivity().equals(activity_to)) {
						has = true;
						break;
					}
				}
				if (!has) {
					BranchCondToActivity activityToBranchCond = new BranchCondToActivity();
					// activityToBranchCond.setCondition(condition);
					activityToBranchCond.setTheBranchCond(bCond);
					activityToBranchCond.setTheActivity(activity_to);
					activity_to.getTheBranchCondToActivity().add(activityToBranchCond);
					bCond.getTheBranchCondToActivity().add(activityToBranchCond);

					// Dynamic Changes related code
					ProcessModel pmodel = branch.getTheProcessModel();
					this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G5.30"); //$NON-NLS-1$
					this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
					this.enactmentEngine.determineProcessModelStates(pmodel);
				} else {
					throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcThisActvAlreadyConn")); //$NON-NLS-1$
				}
			}
		} else {
			throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheDependOrContr")); //$NON-NLS-1$
		}

		// Persistence Operations
		actDAO.update(activity_to);
		branchDAO.update(branch);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.qrconsult.spm.services.impl.DynamicModelingExtraida#
	 * defineBranchToConnection(java.lang.String, java.lang.String)
	 */
	@Override
	public void defineBranchToConnection(String con_id, String to_con_id) throws DAOException, WebapseeException {
		// Checks for the parameters

		Object b;
		try {
			b = branchDAO.retrieveBySecondaryKey(con_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccJoin") + //$NON-NLS-1$
					con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (b == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcBranch") + con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Branch branch = (Branch) b;

		Object to_multi;
		try {
			to_multi = multiDAO.retrieveBySecondaryKey(to_con_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccMultConn") + //$NON-NLS-1$
					to_con_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (to_multi == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcMultConn") + to_con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		MultipleCon multipleCon_to = (MultipleCon) to_multi;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		if (!branch.isFired().booleanValue() && !multipleCon_to.isFired().booleanValue() && !this.controlFlow(multipleCon_to, branch)) {

			if (branch instanceof BranchAND) { // Rule G5.24
				BranchAND bAND = (BranchAND) branch;
				Collection multis = bAND.getToMultipleCon();
				if (!multis.contains(multipleCon_to)) {

					if (multipleCon_to instanceof Join) {
						Join toJoin = (Join) multipleCon_to;
						toJoin.getFromMultipleCon().add(bAND);
						bAND.getToMultipleCon().add(toJoin);
					} else { // is Branch
						Branch toBranch = (Branch) multipleCon_to;
						toBranch.setFromMultipleConnection(bAND);
						bAND.getToMultipleCon().add(toBranch);
					}

					// Dynamic Changes related code
					ProcessModel pmodel = branch.getTheProcessModel();
					String processState = this.getTheProcess(pmodel).getPState();
					if (processState.equals(Process.ENACTING)) {
						this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G5.24"); //$NON-NLS-1$
						this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
						this.enactmentEngine.determineProcessModelStates(pmodel);
					}
				} else {
					throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcThisMultConnIsAlreConn")); //$NON-NLS-1$
				}
			} else { // Rule G5.31
				BranchCond bCond = (BranchCond) branch;
				Collection multisTBC = bCond.getTheBranchCondToMultipleCon();
				Iterator iter = multisTBC.iterator();
				boolean has = false;
				while (iter.hasNext()) {
					BranchCondToMultipleCon bCTMC = (BranchCondToMultipleCon) iter.next();
					if (bCTMC.getTheMultipleCon().equals(multipleCon_to)) {
						has = true;
						break;
					}
				}
				if (!has) {
					if (multipleCon_to instanceof Join) {
						Join toJoin = (Join) multipleCon_to;

						BranchCondToMultipleCon branchCondToMultipleCon = new BranchCondToMultipleCon();
						// branchCondToMultipleCon.setCondition(condition);
						branchCondToMultipleCon.setTheBranchCond(bCond);
						branchCondToMultipleCon.setTheMultipleCon(toJoin);
						toJoin.getFromMultipleCon().add(bCond);
						bCond.getTheBranchCondToMultipleCon().add(branchCondToMultipleCon);

						// Dynamic Changes related code
						ProcessModel pmodel = branch.getTheProcessModel();
						String processState = this.getTheProcess(pmodel).getPState();
						if (processState.equals(Process.ENACTING)) {
							this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G5.31"); //$NON-NLS-1$
							this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
							this.enactmentEngine.determineProcessModelStates(pmodel);
						}
					} else { // is Branch
						Branch toBranch = (Branch) multipleCon_to;

						BranchCondToMultipleCon branchCondToMultipleCon = new BranchCondToMultipleCon();
						// branchCondToMultipleCon.setCondition(condition);
						branchCondToMultipleCon.setTheBranchCond(bCond);
						branchCondToMultipleCon.setTheMultipleCon(toBranch);
						toBranch.setFromMultipleConnection(bCond);
						bCond.getTheBranchCondToMultipleCon().add(branchCondToMultipleCon);

						// Dynamic Changes related code
						ProcessModel pmodel = branch.getTheProcessModel();
						String processState = this.getTheProcess(pmodel).getPState();
						if (processState.equals(Process.ENACTING)) {
							this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G5.31"); //$NON-NLS-1$
							this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
							this.enactmentEngine.determineProcessModelStates(pmodel);
						}
					}
				} else {
					throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcThisActvAlreadyConn")); //$NON-NLS-1$
				}
			}
		} else if (branch.isFired().booleanValue() && !this.controlFlow(multipleCon_to, branch)) {
			if (branch instanceof BranchAND) { // Rule G5.25
				BranchAND bAND = (BranchAND) branch;
				Collection multis = bAND.getToMultipleCon();
				if (!multis.contains(multipleCon_to)) {
					if (multipleCon_to instanceof Join) {
						Join toJoin = (Join) multipleCon_to;
						toJoin.getFromMultipleCon().add(bAND);
						bAND.getToMultipleCon().add(toJoin);
					} else { // is Branch
						Branch toBranch = (Branch) multipleCon_to;
						toBranch.setFromMultipleConnection(bAND);
						bAND.getToMultipleCon().add(toBranch);
					}

					// Dynamic Changes related code
					ProcessModel pmodel = branch.getTheProcessModel();
					this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G5.25"); //$NON-NLS-1$
					this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
					this.enactmentEngine.determineProcessModelStates(pmodel);
				} else {
					throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcThisMultConnIsAlreConn")); //$NON-NLS-1$
				}
			} else { // Rule G5.32
				BranchCond bCond = (BranchCond) branch;
				Collection multisTBC = bCond.getTheBranchCondToMultipleCon();
				Iterator iter = multisTBC.iterator();
				boolean has = false;
				while (iter.hasNext()) {
					BranchCondToMultipleCon bCTMC = (BranchCondToMultipleCon) iter.next();
					if (bCTMC.getTheMultipleCon().equals(multipleCon_to)) {
						has = true;
						break;
					}
				}
				if (!has) {
					if (multipleCon_to instanceof Join) {
						Join toJoin = (Join) multipleCon_to;

						BranchCondToMultipleCon branchCondToMultipleCon = new BranchCondToMultipleCon();
						// branchCondToMultipleCon.setCondition(condition);
						branchCondToMultipleCon.setTheBranchCond(bCond);
						branchCondToMultipleCon.setTheMultipleCon(toJoin);
						toJoin.getFromMultipleCon().add(bCond);
						bCond.getTheBranchCondToMultipleCon().add(branchCondToMultipleCon);
					} else { // is Branch
						Branch toBranch = (Branch) multipleCon_to;

						BranchCondToMultipleCon branchCondToMultipleCon = new BranchCondToMultipleCon();
						// branchCondToMultipleCon.setCondition(condition);
						branchCondToMultipleCon.setTheBranchCond(bCond);
						branchCondToMultipleCon.setTheMultipleCon(toBranch);
						toBranch.setFromMultipleConnection(bCond);
						bCond.getTheBranchCondToMultipleCon().add(branchCondToMultipleCon);
					}

					// Dynamic Changes related code
					ProcessModel pmodel = branch.getTheProcessModel();
					this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G5.32"); //$NON-NLS-1$
					this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
					this.enactmentEngine.determineProcessModelStates(pmodel);
				} else {
					throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcThisMultConnIsAlreConn")); //$NON-NLS-1$
				}
			}
		} else {
			throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcThereIsContrFlowAlr")); //$NON-NLS-1$
		}
		// Persistence Operations
		branchDAO.update(branch);
		multiDAO.update(multipleCon_to);
	}

	/*
	 * Definidos na se��o 3 public void removeMultiConSuccessorConnection(String
	 * con_id, String to_con_id){ } public void
	 * removeMultiConSuccessorActivity(String con_id, String to_con_id){ }
	 */

	/**
	 * Related to section 6
	 */

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.qrconsult.spm.services.impl.DynamicModelingExtraida#addRequiredRole
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void addRequiredRole(String act_id, String role_id) throws DAOException, ModelingException {

		// Checks for the parameters

		Object act;
		try {
			act = actDAO.retrieveBySecondaryKey(act_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActivi") + //$NON-NLS-1$
					act_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (act == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Normal actNorm = (Normal) act;

		Object rol;
		try {
			rol = roleDAO.retrieveBySecondaryKey(role_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccRole") + //$NON-NLS-1$
					role_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (rol == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcRole") + role_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Role role = (Role) rol;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		String state = actNorm.getTheEnactionDescription().getState();

		if (state.equals("") // Rule G6.1 //$NON-NLS-1$
				|| (!state.equals(Plain.CANCELED) // Rule G6.2
						&& !state.equals(Plain.FAILED) && !state.equals(Plain.FINISHED))) {

			ReqAgent reqAgent = new ReqAgent();

			role.getTheReqAgent().add(reqAgent);
			reqAgent.setTheRole(role);
			reqAgent.setTheNormal(actNorm);
			actNorm.getTheRequiredPeople().add(reqAgent);

			// Persistence Operations
			actDAO.update(actNorm);
			roleDAO.update(role);
		} else {
			throw new ModelingException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + actNorm.getIdent() + Messages.getString("facades.DynamicModeling.ModelingExcHasAlreadyFinis")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.qrconsult.spm.services.impl.DynamicModelingExtraida#addRequiredGroupType
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void addRequiredGroupType(String act_id, String g_type) throws DAOException, ModelingException {

		// Checks for the parameters

		Object act;
		try {
			act = actDAO.retrieveBySecondaryKey(act_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActivi") + //$NON-NLS-1$
					act_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (act == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Normal actNorm = (Normal) act;

		Object gType;
		try {
			gType = groupTypeDAO.retrieveBySecondaryKey(g_type);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccGroupType") + //$NON-NLS-1$
					g_type + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (gType == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcGroupType") + g_type + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		GroupType groupType = (GroupType) gType;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		String state = actNorm.getTheEnactionDescription().getState();

		if (state.equals("") // Rule G6.3 //$NON-NLS-1$
				|| (!state.equals(Plain.CANCELED) // Rule G6.4
						&& !state.equals(Plain.FAILED) && !state.equals(Plain.FINISHED))) {

			ReqGroup reqGroup = new ReqGroup();

			groupType.getTheReqGroup().add(reqGroup);
			reqGroup.setTheGroupType(groupType);
			reqGroup.setTheNormal(actNorm);
			actNorm.getTheRequiredPeople().add(reqGroup);

			// Persistence Operations
			actDAO.update(actNorm);
			groupTypeDAO.update(groupType);
		} else {
			throw new ModelingException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + actNorm.getIdent() + Messages.getString("facades.DynamicModeling.ModelingExcHasAlreadyFinis")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.qrconsult.spm.services.impl.DynamicModelingExtraida#removeRequiredRole
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void removeRequiredRole(String act_id, String role_id) throws DAOException {

		// Checks for the parameters

		Object act;
		try {
			act = actDAO.retrieveBySecondaryKey(act_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActivi") + //$NON-NLS-1$
					act_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (act == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Normal actNorm = (Normal) act;

		Object rol;
		try {
			rol = roleDAO.retrieveBySecondaryKey(role_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccRole") + //$NON-NLS-1$
					role_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (rol == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcRole") + role_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Role role = (Role) rol;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		String state = actNorm.getTheEnactionDescription().getState();
		Collection reqAgents = this.getRequiredAgents(actNorm);
		Iterator iter = reqAgents.iterator();

		if (state.equals("")) { // Rule G6.5 and G6.7 //$NON-NLS-1$
			while (iter.hasNext()) {
				ReqAgent reqAgent = (ReqAgent) iter.next();
				if (reqAgent.getTheRole().equals(role)) {
					actNorm.getTheRequiredPeople().remove(reqAgent);
					reqAgent.setTheNormal(null);
					if (reqAgent.getTheAgent() != null) {
						reqAgent.getTheAgent().getTheReqAgent().remove(reqAgent);
						reqAgent.setTheAgent(null);
					}

					reqAgent.setTheRole(null);
					role.getTheReqAgent().remove(reqAgent);

					// Persistence Operations
					actDAO.update(actNorm);
					roleDAO.update(role);

					reqAgentDAO.delete(reqAgent);

					break;
				}
			}
		} else {
			while (iter.hasNext()) { // Rule G6.6 and G6.8
				ReqAgent reqAgent = (ReqAgent) iter.next();
				if (reqAgent.getTheAgent() == null && reqAgent.getTheRole().equals(role)) {

					Collection reqPeople = actNorm.getTheRequiredPeople();
					reqPeople.remove(reqAgent);
					reqAgent.setTheNormal(null);

					reqAgent.setTheRole(null);
					role.getTheReqAgent().remove(reqAgent);

					reqPeople.remove(null);
					if (reqPeople.size() > 0) {
						boolean allTasksFinished = enactmentEngine.isActivityFinished(actNorm);
						if (allTasksFinished) {
							try {
								this.enactmentEngine.finishTask(actNorm);
							} catch (DAOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (WebapseeException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

					// Persistence Operations
					actDAO.update(actNorm);
					roleDAO.update(role);

					reqAgentDAO.delete(reqAgent);
					break;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.qrconsult.spm.services.impl.DynamicModelingExtraida#defineRequiredAgent
	 * (java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean defineRequiredAgent(String act_id, String role_id, String ag_id) throws DAOException, WebapseeException {
		System.out.println("Activity Id: " + act_id);
		System.out.println("Role Id: " + role_id);
		System.out.println("Agent Id: " + ag_id);
		// Checks for the parameters

		if (role_id == null || role_id.equals(""))
			return false;

		Object act;
		try {
			act = actDAO.retrieveBySecondaryKey(act_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActivi") + act_id
					+ Messages.getString("facades.DynamicModeling.DaoExcFailed") + e);
		}

		if (act == null)
			throw new DAOException(Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id
					+ Messages.getString("facades.DynamicModeling.DaoExcNotFound"));

		Normal actNorm = (Normal) act;

		Object ag;
		try {
			ag = agentDAO.retrieveBySecondaryKey(ag_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccAgent") + ag_id
					+ Messages.getString("facades.DynamicModeling.DaoExcFailed") + e);
		}

		if (ag == null)
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcAgent") + ag_id
					+ Messages.getString("facades.DynamicModeling.DaoExcNotFound"));

		Agent agent = (Agent) ag;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		String state = actNorm.getTheEnactionDescription().getState();
		Collection invAgents = this.getInvolvedAgents(actNorm);

		// Test for the agent is already allocated to the activity.
		if (invAgents.contains(agent)) {
			throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheagent") + agent.getIdent() //$NON-NLS-1$
					+ Messages.getString("facades.DynamicModeling.ModelingExcAlreadyAllocToActv") //$NON-NLS-1$
					+ actNorm.getName());
		}

		ProcessModel pmodel = actNorm.getTheProcessModel();
		Collection reqAgents = this.getRequiredAgents(actNorm);
		Iterator iter = reqAgents.iterator();

		if (state.equals("")) { // Rule G6.11
			while (iter.hasNext()) {
				ReqAgent reqAgent = (ReqAgent) iter.next();
				if (reqAgent.getTheAgent() == null) {

					// Check if the role is the one specified by parameter
					if (!reqAgent.getTheRole().getIdent().equals(role_id))
						continue;

					if (this.playsRole(agent, reqAgent.getTheRole())) {

						reqAgent.setTheAgent(agent);
						agent.getTheReqAgent().add(reqAgent);
						actNorm.getTheRequiredPeople().add(reqAgent);
						reqAgent.setTheNormal(actNorm);

						// Dynamic Changes related code
						String processState = this.getTheProcess(pmodel).getPState();
						if (processState.equals(Process.ENACTING)) {

							actNorm.getTheEnactionDescription().setStateWithMessage(Plain.WAITING);
							this.logging.registerGlobalActivityEvent(actNorm, "ToWaiting", "Rule G6.11"); //$NON-NLS-1$ //$NON-NLS-2$
							this.updateAgenda(agent, actNorm, Plain.WAITING, "Rule G6.11"); //$NON-NLS-1$

							Collection ids = new HashSet();
							Iterator iterAgentToSend = invAgents.iterator();
							while (iterAgentToSend.hasNext()) {
								Agent toSend = (Agent) iterAgentToSend.next();
								ids.add(toSend.getIdent());
							}
//							this.notifyAgents(actNorm, Messages.getString("facades.DynamicModeling.NotifyAgentActvAdded"), ids, agent.getOid(),
//									DynamicModelingImpl.ADD, agent.getClass(), agent.getIdent(), null); //$NON-NLS-1$
							this.notifyAgents(actNorm, "facades.DynamicModeling.NotifyAgentActvAdded", ids, agent.getOid(),
									DynamicModelingImpl.ADD, agent.getClass(), agent.getIdent(), null); //$NON-NLS-1$

							this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G6.11"); //$NON-NLS-1$
							this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
							this.enactmentEngine.determineProcessModelStates(pmodel);
						}

						// Persistence Operations
						actDAO.update(actNorm);
						agentDAO.update(agent);
						return true;
					}
				}
			}

		} else if ((state.equals(Plain.WAITING) || state.equals(Plain.READY))) {
			// Rule G6.12
			while (iter.hasNext()) {
				ReqAgent reqAgent = (ReqAgent) iter.next();

				// Check if the role is the one specified by parameter
				if (!reqAgent.getTheRole().getIdent().equals(role_id))
					continue;

				if (reqAgent.getTheAgent() == null && this.playsRole(agent, reqAgent.getTheRole())) {
					reqAgent.setTheAgent(agent);
					agent.getTheReqAgent().add(reqAgent);
					actNorm.getTheRequiredPeople().add(reqAgent);
					reqAgent.setTheNormal(actNorm);
					this.updateAgenda(agent, actNorm, state, "Rule G6.12"); //$NON-NLS-1$

					Collection ids = new HashSet();
					Iterator iterAgentToSend = invAgents.iterator();
					while (iterAgentToSend.hasNext()) {
						Agent toSend = (Agent) iterAgentToSend.next();
						ids.add(toSend.getIdent());
					}
					this.notifyAgents(actNorm, Messages.getString("facades.DynamicModeling.NotifyAgentActvAdded"), ids, agent.getOid(),
							DynamicModelingImpl.ADD, agent.getClass(), agent.getIdent(), null); //$NON-NLS-1$

					// Dynamic Changes related code
					this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G6.12"); //$NON-NLS-1$
					this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
					this.enactmentEngine.determineProcessModelStates(pmodel);

					// Persistence Operations
					actDAO.update(actNorm);
					agentDAO.update(agent);
					return true;
				}
			}
		} else if ((state.equals(Plain.ACTIVE) || state.equals(Plain.PAUSED))) {
			// Rule G6.13
			while (iter.hasNext()) {
				ReqAgent reqAgent = (ReqAgent) iter.next();

				// Check if the role is the one specified by parameter
				if (!reqAgent.getTheRole().getIdent().equals(role_id))
					continue;

				if (reqAgent.getTheAgent() == null && this.playsRole(agent, reqAgent.getTheRole())) {

					reqAgent.setTheAgent(agent);
					agent.getTheReqAgent().add(reqAgent);
					actNorm.getTheRequiredPeople().add(reqAgent);
					reqAgent.setTheNormal(actNorm);
					this.updateAgenda(agent, actNorm, Plain.READY, "Rule G6.13"); //$NON-NLS-1$

					Collection ids = new HashSet();
					Iterator iterAgentToSend = invAgents.iterator();
					while (iterAgentToSend.hasNext()) {
						Agent toSend = (Agent) iterAgentToSend.next();
						ids.add(toSend.getIdent());
					}
					this.notifyAgents(actNorm, Messages.getString("facades.DynamicModeling.NotifyAgentActvAdded"), ids, agent.getOid(),
							DynamicModelingImpl.ADD, agent.getClass(), agent.getIdent(), null); //$NON-NLS-1$

					// Dynamic Changes related code
					this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G6.13"); //$NON-NLS-1$
					this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
					this.enactmentEngine.determineProcessModelStates(pmodel);

					// Persistence Operations
					actDAO.update(actNorm);
					agentDAO.update(agent);
					return true;
				}
			}
		} else {
			throw new ModelingException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + actNorm.getIdent() + Messages.getString("facades.DynamicModeling.ModelingExcHasAlreadyFinis")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.qrconsult.spm.services.impl.DynamicModelingExtraida#removeRequiredAgent
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void removeRequiredAgent(String act_id, String ag_id) throws DAOException, ModelingException {

		// Checks for the parameters

		// NormalDAO actDAO = new NormalDAO();
		// actDAO.setSession(currentSession);
		Object act;
		try {
			act = normDAO.retrieveBySecondaryKey(act_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActivi") + //$NON-NLS-1$
					act_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (act == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Normal actNorm = (Normal) act;

		Object ag;
		try {
			ag = agentDAO.retrieveBySecondaryKey(ag_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccAgent") + //$NON-NLS-1$
					ag_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (ag == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcAgent") + ag_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Agent agent = (Agent) ag;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		String state = actNorm.getTheEnactionDescription().getState();
		Collection reqAgents = this.getRequiredAgents(actNorm);
		Collection involvedAgents = this.getInvolvedAgents(actNorm);
		reqAgents.remove(null);

		Iterator iter = reqAgents.iterator();

		if (state.equals("")) { //$NON-NLS-1$
			while (iter.hasNext()) {
				ReqAgent reqAgent = (ReqAgent) iter.next();
				if (reqAgent.getTheAgent() != null) {
					if (reqAgent.getTheAgent().equals(agent) && this.playsRole(agent, reqAgent.getTheRole())) {
						reqAgent.setTheAgent(null);
						agent.getTheReqAgent().remove(reqAgent);

						// Persistence Operations
						actDAO.update(actNorm);
						agentDAO.update(agent);
						break;
					}
				}
			}
		} else if (!(state.equals(Plain.FAILED) || state.equals(Plain.CANCELED) || state.equals(Plain.FINISHED))) {

			if (involvedAgents.size() == 1 && !state.equals(Plain.WAITING)) {
				throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcLastAgent")); //$NON-NLS-1$
			}
			while (iter.hasNext()) {
				ReqAgent reqAgent = (ReqAgent) iter.next();
				if (reqAgent.getTheAgent() != null) {
					if (reqAgent.getTheAgent().equals(agent) && this.playsRole(agent, reqAgent.getTheRole())) {
						reqAgent.setTheAgent(null);
						agent.getTheReqAgent().remove(reqAgent);
						Process process = this.getTheProcess(actNorm.getTheProcessModel());
						Collection agendas = agent.getTheTaskAgenda().getTheProcessAgenda();
						Iterator iterAgendas = agendas.iterator();
						while (iterAgendas.hasNext()) {
							ProcessAgenda agenda = (ProcessAgenda) iterAgendas.next();
							if (agenda.getTheProcess().equals(process)) {
								this.removeTask(agenda, actNorm);
								break;
							}
						}
						Collection ids = new HashSet();
						ids.add(agent.getIdent());
						Iterator iterInvAgents = involvedAgents.iterator();
						while (iterInvAgents.hasNext()) {
							Agent invagent = (Agent) iterInvAgents.next();
							ids.add(invagent.getIdent());
						}
						this.notifyAgents(actNorm, Messages.getString("facades.DynamicModeling.NotifyAgentActivityRemoved"), ids, agent.getOid(),
								DynamicModelingImpl.DEL, agent.getClass(), agent.getIdent(), null); //$NON-NLS-1$

						// Persistence Operations
						actDAO.update(actNorm);
						agentDAO.update(agent);
						break;
					}
				}
			}
		} else {
			throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcActv") + actNorm.getIdent() //$NON-NLS-1$
					+ Messages.getString("facades.DynamicModeling.ModelingExcHasAlreadyFinis")); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.qrconsult.spm.services.impl.DynamicModelingExtraida#defineRequiredGroup
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void defineRequiredGroup(String act_id, String group_id) throws DAOException, WebapseeException {

		// Checks for the parameters

		System.out.println(group_id);

		Object act;
		try {
			act = actDAO.retrieveBySecondaryKey(act_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActv") + //$NON-NLS-1$
					act_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (act == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Normal actNorm = (Normal) act;

		Object gr;
		try {
			gr = groupDAO.retrieveBySecondaryKey(group_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccGroup") + //$NON-NLS-1$
					group_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (gr == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcGroup") + group_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Group group = (Group) gr;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		String state = actNorm.getTheEnactionDescription().getState();

		Collection reqGroups = this.getRequiredGroups(actNorm);

		// Test for the group and its agents are already allocated to the
		// activity.
		Iterator iterExcp = reqGroups.iterator();
		while (iterExcp.hasNext()) {
			ReqGroup reqAux = (ReqGroup) iterExcp.next();
			if (reqAux.getTheGroup() != null) {
				if (reqAux.getTheGroup().equals(group)) {
					throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheGroup") + group.getIdent() //$NON-NLS-1$
							+ Messages.getString("facades.DynamicModeling.ModelingExcIsAlreAllocToActv") //$NON-NLS-1$
							+ actNorm.getName());
				}
			}
		}

		Collection agentsGroup = group.getTheAgent();
		Collection invAgents = this.getInvolvedAgents(actNorm);

		Iterator iterAgents = agentsGroup.iterator();
		while (iterAgents.hasNext()) {
			Agent agent = (Agent) iterAgents.next();
			if (agent != null) {

				Iterator iterInvAg = invAgents.iterator();
				while (iterInvAg.hasNext()) {
					Agent invAg = (Agent) iterInvAg.next();
					if (agent.equals(invAg)) {
						throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheagent") + agent.getIdent() //$NON-NLS-1$
								+ Messages.getString("facades.DynamicModeling.ModelingExcIsAlreAllocToActv") //$NON-NLS-1$
								+ actNorm.getName());

					}
				}
			}
		}

		Iterator iter = reqGroups.iterator();

		if (state.equals("")) { //$NON-NLS-1$
			// Rule G6.17
			while (iter.hasNext()) {

				ReqGroup reqGroup = (ReqGroup) iter.next();
				if (reqGroup.getTheGroup() == null) {
					if (this.isSubType(group.getTheGroupType(), reqGroup.getTheGroupType())) {
						reqGroup.setTheGroup(group);
						group.getTheReqGroup().add(reqGroup);
						actNorm.getTheRequiredPeople().add(reqGroup);
						reqGroup.setTheNormal(actNorm);

						// Dynamic Changes related code
						ProcessModel pmodel = actNorm.getTheProcessModel();
						String processState = this.getTheProcess(pmodel).getPState();
						if (processState.equals(Process.ENACTING)) {
							actNorm.getTheEnactionDescription().setStateWithMessage(Plain.WAITING);
							this.logging.registerGlobalActivityEvent(actNorm, "ToWaiting", "Rule G6.17"); //$NON-NLS-1$ //$NON-NLS-2$
							Collection agentsInGroup = reqGroup.getTheGroup().getTheAgent();
							Iterator iterAg = agentsInGroup.iterator();
							while (iterAg.hasNext()) {
								Agent ag = (Agent) iterAg.next();
								this.updateAgenda(ag, actNorm, Plain.WAITING, "Rule G6.17"); //$NON-NLS-1$
							}
							this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G6.17"); //$NON-NLS-1$
							this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
							this.enactmentEngine.determineProcessModelStates(pmodel);
						}

						// Persistence Operations
						actDAO.update(actNorm);
						groupDAO.update(group);
					}
				}
			}
		} else if ((state.equals(Plain.WAITING) || state.equals(Plain.READY))) {
			// Rule G6.18
			while (iter.hasNext()) {
				ReqGroup reqGroup = (ReqGroup) iter.next();
				if (reqGroup.getTheGroup() == null) {

					if (this.isSubType(group.getTheGroupType(), reqGroup.getTheGroupType())) {
						reqGroup.setTheGroup(group);
						group.getTheReqGroup().add(reqGroup);
						actNorm.getTheRequiredPeople().add(reqGroup);
						reqGroup.setTheNormal(actNorm);

						Collection agentsInGroup = reqGroup.getTheGroup().getTheAgent();
						Iterator iterAg = agentsInGroup.iterator();
						while (iterAg.hasNext()) {
							Agent ag = (Agent) iterAg.next();
							this.updateAgenda(ag, actNorm, state, "Rule G6.18"); //$NON-NLS-1$
						}

						// Dynamic Changes related code
						ProcessModel pmodel = actNorm.getTheProcessModel();
						this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G6.18"); //$NON-NLS-1$
						this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
						this.enactmentEngine.determineProcessModelStates(pmodel);

						// Persistence Operations
						actDAO.update(actNorm);
						groupDAO.update(group);
					}
				}
			}
		} else if ((state.equals(Plain.ACTIVE) || state.equals(Plain.PAUSED))) {
			// Rule G6.19
			while (iter.hasNext()) {
				ReqGroup reqGroup = (ReqGroup) iter.next();
				if (reqGroup.getTheGroup() == null) {

					if (this.isSubType(group.getTheGroupType(), reqGroup.getTheGroupType())) {
						reqGroup.setTheGroup(group);
						group.getTheReqGroup().add(reqGroup);
						actNorm.getTheRequiredPeople().add(reqGroup);
						reqGroup.setTheNormal(actNorm);
						Collection agentsInGroup = reqGroup.getTheGroup().getTheAgent();
						Iterator iterAg = agentsInGroup.iterator();
						while (iterAg.hasNext()) {
							Agent ag = (Agent) iterAg.next();
							this.updateAgenda(ag, actNorm, Plain.READY, "Rule G6.19"); //$NON-NLS-1$
						}

						// Dynamic Changes related code
						ProcessModel pmodel = actNorm.getTheProcessModel();
						this.enactmentEngine.searchForFiredConnections(pmodel.getOid(), "Rule G6.19"); //$NON-NLS-1$
						this.enactmentEngine.searchForReadyActivities(pmodel.getOid());
						this.enactmentEngine.determineProcessModelStates(pmodel);

						// Persistence Operations
						actDAO.update(actNorm);
						groupDAO.update(group);
					}
				}
			}
		} else {
			throw new ModelingException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + actNorm.getIdent() + Messages.getString("facades.DynamicModeling.ModelingExcHasAlreadyFinis")); //$NON-NLS-1$ //$NON-NLS-2$
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.qrconsult.spm.services.impl.DynamicModelingExtraida#removeRequiredGroup
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void removeRequiredGroup(String act_id, String group_id) throws DAOException, ModelingException {

		// Checks for the parameters
		System.out.println(act_id);
		Object act;
		try {
			act = normDAO.retrieveBySecondaryKey(act_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActv") + //$NON-NLS-1$
					act_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (act == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Normal actNorm = (Normal) act;

		Object gr;
		try {
			gr = groupDAO.retrieveBySecondaryKey(group_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccGroup") + //$NON-NLS-1$
					group_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (gr == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcGroup") + group_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Group group = (Group) gr;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		String state = actNorm.getTheEnactionDescription().getState();
		Collection reqAgents = this.getRequiredGroups(actNorm);
		Iterator iter = reqAgents.iterator();

		if (state.equals("")) { // Rules G6.32  //$NON-NLS-1$
			while (iter.hasNext()) {
				ReqGroup reqGroup = (ReqGroup) iter.next();
				if (reqGroup.getTheGroup() != null) {
					if (reqGroup.getTheGroup().equals(group)) {

						reqGroup.removeFromTheGroup();

						// Persistence Operations
						actDAO.update(actNorm);
						groupDAO.update(group);
						break;
					}
				}
			}
		} else if (!(state.equals(Plain.FAILED) || state.equals(Plain.CANCELED) || state.equals(Plain.FINISHED))) { // Rule
																													// G6.33
			while (iter.hasNext()) {
				ReqGroup reqGroup = (ReqGroup) iter.next();
				if (reqGroup.getTheGroup() != null) {
					if (reqGroup.getTheGroup().equals(group)) {

						reqGroup.removeFromTheGroup();

						Collection ids = new HashSet();

						Collection agentsInGroup = group.getTheAgent();
						Iterator iterAgents = agentsInGroup.iterator();
						while (iterAgents.hasNext()) {
							Agent agent = (Agent) iterAgents.next();
							Process process = this.getTheProcess(actNorm.getTheProcessModel());
							Collection agendas = agent.getTheTaskAgenda().getTheProcessAgenda();
							Iterator iterAgendas = agendas.iterator();
							while (iterAgendas.hasNext()) {
								ProcessAgenda agenda = (ProcessAgenda) iterAgendas.next();
								if (agenda.getTheProcess().equals(process)) {
									ids.add(agent.getIdent());
									this.removeTask(agenda, actNorm);
									break;
								}
							}
						}

						this.notifyAgents(actNorm, null, ids, group.getOid(),
								DynamicModelingImpl.DEL, group.getClass(), group.getIdent(), null); //$NON-NLS-1$

						// Persistence Operations
						actDAO.update(actNorm);
						groupDAO.update(group);
						break;
					}
				}
			}
		} else {
			throw new ModelingException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + actNorm.getIdent() + Messages.getString("facades.DynamicModeling.ModelingExcHasAlreadyFinis")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.qrconsult.spm.services.impl.DynamicModelingExtraida#
	 * removeRequiredGroupType(java.lang.String, java.lang.String)
	 */
	@Override
	public void removeRequiredGroupType(String act_id, String g_type) throws DAOException {

		// Checks for the parameters

		Object act;
		try {
			act = actDAO.retrieveBySecondaryKey(act_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActv") + act_id //$NON-NLS-1$
					+ Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (act == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Normal actNorm = (Normal) act;

		Object gt;
		try {
			gt = groupTypeDAO.retrieveBySecondaryKey(g_type);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccGroupType") + g_type //$NON-NLS-1$
					+ Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (gt == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcGroupType") + g_type + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		GroupType groupType = (GroupType) gt;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		String state = actNorm.getTheEnactionDescription().getState();
		Collection reqAgents = this.getRequiredGroups(actNorm);
		reqAgents.remove(null);
		Iterator iter = reqAgents.iterator();

		if (state.equals("")) { // Rule G6.24 and G6.26 //$NON-NLS-1$
			while (iter.hasNext()) {
				ReqGroup reqGroup = (ReqGroup) iter.next();
				if (reqGroup.getTheGroupType().equals(groupType)) {
					actNorm.removeFromTheRequiredPeople(reqGroup);

					if (reqGroup.getTheGroup() != null) {
						reqGroup.getTheGroup().removeFromTheReqGroup(reqGroup);
					}

					groupType.removeFromTheReqGroup(reqGroup);

					// Persistence Operations
					actDAO.update(actNorm);
					groupTypeDAO.update(groupType);

					reqGroupDAO.delete(reqGroup);
					break;
				}
			}
		} else {
			while (iter.hasNext()) { // Rule G6.25 and G6.27
				ReqGroup reqGroup = (ReqGroup) iter.next();
				if (reqGroup.getTheGroup() == null && reqGroup.getTheGroupType().equals(groupType)) {

					actNorm.removeFromTheRequiredPeople(reqGroup);
					groupType.removeFromTheReqGroup(reqGroup);

					// Persistence Operations
					actDAO.update(actNorm);
					groupTypeDAO.update(groupType);

					reqGroupDAO.delete(reqGroup);
					break;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.qrconsult.spm.services.impl.DynamicModelingExtraida#newAgent(java
	 * .lang.String, java.lang.String, java.lang.String)
	 */

	@Override
	public Integer newAgent(String agent_id, String role_id, String normal_id) throws WebapseeException {// retorna
																											// Oid
																											// ReqAgent

		this.addRequiredRole(normal_id, role_id);

		try {
			this.defineRequiredAgent(normal_id, role_id, agent_id);
		} catch (WebapseeException e) {
			this.removeRequiredRole(normal_id, role_id);
			throw e;
		}
		System.out.println("caiu na normal"+ normal_id);
		Normal normalAux = (Normal) normDAO.retrieveBySecondaryKey(normal_id);
		Collection reqPeople = normalAux.getTheRequiredPeople();
		Iterator iter = reqPeople.iterator();
		System.out.println("caiu na normal "+normalAux.getTheRequiredPeople());
		while (iter.hasNext()) {
			System.out.println("caiu no while");
			RequiredPeople reqP = (RequiredPeople) iter.next();
			if (reqP instanceof ReqAgent) {
				ReqAgent reqAgent = (ReqAgent) reqP;
				if (reqAgent.getTheAgent() != null) {
					if (reqAgent.getTheAgent().getIdent().equals(agent_id)) {
						return reqAgent.getOid();
					}
				}
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.qrconsult.spm.services.impl.DynamicModelingExtraida#newGroup(java
	 * .lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Integer newGroup(String group_id, String groupType_id, String normal_id) throws WebapseeException, ModelingException {// retorna
																																	// Oid
																																	// ReqGroup

		this.addRequiredGroupType(normal_id, groupType_id);
		try {
			this.defineRequiredGroup(normal_id, group_id);
		} catch (WebapseeException e) {
			this.removeRequiredGroupType(normal_id, groupType_id);
			throw e;
		}

		Normal normalAux = (Normal) normDAO.retrieveBySecondaryKey(normal_id);
		Collection reqPeople = normalAux.getTheRequiredPeople();
		Iterator iter = reqPeople.iterator();
		while (iter.hasNext()) {
			RequiredPeople reqP = (RequiredPeople) iter.next();
			if (reqP instanceof ReqGroup) {
				ReqGroup reqGroup = (ReqGroup) reqP;
				if (reqGroup.getTheGroup() != null) {
					if (reqGroup.getTheGroup().getIdent().equals(group_id)) {
						return reqGroup.getOid();
					}
				}
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.qrconsult.spm.services.impl.DynamicModelingExtraida#newResource(java
	 * .lang.String, java.lang.String, java.lang.String, float)
	 */
	@Override
	public Integer newResource(String resource_id, String resourceType_id, String normal_id, float amount_needed) throws WebapseeException,
			ModelingException {// retorna Oid RequiredResource
		System.out.println("Entrou no método newResource: id = " + resource_id +
				"; typeId = " + resourceType_id + "; normalId = " + normal_id +
				"; amount = " + amount_needed);
		this.addRequiredResourceType(normal_id, resourceType_id);
		try {
			this.defineRequiredResource(normal_id, resource_id, amount_needed);
		} catch (WebapseeException e) {
			this.removeRequiredResourceType(normal_id, resourceType_id);
			throw e;
		}

		Normal normalAux = (Normal) normDAO.retrieveBySecondaryKey(normal_id);
		Collection reqResource = normalAux.getTheRequiredResource();
		Iterator iter = reqResource.iterator();
		while (iter.hasNext()) {
			RequiredResource reqR = (RequiredResource) iter.next();
			if (reqR.getTheResource() != null) {
				if (reqR.getTheResource().getIdent().equals(resource_id)) {
					return reqR.getOid();
				}
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.qrconsult.spm.services.impl.DynamicModelingExtraida#
	 * removeCompleteRequiredAgent(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void removeCompleteRequiredAgent(String normal_id, String agent_id, String role_id) throws DAOException, ModelingException {

		this.removeRequiredAgent(normal_id, agent_id);
		this.removeRequiredRole(normal_id, role_id);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.qrconsult.spm.services.impl.DynamicModelingExtraida#
	 * removeCompleteRequiredGroup(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void removeCompleteRequiredGroup(String normal_id, String group_id, String groupType_id) throws DAOException, ModelingException {
		this.removeRequiredGroup(normal_id, group_id);
		this.removeRequiredGroupType(normal_id, groupType_id);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.qrconsult.spm.services.impl.DynamicModelingExtraida#
	 * removeCompleteRequiredResource(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void removeCompleteRequiredResource(String normal_id, String resource_id, String resourceType_id) throws DAOException, ModelingException {

		this.removeRequiredResource(normal_id, resource_id);
		this.removeRequiredResourceType(normal_id, resourceType_id);
	}

	/**
	 * Related to section 7
	 */

	/*
	 * (non-Javadoc)
	 *
	 * @see org.qrconsult.spm.services.impl.DynamicModelingExtraida#
	 * addRequiredResourceType(java.lang.String, java.lang.String)
	 */
	@Override
	public Integer addRequiredResourceType(String act_id, String resType_id) throws DAOException, ModelingException {// retorna
																														// Oid
																														// RequiredResource

		// Checks for the parameters

		Object act;
		try {
			act = normDAO.retrieveBySecondaryKey(act_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActv") + act_id //$NON-NLS-1$
					+ Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (act == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Normal actNorm = (Normal) act;

		Object resType;
		try {
			resType = resTypeDAO.retrieveBySecondaryKey(resType_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccResourceType") + resType_id //$NON-NLS-1$
					+ Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (resType == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcResourType") + resType_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		ResourceType resourceType = (ResourceType) resType;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		String state = actNorm.getTheEnactionDescription().getState();
		RequiredResource reqResource = null;

		if (state.equals("") // Rule 7.1 //$NON-NLS-1$
				|| !(state.equals(Plain.FAILED) // Rule 7.2
						|| state.equals(Plain.CANCELED) || state.equals(Plain.FINISHED))) {

			// if (!this.hasTheRequiredResourceType(actNorm, resourceType)) {
			reqResource = new RequiredResource();
			reqResource.setTheNormal(actNorm);
			actNorm.getTheRequiredResource().add(reqResource);
			reqResource.setTheResourceType(resourceType);
			resourceType.getTheRequiredResource().add(reqResource);

			// Persistence Operations
			actDAO.update(actNorm);
			resTypeDAO.update(resourceType);
			// }

			return reqResource.getOid();
		} else {
			throw new ModelingException(
					"Exception no dynamicModelingActivity " + actNorm.getIdent() + " A atividade já foi finalizada"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.qrconsult.spm.services.impl.DynamicModelingExtraida#
	 * changeRequiredResourceType(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Integer changeRequiredResourceType(String act_id, String oldResType_id, String newResType_id) throws DAOException, ModelingException {// retorna
																																					// Oid
																																					// RequiredResource

		// Checks for the parameters

		Object act;
		try {
			act = normDAO.retrieveBySecondaryKey(act_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActv") + act_id //$NON-NLS-1$
					+ Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (act == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Normal actNorm = (Normal) act;

		Object oldResType;
		try {
			oldResType = resTypeDAO.retrieveBySecondaryKey(oldResType_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccResourceType") + oldResType_id //$NON-NLS-1$
					+ Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (oldResType == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcResourType") + oldResType_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		ResourceType oldResourceType = (ResourceType) oldResType;

		Object newResType;
		try {
			newResType = resTypeDAO.retrieveBySecondaryKey(newResType_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccResourceType") + newResType_id //$NON-NLS-1$
					+ Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (newResType == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcResourType") + newResType_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		ResourceType newResourceType = (ResourceType) newResType;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		String state = actNorm.getTheEnactionDescription().getState();

		RequiredResource reqRes = null;

		if (state.equals("") // Rule 7.3 //$NON-NLS-1$
				|| !(state.equals(Plain.FAILED) // Rule 7.4
						|| state.equals(Plain.CANCELED) || state.equals(Plain.FINISHED))) {
			if (!this.hasTheRequiredResourceType(actNorm, newResourceType)) {
				if (this.hasTheRequiredResourceType(actNorm, oldResourceType)) {
					Collection reqRess = actNorm.getTheRequiredResource();
					Iterator iter = reqRess.iterator();
					while (iter.hasNext()) {
						reqRes = (RequiredResource) iter.next();
						if (reqRes.getTheResourceType().equals(oldResourceType)) {
							oldResourceType.getTheRequiredResource().remove(reqRes);
							reqRes.setTheResourceType(newResourceType);
							newResourceType.getTheRequiredResource().add(reqRes);

							// Persistence Operations
							actDAO.update(actNorm);
							resTypeDAO.update(oldResourceType);
							resTypeDAO.update(newResourceType);

							return reqRes.getOid();
						}
					}
					return reqRes.getOid();
				} else {
					throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheOldRequiResoType")); //$NON-NLS-1$
				}
			} else {
				throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheNewRequiResouType")); //$NON-NLS-1$
			}
		} else {
			throw new ModelingException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + actNorm.getIdent() + Messages.getString("facades.DynamicModeling.ModelingExcHasAlreadyFinis")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.qrconsult.spm.services.impl.DynamicModelingExtraida#
	 * removeRequiredResourceType(java.lang.String, java.lang.String)
	 */
	@Override
	public void removeRequiredResourceType(String act_id, String resType_id) throws DAOException, ModelingException {

		// Checks for the parameters

		Object act;
		try {
			act = normDAO.retrieveBySecondaryKey(act_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActv") + act_id //$NON-NLS-1$
					+ Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (act == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Normal actNorm = (Normal) act;

		Object resType;
		try {
			resType = resTypeDAO.retrieveBySecondaryKey(resType_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccResourceType") + resType_id //$NON-NLS-1$
					+ Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (resType == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcResourType") + resType_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		ResourceType resourceType = (ResourceType) resType;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		String state = actNorm.getTheEnactionDescription().getState();

		if (state.equals("")) { // Rules G7.5 and G7.7 //$NON-NLS-1$
			Collection reqRess = actNorm.getTheRequiredResource();
			RequiredResource reqRes = null;
			boolean find = false;
			Iterator iter = reqRess.iterator();
			while (iter.hasNext()) {
				reqRes = (RequiredResource) iter.next();
				if (reqRes.getTheResourceType().equals(resourceType)) {
					if (reqRes.getTheResource() == null) {
						find = true;
						break;
					}
				}
			}
			if (find) {
				actNorm.getTheRequiredResource().remove(reqRes);
				reqRes.setTheNormal(null);
				reqRes.setTheResourceType(null);
				resourceType.getTheRequiredResource().remove(reqRes);

				// Persistence Operations
				actDAO.update(actNorm);
				resTypeDAO.update(resourceType);

				reqResDAO.delete(reqRes);
			}

		} else if (!(state.equals(Plain.FAILED) || state.equals(Plain.FINISHED) || state.equals(Plain.CANCELED))) { // Rules
																													// G7.6
																													// and
																													// G7.8
			Collection reqRess = actNorm.getTheRequiredResource();
			RequiredResource reqRes = null;
			boolean find = false;
			Iterator iter = reqRess.iterator();
			while (iter.hasNext()) {
				reqRes = (RequiredResource) iter.next();
				if (reqRes.getTheResourceType().equals(resourceType) && reqRes.getTheResource() == null) {
					find = true;
					break;
				}
			}
			if (find) {
				actNorm.getTheRequiredResource().remove(reqRes);
				reqRes.setTheNormal(null);
				reqRes.setTheResourceType(null);
				resourceType.getTheRequiredResource().remove(reqRes);

				// Persistence Operations
				actDAO.update(actNorm);
				resTypeDAO.update(resourceType);

				reqResDAO.delete(reqRes);
			}
		} else {
			throw new ModelingException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + actNorm.getIdent() + Messages.getString("facades.DynamicModeling.ModelingExcHasAlreadyFinis")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.qrconsult.spm.services.impl.DynamicModelingExtraida#
	 * defineRequiredResource(java.lang.String, java.lang.String, float)
	 */
	@Override
	public Integer defineRequiredResource(String act_id, String res_id, float amount_needed) throws DAOException, ModelingException,
			WebapseeException {// retorna Oid RequiredResource

		// Checks for the parameters

		Object act;
		try {
			act = normDAO.retrieveBySecondaryKey(act_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException("Problema na activity" + act_id //$NON-NLS-1$
					+ "Execução do DAO falhou" + e); //$NON-NLS-1$
		}

		if (act == null)
			throw new DAOException(
					"Problema na activity ( == null) " + act_id + " Entidade não encontrada"); //$NON-NLS-1$ //$NON-NLS-2$

		Normal actNorm = (Normal) act;

		Object res;
		try {
			res = resDAO.retrieveBySecondaryKey(res_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException("Problema ao recuperar Resource " + res_id //$NON-NLS-1$
					+ " retrieveBySecondaryKey failed " + e); //$NON-NLS-1$
		}

		if (res == null)
			throw new DAOException(
					"Problema ao recuperar Resource " + res_id + " Resource null"); //$NON-NLS-1$ //$NON-NLS-2$

		Resource resource = (Resource) res;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		String state = actNorm.getTheEnactionDescription().getState();
		Collection reqRess = actNorm.getTheRequiredResource();

		// Test for the agent is already allocated to the activity.
		Iterator iterExcp = reqRess.iterator();
		while (iterExcp.hasNext()) {
			RequiredResource reqAux = (RequiredResource) iterExcp.next();
			if (reqAux.getTheResource() != null) {
				if (reqAux.getTheResource().equals(resource)) {
					throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheResource") + resource.getIdent() //$NON-NLS-1$
							+ Messages.getString("facades.DynamicModeling.ModelingExcIsAlreAllocToActv") //$NON-NLS-1$
							+ actNorm.getIdent());
				}
			}
		}

		Iterator iter = reqRess.iterator();

		boolean ok = false;
		RequiredResource reqRes = null;
		if (!this.hasTheResource(actNorm, resource)) {
			if (state.equals("")) { //$NON-NLS-1$
				while (iter.hasNext()) {
					reqRes = (RequiredResource) iter.next();
					if (resource.getTheResourceType() != null) {
						if (this.isSubType(resource.getTheResourceType(), reqRes.getTheResourceType())) {
							if (reqRes.getTheResource() == null) { // Rule G7.11
								reqRes.setAmountNeeded(new Float(amount_needed));
								reqRes.setTheResource(resource);
								resource.getTheRequiredResource().add(reqRes);

								ok = true;
								// Persistence Operations
								actDAO.update(actNorm);
								resDAO.update(resource);
								return reqRes.getOid();
							}
						}
					}
				}
				if (!ok) {
					throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheRequiTypeDoesNot")); //$NON-NLS-1$
				}
				return reqRes.getOid();
			} else if (state.equals(Plain.WAITING) || state.equals(Plain.READY) || state.equals(Plain.PAUSED)) {

				while (iter.hasNext()) {
					reqRes = (RequiredResource) iter.next();
					if (resource.getTheResourceType() != null) {
						if (this.isSubType(resource.getTheResourceType(), reqRes.getTheResourceType()) || reqRes.getTheResource() == null) { // Rule
																																				// G7.12
							reqRes.setAmountNeeded(new Float(amount_needed));
							reqRes.setTheResource(resource);
							resource.getTheRequiredResource().add(reqRes);

							ok = true;

							Collection ids = new HashSet();
							Collection involvedAgents = this.getInvolvedAgents(actNorm);
							Iterator iterInvAgents = involvedAgents.iterator();
							while (iterInvAgents.hasNext()) {
								Agent invagent = (Agent) iterInvAgents.next();
								ids.add(invagent.getIdent());
							}

							this.notifyAgents(actNorm,
									"", ids, resource.getOid(), DynamicModelingImpl.ADD, resource.getClass(), resource.getIdent(), null); //$NON-NLS-1$
							// Persistence Operations
							actDAO.update(actNorm);
							resDAO.update(resource);

							return reqRes.getOid();
						}
					}
				}
				if (!ok) {
					throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheRequiTypeDoesNot")); //$NON-NLS-1$
				}
				return reqRes.getOid();
			} else if (state.equals(Plain.ACTIVE)) {
				while (iter.hasNext()) {
					reqRes = (RequiredResource) iter.next();
					if (resource.getTheResourceType() != null) {
						if (this.isSubType(resource.getTheResourceType(), reqRes.getTheResourceType()) || reqRes.getTheResource() == null) { // Rule
																																				// G7.13
							reqRes.setAmountNeeded(new Float(amount_needed));
							reqRes.setTheResource(resource);
							resource.getTheRequiredResource().add(reqRes);
							this.allocateResource(reqRes, resource, false);

							ok = true;
							Collection ids = new HashSet();
							Collection involvedAgents = this.getInvolvedAgents(actNorm);
							Iterator iterInvAgents = involvedAgents.iterator();
							while (iterInvAgents.hasNext()) {
								Agent invagent = (Agent) iterInvAgents.next();
								ids.add(invagent.getIdent());
							}

							this.notifyAgents(actNorm,
									"", ids, resource.getOid(), DynamicModelingImpl.ADD, resource.getClass(), resource.getIdent(), null); //$NON-NLS-1$

							// Persistence Operations
							actDAO.update(actNorm);
							resDAO.update(resource);

							return reqRes.getOid();
						} else {
							throw new ModelingException(
									Messages.getString("facades.DynamicModeling.ModelingExcTheResource") + resource.getIdent() + Messages.getString("facades.DynamicModeling.ModelingExcHasNoResoTypeDef")); //$NON-NLS-1$ //$NON-NLS-2$
						}
					}
				}
				if (!ok) {
					throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcTheRequiTypeDoesNot")); //$NON-NLS-1$
				}
				return reqRes.getOid();
			} else {
				throw new ModelingException(
						Messages.getString("facades.DynamicModeling.ModelingExcActv") + actNorm.getIdent() + Messages.getString("facades.DynamicModeling.ModelingExcHasAlreadyFinis")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else {
			throw new ModelingException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + actNorm.getIdent() + Messages.getString("facades.DynamicModeling.ModelingExcAlreHasTheResou")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.qrconsult.spm.services.impl.DynamicModelingExtraida#
	 * removeRequiredResource(java.lang.String, java.lang.String)
	 */
	@Override
	public void removeRequiredResource(String act_id, String res_id) throws DAOException, ModelingException {

		// Checks for the parameters

		Object act;
		try {
			act = normDAO.retrieveBySecondaryKey(act_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActv") + act_id //$NON-NLS-1$
					+ Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (act == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Normal actNorm = (Normal) act;

		Object res;
		try {
			res = resDAO.retrieveBySecondaryKey(res_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccResource") + res_id //$NON-NLS-1$
					+ Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (res == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DynamicModeling.DaoExcResource") + res_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Resource resource = (Resource) res;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		String state = actNorm.getTheEnactionDescription().getState();
		Collection reqRess = actNorm.getTheRequiredResource();
		Iterator iter = reqRess.iterator();

		if (this.hasTheResource(actNorm, resource)) {
			if (state.equals("")) { //$NON-NLS-1$
				while (iter.hasNext()) {
					RequiredResource reqRes = (RequiredResource) iter.next();
					if (reqRes.getTheResource() != null) {
						if (reqRes.getTheResource().equals(resource)) {
							reqRes.setTheResource(null);
							resource.getTheRequiredResource().remove(reqRes);
							reqRes.setAmountNeeded(new Float(0));

							// Persistence Operations
							actDAO.update(actNorm);
							resDAO.update(resource);
							break;
						}
					}
				}
			} else if (state.equals(Plain.WAITING) || state.equals(Plain.READY) || state.equals(Plain.PAUSED)) {
				while (iter.hasNext()) {
					RequiredResource reqRes = (RequiredResource) iter.next();
					if (reqRes.getTheResource() != null) {
						if (reqRes.getTheResource().equals(resource)) {
							reqRes.setTheResource(null);
							resource.getTheRequiredResource().remove(reqRes);
							reqRes.setAmountNeeded(new Float(0));

							Collection ids = new HashSet();
							Collection involvedAgents = this.getInvolvedAgents(actNorm);
							Iterator iterInvAgents = involvedAgents.iterator();
							while (iterInvAgents.hasNext()) {
								Agent invagent = (Agent) iterInvAgents.next();
								ids.add(invagent.getIdent());
							}

							this.notifyAgents(actNorm,
									"", ids, resource.getOid(), DynamicModelingImpl.ADD, resource.getClass(), resource.getIdent(), null); //$NON-NLS-1$

							// Persistence Operations
							actDAO.update(actNorm);
							resDAO.update(resource);
							break;
						}
					}
				}
			} else if (state.equals(Plain.ACTIVE)) {
				while (iter.hasNext()) {
					RequiredResource reqRes = (RequiredResource) iter.next();
					if (reqRes.getTheResource() != null) {
						if (reqRes.getTheResource().equals(resource)) {
							reqRes.setTheResource(null);
							resource.getTheRequiredResource().remove(reqRes);
							reqRes.setAmountNeeded(new Float(0));
							this.releaseResourceFromActivity(actNorm, resource);

							Collection ids = new HashSet();
							Collection involvedAgents = this.getInvolvedAgents(actNorm);
							Iterator iterInvAgents = involvedAgents.iterator();
							while (iterInvAgents.hasNext()) {
								Agent invagent = (Agent) iterInvAgents.next();
								ids.add(invagent.getIdent());
							}

							this.notifyAgents(
									actNorm,
									Messages.getString("facades.DynamicModeling.DynamicModeling.DaoExcResource") + resource.getIdent()
											+ Messages.getString("facades.DynamicModeling.NotifyAgentRemoved"), ids, resource.getOid(),
									DynamicModelingImpl.DEL, resource.getClass(), resource.getIdent(), null); //$NON-NLS-1$ //$NON-NLS-2$

							// Persistence Operations
							actDAO.update(actNorm);
							resDAO.update(resource);

							break;
						}
					}
				}
			} else {
				throw new ModelingException(
						Messages.getString("facades.DynamicModeling.ModelingExcActv") + actNorm.getIdent() + Messages.getString("facades.DynamicModeling.ModelingExcHasAlreadyFinis")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else {
			throw new ModelingException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + actNorm.getIdent() + Messages.getString("facades.DynamicModeling.ModelingExcDoesntHasResource")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.qrconsult.spm.services.impl.DynamicModelingExtraida#
	 * changeRequiredResourceAmount(java.lang.String, java.lang.String, float)
	 */
	@Override
	public void changeRequiredResourceAmount(String act_id, String res_id, float new_amount_needed) throws DAOException, ModelingException {

		// Checks for the parameters

		Object act;
		try {
			act = normDAO.retrieveBySecondaryKey(act_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActv") + act_id //$NON-NLS-1$
					+ Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (act == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Normal actNorm = (Normal) act;

		Object res;
		try {
			res = consumableDAO.retrieveBySecondaryKey(res_id);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccConsuResource") + res_id //$NON-NLS-1$
					+ Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (res == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcConsumableResource") + res_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Consumable consumable = (Consumable) res;

		// End Checks for the parameters

		String state = actNorm.getTheEnactionDescription().getState();

		Collection reqResources = actNorm.getTheRequiredResource();
		Iterator iterRes = reqResources.iterator();
		while (iterRes.hasNext()) {
			RequiredResource reqRes = (RequiredResource) iterRes.next();
			if (reqRes != null) {
				Resource resource = reqRes.getTheResource();
				if (resource != null) {
					if (resource instanceof Consumable) {
						Consumable cons = (Consumable) resource;
						if (state.equals("") //$NON-NLS-1$
								|| state.equals(Plain.WAITING) || state.equals(Plain.READY)) {

							reqRes.setAmountNeeded(new Float(new_amount_needed));
							break;
						} else if (state.equals(Plain.ACTIVE) || state.equals(Plain.PAUSED)) {

							float needed = reqRes.getAmountNeeded().floatValue();
							float total = cons.getTotalQuantity().floatValue();
							float used = cons.getAmountUsed().floatValue();
							float balance = total - used;
							if (new_amount_needed > needed) {
								if (new_amount_needed <= balance) {
									cons.setAmountUsed(new Float(used + new_amount_needed - needed));
									reqRes.setAmountNeeded(new Float(new_amount_needed));
								}
							} else {
								reqRes.setAmountNeeded(new Float(new_amount_needed));
							}
						} else {
							throw new ModelingException(Messages.getString("facades.DynamicModeling.ModelingExcItIsNotAllowedChange")); //$NON-NLS-1$
						}
					}
				}
			}
		}

		// Peristence Operations
		actDAO.update(actNorm);
	}

	/**
	 * Related to section 8
	 */

	/**
	 * Rule CF8.1, CF8.2, CF8.3 and CF8.4
	 */
	private boolean controlFlow(Activity act1, Activity act2) {

		boolean ret = false;

		if (this.areConnected(act1, act2)) { // Rule CF8.1 and CF8.2
			ret = true;
		} else {
			Collection connsTo = this.getConnectionsTo(act1);
			Iterator iter = connsTo.iterator();
			while (iter.hasNext()) {
				Connection connTo = (Connection) iter.next();
				Collection suc = this.getSuccessors(connTo);
				Iterator iterSuc = suc.iterator();
				while (iterSuc.hasNext()) {
					Object obj = (Object) iterSuc.next();
					if (obj instanceof Activity) {
						Activity act = (Activity) obj;
						if (this.controlFlow(act, act2)) {
							// Rule CF8.3
							ret = true;
							break;
						}
					} else if (obj instanceof MultipleCon) {
						MultipleCon multi = (MultipleCon) obj;
						if (this.controlFlow(multi, act2)) {
							// Rule CF8.4
							ret = true;
							break;
						}
					}
				}
			}
		}
		return ret;

	}

	/**
	 * Rule CF8.5, CF8.6 and CF8.7
	 */
	private boolean controlFlow(Activity act, MultipleCon multi) {

		boolean ret = false;
		if (this.areConnected(act, multi)) { // Rule CF8.5
			ret = true;
		} else {
			Collection connsTo = this.getConnectionsTo(act);
			Iterator iter = connsTo.iterator();
			while (iter.hasNext()) {
				Connection connTo = (Connection) iter.next();
				Collection suc = this.getSuccessors(connTo);
				Iterator iterSuc = suc.iterator();
				while (iterSuc.hasNext()) {
					Object obj = (Object) iterSuc.next();
					if (obj instanceof Activity) { // Rule CF8.6
						Activity actSuc = (Activity) obj;
						if (this.controlFlow(actSuc, multi)) {
							ret = true;
							break;
						}
					} else if (obj instanceof MultipleCon) { // Rule CF8.7
						MultipleCon multiSuc = (MultipleCon) obj;
						if (this.controlFlow(multiSuc, multi)) {
							ret = true;
							break;
						}
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Rule CF8.8, CF8.9 and CF8.10
	 */
	private boolean controlFlow(MultipleCon multi, Activity act) {

		boolean ret = false;
		if (this.areConnected(multi, act)) { // Rule CF8.8
			ret = true;
		} else {
			Collection suc = this.getSuccessors(multi);
			Iterator iter = suc.iterator();
			while (iter.hasNext()) {
				Object obj = (Object) iter.next();
				if (obj instanceof Activity) { // Rule CF8.9
					Activity actSuc = (Activity) obj;
					if (this.controlFlow(actSuc, act)) {
						ret = true;
						break;
					}
				} else if (obj instanceof MultipleCon) { // Rule CF8.10
					MultipleCon multiSuc = (MultipleCon) obj;
					if (this.controlFlow(multiSuc, act)) {
						ret = true;
						break;
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Rule CF8.11, CF8.12 and CF8.13
	 */
	private boolean controlFlow(MultipleCon multi1, MultipleCon multi2) {

		boolean ret = false;
		if (this.areConnected(multi1, multi2)) { // Rule CF8.11
			ret = true;
		} else {
			Collection suc = this.getSuccessors(multi1);
			Iterator iter = suc.iterator();
			while (iter.hasNext()) {
				Object obj = (Object) iter.next();
				if (obj instanceof Activity) { // Rule CF8.12
					Activity act = (Activity) obj;
					if (this.controlFlow(act, multi2)) {
						ret = true;
						break;
					}
				} else if (obj instanceof MultipleCon) { // Rule CF8.13
					MultipleCon multi = (MultipleCon) obj;
					if (this.controlFlow(multi, multi2)) {
						ret = true;
						break;
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Add and Remove agents from group.
	 */

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.qrconsult.spm.services.impl.DynamicModelingExtraida#addAgentToGroup
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void addAgentToGroup(String agentId, String groupId) throws WebapseeException {

		// Checks for the parameters

		Object ag;
		try {
			ag = agentDAO.retrieveBySecondaryKey(agentId);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccAgent") + //$NON-NLS-1$
					agentId + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (ag == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcAgent") + agentId + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Agent agent = (Agent) ag;

		Object gr;
		try {
			gr = groupDAO.retrieveBySecondaryKey(groupId);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccGroup") + //$NON-NLS-1$
					groupId + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (gr == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcGroup") + groupId + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Group group = (Group) gr;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		Collection processModels = new HashSet();
		Collection reqGroups = group.getTheReqGroup();
		Iterator iterReqGroups = reqGroups.iterator();
		while (iterReqGroups.hasNext()) {
			ReqGroup reqGroup = (ReqGroup) iterReqGroups.next();
			Normal actNorm = reqGroup.getTheNormal();

			if (actNorm != null) {
				Collection invAgents = this.getInvolvedAgents(actNorm);

				String state = actNorm.getTheEnactionDescription().getState();

				boolean contains = false;
				Iterator iter = invAgents.iterator();
				while (iter.hasNext()) {
					Agent auxAg = (Agent) iter.next();
					if (auxAg.equals(agent)) {
						contains = true;
						break;
					}
				}

				// Test for the agent is already allocated to the activity.
				if (!contains) {
					if (state.equals("")) { //$NON-NLS-1$

						// Dynamic Changes related code
						Process process = this.getTheProcess(actNorm.getTheProcessModel());
						String procState = process.getPState();
						if (procState.equals(Process.ENACTING)) {
							actNorm.getTheEnactionDescription().setStateWithMessage(Plain.WAITING);
							this.logging.registerGlobalActivityEvent(actNorm, "ToWaiting", "Rule Adding Agent to a Group"); //$NON-NLS-1$ //$NON-NLS-2$
							this.updateAgenda(agent, actNorm, Plain.WAITING, "Rule Adding Agent to a Group"); //$NON-NLS-1$

							Collection ids = new HashSet();
							ids.add(agent.getIdent());

							this.notifyAgents(actNorm, Messages.getString("facades.DynamicModeling.NotifyAgentActvAdded"), ids, agent.getOid(),
									DynamicModelingImpl.ADD, agent.getClass(), agent.getIdent(), null); //$NON-NLS-1$

							ProcessModel pmodel = actNorm.getTheProcessModel();
							processModels.add(pmodel);
						}
					} else if (!state.equals(Plain.FAILED) && !state.equals(Plain.FINISHED) && !state.equals(Plain.CANCELED)) {

						// Dynamic Changes related code
						this.updateAgenda(agent, actNorm, state, "Rule Adding Agent to a Group"); //$NON-NLS-1$

						Collection ids = new HashSet();
						ids.add(agent.getIdent());
						this.notifyAgents(actNorm, Messages.getString("facades.DynamicModeling.NotifyAgentActvAdded"), ids, agent.getOid(),
								DynamicModelingImpl.ADD, agent.getClass(), agent.getIdent(), null); //$NON-NLS-1$

						ProcessModel pmodel = actNorm.getTheProcessModel();
						processModels.add(pmodel);
					}
				}
			}
		}
		// Establishing the relationship
		group.insertIntoTheAgent(agent);

		Iterator iterPMs = processModels.iterator();
		while (iterPMs.hasNext()) {
			ProcessModel procModel = (ProcessModel) iterPMs.next();
			this.enactmentEngine.searchForFiredConnections(procModel.getOid(), "Rule Adding Agent to a Group"); //$NON-NLS-1$
			this.enactmentEngine.searchForReadyActivities(procModel.getOid());
			this.enactmentEngine.determineProcessModelStates(procModel);

		}

		// Persistence Operations
		groupDAO.update(group);
		agentDAO.update(agent);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.qrconsult.spm.services.impl.DynamicModelingExtraida#removeAgentFromGroup
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void removeAgentFromGroup(String agentId, String groupId) throws WebapseeException {

		// Checks for the parameters

		Object ag;
		try {
			ag = agentDAO.retrieveBySecondaryKey(agentId);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccAgent") + //$NON-NLS-1$
					agentId + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (ag == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcAgent") + agentId + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Agent agent = (Agent) ag;

		Object gr;
		try {
			gr = groupDAO.retrieveBySecondaryKey(groupId);
		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccGroup") + //$NON-NLS-1$
					groupId + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (gr == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.DaoExcGroup") + groupId + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Group group = (Group) gr;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		Collection reqGroups = group.getTheReqGroup();
		Iterator iterReqGroups = reqGroups.iterator();
		while (iterReqGroups.hasNext()) {
			ReqGroup reqGroup = (ReqGroup) iterReqGroups.next();
			Normal actNorm = reqGroup.getTheNormal();

			if (actNorm != null) {
				Collection invAgents = this.getInvolvedAgents(actNorm);

				String state = actNorm.getTheEnactionDescription().getState();
				// Test for the agent is already allocated to the activity.
				if (invAgents.contains(agent)) {
					if (state.equals("")) { //$NON-NLS-1$

						Collection ids = new HashSet();
						Process process = this.getTheProcess(actNorm.getTheProcessModel());
						String procState = process.getPState();
						if (procState.equals(Process.ENACTING)) {
							Collection agendas = agent.getTheTaskAgenda().getTheProcessAgenda();
							Iterator iterAgendas = agendas.iterator();
							while (iterAgendas.hasNext()) {
								ProcessAgenda agenda = (ProcessAgenda) iterAgendas.next();
								if (agenda.getTheProcess().equals(process)) {
									ids.add(agent.getIdent());
									this.removeTask(agenda, actNorm);
									break;
								}
							}
						}
					} else if (!state.equals(Plain.FAILED) && !state.equals(Plain.FINISHED) && !state.equals(Plain.CANCELED)) {

						Collection ids = new HashSet();
						Collection agendas = agent.getTheTaskAgenda().getTheProcessAgenda();
						Iterator iterAgendas = agendas.iterator();
						while (iterAgendas.hasNext()) {
							ProcessAgenda agenda = (ProcessAgenda) iterAgendas.next();
							Process process = this.getTheProcess(actNorm.getTheProcessModel());
							if (agenda.getTheProcess().equals(process)) {
								ids.add(agent.getIdent());
								this.removeTask(agenda, actNorm);
								break;
							}
						}

						this.notifyAgents(actNorm, Messages.getString("facades.DynamicModeling.NotifyAgentActivityRemoved"), ids, agent.getOid(),
								DynamicModelingImpl.DEL, agent.getClass(), agent.getIdent(), null); //$NON-NLS-1$
					}
				}
				// Establishing the relationship
				group.removeFromTheAgent(agent);
			}
		}
		// Establishing the relationship
		group.removeFromTheAgent(agent);

		// Persistence Operations
		groupDAO.update(group);
		agentDAO.update(agent);
	}

	/***************************************************************************
	 * |***Auxiliar Methods***|*
	 **************************************************************************/

	private Group cloneGroup(Group source, String newGroupId) {

		if (source == null)
			return null;

		source.setIsActive(false);

		Group destiny = new Group();
		destiny.setIdent(newGroupId);
		destiny.setName(newGroupId);
		destiny.setDescription(source.getDescription());
		destiny.setIsActive(true);

		// destiny.setSubGroups(null);
		// destiny.setSuperGroup(null);

		destiny.setTheAgent(null);
		destiny.setTheGroupType(source.getTheGroupType());
		// destiny.setTheReqGroup(theReqGroup);

		return destiny;
	}

	// Related to Agents

	/**
	 * Adds a Task that refers to a Normal Activity to an Agent.
	 */
	private void addTaskToAgent(Agent agent, Normal actNorm) {

		TaskAgenda taskAgenda = agent.getTheTaskAgenda();
		Collection procAgendas = taskAgenda.getTheProcessAgenda();
		procAgendas.remove(null);
		if (procAgendas.isEmpty()) {
			ProcessAgenda procAg = new ProcessAgenda();
			Process pro = this.getTheProcess(actNorm.getTheProcessModel());
			procAg.setTheProcess(pro);
			pro.getTheProcessAgenda().add(procAg);
			procAg.setTheTaskAgenda(taskAgenda);
			procAgendas.add(procAg);
			pAgendaDAO.addTask(procAg, actNorm);
		} else {
			Iterator iter = procAgendas.iterator();
			boolean has = false;
			while (iter.hasNext()) {
				ProcessAgenda procAgenda = (ProcessAgenda) iter.next();
				if (procAgenda.getTheProcess().equals(this.getTheProcess(actNorm.getTheProcessModel()))) {
					pAgendaDAO.addTask(procAgenda, actNorm);
					has = true;
				}
			}
			if (!has) {
				ProcessAgenda procAg = new ProcessAgenda();
				Process pro = this.getTheProcess(actNorm.getTheProcessModel());
				procAg.setTheProcess(pro);
				pro.getTheProcessAgenda().add(procAg);
				procAg.setTheTaskAgenda(taskAgenda);
				procAgendas.add(procAg);
				pAgendaDAO.addTask(procAg, actNorm);
			}
		}

		taskAgenda.setTheProcessAgenda(procAgendas);
	}

	/**
	 * Returns all the Agents that are allocated to an Normal Activity.
	 */
	private Collection getInvolvedAgents(Normal normal) {

		Collection involvedAgents = new HashSet();
		Collection reqPeople = normal.getTheRequiredPeople();
		Iterator iter = reqPeople.iterator();
		while (iter.hasNext()) {
			RequiredPeople reqP = (RequiredPeople) iter.next();
			if (reqP instanceof ReqAgent) {
				ReqAgent reqAgent = (ReqAgent) reqP;
				Agent agent = reqAgent.getTheAgent();
				if (agent != null)
					involvedAgents.add(agent);
			} else { // ReqGroup
				ReqGroup reqGroup = (ReqGroup) reqP;
				Group group = reqGroup.getTheGroup();
				if (group != null) {
					Collection agents = group.getTheAgent();
					agents.remove(null);
					if (!agents.isEmpty()) {
						involvedAgents.addAll(agents);
					}
				}
			}
		}
		return involvedAgents;
	}

	/**
	 * Returns all the RequiredAgents allocated to a Normal Activity.
	 */
	private Collection getRequiredAgents(Normal normal) {

		Collection requiredAgents = new HashSet();
		Collection reqPeople = normal.getTheRequiredPeople();
		Iterator iter = reqPeople.iterator();
		while (iter.hasNext()) {
			RequiredPeople reqP = (RequiredPeople) iter.next();
			if (reqP instanceof ReqAgent) {
				ReqAgent reqAgent = (ReqAgent) reqP;
				requiredAgents.add(reqAgent);
			}
		}
		return requiredAgents;
	}

	/**
	 * Returns all the RequiredGroups allocated to a Normal Activity.
	 */
	private Collection getRequiredGroups(Normal normal) {

		Collection requiredGroups = new HashSet();
		Collection reqPeople = normal.getTheRequiredPeople();
		Iterator iter = reqPeople.iterator();
		while (iter.hasNext()) {
			RequiredPeople reqP = (RequiredPeople) iter.next();
			if (reqP instanceof ReqGroup) {
				ReqGroup reqGroup = (ReqGroup) reqP;
				requiredGroups.add(reqGroup);
			}
		}
		return requiredGroups;
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
				if (!(reqAgent.getTheAgent() == null)) {
					has = true;
					break;
				}
			} else { // ReqGroup
				ReqGroup reqGroup = (ReqGroup) reqP;
				Collection agents = reqGroup.getTheGroup().getTheAgent();
				agents.remove(null);
				if (!agents.isEmpty()) {
					has = true;
					break;
				}
			}
		}
		return has;
	}

	private void notifyAgentAboutActivityState(Agent agent, Task task, String newState) {

		if (task == null) {
			return;
		}

		// Notify the agente of the new state of the task!!
		String message = "<MESSAGE>" + //$NON-NLS-1$
				"<ACTIVITYSTATE>" + //$NON-NLS-1$
				"<OID>" + task.getOid() + "</OID>" + //$NON-NLS-1$ //$NON-NLS-2$
				"<CLASS>" + task.getClass().getName() + "</CLASS>" + //$NON-NLS-1$ //$NON-NLS-2$
				"<ID>" + task.getTheNormal().getIdent() + "</ID>" + //$NON-NLS-1$ //$NON-NLS-2$
				"<NEWSTATE>" + newState + "</NEWSTATE>" + //$NON-NLS-1$ //$NON-NLS-2$
				"<BY>APSEE_Manager</BY>" + //$NON-NLS-1$
				"</ACTIVITYSTATE>" + //$NON-NLS-1$
				"</MESSAGE>"; //$NON-NLS-1$
		try {
			if (this.remote == null) {
			}
			if (this.remote != null) {
				this.remote.sendMessageToUser(message, agent.getIdent());
			}

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void notifyAgents(Normal actNorm, String msg, Integer oid, String messageType, Class classe, String identObj, String direction) {

		Collection ids = new HashSet();
		int i = 0;
		Collection agents = this.getInvolvedAgents(actNorm);
		Iterator iter = agents.iterator();
		while (iter.hasNext()) {
			Agent agent = (Agent) iter.next();
			ids.add(agent.getIdent());
			i++;
		}
		// Send Message To Users
		if (ids != null) {

			String dir = "";
			if (direction != null) {
				dir = "<DIRECTION>" + direction + "</DIRECTION>";

			}

			String actId = "";
			if (classe.equals(Artifact.class)) {
				actId = "<ACTIVITY>" + actNorm.getIdent() + "</ACTIVITY>";
			}

			String message = "<MESSAGE>" + //$NON-NLS-1$
					"<NOTIFY>" + //$NON-NLS-1$
					"<OID>" + oid + "</OID>" + //$NON-NLS-1$ //$NON-NLS-2$
					"<ID>" + identObj + "</ID>" + //$NON-NLS-1$ //$NON-NLS-2$
					"<TYPE>" + messageType + "</TYPE>" + //$NON-NLS-1$
					"<CLASS>" + classe.getName() + "</CLASS>" + //$NON-NLS-1$ //$NON-NLS-2$
					"<BY>Apsee:" + msg + "</BY>" + //$NON-NLS-1$ //$NON-NLS-2$
					dir + actId + "</NOTIFY>" + //$NON-NLS-1$
					"</MESSAGE>"; //$NON-NLS-1$
			try {
				if (this.remote == null) {
				}
				if (this.remote != null) {
					this.remote.sendMessageToGroup(message, ids);
				}

				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void notifyAgents(Normal actNorm, String msg, Collection ids, Integer oid, String messageType, Class classe, String identObj,
			String direction) {

		ids.remove(null);

		// Send Message To Users
		if (ids != null && !ids.isEmpty()) {

			String dir = "";
			if (direction != null) {
				dir = "<DIRECTION>" + direction + "</DIRECTION>";

			}

			String actId = "";
			if (classe.equals(Artifact.class) || classe.equals(Agent.class) || classe.equals(Group.class) || classe.equals(Consumable.class)
					|| classe.equals(Exclusive.class) || classe.equals(Shareable.class)) {
				actId = "<ACTIVITY>" + actNorm.getIdent() + "</ACTIVITY>";
			}

			String message = "<MESSAGE>" + //$NON-NLS-1$
					"<NOTIFY>" + //$NON-NLS-1$
					"<OID>" + oid + "</OID>" + //$NON-NLS-1$ //$NON-NLS-2$
					"<ID>" + identObj + "</ID>" + //$NON-NLS-1$ //$NON-NLS-2$
					"<TYPE>" + messageType + "</TYPE>" + //$NON-NLS-1$
					"<CLASS>" + classe.getName() + "</CLASS>" + //$NON-NLS-1$ //$NON-NLS-2$
					"<BY>Apsee:" + msg + "</BY>" + //$NON-NLS-1$ //$NON-NLS-2$
					dir + actId + "</NOTIFY>" + //$NON-NLS-1$
					"</MESSAGE>"; //$NON-NLS-1$
			try {
				if (this.remote == null) {
				}
				if (this.remote != null) {
					this.remote.sendMessageToGroup(message, ids);
				}

				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private boolean playsRole(Agent ag, Role role) {

		boolean ret = false;

		Collection agPlaysRoles = ag.getTheAgentPlaysRole();
		Iterator iter = agPlaysRoles.iterator();
		while (iter.hasNext()) {
			AgentPlaysRole agPR = (AgentPlaysRole) iter.next();
			if ((agPR.getTheRole() != null) && role != null) {
				if (agPR.getTheRole().equals(role)) {
					ret = true;
					break;
				}
			}
		}
		return ret;
	}

	/**
	 * Removes the Task that refers to a Normal Activity from a Process Agenda.
	 */
	private void removeTask(ProcessAgenda procAgenda, Normal normal/*
																	 * , Session
																	 * currentSession
																	 */) {
		Collection tasks = procAgenda.getTheTask();
		Iterator iter = tasks.iterator();
		while (iter.hasNext()) {
			Task task = (Task) iter.next();
			if (task == null)
				continue;

			if (task.getTheNormal().equals(normal)) {
				tasks.remove(task);
				task.setTheProcessAgenda(null);

				String actIdent = task.getTheNormal().getIdent();

				try {
					taskDAO.delete(task);
				} catch (Exception/* DAOException */e) {
					e.printStackTrace();
				}

				// Notify the agente of the task!!
				String message = "<MESSAGE>" + "<NOTIFY>" + "<OID>" + task.getOid() + "</OID>" + "<ID>" + actIdent + "</ID>" + "<TYPE>DEL</TYPE>"
						+ "<CLASS>" + task.getClass().getName() + "</CLASS>" + "<BY>APSEE_Manager</BY>" + "</NOTIFY>" + "</MESSAGE>";
				try {
					if (this.remote == null) {
					}
					if (this.remote != null) {
						this.remote.sendMessageToUser(message, procAgenda.getTheTaskAgenda().getTheAgent().getIdent());
					}

					System.out.println("DynamicModelingImpl.removeTask() remote reference exception");
				}
				break;
			}
		}
	}

	/**
	 * Updates the Agent's agenda (Process Agenda) after an Event (Activity's
	 * State Change).
	 */
	private void updateAgenda(Agent agent, Normal actNorm, String state, String why/*
																					 * ,
																					 * Session
																					 * currentSession
																					 */) {

		ProcessAgenda agenda = null;
		Collection procAgendas = agent.getTheTaskAgenda().getTheProcessAgenda();
		Iterator iter = procAgendas.iterator();
		while (iter.hasNext()) {
			ProcessAgenda procAgenda = (ProcessAgenda) iter.next();
			if (procAgenda.getTheProcess().equals(this.getTheProcess(actNorm.getTheProcessModel()))) {
				agenda = procAgenda;
				break;
			}
		}
		if (agenda == null) {
			agenda = new ProcessAgenda();
			agenda.setTheTaskAgenda(agent.getTheTaskAgenda());
			Process proc = this.getTheProcess(actNorm.getTheProcessModel());
			agenda.setTheProcess(proc);
			TaskAgenda taskAgenda = agent.getTheTaskAgenda();
			taskAgenda.getTheProcessAgenda().add(agenda);
			proc.getTheProcessAgenda().add(agenda);
		}
		Task task = pAgendaDAO.addTask(agenda, actNorm);
		task.setLocalState(state);
		this.notifyAgentAboutActivityState(agent, task, state);
		this.logging.registerAgendaEvent(task, "To" + state, why); //$NON-NLS-1$

	}

	// Related to Artifacts

	/**
	 * Verifies if an Activity already has an Input Artifact
	 */
	private boolean hasTheFromArtifact(Normal actNorm, Artifact artifact) {

		boolean has = false;
		Collection involvedFrom = actNorm.getInvolvedArtifactToNormal();
		Iterator iter = involvedFrom.iterator();
		while (iter.hasNext()) {
			InvolvedArtifacts invArt = (InvolvedArtifacts) iter.next();
			if (invArt.getTheArtifact() != null) {
				if (invArt.getTheArtifact().equals(artifact)) {
					has = true;
					break;
				}
			}
		}
		return has;
	}

	/**
	 * Verifies if an Activity already has an Output Artifact
	 */
	private boolean hasTheToArtifact(Normal actNorm, Artifact artifact) {
		boolean has = false;
		Collection involvedTo = actNorm.getInvolvedArtifactFromNormal();
		Iterator iter = involvedTo.iterator();
		while (iter.hasNext()) {
			InvolvedArtifacts invArt = (InvolvedArtifacts) iter.next();
			if (invArt.getTheArtifact() != null) {
				if (invArt.getTheArtifact().equals(artifact)) {
					has = true;
					break;
				}
			}
		}
		return has;
	}

	/**
	 *
	 * Attributing to normal activities their involved artifacts
	 */
	private void createInvolvedArtifacts(ArtifactCon artifactCon, Artifact artifact, Artifact oldArtifact, ArtifactType type) {

		if (oldArtifact == null) {
			// Attributing to normal activities their input artifacts
			Collection<Activity> toActivities = artifactCon.getToActivity();
			Iterator iterToActivities = toActivities.iterator();
			while (iterToActivities.hasNext()) {
				Activity toActivity = (Activity) iterToActivities.next();
				if (toActivity != null && toActivity instanceof Normal) {

					InvolvedArtifacts invArt = new InvolvedArtifacts();
					invArt.insertIntoTheArtifact(artifact);
					invArt.insertIntoTheArtifactType(type);
					invArt.insertIntoInInvolvedArtifacts((Normal) toActivity);
				}
			}

			// Attributing to normal activities their output artifacts
			Collection<Activity> fromActivities = artifactCon.getFromActivity();
			Iterator iterFromActivities = fromActivities.iterator();
			while (iterFromActivities.hasNext()) {
				Activity fromActivity = (Activity) iterFromActivities.next();
				if (fromActivity != null && fromActivity instanceof Normal) {

					InvolvedArtifacts invArt = new InvolvedArtifacts();
					invArt.insertIntoTheArtifact(artifact);
					invArt.insertIntoTheArtifactType(type);
					invArt.insertIntoOutInvolvedArtifacts((Normal) fromActivity);
				}
			}

		} else {//
			// Attributing to normal activities their input artifacts
			Collection<Activity> toActivities = artifactCon.getToActivity();
			Iterator iterToActivities = toActivities.iterator();
			while (iterToActivities.hasNext()) {
				Activity toActivity = (Activity) iterToActivities.next();
				if (toActivity != null && toActivity instanceof Normal) {
					Normal toNormal = (Normal) toActivity;
					Collection invArts = toNormal.getInvolvedArtifactToNormal();
					Iterator iterInvArts = invArts.iterator();
					while (iterInvArts.hasNext()) {
						InvolvedArtifacts invArt = (InvolvedArtifacts) iterInvArts.next();
						Artifact art = invArt.getTheArtifact();
						if (art != null && art.equals(oldArtifact)) {
							oldArtifact.removeFromTheInvolvedArtifacts(invArt);
							invArt.insertIntoTheArtifact(artifact);
							break;
						}
					}
				}
			}

			// Attributing to normal activities their output artifacts
			Collection<Activity> fromActivities = artifactCon.getFromActivity();
			Iterator iterFromActivities = fromActivities.iterator();
			while (iterFromActivities.hasNext()) {
				Activity fromActivity = (Activity) iterFromActivities.next();
				if (fromActivity != null && fromActivity instanceof Normal) {

					Normal fromNormal = (Normal) fromActivity;
					Collection invArts = fromNormal.getInvolvedArtifactFromNormal();
					Iterator iterInvArts = invArts.iterator();
					while (iterInvArts.hasNext()) {
						InvolvedArtifacts invArt = (InvolvedArtifacts) iterInvArts.next();
						Artifact art = invArt.getTheArtifact();
						if (art != null && art.equals(oldArtifact)) {
							oldArtifact.removeFromTheInvolvedArtifacts(invArt);
							invArt.insertIntoTheArtifact(artifact);
							break;
						}
					}
				}
			}
		}

	}

	// Related to Connections

	private boolean areConnected(Activity act1, Activity act2) {

		Collection toAct1 = this.getConnectionsTo(act1);
		boolean has = false;
		Iterator iter = toAct1.iterator();
		while (iter.hasNext()) {
			Connection con = (Connection) iter.next();
			if (con instanceof Sequence) {
				Sequence seq = (Sequence) con;
				if (seq.getToActivity().equals(act2)) {
					has = true;
					break;
				}
			} else if (con instanceof BranchAND) {
				BranchAND branchAND = (BranchAND) con;
				if (branchAND.getToActivity().contains(act2)) {
					has = true;
					break;
				}
			} else if (con instanceof BranchCond) {
				BranchCond branchCond = (BranchCond) con;
				Collection col = branchCond.getTheBranchCondToActivity();
				Iterator iter2 = col.iterator();
				while (iter2.hasNext()) {
					BranchCondToActivity atbc = (BranchCondToActivity) iter2.next();
					if (atbc.getTheActivity().equals(act2)) {
						has = true;
						break;
					}
				}
				if (has)
					break;
			} else if (con instanceof Join) {
				Join join = (Join) con;
				if (join.getToActivity() != null) {
					if (join.getToActivity().equals(act2)) {
						has = true;
						break;
					}
				}
			}
		}
		return has;
	}

	private boolean areConnected(Activity act, MultipleCon multi) {

		boolean are = false;

		Collection toAct = this.getConnectionsTo(act);
		Iterator iter = toAct.iterator();
		while (iter.hasNext()) {
			Connection con = (Connection) iter.next();
			if (con instanceof BranchAND) {
				BranchAND branchAND = (BranchAND) con;
				if (branchAND.equals(multi)) {
					are = true;
					break;
				}
			} else if (con instanceof BranchCond) {
				BranchCond branchCond = (BranchCond) con;
				if (branchCond.equals(multi)) {
					are = true;
					break;
				}
			} else if (con instanceof Join) {
				Join join = (Join) con;
				if (join.equals(multi)) {
					are = true;
					break;
				}
			}
		}
		return are;
	}

	private boolean areConnected(MultipleCon multi, Activity act) {

		Collection fromAct = this.getConnectionsFrom(act);
		boolean are = false;
		Iterator iter = fromAct.iterator();
		while (iter.hasNext()) {

			Object obj = iter.next();
			Connection con = (Connection) obj;

			if (con instanceof BranchAND) {
				BranchAND branchAND = (BranchAND) con;
				if (branchAND.equals(multi)) {
					are = true;
					break;
				}
			} else if (con instanceof BranchCond) {
				BranchCond branchCond = (BranchCond) con;
				if (branchCond.equals(multi)) {
					are = true;
					break;
				}
			} else if (con instanceof Join) {
				Join join = (Join) con;
				if (join.equals(multi)) {
					are = true;
					break;
				}
			}
		}
		return are;
	}

	private boolean areConnected(MultipleCon multi1, MultipleCon multi2) {

		Collection suc = this.getSuccessors(multi1);
		boolean are = false;
		Iterator iter = suc.iterator();
		while (iter.hasNext()) {
			Object obj = (Object) iter.next();
			if (obj instanceof MultipleCon) {
				MultipleCon multi = (MultipleCon) obj;
				if (multi instanceof BranchAND) {
					BranchAND branchAND = (BranchAND) multi;
					if (branchAND.equals(multi2)) {
						are = true;
						break;
					}
				} else if (multi instanceof BranchCond) {
					BranchCond branchCond = (BranchCond) multi;
					if (branchCond.equals(multi2)) {
						are = true;
						break;
					}
					if (are)
						break;
				} else if (multi instanceof Join) {
					Join join = (Join) multi;
					if (join.equals(multi2)) {
						are = true;
						break;
					}
				}
			}
		}
		return are;
	}

	/**
	 * Returns a Collection with the predecessors of an Activity.
	 */
	private Collection getConnectionsFrom(Activity act) {

		Collection connFrom = new ArrayList();
		connFrom.addAll(act.getFromSimpleCon());
		connFrom.addAll(act.getFromJoin());
		connFrom.addAll(act.getFromBranchAND());
		Collection bctas = act.getTheBranchCondToActivity();
		Iterator iterBctas = bctas.iterator();
		while (iterBctas.hasNext()) {
			BranchCondToActivity bcta = (BranchCondToActivity) iterBctas.next();
			if (bcta.getTheBranchCond() != null)
				connFrom.add(bcta.getTheBranchCond());
		}

		return connFrom;
	}

	/**
	 * Returns a Collection with the successors of an Activity.
	 */
	private Collection getConnectionsTo(Activity act) {

		Collection connTo = new ArrayList();
		connTo.addAll(act.getToSimpleCon());
		connTo.addAll(act.getToJoin());
		connTo.addAll(act.getToBranch());
		return connTo;
	}

	/**
	 * Returns a Collection with the successors (Activities and Multiple
	 * Connection) of a Connection.
	 */
	private Collection getSuccessors(Connection conn) {

		Collection succ = new ArrayList();
		if (conn instanceof Sequence) {
			Sequence seq = (Sequence) conn;
			Activity act = seq.getToActivity();
			if (act != null)
				succ.add(act);
		} else if (conn instanceof Branch) {
			Branch branch = (Branch) conn;

			if (branch instanceof BranchAND) {
				BranchAND bAND = (BranchAND) branch;
				succ.addAll(bAND.getToActivity());
				succ.addAll(bAND.getToMultipleCon());
			} else if (branch instanceof BranchCond) {
				BranchCond bCond = (BranchCond) branch;
				Collection bctmc = bCond.getTheBranchCondToMultipleCon();
				Collection atbc = bCond.getTheBranchCondToActivity();
				Iterator iterMulti = bctmc.iterator(), iterAct = atbc.iterator();
				while (iterMulti.hasNext()) {
					BranchCondToMultipleCon multi = (BranchCondToMultipleCon) iterMulti.next();
					MultipleCon multipleCon = multi.getTheMultipleCon();
					if (multipleCon != null)
						succ.add(multipleCon);
				}
				while (iterAct.hasNext()) {
					BranchCondToActivity act = (BranchCondToActivity) iterAct.next();
					Activity activity = act.getTheActivity();
					if (activity != null)
						succ.add(activity);
				}
			}
		} else if (conn instanceof Join) {
			Join join = (Join) conn;
			Activity activity = join.getToActivity();
			if (activity != null) {
				succ.add(activity);
			}
			MultipleCon multipleCon = join.getToMultipleCon();
			if (multipleCon != null) {
				succ.add(multipleCon);
			}
		}
		return succ;
	}

	/**
	 * Returns a Collection with the predecessors (Activities and Multiple
	 * Connection) of a Connection.
	 */
	private Collection getPredecessors(Connection conn) {

		Collection pred = new ArrayList();
		if (conn instanceof Sequence) {
			Sequence seq = (Sequence) conn;
			Activity act = seq.getFromActivity();
			if (act != null)
				pred.add(act);

		} else if (conn instanceof Branch) {
			Branch branch = (Branch) conn;
			Activity act = branch.getFromActivity();
			if (act != null)
				pred.add(act);
			MultipleCon multi = branch.getFromMultipleConnection();
			if (multi != null)
				pred.add(multi);
		} else if (conn instanceof Join) {
			Join join = (Join) conn;
			pred.addAll(join.getFromActivity());
			pred.addAll(join.getFromMultipleCon());
		}
		return pred;
	}

	private boolean isInPredecessors(Object obj, Connection conn) {

		boolean ret = false;

		Collection preds = this.getPredecessors(conn);
		Iterator iter = preds.iterator();
		while (iter.hasNext()) {
			Object object = (Object) iter.next();
			if (object instanceof Activity) {
				Activity activity = (Activity) object;
				if (activity.equals(obj)) {
					ret = true;
					break;
				}
			} else if (object instanceof MultipleCon) {
				MultipleCon multipleCon = (MultipleCon) object;
				if (multipleCon.equals(obj)) {
					ret = true;
					break;
				}
			}
		}
		return ret;
	}

	private void removeAllConnectionsFromActivity(Activity act/*
															 * , Session
															 * currentSession
															 */) {

		// Removing connections to...
		Collection branchesTo = act.getToBranch();
		Iterator iterBranchTo = branchesTo.iterator();
		while (iterBranchTo.hasNext()) {
			Branch branchTo = (Branch) iterBranchTo.next();
			if (branchTo instanceof BranchAND) {
				BranchAND branchAND = (BranchAND) branchTo;
				branchAND.setFromActivity(null);
			} else {
				BranchCond branchCond = (BranchCond) branchTo;
				branchCond.setFromActivity(null);
			}
		}

		Collection joinsTo = act.getToJoin();
		Iterator iterJoinTo = joinsTo.iterator();
		while (iterJoinTo.hasNext()) {
			Join joinTo = (Join) iterJoinTo.next();
			joinTo.getFromActivity().remove(act);
		}

		Collection connsToDelete = new HashSet();
		Collection simplesTo = act.getToSimpleCon();
		Iterator iterSimpleTo = simplesTo.iterator();
		while (iterSimpleTo.hasNext()) {
			SimpleCon simpleTo = (SimpleCon) iterSimpleTo.next();

			simpleTo.removeFromTheProcessModel();

			Activity actTo = simpleTo.getToActivity();
			actTo.removeFromFromSimpleCon(simpleTo);

			connsToDelete.add(simpleTo);
		}
		act.clearToSimpleCon();

		Iterator iterConnsToDelete = connsToDelete.iterator();
		while (iterConnsToDelete.hasNext()) {
			SimpleCon conn = (SimpleCon) iterConnsToDelete.next();
			// Deleting simple connection from the database

			try {
				simpleDAO.delete(conn);
			} catch (Exception/* DAOException */e) {
				e.printStackTrace();
			}
		}

		connsToDelete.clear();

		Collection artConsTo = act.getToArtifactCon();
		Iterator iterArtTo = artConsTo.iterator();
		while (iterArtTo.hasNext()) {
			ArtifactCon artConTo = (ArtifactCon) iterArtTo.next();
			artConTo.getFromActivity().remove(act);
		}

		// Removing connections from...
		Collection branchesANDFrom = act.getFromBranchAND();
		Iterator iterBranchANDFrom = branchesANDFrom.iterator();
		while (iterBranchANDFrom.hasNext()) {
			BranchAND branchANDFrom = (BranchAND) iterBranchANDFrom.next();
			branchANDFrom.getToActivity().remove(act);
		}

		Collection bctaToDelete = new HashSet();

		Collection bctas = act.getTheBranchCondToActivity();
		Iterator iterBCTAs = bctas.iterator();
		while (iterBCTAs.hasNext()) {
			BranchCondToActivity branchCondToActivity = (BranchCondToActivity) iterBCTAs.next();
			bctaToDelete.add(branchCondToActivity);
		}

		bctas.clear();

		Iterator iterBctasToDelete = bctaToDelete.iterator();
		while (iterBctasToDelete.hasNext()) {
			BranchCondToActivity toDelete = (BranchCondToActivity) iterBctasToDelete.next();
			toDelete.removeFromTheBranchCond();

			// Deleting simple connection from the database

			try {
				branchCondToActivityDAO.delete(toDelete);
			} catch (Exception/* DAOException */e) {
				e.printStackTrace();
			}
		}

		Collection joinsFrom = act.getFromJoin();
		Iterator iterJoinFrom = joinsFrom.iterator();
		while (iterJoinFrom.hasNext()) {
			Join joinFrom = (Join) iterJoinFrom.next();
			joinFrom.setToActivity(null);
		}

		Collection simplesFrom = act.getFromSimpleCon();
		Iterator iterSimpleFrom = simplesFrom.iterator();
		while (iterSimpleFrom.hasNext()) {
			SimpleCon simpleFrom = (SimpleCon) iterSimpleFrom.next();

			simpleFrom.removeFromTheProcessModel();

			Activity actFrom = simpleFrom.getFromActivity();
			actFrom.removeFromToSimpleCon(simpleFrom);

			connsToDelete.add(simpleFrom);
		}

		act.clearFromSimpleCon();

		iterConnsToDelete = connsToDelete.iterator();
		while (iterConnsToDelete.hasNext()) {
			SimpleCon conn = (SimpleCon) iterConnsToDelete.next();
			// Deleting simple connection from the database

			try {
				simpleDAO.delete(conn);
			} catch (Exception/* DAOException */e) {
				e.printStackTrace();
			}
		}

		connsToDelete.clear();

		Collection artConsFrom = act.getFromArtifactCon();
		Iterator iterArtFrom = artConsFrom.iterator();
		while (iterArtFrom.hasNext()) {
			ArtifactCon artConFrom = (ArtifactCon) iterArtFrom.next();

			artConFrom.getToActivity().remove(act);
		}
	}

	// Related to Process Models

	private void copyActivityRelationships(Activity actFrom, Activity actTo) {

		if (actFrom.getActivityEstimation() != null) {
			Collection temp = new HashSet();

			Collection actEstimations = actFrom.getActivityEstimation();
			Iterator iterActEstimations = actEstimations.iterator();
			while (iterActEstimations.hasNext()) {
				ActivityEstimation actEstimation = (ActivityEstimation) iterActEstimations.next();
				temp.add(actEstimation);
				// actFrom.getActivityEstimation().remove(actEstimation);
				actEstimation.setActivity(actTo);
				actTo.getActivityEstimation().add(actEstimation);
			}
			actFrom.getActivityEstimation().removeAll(temp);
		}
		if (actFrom.getActivityMetric() != null) {
			Collection temp = new HashSet();

			Collection actMetrics = actFrom.getActivityMetric();
			Iterator iterActMetrics = actMetrics.iterator();
			while (iterActMetrics.hasNext()) {
				ActivityMetric actMetric = (ActivityMetric) iterActMetrics.next();
				temp.add(actMetric);
				// actFrom.getActivityMetric().remove(actMetric);
				actMetric.setActivity(actTo);
				actTo.getActivityMetric().add(actMetric);
			}
			actFrom.getActivityMetric().removeAll(temp);
		}
		if (actFrom.getFromArtifactCon() != null) {
			Collection temp = new HashSet();

			Collection fromArtifactCons = actFrom.getFromArtifactCon();
			Iterator iterFromArtifactCons = fromArtifactCons.iterator();
			while (iterFromArtifactCons.hasNext()) {
				ArtifactCon fromArtifactCon = (ArtifactCon) iterFromArtifactCons.next();
				temp.add(fromArtifactCon);
				// actFrom.getFromArtifactCon().remove(fromArtifactCon);
				fromArtifactCon.getToActivity().remove(actFrom);
				fromArtifactCon.getToActivity().add(actTo);
				actTo.getFromArtifactCon().add(fromArtifactCon);
			}
			actFrom.getFromArtifactCon().removeAll(temp);
		}
		if (actFrom.getFromBranchAND() != null) {
			Collection temp = new HashSet();

			Collection fromBranchANDs = actFrom.getFromBranchAND();
			Iterator iterFromBranchANDs = fromBranchANDs.iterator();
			while (iterFromBranchANDs.hasNext()) {
				BranchAND fromBranchAND = (BranchAND) iterFromBranchANDs.next();
				temp.add(fromBranchAND);
				// actFrom.getFromBranchAND().remove(fromBranchAND);
				fromBranchAND.getToActivity().remove(actFrom);
				fromBranchAND.getToActivity().add(actTo);
				actTo.getFromBranchAND().add(fromBranchAND);
			}
			actFrom.getFromBranchAND().removeAll(temp);
		}
		if (actFrom.getTheBranchCondToActivity() != null) {
			Collection temp = new HashSet();

			Collection branchCondToActivitys = actFrom.getTheBranchCondToActivity();
			Iterator iterBranchCondToActivitys = branchCondToActivitys.iterator();
			while (iterBranchCondToActivitys.hasNext()) {
				BranchCondToActivity branchCondToActivity = (BranchCondToActivity) iterBranchCondToActivitys.next();
				temp.add(branchCondToActivity);
				// actFrom.getTheBranchCondToActivity().remove(branchCondToActivity);
				branchCondToActivity.setTheActivity(actTo);
				actTo.getTheBranchCondToActivity().add(branchCondToActivity);
			}
			actFrom.getTheBranchCondToActivity().removeAll(temp);
		}
		if (actFrom.getFromJoin() != null) {
			Collection temp = new HashSet();

			Collection fromJoins = actFrom.getFromJoin();
			Iterator iterFromJoins = fromJoins.iterator();
			while (iterFromJoins.hasNext()) {
				Join fromJoin = (Join) iterFromJoins.next();
				temp.add(fromJoin);
				// actFrom.getFromJoin().remove(fromJoin);
				fromJoin.setToActivity(actTo);
				actTo.getFromJoin().add(fromJoin);
			}
			actFrom.getFromJoin().removeAll(temp);
		}
		if (actFrom.getFromSimpleCon() != null) {
			Collection temp = new HashSet();

			Collection fromSimpleCons = actFrom.getFromSimpleCon();
			Iterator iterFromSimpleCons = fromSimpleCons.iterator();
			while (iterFromSimpleCons.hasNext()) {
				SimpleCon fromSimpleCon = (SimpleCon) iterFromSimpleCons.next();
				actTo.getFromSimpleCon().add(fromSimpleCon);
				temp.add(fromSimpleCon);
				// actFrom.getFromSimpleCon().remove(fromSimpleCon);
				fromSimpleCon.setToActivity(actTo);
			}
			actFrom.getFromSimpleCon().removeAll(temp);
		}
		if (actFrom.getHasVersions() != null) {
			Collection temp = new HashSet();

			Collection hasVersions = actFrom.getHasVersions();
			Iterator iterHasVersions = hasVersions.iterator();
			while (iterHasVersions.hasNext()) {
				Activity version = (Activity) iterHasVersions.next();
				temp.add(version);
				// actFrom.getHasVersions().remove(version);
				version.setIsVersion(actTo);
				actTo.getHasVersions().add(version);
			}
			actFrom.getHasVersions().removeAll(temp);
		}
		if (actFrom.getIsVersion() != null) {

			Activity isVersion = actFrom.getIsVersion();
			actFrom.setIsVersion(null);
			isVersion.getHasVersions().remove(actFrom);
			actTo.setIsVersion(isVersion);
			isVersion.getHasVersions().add(actTo);
		}
		if (actFrom.getTheActivityType() != null) {

			ActivityType activityType = actFrom.getTheActivityType();
			actFrom.setTheActivityType(null);
			activityType.getTheActivity().remove(actFrom);
			actTo.setTheActivityType(activityType);
			activityType.getTheActivity().add(actTo);
		}
		if (actFrom.getTheModelingActivityEvent() != null) {
			Collection temp = new HashSet();

			Collection modelingActivityEvents = actFrom.getTheModelingActivityEvent();
			Iterator iterModelingActivityEvents = modelingActivityEvents.iterator();
			while (iterModelingActivityEvents.hasNext()) {
				ModelingActivityEvent modelingActivityEvent = (ModelingActivityEvent) iterModelingActivityEvents.next();
				temp.add(modelingActivityEvent);
				// actFrom.getTheModelingActivityEvent().remove(modelingActivityEvent);
				modelingActivityEvent.setTheActivity(actTo);
				actTo.getTheModelingActivityEvent().add(modelingActivityEvent);
			}
			actFrom.getTheModelingActivityEvent().removeAll(temp);
		}
		if (actFrom.getToArtifactCon() != null) {
			Collection temp = new HashSet();

			Collection toArtifactCons = actFrom.getToArtifactCon();
			Iterator iterToArtifactCons = toArtifactCons.iterator();
			while (iterToArtifactCons.hasNext()) {
				ArtifactCon toArtifactCon = (ArtifactCon) iterToArtifactCons.next();
				temp.add(toArtifactCon);
				// actFrom.getToArtifactCon().remove(toArtifactCon);
				toArtifactCon.getFromActivity().remove(actFrom);
				toArtifactCon.getFromActivity().add(actTo);
				actTo.getToArtifactCon().add(toArtifactCon);
			}
			actFrom.getToArtifactCon().removeAll(temp);
		}
		if (actFrom.getToBranch() != null) {
			Collection temp = new HashSet();

			Collection toBranchs = actFrom.getToBranch();
			Iterator iterToBranchs = toBranchs.iterator();
			while (iterToBranchs.hasNext()) {
				Branch toBranch = (Branch) iterToBranchs.next();
				temp.add(toBranch);
				// actFrom.getToBranch().remove(toBranch);
				toBranch.setFromActivity(actTo);
				actTo.getToBranch().add(toBranch);
			}
			actFrom.getToBranch().removeAll(temp);
		}
		if (actFrom.getToJoin() != null) {
			Collection temp = new HashSet();

			Collection toJoins = actFrom.getToJoin();
			Iterator iterToJoins = toJoins.iterator();
			while (iterToJoins.hasNext()) {
				Join toJoin = (Join) iterToJoins.next();
				temp.add(toJoin);
				// actFrom.getToJoin().remove(toJoin);
				toJoin.getFromActivity().remove(actFrom);
				toJoin.getFromActivity().add(actTo);
				actTo.getToJoin().add(toJoin);
			}
			actFrom.getToJoin().removeAll(temp);
		}
		if (actFrom.getToSimpleCon() != null) {
			Collection temp = new HashSet();

			Collection toSimpleCons = actFrom.getToSimpleCon();
			Iterator iterToSimpleCons = toSimpleCons.iterator();
			while (iterToSimpleCons.hasNext()) {
				SimpleCon toSimpleCon = (SimpleCon) iterToSimpleCons.next();
				temp.add(toSimpleCon);
				// actFrom.getToSimpleCon().remove(toSimpleCon);
				toSimpleCon.setFromActivity(actTo);
				actTo.getToSimpleCon().add(toSimpleCon);
			}
			actFrom.getToSimpleCon().removeAll(temp);
		}
		ProcessModel fromProcessModel = actFrom.getTheProcessModel();
		actFrom.setTheProcessModel(null);
		fromProcessModel.getTheActivity().remove(actFrom);
		actTo.setTheProcessModel(fromProcessModel);
		fromProcessModel.getTheActivity().add(actTo);
	}

	/**
	 * Returns the path of the process model given a String.
	 */
	private String getPath(String string) {

		StringTokenizer st = new StringTokenizer(string, "."); //$NON-NLS-1$

		int i = st.countTokens();
		String currentModel = st.nextToken();
		i--;
		while (i > 1) {
			currentModel += "." + st.nextToken(); //$NON-NLS-1$
			i--;
		}

		return currentModel;
	}

	/**
	 * Returns the state of an Activity, even if it is Plain or Decomposed.
	 */
	private String getState(Activity activity) {

		String state;

		if (activity instanceof Plain) {
			Plain plain = (Plain) activity;
			state = plain.getTheEnactionDescription().getState();
		} else { // decomposed
			Decomposed decomposed = (Decomposed) activity;
			state = decomposed.getTheReferedProcessModel().getPmState();
		}

		return (state);
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

	private boolean isActivityInProcessModel(String act_id, ProcessModel pmodel) {

		boolean find = false;

		Collection acts = pmodel.getTheActivity();
		Iterator iter = acts.iterator();
		while (iter.hasNext()) {
			Object obj = iter.next();
			if (obj instanceof Activity) {
				Activity activity = (Activity) obj;
				if (activity.getIdent().equals(act_id)) {
					find = true;
					break;
				}
			}
		}
		return find;
	}

	private void makeWaiting(Activity activity, String why) {
		if (activity != null) {
			if (activity instanceof Normal) {
				Normal normal = (Normal) activity;

				EnactionDescription enact = normal.getTheEnactionDescription();
				enact.setState(Plain.WAITING);
				this.logging.registerModelingActivityEvent(normal, "To" + Plain.WAITING, why); //$NON-NLS-1$ //$NON-NLS-2$
				this.makeActivityWaiting(normal, why);

			} else if (activity instanceof Decomposed) {
				Decomposed decomposed = (Decomposed) activity;
				this.makeDecomposedWaiting(decomposed.getTheReferedProcessModel(), why);
			}
		}
	}

	private void makeDecomposedWaiting(ProcessModel processModel, String why) {
		Collection activities = processModel.getTheActivity();
		Iterator iterActivities = activities.iterator();
		while (iterActivities.hasNext()) {
			Activity activity = (Activity) iterActivities.next();
			if (activity != null) {
				if (activity instanceof Normal) {
					Normal normal = (Normal) activity;
					String state = normal.getTheEnactionDescription().getState();
					if (state.equals(Plain.READY)) {
						this.makeActivityWaiting(normal, why);
					}
				} else if (activity instanceof Decomposed) {
					Decomposed decomposed = (Decomposed) activity;
					this.makeDecomposedWaiting(decomposed.getTheReferedProcessModel(), why);
				}
			}
		}
	}

	private void makeActivityWaiting(Normal actNorm, String why) {

		Collection reqPeople = this.getInvolvedAgents(actNorm);
		Iterator iter = reqPeople.iterator();
		boolean has = iter.hasNext();
		while (iter.hasNext()) {
			Agent agent = (Agent) iter.next();
			// this.addTaskToAgent (agent, actNorm, currentSession);
			TaskAgenda agenda = agent.getTheTaskAgenda();
			Collection procAgendas = agenda.getTheProcessAgenda();
			Iterator iter2 = procAgendas.iterator();
			boolean find = false;
			while (iter2.hasNext()) {
				ProcessAgenda procAgenda = (ProcessAgenda) iter2.next();
				if (procAgenda.getTheProcess().equals(this.getTheProcess(actNorm.getTheProcessModel()))) {
					Collection tasks = procAgenda.getTheTask();
					Iterator iter3 = tasks.iterator();
					while (iter3.hasNext()) {
						Task task = (Task) iter3.next();
						if (task.getTheNormal().equals(actNorm)) {
							task.setLocalState(Plain.WAITING); //$NON-NLS-1$
							this.logging.registerAgendaEvent(task, "To" + Plain.WAITING, why); //$NON-NLS-1$
							this.notifyAgentAboutActivityState(agent, task, Plain.WAITING);
							find = true;
							break;
						}
					}
					if (find)
						break;
				}
			}
		}

		if (has) {
			actNorm.getTheEnactionDescription().setStateWithMessage(Plain.WAITING);
			this.logging.registerGlobalActivityEvent(actNorm, "ToWaiting", why); //$NON-NLS-1$
		}
	}

	// Related to Resources

	/**
	 * Called by APSEE Manager. Involves the rules: 12.1, 12.5, 12.6 Allocates a
	 * Required Resource to a Normal Activity, and treating if the Resource is
	 * Comsumable.
	 */
	private void allocateResource(RequiredResource reqResource, Resource res, boolean allocConsum) {

		if (res instanceof Exclusive) {

			Exclusive allocRes = (Exclusive) res;
			allocRes.setState(Exclusive.LOCKED);
			this.logging.registerResourceEvent(allocRes, reqResource.getTheNormal(), "ToLocked", "Rule G7.13"); //$NON-NLS-1$ //$NON-NLS-2$

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
			Collection reqResources = allocRes.getTheRequiredResource();
			Iterator iter = reqResources.iterator();
			while (iter.hasNext()) {
				RequiredResource reqRes = (RequiredResource) iter.next();
				if (reqRes.getTheResource().equals(allocRes)) {
					this.logging.registerResourceEvent(allocRes, reqResource.getTheNormal(), "Requested", "Rule G7.13"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}
	}

	private boolean hasTheRequiredResourceType(Normal actNorm, ResourceType resType) {

		boolean has = false;
		Collection reqResTypes = actNorm.getTheRequiredResource();
		Iterator iter = reqResTypes.iterator();
		while (iter.hasNext()) {
			RequiredResource reqRes = (RequiredResource) iter.next();
			if (reqRes.getTheResourceType().equals(resType) && reqRes.getTheResource() == null) {
				has = true;
				break;
			}
		}
		return has;
	}

	private boolean hasTheResource(Normal actNorm, Resource res) {

		boolean has = false;
		Collection reqRess = actNorm.getTheRequiredResource();
		Iterator iter = reqRess.iterator();
		while (iter.hasNext()) {
			RequiredResource reqRes = (RequiredResource) iter.next();
			Resource resource = reqRes.getTheResource();
			if (resource != null) {
				if (resource.equals(res)) {
					has = true;
					break;
				}
			}
		}
		return has;
	}

	private boolean isSubType(Type subResType, GroupType groupType) {
		if (subResType.equals(groupType))
			return true;
		if (subResType.getSuperType() != null) {
			return this.isSubType(subResType.getSuperType(), groupType);
		} else
			return false;
	}

	private boolean isSubType(Type subResType, ResourceType resType) {
		if (subResType.equals(resType))
			return true;
		if (subResType.getSuperType() != null) {
			return this.isSubType(subResType.getSuperType(), resType);
		} else
			return false;
	}

	private boolean isSubType(Type subArtType, ArtifactType artType) {
		if (subArtType.equals(artType))
			return true;
		if (subArtType.getSuperType() != null) {
			return this.isSubType(subArtType.getSuperType(), artType);
		} else
			return false;
	}

	private void releaseResourceFromActivity(Normal actNorm, Resource res) {

		Collection activityResources = actNorm.getTheRequiredResource();
		Iterator iter = activityResources.iterator();
		while (iter.hasNext()) {
			RequiredResource requiredResource = (RequiredResource) iter.next();
			Resource resource = requiredResource.getTheResource();
			if (resource != null) {
				if (resource.equals(res)) {
					if (resource instanceof Exclusive) {
						Exclusive exc = (Exclusive) resource;
						if (exc.getState().equals(Exclusive.LOCKED)) {
							exc.setState(Exclusive.AVAILABLE);
							this.logging.registerResourceEvent(exc, actNorm, "ToAvailable", "Rule 7.19"); //$NON-NLS-1$ //$NON-NLS-2$
						}
					} else if (resource instanceof Shareable) {
						Shareable sha = (Shareable) resource;
						this.logging.registerResourceEvent(sha, actNorm, "Released", "Rule 7.19"); //$NON-NLS-1$ //$NON-NLS-2$
					}
					break;
				}
			}
		}
	}

	/**
	 * Called by APSEE Manager. Involves the rules: G1.4. Release all the
	 * resources from a Normal Activity. Changing their states to Available.
	 */
	private void releaseResourcesFromActivity(Normal actNorm) {

		Collection activityResources = actNorm.getTheRequiredResource();
		Iterator iter = activityResources.iterator();
		while (iter.hasNext()) {
			RequiredResource requiredResource = (RequiredResource) iter.next();
			Resource resource = requiredResource.getTheResource();
			if (resource instanceof Exclusive) {
				Exclusive exc = (Exclusive) resource;
				if (exc.getState().equals(Exclusive.LOCKED)) {
					exc.setState(Exclusive.AVAILABLE);
					this.logging.registerResourceEvent(exc, actNorm, "ToAvailable", "Rule 12.7"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			} else if (resource instanceof Shareable) {
				Shareable sha = (Shareable) resource;
				this.logging.registerResourceEvent(sha, actNorm, "Released", "Rule 12.2"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}

	/*
	 * SecurityFacade.getInstance(); } catch (Exception e) {
	 *
	 * } }
	 */
	// seta as informacoes do WebapseeObjectDTO
	private WebapseeObjectDTO newWebapseeObjectDTO(int oid, String className) {
		WebapseeObjectDTO webapseeObjectDTO = new WebapseeObjectDTO();
		webapseeObjectDTO.setOid(oid);
		webapseeObjectDTO.setClassName(className);
		return webapseeObjectDTO;
	}

	@Override
	public Integer getGlobalEvents(String act_id) throws DAOException, ModelingException {
System.out.println("atividades: "+act_id);
		// Checks for the parameters

		Object act;
		try {
			act = actDAO.retrieveBySecondaryKey(act_id);

		} catch (Exception/* DAOException */e) {
			throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDatabaseAccActv") + //$NON-NLS-1$
					act_id + Messages.getString("facades.DynamicModeling.DaoExcFailed") + e); //$NON-NLS-1$
		}

		if (act == null)
			throw new DAOException(
					Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound")); //$NON-NLS-1$ //$NON-NLS-2$

		Activity activity = (Activity) act;

		// Now we start the implementation of the rules

		if (activity instanceof Decomposed){
			System.out.println("eventos:2 "+activity.getTheProcessModel());
			System.out.println("eventos:3 "+activity.getTheProcessModel().getTheProcess());
			System.out.println("eventos:4 "+activity.getTheProcessModel().getTheProcess().getTheProcessEvent());
			System.out.println("eventos:5 "+activity.getTheProcessModel().getTheProcess().getTheProcessEvent().iterator().next().getTheCatalogEvents());
			System.out.println("eventos:6 "+activity.getTheProcessModel().getTheProcess().getTheProcessEvent().iterator().next().getTheCatalogEvents().getTheGlobalActivityEvent());

		}


		else if (activity instanceof Normal) { // Rule G1.4
			Normal actNorm = (Normal) activity;

		//	System.out.println("eventos:1 "+actNorm.getTheActivityType().getTheProcess());
			// End Checks for the parameters
	System.out.println("eventos:2 "+actNorm.getTheActivityType());
	System.out.println("eventos:3 "+actNorm.getTheActivityType().getTheProcess().iterator().next().getTheProcessEvent());
	System.out.println("eventos:4 "+actNorm.getTheActivityType().getTheProcess().iterator().next().getTheProcessEvent().iterator().next().getTheCatalogEvents());
	System.out.println("eventos:5 "+actNorm.getTheActivityType().getTheProcess().iterator().next().getTheProcessEvent().iterator().next().getTheCatalogEvents().getTheGlobalActivityEvent());


		}
		return null;
	}

}

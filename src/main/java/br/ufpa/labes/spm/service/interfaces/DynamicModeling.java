package br.ufpa.labes.spm.service.interfaces;

import java.util.Collection;


import br.ufpa.labes.spm.service.dto.WebapseeObjectDTO;
import br.ufpa.labes.spm.exceptions.DAOException;
import br.ufpa.labes.spm.exceptions.ModelingException;
import br.ufpa.labes.spm.exceptions.ModelingWarning;
import br.ufpa.labes.spm.exceptions.WebapseeException;
import br.ufpa.labes.spm.domain.Activity;
import br.ufpa.labes.spm.domain.Artifact;
import br.ufpa.labes.spm.domain.MultipleCon;
import br.ufpa.labes.spm.domain.ClassMethodCall;
import br.ufpa.labes.spm.domain.Script;
import br.ufpa.labes.spm.domain.ArtifactType;

public interface DynamicModeling {

	/**
	 * Related to section 1 /** Rule G1.1
	 */
	public Integer newNormalActivity(String level_id, String new_id)
			throws WebapseeException;

	/**
	 * Rule G1.2
	 */
	public Integer newDecomposedActivity(String level_id, String new_id)
			throws WebapseeException;

	/**
	 * Rule G1.3
	 */
	public Integer newAutomaticActivity(String level_id, String new_id)
			throws WebapseeException;

	/**
	 * Rule G1.4 and G1.5
	 */
	public Integer defineAsDecomposed(String act_id)
			throws DAOException, ModelingException;

	/**
	 * Rule G1.6 and G1.7
	 */
	public Integer defineAsAutomatic(String act_id)
			throws DAOException, ModelingException;

	/**
	 * Rule G1.8 and G1.9
	 */
	public Integer defineAsNormal(String act_id) throws DAOException,
			ModelingException;

	/**
	 * Rule G1.12 and G1.13 The Activity in Parameter must be a Normal
	 * activity!
	 */
	public void defineInputArtifact(String act_id, String art_id)
			throws DAOException, ModelingException;

	/**
	 * Rule G1.14 and G1.15 The Activity in Parameter must be a Normal
	 * activity!
	 */
	public void defineOutputArtifact(String act_id, String art_id)
			throws DAOException, ModelingException;

	/**
	 * Rule G1.16 The Activity in Parameter must be an Automatic activity!
	 */
	public Integer defineAutomatic(String auto_id, Script script,
			Collection parameters) throws DAOException;

	/**
	 * Rule G1.16 The Activity in Parameter must be an Automatic activity!
	 */
	public Integer defineAutomatic(String auto_id,
			ClassMethodCall cmc, Collection parameters) throws DAOException;

	/**
	 * Rule G1.17 till G1.103 WARNING: This Method contains Dynamic Changes
	 * related code!
	 */
	public void deleteActivity(String act_id) throws DAOException,
			WebapseeException;

	/**
	 * Rule G2.1
	 */
	public Integer newArtifactConnection(String level_id)
			throws DAOException;

	public Long newArtifactConnection(String level_id, String art_id)
			throws DAOException, ModelingException;

	/**
	 * Rule G2.2
	 */
	public Integer defineType_ArtifactConnection(String con_id,
			String type) throws DAOException;

	/**
	 * Rule G2.3 and G2.4
	 */
	public Integer changeType_ArtifactConnection(String con_id,
			String newType) throws DAOException;

	/**
	 * Rule G2.5, G2.6, G2.7 and G2.8
	 */
	public Integer defineInstance_ArtifactConnection(String con_id,
			String artifact_id) throws DAOException, ModelingException;

	/**
	 * Rule G2.9
	 */
	public Integer removeInstance_ArtifactConnection(String con_id)
			throws DAOException;

	/**
	 * Rule G2.10, G2.11, G2.12, G2.13 and G2.14
	 */
	public Integer defineOutput_ArtifactConnection(String con_id,
			String act_id) throws DAOException, ModelingException,
			WebapseeException;

	/**
	 * Rule G2.15
	 */
	public Integer removeOutput_ArtifactConnection(String con_id,
			String act_id) throws DAOException, ModelingException;

	/**
	 * Rule G2.16, G2.17, G2.18 and G2.19
	 */
	public Integer defineInput_ArtifactConnection_Activity(
			String con_id, String act_id) throws DAOException,
			ModelingException;

	/**
	 * Rule G2.20
	 */
	public Integer removeInput_ArtifactConnection_Activity(
			String con_id, String act_id) throws DAOException,
			ModelingException;

	/**
	 * Rule G2.21, G2.22, G2.23, G2.24 and G2.25
	 */
	public Integer defineInput_ArtifactConnection_Multiple(
			String con_id, String mcon_id) throws DAOException,
			ModelingException;

	/**
	 * Rule G2.26
	 */
	public Integer removeInput_ArtifactConnection_Multiple(
			String con_id, String multi_id) throws DAOException,
			ModelingException;

	/**
	 * Rule G2.27, G2.28, G2.29, G2.30, G2.31, G2.32, G2.33, G2.34, G2.35,
	 * G2.36, G2.37, G2.38, G2.39, G2.40, and G2.41 WARNING: This Method
	 * contains Dynamic Changes related code!
	 */
	public void deleteConnection(String con_id) throws DAOException,
			WebapseeException;

	/***************************************************************************
	 * |***Atomic Methods***|*
	 **************************************************************************/

	public Integer newInputArtifact(String level_id,
			Artifact artifact, Activity activity) throws WebapseeException,
			ModelingException;

	public Integer newInputArtifact(String level_id,
			ArtifactType artifactType, Activity activity)
			throws WebapseeException, ModelingException;

	public Integer newInputArtifact(String level_id,
			Artifact artifact, MultipleCon multipleCon)
			throws WebapseeException, ModelingException;

	public Integer newInputArtifact(String level_id,
			ArtifactType artifactType, MultipleCon multipleCon)
			throws WebapseeException, ModelingException;

	public Integer newOutputArtifact(String level_id,
			Artifact artifact, Activity activity) throws WebapseeException,
			ModelingException;

	public Integer newOutputArtifact(String level_id,
			ArtifactType artifactType, Activity activity)
			throws WebapseeException, ModelingException;

	/**
	 * Rule G3.1, 3.2, 3.3, 3.4, 3.5 and 3.6 WARNING: This Method contains
	 * Dynamic Changes related code!
	 */
	public Integer addSimpleConnection(String act_id_from,
			String act_id_to, String dependency) throws DAOException,
			WebapseeException;

	/**
	 * Rule G3.7 and 3.8
	 *
	 * @throws ModelingWarning
	 */
	public Integer addFeedbackConnection(String act_id_from,
			String act_id_to) throws DAOException, ModelingException,
			ModelingWarning;

	/**
	 * Rule G3.9
	 */
	public Integer newJoinANDCon(String dependency, String level_id)
			throws DAOException;

	/**
	 * Rule G3.10
	 */
	public Integer newJoinConOR(String dependency, String level_id)
			throws DAOException;

	/**
	 * Rule G3.11
	 */
	public Integer newJoinConXOR(String dependency, String level_id)
			throws DAOException;

	/**
	 * Rule G3.12
	 */
	public Integer newBranchANDCon(String dependency, String level_id)
			throws DAOException;

	/**
	 * Rule G3.13
	 */
	public Integer newBranchConOR(String dependency, String level_id)
			throws DAOException;

	/**
	 * Rule G3.14
	 */
	public Integer newBranchConXOR(String dependency, String level_id)
			throws DAOException;

	/**
	 * Rule G3.15 WARNING: This Method contains Dynamic Changes related code!
	 */
	public void removeMultiConPredecessorConnection(String con_id,
			String from_con_id) throws DAOException, WebapseeException;

	/**
	 * Rule G3.16 WARNING: This Method contains Dynamic Changes related code!
	 */
	public void removeMultiConPredecessorActivity(String con_id,
			String act_id) throws DAOException, WebapseeException;

	/**
	 * Rule G3.17 and G5.33 WARNING: This Method contains Dynamic Changes
	 * related code!
	 */
	public void removeMultiConSuccessorConnection(String con_id,
			String to_con_id) throws DAOException, WebapseeException;

	/**
	 * Rule G3.18 and G5.34 WARNING: This Method contains Dynamic Changes
	 * related code!
	 */
	public void removeMultiConSuccessorActivity(String con_id,
			String act_id) throws DAOException, WebapseeException;

	/**
	 * Rules G4.1, G4.2, G4.3, G4.4, G4.5, G4.6 G4.7, G4.8, G4.9, G4.10, G4.11,
	 * G4.12 G4.13, G4.14 and G4.15. WARNING: This Method contains Dynamic
	 * Changes related code!
	 */
	public WebapseeObjectDTO defineJoinConTo_Activity(String con_id,
			String act_id) throws DAOException, WebapseeException;

	/**
	 * Rules G4.16, G4.17, G4.18, G4.19, G4.20 and G4.21 WARNING: This Method
	 * contains Dynamic Changes related code!
	 */
	public Integer defineJoinConTo_Connection(String con_id,
			String to_con_id) throws DAOException, WebapseeException;

	/**
	 * Rules G4.22, G4.23, G4.28 and G4.29 WARNING: This Method contains Dynamic
	 * Changes related code!
	 */
	public void defineJoinConFrom_Connection(String con_id,
			String from_con_id) throws DAOException, WebapseeException;

	/**
	 * Rules G4.24, G4.25, G4.26, G4.27 G4.30, G4.31, G4.32 and G4.33 WARNING:
	 * This Method contains Dynamic Changes related code!
	 */
	public void defineJoinConFrom_Activity(String con_id, String act_id)
			throws DAOException, WebapseeException;

	/**
	 * Rules G5.1, G5.2, G5.3, G5.4 G5.5, G5.6, G5.7, G5.8, G5.9 G5.10, G5.11
	 * and G5.12 WARNING: This Method contains Dynamic Changes related code!
	 */
	public WebapseeObjectDTO defineBranchConFromActivity(String con_id,
			String act_id) throws DAOException, WebapseeException;

	/**
	 * Rules G5.13, G5.14, G5.15 G5.16, G5.17 and G5.18 WARNING: This Method
	 * contains Dynamic Changes related code!
	 */
	public WebapseeObjectDTO defineBranchConFromConnection(String con_id,
			String from_con_id) throws DAOException, WebapseeException;

	/**
	 * Rules G5.19, G5.20, G5.21 G5.22, G5.23, G5.26, G5.27 G5.28, G5.29 and
	 * G5.30 WARNING: This Method contains Dynamic Changes related code!
	 */
	public void defineBranchConToActivity(String con_id, String act_id)
			throws DAOException, WebapseeException;

	/**
	 * Rules G5.24, G5.25, G5.31 and G5.32 WARNING: This Method contains Dynamic
	 * Changes related code!
	 */
	public void defineBranchConToConnection(String con_id,
			String to_con_id) throws DAOException, WebapseeException;

	/**
	 * Rules G6.1 and G6.2 The Activity in Parameter must be a Normal activity!
	 */
	public void addRequiredRole(String act_id, String role_id)
			throws DAOException, ModelingException;

	/**
	 * Rules G6.3 and G6.4 The Activity in Parameter must be a Normal activity!
	 */
	public void addRequiredWorkGroupType(String act_id, String g_type)
			throws DAOException, ModelingException;

	/**
	 * Rules G6.5, G6.6, G6.7 G6.8, G6.9 and G6.10 The Activity in Parameter
	 * must be a Normal activity!
	 */
	public void removeRequiredRole(String act_id, String role_id)
			throws DAOException;

	/**
	 * Rules G6.11, G6.12 and G6.13 Rules G6.14, G6.15 and G6.16 are not
	 * applicable! WARNING: This Method contains Dynamic Changes related code!
	 * The Activity in Parameter must be a Normal activity!
	 */
	public boolean defineRequiredAgent(String act_id, String role_id,
			String ag_id) throws DAOException, WebapseeException;

	/**
	 * Rules G6.30 and G6.31 The Activity in Parameter must be a Normal
	 * activity!
	 */
	public void removeRequiredAgent(String act_id, String ag_id)
			throws DAOException, ModelingException;

	/**
	 * Rules G6.17, G6.18, G6.19 Rules G6.20, G6.21, G6.22 and G6.23 are not
	 * applicable. WARNING: This Method contains Dynamic Changes related code!
	 * The Activity in Parameter must be a Normal activity!
	 */
	public void defineRequiredWorkGroup(String act_id, String WorkGroup_id)
			throws DAOException, WebapseeException;

	/**
	 * Rules G6.32 and G6.33 The Activity in Parameter must be a Normal
	 * activity!
	 */
	public void removeRequiredWorkGroup(String act_id, String WorkGroup_id)
			throws DAOException, ModelingException;

	/**
	 * Rules G6.24, G6.25, G6.26 and G6.27 Rules G6.28 and G6.29 are not
	 * applicable! The Activity in Parameter must be a Normal activity!
	 */
	public void removeRequiredWorkGroupType(String act_id, String g_type)
			throws DAOException;

	/***************************************************************************
	 * |***Atomic Methods***|*
	 **************************************************************************/

	public Integer newAgent(String agent_id, String role_id,
			String normal_id) throws WebapseeException;

	public Integer newWorkGroup(String WorkGroup_id, String WorkGroupType_id,
			String normal_id) throws WebapseeException, ModelingException;

	public Integer newResource(String resource_id,
			String resourceType_id, String normal_id, float amount_needed)
			throws WebapseeException, ModelingException;

	public void removeCompleteRequiredAgent(String normal_id,
			String agent_id, String role_id) throws DAOException,
			ModelingException;

	public void removeCompleteRequiredWorkGroup(String normal_id,
			String WorkGroup_id, String WorkGroupType_id) throws DAOException,
			ModelingException;

	public void removeCompleteRequiredResource(String normal_id,
			String resource_id, String resourceType_id) throws DAOException,
			ModelingException;

	/**
	 * Rules G7.1 and G7.2 The Activity in Parameter must be a Normal activity!
	 */
	public Integer addRequiredResourceType(String act_id,
			String resType_id) throws DAOException, ModelingException;

	/**
	 * Rules G7.3 and G7.4 The Activity in Parameter must be a Normal activity!
	 */
	public Integer changeRequiredResourceType(String act_id,
			String oldResType_id, String newResType_id) throws DAOException,
			ModelingException;

	/**
	 * Rules G7.5, G7.6, G7.7 and G7.8 Rules G7.9 and G7.10 are not applicable!
	 * The Activity in Parameter must be a Normal activity!
	 */
	public void removeRequiredResourceType(String act_id,
			String resType_id) throws DAOException, ModelingException;

	/**
	 * Rules G7.11, G7.12, G7.13 Rules G7.14, G7.15 and G7.16 are not
	 * applicable! The Activity in Parameter must be a Normal activity!
	 */
	public Integer defineRequiredResource(String act_id,
			String res_id, float amount_needed) throws DAOException,
			ModelingException, WebapseeException;

	/**
	 * Rules G7.17, G7.18 and G7.19 The Activity in Parameter must be a Normal
	 * activity!
	 */
	public void removeRequiredResource(String act_id, String res_id)
			throws DAOException, ModelingException;

	public void changeRequiredResourceAmount(String act_id,
			String res_id, float new_amount_needed) throws DAOException,
			ModelingException;

	/**
	 * Rule Adding Agent to a WorkGroup
	 */
	public void addAgentToWorkGroup(String agentId, String WorkGroupId)
			throws WebapseeException;

	/**
	 * Rule Removing Agent to a WorkGroup
	 */
	public void removeAgentFromWorkGroup(String agentId, String WorkGroupId)
			throws WebapseeException;


	public void imprimirNoConsole(String mensagem);

	public String newInputArtifact(String level_id, String artifactIdent, String activityIdent)
			throws WebapseeException, ModelingException;

	public String newOutputArtifact(String level_id, String artifactIdent,
			String activityIdent) throws WebapseeException, ModelingException;

	public Integer getGlobalEvents(String act_id) throws DAOException,
			ModelingException;
}

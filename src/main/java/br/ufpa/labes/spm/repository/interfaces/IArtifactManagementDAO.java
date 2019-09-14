package br.ufpa.labes.spm.repository.interfaces;

import javax.persistence.EntityManager;

import br.ufpa.labes.spm.domain.Artifact;
import br.ufpa.labes.spm.domain.Task;

public interface IArtifactManagementDAO {
	EntityManager getPersistenceContext();

	Task getAgentTask(String agentIdent, String normalIdent);
	Object[] getArtifactsIdentsFromProcessModelWithoutTemplates(String oldProcessIdent);
	Artifact[] getArtifactsFromProcessModelWithPathNotEmpty(String processIdent);
}

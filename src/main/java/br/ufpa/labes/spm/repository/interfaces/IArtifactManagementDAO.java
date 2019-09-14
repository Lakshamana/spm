package br.ufpa.labes.spm.repository.interfaces;

import javax.ejb.Local;
import javax.persistence.EntityManager;

import br.ufpa.labes.spm.domain.Artifact;
import br.ufpa.labes.spm.domain.Task;

@Local
public interface IArtifactManagementDAO {
	EntityManager getPersistenceContext();

	Task getAgentTask(String agentIdent, String normalIdent);
	Object[] getArtifactsIdentsFromProcessModelWithoutTemplates(String oldProcessIdent);
	Artifact[] getArtifactsFromProcessModelWithPathNotEmpty(String processIdent);
}

package br.ufpa.labes.spm.repository.interfaces.plainActivities;


import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Artifact;
import br.ufpa.labes.spm.domain.Normal;

public interface INormalDAO extends IBaseDAO<Normal, Integer> {
	public String[] getInvolvedAgentsForNormal(String normalIdent);

	public String[] getRequiredResourcesForNormal(String normalIdent);

	public Artifact[] getOutputArtifactsForNormal(String normalIdent);

	public String[] getOutputArtifactsIdentsForNormal(String normalIdent);

	public String[] getInputArtifactsIdentsForNormal(String normalIdent);

}

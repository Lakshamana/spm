package br.ufpa.labes.spm.repository.interfaces.artifacts;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.service.dto.SimpleArtifactDescriptorDTO;
import br.ufpa.labes.spm.domain.Artifact;

public interface IArtifactDAO extends IBaseDAO<Artifact, String>{

	public Object[] getArtifactsIdentsFromProcessModelWithoutTemplates( String ident );
	public Artifact getByName(String name);
	public SimpleArtifactDescriptorDTO[] getInputArtifactsForNormal(String normalIdent);
	public SimpleArtifactDescriptorDTO[] getOutputArtifactsForNormal(String identActivity);
}

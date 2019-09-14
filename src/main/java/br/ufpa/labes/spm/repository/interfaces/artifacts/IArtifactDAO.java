package br.ufpa.labes.spm.repository.interfaces.artifacts;

import javax.ejb.Local;
import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import org.qrconsult.spm.dtos.agenda.SimpleArtifactDescriptorDTO;
import br.ufpa.labes.spm.domain.Artifact;

@Local
public interface IArtifactDAO extends IBaseDAO<Artifact, String>{

	public Object[] getArtifactsIdentsFromProcessModelWithoutTemplates( String ident );
	public Artifact getByName(String name);
	public SimpleArtifactDescriptorDTO[] getInputArtifactsForNormal(String normalIdent);
	public SimpleArtifactDescriptorDTO[] getOutputArtifactsForNormal(String identActivity);
}
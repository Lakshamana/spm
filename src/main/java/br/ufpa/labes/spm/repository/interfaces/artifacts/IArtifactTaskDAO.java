package br.ufpa.labes.spm.repository.interfaces.artifacts;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.ArtifactTask;

@Local
public interface IArtifactTaskDAO extends IBaseDAO<ArtifactTask, Integer>{

}

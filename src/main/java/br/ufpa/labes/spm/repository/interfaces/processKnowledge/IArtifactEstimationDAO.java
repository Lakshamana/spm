package br.ufpa.labes.spm.repository.interfaces.processKnowledge;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.ArtifactEstimation;

@Local
public interface IArtifactEstimationDAO extends IBaseDAO<ArtifactEstimation, Integer>{

}

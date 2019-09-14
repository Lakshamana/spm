package br.ufpa.labes.spm.repository.interfaces.plainActivities;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.ArtifactParam;

@Local
public interface IArtifactParamDAO extends IBaseDAO<ArtifactParam, Integer>{

}

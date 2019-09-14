package br.ufpa.labes.spm.repository.interfaces.processKnowledge;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.ResourceEstimation;

@Local
public interface IResourceEstimationDAO   extends IBaseDAO<ResourceEstimation, Integer>{

}

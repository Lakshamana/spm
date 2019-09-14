package br.ufpa.labes.spm.repository.interfaces.processKnowledge;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Estimation;

@Local
public interface IEstimationDAO   extends IBaseDAO<Estimation, Integer>{

}

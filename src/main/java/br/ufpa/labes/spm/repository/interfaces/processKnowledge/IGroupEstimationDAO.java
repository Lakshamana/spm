package br.ufpa.labes.spm.repository.interfaces.processKnowledge;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.GroupEstimation;

@Local
public interface IGroupEstimationDAO   extends IBaseDAO<GroupEstimation, Integer>{

}

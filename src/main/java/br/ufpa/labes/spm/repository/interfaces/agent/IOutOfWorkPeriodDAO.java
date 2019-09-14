package br.ufpa.labes.spm.repository.interfaces.agent;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.OutOfWorkPeriod;

@Local
public interface IOutOfWorkPeriodDAO extends IBaseDAO<OutOfWorkPeriod, Integer>{

}

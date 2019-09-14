package br.ufpa.labes.spm.repository.interfaces.processModels;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.ProcessModel;

@Local
public interface IProcessModelDAO extends IBaseDAO<ProcessModel, Integer>{

}

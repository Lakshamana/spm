package br.ufpa.labes.spm.repository.interfaces.log;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Log;

@Local
public interface ILogDAO extends IBaseDAO<Log, Integer>{

}

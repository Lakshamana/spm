package br.ufpa.labes.spm.repository.interfaces.log;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.GlobalActivityEvent;

@Local
public interface IGlobalActivityEventDAO extends IBaseDAO<GlobalActivityEvent, Integer>{

}

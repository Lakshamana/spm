package br.ufpa.labes.spm.repository.interfaces.taskagenda;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Ocurrence;

@Local
public interface IOcurrenceDAO extends IBaseDAO<Ocurrence, Integer>{

}

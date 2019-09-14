package br.ufpa.labes.spm.repository.interfaces.plainActivities;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Parameters;

@Local
public interface IParametersDAO extends IBaseDAO<Parameters, Integer>{

}

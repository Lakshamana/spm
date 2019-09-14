package br.ufpa.labes.spm.repository.interfaces.plainActivities;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.PrimitiveParam;

@Local
public interface IPrimitiveParamDAO extends IBaseDAO<PrimitiveParam, Integer>{

}

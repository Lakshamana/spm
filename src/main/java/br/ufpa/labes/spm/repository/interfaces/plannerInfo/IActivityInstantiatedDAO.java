package br.ufpa.labes.spm.repository.interfaces.plannerInfo;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.ActivityInstantiated;

@Local
public interface IActivityInstantiatedDAO  extends IBaseDAO<ActivityInstantiated, Integer>{

}

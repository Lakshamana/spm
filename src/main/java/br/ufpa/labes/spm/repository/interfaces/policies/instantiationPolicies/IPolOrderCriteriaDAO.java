package br.ufpa.labes.spm.repository.interfaces.policies.instantiationPolicies;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.PolOrderCriteria;

@Local
public interface IPolOrderCriteriaDAO extends IBaseDAO<PolOrderCriteria, Integer>{

}

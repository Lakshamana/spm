package br.ufpa.labes.spm.repository.interfaces.policies.instantiationPolicies;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.AbilityPolParam;

@Local
public interface IAbilityPolParamDAO extends IBaseDAO<AbilityPolParam, Integer>{

}

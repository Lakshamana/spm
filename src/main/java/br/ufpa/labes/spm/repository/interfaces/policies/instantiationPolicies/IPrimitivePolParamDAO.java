package br.ufpa.labes.spm.repository.interfaces.policies.instantiationPolicies;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.PrimitivePolParam;

@Local
public interface IPrimitivePolParamDAO extends IBaseDAO<PrimitivePolParam, Integer>{

}

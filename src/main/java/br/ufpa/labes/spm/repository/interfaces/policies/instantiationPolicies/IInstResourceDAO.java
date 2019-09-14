package br.ufpa.labes.spm.repository.interfaces.policies.instantiationPolicies;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.InstResource;

@Local
public interface IInstResourceDAO extends IBaseDAO<InstResource, String>{

}

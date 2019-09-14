package br.ufpa.labes.spm.repository.interfaces.policies.staticPolicies;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.PolConnection;

@Local
public interface IPolConnectionDAO extends IBaseDAO<PolConnection, Integer>{

}

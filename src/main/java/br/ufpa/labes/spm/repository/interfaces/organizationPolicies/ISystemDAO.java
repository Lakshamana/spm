package br.ufpa.labes.spm.repository.interfaces.organizationPolicies;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.System;

@Local
public interface ISystemDAO extends IBaseDAO<System, String>{

}

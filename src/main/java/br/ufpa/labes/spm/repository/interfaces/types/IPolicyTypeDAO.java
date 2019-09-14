package br.ufpa.labes.spm.repository.interfaces.types;

import javax.ejb.Local;
import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.PolicyType;

@Local
public interface IPolicyTypeDAO extends IBaseDAO<PolicyType, String>{

}

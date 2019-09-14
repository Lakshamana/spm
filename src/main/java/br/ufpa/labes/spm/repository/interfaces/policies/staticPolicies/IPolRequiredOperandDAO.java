package br.ufpa.labes.spm.repository.interfaces.policies.staticPolicies;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.PolRequiredOperand;

@Local
public interface IPolRequiredOperandDAO  extends IBaseDAO<PolRequiredOperand, Integer>{

}

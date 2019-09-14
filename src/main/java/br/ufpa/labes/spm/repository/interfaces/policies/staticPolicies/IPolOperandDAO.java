package br.ufpa.labes.spm.repository.interfaces.policies.staticPolicies;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.PolOperand;

@Local
public interface IPolOperandDAO extends IBaseDAO<PolOperand, Integer>{

}

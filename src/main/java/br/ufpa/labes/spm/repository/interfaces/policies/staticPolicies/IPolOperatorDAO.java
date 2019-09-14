package br.ufpa.labes.spm.repository.interfaces.policies.staticPolicies;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.PolOperator;

@Local
public interface IPolOperatorDAO  extends IBaseDAO<PolOperator, Integer>{

}

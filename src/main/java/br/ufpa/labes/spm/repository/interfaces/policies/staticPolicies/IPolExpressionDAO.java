package br.ufpa.labes.spm.repository.interfaces.policies.staticPolicies;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.PolExpression;

@Local
public interface IPolExpressionDAO  extends IBaseDAO<PolExpression, Integer>{

}

package br.ufpa.labes.spm.repository.interfaces.policies.instantiationPolicies;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.MetricPolParam;

@Local
public interface IMetricPolParamDAO extends IBaseDAO<MetricPolParam, Integer>{

}

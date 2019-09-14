package br.ufpa.labes.spm.repository.interfaces.types;

import javax.ejb.Local;
import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.MetricType;

@Local
public interface IMetricTypeDAO  extends IBaseDAO<MetricType, String>{

}

package br.ufpa.labes.spm.repository.interfaces.processKnowledge;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.ActivityMetric;

@Local
public interface IActivityMetricDAO   extends IBaseDAO<ActivityMetric, Integer>{

}

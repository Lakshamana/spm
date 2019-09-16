package br.ufpa.labes.spm.repository.interfaces.processKnowledge;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.ActivityEstimation;

public interface IActivityEstimationDAO extends IBaseDAO<ActivityEstimation, Integer> {
  public float getHoursEstimationForActivity(String normalIdent);
}

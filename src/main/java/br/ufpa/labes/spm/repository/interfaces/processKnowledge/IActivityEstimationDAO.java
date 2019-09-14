package br.ufpa.labes.spm.repository.interfaces.processKnowledge;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.ActivityEstimation;

@Local
public interface IActivityEstimationDAO   extends IBaseDAO<ActivityEstimation, Integer>{
	public float getHoursEstimationForActivity(String normalIdent) ;
}

package br.ufpa.labes.spm.repository.interfaces.processKnowledge;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.ProcessEstimation;

@Local
public interface IProcessEstimationDAO   extends IBaseDAO<ProcessEstimation, Integer> {

	public float getHoursEstimationForProject(String projectIdent);

}

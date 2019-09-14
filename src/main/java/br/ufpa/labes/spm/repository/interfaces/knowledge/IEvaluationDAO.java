package br.ufpa.labes.spm.repository.interfaces.knowledge;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Evaluation;

@Local
public interface IEvaluationDAO extends IBaseDAO<Evaluation, Integer>{

}

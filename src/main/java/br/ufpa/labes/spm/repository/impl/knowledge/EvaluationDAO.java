package br.ufpa.labes.spm.repository.impl.knowledge;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.knowledge.IEvaluationDAO;
import br.ufpa.labes.spm.domain.Evaluation;

@Stateless
public class EvaluationDAO extends BaseDAO<Evaluation, Integer> implements IEvaluationDAO{

	protected EvaluationDAO(Class<Evaluation> businessClass) {
		super(businessClass);
	}

	public EvaluationDAO() {
		super(Evaluation.class);
	}


}

package br.ufpa.labes.spm.repository.impl.knowledge;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.knowledge.IEvaluationDAO;
import br.ufpa.labes.spm.domain.Evaluation;

public class EvaluationDAO extends BaseDAO<Evaluation, Integer> implements IEvaluationDAO{

	protected EvaluationDAO(Class<Evaluation> businessClass) {
		super(businessClass);
	}

	public EvaluationDAO() {
		super(Evaluation.class);
	}


}

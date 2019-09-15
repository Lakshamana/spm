package br.ufpa.labes.spm.repository.impl.knowledge;


import br.ufpa.labes.spm.repository.impl.BaseDAO;

public class EvaluationDAO extends BaseDAO<Evaluation, Integer> implements IEvaluationDAO{

	protected EvaluationDAO(Class<Evaluation> businessClass) {
		super(businessClass);
	}

	public EvaluationDAO() {
		super(Evaluation.class);
	}


}

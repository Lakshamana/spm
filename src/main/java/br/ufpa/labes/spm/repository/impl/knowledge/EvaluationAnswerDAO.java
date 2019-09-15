package br.ufpa.labes.spm.repository.impl.knowledge;


import br.ufpa.labes.spm.repository.impl.BaseDAO;

public class EvaluationAnswerDAO extends BaseDAO<EvaluationAnswer, Integer> implements IEvaluationAnswerDAO{

	protected EvaluationAnswerDAO(Class<EvaluationAnswer> businessClass) {
		super(businessClass);
	}

	public EvaluationAnswerDAO() {
		super(EvaluationAnswer.class);
	}


}

package br.ufpa.labes.spm.repository.impl.knowledge;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.knowledge.IEvaluationAnswerDAO;
import br.ufpa.labes.spm.domain.EvaluationAnswer;

public class EvaluationAnswerDAO extends BaseDAO<EvaluationAnswer, Integer> implements IEvaluationAnswerDAO{

	protected EvaluationAnswerDAO(Class<EvaluationAnswer> businessClass) {
		super(businessClass);
	}

	public EvaluationAnswerDAO() {
		super(EvaluationAnswer.class);
	}


}

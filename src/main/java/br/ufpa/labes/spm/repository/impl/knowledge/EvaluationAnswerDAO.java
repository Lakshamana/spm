package br.ufpa.labes.spm.repository.impl.knowledge;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.knowledge.IEvaluationAnswerDAO;
import br.ufpa.labes.spm.domain.EvaluationAnswer;

@Stateless
public class EvaluationAnswerDAO extends BaseDAO<EvaluationAnswer, Integer> implements IEvaluationAnswerDAO{

	protected EvaluationAnswerDAO(Class<EvaluationAnswer> businessClass) {
		super(businessClass);
	}

	public EvaluationAnswerDAO() {
		super(EvaluationAnswer.class);
	}


}

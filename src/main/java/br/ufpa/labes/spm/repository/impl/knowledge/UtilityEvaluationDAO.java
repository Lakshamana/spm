package br.ufpa.labes.spm.repository.impl.knowledge;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.knowledge.IUtilityEvaluationDAO;
import br.ufpa.labes.spm.domain.UtilityEvaluation;

public class UtilityEvaluationDAO extends BaseDAO<UtilityEvaluation, String> implements IUtilityEvaluationDAO{

	protected UtilityEvaluationDAO(Class<UtilityEvaluation> businessClass) {
		super(businessClass);
	}

	public UtilityEvaluationDAO() {
		super(UtilityEvaluation.class);
	}


}

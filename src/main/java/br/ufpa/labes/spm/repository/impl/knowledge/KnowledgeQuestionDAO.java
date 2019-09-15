package br.ufpa.labes.spm.repository.impl.knowledge;


import br.ufpa.labes.spm.repository.impl.BaseDAO;

public class KnowledgeQuestionDAO extends BaseDAO<KnowledgeQuestion, String> implements IKnowledgeQuestionDAO{

	protected KnowledgeQuestionDAO(Class<KnowledgeQuestion> businessClass) {
		super(businessClass);
	}

	public KnowledgeQuestionDAO() {
		super(KnowledgeQuestion.class);
	}


}

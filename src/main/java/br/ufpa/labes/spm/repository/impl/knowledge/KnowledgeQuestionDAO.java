package br.ufpa.labes.spm.repository.impl.knowledge;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.knowledge.IKnowledgeQuestionDAO;
import br.ufpa.labes.spm.domain.KnowledgeQuestion;

@Stateless
public class KnowledgeQuestionDAO extends BaseDAO<KnowledgeQuestion, String> implements IKnowledgeQuestionDAO{

	protected KnowledgeQuestionDAO(Class<KnowledgeQuestion> businessClass) {
		super(businessClass);
	}

	public KnowledgeQuestionDAO() {
		super(KnowledgeQuestion.class);
	}


}

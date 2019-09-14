package br.ufpa.labes.spm.repository.impl.knowledge;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.knowledge.IKnowledgeQuestionPositionDAO;
import br.ufpa.labes.spm.domain.KnowledgeQuestionPosition;

@Stateless
public class KnowledgeQuestionPositionDAO extends BaseDAO<KnowledgeQuestionPosition, Integer> implements IKnowledgeQuestionPositionDAO{

	protected KnowledgeQuestionPositionDAO(Class<KnowledgeQuestionPosition> businessClass) {
		super(businessClass);
	}

	public KnowledgeQuestionPositionDAO() {
		super(KnowledgeQuestionPosition.class);
	}


}

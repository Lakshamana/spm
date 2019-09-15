package br.ufpa.labes.spm.repository.impl.knowledge;


import br.ufpa.labes.spm.repository.impl.BaseDAO;

public class KnowledgeAttributePositionDAO extends BaseDAO<KnowledgeAttributePosition, Integer> implements IKnowledgeAttributePositionDAO{

	protected KnowledgeAttributePositionDAO(Class<KnowledgeAttributePosition> businessClass) {
		super(businessClass);
	}

	public KnowledgeAttributePositionDAO() {
		super(KnowledgeAttributePosition.class);
	}


}

package br.ufpa.labes.spm.repository.impl.knowledge;


import br.ufpa.labes.spm.repository.impl.BaseDAO;

public class KnowledgeValueDAO extends BaseDAO<KnowledgeValue, Integer> implements IKnowledgeValueDAO{

	protected KnowledgeValueDAO(Class<KnowledgeValue> businessClass) {
		super(businessClass);
	}

	public KnowledgeValueDAO() {
		super(KnowledgeValue.class);
	}


}

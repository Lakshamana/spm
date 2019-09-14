package br.ufpa.labes.spm.repository.impl.knowledge;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.knowledge.IKnowledgeValueDAO;
import br.ufpa.labes.spm.domain.KnowledgeValue;

public class KnowledgeValueDAO extends BaseDAO<KnowledgeValue, Integer> implements IKnowledgeValueDAO{

	protected KnowledgeValueDAO(Class<KnowledgeValue> businessClass) {
		super(businessClass);
	}

	public KnowledgeValueDAO() {
		super(KnowledgeValue.class);
	}


}

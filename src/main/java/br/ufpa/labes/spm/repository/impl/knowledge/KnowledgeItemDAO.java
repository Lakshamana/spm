package br.ufpa.labes.spm.repository.impl.knowledge;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.knowledge.IKnowledgeItemDAO;
import br.ufpa.labes.spm.domain.KnowledgeItem;

public class KnowledgeItemDAO extends BaseDAO<KnowledgeItem, String> implements IKnowledgeItemDAO{

	protected KnowledgeItemDAO(Class<KnowledgeItem> businessClass) {
		super(businessClass);
	}

	public KnowledgeItemDAO() {
		super(KnowledgeItem.class);
	}


}

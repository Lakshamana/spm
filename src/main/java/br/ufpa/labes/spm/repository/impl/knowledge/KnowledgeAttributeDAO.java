package br.ufpa.labes.spm.repository.impl.knowledge;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.knowledge.IKnowledgeAttributeDAO;
import br.ufpa.labes.spm.domain.KnowledgeAttribute;

public class KnowledgeAttributeDAO extends BaseDAO<KnowledgeAttribute, String> implements IKnowledgeAttributeDAO{

	protected KnowledgeAttributeDAO(Class<KnowledgeAttribute> businessClass) {
		super(businessClass);
	}

	public KnowledgeAttributeDAO() {
		super(KnowledgeAttribute.class);
	}


}

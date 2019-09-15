package br.ufpa.labes.spm.repository.impl.knowledge;


import br.ufpa.labes.spm.repository.impl.BaseDAO;

public class KnowledgeAttributeDAO extends BaseDAO<KnowledgeAttribute, String> implements IKnowledgeAttributeDAO{

	protected KnowledgeAttributeDAO(Class<KnowledgeAttribute> businessClass) {
		super(businessClass);
	}

	public KnowledgeAttributeDAO() {
		super(KnowledgeAttribute.class);
	}


}

package br.ufpa.labes.spm.repository.impl.types;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IKnowledgeTypeDAO;
import br.ufpa.labes.spm.domain.KnowledgeType;

public class KnowledgeTypeDAO extends BaseDAO<KnowledgeType, String> implements IKnowledgeTypeDAO{

	protected KnowledgeTypeDAO(Class<KnowledgeType> businessClass) {
		super(businessClass);
	}

	public KnowledgeTypeDAO() {
		super(KnowledgeType.class);
	}


}

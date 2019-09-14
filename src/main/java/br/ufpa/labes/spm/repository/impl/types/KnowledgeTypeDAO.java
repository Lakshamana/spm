package br.ufpa.labes.spm.repository.impl.types;

import javax.ejb.Stateless;
import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IKnowledgeTypeDAO;
import br.ufpa.labes.spm.domain.KnowledgeType;

@Stateless
public class KnowledgeTypeDAO extends BaseDAO<KnowledgeType, String> implements IKnowledgeTypeDAO{

	protected KnowledgeTypeDAO(Class<KnowledgeType> businessClass) {
		super(businessClass);
	}

	public KnowledgeTypeDAO() {
		super(KnowledgeType.class);
	}


}

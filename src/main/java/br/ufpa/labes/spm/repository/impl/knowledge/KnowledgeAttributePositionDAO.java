package br.ufpa.labes.spm.repository.impl.knowledge;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.knowledge.IKnowledgeAttributePositionDAO;
import br.ufpa.labes.spm.domain.KnowledgeAttributePosition;

@Stateless
public class KnowledgeAttributePositionDAO extends BaseDAO<KnowledgeAttributePosition, Integer> implements IKnowledgeAttributePositionDAO{

	protected KnowledgeAttributePositionDAO(Class<KnowledgeAttributePosition> businessClass) {
		super(businessClass);
	}

	public KnowledgeAttributePositionDAO() {
		super(KnowledgeAttributePosition.class);
	}


}

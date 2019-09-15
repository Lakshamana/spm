package br.ufpa.labes.spm.repository.impl.knowledge;


import br.ufpa.labes.spm.repository.impl.BaseDAO;

public class KnowledgeMilestoneDAO extends BaseDAO<KnowledgeMilestone, Integer> implements IKnowledgeMilestoneDAO{

	protected KnowledgeMilestoneDAO(Class<KnowledgeMilestone> businessClass) {
		super(businessClass);
	}

	public KnowledgeMilestoneDAO() {
		super(KnowledgeMilestone.class);
	}


}

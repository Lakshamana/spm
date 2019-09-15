package br.ufpa.labes.spm.repository.impl.knowledge;


import br.ufpa.labes.spm.repository.impl.BaseDAO;

public class KnowledgeMaintenanceDAO extends BaseDAO<KnowledgeMaintenance, Integer> implements IKnowledgeMaintenanceDAO{

	protected KnowledgeMaintenanceDAO(Class<KnowledgeMaintenance> businessClass) {
		super(businessClass);
	}

	public KnowledgeMaintenanceDAO() {
		super(KnowledgeMaintenance.class);
	}

}

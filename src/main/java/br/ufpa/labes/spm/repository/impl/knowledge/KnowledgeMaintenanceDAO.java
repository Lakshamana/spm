package br.ufpa.labes.spm.repository.impl.knowledge;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.knowledge.IKnowledgeMaintenanceDAO;
import br.ufpa.labes.spm.domain.KnowledgeMaintenance;

public class KnowledgeMaintenanceDAO extends BaseDAO<KnowledgeMaintenance, Integer> implements IKnowledgeMaintenanceDAO{

	protected KnowledgeMaintenanceDAO(Class<KnowledgeMaintenance> businessClass) {
		super(businessClass);
	}

	public KnowledgeMaintenanceDAO() {
		super(KnowledgeMaintenance.class);
	}

}

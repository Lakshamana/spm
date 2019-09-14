package br.ufpa.labes.spm.repository.impl.knowledge;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.knowledge.IKnowledgeMaintenanceDAO;
import br.ufpa.labes.spm.domain.KnowledgeMaintenance;

@Stateless
public class KnowledgeMaintenanceDAO extends BaseDAO<KnowledgeMaintenance, Integer> implements IKnowledgeMaintenanceDAO{

	protected KnowledgeMaintenanceDAO(Class<KnowledgeMaintenance> businessClass) {
		super(businessClass);
	}

	public KnowledgeMaintenanceDAO() {
		super(KnowledgeMaintenance.class);
	}

}

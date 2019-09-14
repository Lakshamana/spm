package br.ufpa.labes.spm.repository.impl.knowledge;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.knowledge.IKMPlanDAO;
import br.ufpa.labes.spm.domain.KMPlan;

public class KMPlanDAO extends BaseDAO<KMPlan, String> implements IKMPlanDAO{

	protected KMPlanDAO(Class<KMPlan> businessClass) {
		super(businessClass);
	}

	public KMPlanDAO() {
		super(KMPlan.class);
	}


}

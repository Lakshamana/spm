package br.ufpa.labes.spm.repository.impl.processKnowledge;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.processKnowledge.IAgentEstimationDAO;
import br.ufpa.labes.spm.domain.AgentEstimation;

@Stateless
public class AgentEstimationDAO extends BaseDAO<AgentEstimation, Integer> implements IAgentEstimationDAO{

	protected AgentEstimationDAO(Class<AgentEstimation> businessClass) {
		super(businessClass);
	}

	public AgentEstimationDAO() {
		super(AgentEstimation.class);
	}


}

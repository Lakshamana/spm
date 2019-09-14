package br.ufpa.labes.spm.repository.impl.processKnowledge;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.processKnowledge.IAgentMetricDAO;
import br.ufpa.labes.spm.domain.AgentMetric;

@Stateless
public class AgentMetricDAO extends BaseDAO<AgentMetric, Integer> implements IAgentMetricDAO{

	protected AgentMetricDAO(Class<AgentMetric> businessClass) {
		super(businessClass);
	}

	public AgentMetricDAO() {
		super(AgentMetric.class);
	}


}

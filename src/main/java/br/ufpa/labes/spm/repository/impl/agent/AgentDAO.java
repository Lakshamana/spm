package br.ufpa.labes.spm.repository.impl.agent;

import javax.persistence.Query;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.agent.IAgentDAO;
import org.qrconsult.spm.dtos.formAgent.AgentDTO;
import br.ufpa.labes.spm.domain.Agent;

public class AgentDAO extends BaseDAO<Agent, Integer> implements IAgentDAO {

	protected AgentDAO(Class<Agent> businessClass) {
		super(businessClass);
	}

	public AgentDAO() {
		super(Agent.class);
	}

	@Override
	public AgentDTO login(String name, String password) {

		Query query = getPersistenceContext()
				.createQuery(
						"SELECT agent FROM "
								+ Agent.class.getName()
								+ " AS agent "
								+ "WHERE agent.name like :name and agent.password like :password");

		query.setParameter("name", name);
		query.setParameter("password", password);
		try {

			return (AgentDTO) query.getSingleResult();

		} catch (Exception e) {

			return null;
		}
	}

}

package org.qrconsult.spm.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.qrconsult.spm.converter.core.Converter;
import org.qrconsult.spm.converter.core.ConverterImpl;
import org.qrconsult.spm.converter.exception.ImplementationException;
import org.qrconsult.spm.dataAccess.interfaces.agent.IAbilityDAO;
import org.qrconsult.spm.dataAccess.interfaces.agent.IAgentAffinityAgentDAO;
import org.qrconsult.spm.dataAccess.interfaces.agent.IAgentDAO;
import org.qrconsult.spm.dataAccess.interfaces.agent.IAgentHasAbilityDAO;
import org.qrconsult.spm.dataAccess.interfaces.agent.IAgentPlaysRoleDAO;
import org.qrconsult.spm.dataAccess.interfaces.agent.IConfiDAO;
import org.qrconsult.spm.dataAccess.interfaces.agent.IGroupDAO;
import org.qrconsult.spm.dataAccess.interfaces.agent.IRoleDAO;
import org.qrconsult.spm.dataAccess.interfaces.agent.IRoleNeedsAbilityDAO;
import org.qrconsult.spm.dataAccess.interfaces.processKnowledge.IActivityEstimationDAO;
import org.qrconsult.spm.dataAccess.interfaces.taskagenda.IProcessAgendaDAO;
import org.qrconsult.spm.dataAccess.interfaces.taskagenda.ITaskAgendaDAO;
import org.qrconsult.spm.dataAccess.interfaces.taskagenda.ITaskDAO;
import org.qrconsult.spm.dataAccess.interfaces.types.IRoleTypeDAO;
import org.qrconsult.spm.dtos.agenda.TaskDTO;
import org.qrconsult.spm.dtos.dashboard.Time;
import org.qrconsult.spm.dtos.formAbility.AbilityDTO;
import org.qrconsult.spm.dtos.formAgent.AgentAffinityAgentDTO;
import org.qrconsult.spm.dtos.formAgent.AgentDTO;
import org.qrconsult.spm.dtos.formAgent.AgentHasAbilityDTO;
import org.qrconsult.spm.dtos.formAgent.AgentsDTO;
import org.qrconsult.spm.dtos.formAgent.ConfigurationDTO;
import org.qrconsult.spm.dtos.formGroup.GroupDTO;
import org.qrconsult.spm.dtos.formRole.RoleDTO;
import org.qrconsult.spm.model.agent.Ability;
import org.qrconsult.spm.model.agent.Agent;
import org.qrconsult.spm.model.agent.AgentAffinityAgent;
import org.qrconsult.spm.model.agent.AgentHasAbility;
import org.qrconsult.spm.model.agent.AgentPlaysRole;
import org.qrconsult.spm.model.agent.Group;
import org.qrconsult.spm.model.agent.Role;
import org.qrconsult.spm.model.configuration.Configuration;
import org.qrconsult.spm.model.organizationPolicies.Project;
import org.qrconsult.spm.model.processModels.Process;
import org.qrconsult.spm.model.taskagenda.ProcessAgenda;
import org.qrconsult.spm.model.taskagenda.Task;
import org.qrconsult.spm.model.taskagenda.TaskAgenda;
import org.qrconsult.spm.services.interfaces.AgentServices;
import org.qrconsult.spm.util.Md5;

@Stateless
public class AgentServicesImpl implements AgentServices {

	private static final String PROJECT_CLASSNAME = Project.class.getSimpleName();

	private static final String TASK_CLASSNAME = Task.class.getSimpleName();
	
	private static final String TASKAGENDA_CLASSNAME = TaskAgenda.class.getSimpleName();

	private static final String AGENT_HAS_ABILLITY_CLASSNAME = AgentHasAbility.class.getSimpleName();

	private static final String AGENT_AFFINITY_AGENT_CLASSNAME = AgentAffinityAgent.class.getSimpleName();

	private static final String AGENT_PLAY_ROLE_CLASSNAME = AgentPlaysRole.class.getSimpleName();

	private static final String ROLE_CLASSNAME = Role.class.getSimpleName();

	private static final String ABILITY_CLASSNAME = Ability.class.getSimpleName();

	private static final String GROUP_CLASSNAME = Group.class.getSimpleName();

	private static final String PROCESS_AGENDA_CLASSNAME = ProcessAgenda.class.getName();

	private static final String AGENT_CLASSNAME = Agent.class.getSimpleName();

	@EJB
	IAgentDAO agentDAO;

	@EJB
	IAbilityDAO abilityDAO;

	@EJB
	IRoleDAO roleDAO;

	@EJB
	IRoleTypeDAO roleTypeDAO;

	@EJB
	IRoleNeedsAbilityDAO roleNeedsDAO;

	@EJB
	IAgentPlaysRoleDAO agentPlaysRoleDAO;

	@EJB
	IAgentHasAbilityDAO agentHasAbilityDAO;

	@EJB
	IAgentAffinityAgentDAO agentAffinityAgentDAO;

	@EJB
	IGroupDAO groupDAO;
	
	@EJB
	IConfiDAO confiDAO;
	
	@EJB
	IProcessAgendaDAO processAgendaDAO;
	
	@EJB
	ITaskDAO taskDAO;
	
	@EJB
	ITaskAgendaDAO taskAgendaDAO;
	
	@EJB
	IActivityEstimationDAO activityEstimationDAO;	

	Converter converter = new ConverterImpl();

	private Query query;

	private Agent agent;
	private Agent agentRetorno;
	private AgentDTO agentd;

	@Override
	public AgentDTO getAgent(String agentIdent) {
//		Agent agent = this.getAgentForName(nameAgent);
		Agent agent = agentDAO.retrieveBySecondaryKey(agentIdent);

		AgentDTO agentDTO = this.convertAgentToAgentDTO(agent, false);

		return agentDTO;
	}

	@SuppressWarnings("unchecked")
	private Agent getAgentForName(String nome) {
		String hql;
		Query query;
		List<Agent> result = null;

		hql = "select agent from " + AGENT_CLASSNAME
				+ " as agent where agent.name = :rolname";
		query = agentDAO.getPersistenceContext().createQuery(hql);
		query.setParameter("rolname", nome);

		result = query.getResultList();

		if (!result.isEmpty()) {
			Agent agent = result.get(0);

			return agent;
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private Agent getAgentForLogin(String login) {
		String hql;
		Query query;
		List<Agent> result = null;

		hql = "select agent from " + AGENT_CLASSNAME
				+ " as agent where agent.eMail = :email";
		query = agentDAO.getPersistenceContext().createQuery(hql);
		query.setParameter("email", login);

		result = query.getResultList();

		if (!result.isEmpty()) {
			Agent agent = result.get(0);

			return agent;
		} else {
			return null;
		}
	}

	@Override
	public AgentDTO saveAgent(AgentDTO agentDTO,ConfigurationDTO config) {
		agent = new Agent();

//		agent = this.getAgentForName(agentDTO.getName());
		agent = agentDAO.retrieveBySecondaryKey(agentDTO.getIdent());

		if (agent != null) {
			updateAgent(agent, agentDTO);
			agentDAO.update(agent);

		} else {
			agentDTO.setPassword(Md5.getMd5Digest(agentDTO.getPassword()));
			agentDTO.setArtifactMngPassword(Md5.getMd5Digest(agentDTO.getArtifactMngPassword()));
			
			agent = this.convertAgentDTOToAgent(agentDTO);
			agentDAO.save(agent);
			
			TaskAgenda theTaskAgenda = new TaskAgenda();
			agent.setTheTaskAgenda(theTaskAgenda);
			theTaskAgenda.setTheAgent(agent);
			taskAgendaDAO.save(theTaskAgenda);
			
			String newIdent = agentDAO.generateIdent(agent.getName(), agent);
			agent.setIdent(newIdent);
			agentDAO.update(agent);
			
			getPerfilAgentePadrao(agent.getIdent());
			
			Configuration configuration = new Configuration();
			configuration.setFiltro("ID,teste,teste");		
			configuration.setAgent(agentRetorno);
			configuration.setIdioma(config.getIdioma());
			configuration.setSenhaEmRecuperacao(false);
			
			confiDAO.save(configuration);
		}

		
		updateAgentDependencies(agent, agentDTO);

		return agentDTO;
	}

	@Override
	public Boolean updateSenha(AgentDTO agentDTO) {
		agent = new Agent();
		Boolean retorno = null;

		try {
			agent = agentDAO.retrieveBySecondaryKey(agentDTO.getIdent());

			if (agent != null) {
				agentDTO.setPassword(Md5.getMd5Digest(agentDTO.getPassword()));
				System.out.println("nova senha: "+agentDTO.getPassword());
				agent.setPassword(agentDTO.getPassword());
				updateAgent(agent, agentDTO);
				agentDAO.update(agent);

			}

			updateAgentDependencies(agent, agentDTO);
			retorno = true;
		} catch (Exception e) {
			e.printStackTrace();
			retorno = false;
		}
		// agent = this.getAgentForName(agentDTO.getName());

		return retorno;
	}
	
	@Override
	public Agent getPerfilAgentePadrao(String ident) {
		
		query = agentDAO
				.getPersistenceContext()
				.createQuery(
						"SELECT agent FROM "
								+ AGENT_CLASSNAME
								+ " AS agent "
								+ "WHERE agent.ident like :ident");

		query.setParameter("ident", ident);
	
		agentRetorno = (Agent) query.getSingleResult();
		return (Agent) query.getSingleResult();
	}
	
	private void updateAgent(Agent agent, AgentDTO agentDTO) {
		agent.setPassword(agentDTO.getPassword());
		agent.setArtifactMngPassword(agentDTO.getArtifactMngPassword());
		agent.setEMail(agentDTO.getEMail());
		agent.setCostHour(agentDTO.getCostHour());
		agent.setIsActive(agentDTO.isIsActive());
		agent.setDescription(agentDTO.getDescription());
		agent.setUpload(agentDTO.getUpload());
	}

	private void updateAgentDependencies(Agent agent, AgentDTO agentDTO) {
		this.saveRoleToAgent(agentDTO);
		this.saveGroupToAgent(agentDTO);

		if (!agentDTO.getAbilityToAgent().isEmpty()) {
			for (String abilityName : agentDTO.getAbilityToAgent()) {
				Ability ability = this.getAbilityFromName(abilityName);
				if (ability != null) {
					AgentHasAbility agentHasAbility = new AgentHasAbility();
					agentHasAbility.setTheAgent(agent);
					agentHasAbility.setTheAbility(ability);

					boolean isAgentHasAbility = agent.getTheAgentHasAbility()
							.contains(agentHasAbility);

					if (!isAgentHasAbility) {
						new AgentHasAbility(agent, ability);
					}
				}
			}
		}

		if (!agentDTO.getAfinityToAgent().isEmpty()) {
			for (String agentName : agentDTO.getAfinityToAgent()) {
				Agent afinityAgent = this.getAgentForName(agentName);
				if (afinityAgent != null) {
					AgentAffinityAgent agAffinity = this.getAffinityByName(
							agentName, agentDTO.getName());

					boolean isAgentHasAffinity = agent.getToAgentAffinity()
							.contains(agAffinity);

					if (!isAgentHasAffinity) {
						new AgentAffinityAgent(agent, afinityAgent);
						new AgentAffinityAgent(afinityAgent, agent);
					}
				}
			}
		}

		agentDAO.update(agent);
		agent = null;
	}

	@Override
	public Boolean removeAgent(String agentIdent) {
//		Agent agent = this.getAgentForName(nameAgent);
		Agent agent = agentDAO.retrieveBySecondaryKey(agentIdent);
		String hql = "SELECT c FROM " + Configuration.class.getSimpleName() + " AS c WHERE c.agent.ident = :ident";
		TypedQuery<Configuration> query = confiDAO.getPersistenceContext().createQuery(hql, Configuration.class);
		query.setParameter("ident", agentIdent);
		
		hql = "SELECT ta FROM " + TaskAgenda.class.getSimpleName() + " AS ta WHERE ta.theAgent.ident = :ident";
		TypedQuery<TaskAgenda> query2 = taskAgendaDAO.getPersistenceContext().createQuery(hql, TaskAgenda.class);
		query2.setParameter("ident", agentIdent);

		if (agent != null) {
			if(!query.getResultList().isEmpty()) {
				Configuration config = query.getResultList().get(0);
				config.setAgent(null);
				confiDAO.delete(config);
			}
			if(!query2.getResultList().isEmpty()) {
				TaskAgenda taskAgenda = query2.getResultList().get(0);
				taskAgenda.setTheAgent(null);
				taskAgendaDAO.delete(taskAgenda);
			}
			
			for (AgentHasAbility agentHasAbility : agent
					.getTheAgentHasAbility()) {
				agentHasAbility.removeFromTheAbility();
				agentHasAbility.setTheAgent(null);
				agentHasAbilityDAO.delete(agentHasAbility);
			}

			for (AgentPlaysRole agentPlaysRole : agent.getTheAgentPlaysRole()) {
				agentPlaysRole.removeFromTheRole();
				agentPlaysRole.setTheAgent(null);
			}

			for (Group group : agent.getTheGroup()) {
				group.getTheAgent().remove(agent);
			}

			for (AgentAffinityAgent agentAffinityAgent : agent
					.getFromAgentAffinity()) {
				agentAffinityAgent.removeFromFromAffinity();
				agentAffinityAgent.setToAffinity(null);
				agentAffinityAgentDAO.delete(agentAffinityAgent);
			}
			
			for (AgentAffinityAgent agentAffinityAgent : agent
					.getToAgentAffinity()) {
				agentAffinityAgent.removeFromToAffinity();
				agentAffinityAgent.setFromAffinity(null);
				agentAffinityAgentDAO.delete(agentAffinityAgent);
			}

			agent.setTheGroup(new HashSet<Group>());
			agent.setFromAgentAffinity(new HashSet<AgentAffinityAgent>());
			agent.setToAgentAffinity(new HashSet<AgentAffinityAgent>());
			agent.setTheAgentPlaysRole(new HashSet<AgentPlaysRole>());
			agent.setTheAgentHasAbility(new HashSet<AgentHasAbility>());

			agentDAO.delete(agent);
			return true;
		}

		return false;
	}

	@Override
	public AgentsDTO getRoles(String termoBusca, String domainFilter) {
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AbilityDTO> getAbilitysToAgent() {
		String hql;
		Query query;
		List<Ability> result = null;
		List<AbilityDTO> resultDTO = new ArrayList<AbilityDTO>();

		hql = "select ability from " + ABILITY_CLASSNAME
				+ " as ability";
		query = agentDAO.getPersistenceContext().createQuery(hql);
		result = query.getResultList();
		Converter converter = new ConverterImpl();

		if (!result.isEmpty()) {
			AbilityDTO abi = null;
			for (int i = 0; i < result.size(); i++) {
				try {
					abi = (AbilityDTO) converter.getDTO(result.get(i),
							AbilityDTO.class);
					resultDTO.add(abi);
				} catch (ImplementationException e) {
					e.printStackTrace();
				}

			}
			return resultDTO;
		} else {
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<RoleDTO> getRolesToAgent() {
		String hql;
		Query query;
		List<Role> result = new ArrayList<Role>();
		List<RoleDTO> resultDTO = new ArrayList<RoleDTO>();

		hql = "select role from " + ROLE_CLASSNAME + " as role";
		query = roleDAO.getPersistenceContext().createQuery(hql);
		result = query.getResultList();
		Converter converter = new ConverterImpl();

		if (!result.isEmpty()) {
			RoleDTO abi = null;
			for (int i = 0; i < result.size(); i++) {
				try {
					abi = (RoleDTO) converter.getDTO(result.get(i),
							RoleDTO.class);
					resultDTO.add(abi);
				} catch (ImplementationException e) {
					e.printStackTrace();
				}
			}
			return resultDTO;
		} else {
			return new ArrayList<RoleDTO>();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AgentDTO> getAfinityToAgent() {
		String hql;
		Query query;
		List<Agent> result = null;
		List<AgentDTO> resultDTO = new ArrayList<AgentDTO>();

		hql = "select agente from " + AGENT_CLASSNAME + " as agente";
		query = agentDAO.getPersistenceContext().createQuery(hql);
		result = query.getResultList();
		Converter converter = new ConverterImpl();

		if (!result.isEmpty()) {
			AgentDTO abi = null;
			for (int i = 0; i < result.size(); i++) {
				try {
					abi = (AgentDTO) converter.getDTO(result.get(i),
							AgentDTO.class);
					resultDTO.add(abi);
				} catch (ImplementationException e) {
					e.printStackTrace();
				}
			}
			return resultDTO;
		} else {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public AgentAffinityAgentDTO getAffinityAgent(String affinityAgentName) {
		String hql = "SELECT afa FROM "
				+ AGENT_AFFINITY_AGENT_CLASSNAME
				+ " as afa WHERE afa.toAffinity.name = :toAffinity";
		query = agentAffinityAgentDAO.getPersistenceContext().createQuery(hql);
		query.setParameter("toAffinity", affinityAgentName);

		List<AgentAffinityAgent> result = query.getResultList();

		if (!result.isEmpty()) {
			AgentAffinityAgent afa = result.get(0);
			return new AgentAffinityAgentDTO(afa.getDegree(), afa
					.getToAffinity().getName(), afa.getFromAffinity().getName());
		}

		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public AgentAffinityAgentDTO getAffinityAgent(String fromAffinityName,
			String toAffinityName) {
		String hql = "SELECT afa FROM "
				+ AGENT_AFFINITY_AGENT_CLASSNAME
				+ " as afa WHERE afa.toAffinity.name = :toAffinity AND afa.fromAffinity.name = :fromAffinity";
		query = agentAffinityAgentDAO.getPersistenceContext().createQuery(hql);
		query.setParameter("toAffinity", toAffinityName);
		query.setParameter("fromAffinity", fromAffinityName);

		List<AgentAffinityAgent> result = query.getResultList();

		if (!result.isEmpty()) {
			AgentAffinityAgent afa = result.get(0);
			return new AgentAffinityAgentDTO(afa.getDegree(), afa
					.getToAffinity().getName(), afa.getFromAffinity().getName());
		}

		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<GroupDTO> getGroupToAgent() {
		String hql;
		Query query;
		List<Group> result = null;
		List<GroupDTO> resultDTO = new ArrayList<GroupDTO>();

		hql = "select grupo from " + GROUP_CLASSNAME + " as grupo";
		query = agentDAO.getPersistenceContext().createQuery(hql);
		result = query.getResultList();
		Converter converter = new ConverterImpl();

		if (!result.isEmpty()) {
			GroupDTO abi = null;
			for (int i = 0; i < result.size(); i++) {
				try {
					abi = (GroupDTO) converter.getDTO(result.get(i),
							GroupDTO.class);
					resultDTO.add(abi);
				} catch (ImplementationException e) {
					e.printStackTrace();
				}
			}
			return resultDTO;
		} else {
			return null;
		}
	}

	@Override
	public AgentDTO saveCargoToAgent(AgentDTO agentDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AgentDTO saveRoleToAgent(AgentDTO agentDTO) {
		agent = this.getAgentForName(agentDTO.getName());

		if (!agentDTO.getRoleToAgent().isEmpty()) {
			for (String roleName : agentDTO.getRoleToAgent()) {
				Role role = this.getRoleFromName(roleName);
				if (role != null) {
					AgentPlaysRole agentPlaysRole = new AgentPlaysRole(role,
							agent);
					if (!agent.getTheAgentPlaysRole().contains(agentPlaysRole)) {
						agent.getTheAgentPlaysRole().add(agentPlaysRole);
					}
				}
			}
		}
		agentDAO.update(agent);
		agent = null;

		return agentDTO;
	}

	@Override
	@SuppressWarnings("unchecked")
	public AgentHasAbilityDTO saveAbilityToAgent(
			AgentHasAbilityDTO agentHasAbilityDTO) {
		agent = this.getAgentForName(agentHasAbilityDTO.getTheAgent());
		if (agent == null) {
			return null;
		}

		String hql = "SELECT aha FROM "
				+ AGENT_HAS_ABILLITY_CLASSNAME
				+ " as aha WHERE aha.theAgent.name = :theAgent AND aha.theAbility.name = :theAbility";
		query = agentHasAbilityDAO.getPersistenceContext().createQuery(hql);
		query.setParameter("theAgent", agentHasAbilityDTO.getTheAgent());
		query.setParameter("theAbility", agentHasAbilityDTO.getTheAbility());

		List<AgentHasAbility> result = query.getResultList();

		if (!result.isEmpty()) {
			AgentHasAbility old = result.get(0);
			boolean newDegreeDifferentFromOld = !old.getDegree().equals(
					agentHasAbilityDTO.getDegree());

			if (newDegreeDifferentFromOld) {
				old.setDegree(agentHasAbilityDTO.getDegree());
				agentHasAbilityDAO.update(old);
			}
		} else {
			Ability ability = this.getAbilityFromName(agentHasAbilityDTO
					.getTheAbility());
			new AgentHasAbility(agentHasAbilityDTO.getDegree(), agent, ability);

			agentDAO.update(agent);
		}

		agent = null;

		return agentHasAbilityDTO;
	}

	@Override
	@SuppressWarnings("unchecked")
	public AgentHasAbilityDTO getAbilityToAgent(String abilityName) {
		String hql = "SELECT aha FROM " + AGENT_HAS_ABILLITY_CLASSNAME + " AS aha WHERE aha.theAbility.name = :abilityName";

		query = agentHasAbilityDAO.getPersistenceContext().createQuery(hql);
		query.setParameter("abilityName", abilityName);

		List<AgentHasAbility> result = query.getResultList();

		System.out.println("isEmpty? " + result.isEmpty());
		AgentHasAbilityDTO dto = null;

		if (!result.isEmpty()) {
			AgentHasAbility aha = result.get(0);

			dto = new AgentHasAbilityDTO(aha.getDegree(), aha.getTheAgent()
					.getName(), aha.getTheAbility().getName());
			dto.setDescriptionTheAbility(aha.getTheAbility().getDescription());

			return dto;
		} else {
			Ability ability = this.getAbilityFromName(abilityName);
			dto = new AgentHasAbilityDTO(0, null, ability.getName());
			dto.setDescriptionTheAbility(ability.getDescription());

			return dto;
		}
	}

	@Override
	public AgentAffinityAgentDTO saveAfinityToAgent(
			AgentAffinityAgentDTO agentAffinityAgentDTO) {

		agent = this.getAgentForName(agentAffinityAgentDTO.getFromAffinity());
		if (agent == null) {
			return null;
		}

		Agent affinityAgent = this.getAgentForName(agentAffinityAgentDTO
				.getToAffinity());

		AgentAffinityAgent agentAffinityAgent = new AgentAffinityAgent(
				agentAffinityAgentDTO.getDegree(), agent, affinityAgent);
		AgentAffinityAgent old = this.getAffinityByName(
				agentAffinityAgentDTO.getToAffinity(),
				agentAffinityAgentDTO.getFromAffinity());

		System.out.println("OLD?  = " + old);
		if (old != null) {
			boolean newDegreeDifferentFromOld = !old.getDegree().equals(
					agentAffinityAgent.getDegree());

			if (newDegreeDifferentFromOld) {
				old.setDegree(agentAffinityAgentDTO.getDegree());
				agentAffinityAgentDAO.update(old);

				AgentAffinityAgent inverseOld = this.getAffinityByName(
						agentAffinityAgentDTO.getFromAffinity(),
						agentAffinityAgentDTO.getToAffinity());

				inverseOld.setDegree(agentAffinityAgentDTO.getDegree());
				agentAffinityAgentDAO.update(inverseOld);
			}
		} else {
			new AgentAffinityAgent(agentAffinityAgentDTO.getDegree(),
					agentAffinityAgent.getFromAffinity(),
					agentAffinityAgent.getToAffinity());
			new AgentAffinityAgent(agentAffinityAgentDTO.getDegree(),
					agentAffinityAgent.getToAffinity(),
					agentAffinityAgent.getFromAffinity());

			agentDAO.update(agent);
		}

		agent = null;

		return agentAffinityAgentDTO;
	}
	
	@SuppressWarnings("unchecked")
	public AgentAffinityAgent getAffinityByName(String toAffinity,
			String fromAffinity) {
		String hql = "SELECT afa FROM "
				+ AGENT_AFFINITY_AGENT_CLASSNAME
				+ " as afa WHERE afa.toAffinity.name = :toAffinity AND afa.fromAffinity.name = :fromAffinity";
		query = agentAffinityAgentDAO.getPersistenceContext().createQuery(hql);
		query.setParameter("toAffinity", toAffinity);
		query.setParameter("fromAffinity", fromAffinity);

		List<AgentAffinityAgent> result = query.getResultList();
		if (!result.isEmpty()) {
			AgentAffinityAgent agentAffinityAgent = result.get(0);

			return agentAffinityAgent;
		}

		return null;
	}

	@Override
	public AgentDTO saveGroupToAgent(AgentDTO agentDTO) {
		agent = this.getAgentForName(agentDTO.getName());

		if (!agentDTO.getGroupToAgent().isEmpty()) {
			for (String groupName : agentDTO.getGroupToAgent()) {
				Group group = this.getGroupFromName(groupName);
				if (group != null) {

					if (!agent.getTheGroup().contains(group)
							&& !group.getTheAgent().contains(agent)) {
						agent.getTheGroup().add(group);
						group.getTheAgent().add(agent);
						groupDAO.update(group);
						agentDAO.update(agent);
					}
				}
			}
		}

		agentDAO.update(agent);
		agent = null;

		return agentDTO;
	}

	@Override
	@SuppressWarnings("unchecked")
	public AgentsDTO getAgents() {
		String hql;

		hql = "SELECT name FROM " + AGENT_CLASSNAME;
		query = agentDAO.getPersistenceContext().createQuery(hql);

		List<String> agents = new ArrayList<String>();
		agents = query.getResultList();
		String[] agentsArray = new String[agents.size()];
		agents.toArray(agentsArray);

		List<String> agent = new ArrayList<String>();

		List<RoleDTO> roleTo = getRolesToAgent();
		String[] rolesToAgent = null;

		if (!roleTo.isEmpty()) {
			rolesToAgent = new String[roleTo.size()];
			for (int i = 0; i < roleTo.size(); i++) {
				agent.add(roleTo.get(i).getName());
			}
			agent.toArray(rolesToAgent);
		}

		AgentsDTO agentsDTO = new AgentsDTO();

		hql = "SELECT agent FROM " + AGENT_CLASSNAME + " AS agent";
		query = agentDAO.getPersistenceContext().createQuery(hql);
		List<Agent> result = query.getResultList();
		List<String> idents = new ArrayList<String>();
		
		for (Agent agent2 : result) {
			agentsDTO.getAgents().add(convertAgentToAgentDTO(agent2, true));
			idents.add(agent2.getIdent());
		}
		
		agentsDTO.setNameAgents(agentsArray);
		agentsDTO.setCargoAgents(rolesToAgent);
		agentsDTO.setAgentIdents(idents);
		return agentsDTO;
	}

	@Override
	@SuppressWarnings("unchecked")
	public AgentsDTO getAgents(String agentName, String roleName,
			Boolean isActive) {
		String activeFilter = (isActive == null) ? ""
				: " and agent.isActive is :isActive";

		String hql = "SELECT agent FROM " + AGENT_CLASSNAME + " AS agent "
				+ "WHERE agent.name like :name" + activeFilter;
		query = agentDAO.getPersistenceContext().createQuery(hql);
		query.setParameter("name", "%" + agentName + "%");
		if (isActive != null) {
			query.setParameter("isActive", isActive);
		}
		List<Agent> result = query.getResultList();

		if (!result.isEmpty()) {

			hql = "SELECT agentPlaysRole FROM "
					+ AGENT_PLAY_ROLE_CLASSNAME
					+ " AS agentPlaysRole "
					+ "WHERE agentPlaysRole.theRole.name = :roleName";
			query = agentPlaysRoleDAO.getPersistenceContext().createQuery(hql);
			query.setParameter("roleName", roleName);

			List<AgentPlaysRole> agents = query.getResultList();

			if (!agents.isEmpty()) {

				List<String> agentNames = new ArrayList<String>();
				for (AgentPlaysRole agentPlaysRole : agents) {
					if (result.contains(agentPlaysRole.getTheAgent())) {
						agentNames.add(agentPlaysRole.getTheAgent().getName());
					}
				}

				String[] agentsArray = new String[agentNames.size()];
				agentNames.toArray(agentsArray);

				AgentsDTO agentsDTO = new AgentsDTO();
				agentsDTO.setNameAgents(agentsArray);
				agentsDTO.setCargoAgents(new String[] {});

				for (Agent agent : result) {
					agentsDTO.add(convertAgentToAgentDTO(agent, true));
				}
				
				return agentsDTO;
			}
		}

		return null;
	}

	@Override
	public Boolean removeAbilityAgent(AgentDTO agentDTO, String abilityName) {
		Ability ability = this.getAbilityFromName(abilityName);
		Agent agent = this.getAgentForName(agentDTO.getName());
		List<AgentHasAbility> agentHasAbilitiesToRemove = new ArrayList<AgentHasAbility>();

		for (AgentHasAbility agentHasAbility : agent.getTheAgentHasAbility()) {
			boolean equalAgents = agentHasAbility.getTheAgent().equals(agent);
			boolean equalAbilities = agentHasAbility.getTheAbility().getName()
					.equals(ability.getName());

			if (equalAgents && equalAbilities) {
				agentHasAbilitiesToRemove.add(agentHasAbility);
				// agentHasAbility.setTheAbility(null);
				// agentHasAbility.setTheAgent(null);
				agentHasAbilityDAO.delete(agentHasAbility);
			}
		}

		if (!agentHasAbilitiesToRemove.isEmpty()) {
			agent.getTheAgentHasAbility().removeAll(agentHasAbilitiesToRemove);
			agentDAO.update(agent);

			ability.getTheAgentHasAbility()
					.removeAll(agentHasAbilitiesToRemove);
			abilityDAO.update(ability);
		}

		return false;
	}

	@Override
	public Boolean removeCargoAgent(AgentDTO roleDTO) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Boolean removeRoleAgent(AgentDTO agentDTO, String roleName) {
		Role role = this.getRoleFromName(roleName);
		Agent agent = this.getAgentForName(agentDTO.getName());
		List<AgentPlaysRole> agentPlaysRolesToRemove = new ArrayList<AgentPlaysRole>();

		for (AgentPlaysRole agentPlaysRole : agent.getTheAgentPlaysRole()) {

			boolean equalAgents = agentPlaysRole.getTheAgent().equals(agent);
			boolean equalRoles = agentPlaysRole.getTheRole().equals(role);
			if (equalAgents && equalRoles) {
				agentPlaysRolesToRemove.add(agentPlaysRole);
				agentPlaysRole.setTheAgent(null);
				agentPlaysRole.setTheRole(null);
				agentPlaysRoleDAO.delete(agentPlaysRole);
			}

		}

		if (!agent.getTheAgentPlaysRole().isEmpty()) {
			agent.getTheAgentPlaysRole().removeAll(agentPlaysRolesToRemove);
			agentDAO.update(agent);

			return true;
		}

		return false;
	}

	@Override
	public Boolean removeAfinityAgent(AgentDTO agentDTO, String affinityName) {
		AgentAffinityAgent agentAffinityAgent = this.getAffinityByName(
				affinityName, agentDTO.getName());
		AgentAffinityAgent inverseAgentAffinityAgent = this.getAffinityByName(
				agentDTO.getName(), affinityName);
		Agent agent = this.getAgentForName(agentDTO.getName());

		if (agent.getFromAgentAffinity().contains(agentAffinityAgent)
				|| agent.getToAgentAffinity().contains(agentAffinityAgent)) {
			agentAffinityAgent.removeFromFromAffinity();
			agentAffinityAgent.removeFromToAffinity();
			agentAffinityAgentDAO.delete(agentAffinityAgent);
			agentAffinityAgentDAO.delete(inverseAgentAffinityAgent);

			return true;
		}

		return false;
	}

	@Override
	public Boolean removeGroupAgent(AgentDTO agentDTO, String groupName) {
		Group group = this.getGroupFromName(groupName);
		Agent agent = this.getAgentForName(agentDTO.getName());

		if (agent.getTheGroup().contains(group)) {
			agent.getTheGroup().remove(group);
			agentDAO.update(agent);
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	private Role getRoleFromName(String nome) {

		String hql = "SELECT role FROM " + ROLE_CLASSNAME
				+ " AS role WHERE role.name = :rolename";
		query = roleDAO.getPersistenceContext().createQuery(hql);
		query.setParameter("rolename", nome);

		List<Role> result = query.getResultList();

		if (!result.isEmpty()) {
			Role role = result.get(0);
			return role;
		}

		return null;
	}
	
	@Override
	public AgentsDTO getAgentsFromProject(Integer theProcessOid) {
		List<Agent> agentList = getAgentsForProject(theProcessOid);
		
		AgentsDTO list = convertAgentsToAgentsDTO(agentList, true);
		return list;
	}	
	
	@Override
	public AgentsDTO getAgentsFromProject(Integer theProcessOid, boolean lazy) {
		List<Agent> agentList = getAgentsForProject(theProcessOid);
		
		AgentsDTO list = convertAgentsToAgentsDTO(agentList, lazy);
		return list;
	}

	@SuppressWarnings("unchecked")
	private List<Agent> getAgentsForProject(Integer theProjectOid) {
		String hql_project = "SELECT DISTINCT agent FROM " + AGENT_CLASSNAME + " as agent, " +
						" "+ TASKAGENDA_CLASSNAME +" as taskagenda," + PROCESS_AGENDA_CLASSNAME + " as processagenda, " 
						+ PROJECT_CLASSNAME + " as project, " + Process.class.getSimpleName() + " as process " + 
		                     " WHERE taskagenda.oid = processagenda.theTaskAgenda.oid " +
		                     "AND processagenda.theProcess.oid = process.oid " +
		                     "AND process.oid = project.processRefered.oid " +
		                     "AND taskagenda.theAgent.oid = agent.oid " +
		                     "AND project.oid = :theProject";
		
		query = agentDAO.getPersistenceContext().createQuery(hql_project);
		query.setParameter("theProject", theProjectOid);
		List<Agent> agentList = new ArrayList<Agent>();
		agentList.addAll(query.getResultList());
		return agentList;
	}	
	
	@Override
	public List<TaskDTO> getAgentTasks(String agentIdent, String processIdent) {
		List<TaskDTO> tasksDTO = new ArrayList<TaskDTO>();
		String hql = "SELECT agenda.theTask FROM " + PROCESS_AGENDA_CLASSNAME + " AS agenda WHERE (agenda.theTaskAgenda.theAgent.ident = :agentID) AND (agenda.theProcess.ident = :processID)";

//		String hql = "SELECT task FROM " + TASK_CLASSNAME + " task WHERE task.theProcessAgenda.theProcess.ident = :processID";

		query = processAgendaDAO.getPersistenceContext().createQuery(hql);
		query.setParameter( "agentID", agentIdent );
		query.setParameter( "processID", processIdent );

		tasksDTO = convertTasksToTasksDTO(agentIdent, query.getResultList());

		return tasksDTO;
	}

	private List<TaskDTO> convertTasksToTasksDTO(String agentIdent, Collection<Task> tasks) {
		List<TaskDTO> tasksDTO = new ArrayList<TaskDTO>();

		for (Task task : tasks) {
			ProcessAgenda p = task.getTheProcessAgenda();
			String agentName = "";
			if(p != null) {
				agentName = p.getTheTaskAgenda().getTheAgent().getName();
			}
			
			float workingHours = taskDAO.getWorkingHoursForTask(task.getTheNormal().getIdent(), agentIdent);
			Time realWorkingTime = taskDAO.getWorkingHoursForTask2(task.getTheNormal().getIdent(), agentIdent);
			float estimatedTime = activityEstimationDAO.getHoursEstimationForActivity(task.getTheNormal().getIdent());
			Time estimatedTaskTime = new Time((int) estimatedTime, (int) ((estimatedTime * 60) % 60));
            
			TaskDTO t = new TaskDTO(task.getOid(), task.getTheNormal().getName(),
					task.getLocalState(), task.getBeginDate(),
					task.getEndDate(), workingHours,
					task.getDateDelegatedTo(), task.getDateDelegatedFrom(),
					task.getTheNormal().getHowLong(), task.getTheNormal().getHowLongUnit(),
					task.getTheNormal().getPlannedBegin(), task.getTheNormal().getPlannedEnd(),
					task.getTheNormal().getScript(), task.getTheNormal().getIdent(), agentName);

			t.setEstimatedTime(estimatedTaskTime);
			t.setRealWorkingTime(realWorkingTime);
			tasksDTO.add(t);
		}
		return tasksDTO;
	}

	private List<String> getAgentNames(List<Agent> agents) {
		List<String> names = new ArrayList<String>();
		for (Agent agent : agents) {
			names.add(agent.getName());
		}
		return names;
	}

	private Group getGroupFromName(String groupName) {
		String hql = "SELECT group FROM " + GROUP_CLASSNAME
				+ " as group where group.name = :name";
		query = groupDAO.getPersistenceContext().createQuery(hql);
		query.setParameter("name", groupName);

		Group result = null;
		if (!query.getResultList().isEmpty()) {
			result = (Group) query.getResultList().get(0);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public Ability getAbilityFromName(String name) {
		String hql = "select ability from " + ABILITY_CLASSNAME
				+ " as ability where ability.name = :abiname";
		query = abilityDAO.getPersistenceContext().createQuery(hql);
		query.setParameter("abiname", name);

		List<Ability> result = query.getResultList();

		if (!result.isEmpty()) {
			Ability ability = result.get(0);
			return ability;
		}

		return null;
	}
	
	private AgentsDTO convertAgentsToAgentsDTO(List<Agent> agentList, boolean lazy){
		List<AgentDTO> agente =  new ArrayList<AgentDTO>();
	
		for (Agent agent : agentList) {
			agente.add(this.convertAgentToAgentDTO(agent, lazy));
		}
		
		return new AgentsDTO(agente);
	}

	private Agent convertAgentDTOToAgent(AgentDTO agentDTO) {
		try {
			Agent agent = new Agent();
			agent = (Agent) converter.getEntity(agentDTO, agent);
			agent.setTheTaskAgenda(null);

			return agent;

		} catch (ImplementationException e) {
			e.printStackTrace();
		}

		return null;
	}

	private AgentDTO convertAgentToAgentDTO(Agent agent, boolean lazy) {
		AgentDTO agentDTO = new AgentDTO();
		try {
			agentDTO = (AgentDTO) converter.getDTO(agent, AgentDTO.class);
			agentDTO.setRoleToAgent(new ArrayList<String>());
			agentDTO.setRoleIdentsToAgent(new ArrayList<String>());
			if (!agent.getTheAgentPlaysRole().isEmpty()) {

				for (AgentPlaysRole agentPlayRole : agent
						.getTheAgentPlaysRole()) {
					agentDTO.getRoleToAgent().add(
							agentPlayRole.getTheRole().getName());
					agentDTO.getRoleIdentsToAgent().add(
							agentPlayRole.getTheRole().getIdent());
				}
			}
			
			if(!lazy) {
				boolean isCostHourPositive = agent.getCostHour()>0;
				Float custoDoTrabalho = 0.0f;
				Float custoEstimadoDoTrabalho = 0.0f;

				TaskAgenda theTaskAgenda = agent.getTheTaskAgenda();
				if((theTaskAgenda != null) && (theTaskAgenda.getTheProcessAgenda() != null)) {
					for (ProcessAgenda agenda : theTaskAgenda.getTheProcessAgenda()) {
						List<TaskDTO> agentTasks = this.convertTasksToTasksDTO(agent.getIdent(), agenda.getTheTask());
						agentDTO.getTasks().addAll(agentTasks);
						//custo
						for(TaskDTO task : agentDTO.getTasks()) {
							boolean isEstimatedHourPositive = task.getEstimatedTime().getHour()>0;
							boolean isWorkedHourPositive = task.getRealWorkingTime().getHour()>0;
							
							//TODO Falta colocar os MINUTOS
							if(isCostHourPositive && isEstimatedHourPositive) custoEstimadoDoTrabalho += agentDTO.getCostHour() * task.getEstimatedTime().getHour();
							if(isCostHourPositive && isWorkedHourPositive) custoDoTrabalho += agentDTO.getCostHour() * task.getRealWorkingTime().getHour();
						}
					}
				}
				
				agentDTO.setEstimatedWorkingCost(custoEstimadoDoTrabalho);
				agentDTO.setWorkingCost(custoDoTrabalho);
			}

			agentDTO.setGroupToAgent(new ArrayList<String>());
			for (Group group : agent.getTheGroup()) {
				agentDTO.getGroupToAgent().add(group.getName());
			}

			agentDTO.setAbilityToAgent(new ArrayList<String>());
			for (AgentHasAbility agentHasAbility : agent
					.getTheAgentHasAbility()) {
				String abilityName = agentHasAbility.getTheAbility().getName();

				agentDTO.getAbilityToAgent().add(abilityName);
			}

			agentDTO.setAfinityToAgent(new ArrayList<String>());
			for (AgentAffinityAgent agentAffinityAgent : agent
					.getFromAgentAffinity()) {
				String fromAffinityName = agentAffinityAgent.getFromAffinity()
						.getName();

				agentDTO.getAfinityToAgent().add(fromAffinityName);
			}

			return agentDTO;

		} catch (ImplementationException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public AgentDTO login(String email, String password) {
		password = Md5.getMd5Digest(password);
		System.out.println("caiu aqui no login"+email+password);
		query = agentDAO
				.getPersistenceContext()
				.createQuery(
						"SELECT agent FROM "
								+ AGENT_CLASSNAME
								+ " AS agent "
								+ "WHERE agent.eMail like :email and agent.password like :password");

		query.setParameter("email", email);
		query.setParameter("password", password);
	
		try {

			AgentDTO agent = convertAgentToAgentDTO(
					(Agent) query.getSingleResult(), true);

			return agent;

		} catch (NoResultException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public AgentDTO getUserForEmail(String email) {
		
		System.out.println("caiu aqui no envia email" + email);
		query = agentDAO.getPersistenceContext().createQuery(
				"SELECT agent FROM " + AGENT_CLASSNAME + " AS agent "
						+ "WHERE agent.eMail like :email");

		query.setParameter("email", email);

		try {
		
			
			AgentDTO agent = convertAgentToAgentDTO((Agent) query.getSingleResult(), true);
			
			return agent;

		} catch (NoResultException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean update(AgentDTO agente) {
		System.out.println(agente.getPassword());
								
		agentd = agente;
		
		agentDAO.update(convertAgentDTOToAgent(agentd));
		System.out.println(agentd.getPassword());
		if (agent != null)
			return true;
		else
			return false;

	}
	@Override
	public String criptografa(String senha){
		System.out.println("Criptografia" + Md5.getMd5Digest(senha));
		return Md5.getMd5Digest(senha);
	}

	@Override
	public AgentDTO getAgentFromEMail(String email) {
		query = agentDAO
				.getPersistenceContext()
				.createQuery(
						"SELECT agent FROM "
								+ AGENT_CLASSNAME
								+ " AS agent "
								+ "WHERE agent.eMail = :email");

		query.setParameter("email", email);
		try {
			AgentDTO agent = convertAgentToAgentDTO((Agent) query.getSingleResult(), true);
			
			return agent;
		} catch (NoResultException e) {
			e.printStackTrace();
			return null;
		}
	}
}

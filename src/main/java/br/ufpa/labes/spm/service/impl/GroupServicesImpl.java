package br.ufpa.labes.spm.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.qrconsult.spm.converter.core.Converter;
import org.qrconsult.spm.converter.core.ConverterImpl;
import org.qrconsult.spm.converter.exception.ImplementationException;
import br.ufpa.labes.spm.repository.interfaces.agent.IAgentDAO;
import br.ufpa.labes.spm.repository.interfaces.agent.IGroupDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IGroupTypeDAO;
import br.ufpa.labes.spm.repository.interfaces.types.ITypeDAO;
import org.qrconsult.spm.dtos.formAgent.AgentDTO;
import org.qrconsult.spm.dtos.formAgent.AgentsDTO;
import org.qrconsult.spm.dtos.formGroup.GroupDTO;
import org.qrconsult.spm.dtos.formGroup.GroupsDTO;
import org.qrconsult.spm.dtos.formTypes.TypesDTO;
import br.ufpa.labes.spm.domain.Agent;
import br.ufpa.labes.spm.domain.Group;
import br.ufpa.labes.spm.domain.ArtifactType;
import br.ufpa.labes.spm.domain.GroupType;
import br.ufpa.labes.spm.domain.Type;
import br.ufpa.labes.spm.service.interfaces.GroupServices;

public class GroupServicesImpl implements GroupServices {

	private static final String GROUP_CLASS_NAME = Group.class.getSimpleName();

	IGroupDAO groupDAO;

	IGroupTypeDAO groupTypeDAO;

	ITypeDAO typeDAO;

	IAgentDAO agenteDAO;

	Converter converter = new ConverterImpl();

	private Query query;

	@Override
	@SuppressWarnings("unchecked")
	public TypesDTO getGroupTypes() {
		String hql;
		List<Type> typesLists = new ArrayList<Type>();

		hql = "from " + GroupType.class.getSimpleName();
		query = typeDAO.getPersistenceContext().createQuery(hql);
		typesLists = query.getResultList();

		TypesDTO typesDTO = new TypesDTO(typesLists.size());
		int j = 0;
		for (Type type : typesLists) {
			String typeIdent = type.getIdent();
			String superTypeIdent = (type.getSuperType()!=null ? type.getSuperType().getIdent() : "");
			String rootType = ArtifactType.class.getSimpleName();
			typesDTO.addType(typeIdent, superTypeIdent, rootType, j);
			j++;
		}
		return typesDTO;
	}

	@Override
	@SuppressWarnings("unchecked")
	public GroupsDTO getGroups() {
		String hql = "SELECT group FROM " + GROUP_CLASS_NAME + " as group";
		query = groupDAO.getPersistenceContext().createQuery(hql);
		List<Group> result = query.getResultList();

		GroupsDTO groupsDTO = this.convertGroupsToGroupsDTO(result);
		for (Group group : result) {
			System.out.println(group.getName() + " - " + group.getIdent());
		}
		return groupsDTO;
	}

	@Override
	public GroupDTO saveGroup(GroupDTO groupDTO) {
		Group group = null;
		Group superGroup = this.retrieveGroup(groupDTO.getSuperGroup());
		GroupType theGroupType = (GroupType) typeDAO.retrieveBySecondaryKey(groupDTO.getTheGroupType());

//		group = this.retrieveGroup(groupDTO.getName());
		group = groupDAO.retrieveBySecondaryKey(groupDTO.getIdent());
		if(group != null) {
			group.setIsActive(groupDTO.isIsActive());
			group.setDescription(groupDTO.getDescription());

		} else {
			group = this.convertGroupDTOToGroup(groupDTO);
			groupDAO.save(group);

			String newIdent = groupDAO.generateIdent(group.getName(), group);
			group.setIdent(newIdent);
			groupDTO.setIdent(newIdent);
		}

		group.setSuperGroup(superGroup);
		group.setTheGroupType(theGroupType);

		updateDependencies(groupDTO, group);

		groupDAO.update(group);

		return groupDTO;
	}

	@Override
	public Boolean removeGroup(String groupName) {
		Group group = this.retrieveGroup(groupName);
		if(group != null) {
			for(Agent agent : group.getTheAgent()) {
				agent.getTheGroup().remove(group);
			}
			group.getTheAgent().clear();

			groupDAO.update(group);

			groupDAO.delete(group);
			return true;
		}

		return false;
	}

	private void updateDependencies(GroupDTO groupDTO, Group group) {
		if(!groupDTO.getAgents().isEmpty()) {
			for (String agentName : groupDTO.getAgents()) {
				Agent agent = getAgentFromDatabase(agentName);

				if(!agent.getTheGroup().contains(group) && !group.getTheAgent().contains(agent)) {
					agent.getTheGroup().add(group);
					group.getTheAgent().add(agent);
				}
			}
		}
	}

	@Override
	public GroupDTO getGroup(String groupIdent) {
//		Group result = this.retrieveGroup(groupIdent);
		Group result = groupDAO.retrieveBySecondaryKey(groupIdent);
		GroupDTO groupDTO = null;

		if(result != null) {
			groupDTO = this.convertGroupToGroupDTO(result);
		}

		return groupDTO;
	}

	@Override
	@SuppressWarnings("unchecked")
	public GroupsDTO getGroups(String searchTerm, String typeFilter, Boolean ativoFilter) {
		String activeFilter = (ativoFilter == null) ? "" : " and group.isActive is :orgFilter" ;

		String hql;
		if(typeFilter != null) {
			hql = "SELECT group FROM " + GROUP_CLASS_NAME + " AS group " +
					"WHERE group.name like :name and group.theGroupType.ident = :type" + activeFilter;
			query = groupDAO.getPersistenceContext().createQuery(hql);
			query.setParameter("type", typeFilter);
		} else {
			hql = "SELECT group FROM " + GROUP_CLASS_NAME + " AS group " +
					"WHERE group.name like :name" + activeFilter;
			query = groupDAO.getPersistenceContext().createQuery(hql);
		}
		query.setParameter("name", "%"+ searchTerm + "%");

		if(!activeFilter.isEmpty()) {
			query.setParameter("orgFilter", ativoFilter);
		}

		List<Group> resultado = query.getResultList();
		GroupsDTO groupsDTO = new GroupsDTO(new ArrayList<GroupDTO>());
		groupsDTO = this.convertGroupsToGroupsDTO(resultado);

		return groupsDTO;
	}

	@Override
	@SuppressWarnings("unchecked")
	public AgentsDTO getAgents() {
		String hql = "SELECT agent.name FROM " + Agent.class.getSimpleName() + " AS agent";
		query = agenteDAO.getPersistenceContext().createQuery(hql);

		List<String> resultado = query.getResultList();
		String[] list = new String[resultado.size()];
		resultado.toArray(list);

		AgentsDTO agentsDTO = new AgentsDTO();
		agentsDTO.setNameAgents(list);

		return agentsDTO;
	}

	@Override
	@SuppressWarnings("unchecked")
	public AgentDTO getAgent(String agentName) {
		String hql = "SELECT agent FROM " + Agent.class.getSimpleName() + " AS agent WHERE agent.name = :name";
		query.setParameter("name", agentName);
		query = agenteDAO.getPersistenceContext().createQuery(hql);

		List<Agent> agents = query.getResultList();
		Agent agent = agents.get(0);

		AgentDTO agentDTO = new AgentDTO();
		try {
			converter.getDTO(agent, AgentDTO.class);
		} catch (ImplementationException e) {
			e.printStackTrace();
		}

		return agentDTO;
	}

	@Override
	public Boolean removeAgent(String groupName, String agentName) {
		System.out.println(groupName + " ---------- " + agentName);

		Group group = this.retrieveGroup(groupName);
		Agent agent = this.getAgentFromDatabase(agentName);

		System.out.println("group: " + group + ", agent: " + agent);

		if((group != null) && (agent != null)) {
			group.getTheAgent().remove(agent);
			agent.getTheGroup().remove(group);

			this.agenteDAO.update(agent);
			this.groupDAO.update(group);

			return true;
		}

		return false;
	}

	private Agent getAgentFromDatabase(String agentName) {
		String hql = "SELECT agent FROM " + Agent.class.getSimpleName() + " AS agent WHERE agent.name = :name";
		query = agenteDAO.getPersistenceContext().createQuery(hql);
		query.setParameter("name", agentName);

		List<Agent> agents = query.getResultList();
		Agent agent = agents.get(0);

		return agent;
	}

	public Group retrieveGroup(String groupName) {
		String hql = "SELECT group FROM " + GROUP_CLASS_NAME + " as group where group.name = :name";
		query = groupDAO.getPersistenceContext().createQuery(hql);
		query.setParameter("name", groupName);

		Group result = null;
		if(!query.getResultList().isEmpty()) {
			result = (Group) query.getResultList().get(0);
		}

		return result;
	}

	private GroupDTO convertGroupToGroupDTO(Group group) {
		try {

			GroupDTO groupDTO = new GroupDTO();
			groupDTO = (GroupDTO) this.converter.getDTO(group, GroupDTO.class);
			String superGroup = (group.getSuperGroup() != null) ? group.getSuperGroup().getName() : "";
			String theGroupType = (group.getTheGroupType() != null) ? group.getTheGroupType().getIdent() : "";
			groupDTO.setSuperGroup(superGroup);
			groupDTO.setTheGroupType(theGroupType);

			groupDTO.setAgents(new ArrayList<String>());
			for (Agent agent : group.getTheAgent()) {
				groupDTO.getAgents().add(agent.getName());
			}

			return groupDTO;

		} catch (ImplementationException e) {
			e.printStackTrace();
		}

		return null;
	}

	private Group convertGroupDTOToGroup(GroupDTO groupDTO) {
		try {

			Group group = new Group();
			group = (Group) this.converter.getEntity(groupDTO, group);
			return group;

		} catch (ImplementationException e) {
			e.printStackTrace();
		}

		return null;
	}

	private GroupsDTO convertGroupsToGroupsDTO(List<Group> groups) {
		GroupsDTO groupsDTO = new GroupsDTO(new ArrayList<GroupDTO>());
		for (Group group : groups) {
			GroupDTO groupDTO = this.convertGroupToGroupDTO(group);
			groupsDTO.addGroupDTO(groupDTO);
		}

		return groupsDTO;
	}

	private List<String> getGroupSubordinados(GroupsDTO groupsDTO, GroupDTO groupDTO) {
		List<String> subordinados = new ArrayList<String>();

		for (int j = 0; j < groupsDTO.size(); j++) {
			GroupDTO subordinadoGroupDTO = groupsDTO.getGroupDTO(j);
			boolean isSubordinadoAoGrupo = (subordinadoGroupDTO.getSuperGroup() != null)
					? subordinadoGroupDTO.getSuperGroup().equals(groupDTO.getName()) : false;
			System.out.println(groupDTO.getName() + " <----> " + groupDTO.getSuperGroup() + " ==> " + isSubordinadoAoGrupo);
			if(isSubordinadoAoGrupo) {
				subordinados.add(subordinadoGroupDTO.getName());
			}

		}
		return subordinados;
	}
}

package br.ufpa.labes.spm.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import br.ufpa.labes.spm.converter.Converter;
import br.ufpa.labes.spm.converter.ConverterImpl;
import br.ufpa.labes.spm.exceptions.ImplementationException;
import br.ufpa.labes.spm.repository.interfaces.agent.IAgentDAO;
import br.ufpa.labes.spm.repository.interfaces.agent.IWorkGroupDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IWorkGroupTypeDAO;
import br.ufpa.labes.spm.repository.interfaces.types.ITypeDAO;
import br.ufpa.labes.spm.service.dto.AgentDTO;
import br.ufpa.labes.spm.service.dto.AgentsDTO;
import br.ufpa.labes.spm.service.dto.GroupDTO;
import br.ufpa.labes.spm.service.dto.GroupsDTO;
import br.ufpa.labes.spm.service.dto.TypesDTO;
import br.ufpa.labes.spm.domain.Agent;
import br.ufpa.labes.spm.domain.WorkGroup;
import br.ufpa.labes.spm.domain.ArtifactType;
import br.ufpa.labes.spm.domain.WorkGroupType;
import br.ufpa.labes.spm.domain.Type;
import br.ufpa.labes.spm.service.interfaces.WorkGroupServices;

public class GroupServicesImpl implements WorkGroupServices {

  private static final String GROUP_CLASS_NAME = WorkGroup.class.getSimpleName();

  IWorkGroupDAO groupDAO;

  IWorkGroupTypeDAO groupTypeDAO;

  ITypeDAO typeDAO;

  IAgentDAO agenteDAO;

  Converter converter = new ConverterImpl();

  private Query query;

  @Override
  @SuppressWarnings("unchecked")
  public TypesDTO getWorkGroupTypes() {
    String hql;
    List<Type> typesLists = new ArrayList<Type>();

    hql = "from " + WorkGroupType.class.getSimpleName();
    query = typeDAO.getPersistenceContext().createQuery(hql);
    typesLists = query.getResultList();

    TypesDTO typesDTO = new TypesDTO(typesLists.size());
    int j = 0;
    for (Type type : typesLists) {
      String typeIdent = type.getIdent();
      String superTypeIdent = (type.getSuperType() != null ? type.getSuperType().getIdent() : "");
      String rootType = ArtifactType.class.getSimpleName();
      typesDTO.addType(typeIdent, superTypeIdent, rootType, j);
      j++;
    }
    return typesDTO;
  }

  @Override
  @SuppressWarnings("unchecked")
  public GroupsDTO getWorkGroups() {
    String hql = "SELECT group FROM " + GROUP_CLASS_NAME + " as group";
    query = groupDAO.getPersistenceContext().createQuery(hql);
    List<WorkGroup> result = query.getResultList();

    GroupsDTO groupsDTO = this.convertGroupsToGroupsDTO(result);
    for (WorkGroup group : result) {
      System.out.println(group.getName() + " - " + group.getIdent());
    }
    return groupsDTO;
  }

  @Override
  public GroupDTO saveWorkGroup(GroupDTO groupDTO) {
    WorkGroup group = null;
    WorkGroup superGroup = this.retrieveGroup(groupDTO.getSuperGroup());
    WorkGroupType theGroupType =
        (WorkGroupType) typeDAO.retrieveBySecondaryKey(groupDTO.getTheGroupType());

    //		group = this.retrieveGroup(groupDTO.getName());
    group = groupDAO.retrieveBySecondaryKey(groupDTO.getIdent());
    if (group != null) {
      group.setIsActive(groupDTO.isIsActive());
      group.setDescription(groupDTO.getDescription());

    } else {
      group = this.convertGroupDTOToGroup(groupDTO);
      groupDAO.daoSave(group);

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
  public Boolean removeWorkGroup(String groupName) {
    WorkGroup group = this.retrieveGroup(groupName);
    if (group != null) {
      for (Agent agent : group.getTheAgents()) {
        agent.getTheWorkGroups().remove(group);
      }
      group.getTheAgents().clear();

      groupDAO.update(group);

      groupDAO.daoDelete(group);
      return true;
    }

    return false;
  }

  private void updateDependencies(GroupDTO groupDTO, WorkGroup group) {
    if (!groupDTO.getAgents().isEmpty()) {
      for (String agentName : groupDTO.getAgents()) {
        Agent agent = getAgentFromDatabase(agentName);

        if (!agent.getTheWorkGroups().contains(group) && !group.getTheAgents().contains(agent)) {
          agent.getTheWorkGroups().add(group);
          group.getTheAgents().add(agent);
        }
      }
    }
  }

  @Override
  public GroupDTO getWorkGroup(String groupIdent) {
    //		WorkGroup result = this.retrieveGroup(groupIdent);
    WorkGroup result = groupDAO.retrieveBySecondaryKey(groupIdent);
    GroupDTO groupDTO = null;

    if (result != null) {
      groupDTO = this.convertGroupToGroupDTO(result);
    }

    return groupDTO;
  }

  @Override
  @SuppressWarnings("unchecked")
  public GroupsDTO getWorkGroups(String searchTerm, String typeFilter, Boolean ativoFilter) {
    String activeFilter = (ativoFilter == null) ? "" : " and group.isActive is :orgFilter";

    String hql;
    if (typeFilter != null) {
      hql =
          "SELECT group FROM "
              + GROUP_CLASS_NAME
              + " AS group "
              + "WHERE group.name like :name and group.theGroupType.ident = :type"
              + activeFilter;
      query = groupDAO.getPersistenceContext().createQuery(hql);
      query.setParameter("type", typeFilter);
    } else {
      hql =
          "SELECT group FROM "
              + GROUP_CLASS_NAME
              + " AS group "
              + "WHERE group.name like :name"
              + activeFilter;
      query = groupDAO.getPersistenceContext().createQuery(hql);
    }
    query.setParameter("name", "%" + searchTerm + "%");

    if (!activeFilter.isEmpty()) {
      query.setParameter("orgFilter", ativoFilter);
    }

    List<WorkGroup> resultado = query.getResultList();
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
    String hql =
        "SELECT agent FROM " + Agent.class.getSimpleName() + " AS agent WHERE agent.name = :name";
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

    WorkGroup group = this.retrieveGroup(groupName);
    Agent agent = this.getAgentFromDatabase(agentName);

    System.out.println("group: " + group + ", agent: " + agent);

    if ((group != null) && (agent != null)) {
      group.getTheAgents().remove(agent);
      agent.getTheWorkGroups().remove(group);

      this.agenteDAO.update(agent);
      this.groupDAO.update(group);

      return true;
    }

    return false;
  }

  private Agent getAgentFromDatabase(String agentName) {
    String hql =
        "SELECT agent FROM " + Agent.class.getSimpleName() + " AS agent WHERE agent.name = :name";
    query = agenteDAO.getPersistenceContext().createQuery(hql);
    query.setParameter("name", agentName);

    List<Agent> agents = query.getResultList();
    Agent agent = agents.get(0);

    return agent;
  }

  public WorkGroup retrieveGroup(String groupName) {
    String hql = "SELECT group FROM " + GROUP_CLASS_NAME + " as group where group.name = :name";
    query = groupDAO.getPersistenceContext().createQuery(hql);
    query.setParameter("name", groupName);

    WorkGroup result = null;
    if (!query.getResultList().isEmpty()) {
      result = (WorkGroup) query.getResultList().get(0);
    }

    return result;
  }

  private GroupDTO convertGroupToGroupDTO(WorkGroup group) {
    try {

      GroupDTO groupDTO = new GroupDTO();
      groupDTO = (GroupDTO) this.converter.getDTO(group, GroupDTO.class);
      String superGroup = (group.getSuperGroup() != null) ? group.getSuperGroup().getName() : "";
      String theGroupType =
          (group.getTheGroupType() != null) ? group.getTheGroupType().getIdent() : "";
      groupDTO.setSuperGroup(superGroup);
      groupDTO.setTheGroupType(theGroupType);

      groupDTO.setAgents(new ArrayList<String>());
      for (Agent agent : group.getTheAgents()) {
        groupDTO.getAgents().add(agent.getName());
      }

      return groupDTO;

    } catch (ImplementationException e) {
      e.printStackTrace();
    }

    return null;
  }

  private WorkGroup convertGroupDTOToGroup(GroupDTO groupDTO) {
    try {

      WorkGroup group = new WorkGroup();
      group = (WorkGroup) this.converter.getEntity(groupDTO, group);
      return group;

    } catch (ImplementationException e) {
      e.printStackTrace();
    }

    return null;
  }

  private GroupsDTO convertGroupsToGroupsDTO(List<WorkGroup> groups) {
    GroupsDTO groupsDTO = new GroupsDTO(new ArrayList<GroupDTO>());
    for (WorkGroup group : groups) {
      GroupDTO groupDTO = this.convertGroupToGroupDTO(group);
      groupsDTO.addGroupDTO(groupDTO);
    }

    return groupsDTO;
  }

  private List<String> getGroupSubordinados(GroupsDTO groupsDTO, GroupDTO groupDTO) {
    List<String> subordinados = new ArrayList<String>();

    for (int j = 0; j < groupsDTO.size(); j++) {
      GroupDTO subordinadoGroupDTO = groupsDTO.getGroupDTO(j);
      boolean isSubordinadoAoGrupo =
          (subordinadoGroupDTO.getSuperGroup() != null)
              ? subordinadoGroupDTO.getSuperGroup().equals(groupDTO.getName())
              : false;
      System.out.println(
          groupDTO.getName()
              + " <----> "
              + groupDTO.getSuperGroup()
              + " ==> "
              + isSubordinadoAoGrupo);
      if (isSubordinadoAoGrupo) {
        subordinados.add(subordinadoGroupDTO.getName());
      }
    }
    return subordinados;
  }
}

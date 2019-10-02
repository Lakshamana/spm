package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.AgentDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link Agent} and its DTO {@link AgentDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {
      TaskAgendaMapper.class,
      SpmConfigurationMapper.class,
      ResourceEventMapper.class,
      ProcessMapper.class,
      WorkGroupMapper.class,
      CompanyUnitMapper.class,
      EmailConfigurationMapper.class
    })
public interface AgentMapper extends EntityMapper<AgentDTO, Agent> {

  @Mapping(source = "theTaskAgenda.id", target = "theTaskAgendaId")
  @Mapping(source = "configuration.id", target = "configurationId")
  @Mapping(source = "theResourceEvent.id", target = "theResourceEventId")
  @Mapping(source = "theEmailConfiguration.id", target = "theEmailConfigurationId")
  AgentDTO toDto(Agent agent);

  @Mapping(source = "theTaskAgendaId", target = "theTaskAgenda")
  @Mapping(source = "configurationId", target = "configuration")
  @Mapping(target = "delegates", ignore = true)
  @Mapping(target = "removeDelegates", ignore = true)
  @Mapping(target = "isDelegatedFors", ignore = true)
  @Mapping(target = "removeIsDelegatedFor", ignore = true)
  @Mapping(target = "theModelingActivityEvents", ignore = true)
  @Mapping(target = "removeTheModelingActivityEvent", ignore = true)
  @Mapping(target = "theReqAgents", ignore = true)
  @Mapping(target = "removeTheReqAgent", ignore = true)
  @Mapping(target = "theAgentMetrics", ignore = true)
  @Mapping(target = "removeTheAgentMetric", ignore = true)
  @Mapping(target = "theAgentEstimations", ignore = true)
  @Mapping(target = "removeTheAgentEstimation", ignore = true)
  @Mapping(target = "theManagedOrgUnits", ignore = true)
  @Mapping(target = "removeTheManagedOrgUnits", ignore = true)
  @Mapping(source = "theResourceEventId", target = "theResourceEvent")
  @Mapping(target = "removeTheProcess", ignore = true)
  @Mapping(target = "removeTheWorkGroup", ignore = true)
  @Mapping(target = "removeTheOrgUnits", ignore = true)
  @Mapping(target = "theChatMessage", ignore = true)
  @Mapping(source = "theEmailConfigurationId", target = "theEmailConfiguration")
  @Mapping(target = "fromAgentAffinities", ignore = true)
  @Mapping(target = "removeFromAgentAffinity", ignore = true)
  @Mapping(target = "toAgentAffinities", ignore = true)
  @Mapping(target = "removeToAgentAffinity", ignore = true)
  @Mapping(target = "theAgentHasAbilities", ignore = true)
  @Mapping(target = "removeTheAgentHasAbility", ignore = true)
  @Mapping(target = "theAgentPlaysRoles", ignore = true)
  @Mapping(target = "removeTheAgentPlaysRole", ignore = true)
  @Mapping(target = "theOutOfWorkPeriods", ignore = true)
  @Mapping(target = "removeTheOutOfWorkPeriod", ignore = true)
  @Mapping(target = "theAgentInstSugs", ignore = true)
  @Mapping(target = "removeTheAgentInstSug", ignore = true)
  @Mapping(target = "theAgentInstSugToAgents", ignore = true)
  @Mapping(target = "removeTheAgentInstSugToAgent", ignore = true)
  @Mapping(target = "theAgentWorkingLoads", ignore = true)
  @Mapping(target = "removeTheAgentWorkingLoad", ignore = true)
  @Mapping(target = "theChatLogs", ignore = true)
  @Mapping(target = "removeTheChatLog", ignore = true)
  Agent toEntity(AgentDTO agentDTO);

  default Agent fromId(Long id) {
    if (id == null) {
      return null;
    }
    Agent agent = new Agent();
    agent.setId(id);
    return agent;
  }
}

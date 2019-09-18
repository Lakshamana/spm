package br.ufpa.labes.spm.service.interfaces;

import java.util.List;


import br.ufpa.labes.spm.service.dto.TaskDTO;
import br.ufpa.labes.spm.service.dto.AbilityDTO;
import br.ufpa.labes.spm.service.dto.AgentAffinityAgentDTO;
import br.ufpa.labes.spm.service.dto.AgentDTO;
import br.ufpa.labes.spm.service.dto.AgentHasAbilityDTO;
import br.ufpa.labes.spm.service.dto.AgentsDTO;
import br.ufpa.labes.spm.service.dto.ConfigurationDTO;
import br.ufpa.labes.spm.service.dto.GroupDTO;
import br.ufpa.labes.spm.service.dto.RoleDTO;
import br.ufpa.labes.spm.domain.Agent;

@Remote
public interface AgentServices {
	public AgentDTO getAgent(String nameAgent);

	public AgentDTO saveAgent(AgentDTO agentDTO,ConfigurationDTO config);

	public Boolean removeAgent(String nameAgent);

	public AgentsDTO getRoles(String termoBusca, String domainFilter);

	//public String[] getRoleTypes();

	public List<AbilityDTO> getAbilitysToAgent();

	public List<RoleDTO>  getRolesToAgent();

	public List<AgentDTO> getAfinityToAgent();

	public List<GroupDTO> getGroupToAgent();

	public AgentDTO saveCargoToAgent(AgentDTO agentDTO);

	public AgentDTO saveRoleToAgent(AgentDTO agentDTO);

	public AgentHasAbilityDTO saveAbilityToAgent(AgentHasAbilityDTO agentHasAbilityDTO);

	public AgentHasAbilityDTO getAbilityToAgent(String abilityName);

	public AgentAffinityAgentDTO saveAfinityToAgent(AgentAffinityAgentDTO agentAffinityAgentDTO);

	public AgentAffinityAgentDTO getAffinityAgent(String toAffinity, String fromAffinity);

	public AgentDTO saveGroupToAgent(AgentDTO agentDTO);

	public AgentsDTO getAgents();

	public AgentsDTO getAgents(String agentName, String roleName, Boolean isActive);

	//public List<AgentDTO> getAgentToRole();

	public Boolean removeAbilityAgent(AgentDTO agentDTO, String abilityName);

	public Boolean removeCargoAgent(AgentDTO roleDTO);

	public Boolean removeRoleAgent(AgentDTO agentDTO, String roleName);

	public Boolean removeAfinityAgent(AgentDTO agentDTO, String affinityName);

	public Boolean removeGroupAgent(AgentDTO agentDTO, String groupName);

	public List<TaskDTO> getAgentTasks(String agentIdent, String processIdent);

	public AgentDTO login(String login,String senha);

	public boolean update(AgentDTO agent);

	public String criptografa(String senha);

	public Agent getPerfilAgentePadrao(String ident);

	public AgentsDTO getAgentsFromProject(Integer theProcessOid);

	public AgentsDTO getAgentsFromProject(Integer theProcessOid, boolean lazy);

	public AgentDTO getAgentFromEMail(String email);

	public AgentDTO getUserForEmail(String email);

	public Boolean updateSenha(AgentDTO agentDTO);
}

package br.ufpa.labes.spm.service.interfaces;


import br.ufpa.labes.spm.service.dto.AgentDTO;
import br.ufpa.labes.spm.service.dto.AgentsDTO;
import br.ufpa.labes.spm.service.dto.GroupDTO;
import br.ufpa.labes.spm.service.dto.GroupsDTO;
import br.ufpa.labes.spm.service.dto.WorkGroupDTO;
import br.ufpa.labes.spm.service.dto.TypesDTO;

public interface WorkGroupServices {

	public TypesDTO getWorkGroupTypes();

	public GroupDTO saveWorkGroup(GroupDTO groupDTO);

	public GroupsDTO getWorkGroups();

	public GroupDTO getWorkGroup(String WorkGroupName);

	public GroupsDTO getWorkGroups(String searchTerm, String typeFilter, Boolean ativoFilter);

	public AgentsDTO getAgents();

	public AgentDTO getAgent(String agentName);

	public Boolean removeAgent(String WorkGroupName, String agentName);

	public Boolean removeWorkGroup(String WorkGroupName);
}
